package il.ac.technion.ie.dataset.prepare.newFiles;

import il.ac.technion.ie.dataset.prepare.context.PropertiesHolderTest;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LexiconTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testCreateInstance() throws Exception {
        Lexicon lexicon = new Lexicon(this.getPropFileForTest().getAbsolutePath());
        assertThat(lexicon.getColumnIndexToId().size(), is(9));
        assertThat(lexicon.getWeightByIndex(4), closeTo(0.999, 0.00001));
    }

    private File getPropFileForTest() throws URISyntaxException {
        URL resourceUrl = PropertiesHolderTest.class.getResource("/weights.properties");
        return new File(resourceUrl.toURI());
    }
}