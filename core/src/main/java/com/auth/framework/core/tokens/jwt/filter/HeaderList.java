package com.auth.framework.core.tokens.jwt.filter;

import java.util.ArrayList;
import java.util.List;

public class HeaderList {

    private List<String> headers = new ArrayList<>();

    public HeaderList(List<String> headers) {
        this.headers.addAll(headers);
    }

    public List<String> getHeaders() {
        return this.headers;
    }

    public void addHeader(String name) {
        this.headers.add(name);
    }
}
