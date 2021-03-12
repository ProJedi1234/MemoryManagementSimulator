import Exceptions.*;

public class TLB {
    private TLBEntry[] data;
    private int lastIn = 0;
    private int tlbSize;

    public TLB(int tlbSize) {
        this.data = new TLBEntry[tlbSize];
        this.tlbSize = tlbSize;
    }

    public void addNewAddress(TLBEntry entry, MMU mmu) {
        TLBEntry curEntry = data[lastIn];

        if (curEntry != null && curEntry.tableEntry.d == 1) {
            //dirty bit set; update page table
            mmu.setDirtyBit(curEntry.vPage);
        }

        data[lastIn] = entry;

        lastIn++;

        if (lastIn >= tlbSize) {
            lastIn = 0;
        }
    }
    public TLBEntry findPage(Hexadecimal memoryAddress) throws TLBFault {
        for (TLBEntry tlbEntry : data) {
            if (tlbEntry != null && tlbEntry.vPage == memoryAddress.pageFrame){
                return tlbEntry;
            }
        }

        throw new TLBFault();
    }
    public void deletePage(Hexadecimal memoryAddress) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] != null && data[i].vPage == memoryAddress.pageFrame) {
                data[i] = null;

                break;
            }
        }
    }
    public void setDirtyBit(Hexadecimal memoryLocation) {
        TLBEntry entry = findPage(memoryLocation);
        entry.tableEntry.d = 1;
    }
}
