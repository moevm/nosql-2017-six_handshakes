package com.eltech.sh.service;

import com.eltech.sh.beans.ResponseBean;
import com.eltech.sh.model.Person;

public interface CoreService {
    ResponseBean run(String fromId, String toId, String currentUserId);

    Person getPersonByStringId(String id);
}
