/*     */ package org.apache.tomcat.util.net;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
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
/*     */ public class AprSSLSupport
/*     */   implements SSLSupport
/*     */ {
/*     */   private final AprEndpoint.AprSocketWrapper socketWrapper;
/*     */   private final String clientCertProvider;
/*     */   
/*     */   public AprSSLSupport(AprEndpoint.AprSocketWrapper socketWrapper, String clientCertProvider)
/*     */   {
/*  39 */     this.socketWrapper = socketWrapper;
/*  40 */     this.clientCertProvider = clientCertProvider;
/*     */   }
/*     */   
/*     */   public String getCipherSuite() throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  47 */       return this.socketWrapper.getSSLInfoS(2);
/*     */     } catch (Exception e) {
/*  49 */       throw new IOException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public X509Certificate[] getPeerCertificateChain()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  60 */       int certLength = this.socketWrapper.getSSLInfoI(1024);
/*  61 */       byte[] clientCert = this.socketWrapper.getSSLInfoB(263);
/*  62 */       X509Certificate[] certs = null;
/*     */       
/*  64 */       if (clientCert != null) {
/*  65 */         if (certLength < 0) {
/*  66 */           certLength = 0;
/*     */         }
/*  68 */         certs = new X509Certificate[certLength + 1];
/*     */         CertificateFactory cf;
/*  70 */         CertificateFactory cf; if (this.clientCertProvider == null) {
/*  71 */           cf = CertificateFactory.getInstance("X.509");
/*     */         } else {
/*  73 */           cf = CertificateFactory.getInstance("X.509", this.clientCertProvider);
/*     */         }
/*  75 */         certs[0] = ((X509Certificate)cf.generateCertificate(new ByteArrayInputStream(clientCert)));
/*  76 */         for (int i = 0; i < certLength; i++) {
/*  77 */           byte[] data = this.socketWrapper.getSSLInfoB(1024 + i);
/*  78 */           certs[(i + 1)] = ((X509Certificate)cf.generateCertificate(new ByteArrayInputStream(data)));
/*     */         }
/*     */       }
/*  81 */       return certs;
/*     */     } catch (Exception e) {
/*  83 */       throw new IOException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public Integer getKeySize() throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  91 */       return Integer.valueOf(this.socketWrapper.getSSLInfoI(3));
/*     */     } catch (Exception e) {
/*  93 */       throw new IOException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getSessionId() throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 101 */       return this.socketWrapper.getSSLInfoS(1);
/*     */     } catch (Exception e) {
/* 103 */       throw new IOException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getProtocol() throws IOException
/*     */   {
/*     */     try {
/* 110 */       return this.socketWrapper.getSSLInfoS(7);
/*     */     } catch (Exception e) {
/* 112 */       throw new IOException(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\AprSSLSupport.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */