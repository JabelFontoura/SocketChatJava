package br.feevale.cliente;

import java.awt.Color;
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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import br.feevale.servidor.Servidor;

public class Cliente extends JFrame implements ActionListener, KeyListener {
	private static final long serialVersionUID = 1L;
	private static JTextArea txtHistory;
	private JTextField txtMsg;
	private JButton btnSend;
	private JButton btnExit;
	private JLabel lblUsuarios;
	private JPanel pnlContent;
	private static Socket socket;
	private OutputStream ou;
	private Writer ouw; 
	private BufferedWriter bfw;
	private JTextField txtIP;
	private JTextField txtPort;
	private JTextField txtName;
	private JScrollPane scroll;
	private JLabel lblPort;
	private JLabel lblIP;
	private JLabel lblNome;
	private JButton btnConnect;
	private static boolean connected = false;

	public Cliente() throws IOException{                               

		pnlContent = new JPanel();

		lblIP = new JLabel("IP:");
		lblIP.setBounds(10, 11, 46, 14);

		txtIP = new JTextField();
		txtIP.setText("127.0.0.1");
		txtIP.setBounds(51, 8, 86, 20);
		pnlContent.add(txtIP);
		txtIP.setColumns(10);

		lblPort = new JLabel("Porta:");
		lblPort.setBounds(10, 41, 46, 14);
		pnlContent.add(lblPort);
		txtPort = new JTextField("8888");
		txtPort.setBounds(51, 39, 86, 20);
		pnlContent.add(txtPort);
		txtPort.setColumns(10);

		lblNome = new JLabel("Nome:");
		lblNome.setBounds(10, 72, 46, 14);
		pnlContent.add(lblNome);

		txtName = new JTextField();
		txtName.setBounds(51, 66, 86, 20);
		txtName.setColumns(10);

		btnConnect = new JButton("Connect");
		btnConnect.setBounds(152, 7, 89, 79);
		btnConnect.addActionListener(this);
		pnlContent.add(btnConnect);

		txtHistory = new JTextArea(20,20);
		txtHistory.setEnabled(false);
		txtHistory.setEditable(false);
		txtHistory.setBackground(new Color(240,240,240));
		txtHistory.setLineWrap(true); 
		txtHistory.setBorder(BorderFactory.createEtchedBorder(Color.BLUE,Color.BLUE));

		txtMsg = new JTextField(20);
		txtMsg.setEnabled(false);
		txtMsg.setBounds(5, 514, 403, 46);
		txtMsg.addKeyListener(this);
		txtMsg.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE)); 

		lblUsuarios = new JLabel("");
		lblUsuarios.setBounds(5, 97, 396, 22);

		btnSend = new JButton("Enviar");
		btnSend.setEnabled(false);
		btnSend.setBounds(415, 514, 102, 46);
		btnSend.addActionListener(this);
		btnSend.addKeyListener(this);

		btnExit = new JButton("Sair");
		btnExit.setBounds(415, 478, 90, 25);
		btnExit.setToolTipText("Sair do Chat");	
		btnExit.addActionListener(this);

		scroll = new JScrollPane(txtHistory);
		scroll.setBounds(5, 130, 403, 366);

		pnlContent.setLayout(null);
		pnlContent.add(lblUsuarios);
		pnlContent.add(scroll);
		pnlContent.add(txtMsg);
		pnlContent.add(btnExit);
		pnlContent.add(btnSend);
		pnlContent.add(txtIP);
		pnlContent.add(txtName);
		pnlContent.add(lblIP);
		pnlContent.setBackground(Color.LIGHT_GRAY);  

		//setLocationRelativeTo(null);
		setResizable(false);
		setSize(550,600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);	
		setTitle(txtName.getText());
		setContentPane(pnlContent);

	}

	public void connect() throws IOException{      
		socket = new Socket(txtIP.getText(),Integer.parseInt(txtPort.getText()));
		ou = socket.getOutputStream();
		ouw = new OutputStreamWriter(ou);
		bfw = new BufferedWriter(ouw);
		bfw.write(txtName.getText()+"\r\n");
		lblUsuarios.setText("(" + txtName.getText() + ") " );
		bfw.flush();
		btnSend.setEnabled(true);
		txtMsg.setEnabled(true);
		txtHistory.setEnabled(true);
		btnConnect.setEnabled(false);

		connected= true;

	}

	public void enviarMensagem(String msg) throws IOException{

		if(msg.equals("Sair")){
			bfw.write("Desconectado \r\n");
			txtHistory.append(txtName.getText() + " desconectado \r\n");
		}else{
			bfw.write(msg + "\r\n");
			txtHistory.append( txtName.getText() + ": " + txtMsg.getText()+"\r\n");
		}
		bfw.flush();
		txtMsg.setText("");  

	}

	public static void escutar() throws IOException{

		InputStream in = socket.getInputStream();
		InputStreamReader inr = new InputStreamReader(in);
		BufferedReader bfr = new BufferedReader(inr);
		String msg = "";
		System.out.println("esc");
		while(!"Sair".equalsIgnoreCase(msg))

			if(bfr.ready()){
				msg = bfr.readLine();
				System.out.println("escutar" + msg);
				if(msg.equals("Sair"))txtHistory.append("Servidor caiu! \r\n");
				else txtHistory.append(msg + "\r\n");         
			}
	}

	public void sair() throws IOException{

		enviarMensagem("Sair");
		bfw.close();
		ouw.close();
		ou.close();
		socket.close();
		btnConnect.setEnabled(true);
		connected = false;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		try {
			if(ae.getActionCommand().equals(btnSend.getActionCommand())) enviarMensagem(txtMsg.getText());
			else if(ae.getActionCommand().equals((btnConnect.getActionCommand()))) connect();
			else if(ae.getActionCommand().equals(btnExit.getActionCommand())) sair();
		}catch (IOException e) {
			e.printStackTrace();
		}                       
	}

	@Override
	public void keyPressed(KeyEvent ke) {

		if(ke.getKeyCode() == KeyEvent.VK_ENTER){
			try {
				enviarMensagem(txtMsg.getText());
			} catch (IOException e) {
				e.printStackTrace();
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
		new Cliente();

		new Thread(() -> {
			while(true){
				try {
					System.out.println(connected);
					if(connected)escutar();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

	}	

}







