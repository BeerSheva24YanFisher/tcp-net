package telran.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServer {
    volatile static boolean isShuttingDown = false;
    private final int port;
    private final Protocol protocol;
    private final ExecutorService executorService;

    public TcpServer(int port, Protocol protocol, int maxThreads) {
        this.port = port;
        this.protocol = protocol;
        this.executorService = Executors.newFixedThreadPool(maxThreads);
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (!isShuttingDown) {
                try {
                    Socket socket = serverSocket.accept();
                    socket.setSoTimeout(1000);
                    if (isShuttingDown) {
                        socket.close();
                    } else {
                        executorService.execute(new TcpClientServerSession(protocol, socket, 1,10));
                    }
                } catch (IOException e) {
                    if (isShuttingDown) break;
                }
            }
        } finally {
            executorService.shutdownNow();
        }
    }

    public void shutdown() {
        isShuttingDown = true;
    }
}