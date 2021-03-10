package Exceptions;

public class PageFault extends RuntimeException {
    public PageFault() {
        super("Page fault");
    }
}
