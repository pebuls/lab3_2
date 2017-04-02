package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest( {ConfigurationLoader.class, NewsReaderFactory.class, PublishableNews.class} )
public class NewsLoaderTest {

    private class PublishableNewsAnalyzer extends PublishableNews {

        private final List<String> publicNews = new ArrayList<>();
        private final HashMap<String, SubsciptionType> subscribedNews = new HashMap<>();

        @Override
        public void addPublicInfo(String content) {
            super.addPublicInfo(content);
            publicNews.add(content);
        }

        @Override
        public void addForSubscription(String content, SubsciptionType subscriptionType) {
            super.addForSubscription(content, subscriptionType);
            subscribedNews.put(content, subscriptionType);
        }
    }

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
        mockStatic(PublishableNews.class);
        when(PublishableNews.create()).thenReturn(new PublishableNewsAnalyzer());

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

    @Test
    public void testLoadNews_CheckPublicMessages() throws Exception {
        PublishableNewsAnalyzer news = (PublishableNewsAnalyzer) newsLoader.loadNews();

        assertThat(news.publicNews, hasItem(publicInfo1.getContent()));
        assertThat(news.publicNews, not(hasItem(subscribedInfo1.getContent())));
        assertThat(news.publicNews, not(hasItem(subscribedInfo2.getContent())));
    }

    @Test
    public void testLoadNews_CheckSubscribedMessages() throws Exception {
        PublishableNewsAnalyzer news = (PublishableNewsAnalyzer) newsLoader.loadNews();

        assertThat(news.subscribedNews.keySet(), not(hasItem(publicInfo1.getContent())));
        assertThat(news.subscribedNews.keySet(), hasItem(subscribedInfo1.getContent()));
        assertThat(news.subscribedNews.get(subscribedInfo1.getContent()), is(equalTo(subscribedInfo1.getSubscriptionType())));
        assertThat(news.subscribedNews.keySet(), hasItem(subscribedInfo2.getContent()));
        assertThat(news.subscribedNews.get(subscribedInfo2.getContent()), is(equalTo(subscribedInfo2.getSubscriptionType())));
    }

    @Test
    public void testLoadNews_CheckNewsReaderFactoryCalledWithCorrectParameter() throws Exception {
        newsLoader.loadNews();
        verifyStatic( times(1));
        NewsReaderFactory.getReader(readerType);
    }

}