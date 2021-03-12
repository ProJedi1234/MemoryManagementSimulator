import Exceptions.*;

public class MMU {
    VirtualPageTable virtualPageTable;

    public MMU(int busSize, int offset) {
        this.virtualPageTable = new VirtualPageTable((int)Math.pow(2, busSize - offset), (int)Math.pow(2, offset));
    }
    public void addToMemory(Hexadecimal memoryLocation, int pageFrame, TLB tlb) {
        PageTableEntry entry = new PageTableEntry(1, 1, 0, pageFrame);
        virtualPageTable.pageTable[memoryLocation.pageFrame] = entry;
        if (pageFrame != -1) {
            tlb.addNewAddress(new TLBEntry(memoryLocation.pageFrame, entry), this);
        } else {
            tlb.deletePage(memoryLocation);
        }
    }
    public void setDirtyBit(Hexadecimal memoryLocation, TLB tlb) {
        try {
            tlb.setDirtyBit(memoryLocation);
        } catch (TLBFault e) {
            virtualPageTable.pageTable[memoryLocation.pageFrame].d = 1;
        }
    }
    public void setDirtyBit(int vpageIndex) {
        virtualPageTable.pageTable[vpageIndex].d = 1;
    }
    public Hexadecimal readMemory(TLB tlb, Hexadecimal memoryLocation) {
        final PageTableEntry page = virtualPageTable.pageTable[memoryLocation.pageFrame];
        if (page != null && page.pageFrame != -1) {
            tlb.addNewAddress(new TLBEntry(memoryLocation.pageFrame, 1, 1, page.d, page.pageFrame), this);
            virtualPageTable.pageTable[memoryLocation.pageFrame].r = 1;
            return new Hexadecimal(String.format("%02X", page.pageFrame) + memoryLocation.offsetHex());
        } else {
            throw new PageFault();
        }
    }
}
