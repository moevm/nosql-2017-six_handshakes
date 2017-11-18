package com.eltech.sh.service;

import com.eltech.sh.beans.Edge;
import javafx.util.Pair;

import java.util.List;

public interface DBService {
    void migrateToDB(Integer curUser);

    List<Integer> findPathByQuery(Integer from, Integer to, Integer curUser);

    Boolean isPathExist(Integer from, Integer to, Integer curUser);

    Pair<List<Edge>, List<Integer>> findWebByQuery(Integer from, Integer to, Integer curUser);

    Integer countPeople(Integer user);

    void deleteCluster(Integer user);

    int countPathLength(Integer from, Integer to, Integer curUser);
}