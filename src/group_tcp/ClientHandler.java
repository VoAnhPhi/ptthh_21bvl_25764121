
package group_tcp;

import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	String name;
	String room = null;

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			// Nhập tên Client
			out.println("Nhap ten cua ban:");
			name = in.readLine();
			if (name == null || name.trim().isEmpty())
				return;
			name = name.trim();

			synchronized (ChatServer.clients) {
				ChatServer.clients.add(this);
			}

			while (true) {
				// Hiển thị danh sách phòng
				if (room == null) {
					out.println("\n--- DANH SACH PHONG ---");
					for (String r : ChatServer.rooms) {
						out.println("- " + r);
					}
					out.println("Nhap ten phong hoac /exit:");

					String input = in.readLine();
					if (input == null || input.trim().equalsIgnoreCase("/exit"))
						break;

					String selectedRoom = null;
					for (String r : ChatServer.rooms) {
						if (r.equalsIgnoreCase(input.trim())) {
							selectedRoom = r;
							break;
						}
					}

					if (selectedRoom == null) {
						out.println("Phong khong ton tai!");
					} else {
						synchronized (ChatServer.clients) {
							room = selectedRoom;
						}
						out.println("Da vao phong " + room);
						out.println("Nhap /quit de roi phong, /exit de thoat");
						broadcast(name + " da vao phong.");
					}
				} else {
					// Nhận và xử lý tin nhắn
					String message = in.readLine();
					if (message == null || message.equalsIgnoreCase("/exit"))
						break;

					if (message.equalsIgnoreCase("/quit")) {
						broadcast(name + " da roi phong.");
						synchronized (ChatServer.clients) {
							room = null;
						}
						out.println("Ban da roi phong!");
					} else {
						broadcast(name + ": " + message);
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Client mat ket noi!");
		} finally {
			// Xóa Client khi ngắt kết nối
			if (room != null)
				broadcast(name + " da thoat.");

			synchronized (ChatServer.clients) {
				ChatServer.clients.remove(this);
			}

			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("Loi dong Socket!");
			}
			System.out.println(name + " da ngat ket noi.");
		}
	}

	// Gửi tin nhắn cho các Client khác trong cùng phòng
	void broadcast(String message) {
		synchronized (ChatServer.clients) {
			for (ClientHandler client : ChatServer.clients) {
				if (client != this && room != null && room.equals(client.room)) {
					client.out.println(message);
				}
			}
		}
	}
}
