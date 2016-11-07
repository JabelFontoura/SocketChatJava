package br.feevale.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.json.JSONException;
import org.json.JSONObject;

import br.feevale.client.Client;

public class Server extends Thread {
	private static ArrayList<BufferedWriter>clientes;
	private static ServerSocket server;
	private static String name;
	private Socket con;
	private InputStream in;
	private FileOutputStream out;
	private InputStreamReader inr;
	private BufferedReader bfr;
	private Client cli;

	public Server(Socket con){
		this.con = con; 
		try { 
			in = con.getInputStream(); 
			inr = new InputStreamReader(in);
			bfr = new BufferedReader(inr); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
	}

	public void run(){
		try{
			String msg;
			JSONObject msgJSON;
			OutputStream ou = this.con.getOutputStream();
			Writer ouw = new OutputStreamWriter(ou);
			BufferedWriter bfw = new BufferedWriter(ouw);
			clientes.add(bfw);
			name = msg = bfr.readLine();
			
			while(!"Sair".equalsIgnoreCase(msg) && msg != null) {
				msg = bfr.readLine();
				cli = new Client();
				msgJSON = cli.buildMessage(msg, name);
				sendToAll(bfw, msgJSON);
				reciveFile(getClass().getResource( "/file.txt" ).toString().substring(5));
			} 
		}catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	public void reciveFile(String file) throws IOException{
		try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found. ");
        }
		
		 byte[] bytes = new byte[16*1024];
		
		 int count;
	        while ((count = in.read(bytes)) > 0) out.write(bytes, 0, count);
	}

	public void sendToAll(BufferedWriter bwExit, JSONObject msg) throws  IOException, JSONException {
		BufferedWriter bwS;
		
		String message = msg.getString("Mensagem");
		Object date = msg.getString("DataHora");
		//String user = msg.getString("Usuario");
		for(BufferedWriter bw : clientes){

			bwS = (BufferedWriter)bw;
			if(bwExit != bwS){
				if(msg != null){
					bw.write(name + ": " + message + " -- [ " + date + " ]\r\n");
				}
				bw.flush(); 
			}
		}          
	}

	public static void main(String []args) {
		try{
			JLabel lblMessage = new JLabel("Porta do Servidor:");
			JTextField txtPort = new JTextField("8888");
			Object[] texts = {lblMessage, txtPort };
			JOptionPane.showMessageDialog(null, texts);
			server = new ServerSocket(Integer.parseInt(txtPort.getText()));
			clientes = new ArrayList<BufferedWriter>();
			JOptionPane.showMessageDialog(null,"Servidor ativo na porta: "+ txtPort.getText());
			while(true){
				System.out.println("Aguardando conex�o...");
				Socket con = server.accept();
				System.out.println("Cliente conectado...");
				Thread t = new Server(con); t.start();
			}
		}catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showInternalMessageDialog(null, "Errou ao criar o servidor");
		}	
	}

} 



















































