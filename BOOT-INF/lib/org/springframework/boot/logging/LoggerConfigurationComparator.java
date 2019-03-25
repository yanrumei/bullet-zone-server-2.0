/*    */ package org.springframework.boot.logging;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import org.springframework.util.Assert;
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
/*    */ class LoggerConfigurationComparator
/*    */   implements Comparator<LoggerConfiguration>
/*    */ {
/*    */   private final String rootLoggerName;
/*    */   
/*    */   LoggerConfigurationComparator(String rootLoggerName)
/*    */   {
/* 38 */     Assert.notNull(rootLoggerName, "RootLoggerName must not be null");
/* 39 */     this.rootLoggerName = rootLoggerName;
/*    */   }
/*    */   
/*    */   public int compare(LoggerConfiguration o1, LoggerConfiguration o2)
/*    */   {
/* 44 */     if (this.rootLoggerName.equals(o1.getName())) {
/* 45 */       return -1;
/*    */     }
/* 47 */     if (this.rootLoggerName.equals(o2.getName())) {
/* 48 */       return 1;
/*    */     }
/* 50 */     return o1.getName().compareTo(o2.getName());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\LoggerConfigurationComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */