package tec.mf.handler;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class AppConfigTest {

    @Test
    public void init() throws Exception {

        AppConfig config = AppConfig.getInstance();

        assertThat(config.getInputEventParser()).isNotNull();
        assertThat(config.getImageHandlerResponseWriter()).isNotNull();
        assertThat(config.getImageService()).isNotNull();
    }

}
