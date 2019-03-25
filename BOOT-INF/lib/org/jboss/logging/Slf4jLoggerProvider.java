/*    */ package org.jboss.logging;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.slf4j.MDC;
/*    */ import org.slf4j.spi.LocationAwareLogger;
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
/*    */ final class Slf4jLoggerProvider
/*    */   extends AbstractLoggerProvider
/*    */   implements LoggerProvider
/*    */ {
/*    */   public Logger getLogger(String name)
/*    */   {
/* 31 */     org.slf4j.Logger l = LoggerFactory.getLogger(name);
/*    */     try {
/* 33 */       return new Slf4jLocationAwareLogger(name, (LocationAwareLogger)l);
/*    */     }
/*    */     catch (Throwable localThrowable) {}
/* 36 */     return new Slf4jLogger(name, l);
/*    */   }
/*    */   
/*    */   public void clearMdc() {}
/*    */   
/*    */   public Object putMdc(String key, Object value)
/*    */   {
/*    */     try
/*    */     {
/* 45 */       return MDC.get(key);
/*    */     } finally {
/* 47 */       if (value == null) {
/* 48 */         MDC.remove(key);
/*    */       } else {
/* 50 */         MDC.put(key, String.valueOf(value));
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public Object getMdc(String key) {
/* 56 */     return MDC.get(key);
/*    */   }
/*    */   
/*    */   public void removeMdc(String key) {
/* 60 */     MDC.remove(key);
/*    */   }
/*    */   
/*    */   public Map<String, Object> getMdcMap()
/*    */   {
/* 65 */     Map<String, Object> map = MDC.getCopyOfContextMap();
/* 66 */     return map == null ? Collections.emptyMap() : map;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jboss-logging-3.3.1.Final.jar!\org\jboss\logging\Slf4jLoggerProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */