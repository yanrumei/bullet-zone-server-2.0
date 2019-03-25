/*     */ package org.apache.catalina.realm;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.Principal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.catalina.CredentialHandler;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.digester.Digester;
/*     */ import org.apache.tomcat.util.file.ConfigFileLoader;
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
/*     */ public class MemoryRealm
/*     */   extends RealmBase
/*     */ {
/*  47 */   private static final Log log = LogFactory.getLog(MemoryRealm.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  55 */   private static Digester digester = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected static final String name = "MemoryRealm";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  70 */   private String pathname = "conf/tomcat-users.xml";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  76 */   private final Map<String, GenericPrincipal> principals = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPathname()
/*     */   {
/*  86 */     return this.pathname;
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
/*     */   public void setPathname(String pathname)
/*     */   {
/*  99 */     this.pathname = pathname;
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
/*     */   public Principal authenticate(String username, String credentials)
/*     */   {
/* 121 */     if ((username == null) || (credentials == null)) {
/* 122 */       if (log.isDebugEnabled())
/* 123 */         log.debug(sm.getString("memoryRealm.authenticateFailure", new Object[] { username }));
/* 124 */       return null;
/*     */     }
/*     */     
/* 127 */     GenericPrincipal principal = (GenericPrincipal)this.principals.get(username);
/*     */     
/* 129 */     if ((principal == null) || (principal.getPassword() == null))
/*     */     {
/*     */ 
/* 132 */       getCredentialHandler().mutate(credentials);
/*     */       
/* 134 */       if (log.isDebugEnabled())
/* 135 */         log.debug(sm.getString("memoryRealm.authenticateFailure", new Object[] { username }));
/* 136 */       return null;
/*     */     }
/*     */     
/* 139 */     boolean validated = getCredentialHandler().matches(credentials, principal.getPassword());
/*     */     
/* 141 */     if (validated) {
/* 142 */       if (log.isDebugEnabled())
/* 143 */         log.debug(sm.getString("memoryRealm.authenticateSuccess", new Object[] { username }));
/* 144 */       return principal;
/*     */     }
/* 146 */     if (log.isDebugEnabled())
/* 147 */       log.debug(sm.getString("memoryRealm.authenticateFailure", new Object[] { username }));
/* 148 */     return null;
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
/*     */   void addUser(String username, String password, String roles)
/*     */   {
/* 166 */     ArrayList<String> list = new ArrayList();
/* 167 */     roles = roles + ",";
/*     */     for (;;) {
/* 169 */       int comma = roles.indexOf(',');
/* 170 */       if (comma < 0)
/*     */         break;
/* 172 */       String role = roles.substring(0, comma).trim();
/* 173 */       list.add(role);
/* 174 */       roles = roles.substring(comma + 1);
/*     */     }
/*     */     
/*     */ 
/* 178 */     GenericPrincipal principal = new GenericPrincipal(username, password, list);
/*     */     
/* 180 */     this.principals.put(username, principal);
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
/*     */   protected synchronized Digester getDigester()
/*     */   {
/* 194 */     if (digester == null) {
/* 195 */       digester = new Digester();
/* 196 */       digester.setValidating(false);
/*     */       try {
/* 198 */         digester.setFeature("http://apache.org/xml/features/allow-java-encodings", true);
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 202 */         log.warn(sm.getString("memoryRealm.xmlFeatureEncoding"), e);
/*     */       }
/* 204 */       digester.addRuleSet(new MemoryRuleSet());
/*     */     }
/* 206 */     return digester;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected String getName()
/*     */   {
/* 214 */     return "MemoryRealm";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getPassword(String username)
/*     */   {
/* 224 */     GenericPrincipal principal = (GenericPrincipal)this.principals.get(username);
/* 225 */     if (principal != null) {
/* 226 */       return principal.getPassword();
/*     */     }
/* 228 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Principal getPrincipal(String username)
/*     */   {
/* 240 */     return (Principal)this.principals.get(username);
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
/*     */   protected void startInternal()
/*     */     throws LifecycleException
/*     */   {
/* 257 */     String pathName = getPathname();
/* 258 */     try { InputStream is = ConfigFileLoader.getInputStream(pathName);Throwable localThrowable3 = null;
/*     */       try {
/* 260 */         if (log.isDebugEnabled()) {
/* 261 */           log.debug(sm.getString("memoryRealm.loadPath", new Object[] { pathName }));
/*     */         }
/*     */         
/* 264 */         Digester digester = getDigester();
/*     */         try {
/* 266 */           synchronized (digester) {
/* 267 */             digester.push(this);
/* 268 */             digester.parse(is);
/*     */           }
/*     */         } catch (Exception e) {
/* 271 */           throw new LifecycleException(sm.getString("memoryRealm.readXml"), e);
/*     */         } finally {
/* 273 */           digester.reset();
/*     */         }
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 258 */         localThrowable3 = localThrowable1;throw localThrowable1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 275 */         if (is != null) if (localThrowable3 != null) try { is.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else is.close();
/* 276 */       } } catch (IOException ioe) { throw new LifecycleException(sm.getString("memoryRealm.loadExist", new Object[] { pathName }), ioe);
/*     */     }
/*     */     
/* 279 */     super.startInternal();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\realm\MemoryRealm.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */