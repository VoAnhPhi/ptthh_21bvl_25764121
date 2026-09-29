package lab4.src.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUdpServer {

	private static final int PORT = 5004;

	private static final DateTimeFormatter DATE_FORMAT =
			DateTimeFormatter.ofPattern("dd MM yyyy");

	private static final DateTimeFormatter TIME_FORMAT =
			DateTimeFormatter.ofPattern("HH mm ss");

	public static void main(String[] args) {

		byte[] buffer = new byte[1024];

		try (DatagramSocket socket = new DatagramSocket(PORT)) {

			System.out.println(
					"Date Time UDP Server listening on port " + PORT
			);

			while (true) {

				DatagramPacket request = new DatagramPacket(
						buffer,
						buffer.length
				);

				socket.receive(request);

				String command = new String(
						request.getData(),
						request.getOffset(),
						request.getLength(),
						StandardCharsets.UTF_8
				);

				command = command.trim().toUpperCase();

				System.out.println(
						"Client: " + command
				);

				String responseText = process(command);

				byte[] responseData = responseText.getBytes(
						StandardCharsets.UTF_8
				);

				DatagramPacket response = new DatagramPacket(
						responseData,
						responseData.length,
						request.getAddress(),
						request.getPort()
				);

				socket.send(response);
			}

		} catch (Exception e) {
			System.err.println(
					"UDP server error: " + e.getMessage()
			);
		}
	}

	private static String process(String command) {

		LocalDateTime now = LocalDateTime.now();

		switch (command) {

			case "DATE":
				return "OK " + now.format(DATE_FORMAT);

			case "TIME":
				return "OK " + now.format(TIME_FORMAT);

			case "DATETIME":
				return "OK "
						+ now.format(DATE_FORMAT)
						+ " "
						+ now.format(TIME_FORMAT);

			default:
				return "ERR UNKNOWN_COMMAND";
		}
	}
}