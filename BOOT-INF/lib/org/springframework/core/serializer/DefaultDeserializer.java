/*    */ package org.springframework.core.serializer;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.ObjectInputStream;
/*    */ import org.springframework.core.ConfigurableObjectInputStream;
/*    */ import org.springframework.core.NestedIOException;
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
/*    */ public class DefaultDeserializer
/*    */   implements Deserializer<Object>
/*    */ {
/*    */   private final ClassLoader classLoader;
/*    */   
/*    */   public DefaultDeserializer()
/*    */   {
/* 46 */     this.classLoader = null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DefaultDeserializer(ClassLoader classLoader)
/*    */   {
/* 56 */     this.classLoader = classLoader;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object deserialize(InputStream inputStream)
/*    */     throws IOException
/*    */   {
/* 68 */     ObjectInputStream objectInputStream = new ConfigurableObjectInputStream(inputStream, this.classLoader);
/*    */     try {
/* 70 */       return objectInputStream.readObject();
/*    */     }
/*    */     catch (ClassNotFoundException ex) {
/* 73 */       throw new NestedIOException("Failed to deserialize object type", ex);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\serializer\DefaultDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */