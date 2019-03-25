/*    */ package com.fasterxml.jackson.databind;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public abstract class InjectableValues
/*    */ {
/*    */   public abstract Object findInjectableValue(Object paramObject1, DeserializationContext paramDeserializationContext, BeanProperty paramBeanProperty, Object paramObject2);
/*    */   
/*    */   public static class Std
/*    */     extends InjectableValues
/*    */     implements Serializable
/*    */   {
/*    */     private static final long serialVersionUID = 1L;
/*    */     protected final Map<String, Object> _values;
/*    */     
/*    */     public Std()
/*    */     {
/* 47 */       this(new HashMap());
/*    */     }
/*    */     
/*    */     public Std(Map<String, Object> values) {
/* 51 */       this._values = values;
/*    */     }
/*    */     
/*    */     public Std addValue(String key, Object value) {
/* 55 */       this._values.put(key, value);
/* 56 */       return this;
/*    */     }
/*    */     
/*    */     public Std addValue(Class<?> classKey, Object value) {
/* 60 */       this._values.put(classKey.getName(), value);
/* 61 */       return this;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */     public Object findInjectableValue(Object valueId, DeserializationContext ctxt, BeanProperty forProperty, Object beanInstance)
/*    */     {
/* 68 */       if (!(valueId instanceof String)) {
/* 69 */         String type = valueId == null ? "[null]" : valueId.getClass().getName();
/* 70 */         throw new IllegalArgumentException("Unrecognized inject value id type (" + type + "), expecting String");
/*    */       }
/* 72 */       String key = (String)valueId;
/* 73 */       Object ob = this._values.get(key);
/* 74 */       if ((ob == null) && (!this._values.containsKey(key))) {
/* 75 */         throw new IllegalArgumentException("No injectable id with value '" + key + "' found (for property '" + forProperty.getName() + "')");
/*    */       }
/* 77 */       return ob;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\InjectableValues.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */