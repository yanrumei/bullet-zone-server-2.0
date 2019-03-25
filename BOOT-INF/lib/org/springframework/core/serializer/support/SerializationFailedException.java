/*    */ package org.springframework.core.serializer.support;
/*    */ 
/*    */ import org.springframework.core.NestedRuntimeException;
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
/*    */ public class SerializationFailedException
/*    */   extends NestedRuntimeException
/*    */ {
/*    */   public SerializationFailedException(String message)
/*    */   {
/* 39 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SerializationFailedException(String message, Throwable cause)
/*    */   {
/* 49 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\serializer\support\SerializationFailedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */