import java.io.File;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class ServerInitializer {

	public static void main(String[] args) {
		int port = 5000;
		System.out.println("Server ON : " + port);

		Reactor reactor = new Reactor(port);

		try {
			Serializer serializer = new Persister();
			File source = new File("HandlerList.xml");
			ServerListData serverList = serializer.read(ServerListData.class,
					source);

			for (HandlerListData handlerListData : serverList.getServer()) {
				if ("server1".equals(handlerListData.getName())) {
					List<HandlerData> handlerList = handlerListData
							.getHandler();
					for (HandlerData handler : handlerList) {
						try {
							reactor.registerHandler(
									handler.getHeader(),
									(EventHandler) Class.forName(
											handler.getHandler()).newInstance());
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		reactor.startServer();
	}
}