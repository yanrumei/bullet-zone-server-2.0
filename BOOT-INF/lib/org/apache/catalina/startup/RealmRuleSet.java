/*     */ package org.apache.catalina.startup;
/*     */ 
/*     */ import org.apache.tomcat.util.digester.Digester;
/*     */ import org.apache.tomcat.util.digester.RuleSetBase;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RealmRuleSet
/*     */   extends RuleSetBase
/*     */ {
/*  35 */   private static final int MAX_NESTED_REALM_LEVELS = Integer.getInteger("org.apache.catalina.startup.RealmRuleSet.MAX_NESTED_REALM_LEVELS", 3)
/*     */   
/*  37 */     .intValue();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String prefix;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RealmRuleSet()
/*     */   {
/*  56 */     this("");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RealmRuleSet(String prefix)
/*     */   {
/*  68 */     this.prefix = prefix;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addRuleInstances(Digester digester)
/*     */   {
/*  86 */     StringBuilder pattern = new StringBuilder(this.prefix);
/*  87 */     for (int i = 0; i < MAX_NESTED_REALM_LEVELS; i++) {
/*  88 */       if (i > 0) {
/*  89 */         pattern.append('/');
/*     */       }
/*  91 */       pattern.append("Realm");
/*  92 */       addRuleInstances(digester, pattern.toString(), i == 0 ? "setRealm" : "addRealm");
/*     */     }
/*     */   }
/*     */   
/*     */   private void addRuleInstances(Digester digester, String pattern, String methodName) {
/*  97 */     digester.addObjectCreate(pattern, null, "className");
/*     */     
/*  99 */     digester.addSetProperties(pattern);
/* 100 */     digester.addSetNext(pattern, methodName, "org.apache.catalina.Realm");
/* 101 */     digester.addRuleSet(new CredentialHandlerRuleSet(pattern + "/"));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\RealmRuleSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */