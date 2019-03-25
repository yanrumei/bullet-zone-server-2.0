/*    */ package org.springframework.core.convert;
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
/*    */ public abstract class ConversionException
/*    */   extends NestedRuntimeException
/*    */ {
/*    */   public ConversionException(String message)
/*    */   {
/* 35 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ConversionException(String message, Throwable cause)
/*    */   {
/* 44 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\convert\ConversionException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */