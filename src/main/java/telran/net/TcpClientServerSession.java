package telran.net;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
public class TcpClientServerSession implements Runnable{
    Protocol protocol;
    Socket socket;
    private final int maxRequestsPerSecond;
    private final int maxFailedResponses;
    private int requestCount = 0;
    private int failedResponseCount = 0;

public TcpClientServerSession(Protocol protocol, Socket socket, int maxRequestsPerSecond, int maxFailedResponses) {
    this.protocol = protocol;
    this.socket = socket;
    this.maxRequestsPerSecond = maxRequestsPerSecond;
    this.maxFailedResponses = maxFailedResponses;
}
@Override
public void run() {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         PrintStream writer = new PrintStream(socket.getOutputStream())) {
        String request;
        while (!Thread.currentThread().isInterrupted() && (request = reader.readLine()) != null) {
            try {
                String response = protocol.getResponseWithJSON(request);
                writer.println(response);
            } catch (Exception e) {
                if (TcpServer.isShuttingDown) break;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

}