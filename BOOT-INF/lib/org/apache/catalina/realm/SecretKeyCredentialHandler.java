/*     */ package org.apache.catalina.realm;
/*     */ 
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.security.spec.KeySpec;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.crypto.SecretKeyFactory;
/*     */ import javax.crypto.spec.PBEKeySpec;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.buf.HexUtils;
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
/*     */ public class SecretKeyCredentialHandler
/*     */   extends DigestCredentialHandlerBase
/*     */ {
/*  32 */   private static final Log log = LogFactory.getLog(SecretKeyCredentialHandler.class);
/*     */   
/*     */   public static final String DEFAULT_ALGORITHM = "PBKDF2WithHmacSHA1";
/*     */   
/*     */   public static final int DEFAULT_KEY_LENGTH = 160;
/*     */   
/*     */   public static final int DEFAULT_ITERATIONS = 20000;
/*     */   private SecretKeyFactory secretKeyFactory;
/*  40 */   private int keyLength = 160;
/*     */   
/*     */   public SecretKeyCredentialHandler() throws NoSuchAlgorithmException
/*     */   {
/*  44 */     setAlgorithm("PBKDF2WithHmacSHA1");
/*     */   }
/*     */   
/*     */ 
/*     */   public String getAlgorithm()
/*     */   {
/*  50 */     return this.secretKeyFactory.getAlgorithm();
/*     */   }
/*     */   
/*     */   public void setAlgorithm(String algorithm)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/*  56 */     SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
/*  57 */     this.secretKeyFactory = secretKeyFactory;
/*     */   }
/*     */   
/*     */   public int getKeyLength()
/*     */   {
/*  62 */     return this.keyLength;
/*     */   }
/*     */   
/*     */   public void setKeyLength(int keyLength)
/*     */   {
/*  67 */     this.keyLength = keyLength;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean matches(String inputCredentials, String storedCredentials)
/*     */   {
/*  73 */     return matchesSaltIterationsEncoded(inputCredentials, storedCredentials);
/*     */   }
/*     */   
/*     */ 
/*     */   protected String mutate(String inputCredentials, byte[] salt, int iterations)
/*     */   {
/*  79 */     return mutate(inputCredentials, salt, iterations, getKeyLength());
/*     */   }
/*     */   
/*     */   protected String mutate(String inputCredentials, byte[] salt, int iterations, int keyLength)
/*     */   {
/*     */     try
/*     */     {
/*  86 */       KeySpec spec = new PBEKeySpec(inputCredentials.toCharArray(), salt, iterations, keyLength);
/*  87 */       return HexUtils.toHexString(this.secretKeyFactory.generateSecret(spec).getEncoded());
/*     */     } catch (InvalidKeySpecException|IllegalArgumentException e) {
/*  89 */       log.warn(sm.getString("pbeCredentialHandler.invalidKeySpec"), e); }
/*  90 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected int getDefaultIterations()
/*     */   {
/*  97 */     return 20000;
/*     */   }
/*     */   
/*     */ 
/*     */   protected Log getLog()
/*     */   {
/* 103 */     return log;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\realm\SecretKeyCredentialHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */