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
/*    */ public abstract class AbstractCheckedElementTag
/*    */   extends AbstractHtmlInputElementTag
/*    */ {
/*    */   protected void renderFromValue(Object value, TagWriter tagWriter)
/*    */     throws JspException
/*    */   {
/* 40 */     renderFromValue(value, value, tagWriter);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void renderFromValue(Object item, Object value, TagWriter tagWriter)
/*    */     throws JspException
/*    */   {
/* 49 */     String displayValue = convertToDisplayString(value);
/* 50 */     tagWriter.writeAttribute("value", processFieldValue(getName(), displayValue, getInputType()));
/* 51 */     if ((isOptionSelected(value)) || ((value != item) && (isOptionSelected(item)))) {
/* 52 */       tagWriter.writeAttribute("checked", "checked");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private boolean isOptionSelected(Object value)
/*    */     throws JspException
/*    */   {
/* 61 */     return SelectedValueComparator.isSelected(getBindStatus(), value);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void renderFromBoolean(Boolean boundValue, TagWriter tagWriter)
/*    */     throws JspException
/*    */   {
/* 70 */     tagWriter.writeAttribute("value", processFieldValue(getName(), "true", getInputType()));
/* 71 */     if (boundValue.booleanValue()) {
/* 72 */       tagWriter.writeAttribute("checked", "checked");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected String autogenerateId()
/*    */     throws JspException
/*    */   {
/* 81 */     return TagIdGenerator.nextId(super.autogenerateId(), this.pageContext);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected abstract int writeTagContent(TagWriter paramTagWriter)
/*    */     throws JspException;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected boolean isValidDynamicAttribute(String localName, Object value)
/*    */   {
/* 98 */     return !"type".equals(localName);
/*    */   }
/*    */   
/*    */   protected abstract String getInputType();
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\AbstractCheckedElementTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */