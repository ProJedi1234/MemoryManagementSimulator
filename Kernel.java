import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
            List<String> values = Files.readAllLines(Paths.get(fileLocation));
            System.out.println(values.get(memoryAddress.offset));

            final int nextMemLoc = FindEmptyMemory();
            // System.out.println("Next Mem: " + nextMemLoc);

            if (nextMemLoc == -1) {
                final int nextFrame = findNextAvailableMemoryLocation();
                // System.out.println("Next Frame: " + nextFrame);

                //guard
                if (nextFrame == -1) {
                    throw new RuntimeException("No new memory space could be found");
                }

                final PageTableEntry page = cpu.mmu.virtualPageTable.pageTable[nextFrame];

                if (page != null && page.d == 1) {
                    //page file needs to be update
                    String fileContents = "";
                    for (int item : ram[page.pageFrame]) {
                        if (fileContents.isEmpty()) {
                            fileContents = String.format("%d", item);
                        }
                        fileContents += String.format("%n%d", item);
                    }

                    System.out.println("[Writing to Page File]");
                    try(FileWriter myWriter = new FileWriter("page_files/" + String.format("%02X", page.pageFrame) + ".pg")) {
                        myWriter.write(fileContents);
                    } catch (Exception e) {
                        System.out.println("An error occurred while saving your file");
                    }
                }

                cpu.mmu.addToMemory(memoryAddress, nextFrame);
                
                if (page != null) {
                    for (int i = 0; i < 256; i++) {
                        ram[page.pageFrame][i] = Integer.parseInt(values.get(i));
                    }

                    cpu.mmu.virtualPageTable.pageTable[nextFrame].pageFrame = 0;
                } else {
                    for (int i = 0; i < values.size(); i++) {
                        ram[nextFrame][i] = Integer.parseInt(values.get(i));
                    }
                }
            } else {
                cpu.mmu.addToMemory(memoryAddress, nextMemLoc);

                for (int i = 0; i < values.size(); i++) {
                    ram[nextMemLoc][i] = Integer.parseInt(values.get(i));
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public int findNextAvailableMemoryLocation() {
        while (true) {
            PageTableEntry page = cpu.mmu.virtualPageTable.pageTable[clockPosition];
            if (page != null && page.r == 0) {
                return clockPosition;
            } else if (page != null) {
                page.r = 0;
            }
            clockPosition++;

            if (clockPosition >= cpu.mmu.virtualPageTable.pageTable.length) {
                clockPosition = 0;
            }
        }
    }

    /**
     * Looks for an empty space in physical memory
     * @return Location of empty memory page or -1
     */
    public int FindEmptyMemory() {
        for (int i = 0; i < ram.length; i++) {
            if (ram[i][0] == -1) {
                return i;
            }
        }

        return -1;
    }
}
