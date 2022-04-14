package tv.quaint.messages;

import tv.quaint.utils.ParsingUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum CommandMessages {
    RELOAD_CONFIG_RELOADED("{\"text\":\"%color1%Successfully reloaded the %arg1%%color2%!\"}", List.of("%color3%config", "%color3%storage"), List.of('a', '8', 'c')),
    RELOAD_STORAGE_RELOADED("{\"text\":\"%color1%Successfully reloaded the %arg2%%color2%!\"}", List.of("%color3%config", "%color3%storage"), List.of('a', '8', 'c')),
    ;

    public String json;
    public List<String> args;
    public List<Character> colors;

    CommandMessages(String json, List<String> args, List<Character> colors) {
        this.json = json;
        this.args = args;
        this.colors = colors;
    }

    CommandMessages(String json, List<String> args) {
        this.json = json;
        this.args = args;
        this.colors = new ArrayList<>();
    }

    CommandMessages(String json, char... colors) {
        this.json = json;
        this.args = new ArrayList<>();
        this.colors = ParsingUtils.parseCharacters(colors);
    }

    CommandMessages(String json) {
        this.json = json;
        this.args = new ArrayList<>();
        this.colors = new ArrayList<>();
    }

    public String parse() {
        String toReturn = this.json;

        for (int i = 1; i <= this.args.size(); i ++) {
            toReturn = toReturn.replace("%arg" + i + "%", this.args.get(i - 1));
        }

        for (int i = 1; i <= this.colors.size(); i ++) {
            toReturn = toReturn.replace("%color" + i + "%", MessagesStuff.getJsonedLegacyColor(this.colors.get(i - 1)));
        }

        return toReturn;
    }
}
