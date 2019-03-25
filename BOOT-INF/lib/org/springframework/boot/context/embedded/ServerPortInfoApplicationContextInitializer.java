/*     */ package org.springframework.boot.context.embedded;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextInitializer;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.MapPropertySource;
/*     */ import org.springframework.core.env.MutablePropertySources;
/*     */ import org.springframework.core.env.PropertySource;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class ServerPortInfoApplicationContextInitializer
/*     */   implements ApplicationContextInitializer<ConfigurableApplicationContext>
/*     */ {
/*     */   public void initialize(ConfigurableApplicationContext applicationContext)
/*     */   {
/*  56 */     applicationContext.addApplicationListener(new ApplicationListener()
/*     */     {
/*     */ 
/*     */ 
/*     */       public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event)
/*     */       {
/*     */ 
/*  63 */         ServerPortInfoApplicationContextInitializer.this.onApplicationEvent(event);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   protected void onApplicationEvent(EmbeddedServletContainerInitializedEvent event)
/*     */   {
/*  70 */     String propertyName = getPropertyName(event.getApplicationContext());
/*  71 */     setPortProperty(event.getApplicationContext(), propertyName, event
/*  72 */       .getEmbeddedServletContainer().getPort());
/*     */   }
/*     */   
/*     */   protected String getPropertyName(EmbeddedWebApplicationContext context) {
/*  76 */     String name = context.getNamespace();
/*  77 */     if (StringUtils.isEmpty(name)) {
/*  78 */       name = "server";
/*     */     }
/*  80 */     return "local." + name + ".port";
/*     */   }
/*     */   
/*     */   private void setPortProperty(ApplicationContext context, String propertyName, int port)
/*     */   {
/*  85 */     if ((context instanceof ConfigurableApplicationContext)) {
/*  86 */       setPortProperty(((ConfigurableApplicationContext)context).getEnvironment(), propertyName, port);
/*     */     }
/*     */     
/*  89 */     if (context.getParent() != null) {
/*  90 */       setPortProperty(context.getParent(), propertyName, port);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void setPortProperty(ConfigurableEnvironment environment, String propertyName, int port)
/*     */   {
/*  97 */     MutablePropertySources sources = environment.getPropertySources();
/*  98 */     PropertySource<?> source = sources.get("server.ports");
/*  99 */     if (source == null) {
/* 100 */       source = new MapPropertySource("server.ports", new HashMap());
/* 101 */       sources.addFirst(source);
/*     */     }
/* 103 */     ((Map)source.getSource()).put(propertyName, Integer.valueOf(port));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\ServerPortInfoApplicationContextInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */