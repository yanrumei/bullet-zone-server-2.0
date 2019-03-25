/*     */ package org.apache.naming;
/*     */ 
/*     */ import java.util.Hashtable;
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
/*     */ public class ContextAccessController
/*     */ {
/*  33 */   private static final Hashtable<Object, Object> readOnlyContexts = new Hashtable();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  39 */   private static final Hashtable<Object, Object> securityTokens = new Hashtable();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setSecurityToken(Object name, Object token)
/*     */   {
/*  51 */     SecurityManager sm = System.getSecurityManager();
/*  52 */     if (sm != null) {
/*  53 */       sm.checkPermission(new RuntimePermission(ContextAccessController.class
/*  54 */         .getName() + ".setSecurityToken"));
/*     */     }
/*     */     
/*  57 */     if ((!securityTokens.containsKey(name)) && (token != null)) {
/*  58 */       securityTokens.put(name, token);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void unsetSecurityToken(Object name, Object token)
/*     */   {
/*  70 */     if (checkSecurityToken(name, token)) {
/*  71 */       securityTokens.remove(name);
/*     */     }
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
/*     */   public static boolean checkSecurityToken(Object name, Object token)
/*     */   {
/*  88 */     Object refToken = securityTokens.get(name);
/*  89 */     return (refToken == null) || (refToken.equals(token));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setWritable(Object name, Object token)
/*     */   {
/* 100 */     if (checkSecurityToken(name, token)) {
/* 101 */       readOnlyContexts.remove(name);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setReadOnly(Object name)
/*     */   {
/* 111 */     readOnlyContexts.put(name, name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isWritable(Object name)
/*     */   {
/* 123 */     return !readOnlyContexts.containsKey(name);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\ContextAccessController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */