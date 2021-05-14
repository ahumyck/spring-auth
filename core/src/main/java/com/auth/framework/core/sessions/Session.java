package com.auth.framework.core.sessions;

import com.auth.framework.core.tokens.jwt.JsonWebToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Session implements Serializable {

    private static final long serialVersionUID = -4255298202667895054L;
    private String username;
    private Map<String, Object> parameters;

    public Session(JsonWebToken token) {
        this.username = token.getOwner();
        this.parameters = token.getTokenParameters();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Session)) return false;
        Session session = (Session) o;
        return Objects.equals(username, session.username)
                && Objects.equals(parameters, session.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, parameters);
    }
}
