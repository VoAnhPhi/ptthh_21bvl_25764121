package lab4.src.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeTcpServer {

	private static final int PORT = 5003;

	private static final DateTimeFormatter DATE_FORMAT =
			DateTimeFormatter.ofPattern("dd MM yyyy");

	private static final DateTimeFormatter TIME_FORMAT =
			DateTimeFormatter.ofPattern("HH mm ss");

	public static void main(String[] args) {

		try (ServerSocket server = new ServerSocket(PORT)) {

			System.out.println("Date Time TCP Server listening on port " + PORT);

			while (true) {

				try (Socket socket = server.accept()) {

					System.out.println(
							"Client connected: " + socket.getRemoteSocketAddress()
					);

					handleClient(socket);

				} catch (Exception e) {
					System.err.println(
							"Client session error: " + e.getMessage()
					);
				}
			}

		} catch (Exception e) {
			System.err.println(
					"Cannot start TCP server: " + e.getMessage()
			);
		}
	}

	private static void handleClient(Socket socket) throws Exception {

		try (
			BufferedReader in = new BufferedReader(
				new InputStreamReader(
					socket.getInputStream(),
					StandardCharsets.UTF_8
				)
			);

			PrintWriter out = new PrintWriter(
				new OutputStreamWriter(
					socket.getOutputStream(),
					StandardCharsets.UTF_8
				),
				true
			)
		) {

			String request;

			while ((request = in.readLine()) != null) {

				System.out.println("Client: " + request);

				String command = request.trim().toUpperCase();

				if (command.equals("QUIT")) {
					out.println("OK BYE");
					break;
				}

				out.println(process(command));
			}
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