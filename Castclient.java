
import java.net.Socket;
import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;


public class Castclient {
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) {  
        int w = img.getWidth();  
        int h = img.getHeight();  
        BufferedImage dimg = new BufferedImage(newW, newH, img.getType());  
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
				BufferedImage resizedImage = resize(bufferedImage,
						(int)(Toolkit.getDefaultToolkit().getScreenSize().width*0.6),
						(int)(Toolkit.getDefaultToolkit().getScreenSize().height*0.6));
				
				    ImageIO.write(resizedImage, "jpg", output);
				    return output;
		}
	
		public static void main(String[] args) throws IOException {
			
			Socket Nosok = new Socket("192.168.0.103", 9010);
			DataInputStream port = new DataInputStream(Nosok.getInputStream());
			int castport = port.readInt();
			int viewport = port.readInt();
			Socket Cast = new Socket("192.168.0.103", castport);
			System.out.println("Port for cast: " + castport);
			System.out.println("Port for view: " + viewport);
			// На этой строчке бродкастер и вьювер подключены к созданной сессии
			DataInputStream allow = new DataInputStream(Cast.getInputStream());
			DataOutputStream sizeOut = new DataOutputStream(Cast.getOutputStream());
			OutputStream bytearrayOut = Cast.getOutputStream();
		
			
			boolean status;
			try {
				while(true) {
					status = allow.readBoolean();
					if(status) {
						byte[] bytearray = MkScrShot().toByteArray();
						sizeOut.writeInt(bytearray.length);
						bytearrayOut.write(bytearray);
						System.out.println(bytearray.length);
						try {
							Thread.sleep(20000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//
			Cast.close();
			Nosok.close();
		}
		
}