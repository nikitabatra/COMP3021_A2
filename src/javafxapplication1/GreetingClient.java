package javafxapplication1;
import java.io.*;
import java.net.*;
import javafxapplication1.SampleController_multi_server;
public class GreetingClient
{
    private static String serverName;
    public static SampleController_multi_server scm_controller;
    
   public static void main(String [] args)
   {
      String sName = "localhost";
      int port = 6000;
      try
      {
         System.out.println("Connecting to " + sName
                             + " on port " + port);
         Socket client = new Socket(sName, port);
         client = new Socket(sName, port);
         System.out.println("Just connected to "
                      + client.getRemoteSocketAddress());
         InputStream inFromServer = client.getInputStream();
         DataInputStream in =
                        new DataInputStream(inFromServer);
         System.out.println("Server says " + in.readUTF());
         OutputStream outToServer = client.getOutputStream();
         DataOutputStream out =
                       new DataOutputStream(outToServer);

         //out.writeUTF(scm_controller.sendToServer);
         out.writeUTF("Hello from the Client Side!");
         System.out.println("Closing client now...");
         
         client.close();
         System.out.println("Is client closed? "+client.isClosed());
      }
      
      catch(IOException e)
      {
      }
      
   }
}