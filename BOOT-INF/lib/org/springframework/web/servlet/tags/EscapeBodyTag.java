/*     */ package org.springframework.web.servlet.tags;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.JspWriter;
/*     */ import javax.servlet.jsp.tagext.BodyContent;
/*     */ import javax.servlet.jsp.tagext.BodyTag;
/*     */ import org.springframework.web.util.JavaScriptUtils;
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
/*     */ public class EscapeBodyTag
/*     */   extends HtmlEscapingAwareTag
/*     */   implements BodyTag
/*     */ {
/*  46 */   private boolean javaScriptEscape = false;
/*     */   
/*     */ 
/*     */   private BodyContent bodyContent;
/*     */   
/*     */ 
/*     */ 
/*     */   public void setJavaScriptEscape(boolean javaScriptEscape)
/*     */     throws JspException
/*     */   {
/*  56 */     this.javaScriptEscape = javaScriptEscape;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected int doStartTagInternal()
/*     */   {
/*  63 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void doInitBody() {}
/*     */   
/*     */ 
/*     */   public void setBodyContent(BodyContent bodyContent)
/*     */   {
/*  73 */     this.bodyContent = bodyContent;
/*     */   }
/*     */   
/*     */   public int doAfterBody() throws JspException
/*     */   {
/*     */     try {
/*  79 */       String content = readBodyContent();
/*     */       
/*  81 */       content = htmlEscape(content);
/*  82 */       content = this.javaScriptEscape ? JavaScriptUtils.javaScriptEscape(content) : content;
/*  83 */       writeBodyContent(content);
/*     */     }
/*     */     catch (IOException ex) {
/*  86 */       throw new JspException("Could not write escaped body", ex);
/*     */     }
/*  88 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String readBodyContent()
/*     */     throws IOException
/*     */   {
/*  97 */     return this.bodyContent.getString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void writeBodyContent(String content)
/*     */     throws IOException
/*     */   {
/* 107 */     this.bodyContent.getEnclosingWriter().print(content);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\EscapeBodyTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */