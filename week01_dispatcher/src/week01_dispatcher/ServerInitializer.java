package week01_dispatcher;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerInitializer {

	public static void main(String[] args) {
		int port = 5000;
		System.out.println("Server ON : " + port);

		try {
			ServerSocket serverSocket = new ServerSocket(port);
			Dispatcher dispatcher = new Dispatcher();

			while (true) {
				dispatcher.dispatch(serverSocket);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
