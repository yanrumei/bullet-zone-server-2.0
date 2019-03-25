/*    */ package org.springframework.core.serializer.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import org.springframework.core.serializer.DefaultDeserializer;
/*    */ import org.springframework.core.serializer.DefaultSerializer;
/*    */ import org.springframework.core.serializer.Deserializer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SerializationDelegate
/*    */   implements Serializer<Object>, Deserializer<Object>
/*    */ {
/*    */   private final Serializer<Object> serializer;
/*    */   private final Deserializer<Object> deserializer;
/*    */   
/*    */   public SerializationDelegate(ClassLoader classLoader)
/*    */   {
/* 51 */     this.serializer = new DefaultSerializer();
/* 52 */     this.deserializer = new DefaultDeserializer(classLoader);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SerializationDelegate(Serializer<Object> serializer, Deserializer<Object> deserializer)
/*    */   {
/* 61 */     Assert.notNull(serializer, "Serializer must not be null");
/* 62 */     Assert.notNull(deserializer, "Deserializer must not be null");
/* 63 */     this.serializer = serializer;
/* 64 */     this.deserializer = deserializer;
/*    */   }
/*    */   
/*    */   public void serialize(Object object, OutputStream outputStream)
/*    */     throws IOException
/*    */   {
/* 70 */     this.serializer.serialize(object, outputStream);
/*    */   }
/*    */   
/*    */   public Object deserialize(InputStream inputStream) throws IOException
/*    */   {
/* 75 */     return this.deserializer.deserialize(inputStream);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\serializer\support\SerializationDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */