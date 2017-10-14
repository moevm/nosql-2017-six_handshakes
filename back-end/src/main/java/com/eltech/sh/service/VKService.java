package com.eltech.sh.service;

import com.eltech.sh.model.Person;
import com.vk.api.sdk.objects.UserAuthResponse;

import java.util.List;
import java.util.Map;

public interface VKService {
    UserAuthResponse getAuthInfo(String code);

    Person getPersonByStringId(String id);

    List<Integer> findIdsOfPersonFriends(Integer id);

    String getUserImgUrl(Integer userId);

    List<Person> getPersonsByIds(List<Integer> ids);

    Integer getPersonIntegerIdByStringId(String userId);

    Map<Integer, List<Integer>> findFriendsForGivenPeople(List<Integer> userIds);
}
