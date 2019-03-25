/*    */ package org.hibernate.validator.internal.cfg.context;
/*    */ 
/*    */ import org.hibernate.validator.cfg.context.GroupConversionTargetContext;
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
/*    */ class GroupConversionTargetContextImpl<C>
/*    */   implements GroupConversionTargetContext<C>
/*    */ {
/*    */   private final C cascadableContext;
/*    */   private final Class<?> from;
/*    */   private final CascadableConstraintMappingContextImplBase<?> target;
/*    */   
/*    */   GroupConversionTargetContextImpl(Class<?> from, C cascadableContext, CascadableConstraintMappingContextImplBase<?> target)
/*    */   {
/* 23 */     this.from = from;
/* 24 */     this.cascadableContext = cascadableContext;
/* 25 */     this.target = target;
/*    */   }
/*    */   
/*    */   public C to(Class<?> to)
/*    */   {
/* 30 */     this.target.addGroupConversion(this.from, to);
/* 31 */     return (C)this.cascadableContext;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\cfg\context\GroupConversionTargetContextImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */