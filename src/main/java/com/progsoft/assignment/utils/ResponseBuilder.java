package com.progsoft.assignment.utils;

import java.util.HashMap;
import java.util.Map;

public final class ResponseBuilder {

    private Map<String, Object> responseMap;

    private ResponseBuilder() {
        responseMap = new HashMap<>();
    }

    public static ResponseBuilder create() {
        return new ResponseBuilder();
    }

    public ResponseBuilder put(final String key, final Object val) {
        this.responseMap.put(key, val);
        return this;
    }

    public Map<String, Object> build() {
        return this.responseMap;
    }
}
