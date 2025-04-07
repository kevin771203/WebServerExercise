package Obj;

public class Currency {
    private int amount;
    double jpn = 4.4382;
    double usd = 0.0345;
    double cny = 0.2300;

    public Currency(int amount) {
        this.amount = amount;
    }

    public double getJpn() {
        return this.amount * jpn;
    }

    public double getUsd() {
        return this.amount * usd;
    }
    public double getCny() {
        return this.amount * cny;
    }
}
