package lab4.src.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DigitToWordServer {

	private static final int PORT = 5002;

	public static void main(String[] args) {

		try (ServerSocket server = new ServerSocket(PORT)) {

			System.out.println("Digit server listening on port " + PORT);

			while (true) {

				try (Socket socket = server.accept()) {

					System.out.println("Client connected: " + socket.getRemoteSocketAddress());

					handleClient(socket);

				} catch (IOException e) {
					System.err.println("Client session error: " + e.getMessage());
				}
			}

		} catch (IOException e) {
			System.err.println("Cannot start server: " + e.getMessage());
		}
	}

	private static void handleClient(Socket socket) throws IOException {

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

				if (request.equalsIgnoreCase("QUIT")) {
					out.println("OK BYE");
					break;
				}

				out.println(convertDigit(request));
			}
		}
	}

	private static String convertDigit(String value) {

		if (value.length() != 1 || !Character.isDigit(value.charAt(0))) {
			return "ERR INVALID_DIGIT";
		}

		switch (value.charAt(0)) {

			case '0':
				return "OK khong";

			case '1':
				return "OK mot";

			case '2':
				return "OK hai";

			case '3':
				return "OK ba";

			case '4':
				return "OK bon";

			case '5':
				return "OK nam";

			case '6':
				return "OK sau";

			case '7':
				return "OK bay";

			case '8':
				return "OK tam";

			case '9':
				return "OK chin";

			default:
				return "ERR INVALID_DIGIT";
		}
	}
}