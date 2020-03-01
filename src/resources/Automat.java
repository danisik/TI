package resources;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Automat {
    /**
     * Pole prijatelnych minci
     */
    private static resources.MINCE[] Mince = {resources.MINCE.JEDNA, resources.MINCE.DVA, resources.MINCE.PET, resources.MINCE.DESET, resources.MINCE.DVACET, resources.MINCE.PADESAT};
    /**
     * Vybrane kafe
     */
    private static resources.Kafe kafe;
    /**
     * Peněženka pro automat
     */
    public static int penezenka = 0;
    /**
     * pomocna penezenka, pouzita pri vraceni penez kdyz uzivatel stihne stlacit tlacitko jeste predtim, nez se ohreje voda
     */
    public static int penezenkaBezOdecitani = 0;
    /**
     * Scanner pro zjištění vstupu z konzole
     */
    public static Scanner sc = new Scanner(System.in);
    /**
     * dostupné výrobky v automatu
     */
    private static Map<Integer, resources.Kafe> vyrobky = new HashMap<>();
    /**
     * indikator, zda nazacatku bylo vypsano info
     */
    private static boolean infoVypsano = false;
    /**
     * Cyklus vydani kavy je v konci (CK0-CK1)
     */
    private static boolean konecCyklu = false;

    /**
     * Je dostatecne mnozstvi vody v automatu (C0V-C1V)
     */
    public static boolean jeVoda = true;
    /**
     * Je dostatecne mnozstvi cukru v automatu (C0C-C1C)
     */
    public static boolean jeCukr = true;
    /**
     * Je dostatecne mnozstvi kelimku v automatu (C0K-C1K)
     */
    public static boolean jeKelimek = true;
    /**
     * Je dostatecne mnozstvi urciteho kafe v automatu (C0KA-C1KA)
     */
    private static boolean jeKafe = true;
    /**
     * Vypise chybu o nedostatku vody v zasobniku
     */
    private static boolean infoVoda = false;
    /**
     * Vypise chybu o nedostatku cukru
     */
    private static boolean infoCukr = false;
    /**
     * Vypise chybu o nedostatku kelimku
     */
    private static boolean infoKelimek = false;

    /**
     * Vrati vsechny mince, koupe zrusena (CB0-CB1)
     */
    private static boolean vratMinceVse = false;

    /**
     * Zjistuje zda se stisklo tlacitko (CT0-CT1)
     */
    private static boolean zmacknuteTlacitko = false;

    /**
     * Automat dava info, zda ma na rozmeneni ci ne (CA0-CA1)
     */
    private static boolean mamNaRozmenit = true;
    /**
     * Pokud automat nema na rozmeneni, zjistujeme zda to kupujicimu nevadi (CBE0-CBE1)
     */
    private static boolean beruRozmenit = true;

    /**
     * Zda kelimek dopadl na sve misto (CIDLO ZAJISTI)
     */
    private static boolean kelimekNaMiste = true;
    /**
     * Zjistujeme, zda je voda dostatecne ohrata (COH0-COH1)
     */
    private static boolean ohrevVody = false;
    /**
     * Zjistujeme, zda se odsypalo kafe (COD0-COD1)
     */
    private static boolean odsypKavy = false;
    /**
     * Zjistujeme, zda se odlilo vsechno kafe do kelimku (CO0-CO1)
     */
    private static boolean odliti = false;
    /**
     * Zjistujeme, zda se vratil zbytek (CV0-CV1)
     */
    private static boolean vratitZbytek = false;
    /**
     * Zjistujeme, zda se kafe odebralo z vydavaci plochy (CVY0-CVY1)
     */
    private static boolean vydejKafe = false;
    /**
     * Informace, v jakem stavu se prave nachazime
     */
    public static resources.STAV stav = resources.STAV.START;
    /**
     * Informace, jaka akce se ma provest
     */
    public static resources.ACTION action = resources.ACTION.NEPLATNA_AKCE;
    /**
     * Pomocna stringova promenna pro ulozeni hodnoty
     */
    private static String pom = "";
    /**
     * Pomocna promenna pro zajisteni ulozeni ID kafe
     */
    private static int id_KAFE;

    public static void main(String[] args) throws InterruptedException {
        naplnVyrobky();
        while (true) {
            switch (stav) {
                case START: {
                    if (!jeVoda || !jeKelimek || !jeCukr) {
                        stav = resources.STAV.SERVIS;
                        break;
                    }
                    if (!infoVypsano) {
                        vypisInfo();
                        System.out.println();
                        vypisPouzivani();
                        System.out.println();
                    }
                    System.out.println("Aktualni stav: " + stav.toString());
                    String akce = vratInput(sc);
                    zjistiAkci(akce);
                    provedAkci(akce);
                    System.out.println("Aktuální stav peněženky = " + penezenka);
                    break;
                }
                case SERVIS: {
                    vypisInfoChybovy();
                    vypisChybovy();
                    System.out.println("Aktualni stav: " + stav.toString());
                    String akce = vratInput(sc);
                    zjistiAkciChybovy(akce);
                    provedAkci(akce);
                    System.out.println("Aktuální stav peněženky = " + penezenka);
                    break;
                }
                case PENEZENKA: {
                    if (!jeVoda || !jeKelimek || !jeCukr) {
                        stav = resources.STAV.SERVIS;
                    }
                    System.out.println("Aktualni stav: " + stav.toString());
                    String akce = vratInput(sc);
                    zjistiAkci(akce);
                    provedAkci(akce);
                    System.out.println("Aktuální stav peněženky = " + penezenka);
                    break;
                }
                case NAPOJ_ZVOLEN: {
                    action = resources.ACTION.KUP_KAFE;
                    System.out.println("Aktualni stav: " + stav.toString());
                    provedAkci(pom);
                    break;
                }
                case ROZMENENI: {
                    action = resources.ACTION.MAM_ROZMENENI;
                    System.out.println("Aktualni stav: " + stav.toString());
                    provedAkci("");
                    break;
                }
                case PLATIT_VIC: {
                    action = resources.ACTION.CHCI_PLATIT_VIC;
                    System.out.println("Aktualni stav: " + stav.toString());
                    provedAkci("");
                    break;
                }
                case VYDEJ_KELIMEK: {
                    action = resources.ACTION.VYDANI_KELIMKU;
                    System.out.println("Aktualni stav: " + stav.toString());
                    provedAkci("");
                    break;
                }
                case OHREV_VODY: {
                    action = resources.ACTION.OHREVANI_VODY;
                    System.out.println("Aktualni stav: " + stav.toString());
                    provedAkci("");
                    break;
                }
                case ODSYP_KAVY: {
                    action = resources.ACTION.ODSYPANI_KAVY;
                    System.out.println("Aktualni stav: " + stav.toString());
                    provedAkci("");
                    break;
                }
                case ODLITI_NAPOJE: {
                    action = resources.ACTION.ODLEVANI;
                    System.out.println("Aktualni stav: " + stav.toString());
                    provedAkci("");
                    break;
                }
                case VRATIT_ZBYTEK: {
                    action = resources.ACTION.VRAT_ZBYTEK;
                    System.out.println("Aktualni stav: " + stav.toString());
                    provedAkci("");
                    break;
                }
                case VYDEJ_KAFE: {
                    action = resources.ACTION.VYDANI_KAFE;
                    System.out.println("Aktualni stav: " + stav.toString());
                    provedAkci("");
                    break;
                }
                case VRATIT_PENIZE: {
                    action = resources.ACTION.VRAT_VSECHNY_PENIZE;
                    System.out.println("Aktualni stav: " + stav.toString());
                    provedAkci("");
                    break;
                }
            }
        }
    }

    /**
     * Provedeni pozadovane akce
     */
    public static void provedAkci(String akce) throws InterruptedException {
        switch (action) {
            case PRIDEJ_MINCI: {
                try {
                    boolean platnaMince = pridejDoKonta(akce);
                    if (!platnaMince) {
                        System.out.println("Neplatna mince");
                    }
                }
                catch (Exception e) {
                    System.out.println("Neplatna mince");
                }
                break;
            }

            case VYPIS_MINCE: {
                vypisPoctuMinci();
                break;
            }

            case VYPIS_DOSTUPNOST: {
                vypisDostupnostiKafe();
                break;
            }

            case KUP_KAFE: {
                try {
                    id_KAFE = Integer.parseInt(vratHodnotu(akce, "-"));
                }
                catch (Exception e) {

                }
                if (!vyrobky.containsKey(id_KAFE)) {
                    System.out.println("Neplatne ID KAFE");
                    stav = STAV.PENEZENKA;
                    break;
                }
                else if (!vyrobky.get(id_KAFE).isDostupne()) {
                    System.out.println("Omlouvame se, ale toto kafe neni dostupne");
                    stav = STAV.PENEZENKA;
                    break;
                }
                else if (penezenka < vyrobky.get(id_KAFE).getCena()) {
                    System.out.println("Nemate dostatek penez na nakup tohoto kafe");
                    stav = STAV.PENEZENKA;
                    break;
                }
                Thread scannerThread = new Thread(new resources.ScannerThread());
                scannerThread.start();
                //smycka co pozastavuje aktualni thread, kontroluje zda scannerThread stale jede nebo ne
                while(scannerThread.isAlive()) {

                }
                scannerThread.interrupt();

                if (!jeVoda || stav == resources.STAV.START) {
                    vratVsechnyPenize(penezenkaBezOdecitani);
                    break;
                }

                boolean kupuju = kupujuKafe(id_KAFE);
                if (!kupuju) {
                    stav = resources.STAV.PENEZENKA;
                } else {
                    stav = resources.STAV.ROZMENENI;
                }
                break;
            }

            case VYPIS_POUZIVANI: {
                vypisPouzivani();
                break;
            }

            case ODEBER_VODU: {
                action = resources.ACTION.ODEBER_VODU;
                jeVoda = false;
                break;
            }
            case ODEBER_CUKR: {
                jeCukr = false;
                break;
            }
            case ODEBER_KAFE: {
                action = resources.ACTION.ODEBER_KAFE;
                boolean odeber = odeberKafe(akce);
                if (!odeber) {
                    System.out.println("Neplatne ID");
                    action = resources.ACTION.NEPLATNA_AKCE;
                }
                break;
            }
            case ODEBER_KELIMEK: {
                jeKelimek = false;
                break;
            }
            case NEPLATNA_AKCE: {
                System.out.println("Neplatna akce");
                break;
            }
            case PRAZDNA_PENEZENKA: {
                System.out.println("V penezence nic nemate, nelze vratit penize");
                break;
            }
            case VRAT_VSECHNY_PENIZE: {
                vratVsechnyPenize(penezenkaBezOdecitani);
                stav = resources.STAV.START;
                break;
            }
            case VRAT_ZBYTEK: {
                System.out.println("Vraceni penez");
                Thread.sleep(3000);
                vratVsechnyPenize(penezenka);
                stav = resources.STAV.VYDEJ_KAFE;
                break;
            }
            case PRIDEJ_CUKR: {
                if (!jeCukr) {
                    jeCukr = true;
                    System.out.println("Cukr doplnen");
                }
                else {
                    System.out.println("Cukr uz je plny");
                }
                break;
            }
            case PRIDEJ_KAFE: {
                doplnKafe(akce);
                break;
            }
            case PRIDEJ_KELIMEK: {
                if (!jeKelimek) {
                    jeKelimek = true;
                    System.out.println("Kelimky doplneny");
                }
                else {
                    System.out.println("Kelimky jsou uz doplnene");
                }
                break;
            }
            case PRIDEJ_VODU: {
                if (!jeVoda) {
                    jeVoda = true;
                    System.out.println("Voda doplnena");
                }
                else {
                    System.out.println("Voda je uz doplnena");
                }
                break;
            }
            case VRAT_CHYBOVY_STAV: {
                if (jeVoda && jeCukr && jeKelimek) {
                    infoVoda = false;
                    infoCukr = false;
                    infoKelimek = false;
                    infoVypsano = false;
                    stav = resources.STAV.START;
                }
                else {
                    System.out.println("Neni vse doplneno (kelimky + voda + cukr) !!");
                }
                break;
            }
            case MAM_ROZMENENI: {
                Thread scannerThread = new Thread(new resources.ScannerThread());
                scannerThread.start();

                //smycka co pozastavuje aktualni thread, kontroluje zda scannerThread stale jede nebo ne
                while(scannerThread.isAlive()) {

                }

                scannerThread.interrupt();
                if (!jeVoda || stav == resources.STAV.START) {
                    vratVsechnyPenize(penezenkaBezOdecitani);
                    break;
                }

                int vraceni = mamNaRozmeneni();
                if (mamNaRozmenit) {
                    stav = resources.STAV.VYDEJ_KELIMEK;
                }
                else {
                    stav = resources.STAV.PLATIT_VIC;
                    System.out.println("Automat nemá bohužel na rozměnění, maximalne vrati " + vraceni + ", napište a/n, jestli chcete pokračovat nebo ne");
                }
                break;
            }
            case CHCI_PLATIT_VIC: {
                Thread scannerThread = new Thread(new resources.ScannerThread());
                scannerThread.start();

                //smycka co pozastavuje aktualni thread, kontroluje zda scannerThread stale jede nebo ne
                while(scannerThread.isAlive()) {

                }

                scannerThread.interrupt();
                if (!jeVoda || stav == resources.STAV.START) {
                    vratVsechnyPenize(penezenkaBezOdecitani);
                    break;
                }
                stav = resources.STAV.VYDEJ_KELIMEK;

                break;
            }
            case VYDANI_KELIMKU: {
                Thread scannerThread = new Thread(new resources.ScannerThread());
                scannerThread.start();

                //smycka co pozastavuje aktualni thread, kontroluje zda scannerThread stale jede nebo ne
                while(scannerThread.isAlive()) {

                }

                scannerThread.interrupt();
                if (!jeVoda || !jeKelimek || stav == resources.STAV.START) {
                    vratVsechnyPenize(penezenkaBezOdecitani);
                    break;
                }
                stav = resources.STAV.OHREV_VODY;
                break;
            }
            case OHREVANI_VODY: {
                Thread scannerThread = new Thread(new resources.ScannerThread());
                scannerThread.start();

                //smycka co pozastavuje aktualni thread, kontroluje zda scannerThread stale jede nebo ne
                while(scannerThread.isAlive()) {

                }

                scannerThread.interrupt();
                if (!jeVoda || stav == resources.STAV.START) {
                    vratVsechnyPenize(penezenkaBezOdecitani);
                    break;
                }
                stav = resources.STAV.ODSYP_KAVY;
                break;
            }
            case ODSYPANI_KAVY: {
                System.out.println("Odsyp kávy");
                Thread.sleep(3000);
                stav = resources.STAV.ODLITI_NAPOJE;
                break;
            }
            case ODLEVANI: {
                System.out.println("Odlevani kávy");
                Thread.sleep(3000);
                stav = resources.STAV.VRATIT_ZBYTEK;
                break;
            }
            case VYDANI_KAFE: {
                System.out.println("Prosim odeberte kafe");
                stav = resources.STAV.START;
                break;
            }
            case DEFAULT: {
                break;
            }
            default: {
                break;
            }
        }
    }

    /**
     * Pomoci zadaneho dotazu do console zjistuji pozadovanou akci
     *
     * @param akce nactena radka z console
     */
    public static void zjistiAkci(String akce) {
        if (akce.length() > 0) {

            switch (Character.toUpperCase(akce.charAt(0))) {
                case 'D': {
                    action = resources.ACTION.VYPIS_MINCE;
                    break;
                }
                case 'H': {
                    action = resources.ACTION.VYPIS_DOSTUPNOST;
                    break;
                }
                case 'B': {
                    if (penezenka != 0) {
                        stav = resources.STAV.VRATIT_PENIZE;
                        action = resources.ACTION.DEFAULT;
                        break;
                    } else {
                        action = resources.ACTION.PRAZDNA_PENEZENKA;
                        break;
                    }
                }
                case 'M': {
                    action = resources.ACTION.PRIDEJ_MINCI;
                    break;
                }
                case 'T': {
                    if (penezenka != 0) {
                        stav = resources.STAV.NAPOJ_ZVOLEN;
                        action = resources.ACTION.DEFAULT;
                        pom = akce;
                    } else {
                        action = resources.ACTION.PRAZDNA_PENEZENKA;
                    }
                    break;
                }
                case 'I': {
                    action = resources.ACTION.VYPIS_POUZIVANI;
                    break;
                }
                case 'V': {
                    action = resources.ACTION.ODEBER_VODU;
                    break;
                }
                case 'S': {
                    action = resources.ACTION.ODEBER_CUKR;
                    break;
                }
                case 'K': {
                    action = resources.ACTION.ODEBER_KELIMEK;
                    break;
                }
                case 'C': {
                    action = resources.ACTION.ODEBER_KAFE;
                    break;
                }
                case 'E': {
                    stav = resources.STAV.SERVIS;
                    action = resources.ACTION.DEFAULT;
                    break;
                }
                case 'X': {
                    break;
                }
                default: {
                    action = resources.ACTION.NEPLATNA_AKCE;
                    break;
                }
            }
        }
        else {

        }
    }

    /**
     * Zjisti pozadovanou akci ze vstupu
     *
     * @param akce vstup z console
     */
    private static void zjistiAkciChybovy(String akce) {
        if (akce.length() > 0) {
            switch (Character.toUpperCase(akce.charAt(0))) {
                case 'A': {
                    action = resources.ACTION.VRAT_CHYBOVY_STAV;
                    break;
                }
                case 'V': {
                    action = resources.ACTION.PRIDEJ_VODU;
                    break;
                }
                case 'S': {
                    action = resources.ACTION.PRIDEJ_CUKR;
                    break;
                }
                case 'K': {
                    action = resources.ACTION.PRIDEJ_KELIMEK;
                    break;
                }
                case 'C': {
                    action = resources.ACTION.PRIDEJ_KAFE;
                    break;
                }
                default: {
                    action = resources.ACTION.NEPLATNA_AKCE;
                    infoVoda = false;
                    infoCukr = false;
                    infoKelimek = false;
                    break;
                }
            }
        }
        else {
            action = resources.ACTION.NEPLATNA_AKCE;
        }
    }


    /**
     * Vraci nam pozadovanou akci ve stringu
     */
    public static String vratInput(Scanner sc) {
        String tmp = sc.nextLine();
        String[] dotaz = tmp.split("=");
        String akce = "";
        if (dotaz.length > 1) akce = dotaz[1];
        return akce;
    }

    /**
     * Naplneni mapy hodnotama
     */
    private static void naplnVyrobky(){
        vyrobky.put(0, new resources.Kafe(0, 10, "Espresso"));
        vyrobky.put(1, new resources.Kafe(1, 15, "Ristretto"));
        vyrobky.put(2, new resources.Kafe(2, 18, "Lungo"));
        vyrobky.put(3, new resources.Kafe(3, 13, "Espresso doppio"));
        vyrobky.put(4, new resources.Kafe(4, 25, "Cappuccino"));
        vyrobky.put(5, new resources.Kafe(5, 20, "Latte"));
        vyrobky.put(6, new resources.Kafe(6, 22, "Latte macchiato"));
        vyrobky.put(7, new resources.Kafe(7, 30, "Americano"));
        vyrobky.put(8, new resources.Kafe(8, 25, "Irská káva"));
        vyrobky.put(9, new resources.Kafe(9, 8, "Turecká káva"));
        vyrobky.put(10, new resources.Kafe(10, 5, "Čaj citrónový"));
    }

    /**
     * Vypisuje info o pouzivani automatu
     */
    private static void vypisPouzivani() {
        System.out.println("Vstup je ve formátu P=akce");
        System.out.println("P=M-x   : Vhození mince, kde X značí sumu");
        System.out.println("P=D   : Vypis poctu minci");
        System.out.println("P=H   : Vypis dostupnosti kafe");
        System.out.println("P=T-x   : Zmáčknutí tlačítka, kde X značí id kafe");
        System.out.println("P=B   : Vrácení všech mincí, ukončení nákupu");
        System.out.println("P=I   : Vypsání používání");
        System.out.println("P=V   : Simulace dosla voda");
        System.out.println("P=S   : Simulace dosel cukr");
        System.out.println("P=K   : Simulace dosly kelimky");
        System.out.println("P=E   : Chybovy stav (pro testovaci ucely)");
        System.out.println("P=C-x   : Simulace doslo kafe, kde X značí id kafe");
    }

    /**
     * Vypisuje info o pouzivani v chybovem stavu
     */
    private static void vypisChybovy() {
        System.out.println("Vstup je ve formátu P=akce");
        System.out.println("P=A   : Vraceni zpet do automatu");
        System.out.println("P=S   : Cukr doplnen");
        System.out.println("P=V   : Voda doplnena");
        System.out.println("P=K   : Kelimky doplneny");
        System.out.println("P=C-x   : Simulace pridal kafe, kde X značí id kafe");
    }

    /**
     * Vypisuje info o produktech
     */
    private static void vypisInfo() {
        System.out.println("UnoTrio kafe automat nabízí");
        System.out.println("---------------------------");
        for (int i = 0; i < vyrobky.size(); i++) {
            System.out.println(vyrobky.get(i).toString());
        }
        infoVypsano = true;
    }

    /**
     * Vypis dostupnosti vsech kafi
     */
    private static void vypisDostupnostiKafe() {
        for(int i = 0; i < vyrobky.size(); i++) {
            System.out.println(vyrobky.get(i).getNazev() + ", dostupnost = " + vyrobky.get(i).isDostupne() + ", hmotnost = " + vyrobky.get(i).getHmotnost());
        }
    }

    /**
     * Vypise nam aktualni pocet minci, pouze pro testovaci ucely
     */
    private static void vypisPoctuMinci() {
        for (int i = 0; i < Mince.length; i++) {
            System.out.println(Mince[i].vratHodnotu() + ": " + Mince[i].vratPocet());
        }
    }

    /**
     * Vypise info pro servisaka
     */
    private static void vypisInfoChybovy() {
        System.out.println("Info pro servisaka: ");
        if (!infoVoda && !jeVoda) {
            System.out.println("Došla voda");
        }
        if (!infoCukr && !jeCukr) {
            System.out.println("Došel cukr");
        }
        if (!infoKelimek && !jeKelimek) {
            System.out.println("Došel kelímek");
        }
        for (int i = 0; i < vyrobky.size(); i++) {
            if (!vyrobky.get(i).isDostupne()) {
                System.out.println("Kafe " + vyrobky.get(i).getNazev() + " došlo.");
            }
        }
    }

    /**
     * Pomocna funkce pro split stringu
     * @param dotaz dotaz pro split
     * @param znak znak, podle ceho se ma string splitnout
     * @return pole stringu, rozdelenych podle splitu
     */
    private static String vratHodnotu(String dotaz, String znak) {
        String[] str = dotaz.split(znak);
        String value = str[1];
        return value;
    }

    /**
     * Prida minci do konta
     * @param akce pozadovana mince
     * @return zda se povedlo pridat minci
     */
    private static boolean pridejDoKonta(String akce) {
        int hodnota = Integer.parseInt(vratHodnotu(akce, "-"));
        boolean platnaMince = false;
        for (int i = 0; i < Mince.length; i++) {
            if(Mince[i].vratHodnotu() == hodnota) {
                stav = resources.STAV.PENEZENKA;
                Mince[i].setPocet(Mince[i].vratPocet() + 1);
                platnaMince = true;
                penezenka += hodnota;
                penezenkaBezOdecitani = penezenka;
                break;
            }
        }
        return platnaMince;
    }

    /**
     * Simulovani pridani kafe
     */
    private static boolean doplnKafe(String akce) {
        int intID = Integer.parseInt(vratHodnotu(akce, "-"));
        boolean obsahuje = false;
        for(int i = 0; i < vyrobky.size(); i++) {
            if(vyrobky.get(i).getId() == intID) {
                vyrobky.get(intID).setDostupne(true);
                vyrobky.get(intID).setHmotnost(200);
                System.out.println("Kafe s ID = " + vyrobky.get(intID).getId() + " bylo uspesne pridano");
                obsahuje = true;
            }
        }
        return obsahuje;
    }

    /**
     * Vypocita z penezenky kolik a jakych minci ma vratit kupujicimu
     * @param penezen celkova hodnota penezenky
     */
    public static void vratVsechnyPenize(int penezen) {
        int padesat = penezen / MINCE.PADESAT.vratHodnotu();
        if (padesat <= Mince[5].vratPocet()) {
            penezen -= (padesat * MINCE.PADESAT.vratHodnotu());
            Mince[5].setPocet(Mince[5].vratPocet() - padesat);
        }
        else {
            padesat = Mince[5].vratPocet();
            penezen -= (Mince[5].vratPocet() * MINCE.PADESAT.vratHodnotu());
            Mince[5].setPocet(0);
        }

        int dvacet = penezen / MINCE.DVACET.vratHodnotu();
        if (dvacet <= Mince[4].vratPocet()) {
            penezen -= (dvacet * MINCE.DVACET.vratHodnotu());
            Mince[4].setPocet(Mince[4].vratPocet() - dvacet);
        }
        else {
            dvacet = Mince[4].vratPocet();
            penezen -= (Mince[4].vratPocet() * MINCE.DVACET.vratHodnotu());
            Mince[4].setPocet(0);
        }

        int deset = penezen / MINCE.DESET.vratHodnotu();
        if (deset <= Mince[3].vratPocet()) {
            penezen -= (deset * MINCE.DESET.vratHodnotu());
            Mince[3].setPocet(Mince[3].vratPocet() - deset);
        }
        else {
            deset = Mince[3].vratPocet();
            penezen -= (Mince[3].vratPocet() * MINCE.DESET.vratHodnotu());
            Mince[3].setPocet(0);
        }

        int pet = penezen / MINCE.PET.vratHodnotu();
        if (pet <= Mince[2].vratPocet()) {
            penezen -= (pet * MINCE.PET.vratHodnotu());
            Mince[2].setPocet(Mince[2].vratPocet() - pet);
        }
        else {
            pet = Mince[2].vratPocet();
            penezen -= (Mince[2].vratPocet() * MINCE.PET.vratHodnotu());
            Mince[2].setPocet(0);
        }

        int dva = penezen / MINCE.DVA.vratHodnotu();
        if (dva <= Mince[1].vratPocet()) {
            penezen -= (dva * MINCE.DVA.vratHodnotu());
            Mince[1].setPocet(Mince[1].vratPocet() - dva);
        }
        else {
            dva = Mince[1].vratPocet();
            penezen -= (Mince[1].vratPocet() * MINCE.DVA.vratHodnotu());
            Mince[1].setPocet(0);
        }

        int jedna = penezen / MINCE.JEDNA.vratHodnotu();
        if (jedna <= Mince[0].vratPocet()) {
            penezen -= (jedna * MINCE.JEDNA.vratHodnotu());
            Mince[0].setPocet(Mince[0].vratPocet() - jedna);
        }
        else {
            jedna = Mince[0].vratPocet();
            penezen -= (Mince[0].vratPocet() * MINCE.JEDNA.vratHodnotu());
            Mince[0].setPocet(0);
        }

        penezenka = 0;
        penezenkaBezOdecitani = 0;
        vratMinceVse = false;
        infoVypsano = false;
        int celkem = (MINCE.PADESAT.vratHodnotu()*padesat) + (MINCE.DVACET.vratHodnotu()*dvacet) +
                (MINCE.DESET.vratHodnotu()*deset) + (MINCE.PET.vratHodnotu()*pet) +
                (MINCE.DVA.vratHodnotu()*dva) + (MINCE.DVA.vratHodnotu() * jedna);
        System.out.println("Vracím peníze: "+ celkem);
        System.out.println(padesat + "x " + MINCE.PADESAT.vratHodnotu());
        System.out.println(dvacet + "x " + MINCE.DVACET.vratHodnotu());
        System.out.println(deset + "x " + MINCE.DESET.vratHodnotu());
        System.out.println(pet + "x " + MINCE.PET.vratHodnotu());
        System.out.println(dva + "x " + MINCE.DVA.vratHodnotu());
        System.out.println(jedna + "x " + MINCE.JEDNA.vratHodnotu());
        System.out.println();
        stav = resources.STAV.START;
    }

    /**
     * Odebere kafe podle id ziskaneho ze vstupu
     * @param akce vstup z console
     */
    public static boolean odeberKafe(String akce) {
        int intID = Integer.parseInt(vratHodnotu(akce, "-"));
        boolean obsahuje = false;
        for(int i = 0; i < vyrobky.size(); i++) {
            if(vyrobky.get(i).getId() == intID) {
                vyrobky.get(intID).setDostupne(false);
                vyrobky.get(intID).setHmotnost(0);
                System.out.println("Kafe s ID = " + vyrobky.get(intID).getId() + " se uspesne odebralo.");
                obsahuje = true;
            }
        }
        return obsahuje;
    }

    /**
     * Pokud uzivatel chce koupit nejaky druh kafe
     * @param id_KAFE id kafe
     */
    private static boolean kupujuKafe(int id_KAFE) {
        boolean kupuju = false;
        if (vyrobky.containsKey(id_KAFE)) {
            kafe = vyrobky.get(id_KAFE);
            if ((kafe.getHmotnost() - 10) >= 0) {
                if (penezenka >= kafe.getCena()) {
                    penezenkaBezOdecitani = penezenka;
                    penezenka -= kafe.getCena();
                    zmacknuteTlacitko = true;
                    kupuju = true;
                    vyrobky.get(id_KAFE).setHmotnost(kafe.getHmotnost() - 10);
                } else {
                    System.out.println("Nemate dost penez na toto kafe.");
                }
            }
            else {
                kafe.setDostupne(false);
                System.out.println("Omlouvame se, ale toto kafe je momentalne nedostupne");
            }
        }
        else {
            System.out.println("Neplatne id kafe");
        }
        return kupuju;
    }

    /**
     * Testuje, zda ma automat na rozmeneni, pokud ne, vypocita co vse muze vratit a poda info uzivateli
     * @return vraci hodnotu, kterou lze vratit
     */
    private static int mamNaRozmeneni() {
        int celkovyPocet = penezenka;
        int vratit = 0;
        int padesat = celkovyPocet / 50;
        if (padesat > Mince[5].vratPocet()) {
            vratit += Mince[5].vratPocet() * 50;
            celkovyPocet -= Mince[5].vratPocet() * 50;
            mamNaRozmenit = false;
        }
        else {
            celkovyPocet -= padesat * 50;
            vratit += padesat * 50;
        }

        int dvacet = celkovyPocet / 20;
        if (dvacet > Mince[4].vratPocet()) {
            vratit += Mince[4].vratPocet() * 20;
            celkovyPocet -= Mince[4].vratPocet() * 20;
            mamNaRozmenit = false;
        }
        else {
            celkovyPocet -= dvacet * 20;
            vratit += dvacet * 20;
        }

        int deset = celkovyPocet / 10;
        if (deset > Mince[3].vratPocet()) {
            vratit += Mince[3].vratPocet() * 10;
            celkovyPocet -= Mince[3].vratPocet() * 10;
            mamNaRozmenit = false;
        }
        else {
            celkovyPocet -= deset * 10;
            vratit += deset * 10;
        }

        int pet = celkovyPocet / 5;
        if (pet > Mince[2].vratPocet()) {
            vratit += Mince[2].vratPocet() * 5;
            celkovyPocet -= Mince[2].vratPocet() * 5;
            mamNaRozmenit = false;
        }
        else {
            celkovyPocet -= pet * 5;
            vratit += pet * 5;
        }

        int dva = celkovyPocet / 2;
        if (dva > Mince[1].vratPocet()) {
            vratit += Mince[1].vratPocet() * 2;
            celkovyPocet -= Mince[1].vratPocet() * 5;
            mamNaRozmenit = false;
        }
        else {
            celkovyPocet -= dva * 5;
            vratit += dva * 2;
        }

        int jedna = celkovyPocet / 1;
        if (jedna > Mince[0].vratPocet()) {
            vratit += Mince[0].vratPocet() * 1;
            celkovyPocet -= Mince[0].vratPocet() * 1;
            mamNaRozmenit = false;
        }
        else {
            celkovyPocet -= jedna * 1;
            vratit += jedna * 1;
        }
        return vratit;
    }
}
