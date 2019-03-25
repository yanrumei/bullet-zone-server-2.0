/*    */ package org.hibernate.validator.internal.util.annotationfactory;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
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
/*    */ public class AnnotationDescriptor<T extends Annotation>
/*    */ {
/*    */   private final Class<T> type;
/* 30 */   private final Map<String, Object> elements = new HashMap();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static <S extends Annotation> AnnotationDescriptor<S> getInstance(Class<S> annotationType)
/*    */   {
/* 41 */     return new AnnotationDescriptor(annotationType);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static <S extends Annotation> AnnotationDescriptor<S> getInstance(Class<S> annotationType, Map<String, Object> elements)
/*    */   {
/* 54 */     return new AnnotationDescriptor(annotationType, elements);
/*    */   }
/*    */   
/*    */   public AnnotationDescriptor(Class<T> annotationType) {
/* 58 */     this.type = annotationType;
/*    */   }
/*    */   
/*    */   public AnnotationDescriptor(Class<T> annotationType, Map<String, Object> elements) {
/* 62 */     this.type = annotationType;
/* 63 */     for (Map.Entry<String, Object> entry : elements.entrySet()) {
/* 64 */       this.elements.put(entry.getKey(), entry.getValue());
/*    */     }
/*    */   }
/*    */   
/*    */   public void setValue(String elementName, Object value) {
/* 69 */     this.elements.put(elementName, value);
/*    */   }
/*    */   
/*    */   public Object valueOf(String elementName) {
/* 73 */     return this.elements.get(elementName);
/*    */   }
/*    */   
/*    */   public boolean containsElement(String elementName) {
/* 77 */     return this.elements.containsKey(elementName);
/*    */   }
/*    */   
/*    */   public int numberOfElements() {
/* 81 */     return this.elements.size();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Map<String, Object> getElements()
/*    */   {
/* 91 */     return new HashMap(this.elements);
/*    */   }
/*    */   
/*    */   public Class<T> type() {
/* 95 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\annotationfactory\AnnotationDescriptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */