package org.example;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Optional;

public class HttpHandler {
    private final Map<String, RequestRunner> routes;

    public HttpHandler(Map<String, RequestRunner> routes) {
        this.routes = routes;
    }

    public void handleConnection(InputStream inputStream, OutputStream outputStream) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        Optional<HttpRequest> request = HttpDecoder.decode(inputStream);
        request.ifPresentOrElse((r) -> {
            this.handleRequest(r, bufferedWriter);
        }, () -> {
            this.handleInvalidRequest(bufferedWriter);
        });
        bufferedWriter.close();
        inputStream.close();
    }

    private void handleInvalidRequest(BufferedWriter bufferedWriter) {
        HttpResponse notFoundResponse = (new HttpResponse.Builder()).setStatusCode(400).setEntity("Invalid Request...").build();
        ResponseWriter.writeResponse(bufferedWriter, notFoundResponse);
    }

    private void handleRequest(HttpRequest request, BufferedWriter bufferedWriter) {
        String routeKey = request.getHttpMethod().name().concat(request.getUri().getRawPath());
        if (this.routes.containsKey(routeKey)) {
            ResponseWriter.writeResponse(bufferedWriter, ((RequestRunner)this.routes.get(routeKey)).run(request));
        } else {
            ResponseWriter.writeResponse(bufferedWriter, (new HttpResponse.Builder()).setStatusCode(404).setEntity("Route Not Found...").build());
        }

    }
}
