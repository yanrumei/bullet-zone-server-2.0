/*    */ package org.apache.tomcat.util.digester;
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
/*    */ @Deprecated
/*    */ public abstract class RuleSetBase
/*    */   implements RuleSet
/*    */ {
/*    */   @Deprecated
/* 38 */   protected String namespaceURI = null;
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
/*    */   @Deprecated
/*    */   public String getNamespaceURI()
/*    */   {
/* 53 */     return this.namespaceURI;
/*    */   }
/*    */   
/*    */   public abstract void addRuleInstances(Digester paramDigester);
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\digester\RuleSetBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */