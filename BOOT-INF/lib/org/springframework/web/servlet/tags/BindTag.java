/*     */ package org.springframework.web.servlet.tags;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import javax.servlet.jsp.JspTagException;
/*     */ import javax.servlet.jsp.PageContext;
/*     */ import org.springframework.validation.Errors;
/*     */ import org.springframework.web.servlet.support.BindStatus;
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
/*     */ public class BindTag
/*     */   extends HtmlEscapingAwareTag
/*     */   implements EditorAwareTag
/*     */ {
/*     */   public static final String STATUS_VARIABLE_NAME = "status";
/*     */   private String path;
/*  58 */   private boolean ignoreNestedPath = false;
/*     */   
/*     */ 
/*     */ 
/*     */   private BindStatus status;
/*     */   
/*     */ 
/*     */ 
/*     */   private Object previousPageStatus;
/*     */   
/*     */ 
/*     */ 
/*     */   private Object previousRequestStatus;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPath(String path)
/*     */   {
/*  77 */     this.path = path;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getPath()
/*     */   {
/*  84 */     return this.path;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIgnoreNestedPath(boolean ignoreNestedPath)
/*     */   {
/*  92 */     this.ignoreNestedPath = ignoreNestedPath;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isIgnoreNestedPath()
/*     */   {
/*  99 */     return this.ignoreNestedPath;
/*     */   }
/*     */   
/*     */   protected final int doStartTagInternal()
/*     */     throws Exception
/*     */   {
/* 105 */     String resolvedPath = getPath();
/* 106 */     if (!isIgnoreNestedPath()) {
/* 107 */       String nestedPath = (String)this.pageContext.getAttribute("nestedPath", 2);
/*     */       
/*     */ 
/* 110 */       if ((nestedPath != null) && (!resolvedPath.startsWith(nestedPath)) && 
/* 111 */         (!resolvedPath.equals(nestedPath.substring(0, nestedPath.length() - 1)))) {
/* 112 */         resolvedPath = nestedPath + resolvedPath;
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 117 */       this.status = new BindStatus(getRequestContext(), resolvedPath, isHtmlEscape());
/*     */     }
/*     */     catch (IllegalStateException ex) {
/* 120 */       throw new JspTagException(ex.getMessage());
/*     */     }
/*     */     
/*     */ 
/* 124 */     this.previousPageStatus = this.pageContext.getAttribute("status", 1);
/* 125 */     this.previousRequestStatus = this.pageContext.getAttribute("status", 2);
/*     */     
/*     */ 
/*     */ 
/* 129 */     this.pageContext.removeAttribute("status", 1);
/* 130 */     this.pageContext.setAttribute("status", this.status, 2);
/*     */     
/* 132 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */   public int doEndTag()
/*     */   {
/* 138 */     if (this.previousPageStatus != null) {
/* 139 */       this.pageContext.setAttribute("status", this.previousPageStatus, 1);
/*     */     }
/* 141 */     if (this.previousRequestStatus != null) {
/* 142 */       this.pageContext.setAttribute("status", this.previousRequestStatus, 2);
/*     */     }
/*     */     else {
/* 145 */       this.pageContext.removeAttribute("status", 2);
/*     */     }
/* 147 */     return 6;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String getProperty()
/*     */   {
/* 159 */     return this.status.getExpression();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Errors getErrors()
/*     */   {
/* 168 */     return this.status.getErrors();
/*     */   }
/*     */   
/*     */   public final PropertyEditor getEditor()
/*     */   {
/* 173 */     return this.status.getEditor();
/*     */   }
/*     */   
/*     */ 
/*     */   public void doFinally()
/*     */   {
/* 179 */     super.doFinally();
/* 180 */     this.status = null;
/* 181 */     this.previousPageStatus = null;
/* 182 */     this.previousRequestStatus = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\BindTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */