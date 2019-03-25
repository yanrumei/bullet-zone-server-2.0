/*    */ package org.springframework.boot.autoconfigure.security;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.security.core.AuthenticationException;
/*    */ import org.springframework.security.web.AuthenticationEntryPoint;
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
/*    */ public class Http401AuthenticationEntryPoint
/*    */   implements AuthenticationEntryPoint
/*    */ {
/*    */   private final String headerValue;
/*    */   
/*    */   public Http401AuthenticationEntryPoint(String headerValue)
/*    */   {
/* 42 */     this.headerValue = headerValue;
/*    */   }
/*    */   
/*    */   public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
/*    */     throws IOException, ServletException
/*    */   {
/* 48 */     response.setHeader("WWW-Authenticate", this.headerValue);
/* 49 */     response.sendError(401, authException
/* 50 */       .getMessage());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\Http401AuthenticationEntryPoint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */