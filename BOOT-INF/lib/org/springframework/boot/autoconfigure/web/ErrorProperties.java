/*    */ package org.springframework.boot.autoconfigure.web;
/*    */ 
/*    */ import org.springframework.beans.factory.annotation.Value;
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
/*    */ public class ErrorProperties
/*    */ {
/*    */   @Value("${error.path:/error}")
/* 33 */   private String path = "/error";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 39 */   private IncludeStacktrace includeStacktrace = IncludeStacktrace.NEVER;
/*    */   
/*    */   public String getPath() {
/* 42 */     return this.path;
/*    */   }
/*    */   
/*    */   public void setPath(String path) {
/* 46 */     this.path = path;
/*    */   }
/*    */   
/*    */   public IncludeStacktrace getIncludeStacktrace() {
/* 50 */     return this.includeStacktrace;
/*    */   }
/*    */   
/*    */   public void setIncludeStacktrace(IncludeStacktrace includeStacktrace) {
/* 54 */     this.includeStacktrace = includeStacktrace;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static enum IncludeStacktrace
/*    */   {
/* 65 */     NEVER, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 70 */     ALWAYS, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 75 */     ON_TRACE_PARAM;
/*    */     
/*    */     private IncludeStacktrace() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\ErrorProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */