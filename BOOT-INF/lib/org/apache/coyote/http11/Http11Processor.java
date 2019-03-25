/*      */ package org.apache.coyote.http11;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InterruptedIOException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import org.apache.coyote.AbstractProcessor;
/*      */ import org.apache.coyote.ActionCode;
/*      */ import org.apache.coyote.Adapter;
/*      */ import org.apache.coyote.ErrorState;
/*      */ import org.apache.coyote.Request;
/*      */ import org.apache.coyote.RequestInfo;
/*      */ import org.apache.coyote.Response;
/*      */ import org.apache.coyote.UpgradeProtocol;
/*      */ import org.apache.coyote.UpgradeToken;
/*      */ import org.apache.coyote.http11.filters.BufferedInputFilter;
/*      */ import org.apache.coyote.http11.filters.ChunkedInputFilter;
/*      */ import org.apache.coyote.http11.filters.ChunkedOutputFilter;
/*      */ import org.apache.coyote.http11.filters.GzipOutputFilter;
/*      */ import org.apache.coyote.http11.filters.IdentityInputFilter;
/*      */ import org.apache.coyote.http11.filters.IdentityOutputFilter;
/*      */ import org.apache.coyote.http11.filters.SavedRequestInputFilter;
/*      */ import org.apache.coyote.http11.filters.VoidInputFilter;
/*      */ import org.apache.coyote.http11.filters.VoidOutputFilter;
/*      */ import org.apache.coyote.http11.upgrade.InternalHttpUpgradeHandler;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.buf.Ascii;
/*      */ import org.apache.tomcat.util.buf.ByteChunk;
/*      */ import org.apache.tomcat.util.buf.HexUtils;
/*      */ import org.apache.tomcat.util.buf.MessageBytes;
/*      */ import org.apache.tomcat.util.http.FastHttpDateFormat;
/*      */ import org.apache.tomcat.util.http.MimeHeaders;
/*      */ import org.apache.tomcat.util.log.UserDataHelper;
/*      */ import org.apache.tomcat.util.log.UserDataHelper.Mode;
/*      */ import org.apache.tomcat.util.net.AbstractEndpoint;
/*      */ import org.apache.tomcat.util.net.AbstractEndpoint.Handler.SocketState;
/*      */ import org.apache.tomcat.util.net.SSLSupport;
/*      */ import org.apache.tomcat.util.net.SendfileDataBase;
/*      */ import org.apache.tomcat.util.net.SendfileKeepAliveState;
/*      */ import org.apache.tomcat.util.net.SendfileState;
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
/*      */ public class Http11Processor
/*      */   extends AbstractProcessor
/*      */ {
/*   68 */   private static final Log log = LogFactory.getLog(Http11Processor.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   73 */   private static final StringManager sm = StringManager.getManager(Http11Processor.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final UserDataHelper userDataHelper;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Http11InputBuffer inputBuffer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Http11OutputBuffer outputBuffer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   94 */   private int pluggableFilterIndex = Integer.MAX_VALUE;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  100 */   protected volatile boolean keepAlive = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  107 */   protected boolean openSocket = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  113 */   protected boolean readComplete = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  118 */   protected boolean http11 = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  124 */   protected boolean http09 = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  131 */   protected boolean contentDelimitation = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  137 */   protected Pattern restrictedUserAgents = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  143 */   protected int maxKeepAliveRequests = -1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  149 */   protected int connectionUploadTimeout = 300000;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  155 */   protected boolean disableUploadTimeout = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  161 */   protected int compressionLevel = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  167 */   protected int compressionMinSize = 2048;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  173 */   protected int maxSavePostSize = 4096;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  179 */   protected Pattern noCompressionUserAgents = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String[] compressableMimeTypes;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  193 */   protected char[] hostNameC = new char[0];
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  199 */   private String server = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  207 */   private boolean serverRemoveAppProvidedValues = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  213 */   protected UpgradeToken upgradeToken = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  219 */   protected SendfileDataBase sendfileData = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final Map<String, UpgradeProtocol> httpUpgradeProtocols;
/*      */   
/*      */ 
/*      */ 
/*      */   private final boolean allowHostHeaderMismatch;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Http11Processor(int maxHttpHeaderSize, boolean allowHostHeaderMismatch, boolean rejectIllegalHeaderName, AbstractEndpoint<?> endpoint, int maxTrailerSize, Set<String> allowedTrailerHeaders, int maxExtensionSize, int maxSwallowSize, Map<String, UpgradeProtocol> httpUpgradeProtocols, boolean sendReasonPhrase)
/*      */   {
/*  235 */     super(endpoint);
/*  236 */     this.userDataHelper = new UserDataHelper(log);
/*      */     
/*  238 */     this.inputBuffer = new Http11InputBuffer(this.request, maxHttpHeaderSize, rejectIllegalHeaderName);
/*  239 */     this.request.setInputBuffer(this.inputBuffer);
/*      */     
/*  241 */     this.outputBuffer = new Http11OutputBuffer(this.response, maxHttpHeaderSize, sendReasonPhrase);
/*  242 */     this.response.setOutputBuffer(this.outputBuffer);
/*      */     
/*      */ 
/*  245 */     this.inputBuffer.addFilter(new IdentityInputFilter(maxSwallowSize));
/*  246 */     this.outputBuffer.addFilter(new IdentityOutputFilter());
/*      */     
/*      */ 
/*  249 */     this.inputBuffer.addFilter(new ChunkedInputFilter(maxTrailerSize, allowedTrailerHeaders, maxExtensionSize, maxSwallowSize));
/*      */     
/*  251 */     this.outputBuffer.addFilter(new ChunkedOutputFilter());
/*      */     
/*      */ 
/*  254 */     this.inputBuffer.addFilter(new VoidInputFilter());
/*  255 */     this.outputBuffer.addFilter(new VoidOutputFilter());
/*      */     
/*      */ 
/*  258 */     this.inputBuffer.addFilter(new BufferedInputFilter());
/*      */     
/*      */ 
/*      */ 
/*  262 */     this.outputBuffer.addFilter(new GzipOutputFilter());
/*      */     
/*  264 */     this.pluggableFilterIndex = this.inputBuffer.getFilters().length;
/*      */     
/*  266 */     this.httpUpgradeProtocols = httpUpgradeProtocols;
/*  267 */     this.allowHostHeaderMismatch = allowHostHeaderMismatch;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCompression(String compression)
/*      */   {
/*  279 */     if (compression.equals("on")) {
/*  280 */       this.compressionLevel = 1;
/*  281 */     } else if (compression.equals("force")) {
/*  282 */       this.compressionLevel = 2;
/*  283 */     } else if (compression.equals("off")) {
/*  284 */       this.compressionLevel = 0;
/*      */     }
/*      */     else {
/*      */       try
/*      */       {
/*  289 */         this.compressionMinSize = Integer.parseInt(compression);
/*  290 */         this.compressionLevel = 1;
/*      */       } catch (Exception e) {
/*  292 */         this.compressionLevel = 0;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCompressionMinSize(int compressionMinSize)
/*      */   {
/*  304 */     this.compressionMinSize = compressionMinSize;
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
/*      */   public void setNoCompressionUserAgents(String noCompressionUserAgents)
/*      */   {
/*  317 */     if ((noCompressionUserAgents == null) || (noCompressionUserAgents.length() == 0)) {
/*  318 */       this.noCompressionUserAgents = null;
/*      */     }
/*      */     else {
/*  321 */       this.noCompressionUserAgents = Pattern.compile(noCompressionUserAgents);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public void setCompressableMimeTypes(String[] compressibleMimeTypes)
/*      */   {
/*  334 */     setCompressibleMimeTypes(compressibleMimeTypes);
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
/*      */   public void setCompressibleMimeTypes(String[] compressibleMimeTypes)
/*      */   {
/*  347 */     this.compressableMimeTypes = compressibleMimeTypes;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getCompression()
/*      */   {
/*  357 */     switch (this.compressionLevel) {
/*      */     case 0: 
/*  359 */       return "off";
/*      */     case 1: 
/*  361 */       return "on";
/*      */     case 2: 
/*  363 */       return "force";
/*      */     }
/*  365 */     return "off";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean startsWithStringArray(String[] sArray, String value)
/*      */   {
/*  376 */     if (value == null) {
/*  377 */       return false;
/*      */     }
/*  379 */     for (int i = 0; i < sArray.length; i++) {
/*  380 */       if (value.startsWith(sArray[i])) {
/*  381 */         return true;
/*      */       }
/*      */     }
/*  384 */     return false;
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
/*      */   public void setRestrictedUserAgents(String restrictedUserAgents)
/*      */   {
/*  397 */     if ((restrictedUserAgents == null) || 
/*  398 */       (restrictedUserAgents.length() == 0)) {
/*  399 */       this.restrictedUserAgents = null;
/*      */     } else {
/*  401 */       this.restrictedUserAgents = Pattern.compile(restrictedUserAgents);
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
/*      */   public void setMaxKeepAliveRequests(int mkar)
/*      */   {
/*  414 */     this.maxKeepAliveRequests = mkar;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxKeepAliveRequests()
/*      */   {
/*  425 */     return this.maxKeepAliveRequests;
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
/*      */   public void setMaxSavePostSize(int msps)
/*      */   {
/*  438 */     this.maxSavePostSize = msps;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxSavePostSize()
/*      */   {
/*  448 */     return this.maxSavePostSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDisableUploadTimeout(boolean isDisabled)
/*      */   {
/*  460 */     this.disableUploadTimeout = isDisabled;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getDisableUploadTimeout()
/*      */   {
/*  469 */     return this.disableUploadTimeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setConnectionUploadTimeout(int timeout)
/*      */   {
/*  478 */     this.connectionUploadTimeout = timeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getConnectionUploadTimeout()
/*      */   {
/*  487 */     return this.connectionUploadTimeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setServer(String server)
/*      */   {
/*  497 */     if ((server == null) || (server.equals(""))) {
/*  498 */       this.server = null;
/*      */     } else {
/*  500 */       this.server = server;
/*      */     }
/*      */   }
/*      */   
/*      */   public void setServerRemoveAppProvidedValues(boolean serverRemoveAppProvidedValues)
/*      */   {
/*  506 */     this.serverRemoveAppProvidedValues = serverRemoveAppProvidedValues;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isCompressible()
/*      */   {
/*  517 */     MessageBytes contentEncodingMB = this.response.getMimeHeaders().getValue("Content-Encoding");
/*      */     
/*  519 */     if ((contentEncodingMB != null) && 
/*  520 */       (contentEncodingMB.indexOf("gzip") != -1)) {
/*  521 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  525 */     if (this.compressionLevel == 2) {
/*  526 */       return true;
/*      */     }
/*      */     
/*      */ 
/*  530 */     long contentLength = this.response.getContentLengthLong();
/*  531 */     if ((contentLength == -1L) || (contentLength > this.compressionMinSize))
/*      */     {
/*      */ 
/*  534 */       if (this.compressableMimeTypes != null) {
/*  535 */         return startsWithStringArray(this.compressableMimeTypes, this.response.getContentType());
/*      */       }
/*      */     }
/*      */     
/*  539 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean useCompression()
/*      */   {
/*  551 */     MessageBytes acceptEncodingMB = this.request.getMimeHeaders().getValue("accept-encoding");
/*      */     
/*  553 */     if ((acceptEncodingMB == null) || 
/*  554 */       (acceptEncodingMB.indexOf("gzip") == -1)) {
/*  555 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  559 */     if (this.compressionLevel == 2) {
/*  560 */       return true;
/*      */     }
/*      */     
/*      */ 
/*  564 */     if (this.noCompressionUserAgents != null)
/*      */     {
/*  566 */       MessageBytes userAgentValueMB = this.request.getMimeHeaders().getValue("user-agent");
/*  567 */       if (userAgentValueMB != null) {
/*  568 */         String userAgentValue = userAgentValueMB.toString();
/*      */         
/*  570 */         if (this.noCompressionUserAgents.matcher(userAgentValue).matches()) {
/*  571 */           return false;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  576 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int findBytes(ByteChunk bc, byte[] b)
/*      */   {
/*  586 */     byte first = b[0];
/*  587 */     byte[] buff = bc.getBuffer();
/*  588 */     int start = bc.getStart();
/*  589 */     int end = bc.getEnd();
/*      */     
/*      */ 
/*  592 */     int srcEnd = b.length;
/*      */     int srcPos;
/*  594 */     for (int i = start; i <= end - srcEnd; i++) {
/*  595 */       if (Ascii.toLower(buff[i]) == first)
/*      */       {
/*      */ 
/*      */ 
/*  599 */         int myPos = i + 1;
/*  600 */         for (srcPos = 1; (srcPos < srcEnd) && 
/*  601 */               (Ascii.toLower(buff[(myPos++)]) == b[(srcPos++)]);)
/*      */         {
/*      */ 
/*  604 */           if (srcPos == srcEnd)
/*  605 */             return i - start;
/*      */         }
/*      */       }
/*      */     }
/*  609 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean statusDropsConnection(int status)
/*      */   {
/*  618 */     return (status == 400) || (status == 408) || (status == 411) || (status == 413) || (status == 414) || (status == 500) || (status == 503) || (status == 501);
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
/*      */ 
/*      */ 
/*      */   private void addInputFilter(InputFilter[] inputFilters, String encodingName)
/*      */   {
/*  637 */     encodingName = encodingName.trim().toLowerCase(Locale.ENGLISH);
/*      */     
/*  639 */     if (!encodingName.equals("identity"))
/*      */     {
/*  641 */       if (encodingName.equals("chunked"))
/*      */       {
/*  643 */         this.inputBuffer.addActiveFilter(inputFilters[1]);
/*  644 */         this.contentDelimitation = true;
/*      */       } else {
/*  646 */         for (int i = this.pluggableFilterIndex; i < inputFilters.length; i++) {
/*  647 */           if (inputFilters[i].getEncodingName().toString().equals(encodingName)) {
/*  648 */             this.inputBuffer.addActiveFilter(inputFilters[i]);
/*  649 */             return;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  654 */         this.response.setStatus(501);
/*  655 */         setErrorState(ErrorState.CLOSE_CLEAN, null);
/*  656 */         if (log.isDebugEnabled()) {
/*  657 */           log.debug(sm.getString("http11processor.request.prepare") + " Unsupported transfer encoding [" + encodingName + "]");
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public AbstractEndpoint.Handler.SocketState service(SocketWrapperBase<?> socketWrapper)
/*      */     throws IOException
/*      */   {
/*  667 */     RequestInfo rp = this.request.getRequestProcessor();
/*  668 */     rp.setStage(1);
/*      */     
/*      */ 
/*  671 */     setSocketWrapper(socketWrapper);
/*  672 */     this.inputBuffer.init(socketWrapper);
/*  673 */     this.outputBuffer.init(socketWrapper);
/*      */     
/*      */ 
/*  676 */     this.keepAlive = true;
/*  677 */     this.openSocket = false;
/*  678 */     this.readComplete = true;
/*  679 */     boolean keptAlive = false;
/*  680 */     SendfileState sendfileState = SendfileState.DONE;
/*      */     
/*  682 */     while ((!getErrorState().isError()) && (this.keepAlive) && (!isAsync()) && (this.upgradeToken == null) && (sendfileState == SendfileState.DONE) && 
/*  683 */       (!this.endpoint.isPaused()))
/*      */     {
/*      */       try
/*      */       {
/*  687 */         if (!this.inputBuffer.parseRequestLine(keptAlive)) {
/*  688 */           if (this.inputBuffer.getParsingRequestLinePhase() == -1)
/*  689 */             return AbstractEndpoint.Handler.SocketState.UPGRADING;
/*  690 */           if (handleIncompleteRequestLineRead()) {
/*      */             break;
/*      */           }
/*      */         }
/*      */         
/*  695 */         if (this.endpoint.isPaused())
/*      */         {
/*  697 */           this.response.setStatus(503);
/*  698 */           setErrorState(ErrorState.CLOSE_CLEAN, null);
/*      */         } else {
/*  700 */           keptAlive = true;
/*      */           
/*  702 */           this.request.getMimeHeaders().setLimit(this.endpoint.getMaxHeaderCount());
/*  703 */           if (!this.inputBuffer.parseHeaders())
/*      */           {
/*      */ 
/*  706 */             this.openSocket = true;
/*  707 */             this.readComplete = false;
/*  708 */             break;
/*      */           }
/*  710 */           if (!this.disableUploadTimeout) {
/*  711 */             socketWrapper.setReadTimeout(this.connectionUploadTimeout);
/*      */           }
/*      */         }
/*      */       } catch (IOException e) {
/*  715 */         if (log.isDebugEnabled()) {
/*  716 */           log.debug(sm.getString("http11processor.header.parse"), e);
/*      */         }
/*  718 */         setErrorState(ErrorState.CLOSE_CONNECTION_NOW, e);
/*  719 */         break;
/*      */       } catch (Throwable t) {
/*  721 */         ExceptionUtils.handleThrowable(t);
/*  722 */         UserDataHelper.Mode logMode = this.userDataHelper.getNextMode();
/*  723 */         if (logMode != null) {
/*  724 */           String message = sm.getString("http11processor.header.parse");
/*  725 */           switch (logMode) {
/*      */           case INFO_THEN_DEBUG: 
/*  727 */             message = message + sm.getString("http11processor.fallToDebug");
/*      */           
/*      */           case INFO: 
/*  730 */             log.info(message, t);
/*  731 */             break;
/*      */           case DEBUG: 
/*  733 */             log.debug(message, t);
/*      */           }
/*      */           
/*      */         }
/*  737 */         this.response.setStatus(400);
/*  738 */         setErrorState(ErrorState.CLOSE_CLEAN, t);
/*  739 */         getAdapter().log(this.request, this.response, 0L);
/*      */       }
/*      */       
/*      */ 
/*  743 */       Enumeration<String> connectionValues = this.request.getMimeHeaders().values("Connection");
/*  744 */       boolean foundUpgrade = false;
/*  745 */       while ((connectionValues.hasMoreElements()) && (!foundUpgrade))
/*      */       {
/*  747 */         foundUpgrade = ((String)connectionValues.nextElement()).toLowerCase(Locale.ENGLISH).contains("upgrade");
/*      */       }
/*      */       
/*  750 */       if (foundUpgrade)
/*      */       {
/*  752 */         String requestedProtocol = this.request.getHeader("Upgrade");
/*      */         
/*  754 */         UpgradeProtocol upgradeProtocol = (UpgradeProtocol)this.httpUpgradeProtocols.get(requestedProtocol);
/*  755 */         if ((upgradeProtocol != null) && 
/*  756 */           (upgradeProtocol.accept(this.request)))
/*      */         {
/*      */ 
/*  759 */           this.response.setStatus(101);
/*  760 */           this.response.setHeader("Connection", "Upgrade");
/*  761 */           this.response.setHeader("Upgrade", requestedProtocol);
/*  762 */           action(ActionCode.CLOSE, null);
/*  763 */           getAdapter().log(this.request, this.response, 0L);
/*      */           
/*      */ 
/*  766 */           InternalHttpUpgradeHandler upgradeHandler = upgradeProtocol.getInternalUpgradeHandler(
/*  767 */             getAdapter(), cloneRequest(this.request));
/*  768 */           UpgradeToken upgradeToken = new UpgradeToken(upgradeHandler, null, null);
/*  769 */           action(ActionCode.UPGRADE, upgradeToken);
/*  770 */           return AbstractEndpoint.Handler.SocketState.UPGRADING;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  775 */       if (!getErrorState().isError())
/*      */       {
/*  777 */         rp.setStage(2);
/*      */         try {
/*  779 */           prepareRequest();
/*      */         } catch (Throwable t) {
/*  781 */           ExceptionUtils.handleThrowable(t);
/*  782 */           if (log.isDebugEnabled()) {
/*  783 */             log.debug(sm.getString("http11processor.request.prepare"), t);
/*      */           }
/*      */           
/*  786 */           this.response.setStatus(500);
/*  787 */           setErrorState(ErrorState.CLOSE_CLEAN, t);
/*  788 */           getAdapter().log(this.request, this.response, 0L);
/*      */         }
/*      */       }
/*      */       
/*  792 */       if (this.maxKeepAliveRequests == 1) {
/*  793 */         this.keepAlive = false;
/*  794 */       } else if ((this.maxKeepAliveRequests > 0) && 
/*  795 */         (socketWrapper.decrementKeepAlive() <= 0)) {
/*  796 */         this.keepAlive = false;
/*      */       }
/*      */       
/*      */ 
/*  800 */       if (!getErrorState().isError()) {
/*      */         try {
/*  802 */           rp.setStage(3);
/*  803 */           getAdapter().service(this.request, this.response);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  809 */           if ((this.keepAlive) && (!getErrorState().isError()) && (!isAsync()) && 
/*  810 */             (statusDropsConnection(this.response.getStatus()))) {
/*  811 */             setErrorState(ErrorState.CLOSE_CLEAN, null);
/*      */           }
/*      */         } catch (InterruptedIOException e) {
/*  814 */           setErrorState(ErrorState.CLOSE_CONNECTION_NOW, e);
/*      */         } catch (HeadersTooLargeException e) {
/*  816 */           log.error(sm.getString("http11processor.request.process"), e);
/*      */           
/*      */ 
/*  819 */           if (this.response.isCommitted()) {
/*  820 */             setErrorState(ErrorState.CLOSE_NOW, e);
/*      */           } else {
/*  822 */             this.response.reset();
/*  823 */             this.response.setStatus(500);
/*  824 */             setErrorState(ErrorState.CLOSE_CLEAN, e);
/*  825 */             this.response.setHeader("Connection", "close");
/*      */           }
/*      */         } catch (Throwable t) {
/*  828 */           ExceptionUtils.handleThrowable(t);
/*  829 */           log.error(sm.getString("http11processor.request.process"), t);
/*      */           
/*  831 */           this.response.setStatus(500);
/*  832 */           setErrorState(ErrorState.CLOSE_CLEAN, t);
/*  833 */           getAdapter().log(this.request, this.response, 0L);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  838 */       rp.setStage(4);
/*  839 */       if (!isAsync())
/*      */       {
/*      */ 
/*      */ 
/*  843 */         endRequest();
/*      */       }
/*  845 */       rp.setStage(5);
/*      */       
/*      */ 
/*      */ 
/*  849 */       if (getErrorState().isError()) {
/*  850 */         this.response.setStatus(500);
/*      */       }
/*      */       
/*  853 */       if ((!isAsync()) || (getErrorState().isError())) {
/*  854 */         this.request.updateCounters();
/*  855 */         if (getErrorState().isIoAllowed()) {
/*  856 */           this.inputBuffer.nextRequest();
/*  857 */           this.outputBuffer.nextRequest();
/*      */         }
/*      */       }
/*      */       
/*  861 */       if (!this.disableUploadTimeout) {
/*  862 */         int soTimeout = this.endpoint.getConnectionTimeout();
/*  863 */         if (soTimeout > 0) {
/*  864 */           socketWrapper.setReadTimeout(soTimeout);
/*      */         } else {
/*  866 */           socketWrapper.setReadTimeout(0L);
/*      */         }
/*      */       }
/*      */       
/*  870 */       rp.setStage(6);
/*      */       
/*  872 */       sendfileState = processSendfile(socketWrapper);
/*      */     }
/*      */     
/*  875 */     rp.setStage(7);
/*      */     
/*  877 */     if ((getErrorState().isError()) || (this.endpoint.isPaused()))
/*  878 */       return AbstractEndpoint.Handler.SocketState.CLOSED;
/*  879 */     if (isAsync())
/*  880 */       return AbstractEndpoint.Handler.SocketState.LONG;
/*  881 */     if (isUpgrade()) {
/*  882 */       return AbstractEndpoint.Handler.SocketState.UPGRADING;
/*      */     }
/*  884 */     if (sendfileState == SendfileState.PENDING) {
/*  885 */       return AbstractEndpoint.Handler.SocketState.SENDFILE;
/*      */     }
/*  887 */     if (this.openSocket) {
/*  888 */       if (this.readComplete) {
/*  889 */         return AbstractEndpoint.Handler.SocketState.OPEN;
/*      */       }
/*  891 */       return AbstractEndpoint.Handler.SocketState.LONG;
/*      */     }
/*      */     
/*  894 */     return AbstractEndpoint.Handler.SocketState.CLOSED;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private Request cloneRequest(Request source)
/*      */     throws IOException
/*      */   {
/*  902 */     Request dest = new Request();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  907 */     dest.decodedURI().duplicate(source.decodedURI());
/*  908 */     dest.method().duplicate(source.method());
/*  909 */     dest.getMimeHeaders().duplicate(source.getMimeHeaders());
/*  910 */     dest.requestURI().duplicate(source.requestURI());
/*      */     
/*  912 */     return dest;
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean handleIncompleteRequestLineRead()
/*      */   {
/*  918 */     this.openSocket = true;
/*      */     
/*  920 */     if (this.inputBuffer.getParsingRequestLinePhase() > 1)
/*      */     {
/*  922 */       if (this.endpoint.isPaused())
/*      */       {
/*  924 */         this.response.setStatus(503);
/*  925 */         setErrorState(ErrorState.CLOSE_CLEAN, null);
/*  926 */         getAdapter().log(this.request, this.response, 0L);
/*  927 */         return false;
/*      */       }
/*      */       
/*  930 */       this.readComplete = false;
/*      */     }
/*      */     
/*  933 */     return true;
/*      */   }
/*      */   
/*      */   private void checkExpectationAndResponseStatus()
/*      */   {
/*  938 */     if ((this.request.hasExpectation()) && (
/*  939 */       (this.response.getStatus() < 200) || (this.response.getStatus() > 299)))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  946 */       this.inputBuffer.setSwallowInput(false);
/*  947 */       this.keepAlive = false;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void prepareRequest()
/*      */   {
/*  957 */     this.http11 = true;
/*  958 */     this.http09 = false;
/*  959 */     this.contentDelimitation = false;
/*      */     
/*  961 */     if (this.endpoint.isSSLEnabled()) {
/*  962 */       this.request.scheme().setString("https");
/*      */     }
/*  964 */     MessageBytes protocolMB = this.request.protocol();
/*  965 */     if (protocolMB.equals("HTTP/1.1")) {
/*  966 */       this.http11 = true;
/*  967 */       protocolMB.setString("HTTP/1.1");
/*  968 */     } else if (protocolMB.equals("HTTP/1.0")) {
/*  969 */       this.http11 = false;
/*  970 */       this.keepAlive = false;
/*  971 */       protocolMB.setString("HTTP/1.0");
/*  972 */     } else if (protocolMB.equals(""))
/*      */     {
/*  974 */       this.http09 = true;
/*  975 */       this.http11 = false;
/*  976 */       this.keepAlive = false;
/*      */     }
/*      */     else {
/*  979 */       this.http11 = false;
/*      */       
/*  981 */       this.response.setStatus(505);
/*  982 */       setErrorState(ErrorState.CLOSE_CLEAN, null);
/*  983 */       if (log.isDebugEnabled()) {
/*  984 */         log.debug(sm.getString("http11processor.request.prepare") + " Unsupported HTTP version \"" + protocolMB + "\"");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  989 */     MimeHeaders headers = this.request.getMimeHeaders();
/*      */     
/*      */ 
/*  992 */     MessageBytes connectionValueMB = headers.getValue("Connection");
/*  993 */     if (connectionValueMB != null) {
/*  994 */       ByteChunk connectionValueBC = connectionValueMB.getByteChunk();
/*  995 */       if (findBytes(connectionValueBC, Constants.CLOSE_BYTES) != -1) {
/*  996 */         this.keepAlive = false;
/*  997 */       } else if (findBytes(connectionValueBC, Constants.KEEPALIVE_BYTES) != -1)
/*      */       {
/*  999 */         this.keepAlive = true;
/*      */       }
/*      */     }
/*      */     
/* 1003 */     if (this.http11) {
/* 1004 */       MessageBytes expectMB = headers.getValue("expect");
/* 1005 */       if (expectMB != null) {
/* 1006 */         if (expectMB.indexOfIgnoreCase("100-continue", 0) != -1) {
/* 1007 */           this.inputBuffer.setSwallowInput(false);
/* 1008 */           this.request.setExpectation(true);
/*      */         } else {
/* 1010 */           this.response.setStatus(417);
/* 1011 */           setErrorState(ErrorState.CLOSE_CLEAN, null);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1017 */     if ((this.restrictedUserAgents != null) && ((this.http11) || (this.keepAlive))) {
/* 1018 */       MessageBytes userAgentValueMB = headers.getValue("user-agent");
/*      */       
/*      */ 
/* 1021 */       if (userAgentValueMB != null) {
/* 1022 */         String userAgentValue = userAgentValueMB.toString();
/* 1023 */         if ((this.restrictedUserAgents != null) && 
/* 1024 */           (this.restrictedUserAgents.matcher(userAgentValue).matches())) {
/* 1025 */           this.http11 = false;
/* 1026 */           this.keepAlive = false;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1033 */     MessageBytes hostValueMB = null;
/*      */     try {
/* 1035 */       hostValueMB = headers.getUniqueValue("host");
/*      */     }
/*      */     catch (IllegalArgumentException iae)
/*      */     {
/* 1039 */       this.response.setStatus(400);
/* 1040 */       setErrorState(ErrorState.CLOSE_CLEAN, null);
/* 1041 */       if (log.isDebugEnabled()) {
/* 1042 */         log.debug(sm.getString("http11processor.request.multipleHosts"));
/*      */       }
/*      */     }
/* 1045 */     if ((this.http11) && (hostValueMB == null))
/*      */     {
/* 1047 */       this.response.setStatus(400);
/* 1048 */       setErrorState(ErrorState.CLOSE_CLEAN, null);
/* 1049 */       if (log.isDebugEnabled()) {
/* 1050 */         log.debug(sm.getString("http11processor.request.prepare") + " host header missing");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1056 */     ByteChunk uriBC = this.request.requestURI().getByteChunk();
/* 1057 */     if (uriBC.startsWithIgnoreCase("http", 0))
/*      */     {
/* 1059 */       int pos = uriBC.indexOf("://", 0, 3, 4);
/* 1060 */       int uriBCStart = uriBC.getStart();
/* 1061 */       int slashPos = -1;
/* 1062 */       if (pos != -1) {
/* 1063 */         pos += 3;
/* 1064 */         byte[] uriB = uriBC.getBytes();
/* 1065 */         slashPos = uriBC.indexOf('/', pos);
/* 1066 */         int atPos = uriBC.indexOf('@', pos);
/* 1067 */         if (slashPos == -1) {
/* 1068 */           slashPos = uriBC.getLength();
/*      */           
/* 1070 */           this.request.requestURI()
/* 1071 */             .setBytes(uriB, uriBCStart + pos - 2, 1);
/*      */         }
/*      */         else {
/* 1074 */           this.request.requestURI().setBytes(uriB, uriBCStart + slashPos, uriBC
/* 1075 */             .getLength() - slashPos);
/*      */         }
/*      */         
/* 1078 */         if (atPos != -1) {
/* 1079 */           pos = atPos + 1;
/*      */         }
/* 1081 */         if (this.http11)
/*      */         {
/* 1083 */           if (hostValueMB != null)
/*      */           {
/*      */ 
/* 1086 */             if (!hostValueMB.getByteChunk().equals(uriB, uriBCStart + pos, slashPos - pos))
/*      */             {
/* 1088 */               if (this.allowHostHeaderMismatch)
/*      */               {
/*      */ 
/*      */ 
/*      */ 
/* 1093 */                 hostValueMB = headers.setValue("host");
/* 1094 */                 hostValueMB.setBytes(uriB, uriBCStart + pos, slashPos - pos);
/*      */ 
/*      */               }
/*      */               else
/*      */               {
/* 1099 */                 this.response.setStatus(400);
/* 1100 */                 setErrorState(ErrorState.CLOSE_CLEAN, null);
/* 1101 */                 if (log.isDebugEnabled()) {
/* 1102 */                   log.debug(sm.getString("http11processor.request.inconsistentHosts"));
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1110 */           hostValueMB = headers.setValue("host");
/* 1111 */           hostValueMB.setBytes(uriB, uriBCStart + pos, slashPos - pos);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1117 */     InputFilter[] inputFilters = this.inputBuffer.getFilters();
/*      */     
/*      */ 
/* 1120 */     if (this.http11) {
/* 1121 */       MessageBytes transferEncodingValueMB = headers.getValue("transfer-encoding");
/* 1122 */       if (transferEncodingValueMB != null) {
/* 1123 */         String transferEncodingValue = transferEncodingValueMB.toString();
/*      */         
/* 1125 */         int startPos = 0;
/* 1126 */         int commaPos = transferEncodingValue.indexOf(',');
/* 1127 */         String encodingName = null;
/* 1128 */         while (commaPos != -1) {
/* 1129 */           encodingName = transferEncodingValue.substring(startPos, commaPos);
/* 1130 */           addInputFilter(inputFilters, encodingName);
/* 1131 */           startPos = commaPos + 1;
/* 1132 */           commaPos = transferEncodingValue.indexOf(',', startPos);
/*      */         }
/* 1134 */         encodingName = transferEncodingValue.substring(startPos);
/* 1135 */         addInputFilter(inputFilters, encodingName);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1140 */     long contentLength = this.request.getContentLengthLong();
/* 1141 */     if (contentLength >= 0L) {
/* 1142 */       if (this.contentDelimitation)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1148 */         headers.removeHeader("content-length");
/* 1149 */         this.request.setContentLength(-1L);
/*      */       }
/*      */       else {
/* 1152 */         this.inputBuffer.addActiveFilter(inputFilters[0]);
/* 1153 */         this.contentDelimitation = true;
/*      */       }
/*      */     }
/*      */     
/* 1157 */     parseHost(hostValueMB);
/*      */     
/* 1159 */     if (!this.contentDelimitation)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1164 */       this.inputBuffer.addActiveFilter(inputFilters[2]);
/* 1165 */       this.contentDelimitation = true;
/*      */     }
/*      */     
/* 1168 */     if (getErrorState().isError()) {
/* 1169 */       getAdapter().log(this.request, this.response, 0L);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void prepareResponse()
/*      */     throws IOException
/*      */   {
/* 1181 */     boolean entityBody = true;
/* 1182 */     this.contentDelimitation = false;
/*      */     
/* 1184 */     OutputFilter[] outputFilters = this.outputBuffer.getFilters();
/*      */     
/* 1186 */     if (this.http09 == true)
/*      */     {
/* 1188 */       this.outputBuffer.addActiveFilter(outputFilters[0]);
/* 1189 */       this.outputBuffer.commit();
/* 1190 */       return;
/*      */     }
/*      */     
/* 1193 */     int statusCode = this.response.getStatus();
/* 1194 */     if ((statusCode < 200) || (statusCode == 204) || (statusCode == 205) || (statusCode == 304))
/*      */     {
/*      */ 
/*      */ 
/* 1198 */       this.outputBuffer.addActiveFilter(outputFilters[2]);
/* 1199 */       entityBody = false;
/* 1200 */       this.contentDelimitation = true;
/* 1201 */       if (statusCode == 205)
/*      */       {
/*      */ 
/* 1204 */         this.response.setContentLength(0L);
/*      */       } else {
/* 1206 */         this.response.setContentLength(-1L);
/*      */       }
/*      */     }
/*      */     
/* 1210 */     MessageBytes methodMB = this.request.method();
/* 1211 */     if (methodMB.equals("HEAD"))
/*      */     {
/*      */ 
/* 1214 */       this.outputBuffer.addActiveFilter(outputFilters[2]);
/* 1215 */       this.contentDelimitation = true;
/*      */     }
/*      */     
/*      */ 
/* 1219 */     if (this.endpoint.getUseSendfile()) {
/* 1220 */       prepareSendfile(outputFilters);
/*      */     }
/*      */     
/*      */ 
/* 1224 */     boolean isCompressible = false;
/* 1225 */     boolean useCompression = false;
/* 1226 */     if ((entityBody) && (this.compressionLevel > 0) && (this.sendfileData == null)) {
/* 1227 */       isCompressible = isCompressible();
/* 1228 */       if (isCompressible) {
/* 1229 */         useCompression = useCompression();
/*      */       }
/*      */       
/* 1232 */       if (useCompression) {
/* 1233 */         this.response.setContentLength(-1L);
/*      */       }
/*      */     }
/*      */     
/* 1237 */     MimeHeaders headers = this.response.getMimeHeaders();
/*      */     
/* 1239 */     if ((entityBody) || (statusCode == 204)) {
/* 1240 */       String contentType = this.response.getContentType();
/* 1241 */       if (contentType != null) {
/* 1242 */         headers.setValue("Content-Type").setString(contentType);
/*      */       }
/* 1244 */       String contentLanguage = this.response.getContentLanguage();
/* 1245 */       if (contentLanguage != null)
/*      */       {
/* 1247 */         headers.setValue("Content-Language").setString(contentLanguage);
/*      */       }
/*      */     }
/*      */     
/* 1251 */     long contentLength = this.response.getContentLengthLong();
/* 1252 */     boolean connectionClosePresent = false;
/* 1253 */     if (contentLength != -1L) {
/* 1254 */       headers.setValue("Content-Length").setLong(contentLength);
/* 1255 */       this.outputBuffer
/* 1256 */         .addActiveFilter(outputFilters[0]);
/* 1257 */       this.contentDelimitation = true;
/*      */     }
/*      */     else
/*      */     {
/* 1261 */       connectionClosePresent = isConnectionClose(headers);
/* 1262 */       if ((entityBody) && (this.http11) && (!connectionClosePresent))
/*      */       {
/* 1264 */         this.outputBuffer.addActiveFilter(outputFilters[1]);
/* 1265 */         this.contentDelimitation = true;
/* 1266 */         headers.addValue("Transfer-Encoding").setString("chunked");
/*      */       }
/*      */       else {
/* 1269 */         this.outputBuffer.addActiveFilter(outputFilters[0]);
/*      */       }
/*      */     }
/*      */     
/* 1273 */     if (useCompression) {
/* 1274 */       this.outputBuffer.addActiveFilter(outputFilters[3]);
/* 1275 */       headers.setValue("Content-Encoding").setString("gzip");
/*      */     }
/*      */     
/* 1278 */     if (isCompressible)
/*      */     {
/* 1280 */       MessageBytes vary = headers.getValue("Vary");
/* 1281 */       if (vary == null)
/*      */       {
/* 1283 */         headers.setValue("Vary").setString("Accept-Encoding");
/* 1284 */       } else if (!vary.equals("*"))
/*      */       {
/*      */ 
/*      */ 
/* 1288 */         headers.setValue("Vary").setString(vary
/* 1289 */           .getString() + ",Accept-Encoding");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1295 */     if (headers.getValue("Date") == null) {
/* 1296 */       headers.addValue("Date").setString(
/* 1297 */         FastHttpDateFormat.getCurrentDate());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1302 */     if ((entityBody) && (!this.contentDelimitation))
/*      */     {
/*      */ 
/* 1305 */       this.keepAlive = false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1310 */     checkExpectationAndResponseStatus();
/*      */     
/*      */ 
/*      */ 
/* 1314 */     if ((this.keepAlive) && (statusDropsConnection(statusCode))) {
/* 1315 */       this.keepAlive = false;
/*      */     }
/* 1317 */     if (!this.keepAlive)
/*      */     {
/* 1319 */       if (!connectionClosePresent) {
/* 1320 */         headers.addValue("Connection").setString("close");
/*      */       }
/*      */     }
/* 1323 */     else if ((!this.http11) && (!getErrorState().isError())) {
/* 1324 */       headers.addValue("Connection").setString("keep-alive");
/*      */     }
/*      */     
/*      */ 
/* 1328 */     if (this.server == null) {
/* 1329 */       if (this.serverRemoveAppProvidedValues) {
/* 1330 */         headers.removeHeader("server");
/*      */       }
/*      */     }
/*      */     else {
/* 1334 */       headers.setValue("Server").setString(this.server);
/*      */     }
/*      */     
/*      */     try
/*      */     {
/* 1339 */       this.outputBuffer.sendStatus();
/*      */       
/* 1341 */       int size = headers.size();
/* 1342 */       for (int i = 0; i < size; i++) {
/* 1343 */         this.outputBuffer.sendHeader(headers.getName(i), headers.getValue(i));
/*      */       }
/* 1345 */       this.outputBuffer.endHeaders();
/*      */     } catch (Throwable t) {
/* 1347 */       ExceptionUtils.handleThrowable(t);
/*      */       
/*      */ 
/* 1350 */       this.outputBuffer.resetHeaderBuffer();
/* 1351 */       throw t;
/*      */     }
/*      */     
/* 1354 */     this.outputBuffer.commit();
/*      */   }
/*      */   
/*      */   private static boolean isConnectionClose(MimeHeaders headers) {
/* 1358 */     MessageBytes connection = headers.getValue("Connection");
/* 1359 */     if (connection == null) {
/* 1360 */       return false;
/*      */     }
/* 1362 */     return connection.equals("close");
/*      */   }
/*      */   
/*      */   private void prepareSendfile(OutputFilter[] outputFilters) {
/* 1366 */     String fileName = (String)this.request.getAttribute("org.apache.tomcat.sendfile.filename");
/*      */     
/* 1368 */     if (fileName == null) {
/* 1369 */       this.sendfileData = null;
/*      */     }
/*      */     else {
/* 1372 */       this.outputBuffer.addActiveFilter(outputFilters[2]);
/* 1373 */       this.contentDelimitation = true;
/*      */       
/* 1375 */       long pos = ((Long)this.request.getAttribute("org.apache.tomcat.sendfile.start")).longValue();
/*      */       
/* 1377 */       long end = ((Long)this.request.getAttribute("org.apache.tomcat.sendfile.end")).longValue();
/* 1378 */       this.sendfileData = this.socketWrapper.createSendfileData(fileName, pos, end - pos);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void parseHost(MessageBytes valueMB)
/*      */   {
/* 1387 */     if ((valueMB == null) || (valueMB.isNull()))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1392 */       this.request.setServerPort(this.endpoint.getPort());
/* 1393 */       return;
/*      */     }
/*      */     
/* 1396 */     ByteChunk valueBC = valueMB.getByteChunk();
/* 1397 */     byte[] valueB = valueBC.getBytes();
/* 1398 */     int valueL = valueBC.getLength();
/* 1399 */     int valueS = valueBC.getStart();
/* 1400 */     int colonPos = -1;
/* 1401 */     if (this.hostNameC.length < valueL) {
/* 1402 */       this.hostNameC = new char[valueL];
/*      */     }
/*      */     
/* 1405 */     boolean ipv6 = valueB[valueS] == 91;
/* 1406 */     boolean bracketClosed = false;
/* 1407 */     for (int i = 0; i < valueL; i++) {
/* 1408 */       char b = (char)valueB[(i + valueS)];
/* 1409 */       this.hostNameC[i] = b;
/* 1410 */       if (b == ']') {
/* 1411 */         bracketClosed = true;
/* 1412 */       } else if ((b == ':') && (
/* 1413 */         (!ipv6) || (bracketClosed))) {
/* 1414 */         colonPos = i;
/* 1415 */         break;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1420 */     if (colonPos < 0) {
/* 1421 */       this.request.serverName().setChars(this.hostNameC, 0, valueL);
/*      */     } else {
/* 1423 */       this.request.serverName().setChars(this.hostNameC, 0, colonPos);
/*      */       
/* 1425 */       int port = 0;
/* 1426 */       int mult = 1;
/* 1427 */       for (int i = valueL - 1; i > colonPos; i--) {
/* 1428 */         int charValue = HexUtils.getDec(valueB[(i + valueS)]);
/* 1429 */         if ((charValue == -1) || (charValue > 9))
/*      */         {
/*      */ 
/* 1432 */           this.response.setStatus(400);
/* 1433 */           setErrorState(ErrorState.CLOSE_CLEAN, null);
/* 1434 */           break;
/*      */         }
/* 1436 */         port += charValue * mult;
/* 1437 */         mult = 10 * mult;
/*      */       }
/* 1439 */       this.request.setServerPort(port);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected boolean flushBufferedWrite()
/*      */     throws IOException
/*      */   {
/* 1447 */     if ((this.outputBuffer.hasDataToWrite()) && 
/* 1448 */       (this.outputBuffer.flushBuffer(false)))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1456 */       this.outputBuffer.registerWriteInterest();
/* 1457 */       return true;
/*      */     }
/*      */     
/* 1460 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   protected AbstractEndpoint.Handler.SocketState dispatchEndRequest()
/*      */   {
/* 1466 */     if (!this.keepAlive) {
/* 1467 */       return AbstractEndpoint.Handler.SocketState.CLOSED;
/*      */     }
/* 1469 */     endRequest();
/* 1470 */     this.inputBuffer.nextRequest();
/* 1471 */     this.outputBuffer.nextRequest();
/* 1472 */     if (this.socketWrapper.isReadPending()) {
/* 1473 */       return AbstractEndpoint.Handler.SocketState.LONG;
/*      */     }
/* 1475 */     return AbstractEndpoint.Handler.SocketState.OPEN;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Log getLog()
/*      */   {
/* 1483 */     return log;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void endRequest()
/*      */   {
/* 1493 */     if (getErrorState().isError())
/*      */     {
/*      */ 
/*      */ 
/* 1497 */       this.inputBuffer.setSwallowInput(false);
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1502 */       checkExpectationAndResponseStatus();
/*      */     }
/*      */     
/*      */ 
/* 1506 */     if (getErrorState().isIoAllowed()) {
/*      */       try {
/* 1508 */         this.inputBuffer.endRequest();
/*      */       } catch (IOException e) {
/* 1510 */         setErrorState(ErrorState.CLOSE_CONNECTION_NOW, e);
/*      */       } catch (Throwable t) {
/* 1512 */         ExceptionUtils.handleThrowable(t);
/*      */         
/*      */ 
/*      */ 
/* 1516 */         this.response.setStatus(500);
/* 1517 */         setErrorState(ErrorState.CLOSE_NOW, t);
/* 1518 */         log.error(sm.getString("http11processor.request.finish"), t);
/*      */       }
/*      */     }
/* 1521 */     if (getErrorState().isIoAllowed()) {
/*      */       try {
/* 1523 */         action(ActionCode.COMMIT, null);
/* 1524 */         this.outputBuffer.end();
/*      */       } catch (IOException e) {
/* 1526 */         setErrorState(ErrorState.CLOSE_CONNECTION_NOW, e);
/*      */       } catch (Throwable t) {
/* 1528 */         ExceptionUtils.handleThrowable(t);
/* 1529 */         setErrorState(ErrorState.CLOSE_NOW, t);
/* 1530 */         log.error(sm.getString("http11processor.response.finish"), t);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected final void finishResponse()
/*      */     throws IOException
/*      */   {
/* 1538 */     this.outputBuffer.end();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void ack()
/*      */   {
/* 1547 */     if ((!this.response.isCommitted()) && (this.request.hasExpectation())) {
/* 1548 */       this.inputBuffer.setSwallowInput(true);
/*      */       try {
/* 1550 */         this.outputBuffer.sendAck();
/*      */       } catch (IOException e) {
/* 1552 */         setErrorState(ErrorState.CLOSE_CONNECTION_NOW, e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected final void flush()
/*      */     throws IOException
/*      */   {
/* 1560 */     this.outputBuffer.flush();
/*      */   }
/*      */   
/*      */ 
/*      */   protected final int available(boolean doRead)
/*      */   {
/* 1566 */     return this.inputBuffer.available(doRead);
/*      */   }
/*      */   
/*      */ 
/*      */   protected final void setRequestBody(ByteChunk body)
/*      */   {
/* 1572 */     InputFilter savedBody = new SavedRequestInputFilter(body);
/* 1573 */     savedBody.setRequest(this.request);
/*      */     
/* 1575 */     Http11InputBuffer internalBuffer = (Http11InputBuffer)this.request.getInputBuffer();
/* 1576 */     internalBuffer.addActiveFilter(savedBody);
/*      */   }
/*      */   
/*      */ 
/*      */   protected final void setSwallowResponse()
/*      */   {
/* 1582 */     this.outputBuffer.responseFinished = true;
/*      */   }
/*      */   
/*      */ 
/*      */   protected final void disableSwallowRequest()
/*      */   {
/* 1588 */     this.inputBuffer.setSwallowInput(false);
/*      */   }
/*      */   
/*      */   protected final void sslReHandShake()
/*      */     throws IOException
/*      */   {
/* 1594 */     if (this.sslSupport != null)
/*      */     {
/*      */ 
/* 1597 */       InputFilter[] inputFilters = this.inputBuffer.getFilters();
/* 1598 */       ((BufferedInputFilter)inputFilters[3]).setLimit(this.maxSavePostSize);
/*      */       
/* 1600 */       this.inputBuffer.addActiveFilter(inputFilters[3]);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1607 */       this.socketWrapper.doClientAuth(this.sslSupport);
/*      */       
/*      */ 
/*      */ 
/*      */       try
/*      */       {
/* 1613 */         Object sslO = this.sslSupport.getPeerCertificateChain();
/* 1614 */         if (sslO != null) {
/* 1615 */           this.request.setAttribute("javax.servlet.request.X509Certificate", sslO);
/*      */         }
/*      */       } catch (IOException ioe) {
/* 1618 */         log.warn(sm.getString("http11processor.socket.ssl"), ioe);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected final boolean isRequestBodyFullyRead()
/*      */   {
/* 1626 */     return this.inputBuffer.isFinished();
/*      */   }
/*      */   
/*      */ 
/*      */   protected final void registerReadInterest()
/*      */   {
/* 1632 */     this.socketWrapper.registerReadInterest();
/*      */   }
/*      */   
/*      */ 
/*      */   protected final boolean isReady()
/*      */   {
/* 1638 */     return this.outputBuffer.isReady();
/*      */   }
/*      */   
/*      */ 
/*      */   public UpgradeToken getUpgradeToken()
/*      */   {
/* 1644 */     return this.upgradeToken;
/*      */   }
/*      */   
/*      */ 
/*      */   protected final void doHttpUpgrade(UpgradeToken upgradeToken)
/*      */   {
/* 1650 */     this.upgradeToken = upgradeToken;
/*      */     
/* 1652 */     this.outputBuffer.responseFinished = true;
/*      */   }
/*      */   
/*      */ 
/*      */   public ByteBuffer getLeftoverInput()
/*      */   {
/* 1658 */     return this.inputBuffer.getLeftover();
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean isUpgrade()
/*      */   {
/* 1664 */     return this.upgradeToken != null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private SendfileState processSendfile(SocketWrapperBase<?> socketWrapper)
/*      */   {
/* 1674 */     this.openSocket = this.keepAlive;
/*      */     
/* 1676 */     SendfileState result = SendfileState.DONE;
/*      */     
/* 1678 */     if ((this.sendfileData != null) && (!getErrorState().isError())) {
/* 1679 */       if (this.keepAlive) {
/* 1680 */         if (available(false) == 0) {
/* 1681 */           this.sendfileData.keepAliveState = SendfileKeepAliveState.OPEN;
/*      */         } else {
/* 1683 */           this.sendfileData.keepAliveState = SendfileKeepAliveState.PIPELINED;
/*      */         }
/*      */       } else {
/* 1686 */         this.sendfileData.keepAliveState = SendfileKeepAliveState.NONE;
/*      */       }
/* 1688 */       result = socketWrapper.processSendfile(this.sendfileData);
/* 1689 */       switch (result)
/*      */       {
/*      */       case ERROR: 
/* 1692 */         if (log.isDebugEnabled()) {
/* 1693 */           log.debug(sm.getString("http11processor.sendfile.error"));
/*      */         }
/* 1695 */         setErrorState(ErrorState.CLOSE_CONNECTION_NOW, null);
/*      */       }
/*      */       
/* 1698 */       this.sendfileData = null;
/*      */     }
/*      */     
/* 1701 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */   public final void recycle()
/*      */   {
/* 1707 */     getAdapter().checkRecycled(this.request, this.response);
/* 1708 */     super.recycle();
/* 1709 */     this.inputBuffer.recycle();
/* 1710 */     this.outputBuffer.recycle();
/* 1711 */     this.upgradeToken = null;
/* 1712 */     this.socketWrapper = null;
/* 1713 */     this.sendfileData = null;
/*      */   }
/*      */   
/*      */   public void pause() {}
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\Http11Processor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */