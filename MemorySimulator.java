import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import Exceptions.*;

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

        File file = new File("test_files/test_1.txt"); 
        Scanner sc;
        try {
            sc = new Scanner(file);
            while (sc.hasNextLine()) {
                if (Integer.parseInt(sc.nextLine()) == 0) {
                    final String value = sc.nextLine();
                    memoryRead(cpu, ram, os, new Hexadecimal(value));
                } else {
                    final String location = sc.nextLine();
                    final int value = Integer.parseInt(sc.nextLine());

                    memoryWrite(cpu, ram, os, new Hexadecimal(location), value);
                }
            }
    
            sc.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void memoryRead(CPU cpu, int[][] ram, Kernel os, Hexadecimal memoryLocation) {
        try {
            final Hexadecimal location = cpu.readMemory(memoryLocation);
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
    public static void memoryWrite(CPU cpu, int[][] ram, Kernel os, Hexadecimal memoryLocation, int value) {
        try {
            final Hexadecimal location = cpu.readMemory(memoryLocation);
            ram[location.pageFrame][location.offset] = value;

            System.out.println("[Memory updated]");
        } catch (PageFault e) {
            System.out.println("[Page fault]");
            os.handlePageFault(memoryLocation);
        }

        cpu.mmu.setDirtyBit(memoryLocation);
    }
}