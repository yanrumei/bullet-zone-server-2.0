/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
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
/*    */ public abstract class NonTypedScalarSerializerBase<T>
/*    */   extends StdScalarSerializer<T>
/*    */ {
/*    */   protected NonTypedScalarSerializerBase(Class<T> t)
/*    */   {
/* 21 */     super(t);
/*    */   }
/*    */   
/*    */   protected NonTypedScalarSerializerBase(Class<?> t, boolean bogus) {
/* 25 */     super(t, bogus);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public final void serializeWithType(T value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
/*    */     throws IOException
/*    */   {
/* 33 */     serialize(value, gen, provider);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\NonTypedScalarSerializerBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */