/*     */ package org.apache.catalina.authenticator.jaspic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.Principal;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.security.auth.Subject;
/*     */ import javax.security.auth.callback.Callback;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.auth.callback.UnsupportedCallbackException;
/*     */ import javax.security.auth.message.callback.CallerPrincipalCallback;
/*     */ import javax.security.auth.message.callback.GroupPrincipalCallback;
/*     */ import org.apache.catalina.realm.GenericPrincipal;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CallbackHandlerImpl
/*     */   implements CallbackHandler
/*     */ {
/*  42 */   private static final Log log = LogFactory.getLog(CallbackHandlerImpl.class);
/*  43 */   private static final StringManager sm = StringManager.getManager(CallbackHandlerImpl.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  49 */   private static CallbackHandler instance = new CallbackHandlerImpl();
/*     */   
/*     */ 
/*     */   public static CallbackHandler getInstance()
/*     */   {
/*  54 */     return instance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void handle(Callback[] callbacks)
/*     */     throws IOException, UnsupportedCallbackException
/*     */   {
/*  66 */     String name = null;
/*  67 */     Principal principal = null;
/*  68 */     Subject subject = null;
/*  69 */     String[] groups = null;
/*     */     
/*  71 */     if (callbacks != null)
/*     */     {
/*     */ 
/*     */ 
/*  75 */       for (Callback callback : callbacks) {
/*  76 */         if ((callback instanceof CallerPrincipalCallback)) {
/*  77 */           CallerPrincipalCallback cpc = (CallerPrincipalCallback)callback;
/*  78 */           name = cpc.getName();
/*  79 */           principal = cpc.getPrincipal();
/*  80 */           subject = cpc.getSubject();
/*  81 */         } else if ((callback instanceof GroupPrincipalCallback)) {
/*  82 */           GroupPrincipalCallback gpc = (GroupPrincipalCallback)callback;
/*  83 */           groups = gpc.getGroups();
/*     */         } else {
/*  85 */           log.error(sm.getString("callbackHandlerImpl.jaspicCallbackMissing", new Object[] {callback
/*  86 */             .getClass().getName() }));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*  91 */       Principal gp = getPrincipal(principal, name, groups);
/*  92 */       if ((subject != null) && (gp != null)) {
/*  93 */         subject.getPrivateCredentials().add(gp);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private Principal getPrincipal(Principal principal, String name, String[] groups)
/*     */   {
/* 101 */     if ((principal instanceof GenericPrincipal)) {
/* 102 */       return principal;
/*     */     }
/* 104 */     if ((name == null) && (principal != null)) {
/* 105 */       name = principal.getName();
/*     */     }
/* 107 */     if (name == null)
/* 108 */       return null;
/*     */     List<String> roles;
/*     */     List<String> roles;
/* 111 */     if ((groups == null) || (groups.length == 0)) {
/* 112 */       roles = Collections.emptyList();
/*     */     } else {
/* 114 */       roles = Arrays.asList(groups);
/*     */     }
/*     */     
/* 117 */     return new GenericPrincipal(name, null, roles, principal);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\authenticator\jaspic\CallbackHandlerImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */