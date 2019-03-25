/*     */ package org.hibernate.validator.messageinterpolation;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Locale;
/*     */ import javax.el.ExpressionFactory;
/*     */ import javax.validation.MessageInterpolator.Context;
/*     */ import org.hibernate.validator.internal.engine.messageinterpolation.InterpolationTerm;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.SetContextClassLoader;
/*     */ import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;
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
/*     */ public class ResourceBundleMessageInterpolator
/*     */   extends AbstractMessageInterpolator
/*     */ {
/*  34 */   private static final Log LOG = ;
/*     */   
/*     */   private final ExpressionFactory expressionFactory;
/*     */   
/*     */   public ResourceBundleMessageInterpolator()
/*     */   {
/*  40 */     this.expressionFactory = buildExpressionFactory();
/*     */   }
/*     */   
/*     */   public ResourceBundleMessageInterpolator(ResourceBundleLocator userResourceBundleLocator) {
/*  44 */     super(userResourceBundleLocator);
/*  45 */     this.expressionFactory = buildExpressionFactory();
/*     */   }
/*     */   
/*     */   public ResourceBundleMessageInterpolator(ResourceBundleLocator userResourceBundleLocator, ResourceBundleLocator contributorResourceBundleLocator)
/*     */   {
/*  50 */     super(userResourceBundleLocator, contributorResourceBundleLocator);
/*  51 */     this.expressionFactory = buildExpressionFactory();
/*     */   }
/*     */   
/*     */ 
/*     */   public ResourceBundleMessageInterpolator(ResourceBundleLocator userResourceBundleLocator, ResourceBundleLocator contributorResourceBundleLocator, boolean cachingEnabled)
/*     */   {
/*  57 */     super(userResourceBundleLocator, contributorResourceBundleLocator, cachingEnabled);
/*  58 */     this.expressionFactory = buildExpressionFactory();
/*     */   }
/*     */   
/*     */   public ResourceBundleMessageInterpolator(ResourceBundleLocator userResourceBundleLocator, boolean cachingEnabled) {
/*  62 */     super(userResourceBundleLocator, null, cachingEnabled);
/*  63 */     this.expressionFactory = buildExpressionFactory();
/*     */   }
/*     */   
/*     */   public ResourceBundleMessageInterpolator(ResourceBundleLocator userResourceBundleLocator, boolean cachingEnabled, ExpressionFactory expressionFactory) {
/*  67 */     super(userResourceBundleLocator, null, cachingEnabled);
/*  68 */     this.expressionFactory = expressionFactory;
/*     */   }
/*     */   
/*     */   public String interpolate(MessageInterpolator.Context context, Locale locale, String term)
/*     */   {
/*  73 */     InterpolationTerm expression = new InterpolationTerm(term, locale, this.expressionFactory);
/*  74 */     return expression.interpolate(context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static ExpressionFactory buildExpressionFactory()
/*     */   {
/*     */     try
/*     */     {
/*  86 */       return ExpressionFactory.newInstance();
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (Throwable localThrowable1)
/*     */     {
/*     */ 
/*     */ 
/*  94 */       ClassLoader originalContextClassLoader = (ClassLoader)run(GetClassLoader.fromContext());
/*     */       try
/*     */       {
/*  97 */         run(SetContextClassLoader.action(ResourceBundleMessageInterpolator.class.getClassLoader()));
/*  98 */         return ExpressionFactory.newInstance();
/*     */       }
/*     */       catch (Throwable e)
/*     */       {
/* 102 */         throw LOG.getUnableToInitializeELExpressionFactoryException(e);
/*     */       }
/*     */       finally {
/* 105 */         run(SetContextClassLoader.action(originalContextClassLoader));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static <T> T run(PrivilegedAction<T> action)
/*     */   {
/* 116 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\messageinterpolation\ResourceBundleMessageInterpolator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */