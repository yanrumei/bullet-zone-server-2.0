/*      */ package org.apache.tomcat.util.net;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.net.InetAddress;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.NetworkInterface;
/*      */ import java.net.Socket;
/*      */ import java.net.SocketException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.RejectedExecutionException;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import javax.management.MalformedObjectNameException;
/*      */ import javax.management.ObjectName;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.IntrospectionUtils;
/*      */ import org.apache.tomcat.util.collections.SynchronizedStack;
/*      */ import org.apache.tomcat.util.modeler.Registry;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ import org.apache.tomcat.util.threads.LimitLatch;
/*      */ import org.apache.tomcat.util.threads.ResizableExecutor;
/*      */ import org.apache.tomcat.util.threads.TaskQueue;
/*      */ import org.apache.tomcat.util.threads.TaskThreadFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractEndpoint<S>
/*      */ {
/*   62 */   protected static final StringManager sm = StringManager.getManager(AbstractEndpoint.class);
/*      */   private static final int INITIAL_ERROR_DELAY = 50;
/*      */   private static final int MAX_ERROR_DELAY = 1600;
/*      */   
/*      */   public static abstract interface Handler<S> { public abstract SocketState process(SocketWrapperBase<S> paramSocketWrapperBase, SocketEvent paramSocketEvent);
/*      */     
/*      */     public abstract Object getGlobal();
/*      */     
/*      */     public abstract Set<S> getOpenSockets();
/*      */     
/*   72 */     public static enum SocketState { OPEN,  CLOSED,  LONG,  ASYNC_END,  SENDFILE,  UPGRADING,  UPGRADED,  SUSPENDED;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       private SocketState() {}
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
/*      */     public abstract void release(SocketWrapperBase<S> paramSocketWrapperBase);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public abstract void pause();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public abstract void recycle();
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
/*      */   protected static enum BindState
/*      */   {
/*  128 */     UNBOUND,  BOUND_ON_INIT,  BOUND_ON_START;
/*      */     
/*      */     private BindState() {}
/*      */   }
/*      */   
/*  133 */   public static abstract class Acceptor implements Runnable { public static enum AcceptorState { NEW,  RUNNING,  PAUSED,  ENDED;
/*      */       
/*      */       private AcceptorState() {} }
/*  136 */     protected volatile AcceptorState state = AcceptorState.NEW;
/*      */     
/*  138 */     public final AcceptorState getState() { return this.state; }
/*      */     
/*      */ 
/*      */     protected final void setThreadName(String threadName)
/*      */     {
/*  143 */       this.threadName = threadName;
/*      */     }
/*      */     
/*  146 */     protected final String getThreadName() { return this.threadName; }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private String threadName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  160 */   protected volatile boolean running = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  166 */   protected volatile boolean paused = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  171 */   protected volatile boolean internalExecutor = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  177 */   private volatile LimitLatch connectionLimitLatch = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  182 */   protected SocketProperties socketProperties = new SocketProperties();
/*      */   
/*  184 */   public SocketProperties getSocketProperties() { return this.socketProperties; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Acceptor[] acceptors;
/*      */   
/*      */ 
/*      */ 
/*      */   protected SynchronizedStack<SocketProcessorBase<S>> processorCache;
/*      */   
/*      */ 
/*      */ 
/*  197 */   private ObjectName oname = null;
/*      */   
/*      */ 
/*      */ 
/*  201 */   private String defaultSSLHostConfigName = "_default_";
/*      */   
/*  203 */   public String getDefaultSSLHostConfigName() { return this.defaultSSLHostConfigName; }
/*      */   
/*      */   public void setDefaultSSLHostConfigName(String defaultSSLHostConfigName) {
/*  206 */     this.defaultSSLHostConfigName = defaultSSLHostConfigName;
/*      */   }
/*      */   
/*      */ 
/*  210 */   protected ConcurrentMap<String, SSLHostConfig> sslHostConfigs = new ConcurrentHashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addSslHostConfig(SSLHostConfig sslHostConfig)
/*      */     throws IllegalArgumentException
/*      */   {
/*  221 */     addSslHostConfig(sslHostConfig, false);
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
/*      */   public void addSslHostConfig(SSLHostConfig sslHostConfig, boolean replace)
/*      */     throws IllegalArgumentException
/*      */   {
/*  238 */     String key = sslHostConfig.getHostName();
/*  239 */     if ((key == null) || (key.length() == 0)) {
/*  240 */       throw new IllegalArgumentException(sm.getString("endpoint.noSslHostName"));
/*      */     }
/*  242 */     if ((this.bindState != BindState.UNBOUND) && (isSSLEnabled())) {
/*  243 */       sslHostConfig.setConfigType(getSslConfigType());
/*      */       try {
/*  245 */         createSSLContext(sslHostConfig);
/*      */       } catch (Exception e) {
/*  247 */         throw new IllegalArgumentException(e);
/*      */       }
/*      */     }
/*  250 */     if (replace) {
/*  251 */       SSLHostConfig previous = (SSLHostConfig)this.sslHostConfigs.put(key, sslHostConfig);
/*  252 */       if (previous != null) {
/*  253 */         unregisterJmx(sslHostConfig);
/*      */       }
/*  255 */       registerJmx(sslHostConfig);
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*  262 */       SSLHostConfig duplicate = (SSLHostConfig)this.sslHostConfigs.putIfAbsent(key, sslHostConfig);
/*  263 */       if (duplicate != null) {
/*  264 */         releaseSSLContext(sslHostConfig);
/*  265 */         throw new IllegalArgumentException(sm.getString("endpoint.duplicateSslHostName", new Object[] { key }));
/*      */       }
/*  267 */       registerJmx(sslHostConfig);
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
/*      */   public SSLHostConfig removeSslHostConfig(String hostName)
/*      */   {
/*  280 */     if (hostName == null) {
/*  281 */       return null;
/*      */     }
/*      */     
/*  284 */     if (hostName.equalsIgnoreCase(getDefaultSSLHostConfigName()))
/*      */     {
/*  286 */       throw new IllegalArgumentException(sm.getString("endpoint.removeDefaultSslHostConfig", new Object[] { hostName }));
/*      */     }
/*  288 */     SSLHostConfig sslHostConfig = (SSLHostConfig)this.sslHostConfigs.remove(hostName);
/*  289 */     unregisterJmx(sslHostConfig);
/*  290 */     return sslHostConfig;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void reloadSslHostConfig(String hostName)
/*      */   {
/*  301 */     SSLHostConfig sslHostConfig = (SSLHostConfig)this.sslHostConfigs.get(hostName);
/*  302 */     if (sslHostConfig == null)
/*      */     {
/*  304 */       throw new IllegalArgumentException(sm.getString("endpoint.unknownSslHostName", new Object[] { hostName }));
/*      */     }
/*  306 */     addSslHostConfig(sslHostConfig, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void reloadSslHostConfigs()
/*      */   {
/*  314 */     for (String hostName : this.sslHostConfigs.keySet())
/*  315 */       reloadSslHostConfig(hostName);
/*      */   }
/*      */   
/*      */   public SSLHostConfig[] findSslHostConfigs() {
/*  319 */     return (SSLHostConfig[])this.sslHostConfigs.values().toArray(new SSLHostConfig[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract SSLHostConfig.Type getSslConfigType();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract void createSSLContext(SSLHostConfig paramSSLHostConfig)
/*      */     throws Exception;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract void releaseSSLContext(SSLHostConfig paramSSLHostConfig);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SSLHostConfig getSSLHostConfig(String sniHostName)
/*      */   {
/*  343 */     SSLHostConfig result = null;
/*      */     
/*  345 */     if (sniHostName != null)
/*      */     {
/*  347 */       result = (SSLHostConfig)this.sslHostConfigs.get(sniHostName);
/*  348 */       if (result != null) {
/*  349 */         return result;
/*      */       }
/*      */       
/*  352 */       int indexOfDot = sniHostName.indexOf('.');
/*  353 */       if (indexOfDot > -1) {
/*  354 */         result = (SSLHostConfig)this.sslHostConfigs.get("*" + sniHostName.substring(indexOfDot));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  359 */     if (result == null) {
/*  360 */       result = (SSLHostConfig)this.sslHostConfigs.get(getDefaultSSLHostConfigName());
/*      */     }
/*  362 */     if (result == null)
/*      */     {
/*  364 */       throw new IllegalStateException();
/*      */     }
/*  366 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  373 */   private boolean useSendfile = true;
/*      */   
/*  375 */   public boolean getUseSendfile() { return this.useSendfile; }
/*      */   
/*      */   public void setUseSendfile(boolean useSendfile) {
/*  378 */     this.useSendfile = useSendfile;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  386 */   private long executorTerminationTimeoutMillis = 5000L;
/*      */   
/*      */   public long getExecutorTerminationTimeoutMillis() {
/*  389 */     return this.executorTerminationTimeoutMillis;
/*      */   }
/*      */   
/*      */   public void setExecutorTerminationTimeoutMillis(long executorTerminationTimeoutMillis)
/*      */   {
/*  394 */     this.executorTerminationTimeoutMillis = executorTerminationTimeoutMillis;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  401 */   protected int acceptorThreadCount = 1;
/*      */   
/*      */ 
/*  404 */   public void setAcceptorThreadCount(int acceptorThreadCount) { this.acceptorThreadCount = acceptorThreadCount; }
/*      */   
/*  406 */   public int getAcceptorThreadCount() { return this.acceptorThreadCount; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  412 */   protected int acceptorThreadPriority = 5;
/*      */   
/*  414 */   public void setAcceptorThreadPriority(int acceptorThreadPriority) { this.acceptorThreadPriority = acceptorThreadPriority; }
/*      */   
/*  416 */   public int getAcceptorThreadPriority() { return this.acceptorThreadPriority; }
/*      */   
/*      */ 
/*  419 */   private int maxConnections = 10000;
/*      */   
/*  421 */   public void setMaxConnections(int maxCon) { this.maxConnections = maxCon;
/*  422 */     LimitLatch latch = this.connectionLimitLatch;
/*  423 */     if (latch != null)
/*      */     {
/*  425 */       if (maxCon == -1) {
/*  426 */         releaseConnectionLatch();
/*      */       } else {
/*  428 */         latch.setLimit(maxCon);
/*      */       }
/*  430 */     } else if (maxCon > 0)
/*  431 */       initializeConnectionLatch();
/*      */   }
/*      */   
/*      */   public int getMaxConnections() {
/*  435 */     return this.maxConnections;
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
/*      */   public long getConnectionCount()
/*      */   {
/*  452 */     LimitLatch latch = this.connectionLimitLatch;
/*  453 */     if (latch != null) {
/*  454 */       return latch.getCount();
/*      */     }
/*  456 */     return -1L;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  462 */   private Executor executor = null;
/*      */   
/*  464 */   public void setExecutor(Executor executor) { this.executor = executor;
/*  465 */     this.internalExecutor = (executor == null); }
/*      */   
/*  467 */   public Executor getExecutor() { return this.executor; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  474 */   public int getPort() { return this.port; }
/*  475 */   public void setPort(int port) { this.port = port; }
/*      */   
/*      */   public final int getLocalPort()
/*      */   {
/*      */     try {
/*  480 */       InetSocketAddress localAddress = getLocalAddress();
/*  481 */       if (localAddress == null) {
/*  482 */         return -1;
/*      */       }
/*  484 */       return localAddress.getPort();
/*      */     } catch (IOException ioe) {}
/*  486 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  495 */   public InetAddress getAddress() { return this.address; }
/*  496 */   public void setAddress(InetAddress address) { this.address = address; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int port;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private InetAddress address;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract InetSocketAddress getLocalAddress()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  519 */   private int acceptCount = 100;
/*  520 */   public void setAcceptCount(int acceptCount) { if (acceptCount > 0) this.acceptCount = acceptCount; }
/*  521 */   public int getAcceptCount() { return this.acceptCount; }
/*      */   @Deprecated
/*  523 */   public void setBacklog(int backlog) { setAcceptCount(backlog); }
/*      */   @Deprecated
/*  525 */   public int getBacklog() { return getAcceptCount(); }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  533 */   private boolean bindOnInit = true;
/*  534 */   public boolean getBindOnInit() { return this.bindOnInit; }
/*  535 */   public void setBindOnInit(boolean b) { this.bindOnInit = b; }
/*  536 */   private volatile BindState bindState = BindState.UNBOUND;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  541 */   private Integer keepAliveTimeout = null;
/*      */   
/*  543 */   public int getKeepAliveTimeout() { if (this.keepAliveTimeout == null) {
/*  544 */       return getSoTimeout();
/*      */     }
/*  546 */     return this.keepAliveTimeout.intValue();
/*      */   }
/*      */   
/*      */   public void setKeepAliveTimeout(int keepAliveTimeout) {
/*  550 */     this.keepAliveTimeout = Integer.valueOf(keepAliveTimeout);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  560 */   public boolean getTcpNoDelay() { return this.socketProperties.getTcpNoDelay(); }
/*  561 */   public void setTcpNoDelay(boolean tcpNoDelay) { this.socketProperties.setTcpNoDelay(tcpNoDelay); }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  570 */   public int getConnectionLinger() { return this.socketProperties.getSoLingerTime(); }
/*      */   
/*  572 */   public void setConnectionLinger(int connectionLinger) { this.socketProperties.setSoLingerTime(connectionLinger);
/*  573 */     this.socketProperties.setSoLingerOn(connectionLinger >= 0); }
/*      */   
/*      */   @Deprecated
/*  576 */   public int getSoLinger() { return getConnectionLinger(); }
/*      */   @Deprecated
/*  578 */   public void setSoLinger(int soLinger) { setConnectionLinger(soLinger); }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  586 */   public int getConnectionTimeout() { return this.socketProperties.getSoTimeout(); }
/*  587 */   public void setConnectionTimeout(int soTimeout) { this.socketProperties.setSoTimeout(soTimeout); }
/*      */   @Deprecated
/*  589 */   public int getSoTimeout() { return getConnectionTimeout(); }
/*      */   @Deprecated
/*  591 */   public void setSoTimeout(int soTimeout) { setConnectionTimeout(soTimeout); }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  596 */   private boolean SSLEnabled = false;
/*  597 */   public boolean isSSLEnabled() { return this.SSLEnabled; }
/*  598 */   public void setSSLEnabled(boolean SSLEnabled) { this.SSLEnabled = SSLEnabled; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract boolean isAlpnSupported();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  610 */   private int minSpareThreads = 10;
/*      */   
/*  612 */   public void setMinSpareThreads(int minSpareThreads) { this.minSpareThreads = minSpareThreads;
/*  613 */     Executor executor = this.executor;
/*  614 */     if ((this.internalExecutor) && ((executor instanceof java.util.concurrent.ThreadPoolExecutor)))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  619 */       ((java.util.concurrent.ThreadPoolExecutor)executor).setCorePoolSize(minSpareThreads);
/*      */     }
/*      */   }
/*      */   
/*  623 */   public int getMinSpareThreads() { return Math.min(getMinSpareThreadsInternal(), getMaxThreads()); }
/*      */   
/*      */   private int getMinSpareThreadsInternal() {
/*  626 */     if (this.internalExecutor) {
/*  627 */       return this.minSpareThreads;
/*      */     }
/*  629 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  637 */   private int maxThreads = 200;
/*      */   
/*  639 */   public void setMaxThreads(int maxThreads) { this.maxThreads = maxThreads;
/*  640 */     Executor executor = this.executor;
/*  641 */     if ((this.internalExecutor) && ((executor instanceof java.util.concurrent.ThreadPoolExecutor)))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  646 */       ((java.util.concurrent.ThreadPoolExecutor)executor).setMaximumPoolSize(maxThreads); }
/*      */   }
/*      */   
/*      */   public int getMaxThreads() {
/*  650 */     if (this.internalExecutor) {
/*  651 */       return this.maxThreads;
/*      */     }
/*  653 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  661 */   protected int threadPriority = 5;
/*      */   
/*      */ 
/*  664 */   public void setThreadPriority(int threadPriority) { this.threadPriority = threadPriority; }
/*      */   
/*      */   public int getThreadPriority() {
/*  667 */     if (this.internalExecutor) {
/*  668 */       return this.threadPriority;
/*      */     }
/*  670 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  678 */   private int maxKeepAliveRequests = 100;
/*      */   
/*  680 */   public int getMaxKeepAliveRequests() { return this.maxKeepAliveRequests; }
/*      */   
/*      */   public void setMaxKeepAliveRequests(int maxKeepAliveRequests) {
/*  683 */     this.maxKeepAliveRequests = maxKeepAliveRequests;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  690 */   private int maxHeaderCount = 100;
/*      */   
/*  692 */   public int getMaxHeaderCount() { return this.maxHeaderCount; }
/*      */   
/*      */   public void setMaxHeaderCount(int maxHeaderCount) {
/*  695 */     this.maxHeaderCount = maxHeaderCount;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  701 */   private String name = "TP";
/*  702 */   public void setName(String name) { this.name = name; }
/*  703 */   public String getName() { return this.name; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  710 */   public void setDomain(String domain) { this.domain = domain; }
/*  711 */   public String getDomain() { return this.domain; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private String domain;
/*      */   
/*      */ 
/*  719 */   private boolean daemon = true;
/*  720 */   public void setDaemon(boolean b) { this.daemon = b; }
/*  721 */   public boolean getDaemon() { return this.daemon; }
/*      */   
/*      */ 
/*      */   protected abstract boolean getDeferAccept();
/*      */   
/*      */ 
/*  727 */   protected final List<String> negotiableProtocols = new ArrayList();
/*      */   
/*  729 */   public void addNegotiatedProtocol(String negotiableProtocol) { this.negotiableProtocols.add(negotiableProtocol); }
/*      */   
/*      */   public boolean hasNegotiableProtocols() {
/*  732 */     return this.negotiableProtocols.size() > 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  739 */   private Handler<S> handler = null;
/*  740 */   public void setHandler(Handler<S> handler) { this.handler = handler; }
/*  741 */   public Handler<S> getHandler() { return this.handler; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  749 */   protected HashMap<String, Object> attributes = new HashMap();
/*      */   
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
/*  762 */     if (getLog().isTraceEnabled()) {
/*  763 */       getLog().trace(sm.getString("endpoint.setAttribute", new Object[] { name, value }));
/*      */     }
/*  765 */     this.attributes.put(name, value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getAttribute(String key)
/*      */   {
/*  776 */     Object value = this.attributes.get(key);
/*  777 */     if (getLog().isTraceEnabled()) {
/*  778 */       getLog().trace(sm.getString("endpoint.getAttribute", new Object[] { key, value }));
/*      */     }
/*  780 */     return value;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean setProperty(String name, String value)
/*      */   {
/*  786 */     setAttribute(name, value);
/*  787 */     String socketName = "socket.";
/*      */     try {
/*  789 */       if (name.startsWith("socket.")) {
/*  790 */         return IntrospectionUtils.setProperty(this.socketProperties, name.substring("socket.".length()), value);
/*      */       }
/*  792 */       return IntrospectionUtils.setProperty(this, name, value, false);
/*      */     }
/*      */     catch (Exception x) {
/*  795 */       getLog().error("Unable to set attribute \"" + name + "\" to \"" + value + "\"", x); }
/*  796 */     return false;
/*      */   }
/*      */   
/*      */   public String getProperty(String name) {
/*  800 */     String value = (String)getAttribute(name);
/*  801 */     String socketName = "socket.";
/*  802 */     if ((value == null) && (name.startsWith("socket."))) {
/*  803 */       Object result = IntrospectionUtils.getProperty(this.socketProperties, name.substring("socket.".length()));
/*  804 */       if (result != null) {
/*  805 */         value = result.toString();
/*      */       }
/*      */     }
/*  808 */     return value;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getCurrentThreadCount()
/*      */   {
/*  817 */     Executor executor = this.executor;
/*  818 */     if (executor != null) {
/*  819 */       if ((executor instanceof org.apache.tomcat.util.threads.ThreadPoolExecutor))
/*  820 */         return ((org.apache.tomcat.util.threads.ThreadPoolExecutor)executor).getPoolSize();
/*  821 */       if ((executor instanceof ResizableExecutor)) {
/*  822 */         return ((ResizableExecutor)executor).getPoolSize();
/*      */       }
/*  824 */       return -1;
/*      */     }
/*      */     
/*  827 */     return -2;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getCurrentThreadsBusy()
/*      */   {
/*  837 */     Executor executor = this.executor;
/*  838 */     if (executor != null) {
/*  839 */       if ((executor instanceof org.apache.tomcat.util.threads.ThreadPoolExecutor))
/*  840 */         return ((org.apache.tomcat.util.threads.ThreadPoolExecutor)executor).getActiveCount();
/*  841 */       if ((executor instanceof ResizableExecutor)) {
/*  842 */         return ((ResizableExecutor)executor).getActiveCount();
/*      */       }
/*  844 */       return -1;
/*      */     }
/*      */     
/*  847 */     return -2;
/*      */   }
/*      */   
/*      */   public boolean isRunning()
/*      */   {
/*  852 */     return this.running;
/*      */   }
/*      */   
/*      */   public boolean isPaused() {
/*  856 */     return this.paused;
/*      */   }
/*      */   
/*      */   public void createExecutor()
/*      */   {
/*  861 */     this.internalExecutor = true;
/*  862 */     TaskQueue taskqueue = new TaskQueue();
/*  863 */     TaskThreadFactory tf = new TaskThreadFactory(getName() + "-exec-", this.daemon, getThreadPriority());
/*  864 */     this.executor = new org.apache.tomcat.util.threads.ThreadPoolExecutor(getMinSpareThreads(), getMaxThreads(), 60L, TimeUnit.SECONDS, taskqueue, tf);
/*  865 */     taskqueue.setParent((org.apache.tomcat.util.threads.ThreadPoolExecutor)this.executor);
/*      */   }
/*      */   
/*      */   public void shutdownExecutor() {
/*  869 */     Executor executor = this.executor;
/*  870 */     if ((executor != null) && (this.internalExecutor)) {
/*  871 */       this.executor = null;
/*  872 */       if ((executor instanceof org.apache.tomcat.util.threads.ThreadPoolExecutor))
/*      */       {
/*  874 */         org.apache.tomcat.util.threads.ThreadPoolExecutor tpe = (org.apache.tomcat.util.threads.ThreadPoolExecutor)executor;
/*  875 */         tpe.shutdownNow();
/*  876 */         long timeout = getExecutorTerminationTimeoutMillis();
/*  877 */         if (timeout > 0L) {
/*      */           try {
/*  879 */             tpe.awaitTermination(timeout, TimeUnit.MILLISECONDS);
/*      */           }
/*      */           catch (InterruptedException localInterruptedException) {}
/*      */           
/*  883 */           if (tpe.isTerminating()) {
/*  884 */             getLog().warn(sm.getString("endpoint.warn.executorShutdown", new Object[] { getName() }));
/*      */           }
/*      */         }
/*  887 */         TaskQueue queue = (TaskQueue)tpe.getQueue();
/*  888 */         queue.setParent(null);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void unlockAccept()
/*      */   {
/*  898 */     int unlocksRequired = 0;
/*  899 */     for (Acceptor acceptor : this.acceptors) {
/*  900 */       if (acceptor.getState() == AbstractEndpoint.Acceptor.AcceptorState.RUNNING) {
/*  901 */         unlocksRequired++;
/*      */       }
/*      */     }
/*  904 */     if (unlocksRequired == 0) {
/*  905 */       return;
/*      */     }
/*      */     
/*  908 */     InetSocketAddress unlockAddress = null;
/*  909 */     InetSocketAddress localAddress = null;
/*      */     try {
/*  911 */       localAddress = getLocalAddress();
/*      */     } catch (IOException ioe) {
/*  913 */       getLog().debug(sm.getString("endpoint.debug.unlock.localFail", new Object[] { getName() }), ioe);
/*      */     }
/*  915 */     if (localAddress == null) {
/*  916 */       getLog().warn(sm.getString("endpoint.debug.unlock.localNone", new Object[] { getName() }));
/*  917 */       return;
/*      */     }
/*      */     try
/*      */     {
/*  921 */       unlockAddress = getUnlockAddress(localAddress);
/*      */       Object localObject1;
/*  923 */       int utmo; for (int i = 0; i < unlocksRequired; i++) {
/*  924 */         Socket s = new Socket();localObject1 = null;
/*  925 */         try { int stmo = 2000;
/*  926 */           utmo = 2000;
/*  927 */           if (getSocketProperties().getSoTimeout() > stmo)
/*  928 */             stmo = getSocketProperties().getSoTimeout();
/*  929 */           if (getSocketProperties().getUnlockTimeout() > utmo)
/*  930 */             utmo = getSocketProperties().getUnlockTimeout();
/*  931 */           s.setSoTimeout(stmo);
/*  932 */           s.setSoLinger(getSocketProperties().getSoLingerOn(), getSocketProperties().getSoLingerTime());
/*  933 */           if (getLog().isDebugEnabled()) {
/*  934 */             getLog().debug("About to unlock socket for:" + unlockAddress);
/*      */           }
/*  936 */           s.connect(unlockAddress, utmo);
/*  937 */           if (getDeferAccept())
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  945 */             OutputStreamWriter sw = new OutputStreamWriter(s.getOutputStream(), "ISO-8859-1");
/*  946 */             sw.write("OPTIONS * HTTP/1.0\r\nUser-Agent: Tomcat wakeup connection\r\n\r\n");
/*      */             
/*  948 */             sw.flush();
/*      */           }
/*  950 */           if (getLog().isDebugEnabled()) {
/*  951 */             getLog().debug("Socket unlock completed for:" + unlockAddress);
/*      */           }
/*      */         }
/*      */         catch (Throwable localThrowable2)
/*      */         {
/*  924 */           localObject1 = localThrowable2;throw localThrowable2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         }
/*      */         finally
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  953 */           if (s != null) if (localObject1 != null) try { s.close(); } catch (Throwable localThrowable3) { ((Throwable)localObject1).addSuppressed(localThrowable3); } else s.close();
/*      */         }
/*      */       }
/*  956 */       long waitLeft = 1000L;
/*  957 */       for (Acceptor acceptor : this.acceptors) {
/*  958 */         while ((waitLeft > 0L) && 
/*  959 */           (acceptor.getState() == AbstractEndpoint.Acceptor.AcceptorState.RUNNING)) {
/*  960 */           Thread.sleep(50L);
/*  961 */           waitLeft -= 50L;
/*      */         }
/*      */       }
/*      */     } catch (Throwable t) {
/*  965 */       ExceptionUtils.handleThrowable(t);
/*  966 */       if (getLog().isDebugEnabled()) {
/*  967 */         getLog().debug(sm.getString("endpoint.debug.unlock.fail", new Object[] { "" + getPort() }), t);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static InetSocketAddress getUnlockAddress(InetSocketAddress localAddress) throws SocketException
/*      */   {
/*  974 */     if (localAddress.getAddress().isAnyLocalAddress())
/*      */     {
/*      */ 
/*      */ 
/*  978 */       InetAddress loopbackUnlockAddress = null;
/*  979 */       InetAddress linkLocalUnlockAddress = null;
/*      */       
/*  981 */       Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
/*  982 */       while (networkInterfaces.hasMoreElements()) {
/*  983 */         NetworkInterface networkInterface = (NetworkInterface)networkInterfaces.nextElement();
/*  984 */         Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
/*  985 */         while (inetAddresses.hasMoreElements()) {
/*  986 */           InetAddress inetAddress = (InetAddress)inetAddresses.nextElement();
/*  987 */           if (localAddress.getAddress().getClass().isAssignableFrom(inetAddress.getClass())) {
/*  988 */             if (inetAddress.isLoopbackAddress()) {
/*  989 */               if (loopbackUnlockAddress == null) {
/*  990 */                 loopbackUnlockAddress = inetAddress;
/*      */               }
/*  992 */             } else if (inetAddress.isLinkLocalAddress()) {
/*  993 */               if (linkLocalUnlockAddress == null) {
/*  994 */                 linkLocalUnlockAddress = inetAddress;
/*      */               }
/*      */             }
/*      */             else {
/*  998 */               return new InetSocketAddress(inetAddress, localAddress.getPort());
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1006 */       if (loopbackUnlockAddress != null) {
/* 1007 */         return new InetSocketAddress(loopbackUnlockAddress, localAddress.getPort());
/*      */       }
/* 1009 */       if (linkLocalUnlockAddress != null) {
/* 1010 */         return new InetSocketAddress(linkLocalUnlockAddress, localAddress.getPort());
/*      */       }
/*      */       
/* 1013 */       return new InetSocketAddress("localhost", localAddress.getPort());
/*      */     }
/* 1015 */     return localAddress;
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
/*      */   public boolean processSocket(SocketWrapperBase<S> socketWrapper, SocketEvent event, boolean dispatch)
/*      */   {
/*      */     try
/*      */     {
/* 1037 */       if (socketWrapper == null) {
/* 1038 */         return false;
/*      */       }
/* 1040 */       SocketProcessorBase<S> sc = (SocketProcessorBase)this.processorCache.pop();
/* 1041 */       if (sc == null) {
/* 1042 */         sc = createSocketProcessor(socketWrapper, event);
/*      */       } else {
/* 1044 */         sc.reset(socketWrapper, event);
/*      */       }
/* 1046 */       Executor executor = getExecutor();
/* 1047 */       if ((dispatch) && (executor != null)) {
/* 1048 */         executor.execute(sc);
/*      */       } else {
/* 1050 */         sc.run();
/*      */       }
/*      */     } catch (RejectedExecutionException ree) {
/* 1053 */       getLog().warn(sm.getString("endpoint.executor.fail", new Object[] { socketWrapper }), ree);
/* 1054 */       return false;
/*      */     } catch (Throwable t) {
/* 1056 */       ExceptionUtils.handleThrowable(t);
/*      */       
/*      */ 
/* 1059 */       getLog().error(sm.getString("endpoint.process.fail"), t);
/* 1060 */       return false;
/*      */     }
/* 1062 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   protected abstract SocketProcessorBase<S> createSocketProcessor(SocketWrapperBase<S> paramSocketWrapperBase, SocketEvent paramSocketEvent);
/*      */   
/*      */ 
/*      */   public abstract void bind()
/*      */     throws Exception;
/*      */   
/*      */ 
/*      */   public abstract void unbind()
/*      */     throws Exception;
/*      */   
/*      */   public abstract void startInternal()
/*      */     throws Exception;
/*      */   
/*      */   public abstract void stopInternal()
/*      */     throws Exception;
/*      */   
/*      */   public void init()
/*      */     throws Exception
/*      */   {
/* 1085 */     if (this.bindOnInit) {
/* 1086 */       bind();
/* 1087 */       this.bindState = BindState.BOUND_ON_INIT;
/*      */     }
/* 1089 */     if (this.domain != null)
/*      */     {
/* 1091 */       this.oname = new ObjectName(this.domain + ":type=ThreadPool,name=\"" + getName() + "\"");
/* 1092 */       Registry.getRegistry(null, null).registerComponent(this, this.oname, null);
/*      */       
/* 1094 */       for (SSLHostConfig sslHostConfig : findSslHostConfigs()) {
/* 1095 */         registerJmx(sslHostConfig);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void registerJmx(SSLHostConfig sslHostConfig)
/*      */   {
/* 1102 */     ObjectName sslOname = null;
/*      */     try
/*      */     {
/* 1105 */       sslOname = new ObjectName(this.domain + ":type=SSLHostConfig,ThreadPool=" + getName() + ",name=" + ObjectName.quote(sslHostConfig.getHostName()));
/* 1106 */       sslHostConfig.setObjectName(sslOname);
/*      */       try {
/* 1108 */         Registry.getRegistry(null, null).registerComponent(sslHostConfig, sslOname, null);
/*      */       } catch (Exception e) {
/* 1110 */         getLog().warn(sm.getString("endpoint.jmxRegistrationFailed", new Object[] { sslOname }), e);
/*      */       }
/*      */     } catch (MalformedObjectNameException e) {
/* 1113 */       getLog().warn(sm.getString("endpoint.invalidJmxNameSslHost", new Object[] {sslHostConfig
/* 1114 */         .getHostName() }), e);
/*      */     }
/*      */     
/* 1117 */     for (SSLHostConfigCertificate sslHostConfigCert : sslHostConfig.getCertificates()) {
/* 1118 */       ObjectName sslCertOname = null;
/*      */       
/*      */ 
/*      */       try
/*      */       {
/* 1123 */         sslCertOname = new ObjectName(this.domain + ":type=SSLHostConfigCertificate,ThreadPool=" + getName() + ",Host=" + ObjectName.quote(sslHostConfig.getHostName()) + ",name=" + sslHostConfigCert.getType());
/* 1124 */         sslHostConfigCert.setObjectName(sslCertOname);
/*      */         try {
/* 1126 */           Registry.getRegistry(null, null).registerComponent(sslHostConfigCert, sslCertOname, null);
/*      */         }
/*      */         catch (Exception e) {
/* 1129 */           getLog().warn(sm.getString("endpoint.jmxRegistrationFailed", new Object[] { sslCertOname }), e);
/*      */         }
/*      */       } catch (MalformedObjectNameException e) {
/* 1132 */         getLog().warn(sm.getString("endpoint.invalidJmxNameSslHostCert", new Object[] {sslHostConfig
/* 1133 */           .getHostName(), sslHostConfigCert.getType() }), e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void unregisterJmx(SSLHostConfig sslHostConfig)
/*      */   {
/* 1140 */     Registry registry = Registry.getRegistry(null, null);
/* 1141 */     registry.unregisterComponent(sslHostConfig.getObjectName());
/* 1142 */     for (SSLHostConfigCertificate sslHostConfigCert : sslHostConfig.getCertificates()) {
/* 1143 */       registry.unregisterComponent(sslHostConfigCert.getObjectName());
/*      */     }
/*      */   }
/*      */   
/*      */   public final void start() throws Exception
/*      */   {
/* 1149 */     if (this.bindState == BindState.UNBOUND) {
/* 1150 */       bind();
/* 1151 */       this.bindState = BindState.BOUND_ON_START;
/*      */     }
/* 1153 */     startInternal();
/*      */   }
/*      */   
/*      */   protected final void startAcceptorThreads() {
/* 1157 */     int count = getAcceptorThreadCount();
/* 1158 */     this.acceptors = new Acceptor[count];
/*      */     
/* 1160 */     for (int i = 0; i < count; i++) {
/* 1161 */       this.acceptors[i] = createAcceptor();
/* 1162 */       String threadName = getName() + "-Acceptor-" + i;
/* 1163 */       this.acceptors[i].setThreadName(threadName);
/* 1164 */       Thread t = new Thread(this.acceptors[i], threadName);
/* 1165 */       t.setPriority(getAcceptorThreadPriority());
/* 1166 */       t.setDaemon(getDaemon());
/* 1167 */       t.start();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract Acceptor createAcceptor();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void pause()
/*      */   {
/* 1183 */     if ((this.running) && (!this.paused)) {
/* 1184 */       this.paused = true;
/* 1185 */       unlockAccept();
/* 1186 */       getHandler().pause();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void resume()
/*      */   {
/* 1195 */     if (this.running) {
/* 1196 */       this.paused = false;
/*      */     }
/*      */   }
/*      */   
/*      */   public final void stop() throws Exception {
/* 1201 */     stopInternal();
/* 1202 */     if (this.bindState == BindState.BOUND_ON_START) {
/* 1203 */       unbind();
/* 1204 */       this.bindState = BindState.UNBOUND;
/*      */     }
/*      */   }
/*      */   
/*      */   public final void destroy() throws Exception {
/* 1209 */     if (this.bindState == BindState.BOUND_ON_INIT) {
/* 1210 */       unbind();
/* 1211 */       this.bindState = BindState.UNBOUND;
/*      */     }
/* 1213 */     Registry registry = Registry.getRegistry(null, null);
/* 1214 */     registry.unregisterComponent(this.oname);
/* 1215 */     for (SSLHostConfig sslHostConfig : findSslHostConfigs()) {
/* 1216 */       unregisterJmx(sslHostConfig);
/*      */     }
/*      */   }
/*      */   
/*      */   protected abstract Log getLog();
/*      */   
/*      */   protected LimitLatch initializeConnectionLatch()
/*      */   {
/* 1224 */     if (this.maxConnections == -1) return null;
/* 1225 */     if (this.connectionLimitLatch == null) {
/* 1226 */       this.connectionLimitLatch = new LimitLatch(getMaxConnections());
/*      */     }
/* 1228 */     return this.connectionLimitLatch;
/*      */   }
/*      */   
/*      */   protected void releaseConnectionLatch() {
/* 1232 */     LimitLatch latch = this.connectionLimitLatch;
/* 1233 */     if (latch != null) latch.releaseAll();
/* 1234 */     this.connectionLimitLatch = null;
/*      */   }
/*      */   
/*      */   protected void countUpOrAwaitConnection() throws InterruptedException {
/* 1238 */     if (this.maxConnections == -1) return;
/* 1239 */     LimitLatch latch = this.connectionLimitLatch;
/* 1240 */     if (latch != null) latch.countUpOrAwait();
/*      */   }
/*      */   
/*      */   protected long countDownConnection() {
/* 1244 */     if (this.maxConnections == -1) return -1L;
/* 1245 */     LimitLatch latch = this.connectionLimitLatch;
/* 1246 */     if (latch != null) {
/* 1247 */       long result = latch.countDown();
/* 1248 */       if (result < 0L) {
/* 1249 */         getLog().warn(sm.getString("endpoint.warn.incorrectConnectionCount"));
/*      */       }
/* 1251 */       return result; }
/* 1252 */     return -1L;
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
/*      */   protected int handleExceptionWithDelay(int currentErrorDelay)
/*      */   {
/* 1267 */     if (currentErrorDelay > 0) {
/*      */       try {
/* 1269 */         Thread.sleep(currentErrorDelay);
/*      */       }
/*      */       catch (InterruptedException localInterruptedException) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1277 */     if (currentErrorDelay == 0)
/* 1278 */       return 50;
/* 1279 */     if (currentErrorDelay < 1600) {
/* 1280 */       return currentErrorDelay * 2;
/*      */     }
/* 1282 */     return 1600;
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\AbstractEndpoint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */