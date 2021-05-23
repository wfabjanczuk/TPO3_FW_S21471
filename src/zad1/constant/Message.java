package zad1.constant;

public class Message {
    public static String goodbyeFromAdmin = "goodbye-from-admin";
    public static String goodbyeFromMainServer = "goodbye-from-main-server";
    public static String goodbyeFromClient = "goodbye-from-client";

    public static String getTopicsForAdmin = "get-topics-for-admin";
    public static String getTopicsForClient = "get-topics-for-client";

    public static String addTopic = "add-topic";
    public static String addTopicSuccess = "Success: Topic has been set";
    public static String addTopicExistsError = "Error: Topic already exists";
    public static String addTopicEmptyError = "Error: Topic is empty";

    public static String deleteTopic = "delete-topic";
    public static String deleteTopicSuccess = "Success: Topic has been deleted";
    public static String deleteTopicNotExistsError = "Error: Topic does not exist";

    public static String publish = "publish";
    public static String publishSuccess = "Success: Message has been published";

    public static String registerMessageInbox = "register-message-inbox";
    public static String registerMessageInboxSuccess = "Success: MessageInbox has been registered";
    public static String registerMessageInboxError = "Error: MessageInbox could not be registered";

    public static String subscribeToTopic = "subscribe-to-topic";
    public static String subscribeToTopicSuccess = "Success: Topic subscribed";
    public static String subscribeToTopicSubscribedError = "Error: Topic was already subscribed";
    public static String subscribeToTopicEmptyError = "Error: Topic is empty";
    public static String subscribeToTopicNotExistsError = "Error: Topic does not exist";

    public static String unsubscribeFromTopic = "unsubscribe-from-topic";
    public static String unsubscribeFromTopicSuccess = "Success: Topic unsubscribed";
    public static String unsubscribeFromTopicEmptyError = "Error: Topic is empty";
    public static String unsubscribeFromTopicNotSubscribedError = "Error: Topic was not subscribed";
    public static String unsubscribeFromTopicNotExistsError = "Error: Topic does not exist";
}
