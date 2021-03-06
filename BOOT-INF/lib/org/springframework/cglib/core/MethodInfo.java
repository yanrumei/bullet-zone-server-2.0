/*    */ package org.springframework.cglib.core;
/*    */ 
/*    */ import org.springframework.asm.Type;
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
/*    */ public abstract class MethodInfo
/*    */ {
/*    */   public abstract ClassInfo getClassInfo();
/*    */   
/*    */   public abstract int getModifiers();
/*    */   
/*    */   public abstract Signature getSignature();
/*    */   
/*    */   public abstract Type[] getExceptionTypes();
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 32 */     if (o == null)
/* 33 */       return false;
/* 34 */     if (!(o instanceof MethodInfo))
/* 35 */       return false;
/* 36 */     return getSignature().equals(((MethodInfo)o).getSignature());
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 40 */     return getSignature().hashCode();
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 45 */     return getSignature().toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\core\MethodInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */