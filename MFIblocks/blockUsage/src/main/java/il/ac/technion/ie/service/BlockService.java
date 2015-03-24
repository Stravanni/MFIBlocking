package il.ac.technion.ie.service;

import il.ac.technion.ie.model.Block;
import il.ac.technion.ie.model.CandidatePairs;
import il.ac.technion.ie.exception.NotImplementedYetException;
import il.ac.technion.ie.logic.BlockLogic;
import il.ac.technion.ie.logic.iBlockLogic;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by I062070 on 13/03/2015.
 */
public class BlockService implements iBlockService {

    static final Logger logger = Logger.getLogger(BlockService.class);

    private iBlockLogic blockLogic;

    public BlockService() {
        this.blockLogic = new BlockLogic();
    }
    @Override
    public List<Block> getBlocks(CandidatePairs candidatePairs) {
        List<Block> result = blockLogic.findBlocks(candidatePairs);
        logger.debug("Finished finding blocks from input");
        return result;
    }

    @Override
    public List<Integer> getBlocksOfRecord(CandidatePairs candidatePairs, int record) {
        String message = String.format("The method %s of class %s it not implemented yet", "getBlocksOfRecord", this.getClass().getSimpleName());
        NotImplementedYetException exception = new NotImplementedYetException(message);
        logger.error(message, exception);
        throw exception;
    }
}