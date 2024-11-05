package org.example;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws IOException {
        Server myServer = new Server(8088);
        myServer.addRoute(HttpMethod.GET, "/testOne", (req) -> {
            return (new HttpResponse.Builder()).setStatusCode(200).addHeader("Content-Type", "text/html").setEntity("<HTML> <P> Hello, I am good, and you --? </P> </HTML>").build();
        });
        myServer.start();
    }
}
