/*     */ package org.springframework.boot.context.embedded;
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
/*     */ public class XmlEmbeddedWebApplicationContext
/*     */   extends EmbeddedWebApplicationContext
/*     */ {
/*  41 */   private final XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public XmlEmbeddedWebApplicationContext()
/*     */   {
/*  48 */     this.reader.setEnvironment(getEnvironment());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public XmlEmbeddedWebApplicationContext(Resource... resources)
/*     */   {
/*  57 */     load(resources);
/*  58 */     refresh();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public XmlEmbeddedWebApplicationContext(String... resourceLocations)
/*     */   {
/*  67 */     load(resourceLocations);
/*  68 */     refresh();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public XmlEmbeddedWebApplicationContext(Class<?> relativeClass, String... resourceNames)
/*     */   {
/*  80 */     load(relativeClass, resourceNames);
/*  81 */     refresh();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValidating(boolean validating)
/*     */   {
/*  89 */     this.reader.setValidating(validating);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEnvironment(ConfigurableEnvironment environment)
/*     */   {
/* 100 */     super.setEnvironment(environment);
/* 101 */     this.reader.setEnvironment(getEnvironment());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void load(Resource... resources)
/*     */   {
/* 109 */     this.reader.loadBeanDefinitions(resources);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void load(String... resourceLocations)
/*     */   {
/* 117 */     this.reader.loadBeanDefinitions(resourceLocations);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void load(Class<?> relativeClass, String... resourceNames)
/*     */   {
/* 127 */     Resource[] resources = new Resource[resourceNames.length];
/* 128 */     for (int i = 0; i < resourceNames.length; i++) {
/* 129 */       resources[i] = new ClassPathResource(resourceNames[i], relativeClass);
/*     */     }
/* 131 */     this.reader.loadBeanDefinitions(resources);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\XmlEmbeddedWebApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */