package fimEntityResolution;

import il.ac.technion.ie.model.CandidatePairs;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class TrueClusters {

    private static final String TRANSACTION_SEPARATOR = " ";
    private int cardinality;
	private CandidatePairs cps;
	
	static final Logger logger = Logger.getLogger(TrueClusters.class);

    public static TrueClusters createTrueClusters(String clustersFile) {
        TrueClusters trueClusters = new TrueClusters();
        trueClusters.findClustersAssignments(clustersFile);
        return trueClusters;
    }

    public CandidatePairs getGroundTruthCandidatePairs() {
        return cps;
	}
	
	public int getCardinality(){
		return cardinality;
	}

    private TrueClusters() {
        cps = new CandidatePairs(); //no limit
		cardinality = 0;
	}

    private void findClustersAssignments(String clustersFile) {
        try {
			List<String> lines = Files.readAllLines(Paths.get(clustersFile), Charset.defaultCharset());
			
			for (String line : lines) {
                String[] transactions = line.trim().split(TRANSACTION_SEPARATOR);

                List<Integer> cluster = new ArrayList<>(transactions.length);
                for (String transaction : transactions) {
					Integer transactionKey = Integer.parseInt(transaction);					
					cluster.add(transactionKey);
				}
				
				if (cluster.size() < 2) {
					logger.fatal("A cluster for a single record was created");
				}
				
				assosiateRecordsAsMatching(cluster);
			}
			System.out.println("num of pairs: groundTruth.cardinality "  + cardinality);
		} catch (IOException e) {
			logger.error("Failed to read true clusters from: " + clustersFile, e);
			e.printStackTrace();
		}
	}

	private void assosiateRecordsAsMatching(List<Integer> cluster) {
		for (int i = 0; i < cluster.size(); i++) {
			for (int j = i + 1; j < cluster.size(); j++) {
				cps.setPair(cluster.get(i), cluster.get(j), 0.0);
				cardinality++;
			}
		}
	}

}
