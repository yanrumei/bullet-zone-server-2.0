/*    */ package org.springframework.web.servlet.tags;
/*    */ 
/*    */ import javax.servlet.jsp.JspException;
/*    */ import javax.servlet.jsp.tagext.BodyContent;
/*    */ import javax.servlet.jsp.tagext.BodyTagSupport;
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
/*    */ public class ArgumentTag
/*    */   extends BodyTagSupport
/*    */ {
/*    */   private Object value;
/*    */   private boolean valueSet;
/*    */   
/*    */   public void setValue(Object value)
/*    */   {
/* 47 */     this.value = value;
/* 48 */     this.valueSet = true;
/*    */   }
/*    */   
/*    */   public int doEndTag()
/*    */     throws JspException
/*    */   {
/* 54 */     Object argument = null;
/* 55 */     if (this.valueSet) {
/* 56 */       argument = this.value;
/*    */     }
/* 58 */     else if (getBodyContent() != null)
/*    */     {
/* 60 */       argument = getBodyContent().getString().trim();
/*    */     }
/*    */     
/*    */ 
/* 64 */     ArgumentAware argumentAwareTag = (ArgumentAware)findAncestorWithClass(this, ArgumentAware.class);
/* 65 */     if (argumentAwareTag == null) {
/* 66 */       throw new JspException("The argument tag must be a descendant of a tag that supports arguments");
/*    */     }
/* 68 */     argumentAwareTag.addArgument(argument);
/*    */     
/* 70 */     return 6;
/*    */   }
/*    */   
/*    */   public void release()
/*    */   {
/* 75 */     super.release();
/* 76 */     this.value = null;
/* 77 */     this.valueSet = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\ArgumentTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */