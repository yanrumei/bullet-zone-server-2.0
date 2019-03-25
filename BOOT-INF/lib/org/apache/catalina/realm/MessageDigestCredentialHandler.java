/*     */ package org.apache.catalina.realm;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Arrays;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.buf.B2CConverter;
/*     */ import org.apache.tomcat.util.buf.HexUtils;
/*     */ import org.apache.tomcat.util.codec.binary.Base64;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ import org.apache.tomcat.util.security.ConcurrentMessageDigest;
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
/*     */ public class MessageDigestCredentialHandler
/*     */   extends DigestCredentialHandlerBase
/*     */ {
/*  55 */   private static final Log log = LogFactory.getLog(MessageDigestCredentialHandler.class);
/*     */   
/*     */   public static final int DEFAULT_ITERATIONS = 1;
/*     */   
/*  59 */   private Charset encoding = StandardCharsets.UTF_8;
/*  60 */   private String algorithm = null;
/*     */   
/*     */   public String getEncoding()
/*     */   {
/*  64 */     return this.encoding.name();
/*     */   }
/*     */   
/*     */   public void setEncoding(String encodingName)
/*     */   {
/*  69 */     if (encodingName == null) {
/*  70 */       this.encoding = StandardCharsets.UTF_8;
/*     */     } else {
/*     */       try {
/*  73 */         this.encoding = B2CConverter.getCharset(encodingName);
/*     */       } catch (UnsupportedEncodingException e) {
/*  75 */         log.warn(sm.getString("mdCredentialHandler.unknownEncoding", new Object[] { encodingName, this.encoding
/*  76 */           .name() }));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String getAlgorithm()
/*     */   {
/*  84 */     return this.algorithm;
/*     */   }
/*     */   
/*     */   public void setAlgorithm(String algorithm)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/*  90 */     ConcurrentMessageDigest.init(algorithm);
/*  91 */     this.algorithm = algorithm;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean matches(String inputCredentials, String storedCredentials)
/*     */   {
/*  98 */     if ((inputCredentials == null) || (storedCredentials == null)) {
/*  99 */       return false;
/*     */     }
/*     */     
/* 102 */     if (getAlgorithm() == null)
/*     */     {
/* 104 */       return storedCredentials.equals(inputCredentials);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 109 */     if ((storedCredentials.startsWith("{MD5}")) || 
/* 110 */       (storedCredentials.startsWith("{SHA}")))
/*     */     {
/*     */ 
/* 113 */       String serverDigest = storedCredentials.substring(5);
/* 114 */       String userDigest = Base64.encodeBase64String(ConcurrentMessageDigest.digest(
/* 115 */         getAlgorithm(), new byte[][] { inputCredentials.getBytes(StandardCharsets.ISO_8859_1) }));
/* 116 */       return userDigest.equals(serverDigest);
/*     */     }
/* 118 */     if (storedCredentials.startsWith("{SSHA}"))
/*     */     {
/*     */ 
/*     */ 
/* 122 */       String serverDigestPlusSalt = storedCredentials.substring(6);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 127 */       byte[] serverDigestPlusSaltBytes = Base64.decodeBase64(serverDigestPlusSalt);
/* 128 */       int saltPos = 20;
/* 129 */       byte[] serverDigestBytes = new byte[20];
/* 130 */       System.arraycopy(serverDigestPlusSaltBytes, 0, serverDigestBytes, 0, 20);
/*     */       
/* 132 */       int saltLength = serverDigestPlusSaltBytes.length - 20;
/* 133 */       byte[] serverSaltBytes = new byte[saltLength];
/* 134 */       System.arraycopy(serverDigestPlusSaltBytes, 20, serverSaltBytes, 0, saltLength);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 139 */       byte[] userDigestBytes = ConcurrentMessageDigest.digest(getAlgorithm(), new byte[][] {inputCredentials
/* 140 */         .getBytes(StandardCharsets.ISO_8859_1), serverSaltBytes });
/*     */       
/*     */ 
/* 143 */       return Arrays.equals(userDigestBytes, serverDigestBytes);
/*     */     }
/* 145 */     if (storedCredentials.indexOf('$') > -1) {
/* 146 */       return matchesSaltIterationsEncoded(inputCredentials, storedCredentials);
/*     */     }
/*     */     
/*     */ 
/* 150 */     String userDigest = mutate(inputCredentials, null, 1);
/* 151 */     if (userDigest == null)
/*     */     {
/*     */ 
/* 154 */       return false;
/*     */     }
/* 156 */     return storedCredentials.equalsIgnoreCase(userDigest);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String mutate(String inputCredentials, byte[] salt, int iterations)
/*     */   {
/* 164 */     if (this.algorithm == null)
/* 165 */       return inputCredentials;
/*     */     byte[] userDigest;
/*     */     byte[] userDigest;
/* 168 */     if (salt == null) {
/* 169 */       userDigest = ConcurrentMessageDigest.digest(this.algorithm, iterations, new byte[][] {inputCredentials
/* 170 */         .getBytes(this.encoding) });
/*     */     } else {
/* 172 */       userDigest = ConcurrentMessageDigest.digest(this.algorithm, iterations, new byte[][] { salt, inputCredentials
/* 173 */         .getBytes(this.encoding) });
/*     */     }
/* 175 */     return HexUtils.toHexString(userDigest);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected int getDefaultIterations()
/*     */   {
/* 182 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */   protected Log getLog()
/*     */   {
/* 188 */     return log;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\realm\MessageDigestCredentialHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */