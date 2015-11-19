package il.ac.technion.ie.dataset.prepare.context;

import java.io.File;
import java.util.List;

/**
 * Created by I062070 on 19/11/2015.
 */
public interface IDatasetProperties {

    File getDataset();

    List<String> getStopwords();

    int getQgramSize();

    int getNumberOfRecords();

    int getPercentegeToPrune();

    File getArtifactDestFolder();
}
