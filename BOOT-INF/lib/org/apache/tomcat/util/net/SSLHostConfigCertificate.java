/*     */ package org.apache.tomcat.util.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.security.KeyStore;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
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
/*     */ public class SSLHostConfigCertificate
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  36 */   private static final Log log = LogFactory.getLog(SSLHostConfigCertificate.class);
/*  37 */   private static final StringManager sm = StringManager.getManager(SSLHostConfigCertificate.class);
/*     */   
/*  39 */   public static final Type DEFAULT_TYPE = Type.UNDEFINED;
/*     */   
/*     */ 
/*  42 */   static final String DEFAULT_KEYSTORE_PROVIDER = System.getProperty("javax.net.ssl.keyStoreProvider");
/*     */   
/*  44 */   static final String DEFAULT_KEYSTORE_TYPE = System.getProperty("javax.net.ssl.keyStoreType", "JKS");
/*     */   
/*     */ 
/*     */   private ObjectName oname;
/*     */   
/*     */ 
/*     */   private transient SSLContext sslContext;
/*     */   
/*     */ 
/*     */   private final SSLHostConfig sslHostConfig;
/*     */   
/*     */   private final Type type;
/*     */   
/*  57 */   private String certificateKeyPassword = null;
/*     */   
/*     */   private String certificateKeyAlias;
/*     */   
/*  61 */   private String certificateKeystorePassword = "changeit";
/*  62 */   private String certificateKeystoreFile = System.getProperty("user.home") + "/.keystore";
/*  63 */   private String certificateKeystoreProvider = DEFAULT_KEYSTORE_PROVIDER;
/*  64 */   private String certificateKeystoreType = DEFAULT_KEYSTORE_TYPE;
/*  65 */   private transient KeyStore certificateKeystore = null;
/*     */   
/*     */   private String certificateChainFile;
/*     */   
/*     */   private String certificateFile;
/*     */   
/*     */   private String certificateKeyFile;
/*     */   
/*  73 */   private StoreType storeType = null;
/*     */   
/*     */   public SSLHostConfigCertificate() {
/*  76 */     this(null, Type.UNDEFINED);
/*     */   }
/*     */   
/*     */   public SSLHostConfigCertificate(SSLHostConfig sslHostConfig, Type type) {
/*  80 */     this.sslHostConfig = sslHostConfig;
/*  81 */     this.type = type;
/*     */   }
/*     */   
/*     */   public SSLContext getSslContext()
/*     */   {
/*  86 */     return this.sslContext;
/*     */   }
/*     */   
/*     */   public void setSslContext(SSLContext sslContext)
/*     */   {
/*  91 */     this.sslContext = sslContext;
/*     */   }
/*     */   
/*     */   public SSLHostConfig getSSLHostConfig()
/*     */   {
/*  96 */     return this.sslHostConfig;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ObjectName getObjectName()
/*     */   {
/* 103 */     return this.oname;
/*     */   }
/*     */   
/*     */   public void setObjectName(ObjectName oname)
/*     */   {
/* 108 */     this.oname = oname;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Type getType()
/*     */   {
/* 115 */     return this.type;
/*     */   }
/*     */   
/*     */   public String getCertificateKeyPassword()
/*     */   {
/* 120 */     return this.certificateKeyPassword;
/*     */   }
/*     */   
/*     */   public void setCertificateKeyPassword(String certificateKeyPassword)
/*     */   {
/* 125 */     this.certificateKeyPassword = certificateKeyPassword;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setCertificateKeyAlias(String certificateKeyAlias)
/*     */   {
/* 132 */     this.sslHostConfig.setProperty("Certificate.certificateKeyAlias", SSLHostConfig.Type.JSSE);
/*     */     
/* 134 */     this.certificateKeyAlias = certificateKeyAlias;
/*     */   }
/*     */   
/*     */   public String getCertificateKeyAlias()
/*     */   {
/* 139 */     return this.certificateKeyAlias;
/*     */   }
/*     */   
/*     */   public void setCertificateKeystoreFile(String certificateKeystoreFile)
/*     */   {
/* 144 */     this.sslHostConfig.setProperty("Certificate.certificateKeystoreFile", SSLHostConfig.Type.JSSE);
/*     */     
/* 146 */     setStoreType("Certificate.certificateKeystoreFile", StoreType.KEYSTORE);
/* 147 */     this.certificateKeystoreFile = certificateKeystoreFile;
/*     */   }
/*     */   
/*     */   public String getCertificateKeystoreFile()
/*     */   {
/* 152 */     return this.certificateKeystoreFile;
/*     */   }
/*     */   
/*     */   public void setCertificateKeystorePassword(String certificateKeystorePassword)
/*     */   {
/* 157 */     this.sslHostConfig.setProperty("Certificate.certificateKeystorePassword", SSLHostConfig.Type.JSSE);
/*     */     
/* 159 */     setStoreType("Certificate.certificateKeystorePassword", StoreType.KEYSTORE);
/* 160 */     this.certificateKeystorePassword = certificateKeystorePassword;
/*     */   }
/*     */   
/*     */   public String getCertificateKeystorePassword()
/*     */   {
/* 165 */     return this.certificateKeystorePassword;
/*     */   }
/*     */   
/*     */   public void setCertificateKeystoreProvider(String certificateKeystoreProvider)
/*     */   {
/* 170 */     this.sslHostConfig.setProperty("Certificate.certificateKeystoreProvider", SSLHostConfig.Type.JSSE);
/*     */     
/* 172 */     setStoreType("Certificate.certificateKeystoreProvider", StoreType.KEYSTORE);
/* 173 */     this.certificateKeystoreProvider = certificateKeystoreProvider;
/*     */   }
/*     */   
/*     */   public String getCertificateKeystoreProvider()
/*     */   {
/* 178 */     return this.certificateKeystoreProvider;
/*     */   }
/*     */   
/*     */   public void setCertificateKeystoreType(String certificateKeystoreType)
/*     */   {
/* 183 */     this.sslHostConfig.setProperty("Certificate.certificateKeystoreType", SSLHostConfig.Type.JSSE);
/*     */     
/* 185 */     setStoreType("Certificate.certificateKeystoreType", StoreType.KEYSTORE);
/* 186 */     this.certificateKeystoreType = certificateKeystoreType;
/*     */   }
/*     */   
/*     */   public String getCertificateKeystoreType()
/*     */   {
/* 191 */     return this.certificateKeystoreType;
/*     */   }
/*     */   
/*     */   public void setCertificateKeystore(KeyStore certificateKeystore)
/*     */   {
/* 196 */     this.certificateKeystore = certificateKeystore;
/*     */   }
/*     */   
/*     */   public KeyStore getCertificateKeystore() throws IOException
/*     */   {
/* 201 */     KeyStore result = this.certificateKeystore;
/*     */     
/* 203 */     if ((result == null) && (this.storeType == StoreType.KEYSTORE)) {
/* 204 */       result = SSLUtilBase.getStore(getCertificateKeystoreType(), 
/* 205 */         getCertificateKeystoreProvider(), getCertificateKeystoreFile(), 
/* 206 */         getCertificateKeystorePassword());
/*     */     }
/*     */     
/* 209 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setCertificateChainFile(String certificateChainFile)
/*     */   {
/* 216 */     setStoreType("Certificate.certificateChainFile", StoreType.PEM);
/* 217 */     this.certificateChainFile = certificateChainFile;
/*     */   }
/*     */   
/*     */   public String getCertificateChainFile()
/*     */   {
/* 222 */     return this.certificateChainFile;
/*     */   }
/*     */   
/*     */   public void setCertificateFile(String certificateFile)
/*     */   {
/* 227 */     setStoreType("Certificate.certificateFile", StoreType.PEM);
/* 228 */     this.certificateFile = certificateFile;
/*     */   }
/*     */   
/*     */   public String getCertificateFile()
/*     */   {
/* 233 */     return this.certificateFile;
/*     */   }
/*     */   
/*     */   public void setCertificateKeyFile(String certificateKeyFile)
/*     */   {
/* 238 */     setStoreType("Certificate.certificateKeyFile", StoreType.PEM);
/* 239 */     this.certificateKeyFile = certificateKeyFile;
/*     */   }
/*     */   
/*     */   public String getCertificateKeyFile()
/*     */   {
/* 244 */     return this.certificateKeyFile;
/*     */   }
/*     */   
/*     */   private void setStoreType(String name, StoreType type)
/*     */   {
/* 249 */     if (this.storeType == null) {
/* 250 */       this.storeType = type;
/* 251 */     } else if (this.storeType != type) {
/* 252 */       log.warn(sm.getString("sslHostConfigCertificate.mismatch", new Object[] { name, this.sslHostConfig
/* 253 */         .getHostName(), type, this.storeType }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static enum Type
/*     */   {
/* 261 */     UNDEFINED(new Authentication[0]), 
/* 262 */     RSA(new Authentication[] { Authentication.RSA }), 
/* 263 */     DSA(new Authentication[] { Authentication.DSS }), 
/* 264 */     EC(new Authentication[] { Authentication.ECDH, Authentication.ECDSA });
/*     */     
/*     */     private final Set<Authentication> compatibleAuthentications;
/*     */     
/*     */     private Type(Authentication... authentications) {
/* 269 */       this.compatibleAuthentications = new HashSet();
/* 270 */       if (authentications != null) {
/* 271 */         for (Authentication authentication : authentications) {
/* 272 */           this.compatibleAuthentications.add(authentication);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean isCompatibleWith(Authentication au) {
/* 278 */       return this.compatibleAuthentications.contains(au);
/*     */     }
/*     */   }
/*     */   
/*     */   private static enum StoreType {
/* 283 */     KEYSTORE, 
/* 284 */     PEM;
/*     */     
/*     */     private StoreType() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\SSLHostConfigCertificate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */