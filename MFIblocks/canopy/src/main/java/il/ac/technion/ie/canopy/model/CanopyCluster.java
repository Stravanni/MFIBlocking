package il.ac.technion.ie.canopy.model;

import il.ac.technion.ie.canopy.exception.CanopyParametersException;
import il.ac.technion.ie.canopy.utils.CanopyUtils;
import il.ac.technion.ie.model.CanopyRecord;
import il.ac.technion.ie.model.Record;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by I062070 on 22/11/2015.
 */
public class CanopyCluster implements Serializable{
    private final Collection<CanopyRecord> candidateRecords;
    private final double t2;
    private final double t1;
    private final double range;
    private final double minScore;
    private List<CanopyRecord> allRecords;
    private List<CanopyRecord> tightRecords;

    private static final Logger logger = Logger.getLogger(CanopyCluster.class);


    private CanopyCluster(Collection<CanopyRecord> candidateRecordsForCanopy, double t2, double t1) throws CanopyParametersException {
        CanopyUtils.assertT1andT2(t1, t2);
        candidateRecords = candidateRecordsForCanopy;
        logger.debug("Obtained " + candidateRecordsForCanopy.size() + " as candidates for canopy");
        double localMaxScore = 0;
        double localMinScore = 0;
        for (CanopyRecord canopyRecord : candidateRecordsForCanopy) {
            localMinScore = Math.min(localMinScore, canopyRecord.getScore());
            localMaxScore = Math.max(localMaxScore, canopyRecord.getScore());
        }
        logger.debug("local Max score is: " + localMaxScore);
        logger.debug("local Min score is: " + localMinScore);
        this.range = localMaxScore - localMinScore;
        this.minScore = localMinScore;
        this.t2 = t2;
        this.t1 = t1;
        allRecords = new ArrayList<>();
        tightRecords = new ArrayList<>();
    }

    public static CanopyCluster newCanopyCluster(Collection<CanopyRecord> candidateRecordsForCanopy, double t2, double t1) throws CanopyParametersException {
        CanopyCluster canopyCluster = new CanopyCluster(candidateRecordsForCanopy, t2, t1);
        canopyCluster.removeRecordsBelowT2();
        canopyCluster.removeRecordsBelowT1();
        return canopyCluster;
    }

    public void removeRecordsBelowT2() {
        double normRange = convertThreshold(t2);
        logger.debug("The normalized T2 value is: " + normRange);
        logger.debug("Removing records whose score is below T2 parameter");
        removeLessThanLooseRecords(normRange, allRecords);
        candidateRecords.clear();
    }

    public void removeRecordsBelowT1() {
        double normRange = convertThreshold(t1);
        logger.debug("The normalized T1 value is: " + normRange);
        logger.debug("Removing records whose score is below T1 parameter");
        for (CanopyRecord canopyRecord : allRecords) {
            if (canopyRecord.getScore() >= normRange) {
                logger.trace(canopyRecord + " was added to a collection");
                tightRecords.add(canopyRecord);
            }
        }
    }

    private void removeLessThanLooseRecords(double normRange, Collection<CanopyRecord> collection) {
        for (CanopyRecord canopyRecord : candidateRecords) {
            if (canopyRecord.getScore() >= normRange) {
                logger.trace(canopyRecord + " was added to a collection");
                collection.add(canopyRecord);
            }
        }
    }

    public List<CanopyRecord> getAllRecords() {
        return Collections.unmodifiableList(allRecords);
    }

    public List<CanopyRecord> getTightRecords() {
        return Collections.unmodifiableList(tightRecords);
    }

    private double convertThreshold(double threshold) {
        return range * threshold + minScore;
    }

    public boolean contains(Record record) {
        return allRecords.contains(record);
    }

    public int size() {
        return this.getAllRecords().size();
    }
}
