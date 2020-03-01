package resources;

public enum MINCE {

    JEDNA(1, 50),
    DVA(2, 30),
    PET(5, 10),
    DESET(10, 20),
    DVACET(20, 5),
    PADESAT(50, 30);

    /*
    JEDNA(1, 0),
    DVA(2, 0),
    PET(5, 0),
    DESET(10, 0),
    DVACET(20, 0),
    PADESAT(50, 0);
*/
    private int hodnota;
    private int pocet;

    private MINCE(int hodnota, int pocet) {
        this.hodnota = hodnota;
        this.pocet = pocet;
    }

    public int vratHodnotu() {
        return hodnota;
    }

    public int vratPocet() {
        return pocet;
    }

    public void setPocet(int pocet) {
        this.pocet = pocet;
    }
}
