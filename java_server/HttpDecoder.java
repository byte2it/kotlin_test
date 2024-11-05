package org.example;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class HttpDecoder {
    public HttpDecoder() {
    }

    public static Optional<HttpRequest> decode(InputStream is) {
        return readMessage(is).flatMap(HttpDecoder::buildRequest);
    }

    private static Optional<HttpRequest> buildRequest(List<String> msg) {
        if (msg.isEmpty()) {
            return Optional.empty();
        } else {
            String firstLine = (String)msg.get(0);
            String[] httpInfo = firstLine.split(" ");
            if (httpInfo.length != 3) {
                return Optional.empty();
            } else {
                String httpVersion = httpInfo[2];
                if (!httpVersion.equals("HTTP/1.1")) {
                    return Optional.empty();
                } else {
                    try {
                        HttpRequest.Builder requestBuilder = new HttpRequest.Builder();
                        requestBuilder.setHttpMethod(HttpMethod.valueOf(httpInfo[0]));
                        requestBuilder.setUri(new URI(httpInfo[1]));
                        return Optional.of(addRequestHeaders(msg, requestBuilder));
                    } catch (IllegalArgumentException | URISyntaxException var5) {
                        return Optional.empty();
                    }
                }
            }
        }
    }

    private static Optional<List<String>> readMessage(InputStream is) {
        try {
            if (is.available() <= 0) {
                return Optional.empty();
            } else {
                char[] inBuffer = new char[is.available()];
                InputStreamReader inReader = new InputStreamReader(is);
                inReader.read(inBuffer);
                List<String> msg = new ArrayList();
                Scanner sc = new Scanner(new String(inBuffer));

                try {
                    while(sc.hasNextLine()) {
                        String line = sc.nextLine();
                        msg.add(line);
                    }
                } catch (Throwable thr) {
                    try {
                        sc.close();
                    } catch (Throwable thr2) {
                        thr.addSuppressed(thr2);
                    }

                    throw thr;
                }

                sc.close();
                return Optional.of(msg);
            }
        } catch (Exception var10) {
            return Optional.empty();
        }
    }

    private static HttpRequest addRequestHeaders(List<String> msg, HttpRequest.Builder builder) {
        Map<String, List<String>> requestHeaders = new HashMap();
        if (msg.size() > 1) {
            for(int i = 1; i < msg.size(); ++i) {
                String header = (String)msg.get(i);
                int colonIndex = header.indexOf(58);
                if (colonIndex <= 0 || header.length() <= colonIndex + 1) {
                    break;
                }

                String headerName = header.substring(0, colonIndex);
                String headerValue = header.substring(colonIndex + 1);
                requestHeaders.compute(headerName, (key, values) -> {
                    if (values != null) {
                        ((List)values).add(headerValue);
                    } else {
                        values = new ArrayList();
                    }

                    return (List)values;
                });
            }
        }

        builder.setRequestHeaders(requestHeaders);
        return builder.build();
    }
}
