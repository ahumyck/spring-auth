package com.diplom.impl.requestBody;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UsernameSessionBody {
    private String username;
    private String sessionName;
}
