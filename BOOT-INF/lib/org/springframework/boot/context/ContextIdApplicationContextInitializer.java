/*     */ package org.springframework.boot.context;
/*     */ 
/*     */ import org.springframework.context.ApplicationContextInitializer;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
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
/*     */ public class ContextIdApplicationContextInitializer
/*     */   implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered
/*     */ {
/*     */   private static final String NAME_PATTERN = "${spring.application.name:${vcap.application.name:${spring.config.name:application}}}";
/*     */   private static final String INDEX_PATTERN = "${vcap.application.instance_index:${spring.application.index:${server.port:${PORT:null}}}}";
/*     */   private final String name;
/*  80 */   private int order = 2147483637;
/*     */   
/*     */   public ContextIdApplicationContextInitializer() {
/*  83 */     this("${spring.application.name:${vcap.application.name:${spring.config.name:application}}}");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContextIdApplicationContextInitializer(String name)
/*     */   {
/*  91 */     this.name = name;
/*     */   }
/*     */   
/*     */   public void setOrder(int order) {
/*  95 */     this.order = order;
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/* 100 */     return this.order;
/*     */   }
/*     */   
/*     */   public void initialize(ConfigurableApplicationContext applicationContext)
/*     */   {
/* 105 */     applicationContext.setId(getApplicationId(applicationContext.getEnvironment()));
/*     */   }
/*     */   
/*     */   private String getApplicationId(ConfigurableEnvironment environment) {
/* 109 */     String name = environment.resolvePlaceholders(this.name);
/* 110 */     String index = environment.resolvePlaceholders("${vcap.application.instance_index:${spring.application.index:${server.port:${PORT:null}}}}");
/*     */     
/* 112 */     String profiles = StringUtils.arrayToCommaDelimitedString(environment.getActiveProfiles());
/* 113 */     if (StringUtils.hasText(profiles)) {
/* 114 */       name = name + ":" + profiles;
/*     */     }
/* 116 */     if (!"null".equals(index)) {
/* 117 */       name = name + ":" + index;
/*     */     }
/* 119 */     return name;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\ContextIdApplicationContextInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */