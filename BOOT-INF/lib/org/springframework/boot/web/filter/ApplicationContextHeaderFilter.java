/*    */ package org.springframework.boot.web.filter;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.FilterChain;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.web.filter.OncePerRequestFilter;
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
/*    */ public class ApplicationContextHeaderFilter
/*    */   extends OncePerRequestFilter
/*    */ {
/*    */   public static final String HEADER_NAME = "X-Application-Context";
/*    */   private final ApplicationContext applicationContext;
/*    */   
/*    */   public ApplicationContextHeaderFilter(ApplicationContext context)
/*    */   {
/* 47 */     this.applicationContext = context;
/*    */   }
/*    */   
/*    */ 
/*    */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
/*    */     throws ServletException, IOException
/*    */   {
/* 54 */     response.addHeader("X-Application-Context", this.applicationContext.getId());
/* 55 */     filterChain.doFilter(request, response);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\filter\ApplicationContextHeaderFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */