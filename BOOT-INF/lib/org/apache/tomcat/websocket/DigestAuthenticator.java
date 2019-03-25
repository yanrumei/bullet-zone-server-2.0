/*     */ package org.apache.tomcat.websocket;
/*     */ 
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.Map;
/*     */ import org.apache.tomcat.util.security.MD5Encoder;
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
/*     */ public class DigestAuthenticator
/*     */   extends Authenticator
/*     */ {
/*     */   public static final String schemeName = "digest";
/*     */   private SecureRandom cnonceGenerator;
/*  34 */   private int nonceCount = 0;
/*     */   
/*     */   private long cNonce;
/*     */   
/*     */   public String getAuthorization(String requestUri, String WWWAuthenticate, Map<String, Object> userProperties)
/*     */     throws AuthenticationException
/*     */   {
/*  41 */     String userName = (String)userProperties.get("org.apache.tomcat.websocket.WS_AUTHENTICATION_USER_NAME");
/*  42 */     String password = (String)userProperties.get("org.apache.tomcat.websocket.WS_AUTHENTICATION_PASSWORD");
/*     */     
/*  44 */     if ((userName == null) || (password == null)) {
/*  45 */       throw new AuthenticationException("Failed to perform Digest authentication due to  missing user/password");
/*     */     }
/*     */     
/*     */ 
/*  49 */     Map<String, String> wwwAuthenticate = parseWWWAuthenticateHeader(WWWAuthenticate);
/*     */     
/*  51 */     String realm = (String)wwwAuthenticate.get("realm");
/*  52 */     String nonce = (String)wwwAuthenticate.get("nonce");
/*  53 */     String messageQop = (String)wwwAuthenticate.get("qop");
/*     */     
/*  55 */     String algorithm = wwwAuthenticate.get("algorithm") == null ? "MD5" : (String)wwwAuthenticate.get("algorithm");
/*  56 */     String opaque = (String)wwwAuthenticate.get("opaque");
/*     */     
/*  58 */     StringBuilder challenge = new StringBuilder();
/*     */     
/*  60 */     if (!messageQop.isEmpty()) {
/*  61 */       if (this.cnonceGenerator == null) {
/*  62 */         this.cnonceGenerator = new SecureRandom();
/*     */       }
/*     */       
/*  65 */       this.cNonce = this.cnonceGenerator.nextLong();
/*  66 */       this.nonceCount += 1;
/*     */     }
/*     */     
/*  69 */     challenge.append("Digest ");
/*  70 */     challenge.append("username =\"" + userName + "\",");
/*  71 */     challenge.append("realm=\"" + realm + "\",");
/*  72 */     challenge.append("nonce=\"" + nonce + "\",");
/*  73 */     challenge.append("uri=\"" + requestUri + "\",");
/*     */     try
/*     */     {
/*  76 */       challenge.append("response=\"" + calculateRequestDigest(requestUri, userName, password, realm, nonce, messageQop, algorithm) + "\",");
/*     */ 
/*     */     }
/*     */     catch (NoSuchAlgorithmException e)
/*     */     {
/*     */ 
/*  82 */       throw new AuthenticationException("Unable to generate request digest " + e.getMessage());
/*     */     }
/*     */     
/*  85 */     challenge.append("algorithm=" + algorithm + ",");
/*  86 */     challenge.append("opaque=\"" + opaque + "\",");
/*     */     
/*  88 */     if (!messageQop.isEmpty()) {
/*  89 */       challenge.append("qop=\"" + messageQop + "\"");
/*  90 */       challenge.append(",cnonce=\"" + this.cNonce + "\",");
/*  91 */       challenge.append("nc=" + String.format("%08X", new Object[] { Integer.valueOf(this.nonceCount) }));
/*     */     }
/*     */     
/*  94 */     return challenge.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private String calculateRequestDigest(String requestUri, String userName, String password, String realm, String nonce, String qop, String algorithm)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 102 */     StringBuilder preDigest = new StringBuilder();
/*     */     String A1;
/*     */     String A1;
/* 105 */     if (algorithm.equalsIgnoreCase("MD5")) {
/* 106 */       A1 = userName + ":" + realm + ":" + password;
/*     */     }
/*     */     else {
/* 109 */       A1 = encodeMD5(new StringBuilder().append(userName).append(":").append(realm).append(":").append(password).toString()) + ":" + nonce + ":" + this.cNonce;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 116 */     String A2 = "GET:" + requestUri;
/*     */     
/* 118 */     preDigest.append(encodeMD5(A1));
/* 119 */     preDigest.append(":");
/* 120 */     preDigest.append(nonce);
/*     */     
/* 122 */     if (qop.toLowerCase().contains("auth")) {
/* 123 */       preDigest.append(":");
/* 124 */       preDigest.append(String.format("%08X", new Object[] { Integer.valueOf(this.nonceCount) }));
/* 125 */       preDigest.append(":");
/* 126 */       preDigest.append(String.valueOf(this.cNonce));
/* 127 */       preDigest.append(":");
/* 128 */       preDigest.append(qop);
/*     */     }
/*     */     
/* 131 */     preDigest.append(":");
/* 132 */     preDigest.append(encodeMD5(A2));
/*     */     
/* 134 */     return encodeMD5(preDigest.toString());
/*     */   }
/*     */   
/*     */   private String encodeMD5(String value) throws NoSuchAlgorithmException
/*     */   {
/* 139 */     byte[] bytesOfMessage = value.getBytes(StandardCharsets.ISO_8859_1);
/* 140 */     MessageDigest md = MessageDigest.getInstance("MD5");
/* 141 */     byte[] thedigest = md.digest(bytesOfMessage);
/*     */     
/* 143 */     return MD5Encoder.encode(thedigest);
/*     */   }
/*     */   
/*     */   public String getSchemeName()
/*     */   {
/* 148 */     return "digest";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\DigestAuthenticator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */