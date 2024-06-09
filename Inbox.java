import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;

import java.sql.*;

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


public String PacketHandle(String msg,String cword) throws IOException, InterruptedException {
  writer.print(msg);
  writer.flush();
  System.out.println(msg);
  return handleIS(cword);




}
public void PacketSend(String msg) throws IOException, InterruptedException {
  writer.print(msg);
  writer.flush();
  System.out.println(msg);




}
public void HandleTransId(){
  try {  
      
      FileWriter FW = new FileWriter("lastTransId.txt", false);
      FW.write(transId+"");
      FW.close();
     
   
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
  String FromData = PacketHandle("250 SMTPUTF8\r\n","MAIL");
  String ToData = PacketHandle("250 2.1.0 OK 000000000-"+ transId +".220 - gsmtp :3 \r\n","RCPT");
  PacketHandle("250 2.1.0 OK 000000000-"+ transId +".220 - gsmtp :3 \r\n","DATA");
  String Data = PacketHandle("354 Go ahead 000000000-"+ transId +".220 - gsmtp :3 \r\n","\r\n.\r\n");

  PacketHandle("250 2.1.0 OK 000000000-"+ transId +".220 - gsmtp :3 \r\n","QUIT");
  PacketSend("250 2.1.0 OK 000000000-"+ transId +".220 - gsmtp :3 \r\n");



  Pattern MSGID_Pattern = Pattern.compile("Message-ID: <(.*?)>");
  Pattern SUBJECT_Pattern = Pattern.compile("Subject: (.*?)\r\n");


  Matcher MSGID_Matcher = MSGID_Pattern.matcher(Data); 
  if (MSGID_Matcher.find()) {
    System.out.println("Message-ID: " + MSGID_Matcher.group(1));
  } else {
    System.out.println("Message-ID not found.");
  }

  Matcher SUBJECT_Matcher = SUBJECT_Pattern.matcher(Data);
  if (SUBJECT_Matcher.find()) {
    System.out.println("Subject: " + SUBJECT_Matcher.group(1));
  } else {
    System.out.println("Subject not found.");
  }

  String body = Data.substring(Data.indexOf("<div"),Data.lastIndexOf("div>")+4);
  if (body.length() >= 1) {
    System.out.println("Body: " + body) ;
  } else {
    System.out.println("Body not found.");
  }

  String From = FromData.substring(FromData.indexOf("<"),FromData.lastIndexOf(">")+1);
  if (From.length() >= 1) {
    System.out.println("From: " + From) ;
  } else {
    System.out.println("From not found.");
  }

  String To = ToData.substring(ToData.indexOf("<"),ToData.lastIndexOf(">")+1);
  if (To.length() >= 1) {
    System.out.println("To: " + To) ;
  } else {
    System.out.println("To not found.");
  }

  

  message rec_email = new message(SUBJECT_Matcher.group(1), From, To, body, MSGID_Matcher.group(1), (transId+""));
  rec_email.pushEmail();
   


}
public String handleIS(String cword) throws IOException, InterruptedException {

String cod = "";
int count = 0;
while(cod.indexOf(cword) == -1 && count < 7000){
Thread.sleep(10);
count +=10;
char[] buf = new char[15720];
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
return cod;

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


public void pushEmail() {
    String url = "jdbc:mariadb://172.232.207.186:3306/Inbox";
    String username = "ovhcreator";
    String password = "TqDMgdMV4yRvff@memedi";

    String sql = "INSERT INTO emails (`msg_id`, `subject`, `from`, `to`, `body`, `trans_id`) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection connection = DriverManager.getConnection(url, username, password);
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

        preparedStatement.setString(1, MsgId);
        preparedStatement.setString(2, Subject);
        preparedStatement.setString(3, From);
        preparedStatement.setString(4, To);
        preparedStatement.setString(5, Body);
        preparedStatement.setString(6, TransId);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Email pushed successfully.");
        } else {
            System.out.println("Failed to push email.");
        }

    } catch (SQLException e) {
        System.out.println(e);
    }
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
