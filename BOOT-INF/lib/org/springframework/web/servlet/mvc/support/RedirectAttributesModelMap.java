/*     */ package org.springframework.web.servlet.mvc.support;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import org.springframework.ui.ModelMap;
/*     */ import org.springframework.validation.DataBinder;
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
/*     */ public class RedirectAttributesModelMap
/*     */   extends ModelMap
/*     */   implements RedirectAttributes
/*     */ {
/*     */   private final DataBinder dataBinder;
/*  39 */   private final ModelMap flashAttributes = new ModelMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RedirectAttributesModelMap()
/*     */   {
/*  47 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RedirectAttributesModelMap(DataBinder dataBinder)
/*     */   {
/*  55 */     this.dataBinder = dataBinder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, ?> getFlashAttributes()
/*     */   {
/*  64 */     return this.flashAttributes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RedirectAttributesModelMap addAttribute(String attributeName, Object attributeValue)
/*     */   {
/*  73 */     super.addAttribute(attributeName, formatValue(attributeValue));
/*  74 */     return this;
/*     */   }
/*     */   
/*     */   private String formatValue(Object value) {
/*  78 */     if (value == null) {
/*  79 */       return null;
/*     */     }
/*  81 */     return this.dataBinder != null ? (String)this.dataBinder.convertIfNecessary(value, String.class) : value.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RedirectAttributesModelMap addAttribute(Object attributeValue)
/*     */   {
/*  90 */     super.addAttribute(attributeValue);
/*  91 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RedirectAttributesModelMap addAllAttributes(Collection<?> attributeValues)
/*     */   {
/* 100 */     super.addAllAttributes(attributeValues);
/* 101 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RedirectAttributesModelMap addAllAttributes(Map<String, ?> attributes)
/*     */   {
/* 110 */     if (attributes != null) {
/* 111 */       for (String key : attributes.keySet()) {
/* 112 */         addAttribute(key, attributes.get(key));
/*     */       }
/*     */     }
/* 115 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RedirectAttributesModelMap mergeAttributes(Map<String, ?> attributes)
/*     */   {
/* 124 */     if (attributes != null) {
/* 125 */       for (String key : attributes.keySet()) {
/* 126 */         if (!containsKey(key)) {
/* 127 */           addAttribute(key, attributes.get(key));
/*     */         }
/*     */       }
/*     */     }
/* 131 */     return this;
/*     */   }
/*     */   
/*     */   public Map<String, Object> asMap()
/*     */   {
/* 136 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object put(String key, Object value)
/*     */   {
/* 145 */     return super.put(key, formatValue(value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putAll(Map<? extends String, ? extends Object> map)
/*     */   {
/* 154 */     if (map != null) {
/* 155 */       for (String key : map.keySet()) {
/* 156 */         put(key, formatValue(map.get(key)));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public RedirectAttributes addFlashAttribute(String attributeName, Object attributeValue)
/*     */   {
/* 163 */     this.flashAttributes.addAttribute(attributeName, attributeValue);
/* 164 */     return this;
/*     */   }
/*     */   
/*     */   public RedirectAttributes addFlashAttribute(Object attributeValue)
/*     */   {
/* 169 */     this.flashAttributes.addAttribute(attributeValue);
/* 170 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\support\RedirectAttributesModelMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */