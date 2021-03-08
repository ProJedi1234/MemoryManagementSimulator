public class MMU {
    VirtualPageTable virtualPageTable;

    public MMU(int busSize) {
        this.virtualPageTable = new VirtualPageTable(2^busSize);
    }
}
