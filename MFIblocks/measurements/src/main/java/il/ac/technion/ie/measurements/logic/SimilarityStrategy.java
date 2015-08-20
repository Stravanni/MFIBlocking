package il.ac.technion.ie.measurements.logic;

import cern.colt.matrix.DoubleMatrix2D;
import il.ac.technion.ie.model.Block;

/**
 * Created by I062070 on 18/06/2015.
 */
public class SimilarityStrategy implements iUpdateMatrixStrategy {
    @Override
    public double getRecordValue(int recordId, Block block) {
        return block.getMemberAvgSimilarity(recordId);
    }

    @Override
    public double calcCurrentCellValue(double currentValue, DoubleMatrix2D matrix, int rowIndex, int colIndex) {
        return Math.max(currentValue, matrix.getQuick(rowIndex, colIndex));
    }

    @Override
    public String getUpdateLogMessage(int rowIndex, int colIndex, double currentValue, double posValue) {
        return String.format("Increasing similiatiry at pos (%d,%d) from by %s to %s",
                rowIndex, colIndex, currentValue, posValue);
    }
}
