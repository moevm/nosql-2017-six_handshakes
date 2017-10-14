package com.eltech.sh.service;

import com.eltech.sh.beans.TimeBean;
import com.eltech.sh.enums.Timers;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Service;

@Service
public class TimerServiceImpl implements TimerService {

    private StopWatch vkTimer;
    private StopWatch dbTimer;
    private StopWatch pathTimer;
    private StopWatch csvTimer;

    @Override
    public void init() {
        this.vkTimer = new StopWatch();
        this.dbTimer = new StopWatch();
        this.pathTimer = new StopWatch();
        this.csvTimer = new StopWatch();
    }

    private void start(StopWatch timer) {
        if (timer.isSuspended()) {
            timer.resume();
        } else {
            timer.start();
        }
    }

    private StopWatch getTimer(Timers type) {
        switch (type) {
            case VK_TIMER:
                return vkTimer;
            case DB_TIMER:
                return dbTimer;
            case CSV_TIMER:
                return csvTimer;
            case PATH_TIMER:
                return pathTimer;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public void startTimer(Timers type) {
        start(getTimer(type));
    }

    @Override
    public TimeBean getTimers() {
        return new TimeBean(dbTimer.getTime(), vkTimer.getTime(), pathTimer.getTime(), csvTimer.getTime());
    }

    @Override
    public void suspendTimer(Timers type) {
        getTimer(type).suspend();
    }
}
