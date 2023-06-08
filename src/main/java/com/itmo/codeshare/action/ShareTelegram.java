package com.itmo.codeshare.action;


import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.itmo.codeshare.utils.ConfigReader;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URISyntaxException;

public class ShareTelegram extends ShareSnippet {
    public static String telegramUrl;
    private final String shareMessage;
    private final String tooLongMessage;
    private final int msgLengthLimit;
    public ShareTelegram() throws IOException {
        ConfigReader config = new ConfigReader();
        telegramUrl = config.getValue("telegram.url");
        shareMessage = config.getValue("telegram.msg.header");
        tooLongMessage = config.getValue("telegram.msg.tooLong");
        msgLengthLimit = Integer.parseInt(config.getValue("telegram.msgLim"));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String selectedText = getSelectedText(e);
        try {
            String snippetUrl = createSnippet(selectedText);
            shareTelegram(snippetUrl, selectedText);
        } catch (RuntimeException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    private void shareTelegram(String url, String text) throws URISyntaxException {
        String shareUrl = new URIBuilder(telegramUrl)
                .addParameter("url", url)
                .addParameter("text", buildMessage(text))
                .build().toString();
        BrowserUtil.browse(shareUrl);
    }

    private String buildMessage(String text) {
        String message = shareMessage + "\n```";
        message += text.substring(0, Math.min(msgLengthLimit, text.length()));
        message += "```";
        if (text.length() > msgLengthLimit) {
            message += "...\n" + tooLongMessage;
        }
        return message;
    }
}
