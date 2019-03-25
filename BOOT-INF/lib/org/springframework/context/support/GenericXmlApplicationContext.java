/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.io.ClassPathResource;
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
/*     */ public class GenericXmlApplicationContext
/*     */   extends GenericApplicationContext
/*     */ {
/*  44 */   private final XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GenericXmlApplicationContext() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GenericXmlApplicationContext(Resource... resources)
/*     */   {
/*  60 */     load(resources);
/*  61 */     refresh();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GenericXmlApplicationContext(String... resourceLocations)
/*     */   {
/*  70 */     load(resourceLocations);
/*  71 */     refresh();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GenericXmlApplicationContext(Class<?> relativeClass, String... resourceNames)
/*     */   {
/*  82 */     load(relativeClass, resourceNames);
/*  83 */     refresh();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final XmlBeanDefinitionReader getReader()
/*     */   {
/*  92 */     return this.reader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setValidating(boolean validating)
/*     */   {
/*  99 */     this.reader.setValidating(validating);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEnvironment(ConfigurableEnvironment environment)
/*     */   {
/* 108 */     super.setEnvironment(environment);
/* 109 */     this.reader.setEnvironment(getEnvironment());
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
/*     */   public void load(Resource... resources)
/*     */   {
/* 122 */     this.reader.loadBeanDefinitions(resources);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void load(String... resourceLocations)
/*     */   {
/* 130 */     this.reader.loadBeanDefinitions(resourceLocations);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void load(Class<?> relativeClass, String... resourceNames)
/*     */   {
/* 140 */     Resource[] resources = new Resource[resourceNames.length];
/* 141 */     for (int i = 0; i < resourceNames.length; i++) {
/* 142 */       resources[i] = new ClassPathResource(resourceNames[i], relativeClass);
/*     */     }
/* 144 */     load(resources);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\support\GenericXmlApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */