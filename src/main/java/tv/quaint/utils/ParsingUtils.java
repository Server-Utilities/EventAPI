package tv.quaint.utils;

import java.util.ArrayList;
import java.util.List;

public class ParsingUtils {
    public static List<Character> parseCharacters(char... chars) {
        List<Character> characters = new ArrayList<>();

        for (char c : chars) {
            characters.add(c);
        }

        return characters;
    }
}
