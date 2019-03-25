/*     */ package org.apache.catalina.authenticator;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.Principal;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.Realm;
/*     */ import org.apache.catalina.connector.Request;
/*     */ import org.apache.catalina.util.SessionIdGeneratorBase;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.http.parser.Authorization;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ import org.apache.tomcat.util.security.ConcurrentMessageDigest;
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
/*     */ 
/*     */ 
/*     */ public class DigestAuthenticator
/*     */   extends AuthenticatorBase
/*     */ {
/*  48 */   private static final Log log = LogFactory.getLog(DigestAuthenticator.class);
/*     */   
/*     */ 
/*     */ 
/*     */   protected static final String QOP = "auth";
/*     */   
/*     */ 
/*     */ 
/*     */   protected Map<String, NonceInfo> nonces;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DigestAuthenticator()
/*     */   {
/*  63 */     setCache(false);
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
/*  79 */   protected long lastTimestamp = 0L;
/*  80 */   protected final Object lastTimestampLock = new Object();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  87 */   protected int nonceCacheSize = 1000;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  94 */   protected int nonceCountWindowSize = 100;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  99 */   protected String key = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 106 */   protected long nonceValidity = 300000L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String opaque;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 119 */   protected boolean validateUri = true;
/*     */   
/*     */ 
/*     */   public int getNonceCountWindowSize()
/*     */   {
/* 124 */     return this.nonceCountWindowSize;
/*     */   }
/*     */   
/*     */   public void setNonceCountWindowSize(int nonceCountWindowSize)
/*     */   {
/* 129 */     this.nonceCountWindowSize = nonceCountWindowSize;
/*     */   }
/*     */   
/*     */   public int getNonceCacheSize()
/*     */   {
/* 134 */     return this.nonceCacheSize;
/*     */   }
/*     */   
/*     */   public void setNonceCacheSize(int nonceCacheSize)
/*     */   {
/* 139 */     this.nonceCacheSize = nonceCacheSize;
/*     */   }
/*     */   
/*     */   public String getKey()
/*     */   {
/* 144 */     return this.key;
/*     */   }
/*     */   
/*     */   public void setKey(String key)
/*     */   {
/* 149 */     this.key = key;
/*     */   }
/*     */   
/*     */   public long getNonceValidity()
/*     */   {
/* 154 */     return this.nonceValidity;
/*     */   }
/*     */   
/*     */   public void setNonceValidity(long nonceValidity)
/*     */   {
/* 159 */     this.nonceValidity = nonceValidity;
/*     */   }
/*     */   
/*     */   public String getOpaque()
/*     */   {
/* 164 */     return this.opaque;
/*     */   }
/*     */   
/*     */   public void setOpaque(String opaque)
/*     */   {
/* 169 */     this.opaque = opaque;
/*     */   }
/*     */   
/*     */   public boolean isValidateUri()
/*     */   {
/* 174 */     return this.validateUri;
/*     */   }
/*     */   
/*     */   public void setValidateUri(boolean validateUri)
/*     */   {
/* 179 */     this.validateUri = validateUri;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean doAuthenticate(Request request, HttpServletResponse response)
/*     */     throws IOException
/*     */   {
/* 208 */     if (checkForCachedAuthentication(request, response, false)) {
/* 209 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 213 */     Principal principal = null;
/* 214 */     String authorization = request.getHeader("authorization");
/*     */     
/* 216 */     DigestInfo digestInfo = new DigestInfo(getOpaque(), getNonceValidity(), getKey(), this.nonces, isValidateUri());
/* 217 */     if ((authorization != null) && 
/* 218 */       (digestInfo.parse(request, authorization))) {
/* 219 */       if (digestInfo.validate(request)) {
/* 220 */         principal = digestInfo.authenticate(this.context.getRealm());
/*     */       }
/*     */       
/* 223 */       if ((principal != null) && (!digestInfo.isNonceStale())) {
/* 224 */         register(request, response, principal, "DIGEST", digestInfo
/*     */         
/* 226 */           .getUsername(), null);
/* 227 */         return true;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 236 */     String nonce = generateNonce(request);
/*     */     
/* 238 */     setAuthenticateHeader(request, response, nonce, (principal != null) && 
/* 239 */       (digestInfo.isNonceStale()));
/* 240 */     response.sendError(401);
/* 241 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   protected String getAuthMethod()
/*     */   {
/* 247 */     return "DIGEST";
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
/*     */   protected static String removeQuotes(String quotedString, boolean quotesRequired)
/*     */   {
/* 265 */     if ((quotedString.length() > 0) && (quotedString.charAt(0) != '"') && (!quotesRequired))
/*     */     {
/* 267 */       return quotedString; }
/* 268 */     if (quotedString.length() > 2) {
/* 269 */       return quotedString.substring(1, quotedString.length() - 1);
/*     */     }
/* 271 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static String removeQuotes(String quotedString)
/*     */   {
/* 282 */     return removeQuotes(quotedString, false);
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
/*     */   protected String generateNonce(Request request)
/*     */   {
/* 295 */     long currentTime = System.currentTimeMillis();
/*     */     
/* 297 */     synchronized (this.lastTimestampLock) {
/* 298 */       if (currentTime > this.lastTimestamp) {
/* 299 */         this.lastTimestamp = currentTime;
/*     */       } else {
/* 301 */         currentTime = ++this.lastTimestamp;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 306 */     String ipTimeKey = request.getRemoteAddr() + ":" + currentTime + ":" + getKey();
/*     */     
/* 308 */     byte[] buffer = ConcurrentMessageDigest.digestMD5(new byte[][] {ipTimeKey
/* 309 */       .getBytes(StandardCharsets.ISO_8859_1) });
/* 310 */     String nonce = currentTime + ":" + MD5Encoder.encode(buffer);
/*     */     
/* 312 */     NonceInfo info = new NonceInfo(currentTime, getNonceCountWindowSize());
/* 313 */     synchronized (this.nonces) {
/* 314 */       this.nonces.put(nonce, info);
/*     */     }
/*     */     
/* 317 */     return nonce;
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
/*     */   protected void setAuthenticateHeader(HttpServletRequest request, HttpServletResponse response, String nonce, boolean isNonceStale)
/*     */   {
/* 352 */     String realmName = getRealmName(this.context);
/*     */     String authenticateHeader;
/*     */     String authenticateHeader;
/* 355 */     if (isNonceStale)
/*     */     {
/*     */ 
/* 358 */       authenticateHeader = "Digest realm=\"" + realmName + "\", qop=\"" + "auth" + "\", nonce=\"" + nonce + "\", opaque=\"" + getOpaque() + "\", stale=true";
/*     */     }
/*     */     else
/*     */     {
/* 362 */       authenticateHeader = "Digest realm=\"" + realmName + "\", qop=\"" + "auth" + "\", nonce=\"" + nonce + "\", opaque=\"" + getOpaque() + "\"";
/*     */     }
/*     */     
/* 365 */     response.setHeader("WWW-Authenticate", authenticateHeader);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected synchronized void startInternal()
/*     */     throws LifecycleException
/*     */   {
/* 374 */     super.startInternal();
/*     */     
/*     */ 
/* 377 */     if (getKey() == null) {
/* 378 */       setKey(this.sessionIdGenerator.generateSessionId());
/*     */     }
/*     */     
/*     */ 
/* 382 */     if (getOpaque() == null) {
/* 383 */       setOpaque(this.sessionIdGenerator.generateSessionId());
/*     */     }
/*     */     
/* 386 */     this.nonces = new LinkedHashMap()
/*     */     {
/*     */       private static final long serialVersionUID = 1L;
/*     */       
/*     */       private static final long LOG_SUPPRESS_TIME = 300000L;
/* 391 */       private long lastLog = 0L;
/*     */       
/*     */ 
/*     */ 
/*     */       protected boolean removeEldestEntry(Map.Entry<String, DigestAuthenticator.NonceInfo> eldest)
/*     */       {
/* 397 */         long currentTime = System.currentTimeMillis();
/* 398 */         if (size() > DigestAuthenticator.this.getNonceCacheSize()) {
/* 399 */           if (this.lastLog < currentTime)
/*     */           {
/* 401 */             if (currentTime - ((DigestAuthenticator.NonceInfo)eldest.getValue()).getTimestamp() < DigestAuthenticator.this.getNonceValidity())
/*     */             {
/* 403 */               DigestAuthenticator.log.warn(AuthenticatorBase.sm.getString("digestAuthenticator.cacheRemove"));
/*     */               
/* 405 */               this.lastLog = (currentTime + 300000L);
/*     */             } }
/* 407 */           return true;
/*     */         }
/* 409 */         return false;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public static class DigestInfo
/*     */   {
/*     */     private final String opaque;
/*     */     private final long nonceValidity;
/*     */     private final String key;
/*     */     private final Map<String, DigestAuthenticator.NonceInfo> nonces;
/* 420 */     private boolean validateUri = true;
/*     */     
/* 422 */     private String userName = null;
/* 423 */     private String method = null;
/* 424 */     private String uri = null;
/* 425 */     private String response = null;
/* 426 */     private String nonce = null;
/* 427 */     private String nc = null;
/* 428 */     private String cnonce = null;
/* 429 */     private String realmName = null;
/* 430 */     private String qop = null;
/* 431 */     private String opaqueReceived = null;
/*     */     
/* 433 */     private boolean nonceStale = false;
/*     */     
/*     */ 
/*     */     public DigestInfo(String opaque, long nonceValidity, String key, Map<String, DigestAuthenticator.NonceInfo> nonces, boolean validateUri)
/*     */     {
/* 438 */       this.opaque = opaque;
/* 439 */       this.nonceValidity = nonceValidity;
/* 440 */       this.key = key;
/* 441 */       this.nonces = nonces;
/* 442 */       this.validateUri = validateUri;
/*     */     }
/*     */     
/*     */     public String getUsername()
/*     */     {
/* 447 */       return this.userName;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean parse(Request request, String authorization)
/*     */     {
/* 453 */       if (authorization == null) {
/* 454 */         return false;
/*     */       }
/*     */       
/*     */       try
/*     */       {
/* 459 */         directives = Authorization.parseAuthorizationDigest(new StringReader(authorization));
/*     */       } catch (IOException e) {
/*     */         Map<String, String> directives;
/* 462 */         return false;
/*     */       }
/*     */       Map<String, String> directives;
/* 465 */       if (directives == null) {
/* 466 */         return false;
/*     */       }
/*     */       
/* 469 */       this.method = request.getMethod();
/* 470 */       this.userName = ((String)directives.get("username"));
/* 471 */       this.realmName = ((String)directives.get("realm"));
/* 472 */       this.nonce = ((String)directives.get("nonce"));
/* 473 */       this.nc = ((String)directives.get("nc"));
/* 474 */       this.cnonce = ((String)directives.get("cnonce"));
/* 475 */       this.qop = ((String)directives.get("qop"));
/* 476 */       this.uri = ((String)directives.get("uri"));
/* 477 */       this.response = ((String)directives.get("response"));
/* 478 */       this.opaqueReceived = ((String)directives.get("opaque"));
/*     */       
/* 480 */       return true;
/*     */     }
/*     */     
/*     */     public boolean validate(Request request) {
/* 484 */       if ((this.userName == null) || (this.realmName == null) || (this.nonce == null) || (this.uri == null) || (this.response == null))
/*     */       {
/* 486 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 490 */       if (this.validateUri)
/*     */       {
/* 492 */         String query = request.getQueryString();
/* 493 */         String uriQuery; String uriQuery; if (query == null) {
/* 494 */           uriQuery = request.getRequestURI();
/*     */         } else {
/* 496 */           uriQuery = request.getRequestURI() + "?" + query;
/*     */         }
/* 498 */         if (!this.uri.equals(uriQuery))
/*     */         {
/*     */ 
/*     */ 
/* 502 */           String host = request.getHeader("host");
/* 503 */           String scheme = request.getScheme();
/* 504 */           if ((host != null) && (!uriQuery.startsWith(scheme))) {
/* 505 */             StringBuilder absolute = new StringBuilder();
/* 506 */             absolute.append(scheme);
/* 507 */             absolute.append("://");
/* 508 */             absolute.append(host);
/* 509 */             absolute.append(uriQuery);
/* 510 */             if (!this.uri.equals(absolute.toString())) {
/* 511 */               return false;
/*     */             }
/*     */           } else {
/* 514 */             return false;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 520 */       String lcRealm = AuthenticatorBase.getRealmName(request.getContext());
/* 521 */       if (!lcRealm.equals(this.realmName)) {
/* 522 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 526 */       if (!this.opaque.equals(this.opaqueReceived)) {
/* 527 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 531 */       int i = this.nonce.indexOf(':');
/* 532 */       if ((i < 0) || (i + 1 == this.nonce.length())) {
/* 533 */         return false;
/*     */       }
/*     */       try
/*     */       {
/* 537 */         nonceTime = Long.parseLong(this.nonce.substring(0, i));
/*     */       } catch (NumberFormatException nfe) { long nonceTime;
/* 539 */         return false; }
/*     */       long nonceTime;
/* 541 */       String md5clientIpTimeKey = this.nonce.substring(i + 1);
/* 542 */       long currentTime = System.currentTimeMillis();
/* 543 */       if (currentTime - nonceTime > this.nonceValidity) {
/* 544 */         this.nonceStale = true;
/* 545 */         synchronized (this.nonces) {
/* 546 */           this.nonces.remove(this.nonce);
/*     */         }
/*     */       }
/*     */       
/* 550 */       String serverIpTimeKey = request.getRemoteAddr() + ":" + nonceTime + ":" + this.key;
/* 551 */       byte[] buffer = ConcurrentMessageDigest.digestMD5(new byte[][] {serverIpTimeKey
/* 552 */         .getBytes(StandardCharsets.ISO_8859_1) });
/* 553 */       String md5ServerIpTimeKey = MD5Encoder.encode(buffer);
/* 554 */       if (!md5ServerIpTimeKey.equals(md5clientIpTimeKey)) {
/* 555 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 559 */       if ((this.qop != null) && (!"auth".equals(this.qop))) {
/* 560 */         return false;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 565 */       if (this.qop == null) {
/* 566 */         if ((this.cnonce != null) || (this.nc != null)) {
/* 567 */           return false;
/*     */         }
/*     */       } else {
/* 570 */         if ((this.cnonce == null) || (this.nc == null)) {
/* 571 */           return false;
/*     */         }
/*     */         
/*     */ 
/* 575 */         if ((this.nc.length() < 6) || (this.nc.length() > 8)) {
/* 576 */           return false;
/*     */         }
/*     */         try
/*     */         {
/* 580 */           count = Long.parseLong(this.nc, 16);
/*     */         } catch (NumberFormatException nfe) { long count;
/* 582 */           return false; }
/*     */         long count;
/*     */         DigestAuthenticator.NonceInfo info;
/* 585 */         synchronized (this.nonces) {
/* 586 */           info = (DigestAuthenticator.NonceInfo)this.nonces.get(this.nonce); }
/*     */         DigestAuthenticator.NonceInfo info;
/* 588 */         if (info == null)
/*     */         {
/*     */ 
/* 591 */           this.nonceStale = true;
/*     */         }
/* 593 */         else if (!info.nonceCountValid(count)) {
/* 594 */           return false;
/*     */         }
/*     */       }
/*     */       
/* 598 */       return true;
/*     */     }
/*     */     
/*     */     public boolean isNonceStale() {
/* 602 */       return this.nonceStale;
/*     */     }
/*     */     
/*     */ 
/*     */     public Principal authenticate(Realm realm)
/*     */     {
/* 608 */       String a2 = this.method + ":" + this.uri;
/*     */       
/* 610 */       byte[] buffer = ConcurrentMessageDigest.digestMD5(new byte[][] {a2
/* 611 */         .getBytes(StandardCharsets.ISO_8859_1) });
/* 612 */       String md5a2 = MD5Encoder.encode(buffer);
/*     */       
/* 614 */       return realm.authenticate(this.userName, this.response, this.nonce, this.nc, this.cnonce, this.qop, this.realmName, md5a2);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class NonceInfo
/*     */   {
/*     */     private final long timestamp;
/*     */     private final boolean[] seen;
/*     */     private final int offset;
/* 624 */     private int count = 0;
/*     */     
/*     */     public NonceInfo(long currentTime, int seenWindowSize) {
/* 627 */       this.timestamp = currentTime;
/* 628 */       this.seen = new boolean[seenWindowSize];
/* 629 */       this.offset = (seenWindowSize / 2);
/*     */     }
/*     */     
/*     */     public synchronized boolean nonceCountValid(long nonceCount) {
/* 633 */       if ((this.count - this.offset >= nonceCount) || (nonceCount > this.count - this.offset + this.seen.length))
/*     */       {
/* 635 */         return false;
/*     */       }
/* 637 */       int checkIndex = (int)((nonceCount + this.offset) % this.seen.length);
/* 638 */       if (this.seen[checkIndex] != 0) {
/* 639 */         return false;
/*     */       }
/* 641 */       this.seen[checkIndex] = true;
/* 642 */       this.seen[(this.count % this.seen.length)] = false;
/* 643 */       this.count += 1;
/* 644 */       return true;
/*     */     }
/*     */     
/*     */     public long getTimestamp()
/*     */     {
/* 649 */       return this.timestamp;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\authenticator\DigestAuthenticator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */