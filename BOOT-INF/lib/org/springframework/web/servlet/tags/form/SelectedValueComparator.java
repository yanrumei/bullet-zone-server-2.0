/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ abstract class SelectedValueComparator
/*     */ {
/*     */   public static boolean isSelected(BindStatus bindStatus, Object candidateValue)
/*     */   {
/*  63 */     if (bindStatus == null) {
/*  64 */       return candidateValue == null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  69 */     Object boundValue = bindStatus.getValue();
/*  70 */     if (ObjectUtils.nullSafeEquals(boundValue, candidateValue)) {
/*  71 */       return true;
/*     */     }
/*  73 */     Object actualValue = bindStatus.getActualValue();
/*  74 */     if ((actualValue != null) && (actualValue != boundValue) && 
/*  75 */       (ObjectUtils.nullSafeEquals(actualValue, candidateValue))) {
/*  76 */       return true;
/*     */     }
/*  78 */     if (actualValue != null) {
/*  79 */       boundValue = actualValue;
/*     */     }
/*  81 */     else if (boundValue == null) {
/*  82 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  87 */     boolean selected = false;
/*  88 */     if (boundValue.getClass().isArray()) {
/*  89 */       selected = collectionCompare(CollectionUtils.arrayToList(boundValue), candidateValue, bindStatus);
/*     */     }
/*  91 */     else if ((boundValue instanceof Collection)) {
/*  92 */       selected = collectionCompare((Collection)boundValue, candidateValue, bindStatus);
/*     */     }
/*  94 */     else if ((boundValue instanceof Map)) {
/*  95 */       selected = mapCompare((Map)boundValue, candidateValue, bindStatus);
/*     */     }
/*  97 */     if (!selected) {
/*  98 */       selected = exhaustiveCompare(boundValue, candidateValue, bindStatus.getEditor(), null);
/*     */     }
/* 100 */     return selected;
/*     */   }
/*     */   
/*     */   private static boolean collectionCompare(Collection<?> boundCollection, Object candidateValue, BindStatus bindStatus) {
/*     */     try {
/* 105 */       if (boundCollection.contains(candidateValue)) {
/* 106 */         return true;
/*     */       }
/*     */     }
/*     */     catch (ClassCastException localClassCastException) {}
/*     */     
/*     */ 
/* 112 */     return exhaustiveCollectionCompare(boundCollection, candidateValue, bindStatus);
/*     */   }
/*     */   
/*     */   private static boolean mapCompare(Map<?, ?> boundMap, Object candidateValue, BindStatus bindStatus) {
/*     */     try {
/* 117 */       if (boundMap.containsKey(candidateValue)) {
/* 118 */         return true;
/*     */       }
/*     */     }
/*     */     catch (ClassCastException localClassCastException) {}
/*     */     
/*     */ 
/* 124 */     return exhaustiveCollectionCompare(boundMap.keySet(), candidateValue, bindStatus);
/*     */   }
/*     */   
/*     */ 
/*     */   private static boolean exhaustiveCollectionCompare(Collection<?> collection, Object candidateValue, BindStatus bindStatus)
/*     */   {
/* 130 */     Map<PropertyEditor, Object> convertedValueCache = new HashMap(1);
/* 131 */     PropertyEditor editor = null;
/* 132 */     boolean candidateIsString = candidateValue instanceof String;
/* 133 */     if (!candidateIsString) {
/* 134 */       editor = bindStatus.findEditor(candidateValue.getClass());
/*     */     }
/* 136 */     for (Object element : collection) {
/* 137 */       if ((editor == null) && (element != null) && (candidateIsString)) {
/* 138 */         editor = bindStatus.findEditor(element.getClass());
/*     */       }
/* 140 */       if (exhaustiveCompare(element, candidateValue, editor, convertedValueCache)) {
/* 141 */         return true;
/*     */       }
/*     */     }
/* 144 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   private static boolean exhaustiveCompare(Object boundValue, Object candidate, PropertyEditor editor, Map<PropertyEditor, Object> convertedValueCache)
/*     */   {
/* 150 */     String candidateDisplayString = ValueFormatter.getDisplayString(candidate, editor, false);
/* 151 */     if ((boundValue != null) && (boundValue.getClass().isEnum())) {
/* 152 */       Enum<?> boundEnum = (Enum)boundValue;
/* 153 */       String enumCodeAsString = ObjectUtils.getDisplayString(boundEnum.name());
/* 154 */       if (enumCodeAsString.equals(candidateDisplayString)) {
/* 155 */         return true;
/*     */       }
/* 157 */       String enumLabelAsString = ObjectUtils.getDisplayString(boundEnum.toString());
/* 158 */       if (enumLabelAsString.equals(candidateDisplayString)) {
/* 159 */         return true;
/*     */       }
/*     */     }
/* 162 */     else if (ObjectUtils.getDisplayString(boundValue).equals(candidateDisplayString)) {
/* 163 */       return true;
/*     */     }
/*     */     
/* 166 */     if ((editor != null) && ((candidate instanceof String)))
/*     */     {
/* 168 */       String candidateAsString = (String)candidate;
/*     */       Object candidateAsValue;
/* 170 */       Object candidateAsValue; if ((convertedValueCache != null) && (convertedValueCache.containsKey(editor))) {
/* 171 */         candidateAsValue = convertedValueCache.get(editor);
/*     */       }
/*     */       else {
/* 174 */         editor.setAsText(candidateAsString);
/* 175 */         candidateAsValue = editor.getValue();
/* 176 */         if (convertedValueCache != null) {
/* 177 */           convertedValueCache.put(editor, candidateAsValue);
/*     */         }
/*     */       }
/* 180 */       if (ObjectUtils.nullSafeEquals(boundValue, candidateAsValue)) {
/* 181 */         return true;
/*     */       }
/*     */     }
/* 184 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\form\SelectedValueComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */