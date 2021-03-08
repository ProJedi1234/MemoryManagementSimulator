public class TLBEntry {
    int vPage;
    PageTableEntry tableEntry;

    public TLBEntry(int vPage, PageTableEntry tableEntry) {
        this.vPage = vPage;
        this.tableEntry = tableEntry;
    }
    public TLBEntry(int vPage, int v, int r, int d, int pageFrame) {
        this.vPage = vPage;
        this.tableEntry = new PageTableEntry(v, r, d, pageFrame);
    }
}
