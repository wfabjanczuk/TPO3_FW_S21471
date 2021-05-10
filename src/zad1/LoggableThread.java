package zad1;

public interface LoggableThread {

    default String getThreadLabel() {
        return this.getClass().getSimpleName() + "@" + this.hashCode() + ":";
    }

    default void logThreadException(Exception exception) {
        System.out.println(getThreadLabel() + " " + exception.getMessage());
        exception.printStackTrace();
    }

    default void logThreadInitialized() {
        System.out.println(getThreadLabel() + " Initialized.");
    }

    default void logThreadStarted() {
        System.out.println(getThreadLabel() + " Started.");
    }

    default void logThreadCannotInitialize() {
        System.out.println(getThreadLabel() + " Thread cannot initialize.");
    }

    default void logThreadCannotStart() {
        System.out.println(getThreadLabel() + " Thread cannot start.");
    }

    default void logThreadAcceptedConnection() {
        System.out.println(getThreadLabel() + " new connection accepted.");
    }

    default void logThreadChannelClosed() {
        System.out.println(getThreadLabel() + " channel closed.");
    }

    default void logThreadReceived(String message) {
        String information = (message == null || message.trim().length() == 0)
                ? " received empty message."
                : " received message: " + message.trim();

        System.out.println(getThreadLabel() + information);
    }

    default void logThreadSent(String message) {
        System.out.println(getThreadLabel() + " sent message: " + message.trim());
    }
}
