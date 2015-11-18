package il.ac.technion.ie.model;

import il.ac.technion.ie.context.MfiContext;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RecordSet {
    private Map<Integer, MfiRecord> values;
    private int numberOfRecords;

    private int minRecordLength = Integer.MAX_VALUE;

    private int dbSize;

    private RecordSet() {
    }

    public static RecordSet factory(MfiContext mfiContext) {
        RecordSet recordSet = new RecordSet();
        recordSet.readRecords(mfiContext);
        return recordSet;
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
            dbSize = values.size();
            numberOfRecords = values.size();

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void setMinRecordLength(int minRecordLength) {
        this.minRecordLength = minRecordLength;
    }

    public void setDBSize(int dbSize) {
        this.dbSize = dbSize;
    }

    public int getDBSize() {
        return this.dbSize;
    }
}
