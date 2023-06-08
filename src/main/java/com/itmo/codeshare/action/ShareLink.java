package com.itmo.codeshare.action;

import com.itmo.codeshare.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ShareLink extends ShareSnippet {

    public ShareLink() throws IOException {
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String selectedText = getSelectedText(e);
        try {
            String snippetUrl = createSnippet(selectedText);
            addToClipboard(snippetUrl);
            showLinkSharedNotification(snippetUrl);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }

    private void showLinkSharedNotification(String link) {
        Notification.notify("Public link created successfully!",
                "Link was copied to your clipboard (" + link + ")", NotificationType.INFORMATION);
    }
}
