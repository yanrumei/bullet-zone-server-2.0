/*    */ package org.apache.catalina.core;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Objects;
/*    */ import org.apache.catalina.AccessLog;
/*    */ import org.apache.catalina.connector.Request;
/*    */ import org.apache.catalina.connector.Response;
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
/*    */ public class AccessLogAdapter
/*    */   implements AccessLog
/*    */ {
/*    */   private AccessLog[] logs;
/*    */   
/*    */   public AccessLogAdapter(AccessLog log)
/*    */   {
/* 34 */     Objects.requireNonNull(log);
/* 35 */     this.logs = new AccessLog[] { log };
/*    */   }
/*    */   
/*    */   public void add(AccessLog log) {
/* 39 */     Objects.requireNonNull(log);
/* 40 */     AccessLog[] newArray = (AccessLog[])Arrays.copyOf(this.logs, this.logs.length + 1);
/* 41 */     newArray[(newArray.length - 1)] = log;
/* 42 */     this.logs = newArray;
/*    */   }
/*    */   
/*    */   public void log(Request request, Response response, long time)
/*    */   {
/* 47 */     for (AccessLog log : this.logs) {
/* 48 */       log.log(request, response, time);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setRequestAttributesEnabled(boolean requestAttributesEnabled) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean getRequestAttributesEnabled()
/*    */   {
/* 61 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\AccessLogAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */