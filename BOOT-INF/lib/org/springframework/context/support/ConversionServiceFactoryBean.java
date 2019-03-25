/*    */ package org.springframework.context.support;
/*    */ 
/*    */ import java.util.Set;
/*    */ import org.springframework.beans.factory.FactoryBean;
/*    */ import org.springframework.beans.factory.InitializingBean;
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.support.ConversionServiceFactory;
/*    */ import org.springframework.core.convert.support.DefaultConversionService;
/*    */ import org.springframework.core.convert.support.GenericConversionService;
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
/*    */ public class ConversionServiceFactoryBean
/*    */   implements FactoryBean<ConversionService>, InitializingBean
/*    */ {
/*    */   private Set<?> converters;
/*    */   private GenericConversionService conversionService;
/*    */   
/*    */   public void setConverters(Set<?> converters)
/*    */   {
/* 64 */     this.converters = converters;
/*    */   }
/*    */   
/*    */   public void afterPropertiesSet()
/*    */   {
/* 69 */     this.conversionService = createConversionService();
/* 70 */     ConversionServiceFactory.registerConverters(this.converters, this.conversionService);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected GenericConversionService createConversionService()
/*    */   {
/* 80 */     return new DefaultConversionService();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ConversionService getObject()
/*    */   {
/* 88 */     return this.conversionService;
/*    */   }
/*    */   
/*    */   public Class<? extends ConversionService> getObjectType()
/*    */   {
/* 93 */     return GenericConversionService.class;
/*    */   }
/*    */   
/*    */   public boolean isSingleton()
/*    */   {
/* 98 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\support\ConversionServiceFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */