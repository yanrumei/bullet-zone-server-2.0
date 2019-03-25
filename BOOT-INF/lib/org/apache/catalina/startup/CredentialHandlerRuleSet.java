/*    */ package org.apache.catalina.startup;
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
/*    */ public class CredentialHandlerRuleSet
/*    */   extends RuleSetBase
/*    */ {
/* 32 */   private static final int MAX_NESTED_LEVELS = Integer.getInteger("org.apache.catalina.startup.CredentialHandlerRuleSet.MAX_NESTED_LEVELS", 3)
/*    */   
/* 34 */     .intValue();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected final String prefix;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public CredentialHandlerRuleSet()
/*    */   {
/* 53 */     this("");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public CredentialHandlerRuleSet(String prefix)
/*    */   {
/* 65 */     this.prefix = prefix;
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
/*    */   public void addRuleInstances(Digester digester)
/*    */   {
/* 83 */     StringBuilder pattern = new StringBuilder(this.prefix);
/* 84 */     for (int i = 0; i < MAX_NESTED_LEVELS; i++) {
/* 85 */       if (i > 0) {
/* 86 */         pattern.append('/');
/*    */       }
/* 88 */       pattern.append("CredentialHandler");
/* 89 */       addRuleInstances(digester, pattern.toString(), i == 0 ? "setCredentialHandler" : "addCredentialHandler");
/*    */     }
/*    */   }
/*    */   
/*    */   private void addRuleInstances(Digester digester, String pattern, String methodName)
/*    */   {
/* 95 */     digester.addObjectCreate(pattern, null, "className");
/*    */     
/* 97 */     digester.addSetProperties(pattern);
/* 98 */     digester.addSetNext(pattern, methodName, "org.apache.catalina.CredentialHandler");
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\CredentialHandlerRuleSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */