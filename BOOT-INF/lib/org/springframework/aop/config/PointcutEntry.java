/*    */ package org.springframework.aop.config;
/*    */ 
/*    */ import org.springframework.beans.factory.parsing.ParseState.Entry;
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
/*    */ public class PointcutEntry
/*    */   implements ParseState.Entry
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   public PointcutEntry(String name)
/*    */   {
/* 36 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 41 */     return "Pointcut '" + this.name + "'";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\config\PointcutEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */