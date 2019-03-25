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
/*     */ public class HostRuleSet
/*     */   extends RuleSetBase
/*     */ {
/*     */   protected final String prefix;
/*     */   
/*     */   public HostRuleSet()
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
/*     */   public HostRuleSet(String prefix)
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
/*  86 */     digester.addObjectCreate(this.prefix + "Host", "org.apache.catalina.core.StandardHost", "className");
/*     */     
/*     */ 
/*  89 */     digester.addSetProperties(this.prefix + "Host");
/*  90 */     digester.addRule(this.prefix + "Host", new CopyParentClassLoaderRule());
/*     */     
/*  92 */     digester.addRule(this.prefix + "Host", new LifecycleListenerRule("org.apache.catalina.startup.HostConfig", "hostConfigClass"));
/*     */     
/*     */ 
/*     */ 
/*  96 */     digester.addSetNext(this.prefix + "Host", "addChild", "org.apache.catalina.Container");
/*     */     
/*     */ 
/*     */ 
/* 100 */     digester.addCallMethod(this.prefix + "Host/Alias", "addAlias", 0);
/*     */     
/*     */ 
/*     */ 
/* 104 */     digester.addObjectCreate(this.prefix + "Host/Cluster", null, "className");
/*     */     
/*     */ 
/* 107 */     digester.addSetProperties(this.prefix + "Host/Cluster");
/* 108 */     digester.addSetNext(this.prefix + "Host/Cluster", "setCluster", "org.apache.catalina.Cluster");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 113 */     digester.addObjectCreate(this.prefix + "Host/Listener", null, "className");
/*     */     
/*     */ 
/* 116 */     digester.addSetProperties(this.prefix + "Host/Listener");
/* 117 */     digester.addSetNext(this.prefix + "Host/Listener", "addLifecycleListener", "org.apache.catalina.LifecycleListener");
/*     */     
/*     */ 
/*     */ 
/* 121 */     digester.addRuleSet(new RealmRuleSet(this.prefix + "Host/"));
/*     */     
/* 123 */     digester.addObjectCreate(this.prefix + "Host/Valve", null, "className");
/*     */     
/*     */ 
/* 126 */     digester.addSetProperties(this.prefix + "Host/Valve");
/* 127 */     digester.addSetNext(this.prefix + "Host/Valve", "addValve", "org.apache.catalina.Valve");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\HostRuleSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */