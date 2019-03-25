/*    */ package org.springframework.boot.context.config;
/*    */ 
/*    */ import org.springframework.boot.ansi.AnsiOutput;
/*    */ import org.springframework.boot.ansi.AnsiOutput.Enabled;
/*    */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*    */ import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
/*    */ import org.springframework.context.ApplicationListener;
/*    */ import org.springframework.core.Ordered;
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
/*    */ public class AnsiOutputApplicationListener
/*    */   implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered
/*    */ {
/*    */   public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event)
/*    */   {
/* 40 */     RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(event.getEnvironment(), "spring.output.ansi.");
/* 41 */     if (resolver.containsProperty("enabled")) {
/* 42 */       String enabled = resolver.getProperty("enabled");
/* 43 */       AnsiOutput.setEnabled((AnsiOutput.Enabled)Enum.valueOf(AnsiOutput.Enabled.class, enabled.toUpperCase()));
/*    */     }
/*    */     
/* 46 */     if (resolver.containsProperty("console-available")) {
/* 47 */       AnsiOutput.setConsoleAvailable(
/* 48 */         (Boolean)resolver.getProperty("console-available", Boolean.class));
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public int getOrder()
/*    */   {
/* 56 */     return -2147483637;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\config\AnsiOutputApplicationListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */