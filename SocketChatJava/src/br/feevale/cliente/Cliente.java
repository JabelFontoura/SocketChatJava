package br.feevale.cliente;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import javax.swing.*;

public class Cliente extends JFrame implements ActionListener, KeyListener {
	private static final long serialVersionUID = 1L;
	private JTextArea txtHistorico;
	private JTextField txtMsg;
	private JButton btnSend;
	private JButton btnSair;
	private JLabel lblUsuarios;
	private JPanel pnlContent;
	private Socket socket;
	private OutputStream ou;
	private Writer ouw; 
	private BufferedWriter bfw;
	private JTextField txtIP;
	private JTextField txtPorta;
	private JTextField txtNome;
	private JScrollPane scroll;

	public Cliente() throws IOException{                  
		JLabel lblMessage = new JLabel("Verificar!");
		txtIP = new JTextField("127.0.0.1");
		txtPorta = new JTextField("");
		txtNome = new JTextField("Cliente");                
		Object[] texts = {lblMessage, txtIP, txtPorta, txtNome };  
		JOptionPane.showMessageDialog(null, texts);              
		pnlContent = new JPanel();
		txtHistorico = new JTextArea(20,20);
		txtHistorico.setEditable(false);
		txtHistorico.setBackground(new Color(240,240,240));
		txtMsg = new JTextField(20);
		txtMsg.setBounds(5, 412, 403, 46);
		lblUsuarios = new JLabel("");
		lblUsuarios.setBounds(12, 10, 396, 22);
		btnSend = new JButton("Enviar");
		btnSend.setBounds(415, 412, 102, 46);
		btnSend.setToolTipText("");
		btnSair = new JButton("Sair");
		btnSair.setBounds(427, 374, 90, 25);
		btnSair.setToolTipText("Sair do Chat");
		btnSend.addActionListener(this);
		btnSair.addActionListener(this);
		btnSend.addKeyListener(this);
		txtMsg.addKeyListener(this);
		scroll = new JScrollPane(txtHistorico);
		scroll.setBounds(5, 35, 403, 366);
		txtHistorico.setLineWrap(true);  
		pnlContent.setLayout(null);
		pnlContent.add(lblUsuarios);
		pnlContent.add(scroll);
		pnlContent.add(txtMsg);
		pnlContent.add(btnSair);
		pnlContent.add(btnSend);
		pnlContent.setBackground(Color.LIGHT_GRAY);                                 
		txtHistorico.setBorder(BorderFactory.createEtchedBorder(Color.BLUE,Color.BLUE));
		txtMsg.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));                    
		setTitle(txtNome.getText());
		setContentPane(pnlContent);
		setLocationRelativeTo(null);
		setResizable(false);
		setSize(550,500);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	/***
	 * Método usado para conectar no server socket, retorna IO Exception caso dê algum erro.
	 * @throws IOException
	 */
	public void conectar() throws IOException{                       
		socket = new Socket(txtIP.getText(),Integer.parseInt(txtPorta.getText()));
		ou = socket.getOutputStream();
		ouw = new OutputStreamWriter(ou);
		bfw = new BufferedWriter(ouw);
		bfw.write(txtNome.getText()+"\r\n");
		lblUsuarios.setText("(asdasd" );
		bfw.flush();
	}

	/***
	 * Método usado para enviar mensagem para o server socket
	 * @param msg do tipo String
	 * @throws IOException retorna IO Exception caso dê algum erro.
	 */
	public void enviarMensagem(String msg) throws IOException{

		if(msg.equals("Sair")){
			bfw.write("Desconectado \r\n");
			txtHistorico.append("Desconectado \r\n");
		}else{
			bfw.write(msg+"\r\n");
			txtHistorico.append( txtNome.getText() + ": " + txtMsg.getText()+"\r\n");
		}
		bfw.flush();
		txtMsg.setText("");        
	}

	/**
	 * Método usado para receber mensagem do servidor
	 * @throws IOException retorna IO Exception caso dê algum erro.
	 */
	public void escutar() throws IOException{

		InputStream in = socket.getInputStream();
		InputStreamReader inr = new InputStreamReader(in);
		BufferedReader bfr = new BufferedReader(inr);
		String msg = "";

		while(!"Sair".equalsIgnoreCase(msg))

			if(bfr.ready()){
				msg = bfr.readLine();
				if(msg.equals("Sair"))
					txtHistorico.append("Servidor caiu! \r\n");
				else
					txtHistorico.append(msg+"\r\n");         
			}
	}

	/***
	 * Método usado quando o usuário clica em sair
	 * @throws IOException retorna IO Exception caso dê algum erro.
	 */
	public void sair() throws IOException{

		enviarMensagem("Sair");
		bfw.close();
		ouw.close();
		ou.close();
		socket.close();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		try {
			if(e.getActionCommand().equals(btnSend.getActionCommand()))
				enviarMensagem(txtMsg.getText());
			else
				if(e.getActionCommand().equals(btnSair.getActionCommand()))
					sair();
		} catch (IOException e1) {
			e1.printStackTrace();
		}                       
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			try {
				enviarMensagem(txtMsg.getText());
			} catch (IOException e1) {
				e1.printStackTrace();
			}                                                          
		}                       
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

	public static void main(String []args) throws IOException{
		Cliente app = new Cliente();
		app.conectar();
		app.escutar();
	} 
}







