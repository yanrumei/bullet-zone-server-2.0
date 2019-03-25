/*     */ package org.springframework.remoting.jaxws;
/*     */ 
/*     */ import com.sun.net.httpserver.Authenticator;
/*     */ import com.sun.net.httpserver.Filter;
/*     */ import com.sun.net.httpserver.HttpContext;
/*     */ import com.sun.net.httpserver.HttpServer;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.List;
/*     */ import javax.jws.WebService;
/*     */ import javax.xml.ws.Endpoint;
/*     */ import javax.xml.ws.WebServiceProvider;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.UsesSunHttpServer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @UsesSunHttpServer
/*     */ public class SimpleHttpServerJaxWsServiceExporter
/*     */   extends AbstractJaxWsServiceExporter
/*     */ {
/*  54 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private HttpServer server;
/*     */   
/*  58 */   private int port = 8080;
/*     */   
/*     */   private String hostname;
/*     */   
/*  62 */   private int backlog = -1;
/*     */   
/*  64 */   private int shutdownDelay = 0;
/*     */   
/*  66 */   private String basePath = "/";
/*     */   
/*     */   private List<Filter> filters;
/*     */   
/*     */   private Authenticator authenticator;
/*     */   
/*  72 */   private boolean localServer = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServer(HttpServer server)
/*     */   {
/*  84 */     this.server = server;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPort(int port)
/*     */   {
/*  93 */     this.port = port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHostname(String hostname)
/*     */   {
/* 103 */     this.hostname = hostname;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBacklog(int backlog)
/*     */   {
/* 113 */     this.backlog = backlog;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setShutdownDelay(int shutdownDelay)
/*     */   {
/* 123 */     this.shutdownDelay = shutdownDelay;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBasePath(String basePath)
/*     */   {
/* 135 */     this.basePath = basePath;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFilters(List<Filter> filters)
/*     */   {
/* 143 */     this.filters = filters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAuthenticator(Authenticator authenticator)
/*     */   {
/* 151 */     this.authenticator = authenticator;
/*     */   }
/*     */   
/*     */   public void afterPropertiesSet()
/*     */     throws Exception
/*     */   {
/* 157 */     if (this.server == null) {
/* 158 */       InetSocketAddress address = this.hostname != null ? new InetSocketAddress(this.hostname, this.port) : new InetSocketAddress(this.port);
/*     */       
/* 160 */       this.server = HttpServer.create(address, this.backlog);
/* 161 */       if (this.logger.isInfoEnabled()) {
/* 162 */         this.logger.info("Starting HttpServer at address " + address);
/*     */       }
/* 164 */       this.server.start();
/* 165 */       this.localServer = true;
/*     */     }
/* 167 */     super.afterPropertiesSet();
/*     */   }
/*     */   
/*     */   protected void publishEndpoint(Endpoint endpoint, WebService annotation)
/*     */   {
/* 172 */     endpoint.publish(buildHttpContext(endpoint, annotation.serviceName()));
/*     */   }
/*     */   
/*     */   protected void publishEndpoint(Endpoint endpoint, WebServiceProvider annotation)
/*     */   {
/* 177 */     endpoint.publish(buildHttpContext(endpoint, annotation.serviceName()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HttpContext buildHttpContext(Endpoint endpoint, String serviceName)
/*     */   {
/* 187 */     String fullPath = calculateEndpointPath(endpoint, serviceName);
/* 188 */     HttpContext httpContext = this.server.createContext(fullPath);
/* 189 */     if (this.filters != null) {
/* 190 */       httpContext.getFilters().addAll(this.filters);
/*     */     }
/* 192 */     if (this.authenticator != null) {
/* 193 */       httpContext.setAuthenticator(this.authenticator);
/*     */     }
/* 195 */     return httpContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String calculateEndpointPath(Endpoint endpoint, String serviceName)
/*     */   {
/* 205 */     return this.basePath + serviceName;
/*     */   }
/*     */   
/*     */ 
/*     */   public void destroy()
/*     */   {
/* 211 */     super.destroy();
/* 212 */     if (this.localServer) {
/* 213 */       this.logger.info("Stopping HttpServer");
/* 214 */       this.server.stop(this.shutdownDelay);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\remoting\jaxws\SimpleHttpServerJaxWsServiceExporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */