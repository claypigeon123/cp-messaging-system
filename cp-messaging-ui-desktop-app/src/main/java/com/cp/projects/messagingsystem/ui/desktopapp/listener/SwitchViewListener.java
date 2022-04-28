package com.cp.projects.messagingsystem.ui.desktopapp.listener;

import com.cp.projects.messagingsystem.ui.desktopapp.event.SwitchViewEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SwitchViewListener implements ApplicationListener<SwitchViewEvent> {

    @Override
    public void onApplicationEvent(SwitchViewEvent event) {

    }

}
