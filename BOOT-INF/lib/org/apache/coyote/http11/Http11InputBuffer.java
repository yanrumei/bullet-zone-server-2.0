/*      */ package org.apache.coyote.http11;
/*      */ 
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ import java.nio.Buffer;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import org.apache.coyote.InputBuffer;
/*      */ import org.apache.coyote.Request;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.util.buf.ByteChunk;
/*      */ import org.apache.tomcat.util.buf.MessageBytes;
/*      */ import org.apache.tomcat.util.http.MimeHeaders;
/*      */ import org.apache.tomcat.util.http.parser.HttpParser;
/*      */ import org.apache.tomcat.util.net.AbstractEndpoint;
/*      */ import org.apache.tomcat.util.net.ApplicationBufferHandler;
/*      */ import org.apache.tomcat.util.net.SocketBufferHandler;
/*      */ import org.apache.tomcat.util.net.SocketWrapperBase;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Http11InputBuffer
/*      */   implements InputBuffer, ApplicationBufferHandler
/*      */ {
/*   44 */   private static final Log log = LogFactory.getLog(Http11InputBuffer.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   49 */   private static final StringManager sm = StringManager.getManager(Http11InputBuffer.class);
/*      */   
/*      */ 
/*   52 */   private static final byte[] CLIENT_PREFACE_START = "PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n"
/*   53 */     .getBytes(StandardCharsets.ISO_8859_1);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final Request request;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final MimeHeaders headers;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final boolean rejectIllegalHeaderName;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean parsingHeader;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean swallowInput;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private ByteBuffer byteBuffer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int end;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private SocketWrapperBase<?> wrapper;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private InputBuffer inputStreamInputBuffer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private InputFilter[] filterLibrary;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private InputFilter[] activeFilters;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int lastActiveFilter;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean parsingRequestLine;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  130 */   private int parsingRequestLinePhase = 0;
/*  131 */   private boolean parsingRequestLineEol = false;
/*  132 */   private int parsingRequestLineStart = 0;
/*  133 */   private int parsingRequestLineQPos = -1;
/*      */   private HeaderParsePosition headerParsePos;
/*  135 */   private final HeaderParseData headerData = new HeaderParseData(null);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int headerBufferSize;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int socketReadBufferSize;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Http11InputBuffer(Request request, int headerBufferSize, boolean rejectIllegalHeaderName)
/*      */   {
/*  154 */     this.request = request;
/*  155 */     this.headers = request.getMimeHeaders();
/*      */     
/*  157 */     this.headerBufferSize = headerBufferSize;
/*  158 */     this.rejectIllegalHeaderName = rejectIllegalHeaderName;
/*      */     
/*  160 */     this.filterLibrary = new InputFilter[0];
/*  161 */     this.activeFilters = new InputFilter[0];
/*  162 */     this.lastActiveFilter = -1;
/*      */     
/*  164 */     this.parsingHeader = true;
/*  165 */     this.parsingRequestLine = true;
/*  166 */     this.parsingRequestLinePhase = 0;
/*  167 */     this.parsingRequestLineEol = false;
/*  168 */     this.parsingRequestLineStart = 0;
/*  169 */     this.parsingRequestLineQPos = -1;
/*  170 */     this.headerParsePos = HeaderParsePosition.HEADER_START;
/*  171 */     this.swallowInput = true;
/*      */     
/*  173 */     this.inputStreamInputBuffer = new SocketInputBuffer(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void addFilter(InputFilter filter)
/*      */   {
/*  186 */     if (filter == null) {
/*  187 */       throw new NullPointerException(sm.getString("iib.filter.npe"));
/*      */     }
/*      */     
/*  190 */     InputFilter[] newFilterLibrary = new InputFilter[this.filterLibrary.length + 1];
/*  191 */     for (int i = 0; i < this.filterLibrary.length; i++) {
/*  192 */       newFilterLibrary[i] = this.filterLibrary[i];
/*      */     }
/*  194 */     newFilterLibrary[this.filterLibrary.length] = filter;
/*  195 */     this.filterLibrary = newFilterLibrary;
/*      */     
/*  197 */     this.activeFilters = new InputFilter[this.filterLibrary.length];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   InputFilter[] getFilters()
/*      */   {
/*  205 */     return this.filterLibrary;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void addActiveFilter(InputFilter filter)
/*      */   {
/*  214 */     if (this.lastActiveFilter == -1) {
/*  215 */       filter.setBuffer(this.inputStreamInputBuffer);
/*      */     } else {
/*  217 */       for (int i = 0; i <= this.lastActiveFilter; i++) {
/*  218 */         if (this.activeFilters[i] == filter)
/*  219 */           return;
/*      */       }
/*  221 */       filter.setBuffer(this.activeFilters[this.lastActiveFilter]);
/*      */     }
/*      */     
/*  224 */     this.activeFilters[(++this.lastActiveFilter)] = filter;
/*      */     
/*  226 */     filter.setRequest(this.request);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   void setSwallowInput(boolean swallowInput)
/*      */   {
/*  234 */     this.swallowInput = swallowInput;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public int doRead(ByteChunk chunk)
/*      */     throws IOException
/*      */   {
/*  248 */     if (this.lastActiveFilter == -1) {
/*  249 */       return this.inputStreamInputBuffer.doRead(chunk);
/*      */     }
/*  251 */     return this.activeFilters[this.lastActiveFilter].doRead(chunk);
/*      */   }
/*      */   
/*      */ 
/*      */   public int doRead(ApplicationBufferHandler handler)
/*      */     throws IOException
/*      */   {
/*  258 */     if (this.lastActiveFilter == -1) {
/*  259 */       return this.inputStreamInputBuffer.doRead(handler);
/*      */     }
/*  261 */     return this.activeFilters[this.lastActiveFilter].doRead(handler);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void recycle()
/*      */   {
/*  273 */     this.wrapper = null;
/*  274 */     this.request.recycle();
/*      */     
/*  276 */     for (int i = 0; i <= this.lastActiveFilter; i++) {
/*  277 */       this.activeFilters[i].recycle();
/*      */     }
/*      */     
/*  280 */     this.byteBuffer.limit(0).position(0);
/*  281 */     this.lastActiveFilter = -1;
/*  282 */     this.parsingHeader = true;
/*  283 */     this.swallowInput = true;
/*      */     
/*  285 */     this.headerParsePos = HeaderParsePosition.HEADER_START;
/*  286 */     this.parsingRequestLine = true;
/*  287 */     this.parsingRequestLinePhase = 0;
/*  288 */     this.parsingRequestLineEol = false;
/*  289 */     this.parsingRequestLineStart = 0;
/*  290 */     this.parsingRequestLineQPos = -1;
/*  291 */     this.headerData.recycle();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void nextRequest()
/*      */   {
/*  302 */     this.request.recycle();
/*      */     
/*  304 */     if (this.byteBuffer.position() > 0) {
/*  305 */       if (this.byteBuffer.remaining() > 0)
/*      */       {
/*  307 */         this.byteBuffer.compact();
/*  308 */         this.byteBuffer.flip();
/*      */       }
/*      */       else {
/*  311 */         this.byteBuffer.position(0).limit(0);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  316 */     for (int i = 0; i <= this.lastActiveFilter; i++) {
/*  317 */       this.activeFilters[i].recycle();
/*      */     }
/*      */     
/*      */ 
/*  321 */     this.lastActiveFilter = -1;
/*  322 */     this.parsingHeader = true;
/*  323 */     this.swallowInput = true;
/*      */     
/*  325 */     this.headerParsePos = HeaderParsePosition.HEADER_START;
/*  326 */     this.parsingRequestLine = true;
/*  327 */     this.parsingRequestLinePhase = 0;
/*  328 */     this.parsingRequestLineEol = false;
/*  329 */     this.parsingRequestLineStart = 0;
/*  330 */     this.parsingRequestLineQPos = -1;
/*  331 */     this.headerData.recycle();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   boolean parseRequestLine(boolean keptAlive)
/*      */     throws IOException
/*      */   {
/*  349 */     if (!this.parsingRequestLine) {
/*  350 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  355 */     if (this.parsingRequestLinePhase < 2) {
/*  356 */       byte chr = 0;
/*      */       
/*      */       do
/*      */       {
/*  360 */         if (this.byteBuffer.position() >= this.byteBuffer.limit()) {
/*  361 */           if (keptAlive)
/*      */           {
/*      */ 
/*  364 */             this.wrapper.setReadTimeout(this.wrapper.getEndpoint().getKeepAliveTimeout());
/*      */           }
/*  366 */           if (!fill(false))
/*      */           {
/*  368 */             this.parsingRequestLinePhase = 1;
/*  369 */             return false;
/*      */           }
/*      */           
/*      */ 
/*  373 */           this.wrapper.setReadTimeout(this.wrapper.getEndpoint().getConnectionTimeout());
/*      */         }
/*  375 */         if ((!keptAlive) && (this.byteBuffer.position() == 0) && (this.byteBuffer.limit() >= CLIENT_PREFACE_START.length - 1)) {
/*  376 */           boolean prefaceMatch = true;
/*  377 */           for (int i = 0; (i < CLIENT_PREFACE_START.length) && (prefaceMatch); i++) {
/*  378 */             if (CLIENT_PREFACE_START[i] != this.byteBuffer.get(i)) {
/*  379 */               prefaceMatch = false;
/*      */             }
/*      */           }
/*  382 */           if (prefaceMatch)
/*      */           {
/*  384 */             this.parsingRequestLinePhase = -1;
/*  385 */             return false;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  390 */         if (this.request.getStartTime() < 0L) {
/*  391 */           this.request.setStartTime(System.currentTimeMillis());
/*      */         }
/*  393 */         chr = this.byteBuffer.get();
/*  394 */       } while ((chr == 13) || (chr == 10));
/*  395 */       this.byteBuffer.position(this.byteBuffer.position() - 1);
/*      */       
/*  397 */       this.parsingRequestLineStart = this.byteBuffer.position();
/*  398 */       this.parsingRequestLinePhase = 2;
/*  399 */       if (log.isDebugEnabled()) {
/*  400 */         log.debug("Received [" + new String(this.byteBuffer
/*  401 */           .array(), this.byteBuffer.position(), this.byteBuffer.remaining(), StandardCharsets.ISO_8859_1) + "]");
/*      */       }
/*      */     }
/*  404 */     if (this.parsingRequestLinePhase == 2)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  409 */       boolean space = false;
/*  410 */       while (!space)
/*      */       {
/*  412 */         if ((this.byteBuffer.position() >= this.byteBuffer.limit()) && 
/*  413 */           (!fill(false))) {
/*  414 */           return false;
/*      */         }
/*      */         
/*      */ 
/*  418 */         int pos = this.byteBuffer.position();
/*  419 */         byte chr = this.byteBuffer.get();
/*  420 */         if ((chr == 32) || (chr == 9)) {
/*  421 */           space = true;
/*  422 */           this.request.method().setBytes(this.byteBuffer.array(), this.parsingRequestLineStart, pos - this.parsingRequestLineStart);
/*      */         }
/*  424 */         else if (!HttpParser.isToken(chr)) {
/*  425 */           this.byteBuffer.position(this.byteBuffer.position() - 1);
/*  426 */           throw new IllegalArgumentException(sm.getString("iib.invalidmethod"));
/*      */         }
/*      */       }
/*  429 */       this.parsingRequestLinePhase = 3;
/*      */     }
/*  431 */     if (this.parsingRequestLinePhase == 3)
/*      */     {
/*  433 */       boolean space = true;
/*  434 */       while (space)
/*      */       {
/*  436 */         if ((this.byteBuffer.position() >= this.byteBuffer.limit()) && 
/*  437 */           (!fill(false))) {
/*  438 */           return false;
/*      */         }
/*  440 */         byte chr = this.byteBuffer.get();
/*  441 */         if ((chr != 32) && (chr != 9)) {
/*  442 */           space = false;
/*  443 */           this.byteBuffer.position(this.byteBuffer.position() - 1);
/*      */         }
/*      */       }
/*  446 */       this.parsingRequestLineStart = this.byteBuffer.position();
/*  447 */       this.parsingRequestLinePhase = 4;
/*      */     }
/*  449 */     if (this.parsingRequestLinePhase == 4)
/*      */     {
/*      */ 
/*  452 */       int end = 0;
/*      */       
/*      */ 
/*      */ 
/*  456 */       boolean space = false;
/*  457 */       while (!space)
/*      */       {
/*  459 */         if ((this.byteBuffer.position() >= this.byteBuffer.limit()) && 
/*  460 */           (!fill(false))) {
/*  461 */           return false;
/*      */         }
/*  463 */         int pos = this.byteBuffer.position();
/*  464 */         byte chr = this.byteBuffer.get();
/*  465 */         if ((chr == 32) || (chr == 9)) {
/*  466 */           space = true;
/*  467 */           end = pos;
/*  468 */         } else if ((chr == 13) || (chr == 10))
/*      */         {
/*  470 */           this.parsingRequestLineEol = true;
/*  471 */           space = true;
/*  472 */           end = pos;
/*  473 */         } else if ((chr == 63) && (this.parsingRequestLineQPos == -1)) {
/*  474 */           this.parsingRequestLineQPos = pos;
/*  475 */         } else if (HttpParser.isNotRequestTarget(chr)) {
/*  476 */           throw new IllegalArgumentException(sm.getString("iib.invalidRequestTarget"));
/*      */         }
/*      */       }
/*  479 */       if (this.parsingRequestLineQPos >= 0) {
/*  480 */         this.request.queryString().setBytes(this.byteBuffer.array(), this.parsingRequestLineQPos + 1, end - this.parsingRequestLineQPos - 1);
/*      */         
/*  482 */         this.request.requestURI().setBytes(this.byteBuffer.array(), this.parsingRequestLineStart, this.parsingRequestLineQPos - this.parsingRequestLineStart);
/*      */       }
/*      */       else {
/*  485 */         this.request.requestURI().setBytes(this.byteBuffer.array(), this.parsingRequestLineStart, end - this.parsingRequestLineStart);
/*      */       }
/*      */       
/*  488 */       this.parsingRequestLinePhase = 5;
/*      */     }
/*  490 */     if (this.parsingRequestLinePhase == 5)
/*      */     {
/*  492 */       boolean space = true;
/*  493 */       while (space)
/*      */       {
/*  495 */         if ((this.byteBuffer.position() >= this.byteBuffer.limit()) && 
/*  496 */           (!fill(false))) {
/*  497 */           return false;
/*      */         }
/*  499 */         byte chr = this.byteBuffer.get();
/*  500 */         if ((chr != 32) && (chr != 9)) {
/*  501 */           space = false;
/*  502 */           this.byteBuffer.position(this.byteBuffer.position() - 1);
/*      */         }
/*      */       }
/*  505 */       this.parsingRequestLineStart = this.byteBuffer.position();
/*  506 */       this.parsingRequestLinePhase = 6;
/*      */       
/*      */ 
/*  509 */       this.end = 0;
/*      */     }
/*  511 */     if (this.parsingRequestLinePhase == 6)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  516 */       while (!this.parsingRequestLineEol)
/*      */       {
/*  518 */         if ((this.byteBuffer.position() >= this.byteBuffer.limit()) && 
/*  519 */           (!fill(false))) {
/*  520 */           return false;
/*      */         }
/*      */         
/*  523 */         int pos = this.byteBuffer.position();
/*  524 */         byte chr = this.byteBuffer.get();
/*  525 */         if (chr == 13) {
/*  526 */           this.end = pos;
/*  527 */         } else if (chr == 10) {
/*  528 */           if (this.end == 0) {
/*  529 */             this.end = pos;
/*      */           }
/*  531 */           this.parsingRequestLineEol = true;
/*  532 */         } else if (!HttpParser.isHttpProtocol(chr)) {
/*  533 */           throw new IllegalArgumentException(sm.getString("iib.invalidHttpProtocol"));
/*      */         }
/*      */       }
/*      */       
/*  537 */       if (this.end - this.parsingRequestLineStart > 0) {
/*  538 */         this.request.protocol().setBytes(this.byteBuffer.array(), this.parsingRequestLineStart, this.end - this.parsingRequestLineStart);
/*      */       }
/*      */       else {
/*  541 */         this.request.protocol().setString("");
/*      */       }
/*  543 */       this.parsingRequestLine = false;
/*  544 */       this.parsingRequestLinePhase = 0;
/*  545 */       this.parsingRequestLineEol = false;
/*  546 */       this.parsingRequestLineStart = 0;
/*  547 */       return true;
/*      */     }
/*  549 */     throw new IllegalStateException("Invalid request line parse phase:" + this.parsingRequestLinePhase);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   boolean parseHeaders()
/*      */     throws IOException
/*      */   {
/*  558 */     if (!this.parsingHeader) {
/*  559 */       throw new IllegalStateException(sm.getString("iib.parseheaders.ise.error"));
/*      */     }
/*      */     
/*  562 */     HeaderParseStatus status = HeaderParseStatus.HAVE_MORE_HEADERS;
/*      */     do
/*      */     {
/*  565 */       status = parseHeader();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  574 */       if ((this.byteBuffer.position() > this.headerBufferSize) || (this.byteBuffer.capacity() - this.byteBuffer.position() < this.socketReadBufferSize)) {
/*  575 */         throw new IllegalArgumentException(sm.getString("iib.requestheadertoolarge.error"));
/*      */       }
/*  577 */     } while (status == HeaderParseStatus.HAVE_MORE_HEADERS);
/*  578 */     if (status == HeaderParseStatus.DONE) {
/*  579 */       this.parsingHeader = false;
/*  580 */       this.end = this.byteBuffer.position();
/*  581 */       return true;
/*      */     }
/*  583 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   int getParsingRequestLinePhase()
/*      */   {
/*  589 */     return this.parsingRequestLinePhase;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void endRequest()
/*      */     throws IOException
/*      */   {
/*  600 */     if ((this.swallowInput) && (this.lastActiveFilter != -1)) {
/*  601 */       int extraBytes = (int)this.activeFilters[this.lastActiveFilter].end();
/*  602 */       this.byteBuffer.position(this.byteBuffer.position() - extraBytes);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   int available(boolean read)
/*      */   {
/*  612 */     int available = this.byteBuffer.remaining();
/*  613 */     if ((available == 0) && (this.lastActiveFilter >= 0)) {
/*  614 */       for (int i = 0; (available == 0) && (i <= this.lastActiveFilter); i++) {
/*  615 */         available = this.activeFilters[i].available();
/*      */       }
/*      */     }
/*  618 */     if ((available > 0) || (!read)) {
/*  619 */       return available;
/*      */     }
/*      */     try
/*      */     {
/*  623 */       fill(false);
/*  624 */       available = this.byteBuffer.remaining();
/*      */     } catch (IOException ioe) {
/*  626 */       if (log.isDebugEnabled()) {
/*  627 */         log.debug(sm.getString("iib.available.readFail"), ioe);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  632 */       available = 1;
/*      */     }
/*  634 */     return available;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   boolean isFinished()
/*      */   {
/*  644 */     if (this.byteBuffer.limit() > this.byteBuffer.position())
/*      */     {
/*  646 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  660 */     if (this.lastActiveFilter >= 0) {
/*  661 */       return this.activeFilters[this.lastActiveFilter].isFinished();
/*      */     }
/*      */     
/*      */ 
/*  665 */     return false;
/*      */   }
/*      */   
/*      */   ByteBuffer getLeftover()
/*      */   {
/*  670 */     int available = this.byteBuffer.remaining();
/*  671 */     if (available > 0) {
/*  672 */       return ByteBuffer.wrap(this.byteBuffer.array(), this.byteBuffer.position(), available);
/*      */     }
/*  674 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   void init(SocketWrapperBase<?> socketWrapper)
/*      */   {
/*  681 */     this.wrapper = socketWrapper;
/*  682 */     this.wrapper.setAppReadBufHandler(this);
/*      */     
/*      */ 
/*  685 */     int bufLength = this.headerBufferSize + this.wrapper.getSocketBufferHandler().getReadBuffer().capacity();
/*  686 */     if ((this.byteBuffer == null) || (this.byteBuffer.capacity() < bufLength)) {
/*  687 */       this.byteBuffer = ByteBuffer.allocate(bufLength);
/*  688 */       this.byteBuffer.position(0).limit(0);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean fill(boolean block)
/*      */     throws IOException
/*      */   {
/*  704 */     if (this.parsingHeader) {
/*  705 */       if (this.byteBuffer.limit() >= this.headerBufferSize) {
/*  706 */         throw new IllegalArgumentException(sm.getString("iib.requestheadertoolarge.error"));
/*      */       }
/*      */     } else {
/*  709 */       this.byteBuffer.limit(this.end).position(this.end);
/*      */     }
/*      */     
/*  712 */     this.byteBuffer.mark();
/*  713 */     if (this.byteBuffer.position() < this.byteBuffer.limit()) {
/*  714 */       this.byteBuffer.position(this.byteBuffer.limit());
/*      */     }
/*  716 */     this.byteBuffer.limit(this.byteBuffer.capacity());
/*  717 */     int nRead = this.wrapper.read(block, this.byteBuffer);
/*  718 */     this.byteBuffer.limit(this.byteBuffer.position()).reset();
/*  719 */     if (nRead > 0)
/*  720 */       return true;
/*  721 */     if (nRead == -1) {
/*  722 */       throw new EOFException(sm.getString("iib.eof.error"));
/*      */     }
/*  724 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private HeaderParseStatus parseHeader()
/*      */     throws IOException
/*      */   {
/*  742 */     byte chr = 0;
/*  743 */     while (this.headerParsePos == HeaderParsePosition.HEADER_START)
/*      */     {
/*      */ 
/*  746 */       if ((this.byteBuffer.position() >= this.byteBuffer.limit()) && 
/*  747 */         (!fill(false))) {
/*  748 */         this.headerParsePos = HeaderParsePosition.HEADER_START;
/*  749 */         return HeaderParseStatus.NEED_MORE_DATA;
/*      */       }
/*      */       
/*      */ 
/*  753 */       chr = this.byteBuffer.get();
/*      */       
/*  755 */       if (chr != 13)
/*      */       {
/*  757 */         if (chr == 10) {
/*  758 */           return HeaderParseStatus.DONE;
/*      */         }
/*  760 */         this.byteBuffer.position(this.byteBuffer.position() - 1);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  766 */     if (this.headerParsePos == HeaderParsePosition.HEADER_START)
/*      */     {
/*  768 */       this.headerData.start = this.byteBuffer.position();
/*  769 */       this.headerParsePos = HeaderParsePosition.HEADER_NAME;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  777 */     while (this.headerParsePos == HeaderParsePosition.HEADER_NAME)
/*      */     {
/*      */ 
/*  780 */       if ((this.byteBuffer.position() >= this.byteBuffer.limit()) && 
/*  781 */         (!fill(false))) {
/*  782 */         return HeaderParseStatus.NEED_MORE_DATA;
/*      */       }
/*      */       
/*      */ 
/*  786 */       int pos = this.byteBuffer.position();
/*  787 */       chr = this.byteBuffer.get();
/*  788 */       if (chr == 58) {
/*  789 */         this.headerParsePos = HeaderParsePosition.HEADER_VALUE_START;
/*  790 */         this.headerData.headerValue = this.headers.addValue(this.byteBuffer.array(), this.headerData.start, pos - this.headerData.start);
/*      */         
/*  792 */         pos = this.byteBuffer.position();
/*      */         
/*  794 */         this.headerData.start = pos;
/*  795 */         this.headerData.realPos = pos;
/*  796 */         this.headerData.lastSignificantChar = pos;
/*  797 */         break; }
/*  798 */       if (!HttpParser.isToken(chr))
/*      */       {
/*      */ 
/*  801 */         this.headerData.lastSignificantChar = pos;
/*  802 */         this.byteBuffer.position(this.byteBuffer.position() - 1);
/*      */         
/*  804 */         return skipLine();
/*      */       }
/*      */       
/*      */ 
/*  808 */       if ((chr >= 65) && (chr <= 90)) {
/*  809 */         this.byteBuffer.put(pos, (byte)(chr - -32));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  814 */     if (this.headerParsePos == HeaderParsePosition.HEADER_SKIPLINE) {
/*  815 */       return skipLine();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  822 */     while ((this.headerParsePos == HeaderParsePosition.HEADER_VALUE_START) || (this.headerParsePos == HeaderParsePosition.HEADER_VALUE) || (this.headerParsePos == HeaderParsePosition.HEADER_MULTI_LINE))
/*      */     {
/*      */ 
/*      */ 
/*  826 */       if (this.headerParsePos == HeaderParsePosition.HEADER_VALUE_START)
/*      */       {
/*      */         do
/*      */         {
/*  830 */           if ((this.byteBuffer.position() >= this.byteBuffer.limit()) && 
/*  831 */             (!fill(false)))
/*      */           {
/*  833 */             return HeaderParseStatus.NEED_MORE_DATA;
/*      */           }
/*      */           
/*      */ 
/*  837 */           chr = this.byteBuffer.get();
/*  838 */         } while ((chr == 32) || (chr == 9));
/*  839 */         this.headerParsePos = HeaderParsePosition.HEADER_VALUE;
/*  840 */         this.byteBuffer.position(this.byteBuffer.position() - 1);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  845 */       if (this.headerParsePos == HeaderParsePosition.HEADER_VALUE)
/*      */       {
/*      */ 
/*  848 */         boolean eol = false;
/*  849 */         while (!eol)
/*      */         {
/*      */ 
/*  852 */           if ((this.byteBuffer.position() >= this.byteBuffer.limit()) && 
/*  853 */             (!fill(false)))
/*      */           {
/*  855 */             return HeaderParseStatus.NEED_MORE_DATA;
/*      */           }
/*      */           
/*      */ 
/*  859 */           chr = this.byteBuffer.get();
/*  860 */           if (chr != 13)
/*      */           {
/*  862 */             if (chr == 10) {
/*  863 */               eol = true;
/*  864 */             } else if ((chr == 32) || (chr == 9)) {
/*  865 */               this.byteBuffer.put(this.headerData.realPos, chr);
/*  866 */               this.headerData.realPos += 1;
/*      */             } else {
/*  868 */               this.byteBuffer.put(this.headerData.realPos, chr);
/*  869 */               this.headerData.realPos += 1;
/*  870 */               this.headerData.lastSignificantChar = this.headerData.realPos;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  875 */         this.headerData.realPos = this.headerData.lastSignificantChar;
/*      */         
/*      */ 
/*      */ 
/*  879 */         this.headerParsePos = HeaderParsePosition.HEADER_MULTI_LINE;
/*      */       }
/*      */       
/*  882 */       if ((this.byteBuffer.position() >= this.byteBuffer.limit()) && 
/*  883 */         (!fill(false)))
/*      */       {
/*  885 */         return HeaderParseStatus.NEED_MORE_DATA;
/*      */       }
/*      */       
/*      */ 
/*  889 */       chr = this.byteBuffer.get(this.byteBuffer.position());
/*  890 */       if (this.headerParsePos == HeaderParsePosition.HEADER_MULTI_LINE) {
/*  891 */         if ((chr != 32) && (chr != 9)) {
/*  892 */           this.headerParsePos = HeaderParsePosition.HEADER_START;
/*  893 */           break;
/*      */         }
/*      */         
/*      */ 
/*  897 */         this.byteBuffer.put(this.headerData.realPos, chr);
/*  898 */         this.headerData.realPos += 1;
/*  899 */         this.headerParsePos = HeaderParsePosition.HEADER_VALUE_START;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  904 */     this.headerData.headerValue.setBytes(this.byteBuffer.array(), this.headerData.start, this.headerData.lastSignificantChar - this.headerData.start);
/*      */     
/*  906 */     this.headerData.recycle();
/*  907 */     return HeaderParseStatus.HAVE_MORE_HEADERS;
/*      */   }
/*      */   
/*      */   private HeaderParseStatus skipLine() throws IOException
/*      */   {
/*  912 */     this.headerParsePos = HeaderParsePosition.HEADER_SKIPLINE;
/*  913 */     boolean eol = false;
/*      */     
/*      */ 
/*  916 */     while (!eol)
/*      */     {
/*      */ 
/*  919 */       if ((this.byteBuffer.position() >= this.byteBuffer.limit()) && 
/*  920 */         (!fill(false))) {
/*  921 */         return HeaderParseStatus.NEED_MORE_DATA;
/*      */       }
/*      */       
/*      */ 
/*  925 */       int pos = this.byteBuffer.position();
/*  926 */       byte chr = this.byteBuffer.get();
/*  927 */       if (chr != 13)
/*      */       {
/*  929 */         if (chr == 10) {
/*  930 */           eol = true;
/*      */         } else
/*  932 */           this.headerData.lastSignificantChar = pos;
/*      */       }
/*      */     }
/*  935 */     if ((this.rejectIllegalHeaderName) || (log.isDebugEnabled())) {
/*  936 */       String message = sm.getString("iib.invalidheader", new Object[] { new String(this.byteBuffer
/*  937 */         .array(), this.headerData.start, this.headerData.lastSignificantChar - this.headerData.start + 1, StandardCharsets.ISO_8859_1) });
/*      */       
/*      */ 
/*  940 */       if (this.rejectIllegalHeaderName) {
/*  941 */         throw new IllegalArgumentException(message);
/*      */       }
/*  943 */       log.debug(message);
/*      */     }
/*      */     
/*  946 */     this.headerParsePos = HeaderParsePosition.HEADER_START;
/*  947 */     return HeaderParseStatus.HAVE_MORE_HEADERS;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static enum HeaderParseStatus
/*      */   {
/*  954 */     DONE,  HAVE_MORE_HEADERS,  NEED_MORE_DATA;
/*      */     
/*      */ 
/*      */     private HeaderParseStatus() {}
/*      */   }
/*      */   
/*      */ 
/*      */   private static enum HeaderParsePosition
/*      */   {
/*  963 */     HEADER_START, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  970 */     HEADER_NAME, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  976 */     HEADER_VALUE_START, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  983 */     HEADER_VALUE, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  990 */     HEADER_MULTI_LINE, 
/*      */     
/*      */ 
/*      */ 
/*  994 */     HEADER_SKIPLINE;
/*      */     
/*      */ 
/*      */ 
/*      */     private HeaderParsePosition() {}
/*      */   }
/*      */   
/*      */ 
/*      */   private static class HeaderParseData
/*      */   {
/* 1004 */     int start = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1014 */     int realPos = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1020 */     int lastSignificantChar = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1025 */     MessageBytes headerValue = null;
/*      */     
/* 1027 */     public void recycle() { this.start = 0;
/* 1028 */       this.realPos = 0;
/* 1029 */       this.lastSignificantChar = 0;
/* 1030 */       this.headerValue = null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private class SocketInputBuffer
/*      */     implements InputBuffer
/*      */   {
/*      */     private SocketInputBuffer() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     @Deprecated
/*      */     public int doRead(ByteChunk chunk)
/*      */       throws IOException
/*      */     {
/* 1052 */       if (Http11InputBuffer.this.byteBuffer.position() >= Http11InputBuffer.this.byteBuffer.limit())
/*      */       {
/*      */ 
/* 1055 */         if (!Http11InputBuffer.this.fill(true)) {
/* 1056 */           return -1;
/*      */         }
/*      */       }
/* 1059 */       int length = Http11InputBuffer.this.byteBuffer.remaining();
/* 1060 */       chunk.setBytes(Http11InputBuffer.this.byteBuffer.array(), Http11InputBuffer.this.byteBuffer.position(), length);
/* 1061 */       Http11InputBuffer.this.byteBuffer.position(Http11InputBuffer.this.byteBuffer.limit());
/*      */       
/* 1063 */       return length;
/*      */     }
/*      */     
/*      */     public int doRead(ApplicationBufferHandler handler)
/*      */       throws IOException
/*      */     {
/* 1069 */       if (Http11InputBuffer.this.byteBuffer.position() >= Http11InputBuffer.this.byteBuffer.limit())
/*      */       {
/*      */ 
/* 1072 */         if (!Http11InputBuffer.this.fill(true)) {
/* 1073 */           return -1;
/*      */         }
/*      */       }
/* 1076 */       int length = Http11InputBuffer.this.byteBuffer.remaining();
/* 1077 */       handler.setByteBuffer(Http11InputBuffer.this.byteBuffer.duplicate());
/* 1078 */       Http11InputBuffer.this.byteBuffer.position(Http11InputBuffer.this.byteBuffer.limit());
/*      */       
/* 1080 */       return length;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void setByteBuffer(ByteBuffer buffer)
/*      */   {
/* 1087 */     this.byteBuffer = buffer;
/*      */   }
/*      */   
/*      */ 
/*      */   public ByteBuffer getByteBuffer()
/*      */   {
/* 1093 */     return this.byteBuffer;
/*      */   }
/*      */   
/*      */ 
/*      */   public void expand(int size)
/*      */   {
/* 1099 */     if (this.byteBuffer.capacity() >= size) {
/* 1100 */       this.byteBuffer.limit(size);
/*      */     }
/* 1102 */     ByteBuffer temp = ByteBuffer.allocate(size);
/* 1103 */     temp.put(this.byteBuffer);
/* 1104 */     this.byteBuffer = temp;
/* 1105 */     this.byteBuffer.mark();
/* 1106 */     temp = null;
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\Http11InputBuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */