import java.io.FileWriter;
import java.util.ArrayList;

public class CSVFile {
    ArrayList<CSVRow> rows;

    public CSVFile() {
        rows = new ArrayList<CSVRow>();
    }
    public CSVFile(ArrayList<CSVRow> rows) {
        this.rows = rows;
    }

    public void saveFile(String fileName) throws Exception {
        try(FileWriter myWriter = new FileWriter(fileName)) {
            myWriter.write(toString());
        } catch (Exception e) {
            System.out.println("An error occurred while saving your file");
        }
    }

    @Override
    public String toString() {
        String toReturn = "";

        toReturn += "Address,Value,Soft Miss,Hard Miss, Hit, Evicted Page, Dirty Bit\n";

        for (CSVRow row : rows) {
            toReturn += row.toString() + "\n";
        }

        return toReturn;
    }
    public CSVRow first() {
        return rows.get(0);
    }
    public CSVRow last() {
        return rows.get(rows.size() - 1);
    }
}
