/*    */ package org.springframework.boot;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import org.springframework.core.env.Environment;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface Banner
/*    */ {
/*    */   public abstract void printBanner(Environment paramEnvironment, Class<?> paramClass, PrintStream paramPrintStream);
/*    */   
/*    */   public static enum Mode
/*    */   {
/* 49 */     OFF, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 54 */     CONSOLE, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 59 */     LOG;
/*    */     
/*    */     private Mode() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\Banner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */