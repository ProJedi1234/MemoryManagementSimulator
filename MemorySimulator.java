public class MemorySimulator {
    static final int CPU_ADDRESS_WIDTH = 16;
    static final int MEMORY_ADDRESS_WIDTH = 12;
    static final int PAGE_OFFSET = 8;
    static final int TLB_SIZE = 16;

    public static void main(String[] args) {
        CPU cpu = new CPU(CPU_ADDRESS_WIDTH, TLB_SIZE, PAGE_OFFSET);
        int[][] ram = new int[(int)Math.pow(2,(MEMORY_ADDRESS_WIDTH - PAGE_OFFSET))][(int)Math.pow(2,(PAGE_OFFSET))];

        for (int i = 0; i < ram.length; i++) {
            for (int j = 0; j < ram[i].length; j++) {
                ram[i][j] = -1;
            }
        }

        Kernel os = new Kernel(cpu, ram);

        try {
            final Hexadecimal location = cpu.readMemory(new Hexadecimal("5819"));
            System.out.println(ram[location.pageFrame][location.offset]);
        } catch (PageFault e) {
            System.out.println("Page fault");
            os.handlePageFault(new Hexadecimal("5819"));
        }
        try {
            final Hexadecimal location = cpu.readMemory(new Hexadecimal("5819"));
            System.out.println(ram[location.pageFrame][location.offset]);
        } catch (PageFault e) {
            System.out.println("Page fault");
            os.handlePageFault(new Hexadecimal("5819"));
        }
    }
}