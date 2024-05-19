import java.io.*;
import java.net.*;
import java.util.Scanner;

public class clint{


        public static void main(String[] args) throws IOException{
        final String smtpServer = "alt2.gmail-smtp-in.l.google.com";
        final int port = 25;
        EMAIL email = new EMAIL(smtpServer,port);
	email.startReader();
	email.PacketHandle("EHLO smtp.gmail.com\r\n");
	email.PacketHandle("MAIL FROM:<admin@aktikerem.org>\r\n");
	email.PacketHandle("RCPT TO:<kerem.akti@gmail.com>\r\n");
	email.PacketHandle("DATA \r\n");
	email.PacketHandle("From: admin@aktikerem.org");
	//email.PacketHandle("\r\n.\r\n");

	            

	}





}

class EMAIL{

private String smtpServer = new String();
private int port;
private Socket soc;
private PrintWriter writer;
private BufferedReader reader;


public EMAIL(String smtpServer, int port) throws IOException {
this.smtpServer = smtpServer;
this.port = port;
this.soc = new Socket(this.smtpServer,this.port);
this.writer = new PrintWriter(new OutputStreamWriter(soc.getOutputStream(), "UTF-8"), true);
this.reader = new BufferedReader(new InputStreamReader(soc.getInputStream(), "UTF-8"));
}

public void PacketHandle(String msg) throws IOException{
  writer.print(msg);
  writer.flush();
  //System.outrintln(msg);  
}


public void startReader() {
        Thread readerThread = new Thread(() -> {
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.err.println("IOException caught: " + e.getMessage());
            }
        });
        readerThread.start();
    }
}
