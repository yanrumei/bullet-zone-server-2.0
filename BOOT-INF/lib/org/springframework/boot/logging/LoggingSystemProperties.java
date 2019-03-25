/*    */ package org.springframework.boot.logging;
/*    */ 
/*    */ import org.springframework.boot.ApplicationPid;
/*    */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*    */ import org.springframework.core.env.Environment;
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
/*    */ 
/*    */ 
/*    */ class LoggingSystemProperties
/*    */ {
/*    */   static final String PID_KEY = "PID";
/*    */   static final String EXCEPTION_CONVERSION_WORD = "LOG_EXCEPTION_CONVERSION_WORD";
/*    */   static final String CONSOLE_LOG_PATTERN = "CONSOLE_LOG_PATTERN";
/*    */   static final String FILE_LOG_PATTERN = "FILE_LOG_PATTERN";
/*    */   static final String LOG_LEVEL_PATTERN = "LOG_LEVEL_PATTERN";
/*    */   private final Environment environment;
/*    */   
/*    */   LoggingSystemProperties(Environment environment)
/*    */   {
/* 44 */     this.environment = environment;
/*    */   }
/*    */   
/*    */   public void apply() {
/* 48 */     apply(null);
/*    */   }
/*    */   
/*    */   public void apply(LogFile logFile)
/*    */   {
/* 53 */     RelaxedPropertyResolver propertyResolver = RelaxedPropertyResolver.ignoringUnresolvableNestedPlaceholders(this.environment, "logging.");
/* 54 */     setSystemProperty(propertyResolver, "LOG_EXCEPTION_CONVERSION_WORD", "exception-conversion-word");
/*    */     
/* 56 */     setSystemProperty("PID", new ApplicationPid().toString());
/* 57 */     setSystemProperty(propertyResolver, "CONSOLE_LOG_PATTERN", "pattern.console");
/* 58 */     setSystemProperty(propertyResolver, "FILE_LOG_PATTERN", "pattern.file");
/* 59 */     setSystemProperty(propertyResolver, "LOG_LEVEL_PATTERN", "pattern.level");
/* 60 */     if (logFile != null) {
/* 61 */       logFile.applyToSystemProperties();
/*    */     }
/*    */   }
/*    */   
/*    */   private void setSystemProperty(RelaxedPropertyResolver propertyResolver, String systemPropertyName, String propertyName)
/*    */   {
/* 67 */     setSystemProperty(systemPropertyName, propertyResolver.getProperty(propertyName));
/*    */   }
/*    */   
/*    */   private void setSystemProperty(String name, String value) {
/* 71 */     if ((System.getProperty(name) == null) && (value != null)) {
/* 72 */       System.setProperty(name, value);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\LoggingSystemProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */