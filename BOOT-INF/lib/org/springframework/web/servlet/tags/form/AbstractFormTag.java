/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import org.springframework.web.servlet.support.RequestContext;
/*     */ import org.springframework.web.servlet.tags.HtmlEscapingAwareTag;
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
/*     */ public abstract class AbstractFormTag
/*     */   extends HtmlEscapingAwareTag
/*     */ {
/*     */   protected Object evaluate(String attributeName, Object value)
/*     */     throws JspException
/*     */   {
/*  48 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void writeOptionalAttribute(TagWriter tagWriter, String attributeName, String value)
/*     */     throws JspException
/*     */   {
/*  62 */     if (value != null) {
/*  63 */       tagWriter.writeOptionalAttributeValue(attributeName, getDisplayString(evaluate(attributeName, value)));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TagWriter createTagWriter()
/*     */   {
/*  74 */     return new TagWriter(this.pageContext);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final int doStartTagInternal()
/*     */     throws Exception
/*     */   {
/*  84 */     return writeTagContent(createTagWriter());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDisplayString(Object value)
/*     */   {
/*  92 */     return ValueFormatter.getDisplayString(value, isHtmlEscape());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getDisplayString(Object value, PropertyEditor propertyEditor)
/*     */   {
/* 102 */     return ValueFormatter.getDisplayString(value, propertyEditor, isHtmlEscape());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isDefaultHtmlEscape()
/*     */   {
/* 110 */     Boolean defaultHtmlEscape = getRequestContext().getDefaultHtmlEscape();
/* 111 */     return (defaultHtmlEscape == null) || (defaultHtmlEscape.booleanValue());
/*     */   }
/*     */   
/*     */   protected abstract int writeTagContent(TagWriter paramTagWriter)
/*     */     throws JspException;
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\AbstractFormTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */