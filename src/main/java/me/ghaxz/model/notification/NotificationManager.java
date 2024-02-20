package me.ghaxz.model.notification;

import java.util.ArrayList;

public class NotificationManager {

    private static final ArrayList<NotificationManager> managers = new ArrayList<>();

    private final NotificationPublisher publisher;
    private final ArrayList<NotificationSubscriber> subscribers;

    private NotificationManager(NotificationPublisher publisher) {
        this.publisher = publisher;
        this.subscribers = new ArrayList<>();
    }

    public static NotificationManager getManager(NotificationPublisher providedPublisher) {
        if(managers.stream().noneMatch(pub -> pub.getPublisher().equals(providedPublisher))) {
            managers.add(new NotificationManager(providedPublisher));
        }

        return managers.stream().filter(pub -> pub.getPublisher().equals(providedPublisher)).findFirst().get();
    }

    public void addSubscriber(NotificationSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(NotificationSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public NotificationPublisher getPublisher() {
        return publisher;
    }

    public ArrayList<NotificationSubscriber> getSubscribers() {
        return subscribers;
    }
}
