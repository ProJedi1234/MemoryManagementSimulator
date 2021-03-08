public class PageTableEntry {
    int v;
    int r;
    int d;
    int pageFrame;

    public PageTableEntry(int v, int r, int d, int pageFrame) {
        this.v = v;
        this.r = r;
        this.d = d;
        this.pageFrame = pageFrame;
    }
}
