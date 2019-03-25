/*    */ package org.springframework.aop.config;
/*    */ 
/*    */ import org.springframework.beans.factory.parsing.ParseState.Entry;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AspectEntry
/*    */   implements ParseState.Entry
/*    */ {
/*    */   private final String id;
/*    */   private final String ref;
/*    */   
/*    */   public AspectEntry(String id, String ref)
/*    */   {
/* 42 */     this.id = id;
/* 43 */     this.ref = ref;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 48 */     return "Aspect: " + (StringUtils.hasLength(this.id) ? "id='" + this.id + "'" : new StringBuilder().append("ref='").append(this.ref).append("'").toString());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\config\AspectEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */