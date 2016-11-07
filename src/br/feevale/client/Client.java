package br.feevale.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

import java.io.File;

import br.feevale.view.ClientInterface;

public class Client {
	private static Socket socket;
	private static OutputStream ou;
	private static Writer ouw; 
	private static BufferedWriter bfw;
	private static boolean connected = false;
	private JSONObject obj;
	private JSONObject file;
	private DateFormat dateFormat;
	private FileInputStream fis;
	private BufferedInputStream bis;
	private FileOutputStream fos;
	private BufferedOutputStream bos;

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
	
	public JSONObject buildMessage(String msg, String name) throws JSONException{
		
		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		obj = new JSONObject();
		file = new JSONObject();
		file.put("Nome", "");
		file.put("Conteudo", "");
		file.put("Tipo", "");
		
		obj.put( "Mensagem", msg );
		obj.put( "DataHora", dateFormat.format(Calendar.getInstance().getTime()).toString());		
		obj.put( "Usuario", name);
		obj.put( "Arquivo",  file);
		
		return obj;
	}
	
	public void reciveFile(int fileSize, String file) throws IOException{
		int bytesRead = 0;
	    int current = 0;

	      // receive file
	      byte [] mybytearray  = new byte [fileSize];
	      InputStream is = socket.getInputStream();
	      fos = new FileOutputStream(file);
	      bos = new BufferedOutputStream(fos);
	      bytesRead = is.read(mybytearray,0,mybytearray.length);
	      current = bytesRead;

	      do{
	         bytesRead = is.read(mybytearray, current, (mybytearray.length-current));
	         if(bytesRead >= 0) current += bytesRead;
	      } while(bytesRead > -1);

	      bos.write(mybytearray, 0 , current);
	      bos.flush();
	      System.out.println("File " + file + " downloaded (" + current + " bytes read)");
	}
	
	public void sendFile(String fileLocal) throws IOException{

        // send file
		File file = new File(fileLocal);
        // Get the size of the file
        long length = file.length();
        byte[] bytes = new byte[16 * 1024];
        InputStream in = new FileInputStream(file);
        OutputStream out = socket.getOutputStream();

        int count;
        while ((count = in.read(bytes)) > 0) out.write(bytes, 0, count);
        

	}

	public void sendMessage(JSONObject msg, JTextArea txtHistory, JTextField txtName, JTextField txtMsg) throws IOException, JSONException{
		String message = msg.getString("Mensagem");
		Object date = msg.getString("DataHora");
		String user = msg.getString("Usuario");
		
		if(message.equals("/Sair")){
			bfw.write("Desconectado \r\n");
			txtHistory.append(user + ": desconectado \r\n");
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







