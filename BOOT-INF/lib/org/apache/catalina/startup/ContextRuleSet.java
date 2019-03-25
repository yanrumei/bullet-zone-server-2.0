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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContextRuleSet
/*     */   extends RuleSetBase
/*     */ {
/*     */   protected final String prefix;
/*     */   protected final boolean create;
/*     */   
/*     */   public ContextRuleSet()
/*     */   {
/*  59 */     this("");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContextRuleSet(String prefix)
/*     */   {
/*  71 */     this(prefix, true);
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
/*     */   public ContextRuleSet(String prefix, boolean create)
/*     */   {
/*  85 */     this.prefix = prefix;
/*  86 */     this.create = create;
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
/* 105 */     if (this.create) {
/* 106 */       digester.addObjectCreate(this.prefix + "Context", "org.apache.catalina.core.StandardContext", "className");
/*     */       
/* 108 */       digester.addSetProperties(this.prefix + "Context");
/*     */     } else {
/* 110 */       digester.addRule(this.prefix + "Context", new SetContextPropertiesRule());
/*     */     }
/*     */     
/* 113 */     if (this.create) {
/* 114 */       digester.addRule(this.prefix + "Context", new LifecycleListenerRule("org.apache.catalina.startup.ContextConfig", "configClass"));
/*     */       
/*     */ 
/*     */ 
/* 118 */       digester.addSetNext(this.prefix + "Context", "addChild", "org.apache.catalina.Container");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 123 */     digester.addObjectCreate(this.prefix + "Context/Listener", null, "className");
/*     */     
/*     */ 
/* 126 */     digester.addSetProperties(this.prefix + "Context/Listener");
/* 127 */     digester.addSetNext(this.prefix + "Context/Listener", "addLifecycleListener", "org.apache.catalina.LifecycleListener");
/*     */     
/*     */ 
/*     */ 
/* 131 */     digester.addObjectCreate(this.prefix + "Context/Loader", "org.apache.catalina.loader.WebappLoader", "className");
/*     */     
/*     */ 
/* 134 */     digester.addSetProperties(this.prefix + "Context/Loader");
/* 135 */     digester.addSetNext(this.prefix + "Context/Loader", "setLoader", "org.apache.catalina.Loader");
/*     */     
/*     */ 
/*     */ 
/* 139 */     digester.addObjectCreate(this.prefix + "Context/Manager", "org.apache.catalina.session.StandardManager", "className");
/*     */     
/*     */ 
/* 142 */     digester.addSetProperties(this.prefix + "Context/Manager");
/* 143 */     digester.addSetNext(this.prefix + "Context/Manager", "setManager", "org.apache.catalina.Manager");
/*     */     
/*     */ 
/*     */ 
/* 147 */     digester.addObjectCreate(this.prefix + "Context/Manager/Store", null, "className");
/*     */     
/*     */ 
/* 150 */     digester.addSetProperties(this.prefix + "Context/Manager/Store");
/* 151 */     digester.addSetNext(this.prefix + "Context/Manager/Store", "setStore", "org.apache.catalina.Store");
/*     */     
/*     */ 
/*     */ 
/* 155 */     digester.addObjectCreate(this.prefix + "Context/Manager/SessionIdGenerator", "org.apache.catalina.util.StandardSessionIdGenerator", "className");
/*     */     
/*     */ 
/* 158 */     digester.addSetProperties(this.prefix + "Context/Manager/SessionIdGenerator");
/* 159 */     digester.addSetNext(this.prefix + "Context/Manager/SessionIdGenerator", "setSessionIdGenerator", "org.apache.catalina.SessionIdGenerator");
/*     */     
/*     */ 
/*     */ 
/* 163 */     digester.addObjectCreate(this.prefix + "Context/Parameter", "org.apache.tomcat.util.descriptor.web.ApplicationParameter");
/*     */     
/* 165 */     digester.addSetProperties(this.prefix + "Context/Parameter");
/* 166 */     digester.addSetNext(this.prefix + "Context/Parameter", "addApplicationParameter", "org.apache.tomcat.util.descriptor.web.ApplicationParameter");
/*     */     
/*     */ 
/*     */ 
/* 170 */     digester.addRuleSet(new RealmRuleSet(this.prefix + "Context/"));
/*     */     
/* 172 */     digester.addObjectCreate(this.prefix + "Context/Resources", "org.apache.catalina.webresources.StandardRoot", "className");
/*     */     
/*     */ 
/* 175 */     digester.addSetProperties(this.prefix + "Context/Resources");
/* 176 */     digester.addSetNext(this.prefix + "Context/Resources", "setResources", "org.apache.catalina.WebResourceRoot");
/*     */     
/*     */ 
/*     */ 
/* 180 */     digester.addObjectCreate(this.prefix + "Context/Resources/PreResources", null, "className");
/*     */     
/*     */ 
/* 183 */     digester.addSetProperties(this.prefix + "Context/Resources/PreResources");
/* 184 */     digester.addSetNext(this.prefix + "Context/Resources/PreResources", "addPreResources", "org.apache.catalina.WebResourceSet");
/*     */     
/*     */ 
/*     */ 
/* 188 */     digester.addObjectCreate(this.prefix + "Context/Resources/JarResources", null, "className");
/*     */     
/*     */ 
/* 191 */     digester.addSetProperties(this.prefix + "Context/Resources/JarResources");
/* 192 */     digester.addSetNext(this.prefix + "Context/Resources/JarResources", "addJarResources", "org.apache.catalina.WebResourceSet");
/*     */     
/*     */ 
/*     */ 
/* 196 */     digester.addObjectCreate(this.prefix + "Context/Resources/PostResources", null, "className");
/*     */     
/*     */ 
/* 199 */     digester.addSetProperties(this.prefix + "Context/Resources/PostResources");
/* 200 */     digester.addSetNext(this.prefix + "Context/Resources/PostResources", "addPostResources", "org.apache.catalina.WebResourceSet");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 205 */     digester.addObjectCreate(this.prefix + "Context/ResourceLink", "org.apache.tomcat.util.descriptor.web.ContextResourceLink");
/*     */     
/* 207 */     digester.addSetProperties(this.prefix + "Context/ResourceLink");
/* 208 */     digester.addRule(this.prefix + "Context/ResourceLink", new SetNextNamingRule("addResourceLink", "org.apache.tomcat.util.descriptor.web.ContextResourceLink"));
/*     */     
/*     */ 
/*     */ 
/* 212 */     digester.addObjectCreate(this.prefix + "Context/Valve", null, "className");
/*     */     
/*     */ 
/* 215 */     digester.addSetProperties(this.prefix + "Context/Valve");
/* 216 */     digester.addSetNext(this.prefix + "Context/Valve", "addValve", "org.apache.catalina.Valve");
/*     */     
/*     */ 
/*     */ 
/* 220 */     digester.addCallMethod(this.prefix + "Context/WatchedResource", "addWatchedResource", 0);
/*     */     
/*     */ 
/* 223 */     digester.addCallMethod(this.prefix + "Context/WrapperLifecycle", "addWrapperLifecycle", 0);
/*     */     
/*     */ 
/* 226 */     digester.addCallMethod(this.prefix + "Context/WrapperListener", "addWrapperListener", 0);
/*     */     
/*     */ 
/* 229 */     digester.addObjectCreate(this.prefix + "Context/JarScanner", "org.apache.tomcat.util.scan.StandardJarScanner", "className");
/*     */     
/*     */ 
/* 232 */     digester.addSetProperties(this.prefix + "Context/JarScanner");
/* 233 */     digester.addSetNext(this.prefix + "Context/JarScanner", "setJarScanner", "org.apache.tomcat.JarScanner");
/*     */     
/*     */ 
/*     */ 
/* 237 */     digester.addObjectCreate(this.prefix + "Context/JarScanner/JarScanFilter", "org.apache.tomcat.util.scan.StandardJarScanFilter", "className");
/*     */     
/*     */ 
/* 240 */     digester.addSetProperties(this.prefix + "Context/JarScanner/JarScanFilter");
/* 241 */     digester.addSetNext(this.prefix + "Context/JarScanner/JarScanFilter", "setJarScanFilter", "org.apache.tomcat.JarScanFilter");
/*     */     
/*     */ 
/*     */ 
/* 245 */     digester.addObjectCreate(this.prefix + "Context/CookieProcessor", "org.apache.tomcat.util.http.Rfc6265CookieProcessor", "className");
/*     */     
/*     */ 
/* 248 */     digester.addSetProperties(this.prefix + "Context/CookieProcessor");
/* 249 */     digester.addSetNext(this.prefix + "Context/CookieProcessor", "setCookieProcessor", "org.apache.tomcat.util.http.CookieProcessor");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\ContextRuleSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */