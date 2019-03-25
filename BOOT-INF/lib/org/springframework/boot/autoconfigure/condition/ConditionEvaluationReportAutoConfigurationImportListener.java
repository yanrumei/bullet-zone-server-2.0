/*    */ package org.springframework.boot.autoconfigure.condition;
/*    */ 
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.BeanFactoryAware;
/*    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigurationImportEvent;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigurationImportListener;
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
/*    */ class ConditionEvaluationReportAutoConfigurationImportListener
/*    */   implements AutoConfigurationImportListener, BeanFactoryAware
/*    */ {
/*    */   private ConfigurableListableBeanFactory beanFactory;
/*    */   
/*    */   public void onAutoConfigurationImportEvent(AutoConfigurationImportEvent event)
/*    */   {
/* 39 */     if (this.beanFactory != null)
/*    */     {
/* 41 */       ConditionEvaluationReport report = ConditionEvaluationReport.get(this.beanFactory);
/* 42 */       report.recordEvaluationCandidates(event.getCandidateConfigurations());
/* 43 */       report.recordExclusions(event.getExclusions());
/*    */     }
/*    */   }
/*    */   
/*    */   public void setBeanFactory(BeanFactory beanFactory) throws BeansException
/*    */   {
/* 49 */     this.beanFactory = ((beanFactory instanceof ConfigurableListableBeanFactory) ? (ConfigurableListableBeanFactory)beanFactory : null);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\condition\ConditionEvaluationReportAutoConfigurationImportListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */