package il.ac.technion.ie.utils;

import il.ac.technion.ie.data.structure.Clearer;
import il.ac.technion.ie.data.structure.IFRecord;
import il.ac.technion.ie.model.BitSetIF;
import il.ac.technion.ie.pools.FIRunnableDBPool;
import il.ac.technion.ie.pools.GDSPool;

import java.util.List;
import java.util.Map;


public class FIRunnableDB implements Runnable, Clearer {

	private List<Integer> currIS = null;
	private int minSup;
    private double NG_PARAM;
    private Map<Integer, GDS_NG> coverageIndexDB;

	public FIRunnableDB(List<Integer> currIS, int minSup,
                        double NG_PARAM,
                        Map<Integer, GDS_NG> coverageIndexDB) {
		this.currIS = currIS;
		this.minSup = minSup;
        this.NG_PARAM = NG_PARAM;
        this.coverageIndexDB = coverageIndexDB;
	}

	public void setParams(List<Integer> currIS, int minSup,
                          double NG_PARAM,
                          Map<Integer, GDS_NG> coverageIndexDB) {
		this.currIS = currIS;
		this.minSup = minSup;
        this.NG_PARAM = NG_PARAM;
        this.coverageIndexDB = coverageIndexDB;
	}

	@Override
	public void run() {
		BitSetIF support = Utilities.getItemsetSupport(currIS);
		if (support.getCardinality() < (long) minSup) {
			Utilities.nonFIs.incrementAndGet();				
			return; // must be the case that the item appears minSup
					// times but in a number of records < minsup
		}
		List<IFRecord> FISupportRecords = support.getRecords();

		long start = System.currentTimeMillis();
		double currClusterScore = StringSimTools.softTFIDF(
                FISupportRecords, currIS, Utilities.scoreThreshold);
        Utilities.timeSpentCalcScore
				.addAndGet(System.currentTimeMillis() - start);
		Utilities.clusterScores[cellForCluster(currClusterScore)]
				.incrementAndGet();

		if (currClusterScore > Utilities.scoreThreshold) {
			Utilities.numOfFIs.incrementAndGet();
			int clusterCell = cellForCluster(currClusterScore);

			synchronized (FIRunnableDB.class) {
				int begIndex = Utilities.getIntForThresh(Utilities.scoreThreshold);
				for (int i = begIndex; i <= clusterCell; i++) {
					GDS_NG gds = coverageIndexDB.get(i);
					if (gds == null) {
						gds = GDSPool.getInstance().getGDS(
								NG_PARAM * minSup);
						Utilities.numOfGDs.incrementAndGet();
					}
					support.markPairs(gds,currClusterScore,currIS);						
					/*
					 * NG constraint compromised: this means that a
					 * cluster of score currClusterScore caused the
					 * breach of the NG constraint on the matrix
					 * representing Clusters with scores above
					 * i*BottomUp.THRESH_STEP. This measn that that
					 * matrix cannot possibly represent the blocking
					 * result of our algorithm. The meaning is twofold:
					 * 1. There is not need to continue marking in this
					 * matrix 2. We know that the threshold needs to be
					 * increased because the set of clusters above score
					 * i*BottomUp.THRESH_STEP create large neighborhoods
					 */
					if (gds.getMaxNG() > NG_PARAM * minSup) {
						double prevThresh = Utilities.scoreThreshold;
						Utilities.scoreThreshold = i
								* Constants.THRESH_STEP;

						// remove all BMs located below the new
						// threshold
						int delIndex = Utilities.getIntForThresh(Utilities.scoreThreshold) - 1;
						for (int k = Utilities.getIntForThresh(prevThresh); k <= delIndex; k++) {
							if (coverageIndexDB.containsKey(k)) {
								GDS_NG removed = coverageIndexDB
										.remove(k);
								if (removed != null) {
									removed.endUse();
									GDSPool.getInstance().returnGDS(
											removed);
								}
							}
						}
					} else {
						coverageIndexDB.put(i, gds);
					}
				}
			}
		}			
		FIRunnableDBPool.getInstance().returnRunnable(this); // completed
																// running
	}

	@Override
	public void clearAll() {
		this.setParams(null, 0, 0, null);
	}

	private static int cellForCluster(final double score) {
		return (int) Math.ceil(score / 0.05);
	}

}