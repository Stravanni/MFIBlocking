package il.ac.technion.ie.dataset.prepare.model;

import il.ac.technion.ie.dataset.prepare.exception.InvalidSizeException;
import il.ac.technion.ie.dataset.prepare.utils.Util;

/**
 * Created by I062070 on 19/11/2015.
 */
public class ColumnMetaData {

    public static final int METADATA_SIZE = 3;
    private int id;
    private double weight;
    private int length;

    public ColumnMetaData(String[] values) throws InvalidSizeException {
        if (values == null || values.length != METADATA_SIZE) {
            throw new InvalidSizeException("Size of metadata should be only: " + METADATA_SIZE);
        }
        id = Util.convertStringToInt(values[0]);
        weight = Util.convertStringToDouble(values[1]);
        length = Util.convertStringToInt(values[2]);
    }


    public int getID() {
        return id;
    }

    public double getWeight() {
        return weight;
    }

    public int getLength() {
        return length;
    }
}
