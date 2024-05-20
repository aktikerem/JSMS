import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.lang.Thread;
public class clint{


        public static void main(String[] args) throws IOException, InterruptedException{
        final String smtpServer = "alt2.gmail-smtp-in.l.google.com";
        final int port = 25;
        EMAIL email = new EMAIL(smtpServer,port);
//	email.startReader();
	email.PacketHandle("EHLO smtp.gmail.com\r\n");
	Thread.sleep(500);
	email.PacketHandle("MAIL FROM:<admin@aktikerem.org>\r\n");
	Thread.sleep(500);
	email.PacketHandle("RCPT TO:<kerem.akti@gmail.com>\r\n");
	Thread.sleep(500);
	email.PacketHandle("DATA\r\n");
	Thread.sleep(500);
	email.PacketHandle("From: admin@aktikerem.com\r\n");
	Thread.sleep(500);
	email.PacketHandle("To: kerem.akti@gmail.com\r\n");
//	Thread.sleep(500);
//	email.PacketHandle("Subject: yopers ");
//	Thread.sleep(500);
//	email.PacketHandle("Message-ID: <2131400034@aktikerem.org> ");
//	Thread.sleep(500);
//	email.PacketHandle("Date: Mon, 20 May 2024 16:20:00 -0700 ");
//	Thread.sleep(500);
//	email.PacketHandle("\r\n.\r\n");
//	email.PacketHandle("To: kerem.akti@gmail.com\r\n");
//	email.PacketHandle("From: admin@aktikerem.org\r\nTo: kerem.akti@gmail.com\r\nSubject: dalugay\r\nMessage-ID: <243002962163@aktikerem.org>\r\nDate: Fri, 19 May 2024 16:20:00 -0700\r\n\r\ndali top fr\r\n.\r\n");
	//email.PacketHandle("DATA \r\n");
	//email.PacketHandle("\r\n.\r\n");
	//email.PacketHandle("\r\n.\r\n");

	            

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

public void PacketHandle(String msg) throws IOException{
  writer.print(msg);
  writer.flush();
  //System.outrintln(msg);
  handleIS();


}


public int handleIS() throws IOException{
byte[] buf = new byte[1024];
InputStream bufIS = soc.getInputStream();
bufIS.skip(bufCountBytes);
bufCountBytes += bufIS.read(buf);
for(int i = 0;i<buf.length;i++){
System.out.print(buf[i]+" ");
}
return 10;
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
