package br.com.neurotech.challenge.exceptions;

public class ClientNotEligibleException extends RuntimeException {
    public ClientNotEligibleException(String message) {
        super(message);
    }
}
