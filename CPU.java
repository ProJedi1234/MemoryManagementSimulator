import Exceptions.PageFault;
import Exceptions.TLBFault;

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
    private Hexadecimal readMemory(Hexadecimal memoryLocation) {
        try {
            TLBEntry page = tlb.findPage(memoryLocation);
            return new Hexadecimal(String.format("%02X", page.tableEntry.pageFrame) + memoryLocation.offsetHex());
        } catch (TLBFault ex) {
            System.out.println("[TLB Fault]");
            return mmu.readMemory(tlb, memoryLocation);
        }
    }

    public void memoryRead(int[][] ram, Kernel os, Hexadecimal memoryLocation) {
        try {
            final Hexadecimal location = readMemory(memoryLocation);
            final int value = ram[location.pageFrame][location.offset];
            if (value != -1) {
                System.out.println(value);
            } else {
                System.out.println("[No Data]");
            }
        } catch (PageFault e) {
            System.out.println("Page fault");
            os.handlePageFault(memoryLocation);
        }
    }
    public void memoryWrite(int[][] ram, Kernel os, Hexadecimal memoryLocation, int value) {
        try {
            final Hexadecimal location = readMemory(memoryLocation);
            ram[location.pageFrame][location.offset] = value;

            System.out.println("[Memory updated]");
        } catch (PageFault e) {
            System.out.println("[Page fault]");
            os.handlePageFault(memoryLocation);
        }

        mmu.setDirtyBit(memoryLocation, tlb);
    }
}
