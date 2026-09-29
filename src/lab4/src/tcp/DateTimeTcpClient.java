package lab4.src.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DateTimeTcpClient {

	public static void main(String[] args) {

		String host = args.length > 0 ? args[0] : "localhost";
		int port = args.length > 1 ? Integer.parseInt(args[1]) : 5003;

		try (
			Socket socket = new Socket(host, port);

			BufferedReader console = new BufferedReader(
				new InputStreamReader(
					System.in,
					StandardCharsets.UTF_8
				)
			);

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

			System.out.println("Connected to TCP Date Time Server");
			System.out.println("Commands: DATE, TIME, DATETIME, QUIT");

			String request;

			while ((request = console.readLine()) != null) {

				out.println(request);

				String response = in.readLine();

				if (response == null) {
					System.out.println("Server closed the connection");
					break;
				}

				System.out.println("Server: " + response);

				if (request.equalsIgnoreCase("QUIT")) {
					break;
				}
			}

		} catch (Exception e) {
			System.err.println(
					"TCP client error: " + e.getMessage()
			);
		}
	}
}