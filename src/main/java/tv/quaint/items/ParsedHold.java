package tv.quaint.items;

public class ParsedHold {
    public String material;
    public boolean mainHand;

    public ParsedHold(String material, boolean mainHand) {
        this.material = material;
        this.mainHand = mainHand;
    }

    public ParsedHold() {
        this.material = "";
        this.mainHand = true;
    }

    public ParsedHold setMaterial(String material) {
        this.material = material;

        return this;
    }

    public ParsedHold setMainHand(boolean mainHand) {
        this.mainHand = mainHand;

        return this;
    }
}
