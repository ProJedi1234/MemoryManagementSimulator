public class CPU {
    final int CPU_BUS_SIZE;

    TLB tlb;
    MMU mmu;

    public CPU(int busSize, int tlbSize, int pageOffset) {
        this.CPU_BUS_SIZE = busSize;
        this.tlb = new TLB(tlbSize);
        this.mmu = new MMU(busSize, pageOffset);
    }
    /**
     * Finds the physical page frame in memory starts disk paging
     * @param memoryLocation location in hex
     * @return returns page frame
     */
    public Hexadecimal readMemory(Hexadecimal memoryLocation) {
        return mmu.readMemory(memoryLocation);
    }
}
