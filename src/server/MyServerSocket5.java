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
	Vector<SocketThread> vc; // 대기열(큐)

	public MyServerSocket5() {
		try {
			serverSocket = new ServerSocket(10000);
			vc = new Vector<>();

			while (true) {
				System.out.println("요청 대기중 ...");
				Socket socket = serverSocket.accept();
				// 1. 새로운 소켓 생성 socket
				// 2. 새로운 스레드 생성
				// 3. 새로운 스레드한테 socket변수 넘기기
				// 4. 새로운 스레드 자체를 vc에 담기
				System.out.println("요청 받음 ...");
				SocketThread st = new SocketThread(socket);
				st.start();
				vc.add(st);
			}

		} catch (Exception e) {
			System.out.println("서버 연결 오류");
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
					// Routing (라우팅 하기)
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
			   writer.println("당신의 ID는 " + id + "입니다.");
			   writer.flush();
			} else {
				writer.println("먼저 ID를 입력하세요");
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
