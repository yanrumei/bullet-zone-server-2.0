/*      */ package org.apache.catalina.connector;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.PrintWriter;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.nio.charset.Charset;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Enumeration;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Set;
/*      */ import java.util.TimeZone;
/*      */ import java.util.Vector;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import javax.servlet.ServletContext;
/*      */ import javax.servlet.ServletOutputStream;
/*      */ import javax.servlet.ServletResponse;
/*      */ import javax.servlet.SessionTrackingMode;
/*      */ import javax.servlet.http.Cookie;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import javax.servlet.http.HttpServletResponseWrapper;
/*      */ import org.apache.catalina.Context;
/*      */ import org.apache.catalina.Globals;
/*      */ import org.apache.catalina.Session;
/*      */ import org.apache.catalina.Wrapper;
/*      */ import org.apache.catalina.security.SecurityUtil;
/*      */ import org.apache.catalina.util.SessionConfig;
/*      */ import org.apache.coyote.ActionCode;
/*      */ import org.apache.coyote.Constants;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.util.buf.CharChunk;
/*      */ import org.apache.tomcat.util.buf.MessageBytes;
/*      */ import org.apache.tomcat.util.buf.UEncoder;
/*      */ import org.apache.tomcat.util.buf.UEncoder.SafeCharsSet;
/*      */ import org.apache.tomcat.util.buf.UriUtil;
/*      */ import org.apache.tomcat.util.http.CookieProcessor;
/*      */ import org.apache.tomcat.util.http.FastHttpDateFormat;
/*      */ import org.apache.tomcat.util.http.MimeHeaders;
/*      */ import org.apache.tomcat.util.http.parser.MediaTypeCache;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ import org.apache.tomcat.util.security.Escape;
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
/*      */ public class Response
/*      */   implements HttpServletResponse
/*      */ {
/*   73 */   private static final Log log = LogFactory.getLog(Response.class);
/*   74 */   protected static final StringManager sm = StringManager.getManager(Response.class);
/*      */   
/*   76 */   private static final MediaTypeCache MEDIA_TYPE_CACHE = new MediaTypeCache(100);
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
/*   87 */   private static final boolean ENFORCE_ENCODING_IN_GET_WRITER = Boolean.parseBoolean(
/*   88 */     System.getProperty("org.apache.catalina.connector.Response.ENFORCE_ENCODING_IN_GET_WRITER", "true"));
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   98 */   protected SimpleDateFormat format = null;
/*      */   
/*      */   protected org.apache.coyote.Response coyoteResponse;
/*      */   
/*      */   protected OutputBuffer outputBuffer;
/*      */   
/*      */   protected CoyoteOutputStream outputStream;
/*      */   protected CoyoteWriter writer;
/*      */   
/*      */   public void setConnector(Connector connector)
/*      */   {
/*  109 */     if ("AJP/1.3".equals(connector.getProtocol()))
/*      */     {
/*  111 */       this.outputBuffer = new OutputBuffer(8184);
/*      */     } else {
/*  113 */       this.outputBuffer = new OutputBuffer();
/*      */     }
/*  115 */     this.outputStream = new CoyoteOutputStream(this.outputBuffer);
/*  116 */     this.writer = new CoyoteWriter(this.outputBuffer);
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
/*      */   public void setCoyoteResponse(org.apache.coyote.Response coyoteResponse)
/*      */   {
/*  131 */     this.coyoteResponse = coyoteResponse;
/*  132 */     this.outputBuffer.setResponse(coyoteResponse);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public org.apache.coyote.Response getCoyoteResponse()
/*      */   {
/*  139 */     return this.coyoteResponse;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Context getContext()
/*      */   {
/*  147 */     return this.request.getContext();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  172 */   protected boolean appCommitted = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  178 */   protected boolean included = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  184 */   private boolean isCharacterEncodingSet = false;
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
/*  215 */   private final AtomicInteger errorState = new AtomicInteger(0);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  221 */   protected boolean usingOutputStream = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  227 */   protected boolean usingWriter = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  233 */   protected final UEncoder urlEncoder = new UEncoder(UEncoder.SafeCharsSet.WITH_SLASH);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  239 */   protected final CharChunk redirectURLCC = new CharChunk();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  246 */   private final List<Cookie> cookies = new ArrayList();
/*      */   
/*  248 */   private HttpServletResponse applicationResponse = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void recycle()
/*      */   {
/*  259 */     this.cookies.clear();
/*  260 */     this.outputBuffer.recycle();
/*  261 */     this.usingOutputStream = false;
/*  262 */     this.usingWriter = false;
/*  263 */     this.appCommitted = false;
/*  264 */     this.included = false;
/*  265 */     this.errorState.set(0);
/*  266 */     this.isCharacterEncodingSet = false;
/*      */     
/*  268 */     this.applicationResponse = null;
/*  269 */     if ((Globals.IS_SECURITY_ENABLED) || (Connector.RECYCLE_FACADES)) {
/*  270 */       if (this.facade != null) {
/*  271 */         this.facade.clear();
/*  272 */         this.facade = null;
/*      */       }
/*  274 */       if (this.outputStream != null) {
/*  275 */         this.outputStream.clear();
/*  276 */         this.outputStream = null;
/*      */       }
/*  278 */       if (this.writer != null) {
/*  279 */         this.writer.clear();
/*  280 */         this.writer = null;
/*      */       }
/*      */     } else {
/*  283 */       this.writer.recycle();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public List<Cookie> getCookies()
/*      */   {
/*  290 */     return this.cookies;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getContentWritten()
/*      */   {
/*  302 */     return this.outputBuffer.getContentWritten();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getBytesWritten(boolean flush)
/*      */   {
/*  312 */     if (flush) {
/*      */       try {
/*  314 */         this.outputBuffer.flush();
/*      */       }
/*      */       catch (IOException localIOException) {}
/*      */     }
/*      */     
/*  319 */     return getCoyoteResponse().getBytesWritten(flush);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAppCommitted(boolean appCommitted)
/*      */   {
/*  328 */     this.appCommitted = appCommitted;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isAppCommitted()
/*      */   {
/*  338 */     return (this.appCommitted) || (isCommitted()) || (isSuspended()) || (
/*  339 */       (getContentLength() > 0) && 
/*  340 */       (getContentWritten() >= getContentLength()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  347 */   protected Request request = null;
/*      */   
/*      */ 
/*      */ 
/*      */   public Request getRequest()
/*      */   {
/*  353 */     return this.request;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRequest(Request request)
/*      */   {
/*  362 */     this.request = request;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  369 */   protected ResponseFacade facade = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpServletResponse getResponse()
/*      */   {
/*  377 */     if (this.facade == null) {
/*  378 */       this.facade = new ResponseFacade(this);
/*      */     }
/*  380 */     if (this.applicationResponse == null) {
/*  381 */       this.applicationResponse = this.facade;
/*      */     }
/*  383 */     return this.applicationResponse;
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
/*      */   public void setResponse(HttpServletResponse applicationResponse)
/*      */   {
/*  398 */     ServletResponse r = applicationResponse;
/*  399 */     while ((r instanceof HttpServletResponseWrapper)) {
/*  400 */       r = ((HttpServletResponseWrapper)r).getResponse();
/*      */     }
/*  402 */     if (r != this.facade) {
/*  403 */       throw new IllegalArgumentException(sm.getString("response.illegalWrap"));
/*      */     }
/*  405 */     this.applicationResponse = applicationResponse;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSuspended(boolean suspended)
/*      */   {
/*  415 */     this.outputBuffer.setSuspended(suspended);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSuspended()
/*      */   {
/*  425 */     return this.outputBuffer.isSuspended();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isClosed()
/*      */   {
/*  435 */     return this.outputBuffer.isClosed();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean setError()
/*      */   {
/*  445 */     boolean result = this.errorState.compareAndSet(0, 1);
/*  446 */     if (result) {
/*  447 */       Wrapper wrapper = getRequest().getWrapper();
/*  448 */       if (wrapper != null) {
/*  449 */         wrapper.incrementErrorCount();
/*      */       }
/*      */     }
/*  452 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isError()
/*      */   {
/*  462 */     return this.errorState.get() > 0;
/*      */   }
/*      */   
/*      */   public boolean isErrorReportRequired()
/*      */   {
/*  467 */     return this.errorState.get() == 1;
/*      */   }
/*      */   
/*      */   public boolean setErrorReported()
/*      */   {
/*  472 */     return this.errorState.compareAndSet(1, 2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void finishResponse()
/*      */     throws IOException
/*      */   {
/*  484 */     this.outputBuffer.close();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getContentLength()
/*      */   {
/*  492 */     return getCoyoteResponse().getContentLength();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getContentType()
/*      */   {
/*  502 */     return getCoyoteResponse().getContentType();
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
/*      */   public PrintWriter getReporter()
/*      */     throws IOException
/*      */   {
/*  519 */     if (this.outputBuffer.isNew()) {
/*  520 */       this.outputBuffer.checkConverter();
/*  521 */       if (this.writer == null) {
/*  522 */         this.writer = new CoyoteWriter(this.outputBuffer);
/*      */       }
/*  524 */       return this.writer;
/*      */     }
/*  526 */     return null;
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
/*      */   public void flushBuffer()
/*      */     throws IOException
/*      */   {
/*  541 */     this.outputBuffer.flush();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getBufferSize()
/*      */   {
/*  550 */     return this.outputBuffer.getBufferSize();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getCharacterEncoding()
/*      */   {
/*  559 */     String charset = getCoyoteResponse().getCharacterEncoding();
/*  560 */     if (charset != null) {
/*  561 */       return charset;
/*      */     }
/*      */     
/*  564 */     Context context = getContext();
/*  565 */     String result = null;
/*  566 */     if (context != null) {
/*  567 */       result = context.getResponseCharacterEncoding();
/*      */     }
/*      */     
/*  570 */     if (result == null) {
/*  571 */       result = Constants.DEFAULT_BODY_CHARSET.name();
/*      */     }
/*      */     
/*  574 */     return result;
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
/*      */   public ServletOutputStream getOutputStream()
/*      */     throws IOException
/*      */   {
/*  589 */     if (this.usingWriter)
/*      */     {
/*  591 */       throw new IllegalStateException(sm.getString("coyoteResponse.getOutputStream.ise"));
/*      */     }
/*      */     
/*  594 */     this.usingOutputStream = true;
/*  595 */     if (this.outputStream == null) {
/*  596 */       this.outputStream = new CoyoteOutputStream(this.outputBuffer);
/*      */     }
/*  598 */     return this.outputStream;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/*  608 */     return getCoyoteResponse().getLocale();
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
/*      */   public PrintWriter getWriter()
/*      */     throws IOException
/*      */   {
/*  623 */     if (this.usingOutputStream)
/*      */     {
/*  625 */       throw new IllegalStateException(sm.getString("coyoteResponse.getWriter.ise"));
/*      */     }
/*      */     
/*  628 */     if (ENFORCE_ENCODING_IN_GET_WRITER)
/*      */     {
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
/*  641 */       setCharacterEncoding(getCharacterEncoding());
/*      */     }
/*      */     
/*  644 */     this.usingWriter = true;
/*  645 */     this.outputBuffer.checkConverter();
/*  646 */     if (this.writer == null) {
/*  647 */       this.writer = new CoyoteWriter(this.outputBuffer);
/*      */     }
/*  649 */     return this.writer;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isCommitted()
/*      */   {
/*  660 */     return getCoyoteResponse().isCommitted();
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
/*      */   public void reset()
/*      */   {
/*  673 */     if (this.included) {
/*  674 */       return;
/*      */     }
/*      */     
/*  677 */     getCoyoteResponse().reset();
/*  678 */     this.outputBuffer.reset();
/*  679 */     this.usingOutputStream = false;
/*  680 */     this.usingWriter = false;
/*  681 */     this.isCharacterEncodingSet = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void resetBuffer()
/*      */   {
/*  693 */     resetBuffer(false);
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
/*      */   public void resetBuffer(boolean resetWriterStreamFlags)
/*      */   {
/*  710 */     if (isCommitted())
/*      */     {
/*  712 */       throw new IllegalStateException(sm.getString("coyoteResponse.resetBuffer.ise"));
/*      */     }
/*      */     
/*  715 */     this.outputBuffer.reset(resetWriterStreamFlags);
/*      */     
/*  717 */     if (resetWriterStreamFlags) {
/*  718 */       this.usingOutputStream = false;
/*  719 */       this.usingWriter = false;
/*  720 */       this.isCharacterEncodingSet = false;
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
/*      */ 
/*      */ 
/*      */   public void setBufferSize(int size)
/*      */   {
/*  737 */     if ((isCommitted()) || (!this.outputBuffer.isNew()))
/*      */     {
/*  739 */       throw new IllegalStateException(sm.getString("coyoteResponse.setBufferSize.ise"));
/*      */     }
/*      */     
/*  742 */     this.outputBuffer.setBufferSize(size);
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
/*      */   public void setContentLength(int length)
/*      */   {
/*  755 */     setContentLengthLong(length);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setContentLengthLong(long length)
/*      */   {
/*  761 */     if (isCommitted()) {
/*  762 */       return;
/*      */     }
/*      */     
/*      */ 
/*  766 */     if (this.included) {
/*  767 */       return;
/*      */     }
/*      */     
/*  770 */     getCoyoteResponse().setContentLength(length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setContentType(String type)
/*      */   {
/*  782 */     if (isCommitted()) {
/*  783 */       return;
/*      */     }
/*      */     
/*      */ 
/*  787 */     if (this.included) {
/*  788 */       return;
/*      */     }
/*      */     
/*  791 */     if (type == null) {
/*  792 */       getCoyoteResponse().setContentType(null);
/*  793 */       return;
/*      */     }
/*      */     
/*  796 */     String[] m = MEDIA_TYPE_CACHE.parse(type);
/*  797 */     if (m == null)
/*      */     {
/*      */ 
/*  800 */       getCoyoteResponse().setContentTypeNoCharset(type);
/*  801 */       return;
/*      */     }
/*      */     
/*  804 */     getCoyoteResponse().setContentTypeNoCharset(m[0]);
/*      */     
/*  806 */     if (m[1] != null)
/*      */     {
/*  808 */       if (!this.usingWriter) {
/*      */         try {
/*  810 */           getCoyoteResponse().setCharacterEncoding(m[1]);
/*      */         } catch (IllegalArgumentException e) {
/*  812 */           log.warn(sm.getString("coyoteResponse.encoding.invalid", new Object[] { m[1] }), e);
/*      */         }
/*      */         
/*  815 */         this.isCharacterEncodingSet = true;
/*      */       }
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
/*      */ 
/*      */   public void setCharacterEncoding(String characterEncoding)
/*      */   {
/*  832 */     if (isCommitted()) {
/*  833 */       return;
/*      */     }
/*      */     
/*      */ 
/*  837 */     if (this.included) {
/*  838 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  843 */     if (this.usingWriter) {
/*  844 */       return;
/*      */     }
/*      */     try
/*      */     {
/*  848 */       getCoyoteResponse().setCharacterEncoding(characterEncoding);
/*      */     } catch (IllegalArgumentException e) {
/*  850 */       log.warn(sm.getString("coyoteResponse.encoding.invalid", new Object[] { characterEncoding }), e);
/*  851 */       return;
/*      */     }
/*  853 */     this.isCharacterEncodingSet = true;
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
/*      */   public void setLocale(Locale locale)
/*      */   {
/*  866 */     if (isCommitted()) {
/*  867 */       return;
/*      */     }
/*      */     
/*      */ 
/*  871 */     if (this.included) {
/*  872 */       return;
/*      */     }
/*      */     
/*  875 */     getCoyoteResponse().setLocale(locale);
/*      */     
/*      */ 
/*      */ 
/*  879 */     if (this.usingWriter) {
/*  880 */       return;
/*      */     }
/*      */     
/*  883 */     if (this.isCharacterEncodingSet) {
/*  884 */       return;
/*      */     }
/*      */     
/*  887 */     String charset = getContext().getCharset(locale);
/*  888 */     if (charset != null) {
/*      */       try {
/*  890 */         getCoyoteResponse().setCharacterEncoding(charset);
/*      */       } catch (IllegalArgumentException e) {
/*  892 */         log.warn(sm.getString("coyoteResponse.encoding.invalid", new Object[] { charset }), e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getHeader(String name)
/*      */   {
/*  903 */     return getCoyoteResponse().getMimeHeaders().getHeader(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Collection<String> getHeaderNames()
/*      */   {
/*  910 */     MimeHeaders headers = getCoyoteResponse().getMimeHeaders();
/*  911 */     int n = headers.size();
/*  912 */     List<String> result = new ArrayList(n);
/*  913 */     for (int i = 0; i < n; i++) {
/*  914 */       result.add(headers.getName(i).toString());
/*      */     }
/*  916 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Collection<String> getHeaders(String name)
/*      */   {
/*  925 */     Enumeration<String> enumeration = getCoyoteResponse().getMimeHeaders().values(name);
/*  926 */     Vector<String> result = new Vector();
/*  927 */     while (enumeration.hasMoreElements()) {
/*  928 */       result.addElement(enumeration.nextElement());
/*      */     }
/*  930 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getMessage()
/*      */   {
/*  939 */     return getCoyoteResponse().getMessage();
/*      */   }
/*      */   
/*      */ 
/*      */   public int getStatus()
/*      */   {
/*  945 */     return getCoyoteResponse().getStatus();
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
/*      */   public void addCookie(Cookie cookie)
/*      */   {
/*  961 */     if ((this.included) || (isCommitted())) {
/*  962 */       return;
/*      */     }
/*      */     
/*  965 */     this.cookies.add(cookie);
/*      */     
/*  967 */     String header = generateCookieString(cookie);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  972 */     addHeader("Set-Cookie", header, getContext().getCookieProcessor().getCharset());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addSessionCookieInternal(Cookie cookie)
/*      */   {
/*  982 */     if (isCommitted()) {
/*  983 */       return;
/*      */     }
/*      */     
/*  986 */     String name = cookie.getName();
/*  987 */     String headername = "Set-Cookie";
/*  988 */     String startsWith = name + "=";
/*  989 */     String header = generateCookieString(cookie);
/*  990 */     boolean set = false;
/*  991 */     MimeHeaders headers = getCoyoteResponse().getMimeHeaders();
/*  992 */     int n = headers.size();
/*  993 */     for (int i = 0; i < n; i++) {
/*  994 */       if ((headers.getName(i).toString().equals("Set-Cookie")) && 
/*  995 */         (headers.getValue(i).toString().startsWith(startsWith))) {
/*  996 */         headers.getValue(i).setString(header);
/*  997 */         set = true;
/*      */       }
/*      */     }
/*      */     
/* 1001 */     if (!set) {
/* 1002 */       addHeader("Set-Cookie", header);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String generateCookieString(final Cookie cookie)
/*      */   {
/* 1011 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 1012 */       (String)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public String run() {
/* 1015 */           return Response.this.getContext().getCookieProcessor().generateHeader(cookie);
/*      */         }
/*      */       });
/*      */     }
/* 1019 */     return getContext().getCookieProcessor().generateHeader(cookie);
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
/*      */   public void addDateHeader(String name, long value)
/*      */   {
/* 1033 */     if ((name == null) || (name.length() == 0)) {
/* 1034 */       return;
/*      */     }
/*      */     
/* 1037 */     if (isCommitted()) {
/* 1038 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1042 */     if (this.included) {
/* 1043 */       return;
/*      */     }
/*      */     
/* 1046 */     if (this.format == null) {
/* 1047 */       this.format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
/*      */       
/* 1049 */       this.format.setTimeZone(TimeZone.getTimeZone("GMT"));
/*      */     }
/*      */     
/* 1052 */     addHeader(name, FastHttpDateFormat.formatDate(value, this.format));
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
/*      */   public void addHeader(String name, String value)
/*      */   {
/* 1065 */     addHeader(name, value, null);
/*      */   }
/*      */   
/*      */ 
/*      */   private void addHeader(String name, String value, Charset charset)
/*      */   {
/* 1071 */     if ((name == null) || (name.length() == 0) || (value == null)) {
/* 1072 */       return;
/*      */     }
/*      */     
/* 1075 */     if (isCommitted()) {
/* 1076 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1080 */     if (this.included) {
/* 1081 */       return;
/*      */     }
/*      */     
/* 1084 */     char cc = name.charAt(0);
/* 1085 */     if (((cc == 'C') || (cc == 'c')) && 
/* 1086 */       (checkSpecialHeader(name, value))) {
/* 1087 */       return;
/*      */     }
/*      */     
/* 1090 */     getCoyoteResponse().addHeader(name, value, charset);
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
/*      */   private boolean checkSpecialHeader(String name, String value)
/*      */   {
/* 1104 */     if (name.equalsIgnoreCase("Content-Type")) {
/* 1105 */       setContentType(value);
/* 1106 */       return true;
/*      */     }
/* 1108 */     return false;
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
/*      */   public void addIntHeader(String name, int value)
/*      */   {
/* 1121 */     if ((name == null) || (name.length() == 0)) {
/* 1122 */       return;
/*      */     }
/*      */     
/* 1125 */     if (isCommitted()) {
/* 1126 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1130 */     if (this.included) {
/* 1131 */       return;
/*      */     }
/*      */     
/* 1134 */     addHeader(name, "" + value);
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
/*      */   public boolean containsHeader(String name)
/*      */   {
/* 1149 */     char cc = name.charAt(0);
/* 1150 */     if ((cc == 'C') || (cc == 'c')) {
/* 1151 */       if (name.equalsIgnoreCase("Content-Type"))
/*      */       {
/* 1153 */         return getCoyoteResponse().getContentType() != null;
/*      */       }
/* 1155 */       if (name.equalsIgnoreCase("Content-Length"))
/*      */       {
/* 1157 */         return getCoyoteResponse().getContentLengthLong() != -1L;
/*      */       }
/*      */     }
/*      */     
/* 1161 */     return getCoyoteResponse().containsHeader(name);
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
/*      */   public String encodeRedirectURL(String url)
/*      */   {
/* 1175 */     if (isEncodeable(toAbsolute(url))) {
/* 1176 */       return toEncoded(url, this.request.getSessionInternal().getIdInternal());
/*      */     }
/* 1178 */     return url;
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
/*      */   @Deprecated
/*      */   public String encodeRedirectUrl(String url)
/*      */   {
/* 1197 */     return encodeRedirectURL(url);
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
/*      */   public String encodeURL(String url)
/*      */   {
/*      */     try
/*      */     {
/* 1213 */       absolute = toAbsolute(url);
/*      */     } catch (IllegalArgumentException iae) {
/*      */       String absolute;
/* 1216 */       return url;
/*      */     }
/*      */     String absolute;
/* 1219 */     if (isEncodeable(absolute))
/*      */     {
/* 1221 */       if (url.equalsIgnoreCase("")) {
/* 1222 */         url = absolute;
/* 1223 */       } else if ((url.equals(absolute)) && (!hasPath(url))) {
/* 1224 */         url = url + '/';
/*      */       }
/* 1226 */       return toEncoded(url, this.request.getSessionInternal().getIdInternal());
/*      */     }
/* 1228 */     return url;
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
/*      */   @Deprecated
/*      */   public String encodeUrl(String url)
/*      */   {
/* 1247 */     return encodeURL(url);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void sendAcknowledgement()
/*      */     throws IOException
/*      */   {
/* 1259 */     if (isCommitted()) {
/* 1260 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1264 */     if (this.included) {
/* 1265 */       return;
/*      */     }
/*      */     
/* 1268 */     getCoyoteResponse().action(ActionCode.ACK, null);
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
/*      */   public void sendError(int status)
/*      */     throws IOException
/*      */   {
/* 1284 */     sendError(status, null);
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
/*      */   public void sendError(int status, String message)
/*      */     throws IOException
/*      */   {
/* 1301 */     if (isCommitted())
/*      */     {
/* 1303 */       throw new IllegalStateException(sm.getString("coyoteResponse.sendError.ise"));
/*      */     }
/*      */     
/*      */ 
/* 1307 */     if (this.included) {
/* 1308 */       return;
/*      */     }
/*      */     
/* 1311 */     setError();
/*      */     
/* 1313 */     getCoyoteResponse().setStatus(status);
/* 1314 */     getCoyoteResponse().setMessage(message);
/*      */     
/*      */ 
/* 1317 */     resetBuffer();
/*      */     
/*      */ 
/* 1320 */     setSuspended(true);
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
/*      */   public void sendRedirect(String location)
/*      */     throws IOException
/*      */   {
/* 1335 */     sendRedirect(location, 302);
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
/*      */   public void sendRedirect(String location, int status)
/*      */     throws IOException
/*      */   {
/* 1349 */     if (isCommitted()) {
/* 1350 */       throw new IllegalStateException(sm.getString("coyoteResponse.sendRedirect.ise"));
/*      */     }
/*      */     
/*      */ 
/* 1354 */     if (this.included) {
/* 1355 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1359 */     resetBuffer(true);
/*      */     
/*      */     try
/*      */     {
/*      */       String locationUri;
/*      */       String locationUri;
/* 1365 */       if ((getRequest().getCoyoteRequest().getSupportsRelativeRedirects()) && 
/* 1366 */         (getContext().getUseRelativeRedirects())) {
/* 1367 */         locationUri = location;
/*      */       } else {
/* 1369 */         locationUri = toAbsolute(location);
/*      */       }
/* 1371 */       setStatus(status);
/* 1372 */       setHeader("Location", locationUri);
/* 1373 */       if (getContext().getSendRedirectBody()) {
/* 1374 */         PrintWriter writer = getWriter();
/* 1375 */         writer.print(sm.getString("coyoteResponse.sendRedirect.note", new Object[] {
/* 1376 */           Escape.htmlElementContent(locationUri) }));
/* 1377 */         flushBuffer();
/*      */       }
/*      */     } catch (IllegalArgumentException e) {
/* 1380 */       log.warn(sm.getString("response.sendRedirectFail", new Object[] { location }), e);
/* 1381 */       setStatus(404);
/*      */     }
/*      */     
/*      */ 
/* 1385 */     setSuspended(true);
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
/*      */   public void setDateHeader(String name, long value)
/*      */   {
/* 1398 */     if ((name == null) || (name.length() == 0)) {
/* 1399 */       return;
/*      */     }
/*      */     
/* 1402 */     if (isCommitted()) {
/* 1403 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1407 */     if (this.included) {
/* 1408 */       return;
/*      */     }
/*      */     
/* 1411 */     if (this.format == null) {
/* 1412 */       this.format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
/*      */       
/* 1414 */       this.format.setTimeZone(TimeZone.getTimeZone("GMT"));
/*      */     }
/*      */     
/* 1417 */     setHeader(name, FastHttpDateFormat.formatDate(value, this.format));
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
/*      */   public void setHeader(String name, String value)
/*      */   {
/* 1430 */     if ((name == null) || (name.length() == 0) || (value == null)) {
/* 1431 */       return;
/*      */     }
/*      */     
/* 1434 */     if (isCommitted()) {
/* 1435 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1439 */     if (this.included) {
/* 1440 */       return;
/*      */     }
/*      */     
/* 1443 */     char cc = name.charAt(0);
/* 1444 */     if (((cc == 'C') || (cc == 'c')) && 
/* 1445 */       (checkSpecialHeader(name, value))) {
/* 1446 */       return;
/*      */     }
/*      */     
/* 1449 */     getCoyoteResponse().setHeader(name, value);
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
/*      */   public void setIntHeader(String name, int value)
/*      */   {
/* 1462 */     if ((name == null) || (name.length() == 0)) {
/* 1463 */       return;
/*      */     }
/*      */     
/* 1466 */     if (isCommitted()) {
/* 1467 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1471 */     if (this.included) {
/* 1472 */       return;
/*      */     }
/*      */     
/* 1475 */     setHeader(name, "" + value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setStatus(int status)
/*      */   {
/* 1487 */     setStatus(status, null);
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
/*      */   @Deprecated
/*      */   public void setStatus(int status, String message)
/*      */   {
/* 1505 */     if (isCommitted()) {
/* 1506 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1510 */     if (this.included) {
/* 1511 */       return;
/*      */     }
/*      */     
/* 1514 */     getCoyoteResponse().setStatus(status);
/* 1515 */     getCoyoteResponse().setMessage(message);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isEncodeable(final String location)
/*      */   {
/* 1539 */     if (location == null) {
/* 1540 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1544 */     if (location.startsWith("#")) {
/* 1545 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1549 */     final Request hreq = this.request;
/* 1550 */     final Session session = hreq.getSessionInternal(false);
/* 1551 */     if (session == null) {
/* 1552 */       return false;
/*      */     }
/* 1554 */     if (hreq.isRequestedSessionIdFromCookie()) {
/* 1555 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1560 */     if (!hreq.getServletContext().getEffectiveSessionTrackingModes().contains(SessionTrackingMode.URL)) {
/* 1561 */       return false;
/*      */     }
/*      */     
/* 1564 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 1565 */       
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1572 */         ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public Boolean run()
/*      */           {
/* 1570 */             return Boolean.valueOf(Response.this.doIsEncodeable(hreq, session, location));
/*      */           }
/*      */         })).booleanValue();
/*      */     }
/* 1574 */     return doIsEncodeable(hreq, session, location);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean doIsEncodeable(Request hreq, Session session, String location)
/*      */   {
/* 1581 */     URL url = null;
/*      */     try {
/* 1583 */       url = new URL(location);
/*      */     } catch (MalformedURLException e) {
/* 1585 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1589 */     if (!hreq.getScheme().equalsIgnoreCase(url.getProtocol())) {
/* 1590 */       return false;
/*      */     }
/* 1592 */     if (!hreq.getServerName().equalsIgnoreCase(url.getHost())) {
/* 1593 */       return false;
/*      */     }
/* 1595 */     int serverPort = hreq.getServerPort();
/* 1596 */     if (serverPort == -1) {
/* 1597 */       if ("https".equals(hreq.getScheme())) {
/* 1598 */         serverPort = 443;
/*      */       } else {
/* 1600 */         serverPort = 80;
/*      */       }
/*      */     }
/* 1603 */     int urlPort = url.getPort();
/* 1604 */     if (urlPort == -1) {
/* 1605 */       if ("https".equals(url.getProtocol())) {
/* 1606 */         urlPort = 443;
/*      */       } else {
/* 1608 */         urlPort = 80;
/*      */       }
/*      */     }
/* 1611 */     if (serverPort != urlPort) {
/* 1612 */       return false;
/*      */     }
/*      */     
/* 1615 */     String contextPath = getContext().getPath();
/* 1616 */     if (contextPath != null) {
/* 1617 */       String file = url.getFile();
/* 1618 */       if (!file.startsWith(contextPath)) {
/* 1619 */         return false;
/*      */       }
/*      */       
/*      */ 
/* 1623 */       String tok = ";" + SessionConfig.getSessionUriParamName(this.request.getContext()) + "=" + session.getIdInternal();
/* 1624 */       if (file.indexOf(tok, contextPath.length()) >= 0) {
/* 1625 */         return false;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1630 */     return true;
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
/*      */   protected String toAbsolute(String location)
/*      */   {
/* 1648 */     if (location == null) {
/* 1649 */       return location;
/*      */     }
/*      */     
/* 1652 */     boolean leadingSlash = location.startsWith("/");
/*      */     
/* 1654 */     if (location.startsWith("//"))
/*      */     {
/* 1656 */       this.redirectURLCC.recycle();
/*      */       
/* 1658 */       String scheme = this.request.getScheme();
/*      */       try {
/* 1660 */         this.redirectURLCC.append(scheme, 0, scheme.length());
/* 1661 */         this.redirectURLCC.append(':');
/* 1662 */         this.redirectURLCC.append(location, 0, location.length());
/* 1663 */         return this.redirectURLCC.toString();
/*      */       } catch (IOException e) {
/* 1665 */         IllegalArgumentException iae = new IllegalArgumentException(location);
/*      */         
/* 1667 */         iae.initCause(e);
/* 1668 */         throw iae;
/*      */       }
/*      */     }
/* 1671 */     if ((leadingSlash) || (!UriUtil.hasScheme(location)))
/*      */     {
/* 1673 */       this.redirectURLCC.recycle();
/*      */       
/* 1675 */       String scheme = this.request.getScheme();
/* 1676 */       String name = this.request.getServerName();
/* 1677 */       int port = this.request.getServerPort();
/*      */       try
/*      */       {
/* 1680 */         this.redirectURLCC.append(scheme, 0, scheme.length());
/* 1681 */         this.redirectURLCC.append("://", 0, 3);
/* 1682 */         this.redirectURLCC.append(name, 0, name.length());
/* 1683 */         if (((scheme.equals("http")) && (port != 80)) || (
/* 1684 */           (scheme.equals("https")) && (port != 443))) {
/* 1685 */           this.redirectURLCC.append(':');
/* 1686 */           String portS = port + "";
/* 1687 */           this.redirectURLCC.append(portS, 0, portS.length());
/*      */         }
/* 1689 */         if (!leadingSlash) {
/* 1690 */           String relativePath = this.request.getDecodedRequestURI();
/* 1691 */           int pos = relativePath.lastIndexOf('/');
/* 1692 */           CharChunk encodedURI = null;
/* 1693 */           final String frelativePath = relativePath;
/* 1694 */           final int fend = pos;
/* 1695 */           if (SecurityUtil.isPackageProtectionEnabled()) {
/*      */             try {
/* 1697 */               encodedURI = (CharChunk)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */               {
/*      */                 public CharChunk run() throws IOException
/*      */                 {
/* 1701 */                   return Response.this.urlEncoder.encodeURL(frelativePath, 0, fend);
/*      */                 }
/*      */               });
/*      */             } catch (PrivilegedActionException pae) {
/* 1705 */               IllegalArgumentException iae = new IllegalArgumentException(location);
/*      */               
/* 1707 */               iae.initCause(pae.getException());
/* 1708 */               throw iae;
/*      */             }
/*      */           } else {
/* 1711 */             encodedURI = this.urlEncoder.encodeURL(relativePath, 0, pos);
/*      */           }
/* 1713 */           this.redirectURLCC.append(encodedURI);
/* 1714 */           encodedURI.recycle();
/* 1715 */           this.redirectURLCC.append('/');
/*      */         }
/* 1717 */         this.redirectURLCC.append(location, 0, location.length());
/*      */         
/* 1719 */         normalize(this.redirectURLCC);
/*      */       } catch (IOException e) {
/* 1721 */         IllegalArgumentException iae = new IllegalArgumentException(location);
/*      */         
/* 1723 */         iae.initCause(e);
/* 1724 */         throw iae;
/*      */       }
/*      */       
/* 1727 */       return this.redirectURLCC.toString();
/*      */     }
/*      */     
/*      */ 
/* 1731 */     return location;
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
/*      */   private void normalize(CharChunk cc)
/*      */   {
/* 1746 */     int truncate = cc.indexOf('?');
/* 1747 */     if (truncate == -1) {
/* 1748 */       truncate = cc.indexOf('#');
/*      */     }
/* 1750 */     char[] truncateCC = null;
/* 1751 */     if (truncate > -1) {
/* 1752 */       truncateCC = Arrays.copyOfRange(cc.getBuffer(), cc
/* 1753 */         .getStart() + truncate, cc.getEnd());
/* 1754 */       cc.setEnd(cc.getStart() + truncate);
/*      */     }
/*      */     
/* 1757 */     if ((cc.endsWith("/.")) || (cc.endsWith("/.."))) {
/*      */       try {
/* 1759 */         cc.append('/');
/*      */       } catch (IOException e) {
/* 1761 */         throw new IllegalArgumentException(cc.toString(), e);
/*      */       }
/*      */     }
/*      */     
/* 1765 */     char[] c = cc.getChars();
/* 1766 */     int start = cc.getStart();
/* 1767 */     int end = cc.getEnd();
/* 1768 */     int index = 0;
/* 1769 */     int startIndex = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1774 */     for (int i = 0; i < 3; i++) {
/* 1775 */       startIndex = cc.indexOf('/', startIndex + 1);
/*      */     }
/*      */     
/*      */ 
/* 1779 */     index = startIndex;
/*      */     for (;;) {
/* 1781 */       index = cc.indexOf("/./", 0, 3, index);
/* 1782 */       if (index < 0) {
/*      */         break;
/*      */       }
/* 1785 */       copyChars(c, start + index, start + index + 2, end - start - index - 2);
/*      */       
/* 1787 */       end -= 2;
/* 1788 */       cc.setEnd(end);
/*      */     }
/*      */     
/*      */ 
/* 1792 */     index = startIndex;
/*      */     for (;;)
/*      */     {
/* 1795 */       index = cc.indexOf("/../", 0, 4, index);
/* 1796 */       if (index < 0) {
/*      */         break;
/*      */       }
/*      */       
/* 1800 */       if (index == startIndex) {
/* 1801 */         throw new IllegalArgumentException();
/*      */       }
/* 1803 */       int index2 = -1;
/* 1804 */       for (int pos = start + index - 1; (pos >= 0) && (index2 < 0); pos--) {
/* 1805 */         if (c[pos] == '/') {
/* 1806 */           index2 = pos;
/*      */         }
/*      */       }
/* 1809 */       copyChars(c, start + index2, start + index + 3, end - start - index - 3);
/*      */       
/* 1811 */       end = end + index2 - index - 3;
/* 1812 */       cc.setEnd(end);
/* 1813 */       index = index2;
/*      */     }
/*      */     
/*      */ 
/* 1817 */     if (truncateCC != null) {
/*      */       try {
/* 1819 */         cc.append(truncateCC, 0, truncateCC.length);
/*      */       } catch (IOException ioe) {
/* 1821 */         throw new IllegalArgumentException(ioe);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void copyChars(char[] c, int dest, int src, int len) {
/* 1827 */     for (int pos = 0; pos < len; pos++) {
/* 1828 */       c[(pos + dest)] = c[(pos + src)];
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean hasPath(String uri)
/*      */   {
/* 1840 */     int pos = uri.indexOf("://");
/* 1841 */     if (pos < 0) {
/* 1842 */       return false;
/*      */     }
/* 1844 */     pos = uri.indexOf('/', pos + 3);
/* 1845 */     if (pos < 0) {
/* 1846 */       return false;
/*      */     }
/* 1848 */     return true;
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
/*      */   protected String toEncoded(String url, String sessionId)
/*      */   {
/* 1861 */     if ((url == null) || (sessionId == null)) {
/* 1862 */       return url;
/*      */     }
/*      */     
/* 1865 */     String path = url;
/* 1866 */     String query = "";
/* 1867 */     String anchor = "";
/* 1868 */     int question = url.indexOf('?');
/* 1869 */     if (question >= 0) {
/* 1870 */       path = url.substring(0, question);
/* 1871 */       query = url.substring(question);
/*      */     }
/* 1873 */     int pound = path.indexOf('#');
/* 1874 */     if (pound >= 0) {
/* 1875 */       anchor = path.substring(pound);
/* 1876 */       path = path.substring(0, pound);
/*      */     }
/* 1878 */     StringBuilder sb = new StringBuilder(path);
/* 1879 */     if (sb.length() > 0) {
/* 1880 */       sb.append(";");
/* 1881 */       sb.append(SessionConfig.getSessionUriParamName(this.request
/* 1882 */         .getContext()));
/* 1883 */       sb.append("=");
/* 1884 */       sb.append(sessionId);
/*      */     }
/* 1886 */     sb.append(anchor);
/* 1887 */     sb.append(query);
/* 1888 */     return sb.toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\connector\Response.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */