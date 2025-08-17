package br.com.wilgner.brotreinos.exception;

public class ResourceNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;
    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
