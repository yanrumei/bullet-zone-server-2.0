/*     */ package org.apache.coyote.http2;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.apache.coyote.ProtocolException;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.buf.ByteBufferUtils;
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
/*     */ class Http2Parser
/*     */ {
/*  32 */   private static final Log log = LogFactory.getLog(Http2Parser.class);
/*  33 */   private static final StringManager sm = StringManager.getManager(Http2Parser.class);
/*     */   
/*  35 */   static final byte[] CLIENT_PREFACE_START = "PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n"
/*  36 */     .getBytes(StandardCharsets.ISO_8859_1);
/*     */   
/*     */   private final String connectionId;
/*     */   private final Input input;
/*     */   private final Output output;
/*  41 */   private final byte[] frameHeaderBuffer = new byte[9];
/*     */   
/*     */   private volatile HpackDecoder hpackDecoder;
/*     */   
/*  45 */   private volatile ByteBuffer headerReadBuffer = ByteBuffer.allocate(1024);
/*  46 */   private volatile int headersCurrentStream = -1;
/*  47 */   private volatile boolean headersEndStream = false;
/*     */   
/*     */   Http2Parser(String connectionId, Input input, Output output) {
/*  50 */     this.connectionId = connectionId;
/*  51 */     this.input = input;
/*  52 */     this.output = output;
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
/*     */   boolean readFrame(boolean block)
/*     */     throws Http2Exception, IOException
/*     */   {
/*  69 */     return readFrame(block, null);
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean readFrame(boolean block, FrameType expected)
/*     */     throws IOException, Http2Exception
/*     */   {
/*  76 */     if (!this.input.fill(block, this.frameHeaderBuffer)) {
/*  77 */       return false;
/*     */     }
/*     */     
/*  80 */     int payloadSize = ByteUtil.getThreeBytes(this.frameHeaderBuffer, 0);
/*  81 */     FrameType frameType = FrameType.valueOf(ByteUtil.getOneByte(this.frameHeaderBuffer, 3));
/*  82 */     int flags = ByteUtil.getOneByte(this.frameHeaderBuffer, 4);
/*  83 */     int streamId = ByteUtil.get31Bits(this.frameHeaderBuffer, 5);
/*     */     try
/*     */     {
/*  86 */       validateFrame(expected, frameType, streamId, flags, payloadSize);
/*     */     } catch (StreamException se) {
/*  88 */       swallow(streamId, payloadSize, false);
/*  89 */       throw se;
/*     */     }
/*     */     
/*  92 */     switch (frameType) {
/*     */     case DATA: 
/*  94 */       readDataFrame(streamId, flags, payloadSize);
/*  95 */       break;
/*     */     case HEADERS: 
/*  97 */       readHeadersFrame(streamId, flags, payloadSize);
/*  98 */       break;
/*     */     case PRIORITY: 
/* 100 */       readPriorityFrame(streamId);
/* 101 */       break;
/*     */     case RST: 
/* 103 */       readRstFrame(streamId);
/* 104 */       break;
/*     */     case SETTINGS: 
/* 106 */       readSettingsFrame(flags, payloadSize);
/* 107 */       break;
/*     */     case PUSH_PROMISE: 
/* 109 */       readPushPromiseFrame(streamId);
/* 110 */       break;
/*     */     case PING: 
/* 112 */       readPingFrame(flags);
/* 113 */       break;
/*     */     case GOAWAY: 
/* 115 */       readGoawayFrame(payloadSize);
/* 116 */       break;
/*     */     case WINDOW_UPDATE: 
/* 118 */       readWindowUpdateFrame(streamId);
/* 119 */       break;
/*     */     case CONTINUATION: 
/* 121 */       readContinuationFrame(streamId, flags, payloadSize);
/* 122 */       break;
/*     */     case UNKNOWN: 
/* 124 */       readUnknownFrame(streamId, frameType, flags, payloadSize);
/*     */     }
/*     */     
/* 127 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private void readDataFrame(int streamId, int flags, int payloadSize)
/*     */     throws Http2Exception, IOException
/*     */   {
/* 134 */     int padLength = 0;
/*     */     
/* 136 */     boolean endOfStream = Flags.isEndOfStream(flags);
/*     */     int dataLength;
/*     */     int dataLength;
/* 139 */     if (Flags.hasPadding(flags)) {
/* 140 */       byte[] b = new byte[1];
/* 141 */       this.input.fill(true, b);
/* 142 */       padLength = b[0] & 0xFF;
/*     */       
/* 144 */       if (padLength >= payloadSize)
/*     */       {
/* 146 */         throw new ConnectionException(sm.getString("http2Parser.processFrame.tooMuchPadding", new Object[] { this.connectionId, 
/* 147 */           Integer.toString(streamId), Integer.toString(padLength), 
/* 148 */           Integer.toString(payloadSize) }), Http2Error.PROTOCOL_ERROR);
/*     */       }
/*     */       
/* 151 */       dataLength = payloadSize - (padLength + 1);
/*     */     } else {
/* 153 */       dataLength = payloadSize;
/*     */     }
/*     */     
/* 156 */     if (log.isDebugEnabled()) { String padding;
/*     */       String padding;
/* 158 */       if (Flags.hasPadding(flags)) {
/* 159 */         padding = Integer.toString(padLength);
/*     */       } else {
/* 161 */         padding = "none";
/*     */       }
/* 163 */       log.debug(sm.getString("http2Parser.processFrameData.lengths", new Object[] { this.connectionId, 
/* 164 */         Integer.toString(streamId), Integer.toString(dataLength), padding }));
/*     */     }
/*     */     
/* 167 */     ByteBuffer dest = this.output.startRequestBodyFrame(streamId, payloadSize);
/* 168 */     if (dest == null) {
/* 169 */       swallow(streamId, dataLength, false);
/*     */       
/*     */ 
/* 172 */       if (padLength > 0) {
/* 173 */         swallow(streamId, padLength, true);
/*     */       }
/* 175 */       if (endOfStream) {
/* 176 */         this.output.receivedEndOfStream(streamId);
/*     */       }
/*     */     } else {
/* 179 */       synchronized (dest) {
/* 180 */         if (dest.remaining() < dataLength) {
/* 181 */           swallow(streamId, dataLength, false);
/*     */           
/* 183 */           throw new StreamException("Client sent more data than stream window allowed", Http2Error.FLOW_CONTROL_ERROR, streamId);
/*     */         }
/* 185 */         this.input.fill(true, dest, dataLength);
/*     */         
/*     */ 
/* 188 */         if (padLength > 0) {
/* 189 */           swallow(streamId, padLength, true);
/*     */         }
/* 191 */         if (endOfStream) {
/* 192 */           this.output.receivedEndOfStream(streamId);
/*     */         }
/* 194 */         this.output.endRequestBodyFrame(streamId);
/*     */       }
/*     */     }
/* 197 */     if (padLength > 0) {
/* 198 */       this.output.swallowedPadding(streamId, padLength);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void readHeadersFrame(int streamId, int flags, int payloadSize)
/*     */     throws Http2Exception, IOException
/*     */   {
/* 206 */     this.headersEndStream = Flags.isEndOfStream(flags);
/*     */     
/* 208 */     if (this.hpackDecoder == null) {
/* 209 */       this.hpackDecoder = this.output.getHpackDecoder();
/*     */     }
/*     */     try {
/* 212 */       this.hpackDecoder.setHeaderEmitter(this.output.headersStart(streamId, this.headersEndStream));
/*     */     } catch (StreamException se) {
/* 214 */       swallow(streamId, payloadSize, false);
/* 215 */       throw se;
/*     */     }
/*     */     
/* 218 */     int padLength = 0;
/* 219 */     boolean padding = Flags.hasPadding(flags);
/* 220 */     boolean priority = Flags.hasPriority(flags);
/* 221 */     int optionalLen = 0;
/* 222 */     if (padding) {
/* 223 */       optionalLen = 1;
/*     */     }
/* 225 */     if (priority) {
/* 226 */       optionalLen += 5;
/*     */     }
/* 228 */     if (optionalLen > 0) {
/* 229 */       byte[] optional = new byte[optionalLen];
/* 230 */       this.input.fill(true, optional);
/* 231 */       int optionalPos = 0;
/* 232 */       if (padding) {
/* 233 */         padLength = ByteUtil.getOneByte(optional, optionalPos++);
/* 234 */         if (padLength >= payloadSize)
/*     */         {
/* 236 */           throw new ConnectionException(sm.getString("http2Parser.processFrame.tooMuchPadding", new Object[] { this.connectionId, 
/* 237 */             Integer.toString(streamId), Integer.toString(padLength), 
/* 238 */             Integer.toString(payloadSize) }), Http2Error.PROTOCOL_ERROR);
/*     */         }
/*     */       }
/* 241 */       if (priority) {
/* 242 */         boolean exclusive = ByteUtil.isBit7Set(optional[optionalPos]);
/* 243 */         int parentStreamId = ByteUtil.get31Bits(optional, optionalPos);
/* 244 */         int weight = ByteUtil.getOneByte(optional, optionalPos + 4) + 1;
/* 245 */         this.output.reprioritise(streamId, parentStreamId, exclusive, weight);
/*     */       }
/*     */       
/* 248 */       payloadSize -= optionalLen;
/* 249 */       payloadSize -= padLength;
/*     */     }
/*     */     
/* 252 */     readHeaderPayload(streamId, payloadSize);
/*     */     
/* 254 */     swallow(streamId, padLength, true);
/*     */     
/* 256 */     if (Flags.isEndOfHeaders(flags)) {
/* 257 */       onHeadersComplete(streamId);
/*     */     } else {
/* 259 */       this.headersCurrentStream = streamId;
/*     */     }
/*     */   }
/*     */   
/*     */   private void readPriorityFrame(int streamId) throws Http2Exception, IOException
/*     */   {
/* 265 */     byte[] payload = new byte[5];
/* 266 */     this.input.fill(true, payload);
/*     */     
/* 268 */     boolean exclusive = ByteUtil.isBit7Set(payload[0]);
/* 269 */     int parentStreamId = ByteUtil.get31Bits(payload, 0);
/* 270 */     int weight = ByteUtil.getOneByte(payload, 4) + 1;
/*     */     
/* 272 */     if (streamId == parentStreamId) {
/* 273 */       throw new StreamException(sm.getString("http2Parser.processFramePriority.invalidParent", new Object[] { this.connectionId, 
/* 274 */         Integer.valueOf(streamId) }), Http2Error.PROTOCOL_ERROR, streamId);
/*     */     }
/*     */     
/* 277 */     this.output.reprioritise(streamId, parentStreamId, exclusive, weight);
/*     */   }
/*     */   
/*     */   private void readRstFrame(int streamId) throws Http2Exception, IOException
/*     */   {
/* 282 */     byte[] payload = new byte[4];
/* 283 */     this.input.fill(true, payload);
/*     */     
/* 285 */     long errorCode = ByteUtil.getFourBytes(payload, 0);
/* 286 */     this.output.reset(streamId, errorCode);
/* 287 */     this.headersCurrentStream = -1;
/* 288 */     this.headersEndStream = false;
/*     */   }
/*     */   
/*     */   private void readSettingsFrame(int flags, int payloadSize) throws Http2Exception, IOException
/*     */   {
/* 293 */     boolean ack = Flags.isAck(flags);
/* 294 */     if ((payloadSize > 0) && (ack)) {
/* 295 */       throw new ConnectionException(sm.getString("http2Parser.processFrameSettings.ackWithNonZeroPayload"), Http2Error.FRAME_SIZE_ERROR);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 300 */     if (payloadSize != 0)
/*     */     {
/* 302 */       byte[] setting = new byte[6];
/* 303 */       for (int i = 0; i < payloadSize / 6; i++) {
/* 304 */         this.input.fill(true, setting);
/* 305 */         int id = ByteUtil.getTwoBytes(setting, 0);
/* 306 */         long value = ByteUtil.getFourBytes(setting, 2);
/* 307 */         this.output.setting(Setting.valueOf(id), value);
/*     */       }
/*     */     }
/* 310 */     this.output.settingsEnd(ack);
/*     */   }
/*     */   
/*     */   private void readPushPromiseFrame(int streamId) throws Http2Exception
/*     */   {
/* 315 */     throw new ConnectionException(sm.getString("http2Parser.processFramePushPromise", new Object[] { this.connectionId, 
/* 316 */       Integer.valueOf(streamId) }), Http2Error.PROTOCOL_ERROR);
/*     */   }
/*     */   
/*     */   private void readPingFrame(int flags)
/*     */     throws IOException
/*     */   {
/* 322 */     byte[] payload = new byte[8];
/* 323 */     this.input.fill(true, payload);
/* 324 */     this.output.pingReceive(payload, Flags.isAck(flags));
/*     */   }
/*     */   
/*     */   private void readGoawayFrame(int payloadSize) throws IOException
/*     */   {
/* 329 */     byte[] payload = new byte[payloadSize];
/* 330 */     this.input.fill(true, payload);
/*     */     
/* 332 */     int lastStreamId = ByteUtil.get31Bits(payload, 0);
/* 333 */     long errorCode = ByteUtil.getFourBytes(payload, 4);
/* 334 */     String debugData = null;
/* 335 */     if (payloadSize > 8) {
/* 336 */       debugData = new String(payload, 8, payloadSize - 8, StandardCharsets.UTF_8);
/*     */     }
/* 338 */     this.output.goaway(lastStreamId, errorCode, debugData);
/*     */   }
/*     */   
/*     */   private void readWindowUpdateFrame(int streamId) throws Http2Exception, IOException
/*     */   {
/* 343 */     byte[] payload = new byte[4];
/* 344 */     this.input.fill(true, payload);
/* 345 */     int windowSizeIncrement = ByteUtil.get31Bits(payload, 0);
/*     */     
/* 347 */     if (log.isDebugEnabled()) {
/* 348 */       log.debug(sm.getString("http2Parser.processFrameWindowUpdate.debug", new Object[] { this.connectionId, 
/* 349 */         Integer.toString(streamId), Integer.toString(windowSizeIncrement) }));
/*     */     }
/*     */     
/*     */ 
/* 353 */     if (windowSizeIncrement == 0) {
/* 354 */       if (streamId == 0)
/*     */       {
/* 356 */         throw new ConnectionException(sm.getString("http2Parser.processFrameWindowUpdate.invalidIncrement"), Http2Error.PROTOCOL_ERROR);
/*     */       }
/*     */       
/*     */ 
/* 360 */       throw new StreamException(sm.getString("http2Parser.processFrameWindowUpdate.invalidIncrement"), Http2Error.PROTOCOL_ERROR, streamId);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 365 */     this.output.incrementWindowSize(streamId, windowSizeIncrement);
/*     */   }
/*     */   
/*     */   private void readContinuationFrame(int streamId, int flags, int payloadSize)
/*     */     throws Http2Exception, IOException
/*     */   {
/* 371 */     if (this.headersCurrentStream == -1)
/*     */     {
/* 373 */       throw new ConnectionException(sm.getString("http2Parser.processFrameContinuation.notExpected", new Object[] { this.connectionId, 
/*     */       
/* 375 */         Integer.toString(streamId) }), Http2Error.PROTOCOL_ERROR);
/*     */     }
/*     */     
/* 378 */     readHeaderPayload(streamId, payloadSize);
/*     */     
/* 380 */     if (Flags.isEndOfHeaders(flags)) {
/* 381 */       this.headersCurrentStream = -1;
/* 382 */       onHeadersComplete(streamId);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void readHeaderPayload(int streamId, int payloadSize)
/*     */     throws Http2Exception, IOException
/*     */   {
/* 390 */     if (log.isDebugEnabled()) {
/* 391 */       log.debug(sm.getString("http2Parser.processFrameHeaders.payload", new Object[] { this.connectionId, 
/* 392 */         Integer.valueOf(streamId), Integer.valueOf(payloadSize) }));
/*     */     }
/*     */     
/* 395 */     int remaining = payloadSize;
/*     */     
/* 397 */     while (remaining > 0) {
/* 398 */       if (this.headerReadBuffer.remaining() == 0) {
/*     */         int newSize;
/*     */         int newSize;
/* 401 */         if (this.headerReadBuffer.capacity() < payloadSize)
/*     */         {
/*     */ 
/* 404 */           newSize = payloadSize;
/*     */         }
/*     */         else
/*     */         {
/* 408 */           newSize = this.headerReadBuffer.capacity() * 2;
/*     */         }
/* 410 */         this.headerReadBuffer = ByteBufferUtils.expand(this.headerReadBuffer, newSize);
/*     */       }
/* 412 */       int toRead = Math.min(this.headerReadBuffer.remaining(), remaining);
/*     */       
/* 414 */       this.input.fill(true, this.headerReadBuffer, toRead);
/*     */       
/* 416 */       this.headerReadBuffer.flip();
/*     */       try {
/* 418 */         this.hpackDecoder.decode(this.headerReadBuffer);
/*     */       }
/*     */       catch (HpackException hpe) {
/* 421 */         throw new ConnectionException(sm.getString("http2Parser.processFrameHeaders.decodingFailed"), Http2Error.COMPRESSION_ERROR, hpe);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 426 */       this.headerReadBuffer.compact();
/* 427 */       remaining -= toRead;
/*     */       
/* 429 */       if (this.hpackDecoder.isHeaderCountExceeded()) {
/* 430 */         StreamException headerException = new StreamException(sm.getString("http2Parser.headerLimitCount", new Object[] { this.connectionId, 
/* 431 */           Integer.valueOf(streamId) }), Http2Error.ENHANCE_YOUR_CALM, streamId);
/*     */         
/* 433 */         this.hpackDecoder.getHeaderEmitter().setHeaderException(headerException);
/*     */       }
/*     */       
/* 436 */       if (this.hpackDecoder.isHeaderSizeExceeded(this.headerReadBuffer.position())) {
/* 437 */         StreamException headerException = new StreamException(sm.getString("http2Parser.headerLimitSize", new Object[] { this.connectionId, 
/* 438 */           Integer.valueOf(streamId) }), Http2Error.ENHANCE_YOUR_CALM, streamId);
/*     */         
/* 440 */         this.hpackDecoder.getHeaderEmitter().setHeaderException(headerException);
/*     */       }
/*     */       
/* 443 */       if (this.hpackDecoder.isHeaderSwallowSizeExceeded(this.headerReadBuffer.position())) {
/* 444 */         throw new ConnectionException(sm.getString("http2Parser.headerLimitSize", new Object[] { this.connectionId, 
/* 445 */           Integer.valueOf(streamId) }), Http2Error.ENHANCE_YOUR_CALM);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void onHeadersComplete(int streamId)
/*     */     throws Http2Exception
/*     */   {
/* 453 */     if (this.headerReadBuffer.position() > 0)
/*     */     {
/* 455 */       throw new ConnectionException(sm.getString("http2Parser.processFrameHeaders.decodingDataLeft"), Http2Error.COMPRESSION_ERROR);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 462 */     this.hpackDecoder.getHeaderEmitter().validateHeaders();
/*     */     
/* 464 */     this.output.headersEnd(streamId);
/*     */     
/* 466 */     if (this.headersEndStream) {
/* 467 */       this.output.receivedEndOfStream(streamId);
/* 468 */       this.headersEndStream = false;
/*     */     }
/*     */     
/*     */ 
/* 472 */     if (this.headerReadBuffer.capacity() > 1024) {
/* 473 */       this.headerReadBuffer = ByteBuffer.allocate(1024);
/*     */     }
/*     */   }
/*     */   
/*     */   private void readUnknownFrame(int streamId, FrameType frameType, int flags, int payloadSize) throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 481 */       swallow(streamId, payloadSize, false);
/*     */     }
/*     */     catch (ConnectionException localConnectionException) {}
/*     */     
/*     */ 
/* 486 */     this.output.swallowed(streamId, frameType, flags, payloadSize);
/*     */   }
/*     */   
/*     */   private void swallow(int streamId, int len, boolean mustBeZero)
/*     */     throws IOException, ConnectionException
/*     */   {
/* 492 */     if (log.isDebugEnabled()) {
/* 493 */       log.debug(sm.getString("http2Parser.swallow.debug", new Object[] { this.connectionId, 
/* 494 */         Integer.toString(streamId), Integer.toString(len) }));
/*     */     }
/* 496 */     if (len == 0) {
/* 497 */       return;
/*     */     }
/* 499 */     int read = 0;
/* 500 */     byte[] buffer = new byte['Ѐ'];
/* 501 */     while (read < len) {
/* 502 */       int thisTime = Math.min(buffer.length, len - read);
/* 503 */       this.input.fill(true, buffer, 0, thisTime);
/* 504 */       if (mustBeZero)
/*     */       {
/*     */ 
/*     */ 
/* 508 */         for (int i = 0; i < thisTime; i++) {
/* 509 */           if (buffer[i] != 0) {
/* 510 */             throw new ConnectionException(sm.getString("http2Parser.nonZeroPadding", new Object[] { this.connectionId, 
/* 511 */               Integer.toString(streamId) }), Http2Error.PROTOCOL_ERROR);
/*     */           }
/*     */         }
/*     */       }
/* 515 */       read += thisTime;
/*     */     }
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
/*     */   private void validateFrame(FrameType expected, FrameType frameType, int streamId, int flags, int payloadSize)
/*     */     throws Http2Exception
/*     */   {
/* 531 */     if (log.isDebugEnabled()) {
/* 532 */       log.debug(sm.getString("http2Parser.processFrame", new Object[] { this.connectionId, 
/* 533 */         Integer.toString(streamId), frameType, Integer.toString(flags), 
/* 534 */         Integer.toString(payloadSize) }));
/*     */     }
/*     */     
/* 537 */     if ((expected != null) && (frameType != expected)) {
/* 538 */       throw new StreamException(sm.getString("http2Parser.processFrame.unexpectedType", new Object[] { expected, frameType }), Http2Error.PROTOCOL_ERROR, streamId);
/*     */     }
/*     */     
/*     */ 
/* 542 */     int maxFrameSize = this.input.getMaxFrameSize();
/* 543 */     if (payloadSize > maxFrameSize) {
/* 544 */       throw new ConnectionException(sm.getString("http2Parser.payloadTooBig", new Object[] {
/* 545 */         Integer.toString(payloadSize), Integer.toString(maxFrameSize) }), Http2Error.FRAME_SIZE_ERROR);
/*     */     }
/*     */     
/*     */ 
/* 549 */     if (this.headersCurrentStream != -1) {
/* 550 */       if (this.headersCurrentStream != streamId) {
/* 551 */         throw new ConnectionException(sm.getString("http2Parser.headers.wrongStream", new Object[] { this.connectionId, 
/* 552 */           Integer.toString(this.headersCurrentStream), 
/* 553 */           Integer.toString(streamId) }), Http2Error.COMPRESSION_ERROR);
/*     */       }
/* 555 */       if (frameType != FrameType.RST)
/*     */       {
/* 557 */         if (frameType != FrameType.CONTINUATION) {
/* 558 */           throw new ConnectionException(sm.getString("http2Parser.headers.wrongFrameType", new Object[] { this.connectionId, 
/* 559 */             Integer.toString(this.headersCurrentStream), frameType }), Http2Error.COMPRESSION_ERROR);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 564 */     frameType.check(streamId, payloadSize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void readConnectionPreface()
/*     */     throws Http2Exception
/*     */   {
/* 572 */     byte[] data = new byte[CLIENT_PREFACE_START.length];
/*     */     try {
/* 574 */       this.input.fill(true, data);
/*     */       
/* 576 */       for (int i = 0; i < CLIENT_PREFACE_START.length; i++) {
/* 577 */         if (CLIENT_PREFACE_START[i] != data[i]) {
/* 578 */           throw new ProtocolException(sm.getString("http2Parser.preface.invalid"));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 583 */       readFrame(true, FrameType.SETTINGS);
/*     */     } catch (IOException ioe) {
/* 585 */       throw new ProtocolException(sm.getString("http2Parser.preface.io"), ioe);
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract interface Output
/*     */   {
/*     */     public abstract HpackDecoder getHpackDecoder();
/*     */     
/*     */     public abstract ByteBuffer startRequestBodyFrame(int paramInt1, int paramInt2)
/*     */       throws Http2Exception;
/*     */     
/*     */     public abstract void endRequestBodyFrame(int paramInt)
/*     */       throws Http2Exception;
/*     */     
/*     */     public abstract void receivedEndOfStream(int paramInt)
/*     */       throws ConnectionException;
/*     */     
/*     */     public abstract void swallowedPadding(int paramInt1, int paramInt2)
/*     */       throws ConnectionException, IOException;
/*     */     
/*     */     public abstract HpackDecoder.HeaderEmitter headersStart(int paramInt, boolean paramBoolean)
/*     */       throws Http2Exception;
/*     */     
/*     */     public abstract void headersEnd(int paramInt)
/*     */       throws ConnectionException;
/*     */     
/*     */     public abstract void reprioritise(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
/*     */       throws Http2Exception;
/*     */     
/*     */     public abstract void reset(int paramInt, long paramLong)
/*     */       throws Http2Exception;
/*     */     
/*     */     public abstract void setting(Setting paramSetting, long paramLong)
/*     */       throws ConnectionException;
/*     */     
/*     */     public abstract void settingsEnd(boolean paramBoolean)
/*     */       throws IOException;
/*     */     
/*     */     public abstract void pingReceive(byte[] paramArrayOfByte, boolean paramBoolean)
/*     */       throws IOException;
/*     */     
/*     */     public abstract void goaway(int paramInt, long paramLong, String paramString);
/*     */     
/*     */     public abstract void incrementWindowSize(int paramInt1, int paramInt2)
/*     */       throws Http2Exception;
/*     */     
/*     */     public abstract void swallowed(int paramInt1, FrameType paramFrameType, int paramInt2, int paramInt3)
/*     */       throws IOException;
/*     */   }
/*     */   
/*     */   static abstract interface Input
/*     */   {
/*     */     public abstract boolean fill(boolean paramBoolean, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */       throws IOException;
/*     */     
/*     */     public abstract boolean fill(boolean paramBoolean, byte[] paramArrayOfByte)
/*     */       throws IOException;
/*     */     
/*     */     public abstract boolean fill(boolean paramBoolean, ByteBuffer paramByteBuffer, int paramInt)
/*     */       throws IOException;
/*     */     
/*     */     public abstract int getMaxFrameSize();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\Http2Parser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */