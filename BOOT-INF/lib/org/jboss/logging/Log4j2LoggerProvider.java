/*    */ package org.jboss.logging;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.ThreadContext;
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
/*    */ final class Log4j2LoggerProvider
/*    */   implements LoggerProvider
/*    */ {
/*    */   public Log4j2Logger getLogger(String name)
/*    */   {
/* 30 */     return new Log4j2Logger(name);
/*    */   }
/*    */   
/*    */ 
/*    */   public void clearMdc() {}
/*    */   
/*    */ 
/*    */   public Object putMdc(String key, Object value)
/*    */   {
/*    */     try
/*    */     {
/* 41 */       return ThreadContext.get(key);
/*    */     } finally {
/* 43 */       ThreadContext.put(key, String.valueOf(value));
/*    */     }
/*    */   }
/*    */   
/*    */   public Object getMdc(String key)
/*    */   {
/* 49 */     return ThreadContext.get(key);
/*    */   }
/*    */   
/*    */   public void removeMdc(String key)
/*    */   {
/* 54 */     ThreadContext.remove(key);
/*    */   }
/*    */   
/*    */   public Map<String, Object> getMdcMap()
/*    */   {
/* 59 */     return new HashMap(ThreadContext.getImmutableContext());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void clearNdc() {}
/*    */   
/*    */ 
/*    */   public String getNdc()
/*    */   {
/* 69 */     return ThreadContext.peek();
/*    */   }
/*    */   
/*    */   public int getNdcDepth()
/*    */   {
/* 74 */     return ThreadContext.getDepth();
/*    */   }
/*    */   
/*    */   public String popNdc()
/*    */   {
/* 79 */     return ThreadContext.pop();
/*    */   }
/*    */   
/*    */   public String peekNdc()
/*    */   {
/* 84 */     return ThreadContext.peek();
/*    */   }
/*    */   
/*    */   public void pushNdc(String message)
/*    */   {
/* 89 */     ThreadContext.push(message);
/*    */   }
/*    */   
/*    */   public void setNdcMaxDepth(int maxDepth)
/*    */   {
/* 94 */     ThreadContext.trim(maxDepth);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jboss-logging-3.3.1.Final.jar!\org\jboss\logging\Log4j2LoggerProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */