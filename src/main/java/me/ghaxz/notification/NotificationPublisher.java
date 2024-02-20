package me.ghaxz.notification;

public interface NotificationPublisher {
    default void notify(String message, NotificationType type) {
        getManager().getSubscribers().forEach(sub -> sub.onNotification(new NotificationEvent(message, type)));
    }

    default void addSubscriber(NotificationSubscriber subscriber) {
        getManager().addSubscriber(subscriber);
    }

    default void removeSubscriber(NotificationSubscriber subscriber) {
        getManager().removeSubscriber(subscriber);
    }

    default NotificationManager getManager() {
        return NotificationManager.getManager(this);
    }
}
