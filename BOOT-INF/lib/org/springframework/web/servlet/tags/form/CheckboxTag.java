/*    */ package org.springframework.web.servlet.tags.form;
/*    */ 
/*    */ import javax.servlet.jsp.JspException;
/*    */ import org.springframework.web.servlet.support.BindStatus;
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
/*    */ 
/*    */ 
/*    */ public class CheckboxTag
/*    */   extends AbstractSingleCheckedElementTag
/*    */ {
/*    */   protected int writeTagContent(TagWriter tagWriter)
/*    */     throws JspException
/*    */   {
/* 52 */     super.writeTagContent(tagWriter);
/*    */     
/* 54 */     if (!isDisabled())
/*    */     {
/* 56 */       tagWriter.startTag("input");
/* 57 */       tagWriter.writeAttribute("type", "hidden");
/* 58 */       String name = "_" + getName();
/* 59 */       tagWriter.writeAttribute("name", name);
/* 60 */       tagWriter.writeAttribute("value", processFieldValue(name, "on", getInputType()));
/* 61 */       tagWriter.endTag();
/*    */     }
/*    */     
/* 64 */     return 0;
/*    */   }
/*    */   
/*    */   protected void writeTagDetails(TagWriter tagWriter) throws JspException
/*    */   {
/* 69 */     tagWriter.writeAttribute("type", getInputType());
/*    */     
/* 71 */     Object boundValue = getBoundValue();
/* 72 */     Class<?> valueType = getBindStatus().getValueType();
/*    */     
/* 74 */     if ((Boolean.class == valueType) || (Boolean.TYPE == valueType))
/*    */     {
/* 76 */       if ((boundValue instanceof String)) {
/* 77 */         boundValue = Boolean.valueOf((String)boundValue);
/*    */       }
/* 79 */       Boolean booleanValue = boundValue != null ? (Boolean)boundValue : Boolean.FALSE;
/* 80 */       renderFromBoolean(booleanValue, tagWriter);
/*    */     }
/*    */     else
/*    */     {
/* 84 */       Object value = getValue();
/* 85 */       if (value == null) {
/* 86 */         throw new IllegalArgumentException("Attribute 'value' is required when binding to non-boolean values");
/*    */       }
/* 88 */       Object resolvedValue = (value instanceof String) ? evaluate("value", value) : value;
/* 89 */       renderFromValue(resolvedValue, tagWriter);
/*    */     }
/*    */   }
/*    */   
/*    */   protected String getInputType()
/*    */   {
/* 95 */     return "checkbox";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\CheckboxTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */