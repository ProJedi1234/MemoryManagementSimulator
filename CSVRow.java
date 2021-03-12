public class CSVRow {
    Hexadecimal address;
    int value;
    boolean softMiss, hardMiss, hit;
    int evictedPage;
    int evictedPage_dirtyBit;

    public CSVRow() {
        address = new Hexadecimal("0000");
        value = -1;
        softMiss = hardMiss = hit = false;
        evictedPage = 0;
        evictedPage_dirtyBit = 0;
    }
    public CSVRow(Hexadecimal address, int value, boolean softMiss, boolean hardMiss, 
        boolean hit, int evictedPage, int evictedPage_dirtyBit) {
        this.address = address;
        this.value = value;
        this.softMiss = softMiss;
        this.hardMiss = hardMiss;
        this.hit = hit;
        this.evictedPage = evictedPage;
        this.evictedPage_dirtyBit = evictedPage_dirtyBit;
    }

    @Override
    public String toString() {
        return address.getHex() + "," + value + "," + getStringForBool(softMiss) 
            + "," + getStringForBool(hardMiss) + "," + getStringForBool(hit)
            + "," + evictedPage + "," + evictedPage_dirtyBit;
    }

    private String getStringForBool(boolean value) {
        return value ? "1" : "0";
    }
}
