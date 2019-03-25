/*    */ package org.springframework.beans.factory.support;
/*    */ 
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
/*    */ public class ManagedArray
/*    */   extends ManagedList<Object>
/*    */ {
/*    */   volatile Class<?> resolvedElementType;
/*    */   
/*    */   public ManagedArray(String elementTypeName, int size)
/*    */   {
/* 41 */     super(size);
/* 42 */     Assert.notNull(elementTypeName, "elementTypeName must not be null");
/* 43 */     setElementTypeName(elementTypeName);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\support\ManagedArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */