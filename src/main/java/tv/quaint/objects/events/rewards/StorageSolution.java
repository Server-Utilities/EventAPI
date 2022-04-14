package tv.quaint.objects.events.rewards;

public class StorageSolution {
    public String identifier;
    public String value;

    public StorageSolution(String identifier, String value) {
        this.identifier = identifier;
        this.value = value;
    }

    public StorageSolution() {
        this.identifier = "";
        this.value = "";
    }

    public StorageSolution setIdentifier(String identifier) {
        this.identifier = identifier;

        return this;
    }

    public StorageSolution setValue(String value) {
        this.value = value;

        return this;
    }
}
