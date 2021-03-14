public class MemorySimulator {
    static final int CPU_ADDRESS_WIDTH = 16;
    static final int MEMORY_ADDRESS_WIDTH = 12;
    static final int PAGE_OFFSET = 8;
    static final int TLB_SIZE = 16;

    // initialize static report
    static CSVFile report = new CSVFile();

    public static void main(String[] args) throws Exception {
        CPU cpu = new CPU(CPU_ADDRESS_WIDTH, TLB_SIZE, PAGE_OFFSET);
        int[][] ram = new int[(int)Math.pow(2,(MEMORY_ADDRESS_WIDTH - PAGE_OFFSET))][(int)Math.pow(2,(PAGE_OFFSET))];

        for (int i = 0; i < ram.length; i++) {
            for (int j = 0; j < ram[i].length; j++) {
                ram[i][j] = -1;
            }
        }

        Kernel os = new Kernel(cpu, ram);

        if (args.length == 0) {
            System.err.println("No file was provided");
            return;
        }
        if (args.length == 1) {
            System.err.println("No report file was provided");
            return;
        }

        os.RunInstructions(args[0], args[1]);
    }
}