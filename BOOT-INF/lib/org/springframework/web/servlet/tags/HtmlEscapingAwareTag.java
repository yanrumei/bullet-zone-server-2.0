/*     */ package org.springframework.web.servlet.tags;
/*     */ 
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.PageContext;
/*     */ import org.springframework.web.servlet.support.RequestContext;
/*     */ import org.springframework.web.util.HtmlUtils;
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
/*     */ public abstract class HtmlEscapingAwareTag
/*     */   extends RequestContextAwareTag
/*     */ {
/*     */   private Boolean htmlEscape;
/*     */   
/*     */   public void setHtmlEscape(boolean htmlEscape)
/*     */     throws JspException
/*     */   {
/*  52 */     this.htmlEscape = Boolean.valueOf(htmlEscape);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isHtmlEscape()
/*     */   {
/*  61 */     if (this.htmlEscape != null) {
/*  62 */       return this.htmlEscape.booleanValue();
/*     */     }
/*     */     
/*  65 */     return isDefaultHtmlEscape();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isDefaultHtmlEscape()
/*     */   {
/*  76 */     return getRequestContext().isDefaultHtmlEscape();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isResponseEncodedHtmlEscape()
/*     */   {
/*  88 */     return getRequestContext().isResponseEncodedHtmlEscape();
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
/*     */   protected String htmlEscape(String content)
/*     */   {
/* 102 */     String out = content;
/* 103 */     if (isHtmlEscape()) {
/* 104 */       if (isResponseEncodedHtmlEscape()) {
/* 105 */         out = HtmlUtils.htmlEscape(content, this.pageContext.getResponse().getCharacterEncoding());
/*     */       }
/*     */       else {
/* 108 */         out = HtmlUtils.htmlEscape(content);
/*     */       }
/*     */     }
/* 111 */     return out;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\HtmlEscapingAwareTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */