/*    */ package org.springframework.util;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
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
/*    */ public abstract class SerializationUtils
/*    */ {
/*    */   public static byte[] serialize(Object object)
/*    */   {
/* 39 */     if (object == null) {
/* 40 */       return null;
/*    */     }
/* 42 */     ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
/*    */     try {
/* 44 */       ObjectOutputStream oos = new ObjectOutputStream(baos);
/* 45 */       oos.writeObject(object);
/* 46 */       oos.flush();
/*    */     }
/*    */     catch (IOException ex) {
/* 49 */       throw new IllegalArgumentException("Failed to serialize object of type: " + object.getClass(), ex);
/*    */     }
/* 51 */     return baos.toByteArray();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Object deserialize(byte[] bytes)
/*    */   {
/* 60 */     if (bytes == null) {
/* 61 */       return null;
/*    */     }
/*    */     try {
/* 64 */       ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
/* 65 */       return ois.readObject();
/*    */     }
/*    */     catch (IOException ex) {
/* 68 */       throw new IllegalArgumentException("Failed to deserialize object", ex);
/*    */     }
/*    */     catch (ClassNotFoundException ex) {
/* 71 */       throw new IllegalStateException("Failed to deserialize object type", ex);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\SerializationUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */