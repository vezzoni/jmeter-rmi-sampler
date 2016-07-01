package vezzoni.jmeter.rmi;

public class RmiException extends Exception {

    public RmiException(String message) {
        super(message);
    }

    public RmiException(String message, Throwable ex) {
        super(message, ex);
    }
}