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
/*     */ public class NamingRuleSet
/*     */   extends RuleSetBase
/*     */ {
/*     */   protected final String prefix;
/*     */   
/*     */   public NamingRuleSet()
/*     */   {
/*  54 */     this("");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NamingRuleSet(String prefix)
/*     */   {
/*  66 */     this.prefix = prefix;
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
/*     */ 
/*     */   public void addRuleInstances(Digester digester)
/*     */   {
/*  85 */     digester.addObjectCreate(this.prefix + "Ejb", "org.apache.tomcat.util.descriptor.web.ContextEjb");
/*     */     
/*  87 */     digester.addRule(this.prefix + "Ejb", new SetAllPropertiesRule());
/*  88 */     digester.addRule(this.prefix + "Ejb", new SetNextNamingRule("addEjb", "org.apache.tomcat.util.descriptor.web.ContextEjb"));
/*     */     
/*     */ 
/*     */ 
/*  92 */     digester.addObjectCreate(this.prefix + "Environment", "org.apache.tomcat.util.descriptor.web.ContextEnvironment");
/*     */     
/*  94 */     digester.addSetProperties(this.prefix + "Environment");
/*  95 */     digester.addRule(this.prefix + "Environment", new SetNextNamingRule("addEnvironment", "org.apache.tomcat.util.descriptor.web.ContextEnvironment"));
/*     */     
/*     */ 
/*     */ 
/*  99 */     digester.addObjectCreate(this.prefix + "LocalEjb", "org.apache.tomcat.util.descriptor.web.ContextLocalEjb");
/*     */     
/* 101 */     digester.addRule(this.prefix + "LocalEjb", new SetAllPropertiesRule());
/* 102 */     digester.addRule(this.prefix + "LocalEjb", new SetNextNamingRule("addLocalEjb", "org.apache.tomcat.util.descriptor.web.ContextLocalEjb"));
/*     */     
/*     */ 
/*     */ 
/* 106 */     digester.addObjectCreate(this.prefix + "Resource", "org.apache.tomcat.util.descriptor.web.ContextResource");
/*     */     
/* 108 */     digester.addRule(this.prefix + "Resource", new SetAllPropertiesRule());
/* 109 */     digester.addRule(this.prefix + "Resource", new SetNextNamingRule("addResource", "org.apache.tomcat.util.descriptor.web.ContextResource"));
/*     */     
/*     */ 
/*     */ 
/* 113 */     digester.addObjectCreate(this.prefix + "ResourceEnvRef", "org.apache.tomcat.util.descriptor.web.ContextResourceEnvRef");
/*     */     
/* 115 */     digester.addRule(this.prefix + "ResourceEnvRef", new SetAllPropertiesRule());
/* 116 */     digester.addRule(this.prefix + "ResourceEnvRef", new SetNextNamingRule("addResourceEnvRef", "org.apache.tomcat.util.descriptor.web.ContextResourceEnvRef"));
/*     */     
/*     */ 
/*     */ 
/* 120 */     digester.addObjectCreate(this.prefix + "ServiceRef", "org.apache.tomcat.util.descriptor.web.ContextService");
/*     */     
/* 122 */     digester.addRule(this.prefix + "ServiceRef", new SetAllPropertiesRule());
/* 123 */     digester.addRule(this.prefix + "ServiceRef", new SetNextNamingRule("addService", "org.apache.tomcat.util.descriptor.web.ContextService"));
/*     */     
/*     */ 
/*     */ 
/* 127 */     digester.addObjectCreate(this.prefix + "Transaction", "org.apache.tomcat.util.descriptor.web.ContextTransaction");
/*     */     
/* 129 */     digester.addRule(this.prefix + "Transaction", new SetAllPropertiesRule());
/* 130 */     digester.addRule(this.prefix + "Transaction", new SetNextNamingRule("setTransaction", "org.apache.tomcat.util.descriptor.web.ContextTransaction"));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\NamingRuleSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */