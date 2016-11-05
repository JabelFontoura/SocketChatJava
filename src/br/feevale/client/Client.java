package br.feevale.client;

import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import br.feevale.server.Server;
import br.feevale.view.ClientInterface;

public class Client {
	private static Socket socket;
	private OutputStream ou;
	private Writer ouw; 
	private BufferedWriter bfw;
	private static boolean connected = false;

	public Client() throws IOException{                               

	}

	public void connect(String ip, int port, JTextField txtName, JLabel lblUsers, JButton btnSend, JTextField txtMsg, JTextArea txtHistory, JButton btnConnect) throws IOException{      
		socket = new Socket(ip,port);
		ou = socket.getOutputStream();
		ouw = new OutputStreamWriter(ou);
		bfw = new BufferedWriter(ouw);
		bfw.write(txtName.getText()+"\r\n");
		lblUsers.setText("");
		bfw.flush();
		txtMsg.requestFocus();
		btnSend.setEnabled(true);
		txtMsg.setEnabled(true);
		txtHistory.setEnabled(true);
		btnConnect.setEnabled(false);
		
		connected = true;
		
	}

	public void sendMessage(String msg, JTextArea txtHistory, JTextField txtName, JTextField txtMsg) throws IOException{

		if(msg.equals("Sair")){
			bfw.write("Desconectado \r\n");
			txtHistory.append(txtName.getText() + " desconectado \r\n");
		}else if(!msg.equalsIgnoreCase("")){
			bfw.write(msg + "\r\n");
			txtHistory.append("Você: " + txtMsg.getText()+"\r\n");
		}
		bfw.flush();
		txtMsg.setText("");  

	}

	public static void listen() throws IOException{

		InputStream in = socket.getInputStream();
		InputStreamReader inr = new InputStreamReader(in);
		BufferedReader bfr = new BufferedReader(inr);
		String msg = "";
		int i = 0;
		while(!msg.equalsIgnoreCase("Sair"))
			if(bfr.ready()){
				msg = bfr.readLine();
				if(msg.equals("Sair"))ClientInterface.getTxtHistory().append("Servidor caiu! \r\n");
				else ClientInterface.getTxtHistory().append(msg + "\r\n");         
			}
	}

	public void exit(JTextArea txtHistory, JTextField txtName, JTextField txtMsg, JButton btnConnect, JButton btnSend) throws IOException{

		sendMessage("Sair", txtHistory, txtName, txtMsg);
		bfw.close();
		ouw.close();
		ou.close();
		socket.close();
		btnConnect.setEnabled(true);
		btnSend.setEnabled(false);
		txtMsg.setEnabled(false);
		connected = false;
	}

	public static void main(String []args) throws IOException{
		new ClientInterface();

		new Thread(() -> {
			while(true){
				try {
					System.out.println(connected);
					if(connected) listen();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}).start();

	}
	
	public boolean isConnected(){
		return connected;
	}

}







