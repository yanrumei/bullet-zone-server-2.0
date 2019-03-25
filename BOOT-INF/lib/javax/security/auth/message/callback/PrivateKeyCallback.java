/*     */ package javax.security.auth.message.callback;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.cert.Certificate;
/*     */ import javax.security.auth.callback.Callback;
/*     */ import javax.security.auth.x500.X500Principal;
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
/*     */ public class PrivateKeyCallback
/*     */   implements Callback
/*     */ {
/*     */   private final Request request;
/*     */   private Certificate[] chain;
/*     */   private PrivateKey key;
/*     */   
/*     */   public PrivateKeyCallback(Request request)
/*     */   {
/*  39 */     this.request = request;
/*     */   }
/*     */   
/*     */   public Request getRequest() {
/*  43 */     return this.request;
/*     */   }
/*     */   
/*     */   public void setKey(PrivateKey key, Certificate[] chain) {
/*  47 */     this.key = key;
/*  48 */     this.chain = chain;
/*     */   }
/*     */   
/*     */   public PrivateKey getKey() {
/*  52 */     return this.key;
/*     */   }
/*     */   
/*     */   public Certificate[] getChain() {
/*  56 */     return this.chain;
/*     */   }
/*     */   
/*     */   public static abstract interface Request {}
/*     */   
/*     */   public static class AliasRequest implements PrivateKeyCallback.Request
/*     */   {
/*     */     private final String alias;
/*     */     
/*     */     public AliasRequest(String alias)
/*     */     {
/*  67 */       this.alias = alias;
/*     */     }
/*     */     
/*     */     public String getAlias() {
/*  71 */       return this.alias;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class DigestRequest implements PrivateKeyCallback.Request {
/*     */     private final byte[] digest;
/*     */     private final String algorithm;
/*     */     
/*     */     public DigestRequest(byte[] digest, String algorithm) {
/*  80 */       this.digest = digest;
/*  81 */       this.algorithm = algorithm;
/*     */     }
/*     */     
/*     */     public byte[] getDigest() {
/*  85 */       return this.digest;
/*     */     }
/*     */     
/*     */     public String getAlgorithm() {
/*  89 */       return this.algorithm;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SubjectKeyIDRequest implements PrivateKeyCallback.Request
/*     */   {
/*     */     private final byte[] subjectKeyID;
/*     */     
/*     */     public SubjectKeyIDRequest(byte[] subjectKeyID) {
/*  98 */       this.subjectKeyID = subjectKeyID;
/*     */     }
/*     */     
/*     */     public byte[] getSubjectKeyID() {
/* 102 */       return this.subjectKeyID;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class IssuerSerialNumRequest implements PrivateKeyCallback.Request {
/*     */     private final X500Principal issuer;
/*     */     private final BigInteger serialNum;
/*     */     
/*     */     public IssuerSerialNumRequest(X500Principal issuer, BigInteger serialNum) {
/* 111 */       this.issuer = issuer;
/* 112 */       this.serialNum = serialNum;
/*     */     }
/*     */     
/*     */     public X500Principal getIssuer() {
/* 116 */       return this.issuer;
/*     */     }
/*     */     
/*     */     public BigInteger getSerialNum() {
/* 120 */       return this.serialNum;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\security\auth\message\callback\PrivateKeyCallback.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */