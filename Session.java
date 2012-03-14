
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
	 
	 
	public class Session implements Runnable {
	 
	 protected ServerSocket CastSocket  = null;
	 protected ServerSocket ViewSocket  = null;
	 
	    public Session(ServerSocket forCast, ServerSocket forView) {
	        this.CastSocket = forCast;
	        this.ViewSocket = forView;
	    }
	 
	    @Override
	    public void run() {
	    	 System.out.println("New session created!");
		     try {
		    	 Socket CastClient = CastSocket.accept();
		         Socket ViewClient = ViewSocket.accept();
		         System.out.println("Broadcaster is connected to: " + CastSocket.getLocalPort());
		         System.out.println("Viewer is connected to: " + ViewSocket.getLocalPort());
		        //Теперь начинаем регламентировать работу сессии, все подключены, всё готово
		         boolean status = true,
		        		 delivery;
		         int size, i;   
		         byte[] bytearray = null;
		         DataInputStream sizeIn  = new DataInputStream(CastClient.getInputStream());
		         InputStream  bytearrayIn = CastClient.getInputStream( );
		         DataOutputStream allow = new DataOutputStream(CastClient.getOutputStream());

		         DataInputStream deliver = new DataInputStream(ViewClient.getInputStream());
		         DataOutputStream sizeOut = new DataOutputStream(ViewClient.getOutputStream());
		     	 OutputStream bytearrayOut = ViewClient.getOutputStream();
			     try {
			    	 while(true) {
			    		 while(status) {
			    			 // Отправляем статус бродкастеру (разрешено отправить)
				             allow.writeBoolean(status);
				             size = sizeIn.readInt();
				         	 // Get bytearray
							 i = 0;
							 bytearray = new byte[size];
							 while(i < size) { 
								 bytearray[i] = (byte) bytearrayIn.read();
								 i++;
							 }
						     status = false;
				             allow.writeBoolean(status);	
				         }
			    		 delivery = deliver.readBoolean();
				         // Если можно отправлять вьюверу, то отправляем
				         if(delivery) {
				        	 sizeOut.writeInt(bytearray.length);
				        	 bytearrayOut.write(bytearray);
				         } else {
				        	 while(delivery) {
				        		 delivery = deliver.readBoolean();
				             }
				        	 sizeOut.writeInt(bytearray.length);
				        	 bytearrayOut.write(bytearray);
				         }
				         delivery = deliver.readBoolean();
				         if(!delivery) {
				        	 // Ждём сообщения о продолжении работы от вьювера
					         while(delivery) {
					        	 delivery = deliver.readBoolean();
				             }
				         }
				         status = true;
				         }
			    	 } catch (IOException e) {
					     System.out.println("Сессия завершёна!");
					     CastClient.close();
					     ViewClient.close();
						 e.printStackTrace();
					 }
		            //
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	    	//}
	    }
	}

	
