/*    */ package org.hibernate.validator.internal.engine.resolver;
/*    */ 
/*    */ import java.lang.annotation.ElementType;
/*    */ import javax.persistence.Persistence;
/*    */ import javax.persistence.PersistenceUtil;
/*    */ import javax.validation.Path;
/*    */ import javax.validation.Path.Node;
/*    */ import javax.validation.TraversableResolver;
/*    */ import org.hibernate.validator.internal.util.logging.Log;
/*    */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
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
/*    */ public class JPATraversableResolver
/*    */   implements TraversableResolver
/*    */ {
/* 27 */   private static final Log log = ;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public final boolean isReachable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType)
/*    */   {
/* 35 */     if (log.isTraceEnabled()) {
/* 36 */       log.tracef("Calling isReachable on object %s with node name %s.", traversableObject, traversableProperty
/*    */       
/*    */ 
/* 39 */         .getName());
/*    */     }
/*    */     
/*    */ 
/* 43 */     if (traversableObject == null) {
/* 44 */       return true;
/*    */     }
/*    */     
/* 47 */     return Persistence.getPersistenceUtil().isLoaded(traversableObject, traversableProperty.getName());
/*    */   }
/*    */   
/*    */   public final boolean isCascadable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType)
/*    */   {
/* 52 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\resolver\JPATraversableResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */