package zad1.serialization;

public class Json {
    public static String serializeArrayOfStrings(String[] stringArray) {
        return "[\""
                + String.join("\",\"", stringArray)
                + "\"]";
    }
}
