/*     */ package org.springframework.web.servlet.tags;
/*     */ 
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.PageContext;
/*     */ import javax.servlet.jsp.tagext.TagSupport;
/*     */ import javax.servlet.jsp.tagext.TryCatchFinally;
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
/*     */ 
/*     */ 
/*     */ public class NestedPathTag
/*     */   extends TagSupport
/*     */   implements TryCatchFinally
/*     */ {
/*     */   public static final String NESTED_PATH_VARIABLE_NAME = "nestedPath";
/*     */   private String path;
/*     */   private String previousNestedPath;
/*     */   
/*     */   public void setPath(String path)
/*     */   {
/*  64 */     if (path == null) {
/*  65 */       path = "";
/*     */     }
/*  67 */     if ((path.length() > 0) && (!path.endsWith("."))) {
/*  68 */       path = path + ".";
/*     */     }
/*  70 */     this.path = path;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getPath()
/*     */   {
/*  77 */     return this.path;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int doStartTag()
/*     */     throws JspException
/*     */   {
/*  86 */     this.previousNestedPath = ((String)this.pageContext.getAttribute("nestedPath", 2));
/*     */     
/*  88 */     String nestedPath = this.previousNestedPath != null ? this.previousNestedPath + getPath() : getPath();
/*  89 */     this.pageContext.setAttribute("nestedPath", nestedPath, 2);
/*     */     
/*  91 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int doEndTag()
/*     */   {
/*  99 */     if (this.previousNestedPath != null)
/*     */     {
/* 101 */       this.pageContext.setAttribute("nestedPath", this.previousNestedPath, 2);
/*     */     }
/*     */     else
/*     */     {
/* 105 */       this.pageContext.removeAttribute("nestedPath", 2);
/*     */     }
/*     */     
/* 108 */     return 6;
/*     */   }
/*     */   
/*     */   public void doCatch(Throwable throwable) throws Throwable
/*     */   {
/* 113 */     throw throwable;
/*     */   }
/*     */   
/*     */   public void doFinally()
/*     */   {
/* 118 */     this.previousNestedPath = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\NestedPathTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */