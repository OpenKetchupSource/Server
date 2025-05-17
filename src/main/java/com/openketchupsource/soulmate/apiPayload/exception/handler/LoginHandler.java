package com.openketchupsource.soulmate.apiPayload.exception.handler;

import com.openketchupsource.soulmate.apiPayload.exception.GeneralException;
import com.openketchupsource.soulmate.apiPayload.form.BaseCode;

public class LoginHandler extends GeneralException {

    private final Object result;

    public LoginHandler(BaseCode errorCode) {
        super(errorCode);
        this.result = null;
    }

    public LoginHandler(BaseCode errorCode, Object result) {
        super(errorCode);
        this.result = result;
    }

    public Object getResult() {
        return result;
    }
}
