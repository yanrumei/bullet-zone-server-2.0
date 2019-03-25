/*    */ package org.apache.tomcat.websocket;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
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
/*    */ public abstract class Authenticator
/*    */ {
/* 29 */   private static final Pattern pattern = Pattern.compile("(\\w+)\\s*=\\s*(\"([^\"]+)\"|([^,=\"]+))\\s*,?");
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract String getAuthorization(String paramString1, String paramString2, Map<String, Object> paramMap)
/*    */     throws AuthenticationException;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract String getSchemeName();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Map<String, String> parseWWWAuthenticateHeader(String WWWAuthenticate)
/*    */   {
/* 55 */     Matcher m = pattern.matcher(WWWAuthenticate);
/* 56 */     Map<String, String> challenge = new HashMap();
/*    */     
/* 58 */     while (m.find()) {
/* 59 */       String key = m.group(1);
/* 60 */       String qtedValue = m.group(3);
/* 61 */       String value = m.group(4);
/*    */       
/* 63 */       challenge.put(key, qtedValue != null ? qtedValue : value);
/*    */     }
/*    */     
/*    */ 
/* 67 */     return challenge;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\Authenticator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */