package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by pebuls on 03.04.17.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})
public class NewsLoaderTest {

    private NewsLoader newsLoader;
    private String readerType = "testDataReader";
    private IncomingNews testIncomingNews = new IncomingNews();
    private IncomingInfo publicInfoNone = new IncomingInfo("publicInfoNone", SubscriptionType.NONE);
    private IncomingInfo subscribedInfoA = new IncomingInfo("subscribedInfoA", SubscriptionType.A);
    private IncomingInfo subscribedInfoC = new IncomingInfo("SubscribedInfoC", SubscriptionType.C);


    private void setUpNews() {
        testIncomingNews = new IncomingNews();
        testIncomingNews.add(publicInfoNone);
        testIncomingNews.add(subscribedInfoA);
        testIncomingNews.add(subscribedInfoC);
    }

    private void setUpConfig() {
        mockStatic(ConfigurationLoader.class);
        ConfigurationLoader mockLoader = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(mockLoader);

        Configuration configuration = new Configuration();
        Whitebox.setInternalState(configuration, "readerType", readerType);

        when(mockLoader.loadConfiguration()).thenReturn(configuration);
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

    @Before
    public void setUp() throws Exception {

        setUpNews();
        setUpConfig();
        setUpReader();

        newsLoader = new NewsLoader();
    }


}