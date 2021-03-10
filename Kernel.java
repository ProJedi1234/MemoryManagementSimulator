import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Kernel {
    CPU cpu;
    int[][] ram;

    public Kernel(CPU cpu, int[][] ram) {
        this.cpu = cpu;
        this.ram = ram;
    }

    public void handlePageFault(Hexadecimal memoryAddress) {
        //find value on disk
        final String fileLocation = "page_files/" + memoryAddress.pageFrameHex() + ".pg";
        try {
            String value = Files.readAllLines(Paths.get(fileLocation)).get(memoryAddress.offset);
            System.out.println(value);

            final int nextFrame = findNextAvailableFrame();

            cpu.mmu.addToMemory(memoryAddress, nextFrame);
            ram[nextFrame][memoryAddress.offset] = Integer.parseInt(value);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public int findNextAvailableFrame() {
        for (int i = 0; i < ram.length; i++) {
            if (ram[i][0] == -1) {
                return i;
            }
        }

        return -1;
    }
}
