/*    */ package org.apache.catalina.valves.rewrite;
/*    */ 
/*    */ import java.nio.charset.Charset;
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
/*    */ public abstract class Resolver
/*    */ {
/*    */   public abstract String resolve(String paramString);
/*    */   
/*    */   public String resolveEnv(String key)
/*    */   {
/* 30 */     return System.getProperty(key);
/*    */   }
/*    */   
/*    */   public abstract String resolveSsl(String paramString);
/*    */   
/*    */   public abstract String resolveHttp(String paramString);
/*    */   
/*    */   public abstract boolean resolveResource(int paramInt, String paramString);
/*    */   
/*    */   @Deprecated
/*    */   public abstract String getUriEncoding();
/*    */   
/*    */   public abstract Charset getUriCharset();
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\rewrite\Resolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */