/*     */ package org.springframework.boot.context.config;
/*     */ 
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.MutablePropertySources;
/*     */ import org.springframework.core.env.PropertySource;
/*     */ import org.springframework.util.DigestUtils;
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
/*     */ public class RandomValuePropertySource
/*     */   extends PropertySource<Random>
/*     */ {
/*     */   public static final String RANDOM_PROPERTY_SOURCE_NAME = "random";
/*     */   private static final String PREFIX = "random.";
/*  62 */   private static final Log logger = LogFactory.getLog(RandomValuePropertySource.class);
/*     */   
/*     */   public RandomValuePropertySource(String name) {
/*  65 */     super(name, new Random());
/*     */   }
/*     */   
/*     */   public RandomValuePropertySource() {
/*  69 */     this("random");
/*     */   }
/*     */   
/*     */   public Object getProperty(String name)
/*     */   {
/*  74 */     if (!name.startsWith("random.")) {
/*  75 */       return null;
/*     */     }
/*  77 */     if (logger.isTraceEnabled()) {
/*  78 */       logger.trace("Generating random property for '" + name + "'");
/*     */     }
/*  80 */     return getRandomValue(name.substring("random.".length()));
/*     */   }
/*     */   
/*     */   private Object getRandomValue(String type) {
/*  84 */     if (type.equals("int")) {
/*  85 */       return Integer.valueOf(((Random)getSource()).nextInt());
/*     */     }
/*  87 */     if (type.equals("long")) {
/*  88 */       return Long.valueOf(((Random)getSource()).nextLong());
/*     */     }
/*  90 */     String range = getRange(type, "int");
/*  91 */     if (range != null) {
/*  92 */       return Integer.valueOf(getNextIntInRange(range));
/*     */     }
/*  94 */     range = getRange(type, "long");
/*  95 */     if (range != null) {
/*  96 */       return Long.valueOf(getNextLongInRange(range));
/*     */     }
/*  98 */     if (type.equals("uuid")) {
/*  99 */       return UUID.randomUUID().toString();
/*     */     }
/* 101 */     return getRandomBytes();
/*     */   }
/*     */   
/*     */   private String getRange(String type, String prefix) {
/* 105 */     if (type.startsWith(prefix)) {
/* 106 */       int startIndex = prefix.length() + 1;
/* 107 */       if (type.length() > startIndex) {
/* 108 */         return type.substring(startIndex, type.length() - 1);
/*     */       }
/*     */     }
/* 111 */     return null;
/*     */   }
/*     */   
/*     */   private int getNextIntInRange(String range) {
/* 115 */     String[] tokens = StringUtils.commaDelimitedListToStringArray(range);
/* 116 */     int start = Integer.parseInt(tokens[0]);
/* 117 */     if (tokens.length == 1) {
/* 118 */       return ((Random)getSource()).nextInt(start);
/*     */     }
/* 120 */     return start + ((Random)getSource()).nextInt(Integer.parseInt(tokens[1]) - start);
/*     */   }
/*     */   
/*     */   private long getNextLongInRange(String range) {
/* 124 */     String[] tokens = StringUtils.commaDelimitedListToStringArray(range);
/* 125 */     if (tokens.length == 1) {
/* 126 */       return Math.abs(((Random)getSource()).nextLong() % Long.parseLong(tokens[0]));
/*     */     }
/* 128 */     long lowerBound = Long.parseLong(tokens[0]);
/* 129 */     long upperBound = Long.parseLong(tokens[1]) - lowerBound;
/* 130 */     return lowerBound + Math.abs(((Random)getSource()).nextLong() % upperBound);
/*     */   }
/*     */   
/*     */   private Object getRandomBytes() {
/* 134 */     byte[] bytes = new byte[32];
/* 135 */     ((Random)getSource()).nextBytes(bytes);
/* 136 */     return DigestUtils.md5DigestAsHex(bytes);
/*     */   }
/*     */   
/*     */   public static void addToEnvironment(ConfigurableEnvironment environment) {
/* 140 */     environment.getPropertySources().addAfter("systemEnvironment", new RandomValuePropertySource("random"));
/*     */     
/*     */ 
/* 143 */     logger.trace("RandomValuePropertySource add to Environment");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\config\RandomValuePropertySource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */