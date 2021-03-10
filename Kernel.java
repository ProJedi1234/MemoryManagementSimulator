import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Kernel {
    CPU cpu;
    int[][] ram;

    int clockPosition = 0;

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

            final int nextFrame = findNextAvailableMemoryLocation();

            //guard
            if (nextFrame == -1) {
                throw new RuntimeException("No new memory space could be found");
            }

            cpu.mmu.addToMemory(memoryAddress, nextFrame);
            ram[nextFrame][memoryAddress.offset] = Integer.parseInt(value);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public int findNextAvailableMemoryLocation() {
        while (true) {
            PageTableEntry page = cpu.mmu.virtualPageTable.pageTable[clockPosition];
            if (page == null || page.r == 0) {
                return clockPosition;
            }
            clockPosition++;
        }
    }
}
