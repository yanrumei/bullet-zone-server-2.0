/*    */ package org.springframework.web.servlet.tags.form;
/*    */ 
/*    */ import javax.servlet.jsp.JspException;
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
/*    */ public class PasswordInputTag
/*    */   extends InputTag
/*    */ {
/* 33 */   private boolean showPassword = false;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setShowPassword(boolean showPassword)
/*    */   {
/* 41 */     this.showPassword = showPassword;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isShowPassword()
/*    */   {
/* 49 */     return this.showPassword;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected boolean isValidDynamicAttribute(String localName, Object value)
/*    */   {
/* 58 */     return !"type".equals(localName);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected String getType()
/*    */   {
/* 67 */     return "password";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void writeValue(TagWriter tagWriter)
/*    */     throws JspException
/*    */   {
/* 77 */     if (this.showPassword) {
/* 78 */       super.writeValue(tagWriter);
/*    */     }
/*    */     else {
/* 81 */       tagWriter.writeAttribute("value", processFieldValue(getName(), "", getType()));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\PasswordInputTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */