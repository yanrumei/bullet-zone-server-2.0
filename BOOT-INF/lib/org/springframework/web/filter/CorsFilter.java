/*    */ package org.springframework.web.filter;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.FilterChain;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.cors.CorsConfiguration;
/*    */ import org.springframework.web.cors.CorsConfigurationSource;
/*    */ import org.springframework.web.cors.CorsProcessor;
/*    */ import org.springframework.web.cors.CorsUtils;
/*    */ import org.springframework.web.cors.DefaultCorsProcessor;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CorsFilter
/*    */   extends OncePerRequestFilter
/*    */ {
/*    */   private final CorsConfigurationSource configSource;
/* 57 */   private CorsProcessor processor = new DefaultCorsProcessor();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public CorsFilter(CorsConfigurationSource configSource)
/*    */   {
/* 66 */     Assert.notNull(configSource, "CorsConfigurationSource must not be null");
/* 67 */     this.configSource = configSource;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setCorsProcessor(CorsProcessor processor)
/*    */   {
/* 77 */     Assert.notNull(processor, "CorsProcessor must not be null");
/* 78 */     this.processor = processor;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
/*    */     throws ServletException, IOException
/*    */   {
/* 86 */     if (CorsUtils.isCorsRequest(request)) {
/* 87 */       CorsConfiguration corsConfiguration = this.configSource.getCorsConfiguration(request);
/* 88 */       if (corsConfiguration != null) {
/* 89 */         boolean isValid = this.processor.processRequest(corsConfiguration, request, response);
/* 90 */         if ((!isValid) || (CorsUtils.isPreFlightRequest(request))) {
/* 91 */           return;
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 96 */     filterChain.doFilter(request, response);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\filter\CorsFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */