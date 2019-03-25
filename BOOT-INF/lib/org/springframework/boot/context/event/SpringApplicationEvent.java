/*    */ package org.springframework.boot.context.event;
/*    */ 
/*    */ import org.springframework.boot.SpringApplication;
/*    */ import org.springframework.context.ApplicationEvent;
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
/*    */ public abstract class SpringApplicationEvent
/*    */   extends ApplicationEvent
/*    */ {
/*    */   private final String[] args;
/*    */   
/*    */   public SpringApplicationEvent(SpringApplication application, String[] args)
/*    */   {
/* 33 */     super(application);
/* 34 */     this.args = args;
/*    */   }
/*    */   
/*    */   public SpringApplication getSpringApplication() {
/* 38 */     return (SpringApplication)getSource();
/*    */   }
/*    */   
/*    */   public final String[] getArgs() {
/* 42 */     return this.args;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\event\SpringApplicationEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */