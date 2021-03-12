import Exceptions.*;

public class TLB {
    private TLBEntry[] data;
    private int lastIn = 0;
    private int tlbSize;

    public TLB(int tlbSize) {
        this.data = new TLBEntry[tlbSize];
        this.tlbSize = tlbSize;
    }

    public void addNewAddress(TLBEntry entry) {
        data[lastIn++] = entry;

        if (lastIn >= tlbSize) {
            lastIn = 0;
        }
    }
    public TLBEntry findPage(Hexadecimal memoryAddress) throws TLBFault {
        for (TLBEntry tlbEntry : data) {
            if (tlbEntry.vPage == memoryAddress.pageFrame){
                return tlbEntry;
            }
        }

        throw new TLBFault();
    }
    public void setDirtyBit(Hexadecimal memoryLocation) {
        TLBEntry entry = findPage(memoryLocation);
        entry.tableEntry.d = 1;
    }
}
