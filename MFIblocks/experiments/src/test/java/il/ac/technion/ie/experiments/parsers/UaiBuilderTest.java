package il.ac.technion.ie.experiments.parsers;

import com.google.common.collect.*;
import il.ac.technion.ie.experiments.model.BlockWithData;
import il.ac.technion.ie.experiments.model.UaiVariableContext;
import il.ac.technion.ie.model.Record;
import il.ac.technion.ie.potential.model.MatrixCell;
import il.ac.technion.ie.potential.model.SharedMatrix;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.List;
import java.util.Map;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UaiBuilder.class, UaiVariableContext.class, SharedMatrix.class})
public class UaiBuilderTest {

    private UaiBuilder classUnderTest;

    @Before
    public void setUp() throws Exception {
        classUnderTest = new UaiBuilder(PowerMockito.mock(List.class));
    }

    @Test
    public void testCountCliques() throws Exception {

        List<MatrixCell<Double>> cells = Lists.newArrayList(new MatrixCell<>(1, 2, 0.2), new MatrixCell<>(1, 3, 0.2), new MatrixCell<>(1, 5, 0.2),
                new MatrixCell<>(2, 1, 0.2), new MatrixCell<>(2, 3, 0.2),
                new MatrixCell<>(3, 1, 0.2), new MatrixCell<>(3, 2, 0.2), new MatrixCell<>(3, 5, 0.2),
                new MatrixCell<>(5, 1, 0.2), new MatrixCell<>(5, 3, 0.2));
        int numberOfCliques = Whitebox.invokeMethod(classUnderTest, "countCliques", cells);
        MatcherAssert.assertThat(numberOfCliques, Matchers.is(cells.size() / 2));
    }

    @Test
    public void testCountNumberOfVariables() throws Exception {
        //prepare data for test
        Record record_1 = PowerMockito.mock(Record.class);
        PowerMockito.when(record_1.getRecordID()).thenReturn(1);
        Record record_2 = PowerMockito.mock(Record.class);
        PowerMockito.when(record_2.getRecordID()).thenReturn(2);
        Record record_3 = PowerMockito.mock(Record.class);
        PowerMockito.when(record_3.getRecordID()).thenReturn(3);
        Record record_4 = PowerMockito.mock(Record.class);
        PowerMockito.when(record_4.getRecordID()).thenReturn(4);
        Record record_5 = PowerMockito.mock(Record.class);
        PowerMockito.when(record_5.getRecordID()).thenReturn(5);
        Record record_6 = PowerMockito.mock(Record.class);
        PowerMockito.when(record_6.getRecordID()).thenReturn(6);
        Record record_7 = PowerMockito.mock(Record.class);
        PowerMockito.when(record_7.getRecordID()).thenReturn(7);
        Record record_8 = PowerMockito.mock(Record.class);
        PowerMockito.when(record_8.getRecordID()).thenReturn(8);
        Record record_9 = PowerMockito.mock(Record.class);
        PowerMockito.when(record_9.getRecordID()).thenReturn(9);

        BlockWithData block_1 = new BlockWithData(Lists.newArrayList(record_1, record_2, record_4));
        BlockWithData block_2 = new BlockWithData(Lists.newArrayList(record_1, record_3, record_5));
        BlockWithData block_3 = new BlockWithData(Lists.newArrayList(record_7, record_8));
        BlockWithData block_4 = new BlockWithData(Lists.newArrayList(record_7, record_9));

        List<BlockWithData> blocks = Lists.newArrayList(block_1, block_2, block_3, block_4);
        Whitebox.setInternalState(classUnderTest, "blocks", blocks);

        //execute tested method
        int numberOfVariables = Whitebox.invokeMethod(classUnderTest, "countNumberOfVariables");

        //there are 4 blocks and 2 cliques
        MatcherAssert.assertThat(numberOfVariables, Matchers.is(6));
    }

    @Test
    public void testWriteVariableSizeAndIndecies() throws Exception {
        Multimap<Integer, Integer> multimap = ArrayListMultimap.create();
        multimap.put(1, 0);
        multimap.put(1, 1);
        multimap.putAll(1, Lists.newArrayList(2, 3, 4, 5));
        multimap.put(1, 6);
        multimap.put(2, 0);
        multimap.put(2, 1);
        multimap.put(2, 0);
        multimap.put(2, 2);
        multimap.put(2, 2);
        multimap.put(2, 3);

        String expected = "1 0\n" +
                "1 1\n" +
                "1 2\n" +
                "1 3\n" +
                "1 4\n" +
                "1 5\n" +
                "1 6\n" +
                "2 0 1\n" +
                "2 0 2\n" +
                "2 2 3";
        String sizeAndIndecies = Whitebox.invokeMethod(classUnderTest, "buildStringOfVariableSizeAndIndecies", multimap);
        MatcherAssert.assertThat(sizeAndIndecies, Matchers.equalToIgnoringWhiteSpace(expected));
    }

    @Test
    public void testBuildStringOfBlocksAndProbabilities() throws Exception {
        //mocking
        UaiVariableContext variableContext = PowerMockito.mock(UaiVariableContext.class);
        TreeMultimap<Integer, Integer> variableIdToBlocksMultimap = TreeMultimap.create();
        variableIdToBlocksMultimap.put(1, 11);
        variableIdToBlocksMultimap.put(0, 10);
        variableIdToBlocksMultimap.put(3, 13);
        variableIdToBlocksMultimap.put(2, 12);
        variableIdToBlocksMultimap.put(4, 14);
        Map<Integer, Integer> map = Maps.newHashMap(ImmutableMap.of(0, 10, 1, 11, 2, 12, 4, 14));
        BiMap<Integer, Integer> variableIdToBlockId = HashBiMap.create(map);

        PowerMockito.when(variableContext.getVariablesIdsSorted()).thenReturn(Lists.newArrayList(0, 1, 2, 3, 4));
        PowerMockito.when(variableContext.getVariableIdToBlockId()).thenReturn(variableIdToBlockId);

        PowerMockito.when(variableContext.getProbsOfBlockByID(Mockito.eq(10)))
                .thenReturn(Lists.newArrayList(0.1, 0.2, 0.3, 0.4));
        PowerMockito.when(variableContext.getSizeOfBlockById(Mockito.eq(10))).thenReturn(4);


        PowerMockito.when(variableContext.getProbsOfBlockByID(Mockito.eq(11)))
                .thenReturn(Lists.newArrayList(0.2, 0.3, 0.5));
        PowerMockito.when(variableContext.getSizeOfBlockById(Mockito.eq(11))).thenReturn(3);

        PowerMockito.when(variableContext.getProbsOfBlockByID(Mockito.eq(12)))
                .thenReturn(Lists.newArrayList(0.7, 0.2, 0.1));
        PowerMockito.when(variableContext.getSizeOfBlockById(Mockito.eq(12))).thenReturn(3);

        PowerMockito.when(variableContext.getProbsOfBlockByID(Mockito.eq(14)))
                .thenReturn(Lists.newArrayList(0.6, 0.4));
        PowerMockito.when(variableContext.getSizeOfBlockById(Mockito.eq(14))).thenReturn(2);

        //execution
        String stringOfBlocksAndProbabilities = Whitebox.invokeMethod(classUnderTest, "buildStringOfBlocksAndProbabilities", variableContext);

        //assertion
        String expected = "4\n" +
                " 0.1 0.2 0.3 0.4\n" +
                "\n" +
                "3\n" +
                " 0.2 0.3 0.5\n" +
                "\n" +
                "3\n" +
                " 0.7 0.2 0.1\n" +
                "\n" +
                "2\n" +
                " 0.6 0.4";
        MatcherAssert.assertThat(stringOfBlocksAndProbabilities, Matchers.equalToIgnoringWhiteSpace(expected));
    }

    @Test
    public void testBuildCliquesAndSharedMatrix() throws Exception {
        //Mocking
        UaiVariableContext variableContext = PowerMockito.mock(UaiVariableContext.class);
        PowerMockito.when(variableContext.getVariablesIdsWithSharedMatricesSorted()).thenReturn(Lists.newArrayList(7, 8, 9));
        PowerMockito.when(variableContext.getSharedMatrixSizeByVariableId(Mockito.anyInt())).thenReturn(16, 9, 4);

        //mocking 4x4 matrix
        SharedMatrix matrix_4x4 = PowerMockito.mock(SharedMatrix.class);
        PowerMockito.when(matrix_4x4.numberOfRows()).thenReturn(4);
        PowerMockito.when(matrix_4x4.viewRow(Mockito.eq(0))).thenReturn(Lists.newArrayList(0, 0, 0, 0));
        PowerMockito.when(matrix_4x4.viewRow(Mockito.eq(1))).thenReturn(Lists.newArrayList(0, 0, 0, -10));
        PowerMockito.when(matrix_4x4.viewRow(Mockito.eq(2))).thenReturn(Lists.newArrayList(0, 0, 0, 0));
        PowerMockito.when(matrix_4x4.viewRow(Mockito.eq(3))).thenReturn(Lists.newArrayList(0, 0, 0, 0));

        //mocking 3x3 matrix
        SharedMatrix matrix_3x3 = PowerMockito.mock(SharedMatrix.class);
        PowerMockito.when(matrix_3x3.numberOfRows()).thenReturn(3);
        PowerMockito.when(matrix_3x3.viewRow(Mockito.eq(0))).thenReturn(Lists.newArrayList(0, -10, 0));
        PowerMockito.when(matrix_3x3.viewRow(Mockito.eq(1))).thenReturn(Lists.newArrayList(0, 0, 0));
        PowerMockito.when(matrix_3x3.viewRow(Mockito.eq(2))).thenReturn(Lists.newArrayList(-10, 0, 0));

        //mocking 2x2 matrix
        SharedMatrix matrix_2x2 = PowerMockito.mock(SharedMatrix.class);
        PowerMockito.when(matrix_2x2.numberOfRows()).thenReturn(2);
        PowerMockito.when(matrix_2x2.viewRow(Mockito.eq(0))).thenReturn(Lists.newArrayList(0, 0));
        PowerMockito.when(matrix_2x2.viewRow(Mockito.eq(1))).thenReturn(Lists.newArrayList(-10, 0));

        PowerMockito.when(variableContext.getSharedMatrixByVariableId(Mockito.anyInt()))
                .thenReturn(matrix_4x4, matrix_3x3, matrix_2x2);

        //execution
        String cliquesAndSharedMatrix = Whitebox.invokeMethod(classUnderTest, "buildCliquesAndSharedMatrix", variableContext);

        //assertion
        String expected = "16\n" +
                " 0 0 0 0\n" +
                " 0 0 0 -10\n" +
                " 0 0 0 0\n" +
                " 0 0 0 0\n" +
                " \n" +
                "9\n" +
                " 0 -10 0\n" +
                " 0 0 0\n" +
                " -10 0 0\n" +
                " \n" +
                "4\n" +
                " 0 0\n" +
                " -10 0";
        MatcherAssert.assertThat(cliquesAndSharedMatrix, Matchers.equalToIgnoringWhiteSpace(expected));
    }
}