package lab4.src.network;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class HostUriInspector {

	public static void main(String[] args) {

		if (args.length != 2) {
			System.out.println("Usage: java lab4.src.network.HostUriInspector <hostname> <uri>");
			return;
		}

		String hostname = args[0];
		String uriText = args[1];

		inspectHost(hostname);
		inspectUri(uriText);
	}

	private static void inspectHost(String hostname) {

		System.out.println("===== HOST INFORMATION =====");

		try {
			InetAddress[] addresses = InetAddress.getAllByName(hostname);

			System.out.println("Host: " + hostname);

			for (InetAddress address : addresses) {

				System.out.println("-------------------------");
				System.out.println("IP: " + address.getHostAddress());

				if (address instanceof Inet4Address) {
					System.out.println("Type: IPv4");
				} else if (address instanceof Inet6Address) {
					System.out.println("Type: IPv6");
				}

				System.out.println("Loopback: " + address.isLoopbackAddress());
				System.out.println("Site local: " + address.isSiteLocalAddress());
			}

		} catch (UnknownHostException e) {
			System.out.println("Cannot resolve host: " + hostname);
		}
	}

	private static void inspectUri(String uriText) {

		System.out.println();
		System.out.println("===== URI INFORMATION =====");

		try {
			URI uri = new URI(uriText);

			System.out.println("Scheme: " + uri.getScheme());
			System.out.println("Host: " + uri.getHost());
			System.out.println("Port: " + uri.getPort());
			System.out.println("Path: " + uri.getPath());
			System.out.println("Query: " + uri.getQuery());
			System.out.println("Fragment: " + uri.getFragment());

		} catch (URISyntaxException e) {
			System.out.println("Invalid URI: " + uriText);
		}
	}
}