package il.ac.technion.ie.model;

import au.com.bytecode.opencsv.CSVReader;
import il.ac.technion.ie.context.MfiContext;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RecordSet {
    private Map<Integer, MfiRecord> values;
    private String[][] originalRecords;
    private String[] columnNames;
    private int numberOfRecords;

    private int minRecordLength = Integer.MAX_VALUE;

    private int SCHEMA_SIZE;

    private RecordSet() {
    }

    public static RecordSet factory(MfiContext mfiContext) {
        RecordSet recordSet = new RecordSet();
        recordSet.readRecords(mfiContext);
        return recordSet;
    }

    /*public  void setRecords(Map<Integer, MfiRecord> records) {
        values = records;
        numberOfRecords = values.size();
    }*/


    public void loadOriginalRecordsFromCSV(String filename) throws IOException {
        originalRecords = new String[numberOfRecords][SCHEMA_SIZE];
        CSVReader cvsReader;

        cvsReader = new CSVReader(new FileReader(new File(filename)));

        String[] currLine;
        int recordId = 1;
        boolean first = true;
        String[] attNames = null;
        while ((currLine = cvsReader.readNext()) != null) {
            if (first) {
                attNames = currLine;
                first = false;
                continue;
            }
            SCHEMA_SIZE = attNames.length;
            originalRecords[recordId - 1] = currLine;
            recordId++;
        }
        columnNames = attNames;
    }

    public void readRecords(MfiContext context) {
        String numericRecordsFile = context.getRecordsFile();
        String origRecordsFile = context.getOriginalFile();
        String srcFile = context.getRecordsFile();
        values = new HashMap<>();
        try {
            BufferedReader recordsFileReader = new BufferedReader(
                    new FileReader(new File(numericRecordsFile)));
            BufferedReader origRecordsFileReader = new BufferedReader(
                    new FileReader(new File(origRecordsFile)));
            BufferedReader srcFileReader = null;
            if (StringUtils.isNotEmpty(srcFile)) {
                srcFileReader = new BufferedReader(new FileReader(new File(srcFile)));
            }
            System.out.println("readRecords: srcFile = " + srcFile);

            String numericLine = "";
            String recordLine;
            Pattern ws = Pattern.compile("[\\s]+");
            int recordIndex = 1;
            while (numericLine != null) {
                try {
                    numericLine = recordsFileReader.readLine();
                    if (numericLine == null) {
                        break;
                    }
                    numericLine = numericLine.trim();
                    recordLine = origRecordsFileReader.readLine().trim();
                    String src = null;
                    if (srcFileReader != null) {
                        src = srcFileReader.readLine().trim();
                    }
                    MfiRecord r = new MfiRecord(recordIndex, recordLine);
                    r.setSrc(src); // in the worst case this is null
                    String[] words = ws.split(numericLine);
                    if (numericLine.length() > 0) { // very special case when
                        // there is an empty line
                        for (String word : words) {
                            int item = Integer.parseInt(word);
                            r.addItem(item);
                        }
                    }
                    minRecordLength = Math.min(r.getSize(), minRecordLength);
                    values.put(r.getId(), r);
                    recordIndex++;
                } catch (Exception e) {
                    System.out.println("Exception while reading line " + recordIndex + ":" + numericLine);
                    System.out.println(e);
                    break;
                }
            }
            recordsFileReader.close();
            System.out.println("Num of records read: " + values.size());
//            DB_SIZE = values.size();
            numberOfRecords = values.size();

        } catch (IOException e) {
            e.printStackTrace();
        }
//        this.setRecords(values);
        System.out.println("number Of Records is: " + numberOfRecords);
    }

    public int getNumberOfRecords() {
        return numberOfRecords;
    }


    public MfiRecord getRecordByKey(int index) {
        return this.values.get(index);
    }

    public int getMinRecordLength() {
        return minRecordLength;
    }
}
