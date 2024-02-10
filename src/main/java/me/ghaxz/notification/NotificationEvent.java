package me.ghaxz.notification;

public class NotificationEvent {
    private final String message;
    private final NotificationType type;

    public NotificationEvent(String text, NotificationType type) {
        this.message = text;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public NotificationType getType() {
        return type;
    }
}
