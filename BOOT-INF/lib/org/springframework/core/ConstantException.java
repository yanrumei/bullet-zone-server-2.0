/*    */ package org.springframework.core;
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
/*    */ public class ConstantException
/*    */   extends IllegalArgumentException
/*    */ {
/*    */   public ConstantException(String className, String field, String message)
/*    */   {
/* 37 */     super("Field '" + field + "' " + message + " in class [" + className + "]");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ConstantException(String className, String namePrefix, Object value)
/*    */   {
/* 47 */     super("No '" + namePrefix + "' field with value '" + value + "' found in class [" + className + "]");
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\ConstantException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */