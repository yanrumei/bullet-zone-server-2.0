/*    */ package org.springframework.boot.info;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.Properties;
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
/*    */ public class BuildProperties
/*    */   extends InfoProperties
/*    */ {
/*    */   public BuildProperties(Properties entries)
/*    */   {
/* 37 */     super(processEntries(entries));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getGroup()
/*    */   {
/* 45 */     return get("group");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getArtifact()
/*    */   {
/* 53 */     return get("artifact");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getName()
/*    */   {
/* 61 */     return get("name");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getVersion()
/*    */   {
/* 69 */     return get("version");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Date getTime()
/*    */   {
/* 81 */     return getDate("time");
/*    */   }
/*    */   
/*    */   private static Properties processEntries(Properties properties) {
/* 85 */     coerceDate(properties, "time");
/* 86 */     return properties;
/*    */   }
/*    */   
/*    */   private static void coerceDate(Properties properties, String key) {
/* 90 */     String value = properties.getProperty(key);
/* 91 */     if (value != null) {
/* 92 */       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
/*    */       try {
/* 94 */         String updatedValue = String.valueOf(format.parse(value).getTime());
/* 95 */         properties.setProperty(key, updatedValue);
/*    */       }
/*    */       catch (ParseException localParseException) {}
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\info\BuildProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */