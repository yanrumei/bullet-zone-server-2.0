/*     */ package org.apache.catalina.startup;
/*     */ 
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.deploy.NamingResourcesImpl;
/*     */ import org.apache.tomcat.util.IntrospectionUtils;
/*     */ import org.apache.tomcat.util.digester.Digester;
/*     */ import org.apache.tomcat.util.digester.Rule;
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
/*     */ public class SetNextNamingRule
/*     */   extends Rule
/*     */ {
/*     */   protected final String methodName;
/*     */   protected final String paramType;
/*     */   
/*     */   public SetNextNamingRule(String methodName, String paramType)
/*     */   {
/*  56 */     this.methodName = methodName;
/*  57 */     this.paramType = paramType;
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
/*     */   public void end(String namespace, String name)
/*     */     throws Exception
/*     */   {
/*  93 */     Object child = this.digester.peek(0);
/*  94 */     Object parent = this.digester.peek(1);
/*     */     
/*  96 */     NamingResourcesImpl namingResources = null;
/*  97 */     if ((parent instanceof Context)) {
/*  98 */       namingResources = ((Context)parent).getNamingResources();
/*     */     } else {
/* 100 */       namingResources = (NamingResourcesImpl)parent;
/*     */     }
/*     */     
/*     */ 
/* 104 */     IntrospectionUtils.callMethod1(namingResources, this.methodName, child, this.paramType, this.digester
/* 105 */       .getClassLoader());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 116 */     StringBuilder sb = new StringBuilder("SetNextRule[");
/* 117 */     sb.append("methodName=");
/* 118 */     sb.append(this.methodName);
/* 119 */     sb.append(", paramType=");
/* 120 */     sb.append(this.paramType);
/* 121 */     sb.append("]");
/* 122 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\SetNextNamingRule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */