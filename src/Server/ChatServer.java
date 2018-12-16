package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer extends Thread {
	private int port;// 端口
	private boolean running = false;// 服务器是否运行中的标记
	private ServerSocket sc;// 服务器对象

	/*
	 * 创建服务器对象时，必须传入端口号
	 *
	 * @param port:服务器所在端口号
	 */
	public ChatServer(int port) {
		this.port = port;
	}

	// 线程中启动服务器
	public void run() {
		setupServer();
	}

	// 在指定端口上启动服务器
	public void setupServer() {
		try {
			ServerSocket sc = new ServerSocket(this.port);
			running = true;
			System.out.println("服务器创建成功：" + port);
			while (running) {
				Socket client = sc.accept();// 等待连结进入
				System.out.println("进入了一个客户机连接" + client.getRemoteSocketAddress());
				// 启动一个处理线程，去处理这个连结对象
				ServerThread ct = new ServerThread(client);
				ct.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 查询服务器是否在运行
	 *
	 * @return: true为运行中
	 */
	public boolean isRunning() {
		return this.running;
	}

	// 关闭服务器
	public void stopchatServer() {
		this.running = false;
		try {
			sc.close();
		} catch (Exception e) {}
	}
}
