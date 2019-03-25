/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.beans.factory.xml.ResourceEntityResolver;
/*     */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlWebApplicationContext
/*     */   extends AbstractRefreshableWebApplicationContext
/*     */ {
/*     */   public static final String DEFAULT_CONFIG_LOCATION = "/WEB-INF/applicationContext.xml";
/*     */   public static final String DEFAULT_CONFIG_LOCATION_PREFIX = "/WEB-INF/";
/*     */   public static final String DEFAULT_CONFIG_LOCATION_SUFFIX = ".xml";
/*     */   
/*     */   protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
/*     */     throws BeansException, IOException
/*     */   {
/*  83 */     XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
/*     */     
/*     */ 
/*     */ 
/*  87 */     beanDefinitionReader.setEnvironment(getEnvironment());
/*  88 */     beanDefinitionReader.setResourceLoader(this);
/*  89 */     beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));
/*     */     
/*     */ 
/*     */ 
/*  93 */     initBeanDefinitionReader(beanDefinitionReader);
/*  94 */     loadBeanDefinitions(beanDefinitionReader);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initBeanDefinitionReader(XmlBeanDefinitionReader beanDefinitionReader) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void loadBeanDefinitions(XmlBeanDefinitionReader reader)
/*     */     throws IOException
/*     */   {
/* 122 */     String[] configLocations = getConfigLocations();
/* 123 */     if (configLocations != null) {
/* 124 */       for (String configLocation : configLocations) {
/* 125 */         reader.loadBeanDefinitions(configLocation);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String[] getDefaultConfigLocations()
/*     */   {
/* 137 */     if (getNamespace() != null) {
/* 138 */       return new String[] { "/WEB-INF/" + getNamespace() + ".xml" };
/*     */     }
/*     */     
/* 141 */     return new String[] { "/WEB-INF/applicationContext.xml" };
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\support\XmlWebApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */