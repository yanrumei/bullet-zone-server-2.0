/*     */ package org.apache.catalina.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SetCharacterEncodingFilter
/*     */   extends FilterBase
/*     */ {
/*  58 */   private static final Log log = LogFactory.getLog(SetCharacterEncodingFilter.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  67 */   private String encoding = null;
/*  68 */   public void setEncoding(String encoding) { this.encoding = encoding; }
/*  69 */   public String getEncoding() { return this.encoding; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  75 */   private boolean ignore = false;
/*  76 */   public void setIgnore(boolean ignore) { this.ignore = ignore; }
/*  77 */   public boolean isIgnore() { return this.ignore; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
/*     */     throws IOException, ServletException
/*     */   {
/* 100 */     if ((this.ignore) || (request.getCharacterEncoding() == null)) {
/* 101 */       String characterEncoding = selectEncoding(request);
/* 102 */       if (characterEncoding != null) {
/* 103 */         request.setCharacterEncoding(characterEncoding);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 108 */     chain.doFilter(request, response);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Log getLogger()
/*     */   {
/* 116 */     return log;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String selectEncoding(ServletRequest request)
/*     */   {
/* 134 */     return this.encoding;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\filters\SetCharacterEncodingFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */