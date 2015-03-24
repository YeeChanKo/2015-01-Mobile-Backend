import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.StringTokenizer;

public class NioSayHelloEventHandler implements NioEventHandler {
	private static final int TOKEN_NUM = 2;

	private AsynchronousSocketChannel channel;
	private ByteBuffer buffer;

	@Override
	public void completed(Integer result, ByteBuffer attachment) {

		if (result == -1) {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (result > 0) {
			buffer.flip();
			String msg = new String(buffer.array());

			String[] params = new String[TOKEN_NUM];
			StringTokenizer token = new StringTokenizer(msg, "|");
			int i = 0;
			while (token.hasMoreTokens()) {
				params[i] = token.nextToken();
				i++;
			}

			sayHello(params);

			try {
				buffer.clear(); // 안해도 상관 없는 거 아닌가? 어차피 initialize할 때 참조 잃으면 가비지컬렉팅 되는 거 아닌가?
				channel.close(); // 최종적으로 채널 닫아주기 - memory leak 방지!
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void failed(Throwable exc, ByteBuffer attachment) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getHandle() {
		return "0x5001";
	}

	@Override
	public int getDataSize() {
		return 512;
	}

	@Override
	public void initialize(AsynchronousSocketChannel channel, ByteBuffer buffer) {
		this.channel = channel;
		this.buffer = buffer;
	}

	private void sayHello(String[] params) {
		System.out.println("SayHello -> name : " + params[0] + " age : "
				+ params[1]);
	}
}
