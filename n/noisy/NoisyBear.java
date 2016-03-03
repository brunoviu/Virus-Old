/*  NoisyBear.java by Mark D. LaDue */

/*  February 15, 1996 */

/*  Copyright (c) 1996 Mark D. LaDue
    You may study, use, modify, and distribute this example for any purpose.
    This example is provided WITHOUT WARRANTY either expressed or implied.  */

/*  This Java Applet displays a stupid looking bear with a clock
    superimposed on his belly.  It refuses to shut up until you quit
    the browser.  */

import java.applet.AudioClip;
import java.awt.*;
import java.util.Date;

public class NoisyBear extends java.applet.Applet implements Runnable {
    Font timeFont = new Font("TimesRoman", Font.BOLD, 24);
    Font wordFont = new Font("TimesRoman", Font.PLAIN, 12);
    Date rightNow;
    Thread announce = null;
    Image bearImage;
    Image offscreenImage;
    Graphics offscreenGraphics;
    AudioClip annoy;
    boolean threadStopped = false;

    public void init() {
    bearImage = getImage(getCodeBase(), "Pictures/sunbear.jpg");
    offscreenImage = createImage(this.size().width, this.size().height);
    offscreenGraphics = offscreenImage.getGraphics();
    annoy = getAudioClip(getCodeBase(), "Sounds/drum.au");    
}

    public void start() {
        if (announce == null) {
        announce = new Thread(this);
        announce.start();
        }
    }

    public void stop() {
        if (announce != null) {
        //if (annoy != null) annoy.stop();  //uncommenting stops the noise
        announce.stop();
        announce = null;
        }
    }

    public void run() {
        if (annoy != null) annoy.loop();
        while (true) {
        rightNow = new Date();
        repaint();
        try { Thread.sleep(1000); }
        catch (InterruptedException e) {}
        }
    }

    public void update(Graphics g) {
//        g.clipRect(125, 150, 350, 50);
        paint(g);
    }

    public void paint(Graphics g) {
        int imwidth = bearImage.getWidth(this);
        int imheight = bearImage.getHeight(this);

     offscreenGraphics.drawImage(bearImage, 0, 0, imwidth, imheight, this);
     offscreenGraphics.setColor(Color.white);
     offscreenGraphics.fillRect(125, 150, 350, 100);
     offscreenGraphics.setColor(Color.blue);
     offscreenGraphics.drawRect(124, 149, 352, 102);
     offscreenGraphics.setFont(timeFont);
     offscreenGraphics.drawString(rightNow.toString(), 135, 200);
     offscreenGraphics.setFont(wordFont);
     offscreenGraphics.drawString("It's time for me to annoy you!", 135, 225);
     g.drawImage(offscreenImage, 0, 0, this);
    }

    public boolean mouseDown(Event evt, int x, int y) {
        if (threadStopped) {
            announce.resume();
        }
        else {
            announce.suspend();
        }
        threadStopped = !threadStopped;
        return true;
    }
}
