package org.example;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HttpResponse {
    private final Map<String, List<String>> responseHeaders;
    private final int statusCode;
    private final Optional<Object> entity;

    private HttpResponse(Map<String, List<String>> responseHeaders, int statusCode, Optional<Object> entity) {
        this.responseHeaders = responseHeaders;
        this.statusCode = statusCode;
        this.entity = entity;
    }

    public Map<String, List<String>> getResponseHeaders() {
        return this.responseHeaders;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public Optional<Object> getEntity() {
        return this.entity;
    }

    public static class Builder {
        private final Map<String, List<String>> responseHeaders = new HashMap();
        private int statusCode;
        private Optional<Object> entity;

        public Builder() {
            this.responseHeaders.put("Server", List.of("MyServer"));
            this.responseHeaders.put("Date", List.of(DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneOffset.UTC))));
            this.entity = Optional.empty();
        }

        public Builder setStatusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder addHeader(String name, String value) {
            this.responseHeaders.put(name, List.of(value));
            return this;
        }

        public Builder setEntity(Object entity) {
            if (entity != null) {
                this.entity = Optional.of(entity);
            }

            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(this.responseHeaders, this.statusCode, this.entity);
        }
    }
}

