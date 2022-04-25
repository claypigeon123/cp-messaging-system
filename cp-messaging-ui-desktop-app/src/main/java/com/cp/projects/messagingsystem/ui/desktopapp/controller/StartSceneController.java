package com.cp.projects.messagingsystem.ui.desktopapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class StartSceneController {

    private final WebClient webClient;

    @FXML
    public TextArea text;

    @FXML
    public Button button;

    public void onAction(ActionEvent actionEvent) {
        webClient.get()
            .uri("https://google.co.uk")
            .retrieve()
            .toEntity(String.class)
            .doOnSuccess(content -> text.setText(content.getBody()))
            .subscribe();
    }
}
