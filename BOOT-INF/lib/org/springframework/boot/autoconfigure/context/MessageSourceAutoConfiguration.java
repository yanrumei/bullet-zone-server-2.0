/*     */ package org.springframework.boot.autoconfigure.context;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureOrder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.Builder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.ItemsBuilder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.SearchStrategy;
/*     */ import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.ConditionContext;
/*     */ import org.springframework.context.annotation.Conditional;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.support.ResourceBundleMessageSource;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
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
/*     */ @Configuration
/*     */ @ConditionalOnMissingBean(value={MessageSource.class}, search=SearchStrategy.CURRENT)
/*     */ @AutoConfigureOrder(Integer.MIN_VALUE)
/*     */ @Conditional({ResourceBundleCondition.class})
/*     */ @EnableConfigurationProperties
/*     */ @ConfigurationProperties(prefix="spring.messages")
/*     */ public class MessageSourceAutoConfiguration
/*     */ {
/*  59 */   private static final Resource[] NO_RESOURCES = new Resource[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  67 */   private String basename = "messages";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  72 */   private Charset encoding = Charset.forName("UTF-8");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  78 */   private int cacheSeconds = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  85 */   private boolean fallbackToSystemLocale = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  91 */   private boolean alwaysUseMessageFormat = false;
/*     */   
/*     */   @Bean
/*     */   public MessageSource messageSource() {
/*  95 */     ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
/*  96 */     if (StringUtils.hasText(this.basename)) {
/*  97 */       messageSource.setBasenames(StringUtils.commaDelimitedListToStringArray(
/*  98 */         StringUtils.trimAllWhitespace(this.basename)));
/*     */     }
/* 100 */     if (this.encoding != null) {
/* 101 */       messageSource.setDefaultEncoding(this.encoding.name());
/*     */     }
/* 103 */     messageSource.setFallbackToSystemLocale(this.fallbackToSystemLocale);
/* 104 */     messageSource.setCacheSeconds(this.cacheSeconds);
/* 105 */     messageSource.setAlwaysUseMessageFormat(this.alwaysUseMessageFormat);
/* 106 */     return messageSource;
/*     */   }
/*     */   
/*     */   public String getBasename() {
/* 110 */     return this.basename;
/*     */   }
/*     */   
/*     */   public void setBasename(String basename) {
/* 114 */     this.basename = basename;
/*     */   }
/*     */   
/*     */   public Charset getEncoding() {
/* 118 */     return this.encoding;
/*     */   }
/*     */   
/*     */   public void setEncoding(Charset encoding) {
/* 122 */     this.encoding = encoding;
/*     */   }
/*     */   
/*     */   public int getCacheSeconds() {
/* 126 */     return this.cacheSeconds;
/*     */   }
/*     */   
/*     */   public void setCacheSeconds(int cacheSeconds) {
/* 130 */     this.cacheSeconds = cacheSeconds;
/*     */   }
/*     */   
/*     */   public boolean isFallbackToSystemLocale() {
/* 134 */     return this.fallbackToSystemLocale;
/*     */   }
/*     */   
/*     */   public void setFallbackToSystemLocale(boolean fallbackToSystemLocale) {
/* 138 */     this.fallbackToSystemLocale = fallbackToSystemLocale;
/*     */   }
/*     */   
/*     */   public boolean isAlwaysUseMessageFormat() {
/* 142 */     return this.alwaysUseMessageFormat;
/*     */   }
/*     */   
/*     */   public void setAlwaysUseMessageFormat(boolean alwaysUseMessageFormat) {
/* 146 */     this.alwaysUseMessageFormat = alwaysUseMessageFormat;
/*     */   }
/*     */   
/*     */   protected static class ResourceBundleCondition extends SpringBootCondition
/*     */   {
/* 151 */     private static ConcurrentReferenceHashMap<String, ConditionOutcome> cache = new ConcurrentReferenceHashMap();
/*     */     
/*     */ 
/*     */ 
/*     */     public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */     {
/* 157 */       String basename = context.getEnvironment().getProperty("spring.messages.basename", "messages");
/* 158 */       ConditionOutcome outcome = (ConditionOutcome)cache.get(basename);
/* 159 */       if (outcome == null) {
/* 160 */         outcome = getMatchOutcomeForBasename(context, basename);
/* 161 */         cache.put(basename, outcome);
/*     */       }
/* 163 */       return outcome;
/*     */     }
/*     */     
/*     */ 
/*     */     private ConditionOutcome getMatchOutcomeForBasename(ConditionContext context, String basename)
/*     */     {
/* 169 */       ConditionMessage.Builder message = ConditionMessage.forCondition("ResourceBundle", new Object[0]);
/* 170 */       for (String name : StringUtils.commaDelimitedListToStringArray(
/* 171 */         StringUtils.trimAllWhitespace(basename))) {
/* 172 */         for (Resource resource : getResources(context.getClassLoader(), name)) {
/* 173 */           if (resource.exists()) {
/* 174 */             return 
/* 175 */               ConditionOutcome.match(message.found("bundle").items(new Object[] { resource }));
/*     */           }
/*     */         }
/*     */       }
/* 179 */       return ConditionOutcome.noMatch(message
/* 180 */         .didNotFind("bundle with basename " + basename).atAll());
/*     */     }
/*     */     
/*     */     private Resource[] getResources(ClassLoader classLoader, String name) {
/* 184 */       String target = name.replace('.', '/');
/*     */       try {
/* 186 */         return 
/* 187 */           new PathMatchingResourcePatternResolver(classLoader).getResources("classpath*:" + target + ".properties");
/*     */       }
/*     */       catch (Exception ex) {}
/* 190 */       return MessageSourceAutoConfiguration.NO_RESOURCES;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\context\MessageSourceAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */