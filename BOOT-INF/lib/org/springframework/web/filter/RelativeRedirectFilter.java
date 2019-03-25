/*    */ package org.springframework.web.filter;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.FilterChain;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RelativeRedirectFilter
/*    */   extends OncePerRequestFilter
/*    */ {
/* 44 */   private HttpStatus redirectStatus = HttpStatus.SEE_OTHER;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setRedirectStatus(HttpStatus status)
/*    */   {
/* 53 */     Assert.notNull(status, "Property 'redirectStatus' is required");
/* 54 */     Assert.isTrue(status.is3xxRedirection(), "Not a redirect status code");
/* 55 */     this.redirectStatus = status;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public HttpStatus getRedirectStatus()
/*    */   {
/* 62 */     return this.redirectStatus;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
/*    */     throws ServletException, IOException
/*    */   {
/* 70 */     response = RelativeRedirectResponseWrapper.wrapIfNecessary(response, this.redirectStatus);
/* 71 */     filterChain.doFilter(request, response);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\filter\RelativeRedirectFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */