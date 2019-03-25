/*    */ package org.springframework.core.serializer;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.io.Serializable;
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
/*    */ public class DefaultSerializer
/*    */   implements Serializer<Object>
/*    */ {
/*    */   public void serialize(Object object, OutputStream outputStream)
/*    */     throws IOException
/*    */   {
/* 41 */     if (!(object instanceof Serializable))
/*    */     {
/* 43 */       throw new IllegalArgumentException(getClass().getSimpleName() + " requires a Serializable payload but received an object of type [" + object.getClass().getName() + "]");
/*    */     }
/* 45 */     ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
/* 46 */     objectOutputStream.writeObject(object);
/* 47 */     objectOutputStream.flush();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\serializer\DefaultSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */