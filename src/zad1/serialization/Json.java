package zad1.serialization;

import java.util.Arrays;
import java.util.List;

public class Json {
    public static String serializeString(String text) {
        return '"' + text + '"';
    }

    public static String unserializeString(String serializedString) {
        return serializedString.trim().replaceAll("\"", "");
    }

    public static String serializeStrings(Iterable<? extends CharSequence> strings) {
        return "[\""
                + String.join("\",\"", strings)
                + "\"]";
    }

    public static List<String> unserializeStrings(String serializedStrings) {
        return Arrays.asList(serializedStrings.trim().replaceAll("[\\[\"\\]]", "").split(","));
    }
}
