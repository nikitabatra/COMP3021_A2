package javafxapplication1;
import java.net.*;
import java.io.*;
import static java.lang.Integer.parseInt;

public class GreetingServer extends Thread
{
    private static int port;
   private ServerSocket serverSocket;
   private String worldClock;
   public static int warriorNum;

   public GreetingServer(int port, String worldClock) throws IOException
   {
      serverSocket = new ServerSocket(port);
      this.worldClock = worldClock;
      warriorNum = -1;
   }

   public void run()
   {
      while(true)
      {
         try
         {
            System.out.println("Waiting for client on port " +
            serverSocket.getLocalPort() + "...");
            Socket server = serverSocket.accept();
            System.out.println("Just connected to "
                  + server.getRemoteSocketAddress());
//            DataOutputStream out =
//                 new DataOutputStream(server.getOutputStream());
//            out.writeUTF(worldClock);
            DataOutputStream out =
                 new DataOutputStream(server.getOutputStream());
            out.writeUTF("hello from the other side");
             DataInputStream in =
                  new DataInputStream(server.getInputStream());
            warriorNum = parseInt(in.readUTF());
             System.out.println(warriorNum);
            server.close();
         }catch(SocketTimeoutException s)
         {
            System.out.println("Socket timed out!");
            break;
         }catch(IOException e)
         {
            e.printStackTrace();
            break;
         }
      }
   }
   public static void main(String [] args)
   {
      port=6000;
      try
      {
         Thread t = new GreetingServer(port, "Test");
         t.start();
      }catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}