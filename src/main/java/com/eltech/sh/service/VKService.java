package com.eltech.sh.service;

import com.eltech.sh.configuration.VkCredentialsConfiguration;
import com.eltech.sh.model.Person;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.io.IOException;
import java.util.List;


@Service
public class VKService {

    private final VkCredentialsConfiguration configuration;
    private final VkApiClient vkApiClient;
    private final Friends friends;

    @Autowired
    public VKService(VkCredentialsConfiguration configuration, VkApiClient vkApiClient, Friends friends) {
        this.configuration = configuration;
        this.vkApiClient = vkApiClient;
        this.friends = friends;
    }

    private UserActor getUserActor(String token){
       return new UserActor(configuration.getAppId(), token);
    }

    private UserXtrCounters getUserById(String userId, String token) {
        try {
            List<UserXtrCounters> list = vkApiClient.users().get(getUserActor(token)).userIds(userId).execute();
            return list.get(0);
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Person> findFriend(String userStringId, String token) {
        Integer userId = getUserById(userStringId, token).getId();
        String resp = null;
        try {
            resp = friends.get(getUserActor(token)).listId(userId).unsafeParam("fields", "city,domain").unsafeParam("user_id", userId).executeAsRaw().getContent();
        } catch (ClientException e) {
            e.printStackTrace();
        }

        //FIXME fix this hardcode
        resp = resp.replace("{\"response\":{\"count\":60,\"items\":", "");
        resp = resp.replace("]}}", "]");

        ObjectMapper objectMapper = new ObjectMapper();
        List<Person> parsedFriends = null;
        try {
            parsedFriends = objectMapper.readValue(resp, new TypeReference<List<Person>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parsedFriends;
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
}
