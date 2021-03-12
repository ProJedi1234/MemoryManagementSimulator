package Exceptions;

public class TLBFault extends RuntimeException {
    public TLBFault() {
        super("TLB no entry");
    }
}
