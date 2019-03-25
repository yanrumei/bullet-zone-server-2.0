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
/*    */ public class HiddenInputTag
/*    */   extends AbstractHtmlElementTag
/*    */ {
/*    */   public static final String DISABLED_ATTRIBUTE = "disabled";
/*    */   private boolean disabled;
/*    */   
/*    */   public void setDisabled(boolean disabled)
/*    */   {
/* 51 */     this.disabled = disabled;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean isDisabled()
/*    */   {
/* 58 */     return this.disabled;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected boolean isValidDynamicAttribute(String localName, Object value)
/*    */   {
/* 67 */     return !"type".equals(localName);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected int writeTagContent(TagWriter tagWriter)
/*    */     throws JspException
/*    */   {
/* 78 */     tagWriter.startTag("input");
/* 79 */     writeDefaultAttributes(tagWriter);
/* 80 */     tagWriter.writeAttribute("type", "hidden");
/* 81 */     if (isDisabled()) {
/* 82 */       tagWriter.writeAttribute("disabled", "disabled");
/*    */     }
/* 84 */     String value = getDisplayString(getBoundValue(), getPropertyEditor());
/* 85 */     tagWriter.writeAttribute("value", processFieldValue(getName(), value, "hidden"));
/* 86 */     tagWriter.endTag();
/* 87 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\HiddenInputTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */