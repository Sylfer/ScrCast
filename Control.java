
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
 
 
public class Control implements Runnable {
 
 protected Socket clientSocket = null;
 
    public Control(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
 
    @Override
    public void run() {
    	
        try {
            InputStream input  = clientSocket.getInputStream();
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
            // В качестве аргумента стоит 0 для порта свободного порта
            ServerSocket forCast = new ServerSocket(0);
            ServerSocket forView = new ServerSocket(0);
            System.out.println("Successfully created serversocket's!");
            output.writeInt(forCast.getLocalPort());
            System.out.println("Port for cast: " + forCast.getLocalPort());
            output.writeInt(forView.getLocalPort());
            System.out.println("Port for view: " + forView.getLocalPort());
            
            new Thread(
                    new Session(forCast, forView)
                ).start();
            //
            output.close();
            input.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
