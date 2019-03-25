/*    */ package org.springframework.core.serializer.support;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import org.springframework.core.convert.converter.Converter;
/*    */ import org.springframework.core.serializer.DefaultSerializer;
/*    */ import org.springframework.core.serializer.Serializer;
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
/*    */ public class SerializingConverter
/*    */   implements Converter<Object, byte[]>
/*    */ {
/*    */   private final Serializer<Object> serializer;
/*    */   
/*    */   public SerializingConverter()
/*    */   {
/* 44 */     this.serializer = new DefaultSerializer();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public SerializingConverter(Serializer<Object> serializer)
/*    */   {
/* 51 */     Assert.notNull(serializer, "Serializer must not be null");
/* 52 */     this.serializer = serializer;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public byte[] convert(Object source)
/*    */   {
/* 61 */     ByteArrayOutputStream byteStream = new ByteArrayOutputStream(1024);
/*    */     try {
/* 63 */       this.serializer.serialize(source, byteStream);
/* 64 */       return byteStream.toByteArray();
/*    */     }
/*    */     catch (Throwable ex)
/*    */     {
/* 68 */       throw new SerializationFailedException("Failed to serialize object using " + this.serializer.getClass().getSimpleName(), ex);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\serializer\support\SerializingConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */