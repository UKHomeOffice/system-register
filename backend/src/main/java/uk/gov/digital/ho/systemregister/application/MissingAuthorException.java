package uk.gov.digital.ho.systemregister.application;

public class MissingAuthorException extends Exception {
    public MissingAuthorException(String msg) {
        super(msg);
    }
}
