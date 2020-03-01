package resources;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ScannerThread implements Runnable {

    int seconds = 0;

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            seconds++;
            if (seconds == 15) {
                timer.cancel();
                this.cancel();
                try {
                    Robot r = new Robot();
                    r.keyPress(KeyEvent.VK_ENTER);
                } catch (AWTException e) {
                    //e.printStackTrace();
                }
                return;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }
    };

    @Override
    public void run() {
        System.out.println("Simulace vyberu kafe:");
        System.out.println("P=V   : Odeber vodu");
        if (Automat.stav == STAV.PLATIT_VIC) {
            System.out.println("P=A   : Beru rozměnění");
            System.out.println("P=N   : Neberu rozměnění");
        }
        else if (Automat.stav == STAV.VYDEJ_KELIMEK) {
            System.out.println("P=K   : Odeber kelímek");
            System.out.println("P=B   : Vrat penize");
        }
        else {
            if (Automat.stav != STAV.OHREV_VODY) System.out.println("P=C-x   : Simulace doslo kafe, kde X značí id kafe");
            System.out.println("P=B   : Vrat penize");
        }
        System.out.println("P=P   : Pokračovat ve výrobě");
        timer.scheduleAtFixedRate(task, 1000, 15);
        while(seconds < 15) {
            String akce = vratInput(Automat.sc);
            if (Automat.stav == STAV.PLATIT_VIC) {
                try {
                    if (akce.length() > 0) {
                        switch (akce.charAt(0)) {
                            case 'V': {
                                Automat.jeVoda = false;
                                seconds = 15;
                                timer.cancel();
                                break;
                            }
                            case 'N': {
                                Automat.stav = STAV.START;
                                seconds = 15;
                                timer.cancel();
                                break;
                            }
                            case 'A': {
                                Automat.stav = STAV.VYDEJ_KELIMEK;
                                seconds = 15;
                                timer.cancel();
                                break;
                            }
                            case 'P': {
                                seconds = 15;
                                timer.cancel();
                                break;
                            }
                            default: {
                                System.out.println("Neplatna akce");
                                break;
                            }
                        }
                    } else {
                        // System.out.println("Neplatna akce");
                    }
                } catch (Exception e) {

                }
            }
            else if (Automat.stav == STAV.VYDEJ_KELIMEK) {
                try {
                    if (akce.length() > 0) {
                        switch (akce.charAt(0)) {
                            case 'V': {
                                Automat.jeVoda = false;
                                seconds = 15;
                                timer.cancel();
                                break;
                            }
                            case 'K': {
                                Automat.jeKelimek = false;
                                seconds = 15;
                                timer.cancel();
                                break;
                            }
                            case 'B': {
                                Automat.stav = STAV.START;
                                seconds = 15;
                                timer.cancel();
                                break;
                            }
                            case 'P': {
                                seconds = 15;
                                timer.cancel();
                                break;
                            }
                            default: {
                                System.out.println("Neplatna akce");
                                break;
                            }
                        }
                    } else {
                        // System.out.println("Neplatna akce");
                    }
                } catch (Exception e) {

                }
            }
            else {
                try {
                    if (akce.length() > 0) {
                        switch (akce.charAt(0)) {
                            case 'V': {
                                Automat.jeVoda = false;
                                seconds = 15;
                                timer.cancel();
                                break;
                            }
                            case 'C': {
                                boolean odeber = Automat.odeberKafe(akce);
                                if (!odeber) {
                                    System.out.println("Neplatne ID");
                                } else {
                                    seconds = 15;
                                    timer.cancel();
                                }

                                break;
                            }
                            case 'B': {
                                Automat.stav = STAV.START;
                                seconds = 15;
                                timer.cancel();
                                break;
                            }
                            case 'P': {
                                seconds = 15;
                                timer.cancel();
                                break;
                            }
                            default: {
                                System.out.println("Neplatna akce");
                                break;
                            }
                        }
                    } else {
                        // System.out.println("Neplatna akce");
                    }
                } catch (Exception e) {

                }
            }
        }
        timer.cancel();
        return;
    }

    public static String vratInput(Scanner sc) {
        String akce = "";
        try {
            String tmp = sc.nextLine();
            String[] dotaz = tmp.split("=");
            if (dotaz.length > 1) akce = dotaz[1];
            return akce;
        }
        catch (Exception e){

        }
        return akce;
    }
}