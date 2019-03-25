/*    */ package org.springframework.core.serializer.support;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import org.springframework.core.convert.converter.Converter;
/*    */ import org.springframework.core.serializer.DefaultDeserializer;
/*    */ import org.springframework.core.serializer.Deserializer;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ 
/*    */ public class DeserializingConverter
/*    */   implements Converter<byte[], Object>
/*    */ {
/*    */   private final Deserializer<Object> deserializer;
/*    */   
/*    */   public DeserializingConverter()
/*    */   {
/* 47 */     this.deserializer = new DefaultDeserializer();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DeserializingConverter(ClassLoader classLoader)
/*    */   {
/* 57 */     this.deserializer = new DefaultDeserializer(classLoader);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public DeserializingConverter(Deserializer<Object> deserializer)
/*    */   {
/* 64 */     Assert.notNull(deserializer, "Deserializer must not be null");
/* 65 */     this.deserializer = deserializer;
/*    */   }
/*    */   
/*    */ 
/*    */   public Object convert(byte[] source)
/*    */   {
/* 71 */     ByteArrayInputStream byteStream = new ByteArrayInputStream(source);
/*    */     try {
/* 73 */       return this.deserializer.deserialize(byteStream);
/*    */ 
/*    */     }
/*    */     catch (Throwable ex)
/*    */     {
/* 78 */       throw new SerializationFailedException("Failed to deserialize payload. Is the byte array a result of corresponding serialization for " + this.deserializer.getClass().getSimpleName() + "?", ex);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\serializer\support\DeserializingConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */