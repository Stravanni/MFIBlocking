package il.ac.technion.ie.dataset.prepare.model;

import com.google.common.collect.Lists;
import il.ac.technion.ie.dataset.prepare.exception.InvalidSizeException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.List;

public class ColumnMetaDataTest {

    @Test(expected = InvalidSizeException.class)
    public void testConstructorWithNull() throws Exception {
        new ColumnMetaData(null);
    }

    @Test(expected = InvalidSizeException.class)
    public void testConstructorWithEmptyArray() throws Exception {
        new ColumnMetaData(new String[]{});
    }

    @Test
    public void testConstructorWithValidData() throws Exception {
        List<String> values = Lists.newArrayList("1", "0.999", "300");
        ColumnMetaData columnMetaData = new ColumnMetaData(values.toArray(new String[]{}));
        MatcherAssert.assertThat(columnMetaData.getID(), Matchers.is(1));
        MatcherAssert.assertThat(columnMetaData.getWeight(), Matchers.closeTo(0.999, 0.0001));
        MatcherAssert.assertThat(columnMetaData.getLength(), Matchers.is(300));
    }
}