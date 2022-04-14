package tv.quaint.objects.events.conditions;

public class BlockEqualsCondition {
    public boolean fromSender;
    public int x;
    public int y;
    public int z;
    public String blockType;

    public BlockEqualsCondition() {
        this.fromSender = true;
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.blockType = "air";
    }

    public BlockEqualsCondition setFromSender(boolean bool) {
        this.fromSender = bool;

        return this;
    }

    public BlockEqualsCondition setX(int x) {
        this.x = x;

        return this;
    }

    public BlockEqualsCondition setY(int y) {
        this.y = y;

        return this;
    }

    public BlockEqualsCondition setZ(int z) {
        this.z = z;

        return this;
    }

    public BlockEqualsCondition setBlockType(String blockType) {
        this.blockType = blockType;

        return this;
    }
}
