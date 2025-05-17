package com.openketchupsource.soulmate.apiPayload.exception;

import com.openketchupsource.soulmate.apiPayload.form.BaseCode;
import com.openketchupsource.soulmate.apiPayload.form.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private BaseCode code;

    public GeneralException(BaseCode code) {
        super(code.getReason().getMessage());
        this.code = code;
    }

    public ReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ReasonDTO getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}
