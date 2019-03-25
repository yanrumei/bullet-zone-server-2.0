/*    */ package org.hibernate.validator.internal.util;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import org.hibernate.validator.internal.util.logging.Log;
/*    */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
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
/*    */ public final class Version
/*    */ {
/* 20 */   private static final Pattern JAVA_VERSION_PATTERN = Pattern.compile("^(?:1\\.)?(\\d+)$");
/*    */   
/* 22 */   private static Log LOG = LoggerFactory.make();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 27 */   private static int JAVA_RELEASE = determineJavaRelease(System.getProperty("java.specification.version"));
/*    */   
/*    */   static {
/* 30 */     LOG.version(getVersionString());
/*    */   }
/*    */   
/*    */   public static String getVersionString() {
/* 34 */     return Version.class.getPackage().getImplementationVersion();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static int getJavaRelease()
/*    */   {
/* 46 */     return JAVA_RELEASE;
/*    */   }
/*    */   
/*    */   public static int determineJavaRelease(String specificationVersion) {
/* 50 */     if ((specificationVersion != null) && (!specificationVersion.trim().isEmpty())) {
/* 51 */       Matcher matcher = JAVA_VERSION_PATTERN.matcher(specificationVersion);
/*    */       
/* 53 */       if (matcher.find()) {
/* 54 */         return Integer.valueOf(matcher.group(1)).intValue();
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 59 */     LOG.unknownJvmVersion(specificationVersion);
/* 60 */     return 6;
/*    */   }
/*    */   
/*    */   public static void touch() {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\Version.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */