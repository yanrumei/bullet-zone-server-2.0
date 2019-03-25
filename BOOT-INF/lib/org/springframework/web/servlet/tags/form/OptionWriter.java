/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.PropertyAccessorFactory;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.web.servlet.support.BindStatus;
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
/*     */ class OptionWriter
/*     */ {
/*     */   private final Object optionSource;
/*     */   private final BindStatus bindStatus;
/*     */   private final String valueProperty;
/*     */   private final String labelProperty;
/*     */   private final boolean htmlEscape;
/*     */   
/*     */   public OptionWriter(Object optionSource, BindStatus bindStatus, String valueProperty, String labelProperty, boolean htmlEscape)
/*     */   {
/* 114 */     Assert.notNull(optionSource, "'optionSource' must not be null");
/* 115 */     Assert.notNull(bindStatus, "'bindStatus' must not be null");
/* 116 */     this.optionSource = optionSource;
/* 117 */     this.bindStatus = bindStatus;
/* 118 */     this.valueProperty = valueProperty;
/* 119 */     this.labelProperty = labelProperty;
/* 120 */     this.htmlEscape = htmlEscape;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeOptions(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/* 129 */     if (this.optionSource.getClass().isArray()) {
/* 130 */       renderFromArray(tagWriter);
/*     */     }
/* 132 */     else if ((this.optionSource instanceof Collection)) {
/* 133 */       renderFromCollection(tagWriter);
/*     */     }
/* 135 */     else if ((this.optionSource instanceof Map)) {
/* 136 */       renderFromMap(tagWriter);
/*     */     }
/* 138 */     else if (((this.optionSource instanceof Class)) && (((Class)this.optionSource).isEnum())) {
/* 139 */       renderFromEnum(tagWriter);
/*     */     }
/*     */     else
/*     */     {
/* 143 */       throw new JspException("Type [" + this.optionSource.getClass().getName() + "] is not valid for option items");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void renderFromArray(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/* 152 */     doRenderFromCollection(CollectionUtils.arrayToList(this.optionSource), tagWriter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void renderFromMap(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/* 161 */     Map<?, ?> optionMap = (Map)this.optionSource;
/* 162 */     for (Map.Entry<?, ?> entry : optionMap.entrySet()) {
/* 163 */       Object mapKey = entry.getKey();
/* 164 */       Object mapValue = entry.getValue();
/*     */       
/* 166 */       Object renderValue = this.valueProperty != null ? PropertyAccessorFactory.forBeanPropertyAccess(mapKey).getPropertyValue(this.valueProperty) : mapKey;
/*     */       
/*     */ 
/* 169 */       Object renderLabel = this.labelProperty != null ? PropertyAccessorFactory.forBeanPropertyAccess(mapValue).getPropertyValue(this.labelProperty) : mapValue;
/*     */       
/* 171 */       renderOption(tagWriter, mapKey, renderValue, renderLabel);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void renderFromCollection(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/* 180 */     doRenderFromCollection((Collection)this.optionSource, tagWriter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void renderFromEnum(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/* 188 */     doRenderFromCollection(CollectionUtils.arrayToList(((Class)this.optionSource).getEnumConstants()), tagWriter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void doRenderFromCollection(Collection<?> optionCollection, TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/* 198 */     for (Object item : optionCollection) {
/* 199 */       BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(item);
/*     */       Object value;
/* 201 */       Object value; if (this.valueProperty != null) {
/* 202 */         value = wrapper.getPropertyValue(this.valueProperty);
/*     */       } else { Object value;
/* 204 */         if ((item instanceof Enum)) {
/* 205 */           value = ((Enum)item).name();
/*     */         }
/*     */         else
/* 208 */           value = item;
/*     */       }
/* 210 */       Object label = this.labelProperty != null ? wrapper.getPropertyValue(this.labelProperty) : item;
/* 211 */       renderOption(tagWriter, item, value, label);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void renderOption(TagWriter tagWriter, Object item, Object value, Object label)
/*     */     throws JspException
/*     */   {
/* 220 */     tagWriter.startTag("option");
/* 221 */     writeCommonAttributes(tagWriter);
/*     */     
/* 223 */     String valueDisplayString = getDisplayString(value);
/* 224 */     String labelDisplayString = getDisplayString(label);
/*     */     
/* 226 */     valueDisplayString = processOptionValue(valueDisplayString);
/*     */     
/*     */ 
/* 229 */     tagWriter.writeAttribute("value", valueDisplayString);
/*     */     
/* 231 */     if ((isOptionSelected(value)) || ((value != item) && (isOptionSelected(item)))) {
/* 232 */       tagWriter.writeAttribute("selected", "selected");
/*     */     }
/* 234 */     if (isOptionDisabled()) {
/* 235 */       tagWriter.writeAttribute("disabled", "disabled");
/*     */     }
/* 237 */     tagWriter.appendValue(labelDisplayString);
/* 238 */     tagWriter.endTag();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getDisplayString(Object value)
/*     */   {
/* 246 */     PropertyEditor editor = value != null ? this.bindStatus.findEditor(value.getClass()) : null;
/* 247 */     return ValueFormatter.getDisplayString(value, editor, this.htmlEscape);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String processOptionValue(String resolvedValue)
/*     */   {
/* 255 */     return resolvedValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isOptionSelected(Object resolvedValue)
/*     */   {
/* 263 */     return SelectedValueComparator.isSelected(this.bindStatus, resolvedValue);
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean isOptionDisabled()
/*     */     throws JspException
/*     */   {
/* 270 */     return false;
/*     */   }
/*     */   
/*     */   protected void writeCommonAttributes(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\OptionWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */