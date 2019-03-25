/*    */ package org.springframework.scheduling.annotation;
/*    */ 
/*    */ import org.springframework.context.annotation.AdviceMode;
/*    */ import org.springframework.context.annotation.AdviceModeImportSelector;
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
/*    */ public class AsyncConfigurationSelector
/*    */   extends AdviceModeImportSelector<EnableAsync>
/*    */ {
/*    */   private static final String ASYNC_EXECUTION_ASPECT_CONFIGURATION_CLASS_NAME = "org.springframework.scheduling.aspectj.AspectJAsyncConfiguration";
/*    */   
/*    */   public String[] selectImports(AdviceMode adviceMode)
/*    */   {
/* 43 */     switch (adviceMode) {
/*    */     case PROXY: 
/* 45 */       return new String[] { ProxyAsyncConfiguration.class.getName() };
/*    */     case ASPECTJ: 
/* 47 */       return new String[] { "org.springframework.scheduling.aspectj.AspectJAsyncConfiguration" };
/*    */     }
/* 49 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\annotation\AsyncConfigurationSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */