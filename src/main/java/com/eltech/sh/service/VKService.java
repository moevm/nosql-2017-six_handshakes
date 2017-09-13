package com.eltech.sh.service;

import com.eltech.sh.model.Person;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

import com.vk.api.sdk.client.ClientResponse;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ApiWallLinksForbiddenException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.actions.Friends;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.users.UserXtrCounters;

@Service
public class VKService {

    //TODO Add VK API support
    List<Person> getPersonFriends(String id) {
        return null;
    }
    public static ClientResponse findFriend(String user_Id)
    {
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);

        Integer USS_ID=6183115;
        String TOKEN="ecdc6e6b4d6bd772e59c22a380d57d449f1e1a31757a8fc59e8d625faf4b76225a30492eec33cb5297529";

        UserActor actor = new UserActor(USS_ID, TOKEN);
        Friends friends = new Friends(vk);
        ClientResponse resp = null;
        try {
            List<UserXtrCounters> list_acc = vk.users().get(actor).userIds(user_Id).execute();
            //String fr = friends.get(actor).listId(list_acc.get(0).getId()).unsafeParam("fields", "city,domain").unsafeParam("user_id", list_acc.get(0).getId()).executeAsString();
            resp= friends.get(actor).listId(list_acc.get(0).getId()).unsafeParam("fields", "city,domain").unsafeParam("user_id", list_acc.get(0).getId()).executeAsRaw();
            // System.out.print(fr);
        }
        catch (ApiWallLinksForbiddenException e) {
            return resp;
            // Links posting is prohibited
        } catch (ApiException e) {
            return resp;
            // Business logic error
        } catch (ClientException e) {
            return resp;
            // Transport layer error
        }
        return resp;
    }
    public static void parsingFromJson(String[] args)
    {

        System.out.print("Введите id пользователя, друзей которого вы хотите найти: ");
        Scanner in = new Scanner(System.in);
        String input_id = in.nextLine();

        ClientResponse response = findFriend(input_id);
        Integer status = response.getStatusCode();

        if (status == 200) {
            JSONParser parser   = new JSONParser();
            try
            {
                JSONObject obj  = (JSONObject) parser.parse(response.getContent());
                JSONArray array = new JSONArray();
                array.add(obj.get("response"));
                JSONObject unicPost  = null;
                for (int i=0; i < array.size(); i++)
                {
                    unicPost = (JSONObject) array.get(i);
                    System.out.println(unicPost.get("text"));
                }
                System.out.print(array);

            } catch (ParseException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }

    }
}
