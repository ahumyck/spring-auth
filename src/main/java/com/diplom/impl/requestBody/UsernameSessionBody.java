package com.diplom.impl.requestBody;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
public class UsernameSessionBody {
    private String username;
    private Map<String, Object> sessionDetails;
}
