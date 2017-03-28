package lab3_2;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.iis.mto.staticmock.Configuration;
import edu.iis.mto.staticmock.ConfigurationLoader;
import edu.iis.mto.staticmock.IncomingInfo;
import edu.iis.mto.staticmock.IncomingNews;
import edu.iis.mto.staticmock.NewsLoader;
import edu.iis.mto.staticmock.NewsReaderFactory;
import edu.iis.mto.staticmock.PublishableNews;
import edu.iis.mto.staticmock.SubsciptionType;
import edu.iis.mto.staticmock.reader.NewsReader;

import static org.powermock.api.mockito.PowerMockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;
import org.mockito.internal.util.reflection.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.hamcrest.CoreMatchers.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest( {
	ConfigurationLoader.class,
	NewsReaderFactory.class,
	PublishableNews.class
})

public class NewsLoaderTest {
	
	private ConfigurationLoader testConfigurationLoader = null;
	private Configuration testConfiguration = null;
	
	@Before
	public void setUpTest() {
		initFakeConfigLoader();
		initFakeConfig();
		initFakeNewsReader();
		initFakeNewsContentChecker();
	}
	
	public void initFakeNewsContentChecker() {
		mockStatic(PublishableNews.class);
		when(PublishableNews.create()).thenReturn(new PublishableNewsContentChecker());
	}
	
	public void initFakeConfigLoader() {
		testConfigurationLoader = PowerMockito.mock(ConfigurationLoader.class);
		PowerMockito.mockStatic(ConfigurationLoader.class);
		PowerMockito.when(ConfigurationLoader.getInstance()).thenReturn(testConfigurationLoader);
	}
	
	public void initFakeConfig() {
		testConfiguration = new Configuration();
		Whitebox.setInternalState(testConfiguration, "readerType", "test");
		when(testConfigurationLoader.loadConfiguration()).thenReturn(testConfiguration);
	}
	
	public void initFakeNewsReader() {
		PowerMockito.mockStatic(NewsReaderFactory.class);
		final IncomingNews news = new IncomingNews();
		news.add(new IncomingInfo("pub", SubsciptionType.NONE));
		news.add(new IncomingInfo("subA", SubsciptionType.A));
		news.add(new IncomingInfo("subB", SubsciptionType.B));
		news.add(new IncomingInfo("subC", SubsciptionType.C));
		NewsReader testNewsReader = new NewsReader() {

			@Override
			public IncomingNews read() {
				return news;
			}
		};
		when(NewsReaderFactory.getReader("test")).thenReturn(testNewsReader);
	}
	
	@Test
	public void testNewsAddedCorrectly() throws Exception {
		NewsLoader testLoader = new NewsLoader();
		PublishableNewsContentChecker testChecker = (PublishableNewsContentChecker) testLoader.loadNews();
		assertThat(testChecker.getSubscribentContent().size(), is(equalTo(3)));
		assertThat(testChecker.getSubscribentContent().get(0), is(equalTo("subA")));
		assertThat(testChecker.getSubscribentContent().get(1), is(equalTo("subB")));
		assertThat(testChecker.getSubscribentContent().get(2), is(equalTo("subC")));
		assertThat(testChecker.getPublicContent().size(), is(equalTo(1)));
		assertThat(testChecker.getPublicContent().get(0), is(equalTo("pub")));
	}
	
	@Test
	public void testNewsReaderFactoryCorrectReaderUsedAndGetReaderCalledOnce() {
		NewsLoader test = new NewsLoader();
		test.loadNews();
		PowerMockito.verifyStatic();
		NewsReaderFactory.getReader("test");
	}
}
