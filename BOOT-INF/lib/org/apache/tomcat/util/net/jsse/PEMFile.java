/*     */ package org.apache.tomcat.util.net.jsse;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.security.spec.KeySpec;
/*     */ import java.security.spec.PKCS8EncodedKeySpec;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.EncryptedPrivateKeyInfo;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.crypto.SecretKeyFactory;
/*     */ import javax.crypto.spec.PBEKeySpec;
/*     */ import org.apache.tomcat.util.codec.binary.Base64;
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
/*     */ class PEMFile
/*     */ {
/*  52 */   private static final StringManager sm = StringManager.getManager(PEMFile.class);
/*     */   
/*     */   private String filename;
/*  55 */   private List<X509Certificate> certificates = new ArrayList();
/*     */   private PrivateKey privateKey;
/*     */   
/*     */   public List<X509Certificate> getCertificates() {
/*  59 */     return this.certificates;
/*     */   }
/*     */   
/*     */   public PrivateKey getPrivateKey() {
/*  63 */     return this.privateKey;
/*     */   }
/*     */   
/*     */   public PEMFile(String filename) throws IOException, GeneralSecurityException {
/*  67 */     this(filename, null);
/*     */   }
/*     */   
/*     */   public PEMFile(String filename, String password) throws IOException, GeneralSecurityException {
/*  71 */     this.filename = filename;
/*     */     
/*  73 */     List<Part> parts = new ArrayList();
/*  74 */     BufferedReader in = new BufferedReader(new FileReader(filename));Throwable localThrowable3 = null;
/*  75 */     try { Part part = null;
/*     */       
/*  77 */       while ((??? = in.readLine()) != null) {
/*  78 */         if (???.startsWith("-----BEGIN ")) {
/*  79 */           part = new Part(null);
/*  80 */           part.type = ???.substring("-----BEGIN ".length(), ???.length() - 5).trim();
/*  81 */         } else if (???.startsWith("-----END ")) {
/*  82 */           parts.add(part);
/*  83 */           part = null;
/*  84 */         } else if ((part != null) && (!???.contains(":")) && (!???.startsWith(" "))) {
/*  85 */           part.content += ???;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/*  74 */       localThrowable3 = ???;throw ???;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  88 */       if (in != null) if (localThrowable3 != null) try { in.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else in.close();
/*     */     }
/*  90 */     for (Part part : parts)
/*  91 */       switch (part.type) {
/*     */       case "PRIVATE KEY": 
/*  93 */         this.privateKey = part.toPrivateKey(null);
/*  94 */         break;
/*     */       case "ENCRYPTED PRIVATE KEY": 
/*  96 */         this.privateKey = part.toPrivateKey(password);
/*  97 */         break;
/*     */       case "CERTIFICATE": 
/*     */       case "X509 CERTIFICATE": 
/* 100 */         this.certificates.add(part.toCertificate());
/*     */       }
/*     */   }
/*     */   
/*     */   private class Part {
/*     */     public static final String BEGIN_BOUNDARY = "-----BEGIN ";
/*     */     public static final String END_BOUNDARY = "-----END ";
/*     */     public String type;
/*     */     
/*     */     private Part() {}
/*     */     
/* 111 */     public String content = "";
/*     */     
/*     */     private byte[] decode() {
/* 114 */       return Base64.decodeBase64(this.content);
/*     */     }
/*     */     
/*     */     public X509Certificate toCertificate() throws CertificateException {
/* 118 */       CertificateFactory factory = CertificateFactory.getInstance("X.509");
/* 119 */       return (X509Certificate)factory.generateCertificate(new ByteArrayInputStream(decode()));
/*     */     }
/*     */     
/*     */     public PrivateKey toPrivateKey(String password) throws GeneralSecurityException, IOException {
/*     */       KeySpec keySpec;
/*     */       KeySpec keySpec;
/* 125 */       if (password == null) {
/* 126 */         keySpec = new PKCS8EncodedKeySpec(decode());
/*     */       } else {
/* 128 */         EncryptedPrivateKeyInfo privateKeyInfo = new EncryptedPrivateKeyInfo(decode());
/* 129 */         secretKeyFactory = SecretKeyFactory.getInstance(privateKeyInfo.getAlgName());
/* 130 */         secretKey = secretKeyFactory.generateSecret(new PBEKeySpec(password.toCharArray()));
/*     */         
/* 132 */         cipher = Cipher.getInstance(privateKeyInfo.getAlgName());
/* 133 */         cipher.init(2, secretKey, privateKeyInfo.getAlgParameters());
/*     */         
/* 135 */         keySpec = privateKeyInfo.getKeySpec(cipher);
/*     */       }
/*     */       
/* 138 */       InvalidKeyException exception = new InvalidKeyException(PEMFile.sm.getString("jsse.pemParseError", new Object[] { PEMFile.this.filename }));
/* 139 */       SecretKeyFactory secretKeyFactory = { "RSA", "DSA", "EC" };SecretKey secretKey = secretKeyFactory.length; for (Cipher cipher = 0; cipher < secretKey; cipher++) { String algorithm = secretKeyFactory[cipher];
/*     */         try {
/* 141 */           return KeyFactory.getInstance(algorithm).generatePrivate(keySpec);
/*     */         } catch (InvalidKeySpecException e) {
/* 143 */           exception.addSuppressed(e);
/*     */         }
/*     */       }
/*     */       
/* 147 */       throw exception;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\jsse\PEMFile.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */