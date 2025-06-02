package com.openketchupsource.soulmate.apiPayload.exception.handler;

import com.openketchupsource.soulmate.apiPayload.exception.GeneralException;
import com.openketchupsource.soulmate.apiPayload.form.BaseCode;

public class DiaryHandler extends GeneralException {
    private final Object result;

    public DiaryHandler(BaseCode errorCode) {
        super(errorCode);
        this.result = null;
    }
}
