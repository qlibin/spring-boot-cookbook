package me.qlibin;

import me.qlibin.formatters.BookFormatter;
import me.qlibin.repository.BookRepository;
import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.List;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {
    /**
     * The @Bean public RemoteIpFilter remoteIpFilter() {…} declaration simply creates a spring bean
     * for the RemoteIpFilter class. When Spring Boot detects all the beans of javax.servlet.Filter,
     * it will add them to the filter chain automatically. So, all we have to do, if we want to add more filters,
     * is to just declare them as @Bean configurations. For example, for a more advanced filter configuration,
     * if we want a particular filter to apply only to specific URL patterns, we can create a @Bean configuration
     * of a FilterRegistrationBean type and use it to configure the precise settings.
     */
    @Bean
    public RemoteIpFilter remoteIpFilter() {
        return new RemoteIpFilter();
    }

    /**
     * When it comes to configuring the Spring MVC internals, it is not as simple as just defining
     * a bunch of beans—at least not always. This is due to the need of providing a more fine-tuned mapping
     * of the MVC components to requests. To make things easier, Spring provides us with an adapter
     * implementation of WebMvcConfigurer, WebMvcConfigurerAdapter,
     * that we can extend and override the settings that we need.
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        return new LocaleChangeInterceptor();
    }

    /**
     * In the particular case of configuring interceptors, we are overriding the
     * addInterceptors(InterceptorRegistry registry) method.
     * This is a typical callback method where we are given a registry in order to register
     * as many additional interceptors as we need. During the MVC autoconfiguration phase, Spring Boot,
     * just as in the case of Filters, detects instances of WebMvcConfigurer and sequentially
     * calls the callback methods on all of them. It means that we can have more than one implementation
     * of the WebMvcConfigurer class if we want to have some logical separation.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    /**
     * Declaring HttpMessageConverter as @Bean is the quickest and simplest way of adding a custom converter
     * to the application. It is similar to how we added Servlet Filters in an earlier example.
     * If Spring detects a bean of the HttpMessageConverter type, it will add it to the list automatically.
     * If we did not have a WebConfiguration class that extends WebMvcConfigurerAdapter,
     * it would have been the preferred approach.
     */
//    @Bean
//    public
//    ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
//        return new ByteArrayHttpMessageConverter();
//    }
    // or:
    /**
     * When the application needs to dictate the extension of WebMvcConfigurerAdapter to configure other things
     * such as interceptors, then it would be more consistent to override the configureMessageConverters method
     * and add our converter to the list. As there can be multiple instances of WebMvcConfigurers,
     * which could be either added by us or via the autoconfiguration settings from various Spring Boot Starters,
     * there is no guarantee that our method can get called in any particular order.
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new ByteArrayHttpMessageConverter());
    }
    // or:
    /**
     * If we need to do something even more drastic such as removing all the other converters from the list
     * or clearing it of duplicate converters, this is where overriding extendMessageConverters comes into play.
     * This method gets invoked after all the WebMvcConfigurers get called for configureMessageConverters
     * and the list of converters is fully populated. Of course, it is entirely possible that
     * some other instance of WebMvcConfigurer could override the extendMessageConverters as well;
     * but the chances of this are very low so you have a high degree of having the desired impact.
     */
//    @Override
//    public void extendMessageConverters(List<HttpMessageConverter<?>>
//                                    converters) {
//        converters.clear();
//        converters.add(new ByteArrayHttpMessageConverter());
//    }

    @Autowired
    private BookRepository bookRepository;

    /**
     * The Formatter facility is aimed towards providing a similar functionality to PropertyEditors.
     * By registering our formatter with the FormatterRegistry in the overridden addFormatters method,
     * we are instructing Spring to use our Formatter to translate a textual representation of our Book into an entity
     * object and back. As Formatters are stateless, we don't need to do the registration in our controller
     * for every call; we have to do it only once and this will ensure Spring to use it for every web request.
     *
     * TIP:
     * It is also good to remember that if you want to define a conversion of a common type,
     * such as String or Boolean — as we did in our IsbnEditor example — it is best to do this
     * via PropertyEditors initialization in Controller's InitBinder method because such a change is probably
     * not globally desired and is only needed for a particular Controller's functionality.
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new BookFormatter(bookRepository));
    }

}

