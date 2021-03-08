public class TLB {
    private TLBEntry[] data;
    private int lastIn = 0;
    private int tlbSize;

    public TLB(int tlbSize) {
        this.data = new TLBEntry[tlbSize];
        this.tlbSize = tlbSize;
    }

    public boolean addNewAddress(TLBEntry entry) {
        data[lastIn++] = entry;

        if (lastIn >= tlbSize) {
            lastIn = 0;
        }

        return true;
    }
}
