/*     */ package org.apache.tomcat.util.net.openssl;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.SSLSessionContext;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.net.SSLContext;
/*     */ import org.apache.tomcat.util.net.SSLHostConfig;
/*     */ import org.apache.tomcat.util.net.SSLHostConfigCertificate;
/*     */ import org.apache.tomcat.util.net.SSLUtilBase;
/*     */ import org.apache.tomcat.util.net.jsse.JSSEUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OpenSSLUtil
/*     */   extends SSLUtilBase
/*     */ {
/*  36 */   private static final Log log = LogFactory.getLog(OpenSSLUtil.class);
/*     */   private final JSSEUtil jsseUtil;
/*     */   
/*     */   public OpenSSLUtil(SSLHostConfigCertificate certificate)
/*     */   {
/*  41 */     super(certificate);
/*     */     
/*  43 */     if (certificate.getCertificateFile() == null)
/*     */     {
/*  45 */       this.jsseUtil = new JSSEUtil(certificate);
/*     */     }
/*     */     else {
/*  48 */       this.jsseUtil = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected Log getLog()
/*     */   {
/*  55 */     return log;
/*     */   }
/*     */   
/*     */ 
/*     */   protected Set<String> getImplementedProtocols()
/*     */   {
/*  61 */     return OpenSSLEngine.IMPLEMENTED_PROTOCOLS_SET;
/*     */   }
/*     */   
/*     */ 
/*     */   protected Set<String> getImplementedCiphers()
/*     */   {
/*  67 */     return OpenSSLEngine.AVAILABLE_CIPHER_SUITES;
/*     */   }
/*     */   
/*     */   public SSLContext createSSLContext(List<String> negotiableProtocols)
/*     */     throws Exception
/*     */   {
/*  73 */     return new OpenSSLContext(this.certificate, negotiableProtocols);
/*     */   }
/*     */   
/*     */   public KeyManager[] getKeyManagers() throws Exception
/*     */   {
/*  78 */     if (this.jsseUtil != null) {
/*  79 */       return this.jsseUtil.getKeyManagers();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  84 */     KeyManager[] managers = { new OpenSSLKeyManager(SSLHostConfig.adjustRelativePath(this.certificate.getCertificateFile()), SSLHostConfig.adjustRelativePath(this.certificate.getCertificateKeyFile())) };
/*     */     
/*  86 */     return managers;
/*     */   }
/*     */   
/*     */   public TrustManager[] getTrustManagers()
/*     */     throws Exception
/*     */   {
/*  92 */     if (this.jsseUtil != null) {
/*  93 */       return this.jsseUtil.getTrustManagers();
/*     */     }
/*  95 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void configureSessionContext(SSLSessionContext sslSessionContext)
/*     */   {
/* 101 */     if (this.jsseUtil != null) {
/* 102 */       this.jsseUtil.configureSessionContext(sslSessionContext);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\openssl\OpenSSLUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */