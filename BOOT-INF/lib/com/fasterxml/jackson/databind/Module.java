/*    */ package com.fasterxml.jackson.databind;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonFactory.Feature;
/*    */ import com.fasterxml.jackson.core.JsonGenerator.Feature;
/*    */ import com.fasterxml.jackson.core.JsonParser.Feature;
/*    */ import com.fasterxml.jackson.core.ObjectCodec;
/*    */ import com.fasterxml.jackson.core.Version;
/*    */ import com.fasterxml.jackson.core.Versioned;
/*    */ import com.fasterxml.jackson.databind.cfg.MutableConfigOverride;
/*    */ import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
/*    */ import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
/*    */ import com.fasterxml.jackson.databind.deser.Deserializers;
/*    */ import com.fasterxml.jackson.databind.deser.KeyDeserializers;
/*    */ import com.fasterxml.jackson.databind.deser.ValueInstantiators;
/*    */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
/*    */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*    */ import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
/*    */ import com.fasterxml.jackson.databind.ser.Serializers;
/*    */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*    */ import com.fasterxml.jackson.databind.type.TypeModifier;
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
/*    */ public abstract class Module
/*    */   implements Versioned
/*    */ {
/*    */   public abstract String getModuleName();
/*    */   
/*    */   public abstract Version version();
/*    */   
/*    */   public Object getTypeId()
/*    */   {
/* 59 */     return getClass().getName();
/*    */   }
/*    */   
/*    */   public abstract void setupModule(SetupContext paramSetupContext);
/*    */   
/*    */   public static abstract interface SetupContext
/*    */   {
/*    */     public abstract Version getMapperVersion();
/*    */     
/*    */     public abstract <C extends ObjectCodec> C getOwner();
/*    */     
/*    */     public abstract TypeFactory getTypeFactory();
/*    */     
/*    */     public abstract boolean isEnabled(MapperFeature paramMapperFeature);
/*    */     
/*    */     public abstract boolean isEnabled(DeserializationFeature paramDeserializationFeature);
/*    */     
/*    */     public abstract boolean isEnabled(SerializationFeature paramSerializationFeature);
/*    */     
/*    */     public abstract boolean isEnabled(JsonFactory.Feature paramFeature);
/*    */     
/*    */     public abstract boolean isEnabled(JsonParser.Feature paramFeature);
/*    */     
/*    */     public abstract boolean isEnabled(JsonGenerator.Feature paramFeature);
/*    */     
/*    */     public abstract MutableConfigOverride configOverride(Class<?> paramClass);
/*    */     
/*    */     public abstract void addDeserializers(Deserializers paramDeserializers);
/*    */     
/*    */     public abstract void addKeyDeserializers(KeyDeserializers paramKeyDeserializers);
/*    */     
/*    */     public abstract void addSerializers(Serializers paramSerializers);
/*    */     
/*    */     public abstract void addKeySerializers(Serializers paramSerializers);
/*    */     
/*    */     public abstract void addBeanDeserializerModifier(BeanDeserializerModifier paramBeanDeserializerModifier);
/*    */     
/*    */     public abstract void addBeanSerializerModifier(BeanSerializerModifier paramBeanSerializerModifier);
/*    */     
/*    */     public abstract void addAbstractTypeResolver(AbstractTypeResolver paramAbstractTypeResolver);
/*    */     
/*    */     public abstract void addTypeModifier(TypeModifier paramTypeModifier);
/*    */     
/*    */     public abstract void addValueInstantiators(ValueInstantiators paramValueInstantiators);
/*    */     
/*    */     public abstract void setClassIntrospector(ClassIntrospector paramClassIntrospector);
/*    */     
/*    */     public abstract void insertAnnotationIntrospector(AnnotationIntrospector paramAnnotationIntrospector);
/*    */     
/*    */     public abstract void appendAnnotationIntrospector(AnnotationIntrospector paramAnnotationIntrospector);
/*    */     
/*    */     public abstract void registerSubtypes(Class<?>... paramVarArgs);
/*    */     
/*    */     public abstract void registerSubtypes(NamedType... paramVarArgs);
/*    */     
/*    */     public abstract void setMixInAnnotations(Class<?> paramClass1, Class<?> paramClass2);
/*    */     
/*    */     public abstract void addDeserializationProblemHandler(DeserializationProblemHandler paramDeserializationProblemHandler);
/*    */     
/*    */     public abstract void setNamingStrategy(PropertyNamingStrategy paramPropertyNamingStrategy);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\Module.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */