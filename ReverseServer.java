import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ReverseServer {
    private static final int PORT = 12345;
    private static final int MAX_CLIENTS = 10;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server a démarré sur  " + PORT);

        while (true) {
            if (Thread.activeCount() - 1 < MAX_CLIENTS) { // -1 to exclude the main thread
                new ClientHandler(serverSocket.accept()).start();
            } else {
                System.out.println("Maximum clients! Veuillez attendre.");
                try {
                    Thread.sleep(1000); 
                } catch (InterruptedException e) {
                    System.out.println("Server sleep interrupted: " + e.getMessage());
                }
            }
        }
    }

    private static class ClientHandler extends Thread {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {

                    try {
                        Thread.sleep(5000); // Sleep for 5 seconds
                    } catch (InterruptedException e) {
                        System.out.println("Thread sleep interrupted: " + e.getMessage());
                    }

                    
                    out.println(new StringBuilder(inputLine).reverse().toString());
                }

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Exception caught");
                System.out.println(e.getMessage());
            }
        }
    }
}
