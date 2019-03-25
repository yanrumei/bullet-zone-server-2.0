/*    */ package org.hibernate.validator.internal.engine.valuehandling;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import org.hibernate.validator.internal.util.IgnoreJava6Requirement;
/*    */ import org.hibernate.validator.internal.util.TypeResolutionHelper;
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
/*    */ @IgnoreJava6Requirement
/*    */ public class OptionalValueUnwrapper
/*    */   extends TypeResolverBasedValueUnwrapper<Optional<?>>
/*    */ {
/*    */   public OptionalValueUnwrapper(TypeResolutionHelper typeResolutionHelper)
/*    */   {
/* 24 */     super(typeResolutionHelper);
/*    */   }
/*    */   
/*    */   public Object handleValidatedValue(Optional<?> value)
/*    */   {
/* 29 */     if (value == null) {
/* 30 */       return null;
/*    */     }
/*    */     
/* 33 */     if (value.isPresent()) {
/* 34 */       return value.get();
/*    */     }
/*    */     
/* 37 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\valuehandling\OptionalValueUnwrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */