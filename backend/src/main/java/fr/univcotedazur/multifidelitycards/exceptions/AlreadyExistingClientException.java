package fr.univcotedazur.multifidelitycards.exceptions;

public class AlreadyExistingClientException extends Exception {
    public AlreadyExistingClientException(String name) {
        super(name);
    }
}
