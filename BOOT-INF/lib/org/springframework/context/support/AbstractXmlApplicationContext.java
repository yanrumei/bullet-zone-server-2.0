/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.beans.factory.xml.ResourceEntityResolver;
/*     */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.core.io.Resource;
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
/*     */ public abstract class AbstractXmlApplicationContext
/*     */   extends AbstractRefreshableConfigApplicationContext
/*     */ {
/*  47 */   private boolean validating = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractXmlApplicationContext() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractXmlApplicationContext(ApplicationContext parent)
/*     */   {
/*  61 */     super(parent);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValidating(boolean validating)
/*     */   {
/*  69 */     this.validating = validating;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
/*     */     throws BeansException, IOException
/*     */   {
/*  82 */     XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
/*     */     
/*     */ 
/*     */ 
/*  86 */     beanDefinitionReader.setEnvironment(getEnvironment());
/*  87 */     beanDefinitionReader.setResourceLoader(this);
/*  88 */     beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));
/*     */     
/*     */ 
/*     */ 
/*  92 */     initBeanDefinitionReader(beanDefinitionReader);
/*  93 */     loadBeanDefinitions(beanDefinitionReader);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initBeanDefinitionReader(XmlBeanDefinitionReader reader)
/*     */   {
/* 105 */     reader.setValidating(this.validating);
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
/*     */   protected void loadBeanDefinitions(XmlBeanDefinitionReader reader)
/*     */     throws BeansException, IOException
/*     */   {
/* 121 */     Resource[] configResources = getConfigResources();
/* 122 */     if (configResources != null) {
/* 123 */       reader.loadBeanDefinitions(configResources);
/*     */     }
/* 125 */     String[] configLocations = getConfigLocations();
/* 126 */     if (configLocations != null) {
/* 127 */       reader.loadBeanDefinitions(configLocations);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Resource[] getConfigResources()
/*     */   {
/* 140 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\support\AbstractXmlApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */