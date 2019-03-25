/*     */ package org.apache.catalina.authenticator;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.security.Principal;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.catalina.Session;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SingleSignOnEntry
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  49 */   private String authType = null;
/*     */   
/*  51 */   private String password = null;
/*     */   
/*     */ 
/*  54 */   private transient Principal principal = null;
/*     */   
/*  56 */   private final ConcurrentMap<SingleSignOnSessionKey, SingleSignOnSessionKey> sessionKeys = new ConcurrentHashMap();
/*     */   
/*     */ 
/*  59 */   private String username = null;
/*     */   
/*  61 */   private boolean canReauthenticate = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SingleSignOnEntry(Principal principal, String authType, String username, String password)
/*     */   {
/*  78 */     updateCredentials(principal, authType, username, password);
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
/*     */   public void addSession(SingleSignOn sso, String ssoId, Session session)
/*     */   {
/*  93 */     SingleSignOnSessionKey key = new SingleSignOnSessionKey(session);
/*  94 */     SingleSignOnSessionKey currentKey = (SingleSignOnSessionKey)this.sessionKeys.putIfAbsent(key, key);
/*  95 */     if (currentKey == null)
/*     */     {
/*  97 */       session.addSessionListener(sso.getSessionListener(ssoId));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeSession(Session session)
/*     */   {
/* 108 */     SingleSignOnSessionKey key = new SingleSignOnSessionKey(session);
/* 109 */     this.sessionKeys.remove(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<SingleSignOnSessionKey> findSessions()
/*     */   {
/* 119 */     return this.sessionKeys.keySet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getAuthType()
/*     */   {
/* 129 */     return this.authType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getCanReauthenticate()
/*     */   {
/* 140 */     return this.canReauthenticate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPassword()
/*     */   {
/* 151 */     return this.password;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Principal getPrincipal()
/*     */   {
/* 161 */     return this.principal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUsername()
/*     */   {
/* 172 */     return this.username;
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
/*     */   public synchronized void updateCredentials(Principal principal, String authType, String username, String password)
/*     */   {
/* 189 */     this.principal = principal;
/* 190 */     this.authType = authType;
/* 191 */     this.username = username;
/* 192 */     this.password = password;
/*     */     
/* 194 */     this.canReauthenticate = (("BASIC".equals(authType)) || ("FORM".equals(authType)));
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException
/*     */   {
/* 199 */     out.defaultWriteObject();
/* 200 */     if ((this.principal instanceof Serializable)) {
/* 201 */       out.writeBoolean(true);
/* 202 */       out.writeObject(this.principal);
/*     */     } else {
/* 204 */       out.writeBoolean(false);
/*     */     }
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
/*     */   {
/* 210 */     in.defaultReadObject();
/* 211 */     boolean hasPrincipal = in.readBoolean();
/* 212 */     if (hasPrincipal) {
/* 213 */       this.principal = ((Principal)in.readObject());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\authenticator\SingleSignOnEntry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */