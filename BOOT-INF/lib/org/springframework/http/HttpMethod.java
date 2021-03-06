/*    */ package org.springframework.http;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public enum HttpMethod
/*    */ {
/* 33 */   GET,  HEAD,  POST,  PUT,  PATCH,  DELETE,  OPTIONS,  TRACE;
/*    */   
/*    */   static {
/* 36 */     mappings = new HashMap(8);
/*    */     
/*    */ 
/* 39 */     for (HttpMethod httpMethod : values()) {
/* 40 */       mappings.put(httpMethod.name(), httpMethod);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static HttpMethod resolve(String method)
/*    */   {
/* 52 */     return method != null ? (HttpMethod)mappings.get(method) : null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private static final Map<String, HttpMethod> mappings;
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean matches(String method)
/*    */   {
/* 64 */     return this == resolve(method);
/*    */   }
/*    */   
/*    */   private HttpMethod() {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\HttpMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */