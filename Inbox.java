import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

class Inbox {

    private static final int port = 25; // Define the port number

    public static void main(String[] args) {
      while(true){	
	try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            // Wait for a client to connect
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            // Handle client connection
	    Thread thread = new Thread(() -> {
	    	try{
	    		EMAIL email = new EMAIL(clientSocket);
					email.startHandling();
	 	}
	    	catch(IOException | InterruptedException ez){
	    		System.err.println("IOException occurred: " + ez.getMessage());	   
	   		}
	 	 });
		 thread.start();
     
		 } catch (IOException e) {
           		System.out.println("Server exception: " + e.getMessage());
            		e.printStackTrace();
        }
    }
    
}
}

class EMAIL{

//private String smtpServer = new String();
//private int port;
private Socket soc;
private PrintWriter writer;
private BufferedReader reader;


public EMAIL(Socket soc) throws IOException {
//this.smtpServer = smtpServer;
//this.port = port;
this.soc = soc;
this.writer = new PrintWriter(new OutputStreamWriter(soc.getOutputStream(), "UTF-8"), true);
this.reader = new BufferedReader(new InputStreamReader(soc.getInputStream(), "UTF-8"));



}


public void PacketHandle(String msg) throws IOException, InterruptedException {
  writer.print(msg);
  writer.flush();
  System.out.println(msg);
  handleIS();




}
public void PacketSend(String msg) throws IOException, InterruptedException {
  writer.print(msg);
  writer.flush();
  System.out.println(msg);




}

public void startHandling() throws IOException, InterruptedException {

  PacketHandle("220 smtp.aktimail.com ESMTP d75a77b69052e-43ff43d92c0si52553091cf.220 - gsmtp :3 \r\n");
  PacketSend("250-smtp.aktimail.com at ur service, ["+soc.getInetAddress().getHostAddress()+"] \r\n");
  PacketSend("250-SIZE 157286400\r\n");
  PacketSend("250-8BITMIME\r\n");
//  PacketSend("250-ENHANCEDSTATUSCODES \r\n");
//  PacketSend("250-PIPELINING\r\n");
//  PacketSend("250-CHUNKING\r\n");
  PacketHandle("250 SMTPUTF8\r\n");
  PacketHandle("250 2.1.0 OK d75a77b69052e-43ff43d92c0si52553091cf.220 - gsmtp :3 \r\n");
  PacketHandle("250 2.1.0 OK d75a77b69052e-43ff43d92c0si52553091cf.220 - gsmtp :3 \r\n");
  PacketHandle("354 Go ahead ffacd75a77b69052e-43ff43d92c0si52553091cf.220 - gsmtp :3 \r\n");
  PacketHandle("250 2.1.0 OK d75a77b69052e-43ff43d92c0si52553091cf.220 - gsmtp :3 \r\n");
  PacketHandle("250 2.1.0 OK d75a77b69052e-43ff43d92c0si52553091cf.220 - gsmtp :3 \r\n");
 



}

public void handleIS() throws IOException, InterruptedException {

String cod = "";

//while(cod.indexOf("g") == -1){
cod = "";
Thread.sleep(1000);
char[] buf = new char[4112];
InputStreamReader ISR = new InputStreamReader(soc.getInputStream(), "UTF-8");
ISR.read(buf, 0,((soc.getInputStream().available())));

for(int i = 0; i<buf.length;i++){

cod += buf[i];
  }


//}


if((cod.substring(0,1).equals("5")) || (cod.substring(0,1).equals("4")) || (cod.substring(0,1).equals("1"))){
System.out.println("equals '"+cod+"'");
throw new IOException("Server Err.");
}


System.out.println("here : "+cod);
cod = "";
}


}
