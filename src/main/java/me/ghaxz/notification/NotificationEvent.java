package me.ghaxz.notification;

public class NotificationEvent {
    private final String message;
    private final NotificationType type;

    public NotificationEvent(String message, NotificationType type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public NotificationType getType() {
        return type;
    }
}
