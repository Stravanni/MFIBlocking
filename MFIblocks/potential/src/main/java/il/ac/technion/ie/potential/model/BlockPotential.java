package il.ac.technion.ie.potential.model;

import il.ac.technion.ie.model.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XPS_Sapir on 09/07/2015.
 */
public class BlockPotential {

    private Map<Integer, Double> potential;

    public BlockPotential(Block block) {
        this.potential = new HashMap<>();

        for (Integer recordId : block.getMembers()) {
            double memberScore = (double)block.getMemberScore(recordId);
            potential.put(recordId, Math.log(memberScore));
        }
    }

    public List<Double> getPotentialValues() {
        return new ArrayList<>(potential.values());
    }
}
