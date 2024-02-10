package me.ghaxz.notification;

import java.util.concurrent.CopyOnWriteArrayList;

// todo change implementation (ThreadLocal) to make more clear that it doesn't notify itself
public interface NotificationPublisher {
    CopyOnWriteArrayList<NotificationSubscriber> subscribers = new CopyOnWriteArrayList<>();
    ThreadLocal<NotificationPublisher> currentPublisher = new ThreadLocal<>();

    default void notify(String message, NotificationType type) {
        currentPublisher.set(this);
        try {
            subscribers.forEach(sub -> {
                if (!sub.equals(currentPublisher.get())) {
                    sub.onNotification(new NotificationEvent(message, type));
                }
            });
        } finally {
            currentPublisher.remove();
        }
    }

    default void addSubscriber(NotificationSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    default void removeSubscriber(NotificationSubscriber subscriber) {
        subscribers.remove(subscriber);
    }
}
