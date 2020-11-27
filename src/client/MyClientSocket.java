package client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClientSocket {
 
	private Socket socket;
	
	public MyClientSocket() {
		try {
			socket = new Socket("127.0.0.1", 10000);
			
			// Ű����κ��� �Է� �޾Ƽ�
			Scanner sc = new Scanner(System.in);
			// ���Ͽ� ���۴ޱ�(����)
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			while (true) {
				String input = sc.nextLine(); // ���ڿ� �ޱ�
				//������ ����
				bw.write(input + "\n");
				bw.flush();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		new MyClientSocket();

	}

}
