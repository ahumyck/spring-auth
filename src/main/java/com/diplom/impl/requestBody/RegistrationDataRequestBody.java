package com.diplom.impl.requestBody;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RegistrationDataRequestBody {
    private final String email;
    private final String username;
    private final String password;
}
