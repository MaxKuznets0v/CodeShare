package com.itmo.codeshare.window;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.intellij.util.ui.JBUI;
import com.itmo.codeshare.notification.Notification;
import com.itmo.codeshare.utils.CodeShareService;
import com.itmo.codeshare.utils.ConfigReader;
import com.itmo.codeshare.utils.HttpClient;
import org.apache.http.HttpResponse;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.DumbAware;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;


public class SnippetListWindowFactory implements ToolWindowFactory, DumbAware {
    private final String baseUrl;
    private ContentManager contentManager;
    final private CodeShareService storage = ApplicationManager.getApplication().getService(CodeShareService.class);
    public SnippetListWindowFactory() throws IOException {
        baseUrl = new ConfigReader().getValue("backend.url") + "/account";
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        contentManager = toolWindow.getContentManager();
        JPanel panel;
        String displayName;
        if (storage.getUserId() == null) {
            panel = createLoginPanel();
            displayName = "Sign In";
        } else {
            panel = createQuitPanel();
            displayName = "Sign Out";
        }
        addContent(panel, displayName);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JTextField login = createUsernameField(panel, c);
        JPasswordField password = createPasswordField(panel, c);
        createControlPanel(panel, c, login, password);
        return panel;
    }
    private JPanel createQuitPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel controlsPanel = new JPanel();
        JButton quit = new JButton("Quit from " + storage.getUserName());
        quit.addActionListener(e -> quitUser());
        controlsPanel.add(quit);
        panel.add(controlsPanel);
        return panel;
    }
    private void createControlPanel(JPanel panel, GridBagConstraints c, JTextField loginField, JPasswordField passwordField) {
        JPanel controlsPanel = new JPanel();
        JButton login = new JButton("Login");
        login.addActionListener(e -> loginUser(loginField.getText(), passwordField.getPassword()));
        JButton register = new JButton("Register");
        register.addActionListener(e -> registerUser(loginField.getText(), passwordField.getPassword()));
        controlsPanel.add(login);
        controlsPanel.add(register);
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = JBUI.insetsTop(20);
        panel.add(controlsPanel, c);
    }

    private JTextField createUsernameField(JPanel panel, GridBagConstraints c) {
        c.gridx = 0;
        c.gridy = 0;
        c.insets = JBUI.insets(10, 10, 0, 10);
        c.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Username"), c);
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        JTextField username = new JTextField(20);
        panel.add(username, c);
        return username;
    }
    private JPasswordField createPasswordField(JPanel panel, GridBagConstraints c) {
        c.gridx = 0;
        c.gridy = 1;
        c.insets = JBUI.insets(10);
        c.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Password"), c);
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        JPasswordField password = new JPasswordField(20);
        panel.add(password, c);
        return password;
    }

    private void addContent(JPanel panel, String displayName) {
        Content content = contentManager.getFactory().createContent(panel, displayName, false);
        contentManager.addContent(content);
    }
    private void setContent(JPanel panel, String displayName) {
        Content content = contentManager.getFactory().createContent(panel, displayName, false);
        Content contentToRemove = contentManager.getSelectedContent();
        contentManager.removeContent(contentToRemove, true);
        contentManager.addContent(content);
    }

    private void loginUser(String login, char[] password) {
        String pass = new String(password);
        if (!validateLoginForm(login, pass)) {
            Notification.notify("Failed to login!", "Login and password should have length more than 4", NotificationType.ERROR);
            return;
        }
        HttpClient client = new HttpClient(baseUrl + "/login");
        try {
            HttpResponse resp = client.requestPOST(HttpClient.buildJson(new HashMap<>(){{
                put("login", login);
                put("password", pass);
            }}));
            if (resp.getStatusLine().getStatusCode() != 200) {
                Notification.notify("Wrong credentials!", "Wrong username or password provided!", NotificationType.ERROR);
            } else {
                String userId = HttpClient.getResponseData(resp);
                storage.setUserId(userId);
                storage.setUserName(login);
                Notification.notify("Success!", "You are successfully logged in as " + login + "!", NotificationType.INFORMATION);
                setContent(createQuitPanel(), "Sign Out");
            }
        } catch (IOException e) {
            Notification.notify("Failed to login!", "Unable to send request", NotificationType.ERROR);
        }
    }
    private void registerUser(String login, char[] password) {
        String pass = new String(password);
        if (!validateLoginForm(login, pass)) {
            Notification.notify("Failed to register!", "Login and password should have length more than 4", NotificationType.ERROR);
            return;
        }
        HttpClient client = new HttpClient(baseUrl + "/register");
        try {
            HttpResponse resp = client.requestPOST(HttpClient.buildJson(new HashMap<>(){{
                put("login", login);
                put("password", pass);
            }}));
            if (resp.getStatusLine().getStatusCode() != 200) {
                Notification.notify("Wrong credentials!", "User with this login already exists!", NotificationType.ERROR);
            } else {
                Notification.notify("Success!", "You are successfully registered!", NotificationType.INFORMATION);
                setContent(createQuitPanel(), "Sign Out");
            }
        } catch (IOException e) {
            Notification.notify("Failed to register!", "Unable to send request", NotificationType.ERROR);
        }
    }
    private boolean validateLoginForm(String login, String password) {
        return !(login.length() < 4 || password.length() < 4);
    }
    private void quitUser() {
        storage.setUserId(null);
        storage.setUserName(null);
        Notification.notify("Success!", "You are logged out!", NotificationType.INFORMATION);
        setContent(createLoginPanel(), "Sign In");
    }
}
