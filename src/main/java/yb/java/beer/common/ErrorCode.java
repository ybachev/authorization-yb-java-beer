package yb.java.beer.common;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author yavor bachev
 * @since 0.1
 * 2017 Dec
 */

public enum ErrorCode {
    GLOBAL(2),

    AUTHENTICATION(10), JWT_TOKEN_EXPIRED(11);

    private int errorCode;

    private ErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @JsonValue
    public int getErrorCode() {
        return errorCode;
    }
}
