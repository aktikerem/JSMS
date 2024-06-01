import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.lang.Thread;
public class clint{


        public static void main(String args[]) throws IOException, InterruptedException{
        final String smtpServer = "alt2.gmail-smtp-in.l.google.com";
        final int port = 25;
	final int msgID = (int) (Math.random()*100000) + 10000;
        EMAIL email = new EMAIL(smtpServer,port);

	email.PacketHandle("EHLO smtp.aktikerem.org\r\n");
	email.PacketHandle("MAIL FROM:<admin@aktikerem.org>\r\n");
	email.PacketHandle("RCPT TO:<" + args[0] + ">\r\n");
	email.PacketHandle("DATA\r\n");
	email.PacketHandle("From: admin@aktikerem.com\r\n");
	email.PacketHandle("To: " + args[0] + "\r\n");
	email.PacketHandle("Subject: "+args[1]+"\r\n");
	email.PacketHandle("Message-ID: <"+msgID+"@aktikerem.org>\r\n");
//	email.PacketHandle("Date: Tue, 21 May 2024 16:20:00 -0700\r\n ");
	email.PacketHandle("\r\n"+args[2]+"\r\n");
	email.PacketHandle(".\r\n");
	email.PacketHandle("QUIT");

	            

	}





}

class EMAIL{

private String smtpServer = new String();
private int port;
private Socket soc;
private PrintWriter writer;
private BufferedReader reader;
private int bufCountBytes;
private String check = "";

public EMAIL(String smtpServer, int port) throws IOException {
this.smtpServer = smtpServer;
this.port = port;
this.soc = new Socket(this.smtpServer,this.port);
this.writer = new PrintWriter(new OutputStreamWriter(soc.getOutputStream(), "UTF-8"), true);
this.reader = new BufferedReader(new InputStreamReader(soc.getInputStream(), "UTF-8"));
}



public void PacketHandle(String msg) throws IOException, InterruptedException {
  writer.print(msg);
  writer.flush();
  System.out.println(msg);

  handleIS();



}


public void handleIS() throws IOException, InterruptedException {

char[] buf = new char[512];
InputStreamReader ISR = new InputStreamReader(soc.getInputStream(), "UTF-8");
bufCountBytes += ISR.read(buf, 0,((soc.getInputStream().available())));
	
String cod = check;

while(cod.equals(check)){
cod = "";
Thread.sleep(100);
for(int i = 0; i<buf.length;i++){

cod += buf[i];
  }
}


if((cod.substring(0,1).equals("5")) || (cod.substring(0,1).equals("4")) || (cod.substring(0,1).equals("1"))){
System.out.println("equals '"+cod+"'");
throw new IOException("Server Err.");
}

if((cod.substring(0,1).equals("2")) || (cod.substring(0,1).equals("3"))){
check = cod;
}


System.out.println(cod);
cod = "";
}



}
