/*    */ package org.springframework.boot.autoconfigure.hateoas;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import javax.annotation.PostConstruct;
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.BeanFactoryAware;
/*    */ import org.springframework.beans.factory.ListableBeanFactory;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.hateoas.mvc.TypeConstrainedMappingJackson2HttpMessageConverter;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.http.converter.AbstractHttpMessageConverter;
/*    */ import org.springframework.http.converter.HttpMessageConverter;
/*    */ import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HypermediaHttpMessageConverterConfiguration
/*    */ {
/*    */   @Bean
/*    */   @ConditionalOnProperty(prefix="spring.hateoas", name={"use-hal-as-default-json-media-type"}, matchIfMissing=true)
/*    */   public static HalMessageConverterSupportedMediaTypesCustomizer halMessageConverterSupportedMediaTypeCustomizer()
/*    */   {
/* 49 */     return new HalMessageConverterSupportedMediaTypesCustomizer(null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private static class HalMessageConverterSupportedMediaTypesCustomizer
/*    */     implements BeanFactoryAware
/*    */   {
/*    */     private volatile BeanFactory beanFactory;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     @PostConstruct
/*    */     public void configureHttpMessageConverters()
/*    */     {
/* 66 */       if ((this.beanFactory instanceof ListableBeanFactory)) {
/* 67 */         configureHttpMessageConverters(((ListableBeanFactory)this.beanFactory)
/* 68 */           .getBeansOfType(RequestMappingHandlerAdapter.class).values());
/*    */       }
/*    */     }
/*    */     
/*    */     private void configureHttpMessageConverters(Collection<RequestMappingHandlerAdapter> handlerAdapters)
/*    */     {
/* 74 */       for (RequestMappingHandlerAdapter handlerAdapter : handlerAdapters) {
/* 75 */         for (HttpMessageConverter<?> messageConverter : handlerAdapter
/* 76 */           .getMessageConverters()) {
/* 77 */           configureHttpMessageConverter(messageConverter);
/*    */         }
/*    */       }
/*    */     }
/*    */     
/*    */     private void configureHttpMessageConverter(HttpMessageConverter<?> converter) {
/* 83 */       if ((converter instanceof TypeConstrainedMappingJackson2HttpMessageConverter))
/*    */       {
/* 85 */         List<MediaType> supportedMediaTypes = new ArrayList(converter.getSupportedMediaTypes());
/* 86 */         if (!supportedMediaTypes.contains(MediaType.APPLICATION_JSON)) {
/* 87 */           supportedMediaTypes.add(MediaType.APPLICATION_JSON);
/*    */         }
/*    */         
/* 90 */         ((AbstractHttpMessageConverter)converter).setSupportedMediaTypes(supportedMediaTypes);
/*    */       }
/*    */     }
/*    */     
/*    */     public void setBeanFactory(BeanFactory beanFactory) throws BeansException
/*    */     {
/* 96 */       this.beanFactory = beanFactory;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\hateoas\HypermediaHttpMessageConverterConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */