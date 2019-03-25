/*    */ package org.jboss.logging;
/*    */ 
/*    */ import java.util.logging.Level;
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
/*    */ final class JDKLevel
/*    */   extends Level
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   protected JDKLevel(String name, int value)
/*    */   {
/* 31 */     super(name, value);
/*    */   }
/*    */   
/*    */   protected JDKLevel(String name, int value, String resourceBundleName) {
/* 35 */     super(name, value, resourceBundleName);
/*    */   }
/*    */   
/* 38 */   public static final JDKLevel FATAL = new JDKLevel("FATAL", 1100);
/* 39 */   public static final JDKLevel ERROR = new JDKLevel("ERROR", 1000);
/* 40 */   public static final JDKLevel WARN = new JDKLevel("WARN", 900);
/*    */   
/* 42 */   public static final JDKLevel INFO = new JDKLevel("INFO", 800);
/* 43 */   public static final JDKLevel DEBUG = new JDKLevel("DEBUG", 500);
/* 44 */   public static final JDKLevel TRACE = new JDKLevel("TRACE", 400);
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jboss-logging-3.3.1.Final.jar!\org\jboss\logging\JDKLevel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */