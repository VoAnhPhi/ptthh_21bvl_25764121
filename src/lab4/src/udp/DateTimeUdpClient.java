package lab4.src.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DateTimeUdpClient {

	public static void main(String[] args) {

		String host = args.length > 0 ? args[0] : "localhost";
		int port = args.length > 1 ? Integer.parseInt(args[1]) : 5004;

		try (
			DatagramSocket socket = new DatagramSocket();
			Scanner scanner = new Scanner(System.in)
		) {

			socket.setSoTimeout(3000);

			InetAddress server = InetAddress.getByName(host);

			System.out.println("UDP Date Time Client");
			System.out.println("Commands: DATE, TIME, DATETIME, EXIT");

			while (true) {

				System.out.print("> ");

				String command = scanner.nextLine();

				if (command.equalsIgnoreCase("EXIT")) {
					break;
				}

				byte[] data = command.getBytes(
						StandardCharsets.UTF_8
				);

				DatagramPacket request = new DatagramPacket(
						data,
						data.length,
						server,
						port
				);

				socket.send(request);

				byte[] buffer = new byte[1024];

				DatagramPacket response = new DatagramPacket(
						buffer,
						buffer.length
				);

				try {

					socket.receive(response);

					String result = new String(
							response.getData(),
							response.getOffset(),
							response.getLength(),
							StandardCharsets.UTF_8
					);

					System.out.println(
							"Server: " + result
					);

				} catch (SocketTimeoutException e) {

					System.out.println(
							"Timeout: no response after 3 seconds"
					);
				}
			}

		} catch (Exception e) {
			System.err.println(
					"UDP client error: " + e.getMessage()
			);
		}
	}
}