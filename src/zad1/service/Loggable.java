package zad1.service;

public interface Loggable {

    default String getLabel() {
        return this.getClass().getSimpleName() + "@" + this.hashCode() + ":";
    }

    default void logException(Exception exception) {
        System.out.println(getLabel() + " " + exception.getMessage());
        exception.printStackTrace();
    }

    default void logInitialized() {
        System.out.println(getLabel() + " initialized.");
    }

    default void logStarted() {
        System.out.println(getLabel() + " started.");
    }

    default void logCannotInitialize() {
        System.out.println(getLabel() + " cannot initialize.");
    }

    default void logCannotStart() {
        System.out.println(getLabel() + " cannot start.");
    }

    default void logAcceptedConnection() {
        System.out.println(getLabel() + " new connection accepted.");
    }

    default void logChannelClosed() {
        System.out.println(getLabel() + " channel closed.");
    }

    default void logReceived(String message) {
        String information = (message == null || message.trim().length() == 0)
                ? " received empty message."
                : " received message: " + message.trim();

        System.out.println(getLabel() + information);
    }

    default void logSent(String message) {
        System.out.println(getLabel() + " sent message: " + message.trim());
    }
}
