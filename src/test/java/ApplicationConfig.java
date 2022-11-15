import org.api.bullish.service.ProductServiceImpl;
import org.api.bullish.service.PromocodeServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan("org.api.bullish")
public class ApplicationConfig {

    @Bean
    public PromocodeServiceImpl promocodeService(){
        return mock(PromocodeServiceImpl.class);
    }

    @Bean
    public ProductServiceImpl productService(){
        return mock(ProductServiceImpl.class);
    }
}
