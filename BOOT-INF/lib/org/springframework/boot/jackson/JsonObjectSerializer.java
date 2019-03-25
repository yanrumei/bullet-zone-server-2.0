/*    */ package org.springframework.boot.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.JsonSerializer;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
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
/*    */ public abstract class JsonObjectSerializer<T>
/*    */   extends JsonSerializer<T>
/*    */ {
/*    */   public final void serialize(T value, JsonGenerator jgen, SerializerProvider provider)
/*    */     throws IOException
/*    */   {
/*    */     try
/*    */     {
/* 40 */       jgen.writeStartObject();
/* 41 */       serializeObject(value, jgen, provider);
/* 42 */       jgen.writeEndObject();
/*    */     }
/*    */     catch (Exception ex) {
/* 45 */       if ((ex instanceof IOException)) {
/* 46 */         throw ((IOException)ex);
/*    */       }
/* 48 */       throw new JsonMappingException(jgen, "Object serialize error", ex);
/*    */     }
/*    */   }
/*    */   
/*    */   protected abstract void serializeObject(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
/*    */     throws IOException;
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jackson\JsonObjectSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */