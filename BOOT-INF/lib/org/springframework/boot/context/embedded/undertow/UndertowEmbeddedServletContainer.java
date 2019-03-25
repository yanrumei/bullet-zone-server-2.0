/*     */ package org.springframework.boot.context.embedded.undertow;
/*     */ 
/*     */ import io.undertow.Handlers;
/*     */ import io.undertow.Undertow;
/*     */ import io.undertow.Undertow.Builder;
/*     */ import io.undertow.attribute.RequestHeaderAttribute;
/*     */ import io.undertow.predicate.Predicate;
/*     */ import io.undertow.predicate.Predicates;
/*     */ import io.undertow.server.HttpHandler;
/*     */ import io.undertow.server.HttpServerExchange;
/*     */ import io.undertow.server.handlers.PathHandler;
/*     */ import io.undertow.server.handlers.encoding.ContentEncodingRepository;
/*     */ import io.undertow.server.handlers.encoding.EncodingHandler;
/*     */ import io.undertow.server.handlers.encoding.GzipEncodingProvider;
/*     */ import io.undertow.servlet.api.DeploymentManager;
/*     */ import io.undertow.util.HeaderMap;
/*     */ import io.undertow.util.Headers;
/*     */ import io.undertow.util.HttpString;
/*     */ import java.lang.reflect.Field;
/*     */ import java.net.BindException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.servlet.ServletException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.boot.context.embedded.Compression;
/*     */ import org.springframework.boot.context.embedded.EmbeddedServletContainer;
/*     */ import org.springframework.boot.context.embedded.EmbeddedServletContainerException;
/*     */ import org.springframework.boot.context.embedded.PortInUseException;
/*     */ import org.springframework.util.MimeType;
/*     */ import org.springframework.util.MimeTypeUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.xnio.channels.BoundChannel;
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
/*     */ public class UndertowEmbeddedServletContainer
/*     */   implements EmbeddedServletContainer
/*     */ {
/*  71 */   private static final Log logger = LogFactory.getLog(UndertowEmbeddedServletContainer.class);
/*     */   
/*  73 */   private final Object monitor = new Object();
/*     */   
/*     */   private final Undertow.Builder builder;
/*     */   
/*     */   private final DeploymentManager manager;
/*     */   
/*     */   private final String contextPath;
/*     */   
/*     */   private final boolean useForwardHeaders;
/*     */   
/*     */   private final boolean autoStart;
/*     */   
/*     */   private final Compression compression;
/*     */   
/*     */   private final String serverHeader;
/*     */   
/*     */   private Undertow undertow;
/*     */   
/*  91 */   private volatile boolean started = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UndertowEmbeddedServletContainer(Undertow.Builder builder, DeploymentManager manager, String contextPath, boolean autoStart, Compression compression)
/*     */   {
/* 103 */     this(builder, manager, contextPath, false, autoStart, compression);
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
/*     */   public UndertowEmbeddedServletContainer(Undertow.Builder builder, DeploymentManager manager, String contextPath, boolean useForwardHeaders, boolean autoStart, Compression compression)
/*     */   {
/* 118 */     this(builder, manager, contextPath, useForwardHeaders, autoStart, compression, null);
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
/*     */   public UndertowEmbeddedServletContainer(Undertow.Builder builder, DeploymentManager manager, String contextPath, boolean useForwardHeaders, boolean autoStart, Compression compression, String serverHeader)
/*     */   {
/* 135 */     this.builder = builder;
/* 136 */     this.manager = manager;
/* 137 */     this.contextPath = contextPath;
/* 138 */     this.useForwardHeaders = useForwardHeaders;
/* 139 */     this.autoStart = autoStart;
/* 140 */     this.compression = compression;
/* 141 */     this.serverHeader = serverHeader;
/*     */   }
/*     */   
/*     */   public void start() throws EmbeddedServletContainerException
/*     */   {
/* 146 */     synchronized (this.monitor) {
/* 147 */       if (this.started) {
/* 148 */         return;
/*     */       }
/*     */       try {
/* 151 */         if (!this.autoStart) {
/* 152 */           return;
/*     */         }
/* 154 */         if (this.undertow == null) {
/* 155 */           this.undertow = createUndertowServer();
/*     */         }
/* 157 */         this.undertow.start();
/* 158 */         this.started = true;
/* 159 */         logger
/* 160 */           .info("Undertow started on port(s) " + getPortsDescription());
/*     */       }
/*     */       catch (Exception ex) {
/*     */         try {
/* 164 */           if (findBindException(ex) != null) {
/* 165 */             List<Port> failedPorts = getConfiguredPorts();
/* 166 */             List<Port> actualPorts = getActualPorts();
/* 167 */             failedPorts.removeAll(actualPorts);
/* 168 */             if (failedPorts.size() == 1)
/*     */             {
/* 170 */               throw new PortInUseException(((Port)failedPorts.iterator().next()).getNumber());
/*     */             }
/*     */           }
/* 173 */           throw new EmbeddedServletContainerException("Unable to start embedded Undertow", ex);
/*     */         }
/*     */         finally
/*     */         {
/* 177 */           stopSilently();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void stopSilently() {
/*     */     try {
/* 185 */       if (this.manager != null) {
/* 186 */         this.manager.stop();
/*     */       }
/* 188 */       if (this.undertow != null) {
/* 189 */         this.undertow.stop();
/*     */       }
/*     */     }
/*     */     catch (Exception localException) {}
/*     */   }
/*     */   
/*     */ 
/*     */   private BindException findBindException(Exception ex)
/*     */   {
/* 198 */     Throwable candidate = ex;
/* 199 */     while (candidate != null) {
/* 200 */       if ((candidate instanceof BindException)) {
/* 201 */         return (BindException)candidate;
/*     */       }
/* 203 */       candidate = candidate.getCause();
/*     */     }
/* 205 */     return null;
/*     */   }
/*     */   
/*     */   private Undertow createUndertowServer() throws ServletException {
/* 209 */     HttpHandler httpHandler = this.manager.start();
/* 210 */     httpHandler = getContextHandler(httpHandler);
/* 211 */     if (this.useForwardHeaders) {
/* 212 */       httpHandler = Handlers.proxyPeerAddress(httpHandler);
/*     */     }
/* 214 */     if (StringUtils.hasText(this.serverHeader)) {
/* 215 */       httpHandler = Handlers.header(httpHandler, "Server", this.serverHeader);
/*     */     }
/* 217 */     this.builder.setHandler(httpHandler);
/* 218 */     return this.builder.build();
/*     */   }
/*     */   
/*     */   private HttpHandler getContextHandler(HttpHandler httpHandler) {
/* 222 */     HttpHandler contextHandler = configurationCompressionIfNecessary(httpHandler);
/* 223 */     if (StringUtils.isEmpty(this.contextPath)) {
/* 224 */       return contextHandler;
/*     */     }
/* 226 */     return Handlers.path().addPrefixPath(this.contextPath, contextHandler);
/*     */   }
/*     */   
/*     */   private HttpHandler configurationCompressionIfNecessary(HttpHandler httpHandler) {
/* 230 */     if ((this.compression == null) || (!this.compression.getEnabled())) {
/* 231 */       return httpHandler;
/*     */     }
/* 233 */     ContentEncodingRepository repository = new ContentEncodingRepository();
/* 234 */     repository.addEncodingHandler("gzip", new GzipEncodingProvider(), 50, 
/* 235 */       Predicates.and(getCompressionPredicates(this.compression)));
/* 236 */     return new EncodingHandler(repository).setNext(httpHandler);
/*     */   }
/*     */   
/*     */   private Predicate[] getCompressionPredicates(Compression compression) {
/* 240 */     List<Predicate> predicates = new ArrayList();
/* 241 */     predicates.add(new MaxSizePredicate(compression.getMinResponseSize()));
/* 242 */     predicates.add(new CompressibleMimeTypePredicate(compression.getMimeTypes()));
/* 243 */     if (compression.getExcludedUserAgents() != null) {
/* 244 */       for (String agent : compression.getExcludedUserAgents()) {
/* 245 */         RequestHeaderAttribute agentHeader = new RequestHeaderAttribute(new HttpString("User-Agent"));
/*     */         
/* 247 */         predicates.add(Predicates.not(Predicates.regex(agentHeader, agent)));
/*     */       }
/*     */     }
/* 250 */     return (Predicate[])predicates.toArray(new Predicate[predicates.size()]);
/*     */   }
/*     */   
/*     */   private String getPortsDescription() {
/* 254 */     List<Port> ports = getActualPorts();
/* 255 */     if (!ports.isEmpty()) {
/* 256 */       return StringUtils.collectionToDelimitedString(ports, " ");
/*     */     }
/* 258 */     return "unknown";
/*     */   }
/*     */   
/*     */   private List<Port> getActualPorts() {
/* 262 */     List<Port> ports = new ArrayList();
/*     */     try {
/* 264 */       if (!this.autoStart) {
/* 265 */         ports.add(new Port(-1, "unknown", null));
/*     */       }
/*     */       else {
/* 268 */         for (BoundChannel channel : extractChannels()) {
/* 269 */           ports.add(getPortFromChannel(channel));
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception localException1) {}
/*     */     
/*     */ 
/* 276 */     return ports;
/*     */   }
/*     */   
/*     */   private List<BoundChannel> extractChannels()
/*     */   {
/* 281 */     Field channelsField = ReflectionUtils.findField(Undertow.class, "channels");
/* 282 */     ReflectionUtils.makeAccessible(channelsField);
/* 283 */     return (List)ReflectionUtils.getField(channelsField, this.undertow);
/*     */   }
/*     */   
/*     */   private Port getPortFromChannel(BoundChannel channel)
/*     */   {
/* 288 */     SocketAddress socketAddress = channel.getLocalAddress();
/* 289 */     if ((socketAddress instanceof InetSocketAddress)) {
/* 290 */       String protocol = ReflectionUtils.findField(channel.getClass(), "ssl") != null ? "https" : "http";
/*     */       
/* 292 */       return new Port(((InetSocketAddress)socketAddress).getPort(), protocol, null);
/*     */     }
/* 294 */     return null;
/*     */   }
/*     */   
/*     */   private List<Port> getConfiguredPorts() {
/* 298 */     List<Port> ports = new ArrayList();
/* 299 */     for (Object listener : extractListeners()) {
/*     */       try {
/* 301 */         ports.add(getPortFromListener(listener));
/*     */       }
/*     */       catch (Exception localException) {}
/*     */     }
/*     */     
/*     */ 
/* 307 */     return ports;
/*     */   }
/*     */   
/*     */   private List<Object> extractListeners()
/*     */   {
/* 312 */     Field listenersField = ReflectionUtils.findField(Undertow.class, "listeners");
/* 313 */     ReflectionUtils.makeAccessible(listenersField);
/* 314 */     return (List)ReflectionUtils.getField(listenersField, this.undertow);
/*     */   }
/*     */   
/*     */   private Port getPortFromListener(Object listener) {
/* 318 */     Field typeField = ReflectionUtils.findField(listener.getClass(), "type");
/* 319 */     ReflectionUtils.makeAccessible(typeField);
/* 320 */     String protocol = ReflectionUtils.getField(typeField, listener).toString();
/* 321 */     Field portField = ReflectionUtils.findField(listener.getClass(), "port");
/* 322 */     ReflectionUtils.makeAccessible(portField);
/* 323 */     int port = ((Integer)ReflectionUtils.getField(portField, listener)).intValue();
/* 324 */     return new Port(port, protocol, null);
/*     */   }
/*     */   
/*     */   public void stop() throws EmbeddedServletContainerException
/*     */   {
/* 329 */     synchronized (this.monitor) {
/* 330 */       if (!this.started) {
/* 331 */         return;
/*     */       }
/* 333 */       this.started = false;
/*     */       try {
/* 335 */         this.manager.stop();
/* 336 */         this.undertow.stop();
/*     */       }
/*     */       catch (Exception ex) {
/* 339 */         throw new EmbeddedServletContainerException("Unable to stop undertow", ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 347 */     List<Port> ports = getActualPorts();
/* 348 */     if (ports.isEmpty()) {
/* 349 */       return 0;
/*     */     }
/* 351 */     return ((Port)ports.get(0)).getNumber();
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class Port
/*     */   {
/*     */     private final int number;
/*     */     
/*     */     private final String protocol;
/*     */     
/*     */ 
/*     */     private Port(int number, String protocol)
/*     */     {
/* 364 */       this.number = number;
/* 365 */       this.protocol = protocol;
/*     */     }
/*     */     
/*     */     public int getNumber() {
/* 369 */       return this.number;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 374 */       return this.number + " (" + this.protocol + ")";
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 379 */       return this.number;
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 384 */       if (this == obj) {
/* 385 */         return true;
/*     */       }
/* 387 */       if (obj == null) {
/* 388 */         return false;
/*     */       }
/* 390 */       if (getClass() != obj.getClass()) {
/* 391 */         return false;
/*     */       }
/* 393 */       Port other = (Port)obj;
/* 394 */       if (this.number != other.number) {
/* 395 */         return false;
/*     */       }
/* 397 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CompressibleMimeTypePredicate implements Predicate
/*     */   {
/*     */     private final List<MimeType> mimeTypes;
/*     */     
/*     */     CompressibleMimeTypePredicate(String[] mimeTypes)
/*     */     {
/* 407 */       this.mimeTypes = new ArrayList(mimeTypes.length);
/* 408 */       for (String mimeTypeString : mimeTypes) {
/* 409 */         this.mimeTypes.add(MimeTypeUtils.parseMimeType(mimeTypeString));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean resolve(HttpServerExchange value)
/*     */     {
/* 416 */       String contentType = value.getResponseHeaders().getFirst("Content-Type");
/* 417 */       if (contentType != null) {
/* 418 */         for (MimeType mimeType : this.mimeTypes)
/*     */         {
/* 420 */           if (mimeType.isCompatibleWith(MimeTypeUtils.parseMimeType(contentType))) {
/* 421 */             return true;
/*     */           }
/*     */         }
/*     */       }
/* 425 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class MaxSizePredicate
/*     */     implements Predicate
/*     */   {
/*     */     private final Predicate maxContentSize;
/*     */     
/*     */ 
/*     */     MaxSizePredicate(int size)
/*     */     {
/* 439 */       this.maxContentSize = Predicates.maxContentSize(size);
/*     */     }
/*     */     
/*     */     public boolean resolve(HttpServerExchange value)
/*     */     {
/* 444 */       if (value.getResponseHeaders().contains(Headers.CONTENT_LENGTH)) {
/* 445 */         return this.maxContentSize.resolve(value);
/*     */       }
/* 447 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedde\\undertow\UndertowEmbeddedServletContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */