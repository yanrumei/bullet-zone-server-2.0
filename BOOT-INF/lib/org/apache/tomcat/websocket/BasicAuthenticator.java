/*    */ package org.apache.tomcat.websocket;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Map;
/*    */ import org.apache.tomcat.util.codec.binary.Base64;
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
/*    */ public class BasicAuthenticator
/*    */   extends Authenticator
/*    */ {
/*    */   public static final String schemeName = "basic";
/*    */   public static final String charsetparam = "charset";
/*    */   
/*    */   public String getAuthorization(String requestUri, String WWWAuthenticate, Map<String, Object> userProperties)
/*    */     throws AuthenticationException
/*    */   {
/* 37 */     String userName = (String)userProperties.get("org.apache.tomcat.websocket.WS_AUTHENTICATION_USER_NAME");
/* 38 */     String password = (String)userProperties.get("org.apache.tomcat.websocket.WS_AUTHENTICATION_PASSWORD");
/*    */     
/* 40 */     if ((userName == null) || (password == null)) {
/* 41 */       throw new AuthenticationException("Failed to perform Basic authentication due to  missing user/password");
/*    */     }
/*    */     
/*    */ 
/* 45 */     Map<String, String> wwwAuthenticate = parseWWWAuthenticateHeader(WWWAuthenticate);
/*    */     
/* 47 */     String userPass = userName + ":" + password;
/*    */     Charset charset;
/*    */     Charset charset;
/* 50 */     if ((wwwAuthenticate.get("charset") != null) && 
/* 51 */       (((String)wwwAuthenticate.get("charset")).equalsIgnoreCase("UTF-8"))) {
/* 52 */       charset = StandardCharsets.UTF_8;
/*    */     } else {
/* 54 */       charset = StandardCharsets.ISO_8859_1;
/*    */     }
/*    */     
/* 57 */     String base64 = Base64.encodeBase64String(userPass.getBytes(charset));
/*    */     
/* 59 */     return " Basic " + base64;
/*    */   }
/*    */   
/*    */   public String getSchemeName()
/*    */   {
/* 64 */     return "basic";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\BasicAuthenticator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */