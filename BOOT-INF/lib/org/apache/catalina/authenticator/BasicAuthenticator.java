/*     */ package org.apache.catalina.authenticator;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.Principal;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Realm;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.buf.ByteChunk;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
/*     */ import org.apache.tomcat.util.codec.binary.Base64;
/*     */ import org.apache.tomcat.util.http.MimeHeaders;
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
/*     */ public class BasicAuthenticator
/*     */   extends AuthenticatorBase
/*     */ {
/*  48 */   private static final Log log = LogFactory.getLog(BasicAuthenticator.class);
/*     */   
/*  50 */   private Charset charset = StandardCharsets.ISO_8859_1;
/*  51 */   private String charsetString = null;
/*     */   
/*     */   public String getCharset()
/*     */   {
/*  55 */     return this.charsetString;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setCharset(String charsetString)
/*     */   {
/*  61 */     if ((charsetString == null) || (charsetString.isEmpty())) {
/*  62 */       this.charset = StandardCharsets.ISO_8859_1;
/*  63 */     } else if ("UTF-8".equalsIgnoreCase(charsetString)) {
/*  64 */       this.charset = StandardCharsets.UTF_8;
/*     */     } else {
/*  66 */       throw new IllegalArgumentException(sm.getString("basicAuthenticator.invalidCharset"));
/*     */     }
/*  68 */     this.charsetString = charsetString;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean doAuthenticate(org.apache.catalina.connector.Request request, HttpServletResponse response)
/*     */     throws IOException
/*     */   {
/*  76 */     if (checkForCachedAuthentication(request, response, true)) {
/*  77 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  83 */     MessageBytes authorization = request.getCoyoteRequest().getMimeHeaders().getValue("authorization");
/*     */     
/*  85 */     if (authorization != null) {
/*  86 */       authorization.toBytes();
/*  87 */       ByteChunk authorizationBC = authorization.getByteChunk();
/*  88 */       BasicCredentials credentials = null;
/*     */       try {
/*  90 */         credentials = new BasicCredentials(authorizationBC, this.charset);
/*  91 */         String username = credentials.getUsername();
/*  92 */         String password = credentials.getPassword();
/*     */         
/*  94 */         Principal principal = this.context.getRealm().authenticate(username, password);
/*  95 */         if (principal != null) {
/*  96 */           register(request, response, principal, "BASIC", username, password);
/*     */           
/*  98 */           return true;
/*     */         }
/*     */       }
/*     */       catch (IllegalArgumentException iae) {
/* 102 */         if (log.isDebugEnabled()) {
/* 103 */           log.debug("Invalid Authorization" + iae.getMessage());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 109 */     StringBuilder value = new StringBuilder(16);
/* 110 */     value.append("Basic realm=\"");
/* 111 */     value.append(getRealmName(this.context));
/* 112 */     value.append('"');
/* 113 */     if ((this.charsetString != null) && (!this.charsetString.isEmpty())) {
/* 114 */       value.append(", charset=");
/* 115 */       value.append(this.charsetString);
/*     */     }
/* 117 */     response.setHeader("WWW-Authenticate", value.toString());
/* 118 */     response.sendError(401);
/* 119 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   protected String getAuthMethod()
/*     */   {
/* 125 */     return "BASIC";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class BasicCredentials
/*     */   {
/*     */     private static final String METHOD = "basic ";
/*     */     
/*     */ 
/*     */     private final Charset charset;
/*     */     
/*     */ 
/*     */     private final ByteChunk authorization;
/*     */     
/*     */     private final int initialOffset;
/*     */     
/*     */     private int base64blobOffset;
/*     */     
/*     */     private int base64blobLength;
/*     */     
/* 146 */     private String username = null;
/* 147 */     private String password = null;
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
/*     */     public BasicCredentials(ByteChunk input, Charset charset)
/*     */       throws IllegalArgumentException
/*     */     {
/* 161 */       this.authorization = input;
/* 162 */       this.initialOffset = input.getOffset();
/* 163 */       this.charset = charset;
/*     */       
/* 165 */       parseMethod();
/* 166 */       byte[] decoded = parseBase64();
/* 167 */       parseCredentials(decoded);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getUsername()
/*     */     {
/* 177 */       return this.username;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getPassword()
/*     */     {
/* 187 */       return this.password;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void parseMethod()
/*     */       throws IllegalArgumentException
/*     */     {
/* 195 */       if (this.authorization.startsWithIgnoreCase("basic ", 0))
/*     */       {
/* 197 */         this.base64blobOffset = (this.initialOffset + "basic ".length());
/* 198 */         this.base64blobLength = (this.authorization.getLength() - "basic ".length());
/*     */       }
/*     */       else
/*     */       {
/* 202 */         throw new IllegalArgumentException("Authorization header method is not \"Basic\"");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private byte[] parseBase64()
/*     */       throws IllegalArgumentException
/*     */     {
/* 213 */       byte[] decoded = Base64.decodeBase64(this.authorization
/* 214 */         .getBuffer(), this.base64blobOffset, this.base64blobLength);
/*     */       
/*     */ 
/* 217 */       this.authorization.setOffset(this.initialOffset);
/* 218 */       if (decoded == null) {
/* 219 */         throw new IllegalArgumentException("Basic Authorization credentials are not Base64");
/*     */       }
/*     */       
/* 222 */       return decoded;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private void parseCredentials(byte[] decoded)
/*     */       throws IllegalArgumentException
/*     */     {
/* 232 */       int colon = -1;
/* 233 */       for (int i = 0; i < decoded.length; i++) {
/* 234 */         if (decoded[i] == 58) {
/* 235 */           colon = i;
/* 236 */           break;
/*     */         }
/*     */       }
/*     */       
/* 240 */       if (colon < 0) {
/* 241 */         this.username = new String(decoded, this.charset);
/*     */       }
/*     */       else
/*     */       {
/* 245 */         this.username = new String(decoded, 0, colon, this.charset);
/* 246 */         this.password = new String(decoded, colon + 1, decoded.length - colon - 1, this.charset);
/*     */         
/* 248 */         if (this.password.length() > 1) {
/* 249 */           this.password = this.password.trim();
/*     */         }
/*     */       }
/*     */       
/* 253 */       if (this.username.length() > 1) {
/* 254 */         this.username = this.username.trim();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\authenticator\BasicAuthenticator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */