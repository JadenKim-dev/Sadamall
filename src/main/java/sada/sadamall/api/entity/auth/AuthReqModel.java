package sada.sadamall.api.entity.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthReqModel {
    private String id;
    private String password;
}