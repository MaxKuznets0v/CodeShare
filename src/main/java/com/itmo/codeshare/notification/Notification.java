package com.itmo.codeshare.notification;

import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

public class Notification {
    static public void notify(String groupId, String title, String content, NotificationType type) {
        com.intellij.notification.Notification notification = new com.intellij.notification.Notification(
                groupId, title, content, type);
        Notifications.Bus.notify(notification);
    }

    static public void notify(String title, String content, NotificationType type) {
        notify("com.itmo.codeshare.notification.main", title, content, type);
    }
}
