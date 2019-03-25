/*     */ package org.apache.tomcat.util.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.buf.B2CConverter;
/*     */ import org.apache.tomcat.util.buf.ByteChunk;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
/*     */ import org.apache.tomcat.util.buf.StringUtils;
/*     */ import org.apache.tomcat.util.buf.UDecoder;
/*     */ import org.apache.tomcat.util.log.UserDataHelper;
/*     */ import org.apache.tomcat.util.log.UserDataHelper.Mode;
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
/*     */ public final class Parameters
/*     */ {
/*  45 */   private static final Log log = LogFactory.getLog(Parameters.class);
/*     */   
/*  47 */   private static final UserDataHelper userDataLog = new UserDataHelper(log);
/*     */   
/*  49 */   private static final UserDataHelper maxParamCountLog = new UserDataHelper(log);
/*     */   
/*     */ 
/*  52 */   private static final StringManager sm = StringManager.getManager("org.apache.tomcat.util.http");
/*     */   
/*  54 */   private final Map<String, ArrayList<String>> paramHashValues = new LinkedHashMap();
/*     */   
/*  56 */   private boolean didQueryParameters = false;
/*     */   
/*     */   private MessageBytes queryMB;
/*     */   
/*     */   private UDecoder urlDec;
/*  61 */   private final MessageBytes decodedQuery = MessageBytes.newInstance();
/*     */   
/*  63 */   private Charset charset = StandardCharsets.ISO_8859_1;
/*  64 */   private Charset queryStringCharset = StandardCharsets.UTF_8;
/*     */   
/*  66 */   private int limit = -1;
/*  67 */   private int parameterCount = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  73 */   private FailReason parseFailedReason = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setQuery(MessageBytes queryMB)
/*     */   {
/*  80 */     this.queryMB = queryMB;
/*     */   }
/*     */   
/*     */   public void setLimit(int limit) {
/*  84 */     this.limit = limit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public String getEncoding()
/*     */   {
/*  94 */     return this.charset.name();
/*     */   }
/*     */   
/*     */   public Charset getCharset() {
/*  98 */     return this.charset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setEncoding(String s)
/*     */   {
/* 108 */     setCharset(getCharset(s, DEFAULT_BODY_CHARSET));
/*     */   }
/*     */   
/*     */   public void setCharset(Charset charset) {
/* 112 */     if (charset == null) {
/* 113 */       charset = DEFAULT_BODY_CHARSET;
/*     */     }
/* 115 */     this.charset = charset;
/* 116 */     if (log.isDebugEnabled()) {
/* 117 */       log.debug("Set encoding to " + charset.name());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setQueryStringEncoding(String s)
/*     */   {
/* 128 */     setQueryStringCharset(getCharset(s, DEFAULT_URI_CHARSET));
/*     */   }
/*     */   
/*     */   public void setQueryStringCharset(Charset queryStringCharset) {
/* 132 */     if (queryStringCharset == null) {
/* 133 */       queryStringCharset = DEFAULT_URI_CHARSET;
/*     */     }
/* 135 */     this.queryStringCharset = queryStringCharset;
/*     */     
/* 137 */     if (log.isDebugEnabled()) {
/* 138 */       log.debug("Set query string encoding to " + queryStringCharset.name());
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isParseFailed()
/*     */   {
/* 144 */     return this.parseFailedReason != null;
/*     */   }
/*     */   
/*     */   public FailReason getParseFailedReason()
/*     */   {
/* 149 */     return this.parseFailedReason;
/*     */   }
/*     */   
/*     */   public void setParseFailedReason(FailReason failReason)
/*     */   {
/* 154 */     if (this.parseFailedReason == null) {
/* 155 */       this.parseFailedReason = failReason;
/*     */     }
/*     */   }
/*     */   
/*     */   public void recycle()
/*     */   {
/* 161 */     this.parameterCount = 0;
/* 162 */     this.paramHashValues.clear();
/* 163 */     this.didQueryParameters = false;
/* 164 */     this.charset = DEFAULT_BODY_CHARSET;
/* 165 */     this.decodedQuery.recycle();
/* 166 */     this.parseFailedReason = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getParameterValues(String name)
/*     */   {
/* 175 */     handleQueryParameters();
/*     */     
/* 177 */     ArrayList<String> values = (ArrayList)this.paramHashValues.get(name);
/* 178 */     if (values == null) {
/* 179 */       return null;
/*     */     }
/* 181 */     return (String[])values.toArray(new String[values.size()]);
/*     */   }
/*     */   
/*     */   public Enumeration<String> getParameterNames() {
/* 185 */     handleQueryParameters();
/* 186 */     return Collections.enumeration(this.paramHashValues.keySet());
/*     */   }
/*     */   
/*     */   public String getParameter(String name) {
/* 190 */     handleQueryParameters();
/* 191 */     ArrayList<String> values = (ArrayList)this.paramHashValues.get(name);
/* 192 */     if (values != null) {
/* 193 */       if (values.size() == 0) {
/* 194 */         return "";
/*     */       }
/* 196 */       return (String)values.get(0);
/*     */     }
/* 198 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void handleQueryParameters()
/*     */   {
/* 205 */     if (this.didQueryParameters) {
/* 206 */       return;
/*     */     }
/*     */     
/* 209 */     this.didQueryParameters = true;
/*     */     
/* 211 */     if ((this.queryMB == null) || (this.queryMB.isNull())) {
/* 212 */       return;
/*     */     }
/*     */     
/* 215 */     if (log.isDebugEnabled()) {
/* 216 */       log.debug("Decoding query " + this.decodedQuery + " " + this.queryStringCharset.name());
/*     */     }
/*     */     try
/*     */     {
/* 220 */       this.decodedQuery.duplicate(this.queryMB);
/*     */     }
/*     */     catch (IOException e) {
/* 223 */       e.printStackTrace();
/*     */     }
/* 225 */     processParameters(this.decodedQuery, this.queryStringCharset);
/*     */   }
/*     */   
/*     */ 
/*     */   public void addParameter(String key, String value)
/*     */     throws IllegalStateException
/*     */   {
/* 232 */     if (key == null) {
/* 233 */       return;
/*     */     }
/*     */     
/* 236 */     this.parameterCount += 1;
/* 237 */     if ((this.limit > -1) && (this.parameterCount > this.limit))
/*     */     {
/*     */ 
/* 240 */       setParseFailedReason(FailReason.TOO_MANY_PARAMETERS);
/* 241 */       throw new IllegalStateException(sm.getString("parameters.maxCountFail", new Object[] {
/* 242 */         Integer.valueOf(this.limit) }));
/*     */     }
/*     */     
/* 245 */     ArrayList<String> values = (ArrayList)this.paramHashValues.get(key);
/* 246 */     if (values == null) {
/* 247 */       values = new ArrayList(1);
/* 248 */       this.paramHashValues.put(key, values);
/*     */     }
/* 250 */     values.add(value);
/*     */   }
/*     */   
/*     */   public void setURLDecoder(UDecoder u) {
/* 254 */     this.urlDec = u;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 260 */   private final ByteChunk tmpName = new ByteChunk();
/* 261 */   private final ByteChunk tmpValue = new ByteChunk();
/* 262 */   private final ByteChunk origName = new ByteChunk();
/* 263 */   private final ByteChunk origValue = new ByteChunk();
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public static final String DEFAULT_ENCODING = "ISO-8859-1";
/*     */   
/* 269 */   private static final Charset DEFAULT_BODY_CHARSET = StandardCharsets.ISO_8859_1;
/* 270 */   private static final Charset DEFAULT_URI_CHARSET = StandardCharsets.UTF_8;
/*     */   
/*     */   public void processParameters(byte[] bytes, int start, int len)
/*     */   {
/* 274 */     processParameters(bytes, start, len, this.charset);
/*     */   }
/*     */   
/*     */   private void processParameters(byte[] bytes, int start, int len, Charset charset)
/*     */   {
/* 279 */     if (log.isDebugEnabled()) {
/* 280 */       log.debug(sm.getString("parameters.bytes", new Object[] { new String(bytes, start, len, DEFAULT_BODY_CHARSET) }));
/*     */     }
/*     */     
/*     */ 
/* 284 */     int decodeFailCount = 0;
/*     */     
/* 286 */     int pos = start;
/* 287 */     int end = start + len;
/*     */     
/* 289 */     while (pos < end) {
/* 290 */       int nameStart = pos;
/* 291 */       int nameEnd = -1;
/* 292 */       int valueStart = -1;
/* 293 */       int valueEnd = -1;
/*     */       
/* 295 */       boolean parsingName = true;
/* 296 */       boolean decodeName = false;
/* 297 */       boolean decodeValue = false;
/* 298 */       boolean parameterComplete = false;
/*     */       do
/*     */       {
/* 301 */         switch (bytes[pos]) {
/*     */         case 61: 
/* 303 */           if (parsingName)
/*     */           {
/* 305 */             nameEnd = pos;
/* 306 */             parsingName = false;
/* 307 */             pos++;valueStart = pos;
/*     */           }
/*     */           else {
/* 310 */             pos++;
/*     */           }
/* 312 */           break;
/*     */         case 38: 
/* 314 */           if (parsingName)
/*     */           {
/* 316 */             nameEnd = pos;
/*     */           }
/*     */           else {
/* 319 */             valueEnd = pos;
/*     */           }
/* 321 */           parameterComplete = true;
/* 322 */           pos++;
/* 323 */           break;
/*     */         
/*     */         case 37: 
/*     */         case 43: 
/* 327 */           if (parsingName) {
/* 328 */             decodeName = true;
/*     */           } else {
/* 330 */             decodeValue = true;
/*     */           }
/* 332 */           pos++;
/* 333 */           break;
/*     */         default: 
/* 335 */           pos++;
/*     */         }
/*     */         
/* 338 */       } while ((!parameterComplete) && (pos < end));
/*     */       
/* 340 */       if (pos == end) {
/* 341 */         if (nameEnd == -1) {
/* 342 */           nameEnd = pos;
/* 343 */         } else if ((valueStart > -1) && (valueEnd == -1)) {
/* 344 */           valueEnd = pos;
/*     */         }
/*     */       }
/*     */       
/* 348 */       if ((log.isDebugEnabled()) && (valueStart == -1)) {
/* 349 */         log.debug(sm.getString("parameters.noequal", new Object[] {
/* 350 */           Integer.valueOf(nameStart), Integer.valueOf(nameEnd), new String(bytes, nameStart, nameEnd - nameStart, DEFAULT_BODY_CHARSET) }));
/*     */       }
/*     */       
/*     */ 
/* 354 */       if (nameEnd <= nameStart) {
/* 355 */         if (valueStart == -1)
/*     */         {
/* 357 */           if (log.isDebugEnabled()) {
/* 358 */             log.debug(sm.getString("parameters.emptyChunk"));
/*     */           }
/*     */           
/*     */         }
/*     */         else
/*     */         {
/* 364 */           UserDataHelper.Mode logMode = userDataLog.getNextMode();
/* 365 */           if (logMode != null) { String extract;
/*     */             String extract;
/* 367 */             if (valueEnd > nameStart) {
/* 368 */               extract = new String(bytes, nameStart, valueEnd - nameStart, DEFAULT_BODY_CHARSET);
/*     */             }
/*     */             else {
/* 371 */               extract = "";
/*     */             }
/* 373 */             String message = sm.getString("parameters.invalidChunk", new Object[] {
/* 374 */               Integer.valueOf(nameStart), 
/* 375 */               Integer.valueOf(valueEnd), extract });
/* 376 */             switch (logMode) {
/*     */             case INFO_THEN_DEBUG: 
/* 378 */               message = message + sm.getString("parameters.fallToDebug");
/*     */             
/*     */             case INFO: 
/* 381 */               log.info(message);
/* 382 */               break;
/*     */             case DEBUG: 
/* 384 */               log.debug(message);
/*     */             }
/*     */           }
/* 387 */           setParseFailedReason(FailReason.NO_NAME);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 392 */         this.tmpName.setBytes(bytes, nameStart, nameEnd - nameStart);
/* 393 */         if (valueStart >= 0) {
/* 394 */           this.tmpValue.setBytes(bytes, valueStart, valueEnd - valueStart);
/*     */         } else {
/* 396 */           this.tmpValue.setBytes(bytes, 0, 0);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 402 */         if (log.isDebugEnabled()) {
/*     */           try {
/* 404 */             this.origName.append(bytes, nameStart, nameEnd - nameStart);
/* 405 */             if (valueStart >= 0) {
/* 406 */               this.origValue.append(bytes, valueStart, valueEnd - valueStart);
/*     */             } else {
/* 408 */               this.origValue.append(bytes, 0, 0);
/*     */             }
/*     */           }
/*     */           catch (IOException ioe) {
/* 412 */             log.error(sm.getString("parameters.copyFail"), ioe);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */         try
/*     */         {
/* 420 */           if (decodeName) {
/* 421 */             urlDecode(this.tmpName);
/*     */           }
/* 423 */           this.tmpName.setCharset(charset);
/* 424 */           String name = this.tmpName.toString();
/*     */           String value;
/* 426 */           String value; if (valueStart >= 0) {
/* 427 */             if (decodeValue) {
/* 428 */               urlDecode(this.tmpValue);
/*     */             }
/* 430 */             this.tmpValue.setCharset(charset);
/* 431 */             value = this.tmpValue.toString();
/*     */           } else {
/* 433 */             value = "";
/*     */           }
/*     */           try
/*     */           {
/* 437 */             addParameter(name, value);
/*     */           }
/*     */           catch (IllegalStateException ise)
/*     */           {
/* 441 */             UserDataHelper.Mode logMode = maxParamCountLog.getNextMode();
/* 442 */             if (logMode != null) {
/* 443 */               String message = ise.getMessage();
/* 444 */               switch (logMode) {
/*     */               case INFO_THEN_DEBUG: 
/* 446 */                 message = message + sm.getString("parameters.maxCountFail.fallToDebug");
/*     */               
/*     */ 
/*     */               case INFO: 
/* 450 */                 log.info(message);
/* 451 */                 break;
/*     */               case DEBUG: 
/* 453 */                 log.debug(message);
/*     */               }
/*     */             }
/* 456 */             break;
/*     */           }
/*     */         } catch (IOException e) {
/* 459 */           setParseFailedReason(FailReason.URL_DECODING);
/* 460 */           decodeFailCount++;
/* 461 */           if ((decodeFailCount == 1) || (log.isDebugEnabled())) {
/* 462 */             if (log.isDebugEnabled()) {
/* 463 */               log.debug(sm.getString("parameters.decodeFail.debug", new Object[] {this.origName
/* 464 */                 .toString(), this.origValue.toString() }), e);
/* 465 */             } else if (log.isInfoEnabled()) {
/* 466 */               UserDataHelper.Mode logMode = userDataLog.getNextMode();
/* 467 */               if (logMode != null) {
/* 468 */                 String message = sm.getString("parameters.decodeFail.info", new Object[] {this.tmpName
/*     */                 
/* 470 */                   .toString(), this.tmpValue.toString() });
/* 471 */                 switch (logMode) {
/*     */                 case INFO_THEN_DEBUG: 
/* 473 */                   message = message + sm.getString("parameters.fallToDebug");
/*     */                 
/*     */                 case INFO: 
/* 476 */                   log.info(message);
/* 477 */                   break;
/*     */                 case DEBUG: 
/* 479 */                   log.debug(message);
/*     */                 }
/*     */                 
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 486 */         this.tmpName.recycle();
/* 487 */         this.tmpValue.recycle();
/*     */         
/* 489 */         if (log.isDebugEnabled()) {
/* 490 */           this.origName.recycle();
/* 491 */           this.origValue.recycle();
/*     */         }
/*     */       }
/*     */     }
/* 495 */     if ((decodeFailCount > 1) && (!log.isDebugEnabled())) {
/* 496 */       UserDataHelper.Mode logMode = userDataLog.getNextMode();
/* 497 */       if (logMode != null) {
/* 498 */         String message = sm.getString("parameters.multipleDecodingFail", new Object[] {
/*     */         
/* 500 */           Integer.valueOf(decodeFailCount) });
/* 501 */         switch (logMode) {
/*     */         case INFO_THEN_DEBUG: 
/* 503 */           message = message + sm.getString("parameters.fallToDebug");
/*     */         
/*     */         case INFO: 
/* 506 */           log.info(message);
/* 507 */           break;
/*     */         case DEBUG: 
/* 509 */           log.debug(message);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void urlDecode(ByteChunk bc) throws IOException
/*     */   {
/* 517 */     if (this.urlDec == null) {
/* 518 */       this.urlDec = new UDecoder();
/*     */     }
/* 520 */     this.urlDec.convert(bc, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void processParameters(MessageBytes data, String encoding)
/*     */   {
/* 531 */     processParameters(data, getCharset(encoding, DEFAULT_BODY_CHARSET));
/*     */   }
/*     */   
/*     */   public void processParameters(MessageBytes data, Charset charset) {
/* 535 */     if ((data == null) || (data.isNull()) || (data.getLength() <= 0)) {
/* 536 */       return;
/*     */     }
/*     */     
/* 539 */     if (data.getType() != 2) {
/* 540 */       data.toBytes();
/*     */     }
/* 542 */     ByteChunk bc = data.getByteChunk();
/* 543 */     processParameters(bc.getBytes(), bc.getOffset(), bc.getLength(), charset);
/*     */   }
/*     */   
/*     */   private Charset getCharset(String encoding, Charset defaultCharset) {
/* 547 */     if (encoding == null) {
/* 548 */       return defaultCharset;
/*     */     }
/*     */     try {
/* 551 */       return B2CConverter.getCharset(encoding);
/*     */     } catch (UnsupportedEncodingException e) {}
/* 553 */     return defaultCharset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 562 */     StringBuilder sb = new StringBuilder();
/* 563 */     for (Map.Entry<String, ArrayList<String>> e : this.paramHashValues.entrySet()) {
/* 564 */       sb.append((String)e.getKey()).append('=');
/* 565 */       StringUtils.join((Iterable)e.getValue(), ',', sb);
/* 566 */       sb.append('\n');
/*     */     }
/* 568 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static enum FailReason
/*     */   {
/* 573 */     CLIENT_DISCONNECT, 
/* 574 */     MULTIPART_CONFIG_INVALID, 
/* 575 */     INVALID_CONTENT_TYPE, 
/* 576 */     IO_ERROR, 
/* 577 */     NO_NAME, 
/* 578 */     POST_TOO_LARGE, 
/* 579 */     REQUEST_BODY_INCOMPLETE, 
/* 580 */     TOO_MANY_PARAMETERS, 
/* 581 */     UNKNOWN, 
/* 582 */     URL_DECODING;
/*     */     
/*     */     private FailReason() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\Parameters.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */