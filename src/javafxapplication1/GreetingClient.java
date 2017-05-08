import java.io.*;
import java.net.*;
import javafxapplication1.SampleController_multi;
public class GreetingClient
{
    private static String serverName;
    public static SampleController_multi scm_controller;
   public static void main(String [] args)
   {
      String sName = "localhost";
      int port = 5000;
      try
      {
         System.out.println("Connecting to " + sName
                             + " on port " + port);
         Socket client = new Socket(sName, port);
         System.out.println("Just connected to "
                      + client.getRemoteSocketAddress());
         InputStream inFromServer = client.getInputStream();
         DataInputStream in =
                        new DataInputStream(inFromServer);
         System.out.println("Server says " + in.readUTF());
         OutputStream outToServer = client.getOutputStream();
         DataOutputStream out =
                       new DataOutputStream(outToServer);

         out.writeUTF(scm_controller.sendToServer);
         
         
         client.close();
      }catch(IOException e)
      {
      }
   }
}