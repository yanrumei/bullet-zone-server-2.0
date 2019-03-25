/*    */ package org.apache.catalina.realm;
/*    */ 
/*    */ import org.apache.tomcat.util.digester.Digester;
/*    */ import org.apache.tomcat.util.digester.RuleSetBase;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MemoryRuleSet
/*    */   extends RuleSetBase
/*    */ {
/*    */   protected final String prefix;
/*    */   
/*    */   public MemoryRuleSet()
/*    */   {
/* 56 */     this("tomcat-users/");
/*    */   }
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
/*    */   public MemoryRuleSet(String prefix)
/*    */   {
/* 70 */     this.prefix = prefix;
/*    */   }
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
/*    */   public void addRuleInstances(Digester digester)
/*    */   {
/* 89 */     digester.addRule(this.prefix + "user", new MemoryUserRule());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\realm\MemoryRuleSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */