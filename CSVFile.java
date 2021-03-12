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

    public Float getAverageCompletionTime() {
        Float total = 0f;
        int count = 0;
        for (CSVRow row : rows) {
            if (row.Complete != 0) {
                total += row.Complete;
                count++;
            }
        }

        return count == 0 ? 0 : total/count;
    }

    @Override
    public String toString() {
        String toReturn = "";

        toReturn += "Proc Time,PID,Start Burst,End Burst, Complete\n";

        for (CSVRow row : rows) {
            toReturn += row.toString() + "\n";
        }

        toReturn += "Average,,,," + getAverageCompletionTime();

        return toReturn;
    }
    public CSVRow first() {
        return rows.get(0);
    }
    public CSVRow last() {
        return rows.get(rows.size() - 1);
    }
}
