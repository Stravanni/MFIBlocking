package il.ac.technion.ie.utils;

import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParserSettings;
import il.ac.technion.ie.experiments.parsers.DatasetParser;
import il.ac.technion.ie.model.Record;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

/**
 * Created by I062070 on 10/12/2015.
 */
public class UtilitiesForBlocksAndRecords {
    /**
     * Return a List<String[]> of the parsed file.
     * The first item in the list <code>list.get(0)</code> is a <code>String[]<code/> of headers. The rest
     * of the elements are rows in the file
     *
     * @param pathToFile Full path in File System to a CSV file that contains records
     * @return List<String[]> where the first record is the field names and the rest are the records
     */
    public static List<String[]> readRecordsFromTestFile(String pathToFile) {
        // The settings object provides many configuration options
        CsvParserSettings parserSettings = new CsvParserSettings();

        //You can configure the parser to automatically detect what line separator sequence is in the input
        parserSettings.setLineSeparatorDetectionEnabled(true);

        // A RowListProcessor stores each parsed row in a List.
        RowListProcessor rowProcessor = new RowListProcessor();

        // You can configure the parser to use a RowProcessor to process the values of each parsed row.
        // You will find more RowProcessors in the 'com.univocity.parsers.common.processor' package, but you can also create your own.
        parserSettings.setRowProcessor(rowProcessor);

        parserSettings.setEmptyValue(StringUtils.EMPTY);
        parserSettings.setNullValue(StringUtils.EMPTY);

        // Let's consider the first parsed row as the headers of each column in the file.
        parserSettings.setHeaderExtractionEnabled(true);

        DatasetParser datasetParser = new DatasetParser();
        datasetParser.getParserForFile(pathToFile, parserSettings);

        // get the parsed records from the RowListProcessor here.
        // Note that different implementations of RowProcessor will provide different sets of functionalities.
        String[] headers = rowProcessor.getHeaders();
        List<String[]> rows = rowProcessor.getRows();
        List<String[]> results = new ArrayList<>();
        results.add(headers);
        results.addAll(rows);
        return results;
    }

    public static List<Record> createRecordsFromTestFile(String pathToBigRecordsFile) {
        List<String[]> strings = readRecordsFromTestFile(pathToBigRecordsFile);
        List<Record> records = new ArrayList<>(strings.size());
        List<String> fieldNames = convertArrayToList(strings.get(0));
        for (int i = 1; i < strings.size(); i++) { //skipping first element since it is the field names
            List<String> values = convertArrayToList(strings.get(i));
            Record record = new Record(fieldNames, values, i);
            records.add(record);
        }
        return records;
    }

    private static List<String> convertArrayToList(String[] array) {
        List<String> list = new ArrayList<>(Arrays.asList(array));
//        list.remove(0);
        return list;
    }

    public static String getPathToSmallRecordsFile() throws URISyntaxException {
        String pathToFile = "/20Records.csv";
        File file = getFileFromResourceDir(pathToFile);
        return file.getAbsolutePath();
    }

    public static File getFileFromResourceDir(String pathToFile) throws URISyntaxException {
        URL resourceUrl = UtilitiesForBlocksAndRecords.class.getResource(pathToFile);
        return new File(resourceUrl.toURI());
    }

    public static String getPathToBigRecordsFile() throws URISyntaxException {
        String pathToFile = "/1kRecords.csv";
        File file = getFileFromResourceDir(pathToFile);
        return file.getAbsolutePath();
    }

    public static List<Record> getRecordsFromCsv() throws URISyntaxException {
        //read records from CSV file
        String pathToSmallRecordsFile = getPathToSmallRecordsFile();
        List<Record> records = createRecordsFromTestFile(pathToSmallRecordsFile);
        assertThat(records, hasSize(20));
        return records;
    }
}
