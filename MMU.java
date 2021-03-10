import Exceptions.*;

public class MMU {
    VirtualPageTable virtualPageTable;

    public MMU(int busSize, int offset) {
        this.virtualPageTable = new VirtualPageTable((int)Math.pow(2, busSize - offset), (int)Math.pow(2, offset));
    }
    public void addToMemory(Hexadecimal memoryLocation, int pageFrame) {
        virtualPageTable.pageTable[memoryLocation.pageFrame] = new PageTableEntry(1, 1, 0, pageFrame);
    }
    public void setDirtyBit(Hexadecimal memoryLocation) {
        virtualPageTable.pageTable[memoryLocation.pageFrame].d = 1;
    }
    public Hexadecimal readMemory(Hexadecimal memoryLocation) {
        final PageTableEntry page = virtualPageTable.pageTable[memoryLocation.pageFrame];
        if (page != null && page.pageFrame != -1) {
            return new Hexadecimal(String.format("%02X", page.pageFrame) + memoryLocation.offsetHex());
        } else {
            throw new PageFault();
        }
    }
}
