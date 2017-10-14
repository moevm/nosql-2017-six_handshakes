package com.eltech.sh.service;

import com.eltech.sh.beans.TimeBean;
import com.eltech.sh.enums.Timers;

public interface TimerService {
    void init();

    void startTimer(Timers type);

    TimeBean getTimers();

    void suspendTimer(Timers type);
}
