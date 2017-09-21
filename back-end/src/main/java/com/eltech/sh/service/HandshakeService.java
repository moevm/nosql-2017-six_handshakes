//package com.eltech.sh.service;
//
//import com.eltech.sh.model.Person;
//import com.eltech.sh.repository.PersonRepository;
//import com.vk.api.sdk.objects.users.UserXtrCounters;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
//@Service
//public class HandshakeService {
//
//    private final VKService vkService;
//    private final PersonRepository personRepository;
//    private Set<Integer> visited;
//    private Queue<Integer> toVisit;
//
//    @Autowired
//    public HandshakeService(VKService vkService, PersonRepository personRepository) {
//        this.vkService = vkService;
//        this.personRepository = personRepository;
//
//    }
//
//    public Iterable<Person> checkSixHandshakes(String from, String to) {
//        toVisit = new LinkedList<>();
//        visited = new HashSet<>(10000);
//
//        Integer origIdFrom = vkService.getOriginalId(from),
//                origIdTo = vkService.getOriginalId(to);
//
//        toVisit.add(origIdFrom);
//        toVisit.add(origIdTo);
//
//        Iterable<Person> path = createGraph(origIdFrom, origIdTo);
//        return path;
//    }
//
//    protected Iterable<Person> createGraph(int from, int to) {
//        Queue<Integer> nextLevel = new LinkedList<>();
//
//        for (int i = 0; i < 3; i++) {
//            while (!toVisit.isEmpty()) {
//                Integer cur = toVisit.poll();
//                if (!visited.contains(cur)) {
//                    List<Person> friends = savePersonFriends(cur.toString());
//                    visited.add(cur);
//                    for (Person p : friends) {
//                        if (!visited.contains(p.getVkId())) {
//                            nextLevel.add(p.getVkId());
//                        }
//                    }
//                }
//            }
//
//            toVisit.addAll(nextLevel);
//            nextLevel.clear();
//
//            Iterable<Person> friends = personRepository.findPathByQuery(from, to);
//            if (friends.iterator().hasNext()) return friends;
//        }
//        return personRepository.findPathByQuery(from, to);
//    }
//
//    protected List<Person> savePersonFriends(String id) {
//        UserXtrCounters userById = vkService.getUserById(id);
//        Person user = new Person(userById.getId(), userById.getFirstName(), userById.getLastName());
//
//        List<Person> friends = vkService.findPersonFriends(id);
//        if(friends != null){
//            friends.forEach(user::friendOf);
//            System.out.println(user.getVkId());
//            personRepository.save(user);
//            return friends;
//        } else {
//            return new ArrayList<>();
//        }
//    }
//}