package com.eltech.sh.listener;

import com.eltech.sh.service.CSVService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSessionEvent;


@Component
public class SessionListener implements javax.servlet.http.HttpSessionListener, ApplicationContextAware {

    @Autowired
    private CSVService csvService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof WebApplicationContext) {
            ((WebApplicationContext) applicationContext).getServletContext().addListener(this);
        } else {
            //Either throw an exception or fail gracefully, up to you
            throw new RuntimeException("Must be inside a web application context");
        }
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        System.out.println("SessionDestroyed " + event.getSession().getAttribute("current_user_id"));
        csvService.deleteCSV(event.getSession().getAttribute("current_user_id").toString());
    }
}