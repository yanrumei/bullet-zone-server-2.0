/*    */ package org.apache.log4j.helpers;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ import java.util.NoSuchElementException;
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
/*    */ public class NullEnumeration
/*    */   implements Enumeration
/*    */ {
/* 31 */   private static final NullEnumeration instance = new NullEnumeration();
/*    */   
/*    */ 
/*    */ 
/*    */   public static NullEnumeration getInstance()
/*    */   {
/* 37 */     return instance;
/*    */   }
/*    */   
/*    */   public boolean hasMoreElements() {
/* 41 */     return false;
/*    */   }
/*    */   
/*    */   public Object nextElement() {
/* 45 */     throw new NoSuchElementException();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\log4j-over-slf4j-1.7.25.jar!\org\apache\log4j\helpers\NullEnumeration.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */