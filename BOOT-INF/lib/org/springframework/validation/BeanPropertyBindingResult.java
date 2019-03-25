/*     */ package org.springframework.validation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.ConfigurablePropertyAccessor;
/*     */ import org.springframework.beans.PropertyAccessorFactory;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanPropertyBindingResult
/*     */   extends AbstractPropertyBindingResult
/*     */   implements Serializable
/*     */ {
/*     */   private final Object target;
/*     */   private final boolean autoGrowNestedPaths;
/*     */   private final int autoGrowCollectionLimit;
/*     */   private transient BeanWrapper beanWrapper;
/*     */   
/*     */   public BeanPropertyBindingResult(Object target, String objectName)
/*     */   {
/*  61 */     this(target, objectName, true, Integer.MAX_VALUE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanPropertyBindingResult(Object target, String objectName, boolean autoGrowNestedPaths, int autoGrowCollectionLimit)
/*     */   {
/*  72 */     super(objectName);
/*  73 */     this.target = target;
/*  74 */     this.autoGrowNestedPaths = autoGrowNestedPaths;
/*  75 */     this.autoGrowCollectionLimit = autoGrowCollectionLimit;
/*     */   }
/*     */   
/*     */ 
/*     */   public final Object getTarget()
/*     */   {
/*  81 */     return this.target;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ConfigurablePropertyAccessor getPropertyAccessor()
/*     */   {
/*  91 */     if (this.beanWrapper == null) {
/*  92 */       this.beanWrapper = createBeanWrapper();
/*  93 */       this.beanWrapper.setExtractOldValueForEditor(true);
/*  94 */       this.beanWrapper.setAutoGrowNestedPaths(this.autoGrowNestedPaths);
/*  95 */       this.beanWrapper.setAutoGrowCollectionLimit(this.autoGrowCollectionLimit);
/*     */     }
/*  97 */     return this.beanWrapper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanWrapper createBeanWrapper()
/*     */   {
/* 105 */     Assert.state(this.target != null, "Cannot access properties on null bean instance '" + getObjectName() + "'!");
/* 106 */     return PropertyAccessorFactory.forBeanPropertyAccess(this.target);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\validation\BeanPropertyBindingResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */