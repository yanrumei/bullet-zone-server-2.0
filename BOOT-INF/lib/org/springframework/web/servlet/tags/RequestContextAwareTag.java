/*     */ package org.springframework.web.servlet.tags;
/*     */ 
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.JspTagException;
/*     */ import javax.servlet.jsp.PageContext;
/*     */ import javax.servlet.jsp.tagext.TagSupport;
/*     */ import javax.servlet.jsp.tagext.TryCatchFinally;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.web.servlet.support.JspAwareRequestContext;
/*     */ import org.springframework.web.servlet.support.RequestContext;
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
/*     */ public abstract class RequestContextAwareTag
/*     */   extends TagSupport
/*     */   implements TryCatchFinally
/*     */ {
/*     */   public static final String REQUEST_CONTEXT_PAGE_ATTRIBUTE = "org.springframework.web.servlet.tags.REQUEST_CONTEXT";
/*  60 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private RequestContext requestContext;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int doStartTag()
/*     */     throws JspException
/*     */   {
/*     */     try
/*     */     {
/*  75 */       this.requestContext = ((RequestContext)this.pageContext.getAttribute("org.springframework.web.servlet.tags.REQUEST_CONTEXT"));
/*  76 */       if (this.requestContext == null) {
/*  77 */         this.requestContext = new JspAwareRequestContext(this.pageContext);
/*  78 */         this.pageContext.setAttribute("org.springframework.web.servlet.tags.REQUEST_CONTEXT", this.requestContext);
/*     */       }
/*  80 */       return doStartTagInternal();
/*     */     }
/*     */     catch (JspException ex) {
/*  83 */       this.logger.error(ex.getMessage(), ex);
/*  84 */       throw ex;
/*     */     }
/*     */     catch (RuntimeException ex) {
/*  87 */       this.logger.error(ex.getMessage(), ex);
/*  88 */       throw ex;
/*     */     }
/*     */     catch (Exception ex) {
/*  91 */       this.logger.error(ex.getMessage(), ex);
/*  92 */       throw new JspTagException(ex.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final RequestContext getRequestContext()
/*     */   {
/* 100 */     return this.requestContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract int doStartTagInternal()
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doCatch(Throwable throwable)
/*     */     throws Throwable
/*     */   {
/* 115 */     throw throwable;
/*     */   }
/*     */   
/*     */   public void doFinally()
/*     */   {
/* 120 */     this.requestContext = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\RequestContextAwareTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */