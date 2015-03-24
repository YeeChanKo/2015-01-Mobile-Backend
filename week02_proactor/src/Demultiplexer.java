import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class Demultiplexer implements CompletionHandler<Integer, ByteBuffer> {

	private AsynchronousSocketChannel channel;
	private NioHandleMap handleMap;

	public Demultiplexer(AsynchronousSocketChannel channel,
			NioHandleMap handleMap) {
		this.channel = channel;
		this.handleMap = handleMap;
	}

	@Override
	public void completed(Integer result, ByteBuffer buffer) {

		if (result == -1) {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (result > 0) {
			buffer.flip(); // bytebuffer 읽어올 수 있게 모드 변환 (이전에는 socketchannel에서 read로 buffer에 값을 씀)
			String header = new String(buffer.array());
			
			NioEventHandler handler = handleMap.get(header);
			ByteBuffer newBuffer = ByteBuffer.allocate(handler.getDataSize());
			
			handler.initialize(channel, newBuffer);
			// 이벤트핸들러는 한번 만들어놓고 계속 쓰는 것이기 때문에 (생성자를 쓰지 않고 매번 사용할 때마다) 새로 초기화해줘야 한다
			channel.read(newBuffer, newBuffer, handler);
		}
	}

	@Override
	public void failed(Throwable exc, ByteBuffer attachment) {
		// TODO Auto-generated method stub

	}
}
