/*     */ package org.apache.tomcat.util.net.openssl;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.math.BigInteger;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.Principal;
/*     */ import java.security.PublicKey;
/*     */ import java.security.SignatureException;
/*     */ import java.security.cert.CertificateEncodingException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateExpiredException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.CertificateNotYetValidException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Date;
/*     */ import java.util.Set;
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
/*     */ final class OpenSSLX509Certificate
/*     */   extends X509Certificate
/*     */ {
/*     */   private final byte[] bytes;
/*     */   private X509Certificate wrapped;
/*     */   
/*     */   public OpenSSLX509Certificate(byte[] bytes)
/*     */   {
/*  41 */     this.bytes = bytes;
/*     */   }
/*     */   
/*     */   public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException
/*     */   {
/*  46 */     unwrap().checkValidity();
/*     */   }
/*     */   
/*     */   public void checkValidity(Date date) throws CertificateExpiredException, CertificateNotYetValidException
/*     */   {
/*  51 */     unwrap().checkValidity(date);
/*     */   }
/*     */   
/*     */   public int getVersion()
/*     */   {
/*  56 */     return unwrap().getVersion();
/*     */   }
/*     */   
/*     */   public BigInteger getSerialNumber()
/*     */   {
/*  61 */     return unwrap().getSerialNumber();
/*     */   }
/*     */   
/*     */   public Principal getIssuerDN()
/*     */   {
/*  66 */     return unwrap().getIssuerDN();
/*     */   }
/*     */   
/*     */   public Principal getSubjectDN()
/*     */   {
/*  71 */     return unwrap().getSubjectDN();
/*     */   }
/*     */   
/*     */   public Date getNotBefore()
/*     */   {
/*  76 */     return unwrap().getNotBefore();
/*     */   }
/*     */   
/*     */   public Date getNotAfter()
/*     */   {
/*  81 */     return unwrap().getNotAfter();
/*     */   }
/*     */   
/*     */   public byte[] getTBSCertificate() throws CertificateEncodingException
/*     */   {
/*  86 */     return unwrap().getTBSCertificate();
/*     */   }
/*     */   
/*     */   public byte[] getSignature()
/*     */   {
/*  91 */     return unwrap().getSignature();
/*     */   }
/*     */   
/*     */   public String getSigAlgName()
/*     */   {
/*  96 */     return unwrap().getSigAlgName();
/*     */   }
/*     */   
/*     */   public String getSigAlgOID()
/*     */   {
/* 101 */     return unwrap().getSigAlgOID();
/*     */   }
/*     */   
/*     */   public byte[] getSigAlgParams()
/*     */   {
/* 106 */     return unwrap().getSigAlgParams();
/*     */   }
/*     */   
/*     */   public boolean[] getIssuerUniqueID()
/*     */   {
/* 111 */     return unwrap().getIssuerUniqueID();
/*     */   }
/*     */   
/*     */   public boolean[] getSubjectUniqueID()
/*     */   {
/* 116 */     return unwrap().getSubjectUniqueID();
/*     */   }
/*     */   
/*     */   public boolean[] getKeyUsage()
/*     */   {
/* 121 */     return unwrap().getKeyUsage();
/*     */   }
/*     */   
/*     */   public int getBasicConstraints()
/*     */   {
/* 126 */     return unwrap().getBasicConstraints();
/*     */   }
/*     */   
/*     */   public byte[] getEncoded()
/*     */   {
/* 131 */     return (byte[])this.bytes.clone();
/*     */   }
/*     */   
/*     */ 
/*     */   public void verify(PublicKey key)
/*     */     throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
/*     */   {
/* 138 */     unwrap().verify(key);
/*     */   }
/*     */   
/*     */ 
/*     */   public void verify(PublicKey key, String sigProvider)
/*     */     throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
/*     */   {
/* 145 */     unwrap().verify(key, sigProvider);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 150 */     return unwrap().toString();
/*     */   }
/*     */   
/*     */   public PublicKey getPublicKey()
/*     */   {
/* 155 */     return unwrap().getPublicKey();
/*     */   }
/*     */   
/*     */   public boolean hasUnsupportedCriticalExtension()
/*     */   {
/* 160 */     return unwrap().hasUnsupportedCriticalExtension();
/*     */   }
/*     */   
/*     */   public Set<String> getCriticalExtensionOIDs()
/*     */   {
/* 165 */     return unwrap().getCriticalExtensionOIDs();
/*     */   }
/*     */   
/*     */   public Set<String> getNonCriticalExtensionOIDs()
/*     */   {
/* 170 */     return unwrap().getNonCriticalExtensionOIDs();
/*     */   }
/*     */   
/*     */   public byte[] getExtensionValue(String oid)
/*     */   {
/* 175 */     return unwrap().getExtensionValue(oid);
/*     */   }
/*     */   
/*     */   private X509Certificate unwrap() {
/* 179 */     X509Certificate wrapped = this.wrapped;
/* 180 */     if (wrapped == null) {
/*     */       try {
/* 182 */         wrapped = this.wrapped = (X509Certificate)OpenSSLContext.X509_CERT_FACTORY.generateCertificate(new ByteArrayInputStream(this.bytes));
/*     */       }
/*     */       catch (CertificateException e) {
/* 185 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/* 188 */     return wrapped;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\openssl\OpenSSLX509Certificate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */