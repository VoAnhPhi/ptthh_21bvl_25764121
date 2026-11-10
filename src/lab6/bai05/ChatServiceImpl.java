
package lab6.bai05;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ChatServiceImpl extends UnicastRemoteObject
		implements ChatService {

	private String reply = "";
	private final Scanner sc = new Scanner(System.in);

	public ChatServiceImpl() throws RemoteException {
		super();
	}

	@Override
	public synchronized void sendMessage(String message)
			throws RemoteException {

		System.out.println("\nClient: " + message);

		System.out.print("Server: ");
		reply = sc.nextLine();
	}

	@Override
	public synchronized String getReply() throws RemoteException {
		return reply;
	}
}
