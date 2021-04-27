package com.diplom.impl.requestBody;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UsernamePasswordRequestBody {
    private String username;
    private String password;
}
