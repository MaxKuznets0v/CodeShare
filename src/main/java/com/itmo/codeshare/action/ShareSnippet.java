package com.itmo.codeshare.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.itmo.codeshare.utils.CodeShareService;
import com.itmo.codeshare.utils.ConfigReader;
import com.itmo.codeshare.utils.HttpClient;
import org.apache.http.HttpResponse;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class ShareSnippet extends AnAction {
    protected final String baseUrl;
    final protected CodeShareService storage = ApplicationManager.getApplication().getService(CodeShareService.class);

    public ShareSnippet() throws IOException {
        baseUrl = new ConfigReader().getValue("backend.url") + "/snippet";
    }

    protected String getSelectedText(@NotNull AnActionEvent e) {
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        CaretModel caretModel = editor.getCaretModel();
        return caretModel.getCurrentCaret().getSelectedText();
    }
    protected void addToClipboard(String data) {
        StringSelection selection = new StringSelection(data);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
    protected String createSnippet(String selectedText) {
        if (selectedText == null || selectedText.isEmpty()) {
            throw new RuntimeException("Empty selection!");
        }
        try {
            HttpClient client = new HttpClient(baseUrl + "/create");
            Map<String, String> params = new HashMap<>() {{
                put("snippet", selectedText);
            }};
            if (storage.getUserId() != null) {
                params.put("userId", storage.getUserId());
            }
            HttpResponse resp = client.requestPOST(HttpClient.buildJson(params));

            if (resp.getStatusLine().getStatusCode() == 200) {
                String snippetId = HttpClient.getResponseData(resp);
                return baseUrl + "/" + snippetId;
            }
            throw new RuntimeException("Got non 200 status");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
