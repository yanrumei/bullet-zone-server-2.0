/*    */ package com.fasterxml.jackson.databind.deser.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.BeanProperty.Std;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*    */ import com.fasterxml.jackson.databind.PropertyName;
/*    */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*    */ import com.fasterxml.jackson.databind.util.Annotations;
/*    */ import java.io.IOException;
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
/*    */ public class ValueInjector
/*    */   extends BeanProperty.Std
/*    */ {
/*    */   protected final Object _valueId;
/*    */   
/*    */   public ValueInjector(PropertyName propName, JavaType type, Annotations contextAnnotations, AnnotatedMember mutator, Object valueId)
/*    */   {
/* 31 */     super(propName, type, null, contextAnnotations, mutator, PropertyMetadata.STD_OPTIONAL);
/*    */     
/* 33 */     this._valueId = valueId;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public ValueInjector(String propName, JavaType type, Annotations contextAnnotations, AnnotatedMember mutator, Object valueId)
/*    */   {
/* 41 */     this(new PropertyName(propName), type, contextAnnotations, mutator, valueId);
/*    */   }
/*    */   
/*    */   public Object findValue(DeserializationContext context, Object beanInstance)
/*    */   {
/* 46 */     return context.findInjectableValue(this._valueId, this, beanInstance);
/*    */   }
/*    */   
/*    */   public void inject(DeserializationContext context, Object beanInstance)
/*    */     throws IOException
/*    */   {
/* 52 */     this._member.setValue(beanInstance, findValue(context, beanInstance));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\impl\ValueInjector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */