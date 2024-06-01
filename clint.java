import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.lang.Thread;
public class clint{


        public static void main(String args[]) throws IOException, InterruptedException{
        EMAIL email = new EMAIL("alt2.gmail-smtp-in.l.google.com",25);
	email.sendEmail(args[0],args[1],args[2]);
	            

	}





}

class EMAIL{

private String smtpServer = new String();
private int port;
private Socket soc;
private PrintWriter writer;
private BufferedReader reader;
private int bufCountBytes;


public EMAIL(String smtpServer, int port) throws IOException {
this.smtpServer = smtpServer;
this.port = port;
this.soc = new Socket(this.smtpServer,this.port);
this.writer = new PrintWriter(new OutputStreamWriter(soc.getOutputStream(), "UTF-8"), true);
this.reader = new BufferedReader(new InputStreamReader(soc.getInputStream(), "UTF-8"));



}


public void sendEmail(String address,String subject,String body) throws IOException, InterruptedException{

	final int msgID = (int) (Math.random()*1000000) + 10000;

        PacketHandle("EHLO smtp.aktikerem.org\r\n");
        PacketHandle("MAIL FROM:<admin@aktikerem.org>\r\n");
        PacketHandle("RCPT TO:<" + address + ">\r\n");
        PacketHandle("DATA\r\n");
        PacketHandle("From: admin@aktikerem.com\r\n");
        PacketSend("To: " +address+ "\r\n");
        PacketSend("Subject: "+subject+"\r\n");
        PacketSend("Message-ID: <"+msgID+"@aktikerem.org>\r\n");
        PacketSend("\r\n"+body+"\r\n");
        PacketSend(".\r\n");
        PacketHandle("QUIT");


}

public void PacketHandle(String msg) throws IOException, InterruptedException {
  handleIS();
  writer.print(msg);
  writer.flush();
  System.out.println(msg);




}
public void PacketSend(String msg) throws IOException, InterruptedException {
  writer.print(msg);
  writer.flush();
  System.out.println(msg);




}



public void handleIS() throws IOException, InterruptedException {

String cod = "";

while(cod.indexOf("2") == -1){
cod = "";
Thread.sleep(10);
char[] buf = new char[512];
InputStreamReader ISR = new InputStreamReader(soc.getInputStream(), "UTF-8");
bufCountBytes += ISR.read(buf, 0,((soc.getInputStream().available())));


for(int i = 0; i<buf.length;i++){

cod += buf[i];
  }
}


if((cod.substring(0,1).equals("5")) || (cod.substring(0,1).equals("4")) || (cod.substring(0,1).equals("1"))){
System.out.println("equals '"+cod+"'");
throw new IOException("Server Err.");
}


System.out.println("here : "+cod);
cod = "";
}






}
