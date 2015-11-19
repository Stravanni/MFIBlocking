package il.ac.technion.ie.dataset.prepare.context;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PropertiesHolderTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
    }

    @Test(expected = FileNotFoundException.class)
    public void testInitWithNullValue() throws Exception {
        PropertiesHolder.factory(null);
    }

    @Test(expected = FileNotFoundException.class)
    public void testInitWithoutFile() throws Exception {
        File folder = temporaryFolder.newFolder("mooki");
        PropertiesHolder.factory(folder.getAbsolutePath());
    }

    @Test
    public void testInitWithoutGoodFile() throws Exception {
        File propFileForTest = getPropFileForTest();
        PropertiesHolder propertiesHolder = PropertiesHolder.factory(propFileForTest.getAbsolutePath());
        assertThat(propertiesHolder.getPropertiesConfiguration(), notNullValue());
    }

    private File getPropFileForTest() throws URISyntaxException {
        URL resourceUrl = PropertiesHolderTest.class.getResource("/propFileTest.properties");
        return new File(resourceUrl.toURI());
    }


}