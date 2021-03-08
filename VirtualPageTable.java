public class VirtualPageTable {
    PageTableEntry[] pageTable;

    public VirtualPageTable(int pageTableSize) {
        this.pageTable = new PageTableEntry[pageTableSize];
    }
}
