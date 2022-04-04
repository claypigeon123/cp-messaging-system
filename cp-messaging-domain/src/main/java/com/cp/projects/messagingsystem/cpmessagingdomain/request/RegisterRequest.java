package com.cp.projects.messagingsystem.cpmessagingdomain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String displayName;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;
}
