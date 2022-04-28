package com.cp.projects.messagingsystem.ui.desktopapp.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.core.io.Resource;

public class SwitchViewEvent extends ApplicationEvent {

    public SwitchViewEvent(Resource source) {
        super(source);
    }

    public Resource getResource() {
        return (Resource) getSource();
    }
}
