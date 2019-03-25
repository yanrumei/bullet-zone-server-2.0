/*    */ package com.fasterxml.jackson.databind.deser;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.BeanDescription;
/*    */ import com.fasterxml.jackson.databind.DeserializationConfig;
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
/*    */ public abstract interface ValueInstantiators
/*    */ {
/*    */   public abstract ValueInstantiator findValueInstantiator(DeserializationConfig paramDeserializationConfig, BeanDescription paramBeanDescription, ValueInstantiator paramValueInstantiator);
/*    */   
/*    */   public static class Base
/*    */     implements ValueInstantiators
/*    */   {
/*    */     public ValueInstantiator findValueInstantiator(DeserializationConfig config, BeanDescription beanDesc, ValueInstantiator defaultInstantiator)
/*    */     {
/* 44 */       return defaultInstantiator;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\ValueInstantiators.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */