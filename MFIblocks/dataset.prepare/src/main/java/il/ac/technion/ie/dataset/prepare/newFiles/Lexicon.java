package il.ac.technion.ie.dataset.prepare.newFiles;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import il.ac.technion.ie.dataset.prepare.context.PropertiesHolder;
import il.ac.technion.ie.dataset.prepare.exception.InvalidSizeException;
import il.ac.technion.ie.dataset.prepare.model.ColumnMetaData;
import il.ac.technion.ie.dataset.prepare.utils.Util;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by I062070 on 19/11/2015.
 */
public class Lexicon {

    private static final Logger logger = Logger.getLogger(Lexicon.class);

    private BiMap<Integer, Integer> columnIndexToId;
    private Map<Integer, ColumnMetaData> idsToColumnMetaData;

    public Lexicon(String PathToWeightsPropertiesFile) throws FileNotFoundException {
        columnIndexToId = HashBiMap.create();
        idsToColumnMetaData = new HashMap<>();

        PropertiesHolder propertiesHolder = PropertiesHolder.factory(PathToWeightsPropertiesFile);
        init(propertiesHolder.getPropertiesConfiguration());
    }

    private void init(PropertiesConfiguration propertiesHolder) {
        Iterator<String> keys = propertiesHolder.getKeys();
        while (keys.hasNext()) {
            String colIndex = keys.next();
            String[] values = propertiesHolder.getStringArray(colIndex);
            try {
                ColumnMetaData columnMetaData = new ColumnMetaData(values);
                columnIndexToId.put(Util.convertStringToInt(colIndex), columnMetaData.getID());
                idsToColumnMetaData.put(columnMetaData.getID(), columnMetaData);
            } catch (InvalidSizeException e) {
                e.printStackTrace();
            }
        }
    }


    public BiMap<Integer, Integer> getColumnIndexToId() {
        return columnIndexToId;
    }

    public double getWeightByIndex(int i) {
        return 0;
    }
}
