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
/*    */ public class RadioButtonTag
/*    */   extends AbstractSingleCheckedElementTag
/*    */ {
/*    */   protected void writeTagDetails(TagWriter tagWriter)
/*    */     throws JspException
/*    */   {
/* 40 */     tagWriter.writeAttribute("type", getInputType());
/* 41 */     Object resolvedValue = evaluate("value", getValue());
/* 42 */     renderFromValue(resolvedValue, tagWriter);
/*    */   }
/*    */   
/*    */   protected String getInputType()
/*    */   {
/* 47 */     return "radio";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\RadioButtonTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */