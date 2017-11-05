package com.eltech.sh.service;

import com.eltech.sh.configuration.VkCredentialsConfiguration;
import com.eltech.sh.model.Person;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ApiParamUserIdException;
import com.vk.api.sdk.exceptions.ApiTooManyException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VKServiceImpl implements VKService{

    private final HttpSession session;
    private final VkCredentialsConfiguration configuration;
    private final VkApiClient vkApiClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public VKServiceImpl(VkCredentialsConfiguration configuration,
                         VkApiClient vkApiClient,
                         ObjectMapper objectMapper,
                         HttpSession session) {
        this.configuration = configuration;
        this.vkApiClient = vkApiClient;
        this.objectMapper = objectMapper;
        this.session = session;
    }

    private UserActor getUserActor() {
        return new UserActor(configuration.getAppId(), (String) session.getAttribute("access_token"));
    }

    @Override
    public Person getPersonByStringId(String id) {

        while (true) {
            try {
                UserXtrCounters user = vkApiClient.users().get(getUserActor())
                        .userIds(id)
                        .unsafeParam("fields", "photo_400_orig")
                        .execute()
                        .get(0);
                System.out.println(user);
                return new Person(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getPhoto400Orig());
            } catch (ApiTooManyException e) {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e1) {
                }
            }catch (ApiParamUserIdException e){
                return null;
            } catch (ApiException | ClientException e) {
                System.out.println("Reset request [getPersonByStringId]");
            }
        }
    }

    @Override
    public List<Person> getPersonsByIds(List<Integer> ids) {
        List<String> formattedIds = ids.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
        try {
            String response = vkApiClient.users()
                    .get(getUserActor())
                    .userIds(formattedIds)
                    .unsafeParam("fields", "photo_400_orig")
                    .executeAsRaw()
                    .getContent();

            JsonNode jsonNode = objectMapper.readTree(response).path("response");
            return objectMapper.convertValue(jsonNode, new TypeReference<List<Person>>() {
            });
        } catch (ClientException | IOException e) {        }

        return null;
    }

    @Override
    public UserAuthResponse getAuthInfo(String code) {
        UserAuthResponse authResponse;
        while (true) {
            try {
                authResponse = vkApiClient.oauth()
                        .userAuthorizationCodeFlow(
                                configuration.getAppId(),
                                configuration.getClientSecret(),
                                configuration.getRedirectUri(),
                                code)
                        .execute();
                return authResponse;
            } catch (ApiTooManyException e) {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } catch (ApiException | ClientException e) {
                System.out.println("Reset request [getAuthInfo]");
            }
        }
    }

    @Override
    public Integer getPersonIntegerIdByStringId(String userId) {
        return getPersonByStringId(userId).getVkId();
    }

    @Override
    public Map<Integer, List<Integer>> findFriendsForGivenPeople(List<Integer> userIds) {
        String ids = StringUtils.join(userIds, ", ");
        try {
            String vkScriptCode = readFileCode("src\\main\\vkScript\\getFriendsByIds");
            String response = vkApiClient.execute()
                    .code(
                            getUserActor(),
                            String.format("var ids = \"%s\";%s", ids, vkScriptCode)
                    )
                    .executeAsRaw().getContent();

            JsonNode responseNode = objectMapper.readTree(response).path("response");

            Map<Integer, List<Integer>> resultMap = new HashMap<>();

            int i = 0;
            for (JsonNode node : responseNode) {
                if (node.size() > 0) {
                    List<Integer> userFriendsIds = objectMapper.convertValue(
                            node.findValue("items"),
                            new TypeReference<List<Integer>>() {
                            }
                    );

                    resultMap.put(userIds.get(i), userFriendsIds);
                    i++;
                }
            }
            return resultMap;
        } catch (ClientException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readFileCode(String path) {
        StringBuilder code = new StringBuilder();
        File file = new File(path);

        try (BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()))) {
            String s;
            while ((s = in.readLine()) != null) {
                code.append(s);
                code.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code.toString();
    }
}
