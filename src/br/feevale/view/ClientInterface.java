/*package br.feevale.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import br.feevale.cliente.Cliente;

public class ClientInterface extends JFrame implements ActionListener, KeyListener{
	private static final long serialVersionUID = 1L;
	private JTextArea txtHistory;
	private JTextField txtMsg;
	private JButton btnSend;
	private JButton btnExit;
	private JLabel lblUsuarios;
	private JPanel pnlContent;

	private JTextField txtIP;
	private JTextField txtPort;
	private JTextField txtName;
	private JScrollPane scroll;
	private JLabel lblPort;
	private JLabel lblIP;
	private JLabel lblNome;
	private JButton btnConnect;
	private boolean connected = false;
	private Cliente cli;

	public ClientInterface() throws IOException{                               
             
		cli = new Cliente();
		
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
		
		setLocationRelativeTo(null);
		setResizable(false);
		setSize(550,600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);	
		setTitle(txtName.getText());
		setContentPane(pnlContent);
}

	
	public JButton getBtnConnect() {
		return btnConnect;
	}


	public void setBtnConnect(JButton btnConnect) {
		this.btnConnect = btnConnect;
	}


	public JButton getBtnSend() {
		return btnSend;
	}


	public void setBtnSend(JButton btnSend) {
		this.btnSend = btnSend;
	}


	public JLabel getLblUsuarios() {
		return lblUsuarios;
	}


	public void setLblUsuarios(JLabel lblUsuarios) {
		this.lblUsuarios = lblUsuarios;
	}


	public JTextArea getTxtHistory() {
		return txtHistory;
	}

	public void setTxtHistory(JTextArea txtHistory) {
		this.txtHistory = txtHistory;
	}

	public JTextField getTxtMsg() {
		return txtMsg;
	}

	public void setTxtMsg(JTextField txtMsg) {
		this.txtMsg = txtMsg;
	}

	public JTextField getTxtIP() {
		return txtIP;
	}

	public void setTxtIP(JTextField txtIP) {
		this.txtIP = txtIP;
	}

	public JTextField getTxtPort() {
		return txtPort;
	}

	public void setTxtPort(JTextField txtPort) {
		this.txtPort = txtPort;
	}

	public JTextField getTxtName() {
		return txtName;
	}

	public void setTxtName(JTextField txtName) {
		this.txtName = txtName;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		try {
			if(ae.getActionCommand().equals(btnSend.getActionCommand())) cli.enviarMensagem(txtMsg.getText());
			else if(ae.getActionCommand().equals((btnConnect.getActionCommand()))) cli.connect(txtIP.getText(), Integer.parseInt(txtPort.getText()));
			else if(ae.getActionCommand().equals(btnExit.getActionCommand())) cli.sair();
		}catch (IOException e) {
			e.printStackTrace();
		}                       
	}

	@Override
	public void keyPressed(KeyEvent ke) {

		if(ke.getKeyCode() == KeyEvent.VK_ENTER){
			try {
				cli.enviarMensagem(txtMsg.getText());
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
	
}	
*/