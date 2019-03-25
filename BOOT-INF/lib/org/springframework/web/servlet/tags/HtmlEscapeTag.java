/*    */ package org.springframework.web.servlet.tags;
/*    */ 
/*    */ import javax.servlet.jsp.JspException;
/*    */ import org.springframework.web.servlet.support.RequestContext;
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
/*    */ public class HtmlEscapeTag
/*    */   extends RequestContextAwareTag
/*    */ {
/*    */   private boolean defaultHtmlEscape;
/*    */   
/*    */   public void setDefaultHtmlEscape(boolean defaultHtmlEscape)
/*    */   {
/* 43 */     this.defaultHtmlEscape = defaultHtmlEscape;
/*    */   }
/*    */   
/*    */   protected int doStartTagInternal()
/*    */     throws JspException
/*    */   {
/* 49 */     getRequestContext().setDefaultHtmlEscape(this.defaultHtmlEscape);
/* 50 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\HtmlEscapeTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */