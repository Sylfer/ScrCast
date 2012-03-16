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
     JButton button;
 	 JTextField IP;
 	 JLabel IPlabel;
 	 JTextField FP;
 	 JLabel FPlabel;
 	 JLabel ViewPort;
 	 JTextArea answers;
 	 
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
      setLayout(null);
      button = new JButton("Connect");
      IP = new JTextField("", 15);
      IPlabel = new JLabel("IP-address:");
      FP = new JTextField("", 4);
      FPlabel = new JLabel("First port:");
      ViewPort = new JLabel("ViewPort");
      answers = new JTextArea("Wait...");
      IP.setBounds(120,0,100,20);
      IPlabel.setBounds(20,0,100,20);
      FP.setBounds(120,30,40,20);
      FPlabel.setBounds(20,30,100,20);
      button.setBounds(40,90,100,30);
      ViewPort.setBounds(20,60,100,20);
      answers.setBounds(20, 140, 130, 20);
      button.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  broad();
    	  }
      });
      add(button);
      add(IP);
      add(IPlabel);
      add(FP);
      add(FPlabel);
      answers.setEditable(true);
      answers.setEnabled(true);
      answers.setBackground(Color.WHITE);
      answers.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      add(answers);
    }
	
   public void look(int port){ 
	   answers.setText("Port for view: " + port + "\n");
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