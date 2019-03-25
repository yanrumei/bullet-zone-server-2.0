/*     */ package org.apache.tomcat.util.net;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.KeyStore;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ public abstract class SSLUtilBase
/*     */   implements SSLUtil
/*     */ {
/*  38 */   private static final Log log = LogFactory.getLog(SSLUtilBase.class);
/*  39 */   private static final StringManager sm = StringManager.getManager(SSLUtilBase.class);
/*     */   
/*     */   protected final SSLHostConfigCertificate certificate;
/*     */   
/*     */   private final String[] enabledProtocols;
/*     */   private final String[] enabledCiphers;
/*     */   
/*     */   protected SSLUtilBase(SSLHostConfigCertificate certificate)
/*     */   {
/*  48 */     this.certificate = certificate;
/*  49 */     SSLHostConfig sslHostConfig = certificate.getSSLHostConfig();
/*     */     
/*     */ 
/*  52 */     Set<String> configuredProtocols = sslHostConfig.getProtocols();
/*  53 */     Set<String> implementedProtocols = getImplementedProtocols();
/*     */     
/*  55 */     List<String> enabledProtocols = getEnabled("protocols", getLog(), true, configuredProtocols, implementedProtocols);
/*  56 */     if (enabledProtocols.contains("SSLv3")) {
/*  57 */       log.warn(sm.getString("jsse.ssl3"));
/*     */     }
/*  59 */     this.enabledProtocols = ((String[])enabledProtocols.toArray(new String[enabledProtocols.size()]));
/*     */     
/*     */ 
/*  62 */     List<String> configuredCiphers = sslHostConfig.getJsseCipherNames();
/*  63 */     Set<String> implementedCiphers = getImplementedCiphers();
/*     */     
/*  65 */     List<String> enabledCiphers = getEnabled("ciphers", getLog(), false, configuredCiphers, implementedCiphers);
/*  66 */     this.enabledCiphers = ((String[])enabledCiphers.toArray(new String[enabledCiphers.size()]));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static <T> List<T> getEnabled(String name, Log log, boolean warnOnSkip, Collection<T> configured, Collection<T> implemented)
/*     */   {
/*  73 */     List<T> enabled = new ArrayList();
/*     */     
/*  75 */     if (implemented.size() == 0)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  81 */       enabled.addAll(configured);
/*     */     } else {
/*  83 */       enabled.addAll(configured);
/*  84 */       enabled.retainAll(implemented);
/*     */       
/*  86 */       if (enabled.isEmpty())
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*  91 */         throw new IllegalArgumentException(sm.getString("sslUtilBase.noneSupported", new Object[] { name, configured }));
/*     */       }
/*  93 */       if (log.isDebugEnabled()) {
/*  94 */         log.debug(sm.getString("sslUtilBase.active", new Object[] { name, enabled }));
/*     */       }
/*  96 */       if (((log.isDebugEnabled()) || (warnOnSkip)) && 
/*  97 */         (enabled.size() != configured.size())) {
/*  98 */         List<T> skipped = new ArrayList();
/*  99 */         skipped.addAll(configured);
/* 100 */         skipped.removeAll(enabled);
/* 101 */         String msg = sm.getString("sslUtilBase.skipped", new Object[] { name, skipped });
/* 102 */         if (warnOnSkip) {
/* 103 */           log.warn(msg);
/*     */         } else {
/* 105 */           log.debug(msg);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 111 */     return enabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static KeyStore getStore(String type, String provider, String path, String pass)
/*     */     throws IOException
/*     */   {
/* 121 */     ks = null;
/* 122 */     InputStream istream = null;
/*     */     try {
/* 124 */       if (provider == null) {
/* 125 */         ks = KeyStore.getInstance(type);
/*     */       } else {
/* 127 */         ks = KeyStore.getInstance(type, provider);
/*     */       }
/* 129 */       if (((!"PKCS11".equalsIgnoreCase(type)) && 
/* 130 */         (!"".equalsIgnoreCase(path))) || 
/* 131 */         ("NONE".equalsIgnoreCase(path))) {
/* 132 */         istream = ConfigFileLoader.getInputStream(path);
/*     */       }
/*     */       
/* 135 */       char[] storePass = null;
/* 136 */       if ((pass != null) && (!"".equals(pass))) {
/* 137 */         storePass = pass.toCharArray();
/*     */       }
/* 139 */       ks.load(istream, storePass);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       String msg;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 163 */       return ks;
/*     */     }
/*     */     catch (FileNotFoundException fnfe)
/*     */     {
/* 141 */       log.error(sm.getString("jsse.keystore_load_failed", new Object[] { type, path, fnfe
/* 142 */         .getMessage() }), fnfe);
/* 143 */       throw fnfe;
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/* 147 */       throw ioe;
/*     */     } catch (Exception ex) {
/* 149 */       msg = sm.getString("jsse.keystore_load_failed", new Object[] { type, path, ex
/* 150 */         .getMessage() });
/* 151 */       log.error(msg, ex);
/* 152 */       throw new IOException(msg);
/*     */     } finally {
/* 154 */       if (istream != null) {
/*     */         try {
/* 156 */           istream.close();
/*     */         }
/*     */         catch (IOException localIOException2) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getEnabledProtocols()
/*     */   {
/* 169 */     return this.enabledProtocols;
/*     */   }
/*     */   
/*     */   public String[] getEnabledCiphers()
/*     */   {
/* 174 */     return this.enabledCiphers;
/*     */   }
/*     */   
/*     */   protected abstract Set<String> getImplementedProtocols();
/*     */   
/*     */   protected abstract Set<String> getImplementedCiphers();
/*     */   
/*     */   protected abstract Log getLog();
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\SSLUtilBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */