package resources;

/**
 * Trida reprezentujici druh kafe
 */
public class Kafe {
    /** ID kafe*/
    private int id;
    /** Cena kafe*/
    private int cena;
    /** Nazev kafe*/
    private String nazev;
    /** Zda je v zasobniku jeste nejake kafe tohoto typu*/
    private boolean dostupne;
    /** Hmotnost kafe, pro testovani zda v zasobniku je ci ne(pouze pro simulaci) v gramech*/
    private int hmotnost;

    public Kafe(int id, int cena, String nazev) {
        this.id = id;
        this.cena = cena;
        this.nazev = nazev;
        this.dostupne = true;
        this.hmotnost = 20;
    }

    /**
     * Vraci nam aktualni mnozstvi kafe(hmotnost)
     * @return
     */
    public int getHmotnost() {
        return hmotnost;
    }

    /**
     * Nastavuje hmotnost u kafe
     * @param hmotnost pozadovana hmotnost
     */
    public void setHmotnost(int hmotnost) {
        this.hmotnost = hmotnost;
    }

    /**
     * Vraci id kafe
     * @return id kafe
     */
    public int getId() {
        return id;
    }

    /**
     * Vraci cenu kafe
     * @return cena kafe v kc
     */
    public int getCena() {
        return cena;
    }

    /**
     * Vraci nazev kafe
     * @return nazev kafe
     */
    public String getNazev() {
        return nazev;
    }

    /**
     * Vraci stav kafe, zda v zasobniku stale je ci neni
     * @return true/false hodnota, podle stavu kafe v zasobniku
     */
    public boolean isDostupne() {
        return dostupne;
    }

    /**
     * Nastavujeme stav kafe
     * @param dostupne stav kafe
     */
    public void setDostupne(boolean dostupne) {
        this.dostupne = dostupne;
    }

    @Override
    public String toString() {
        String stav = new String();
        if (this.getHmotnost() > 0) dostupne = true;
        else dostupne = false;
        if (dostupne == true) stav = "dostupne";
        else stav = "nedostupne";
        return id + " - " + nazev + ", cena - " + cena + " Kc" + ", dostupnost = " + stav;
    }
}
