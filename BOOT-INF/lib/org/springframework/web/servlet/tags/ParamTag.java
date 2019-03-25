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
/*    */ public class ParamTag
/*    */   extends BodyTagSupport
/*    */ {
/*    */   private String name;
/*    */   private String value;
/*    */   private boolean valueSet;
/*    */   
/*    */   public void setName(String name)
/*    */   {
/* 48 */     this.name = name;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void setValue(String value)
/*    */   {
/* 55 */     this.value = value;
/* 56 */     this.valueSet = true;
/*    */   }
/*    */   
/*    */   public int doEndTag()
/*    */     throws JspException
/*    */   {
/* 62 */     Param param = new Param();
/* 63 */     param.setName(this.name);
/* 64 */     if (this.valueSet) {
/* 65 */       param.setValue(this.value);
/*    */     }
/* 67 */     else if (getBodyContent() != null)
/*    */     {
/* 69 */       param.setValue(getBodyContent().getString().trim());
/*    */     }
/*    */     
/*    */ 
/* 73 */     ParamAware paramAwareTag = (ParamAware)findAncestorWithClass(this, ParamAware.class);
/* 74 */     if (paramAwareTag == null) {
/* 75 */       throw new JspException("The param tag must be a descendant of a tag that supports parameters");
/*    */     }
/*    */     
/* 78 */     paramAwareTag.addParam(param);
/*    */     
/* 80 */     return 6;
/*    */   }
/*    */   
/*    */   public void release()
/*    */   {
/* 85 */     super.release();
/* 86 */     this.name = null;
/* 87 */     this.value = null;
/* 88 */     this.valueSet = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\ParamTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */