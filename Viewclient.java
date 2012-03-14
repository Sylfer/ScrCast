
import java.net.Socket;
import java.io.*;


public class Viewclient {
	public static void main(String[] args) throws IOException {
		// Указываем адрес порта
		int viewport = 57219;
		Socket View = new Socket("192.168.1.3", viewport);
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