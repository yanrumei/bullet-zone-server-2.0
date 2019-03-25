/*     */ package org.apache.tomcat.util.net.jsse;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.net.SSLSessionManager;
/*     */ import org.apache.tomcat.util.net.SSLSupport;
/*     */ import org.apache.tomcat.util.net.openssl.ciphers.Cipher;
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
/*     */ public class JSSESupport
/*     */   implements SSLSupport, SSLSessionManager
/*     */ {
/*  51 */   private static final Log log = LogFactory.getLog(JSSESupport.class);
/*     */   
/*  53 */   private static final StringManager sm = StringManager.getManager(JSSESupport.class);
/*     */   
/*  55 */   private static final Map<String, Integer> keySizeCache = new HashMap();
/*     */   private SSLSession session;
/*     */   
/*  58 */   static { Cipher cipher; for (cipher : Cipher.values()) {
/*  59 */       for (String jsseName : cipher.getJsseNames()) {
/*  60 */         keySizeCache.put(jsseName, Integer.valueOf(cipher.getStrength_bits()));
/*     */       }
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
/*     */   public JSSESupport(SSLSession session)
/*     */   {
/*  78 */     this.session = session;
/*     */   }
/*     */   
/*     */   public String getCipherSuite()
/*     */     throws IOException
/*     */   {
/*  84 */     if (this.session == null)
/*  85 */       return null;
/*  86 */     return this.session.getCipherSuite();
/*     */   }
/*     */   
/*     */   public X509Certificate[] getPeerCertificateChain()
/*     */     throws IOException
/*     */   {
/*  92 */     if (this.session == null) {
/*  93 */       return null;
/*     */     }
/*  95 */     Certificate[] certs = null;
/*     */     try {
/*  97 */       certs = this.session.getPeerCertificates();
/*     */     } catch (Throwable t) {
/*  99 */       log.debug(sm.getString("jsseSupport.clientCertError"), t);
/* 100 */       return null;
/*     */     }
/* 102 */     if (certs == null) { return null;
/*     */     }
/* 104 */     X509Certificate[] x509Certs = new X509Certificate[certs.length];
/*     */     
/* 106 */     for (int i = 0; i < certs.length; i++) {
/* 107 */       if ((certs[i] instanceof X509Certificate))
/*     */       {
/* 109 */         x509Certs[i] = ((X509Certificate)certs[i]);
/*     */       } else {
/*     */         try {
/* 112 */           byte[] buffer = certs[i].getEncoded();
/*     */           
/* 114 */           CertificateFactory cf = CertificateFactory.getInstance("X.509");
/* 115 */           ByteArrayInputStream stream = new ByteArrayInputStream(buffer);
/*     */           
/* 117 */           x509Certs[i] = 
/* 118 */             ((X509Certificate)cf.generateCertificate(stream));
/*     */         } catch (Exception ex) {
/* 120 */           log.info(sm.getString("jseeSupport.certTranslationError", new Object[] { certs[i] }), ex);
/*     */           
/* 122 */           return null;
/*     */         }
/*     */       }
/* 125 */       if (log.isTraceEnabled())
/* 126 */         log.trace("Cert #" + i + " = " + x509Certs[i]);
/*     */     }
/* 128 */     if (x509Certs.length < 1)
/* 129 */       return null;
/* 130 */     return x509Certs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getKeySize()
/*     */     throws IOException
/*     */   {
/* 142 */     if (this.session == null) {
/* 143 */       return null;
/*     */     }
/*     */     
/* 146 */     return (Integer)keySizeCache.get(this.session.getCipherSuite());
/*     */   }
/*     */   
/*     */ 
/*     */   public String getSessionId()
/*     */     throws IOException
/*     */   {
/* 153 */     if (this.session == null) {
/* 154 */       return null;
/*     */     }
/* 156 */     byte[] ssl_session = this.session.getId();
/* 157 */     if (ssl_session == null)
/* 158 */       return null;
/* 159 */     StringBuilder buf = new StringBuilder();
/* 160 */     for (int x = 0; x < ssl_session.length; x++) {
/* 161 */       String digit = Integer.toHexString(ssl_session[x]);
/* 162 */       if (digit.length() < 2) buf.append('0');
/* 163 */       if (digit.length() > 2) digit = digit.substring(digit.length() - 2);
/* 164 */       buf.append(digit);
/*     */     }
/* 166 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public void setSession(SSLSession session)
/*     */   {
/* 171 */     this.session = session;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void invalidateSession()
/*     */   {
/* 180 */     this.session.invalidate();
/*     */   }
/*     */   
/*     */   public String getProtocol() throws IOException
/*     */   {
/* 185 */     if (this.session == null) {
/* 186 */       return null;
/*     */     }
/* 188 */     return this.session.getProtocol();
/*     */   }
/*     */   
/*     */   static void init() {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\jsse\JSSESupport.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */