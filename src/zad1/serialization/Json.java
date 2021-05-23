package zad1.serialization;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Json {
    public static String serializeString(String text) {
        return '"' + text.replaceAll("\"", "\\\"").replaceAll("\\s", "_") + '"';
    }

    public static String unserializeString(String serializedString) {
        return serializedString.substring(1, serializedString.length() - 1)
                .replaceAll("\\\"", "\"")
                .replaceAll("_", " ");
    }

    public static String serializeStrings(String... strings) {
        return serializeStrings(Arrays.asList(strings));
    }

    public static String serializeStrings(List<String> strings) {
        List<String> escapedStrings = strings.stream().map(Json::serializeString).collect(Collectors.toList());

        return "[" + String.join(",", escapedStrings) + "]";
    }

    public static List<String> unserializeStrings(String serializedStrings) {
        if (serializedStrings.length() < 2) {
            throw new IllegalArgumentException("Invalid string to unserialize");
        }

        Stream<String> stringStream = Arrays.stream(serializedStrings
                .substring(1, serializedStrings.length() - 1)
                .split("(?<=\"),(?=\")")
        );

        return stringStream.map(s -> s.isEmpty() ? s : Json.unserializeString(s)).collect(Collectors.toList());
    }
}
