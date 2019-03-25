/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public class QualifierEntry
/*    */   implements ParseState.Entry
/*    */ {
/*    */   private String typeName;
/*    */   
/*    */   public QualifierEntry(String typeName)
/*    */   {
/* 33 */     if (!StringUtils.hasText(typeName)) {
/* 34 */       throw new IllegalArgumentException("Invalid qualifier type '" + typeName + "'.");
/*    */     }
/* 36 */     this.typeName = typeName;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 41 */     return "Qualifier '" + this.typeName + "'";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\parsing\QualifierEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */