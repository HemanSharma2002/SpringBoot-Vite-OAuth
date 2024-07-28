package org.example.springbootstarteroauthvitebasicsetup.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import java.util.List;

import static java.util.Objects.nonNull;

@Configuration
public class SpringWebMvcConfiguration implements WebMvcConfigurer {

    //ad custom Mapping for cross-origin
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);
    }



    // ignore this in case for cross-origin i.e. only valid id you are running vite on Spring Boot
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        this.serverDirectory(registry,"/","classpath:/static/");
    }

    private void serverDirectory(ResourceHandlerRegistry registry,String endpoint,String location)
    {
        String[] endpointPatterns=endpoint.endsWith("/")
                ?new String[]{endpoint.substring(0,endpoint.length()-1),endpoint,endpoint+"**"}
                : new String[]{endpoint,endpoint+"/**",endpoint+"/"};
        registry
                .addResourceHandler(endpointPatterns)
                .addResourceLocations(location.equals("/")?location:location+"/")
                .resourceChain(true)

                .addResolver(new PathResourceResolver(){
                    @Override
                    public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
                        Resource resource=super.resolveResource(request, requestPath, locations, chain);
                        if(nonNull(resource)){
                            return resource;
                        }
                        return super.resolveResource(request, "/index.html", locations, chain);
                    }
                });
    }
}