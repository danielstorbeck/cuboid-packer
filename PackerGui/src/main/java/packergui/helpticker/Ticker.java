package packergui.helpticker;

import javax.swing.JLabel;

public class Ticker implements Runnable {
    String mess;
    JLabel lab;
    String str;

    Ticker(String s, JLabel l) {
        mess = s;
        lab = l;
        str = "  " + mess.substring(0);
    }

    @Override
    public void run() {
        lab.setText(str);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            return;
        }
        while (!Thread.interrupted()) {
            while (str.length() < 500) {
                str = str + " *** " + mess.substring(0);
            }
            str = str.substring(1);
            lab.setText(str);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
