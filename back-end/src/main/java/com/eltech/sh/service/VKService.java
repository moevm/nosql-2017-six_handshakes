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
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

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

    public Person getUserByStringId(String id) {
        try {
            Thread.sleep(400);
            UserXtrCounters user = vkApiClient.users().get(getUserActor()).userIds(id).execute().get(0);
            return new Person(user.getId(), user.getFirstName(), user.getLastName());
        } catch (ApiException | ClientException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Person> findPersonFriends(Integer id) {
        try {
            String response = friends.get(getUserActor()).listId(id)
                    .unsafeParam("fields", "city,domain")
                    .unsafeParam("user_id", id)
                    .executeAsRaw().getContent();
            JsonNode jsonNode = objectMapper.readTree(response).path("response").path("items");
            return objectMapper.convertValue(jsonNode, new TypeReference<List<Person>>() {});
        } catch (ClientException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUserImgUrl(Integer userId) {
        try {
            List<UserXtrCounters> list = vkApiClient.users().get(getUserActor()).unsafeParam("user_id", userId).unsafeParam("fields", "photo_400_orig").execute();
            return list.get(0).getPhoto400Orig();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserAuthResponse getAuthInfo(String code) {
        UserAuthResponse authResponse = null;
        try {
            authResponse = vkApiClient.oauth()
                    .userAuthorizationCodeFlow(
                            configuration.getAppId(),
                            configuration.getClientSecret(),
                            configuration.getRedirectUri(),
                            code)
                    .execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        return authResponse;
    }


}
