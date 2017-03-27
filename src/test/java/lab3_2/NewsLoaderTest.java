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

import static org.powermock.api.mockito.PowerMockito.*;
import org.mockito.internal.util.reflection.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest( ConfigurationLoader.class )
public class NewsLoaderTest {
	
	public ConfigurationLoader testConfigurationLoader = null;
	
	@Before
	public void setUpTest() {
		initFakeConfigLoader();
		initFakeConfig();
	}
	
	public void initFakeConfigLoader() {
		testConfigurationLoader = PowerMockito.mock(ConfigurationLoader.class);
		PowerMockito.mockStatic(ConfigurationLoader.class);
		PowerMockito.when(ConfigurationLoader.getInstance()).thenReturn(testConfigurationLoader);
	}
	
	public void initFakeConfig() {
		Configuration testConfiguration = new Configuration();
		Whitebox.setInternalState(testConfiguration, "readerType", "testNewsReader");
		when(testConfigurationLoader.loadConfiguration()).thenReturn(testConfiguration);
	}
	
	@Test
	public void test() {
		fail("Not yet implemented!");
	}
}
