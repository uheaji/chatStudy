package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import com.google.gson.Gson;

import protocol.Chat;
import protocol.RequestDto;

public class MyServerSocket5 {

	private ServerSocket serverSocket;
	Vector<SocketThread> vc; // ��⿭(ť)

	public MyServerSocket5() {
		try {
			serverSocket = new ServerSocket(10000);
			vc = new Vector<>();

			while (true) {
				System.out.println("��û ����� ...");
				Socket socket = serverSocket.accept();
				// 1. ���ο� ���� ���� socket
				// 2. ���ο� ������ ����
				// 3. ���ο� ���������� socket���� �ѱ��
				// 4. ���ο� ������ ��ü�� vc�� ���
				System.out.println("��û ���� ...");
				SocketThread st = new SocketThread(socket);
				st.start();
				vc.add(st);
			}

		} catch (Exception e) {
			System.out.println("���� ���� ����");
			e.printStackTrace();
		}
	}

	class SocketThread extends Thread {

		private Socket socket;
		private String id;
		private BufferedReader reader;
		private PrintWriter writer;

		public SocketThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream());
				
				String input = null;
				while ((input = reader.readLine()) != null) {
					// Routing (����� �ϱ�)
					Gson gson = new Gson();
					RequestDto dto = gson.fromJson(input, RequestDto.class);
					routing(dto);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		private void routing(RequestDto dto) {
			if(id == null) {
			if (dto.getGubun().equals(Chat.ID)) {
			   id = dto.getMsg();
			   writer.println("����� ID�� " + id + "�Դϴ�.");
			   writer.flush();
			} else {
				writer.println("���� ID�� �Է��ϼ���");
				writer.flush();
				return;
			}
			}

			if (dto.getGubun().equals(Chat.ALL)) {
				for (int i = 0; i < vc.size(); i++) {
					if (vc.get(i) != this) {
						vc.get(i).writer.println(id + "-->" + dto.getMsg());
						vc.get(i).writer.flush();
					}
				}
			} else if (dto.getGubun().equals(Chat.MSG)) {
				String tempId = dto.getId();
				String tempMsg = dto.getMsg();

				for (int i = 0; i < vc.size(); i++) {
					if (vc.get(i).id != null && vc.get(i).id.equals(tempId)) {
						vc.get(i).writer.println(id + "-->" + tempMsg);
						vc.get(i).writer.flush();
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		new MyServerSocket5();
	}
}
