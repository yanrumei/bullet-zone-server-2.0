/*      */ package org.apache.catalina.connector;
/*      */ 
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.StringReader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.security.Principal;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.TimeZone;
/*      */ import java.util.TreeMap;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import javax.naming.NamingException;
/*      */ import javax.security.auth.Subject;
/*      */ import javax.servlet.AsyncContext;
/*      */ import javax.servlet.DispatcherType;
/*      */ import javax.servlet.FilterChain;
/*      */ import javax.servlet.MultipartConfigElement;
/*      */ import javax.servlet.RequestDispatcher;
/*      */ import javax.servlet.ServletContext;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.ServletInputStream;
/*      */ import javax.servlet.ServletRequest;
/*      */ import javax.servlet.ServletRequestAttributeEvent;
/*      */ import javax.servlet.ServletRequestAttributeListener;
/*      */ import javax.servlet.ServletResponse;
/*      */ import javax.servlet.SessionTrackingMode;
/*      */ import javax.servlet.http.Cookie;
/*      */ import javax.servlet.http.HttpServletRequestWrapper;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import javax.servlet.http.HttpSession;
/*      */ import javax.servlet.http.HttpUpgradeHandler;
/*      */ import javax.servlet.http.Part;
/*      */ import org.apache.catalina.Authenticator;
/*      */ import org.apache.catalina.Container;
/*      */ import org.apache.catalina.Context;
/*      */ import org.apache.catalina.Globals;
/*      */ import org.apache.catalina.Host;
/*      */ import org.apache.catalina.Manager;
/*      */ import org.apache.catalina.Pipeline;
/*      */ import org.apache.catalina.Realm;
/*      */ import org.apache.catalina.Session;
/*      */ import org.apache.catalina.TomcatPrincipal;
/*      */ import org.apache.catalina.Wrapper;
/*      */ import org.apache.catalina.core.ApplicationFilterChain;
/*      */ import org.apache.catalina.core.ApplicationMapping;
/*      */ import org.apache.catalina.core.ApplicationPart;
/*      */ import org.apache.catalina.core.ApplicationPushBuilder;
/*      */ import org.apache.catalina.core.ApplicationSessionCookieConfig;
/*      */ import org.apache.catalina.core.AsyncContextImpl;
/*      */ import org.apache.catalina.mapper.MappingData;
/*      */ import org.apache.catalina.servlet4preview.http.PushBuilder;
/*      */ import org.apache.catalina.servlet4preview.http.ServletMapping;
/*      */ import org.apache.catalina.util.ParameterMap;
/*      */ import org.apache.catalina.util.TLSUtil;
/*      */ import org.apache.catalina.util.URLEncoder;
/*      */ import org.apache.coyote.ActionCode;
/*      */ import org.apache.coyote.Constants;
/*      */ import org.apache.coyote.ProtocolHandler;
/*      */ import org.apache.coyote.UpgradeToken;
/*      */ import org.apache.coyote.http11.upgrade.InternalHttpUpgradeHandler;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.InstanceManager;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.buf.B2CConverter;
/*      */ import org.apache.tomcat.util.buf.ByteChunk;
/*      */ import org.apache.tomcat.util.buf.MessageBytes;
/*      */ import org.apache.tomcat.util.buf.StringUtils;
/*      */ import org.apache.tomcat.util.buf.UDecoder;
/*      */ import org.apache.tomcat.util.http.CookieProcessor;
/*      */ import org.apache.tomcat.util.http.FastHttpDateFormat;
/*      */ import org.apache.tomcat.util.http.MimeHeaders;
/*      */ import org.apache.tomcat.util.http.Parameters;
/*      */ import org.apache.tomcat.util.http.Parameters.FailReason;
/*      */ import org.apache.tomcat.util.http.RequestUtil;
/*      */ import org.apache.tomcat.util.http.ServerCookie;
/*      */ import org.apache.tomcat.util.http.ServerCookies;
/*      */ import org.apache.tomcat.util.http.fileupload.FileItem;
/*      */ import org.apache.tomcat.util.http.fileupload.FileUploadBase.InvalidContentTypeException;
/*      */ import org.apache.tomcat.util.http.fileupload.FileUploadBase.SizeException;
/*      */ import org.apache.tomcat.util.http.fileupload.FileUploadException;
/*      */ import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
/*      */ import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
/*      */ import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
/*      */ import org.apache.tomcat.util.http.parser.AcceptLanguage;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ import org.ietf.jgss.GSSCredential;
/*      */ import org.ietf.jgss.GSSException;
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
/*      */ public class Request
/*      */   implements org.apache.catalina.servlet4preview.http.HttpServletRequest
/*      */ {
/*  129 */   private static final Log log = LogFactory.getLog(Request.class);
/*      */   
/*      */   protected org.apache.coyote.Request coyoteRequest;
/*      */   
/*      */   public Request()
/*      */   {
/*  135 */     this.formats = new SimpleDateFormat[formatsTemplate.length];
/*  136 */     for (int i = 0; i < this.formats.length; i++) {
/*  137 */       this.formats[i] = ((SimpleDateFormat)formatsTemplate[i].clone());
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
/*      */ 
/*      */ 
/*      */   public void setCoyoteRequest(org.apache.coyote.Request coyoteRequest)
/*      */   {
/*  156 */     this.coyoteRequest = coyoteRequest;
/*  157 */     this.inputBuffer.setRequest(coyoteRequest);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public org.apache.coyote.Request getCoyoteRequest()
/*      */   {
/*  166 */     return this.coyoteRequest;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  173 */   protected static final TimeZone GMT_ZONE = TimeZone.getTimeZone("GMT");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  179 */   protected static final StringManager sm = StringManager.getManager(Request.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  185 */   protected Cookie[] cookies = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final SimpleDateFormat[] formats;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  196 */   private static final SimpleDateFormat[] formatsTemplate = { new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US), new SimpleDateFormat("EEEEEE, dd-MMM-yy HH:mm:ss zzz", Locale.US), new SimpleDateFormat("EEE MMMM d HH:mm:ss yyyy", Locale.US) };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  206 */   protected static final Locale defaultLocale = Locale.getDefault();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  212 */   private final Map<String, Object> attributes = new ConcurrentHashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  220 */   protected boolean sslAttributesParsed = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  226 */   protected final ArrayList<Locale> locales = new ArrayList();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  233 */   private final transient HashMap<String, Object> notes = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  239 */   protected String authType = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  245 */   protected DispatcherType internalDispatcherType = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  251 */   protected final InputBuffer inputBuffer = new InputBuffer();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  257 */   protected CoyoteInputStream inputStream = new CoyoteInputStream(this.inputBuffer);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  264 */   protected CoyoteReader reader = new CoyoteReader(this.inputBuffer);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  270 */   protected boolean usingInputStream = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  276 */   protected boolean usingReader = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  282 */   protected Principal userPrincipal = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  288 */   protected boolean parametersParsed = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  295 */   protected boolean cookiesParsed = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  302 */   protected boolean cookiesConverted = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  308 */   protected boolean secure = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  314 */   protected transient Subject subject = null;
/*      */   
/*      */ 
/*      */ 
/*      */   protected static final int CACHED_POST_LEN = 8192;
/*      */   
/*      */ 
/*  321 */   protected byte[] postData = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  327 */   protected ParameterMap<String, String[]> parameterMap = new ParameterMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  333 */   protected Collection<Part> parts = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  339 */   protected Exception partsParseException = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  345 */   protected Session session = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  351 */   protected Object requestDispatcherPath = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  357 */   protected boolean requestedSessionCookie = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  363 */   protected String requestedSessionId = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  369 */   protected boolean requestedSessionURL = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  375 */   protected boolean requestedSessionSSL = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  381 */   protected boolean localesParsed = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  387 */   protected int localPort = -1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  392 */   protected String remoteAddr = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  398 */   protected String remoteHost = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  404 */   protected int remotePort = -1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  409 */   protected String localAddr = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  415 */   protected String localName = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  420 */   private volatile AsyncContextImpl asyncContext = null;
/*      */   
/*  422 */   protected Boolean asyncSupported = null;
/*      */   
/*  424 */   private javax.servlet.http.HttpServletRequest applicationRequest = null;
/*      */   
/*      */   protected Connector connector;
/*      */   
/*      */   protected void addPathParameter(String name, String value)
/*      */   {
/*  430 */     this.coyoteRequest.addPathParameter(name, value);
/*      */   }
/*      */   
/*      */   protected String getPathParameter(String name) {
/*  434 */     return this.coyoteRequest.getPathParameter(name);
/*      */   }
/*      */   
/*      */   public void setAsyncSupported(boolean asyncSupported) {
/*  438 */     this.asyncSupported = Boolean.valueOf(asyncSupported);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void recycle()
/*      */   {
/*  447 */     this.internalDispatcherType = null;
/*  448 */     this.requestDispatcherPath = null;
/*      */     
/*  450 */     this.authType = null;
/*  451 */     this.inputBuffer.recycle();
/*  452 */     this.usingInputStream = false;
/*  453 */     this.usingReader = false;
/*  454 */     this.userPrincipal = null;
/*  455 */     this.subject = null;
/*  456 */     this.parametersParsed = false;
/*  457 */     if (this.parts != null) {
/*  458 */       for (Part part : this.parts) {
/*      */         try {
/*  460 */           part.delete();
/*      */         }
/*      */         catch (IOException localIOException) {}
/*      */       }
/*      */       
/*  465 */       this.parts = null;
/*      */     }
/*  467 */     this.partsParseException = null;
/*  468 */     this.locales.clear();
/*  469 */     this.localesParsed = false;
/*  470 */     this.secure = false;
/*  471 */     this.remoteAddr = null;
/*  472 */     this.remoteHost = null;
/*  473 */     this.remotePort = -1;
/*  474 */     this.localPort = -1;
/*  475 */     this.localAddr = null;
/*  476 */     this.localName = null;
/*      */     
/*  478 */     this.attributes.clear();
/*  479 */     this.sslAttributesParsed = false;
/*  480 */     this.notes.clear();
/*      */     
/*  482 */     recycleSessionInfo();
/*  483 */     recycleCookieInfo(false);
/*      */     
/*  485 */     if ((Globals.IS_SECURITY_ENABLED) || (Connector.RECYCLE_FACADES)) {
/*  486 */       this.parameterMap = new ParameterMap();
/*      */     } else {
/*  488 */       this.parameterMap.setLocked(false);
/*  489 */       this.parameterMap.clear();
/*      */     }
/*      */     
/*  492 */     this.mappingData.recycle();
/*  493 */     this.applicationMapping.recycle();
/*      */     
/*  495 */     this.applicationRequest = null;
/*  496 */     if ((Globals.IS_SECURITY_ENABLED) || (Connector.RECYCLE_FACADES)) {
/*  497 */       if (this.facade != null) {
/*  498 */         this.facade.clear();
/*  499 */         this.facade = null;
/*      */       }
/*  501 */       if (this.inputStream != null) {
/*  502 */         this.inputStream.clear();
/*  503 */         this.inputStream = null;
/*      */       }
/*  505 */       if (this.reader != null) {
/*  506 */         this.reader.clear();
/*  507 */         this.reader = null;
/*      */       }
/*      */     }
/*      */     
/*  511 */     this.asyncSupported = null;
/*  512 */     if (this.asyncContext != null) {
/*  513 */       this.asyncContext.recycle();
/*      */     }
/*  515 */     this.asyncContext = null;
/*      */   }
/*      */   
/*      */   protected void recycleSessionInfo()
/*      */   {
/*  520 */     if (this.session != null) {
/*      */       try {
/*  522 */         this.session.endAccess();
/*      */       } catch (Throwable t) {
/*  524 */         ExceptionUtils.handleThrowable(t);
/*  525 */         log.warn(sm.getString("coyoteRequest.sessionEndAccessFail"), t);
/*      */       }
/*      */     }
/*  528 */     this.session = null;
/*  529 */     this.requestedSessionCookie = false;
/*  530 */     this.requestedSessionId = null;
/*  531 */     this.requestedSessionURL = false;
/*  532 */     this.requestedSessionSSL = false;
/*      */   }
/*      */   
/*      */   protected void recycleCookieInfo(boolean recycleCoyote)
/*      */   {
/*  537 */     this.cookiesParsed = false;
/*  538 */     this.cookiesConverted = false;
/*  539 */     this.cookies = null;
/*  540 */     if (recycleCoyote) {
/*  541 */       getCoyoteRequest().getCookies().recycle();
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
/*      */   public Connector getConnector()
/*      */   {
/*  557 */     return this.connector;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setConnector(Connector connector)
/*      */   {
/*  566 */     this.connector = connector;
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
/*      */   public Context getContext()
/*      */   {
/*  580 */     return this.mappingData.context;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public void setContext(Context context)
/*      */   {
/*  592 */     this.mappingData.context = context;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  599 */   protected FilterChain filterChain = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FilterChain getFilterChain()
/*      */   {
/*  607 */     return this.filterChain;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFilterChain(FilterChain filterChain)
/*      */   {
/*  616 */     this.filterChain = filterChain;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Host getHost()
/*      */   {
/*  624 */     return this.mappingData.host;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  631 */   protected final MappingData mappingData = new MappingData();
/*  632 */   private final ApplicationMapping applicationMapping = new ApplicationMapping(this.mappingData);
/*      */   
/*      */ 
/*      */ 
/*      */   public MappingData getMappingData()
/*      */   {
/*  638 */     return this.mappingData;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  645 */   protected RequestFacade facade = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public javax.servlet.http.HttpServletRequest getRequest()
/*      */   {
/*  653 */     if (this.facade == null) {
/*  654 */       this.facade = new RequestFacade(this);
/*      */     }
/*  656 */     if (this.applicationRequest == null) {
/*  657 */       this.applicationRequest = this.facade;
/*      */     }
/*  659 */     return this.applicationRequest;
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
/*      */   public void setRequest(javax.servlet.http.HttpServletRequest applicationRequest)
/*      */   {
/*  673 */     ServletRequest r = applicationRequest;
/*  674 */     while ((r instanceof HttpServletRequestWrapper)) {
/*  675 */       r = ((HttpServletRequestWrapper)r).getRequest();
/*      */     }
/*  677 */     if (r != this.facade) {
/*  678 */       throw new IllegalArgumentException(sm.getString("request.illegalWrap"));
/*      */     }
/*  680 */     this.applicationRequest = applicationRequest;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  687 */   protected Response response = null;
/*      */   
/*      */ 
/*      */ 
/*      */   public Response getResponse()
/*      */   {
/*  693 */     return this.response;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setResponse(Response response)
/*      */   {
/*  702 */     this.response = response;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public InputStream getStream()
/*      */   {
/*  709 */     if (this.inputStream == null) {
/*  710 */       this.inputStream = new CoyoteInputStream(this.inputBuffer);
/*      */     }
/*  712 */     return this.inputStream;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  718 */   protected B2CConverter URIConverter = null;
/*      */   
/*      */ 
/*      */ 
/*      */   protected B2CConverter getURIConverter()
/*      */   {
/*  724 */     return this.URIConverter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setURIConverter(B2CConverter URIConverter)
/*      */   {
/*  733 */     this.URIConverter = URIConverter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Wrapper getWrapper()
/*      */   {
/*  741 */     return this.mappingData.wrapper;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public void setWrapper(Wrapper wrapper)
/*      */   {
/*  753 */     this.mappingData.wrapper = wrapper;
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
/*      */   public ServletInputStream createInputStream()
/*      */     throws IOException
/*      */   {
/*  769 */     if (this.inputStream == null) {
/*  770 */       this.inputStream = new CoyoteInputStream(this.inputBuffer);
/*      */     }
/*  772 */     return this.inputStream;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void finishRequest()
/*      */     throws IOException
/*      */   {
/*  783 */     if (this.response.getStatus() == 413) {
/*  784 */       checkSwallowInput();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getNote(String name)
/*      */   {
/*  796 */     return this.notes.get(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeNote(String name)
/*      */   {
/*  807 */     this.notes.remove(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLocalPort(int port)
/*      */   {
/*  817 */     this.localPort = port;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNote(String name, Object value)
/*      */   {
/*  828 */     this.notes.put(name, value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRemoteAddr(String remoteAddr)
/*      */   {
/*  838 */     this.remoteAddr = remoteAddr;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRemoteHost(String remoteHost)
/*      */   {
/*  849 */     this.remoteHost = remoteHost;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSecure(boolean secure)
/*      */   {
/*  860 */     this.secure = secure;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setServerPort(int port)
/*      */   {
/*  870 */     this.coyoteRequest.setServerPort(port);
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
/*      */   public Object getAttribute(String name)
/*      */   {
/*  887 */     SpecialAttributeAdapter adapter = (SpecialAttributeAdapter)specialAttributes.get(name);
/*  888 */     if (adapter != null) {
/*  889 */       return adapter.get(this, name);
/*      */     }
/*      */     
/*  892 */     Object attr = this.attributes.get(name);
/*      */     
/*  894 */     if (attr != null) {
/*  895 */       return attr;
/*      */     }
/*      */     
/*  898 */     attr = this.coyoteRequest.getAttribute(name);
/*  899 */     if (attr != null) {
/*  900 */       return attr;
/*      */     }
/*  902 */     if (TLSUtil.isTLSRequestAttribute(name)) {
/*  903 */       this.coyoteRequest.action(ActionCode.REQ_SSL_ATTRIBUTE, this.coyoteRequest);
/*  904 */       attr = this.coyoteRequest.getAttribute("javax.servlet.request.X509Certificate");
/*  905 */       if (attr != null) {
/*  906 */         this.attributes.put("javax.servlet.request.X509Certificate", attr);
/*      */       }
/*  908 */       attr = this.coyoteRequest.getAttribute("javax.servlet.request.cipher_suite");
/*  909 */       if (attr != null) {
/*  910 */         this.attributes.put("javax.servlet.request.cipher_suite", attr);
/*      */       }
/*  912 */       attr = this.coyoteRequest.getAttribute("javax.servlet.request.key_size");
/*  913 */       if (attr != null) {
/*  914 */         this.attributes.put("javax.servlet.request.key_size", attr);
/*      */       }
/*  916 */       attr = this.coyoteRequest.getAttribute("javax.servlet.request.ssl_session_id");
/*  917 */       if (attr != null) {
/*  918 */         this.attributes.put("javax.servlet.request.ssl_session_id", attr);
/*      */       }
/*  920 */       attr = this.coyoteRequest.getAttribute("javax.servlet.request.ssl_session_mgr");
/*  921 */       if (attr != null) {
/*  922 */         this.attributes.put("javax.servlet.request.ssl_session_mgr", attr);
/*      */       }
/*  924 */       attr = this.coyoteRequest.getAttribute("org.apache.tomcat.util.net.secure_protocol_version");
/*  925 */       if (attr != null) {
/*  926 */         this.attributes.put("org.apache.tomcat.util.net.secure_protocol_version", attr);
/*      */       }
/*  928 */       attr = this.attributes.get(name);
/*  929 */       this.sslAttributesParsed = true;
/*      */     }
/*  931 */     return attr;
/*      */   }
/*      */   
/*      */ 
/*      */   public long getContentLengthLong()
/*      */   {
/*  937 */     return this.coyoteRequest.getContentLengthLong();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Enumeration<String> getAttributeNames()
/*      */   {
/*  971 */     if ((isSecure()) && (!this.sslAttributesParsed)) {
/*  972 */       getAttribute("javax.servlet.request.X509Certificate");
/*      */     }
/*      */     
/*      */ 
/*  976 */     Set<String> names = new HashSet();
/*  977 */     names.addAll(this.attributes.keySet());
/*  978 */     return Collections.enumeration(names);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getCharacterEncoding()
/*      */   {
/*  987 */     String characterEncoding = this.coyoteRequest.getCharacterEncoding();
/*  988 */     if (characterEncoding != null) {
/*  989 */       return characterEncoding;
/*      */     }
/*      */     
/*  992 */     Context context = getContext();
/*  993 */     if (context != null) {
/*  994 */       return context.getRequestCharacterEncoding();
/*      */     }
/*      */     
/*  997 */     return null;
/*      */   }
/*      */   
/*      */   private Charset getCharset()
/*      */   {
/* 1002 */     Charset charset = null;
/*      */     try {
/* 1004 */       charset = this.coyoteRequest.getCharset();
/*      */     }
/*      */     catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
/*      */     
/* 1008 */     if (charset != null) {
/* 1009 */       return charset;
/*      */     }
/*      */     
/* 1012 */     Context context = getContext();
/* 1013 */     if (context != null) {
/* 1014 */       String encoding = context.getRequestCharacterEncoding();
/* 1015 */       if (encoding != null) {
/*      */         try {
/* 1017 */           return B2CConverter.getCharset(encoding);
/*      */         }
/*      */         catch (UnsupportedEncodingException localUnsupportedEncodingException1) {}
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1024 */     return Constants.DEFAULT_BODY_CHARSET;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getContentLength()
/*      */   {
/* 1033 */     return this.coyoteRequest.getContentLength();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getContentType()
/*      */   {
/* 1042 */     return this.coyoteRequest.getContentType();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setContentType(String contentType)
/*      */   {
/* 1052 */     this.coyoteRequest.setContentType(contentType);
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
/*      */   public ServletInputStream getInputStream()
/*      */     throws IOException
/*      */   {
/* 1068 */     if (this.usingReader)
/*      */     {
/* 1070 */       throw new IllegalStateException(sm.getString("coyoteRequest.getInputStream.ise"));
/*      */     }
/*      */     
/* 1073 */     this.usingInputStream = true;
/* 1074 */     if (this.inputStream == null) {
/* 1075 */       this.inputStream = new CoyoteInputStream(this.inputBuffer);
/*      */     }
/* 1077 */     return this.inputStream;
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
/*      */   public Locale getLocale()
/*      */   {
/* 1091 */     if (!this.localesParsed) {
/* 1092 */       parseLocales();
/*      */     }
/*      */     
/* 1095 */     if (this.locales.size() > 0) {
/* 1096 */       return (Locale)this.locales.get(0);
/*      */     }
/*      */     
/* 1099 */     return defaultLocale;
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
/*      */   public Enumeration<Locale> getLocales()
/*      */   {
/* 1112 */     if (!this.localesParsed) {
/* 1113 */       parseLocales();
/*      */     }
/*      */     
/* 1116 */     if (this.locales.size() > 0) {
/* 1117 */       return Collections.enumeration(this.locales);
/*      */     }
/* 1119 */     ArrayList<Locale> results = new ArrayList();
/* 1120 */     results.add(defaultLocale);
/* 1121 */     return Collections.enumeration(results);
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
/*      */   public String getParameter(String name)
/*      */   {
/* 1136 */     if (!this.parametersParsed) {
/* 1137 */       parseParameters();
/*      */     }
/*      */     
/* 1140 */     return this.coyoteRequest.getParameters().getParameter(name);
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
/*      */   public Map<String, String[]> getParameterMap()
/*      */   {
/* 1158 */     if (this.parameterMap.isLocked()) {
/* 1159 */       return this.parameterMap;
/*      */     }
/*      */     
/* 1162 */     Enumeration<String> enumeration = getParameterNames();
/* 1163 */     while (enumeration.hasMoreElements()) {
/* 1164 */       String name = (String)enumeration.nextElement();
/* 1165 */       String[] values = getParameterValues(name);
/* 1166 */       this.parameterMap.put(name, values);
/*      */     }
/*      */     
/* 1169 */     this.parameterMap.setLocked(true);
/*      */     
/* 1171 */     return this.parameterMap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Enumeration<String> getParameterNames()
/*      */   {
/* 1182 */     if (!this.parametersParsed) {
/* 1183 */       parseParameters();
/*      */     }
/*      */     
/* 1186 */     return this.coyoteRequest.getParameters().getParameterNames();
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
/*      */   public String[] getParameterValues(String name)
/*      */   {
/* 1200 */     if (!this.parametersParsed) {
/* 1201 */       parseParameters();
/*      */     }
/*      */     
/* 1204 */     return this.coyoteRequest.getParameters().getParameterValues(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getProtocol()
/*      */   {
/* 1214 */     return this.coyoteRequest.protocol().toString();
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
/*      */   public BufferedReader getReader()
/*      */     throws IOException
/*      */   {
/* 1231 */     if (this.usingInputStream)
/*      */     {
/* 1233 */       throw new IllegalStateException(sm.getString("coyoteRequest.getReader.ise"));
/*      */     }
/*      */     
/* 1236 */     this.usingReader = true;
/* 1237 */     this.inputBuffer.checkConverter();
/* 1238 */     if (this.reader == null) {
/* 1239 */       this.reader = new CoyoteReader(this.inputBuffer);
/*      */     }
/* 1241 */     return this.reader;
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
/*      */   @Deprecated
/*      */   public String getRealPath(String path)
/*      */   {
/* 1258 */     Context context = getContext();
/* 1259 */     if (context == null) {
/* 1260 */       return null;
/*      */     }
/* 1262 */     ServletContext servletContext = context.getServletContext();
/* 1263 */     if (servletContext == null) {
/* 1264 */       return null;
/*      */     }
/*      */     try
/*      */     {
/* 1268 */       return servletContext.getRealPath(path);
/*      */     } catch (IllegalArgumentException e) {}
/* 1270 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getRemoteAddr()
/*      */   {
/* 1280 */     if (this.remoteAddr == null)
/*      */     {
/* 1282 */       this.coyoteRequest.action(ActionCode.REQ_HOST_ADDR_ATTRIBUTE, this.coyoteRequest);
/* 1283 */       this.remoteAddr = this.coyoteRequest.remoteAddr().toString();
/*      */     }
/* 1285 */     return this.remoteAddr;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getRemoteHost()
/*      */   {
/* 1294 */     if (this.remoteHost == null) {
/* 1295 */       if (!this.connector.getEnableLookups()) {
/* 1296 */         this.remoteHost = getRemoteAddr();
/*      */       }
/*      */       else {
/* 1299 */         this.coyoteRequest.action(ActionCode.REQ_HOST_ATTRIBUTE, this.coyoteRequest);
/* 1300 */         this.remoteHost = this.coyoteRequest.remoteHost().toString();
/*      */       }
/*      */     }
/* 1303 */     return this.remoteHost;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getRemotePort()
/*      */   {
/* 1312 */     if (this.remotePort == -1)
/*      */     {
/* 1314 */       this.coyoteRequest.action(ActionCode.REQ_REMOTEPORT_ATTRIBUTE, this.coyoteRequest);
/* 1315 */       this.remotePort = this.coyoteRequest.getRemotePort();
/*      */     }
/* 1317 */     return this.remotePort;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getLocalName()
/*      */   {
/* 1326 */     if (this.localName == null)
/*      */     {
/* 1328 */       this.coyoteRequest.action(ActionCode.REQ_LOCAL_NAME_ATTRIBUTE, this.coyoteRequest);
/* 1329 */       this.localName = this.coyoteRequest.localName().toString();
/*      */     }
/* 1331 */     return this.localName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getLocalAddr()
/*      */   {
/* 1340 */     if (this.localAddr == null)
/*      */     {
/* 1342 */       this.coyoteRequest.action(ActionCode.REQ_LOCAL_ADDR_ATTRIBUTE, this.coyoteRequest);
/* 1343 */       this.localAddr = this.coyoteRequest.localAddr().toString();
/*      */     }
/* 1345 */     return this.localAddr;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getLocalPort()
/*      */   {
/* 1355 */     if (this.localPort == -1)
/*      */     {
/* 1357 */       this.coyoteRequest.action(ActionCode.REQ_LOCALPORT_ATTRIBUTE, this.coyoteRequest);
/* 1358 */       this.localPort = this.coyoteRequest.getLocalPort();
/*      */     }
/* 1360 */     return this.localPort;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public RequestDispatcher getRequestDispatcher(String path)
/*      */   {
/* 1372 */     Context context = getContext();
/* 1373 */     if (context == null) {
/* 1374 */       return null;
/*      */     }
/*      */     
/*      */ 
/* 1378 */     if (path == null)
/* 1379 */       return null;
/* 1380 */     if (path.startsWith("/")) {
/* 1381 */       return context.getServletContext().getRequestDispatcher(path);
/*      */     }
/*      */     
/*      */ 
/* 1385 */     String servletPath = (String)getAttribute("javax.servlet.include.servlet_path");
/*      */     
/* 1387 */     if (servletPath == null) {
/* 1388 */       servletPath = getServletPath();
/*      */     }
/*      */     
/*      */ 
/* 1392 */     String pathInfo = getPathInfo();
/* 1393 */     String requestPath = null;
/*      */     
/* 1395 */     if (pathInfo == null) {
/* 1396 */       requestPath = servletPath;
/*      */     } else {
/* 1398 */       requestPath = servletPath + pathInfo;
/*      */     }
/*      */     
/* 1401 */     int pos = requestPath.lastIndexOf('/');
/* 1402 */     String relative = null;
/* 1403 */     if (context.getDispatchersUseEncodedPaths()) {
/* 1404 */       if (pos >= 0) {
/* 1405 */         relative = URLEncoder.DEFAULT.encode(requestPath
/* 1406 */           .substring(0, pos + 1), StandardCharsets.UTF_8) + path;
/*      */       }
/*      */       else {
/* 1408 */         relative = URLEncoder.DEFAULT.encode(requestPath, StandardCharsets.UTF_8) + path;
/*      */       }
/*      */     }
/* 1411 */     else if (pos >= 0) {
/* 1412 */       relative = requestPath.substring(0, pos + 1) + path;
/*      */     } else {
/* 1414 */       relative = requestPath + path;
/*      */     }
/*      */     
/*      */ 
/* 1418 */     return context.getServletContext().getRequestDispatcher(relative);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getScheme()
/*      */   {
/* 1427 */     return this.coyoteRequest.scheme().toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getServerName()
/*      */   {
/* 1436 */     return this.coyoteRequest.serverName().toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getServerPort()
/*      */   {
/* 1445 */     return this.coyoteRequest.getServerPort();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSecure()
/*      */   {
/* 1454 */     return this.secure;
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
/*      */   public void removeAttribute(String name)
/*      */   {
/* 1467 */     if (name.startsWith("org.apache.tomcat.")) {
/* 1468 */       this.coyoteRequest.getAttributes().remove(name);
/*      */     }
/*      */     
/* 1471 */     boolean found = this.attributes.containsKey(name);
/* 1472 */     if (found) {
/* 1473 */       Object value = this.attributes.get(name);
/* 1474 */       this.attributes.remove(name);
/*      */       
/*      */ 
/* 1477 */       notifyAttributeRemoved(name, value);
/*      */     }
/*      */     else {}
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
/*      */   public void setAttribute(String name, Object value)
/*      */   {
/* 1494 */     if (name == null)
/*      */     {
/* 1496 */       throw new IllegalArgumentException(sm.getString("coyoteRequest.setAttribute.namenull"));
/*      */     }
/*      */     
/*      */ 
/* 1500 */     if (value == null) {
/* 1501 */       removeAttribute(name);
/* 1502 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1506 */     SpecialAttributeAdapter adapter = (SpecialAttributeAdapter)specialAttributes.get(name);
/* 1507 */     if (adapter != null) {
/* 1508 */       adapter.set(this, name, value);
/* 1509 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1514 */     if ((Globals.IS_SECURITY_ENABLED) && 
/* 1515 */       (name.equals("org.apache.tomcat.sendfile.filename")))
/*      */     {
/*      */ 
/*      */       try
/*      */       {
/* 1520 */         canonicalPath = new File(value.toString()).getCanonicalPath();
/*      */       } catch (IOException e) { String canonicalPath;
/* 1522 */         throw new SecurityException(sm.getString("coyoteRequest.sendfileNotCanonical", new Object[] { value }), e);
/*      */       }
/*      */       
/*      */ 
/*      */       String canonicalPath;
/*      */       
/* 1528 */       System.getSecurityManager().checkRead(canonicalPath);
/*      */       
/* 1530 */       value = canonicalPath;
/*      */     }
/*      */     
/* 1533 */     Object oldValue = this.attributes.put(name, value);
/*      */     
/*      */ 
/* 1536 */     if (name.startsWith("org.apache.tomcat.")) {
/* 1537 */       this.coyoteRequest.setAttribute(name, value);
/*      */     }
/*      */     
/*      */ 
/* 1541 */     notifyAttributeAssigned(name, value, oldValue);
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
/*      */   private void notifyAttributeAssigned(String name, Object value, Object oldValue)
/*      */   {
/* 1554 */     Context context = getContext();
/* 1555 */     Object[] listeners = context.getApplicationEventListeners();
/* 1556 */     if ((listeners == null) || (listeners.length == 0)) {
/* 1557 */       return;
/*      */     }
/* 1559 */     boolean replaced = oldValue != null;
/* 1560 */     ServletRequestAttributeEvent event = null;
/* 1561 */     if (replaced)
/*      */     {
/* 1563 */       event = new ServletRequestAttributeEvent(context.getServletContext(), getRequest(), name, oldValue);
/*      */     }
/*      */     else {
/* 1566 */       event = new ServletRequestAttributeEvent(context.getServletContext(), getRequest(), name, value);
/*      */     }
/*      */     
/* 1569 */     for (int i = 0; i < listeners.length; i++) {
/* 1570 */       if ((listeners[i] instanceof ServletRequestAttributeListener))
/*      */       {
/*      */ 
/* 1573 */         ServletRequestAttributeListener listener = (ServletRequestAttributeListener)listeners[i];
/*      */         try
/*      */         {
/* 1576 */           if (replaced) {
/* 1577 */             listener.attributeReplaced(event);
/*      */           } else {
/* 1579 */             listener.attributeAdded(event);
/*      */           }
/*      */         } catch (Throwable t) {
/* 1582 */           ExceptionUtils.handleThrowable(t);
/*      */           
/* 1584 */           this.attributes.put("javax.servlet.error.exception", t);
/* 1585 */           context.getLogger().error(sm.getString("coyoteRequest.attributeEvent"), t);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void notifyAttributeRemoved(String name, Object value)
/*      */   {
/* 1598 */     Context context = getContext();
/* 1599 */     Object[] listeners = context.getApplicationEventListeners();
/* 1600 */     if ((listeners == null) || (listeners.length == 0)) {
/* 1601 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1605 */     ServletRequestAttributeEvent event = new ServletRequestAttributeEvent(context.getServletContext(), getRequest(), name, value);
/* 1606 */     for (int i = 0; i < listeners.length; i++) {
/* 1607 */       if ((listeners[i] instanceof ServletRequestAttributeListener))
/*      */       {
/*      */ 
/* 1610 */         ServletRequestAttributeListener listener = (ServletRequestAttributeListener)listeners[i];
/*      */         try
/*      */         {
/* 1613 */           listener.attributeRemoved(event);
/*      */         } catch (Throwable t) {
/* 1615 */           ExceptionUtils.handleThrowable(t);
/*      */           
/* 1617 */           this.attributes.put("javax.servlet.error.exception", t);
/* 1618 */           context.getLogger().error(sm.getString("coyoteRequest.attributeEvent"), t);
/*      */         }
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
/*      */ 
/*      */ 
/*      */   public void setCharacterEncoding(String enc)
/*      */     throws UnsupportedEncodingException
/*      */   {
/* 1639 */     if (this.usingReader) {
/* 1640 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1644 */     Charset charset = B2CConverter.getCharset(enc);
/*      */     
/*      */ 
/* 1647 */     this.coyoteRequest.setCharset(charset);
/*      */   }
/*      */   
/*      */ 
/*      */   public ServletContext getServletContext()
/*      */   {
/* 1653 */     return getContext().getServletContext();
/*      */   }
/*      */   
/*      */   public AsyncContext startAsync()
/*      */   {
/* 1658 */     return startAsync(getRequest(), this.response.getResponse());
/*      */   }
/*      */   
/*      */ 
/*      */   public AsyncContext startAsync(ServletRequest request, ServletResponse response)
/*      */   {
/* 1664 */     if (!isAsyncSupported())
/*      */     {
/* 1666 */       IllegalStateException ise = new IllegalStateException(sm.getString("request.asyncNotSupported"));
/* 1667 */       log.warn(sm.getString("coyoteRequest.noAsync", new Object[] {
/* 1668 */         StringUtils.join(getNonAsyncClassNames()) }), ise);
/* 1669 */       throw ise;
/*      */     }
/*      */     
/* 1672 */     if (this.asyncContext == null) {
/* 1673 */       this.asyncContext = new AsyncContextImpl(this);
/*      */     }
/*      */     
/* 1676 */     this.asyncContext.setStarted(getContext(), request, response, 
/* 1677 */       (request == getRequest()) && (response == getResponse().getResponse()));
/* 1678 */     this.asyncContext.setTimeout(getConnector().getAsyncTimeout());
/*      */     
/* 1680 */     return this.asyncContext;
/*      */   }
/*      */   
/*      */   private Set<String> getNonAsyncClassNames()
/*      */   {
/* 1685 */     Set<String> result = new HashSet();
/*      */     
/* 1687 */     Wrapper wrapper = getWrapper();
/* 1688 */     if (!wrapper.isAsyncSupported()) {
/* 1689 */       result.add(wrapper.getServletClass());
/*      */     }
/*      */     
/* 1692 */     FilterChain filterChain = getFilterChain();
/* 1693 */     if ((filterChain instanceof ApplicationFilterChain)) {
/* 1694 */       ((ApplicationFilterChain)filterChain).findNonAsyncFilters(result);
/*      */     } else {
/* 1696 */       result.add(sm.getString("coyoteRequest.filterAsyncSupportUnknown"));
/*      */     }
/*      */     
/* 1699 */     Container c = wrapper;
/* 1700 */     while (c != null) {
/* 1701 */       c.getPipeline().findNonAsyncValves(result);
/* 1702 */       c = c.getParent();
/*      */     }
/*      */     
/* 1705 */     return result;
/*      */   }
/*      */   
/*      */   public boolean isAsyncStarted()
/*      */   {
/* 1710 */     if (this.asyncContext == null) {
/* 1711 */       return false;
/*      */     }
/*      */     
/* 1714 */     return this.asyncContext.isStarted();
/*      */   }
/*      */   
/*      */   public boolean isAsyncDispatching() {
/* 1718 */     if (this.asyncContext == null) {
/* 1719 */       return false;
/*      */     }
/*      */     
/* 1722 */     AtomicBoolean result = new AtomicBoolean(false);
/* 1723 */     this.coyoteRequest.action(ActionCode.ASYNC_IS_DISPATCHING, result);
/* 1724 */     return result.get();
/*      */   }
/*      */   
/*      */   public boolean isAsyncCompleting() {
/* 1728 */     if (this.asyncContext == null) {
/* 1729 */       return false;
/*      */     }
/*      */     
/* 1732 */     AtomicBoolean result = new AtomicBoolean(false);
/* 1733 */     this.coyoteRequest.action(ActionCode.ASYNC_IS_COMPLETING, result);
/* 1734 */     return result.get();
/*      */   }
/*      */   
/*      */   public boolean isAsync() {
/* 1738 */     if (this.asyncContext == null) {
/* 1739 */       return false;
/*      */     }
/*      */     
/* 1742 */     AtomicBoolean result = new AtomicBoolean(false);
/* 1743 */     this.coyoteRequest.action(ActionCode.ASYNC_IS_ASYNC, result);
/* 1744 */     return result.get();
/*      */   }
/*      */   
/*      */   public boolean isAsyncSupported()
/*      */   {
/* 1749 */     if (this.asyncSupported == null) {
/* 1750 */       return true;
/*      */     }
/*      */     
/* 1753 */     return this.asyncSupported.booleanValue();
/*      */   }
/*      */   
/*      */   public AsyncContext getAsyncContext()
/*      */   {
/* 1758 */     if (!isAsyncStarted()) {
/* 1759 */       throw new IllegalStateException(sm.getString("request.notAsync"));
/*      */     }
/* 1761 */     return this.asyncContext;
/*      */   }
/*      */   
/*      */   public AsyncContextImpl getAsyncContextInternal() {
/* 1765 */     return this.asyncContext;
/*      */   }
/*      */   
/*      */   public DispatcherType getDispatcherType()
/*      */   {
/* 1770 */     if (this.internalDispatcherType == null) {
/* 1771 */       return DispatcherType.REQUEST;
/*      */     }
/*      */     
/* 1774 */     return this.internalDispatcherType;
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
/*      */   public void addCookie(Cookie cookie)
/*      */   {
/* 1787 */     if (!this.cookiesConverted) {
/* 1788 */       convertCookies();
/*      */     }
/*      */     
/* 1791 */     int size = 0;
/* 1792 */     if (this.cookies != null) {
/* 1793 */       size = this.cookies.length;
/*      */     }
/*      */     
/* 1796 */     Cookie[] newCookies = new Cookie[size + 1];
/* 1797 */     for (int i = 0; i < size; i++) {
/* 1798 */       newCookies[i] = this.cookies[i];
/*      */     }
/* 1800 */     newCookies[size] = cookie;
/*      */     
/* 1802 */     this.cookies = newCookies;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addLocale(Locale locale)
/*      */   {
/* 1814 */     this.locales.add(locale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clearCookies()
/*      */   {
/* 1822 */     this.cookiesParsed = true;
/* 1823 */     this.cookiesConverted = true;
/* 1824 */     this.cookies = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clearLocales()
/*      */   {
/* 1832 */     this.locales.clear();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAuthType(String type)
/*      */   {
/* 1844 */     this.authType = type;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPathInfo(String path)
/*      */   {
/* 1856 */     this.mappingData.pathInfo.setString(path);
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
/*      */   public void setRequestedSessionCookie(boolean flag)
/*      */   {
/* 1869 */     this.requestedSessionCookie = flag;
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
/*      */   public void setRequestedSessionId(String id)
/*      */   {
/* 1882 */     this.requestedSessionId = id;
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
/*      */   public void setRequestedSessionURL(boolean flag)
/*      */   {
/* 1896 */     this.requestedSessionURL = flag;
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
/*      */   public void setRequestedSessionSSL(boolean flag)
/*      */   {
/* 1910 */     this.requestedSessionSSL = flag;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDecodedRequestURI()
/*      */   {
/* 1921 */     return this.coyoteRequest.decodedURI().toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MessageBytes getDecodedRequestURIMB()
/*      */   {
/* 1931 */     return this.coyoteRequest.decodedURI();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUserPrincipal(Principal principal)
/*      */   {
/* 1943 */     if (Globals.IS_SECURITY_ENABLED) {
/* 1944 */       if (this.subject == null) {
/* 1945 */         HttpSession session = getSession(false);
/* 1946 */         if (session == null)
/*      */         {
/* 1948 */           this.subject = newSubject(principal);
/*      */         }
/*      */         else {
/* 1951 */           this.subject = ((Subject)session.getAttribute("javax.security.auth.subject"));
/* 1952 */           if (this.subject == null) {
/* 1953 */             this.subject = newSubject(principal);
/* 1954 */             session.setAttribute("javax.security.auth.subject", this.subject);
/*      */           } else {
/* 1956 */             this.subject.getPrincipals().add(principal);
/*      */           }
/*      */         }
/*      */       } else {
/* 1960 */         this.subject.getPrincipals().add(principal);
/*      */       }
/*      */     }
/* 1963 */     this.userPrincipal = principal;
/*      */   }
/*      */   
/*      */   private Subject newSubject(Principal principal)
/*      */   {
/* 1968 */     Subject result = new Subject();
/* 1969 */     result.getPrincipals().add(principal);
/* 1970 */     return result;
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
/*      */   public PushBuilder newPushBuilder()
/*      */   {
/* 1984 */     return newPushBuilder(this);
/*      */   }
/*      */   
/*      */   public PushBuilder newPushBuilder(javax.servlet.http.HttpServletRequest request)
/*      */   {
/* 1989 */     AtomicBoolean result = new AtomicBoolean();
/* 1990 */     this.coyoteRequest.action(ActionCode.IS_PUSH_SUPPORTED, result);
/* 1991 */     if (result.get()) {
/* 1992 */       return new ApplicationPushBuilder(this, request);
/*      */     }
/* 1994 */     return null;
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
/*      */   public <T extends HttpUpgradeHandler> T upgrade(Class<T> httpUpgradeHandlerClass)
/*      */     throws IOException, ServletException
/*      */   {
/* 2009 */     InstanceManager instanceManager = null;
/*      */     try
/*      */     {
/*      */       T handler;
/* 2013 */       if (InternalHttpUpgradeHandler.class.isAssignableFrom(httpUpgradeHandlerClass)) {
/* 2014 */         handler = (HttpUpgradeHandler)httpUpgradeHandlerClass.getConstructor(new Class[0]).newInstance(new Object[0]);
/*      */       } else {
/* 2016 */         instanceManager = getContext().getInstanceManager();
/* 2017 */         handler = (HttpUpgradeHandler)instanceManager.newInstance(httpUpgradeHandlerClass);
/*      */       }
/*      */     }
/*      */     catch (InstantiationException|IllegalAccessException|InvocationTargetException|NamingException|IllegalArgumentException|NoSuchMethodException|SecurityException e) {
/*      */       T handler;
/* 2022 */       throw new ServletException(e);
/*      */     }
/*      */     T handler;
/* 2025 */     UpgradeToken upgradeToken = new UpgradeToken(handler, getContext(), instanceManager);
/*      */     
/* 2027 */     this.coyoteRequest.action(ActionCode.UPGRADE, upgradeToken);
/*      */     
/*      */ 
/*      */ 
/* 2031 */     this.response.setStatus(101);
/*      */     
/* 2033 */     return handler;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getAuthType()
/*      */   {
/* 2041 */     return this.authType;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getContextPath()
/*      */   {
/* 2051 */     String canonicalContextPath = getServletContext().getContextPath();
/* 2052 */     String uri = getRequestURI();
/* 2053 */     char[] uriChars = uri.toCharArray();
/* 2054 */     int lastSlash = this.mappingData.contextSlashCount;
/*      */     
/* 2056 */     if (lastSlash == 0) {
/* 2057 */       return "";
/*      */     }
/* 2059 */     int pos = 0;
/*      */     
/* 2061 */     while (lastSlash > 0) {
/* 2062 */       pos = nextSlash(uriChars, pos + 1);
/* 2063 */       if (pos == -1) {
/*      */         break;
/*      */       }
/* 2066 */       lastSlash--;
/*      */     }
/*      */     
/*      */ 
/*      */     String candidate;
/*      */     
/*      */ 
/* 2073 */     if (pos == -1) {
/* 2074 */       candidate = uri;
/*      */     } else {
/* 2076 */       candidate = uri.substring(0, pos);
/*      */     }
/* 2078 */     String candidate = removePathParameters(candidate);
/* 2079 */     candidate = UDecoder.URLDecode(candidate, this.connector.getURICharset());
/* 2080 */     candidate = RequestUtil.normalize(candidate);
/* 2081 */     boolean match = canonicalContextPath.equals(candidate);
/* 2082 */     while ((!match) && (pos != -1)) {
/* 2083 */       pos = nextSlash(uriChars, pos + 1);
/* 2084 */       if (pos == -1) {
/* 2085 */         candidate = uri;
/*      */       } else {
/* 2087 */         candidate = uri.substring(0, pos);
/*      */       }
/* 2089 */       candidate = removePathParameters(candidate);
/* 2090 */       candidate = UDecoder.URLDecode(candidate, this.connector.getURICharset());
/* 2091 */       candidate = RequestUtil.normalize(candidate);
/* 2092 */       match = canonicalContextPath.equals(candidate);
/*      */     }
/* 2094 */     if (match) {
/* 2095 */       if (pos == -1) {
/* 2096 */         return uri;
/*      */       }
/* 2098 */       return uri.substring(0, pos);
/*      */     }
/*      */     
/*      */ 
/* 2102 */     throw new IllegalStateException(sm.getString("coyoteRequest.getContextPath.ise", new Object[] { canonicalContextPath, uri }));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private String removePathParameters(String input)
/*      */   {
/* 2109 */     int nextSemiColon = input.indexOf(';');
/*      */     
/* 2111 */     if (nextSemiColon == -1) {
/* 2112 */       return input;
/*      */     }
/* 2114 */     StringBuilder result = new StringBuilder(input.length());
/* 2115 */     result.append(input.substring(0, nextSemiColon));
/*      */     for (;;) {
/* 2117 */       int nextSlash = input.indexOf('/', nextSemiColon);
/* 2118 */       if (nextSlash == -1) {
/*      */         break;
/*      */       }
/* 2121 */       nextSemiColon = input.indexOf(';', nextSlash);
/* 2122 */       if (nextSemiColon == -1) {
/* 2123 */         result.append(input.substring(nextSlash));
/* 2124 */         break;
/*      */       }
/* 2126 */       result.append(input.substring(nextSlash, nextSemiColon));
/*      */     }
/*      */     
/*      */ 
/* 2130 */     return result.toString();
/*      */   }
/*      */   
/*      */   private int nextSlash(char[] uri, int startPos)
/*      */   {
/* 2135 */     int len = uri.length;
/* 2136 */     int pos = startPos;
/* 2137 */     while (pos < len) {
/* 2138 */       if (uri[pos] == '/')
/* 2139 */         return pos;
/* 2140 */       if ((UDecoder.ALLOW_ENCODED_SLASH) && (uri[pos] == '%') && (pos + 2 < len) && (uri[(pos + 1)] == '2') && ((uri[(pos + 2)] == 'f') || (uri[(pos + 2)] == 'F')))
/*      */       {
/* 2142 */         return pos;
/*      */       }
/* 2144 */       pos++;
/*      */     }
/* 2146 */     return -1;
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
/*      */   public Cookie[] getCookies()
/*      */   {
/* 2159 */     if (!this.cookiesConverted) {
/* 2160 */       convertCookies();
/*      */     }
/* 2162 */     return this.cookies;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ServerCookies getServerCookies()
/*      */   {
/* 2174 */     parseCookies();
/* 2175 */     return this.coyoteRequest.getCookies();
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
/*      */   public long getDateHeader(String name)
/*      */   {
/* 2192 */     String value = getHeader(name);
/* 2193 */     if (value == null) {
/* 2194 */       return -1L;
/*      */     }
/*      */     
/*      */ 
/* 2198 */     long result = FastHttpDateFormat.parseDate(value, this.formats);
/* 2199 */     if (result != -1L) {
/* 2200 */       return result;
/*      */     }
/* 2202 */     throw new IllegalArgumentException(value);
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
/*      */   public String getHeader(String name)
/*      */   {
/* 2216 */     return this.coyoteRequest.getHeader(name);
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
/*      */   public Enumeration<String> getHeaders(String name)
/*      */   {
/* 2229 */     return this.coyoteRequest.getMimeHeaders().values(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Enumeration<String> getHeaderNames()
/*      */   {
/* 2238 */     return this.coyoteRequest.getMimeHeaders().names();
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
/*      */   public int getIntHeader(String name)
/*      */   {
/* 2255 */     String value = getHeader(name);
/* 2256 */     if (value == null) {
/* 2257 */       return -1;
/*      */     }
/*      */     
/* 2260 */     return Integer.parseInt(value);
/*      */   }
/*      */   
/*      */ 
/*      */   public ServletMapping getServletMapping()
/*      */   {
/* 2266 */     return this.applicationMapping.getServletMapping();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getMethod()
/*      */   {
/* 2275 */     return this.coyoteRequest.method().toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getPathInfo()
/*      */   {
/* 2284 */     return this.mappingData.pathInfo.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getPathTranslated()
/*      */   {
/* 2295 */     Context context = getContext();
/* 2296 */     if (context == null) {
/* 2297 */       return null;
/*      */     }
/*      */     
/* 2300 */     if (getPathInfo() == null) {
/* 2301 */       return null;
/*      */     }
/*      */     
/* 2304 */     return context.getServletContext().getRealPath(getPathInfo());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getQueryString()
/*      */   {
/* 2313 */     return this.coyoteRequest.queryString().toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getRemoteUser()
/*      */   {
/* 2324 */     if (this.userPrincipal == null) {
/* 2325 */       return null;
/*      */     }
/*      */     
/* 2328 */     return this.userPrincipal.getName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MessageBytes getRequestPathMB()
/*      */   {
/* 2338 */     return this.mappingData.requestPath;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getRequestedSessionId()
/*      */   {
/* 2347 */     return this.requestedSessionId;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getRequestURI()
/*      */   {
/* 2356 */     return this.coyoteRequest.requestURI().toString();
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
/*      */   public StringBuffer getRequestURL()
/*      */   {
/* 2379 */     StringBuffer url = new StringBuffer();
/* 2380 */     String scheme = getScheme();
/* 2381 */     int port = getServerPort();
/* 2382 */     if (port < 0)
/*      */     {
/* 2384 */       port = 80;
/*      */     }
/*      */     
/* 2387 */     url.append(scheme);
/* 2388 */     url.append("://");
/* 2389 */     url.append(getServerName());
/* 2390 */     if (((scheme.equals("http")) && (port != 80)) || (
/* 2391 */       (scheme.equals("https")) && (port != 443))) {
/* 2392 */       url.append(':');
/* 2393 */       url.append(port);
/*      */     }
/* 2395 */     url.append(getRequestURI());
/*      */     
/* 2397 */     return url;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getServletPath()
/*      */   {
/* 2407 */     return this.mappingData.wrapperPath.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpSession getSession()
/*      */   {
/* 2417 */     Session session = doGetSession(true);
/* 2418 */     if (session == null) {
/* 2419 */       return null;
/*      */     }
/*      */     
/* 2422 */     return session.getSession();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpSession getSession(boolean create)
/*      */   {
/* 2434 */     Session session = doGetSession(create);
/* 2435 */     if (session == null) {
/* 2436 */       return null;
/*      */     }
/*      */     
/* 2439 */     return session.getSession();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isRequestedSessionIdFromCookie()
/*      */   {
/* 2450 */     if (this.requestedSessionId == null) {
/* 2451 */       return false;
/*      */     }
/*      */     
/* 2454 */     return this.requestedSessionCookie;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isRequestedSessionIdFromURL()
/*      */   {
/* 2465 */     if (this.requestedSessionId == null) {
/* 2466 */       return false;
/*      */     }
/*      */     
/* 2469 */     return this.requestedSessionURL;
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
/*      */   @Deprecated
/*      */   public boolean isRequestedSessionIdFromUrl()
/*      */   {
/* 2483 */     return isRequestedSessionIdFromURL();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isRequestedSessionIdValid()
/*      */   {
/* 2494 */     if (this.requestedSessionId == null) {
/* 2495 */       return false;
/*      */     }
/*      */     
/* 2498 */     Context context = getContext();
/* 2499 */     if (context == null) {
/* 2500 */       return false;
/*      */     }
/*      */     
/* 2503 */     Manager manager = context.getManager();
/* 2504 */     if (manager == null) {
/* 2505 */       return false;
/*      */     }
/*      */     
/* 2508 */     Session session = null;
/*      */     try {
/* 2510 */       session = manager.findSession(this.requestedSessionId);
/*      */     }
/*      */     catch (IOException localIOException) {}
/*      */     
/*      */ 
/* 2515 */     if ((session == null) || (!session.isValid()))
/*      */     {
/* 2517 */       if (getMappingData().contexts == null) {
/* 2518 */         return false;
/*      */       }
/* 2520 */       for (int i = getMappingData().contexts.length; i > 0; i--) {
/* 2521 */         Context ctxt = getMappingData().contexts[(i - 1)];
/*      */         try {
/* 2523 */           if (ctxt.getManager().findSession(this.requestedSessionId) != null)
/*      */           {
/* 2525 */             return true;
/*      */           }
/*      */         }
/*      */         catch (IOException localIOException1) {}
/*      */       }
/*      */       
/* 2531 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 2535 */     return true;
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
/*      */   public boolean isUserInRole(String role)
/*      */   {
/* 2549 */     if (this.userPrincipal == null) {
/* 2550 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 2554 */     Context context = getContext();
/* 2555 */     if (context == null) {
/* 2556 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2561 */     if ("*".equals(role)) {
/* 2562 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2567 */     if (("**".equals(role)) && (!context.findSecurityRole("**"))) {
/* 2568 */       return this.userPrincipal != null;
/*      */     }
/*      */     
/* 2571 */     Realm realm = context.getRealm();
/* 2572 */     if (realm == null) {
/* 2573 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 2577 */     return realm.hasRole(getWrapper(), this.userPrincipal, role);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Principal getPrincipal()
/*      */   {
/* 2585 */     return this.userPrincipal;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Principal getUserPrincipal()
/*      */   {
/* 2594 */     if ((this.userPrincipal instanceof TomcatPrincipal))
/*      */     {
/* 2596 */       GSSCredential gssCredential = ((TomcatPrincipal)this.userPrincipal).getGssCredential();
/* 2597 */       if (gssCredential != null) {
/* 2598 */         int left = -1;
/*      */         try {
/* 2600 */           left = gssCredential.getRemainingLifetime();
/*      */         } catch (GSSException e) {
/* 2602 */           log.warn(sm.getString("coyoteRequest.gssLifetimeFail", new Object[] {this.userPrincipal
/* 2603 */             .getName() }), e);
/*      */         }
/* 2605 */         if (left == 0)
/*      */         {
/*      */           try {
/* 2608 */             logout();
/*      */           }
/*      */           catch (ServletException localServletException) {}
/*      */           
/*      */ 
/* 2613 */           return null;
/*      */         }
/*      */       }
/* 2616 */       return ((TomcatPrincipal)this.userPrincipal).getUserPrincipal();
/*      */     }
/*      */     
/* 2619 */     return this.userPrincipal;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Session getSessionInternal()
/*      */   {
/* 2628 */     return doGetSession(true);
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
/*      */   public void changeSessionId(String newSessionId)
/*      */   {
/* 2643 */     if ((this.requestedSessionId != null) && (this.requestedSessionId.length() > 0)) {
/* 2644 */       this.requestedSessionId = newSessionId;
/*      */     }
/*      */     
/* 2647 */     Context context = getContext();
/* 2648 */     if (context != null)
/*      */     {
/*      */ 
/* 2651 */       if (!context.getServletContext().getEffectiveSessionTrackingModes().contains(SessionTrackingMode.COOKIE)) {
/* 2652 */         return;
/*      */       }
/*      */     }
/* 2655 */     if (this.response != null)
/*      */     {
/* 2657 */       Cookie newCookie = ApplicationSessionCookieConfig.createSessionCookie(context, newSessionId, 
/* 2658 */         isSecure());
/* 2659 */       this.response.addSessionCookieInternal(newCookie);
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
/*      */   public String changeSessionId()
/*      */   {
/* 2673 */     Session session = getSessionInternal(false);
/* 2674 */     if (session == null)
/*      */     {
/* 2676 */       throw new IllegalStateException(sm.getString("coyoteRequest.changeSessionId"));
/*      */     }
/*      */     
/* 2679 */     Manager manager = getContext().getManager();
/* 2680 */     manager.changeSessionId(session);
/*      */     
/* 2682 */     String newSessionId = session.getId();
/* 2683 */     changeSessionId(newSessionId);
/*      */     
/* 2685 */     return newSessionId;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Session getSessionInternal(boolean create)
/*      */   {
/* 2695 */     return doGetSession(create);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isParametersParsed()
/*      */   {
/* 2703 */     return this.parametersParsed;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isFinished()
/*      */   {
/* 2712 */     return this.coyoteRequest.isFinished();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void checkSwallowInput()
/*      */   {
/* 2722 */     Context context = getContext();
/* 2723 */     if ((context != null) && (!context.getSwallowAbortedUploads())) {
/* 2724 */       this.coyoteRequest.action(ActionCode.DISABLE_SWALLOW_INPUT, null);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean authenticate(HttpServletResponse response)
/*      */     throws IOException, ServletException
/*      */   {
/* 2734 */     if (response.isCommitted())
/*      */     {
/* 2736 */       throw new IllegalStateException(sm.getString("coyoteRequest.authenticate.ise"));
/*      */     }
/*      */     
/* 2739 */     return getContext().getAuthenticator().authenticate(this, response);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void login(String username, String password)
/*      */     throws ServletException
/*      */   {
/* 2748 */     if ((getAuthType() != null) || (getRemoteUser() != null) || 
/* 2749 */       (getUserPrincipal() != null))
/*      */     {
/* 2751 */       throw new ServletException(sm.getString("coyoteRequest.alreadyAuthenticated"));
/*      */     }
/*      */     
/* 2754 */     Context context = getContext();
/* 2755 */     if (context.getAuthenticator() == null) {
/* 2756 */       throw new ServletException("no authenticator");
/*      */     }
/*      */     
/* 2759 */     context.getAuthenticator().login(username, password, this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void logout()
/*      */     throws ServletException
/*      */   {
/* 2767 */     getContext().getAuthenticator().logout(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Collection<Part> getParts()
/*      */     throws IOException, IllegalStateException, ServletException
/*      */   {
/* 2777 */     parseParts(true);
/*      */     
/* 2779 */     if (this.partsParseException != null) {
/* 2780 */       if ((this.partsParseException instanceof IOException))
/* 2781 */         throw ((IOException)this.partsParseException);
/* 2782 */       if ((this.partsParseException instanceof IllegalStateException))
/* 2783 */         throw ((IllegalStateException)this.partsParseException);
/* 2784 */       if ((this.partsParseException instanceof ServletException)) {
/* 2785 */         throw ((ServletException)this.partsParseException);
/*      */       }
/*      */     }
/*      */     
/* 2789 */     return this.parts;
/*      */   }
/*      */   
/*      */ 
/*      */   private void parseParts(boolean explicit)
/*      */   {
/* 2795 */     if ((this.parts != null) || (this.partsParseException != null)) {
/* 2796 */       return;
/*      */     }
/*      */     
/* 2799 */     Context context = getContext();
/* 2800 */     MultipartConfigElement mce = getWrapper().getMultipartConfigElement();
/*      */     
/* 2802 */     if (mce == null) {
/* 2803 */       if (context.getAllowCasualMultipartParsing())
/*      */       {
/*      */ 
/*      */ 
/* 2807 */         mce = new MultipartConfigElement(null, this.connector.getMaxPostSize(), this.connector.getMaxPostSize(), this.connector.getMaxPostSize());
/*      */       } else {
/* 2809 */         if (explicit)
/*      */         {
/* 2811 */           this.partsParseException = new IllegalStateException(sm.getString("coyoteRequest.noMultipartConfig"));
/* 2812 */           return;
/*      */         }
/* 2814 */         this.parts = Collections.emptyList();
/* 2815 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2820 */     Parameters parameters = this.coyoteRequest.getParameters();
/* 2821 */     parameters.setLimit(getConnector().getMaxParameterCount());
/*      */     
/* 2823 */     boolean success = false;
/*      */     try
/*      */     {
/* 2826 */       String locationStr = mce.getLocation();
/* 2827 */       File location; File location; if ((locationStr == null) || (locationStr.length() == 0)) {
/* 2828 */         location = (File)context.getServletContext().getAttribute("javax.servlet.context.tempdir");
/*      */       }
/*      */       else
/*      */       {
/* 2832 */         location = new File(locationStr);
/* 2833 */         if (!location.isAbsolute())
/*      */         {
/*      */ 
/*      */ 
/* 2837 */           location = new File((File)context.getServletContext().getAttribute("javax.servlet.context.tempdir"), locationStr).getAbsoluteFile();
/*      */         }
/*      */       }
/*      */       
/* 2841 */       if (!location.isDirectory()) {
/* 2842 */         parameters.setParseFailedReason(Parameters.FailReason.MULTIPART_CONFIG_INVALID);
/*      */         
/* 2844 */         this.partsParseException = new IOException(sm.getString("coyoteRequest.uploadLocationInvalid", new Object[] { location }));
/*      */         
/* 2846 */         return;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2851 */       DiskFileItemFactory factory = new DiskFileItemFactory();
/*      */       try {
/* 2853 */         factory.setRepository(location.getCanonicalFile());
/*      */       } catch (IOException ioe) {
/* 2855 */         parameters.setParseFailedReason(Parameters.FailReason.IO_ERROR);
/* 2856 */         this.partsParseException = ioe;
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
/* 2927 */         if ((this.partsParseException != null) || (!success)) {
/* 2928 */           parameters.setParseFailedReason(Parameters.FailReason.UNKNOWN);
/*      */         }
/* 2857 */         return;
/*      */       }
/* 2859 */       factory.setSizeThreshold(mce.getFileSizeThreshold());
/*      */       
/* 2861 */       ServletFileUpload upload = new ServletFileUpload();
/* 2862 */       upload.setFileItemFactory(factory);
/* 2863 */       upload.setFileSizeMax(mce.getMaxFileSize());
/* 2864 */       upload.setSizeMax(mce.getMaxRequestSize());
/*      */       
/* 2866 */       this.parts = new ArrayList();
/*      */       try
/*      */       {
/* 2869 */         List<FileItem> items = upload.parseRequest(new ServletRequestContext(this));
/* 2870 */         int maxPostSize = getConnector().getMaxPostSize();
/* 2871 */         int postSize = 0;
/* 2872 */         Charset charset = getCharset();
/* 2873 */         for (FileItem item : items) {
/* 2874 */           ApplicationPart part = new ApplicationPart(item, location);
/* 2875 */           this.parts.add(part);
/* 2876 */           if (part.getSubmittedFileName() == null) {
/* 2877 */             String name = part.getName();
/* 2878 */             String value = null;
/*      */             try {
/* 2880 */               value = part.getString(charset.name());
/*      */             }
/*      */             catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
/*      */             
/* 2884 */             if (maxPostSize >= 0)
/*      */             {
/*      */ 
/* 2887 */               postSize += name.getBytes(charset).length;
/* 2888 */               if (value != null)
/*      */               {
/* 2890 */                 postSize++;
/*      */                 
/* 2892 */                 postSize = (int)(postSize + part.getSize());
/*      */               }
/*      */               
/* 2895 */               postSize++;
/* 2896 */               if (postSize > maxPostSize) {
/* 2897 */                 parameters.setParseFailedReason(Parameters.FailReason.POST_TOO_LARGE);
/* 2898 */                 throw new IllegalStateException(sm.getString("coyoteRequest.maxPostSizeExceeded"));
/*      */               }
/*      */             }
/*      */             
/* 2902 */             parameters.addParameter(name, value);
/*      */           }
/*      */         }
/*      */         
/* 2906 */         success = true;
/*      */       } catch (FileUploadBase.InvalidContentTypeException e) {
/* 2908 */         parameters.setParseFailedReason(Parameters.FailReason.INVALID_CONTENT_TYPE);
/* 2909 */         this.partsParseException = new ServletException(e);
/*      */       } catch (FileUploadBase.SizeException e) {
/* 2911 */         parameters.setParseFailedReason(Parameters.FailReason.POST_TOO_LARGE);
/* 2912 */         checkSwallowInput();
/* 2913 */         this.partsParseException = new IllegalStateException(e);
/*      */       } catch (FileUploadException e) {
/* 2915 */         parameters.setParseFailedReason(Parameters.FailReason.IO_ERROR);
/* 2916 */         this.partsParseException = new IOException(e);
/*      */       }
/*      */       catch (IllegalStateException e) {
/* 2919 */         checkSwallowInput();
/* 2920 */         this.partsParseException = e;
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*      */     finally
/*      */     {
/* 2927 */       if ((this.partsParseException != null) || (!success)) {
/* 2928 */         parameters.setParseFailedReason(Parameters.FailReason.UNKNOWN);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Part getPart(String name)
/*      */     throws IOException, IllegalStateException, ServletException
/*      */   {
/* 2940 */     Collection<Part> c = getParts();
/* 2941 */     Iterator<Part> iterator = c.iterator();
/* 2942 */     while (iterator.hasNext()) {
/* 2943 */       Part part = (Part)iterator.next();
/* 2944 */       if (name.equals(part.getName())) {
/* 2945 */         return part;
/*      */       }
/*      */     }
/* 2948 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Session doGetSession(boolean create)
/*      */   {
/* 2957 */     Context context = getContext();
/* 2958 */     if (context == null) {
/* 2959 */       return null;
/*      */     }
/*      */     
/*      */ 
/* 2963 */     if ((this.session != null) && (!this.session.isValid())) {
/* 2964 */       this.session = null;
/*      */     }
/* 2966 */     if (this.session != null) {
/* 2967 */       return this.session;
/*      */     }
/*      */     
/*      */ 
/* 2971 */     Manager manager = context.getManager();
/* 2972 */     if (manager == null) {
/* 2973 */       return null;
/*      */     }
/* 2975 */     if (this.requestedSessionId != null) {
/*      */       try {
/* 2977 */         this.session = manager.findSession(this.requestedSessionId);
/*      */       } catch (IOException e) {
/* 2979 */         this.session = null;
/*      */       }
/* 2981 */       if ((this.session != null) && (!this.session.isValid())) {
/* 2982 */         this.session = null;
/*      */       }
/* 2984 */       if (this.session != null) {
/* 2985 */         this.session.access();
/* 2986 */         return this.session;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2991 */     if (!create) {
/* 2992 */       return null;
/*      */     }
/* 2994 */     if (this.response != null)
/*      */     {
/*      */ 
/* 2997 */       if ((context.getServletContext().getEffectiveSessionTrackingModes().contains(SessionTrackingMode.COOKIE)) && 
/* 2998 */         (this.response.getResponse().isCommitted()))
/*      */       {
/* 3000 */         throw new IllegalStateException(sm.getString("coyoteRequest.sessionCreateCommitted"));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 3005 */     String sessionId = getRequestedSessionId();
/* 3006 */     if (!this.requestedSessionSSL)
/*      */     {
/*      */ 
/* 3009 */       if (("/".equals(context.getSessionCookiePath())) && 
/* 3010 */         (isRequestedSessionIdFromCookie()))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3021 */         if (context.getValidateClientProvidedNewSessionId()) {
/* 3022 */           boolean found = false;
/* 3023 */           for (Container container : getHost().findChildren()) {
/* 3024 */             Manager m = ((Context)container).getManager();
/* 3025 */             if (m != null) {
/*      */               try {
/* 3027 */                 if (m.findSession(sessionId) != null) {
/* 3028 */                   found = true;
/* 3029 */                   break;
/*      */                 }
/*      */               }
/*      */               catch (IOException localIOException1) {}
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 3037 */           if (!found) {
/* 3038 */             sessionId = null;
/*      */           }
/*      */         }
/*      */       } else
/* 3042 */         sessionId = null;
/*      */     }
/* 3044 */     this.session = manager.createSession(sessionId);
/*      */     
/*      */ 
/* 3047 */     if (this.session != null)
/*      */     {
/*      */ 
/* 3050 */       if (context.getServletContext().getEffectiveSessionTrackingModes().contains(SessionTrackingMode.COOKIE))
/*      */       {
/* 3052 */         Cookie cookie = ApplicationSessionCookieConfig.createSessionCookie(context, this.session
/* 3053 */           .getIdInternal(), isSecure());
/*      */         
/* 3055 */         this.response.addSessionCookieInternal(cookie);
/*      */       }
/*      */     }
/* 3058 */     if (this.session == null) {
/* 3059 */       return null;
/*      */     }
/*      */     
/* 3062 */     this.session.access();
/* 3063 */     return this.session;
/*      */   }
/*      */   
/*      */   protected String unescape(String s) {
/* 3067 */     if (s == null) {
/* 3068 */       return null;
/*      */     }
/* 3070 */     if (s.indexOf('\\') == -1) {
/* 3071 */       return s;
/*      */     }
/* 3073 */     StringBuilder buf = new StringBuilder();
/* 3074 */     for (int i = 0; i < s.length(); i++) {
/* 3075 */       char c = s.charAt(i);
/* 3076 */       if (c != '\\') {
/* 3077 */         buf.append(c);
/*      */       } else {
/* 3079 */         i++; if (i >= s.length())
/*      */         {
/* 3081 */           throw new IllegalArgumentException();
/*      */         }
/* 3083 */         c = s.charAt(i);
/* 3084 */         buf.append(c);
/*      */       }
/*      */     }
/* 3087 */     return buf.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void parseCookies()
/*      */   {
/* 3095 */     if (this.cookiesParsed) {
/* 3096 */       return;
/*      */     }
/*      */     
/* 3099 */     this.cookiesParsed = true;
/*      */     
/* 3101 */     ServerCookies serverCookies = this.coyoteRequest.getCookies();
/* 3102 */     serverCookies.setLimit(this.connector.getMaxCookieCount());
/* 3103 */     CookieProcessor cookieProcessor = getContext().getCookieProcessor();
/* 3104 */     cookieProcessor.parseCookieHeader(this.coyoteRequest.getMimeHeaders(), serverCookies);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void convertCookies()
/*      */   {
/* 3112 */     if (this.cookiesConverted) {
/* 3113 */       return;
/*      */     }
/*      */     
/* 3116 */     this.cookiesConverted = true;
/*      */     
/* 3118 */     if (getContext() == null) {
/* 3119 */       return;
/*      */     }
/*      */     
/* 3122 */     parseCookies();
/*      */     
/* 3124 */     ServerCookies serverCookies = this.coyoteRequest.getCookies();
/* 3125 */     CookieProcessor cookieProcessor = getContext().getCookieProcessor();
/*      */     
/* 3127 */     int count = serverCookies.getCookieCount();
/* 3128 */     if (count <= 0) {
/* 3129 */       return;
/*      */     }
/*      */     
/* 3132 */     this.cookies = new Cookie[count];
/*      */     
/* 3134 */     int idx = 0;
/* 3135 */     for (int i = 0; i < count; i++) {
/* 3136 */       ServerCookie scookie = serverCookies.getCookie(i);
/*      */       
/*      */ 
/*      */       try
/*      */       {
/* 3141 */         Cookie cookie = new Cookie(scookie.getName().toString(), null);
/* 3142 */         int version = scookie.getVersion();
/* 3143 */         cookie.setVersion(version);
/* 3144 */         scookie.getValue().getByteChunk().setCharset(cookieProcessor.getCharset());
/* 3145 */         cookie.setValue(unescape(scookie.getValue().toString()));
/* 3146 */         cookie.setPath(unescape(scookie.getPath().toString()));
/* 3147 */         String domain = scookie.getDomain().toString();
/* 3148 */         if (domain != null)
/*      */         {
/* 3150 */           cookie.setDomain(unescape(domain));
/*      */         }
/* 3152 */         String comment = scookie.getComment().toString();
/* 3153 */         cookie.setComment(version == 1 ? unescape(comment) : null);
/* 3154 */         this.cookies[(idx++)] = cookie;
/*      */       }
/*      */       catch (IllegalArgumentException localIllegalArgumentException) {}
/*      */     }
/*      */     
/* 3159 */     if (idx < count) {
/* 3160 */       Cookie[] ncookies = new Cookie[idx];
/* 3161 */       System.arraycopy(this.cookies, 0, ncookies, 0, idx);
/* 3162 */       this.cookies = ncookies;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void parseParameters()
/*      */   {
/* 3172 */     this.parametersParsed = true;
/*      */     
/* 3174 */     Parameters parameters = this.coyoteRequest.getParameters();
/* 3175 */     boolean success = false;
/*      */     try
/*      */     {
/* 3178 */       parameters.setLimit(getConnector().getMaxParameterCount());
/*      */       
/*      */ 
/*      */ 
/* 3182 */       Charset charset = getCharset();
/*      */       
/* 3184 */       boolean useBodyEncodingForURI = this.connector.getUseBodyEncodingForURI();
/* 3185 */       parameters.setCharset(charset);
/* 3186 */       if (useBodyEncodingForURI) {
/* 3187 */         parameters.setQueryStringCharset(charset);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 3192 */       parameters.handleQueryParameters();
/*      */       
/* 3194 */       if ((this.usingInputStream) || (this.usingReader)) {
/* 3195 */         success = true;
/* 3196 */         return;
/*      */       }
/*      */       
/* 3199 */       if (!getConnector().isParseBodyMethod(getMethod())) {
/* 3200 */         success = true;
/* 3201 */         return;
/*      */       }
/*      */       
/* 3204 */       String contentType = getContentType();
/* 3205 */       if (contentType == null) {
/* 3206 */         contentType = "";
/*      */       }
/* 3208 */       int semicolon = contentType.indexOf(';');
/* 3209 */       if (semicolon >= 0) {
/* 3210 */         contentType = contentType.substring(0, semicolon).trim();
/*      */       } else {
/* 3212 */         contentType = contentType.trim();
/*      */       }
/*      */       
/* 3215 */       if ("multipart/form-data".equals(contentType)) {
/* 3216 */         parseParts(false);
/* 3217 */         success = true;
/* 3218 */         return;
/*      */       }
/*      */       
/* 3221 */       if (!"application/x-www-form-urlencoded".equals(contentType)) {
/* 3222 */         success = true;
/* 3223 */         return;
/*      */       }
/*      */       
/* 3226 */       int len = getContentLength();
/*      */       
/* 3228 */       if (len > 0) {
/* 3229 */         int maxPostSize = this.connector.getMaxPostSize();
/* 3230 */         if ((maxPostSize >= 0) && (len > maxPostSize)) {
/* 3231 */           Context context = getContext();
/* 3232 */           if ((context != null) && (context.getLogger().isDebugEnabled())) {
/* 3233 */             context.getLogger().debug(sm
/* 3234 */               .getString("coyoteRequest.postTooLarge"));
/*      */           }
/* 3236 */           checkSwallowInput();
/* 3237 */           parameters.setParseFailedReason(Parameters.FailReason.POST_TOO_LARGE);
/* 3238 */           return;
/*      */         }
/* 3240 */         byte[] formData = null;
/* 3241 */         if (len < 8192) {
/* 3242 */           if (this.postData == null) {
/* 3243 */             this.postData = new byte[' '];
/*      */           }
/* 3245 */           formData = this.postData;
/*      */         } else {
/* 3247 */           formData = new byte[len];
/*      */         }
/*      */         try {
/* 3250 */           if (readPostBody(formData, len) != len) {
/* 3251 */             parameters.setParseFailedReason(Parameters.FailReason.REQUEST_BODY_INCOMPLETE);
/* 3252 */             return;
/*      */           }
/*      */         }
/*      */         catch (IOException e) {
/* 3256 */           Context context = getContext();
/* 3257 */           if ((context != null) && (context.getLogger().isDebugEnabled())) {
/* 3258 */             context.getLogger().debug(sm
/* 3259 */               .getString("coyoteRequest.parseParameters"), e);
/*      */           }
/*      */           
/* 3262 */           parameters.setParseFailedReason(Parameters.FailReason.CLIENT_DISCONNECT);
/* 3263 */           return;
/*      */         }
/* 3265 */         parameters.processParameters(formData, 0, len);
/* 3266 */       } else if ("chunked".equalsIgnoreCase(this.coyoteRequest
/* 3267 */         .getHeader("transfer-encoding"))) {
/* 3268 */         byte[] formData = null;
/*      */         try {
/* 3270 */           formData = readChunkedPostBody();
/*      */         }
/*      */         catch (IllegalStateException ise) {
/* 3273 */           parameters.setParseFailedReason(Parameters.FailReason.POST_TOO_LARGE);
/* 3274 */           Context context = getContext();
/* 3275 */           if ((context != null) && (context.getLogger().isDebugEnabled())) {
/* 3276 */             context.getLogger().debug(sm
/* 3277 */               .getString("coyoteRequest.parseParameters"), ise);
/*      */           }
/*      */           
/* 3280 */           return;
/*      */         }
/*      */         catch (IOException e) {
/* 3283 */           parameters.setParseFailedReason(Parameters.FailReason.CLIENT_DISCONNECT);
/* 3284 */           Context context = getContext();
/* 3285 */           if ((context != null) && (context.getLogger().isDebugEnabled())) {
/* 3286 */             context.getLogger().debug(sm
/* 3287 */               .getString("coyoteRequest.parseParameters"), e);
/*      */           }
/*      */           
/* 3290 */           return;
/*      */         }
/* 3292 */         if (formData != null) {
/* 3293 */           parameters.processParameters(formData, 0, formData.length);
/*      */         }
/*      */       }
/* 3296 */       success = true;
/*      */     } finally {
/* 3298 */       if (!success) {
/* 3299 */         parameters.setParseFailedReason(Parameters.FailReason.UNKNOWN);
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
/*      */   protected int readPostBody(byte[] body, int len)
/*      */     throws IOException
/*      */   {
/* 3317 */     int offset = 0;
/*      */     do {
/* 3319 */       int inputLen = getStream().read(body, offset, len - offset);
/* 3320 */       if (inputLen <= 0) {
/* 3321 */         return offset;
/*      */       }
/* 3323 */       offset += inputLen;
/* 3324 */     } while (len - offset > 0);
/* 3325 */     return len;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected byte[] readChunkedPostBody()
/*      */     throws IOException
/*      */   {
/* 3337 */     ByteChunk body = new ByteChunk();
/*      */     
/* 3339 */     byte[] buffer = new byte[' '];
/*      */     
/* 3341 */     int len = 0;
/* 3342 */     while (len > -1) {
/* 3343 */       len = getStream().read(buffer, 0, 8192);
/* 3344 */       if ((this.connector.getMaxPostSize() >= 0) && 
/* 3345 */         (body.getLength() + len > this.connector.getMaxPostSize()))
/*      */       {
/* 3347 */         checkSwallowInput();
/*      */         
/* 3349 */         throw new IllegalStateException(sm.getString("coyoteRequest.chunkedPostTooLarge"));
/*      */       }
/* 3351 */       if (len > 0) {
/* 3352 */         body.append(buffer, 0, len);
/*      */       }
/*      */     }
/* 3355 */     if (body.getLength() == 0) {
/* 3356 */       return null;
/*      */     }
/* 3358 */     if (body.getLength() < body.getBuffer().length) {
/* 3359 */       int length = body.getLength();
/* 3360 */       byte[] result = new byte[length];
/* 3361 */       System.arraycopy(body.getBuffer(), 0, result, 0, length);
/* 3362 */       return result;
/*      */     }
/*      */     
/* 3365 */     return body.getBuffer();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void parseLocales()
/*      */   {
/* 3374 */     this.localesParsed = true;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3380 */     TreeMap<Double, ArrayList<Locale>> locales = new TreeMap();
/*      */     
/* 3382 */     Enumeration<String> values = getHeaders("accept-language");
/*      */     String value;
/* 3384 */     while (values.hasMoreElements()) {
/* 3385 */       value = (String)values.nextElement();
/* 3386 */       parseLocalesHeader(value, locales);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3391 */     for (ArrayList<Locale> list : locales.values()) {
/* 3392 */       for (Locale locale : list) {
/* 3393 */         addLocale(locale);
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
/*      */   protected void parseLocalesHeader(String value, TreeMap<Double, ArrayList<Locale>> locales)
/*      */   {
/*      */     try
/*      */     {
/* 3409 */       acceptLanguages = AcceptLanguage.parse(new StringReader(value));
/*      */     }
/*      */     catch (IOException e) {
/*      */       List<AcceptLanguage> acceptLanguages;
/*      */       return;
/*      */     }
/*      */     List<AcceptLanguage> acceptLanguages;
/* 3416 */     for (AcceptLanguage acceptLanguage : acceptLanguages)
/*      */     {
/* 3418 */       Double key = Double.valueOf(-acceptLanguage.getQuality());
/* 3419 */       ArrayList<Locale> values = (ArrayList)locales.get(key);
/* 3420 */       if (values == null) {
/* 3421 */         values = new ArrayList();
/* 3422 */         locales.put(key, values);
/*      */       }
/* 3424 */       values.add(acceptLanguage.getLocale());
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
/*      */ 
/* 3440 */   private static final Map<String, SpecialAttributeAdapter> specialAttributes = new HashMap();
/*      */   
/*      */   static
/*      */   {
/* 3444 */     specialAttributes.put("org.apache.catalina.core.DISPATCHER_TYPE", new SpecialAttributeAdapter()
/*      */     {
/*      */       public Object get(Request request, String name)
/*      */       {
/* 3448 */         return request.internalDispatcherType == null ? DispatcherType.REQUEST : request.internalDispatcherType;
/*      */       }
/*      */       
/*      */ 
/*      */       public void set(Request request, String name, Object value)
/*      */       {
/* 3454 */         request.internalDispatcherType = ((DispatcherType)value);
/*      */       }
/* 3456 */     });
/* 3457 */     specialAttributes.put("org.apache.catalina.core.DISPATCHER_REQUEST_PATH", new SpecialAttributeAdapter()
/*      */     {
/*      */       public Object get(Request request, String name)
/*      */       {
/* 3461 */         return request.requestDispatcherPath == null ? request
/* 3462 */           .getRequestPathMB().toString() : request.requestDispatcherPath
/* 3463 */           .toString();
/*      */       }
/*      */       
/*      */       public void set(Request request, String name, Object value)
/*      */       {
/* 3468 */         request.requestDispatcherPath = value;
/*      */       }
/* 3470 */     });
/* 3471 */     specialAttributes.put("org.apache.catalina.ASYNC_SUPPORTED", new SpecialAttributeAdapter()
/*      */     {
/*      */       public Object get(Request request, String name)
/*      */       {
/* 3475 */         return request.asyncSupported;
/*      */       }
/*      */       
/*      */       public void set(Request request, String name, Object value)
/*      */       {
/* 3480 */         Boolean oldValue = request.asyncSupported;
/* 3481 */         request.asyncSupported = ((Boolean)value);
/* 3482 */         request.notifyAttributeAssigned(name, value, oldValue);
/*      */       }
/* 3484 */     });
/* 3485 */     specialAttributes.put("org.apache.catalina.realm.GSS_CREDENTIAL", new SpecialAttributeAdapter()
/*      */     {
/*      */       public Object get(Request request, String name)
/*      */       {
/* 3489 */         if ((request.userPrincipal instanceof TomcatPrincipal)) {
/* 3490 */           return 
/* 3491 */             ((TomcatPrincipal)request.userPrincipal).getGssCredential();
/*      */         }
/* 3493 */         return null;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public void set(Request request, String name, Object value) {}
/* 3500 */     });
/* 3501 */     specialAttributes.put("org.apache.catalina.parameter_parse_failed", new SpecialAttributeAdapter()
/*      */     {
/*      */ 
/*      */       public Object get(Request request, String name)
/*      */       {
/* 3506 */         if (request.getCoyoteRequest().getParameters().isParseFailed()) {
/* 3507 */           return Boolean.TRUE;
/*      */         }
/* 3509 */         return null;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public void set(Request request, String name, Object value) {}
/* 3516 */     });
/* 3517 */     specialAttributes.put("org.apache.catalina.parameter_parse_failed_reason", new SpecialAttributeAdapter()
/*      */     {
/*      */       public Object get(Request request, String name)
/*      */       {
/* 3521 */         return request.getCoyoteRequest().getParameters().getParseFailedReason();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public void set(Request request, String name, Object value) {}
/* 3528 */     });
/* 3529 */     specialAttributes.put("org.apache.tomcat.sendfile.support", new SpecialAttributeAdapter()
/*      */     {
/*      */       public Object get(Request request, String name)
/*      */       {
/* 3533 */         return Boolean.valueOf(
/*      */         
/* 3535 */           (request.getConnector().getProtocolHandler().isSendfileSupported()) && (request.getCoyoteRequest().getSendfile()));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       public void set(Request request, String name, Object value) {}
/*      */     });
/*      */     
/* 3543 */     for (SimpleDateFormat sdf : formatsTemplate) {
/* 3544 */       sdf.setTimeZone(GMT_ZONE);
/*      */     }
/*      */   }
/*      */   
/*      */   private static abstract interface SpecialAttributeAdapter
/*      */   {
/*      */     public abstract Object get(Request paramRequest, String paramString);
/*      */     
/*      */     public abstract void set(Request paramRequest, String paramString, Object paramObject);
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\connector\Request.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */