package com.eltech.sh.service;

import com.eltech.sh.configuration.VkCredentialsConfiguration;
import com.eltech.sh.model.Person;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vk.api.sdk.actions.Friends;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
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
    private final Friends friends;
    private final ObjectMapper objectMapper;

    @Autowired
    public VKServiceImpl(VkCredentialsConfiguration configuration,
                         VkApiClient vkApiClient,
                         Friends friends,
                         ObjectMapper objectMapper,
                         HttpSession session) {
        this.configuration = configuration;
        this.vkApiClient = vkApiClient;
        this.friends = friends;
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
                        .userIds(id).unsafeParam("fields", "photo_400_orig").execute().get(0);
                System.out.println(user);
                return new Person(user.getId(), user.getFirstName(), user.getLastName(), user.getPhoto400Orig());
            } catch (ApiTooManyException e) {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e1) {
                    // e1.printStackTrace();
                }
            } catch (ApiException | ClientException e) {
                System.out.println("Reset request [getPersonByStringId]");
            }
        }
    }

    @Override
    public List<Integer> findIdsOfPersonFriends(Integer id) {
        try {
            String response = friends.get(getUserActor()).listId(id)
                    .unsafeParam("user_id", id)
                    .executeAsRaw().getContent();
            JsonNode jsonNode = objectMapper.readTree(response).path("response").path("items");
            return objectMapper.convertValue(jsonNode, new TypeReference<List<Integer>>() {
            });
        } catch (ClientException | IOException e) {
            //  e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getUserImgUrl(Integer userId) {
        while (true) {
            try {
                List<UserXtrCounters> list = vkApiClient.users().get(getUserActor()).unsafeParam("user_id", userId).unsafeParam("fields", "photo_400_orig").execute();
                return list.get(0).getPhoto400Orig();
            } catch (ApiTooManyException e) {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } catch (ApiException | ClientException e) {
                System.out.println("Reset request [getUserImgUrl]");
            }
        }
    }

    @Override
    public List<Person> getPersonsByIds(List<Integer> ids) {
        List<String> formattedIds = ids.stream().map(Object::toString).collect(Collectors.toList());
        try {
            String response = vkApiClient.users().get(getUserActor()).userIds(formattedIds).unsafeParam("fields", "photo_400_orig").executeAsRaw().getContent();

            JsonNode jsonNode = objectMapper.readTree(response).path("response");
            return objectMapper.convertValue(jsonNode, new TypeReference<List<Person>>() {
            });
        } catch (ClientException | IOException e) {
            // e.printStackTrace();
        }

        return null;
    }

    @Override
    public UserAuthResponse getAuthInfo(String code) {

        UserAuthResponse authResponse = null;
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
