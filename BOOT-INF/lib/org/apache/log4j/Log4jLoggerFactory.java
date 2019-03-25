/*    */ package org.apache.log4j;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import org.apache.log4j.spi.LoggerFactory;
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
/*    */ 
/*    */ class Log4jLoggerFactory
/*    */ {
/* 36 */   private static ConcurrentMap<String, Logger> log4jLoggers = new ConcurrentHashMap();
/*    */   
/*    */   public static Logger getLogger(String name) {
/* 39 */     Logger instance = (Logger)log4jLoggers.get(name);
/* 40 */     if (instance != null) {
/* 41 */       return instance;
/*    */     }
/* 43 */     Logger newInstance = new Logger(name);
/* 44 */     Logger oldInstance = (Logger)log4jLoggers.putIfAbsent(name, newInstance);
/* 45 */     return oldInstance == null ? newInstance : oldInstance;
/*    */   }
/*    */   
/*    */   public static Logger getLogger(String name, LoggerFactory loggerFactory)
/*    */   {
/* 50 */     Logger instance = (Logger)log4jLoggers.get(name);
/* 51 */     if (instance != null) {
/* 52 */       return instance;
/*    */     }
/* 54 */     Logger newInstance = loggerFactory.makeNewLoggerInstance(name);
/* 55 */     Logger oldInstance = (Logger)log4jLoggers.putIfAbsent(name, newInstance);
/* 56 */     return oldInstance == null ? newInstance : oldInstance;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\log4j-over-slf4j-1.7.25.jar!\org\apache\log4j\Log4jLoggerFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */