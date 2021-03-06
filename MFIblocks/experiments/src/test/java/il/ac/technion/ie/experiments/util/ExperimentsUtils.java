package il.ac.technion.ie.experiments.util;

import il.ac.technion.ie.experiments.model.BlockWithData;
import il.ac.technion.ie.experiments.service.FuzzyService;
import il.ac.technion.ie.experiments.service.ParsingService;
import il.ac.technion.ie.experiments.service.ProbabilityService;
import il.ac.technion.ie.utils.UtilitiesForBlocksAndRecords;
import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.mockito.PowerMockito;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by I062070 on 24/08/2015.
 */
public class ExperimentsUtils {
    public static List<String> hugeStringToList(String huge) {
        String[] strings = hugeStringToArray(huge);
        return new ArrayList<>( Arrays.asList(strings) );
    }

    public static String[] hugeStringToArray(String huge) {
        return huge.split(",");
    }

    public static File getUaiFile() throws URISyntaxException {
        String pathToFile = "/uaiFile.uai";
        return getFileFromResourceDir(pathToFile);
    }

    public static File getBinaryFile() throws URISyntaxException {
        String pathToFile = "/uaiBinaryFormat.txt";
        return getFileFromResourceDir(pathToFile);
    }

    private static File getFileFromResourceDir(String pathToFile) throws URISyntaxException {
        URL resourceUrl = ExperimentsUtils.class.getResource(pathToFile);
        return new File(resourceUrl.toURI());
    }

    public static List<BlockWithData> createFuzzyBlocks() throws Exception {
        String recordsFile = UtilitiesForBlocksAndRecords.getPathToSmallRecordsFile();

        ParsingService parsingService = new ParsingService();
        ProbabilityService probabilityService = new ProbabilityService();
        FuzzyService fuzzyService = initFuzzyService();

        List<BlockWithData> originalBlocks = parsingService.parseDataset(recordsFile);
        probabilityService.calcSimilaritiesAndProbabilitiesOfRecords(originalBlocks);

        List<BlockWithData> copyOfOriginalBlocks = new ArrayList<>(originalBlocks);
        Map<Integer, Double> splitProbMap = PowerMockito.mock(Map.class);
        PowerMockito.when(splitProbMap.size()).thenReturn(originalBlocks.size());
        List<BlockWithData> fuzzyBlocks = fuzzyService.splitBlocks(copyOfOriginalBlocks, splitProbMap, 0.6);
        probabilityService.calcSimilaritiesAndProbabilitiesOfRecords(fuzzyBlocks);

        return fuzzyBlocks;
    }

    private static FuzzyService initFuzzyService() throws Exception {
        FuzzyService fuzzyService = PowerMock.createPartialMock(FuzzyService.class, "getSplitProbability");
        PowerMock.expectPrivate(fuzzyService, "getSplitProbability", EasyMock.anyObject(Map.class), EasyMock.anyObject(BlockWithData.class))
                .andReturn(0.3).andReturn(0.7).andReturn(0.6).andReturn(0.4);

        PowerMock.replay(fuzzyService);

        return fuzzyService;
    }

}
