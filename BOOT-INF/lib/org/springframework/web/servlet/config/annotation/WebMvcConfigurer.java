package org.springframework.web.servlet.config.annotation;

import java.util.List;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;

public abstract interface WebMvcConfigurer
{
  public abstract void configurePathMatch(PathMatchConfigurer paramPathMatchConfigurer);
  
  public abstract void configureContentNegotiation(ContentNegotiationConfigurer paramContentNegotiationConfigurer);
  
  public abstract void configureAsyncSupport(AsyncSupportConfigurer paramAsyncSupportConfigurer);
  
  public abstract void configureDefaultServletHandling(DefaultServletHandlerConfigurer paramDefaultServletHandlerConfigurer);
  
  public abstract void addFormatters(FormatterRegistry paramFormatterRegistry);
  
  public abstract void addInterceptors(InterceptorRegistry paramInterceptorRegistry);
  
  public abstract void addResourceHandlers(ResourceHandlerRegistry paramResourceHandlerRegistry);
  
  public abstract void addCorsMappings(CorsRegistry paramCorsRegistry);
  
  public abstract void addViewControllers(ViewControllerRegistry paramViewControllerRegistry);
  
  public abstract void configureViewResolvers(ViewResolverRegistry paramViewResolverRegistry);
  
  public abstract void addArgumentResolvers(List<HandlerMethodArgumentResolver> paramList);
  
  public abstract void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> paramList);
  
  public abstract void configureMessageConverters(List<HttpMessageConverter<?>> paramList);
  
  public abstract void extendMessageConverters(List<HttpMessageConverter<?>> paramList);
  
  public abstract void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> paramList);
  
  public abstract void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> paramList);
  
  public abstract Validator getValidator();
  
  public abstract MessageCodesResolver getMessageCodesResolver();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\annotation\WebMvcConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */