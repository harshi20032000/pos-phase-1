package com.hsw.pos_phase_1.models;


import com.hsw.pos_phase_1.enums.ErrorMessageEnum;
import com.hsw.pos_phase_1.util.CommonUtil;
import com.hsw.pos_phase_1.util.Constants;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class BaseUIResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID =7980140018L;
    private String code;
    private String message;
    private String extendedMessage;
    private String status;
    private boolean hasError;
    private T responsePayload;


    public void setResponsePayload(T responsePayload) {
        this.responsePayload = responsePayload;
        this.hasError = false;
    }

    public void setError(ErrorMessageEnum error) {
        String errorCode = CommonUtil.extractErrorCode(error.toString());
        this.setCode(errorCode);
        this.setMessage(error.getCustomMessage());
        this.setHasError(true);
    }
}
