package week01_dispatcher;

import java.io.InputStream;

public interface EventHandler {

	public String getHandler();

	public void handleEvent(InputStream inputStream);
}
