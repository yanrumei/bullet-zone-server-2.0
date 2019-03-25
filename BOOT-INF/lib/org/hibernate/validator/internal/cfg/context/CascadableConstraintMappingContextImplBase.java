/*    */ package org.hibernate.validator.internal.cfg.context;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.hibernate.validator.cfg.context.Cascadable;
/*    */ import org.hibernate.validator.cfg.context.GroupConversionTargetContext;
/*    */ import org.hibernate.validator.cfg.context.Unwrapable;
/*    */ import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
/*    */ import org.hibernate.validator.internal.util.CollectionHelper;
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
/*    */ abstract class CascadableConstraintMappingContextImplBase<C extends Cascadable<C>,  extends Unwrapable<C>>
/*    */   extends ConstraintMappingContextImplBase
/*    */   implements Cascadable<C>, Unwrapable<C>
/*    */ {
/*    */   protected boolean isCascading;
/* 27 */   protected Map<Class<?>, Class<?>> groupConversions = CollectionHelper.newHashMap();
/* 28 */   private UnwrapMode unwrapMode = UnwrapMode.AUTOMATIC;
/*    */   
/*    */   CascadableConstraintMappingContextImplBase(DefaultConstraintMapping mapping) {
/* 31 */     super(mapping);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected abstract C getThis();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void addGroupConversion(Class<?> from, Class<?> to)
/*    */   {
/* 50 */     this.groupConversions.put(from, to);
/*    */   }
/*    */   
/*    */   public C valid()
/*    */   {
/* 55 */     this.isCascading = true;
/* 56 */     return getThis();
/*    */   }
/*    */   
/*    */   public GroupConversionTargetContext<C> convertGroup(Class<?> from)
/*    */   {
/* 61 */     return new GroupConversionTargetContextImpl(from, getThis(), this);
/*    */   }
/*    */   
/*    */   public boolean isCascading() {
/* 65 */     return this.isCascading;
/*    */   }
/*    */   
/*    */   public Map<Class<?>, Class<?>> getGroupConversions() {
/* 69 */     return this.groupConversions;
/*    */   }
/*    */   
/*    */   public C unwrapValidatedValue(boolean unwrap)
/*    */   {
/* 74 */     if (unwrap) {
/* 75 */       this.unwrapMode = UnwrapMode.UNWRAP;
/*    */     }
/*    */     else {
/* 78 */       this.unwrapMode = UnwrapMode.SKIP_UNWRAP;
/*    */     }
/* 80 */     return getThis();
/*    */   }
/*    */   
/*    */   UnwrapMode unwrapMode() {
/* 84 */     return this.unwrapMode;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\cfg\context\CascadableConstraintMappingContextImplBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */