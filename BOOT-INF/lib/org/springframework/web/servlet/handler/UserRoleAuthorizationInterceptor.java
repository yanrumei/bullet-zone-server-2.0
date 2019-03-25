/*    */ package org.springframework.web.servlet.handler;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
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
/*    */ public class UserRoleAuthorizationInterceptor
/*    */   extends HandlerInterceptorAdapter
/*    */ {
/*    */   private String[] authorizedRoles;
/*    */   
/*    */   public final void setAuthorizedRoles(String... authorizedRoles)
/*    */   {
/* 42 */     this.authorizedRoles = authorizedRoles;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
/*    */     throws ServletException, IOException
/*    */   {
/* 50 */     if (this.authorizedRoles != null) {
/* 51 */       for (String role : this.authorizedRoles) {
/* 52 */         if (request.isUserInRole(role)) {
/* 53 */           return true;
/*    */         }
/*    */       }
/*    */     }
/* 57 */     handleNotAuthorized(request, response, handler);
/* 58 */     return false;
/*    */   }
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
/*    */   protected void handleNotAuthorized(HttpServletRequest request, HttpServletResponse response, Object handler)
/*    */     throws ServletException, IOException
/*    */   {
/* 75 */     response.sendError(403);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\handler\UserRoleAuthorizationInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */