package com.eltech.sh.service;

import com.eltech.sh.configuration.VkCredentialsConfiguration;
import com.eltech.sh.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.sun.javafx.scene.control.SizeLimitedList;
import com.vk.api.sdk.actions.Friends;
import com.vk.api.sdk.client.ClientResponse;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ApiTooManyException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VKService {

    private final HttpSession session;
    private final VkCredentialsConfiguration configuration;
    private final VkApiClient vkApiClient;
    private final Friends friends;
    private final ObjectMapper objectMapper;

    @Autowired
    public VKService(VkCredentialsConfiguration configuration,
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

    public Integer getPersonIntegerIdByStringId(String userId) {
        return getPersonByStringId(userId).getVkId();
    }

    public String readFileCode(String path)
    {
        StringBuilder code = new StringBuilder();
        File file = new File(path);

        try {
            BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
            try {
                String s;
                while ((s = in.readLine()) != null) {
                    code.append(s);
                    code.append("\n");
                }
            } finally {
                in.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
        return code.toString();
    }

    public List<Integer> getFriendsByIds(List<Integer> friendIds)
    {
        String ids = new String();
        ids = ids + friendIds.get(0).toString();
        for(int i=1; i<friendIds.size(); i++)
        {

            ids = ids + ", " + friendIds.get(i).toString();
        }
        try {
            String vkScriptCode = readFileCode("src\\main\\vkScript\\getFriendsByIds");

            String response = vkApiClient.execute().code(
                    getUserActor(), "var ids = \"" + ids + "\";" + vkScriptCode
            ).executeAsRaw().getContent();
            JsonNode jsonNode = objectMapper.readTree(response).path("response");
            List<Integer> save = new ArrayList<>();
            for (JsonNode curr: jsonNode) {
                save.addAll(objectMapper.convertValue(curr.findValue("items"), new TypeReference<List<Integer>>() {
                })) ;
            }
            return objectMapper.convertValue(jsonNode.findValue("items"), new TypeReference<List<Integer>>() {
            });
        } catch (ClientException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
