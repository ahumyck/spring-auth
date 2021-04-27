package com.diplom.impl.requestBody;

import com.auth.framework.core.tokens.jwt.managers.session.Session;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class SessionsResponseBody {
    private List<Session> sessions;
}
