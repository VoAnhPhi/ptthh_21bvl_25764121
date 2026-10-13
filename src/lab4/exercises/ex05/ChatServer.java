package lab4.exercises.ex05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
	private static final int PORT = 5006;

	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(20);

		try (ServerSocket server = new ServerSocket(PORT)) {
			System.out.println("Chat Server Listening on port " + PORT);

			while (true) {
				Socket client = server.accept();
				System.out.println("Client connected: " + client.getRemoteSocketAddress());

				pool.submit(() -> {
					try (client) {
						System.out.println("Handling client: " + client.getRemoteSocketAddress());

						BufferedReader in = new BufferedReader(
								new InputStreamReader(
										client.getInputStream(),
										StandardCharsets.UTF_8));
						String message;

						while ((message = in.readLine()) != null) {
							System.out.println("Received: " + message);

							if (message.equalsIgnoreCase("QUIT")) {
								break;
							}
						}
					} catch (IOException e) {
						System.err.println("Client error: " + e.getMessage());
					}
					System.out.println("Client disconnected");
				});
			}
		} catch (IOException e) {
			System.err.println("Server exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}