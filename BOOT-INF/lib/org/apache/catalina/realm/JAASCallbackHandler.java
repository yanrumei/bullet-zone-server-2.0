/*     */ package org.apache.catalina.realm;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import javax.security.auth.callback.Callback;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.auth.callback.NameCallback;
/*     */ import javax.security.auth.callback.PasswordCallback;
/*     */ import javax.security.auth.callback.TextInputCallback;
/*     */ import javax.security.auth.callback.UnsupportedCallbackException;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.CredentialHandler;
/*     */ import org.apache.juli.logging.Log;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JAASCallbackHandler
/*     */   implements CallbackHandler
/*     */ {
/*     */   public JAASCallbackHandler(JAASRealm realm, String username, String password)
/*     */   {
/*  66 */     this(realm, username, password, null, null, null, null, null, null, null);
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
/*     */   public JAASCallbackHandler(JAASRealm realm, String username, String password, String nonce, String nc, String cnonce, String qop, String realmName, String md5a2, String authMethod)
/*     */   {
/*  90 */     this.realm = realm;
/*  91 */     this.username = username;
/*     */     
/*  93 */     if (realm.hasMessageDigest()) {
/*  94 */       this.password = realm.getCredentialHandler().mutate(password);
/*     */     }
/*     */     else {
/*  97 */       this.password = password;
/*     */     }
/*  99 */     this.nonce = nonce;
/* 100 */     this.nc = nc;
/* 101 */     this.cnonce = cnonce;
/* 102 */     this.qop = qop;
/* 103 */     this.realmName = realmName;
/* 104 */     this.md5a2 = md5a2;
/* 105 */     this.authMethod = authMethod;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 113 */   protected static final StringManager sm = StringManager.getManager(JAASCallbackHandler.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String password;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JAASRealm realm;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String username;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String nonce;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String nc;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String cnonce;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String qop;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String realmName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String md5a2;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String authMethod;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void handle(Callback[] callbacks)
/*     */     throws IOException, UnsupportedCallbackException
/*     */   {
/* 186 */     for (int i = 0; i < callbacks.length; i++)
/*     */     {
/* 188 */       if ((callbacks[i] instanceof NameCallback)) {
/* 189 */         if (this.realm.getContainer().getLogger().isTraceEnabled())
/* 190 */           this.realm.getContainer().getLogger().trace(sm.getString("jaasCallback.username", new Object[] { this.username }));
/* 191 */         ((NameCallback)callbacks[i]).setName(this.username);
/* 192 */       } else if ((callbacks[i] instanceof PasswordCallback)) { char[] passwordcontents;
/*     */         char[] passwordcontents;
/* 194 */         if (this.password != null) {
/* 195 */           passwordcontents = this.password.toCharArray();
/*     */         } else {
/* 197 */           passwordcontents = new char[0];
/*     */         }
/*     */         
/* 200 */         ((PasswordCallback)callbacks[i]).setPassword(passwordcontents);
/* 201 */       } else if ((callbacks[i] instanceof TextInputCallback)) {
/* 202 */         TextInputCallback cb = (TextInputCallback)callbacks[i];
/* 203 */         if (cb.getPrompt().equals("nonce")) {
/* 204 */           cb.setText(this.nonce);
/* 205 */         } else if (cb.getPrompt().equals("nc")) {
/* 206 */           cb.setText(this.nc);
/* 207 */         } else if (cb.getPrompt().equals("cnonce")) {
/* 208 */           cb.setText(this.cnonce);
/* 209 */         } else if (cb.getPrompt().equals("qop")) {
/* 210 */           cb.setText(this.qop);
/* 211 */         } else if (cb.getPrompt().equals("realmName")) {
/* 212 */           cb.setText(this.realmName);
/* 213 */         } else if (cb.getPrompt().equals("md5a2")) {
/* 214 */           cb.setText(this.md5a2);
/* 215 */         } else if (cb.getPrompt().equals("authMethod")) {
/* 216 */           cb.setText(this.authMethod);
/* 217 */         } else if (cb.getPrompt().equals("catalinaBase")) {
/* 218 */           cb.setText(this.realm.getContainer().getCatalinaBase().getAbsolutePath());
/*     */         } else {
/* 220 */           throw new UnsupportedCallbackException(callbacks[i]);
/*     */         }
/*     */       } else {
/* 223 */         throw new UnsupportedCallbackException(callbacks[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\realm\JAASCallbackHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */