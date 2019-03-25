/*      */ package org.apache.coyote;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.net.InetAddress;
/*      */ import java.net.SocketException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.util.Collections;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import javax.management.MBeanRegistration;
/*      */ import javax.management.MBeanServer;
/*      */ import javax.management.MalformedObjectNameException;
/*      */ import javax.management.ObjectName;
/*      */ import javax.servlet.http.HttpUpgradeHandler;
/*      */ import javax.servlet.http.WebConnection;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.tomcat.ContextBind;
/*      */ import org.apache.tomcat.InstanceManager;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.collections.SynchronizedStack;
/*      */ import org.apache.tomcat.util.modeler.Registry;
/*      */ import org.apache.tomcat.util.net.AbstractEndpoint;
/*      */ import org.apache.tomcat.util.net.AbstractEndpoint.Handler;
/*      */ import org.apache.tomcat.util.net.AbstractEndpoint.Handler.SocketState;
/*      */ import org.apache.tomcat.util.net.SocketEvent;
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
/*      */ public abstract class AbstractProtocol<S>
/*      */   implements ProtocolHandler, MBeanRegistration
/*      */ {
/*   55 */   private static final StringManager sm = StringManager.getManager(AbstractProtocol.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   62 */   private static final AtomicInteger nameCounter = new AtomicInteger(0);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   68 */   protected ObjectName rgOname = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   76 */   private int nameIndex = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final AbstractEndpoint<S> endpoint;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private AbstractEndpoint.Handler<S> handler;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   91 */   private final Set<Processor> waitingProcessors = Collections.newSetFromMap(new ConcurrentHashMap());
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   97 */   private AbstractProtocol<S>.AsyncTimeout asyncTimeout = null;
/*      */   protected Adapter adapter;
/*      */   
/*      */   public AbstractProtocol(AbstractEndpoint<S> endpoint) {
/*  101 */     this.endpoint = endpoint;
/*  102 */     setSoLinger(-1);
/*  103 */     setTcpNoDelay(true);
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
/*      */   public boolean setProperty(String name, String value)
/*      */   {
/*  123 */     return this.endpoint.setProperty(name, value);
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
/*      */   public String getProperty(String name)
/*      */   {
/*  136 */     return this.endpoint.getProperty(name);
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
/*  148 */   public void setAdapter(Adapter adapter) { this.adapter = adapter; }
/*      */   
/*  150 */   public Adapter getAdapter() { return this.adapter; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  160 */   protected int processorCache = 200;
/*  161 */   public int getProcessorCache() { return this.processorCache; }
/*      */   
/*  163 */   public void setProcessorCache(int processorCache) { this.processorCache = processorCache; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  176 */   protected String clientCertProvider = null;
/*  177 */   public String getClientCertProvider() { return this.clientCertProvider; }
/*  178 */   public void setClientCertProvider(String s) { this.clientCertProvider = s; }
/*      */   
/*      */ 
/*      */   public boolean isAprRequired()
/*      */   {
/*  183 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean isSendfileSupported()
/*      */   {
/*  189 */     return this.endpoint.getUseSendfile();
/*      */   }
/*      */   
/*      */   public AbstractProtocol<S>.AsyncTimeout getAsyncTimeout()
/*      */   {
/*  194 */     return this.asyncTimeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*  204 */   private boolean sendReasonPhrase = false;
/*      */   
/*      */   protected String domain;
/*      */   
/*      */   protected ObjectName oname;
/*      */   
/*      */   protected MBeanServer mserver;
/*      */   
/*      */ 
/*      */   @Deprecated
/*      */   public boolean getSendReasonPhrase()
/*      */   {
/*  216 */     return this.sendReasonPhrase;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public void setSendReasonPhrase(boolean sendReasonPhrase)
/*      */   {
/*  228 */     this.sendReasonPhrase = sendReasonPhrase;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  235 */   public Executor getExecutor() { return this.endpoint.getExecutor(); }
/*      */   
/*  237 */   public void setExecutor(Executor executor) { this.endpoint.setExecutor(executor); }
/*      */   
/*      */ 
/*      */ 
/*  241 */   public int getMaxThreads() { return this.endpoint.getMaxThreads(); }
/*      */   
/*  243 */   public void setMaxThreads(int maxThreads) { this.endpoint.setMaxThreads(maxThreads); }
/*      */   
/*      */ 
/*  246 */   public int getMaxConnections() { return this.endpoint.getMaxConnections(); }
/*      */   
/*  248 */   public void setMaxConnections(int maxConnections) { this.endpoint.setMaxConnections(maxConnections); }
/*      */   
/*      */ 
/*      */ 
/*  252 */   public int getMinSpareThreads() { return this.endpoint.getMinSpareThreads(); }
/*      */   
/*  254 */   public void setMinSpareThreads(int minSpareThreads) { this.endpoint.setMinSpareThreads(minSpareThreads); }
/*      */   
/*      */ 
/*      */ 
/*  258 */   public int getThreadPriority() { return this.endpoint.getThreadPriority(); }
/*      */   
/*  260 */   public void setThreadPriority(int threadPriority) { this.endpoint.setThreadPriority(threadPriority); }
/*      */   
/*      */ 
/*      */ 
/*  264 */   public int getAcceptCount() { return this.endpoint.getAcceptCount(); }
/*  265 */   public void setAcceptCount(int acceptCount) { this.endpoint.setAcceptCount(acceptCount); }
/*      */   @Deprecated
/*  267 */   public int getBacklog() { return this.endpoint.getBacklog(); }
/*      */   @Deprecated
/*  269 */   public void setBacklog(int backlog) { this.endpoint.setBacklog(backlog); }
/*      */   
/*      */ 
/*  272 */   public boolean getTcpNoDelay() { return this.endpoint.getTcpNoDelay(); }
/*      */   
/*  274 */   public void setTcpNoDelay(boolean tcpNoDelay) { this.endpoint.setTcpNoDelay(tcpNoDelay); }
/*      */   
/*      */ 
/*      */ 
/*  278 */   public int getConnectionLinger() { return this.endpoint.getConnectionLinger(); }
/*      */   
/*  280 */   public void setConnectionLinger(int connectionLinger) { this.endpoint.setConnectionLinger(connectionLinger); }
/*      */   
/*      */   @Deprecated
/*  283 */   public int getSoLinger() { return this.endpoint.getSoLinger(); }
/*      */   @Deprecated
/*  285 */   public void setSoLinger(int soLinger) { this.endpoint.setSoLinger(soLinger); }
/*      */   
/*      */ 
/*  288 */   public int getKeepAliveTimeout() { return this.endpoint.getKeepAliveTimeout(); }
/*      */   
/*  290 */   public void setKeepAliveTimeout(int keepAliveTimeout) { this.endpoint.setKeepAliveTimeout(keepAliveTimeout); }
/*      */   
/*      */ 
/*  293 */   public InetAddress getAddress() { return this.endpoint.getAddress(); }
/*      */   
/*  295 */   public void setAddress(InetAddress ia) { this.endpoint.setAddress(ia); }
/*      */   
/*      */ 
/*      */ 
/*  299 */   public int getPort() { return this.endpoint.getPort(); }
/*      */   
/*  301 */   public void setPort(int port) { this.endpoint.setPort(port); }
/*      */   
/*      */   public int getLocalPort()
/*      */   {
/*  305 */     return this.endpoint.getLocalPort();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getConnectionTimeout()
/*      */   {
/*  312 */     return this.endpoint.getConnectionTimeout();
/*      */   }
/*      */   
/*  315 */   public void setConnectionTimeout(int timeout) { this.endpoint.setConnectionTimeout(timeout); }
/*      */   
/*      */   @Deprecated
/*      */   public int getSoTimeout()
/*      */   {
/*  320 */     return getConnectionTimeout();
/*      */   }
/*      */   
/*      */   @Deprecated
/*  324 */   public void setSoTimeout(int timeout) { setConnectionTimeout(timeout); }
/*      */   
/*      */   public int getMaxHeaderCount()
/*      */   {
/*  328 */     return this.endpoint.getMaxHeaderCount();
/*      */   }
/*      */   
/*  331 */   public void setMaxHeaderCount(int maxHeaderCount) { this.endpoint.setMaxHeaderCount(maxHeaderCount); }
/*      */   
/*      */   public long getConnectionCount()
/*      */   {
/*  335 */     return this.endpoint.getConnectionCount();
/*      */   }
/*      */   
/*      */   public void setAcceptorThreadCount(int threadCount) {
/*  339 */     this.endpoint.setAcceptorThreadCount(threadCount);
/*      */   }
/*      */   
/*  342 */   public int getAcceptorThreadCount() { return this.endpoint.getAcceptorThreadCount(); }
/*      */   
/*      */   public void setAcceptorThreadPriority(int threadPriority)
/*      */   {
/*  346 */     this.endpoint.setAcceptorThreadPriority(threadPriority);
/*      */   }
/*      */   
/*  349 */   public int getAcceptorThreadPriority() { return this.endpoint.getAcceptorThreadPriority(); }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized int getNameIndex()
/*      */   {
/*  356 */     if (this.nameIndex == 0) {
/*  357 */       this.nameIndex = nameCounter.incrementAndGet();
/*      */     }
/*      */     
/*  360 */     return this.nameIndex;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getName()
/*      */   {
/*  372 */     return ObjectName.quote(getNameInternal());
/*      */   }
/*      */   
/*      */   private String getNameInternal()
/*      */   {
/*  377 */     StringBuilder name = new StringBuilder(getNamePrefix());
/*  378 */     name.append('-');
/*  379 */     if (getAddress() != null) {
/*  380 */       name.append(getAddress().getHostAddress());
/*  381 */       name.append('-');
/*      */     }
/*  383 */     int port = getPort();
/*  384 */     if (port == 0)
/*      */     {
/*  386 */       name.append("auto-");
/*  387 */       name.append(getNameIndex());
/*  388 */       port = getLocalPort();
/*  389 */       if (port != -1) {
/*  390 */         name.append('-');
/*  391 */         name.append(port);
/*      */       }
/*      */     } else {
/*  394 */       name.append(port);
/*      */     }
/*  396 */     return name.toString();
/*      */   }
/*      */   
/*      */   public void addWaitingProcessor(Processor processor)
/*      */   {
/*  401 */     this.waitingProcessors.add(processor);
/*      */   }
/*      */   
/*      */   public void removeWaitingProcessor(Processor processor)
/*      */   {
/*  406 */     this.waitingProcessors.remove(processor);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected AbstractEndpoint<S> getEndpoint()
/*      */   {
/*  413 */     return this.endpoint;
/*      */   }
/*      */   
/*      */   protected AbstractEndpoint.Handler<S> getHandler()
/*      */   {
/*  418 */     return this.handler;
/*      */   }
/*      */   
/*      */   protected void setHandler(AbstractEndpoint.Handler<S> handler) {
/*  422 */     this.handler = handler;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract Log getLog();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract String getNamePrefix();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract String getProtocolName();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract UpgradeProtocol getNegotiatedProtocol(String paramString);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract UpgradeProtocol getUpgradeProtocol(String paramString);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract Processor createProcessor();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract Processor createUpgradeProcessor(SocketWrapperBase<?> paramSocketWrapperBase, UpgradeToken paramUpgradeToken);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectName getObjectName()
/*      */   {
/*  492 */     return this.oname;
/*      */   }
/*      */   
/*      */   public String getDomain() {
/*  496 */     return this.domain;
/*      */   }
/*      */   
/*      */   public ObjectName preRegister(MBeanServer server, ObjectName name)
/*      */     throws Exception
/*      */   {
/*  502 */     this.oname = name;
/*  503 */     this.mserver = server;
/*  504 */     this.domain = name.getDomain();
/*  505 */     return name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void postRegister(Boolean registrationDone) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void preDeregister()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */   public void postDeregister() {}
/*      */   
/*      */ 
/*      */   private ObjectName createObjectName()
/*      */     throws MalformedObjectNameException
/*      */   {
/*  525 */     this.domain = getAdapter().getDomain();
/*      */     
/*  527 */     if (this.domain == null) {
/*  528 */       return null;
/*      */     }
/*      */     
/*  531 */     StringBuilder name = new StringBuilder(getDomain());
/*  532 */     name.append(":type=ProtocolHandler,port=");
/*  533 */     int port = getPort();
/*  534 */     if (port > 0) {
/*  535 */       name.append(getPort());
/*      */     } else {
/*  537 */       name.append("auto-");
/*  538 */       name.append(getNameIndex());
/*      */     }
/*  540 */     InetAddress address = getAddress();
/*  541 */     if (address != null) {
/*  542 */       name.append(",address=");
/*  543 */       name.append(ObjectName.quote(address.getHostAddress()));
/*      */     }
/*  545 */     return new ObjectName(name.toString());
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
/*      */   public void init()
/*      */     throws Exception
/*      */   {
/*  559 */     if (getLog().isInfoEnabled()) {
/*  560 */       getLog().info(sm.getString("abstractProtocolHandler.init", new Object[] { getName() }));
/*      */     }
/*      */     
/*  563 */     if (this.oname == null)
/*      */     {
/*  565 */       this.oname = createObjectName();
/*  566 */       if (this.oname != null) {
/*  567 */         Registry.getRegistry(null, null).registerComponent(this, this.oname, null);
/*      */       }
/*      */     }
/*      */     
/*  571 */     if (this.domain != null) {
/*  572 */       this.rgOname = new ObjectName(this.domain + ":type=GlobalRequestProcessor,name=" + getName());
/*  573 */       Registry.getRegistry(null, null).registerComponent(
/*  574 */         getHandler().getGlobal(), this.rgOname, null);
/*      */     }
/*      */     
/*  577 */     String endpointName = getName();
/*  578 */     this.endpoint.setName(endpointName.substring(1, endpointName.length() - 1));
/*  579 */     this.endpoint.setDomain(this.domain);
/*      */     
/*  581 */     this.endpoint.init();
/*      */   }
/*      */   
/*      */   public void start()
/*      */     throws Exception
/*      */   {
/*  587 */     if (getLog().isInfoEnabled()) {
/*  588 */       getLog().info(sm.getString("abstractProtocolHandler.start", new Object[] { getName() }));
/*      */     }
/*      */     
/*  591 */     this.endpoint.start();
/*      */     
/*      */ 
/*  594 */     this.asyncTimeout = new AsyncTimeout();
/*  595 */     Thread timeoutThread = new Thread(this.asyncTimeout, getNameInternal() + "-AsyncTimeout");
/*  596 */     int priority = this.endpoint.getThreadPriority();
/*  597 */     if ((priority < 1) || (priority > 10)) {
/*  598 */       priority = 5;
/*      */     }
/*  600 */     timeoutThread.setPriority(priority);
/*  601 */     timeoutThread.setDaemon(true);
/*  602 */     timeoutThread.start();
/*      */   }
/*      */   
/*      */   public void pause()
/*      */     throws Exception
/*      */   {
/*  608 */     if (getLog().isInfoEnabled()) {
/*  609 */       getLog().info(sm.getString("abstractProtocolHandler.pause", new Object[] { getName() }));
/*      */     }
/*      */     
/*  612 */     this.endpoint.pause();
/*      */   }
/*      */   
/*      */   public void resume()
/*      */     throws Exception
/*      */   {
/*  618 */     if (getLog().isInfoEnabled()) {
/*  619 */       getLog().info(sm.getString("abstractProtocolHandler.resume", new Object[] { getName() }));
/*      */     }
/*      */     
/*  622 */     this.endpoint.resume();
/*      */   }
/*      */   
/*      */   public void stop()
/*      */     throws Exception
/*      */   {
/*  628 */     if (getLog().isInfoEnabled()) {
/*  629 */       getLog().info(sm.getString("abstractProtocolHandler.stop", new Object[] { getName() }));
/*      */     }
/*      */     
/*  632 */     if (this.asyncTimeout != null) {
/*  633 */       this.asyncTimeout.stop();
/*      */     }
/*      */     
/*  636 */     this.endpoint.stop();
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public void destroy()
/*      */     throws Exception
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 90	org/apache/coyote/AbstractProtocol:getLog	()Lorg/apache/juli/logging/Log;
/*      */     //   4: invokeinterface 91 1 0
/*      */     //   9: ifeq +31 -> 40
/*      */     //   12: aload_0
/*      */     //   13: invokevirtual 90	org/apache/coyote/AbstractProtocol:getLog	()Lorg/apache/juli/logging/Log;
/*      */     //   16: getstatic 3	org/apache/coyote/AbstractProtocol:sm	Lorg/apache/tomcat/util/res/StringManager;
/*      */     //   19: ldc 126
/*      */     //   21: iconst_1
/*      */     //   22: anewarray 93	java/lang/Object
/*      */     //   25: dup
/*      */     //   26: iconst_0
/*      */     //   27: aload_0
/*      */     //   28: invokevirtual 94	org/apache/coyote/AbstractProtocol:getName	()Ljava/lang/String;
/*      */     //   31: aastore
/*      */     //   32: invokevirtual 95	org/apache/tomcat/util/res/StringManager:getString	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   35: invokeinterface 96 2 0
/*      */     //   40: aload_0
/*      */     //   41: getfield 1	org/apache/coyote/AbstractProtocol:endpoint	Lorg/apache/tomcat/util/net/AbstractEndpoint;
/*      */     //   44: invokevirtual 127	org/apache/tomcat/util/net/AbstractEndpoint:destroy	()V
/*      */     //   47: aload_0
/*      */     //   48: getfield 79	org/apache/coyote/AbstractProtocol:oname	Ljavax/management/ObjectName;
/*      */     //   51: ifnull +77 -> 128
/*      */     //   54: aload_0
/*      */     //   55: getfield 81	org/apache/coyote/AbstractProtocol:mserver	Ljavax/management/MBeanServer;
/*      */     //   58: ifnonnull +18 -> 76
/*      */     //   61: aconst_null
/*      */     //   62: aconst_null
/*      */     //   63: invokestatic 98	org/apache/tomcat/util/modeler/Registry:getRegistry	(Ljava/lang/Object;Ljava/lang/Object;)Lorg/apache/tomcat/util/modeler/Registry;
/*      */     //   66: aload_0
/*      */     //   67: getfield 79	org/apache/coyote/AbstractProtocol:oname	Ljavax/management/ObjectName;
/*      */     //   70: invokevirtual 128	org/apache/tomcat/util/modeler/Registry:unregisterComponent	(Ljavax/management/ObjectName;)V
/*      */     //   73: goto +55 -> 128
/*      */     //   76: aload_0
/*      */     //   77: getfield 81	org/apache/coyote/AbstractProtocol:mserver	Ljavax/management/MBeanServer;
/*      */     //   80: aload_0
/*      */     //   81: getfield 79	org/apache/coyote/AbstractProtocol:oname	Ljavax/management/ObjectName;
/*      */     //   84: invokeinterface 129 2 0
/*      */     //   89: goto +39 -> 128
/*      */     //   92: astore_1
/*      */     //   93: aload_0
/*      */     //   94: invokevirtual 90	org/apache/coyote/AbstractProtocol:getLog	()Lorg/apache/juli/logging/Log;
/*      */     //   97: getstatic 3	org/apache/coyote/AbstractProtocol:sm	Lorg/apache/tomcat/util/res/StringManager;
/*      */     //   100: ldc -124
/*      */     //   102: iconst_2
/*      */     //   103: anewarray 93	java/lang/Object
/*      */     //   106: dup
/*      */     //   107: iconst_0
/*      */     //   108: aload_0
/*      */     //   109: getfield 79	org/apache/coyote/AbstractProtocol:oname	Ljavax/management/ObjectName;
/*      */     //   112: aastore
/*      */     //   113: dup
/*      */     //   114: iconst_1
/*      */     //   115: aload_0
/*      */     //   116: getfield 81	org/apache/coyote/AbstractProtocol:mserver	Ljavax/management/MBeanServer;
/*      */     //   119: aastore
/*      */     //   120: invokevirtual 95	org/apache/tomcat/util/res/StringManager:getString	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   123: invokeinterface 96 2 0
/*      */     //   128: aload_0
/*      */     //   129: getfield 5	org/apache/coyote/AbstractProtocol:rgOname	Ljavax/management/ObjectName;
/*      */     //   132: ifnull +121 -> 253
/*      */     //   135: aconst_null
/*      */     //   136: aconst_null
/*      */     //   137: invokestatic 98	org/apache/tomcat/util/modeler/Registry:getRegistry	(Ljava/lang/Object;Ljava/lang/Object;)Lorg/apache/tomcat/util/modeler/Registry;
/*      */     //   140: aload_0
/*      */     //   141: getfield 5	org/apache/coyote/AbstractProtocol:rgOname	Ljavax/management/ObjectName;
/*      */     //   144: invokevirtual 128	org/apache/tomcat/util/modeler/Registry:unregisterComponent	(Ljavax/management/ObjectName;)V
/*      */     //   147: goto +106 -> 253
/*      */     //   150: astore_2
/*      */     //   151: aload_0
/*      */     //   152: getfield 79	org/apache/coyote/AbstractProtocol:oname	Ljavax/management/ObjectName;
/*      */     //   155: ifnull +77 -> 232
/*      */     //   158: aload_0
/*      */     //   159: getfield 81	org/apache/coyote/AbstractProtocol:mserver	Ljavax/management/MBeanServer;
/*      */     //   162: ifnonnull +18 -> 180
/*      */     //   165: aconst_null
/*      */     //   166: aconst_null
/*      */     //   167: invokestatic 98	org/apache/tomcat/util/modeler/Registry:getRegistry	(Ljava/lang/Object;Ljava/lang/Object;)Lorg/apache/tomcat/util/modeler/Registry;
/*      */     //   170: aload_0
/*      */     //   171: getfield 79	org/apache/coyote/AbstractProtocol:oname	Ljavax/management/ObjectName;
/*      */     //   174: invokevirtual 128	org/apache/tomcat/util/modeler/Registry:unregisterComponent	(Ljavax/management/ObjectName;)V
/*      */     //   177: goto +55 -> 232
/*      */     //   180: aload_0
/*      */     //   181: getfield 81	org/apache/coyote/AbstractProtocol:mserver	Ljavax/management/MBeanServer;
/*      */     //   184: aload_0
/*      */     //   185: getfield 79	org/apache/coyote/AbstractProtocol:oname	Ljavax/management/ObjectName;
/*      */     //   188: invokeinterface 129 2 0
/*      */     //   193: goto +39 -> 232
/*      */     //   196: astore_3
/*      */     //   197: aload_0
/*      */     //   198: invokevirtual 90	org/apache/coyote/AbstractProtocol:getLog	()Lorg/apache/juli/logging/Log;
/*      */     //   201: getstatic 3	org/apache/coyote/AbstractProtocol:sm	Lorg/apache/tomcat/util/res/StringManager;
/*      */     //   204: ldc -124
/*      */     //   206: iconst_2
/*      */     //   207: anewarray 93	java/lang/Object
/*      */     //   210: dup
/*      */     //   211: iconst_0
/*      */     //   212: aload_0
/*      */     //   213: getfield 79	org/apache/coyote/AbstractProtocol:oname	Ljavax/management/ObjectName;
/*      */     //   216: aastore
/*      */     //   217: dup
/*      */     //   218: iconst_1
/*      */     //   219: aload_0
/*      */     //   220: getfield 81	org/apache/coyote/AbstractProtocol:mserver	Ljavax/management/MBeanServer;
/*      */     //   223: aastore
/*      */     //   224: invokevirtual 95	org/apache/tomcat/util/res/StringManager:getString	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   227: invokeinterface 96 2 0
/*      */     //   232: aload_0
/*      */     //   233: getfield 5	org/apache/coyote/AbstractProtocol:rgOname	Ljavax/management/ObjectName;
/*      */     //   236: ifnull +15 -> 251
/*      */     //   239: aconst_null
/*      */     //   240: aconst_null
/*      */     //   241: invokestatic 98	org/apache/tomcat/util/modeler/Registry:getRegistry	(Ljava/lang/Object;Ljava/lang/Object;)Lorg/apache/tomcat/util/modeler/Registry;
/*      */     //   244: aload_0
/*      */     //   245: getfield 5	org/apache/coyote/AbstractProtocol:rgOname	Ljavax/management/ObjectName;
/*      */     //   248: invokevirtual 128	org/apache/tomcat/util/modeler/Registry:unregisterComponent	(Ljavax/management/ObjectName;)V
/*      */     //   251: aload_2
/*      */     //   252: athrow
/*      */     //   253: return
/*      */     // Line number table:
/*      */     //   Java source line #642	-> byte code offset #0
/*      */     //   Java source line #643	-> byte code offset #12
/*      */     //   Java source line #647	-> byte code offset #40
/*      */     //   Java source line #649	-> byte code offset #47
/*      */     //   Java source line #650	-> byte code offset #54
/*      */     //   Java source line #651	-> byte code offset #61
/*      */     //   Java source line #655	-> byte code offset #76
/*      */     //   Java source line #659	-> byte code offset #89
/*      */     //   Java source line #656	-> byte code offset #92
/*      */     //   Java source line #657	-> byte code offset #93
/*      */     //   Java source line #663	-> byte code offset #128
/*      */     //   Java source line #664	-> byte code offset #135
/*      */     //   Java source line #649	-> byte code offset #150
/*      */     //   Java source line #650	-> byte code offset #158
/*      */     //   Java source line #651	-> byte code offset #165
/*      */     //   Java source line #655	-> byte code offset #180
/*      */     //   Java source line #659	-> byte code offset #193
/*      */     //   Java source line #656	-> byte code offset #196
/*      */     //   Java source line #657	-> byte code offset #197
/*      */     //   Java source line #663	-> byte code offset #232
/*      */     //   Java source line #664	-> byte code offset #239
/*      */     //   Java source line #667	-> byte code offset #253
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	254	0	this	AbstractProtocol<S>
/*      */     //   92	2	1	e	javax.management.JMException
/*      */     //   150	102	2	localObject	Object
/*      */     //   196	2	3	e	javax.management.JMException
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   76	89	92	javax/management/MBeanRegistrationException
/*      */     //   76	89	92	javax/management/InstanceNotFoundException
/*      */     //   40	47	150	finally
/*      */     //   180	193	196	javax/management/MBeanRegistrationException
/*      */     //   180	193	196	javax/management/InstanceNotFoundException
/*      */   }
/*      */   
/*      */   protected static class ConnectionHandler<S>
/*      */     implements AbstractEndpoint.Handler<S>
/*      */   {
/*      */     private final AbstractProtocol<S> proto;
/*  675 */     private final RequestGroupInfo global = new RequestGroupInfo();
/*  676 */     private final AtomicLong registerCount = new AtomicLong(0L);
/*  677 */     private final Map<S, Processor> connections = new ConcurrentHashMap();
/*  678 */     private final AbstractProtocol.RecycledProcessors recycledProcessors = new AbstractProtocol.RecycledProcessors(this);
/*      */     
/*      */     public ConnectionHandler(AbstractProtocol<S> proto) {
/*  681 */       this.proto = proto;
/*      */     }
/*      */     
/*      */     protected AbstractProtocol<S> getProtocol() {
/*  685 */       return this.proto;
/*      */     }
/*      */     
/*      */     protected Log getLog() {
/*  689 */       return getProtocol().getLog();
/*      */     }
/*      */     
/*      */     public Object getGlobal()
/*      */     {
/*  694 */       return this.global;
/*      */     }
/*      */     
/*      */     public void recycle()
/*      */     {
/*  699 */       this.recycledProcessors.clear();
/*      */     }
/*      */     
/*      */ 
/*      */     public AbstractEndpoint.Handler.SocketState process(SocketWrapperBase<S> wrapper, SocketEvent status)
/*      */     {
/*  705 */       if (getLog().isDebugEnabled()) {
/*  706 */         getLog().debug(AbstractProtocol.sm.getString("abstractConnectionHandler.process", new Object[] {wrapper
/*  707 */           .getSocket(), status }));
/*      */       }
/*  709 */       if (wrapper == null)
/*      */       {
/*  711 */         return AbstractEndpoint.Handler.SocketState.CLOSED;
/*      */       }
/*      */       
/*  714 */       S socket = wrapper.getSocket();
/*      */       
/*  716 */       Processor processor = (Processor)this.connections.get(socket);
/*  717 */       if (getLog().isDebugEnabled()) {
/*  718 */         getLog().debug(AbstractProtocol.sm.getString("abstractConnectionHandler.connectionsGet", new Object[] { processor, socket }));
/*      */       }
/*      */       
/*      */ 
/*  722 */       if (processor != null)
/*      */       {
/*  724 */         getProtocol().removeWaitingProcessor(processor);
/*  725 */       } else if ((status == SocketEvent.DISCONNECT) || (status == SocketEvent.ERROR))
/*      */       {
/*      */ 
/*  728 */         return AbstractEndpoint.Handler.SocketState.CLOSED;
/*      */       }
/*      */       
/*  731 */       ContainerThreadMarker.set();
/*      */       try
/*      */       {
/*  734 */         if (processor == null) {
/*  735 */           String negotiatedProtocol = wrapper.getNegotiatedProtocol();
/*  736 */           if (negotiatedProtocol != null)
/*      */           {
/*  738 */             UpgradeProtocol upgradeProtocol = getProtocol().getNegotiatedProtocol(negotiatedProtocol);
/*  739 */             if (upgradeProtocol != null) {
/*  740 */               processor = upgradeProtocol.getProcessor(wrapper, 
/*  741 */                 getProtocol().getAdapter());
/*  742 */             } else if (!negotiatedProtocol.equals("http/1.1"))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  753 */               if (getLog().isDebugEnabled()) {
/*  754 */                 getLog().debug(AbstractProtocol.sm.getString("abstractConnectionHandler.negotiatedProcessor.fail", new Object[] { negotiatedProtocol }));
/*      */               }
/*      */               
/*      */ 
/*  758 */               return AbstractEndpoint.Handler.SocketState.CLOSED;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  770 */         if (processor == null) {
/*  771 */           processor = this.recycledProcessors.pop();
/*  772 */           if (getLog().isDebugEnabled()) {
/*  773 */             getLog().debug(AbstractProtocol.sm.getString("abstractConnectionHandler.processorPop", new Object[] { processor }));
/*      */           }
/*      */         }
/*      */         
/*  777 */         if (processor == null) {
/*  778 */           processor = getProtocol().createProcessor();
/*  779 */           register(processor);
/*      */         }
/*      */         
/*  782 */         processor.setSslSupport(wrapper
/*  783 */           .getSslSupport(getProtocol().getClientCertProvider()));
/*      */         
/*      */ 
/*  786 */         this.connections.put(socket, processor);
/*      */         
/*  788 */         AbstractEndpoint.Handler.SocketState state = AbstractEndpoint.Handler.SocketState.CLOSED;
/*      */         do {
/*  790 */           state = processor.process(wrapper, status);
/*      */           
/*  792 */           if (state == AbstractEndpoint.Handler.SocketState.UPGRADING)
/*      */           {
/*  794 */             UpgradeToken upgradeToken = processor.getUpgradeToken();
/*      */             
/*  796 */             ByteBuffer leftOverInput = processor.getLeftoverInput();
/*  797 */             if (upgradeToken == null)
/*      */             {
/*  799 */               UpgradeProtocol upgradeProtocol = getProtocol().getUpgradeProtocol("h2c");
/*  800 */               if (upgradeProtocol != null) {
/*  801 */                 processor = upgradeProtocol.getProcessor(wrapper, 
/*  802 */                   getProtocol().getAdapter());
/*  803 */                 wrapper.unRead(leftOverInput);
/*      */                 
/*  805 */                 this.connections.put(socket, processor);
/*      */               } else {
/*  807 */                 if (getLog().isDebugEnabled()) {
/*  808 */                   getLog().debug(AbstractProtocol.sm.getString("abstractConnectionHandler.negotiatedProcessor.fail", new Object[] { "h2c" }));
/*      */                 }
/*      */                 
/*      */ 
/*  812 */                 return AbstractEndpoint.Handler.SocketState.CLOSED;
/*      */               }
/*      */             } else {
/*  815 */               HttpUpgradeHandler httpUpgradeHandler = upgradeToken.getHttpUpgradeHandler();
/*      */               
/*  817 */               release(processor);
/*      */               
/*  819 */               processor = getProtocol().createUpgradeProcessor(wrapper, upgradeToken);
/*  820 */               if (getLog().isDebugEnabled()) {
/*  821 */                 getLog().debug(AbstractProtocol.sm.getString("abstractConnectionHandler.upgradeCreate", new Object[] { processor, wrapper }));
/*      */               }
/*      */               
/*  824 */               wrapper.unRead(leftOverInput);
/*      */               
/*  826 */               wrapper.setUpgraded(true);
/*      */               
/*  828 */               this.connections.put(socket, processor);
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  835 */               if (upgradeToken.getInstanceManager() == null) {
/*  836 */                 httpUpgradeHandler.init((WebConnection)processor);
/*      */               } else {
/*  838 */                 ClassLoader oldCL = upgradeToken.getContextBind().bind(false, null);
/*      */                 try {
/*  840 */                   httpUpgradeHandler.init((WebConnection)processor);
/*      */                 } finally {
/*  842 */                   upgradeToken.getContextBind().unbind(false, oldCL);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*  847 */         } while (state == AbstractEndpoint.Handler.SocketState.UPGRADING);
/*      */         UpgradeToken upgradeToken;
/*  849 */         if (state == AbstractEndpoint.Handler.SocketState.LONG)
/*      */         {
/*      */ 
/*      */ 
/*  853 */           longPoll(wrapper, processor);
/*  854 */           if (processor.isAsync()) {
/*  855 */             getProtocol().addWaitingProcessor(processor);
/*      */           }
/*  857 */         } else if (state == AbstractEndpoint.Handler.SocketState.OPEN)
/*      */         {
/*      */ 
/*  860 */           this.connections.remove(socket);
/*  861 */           release(processor);
/*  862 */           wrapper.registerReadInterest();
/*  863 */         } else if (state != AbstractEndpoint.Handler.SocketState.SENDFILE)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*  868 */           if (state == AbstractEndpoint.Handler.SocketState.UPGRADED)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  874 */             if (status != SocketEvent.OPEN_WRITE) {
/*  875 */               longPoll(wrapper, processor);
/*      */             }
/*  877 */           } else if (state != AbstractEndpoint.Handler.SocketState.SUSPENDED)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  884 */             this.connections.remove(socket);
/*  885 */             if (processor.isUpgrade()) {
/*  886 */               upgradeToken = processor.getUpgradeToken();
/*  887 */               HttpUpgradeHandler httpUpgradeHandler = upgradeToken.getHttpUpgradeHandler();
/*  888 */               InstanceManager instanceManager = upgradeToken.getInstanceManager();
/*  889 */               if (instanceManager == null) {
/*  890 */                 httpUpgradeHandler.destroy();
/*      */               } else {
/*  892 */                 ClassLoader oldCL = upgradeToken.getContextBind().bind(false, null);
/*      */                 try {
/*  894 */                   httpUpgradeHandler.destroy();
/*      */                 } finally {
/*      */                   try {
/*  897 */                     instanceManager.destroyInstance(httpUpgradeHandler);
/*      */                   } catch (Throwable e) {
/*  899 */                     ExceptionUtils.handleThrowable(e);
/*  900 */                     getLog().error(AbstractProtocol.sm.getString("abstractConnectionHandler.error"), e);
/*      */                   }
/*  902 */                   upgradeToken.getContextBind().unbind(false, oldCL);
/*      */                 }
/*      */               }
/*      */             } else {
/*  906 */               release(processor);
/*      */             }
/*      */           } }
/*  909 */         return state;
/*      */       }
/*      */       catch (SocketException e) {
/*  912 */         getLog().debug(AbstractProtocol.sm.getString("abstractConnectionHandler.socketexception.debug"), e);
/*      */       }
/*      */       catch (IOException e)
/*      */       {
/*  916 */         getLog().debug(AbstractProtocol.sm.getString("abstractConnectionHandler.ioexception.debug"), e);
/*      */ 
/*      */       }
/*      */       catch (ProtocolException e)
/*      */       {
/*  921 */         getLog().debug(AbstractProtocol.sm.getString("abstractConnectionHandler.protocolexception.debug"), e);
/*      */ 
/*      */ 
/*      */       }
/*      */       catch (Throwable e)
/*      */       {
/*      */ 
/*  928 */         ExceptionUtils.handleThrowable(e);
/*      */         
/*      */ 
/*      */ 
/*  932 */         getLog().error(AbstractProtocol.sm.getString("abstractConnectionHandler.error"), e);
/*      */       } finally {
/*  934 */         ContainerThreadMarker.clear();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  939 */       this.connections.remove(socket);
/*  940 */       release(processor);
/*  941 */       return AbstractEndpoint.Handler.SocketState.CLOSED;
/*      */     }
/*      */     
/*      */     protected void longPoll(SocketWrapperBase<?> socket, Processor processor)
/*      */     {
/*  946 */       if (!processor.isAsync())
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  952 */         socket.registerReadInterest();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     public Set<S> getOpenSockets()
/*      */     {
/*  959 */       return this.connections.keySet();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void release(Processor processor)
/*      */     {
/*  971 */       if (processor != null) {
/*  972 */         processor.recycle();
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  978 */         if (!processor.isUpgrade()) {
/*  979 */           this.recycledProcessors.push(processor);
/*  980 */           getLog().debug("Pushed Processor [" + processor + "]");
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void release(SocketWrapperBase<S> socketWrapper)
/*      */     {
/*  992 */       S socket = socketWrapper.getSocket();
/*  993 */       Processor processor = (Processor)this.connections.remove(socket);
/*  994 */       release(processor);
/*      */     }
/*      */     
/*      */     protected void register(Processor processor)
/*      */     {
/*  999 */       if (getProtocol().getDomain() != null) {
/* 1000 */         synchronized (this) {
/*      */           try {
/* 1002 */             long count = this.registerCount.incrementAndGet();
/*      */             
/* 1004 */             RequestInfo rp = processor.getRequest().getRequestProcessor();
/* 1005 */             rp.setGlobalProcessor(this.global);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 1010 */             ObjectName rpName = new ObjectName(getProtocol().getDomain() + ":type=RequestProcessor,worker=" + getProtocol().getName() + ",name=" + getProtocol().getProtocolName() + "Request" + count);
/*      */             
/* 1012 */             if (getLog().isDebugEnabled()) {
/* 1013 */               getLog().debug("Register " + rpName);
/*      */             }
/* 1015 */             Registry.getRegistry(null, null).registerComponent(rp, rpName, null);
/*      */             
/* 1017 */             rp.setRpName(rpName);
/*      */           } catch (Exception e) {
/* 1019 */             getLog().warn("Error registering request");
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     protected void unregister(Processor processor) {
/* 1026 */       if (getProtocol().getDomain() != null) {
/* 1027 */         synchronized (this) {
/*      */           try {
/* 1029 */             Request r = processor.getRequest();
/* 1030 */             if (r == null)
/*      */             {
/* 1032 */               return;
/*      */             }
/* 1034 */             RequestInfo rp = r.getRequestProcessor();
/* 1035 */             rp.setGlobalProcessor(null);
/* 1036 */             ObjectName rpName = rp.getRpName();
/* 1037 */             if (getLog().isDebugEnabled()) {
/* 1038 */               getLog().debug("Unregister " + rpName);
/*      */             }
/* 1040 */             Registry.getRegistry(null, null).unregisterComponent(rpName);
/*      */             
/* 1042 */             rp.setRpName(null);
/*      */           } catch (Exception e) {
/* 1044 */             getLog().warn("Error unregistering request", e);
/*      */           }
/*      */         }
/*      */       }
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
/*      */     public final void pause()
/*      */     {
/* 1061 */       for (Processor processor : this.connections.values()) {
/* 1062 */         processor.pause();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected static class RecycledProcessors extends SynchronizedStack<Processor>
/*      */   {
/*      */     private final transient AbstractProtocol.ConnectionHandler<?> handler;
/* 1070 */     protected final AtomicInteger size = new AtomicInteger(0);
/*      */     
/*      */     public RecycledProcessors(AbstractProtocol.ConnectionHandler<?> handler) {
/* 1073 */       this.handler = handler;
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean push(Processor processor)
/*      */     {
/* 1079 */       int cacheSize = this.handler.getProtocol().getProcessorCache();
/* 1080 */       boolean offer = cacheSize == -1;
/*      */       
/* 1082 */       boolean result = false;
/* 1083 */       if (offer) {
/* 1084 */         result = super.push(processor);
/* 1085 */         if (result) {
/* 1086 */           this.size.incrementAndGet();
/*      */         }
/*      */       }
/* 1089 */       if (!result) this.handler.unregister(processor);
/* 1090 */       return result;
/*      */     }
/*      */     
/*      */ 
/*      */     public Processor pop()
/*      */     {
/* 1096 */       Processor result = (Processor)super.pop();
/* 1097 */       if (result != null) {
/* 1098 */         this.size.decrementAndGet();
/*      */       }
/* 1100 */       return result;
/*      */     }
/*      */     
/*      */     public synchronized void clear()
/*      */     {
/* 1105 */       Processor next = pop();
/* 1106 */       while (next != null) {
/* 1107 */         this.handler.unregister(next);
/* 1108 */         next = pop();
/*      */       }
/* 1110 */       super.clear();
/* 1111 */       this.size.set(0);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected class AsyncTimeout
/*      */     implements Runnable
/*      */   {
/* 1121 */     private volatile boolean asyncTimeoutRunning = true;
/*      */     
/*      */ 
/*      */ 
/*      */     protected AsyncTimeout() {}
/*      */     
/*      */ 
/*      */ 
/*      */     public void run()
/*      */     {
/* 1131 */       while (this.asyncTimeoutRunning) {
/*      */         try {
/* 1133 */           Thread.sleep(1000L);
/*      */         }
/*      */         catch (InterruptedException localInterruptedException) {}
/*      */         
/* 1137 */         long now = System.currentTimeMillis();
/* 1138 */         for (Processor processor : AbstractProtocol.this.waitingProcessors) {
/* 1139 */           processor.timeoutAsync(now);
/*      */         }
/*      */         
/*      */ 
/* 1143 */         while ((AbstractProtocol.this.endpoint.isPaused()) && (this.asyncTimeoutRunning)) {
/*      */           try {
/* 1145 */             Thread.sleep(1000L);
/*      */           }
/*      */           catch (InterruptedException localInterruptedException2) {}
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     protected void stop()
/*      */     {
/* 1155 */       this.asyncTimeoutRunning = false;
/*      */       
/*      */ 
/* 1158 */       for (Processor processor : AbstractProtocol.this.waitingProcessors) {
/* 1159 */         processor.timeoutAsync(-1L);
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\AbstractProtocol.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */