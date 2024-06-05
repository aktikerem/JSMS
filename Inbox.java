import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.Scanner;

class Inbox {

    private static final int port = 25; // Define the port number
    private static long lastTransId;

    public static void main(String[] args) throws FileNotFoundException{
      File sc = new File("lastTransId.txt");
      Scanner fReader = new Scanner(sc);
      String buf = fReader.nextLine();
      System.out.println(buf);
      lastTransId = Long.parseLong(buf);
      lastTransId++;
      System.out.println(lastTransId);
      fReader.close();

      while(true){	
	try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            // Wait for a client to connect
            Socket clientSocket = serverSocket.accept();
	    lastTransId++;
            System.out.println("Client connected");

            // Handle client connection
	    Thread thread = new Thread(() -> {
	    	try{
	    		EMAIL email = new EMAIL(clientSocket,lastTransId);
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
private long transId;


public EMAIL(Socket soc,long transId) throws IOException {
//this.smtpServer = smtpServer;
//this.port = port;
this.soc = soc;
this.writer = new PrintWriter(new OutputStreamWriter(soc.getOutputStream(), "UTF-8"), true);
this.reader = new BufferedReader(new InputStreamReader(soc.getInputStream(), "UTF-8"));
this.transId = transId;


}


public void PacketHandle(String msg,String cword) throws IOException, InterruptedException {
  writer.print(msg);
  writer.flush();
  System.out.println(msg);
  handleIS(cword);




}
public void PacketSend(String msg) throws IOException, InterruptedException {
  writer.print(msg);
  writer.flush();
  System.out.println(msg);




}
public void HandleTransId(){
  try {  
      
      FileWriter myWriter = new FileWriter("LastTransId.txt");
      myWriter.write(transId+"");
      myWriter.close();
     
   
  } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
  }




}

public void startHandling() throws IOException, InterruptedException {
  HandleTransId();

  PacketHandle("220 smtp.aktimail.com ESMTP 000000000-"+ transId +".220 - gsmtp :3 \r\n","EHLO");
  PacketSend("250-smtp.aktimail.com at ur service, ["+soc.getInetAddress().getHostAddress()+"] \r\n");
  PacketSend("250-SIZE 157286400\r\n");
  PacketSend("250-8BITMIME\r\n");
//  PacketSend("250-ENHANCEDSTATUSCODES \r\n");
//  PacketSend("250-PIPELINING\r\n");
//  PacketSend("250-CHUNKING\r\n");
  PacketHandle("250 SMTPUTF8\r\n","MAIL");
  PacketHandle("250 2.1.0 OK 000000000-"+ transId +".220 - gsmtp :3 \r\n","RCPT");
  PacketHandle("250 2.1.0 OK 000000000-"+ transId +".220 - gsmtp :3 \r\n","DATA");
  PacketHandle("354 Go ahead 000000000-"+ transId +".220 - gsmtp :3 \r\n","g");
  PacketHandle("250 2.1.0 OK 000000000-"+ transId +".220 - gsmtp :3 \r\n","g");
  PacketHandle("250 2.1.0 OK 000000000-"+ transId +".220 - gsmtp :3 \r\n","g");
 


}

public void handleIS(String cword) throws IOException, InterruptedException {

String cod = "";
int count = 0;
while(cod.indexOf(cword) == -1 && count < 7000){
cod = "";
Thread.sleep(10);
count +=10;
char[] buf = new char[4112];
InputStreamReader ISR = new InputStreamReader(soc.getInputStream(), "UTF-8");
ISR.read(buf, 0,((soc.getInputStream().available())));

for(int i = 0; i<buf.length;i++){

cod += buf[i];
  }


}
if(count >= 7000){
System.out.println("timout");
throw new IOException("Timeout reached");

}



System.out.println("here : "+cod);
cod = "";
}


}

class message{
private String Subject;
private String From;
private String To;
private String Body;
private String MsgId;
private String TransId;


public message(String Subject, String From, String To, String Body, String MsgId, String TransId){
this.Subject = Subject;
this.From = From;
this.To = To;
this.Body = Body;
this.MsgId = MsgId;
this.TransId = TransId.toString();



}

public String getSubject(){
return this.Subject;
}
public String getFrom(){
return this.From;
}

public String getTo(){
return this.To;
}

public String getBody(){
return this.Body;
}

public String getMsgId(){
return this.MsgId;
}

public String getTransId(){
return this.TransId;
}



}
