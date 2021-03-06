/*    */ package org.springframework.scheduling.config;
/*    */ 
/*    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*    */ import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
/*    */ import org.springframework.util.StringUtils;
/*    */ import org.w3c.dom.Element;
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
/*    */ public class ExecutorBeanDefinitionParser
/*    */   extends AbstractSingleBeanDefinitionParser
/*    */ {
/*    */   protected String getBeanClassName(Element element)
/*    */   {
/* 38 */     return "org.springframework.scheduling.config.TaskExecutorFactoryBean";
/*    */   }
/*    */   
/*    */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
/*    */   {
/* 43 */     String keepAliveSeconds = element.getAttribute("keep-alive");
/* 44 */     if (StringUtils.hasText(keepAliveSeconds)) {
/* 45 */       builder.addPropertyValue("keepAliveSeconds", keepAliveSeconds);
/*    */     }
/* 47 */     String queueCapacity = element.getAttribute("queue-capacity");
/* 48 */     if (StringUtils.hasText(queueCapacity)) {
/* 49 */       builder.addPropertyValue("queueCapacity", queueCapacity);
/*    */     }
/* 51 */     configureRejectionPolicy(element, builder);
/* 52 */     String poolSize = element.getAttribute("pool-size");
/* 53 */     if (StringUtils.hasText(poolSize)) {
/* 54 */       builder.addPropertyValue("poolSize", poolSize);
/*    */     }
/*    */   }
/*    */   
/*    */   private void configureRejectionPolicy(Element element, BeanDefinitionBuilder builder) {
/* 59 */     String rejectionPolicy = element.getAttribute("rejection-policy");
/* 60 */     if (!StringUtils.hasText(rejectionPolicy)) {
/* 61 */       return;
/*    */     }
/* 63 */     String prefix = "java.util.concurrent.ThreadPoolExecutor.";
/*    */     String policyClassName;
/* 65 */     String policyClassName; if (rejectionPolicy.equals("ABORT")) {
/* 66 */       policyClassName = prefix + "AbortPolicy";
/*    */     } else { String policyClassName;
/* 68 */       if (rejectionPolicy.equals("CALLER_RUNS")) {
/* 69 */         policyClassName = prefix + "CallerRunsPolicy";
/*    */       } else { String policyClassName;
/* 71 */         if (rejectionPolicy.equals("DISCARD")) {
/* 72 */           policyClassName = prefix + "DiscardPolicy";
/*    */         } else { String policyClassName;
/* 74 */           if (rejectionPolicy.equals("DISCARD_OLDEST")) {
/* 75 */             policyClassName = prefix + "DiscardOldestPolicy";
/*    */           }
/*    */           else
/* 78 */             policyClassName = rejectionPolicy;
/*    */         } } }
/* 80 */     builder.addPropertyValue("rejectedExecutionHandler", new RootBeanDefinition(policyClassName));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\config\ExecutorBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */