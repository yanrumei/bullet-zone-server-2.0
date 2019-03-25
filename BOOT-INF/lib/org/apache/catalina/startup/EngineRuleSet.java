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
/*     */ 
/*     */ public class EngineRuleSet
/*     */   extends RuleSetBase
/*     */ {
/*     */   protected final String prefix;
/*     */   
/*     */   public EngineRuleSet()
/*     */   {
/*  55 */     this("");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EngineRuleSet(String prefix)
/*     */   {
/*  67 */     this.prefix = prefix;
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
/*  86 */     digester.addObjectCreate(this.prefix + "Engine", "org.apache.catalina.core.StandardEngine", "className");
/*     */     
/*     */ 
/*  89 */     digester.addSetProperties(this.prefix + "Engine");
/*  90 */     digester.addRule(this.prefix + "Engine", new LifecycleListenerRule("org.apache.catalina.startup.EngineConfig", "engineConfigClass"));
/*     */     
/*     */ 
/*     */ 
/*  94 */     digester.addSetNext(this.prefix + "Engine", "setContainer", "org.apache.catalina.Engine");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  99 */     digester.addObjectCreate(this.prefix + "Engine/Cluster", null, "className");
/*     */     
/*     */ 
/* 102 */     digester.addSetProperties(this.prefix + "Engine/Cluster");
/* 103 */     digester.addSetNext(this.prefix + "Engine/Cluster", "setCluster", "org.apache.catalina.Cluster");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 108 */     digester.addObjectCreate(this.prefix + "Engine/Listener", null, "className");
/*     */     
/*     */ 
/* 111 */     digester.addSetProperties(this.prefix + "Engine/Listener");
/* 112 */     digester.addSetNext(this.prefix + "Engine/Listener", "addLifecycleListener", "org.apache.catalina.LifecycleListener");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 117 */     digester.addRuleSet(new RealmRuleSet(this.prefix + "Engine/"));
/*     */     
/* 119 */     digester.addObjectCreate(this.prefix + "Engine/Valve", null, "className");
/*     */     
/*     */ 
/* 122 */     digester.addSetProperties(this.prefix + "Engine/Valve");
/* 123 */     digester.addSetNext(this.prefix + "Engine/Valve", "addValve", "org.apache.catalina.Valve");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\EngineRuleSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */