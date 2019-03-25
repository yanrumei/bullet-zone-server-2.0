/*    */ package org.springframework.validation;
/*    */ 
/*    */ import org.springframework.beans.ConfigurablePropertyAccessor;
/*    */ import org.springframework.beans.PropertyAccessorFactory;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DirectFieldBindingResult
/*    */   extends AbstractPropertyBindingResult
/*    */ {
/*    */   private final Object target;
/*    */   private final boolean autoGrowNestedPaths;
/*    */   private transient ConfigurablePropertyAccessor directFieldAccessor;
/*    */   
/*    */   public DirectFieldBindingResult(Object target, String objectName)
/*    */   {
/* 52 */     this(target, objectName, true);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DirectFieldBindingResult(Object target, String objectName, boolean autoGrowNestedPaths)
/*    */   {
/* 62 */     super(objectName);
/* 63 */     this.target = target;
/* 64 */     this.autoGrowNestedPaths = autoGrowNestedPaths;
/*    */   }
/*    */   
/*    */ 
/*    */   public final Object getTarget()
/*    */   {
/* 70 */     return this.target;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public final ConfigurablePropertyAccessor getPropertyAccessor()
/*    */   {
/* 80 */     if (this.directFieldAccessor == null) {
/* 81 */       this.directFieldAccessor = createDirectFieldAccessor();
/* 82 */       this.directFieldAccessor.setExtractOldValueForEditor(true);
/* 83 */       this.directFieldAccessor.setAutoGrowNestedPaths(this.autoGrowNestedPaths);
/*    */     }
/* 85 */     return this.directFieldAccessor;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ConfigurablePropertyAccessor createDirectFieldAccessor()
/*    */   {
/* 93 */     Assert.state(this.target != null, "Cannot access fields on null target instance '" + getObjectName() + "'!");
/* 94 */     return PropertyAccessorFactory.forDirectFieldAccess(this.target);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\validation\DirectFieldBindingResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */