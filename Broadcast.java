import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.applet.*;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.*;

 
public class Broadcast extends JApplet
{
	 private JPanel InfoPanel, AnswersPanel, ButtonPanel;
	 private JLabel IPlabel, FPlabel, PortView;
     private JButton button;
 	 private JTextField IP, FP;
 	 private JTextArea answers;
 	 
 	public static BufferedImage resize(BufferedImage img, int newW, int newH) {  
        int w = img.getWidth();  
        int h = img.getHeight();  
        BufferedImage dimg = dimg = new BufferedImage(newW, newH, img.getType());  
        Graphics2D g = dimg.createGraphics();  
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);  
        g.dispose();  
        return dimg;  
    }  
 	
	public static ByteArrayOutputStream MkScrShot() throws AWTException, IOException {
		//делаем скриншот экрана
		Robot robot = new Robot();
		BufferedImage bufferedImage = robot.createScreenCapture(
				new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		//сохраняем буферное изображение в выходной поток ByteArray
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		int height = (int)(1000*Toolkit.getDefaultToolkit().getScreenSize().height)/
					(Toolkit.getDefaultToolkit().getScreenSize().width);
		BufferedImage resizedImage = resize(bufferedImage, 1000, height);
		ImageIO.write(resizedImage, "gif", output);
		return output;
	}   

   public void init()
   {
	  //get the window/form surface--called a pane--that holds the buttons and other graphical content
	  Container contentHolder = getContentPane();

	  //indicate where the pane will go
	  contentHolder.setLayout(new BorderLayout(1,1));

      IP = new JTextField("", 9);
      IPlabel = new JLabel("IP-address:");
      FP = new JTextField("", 4);
      FPlabel = new JLabel("Port:");
      answers = new JTextArea("Wait...", 1, 5);
     
      InfoPanel = new JPanel();
      InfoPanel.add(IPlabel);
      InfoPanel.add(IP);
      InfoPanel.add(FPlabel);
      InfoPanel.add(FP);
      //add InfoPanel to Applet
	  contentHolder.add(InfoPanel, BorderLayout.NORTH);

	  ButtonPanel = new JPanel();
	  button = new JButton("Connect");
	  Color bg = new Color(247,141,29);
	  Color tt = new Color(255,255,255);
	  button.setBackground(bg);
	  button.setForeground(tt);
	  button.setMargin(new Insets(0, 25, 0, 25));
	  ButtonPanel.add(button);
	  contentHolder.add(ButtonPanel, BorderLayout.EAST);

	  AnswersPanel = new JPanel();
	  PortView = new JLabel("Viewers Port:");
      answers.setEditable(true);
      answers.setEnabled(true);
      answers.setBackground(Color.LIGHT_GRAY);
      answers.setMargin(new Insets(4, 2, 2, 2));
      //answers.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      AnswersPanel.add(PortView);
      AnswersPanel.add(answers);
      contentHolder.add(AnswersPanel, BorderLayout.WEST);
      
      
      button.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  broad();
    	  }
      });
    }

   public void look(int port){ 
	   answers.setText("" + port + "");
	   answers.update(answers.getGraphics()); 
   }
   
	public void broad() {
		boolean status;		

		try {			

			String IPadress = IP.getText();
			String fp = FP.getText();
			int FPort = Integer.parseInt(fp);
			Socket Nosok = new Socket(IPadress, FPort);
			DataInputStream port = new DataInputStream(Nosok.getInputStream());
			int castport = port.readInt();
			int viewport = port.readInt();
			look(viewport);
			Socket Cast = new Socket(IPadress, castport);
			System.out.println("Port for cast: " + castport);
			System.out.println("Port for view: " + viewport);
		    DataInputStream allow = new DataInputStream(Cast.getInputStream());
			DataOutputStream sizeOut = new DataOutputStream(Cast.getOutputStream());
			OutputStream bytearrayOut = Cast.getOutputStream();

			while(true) {
					status = allow.readBoolean();
					if(status) {
						byte[] bytearray = MkScrShot().toByteArray();
						sizeOut.writeInt(bytearray.length);
						bytearrayOut.write(bytearray);
						System.out.println(bytearray.length);
					}
			}
		} catch (AWTException e) {
			e.printStackTrace();
			answers.setText("");
		    answers.append("Aras1");
		    repaint();
		} catch (IOException e) {
			e.printStackTrace();
			answers.setText("");
		    answers.append("Aras2");
		    repaint();
		}
		}
}