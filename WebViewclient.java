import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;



public class WebViewclient extends JApplet {
    private JPanel InfoPanel, ScreenPanel, ButtonPanel;
 	private JLabel IpLabel, PortLabel, PicLabel; 
 	private JTextField IP, Port;
 	private JButton RunButton;
 	private final String ButtonText = "Start Viewing";
 	
 	public void paint(Graphics g, Image image) {
 		// Draw image
 	    //super.paintComponents(g);
 	    g.drawImage(image, 0, 0, this);
    }
 	
 	public void init() {	
 		//get the window/form surface--called a pane--that holds the buttons and other graphical content
 		Container contentHolder = getContentPane();

		//indicate where the pane will go
		contentHolder.setLayout(new BorderLayout(100,50));
 				
 		//creating Panels, Labels, Fields
 		InfoPanel = new JPanel();
 		IpLabel = new JLabel("IP:");
 		PortLabel = new JLabel("Port:");
 		IP = new JTextField("",9);
 		Port = new JTextField("",4);
 		
 		//add to InfoPanel
 		InfoPanel.add(IpLabel);
 		InfoPanel.add(IP);
 		InfoPanel.add(PortLabel);
 		InfoPanel.add(Port);
 		//InfoPanel.setBorder(BorderFactory.createLineBorder(Color.yellow));

 		
 		//add InfoPanel to Applet
 		contentHolder.add(InfoPanel, BorderLayout.CENTER);
        
		//add the ScreenPanel and PicLabel
// 		ScreenPanel = new JPanel();
// 		ScreenPanel.add(IpLabel);
// 		ScreenPanel.add(IP);
// 		ScreenPanel.add(PortLabel);
// 		ScreenPanel.add(Port);
//		contentHolder.add(ScreenPanel, BorderLayout.WEST);
//		ScreenPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		//make a panel for the button
		ButtonPanel = new JPanel();
		RunButton = new JButton(ButtonText);
		Color bg = new Color(100, 153, 30);
		Color tt = new Color(255,255,255);
		RunButton.setBackground(bg);
		RunButton.setForeground(tt);
		RunButton.setMargin(new Insets(0, 25, 0, 25));
		ButtonPanel.add(RunButton);
		contentHolder.add(ButtonPanel, BorderLayout.SOUTH);
		contentHolder.update(getGraphics());
		RunButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
    	  try {
			viewer();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
      }
      });
      
      
    }

	public void viewer() throws IOException {

		String ReceivedIP = IP.getText();
		String port = Port.getText();
		int ReceivedPort = Integer.parseInt(port);
		Socket View = new Socket(ReceivedIP, ReceivedPort);
		System.out.println("Established connection with server");
		//На этой строчке бродкастер и вьювер подключены к созданной сессии
		DataInputStream sizeIn = new DataInputStream(View.getInputStream());
		InputStream bytearrayIn = View.getInputStream( );
		DataOutputStream allow = new DataOutputStream(View.getOutputStream());

		boolean status = true;
		int size, i;
		byte [] bytearray;
		try{
			while(true) {
				allow.writeBoolean(status);
				status = false;
				size = sizeIn.readInt();
				// Get bytearray
				i = 0;
				bytearray = new byte[size];
				while(i < size) { 
					bytearray[i] = (byte) bytearrayIn.read();
					i++;
				}
				System.out.println(bytearray.length);

				//drawing image in Applet
				ImageIcon Icon = new ImageIcon(bytearray);
				//Icon.getImage();
			    //PicLabel.setIcon(Icon);
<<<<<<< HEAD

=======
			
>>>>>>> 38bbee32c42ad2540d867960461f7ae9c8011954
				paint(getGraphics(), Icon.getImage());

				allow.writeBoolean(status);
				status = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//
		View.close();
	}
}
