import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Kernel {
    static CSVRow reportRow = new CSVRow();

    CPU cpu;
    int[][] ram;

    int instructionCounter = 0;

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
            
            Kernel.reportRow.value = Integer.parseInt(values.get(memoryAddress.offset));

            final int nextMemLoc = FindEmptyMemory();

            // System.out.println("Next Mem: " + nextMemLoc);

            if (values.size() > 256) {
                throw new RuntimeException("Page file: " + memoryAddress.pageFrameHex() + " has length: " + values.size());
            }

            if (nextMemLoc == -1) {
                final int nextPage = findNextAvailableMemoryLocation();
                Kernel.reportRow.evictedPage = nextPage;
                // System.out.println("Next Page: " + nextPage);

                //guard
                if (nextPage == -1) {
                    throw new RuntimeException("No new memory space could be found");
                }

                final PageTableEntry page = cpu.mmu.virtualPageTable.pageTable[nextPage];

                if (page.pageFrame > ram.length || nextMemLoc > ram.length) {
                    throw new RuntimeException("Page frame outside of memory bounds");
                }

                if (page != null && page.d == 1) {
                    //page file needs to be update
                    Kernel.reportRow.evictedPage_dirtyBit = 1;

                    String fileContents = "";
                    for (int item : ram[page.pageFrame]) {
                        if (fileContents.isEmpty()) {
                            fileContents = String.format("%d", item);
                        } else {
                            fileContents += String.format("%n%d", item);
                        }
                    }

                    System.out.println("[Writing to Page File]");
                    try(FileWriter myWriter = new FileWriter("page_files/" + String.format("%02X", page.pageFrame) + ".pg", false)) {
                        myWriter.write(fileContents);
                    } catch (Exception e) {
                        System.out.println("An error occurred while saving your file");
                    }
                }

                cpu.mmu.addToMemory(memoryAddress, page.pageFrame, cpu.tlb);
                
                if (page != null) {
                    for (int i = 0; i < 256; i++) {
                        ram[page.pageFrame][i] = Integer.parseInt(values.get(i));
                    }

                    cpu.tlb.deletePage(memoryAddress);
                    cpu.mmu.virtualPageTable.pageTable[nextPage].pageFrame = -1;
                } else {
                    for (int i = 0; i < values.size(); i++) {
                        ram[nextPage][i] = Integer.parseInt(values.get(i));
                    }
                }
            } else {
                cpu.mmu.addToMemory(memoryAddress, nextMemLoc, cpu.tlb);

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
            if (page != null && page.pageFrame != -1 && page.r == 0) {
                return clockPosition;
            } else if (page != null && page.pageFrame != -1) {
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

    public void RunInstructions(String fileName, String reportName) {
        File file = new File(fileName); 
        Scanner sc;
        try {
            sc = new Scanner(file);
            while (sc.hasNextLine()) {
                Kernel.reportRow = new CSVRow();

                if (Integer.parseInt(sc.nextLine()) == 0) {
                    final String value = sc.nextLine();
                    Kernel.reportRow.address = new Hexadecimal(value);
                    cpu.memoryRead(ram, this, new Hexadecimal(value));
                } else {
                    final String location = sc.nextLine();
                    final int value = Integer.parseInt(sc.nextLine());

                    Kernel.reportRow.value = value;

                    Kernel.reportRow.address = new Hexadecimal(location);
                    cpu.memoryWrite(ram, this, new Hexadecimal(location), value);
                }

                instructionCounter++;

                if (instructionCounter == 10) {
                    for (PageTableEntry page : cpu.mmu.virtualPageTable.pageTable) {
                        if (page != null) {
                            page.r = 0;
                        }
                    }

                    instructionCounter = 0;
                }

                MemorySimulator.report.rows.add(Kernel.reportRow);
            }
    
            sc.close();

            try {
                MemorySimulator.report.saveFile(reportName);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            System.out.println(MemorySimulator.report);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
