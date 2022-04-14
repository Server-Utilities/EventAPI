package tv.quaint.objects.events.conditions;

public class ConfigurableCondition {
    public ConditionType type;
    public String unparsedValue;

    public ConfigurableCondition(ConditionType type, String unparsedValue) {
        this.type = type;
        this.unparsedValue = unparsedValue;
    }

    public ConfigurableCondition() {
        this.type = null;
        this.unparsedValue = "";
    }

    public ConfigurableCondition setType(ConditionType type) {
        this.type = type;

        return this;
    }

    public ConfigurableCondition setUnparsedValue(String unparsedValue) {
        this.unparsedValue = unparsedValue;

        return this;
    }
}
