package zad1.constant;

public class Message {
    public static String goodbyeFromAdmin = "bye-from-admin";
    public static String goodbyeFromMainServer = "bye-from-main-server";

    public static String getTopics = "get-topics";

    public static String addTopic = "add-topic";
    public static String addTopicResponseSuccess = "Success: Topic has been set";
    public static String addTopicResponseError = "Error: Topic already exists";
    public static String addTopicEmptyError = "Error: Topic is empty";

    public static String deleteTopic = "delete-topic";
    public static String deleteTopicResponseSuccess = "Success: Topic has been deleted";
    public static String deleteTopicResponseError = "Error: Topic does not exist";

    public static String publish = "publish";
    public static String publishResponseSuccess = "Success: Message has been published";
}
