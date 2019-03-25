/*    */ package org.springframework.boot.diagnostics.analyzer;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.io.StringWriter;
/*    */ import java.lang.reflect.Proxy;
/*    */ import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
/*    */ import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
/*    */ import org.springframework.boot.diagnostics.FailureAnalysis;
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
/*    */ public class BeanNotOfRequiredTypeFailureAnalyzer
/*    */   extends AbstractFailureAnalyzer<BeanNotOfRequiredTypeException>
/*    */ {
/*    */   private static final String ACTION = "Consider injecting the bean as one of its interfaces or forcing the use of CGLib-based proxies by setting proxyTargetClass=true on @EnableAsync and/or @EnableCaching.";
/*    */   
/*    */   protected FailureAnalysis analyze(Throwable rootFailure, BeanNotOfRequiredTypeException cause)
/*    */   {
/* 44 */     if (!Proxy.isProxyClass(cause.getActualType())) {
/* 45 */       return null;
/*    */     }
/* 47 */     return new FailureAnalysis(getDescription(cause), "Consider injecting the bean as one of its interfaces or forcing the use of CGLib-based proxies by setting proxyTargetClass=true on @EnableAsync and/or @EnableCaching.", cause);
/*    */   }
/*    */   
/*    */   private String getDescription(BeanNotOfRequiredTypeException ex) {
/* 51 */     StringWriter description = new StringWriter();
/* 52 */     PrintWriter printer = new PrintWriter(description);
/* 53 */     printer.printf("The bean '%s' could not be injected as a '%s' because it is a JDK dynamic proxy that implements:%n", new Object[] {ex
/*    */     
/*    */ 
/* 56 */       .getBeanName(), ex.getRequiredType().getName() });
/* 57 */     for (Class<?> requiredTypeInterface : ex.getRequiredType().getInterfaces()) {
/* 58 */       printer.println("\t" + requiredTypeInterface.getName());
/*    */     }
/* 60 */     return description.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\diagnostics\analyzer\BeanNotOfRequiredTypeFailureAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */