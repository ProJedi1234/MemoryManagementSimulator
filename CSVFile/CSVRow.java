package CSVFile;

public class CSVRow {
    int processedTime,
    PID,
    StartBurst,
    EndBurst,
    Complete;

    public CSVRow() {
        processedTime = 0;
        PID = 0;
        StartBurst = 0;
        EndBurst = 0;
        Complete = 0;
    }
    public CSVRow(int processedTime, int PID, int StartBurst, int EndBurst, int Complete) {
        this.processedTime = processedTime;
        this.PID = PID;
        this.StartBurst = StartBurst;
        this.EndBurst = EndBurst;
        this.Complete = Complete;
    }

    @Override
    public String toString() {
        return String.valueOf(processedTime) + "," + String.valueOf(PID) + "," + String.valueOf(StartBurst)
            + "," + String.valueOf(EndBurst) + "," + String.valueOf(Complete);
    }
}
