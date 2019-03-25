/*    */ package org.springframework.boot;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExitCodeEvent
/*    */   extends ApplicationEvent
/*    */ {
/*    */   private final int exitCode;
/*    */   
/*    */   public ExitCodeEvent(Object source, int exitCode)
/*    */   {
/* 38 */     super(source);
/* 39 */     this.exitCode = exitCode;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getExitCode()
/*    */   {
/* 47 */     return this.exitCode;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\ExitCodeEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */