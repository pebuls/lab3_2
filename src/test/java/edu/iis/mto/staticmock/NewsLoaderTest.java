package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest( {ConfigurationLoader.class, NewsReaderFactory.class} )
public class NewsLoaderTest {

    private NewsLoader newsLoader;

    private String readerType = "testDataReader";

    private IncomingNews testIncomingNews = new IncomingNews();
    private IncomingInfo publicInfo1 = new IncomingInfo("PublicInfo1", SubsciptionType.NONE);
    private IncomingInfo subscribedInfo1 = new IncomingInfo("SubscribedInfo1", SubsciptionType.A);
    private IncomingInfo subscribedInfo2 = new IncomingInfo("SubscribedInfo2", SubsciptionType.B);

    @Before
    public void setUp() throws Exception {
        setUpConfig();
        setUpNews();
        setUpReader();

        newsLoader = new NewsLoader();
    }

    private void setUpConfig() {
        mockStatic(ConfigurationLoader.class);
        ConfigurationLoader mockLoader = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(mockLoader);

        Configuration configuration = new Configuration();
        Whitebox.setInternalState(configuration, "readerType", readerType);

        when(mockLoader.loadConfiguration()).thenReturn(configuration);
    }

    private void setUpNews() {
        testIncomingNews = new IncomingNews();
        testIncomingNews.add(publicInfo1);
        testIncomingNews.add(subscribedInfo1);
        testIncomingNews.add(subscribedInfo2);
    }

    private void setUpReader() {
        NewsReader testDataReader = new NewsReader() {
            @Override
            public IncomingNews read() {
                return testIncomingNews;
            }
        };

        mockStatic(NewsReaderFactory.class);
        when(NewsReaderFactory.getReader(readerType)).thenReturn(testDataReader);
    }

}