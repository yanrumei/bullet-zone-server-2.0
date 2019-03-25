/*     */ package org.apache.catalina.realm;
/*     */ 
/*     */ import java.security.Principal;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.Lifecycle;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.Realm;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ import org.ietf.jgss.GSSContext;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.GSSName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CombinedRealm
/*     */   extends RealmBase
/*     */ {
/*  46 */   private static final Log log = LogFactory.getLog(CombinedRealm.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  51 */   protected final List<Realm> realms = new LinkedList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected static final String name = "CombinedRealm";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addRealm(Realm theRealm)
/*     */   {
/*  66 */     this.realms.add(theRealm);
/*     */     
/*  68 */     if (log.isDebugEnabled()) {
/*  69 */       sm.getString("combinedRealm.addRealm", new Object[] {theRealm
/*  70 */         .getClass().getName(), 
/*  71 */         Integer.toString(this.realms.size()) });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectName[] getRealms()
/*     */   {
/*  80 */     ObjectName[] result = new ObjectName[this.realms.size()];
/*  81 */     for (Realm realm : this.realms) {
/*  82 */       if ((realm instanceof RealmBase))
/*     */       {
/*  84 */         result[this.realms.indexOf(realm)] = ((RealmBase)realm).getObjectName();
/*     */       }
/*     */     }
/*  87 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Realm[] getNestedRealms()
/*     */   {
/*  94 */     return (Realm[])this.realms.toArray(new Realm[0]);
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
/*     */   public Principal authenticate(String username, String clientDigest, String nonce, String nc, String cnonce, String qop, String realmName, String md5a2)
/*     */   {
/* 114 */     Principal authenticatedUser = null;
/*     */     
/* 116 */     for (Realm realm : this.realms) {
/* 117 */       if (log.isDebugEnabled()) {
/* 118 */         log.debug(sm.getString("combinedRealm.authStart", new Object[] { username, realm
/* 119 */           .getClass().getName() }));
/*     */       }
/*     */       
/* 122 */       authenticatedUser = realm.authenticate(username, clientDigest, nonce, nc, cnonce, qop, realmName, md5a2);
/*     */       
/*     */ 
/* 125 */       if (authenticatedUser == null) {
/* 126 */         if (log.isDebugEnabled()) {
/* 127 */           log.debug(sm.getString("combinedRealm.authFail", new Object[] { username, realm
/* 128 */             .getClass().getName() }));
/*     */         }
/*     */       } else {
/* 131 */         if (!log.isDebugEnabled()) break;
/* 132 */         log.debug(sm.getString("combinedRealm.authSuccess", new Object[] { username, realm
/* 133 */           .getClass().getName() })); break;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 138 */     return authenticatedUser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Principal authenticate(String username)
/*     */   {
/* 150 */     Principal authenticatedUser = null;
/*     */     
/* 152 */     for (Realm realm : this.realms) {
/* 153 */       if (log.isDebugEnabled()) {
/* 154 */         log.debug(sm.getString("combinedRealm.authStart", new Object[] { username, realm
/* 155 */           .getClass().getName() }));
/*     */       }
/*     */       
/* 158 */       authenticatedUser = realm.authenticate(username);
/*     */       
/* 160 */       if (authenticatedUser == null) {
/* 161 */         if (log.isDebugEnabled()) {
/* 162 */           log.debug(sm.getString("combinedRealm.authFail", new Object[] { username, realm
/* 163 */             .getClass().getName() }));
/*     */         }
/*     */       } else {
/* 166 */         if (!log.isDebugEnabled()) break;
/* 167 */         log.debug(sm.getString("combinedRealm.authSuccess", new Object[] { username, realm
/* 168 */           .getClass().getName() })); break;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 173 */     return authenticatedUser;
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
/*     */   public Principal authenticate(String username, String credentials)
/*     */   {
/* 187 */     Principal authenticatedUser = null;
/*     */     
/* 189 */     for (Realm realm : this.realms) {
/* 190 */       if (log.isDebugEnabled()) {
/* 191 */         log.debug(sm.getString("combinedRealm.authStart", new Object[] { username, realm
/* 192 */           .getClass().getName() }));
/*     */       }
/*     */       
/* 195 */       authenticatedUser = realm.authenticate(username, credentials);
/*     */       
/* 197 */       if (authenticatedUser == null) {
/* 198 */         if (log.isDebugEnabled()) {
/* 199 */           log.debug(sm.getString("combinedRealm.authFail", new Object[] { username, realm
/* 200 */             .getClass().getName() }));
/*     */         }
/*     */       } else {
/* 203 */         if (!log.isDebugEnabled()) break;
/* 204 */         log.debug(sm.getString("combinedRealm.authSuccess", new Object[] { username, realm
/* 205 */           .getClass().getName() })); break;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 210 */     return authenticatedUser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContainer(Container container)
/*     */   {
/* 221 */     for (Realm realm : this.realms)
/*     */     {
/* 223 */       if ((realm instanceof RealmBase)) {
/* 224 */         ((RealmBase)realm).setRealmPath(
/* 225 */           getRealmPath() + "/realm" + this.realms.indexOf(realm));
/*     */       }
/*     */       
/*     */ 
/* 229 */       realm.setContainer(container);
/*     */     }
/* 231 */     super.setContainer(container);
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
/*     */   protected void startInternal()
/*     */     throws LifecycleException
/*     */   {
/* 246 */     Iterator<Realm> iter = this.realms.iterator();
/*     */     
/* 248 */     while (iter.hasNext()) {
/* 249 */       Realm realm = (Realm)iter.next();
/* 250 */       if ((realm instanceof Lifecycle)) {
/*     */         try {
/* 252 */           ((Lifecycle)realm).start();
/*     */         }
/*     */         catch (LifecycleException e) {
/* 255 */           iter.remove();
/* 256 */           log.error(sm.getString("combinedRealm.realmStartFail", new Object[] {realm
/* 257 */             .getClass().getName() }), e);
/*     */         }
/*     */       }
/*     */     }
/* 261 */     super.startInternal();
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
/*     */   protected void stopInternal()
/*     */     throws LifecycleException
/*     */   {
/* 276 */     super.stopInternal();
/* 277 */     for (Realm realm : this.realms) {
/* 278 */       if ((realm instanceof Lifecycle)) {
/* 279 */         ((Lifecycle)realm).stop();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void destroyInternal()
/*     */     throws LifecycleException
/*     */   {
/* 290 */     for (Realm realm : this.realms) {
/* 291 */       if ((realm instanceof Lifecycle)) {
/* 292 */         ((Lifecycle)realm).destroy();
/*     */       }
/*     */     }
/* 295 */     super.destroyInternal();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void backgroundProcess()
/*     */   {
/* 303 */     super.backgroundProcess();
/*     */     
/* 305 */     for (Realm r : this.realms) {
/* 306 */       r.backgroundProcess();
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
/*     */   public Principal authenticate(X509Certificate[] certs)
/*     */   {
/* 319 */     Principal authenticatedUser = null;
/* 320 */     String username = null;
/* 321 */     if ((certs != null) && (certs.length > 0)) {
/* 322 */       username = certs[0].getSubjectDN().getName();
/*     */     }
/*     */     
/* 325 */     for (Realm realm : this.realms) {
/* 326 */       if (log.isDebugEnabled()) {
/* 327 */         log.debug(sm.getString("combinedRealm.authStart", new Object[] { username, realm
/* 328 */           .getClass().getName() }));
/*     */       }
/*     */       
/* 331 */       authenticatedUser = realm.authenticate(certs);
/*     */       
/* 333 */       if (authenticatedUser == null) {
/* 334 */         if (log.isDebugEnabled()) {
/* 335 */           log.debug(sm.getString("combinedRealm.authFail", new Object[] { username, realm
/* 336 */             .getClass().getName() }));
/*     */         }
/*     */       } else {
/* 339 */         if (!log.isDebugEnabled()) break;
/* 340 */         log.debug(sm.getString("combinedRealm.authSuccess", new Object[] { username, realm
/* 341 */           .getClass().getName() })); break;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 346 */     return authenticatedUser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Principal authenticate(GSSContext gssContext, boolean storeCreds)
/*     */   {
/* 354 */     if (gssContext.isEstablished()) {
/* 355 */       Principal authenticatedUser = null;
/* 356 */       String username = null;
/*     */       
/* 358 */       GSSName name = null;
/*     */       try {
/* 360 */         name = gssContext.getSrcName();
/*     */       } catch (GSSException e) {
/* 362 */         log.warn(sm.getString("realmBase.gssNameFail"), e);
/* 363 */         return null;
/*     */       }
/*     */       
/* 366 */       username = name.toString();
/*     */       
/* 368 */       for (Realm realm : this.realms) {
/* 369 */         if (log.isDebugEnabled()) {
/* 370 */           log.debug(sm.getString("combinedRealm.authStart", new Object[] { username, realm
/* 371 */             .getClass().getName() }));
/*     */         }
/*     */         
/* 374 */         authenticatedUser = realm.authenticate(gssContext, storeCreds);
/*     */         
/* 376 */         if (authenticatedUser == null) {
/* 377 */           if (log.isDebugEnabled()) {
/* 378 */             log.debug(sm.getString("combinedRealm.authFail", new Object[] { username, realm
/* 379 */               .getClass().getName() }));
/*     */           }
/*     */         } else {
/* 382 */           if (!log.isDebugEnabled()) break;
/* 383 */           log.debug(sm.getString("combinedRealm.authSuccess", new Object[] { username, realm
/* 384 */             .getClass().getName() })); break;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 389 */       return authenticatedUser;
/*     */     }
/*     */     
/*     */ 
/* 393 */     return null;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected String getName()
/*     */   {
/* 399 */     return "CombinedRealm";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getPassword(String username)
/*     */   {
/* 408 */     UnsupportedOperationException uoe = new UnsupportedOperationException(sm.getString("combinedRealm.getPassword"));
/* 409 */     log.error(sm.getString("combinedRealm.unexpectedMethod"), uoe);
/* 410 */     throw uoe;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Principal getPrincipal(String username)
/*     */   {
/* 419 */     UnsupportedOperationException uoe = new UnsupportedOperationException(sm.getString("combinedRealm.getPrincipal"));
/* 420 */     log.error(sm.getString("combinedRealm.unexpectedMethod"), uoe);
/* 421 */     throw uoe;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isAvailable()
/*     */   {
/* 427 */     for (Realm realm : this.realms) {
/* 428 */       if (!realm.isAvailable()) {
/* 429 */         return false;
/*     */       }
/*     */     }
/* 432 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\realm\CombinedRealm.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */