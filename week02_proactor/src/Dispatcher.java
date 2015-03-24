import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class Dispatcher
		implements
		CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

	private int HEADER_SIZE = 6;
	private NioHandleMap handleMap;

	public Dispatcher(NioHandleMap handleMap) {
		this.handleMap = handleMap;
	}

	@Override
	public void completed(AsynchronousSocketChannel channel,
			AsynchronousServerSocketChannel listener) {

		listener.accept(listener, this); // 하나의 소켓채널 받아오는 작업이 완료되면 바로 다음 요청(소켓채널)을 받아서 처리한다.

		ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE);
		channel.read(buffer, buffer, new Demultiplexer(channel, handleMap)); // 첫번째 인자에 읽어옴, 두번째는 그냥 attachment
		// channel은 completion handler가 실행되어 오류났을 때는 close,
		// 정상 완료되었을 때는 다른 nioeventhandler에 위임해서 헤더 뒤의 부분을 읽어올 수 있게 넘겨준다
	}

	@Override
	public void failed(Throwable exc, AsynchronousServerSocketChannel listener) {
		// TODO Auto-generated method stub

	}

}
