/*     */ package org.apache.tomcat.util.http.parser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpParser
/*     */ {
/*  41 */   private static final StringManager sm = StringManager.getManager(HttpParser.class);
/*     */   
/*  43 */   private static final Log log = LogFactory.getLog(HttpParser.class);
/*     */   
/*     */   private static final int ARRAY_SIZE = 128;
/*     */   
/*  47 */   private static final boolean[] IS_CONTROL = new boolean[''];
/*  48 */   private static final boolean[] IS_SEPARATOR = new boolean[''];
/*  49 */   private static final boolean[] IS_TOKEN = new boolean[''];
/*  50 */   private static final boolean[] IS_HEX = new boolean[''];
/*  51 */   private static final boolean[] IS_NOT_REQUEST_TARGET = new boolean[''];
/*  52 */   private static final boolean[] IS_HTTP_PROTOCOL = new boolean[''];
/*  53 */   private static final boolean[] REQUEST_TARGET_ALLOW = new boolean[''];
/*     */   
/*     */   static {
/*  56 */     String prop = System.getProperty("tomcat.util.http.parser.HttpParser.requestTargetAllow");
/*  57 */     if (prop != null) {
/*  58 */       for (int i = 0; i < prop.length(); i++) {
/*  59 */         char c = prop.charAt(i);
/*  60 */         if ((c == '{') || (c == '}') || (c == '|')) {
/*  61 */           REQUEST_TARGET_ALLOW[c] = true;
/*     */         } else {
/*  63 */           log.warn(sm.getString("httpparser.invalidRequestTargetCharacter", new Object[] {
/*  64 */             Character.valueOf(c) }));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  69 */     for (int i = 0; i < 128; i++)
/*     */     {
/*  71 */       if ((i < 32) || (i == 127)) {
/*  72 */         IS_CONTROL[i] = true;
/*     */       }
/*     */       
/*     */ 
/*  76 */       if ((i == 40) || (i == 41) || (i == 60) || (i == 62) || (i == 64) || (i == 44) || (i == 59) || (i == 58) || (i == 92) || (i == 34) || (i == 47) || (i == 91) || (i == 93) || (i == 63) || (i == 61) || (i == 123) || (i == 125) || (i == 32) || (i == 9))
/*     */       {
/*     */ 
/*     */ 
/*  80 */         IS_SEPARATOR[i] = true;
/*     */       }
/*     */       
/*     */ 
/*  84 */       if ((IS_CONTROL[i] == 0) && (IS_SEPARATOR[i] == 0) && (i < 128)) {
/*  85 */         IS_TOKEN[i] = true;
/*     */       }
/*     */       
/*     */ 
/*  89 */       if (((i >= 48) && (i <= 57)) || ((i >= 97) && (i <= 102)) || ((i >= 65) && (i <= 70))) {
/*  90 */         IS_HEX[i] = true;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  96 */       if ((IS_CONTROL[i] != 0) || (i > 127) || (i == 32) || (i == 34) || (i == 35) || (i == 60) || (i == 62) || (i == 92) || (i == 94) || (i == 96) || (i == 123) || (i == 124) || (i == 125))
/*     */       {
/*     */ 
/*  99 */         if (REQUEST_TARGET_ALLOW[i] == 0) {
/* 100 */           IS_NOT_REQUEST_TARGET[i] = true;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 106 */       if ((i == 72) || (i == 84) || (i == 80) || (i == 47) || (i == 46) || ((i >= 48) && (i <= 57))) {
/* 107 */         IS_HTTP_PROTOCOL[i] = true;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static String unquote(String input)
/*     */   {
/* 114 */     if ((input == null) || (input.length() < 2)) {
/* 115 */       return input;
/*     */     }
/*     */     
/*     */     int end;
/*     */     
/*     */     int start;
/*     */     int end;
/* 122 */     if (input.charAt(0) == '"') {
/* 123 */       int start = 1;
/* 124 */       end = input.length() - 1;
/*     */     } else {
/* 126 */       start = 0;
/* 127 */       end = input.length();
/*     */     }
/*     */     
/* 130 */     StringBuilder result = new StringBuilder();
/* 131 */     for (int i = start; i < end; i++) {
/* 132 */       char c = input.charAt(i);
/* 133 */       if (input.charAt(i) == '\\') {
/* 134 */         i++;
/* 135 */         result.append(input.charAt(i));
/*     */       } else {
/* 137 */         result.append(c);
/*     */       }
/*     */     }
/* 140 */     return result.toString();
/*     */   }
/*     */   
/*     */   public static boolean isToken(int c)
/*     */   {
/*     */     try
/*     */     {
/* 147 */       return IS_TOKEN[c];
/*     */     } catch (ArrayIndexOutOfBoundsException ex) {}
/* 149 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean isHex(int c)
/*     */   {
/*     */     try
/*     */     {
/* 157 */       return IS_HEX[c];
/*     */     } catch (ArrayIndexOutOfBoundsException ex) {}
/* 159 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isNotRequestTarget(int c)
/*     */   {
/*     */     try
/*     */     {
/* 168 */       return IS_NOT_REQUEST_TARGET[c];
/*     */     } catch (ArrayIndexOutOfBoundsException ex) {}
/* 170 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isHttpProtocol(int c)
/*     */   {
/*     */     try
/*     */     {
/* 179 */       return IS_HTTP_PROTOCOL[c];
/*     */     } catch (ArrayIndexOutOfBoundsException ex) {}
/* 181 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static int skipLws(StringReader input, boolean withReset)
/*     */     throws IOException
/*     */   {
/* 189 */     if (withReset) {
/* 190 */       input.mark(1);
/*     */     }
/* 192 */     int c = input.read();
/*     */     
/* 194 */     while ((c == 32) || (c == 9) || (c == 10) || (c == 13)) {
/* 195 */       if (withReset) {
/* 196 */         input.mark(1);
/*     */       }
/* 198 */       c = input.read();
/*     */     }
/*     */     
/* 201 */     if (withReset) {
/* 202 */       input.reset();
/*     */     }
/* 204 */     return c;
/*     */   }
/*     */   
/*     */   static SkipResult skipConstant(StringReader input, String constant) throws IOException {
/* 208 */     int len = constant.length();
/*     */     
/* 210 */     int c = skipLws(input, false);
/*     */     
/* 212 */     for (int i = 0; i < len; i++) {
/* 213 */       if ((i == 0) && (c == -1)) {
/* 214 */         return SkipResult.EOF;
/*     */       }
/* 216 */       if (c != constant.charAt(i)) {
/* 217 */         input.skip(-(i + 1));
/* 218 */         return SkipResult.NOT_FOUND;
/*     */       }
/* 220 */       if (i != len - 1) {
/* 221 */         c = input.read();
/*     */       }
/*     */     }
/* 224 */     return SkipResult.FOUND;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static String readToken(StringReader input)
/*     */     throws IOException
/*     */   {
/* 233 */     StringBuilder result = new StringBuilder();
/*     */     
/* 235 */     int c = skipLws(input, false);
/*     */     
/* 237 */     while ((c != -1) && (isToken(c))) {
/* 238 */       result.append((char)c);
/* 239 */       c = input.read();
/*     */     }
/*     */     
/* 242 */     input.skip(-1L);
/*     */     
/* 244 */     if ((c != -1) && (result.length() == 0)) {
/* 245 */       return null;
/*     */     }
/* 247 */     return result.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static String readQuotedString(StringReader input, boolean returnQuoted)
/*     */     throws IOException
/*     */   {
/* 258 */     int c = skipLws(input, false);
/*     */     
/* 260 */     if (c != 34) {
/* 261 */       return null;
/*     */     }
/*     */     
/* 264 */     StringBuilder result = new StringBuilder();
/* 265 */     if (returnQuoted) {
/* 266 */       result.append('"');
/*     */     }
/* 268 */     c = input.read();
/*     */     
/* 270 */     while (c != 34) {
/* 271 */       if (c == -1)
/* 272 */         return null;
/* 273 */       if (c == 92) {
/* 274 */         c = input.read();
/* 275 */         if (returnQuoted) {
/* 276 */           result.append('\\');
/*     */         }
/* 278 */         result.append(c);
/*     */       } else {
/* 280 */         result.append((char)c);
/*     */       }
/* 282 */       c = input.read();
/*     */     }
/* 284 */     if (returnQuoted) {
/* 285 */       result.append('"');
/*     */     }
/*     */     
/* 288 */     return result.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   static String readTokenOrQuotedString(StringReader input, boolean returnQuoted)
/*     */     throws IOException
/*     */   {
/* 295 */     int c = skipLws(input, true);
/*     */     
/* 297 */     if (c == 34) {
/* 298 */       return readQuotedString(input, returnQuoted);
/*     */     }
/* 300 */     return readToken(input);
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
/*     */   static String readQuotedToken(StringReader input)
/*     */     throws IOException
/*     */   {
/* 318 */     StringBuilder result = new StringBuilder();
/* 319 */     boolean quoted = false;
/*     */     
/* 321 */     int c = skipLws(input, false);
/*     */     
/* 323 */     if (c == 34) {
/* 324 */       quoted = true;
/* 325 */     } else { if ((c == -1) || (!isToken(c))) {
/* 326 */         return null;
/*     */       }
/* 328 */       result.append((char)c);
/*     */     }
/* 330 */     c = input.read();
/*     */     
/* 332 */     while ((c != -1) && (isToken(c))) {
/* 333 */       result.append((char)c);
/* 334 */       c = input.read();
/*     */     }
/*     */     
/* 337 */     if (quoted) {
/* 338 */       if (c != 34) {
/* 339 */         return null;
/*     */       }
/*     */     }
/*     */     else {
/* 343 */       input.skip(-1L);
/*     */     }
/*     */     
/* 346 */     if ((c != -1) && (result.length() == 0)) {
/* 347 */       return null;
/*     */     }
/* 349 */     return result.toString();
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
/*     */   static String readLhex(StringReader input)
/*     */     throws IOException
/*     */   {
/* 369 */     StringBuilder result = new StringBuilder();
/* 370 */     boolean quoted = false;
/*     */     
/* 372 */     int c = skipLws(input, false);
/*     */     
/* 374 */     if (c == 34) {
/* 375 */       quoted = true;
/* 376 */     } else { if ((c == -1) || (!isHex(c))) {
/* 377 */         return null;
/*     */       }
/* 379 */       if ((65 <= c) && (c <= 70)) {
/* 380 */         c += 32;
/*     */       }
/* 382 */       result.append((char)c);
/*     */     }
/* 384 */     c = input.read();
/*     */     
/* 386 */     while ((c != -1) && (isHex(c))) {
/* 387 */       if ((65 <= c) && (c <= 70)) {
/* 388 */         c += 32;
/*     */       }
/* 390 */       result.append((char)c);
/* 391 */       c = input.read();
/*     */     }
/*     */     
/* 394 */     if (quoted) {
/* 395 */       if (c != 34) {
/* 396 */         return null;
/*     */       }
/*     */     }
/*     */     else {
/* 400 */       input.skip(-1L);
/*     */     }
/*     */     
/* 403 */     if ((c != -1) && (result.length() == 0)) {
/* 404 */       return null;
/*     */     }
/* 406 */     return result.toString();
/*     */   }
/*     */   
/*     */   static double readWeight(StringReader input, char delimiter) throws IOException
/*     */   {
/* 411 */     int c = skipLws(input, false);
/* 412 */     if ((c == -1) || (c == delimiter))
/*     */     {
/* 414 */       return 1.0D; }
/* 415 */     if (c != 113)
/*     */     {
/* 417 */       skipUntil(input, c, delimiter);
/* 418 */       return 0.0D;
/*     */     }
/*     */     
/* 421 */     c = skipLws(input, false);
/* 422 */     if (c != 61)
/*     */     {
/* 424 */       skipUntil(input, c, delimiter);
/* 425 */       return 0.0D;
/*     */     }
/*     */     
/*     */ 
/* 429 */     c = skipLws(input, false);
/*     */     
/*     */ 
/* 432 */     StringBuilder value = new StringBuilder(5);
/* 433 */     int decimalPlacesRead = 0;
/* 434 */     if ((c == 48) || (c == 49)) {
/* 435 */       value.append((char)c);
/* 436 */       c = input.read();
/* 437 */       if (c == 46) {
/* 438 */         value.append('.');
/* 439 */       } else if ((c < 48) || (c > 57)) {
/* 440 */         decimalPlacesRead = 3;
/*     */       }
/*     */       for (;;) {
/* 443 */         c = input.read();
/* 444 */         if ((c < 48) || (c > 57)) break;
/* 445 */         if (decimalPlacesRead < 3) {
/* 446 */           value.append((char)c);
/* 447 */           decimalPlacesRead++;
/*     */         } }
/* 449 */       if ((c != delimiter) && (c != 9) && (c != 32) && (c != -1))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 454 */         skipUntil(input, c, delimiter);
/* 455 */         return 0.0D;
/*     */       }
/*     */       
/*     */     }
/*     */     else
/*     */     {
/* 461 */       skipUntil(input, c, delimiter);
/* 462 */       return 0.0D;
/*     */     }
/*     */     
/* 465 */     double result = Double.parseDouble(value.toString());
/* 466 */     if (result > 1.0D) {
/* 467 */       return 0.0D;
/*     */     }
/* 469 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static SkipResult skipUntil(StringReader input, int c, char target)
/*     */     throws IOException
/*     */   {
/* 478 */     while ((c != -1) && (c != target)) {
/* 479 */       c = input.read();
/*     */     }
/* 481 */     if (c == -1) {
/* 482 */       return SkipResult.EOF;
/*     */     }
/* 484 */     return SkipResult.FOUND;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\parser\HttpParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */