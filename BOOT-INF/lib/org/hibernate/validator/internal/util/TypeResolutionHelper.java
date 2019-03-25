/*    */ package org.hibernate.validator.internal.util;
/*    */ 
/*    */ import com.fasterxml.classmate.TypeResolver;
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
/*    */ public class TypeResolutionHelper
/*    */ {
/*    */   private final TypeResolver typeResolver;
/*    */   
/*    */   public TypeResolutionHelper()
/*    */   {
/* 21 */     this.typeResolver = new TypeResolver();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public TypeResolver getTypeResolver()
/*    */   {
/* 28 */     return this.typeResolver;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\TypeResolutionHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */