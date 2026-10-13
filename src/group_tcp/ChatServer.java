
package group_tcp;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    static final int PORT = 8080;
    static String[] rooms = {"Study", "Game", "TraiNghiem", "Music"};
    static ArrayList<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server dang chay o port " + PORT);

            while (true) {
                Socket socket = server.accept();
                System.out.println("Client moi ket noi: " + socket.getInetAddress());

                // Tạo Thread mới để xử lý từng Client
                ClientHandler client = new ClientHandler(socket);
                client.start();
            }
        } catch (IOException e) {
            System.out.println("Loi Server: " + e.getMessage());
        }
    }
}
