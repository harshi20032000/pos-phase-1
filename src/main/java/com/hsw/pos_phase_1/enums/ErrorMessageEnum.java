package com.hsw.pos_phase_1.enums;

public enum ErrorMessageEnum {

    INVRQ_INVALID_REQUEST_ERROR("A System Exception Occurred. Please Try After Some Time."),

    /**Security level exceptions
     *
     */
    SC_UNAUTHORIZED_EXCEPTION("You are not permitted to do this action.");

    private final String customMessage;

    private ErrorMessageEnum(String customMessage) {
        this.customMessage=customMessage;
    }

    public String getCustomMessage() {
        return customMessage;
    }
}
