package com.eltech.sh.utils;

import com.eltech.sh.beans.Node;
import com.eltech.sh.enums.Nodes;
import com.eltech.sh.model.Person;

import java.util.List;
import java.util.stream.Collectors;

public class NodeUtils {
    public static List<Node> getNodesByPeople (List<Person> people, Integer fromId, Integer toId){
        return people.stream().map(
                person -> new Node(
                        person.getVkId(),
                        String.format("%s %s", person.getFirstName(), person.getLastName()),
                        person.getPhotoUrl(),
                        defineNodeType(person.getVkId(), fromId, toId)
                ))
                .collect(Collectors.toList());
    }

    private static Nodes defineNodeType(Integer nodeId, Integer fromId, Integer toId){
        if (fromId.equals(nodeId)) {
            return Nodes.START_NODE;
        } else if (toId.equals(nodeId)) {
            return Nodes.END_NODE;
        } else {
            return Nodes.REGULAR_NODE;
        }
    }

}