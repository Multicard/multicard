package ch.cas.html5.multicardgame.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
public class MulticardWebConfigurer implements WebMvcConfigurer {


    /*
     * Serves our angular app as a static resource.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/app/**")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource requestedResource = location.createRelative(resourcePath);
                        if (requestedResource.exists() && requestedResource.isReadable()) {
                            return requestedResource;
                        } else {
                            // fuer die Unterstuetzung von HTML5 Routing (push state routing) werden Requests zu
                            // unbekannten Resources innerhalb des WebappServerSubContexts auf die Index Seite redirected
                            // (https://angular.io/guide/deployment#server-configuration)
                            final ClassPathResource indexResource = new ClassPathResource("/static/index.html");
                            return indexResource.exists() && indexResource.isReadable() ? indexResource : null;
                        }
                    }
                });
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/app/")
                .setViewName("forward:/app/index.html");
        registry.addViewController("/app")
                .setViewName("forward:/app/index.html");
    }

}

