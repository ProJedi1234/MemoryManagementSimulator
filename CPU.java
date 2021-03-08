public class CPU {
    final int CPU_BUS_SIZE;

    TLB tlb;
    MMU mmu;

    public CPU(int busSize, int tlbSize) {
        this.CPU_BUS_SIZE = busSize;
        this.tlb = new TLB(tlbSize);
        this.mmu = new MMU(busSize);
    }
}
