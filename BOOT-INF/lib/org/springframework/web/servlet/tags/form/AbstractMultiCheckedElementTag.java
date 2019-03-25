/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.PropertyAccessorFactory;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public abstract class AbstractMultiCheckedElementTag
/*     */   extends AbstractCheckedElementTag
/*     */ {
/*     */   private static final String SPAN_TAG = "span";
/*     */   private Object items;
/*     */   private String itemValue;
/*     */   private String itemLabel;
/*  69 */   private String element = "span";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String delimiter;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setItems(Object items)
/*     */   {
/*  84 */     Assert.notNull(items, "'items' must not be null");
/*  85 */     this.items = items;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object getItems()
/*     */   {
/*  93 */     return this.items;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setItemValue(String itemValue)
/*     */   {
/* 102 */     Assert.hasText(itemValue, "'itemValue' must not be empty");
/* 103 */     this.itemValue = itemValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getItemValue()
/*     */   {
/* 111 */     return this.itemValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setItemLabel(String itemLabel)
/*     */   {
/* 120 */     Assert.hasText(itemLabel, "'itemLabel' must not be empty");
/* 121 */     this.itemLabel = itemLabel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getItemLabel()
/*     */   {
/* 129 */     return this.itemLabel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDelimiter(String delimiter)
/*     */   {
/* 138 */     this.delimiter = delimiter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDelimiter()
/*     */   {
/* 146 */     return this.delimiter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setElement(String element)
/*     */   {
/* 155 */     Assert.hasText(element, "'element' cannot be null or blank");
/* 156 */     this.element = element;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getElement()
/*     */   {
/* 164 */     return this.element;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String resolveId()
/*     */     throws JspException
/*     */   {
/* 174 */     Object id = evaluate("id", getId());
/* 175 */     if (id != null) {
/* 176 */       String idString = id.toString();
/* 177 */       return StringUtils.hasText(idString) ? TagIdGenerator.nextId(idString, this.pageContext) : null;
/*     */     }
/* 179 */     return autogenerateId();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int writeTagContent(TagWriter tagWriter)
/*     */     throws JspException
/*     */   {
/* 190 */     Object items = getItems();
/* 191 */     Object itemsObject = (items instanceof String) ? evaluate("items", items) : items;
/*     */     
/* 193 */     String itemValue = getItemValue();
/* 194 */     String itemLabel = getItemLabel();
/*     */     
/* 196 */     String valueProperty = itemValue != null ? ObjectUtils.getDisplayString(evaluate("itemValue", itemValue)) : null;
/*     */     
/* 198 */     String labelProperty = itemLabel != null ? ObjectUtils.getDisplayString(evaluate("itemLabel", itemLabel)) : null;
/*     */     
/* 200 */     Class<?> boundType = getBindStatus().getValueType();
/* 201 */     if ((itemsObject == null) && (boundType != null) && (boundType.isEnum())) {
/* 202 */       itemsObject = boundType.getEnumConstants();
/*     */     }
/*     */     
/* 205 */     if (itemsObject == null) {
/* 206 */       throw new IllegalArgumentException("Attribute 'items' is required and must be a Collection, an Array or a Map");
/*     */     }
/*     */     
/* 209 */     if (itemsObject.getClass().isArray()) {
/* 210 */       Object[] itemsArray = (Object[])itemsObject;
/* 211 */       for (int i = 0; i < itemsArray.length; i++) {
/* 212 */         Object item = itemsArray[i];
/* 213 */         writeObjectEntry(tagWriter, valueProperty, labelProperty, item, i);
/*     */       }
/*     */     }
/* 216 */     else if ((itemsObject instanceof Collection)) {
/* 217 */       Collection<?> optionCollection = (Collection)itemsObject;
/* 218 */       int itemIndex = 0;
/* 219 */       for (Iterator<?> it = optionCollection.iterator(); it.hasNext(); itemIndex++) {
/* 220 */         Object item = it.next();
/* 221 */         writeObjectEntry(tagWriter, valueProperty, labelProperty, item, itemIndex);
/*     */       }
/*     */     }
/* 224 */     else if ((itemsObject instanceof Map)) {
/* 225 */       Map<?, ?> optionMap = (Map)itemsObject;
/* 226 */       int itemIndex = 0;
/* 227 */       for (Iterator it = optionMap.entrySet().iterator(); it.hasNext(); itemIndex++) {
/* 228 */         Map.Entry entry = (Map.Entry)it.next();
/* 229 */         writeMapEntry(tagWriter, valueProperty, labelProperty, entry, itemIndex);
/*     */       }
/*     */     }
/*     */     else {
/* 233 */       throw new IllegalArgumentException("Attribute 'items' must be an array, a Collection or a Map");
/*     */     }
/*     */     
/* 236 */     return 0;
/*     */   }
/*     */   
/*     */   private void writeObjectEntry(TagWriter tagWriter, String valueProperty, String labelProperty, Object item, int itemIndex)
/*     */     throws JspException
/*     */   {
/* 242 */     BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(item);
/*     */     Object renderValue;
/* 244 */     Object renderValue; if (valueProperty != null) {
/* 245 */       renderValue = wrapper.getPropertyValue(valueProperty);
/*     */     } else { Object renderValue;
/* 247 */       if ((item instanceof Enum)) {
/* 248 */         renderValue = ((Enum)item).name();
/*     */       }
/*     */       else
/* 251 */         renderValue = item;
/*     */     }
/* 253 */     Object renderLabel = labelProperty != null ? wrapper.getPropertyValue(labelProperty) : item;
/* 254 */     writeElementTag(tagWriter, item, renderValue, renderLabel, itemIndex);
/*     */   }
/*     */   
/*     */   private void writeMapEntry(TagWriter tagWriter, String valueProperty, String labelProperty, Map.Entry<?, ?> entry, int itemIndex)
/*     */     throws JspException
/*     */   {
/* 260 */     Object mapKey = entry.getKey();
/* 261 */     Object mapValue = entry.getValue();
/* 262 */     BeanWrapper mapKeyWrapper = PropertyAccessorFactory.forBeanPropertyAccess(mapKey);
/* 263 */     BeanWrapper mapValueWrapper = PropertyAccessorFactory.forBeanPropertyAccess(mapValue);
/*     */     
/* 265 */     Object renderValue = valueProperty != null ? mapKeyWrapper.getPropertyValue(valueProperty) : mapKey.toString();
/*     */     
/* 267 */     Object renderLabel = labelProperty != null ? mapValueWrapper.getPropertyValue(labelProperty) : mapValue.toString();
/* 268 */     writeElementTag(tagWriter, mapKey, renderValue, renderLabel, itemIndex);
/*     */   }
/*     */   
/*     */   private void writeElementTag(TagWriter tagWriter, Object item, Object value, Object label, int itemIndex)
/*     */     throws JspException
/*     */   {
/* 274 */     tagWriter.startTag(getElement());
/* 275 */     if (itemIndex > 0) {
/* 276 */       Object resolvedDelimiter = evaluate("delimiter", getDelimiter());
/* 277 */       if (resolvedDelimiter != null) {
/* 278 */         tagWriter.appendValue(resolvedDelimiter.toString());
/*     */       }
/*     */     }
/* 281 */     tagWriter.startTag("input");
/* 282 */     String id = resolveId();
/* 283 */     writeOptionalAttribute(tagWriter, "id", id);
/* 284 */     writeOptionalAttribute(tagWriter, "name", getName());
/* 285 */     writeOptionalAttributes(tagWriter);
/* 286 */     tagWriter.writeAttribute("type", getInputType());
/* 287 */     renderFromValue(item, value, tagWriter);
/* 288 */     tagWriter.endTag();
/* 289 */     tagWriter.startTag("label");
/* 290 */     tagWriter.writeAttribute("for", id);
/* 291 */     tagWriter.appendValue(convertToDisplayString(label));
/* 292 */     tagWriter.endTag();
/* 293 */     tagWriter.endTag();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\AbstractMultiCheckedElementTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */