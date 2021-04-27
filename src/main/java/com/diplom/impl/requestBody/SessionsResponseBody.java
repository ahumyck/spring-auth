package com.diplom.impl.requestBody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class SessionsResponseBody {
    private List<String> sessions;
}
