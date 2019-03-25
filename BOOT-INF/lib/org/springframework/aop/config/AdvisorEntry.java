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
/*    */ 
/*    */ public class AdvisorEntry
/*    */   implements ParseState.Entry
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   public AdvisorEntry(String name)
/*    */   {
/* 37 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 42 */     return "Advisor '" + this.name + "'";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\config\AdvisorEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */