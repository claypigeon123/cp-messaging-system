package com.cp.projects.messagingsystem.ui.desktopapp;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CpMessagingSpringDesktopApp {
    public static void main(String[] args) {
        Application.launch(CpMessagingJavafxDesktopApp.class, args);
    }
}
