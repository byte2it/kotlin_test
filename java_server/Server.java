package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {
    private final Map<String, RequestRunner> routes = new HashMap();
    private final ServerSocket socket;
    private final Executor threadPool = Executors.newFixedThreadPool(100);
    private HttpHandler handler;

    public Server(int port) throws IOException {
        this.socket = new ServerSocket(port);
    }

    public void start() throws IOException {
        this.handler = new HttpHandler(this.routes);

        while(true) {
            Socket clientConnection = this.socket.accept();
            this.handleConnection(clientConnection);
        }
    }

    private void handleConnection(Socket clientConnection) {
        Runnable httpRequestRunner = () -> {
            try {
                this.handler.handleConnection(clientConnection.getInputStream(), clientConnection.getOutputStream());
            } catch (IOException var3) {
            }

        };
        this.threadPool.execute(httpRequestRunner);
    }

    public void addRoute(HttpMethod opCode, String route, RequestRunner runner) {
        this.routes.put(opCode.name().concat(route), runner);
    }
}
