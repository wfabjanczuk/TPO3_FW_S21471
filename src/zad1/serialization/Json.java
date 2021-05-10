package zad1.serialization;

public class Json {
    public static String serializeArrayOfStrings(String[] array) {
        return "[\""
                + String.join("\",\"", array)
                + "\"]";
    }

    public static String[] unserializeArrayOfStrings(String serializedArray) {
        return serializedArray.trim().replaceAll("[\\[\"\\]]", "").split(",");
    }
}
