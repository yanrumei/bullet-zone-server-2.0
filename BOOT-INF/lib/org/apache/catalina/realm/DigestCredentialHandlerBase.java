/*     */ package org.apache.catalina.realm;
/*     */ 
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.Random;
/*     */ import org.apache.catalina.CredentialHandler;
/*     */ import org.apache.juli.logging.Log;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DigestCredentialHandlerBase
/*     */   implements CredentialHandler
/*     */ {
/*  34 */   protected static final StringManager sm = StringManager.getManager(DigestCredentialHandlerBase.class);
/*     */   
/*     */   public static final int DEFAULT_SALT_LENGTH = 32;
/*     */   
/*  38 */   private int iterations = getDefaultIterations();
/*  39 */   private int saltLength = getDefaultSaltLength();
/*  40 */   private final Object randomLock = new Object();
/*  41 */   private volatile Random random = null;
/*  42 */   private boolean logInvalidStoredCredentials = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIterations()
/*     */   {
/*  50 */     return this.iterations;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIterations(int iterations)
/*     */   {
/*  60 */     this.iterations = iterations;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSaltLength()
/*     */   {
/*  69 */     return this.saltLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSaltLength(int saltLength)
/*     */   {
/*  79 */     this.saltLength = saltLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getLogInvalidStoredCredentials()
/*     */   {
/*  89 */     return this.logInvalidStoredCredentials;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLogInvalidStoredCredentials(boolean logInvalidStoredCredentials)
/*     */   {
/* 101 */     this.logInvalidStoredCredentials = logInvalidStoredCredentials;
/*     */   }
/*     */   
/*     */ 
/*     */   public String mutate(String userCredential)
/*     */   {
/* 107 */     byte[] salt = null;
/* 108 */     int iterations = getIterations();
/* 109 */     int saltLength = getSaltLength();
/* 110 */     if (saltLength == 0) {
/* 111 */       salt = new byte[0];
/* 112 */     } else if (saltLength > 0)
/*     */     {
/* 114 */       if (this.random == null) {
/* 115 */         synchronized (this.randomLock) {
/* 116 */           if (this.random == null) {
/* 117 */             this.random = new SecureRandom();
/*     */           }
/*     */         }
/*     */       }
/* 121 */       salt = new byte[saltLength];
/*     */       
/*     */ 
/* 124 */       this.random.nextBytes(salt);
/*     */     }
/*     */     
/* 127 */     String serverCredential = mutate(userCredential, salt, iterations);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 132 */     if (serverCredential == null) {
/* 133 */       return null;
/*     */     }
/*     */     
/* 136 */     if ((saltLength == 0) && (iterations == 1))
/*     */     {
/* 138 */       return serverCredential;
/*     */     }
/*     */     
/* 141 */     StringBuilder result = new StringBuilder((saltLength << 1) + 10 + serverCredential.length() + 2);
/* 142 */     result.append(HexUtils.toHexString(salt));
/* 143 */     result.append('$');
/* 144 */     result.append(iterations);
/* 145 */     result.append('$');
/* 146 */     result.append(serverCredential);
/*     */     
/* 148 */     return result.toString();
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
/*     */   protected boolean matchesSaltIterationsEncoded(String inputCredentials, String storedCredentials)
/*     */   {
/* 165 */     if (storedCredentials == null)
/*     */     {
/*     */ 
/* 168 */       logInvalidStoredCredentials(null);
/* 169 */       return false;
/*     */     }
/*     */     
/* 172 */     int sep1 = storedCredentials.indexOf('$');
/* 173 */     int sep2 = storedCredentials.indexOf('$', sep1 + 1);
/*     */     
/* 175 */     if ((sep1 < 0) || (sep2 < 0))
/*     */     {
/*     */ 
/* 178 */       logInvalidStoredCredentials(storedCredentials);
/* 179 */       return false;
/*     */     }
/*     */     
/* 182 */     String hexSalt = storedCredentials.substring(0, sep1);
/*     */     
/* 184 */     int iterations = Integer.parseInt(storedCredentials.substring(sep1 + 1, sep2));
/*     */     
/* 186 */     String storedHexEncoded = storedCredentials.substring(sep2 + 1);
/*     */     try
/*     */     {
/* 189 */       salt = HexUtils.fromHexString(hexSalt);
/*     */     } catch (IllegalArgumentException iae) { byte[] salt;
/* 191 */       logInvalidStoredCredentials(storedCredentials);
/* 192 */       return false;
/*     */     }
/*     */     byte[] salt;
/* 195 */     String inputHexEncoded = mutate(inputCredentials, salt, iterations, 
/* 196 */       HexUtils.fromHexString(storedHexEncoded).length * 8);
/* 197 */     if (inputHexEncoded == null)
/*     */     {
/*     */ 
/* 200 */       return false;
/*     */     }
/*     */     
/* 203 */     return storedHexEncoded.equalsIgnoreCase(inputHexEncoded);
/*     */   }
/*     */   
/*     */   private void logInvalidStoredCredentials(String storedCredentials)
/*     */   {
/* 208 */     if (this.logInvalidStoredCredentials)
/*     */     {
/*     */ 
/* 211 */       getLog().warn(sm.getString("credentialHandler.invalidStoredCredential", new Object[] { storedCredentials }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getDefaultSaltLength()
/*     */   {
/* 221 */     return 32;
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
/*     */ 
/*     */   protected abstract String mutate(String paramString, byte[] paramArrayOfByte, int paramInt);
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
/*     */   protected String mutate(String inputCredentials, byte[] salt, int iterations, int keyLength)
/*     */   {
/* 263 */     return mutate(inputCredentials, salt, iterations);
/*     */   }
/*     */   
/*     */   public abstract void setAlgorithm(String paramString)
/*     */     throws NoSuchAlgorithmException;
/*     */   
/*     */   public abstract String getAlgorithm();
/*     */   
/*     */   protected abstract int getDefaultIterations();
/*     */   
/*     */   protected abstract Log getLog();
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\realm\DigestCredentialHandlerBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */