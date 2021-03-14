# Memory Management Simulator

This is a modular memory management simulation that takes in some instructions and some page files.

## Simulated architecture

- 16 bit CPU
- 12 bit RAM
- 8 bit page offset
- 16 entry TLB
- OS uses clock algorithm for replacement
- MMU uses FIFO for replacement
- OS resets the r-bits every 10 instructions

## Building and Running Instructions

- Navigate to source code folder in command line
- Copy page files to a folder `page_files`
- `javac MemorySimulator.java`
- `java MemorySimulator testFile.txt reportName.csv`