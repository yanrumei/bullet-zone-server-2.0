/*     */ package org.springframework.boot.info;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GitProperties
/*     */   extends InfoProperties
/*     */ {
/*     */   public GitProperties(Properties entries)
/*     */   {
/*  33 */     super(processEntries(entries));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getBranch()
/*     */   {
/*  41 */     return get("branch");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCommitId()
/*     */   {
/*  49 */     return get("commit.id");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getShortCommitId()
/*     */   {
/*  57 */     String shortId = get("commit.id.abbrev");
/*  58 */     if (shortId != null) {
/*  59 */       return shortId;
/*     */     }
/*  61 */     String id = getCommitId();
/*  62 */     if (id == null) {
/*  63 */       return null;
/*     */     }
/*  65 */     return id.length() > 7 ? id.substring(0, 7) : id;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getCommitTime()
/*     */   {
/*  77 */     return getDate("commit.time");
/*     */   }
/*     */   
/*     */   private static Properties processEntries(Properties properties) {
/*  81 */     coercePropertyToEpoch(properties, "commit.time");
/*  82 */     coercePropertyToEpoch(properties, "build.time");
/*  83 */     return properties;
/*     */   }
/*     */   
/*     */   private static void coercePropertyToEpoch(Properties properties, String key) {
/*  87 */     String value = properties.getProperty(key);
/*  88 */     if (value != null) {
/*  89 */       properties.setProperty(key, coerceToEpoch(value));
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
/*     */   private static String coerceToEpoch(String s)
/*     */   {
/* 102 */     Long epoch = parseEpochSecond(s);
/* 103 */     if (epoch != null) {
/* 104 */       return String.valueOf(epoch);
/*     */     }
/* 106 */     SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
/*     */     try {
/* 108 */       return String.valueOf(format.parse(s).getTime());
/*     */     }
/*     */     catch (ParseException ex) {}
/* 111 */     return s;
/*     */   }
/*     */   
/*     */   private static Long parseEpochSecond(String s)
/*     */   {
/*     */     try {
/* 117 */       return Long.valueOf(Long.parseLong(s) * 1000L);
/*     */     }
/*     */     catch (NumberFormatException ex) {}
/* 120 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\info\GitProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */