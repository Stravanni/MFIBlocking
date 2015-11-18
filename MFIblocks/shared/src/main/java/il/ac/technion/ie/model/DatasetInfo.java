package il.ac.technion.ie.model;

import au.com.bytecode.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by I062070 on 18/11/2015.
 */
public class DatasetInfo {

    private int numberOfColumns;
    private String[] columnNames;
    private List<String[]> records;

    private DatasetInfo() {
        records = new ArrayList<>();
    }

    public static DatasetInfo createDatasetInfo(String fileName) throws IOException {
        DatasetInfo datasetInfo = new DatasetInfo();
        datasetInfo.loadOriginalRecordsFromCSV(fileName);
        return datasetInfo;
    }


    private void loadOriginalRecordsFromCSV(String filename) throws IOException {
        CSVReader cvsReader = new CSVReader(new FileReader(new File(filename)));

        String[] currLine;
        boolean first = true;
        while ((currLine = cvsReader.readNext()) != null) {
            if (first) {
                columnNames = currLine;
                this.numberOfColumns = currLine.length;
                first = false;
                continue;
            } else {
                records.add(currLine);
            }
        }
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public synchronized String[] getColumnNames() {
        return Arrays.copyOf(columnNames, columnNames.length);
    }

    public String[] getRecordByIndex(int index) {
        return records.get(index);
    }
}
