/*    */ package org.springframework.boot.autoconfigure.transaction;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.core.ResolvableType;
/*    */ import org.springframework.transaction.PlatformTransactionManager;
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
/*    */ public class TransactionManagerCustomizers
/*    */ {
/* 38 */   private static final Log logger = LogFactory.getLog(TransactionManagerCustomizers.class);
/*    */   
/*    */   private final List<PlatformTransactionManagerCustomizer<?>> customizers;
/*    */   
/*    */   public TransactionManagerCustomizers(Collection<? extends PlatformTransactionManagerCustomizer<?>> customizers)
/*    */   {
/* 44 */     this.customizers = (customizers == null ? null : new ArrayList(customizers));
/*    */   }
/*    */   
/*    */   public void customize(PlatformTransactionManager transactionManager)
/*    */   {
/* 49 */     if (this.customizers != null) {
/* 50 */       for (PlatformTransactionManagerCustomizer<?> customizer : this.customizers)
/*    */       {
/*    */ 
/*    */ 
/* 54 */         Class<?> generic = ResolvableType.forClass(PlatformTransactionManagerCustomizer.class, customizer.getClass()).resolveGeneric(new int[0]);
/* 55 */         if (generic.isInstance(transactionManager)) {
/* 56 */           customize(transactionManager, customizer);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   private void customize(PlatformTransactionManager transactionManager, PlatformTransactionManagerCustomizer customizer)
/*    */   {
/*    */     try
/*    */     {
/* 66 */       customizer.customize(transactionManager);
/*    */ 
/*    */     }
/*    */     catch (ClassCastException ex)
/*    */     {
/* 71 */       if (logger.isDebugEnabled()) {
/* 72 */         logger.debug("Non-matching transaction manager type for customizer: " + customizer, ex);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\transaction\TransactionManagerCustomizers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */