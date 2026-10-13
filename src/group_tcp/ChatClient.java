
package group_tcp;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
	private static final String SERVER_ADDRESS = "localhost";
	private static final int PORT = 8080;

	public static void main(String[] args) {
		try (Socket socket = new Socket(SERVER_ADDRESS, PORT)) {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			Scanner scanner = new Scanner(System.in);

			// Luồng nhận tin nhắn từ Server
			Thread readThread = new Thread(() -> {
				try {
					String serverMessage;
					while ((serverMessage = in.readLine()) != null) {
						System.out.println(serverMessage);
					}
				} catch (IOException e) {
					System.out.println("Da ngat ket noi voi Server.");
				}
			});
			readThread.start();

			// Nhập dữ liệu từ bàn phím và gửi lên Server
			while (true) {
				String userInput = scanner.nextLine();
				out.println(userInput);

				if (userInput.equalsIgnoreCase("/exit")) {
					System.out.println("Dang thoat chuong trinh...");
					break;
				}
			}

		} catch (IOException e) {
			System.out.println("Khong the ket noi den Server.");
		}
	}
}
