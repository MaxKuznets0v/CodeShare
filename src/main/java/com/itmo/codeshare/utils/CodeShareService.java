package com.itmo.codeshare.utils;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Service
@State(name = "codeShare", storages = {
        @Storage("codeShare.xml")
})
public class CodeShareService implements PersistentStateComponent<CodeShareService> {

    @Override
    public @Nullable CodeShareService getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull CodeShareService state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }


    private String userId;
    private String userName;
}
