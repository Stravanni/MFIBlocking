package il.ac.technion.ie.context;

import il.ac.technion.ie.model.Block;

import java.util.List;

/**
 * Created by I062070 on 18/11/2015.
 */
public class MfiResult {
    private final List<Block> trueBlocks;
    private final List<Block> algorithmBlocks;

    public MfiResult(List<Block> algorithmBlocks, List<Block> trueBlocks) {
        this.algorithmBlocks = algorithmBlocks;
        this.trueBlocks = trueBlocks;
    }

    public List<Block> getTrueBlocks() {
        return trueBlocks;
    }

    public List<Block> getAlgorithmBlocks() {
        return algorithmBlocks;
    }
}
