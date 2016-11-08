/* 
 Author: Jabel Fontoura
*/
package br.feevale.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.JSONException;
import org.json.JSONObject;

import br.feevale.view.ClientInterface;

public class Client {
	private static Socket socket;
	private OutputStream ou;
	private Writer ouw; 
	private BufferedWriter bfw;
	private JSONObject obj;
	private JSONObject file;
	private DateFormat dateFormat;
	private static boolean connected = false;

	public Client() throws IOException{                               

	}

	public void connect(String ip, int port, JTextField txtName, JLabel lblUsers, JButton btnSend, JTextField txtMsg, JTextArea txtHistory, JButton btnConnect, JButton btnSendFile) throws IOException{      
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
		btnSendFile.setEnabled(true);
		btnConnect.setEnabled(false);
		sendMessage("/Conectar", txtHistory, txtName, txtMsg);
		connected = true;
	}

	public JSONObject buildMessage(String msg, String user, String fileName, String content) throws JSONException{

		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		obj = new JSONObject();
		file = new JSONObject();
		file.put("Nome", fileName);
		file.put("Conteudo", content);
		if(fileName != null) file.put("Tipo", fileName.substring(fileName.length() - 3, fileName.length()));
			
		obj.put( "Mensagem", msg );
		obj.put( "DataHora", dateFormat.format(Calendar.getInstance().getTime()).toString());		
		obj.put( "Usuario", user);
		obj.put( "Arquivo",  file);

		return obj;
	}

	public void sendMessage(JSONObject msg, JTextArea txtHistory, JTextField txtName, JTextField txtMsg) throws IOException, JSONException{
		JSONObject file;
		String fileName = "", content = "", type = "" , message = "", date = "", user = "";
		
		file = msg.getJSONObject("Arquivo");
		if(file.length() != 0){	
			fileName = file.getString("Nome");
			content = file.getString("Conteudo");
			type = file.getString("Tipo");
		}

		if(msg.length() != 0){
			message = msg.getString("Mensagem");
			date = msg.getString("DataHora");
			user = msg.getString("Usuario");
		}

		if(message.equals("/Sair")){
			bfw.write("Desconectado \r\n");
			txtHistory.append(user + " desconectado \r\n");
		}else if(message.contains("Arquivo enviado -->")){
			bfw.write("Arquivo enviado --> " + fileName + " (" + content + " bytes.)\r\n");
			txtHistory.append("Você: Arquivo enviado --> " + fileName + " (" + content + " bytes.) -- [ " + date + " ]\r\n");
		}else if(!message.equalsIgnoreCase("")){
			bfw.write(message + "\r\n");
			txtHistory.append("Você: " + message + " -- [ " + date + " ]\r\n");
		}
		bfw.flush();
		txtMsg.setText(""); 
	}

	public void sendMessage(String msg, JTextArea txtHistory, JTextField txtName, JTextField txtMsg) throws IOException{

		if(msg.equals("/Sair")){
			bfw.write("Desconectado \r\n");
			txtHistory.append(txtName.getText() + " desconectado \r\n");
		}else if(msg.equalsIgnoreCase("/Conectar")){
			bfw.write("Conectou \r\n");
			txtHistory.append(txtName.getText() + ": conectou\r\n");			
		}else if(!msg.equalsIgnoreCase("")){
			bfw.write(msg + "\r\n");
			txtHistory.append("Você: " + msg +"\r\n");
		}
		bfw.flush();
		txtMsg.setText("");  

	}


	public void sendFile(String fileName, JTextArea txtHistory, JTextField txtName, JTextField txtMsg) throws IOException{
		File file = new File (fileName);

		byte[] bytearray  = new byte [(int)file.length()];

		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));
		bin.read(bytearray,0,bytearray.length);
		DataOutputStream os = new DataOutputStream(socket.getOutputStream());
		
		try {
			sendMessage(buildMessage("Arquivo enviado --> ", txtName.getText(), file.getName(), file.length() + ""), txtHistory, txtName, txtMsg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//os.write(bytearray,0,bytearray.length);
		//os.flush();

	}

	public static void listen() throws IOException{

		InputStream in = socket.getInputStream();
		InputStreamReader inr = new InputStreamReader(in);
		BufferedReader bfr = new BufferedReader(inr);
		String msg = "";
		int i = 0;
		while(!msg.equalsIgnoreCase("/Sair"))
			if(bfr.ready()){
				msg = bfr.readLine();
				if(msg.equals("/Sair"))ClientInterface.getTxtHistory().append("Servidor caiu! \r\n");
				else ClientInterface.getTxtHistory().append(msg + "\r\n");         
			}
	}

	public void exit(JTextArea txtHistory, JTextField txtName, JTextField txtMsg, JButton btnConnect, JButton btnSend, JButton btnSendFile) throws IOException{

		sendMessage("/Sair", txtHistory, txtName, txtMsg);
		bfw.close();
		ouw.close();
		ou.close();
		socket.close();
		btnConnect.setEnabled(true);
		btnSend.setEnabled(false);
		txtMsg.setEnabled(false);
		btnSendFile.setEnabled(false);
		connected = false;
	}

	public static void main(String []args) throws IOException{
		new ClientInterface();

		new Thread(() -> {
			while(true){
				try {
					try {
						Thread.sleep(3);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
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







