public class VirtualPageTable {
    final int PAGE_SIZE;
    PageTableEntry[] pageTable;

    public VirtualPageTable(int pageTableSize, int PAGE_SIZE) {
        this.pageTable = new PageTableEntry[pageTableSize];
        this.PAGE_SIZE = PAGE_SIZE;
    }
    public int nextAvailableLocation() {
        for (int i = 0; i < pageTable.length; i++) {
            if (pageTable[i] == null) {
                return i;
            }
        }

        return -1;
    }
}
