package com.diplom.impl.requestBody;


import lombok.Data;

@Data
public class UsernamePasswordRequestBody {
    private final String username;
    private final String password;
}
