/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.PageContext;
/*     */ import javax.servlet.jsp.tagext.BodyContent;
/*     */ import javax.servlet.jsp.tagext.BodyTag;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.servlet.support.BindStatus;
/*     */ import org.springframework.web.util.TagUtils;
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
/*     */ public class OptionTag
/*     */   extends AbstractHtmlElementBodyTag
/*     */   implements BodyTag
/*     */ {
/*     */   public static final String VALUE_VARIABLE_NAME = "value";
/*     */   public static final String DISPLAY_VALUE_VARIABLE_NAME = "displayValue";
/*     */   private static final String SELECTED_ATTRIBUTE = "selected";
/*     */   private static final String VALUE_ATTRIBUTE = "value";
/*     */   private static final String DISABLED_ATTRIBUTE = "disabled";
/*     */   private Object value;
/*     */   private String label;
/*     */   private Object oldValue;
/*     */   private Object oldDisplayValue;
/*     */   private boolean disabled;
/*     */   
/*     */   public void setValue(Object value)
/*     */   {
/*  99 */     this.value = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Object getValue()
/*     */   {
/* 106 */     return this.value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setDisabled(boolean disabled)
/*     */   {
/* 113 */     this.disabled = disabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isDisabled()
/*     */   {
/* 120 */     return this.disabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLabel(String label)
/*     */   {
/* 128 */     Assert.notNull(label, "'label' must not be null");
/* 129 */     this.label = label;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getLabel()
/*     */   {
/* 136 */     return this.label;
/*     */   }
/*     */   
/*     */   protected void renderDefaultContent(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/* 142 */     Object value = this.pageContext.getAttribute("value");
/* 143 */     String label = getLabelValue(value);
/* 144 */     renderOption(value, label, tagWriter);
/*     */   }
/*     */   
/*     */   protected void renderFromBodyContent(BodyContent bodyContent, TagWriter tagWriter) throws JspException
/*     */   {
/* 149 */     Object value = this.pageContext.getAttribute("value");
/* 150 */     String label = bodyContent.getString();
/* 151 */     renderOption(value, label, tagWriter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onWriteTagContent()
/*     */   {
/* 159 */     assertUnderSelectTag();
/*     */   }
/*     */   
/*     */   protected void exposeAttributes() throws JspException
/*     */   {
/* 164 */     Object value = resolveValue();
/* 165 */     this.oldValue = this.pageContext.getAttribute("value");
/* 166 */     this.pageContext.setAttribute("value", value);
/* 167 */     this.oldDisplayValue = this.pageContext.getAttribute("displayValue");
/* 168 */     this.pageContext.setAttribute("displayValue", getDisplayString(value, getBindStatus().getEditor()));
/*     */   }
/*     */   
/*     */   protected BindStatus getBindStatus()
/*     */   {
/* 173 */     return (BindStatus)this.pageContext.getAttribute("org.springframework.web.servlet.tags.form.SelectTag.listValue");
/*     */   }
/*     */   
/*     */   protected void removeAttributes()
/*     */   {
/* 178 */     if (this.oldValue != null) {
/* 179 */       this.pageContext.setAttribute("value", this.oldValue);
/* 180 */       this.oldValue = null;
/*     */     }
/*     */     else {
/* 183 */       this.pageContext.removeAttribute("value");
/*     */     }
/*     */     
/* 186 */     if (this.oldDisplayValue != null) {
/* 187 */       this.pageContext.setAttribute("displayValue", this.oldDisplayValue);
/* 188 */       this.oldDisplayValue = null;
/*     */     }
/*     */     else {
/* 191 */       this.pageContext.removeAttribute("displayValue");
/*     */     }
/*     */   }
/*     */   
/*     */   private void renderOption(Object value, String label, TagWriter tagWriter) throws JspException {
/* 196 */     tagWriter.startTag("option");
/* 197 */     writeOptionalAttribute(tagWriter, "id", resolveId());
/* 198 */     writeOptionalAttributes(tagWriter);
/* 199 */     String renderedValue = getDisplayString(value, getBindStatus().getEditor());
/* 200 */     renderedValue = processFieldValue(getSelectTag().getName(), renderedValue, "option");
/* 201 */     tagWriter.writeAttribute("value", renderedValue);
/* 202 */     if (isSelected(value)) {
/* 203 */       tagWriter.writeAttribute("selected", "selected");
/*     */     }
/* 205 */     if (isDisabled()) {
/* 206 */       tagWriter.writeAttribute("disabled", "disabled");
/*     */     }
/* 208 */     tagWriter.appendValue(label);
/* 209 */     tagWriter.endTag();
/*     */   }
/*     */   
/*     */   protected String autogenerateId() throws JspException
/*     */   {
/* 214 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getLabelValue(Object resolvedValue)
/*     */     throws JspException
/*     */   {
/* 224 */     String label = getLabel();
/* 225 */     Object labelObj = label == null ? resolvedValue : evaluate("label", label);
/* 226 */     return getDisplayString(labelObj, getBindStatus().getEditor());
/*     */   }
/*     */   
/*     */   private void assertUnderSelectTag() {
/* 230 */     TagUtils.assertHasAncestorOfType(this, SelectTag.class, "option", "select");
/*     */   }
/*     */   
/*     */   private SelectTag getSelectTag() {
/* 234 */     return (SelectTag)findAncestorWithClass(this, SelectTag.class);
/*     */   }
/*     */   
/*     */   private boolean isSelected(Object resolvedValue) {
/* 238 */     return SelectedValueComparator.isSelected(getBindStatus(), resolvedValue);
/*     */   }
/*     */   
/*     */   private Object resolveValue() throws JspException {
/* 242 */     return evaluate("value", getValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\OptionTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */