package com.eltech.sh.service;

import com.eltech.sh.configuration.VkCredentialsConfiguration;
import com.eltech.sh.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    public VKService(VkCredentialsConfiguration configuration, VkApiClient vkApiClient, Friends friends, ObjectMapper objectMapper, HttpSession session) {
        this.configuration = configuration;
        this.vkApiClient = vkApiClient;
        this.friends = friends;
        this.objectMapper = objectMapper;
        this.session = session;
    }

    private UserActor getUserActor() {
        return new UserActor(configuration.getAppId(), (String) session.getAttribute("access_token"));
    }



    public UserXtrCounters getUserById(String userId) {
        try {
            List<UserXtrCounters> list = vkApiClient.users().get(getUserActor()).userIds(userId).execute();
            return list.get(0);
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Person> findPersonFriends(String userStringId) {
        Integer userId = getUserById(userStringId).getId();
        String response = null;
        try {
            response = friends.get(getUserActor()).listId(userId).unsafeParam("fields", "city,domain").unsafeParam("user_id", userId).executeAsRaw().getContent();
        } catch (ClientException e) {
            e.printStackTrace();
        }

        List<Person> convertedFriends = null;

        try {
            JsonNode jsonNode = objectMapper.readTree(response).path("response").path("items");
            convertedFriends = objectMapper.convertValue(jsonNode, new TypeReference<List<Person>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return convertedFriends;
    }


    public String getAccessToken(String code) {
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
        return authResponse.getAccessToken();
    }

    public Integer getOriginalId(String id){
      return getUserById(id).getId();
    }
}
