package me.ghaxz.notification;

public interface NotificationSubscriber {
    default void subscribe(NotificationPublisher publisher) {
        publisher.addSubscriber(this);
    }

    default void unsubscribe(NotificationPublisher publisher) {
        publisher.removeSubscriber(this);
    }

    void onNotification(NotificationEvent event);
}