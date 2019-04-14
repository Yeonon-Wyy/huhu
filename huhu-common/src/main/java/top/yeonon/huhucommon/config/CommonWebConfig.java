package top.yeonon.huhucommon.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Author yeonon
 * @date 2019/4/9 0009 19:41
 **/
@Configuration
public class CommonWebConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //只是用一个MessageConverter
        converters.clear();
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}
