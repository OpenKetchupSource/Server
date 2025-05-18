package com.openketchupsource.soulmate.apiPayload.exception.handler;

import com.openketchupsource.soulmate.apiPayload.exception.GeneralException;
import com.openketchupsource.soulmate.apiPayload.form.BaseCode;

public class SettingHandler extends GeneralException {

    private final Object result;

    public SettingHandler(BaseCode errorCode) {
        super(errorCode);
        this.result = null;
    }

    public SettingHandler(BaseCode errorCode, Object result) {
        super(errorCode);
        this.result = result;
    }

    public Object getResult() {
        return result;
    }
}