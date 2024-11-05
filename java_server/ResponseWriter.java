package org.example;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ResponseWriter {
    public ResponseWriter() {
    }

    public static void writeResponse(BufferedWriter os, HttpResponse response) {
        try {
            int statusCode = response.getStatusCode();
            String statusCodeMeaning = (String)HttpStatusCode.STATUS_CODES.get(statusCode);
            List<String> responseHeaders = buildHeaderStrings(response.getResponseHeaders());
            os.write("HTTP/1.1 " + statusCode + " " + statusCodeMeaning + "\r\n");
            Iterator iter = responseHeaders.iterator();

            String encodedString;
            while(iter.hasNext()) {
                encodedString = (String)iter.next();
                os.write(encodedString);
            }

            Optional<String> entityString = response.getEntity().flatMap(ResponseWriter::getResponseString);
            if (entityString.isPresent()) {
                encodedString = new String(((String)entityString.get()).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                os.write("Content-Length: " + encodedString.getBytes().length + "\r\n");
                os.write("\r\n");
                os.write(encodedString);
            } else {
                os.write("\r\n");
            }
        } catch (Exception ex) {
        }

    }

    private static List<String> buildHeaderStrings(Map<String, List<String>> responseHeaders) {
        List<String> responseHeadersList = new ArrayList();
        responseHeaders.forEach((name, values) -> {
            StringBuilder valuesCombined = new StringBuilder();
            Objects.requireNonNull(valuesCombined);
            values.forEach(valuesCombined::append);
            valuesCombined.append(";");
            responseHeadersList.add(name + ": " + valuesCombined + "\r\n");
        });
        return responseHeadersList;
    }

    private static Optional<String> getResponseString(Object entity) {
        if (entity instanceof String) {
            try {
                return Optional.of(entity.toString());
            } catch (Exception var2) {
            }
        }

        return Optional.empty();
    }
}
