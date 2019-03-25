/*    */ package org.springframework.web.servlet.tags;
/*    */ 
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.jsp.JspException;
/*    */ import javax.servlet.jsp.PageContext;
/*    */ import org.springframework.validation.Errors;
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
/*    */ 
/*    */ 
/*    */ public class BindErrorsTag
/*    */   extends HtmlEscapingAwareTag
/*    */ {
/*    */   public static final String ERRORS_VARIABLE_NAME = "errors";
/*    */   private String name;
/*    */   private Errors errors;
/*    */   
/*    */   public void setName(String name)
/*    */   {
/* 50 */     this.name = name;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getName()
/*    */   {
/* 57 */     return this.name;
/*    */   }
/*    */   
/*    */   protected final int doStartTagInternal()
/*    */     throws ServletException, JspException
/*    */   {
/* 63 */     this.errors = getRequestContext().getErrors(this.name, isHtmlEscape());
/* 64 */     if ((this.errors != null) && (this.errors.hasErrors())) {
/* 65 */       this.pageContext.setAttribute("errors", this.errors, 2);
/* 66 */       return 1;
/*    */     }
/*    */     
/* 69 */     return 0;
/*    */   }
/*    */   
/*    */ 
/*    */   public int doEndTag()
/*    */   {
/* 75 */     this.pageContext.removeAttribute("errors", 2);
/* 76 */     return 6;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final Errors getErrors()
/*    */   {
/* 84 */     return this.errors;
/*    */   }
/*    */   
/*    */ 
/*    */   public void doFinally()
/*    */   {
/* 90 */     super.doFinally();
/* 91 */     this.errors = null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\BindErrorsTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */