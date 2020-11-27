package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class MyServerSocket3 {

	private ServerSocket serverSocket;
	private Vector<SocketThread> vc;

	public MyServerSocket3() {
		try {
			serverSocket = new ServerSocket(20000);
			vc = new Vector<>();
			
			while (true) {
				System.out.println("��û ���");
				Socket socket = serverSocket.accept();
				System.out.println("��û ����");
				SocketThread st = new SocketThread(socket);
				st.start();
				vc.add(st);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	// �������� + Ÿ��(run) + �ĺ���(id)
	class SocketThread extends Thread {
		Socket socket;
		String id;
		BufferedReader reader;
		PrintWriter writer;
		
		public SocketThread(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream(),true);
				String input = null;
				while ((input = reader.readLine()) != null) {
					System.out.println("from client : " + input);
					for (SocketThread socketThread : vc) {
						socketThread.writer.println(input);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	public static void main(String[] args) {
		new MyServerSocket3();
	}
}
