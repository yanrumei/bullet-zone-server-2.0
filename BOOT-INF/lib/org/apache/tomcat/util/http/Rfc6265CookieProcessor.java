/*     */ package org.apache.tomcat.util.http;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.text.DateFormat;
/*     */ import java.text.FieldPosition;
/*     */ import java.util.BitSet;
/*     */ import java.util.Date;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.buf.ByteChunk;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
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
/*     */ public class Rfc6265CookieProcessor
/*     */   extends CookieProcessorBase
/*     */ {
/*  34 */   private static final Log log = LogFactory.getLog(Rfc6265CookieProcessor.class);
/*     */   
/*     */ 
/*  37 */   private static final StringManager sm = StringManager.getManager(Rfc6265CookieProcessor.class.getPackage().getName());
/*     */   
/*  39 */   private static final BitSet domainValid = new BitSet(128);
/*     */   
/*     */   static {
/*  42 */     for (char c = '0'; c <= '9'; c = (char)(c + '\001')) {
/*  43 */       domainValid.set(c);
/*     */     }
/*  45 */     for (char c = 'a'; c <= 'z'; c = (char)(c + '\001')) {
/*  46 */       domainValid.set(c);
/*     */     }
/*  48 */     for (char c = 'A'; c <= 'Z'; c = (char)(c + '\001')) {
/*  49 */       domainValid.set(c);
/*     */     }
/*  51 */     domainValid.set(46);
/*  52 */     domainValid.set(45);
/*     */   }
/*     */   
/*     */ 
/*     */   public Charset getCharset()
/*     */   {
/*  58 */     return StandardCharsets.UTF_8;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void parseCookieHeader(MimeHeaders headers, ServerCookies serverCookies)
/*     */   {
/*  66 */     if (headers == null)
/*     */     {
/*  68 */       return;
/*     */     }
/*     */     
/*     */ 
/*  72 */     int pos = headers.findHeader("Cookie", 0);
/*  73 */     while (pos >= 0) {
/*  74 */       MessageBytes cookieValue = headers.getValue(pos);
/*     */       
/*  76 */       if ((cookieValue != null) && (!cookieValue.isNull())) {
/*  77 */         if (cookieValue.getType() != 2) {
/*  78 */           if (log.isDebugEnabled()) {
/*  79 */             Exception e = new Exception();
/*     */             
/*  81 */             log.debug("Cookies: Parsing cookie as String. Expected bytes.", e);
/*     */           }
/*  83 */           cookieValue.toBytes();
/*     */         }
/*  85 */         if (log.isDebugEnabled()) {
/*  86 */           log.debug("Cookies: Parsing b[]: " + cookieValue.toString());
/*     */         }
/*  88 */         ByteChunk bc = cookieValue.getByteChunk();
/*     */         
/*  90 */         org.apache.tomcat.util.http.parser.Cookie.parseCookie(bc.getBytes(), bc.getOffset(), bc.getLength(), serverCookies);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  95 */       pos = headers.findHeader("Cookie", ++pos);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String generateHeader(javax.servlet.http.Cookie cookie)
/*     */   {
/* 104 */     StringBuffer header = new StringBuffer();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 111 */     header.append(cookie.getName());
/* 112 */     header.append('=');
/* 113 */     String value = cookie.getValue();
/* 114 */     if ((value != null) && (value.length() > 0)) {
/* 115 */       validateCookieValue(value);
/* 116 */       header.append(value);
/*     */     }
/*     */     
/*     */ 
/* 120 */     int maxAge = cookie.getMaxAge();
/* 121 */     if (maxAge > -1)
/*     */     {
/* 123 */       header.append("; Max-Age=");
/* 124 */       header.append(maxAge);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 131 */       header.append("; Expires=");
/*     */       
/* 133 */       if (maxAge == 0) {
/* 134 */         header.append(ANCIENT_DATE);
/*     */       } else {
/* 136 */         ((DateFormat)COOKIE_DATE_FORMAT.get()).format(new Date(
/* 137 */           System.currentTimeMillis() + maxAge * 1000L), header, new FieldPosition(0));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 143 */     String domain = cookie.getDomain();
/* 144 */     if ((domain != null) && (domain.length() > 0)) {
/* 145 */       validateDomain(domain);
/* 146 */       header.append("; Domain=");
/* 147 */       header.append(domain);
/*     */     }
/*     */     
/* 150 */     String path = cookie.getPath();
/* 151 */     if ((path != null) && (path.length() > 0)) {
/* 152 */       validatePath(path);
/* 153 */       header.append("; Path=");
/* 154 */       header.append(path);
/*     */     }
/*     */     
/* 157 */     if (cookie.getSecure()) {
/* 158 */       header.append("; Secure");
/*     */     }
/*     */     
/* 161 */     if (cookie.isHttpOnly()) {
/* 162 */       header.append("; HttpOnly");
/*     */     }
/*     */     
/* 165 */     return header.toString();
/*     */   }
/*     */   
/*     */   private void validateCookieValue(String value)
/*     */   {
/* 170 */     int start = 0;
/* 171 */     int end = value.length();
/*     */     
/* 173 */     if ((end > 1) && (value.charAt(0) == '"') && (value.charAt(end - 1) == '"')) {
/* 174 */       start = 1;
/* 175 */       end--;
/*     */     }
/*     */     
/* 178 */     char[] chars = value.toCharArray();
/* 179 */     for (int i = start; i < end; i++) {
/* 180 */       char c = chars[i];
/* 181 */       if ((c < '!') || (c == '"') || (c == ',') || (c == ';') || (c == '\\') || (c == '')) {
/* 182 */         throw new IllegalArgumentException(sm.getString("rfc6265CookieProcessor.invalidCharInValue", new Object[] {
/* 183 */           Integer.toString(c) }));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void validateDomain(String domain)
/*     */   {
/* 190 */     int i = 0;
/* 191 */     int prev = -1;
/* 192 */     int cur = -1;
/* 193 */     char[] chars = domain.toCharArray();
/* 194 */     while (i < chars.length) {
/* 195 */       prev = cur;
/* 196 */       cur = chars[i];
/* 197 */       if (!domainValid.get(cur)) {
/* 198 */         throw new IllegalArgumentException(sm.getString("rfc6265CookieProcessor.invalidDomain", new Object[] { domain }));
/*     */       }
/*     */       
/*     */ 
/* 202 */       if (((prev == 46) || (prev == -1)) && ((cur == 46) || (cur == 45))) {
/* 203 */         throw new IllegalArgumentException(sm.getString("rfc6265CookieProcessor.invalidDomain", new Object[] { domain }));
/*     */       }
/*     */       
/*     */ 
/* 207 */       if ((prev == 45) && (cur == 46)) {
/* 208 */         throw new IllegalArgumentException(sm.getString("rfc6265CookieProcessor.invalidDomain", new Object[] { domain }));
/*     */       }
/*     */       
/* 211 */       i++;
/*     */     }
/*     */     
/* 214 */     if ((cur == 46) || (cur == 45)) {
/* 215 */       throw new IllegalArgumentException(sm.getString("rfc6265CookieProcessor.invalidDomain", new Object[] { domain }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void validatePath(String path)
/*     */   {
/* 222 */     char[] chars = path.toCharArray();
/*     */     
/* 224 */     for (int i = 0; i < chars.length; i++) {
/* 225 */       char ch = chars[i];
/* 226 */       if ((ch < ' ') || (ch > '~') || (ch == ';')) {
/* 227 */         throw new IllegalArgumentException(sm.getString("rfc6265CookieProcessor.invalidPath", new Object[] { path }));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\Rfc6265CookieProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */