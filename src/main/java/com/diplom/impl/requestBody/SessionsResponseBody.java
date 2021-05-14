package com.diplom.impl.requestBody;

import com.auth.framework.core.sessions.Session;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class SessionsResponseBody implements Serializable {
    private static final long serialVersionUID = 7188736590343814489L;
    private List<Session> sessions;
}
