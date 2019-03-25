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
/*    */ public class AdviceEntry
/*    */   implements ParseState.Entry
/*    */ {
/*    */   private final String kind;
/*    */   
/*    */   public AdviceEntry(String kind)
/*    */   {
/* 37 */     this.kind = kind;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 42 */     return "Advice (" + this.kind + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\config\AdviceEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */