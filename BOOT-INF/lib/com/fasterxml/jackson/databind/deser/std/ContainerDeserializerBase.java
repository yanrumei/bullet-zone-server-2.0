/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ContainerDeserializerBase<T>
/*    */   extends StdDeserializer<T>
/*    */ {
/*    */   protected ContainerDeserializerBase(JavaType selfType)
/*    */   {
/* 20 */     super(selfType);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SettableBeanProperty findBackReference(String refName)
/*    */   {
/* 31 */     JsonDeserializer<Object> valueDeser = getContentDeserializer();
/* 32 */     if (valueDeser == null) {
/* 33 */       throw new IllegalArgumentException("Can not handle managed/back reference '" + refName + "': type: container deserializer of type " + getClass().getName() + " returned null for 'getContentDeserializer()'");
/*    */     }
/*    */     
/* 36 */     return valueDeser.findBackReference(refName);
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
/*    */   public abstract JavaType getContentType();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract JsonDeserializer<Object> getContentDeserializer();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void wrapAndThrow(Throwable t, Object ref, String key)
/*    */     throws IOException
/*    */   {
/* 68 */     while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 69 */       t = t.getCause();
/*    */     }
/*    */     
/* 72 */     if ((t instanceof Error)) {
/* 73 */       throw ((Error)t);
/*    */     }
/*    */     
/* 76 */     if (((t instanceof IOException)) && (!(t instanceof JsonMappingException))) {
/* 77 */       throw ((IOException)t);
/*    */     }
/*    */     
/* 80 */     if (key == null) {
/* 81 */       key = "N/A";
/*    */     }
/* 83 */     throw JsonMappingException.wrapWithPath(t, ref, key);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\ContainerDeserializerBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */