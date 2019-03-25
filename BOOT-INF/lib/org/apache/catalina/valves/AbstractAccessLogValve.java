/*      */ package org.apache.catalina.valves;
/*      */ 
/*      */ import java.io.CharArrayWriter;
/*      */ import java.io.IOException;
/*      */ import java.net.InetAddress;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.TimeZone;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.http.Cookie;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpSession;
/*      */ import org.apache.catalina.AccessLog;
/*      */ import org.apache.catalina.LifecycleException;
/*      */ import org.apache.catalina.LifecycleState;
/*      */ import org.apache.catalina.Session;
/*      */ import org.apache.catalina.Valve;
/*      */ import org.apache.catalina.connector.ClientAbortException;
/*      */ import org.apache.catalina.util.TLSUtil;
/*      */ import org.apache.coyote.ActionCode;
/*      */ import org.apache.coyote.RequestInfo;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.collections.SynchronizedStack;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractAccessLogValve
/*      */   extends ValveBase
/*      */   implements AccessLog
/*      */ {
/*  147 */   private static final Log log = LogFactory.getLog(AbstractAccessLogValve.class);
/*      */   
/*      */ 
/*      */ 
/*      */   private static enum FormatType
/*      */   {
/*  153 */     CLF,  SEC,  MSEC,  MSEC_FRAC,  SDF;
/*      */     
/*      */     private FormatType() {}
/*      */   }
/*      */   
/*      */   private static enum PortType
/*      */   {
/*  160 */     LOCAL,  REMOTE;
/*      */     
/*      */     private PortType() {}
/*      */   }
/*      */   
/*  165 */   public AbstractAccessLogValve() { super(true); }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  174 */   protected boolean enabled = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  179 */   protected String pattern = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int globalCacheSize = 300;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int localCacheSize = 60;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static class DateFormatCache
/*      */   {
/*      */     protected class Cache
/*      */     {
/*      */       private static final String cLFFormat = "dd/MMM/yyyy:HH:mm:ss Z";
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  225 */       private long previousSeconds = Long.MIN_VALUE;
/*      */       
/*  227 */       private String previousFormat = "";
/*      */       
/*      */ 
/*  230 */       private long first = Long.MIN_VALUE;
/*      */       
/*  232 */       private long last = Long.MIN_VALUE;
/*      */       
/*  234 */       private int offset = 0;
/*      */       
/*  236 */       private final Date currentDate = new Date();
/*      */       
/*      */       protected final String[] cache;
/*      */       private SimpleDateFormat formatter;
/*  240 */       private boolean isCLF = false;
/*      */       
/*  242 */       private Cache parent = null;
/*      */       
/*      */       private Cache(Cache parent) {
/*  245 */         this(null, parent);
/*      */       }
/*      */       
/*      */       private Cache(String format, Cache parent) {
/*  249 */         this(format, null, parent);
/*      */       }
/*      */       
/*      */       private Cache(String format, Locale loc, Cache parent) {
/*  253 */         this.cache = new String[AbstractAccessLogValve.DateFormatCache.this.cacheSize];
/*  254 */         for (int i = 0; i < AbstractAccessLogValve.DateFormatCache.this.cacheSize; i++) {
/*  255 */           this.cache[i] = null;
/*      */         }
/*  257 */         if (loc == null) {
/*  258 */           loc = AbstractAccessLogValve.DateFormatCache.this.cacheDefaultLocale;
/*      */         }
/*  260 */         if (format == null) {
/*  261 */           this.isCLF = true;
/*  262 */           format = "dd/MMM/yyyy:HH:mm:ss Z";
/*  263 */           this.formatter = new SimpleDateFormat(format, Locale.US);
/*      */         } else {
/*  265 */           this.formatter = new SimpleDateFormat(format, loc);
/*      */         }
/*  267 */         this.formatter.setTimeZone(TimeZone.getDefault());
/*  268 */         this.parent = parent;
/*      */       }
/*      */       
/*      */       private String getFormatInternal(long time)
/*      */       {
/*  273 */         long seconds = time / 1000L;
/*      */         
/*      */ 
/*      */ 
/*  277 */         if (seconds == this.previousSeconds) {
/*  278 */           return this.previousFormat;
/*      */         }
/*      */         
/*      */ 
/*  282 */         this.previousSeconds = seconds;
/*  283 */         int index = (this.offset + (int)(seconds - this.first)) % AbstractAccessLogValve.DateFormatCache.this.cacheSize;
/*  284 */         if (index < 0) {
/*  285 */           index += AbstractAccessLogValve.DateFormatCache.this.cacheSize;
/*      */         }
/*  287 */         if ((seconds >= this.first) && (seconds <= this.last)) {
/*  288 */           if (this.cache[index] != null)
/*      */           {
/*  290 */             this.previousFormat = this.cache[index];
/*  291 */             return this.previousFormat;
/*      */           }
/*      */           
/*      */         }
/*  295 */         else if ((seconds >= this.last + AbstractAccessLogValve.DateFormatCache.this.cacheSize) || (seconds <= this.first - AbstractAccessLogValve.DateFormatCache.this.cacheSize)) {
/*  296 */           this.first = seconds;
/*  297 */           this.last = (this.first + AbstractAccessLogValve.DateFormatCache.this.cacheSize - 1L);
/*  298 */           index = 0;
/*  299 */           this.offset = 0;
/*  300 */           for (int i = 1; i < AbstractAccessLogValve.DateFormatCache.this.cacheSize; i++) {
/*  301 */             this.cache[i] = null;
/*      */           }
/*  303 */         } else if (seconds > this.last) {
/*  304 */           for (int i = 1; i < seconds - this.last; i++) {
/*  305 */             this.cache[((index + AbstractAccessLogValve.DateFormatCache.this.cacheSize - i) % AbstractAccessLogValve.DateFormatCache.this.cacheSize)] = null;
/*      */           }
/*  307 */           this.first = (seconds - (AbstractAccessLogValve.DateFormatCache.this.cacheSize - 1));
/*  308 */           this.last = seconds;
/*  309 */           this.offset = ((index + 1) % AbstractAccessLogValve.DateFormatCache.this.cacheSize);
/*  310 */         } else if (seconds < this.first) {
/*  311 */           for (int i = 1; i < this.first - seconds; i++) {
/*  312 */             this.cache[((index + i) % AbstractAccessLogValve.DateFormatCache.this.cacheSize)] = null;
/*      */           }
/*  314 */           this.first = seconds;
/*  315 */           this.last = (seconds + (AbstractAccessLogValve.DateFormatCache.this.cacheSize - 1));
/*  316 */           this.offset = index;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  321 */         if (this.parent != null) {
/*  322 */           synchronized (this.parent) {
/*  323 */             this.previousFormat = this.parent.getFormatInternal(time);
/*      */           }
/*      */         } else {
/*  326 */           this.currentDate.setTime(time);
/*  327 */           this.previousFormat = this.formatter.format(this.currentDate);
/*  328 */           if (this.isCLF) {
/*  329 */             StringBuilder current = new StringBuilder(32);
/*  330 */             current.append('[');
/*  331 */             current.append(this.previousFormat);
/*  332 */             current.append(']');
/*  333 */             this.previousFormat = current.toString();
/*      */           }
/*      */         }
/*  336 */         this.cache[index] = this.previousFormat;
/*  337 */         return this.previousFormat;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  342 */     private int cacheSize = 0;
/*      */     
/*      */     private final Locale cacheDefaultLocale;
/*      */     private final DateFormatCache parent;
/*      */     protected final Cache cLFCache;
/*  347 */     private final HashMap<String, Cache> formatCache = new HashMap();
/*      */     
/*      */     protected DateFormatCache(int size, Locale loc, DateFormatCache parent) {
/*  350 */       this.cacheSize = size;
/*  351 */       this.cacheDefaultLocale = loc;
/*  352 */       this.parent = parent;
/*  353 */       Cache parentCache = null;
/*  354 */       if (parent != null) {
/*  355 */         synchronized (parent) {
/*  356 */           parentCache = parent.getCache(null, null);
/*      */         }
/*      */       }
/*  359 */       this.cLFCache = new Cache(parentCache, null);
/*      */     }
/*      */     
/*      */     private Cache getCache(String format, Locale loc) { Cache cache;
/*      */       Cache cache;
/*  364 */       if (format == null) {
/*  365 */         cache = this.cLFCache;
/*      */       } else {
/*  367 */         cache = (Cache)this.formatCache.get(format);
/*  368 */         if (cache == null) {
/*  369 */           Cache parentCache = null;
/*  370 */           if (this.parent != null) {
/*  371 */             synchronized (this.parent) {
/*  372 */               parentCache = this.parent.getCache(format, loc);
/*      */             }
/*      */           }
/*  375 */           cache = new Cache(format, loc, parentCache, null);
/*  376 */           this.formatCache.put(format, cache);
/*      */         }
/*      */       }
/*  379 */       return cache;
/*      */     }
/*      */     
/*      */     public String getFormat(long time) {
/*  383 */       return this.cLFCache.getFormatInternal(time);
/*      */     }
/*      */     
/*      */     public String getFormat(String format, Locale loc, long time) {
/*  387 */       return getCache(format, loc).getFormatInternal(time);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  394 */   private static final DateFormatCache globalDateCache = new DateFormatCache(300, 
/*  395 */     Locale.getDefault(), null);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  400 */   private static final ThreadLocal<DateFormatCache> localDateCache = new ThreadLocal()
/*      */   {
/*      */     protected AbstractAccessLogValve.DateFormatCache initialValue()
/*      */     {
/*  404 */       return new AbstractAccessLogValve.DateFormatCache(60, Locale.getDefault(), AbstractAccessLogValve.globalDateCache);
/*      */     }
/*      */   };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  413 */   private static final ThreadLocal<Date> localDate = new ThreadLocal()
/*      */   {
/*      */     protected Date initialValue()
/*      */     {
/*  417 */       return new Date();
/*      */     }
/*      */   };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  425 */   protected String condition = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  431 */   protected String conditionIf = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  437 */   protected String localeName = Locale.getDefault().toString();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  444 */   protected Locale locale = Locale.getDefault();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  449 */   protected AccessLogElement[] logElements = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  457 */   protected boolean requestAttributesEnabled = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  463 */   private SynchronizedStack<CharArrayWriter> charArrayWriters = new SynchronizedStack();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  472 */   private int maxLogMessageBufferSize = 256;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  477 */   private boolean tlsAttributeRequired = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRequestAttributesEnabled(boolean requestAttributesEnabled)
/*      */   {
/*  488 */     this.requestAttributesEnabled = requestAttributesEnabled;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getRequestAttributesEnabled()
/*      */   {
/*  496 */     return this.requestAttributesEnabled;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getEnabled()
/*      */   {
/*  503 */     return this.enabled;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEnabled(boolean enabled)
/*      */   {
/*  511 */     this.enabled = enabled;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getPattern()
/*      */   {
/*  518 */     return this.pattern;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPattern(String pattern)
/*      */   {
/*  528 */     if (pattern == null) {
/*  529 */       this.pattern = "";
/*  530 */     } else if (pattern.equals("common")) {
/*  531 */       this.pattern = "%h %l %u %t \"%r\" %s %b";
/*  532 */     } else if (pattern.equals("combined")) {
/*  533 */       this.pattern = "%h %l %u %t \"%r\" %s %b \"%{Referer}i\" \"%{User-Agent}i\"";
/*      */     } else {
/*  535 */       this.pattern = pattern;
/*      */     }
/*  537 */     this.logElements = createLogElements();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getCondition()
/*      */   {
/*  547 */     return this.condition;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCondition(String condition)
/*      */   {
/*  558 */     this.condition = condition;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getConditionUnless()
/*      */   {
/*  569 */     return getCondition();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setConditionUnless(String condition)
/*      */   {
/*  580 */     setCondition(condition);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getConditionIf()
/*      */   {
/*  590 */     return this.conditionIf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setConditionIf(String condition)
/*      */   {
/*  601 */     this.conditionIf = condition;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getLocale()
/*      */   {
/*  610 */     return this.localeName;
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
/*      */   public void setLocale(String localeName)
/*      */   {
/*  623 */     this.localeName = localeName;
/*  624 */     this.locale = findLocale(localeName, this.locale);
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
/*      */   public void invoke(org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response)
/*      */     throws IOException, ServletException
/*      */   {
/*  642 */     if (this.tlsAttributeRequired)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  648 */       request.getAttribute("javax.servlet.request.X509Certificate");
/*      */     }
/*  650 */     getNext().invoke(request, response);
/*      */   }
/*      */   
/*      */ 
/*      */   public void log(org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */   {
/*  656 */     if ((getState().isAvailable()) && (getEnabled()) && (this.logElements != null) && ((this.condition == null) || 
/*      */     
/*  658 */       (null == request.getRequest().getAttribute(this.condition)))) { if (this.conditionIf != null)
/*      */       {
/*  660 */         if (null != request.getRequest().getAttribute(this.conditionIf)) {} }
/*  661 */     } else { return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  670 */     long start = request.getCoyoteRequest().getStartTime();
/*  671 */     Date date = getDate(start + time);
/*      */     
/*  673 */     CharArrayWriter result = (CharArrayWriter)this.charArrayWriters.pop();
/*  674 */     if (result == null) {
/*  675 */       result = new CharArrayWriter(128);
/*      */     }
/*      */     
/*  678 */     for (int i = 0; i < this.logElements.length; i++) {
/*  679 */       this.logElements[i].addElement(result, date, request, response, time);
/*      */     }
/*      */     
/*  682 */     log(result);
/*      */     
/*  684 */     if (result.size() <= this.maxLogMessageBufferSize) {
/*  685 */       result.reset();
/*  686 */       this.charArrayWriters.push(result);
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
/*      */   protected abstract void log(CharArrayWriter paramCharArrayWriter);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static Date getDate(long systime)
/*      */   {
/*  712 */     Date date = (Date)localDate.get();
/*  713 */     date.setTime(systime);
/*  714 */     return date;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static Locale findLocale(String name, Locale fallback)
/*      */   {
/*  725 */     if ((name == null) || (name.isEmpty())) {
/*  726 */       return Locale.getDefault();
/*      */     }
/*  728 */     for (Locale l : Locale.getAvailableLocales()) {
/*  729 */       if (name.equals(l.toString())) {
/*  730 */         return l;
/*      */       }
/*      */     }
/*      */     
/*  734 */     log.error(sm.getString("accessLogValve.invalidLocale", new Object[] { name }));
/*  735 */     return fallback;
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
/*      */   protected synchronized void startInternal()
/*      */     throws LifecycleException
/*      */   {
/*  749 */     setState(LifecycleState.STARTING);
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
/*      */   protected synchronized void stopInternal()
/*      */     throws LifecycleException
/*      */   {
/*  763 */     setState(LifecycleState.STOPPING);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static abstract interface AccessLogElement
/*      */   {
/*      */     public abstract void addElement(CharArrayWriter paramCharArrayWriter, Date paramDate, org.apache.catalina.connector.Request paramRequest, org.apache.catalina.connector.Response paramResponse, long paramLong);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static class ThreadNameElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/*  782 */       RequestInfo info = request.getCoyoteRequest().getRequestProcessor();
/*  783 */       if (info != null) {
/*  784 */         buf.append(info.getWorkerThreadName());
/*      */       } else {
/*  786 */         buf.append("-");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected static class LocalAddrElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     private static final String LOCAL_ADDR_VALUE;
/*      */     
/*      */     static
/*      */     {
/*      */       String init;
/*      */       try
/*      */       {
/*  801 */         init = InetAddress.getLocalHost().getHostAddress();
/*      */       } catch (Throwable e) { String init;
/*  803 */         ExceptionUtils.handleThrowable(e);
/*  804 */         init = "127.0.0.1";
/*      */       }
/*  806 */       LOCAL_ADDR_VALUE = init;
/*      */     }
/*      */     
/*      */ 
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/*  812 */       buf.append(LOCAL_ADDR_VALUE);
/*      */     }
/*      */   }
/*      */   
/*      */   protected class RemoteAddrElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     protected RemoteAddrElement() {}
/*      */     
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/*  823 */       if (AbstractAccessLogValve.this.requestAttributesEnabled) {
/*  824 */         Object addr = request.getAttribute("org.apache.catalina.AccessLog.RemoteAddr");
/*  825 */         if (addr == null) {
/*  826 */           buf.append(request.getRemoteAddr());
/*      */         } else {
/*  828 */           buf.append(addr.toString());
/*      */         }
/*      */       } else {
/*  831 */         buf.append(request.getRemoteAddr());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected class HostElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     protected HostElement() {}
/*      */     
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/*  843 */       String value = null;
/*  844 */       if (AbstractAccessLogValve.this.requestAttributesEnabled) {
/*  845 */         Object host = request.getAttribute("org.apache.catalina.AccessLog.RemoteHost");
/*  846 */         if (host != null) {
/*  847 */           value = host.toString();
/*      */         }
/*      */       }
/*  850 */       if ((value == null) || (value.length() == 0)) {
/*  851 */         value = request.getRemoteHost();
/*      */       }
/*  853 */       if ((value == null) || (value.length() == 0)) {
/*  854 */         value = "-";
/*      */       }
/*  856 */       buf.append(value);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static class LogicalUserNameElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/*  867 */       buf.append('-');
/*      */     }
/*      */   }
/*      */   
/*      */   protected class ProtocolElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     protected ProtocolElement() {}
/*      */     
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/*  878 */       if (AbstractAccessLogValve.this.requestAttributesEnabled) {
/*  879 */         Object proto = request.getAttribute("org.apache.catalina.AccessLog.Protocol");
/*  880 */         if (proto == null) {
/*  881 */           buf.append(request.getProtocol());
/*      */         } else {
/*  883 */           buf.append(proto.toString());
/*      */         }
/*      */       } else {
/*  886 */         buf.append(request.getProtocol());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static class UserElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/*  898 */       if (request != null) {
/*  899 */         String value = request.getRemoteUser();
/*  900 */         if (value != null) {
/*  901 */           buf.append(value);
/*      */         } else {
/*  903 */           buf.append('-');
/*      */         }
/*      */       } else {
/*  906 */         buf.append('-');
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected class DateAndTimeElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     private static final String requestStartPrefix = "begin";
/*      */     
/*      */ 
/*      */ 
/*      */     private static final String responseEndPrefix = "end";
/*      */     
/*      */ 
/*      */ 
/*      */     private static final String prefixSeparator = ":";
/*      */     
/*      */ 
/*      */ 
/*      */     private static final String secFormat = "sec";
/*      */     
/*      */ 
/*      */ 
/*      */     private static final String msecFormat = "msec";
/*      */     
/*      */ 
/*      */ 
/*      */     private static final String msecFractionFormat = "msec_frac";
/*      */     
/*      */ 
/*      */ 
/*      */     private static final String msecPattern = "{#}";
/*      */     
/*      */ 
/*      */ 
/*      */     private static final String trippleMsecPattern = "{#}{#}{#}";
/*      */     
/*      */ 
/*      */ 
/*      */     private final String format;
/*      */     
/*      */ 
/*      */ 
/*      */     private final boolean usesBegin;
/*      */     
/*      */ 
/*      */ 
/*      */     private final AbstractAccessLogValve.FormatType type;
/*      */     
/*      */ 
/*      */ 
/*  961 */     private boolean usesMsecs = false;
/*      */     
/*      */     protected DateAndTimeElement() {
/*  964 */       this(null);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private String tidyFormat(String format)
/*      */     {
/*  975 */       boolean escape = false;
/*  976 */       StringBuilder result = new StringBuilder();
/*  977 */       int len = format.length();
/*      */       
/*  979 */       for (int i = 0; i < len; i++) {
/*  980 */         char x = format.charAt(i);
/*  981 */         if ((escape) || (x != 'S')) {
/*  982 */           result.append(x);
/*      */         } else {
/*  984 */           result.append("{#}");
/*  985 */           this.usesMsecs = true;
/*      */         }
/*  987 */         if (x == '\'') {
/*  988 */           escape = !escape;
/*      */         }
/*      */       }
/*  991 */       return result.toString();
/*      */     }
/*      */     
/*      */     protected DateAndTimeElement(String header) {
/*  995 */       String format = header;
/*  996 */       boolean usesBegin = false;
/*  997 */       AbstractAccessLogValve.FormatType type = AbstractAccessLogValve.FormatType.CLF;
/*      */       
/*  999 */       if (format != null) {
/* 1000 */         if (format.equals("begin")) {
/* 1001 */           usesBegin = true;
/* 1002 */           format = "";
/* 1003 */         } else if (format.startsWith("begin:")) {
/* 1004 */           usesBegin = true;
/* 1005 */           format = format.substring(6);
/* 1006 */         } else if (format.equals("end")) {
/* 1007 */           usesBegin = false;
/* 1008 */           format = "";
/* 1009 */         } else if (format.startsWith("end:")) {
/* 1010 */           usesBegin = false;
/* 1011 */           format = format.substring(4);
/*      */         }
/* 1013 */         if (format.length() == 0) {
/* 1014 */           type = AbstractAccessLogValve.FormatType.CLF;
/* 1015 */         } else if (format.equals("sec")) {
/* 1016 */           type = AbstractAccessLogValve.FormatType.SEC;
/* 1017 */         } else if (format.equals("msec")) {
/* 1018 */           type = AbstractAccessLogValve.FormatType.MSEC;
/* 1019 */         } else if (format.equals("msec_frac")) {
/* 1020 */           type = AbstractAccessLogValve.FormatType.MSEC_FRAC;
/*      */         } else {
/* 1022 */           type = AbstractAccessLogValve.FormatType.SDF;
/* 1023 */           format = tidyFormat(format);
/*      */         }
/*      */       }
/* 1026 */       this.format = format;
/* 1027 */       this.usesBegin = usesBegin;
/* 1028 */       this.type = type;
/*      */     }
/*      */     
/*      */ 
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/* 1034 */       long timestamp = date.getTime();
/*      */       
/* 1036 */       if (this.usesBegin) {
/* 1037 */         timestamp -= time;
/*      */       }
/* 1039 */       switch (AbstractAccessLogValve.3.$SwitchMap$org$apache$catalina$valves$AbstractAccessLogValve$FormatType[this.type.ordinal()]) {
/*      */       case 1: 
/* 1041 */         buf.append(((AbstractAccessLogValve.DateFormatCache)AbstractAccessLogValve.localDateCache.get()).getFormat(timestamp));
/* 1042 */         break;
/*      */       case 2: 
/* 1044 */         buf.append(Long.toString(timestamp / 1000L));
/* 1045 */         break;
/*      */       case 3: 
/* 1047 */         buf.append(Long.toString(timestamp));
/* 1048 */         break;
/*      */       case 4: 
/* 1050 */         long frac = timestamp % 1000L;
/* 1051 */         if (frac < 100L) {
/* 1052 */           if (frac < 10L) {
/* 1053 */             buf.append('0');
/* 1054 */             buf.append('0');
/*      */           } else {
/* 1056 */             buf.append('0');
/*      */           }
/*      */         }
/* 1059 */         buf.append(Long.toString(frac));
/* 1060 */         break;
/*      */       case 5: 
/* 1062 */         String temp = ((AbstractAccessLogValve.DateFormatCache)AbstractAccessLogValve.localDateCache.get()).getFormat(this.format, AbstractAccessLogValve.this.locale, timestamp);
/* 1063 */         if (this.usesMsecs) {
/* 1064 */           long frac = timestamp % 1000L;
/* 1065 */           StringBuilder trippleMsec = new StringBuilder(4);
/* 1066 */           if (frac < 100L) {
/* 1067 */             if (frac < 10L) {
/* 1068 */               trippleMsec.append('0');
/* 1069 */               trippleMsec.append('0');
/*      */             } else {
/* 1071 */               trippleMsec.append('0');
/*      */             }
/*      */           }
/* 1074 */           trippleMsec.append(frac);
/* 1075 */           temp = temp.replace("{#}{#}{#}", trippleMsec);
/* 1076 */           temp = temp.replace("{#}", Long.toString(frac));
/*      */         }
/* 1078 */         buf.append(temp);
/*      */       }
/*      */       
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static class RequestElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/* 1091 */       if (request != null) {
/* 1092 */         String method = request.getMethod();
/* 1093 */         if (method == null)
/*      */         {
/* 1095 */           buf.append('-');
/*      */         } else {
/* 1097 */           buf.append(request.getMethod());
/* 1098 */           buf.append(' ');
/* 1099 */           buf.append(request.getRequestURI());
/* 1100 */           if (request.getQueryString() != null) {
/* 1101 */             buf.append('?');
/* 1102 */             buf.append(request.getQueryString());
/*      */           }
/* 1104 */           buf.append(' ');
/* 1105 */           buf.append(request.getProtocol());
/*      */         }
/*      */       } else {
/* 1108 */         buf.append('-');
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static class HttpStatusCodeElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/* 1120 */       if (response != null)
/*      */       {
/* 1122 */         int status = response.getStatus();
/* 1123 */         if ((100 <= status) && (status < 1000))
/*      */         {
/*      */ 
/* 1126 */           buf.append((char)(48 + status / 100)).append((char)(48 + status / 10 % 10)).append((char)(48 + status % 10));
/*      */         } else {
/* 1128 */           buf.append(Integer.toString(status));
/*      */         }
/*      */       } else {
/* 1131 */         buf.append('-');
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected class PortElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     private static final String localPort = "local";
/*      */     
/*      */     private static final String remotePort = "remote";
/*      */     
/*      */     private final AbstractAccessLogValve.PortType portType;
/*      */     
/*      */ 
/*      */     public PortElement()
/*      */     {
/* 1150 */       this.portType = AbstractAccessLogValve.PortType.LOCAL;
/*      */     }
/*      */     
/*      */     public PortElement(String type) {
/* 1154 */       switch (type) {
/*      */       case "remote": 
/* 1156 */         this.portType = AbstractAccessLogValve.PortType.REMOTE;
/* 1157 */         break;
/*      */       case "local": 
/* 1159 */         this.portType = AbstractAccessLogValve.PortType.LOCAL;
/* 1160 */         break;
/*      */       default: 
/* 1162 */         AbstractAccessLogValve.log.error(ValveBase.sm.getString("accessLogValve.invalidPortType", new Object[] { type }));
/* 1163 */         this.portType = AbstractAccessLogValve.PortType.LOCAL;
/*      */       }
/*      */       
/*      */     }
/*      */     
/*      */ 
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/* 1171 */       if ((AbstractAccessLogValve.this.requestAttributesEnabled) && (this.portType == AbstractAccessLogValve.PortType.LOCAL)) {
/* 1172 */         Object port = request.getAttribute("org.apache.catalina.AccessLog.ServerPort");
/* 1173 */         if (port == null) {
/* 1174 */           buf.append(Integer.toString(request.getServerPort()));
/*      */         } else {
/* 1176 */           buf.append(port.toString());
/*      */         }
/*      */       }
/* 1179 */       else if (this.portType == AbstractAccessLogValve.PortType.LOCAL) {
/* 1180 */         buf.append(Integer.toString(request.getServerPort()));
/*      */       } else {
/* 1182 */         buf.append(Integer.toString(request.getRemotePort()));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static class ByteSentElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     private final boolean conversion;
/*      */     
/*      */ 
/*      */ 
/*      */     public ByteSentElement(boolean conversion)
/*      */     {
/* 1198 */       this.conversion = conversion;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/* 1206 */       long length = response.getBytesWritten(false);
/* 1207 */       if (length <= 0L)
/*      */       {
/*      */ 
/* 1210 */         Object start = request.getAttribute("org.apache.tomcat.sendfile.start");
/*      */         
/* 1212 */         if ((start instanceof Long)) {
/* 1213 */           Object end = request.getAttribute("org.apache.tomcat.sendfile.end");
/*      */           
/* 1215 */           if ((end instanceof Long))
/*      */           {
/* 1217 */             length = ((Long)end).longValue() - ((Long)start).longValue();
/*      */           }
/*      */         }
/*      */       }
/* 1221 */       if ((length <= 0L) && (this.conversion)) {
/* 1222 */         buf.append('-');
/*      */       } else {
/* 1224 */         buf.append(Long.toString(length));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static class MethodElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/* 1236 */       if (request != null) {
/* 1237 */         buf.append(request.getMethod());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static class ElapsedTimeElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     private final boolean millis;
/*      */     
/*      */ 
/*      */ 
/*      */     public ElapsedTimeElement(boolean millis)
/*      */     {
/* 1253 */       this.millis = millis;
/*      */     }
/*      */     
/*      */ 
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/* 1259 */       if (this.millis) {
/* 1260 */         buf.append(Long.toString(time));
/*      */       }
/*      */       else {
/* 1263 */         buf.append(Long.toString(time / 1000L));
/* 1264 */         buf.append('.');
/* 1265 */         int remains = (int)(time % 1000L);
/* 1266 */         buf.append(Long.toString(remains / 100));
/* 1267 */         remains %= 100;
/* 1268 */         buf.append(Long.toString(remains / 10));
/* 1269 */         buf.append(Long.toString(remains % 10));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected static class FirstByteTimeElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/* 1280 */       long commitTime = response.getCoyoteResponse().getCommitTime();
/* 1281 */       if (commitTime == -1L) {
/* 1282 */         buf.append('-');
/*      */       } else {
/* 1284 */         long delta = commitTime - request.getCoyoteRequest().getStartTime();
/* 1285 */         buf.append(Long.toString(delta));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static class QueryElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/* 1297 */       String query = null;
/* 1298 */       if (request != null) {
/* 1299 */         query = request.getQueryString();
/*      */       }
/* 1301 */       if (query != null) {
/* 1302 */         buf.append('?');
/* 1303 */         buf.append(query);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static class SessionIdElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/* 1315 */       if (request == null) {
/* 1316 */         buf.append('-');
/*      */       } else {
/* 1318 */         Session session = request.getSessionInternal(false);
/* 1319 */         if (session == null) {
/* 1320 */           buf.append('-');
/*      */         } else {
/* 1322 */           buf.append(session.getIdInternal());
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static class RequestURIElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/* 1335 */       if (request != null) {
/* 1336 */         buf.append(request.getRequestURI());
/*      */       } else {
/* 1338 */         buf.append('-');
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static class LocalServerNameElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/* 1350 */       buf.append(request.getServerName());
/*      */     }
/*      */   }
/*      */   
/*      */   protected static class StringElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     private final String str;
/*      */     
/*      */     public StringElement(String str)
/*      */     {
/* 1361 */       this.str = str;
/*      */     }
/*      */     
/*      */ 
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/* 1367 */       buf.append(this.str);
/*      */     }
/*      */   }
/*      */   
/*      */   protected static class HeaderElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     private final String header;
/*      */     
/*      */     public HeaderElement(String header)
/*      */     {
/* 1378 */       this.header = header;
/*      */     }
/*      */     
/*      */ 
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/* 1384 */       Enumeration<String> iter = request.getHeaders(this.header);
/* 1385 */       if (iter.hasMoreElements()) {
/* 1386 */         buf.append((CharSequence)iter.nextElement());
/* 1387 */         while (iter.hasMoreElements()) {
/* 1388 */           buf.append(',').append((CharSequence)iter.nextElement());
/*      */         }
/* 1390 */         return;
/*      */       }
/* 1392 */       buf.append('-');
/*      */     }
/*      */   }
/*      */   
/*      */   protected static class CookieElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     private final String header;
/*      */     
/*      */     public CookieElement(String header)
/*      */     {
/* 1403 */       this.header = header;
/*      */     }
/*      */     
/*      */ 
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/* 1409 */       String value = "-";
/* 1410 */       Cookie[] c = request.getCookies();
/* 1411 */       if (c != null) {
/* 1412 */         for (int i = 0; i < c.length; i++) {
/* 1413 */           if (this.header.equals(c[i].getName())) {
/* 1414 */             value = c[i].getValue();
/* 1415 */             break;
/*      */           }
/*      */         }
/*      */       }
/* 1419 */       buf.append(value);
/*      */     }
/*      */   }
/*      */   
/*      */   protected static class ResponseHeaderElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     private final String header;
/*      */     
/*      */     public ResponseHeaderElement(String header)
/*      */     {
/* 1430 */       this.header = header;
/*      */     }
/*      */     
/*      */ 
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/* 1436 */       if (null != response) {
/* 1437 */         Iterator<String> iter = response.getHeaders(this.header).iterator();
/* 1438 */         if (iter.hasNext()) {
/* 1439 */           buf.append((CharSequence)iter.next());
/* 1440 */           while (iter.hasNext()) {
/* 1441 */             buf.append(',').append((CharSequence)iter.next());
/*      */           }
/* 1443 */           return;
/*      */         }
/*      */       }
/* 1446 */       buf.append('-');
/*      */     }
/*      */   }
/*      */   
/*      */   protected static class RequestAttributeElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     private final String header;
/*      */     
/*      */     public RequestAttributeElement(String header)
/*      */     {
/* 1457 */       this.header = header;
/*      */     }
/*      */     
/*      */ 
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/* 1463 */       Object value = null;
/* 1464 */       if (request != null) {
/* 1465 */         value = request.getAttribute(this.header);
/*      */       } else {
/* 1467 */         value = "??";
/*      */       }
/* 1469 */       if (value != null) {
/* 1470 */         if ((value instanceof String)) {
/* 1471 */           buf.append((String)value);
/*      */         } else {
/* 1473 */           buf.append(value.toString());
/*      */         }
/*      */       } else {
/* 1476 */         buf.append('-');
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected static class SessionAttributeElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     private final String header;
/*      */     
/*      */     public SessionAttributeElement(String header)
/*      */     {
/* 1488 */       this.header = header;
/*      */     }
/*      */     
/*      */ 
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/* 1494 */       Object value = null;
/* 1495 */       if (null != request) {
/* 1496 */         HttpSession sess = request.getSession(false);
/* 1497 */         if (null != sess) {
/* 1498 */           value = sess.getAttribute(this.header);
/*      */         }
/*      */       } else {
/* 1501 */         value = "??";
/*      */       }
/* 1503 */       if (value != null) {
/* 1504 */         if ((value instanceof String)) {
/* 1505 */           buf.append((String)value);
/*      */         } else {
/* 1507 */           buf.append(value.toString());
/*      */         }
/*      */       } else {
/* 1510 */         buf.append('-');
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected static class ConnectionStatusElement
/*      */     implements AbstractAccessLogValve.AccessLogElement
/*      */   {
/*      */     public void addElement(CharArrayWriter buf, Date date, org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response, long time)
/*      */     {
/* 1521 */       if ((response != null) && (request != null)) {
/* 1522 */         boolean statusFound = false;
/*      */         
/*      */ 
/* 1525 */         AtomicBoolean isIoAllowed = new AtomicBoolean(false);
/* 1526 */         request.getCoyoteRequest().action(ActionCode.IS_IO_ALLOWED, isIoAllowed);
/* 1527 */         if (!isIoAllowed.get()) {
/* 1528 */           buf.append('X');
/* 1529 */           statusFound = true;
/*      */ 
/*      */         }
/* 1532 */         else if (response.isError()) {
/* 1533 */           Throwable ex = (Throwable)request.getAttribute("javax.servlet.error.exception");
/* 1534 */           if ((ex instanceof ClientAbortException)) {
/* 1535 */             buf.append('X');
/* 1536 */             statusFound = true;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1542 */         if (!statusFound) {
/* 1543 */           String connStatus = response.getHeader("Connection");
/* 1544 */           if ("close".equalsIgnoreCase(connStatus)) {
/* 1545 */             buf.append('-');
/*      */           } else {
/* 1547 */             buf.append('+');
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/* 1552 */         buf.append('?');
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected AccessLogElement[] createLogElements()
/*      */   {
/* 1562 */     List<AccessLogElement> list = new ArrayList();
/* 1563 */     boolean replace = false;
/* 1564 */     StringBuilder buf = new StringBuilder();
/* 1565 */     for (int i = 0; i < this.pattern.length(); i++) {
/* 1566 */       char ch = this.pattern.charAt(i);
/* 1567 */       if (replace)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1572 */         if ('{' == ch) {
/* 1573 */           StringBuilder name = new StringBuilder();
/* 1574 */           for (int j = i + 1; 
/* 1575 */               (j < this.pattern.length()) && ('}' != this.pattern.charAt(j)); j++) {
/* 1576 */             name.append(this.pattern.charAt(j));
/*      */           }
/* 1578 */           if (j + 1 < this.pattern.length())
/*      */           {
/* 1580 */             j++;
/* 1581 */             list.add(createAccessLogElement(name.toString(), this.pattern
/* 1582 */               .charAt(j)));
/* 1583 */             i = j;
/*      */           }
/*      */           else
/*      */           {
/* 1587 */             list.add(createAccessLogElement(ch));
/*      */           }
/*      */         } else {
/* 1590 */           list.add(createAccessLogElement(ch));
/*      */         }
/* 1592 */         replace = false;
/* 1593 */       } else if (ch == '%') {
/* 1594 */         replace = true;
/* 1595 */         list.add(new StringElement(buf.toString()));
/* 1596 */         buf = new StringBuilder();
/*      */       } else {
/* 1598 */         buf.append(ch);
/*      */       }
/*      */     }
/* 1601 */     if (buf.length() > 0) {
/* 1602 */       list.add(new StringElement(buf.toString()));
/*      */     }
/* 1604 */     return (AccessLogElement[])list.toArray(new AccessLogElement[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected AccessLogElement createAccessLogElement(String name, char pattern)
/*      */   {
/* 1614 */     switch (pattern) {
/*      */     case 'i': 
/* 1616 */       return new HeaderElement(name);
/*      */     case 'c': 
/* 1618 */       return new CookieElement(name);
/*      */     case 'o': 
/* 1620 */       return new ResponseHeaderElement(name);
/*      */     case 'p': 
/* 1622 */       return new PortElement(name);
/*      */     case 'r': 
/* 1624 */       if (TLSUtil.isTLSRequestAttribute(name)) {
/* 1625 */         this.tlsAttributeRequired = true;
/*      */       }
/* 1627 */       return new RequestAttributeElement(name);
/*      */     case 's': 
/* 1629 */       return new SessionAttributeElement(name);
/*      */     case 't': 
/* 1631 */       return new DateAndTimeElement(name);
/*      */     }
/* 1633 */     return new StringElement("???");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected AccessLogElement createAccessLogElement(char pattern)
/*      */   {
/* 1643 */     switch (pattern) {
/*      */     case 'a': 
/* 1645 */       return new RemoteAddrElement();
/*      */     case 'A': 
/* 1647 */       return new LocalAddrElement();
/*      */     case 'b': 
/* 1649 */       return new ByteSentElement(true);
/*      */     case 'B': 
/* 1651 */       return new ByteSentElement(false);
/*      */     case 'D': 
/* 1653 */       return new ElapsedTimeElement(true);
/*      */     case 'F': 
/* 1655 */       return new FirstByteTimeElement();
/*      */     case 'h': 
/* 1657 */       return new HostElement();
/*      */     case 'H': 
/* 1659 */       return new ProtocolElement();
/*      */     case 'l': 
/* 1661 */       return new LogicalUserNameElement();
/*      */     case 'm': 
/* 1663 */       return new MethodElement();
/*      */     case 'p': 
/* 1665 */       return new PortElement();
/*      */     case 'q': 
/* 1667 */       return new QueryElement();
/*      */     case 'r': 
/* 1669 */       return new RequestElement();
/*      */     case 's': 
/* 1671 */       return new HttpStatusCodeElement();
/*      */     case 'S': 
/* 1673 */       return new SessionIdElement();
/*      */     case 't': 
/* 1675 */       return new DateAndTimeElement();
/*      */     case 'T': 
/* 1677 */       return new ElapsedTimeElement(false);
/*      */     case 'u': 
/* 1679 */       return new UserElement();
/*      */     case 'U': 
/* 1681 */       return new RequestURIElement();
/*      */     case 'v': 
/* 1683 */       return new LocalServerNameElement();
/*      */     case 'I': 
/* 1685 */       return new ThreadNameElement();
/*      */     case 'X': 
/* 1687 */       return new ConnectionStatusElement();
/*      */     }
/* 1689 */     return new StringElement("???" + pattern + "???");
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\AbstractAccessLogValve.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */