/*     */ package org.apache.catalina.realm;
/*     */ 
/*     */ import java.security.Principal;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.catalina.LifecycleException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LockOutRealm
/*     */   extends CombinedRealm
/*     */ {
/*  47 */   private static final Log log = LogFactory.getLog(LockOutRealm.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final String name = "LockOutRealm";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  58 */   protected int failureCount = 5;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  64 */   protected int lockOutTime = 300;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */   protected int cacheSize = 1000;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  78 */   protected int cacheRemovalWarningTime = 3600;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  84 */   protected Map<String, LockRecord> failedUsers = null;
/*     */   
/*     */ 
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
/*  99 */     this.failedUsers = new LinkedHashMap(this.cacheSize, 0.75F, true)
/*     */     {
/*     */       private static final long serialVersionUID = 1L;
/*     */       
/*     */       protected boolean removeEldestEntry(Map.Entry<String, LockOutRealm.LockRecord> eldest)
/*     */       {
/* 105 */         if (size() > LockOutRealm.this.cacheSize)
/*     */         {
/*     */ 
/* 108 */           long timeInCache = (System.currentTimeMillis() - ((LockOutRealm.LockRecord)eldest.getValue()).getLastFailureTime()) / 1000L;
/*     */           
/* 110 */           if (timeInCache < LockOutRealm.this.cacheRemovalWarningTime) {
/* 111 */             LockOutRealm.log.warn(RealmBase.sm.getString("lockOutRealm.removeWarning", new Object[] {eldest
/* 112 */               .getKey(), Long.valueOf(timeInCache) }));
/*     */           }
/* 114 */           return true;
/*     */         }
/* 116 */         return false;
/*     */       }
/*     */       
/* 119 */     };
/* 120 */     super.startInternal();
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
/*     */   public Principal authenticate(String username, String clientDigest, String nonce, String nc, String cnonce, String qop, String realmName, String md5a2)
/*     */   {
/* 142 */     Principal authenticatedUser = super.authenticate(username, clientDigest, nonce, nc, cnonce, qop, realmName, md5a2);
/*     */     
/* 144 */     return filterLockedAccounts(username, authenticatedUser);
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
/* 158 */     Principal authenticatedUser = super.authenticate(username, credentials);
/* 159 */     return filterLockedAccounts(username, authenticatedUser);
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
/*     */   public Principal authenticate(X509Certificate[] certs)
/*     */   {
/* 172 */     String username = null;
/* 173 */     if ((certs != null) && (certs.length > 0)) {
/* 174 */       username = certs[0].getSubjectDN().getName();
/*     */     }
/*     */     
/* 177 */     Principal authenticatedUser = super.authenticate(certs);
/* 178 */     return filterLockedAccounts(username, authenticatedUser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Principal authenticate(GSSContext gssContext, boolean storeCreds)
/*     */   {
/* 187 */     if (gssContext.isEstablished()) {
/* 188 */       String username = null;
/* 189 */       GSSName name = null;
/*     */       try {
/* 191 */         name = gssContext.getSrcName();
/*     */       } catch (GSSException e) {
/* 193 */         log.warn(sm.getString("realmBase.gssNameFail"), e);
/* 194 */         return null;
/*     */       }
/*     */       
/* 197 */       username = name.toString();
/*     */       
/* 199 */       Principal authenticatedUser = super.authenticate(gssContext, storeCreds);
/*     */       
/* 201 */       return filterLockedAccounts(username, authenticatedUser);
/*     */     }
/*     */     
/*     */ 
/* 205 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Principal filterLockedAccounts(String username, Principal authenticatedUser)
/*     */   {
/* 215 */     if ((authenticatedUser == null) && (isAvailable())) {
/* 216 */       registerAuthFailure(username);
/*     */     }
/*     */     
/* 219 */     if (isLocked(username))
/*     */     {
/* 221 */       log.warn(sm.getString("lockOutRealm.authLockedUser", new Object[] { username }));
/* 222 */       return null;
/*     */     }
/*     */     
/* 225 */     if (authenticatedUser != null) {
/* 226 */       registerAuthSuccess(username);
/*     */     }
/*     */     
/* 229 */     return authenticatedUser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void unlock(String username)
/*     */   {
/* 241 */     registerAuthSuccess(username);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isLocked(String username)
/*     */   {
/* 250 */     LockRecord lockRecord = null;
/* 251 */     synchronized (this) {
/* 252 */       lockRecord = (LockRecord)this.failedUsers.get(username);
/*     */     }
/*     */     
/*     */ 
/* 256 */     if (lockRecord == null) {
/* 257 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 261 */     if (lockRecord.getFailures() >= this.failureCount)
/*     */     {
/* 263 */       if ((System.currentTimeMillis() - lockRecord.getLastFailureTime()) / 1000L < this.lockOutTime) {
/* 264 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 268 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private synchronized void registerAuthSuccess(String username)
/*     */   {
/* 278 */     this.failedUsers.remove(username);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void registerAuthFailure(String username)
/*     */   {
/* 287 */     LockRecord lockRecord = null;
/* 288 */     synchronized (this) {
/* 289 */       if (!this.failedUsers.containsKey(username)) {
/* 290 */         lockRecord = new LockRecord();
/* 291 */         this.failedUsers.put(username, lockRecord);
/*     */       } else {
/* 293 */         lockRecord = (LockRecord)this.failedUsers.get(username);
/* 294 */         if (lockRecord.getFailures() >= this.failureCount)
/*     */         {
/* 296 */           if ((System.currentTimeMillis() - lockRecord.getLastFailureTime()) / 1000L > this.lockOutTime)
/*     */           {
/*     */ 
/*     */ 
/* 300 */             lockRecord.setFailures(0); }
/*     */         }
/*     */       }
/*     */     }
/* 304 */     lockRecord.registerFailure();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getFailureCount()
/*     */   {
/* 314 */     return this.failureCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFailureCount(int failureCount)
/*     */   {
/* 324 */     this.failureCount = failureCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getLockOutTime()
/*     */   {
/* 333 */     return this.lockOutTime;
/*     */   }
/*     */   
/*     */ 
/*     */   protected String getName()
/*     */   {
/* 339 */     return "LockOutRealm";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLockOutTime(int lockOutTime)
/*     */   {
/* 348 */     this.lockOutTime = lockOutTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getCacheSize()
/*     */   {
/* 358 */     return this.cacheSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCacheSize(int cacheSize)
/*     */   {
/* 368 */     this.cacheSize = cacheSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getCacheRemovalWarningTime()
/*     */   {
/* 379 */     return this.cacheRemovalWarningTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCacheRemovalWarningTime(int cacheRemovalWarningTime)
/*     */   {
/* 390 */     this.cacheRemovalWarningTime = cacheRemovalWarningTime;
/*     */   }
/*     */   
/*     */   protected static class LockRecord
/*     */   {
/* 395 */     private final AtomicInteger failures = new AtomicInteger(0);
/* 396 */     private long lastFailureTime = 0L;
/*     */     
/*     */     public int getFailures() {
/* 399 */       return this.failures.get();
/*     */     }
/*     */     
/*     */     public void setFailures(int theFailures) {
/* 403 */       this.failures.set(theFailures);
/*     */     }
/*     */     
/*     */     public long getLastFailureTime() {
/* 407 */       return this.lastFailureTime;
/*     */     }
/*     */     
/*     */     public void registerFailure() {
/* 411 */       this.failures.incrementAndGet();
/* 412 */       this.lastFailureTime = System.currentTimeMillis();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\realm\LockOutRealm.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */