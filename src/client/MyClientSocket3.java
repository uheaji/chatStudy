package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClientSocket3 {
 
	private Socket socket;
	private Scanner sc; // ������ �����ϸ� heap����
	private BufferedWriter writer;
	private BufferedReader reader;
	
	public MyClientSocket3() {
		try {
			socket = new Socket("127.0.0.1", 10000);
			
			// Ű����κ��� �Է� �޾Ƽ�
			sc = new Scanner(System.in);
			// ���Ͽ� ���۴ޱ�(����)
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			new Thread(new Runnable() { // �͸�Ŭ����
				
				@Override
				public void run() {
					String input = null;
					try {
						while ((input = reader.readLine()) != null) {
							System.out.println("�����κ��� �� �޽��� : " + input);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}).start();
			
			// ���� ������� Ű����κ��� �Է��� ���� �� Ŭ���̾�Ʈ ���Ͽ� ����.
			while (true) {
				String input = sc.nextLine(); // ���ڿ� �ޱ�
				//������ ����
				writer.write(input + "\n");
				writer.flush();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		new MyClientSocket3(); // ���ν����尡 ���� �ִ�!

	}

}
