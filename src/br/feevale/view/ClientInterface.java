package br.feevale.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.JSONException;

import br.feevale.client.Client;

public class ClientInterface extends JFrame implements ActionListener, KeyListener{
	private static final long serialVersionUID = 1L;
	private static JTextArea txtHistory;
	private JTextField txtMsg;
	private JButton btnSend;
	private JButton btnExit;
	private JLabel lblUsers;
	private JPanel pnlContent;
	private JTextField txtIP;
	private JTextField txtPort;
	private JTextField txtName;
	private JScrollPane scroll;
	private JLabel lblPort;
	private JLabel lblIP;
	private JLabel lblName;
	private JButton btnConnect;
	private Client cli;

	public ClientInterface() throws IOException{                               

		cli = new Client();

		pnlContent = new JPanel();

		lblIP = new JLabel("IP:");
		lblIP.setBounds(10, 11, 46, 14);
		pnlContent.add(lblIP);

		txtIP = new JTextField();
		txtIP.setText("127.0.0.1");
		txtIP.setBounds(51, 8, 86, 20);
		pnlContent.add(txtIP);
		txtIP.setColumns(10);
		txtIP.addKeyListener(this);

		lblPort = new JLabel("Porta:");
		lblPort.setBounds(10, 41, 46, 14);
		pnlContent.add(lblPort);
		txtPort = new JTextField("8888");
		txtPort.setBounds(51, 39, 86, 20);
		pnlContent.add(txtPort);
		txtPort.setColumns(10);
		txtPort.addKeyListener(this);

		txtName = new JTextField();
		txtName.setBounds(51, 66, 86, 20);
		txtName.setColumns(10);
		txtName.addKeyListener(this);

		lblName = new JLabel("Nome:");
		lblName.setBounds(10, 72, 46, 14);
		pnlContent.add(lblName);
		pnlContent.add(txtName);
		setTitle(txtName.getText());

		btnConnect = new JButton("Conectar");
		btnConnect.setBounds(152, 7, 89, 79);
		btnConnect.addActionListener(this);
		pnlContent.add(btnConnect);

		txtHistory = new JTextArea(20,20);
		txtHistory.setEnabled(false);
		txtHistory.setEditable(false);
		txtHistory.setBackground(new Color(240,240,240));
		txtHistory.setLineWrap(true); 
		txtHistory.setBorder(BorderFactory.createEtchedBorder(Color.BLACK,Color.BLACK));

		txtMsg = new JTextField(20);
		txtMsg.setEnabled(false);
		txtMsg.setBounds(5, 514, 403, 46);
		txtMsg.addKeyListener(this);
		txtMsg.setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.BLACK)); 

		lblUsers = new JLabel("");
		lblUsers.setBounds(5, 97, 396, 22);

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
		pnlContent.add(lblUsers);
		pnlContent.add(scroll);
		pnlContent.add(txtMsg);
		pnlContent.add(btnExit);
		pnlContent.add(btnSend);
		pnlContent.add(txtIP);
		pnlContent.setBackground(Color.LIGHT_GRAY);  

		requestFocus(false);
		//setLocationRelativeTo(null);
		setResizable(false);
		setSize(550,600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);	
		setContentPane(pnlContent);
		
//		addWindowListener(new WindowAdapter(){
//            public void windowClosing(WindowEvent e){
//                int i=JOptionPane.showConfirmDialog(null, "Tem certeza que quer sair?");
//                if(i == 0){
//                	try {
//						cli.exit(txtHistory, txtName, txtMsg, btnConnect, btnSend);
//					} catch (IOException e1) {
//						e1.printStackTrace();
//					}
//                    System.exit(0);
//                }    
//            }
//        });
	}


	@Override
	public void actionPerformed(ActionEvent ae) {

		try {
			if(ae.getActionCommand().equals(btnSend.getActionCommand())){
				try {
					sendMessage();
				}catch (IOException | JSONException e) {
					e.printStackTrace();
				}
			}
			else if(ae.getActionCommand().equals((btnConnect.getActionCommand()))){
				if(!(txtName.getText().equals("") || txtIP.getText().equals("") || txtPort.getText().equals("")))
					cli.connect(txtIP.getText(), Integer.parseInt(txtPort.getText()), txtName, lblUsers, btnSend, txtMsg, txtHistory, btnConnect);
				else JOptionPane.showMessageDialog(this, "Prencha os campos necessários para conectar");	
				
			}
			else if(ae.getActionCommand().equals(btnExit.getActionCommand())) cli.exit(txtHistory, txtName, txtMsg, btnConnect, btnSend);
		}catch (IOException e) {
			e.printStackTrace();
		}                       
	}

	@Override
	public void keyPressed(KeyEvent ke){

		if((ke.getKeyCode() == KeyEvent.VK_ENTER) && (!cli.isConnected())){
			try{
				cli.connect(txtIP.getText(), Integer.parseInt(txtPort.getText()), txtName, lblUsers, btnSend, txtMsg, txtHistory, btnConnect);
			}catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}
		}else if(ke.getKeyCode() == KeyEvent.VK_ENTER && cli.isConnected()){
			try {
				sendMessage();
			}catch (IOException | JSONException e) {
				e.printStackTrace();
			}
		}	

		tabToTextFields(ke, txtIP);

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	public void sendMessage() throws IOException, JSONException{
		cli.sendMessage(cli.buildMessage(txtMsg.getText(), txtName.getText()), txtHistory, txtName, txtMsg);
	}

	public void tabToTextFields(KeyEvent ke, JTextField txt){
		if (ke.getKeyCode() == KeyEvent.VK_TAB) {
			if(ke.getModifiers() > 0) txt.transferFocusBackward();
			else txt.transferFocus();

			ke.consume();
		}
	}


	public static JTextArea getTxtHistory() {
		return txtHistory;
	}





}	
