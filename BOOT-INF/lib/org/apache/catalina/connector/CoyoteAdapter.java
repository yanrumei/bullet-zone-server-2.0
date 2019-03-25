/*      */ package org.apache.catalina.connector;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashMap;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import javax.servlet.ReadListener;
/*      */ import javax.servlet.ServletContext;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.SessionTrackingMode;
/*      */ import javax.servlet.WriteListener;
/*      */ import org.apache.catalina.Authenticator;
/*      */ import org.apache.catalina.Context;
/*      */ import org.apache.catalina.Engine;
/*      */ import org.apache.catalina.Host;
/*      */ import org.apache.catalina.LifecycleState;
/*      */ import org.apache.catalina.Manager;
/*      */ import org.apache.catalina.Pipeline;
/*      */ import org.apache.catalina.Realm;
/*      */ import org.apache.catalina.Service;
/*      */ import org.apache.catalina.Valve;
/*      */ import org.apache.catalina.Wrapper;
/*      */ import org.apache.catalina.authenticator.AuthenticatorBase;
/*      */ import org.apache.catalina.core.AsyncContextImpl;
/*      */ import org.apache.catalina.mapper.Mapper;
/*      */ import org.apache.catalina.mapper.MappingData;
/*      */ import org.apache.catalina.util.ServerInfo;
/*      */ import org.apache.catalina.util.SessionConfig;
/*      */ import org.apache.catalina.util.URLEncoder;
/*      */ import org.apache.coyote.ActionCode;
/*      */ import org.apache.coyote.Adapter;
/*      */ import org.apache.coyote.RequestInfo;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.buf.B2CConverter;
/*      */ import org.apache.tomcat.util.buf.ByteChunk;
/*      */ import org.apache.tomcat.util.buf.CharChunk;
/*      */ import org.apache.tomcat.util.buf.MessageBytes;
/*      */ import org.apache.tomcat.util.buf.UDecoder;
/*      */ import org.apache.tomcat.util.http.Parameters;
/*      */ import org.apache.tomcat.util.http.ServerCookie;
/*      */ import org.apache.tomcat.util.http.ServerCookies;
/*      */ import org.apache.tomcat.util.net.SocketEvent;
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
/*      */ public class CoyoteAdapter
/*      */   implements Adapter
/*      */ {
/*   66 */   private static final Log log = LogFactory.getLog(CoyoteAdapter.class);
/*      */   
/*      */ 
/*      */ 
/*   70 */   private static final String POWERED_BY = "Servlet/4.0 JSP/2.3 (" + 
/*   71 */     ServerInfo.getServerInfo() + " Java/" + 
/*   72 */     System.getProperty("java.vm.vendor") + "/" + 
/*   73 */     System.getProperty("java.runtime.version") + ")";
/*      */   
/*      */ 
/*   76 */   private static final EnumSet<SessionTrackingMode> SSL_ONLY = EnumSet.of(SessionTrackingMode.SSL);
/*      */   
/*      */ 
/*      */   public static final int ADAPTER_NOTES = 1;
/*      */   
/*      */ 
/*   82 */   protected static final boolean ALLOW_BACKSLASH = Boolean.parseBoolean(System.getProperty("org.apache.catalina.connector.CoyoteAdapter.ALLOW_BACKSLASH", "false"));
/*      */   
/*      */ 
/*   85 */   private static final ThreadLocal<String> THREAD_NAME = new ThreadLocal()
/*      */   {
/*      */ 
/*      */     protected String initialValue()
/*      */     {
/*   90 */       return Thread.currentThread().getName();
/*      */     }
/*      */   };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final Connector connector;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CoyoteAdapter(Connector connector)
/*      */   {
/*  106 */     this.connector = connector;
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
/*  123 */   protected static final StringManager sm = StringManager.getManager(CoyoteAdapter.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean asyncDispatch(org.apache.coyote.Request req, org.apache.coyote.Response res, SocketEvent status)
/*      */     throws Exception
/*      */   {
/*  132 */     Request request = (Request)req.getNote(1);
/*  133 */     Response response = (Response)res.getNote(1);
/*      */     
/*  135 */     if (request == null) {
/*  136 */       throw new IllegalStateException("Dispatch may only happen on an existing request.");
/*      */     }
/*      */     
/*  139 */     boolean success = true;
/*  140 */     AsyncContextImpl asyncConImpl = request.getAsyncContextInternal();
/*      */     
/*  142 */     req.getRequestProcessor().setWorkerThreadName((String)THREAD_NAME.get());
/*      */     try
/*      */     {
/*  145 */       if (!request.isAsync())
/*      */       {
/*      */ 
/*      */ 
/*  149 */         response.setSuspended(false);
/*      */       }
/*      */       
/*  152 */       if (status == SocketEvent.TIMEOUT) {
/*  153 */         if (!asyncConImpl.timeout()) {
/*  154 */           asyncConImpl.setErrorState(null, false);
/*      */         }
/*  156 */       } else if (status == SocketEvent.ERROR)
/*      */       {
/*      */ 
/*      */ 
/*  160 */         success = false;
/*  161 */         Throwable t = (Throwable)req.getAttribute("javax.servlet.error.exception");
/*  162 */         req.getAttributes().remove("javax.servlet.error.exception");
/*  163 */         ClassLoader oldCL = null;
/*      */         try {
/*  165 */           oldCL = request.getContext().bind(false, null);
/*  166 */           if (req.getReadListener() != null) {
/*  167 */             req.getReadListener().onError(t);
/*      */           }
/*  169 */           if (res.getWriteListener() != null) {
/*  170 */             res.getWriteListener().onError(t);
/*      */           }
/*      */         } finally {
/*  173 */           request.getContext().unbind(false, oldCL);
/*      */         }
/*  175 */         if (t != null) {
/*  176 */           asyncConImpl.setErrorState(t, true);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  181 */       if ((!request.isAsyncDispatching()) && (request.isAsync())) {
/*  182 */         WriteListener writeListener = res.getWriteListener();
/*  183 */         ReadListener readListener = req.getReadListener();
/*  184 */         if ((writeListener != null) && (status == SocketEvent.OPEN_WRITE)) {
/*  185 */           ClassLoader oldCL = null;
/*      */           try {
/*  187 */             oldCL = request.getContext().bind(false, null);
/*  188 */             res.onWritePossible();
/*  189 */             if ((request.isFinished()) && (req.sendAllDataReadEvent()) && (readListener != null))
/*      */             {
/*  191 */               readListener.onAllDataRead();
/*      */             }
/*      */           } catch (Throwable t) {
/*  194 */             ExceptionUtils.handleThrowable(t);
/*  195 */             writeListener.onError(t);
/*  196 */             success = false;
/*      */           } finally {
/*  198 */             request.getContext().unbind(false, oldCL);
/*      */           }
/*  200 */         } else if ((readListener != null) && (status == SocketEvent.OPEN_READ)) {
/*  201 */           ClassLoader oldCL = null;
/*      */           try {
/*  203 */             oldCL = request.getContext().bind(false, null);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  209 */             if (!request.isFinished()) {
/*  210 */               readListener.onDataAvailable();
/*      */             }
/*  212 */             if ((request.isFinished()) && (req.sendAllDataReadEvent())) {
/*  213 */               readListener.onAllDataRead();
/*      */             }
/*      */           } catch (Throwable t) {
/*  216 */             ExceptionUtils.handleThrowable(t);
/*  217 */             readListener.onError(t);
/*  218 */             success = false;
/*      */           } finally {
/*  220 */             request.getContext().unbind(false, oldCL);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  228 */       if ((!request.isAsyncDispatching()) && (request.isAsync()) && 
/*  229 */         (response.isErrorReportRequired())) {
/*  230 */         this.connector.getService().getContainer().getPipeline().getFirst().invoke(request, response);
/*      */       }
/*      */       
/*      */ 
/*  234 */       if (request.isAsyncDispatching()) {
/*  235 */         this.connector.getService().getContainer().getPipeline().getFirst().invoke(request, response);
/*      */         
/*  237 */         Throwable t = (Throwable)request.getAttribute("javax.servlet.error.exception");
/*  238 */         if (t != null) {
/*  239 */           asyncConImpl.setErrorState(t, true);
/*      */         }
/*      */       }
/*      */       
/*  243 */       if (!request.isAsync()) {
/*  244 */         request.finishRequest();
/*  245 */         response.finishResponse();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  250 */       AtomicBoolean error = new AtomicBoolean(false);
/*  251 */       res.action(ActionCode.IS_ERROR, error);
/*  252 */       if (error.get()) {
/*  253 */         if (request.isAsyncCompleting())
/*      */         {
/*      */ 
/*      */ 
/*  257 */           res.action(ActionCode.ASYNC_POST_PROCESS, null);
/*      */         }
/*  259 */         success = false;
/*      */       } } catch (IOException e) { long time;
/*      */       Context context;
/*  262 */       success = false;
/*      */     } catch (Throwable t) { long time;
/*      */       Context context;
/*  265 */       ExceptionUtils.handleThrowable(t);
/*  266 */       success = false;
/*  267 */       log.error(sm.getString("coyoteAdapter.asyncDispatch"), t); } finally { long time;
/*      */       Context context;
/*  269 */       if (!success) {
/*  270 */         res.setStatus(500);
/*      */       }
/*      */       
/*      */ 
/*  274 */       if ((!success) || (!request.isAsync())) {
/*  275 */         long time = 0L;
/*  276 */         if (req.getStartTime() != -1L) {
/*  277 */           time = System.currentTimeMillis() - req.getStartTime();
/*      */         }
/*  279 */         Context context = request.getContext();
/*  280 */         if (context != null) {
/*  281 */           context.logAccess(request, response, time, false);
/*      */         } else {
/*  283 */           log(req, res, time);
/*      */         }
/*      */       }
/*      */       
/*  287 */       req.getRequestProcessor().setWorkerThreadName(null);
/*      */       
/*  289 */       if ((!success) || (!request.isAsync())) {
/*  290 */         request.recycle();
/*  291 */         response.recycle();
/*      */       }
/*      */     }
/*  294 */     return success;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void service(org.apache.coyote.Request req, org.apache.coyote.Response res)
/*      */     throws Exception
/*      */   {
/*  302 */     Request request = (Request)req.getNote(1);
/*  303 */     Response response = (Response)res.getNote(1);
/*      */     
/*  305 */     if (request == null)
/*      */     {
/*  307 */       request = this.connector.createRequest();
/*  308 */       request.setCoyoteRequest(req);
/*  309 */       response = this.connector.createResponse();
/*  310 */       response.setCoyoteResponse(res);
/*      */       
/*      */ 
/*  313 */       request.setResponse(response);
/*  314 */       response.setRequest(request);
/*      */       
/*      */ 
/*  317 */       req.setNote(1, request);
/*  318 */       res.setNote(1, response);
/*      */       
/*      */ 
/*  321 */       req.getParameters().setQueryStringCharset(this.connector.getURICharset());
/*      */     }
/*      */     
/*  324 */     if (this.connector.getXpoweredBy()) {
/*  325 */       response.addHeader("X-Powered-By", POWERED_BY);
/*      */     }
/*      */     
/*  328 */     boolean async = false;
/*  329 */     boolean postParseSuccess = false;
/*      */     
/*  331 */     req.getRequestProcessor().setWorkerThreadName((String)THREAD_NAME.get());
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  336 */       postParseSuccess = postParseRequest(req, request, res, response);
/*  337 */       if (postParseSuccess)
/*      */       {
/*  339 */         request.setAsyncSupported(this.connector
/*  340 */           .getService().getContainer().getPipeline().isAsyncSupported());
/*      */         
/*  342 */         this.connector.getService().getContainer().getPipeline().getFirst().invoke(request, response);
/*      */       }
/*      */       
/*  345 */       if (request.isAsync()) {
/*  346 */         async = true;
/*  347 */         ReadListener readListener = req.getReadListener();
/*  348 */         if ((readListener != null) && (request.isFinished()))
/*      */         {
/*      */ 
/*  351 */           ClassLoader oldCL = null;
/*      */           try {
/*  353 */             oldCL = request.getContext().bind(false, null);
/*  354 */             if (req.sendAllDataReadEvent()) {
/*  355 */               req.getReadListener().onAllDataRead();
/*      */             }
/*      */           } finally {
/*  358 */             request.getContext().unbind(false, oldCL);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  363 */         Throwable throwable = (Throwable)request.getAttribute("javax.servlet.error.exception");
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  368 */         if ((!request.isAsyncCompleting()) && (throwable != null)) {
/*  369 */           request.getAsyncContextInternal().setErrorState(throwable, true);
/*      */         }
/*      */       } else {
/*  372 */         request.finishRequest();
/*  373 */         response.finishResponse();
/*      */       }
/*      */     } catch (IOException localIOException) {}finally { AtomicBoolean error;
/*      */       Context context;
/*      */       AtomicBoolean error;
/*      */       Context context;
/*  379 */       AtomicBoolean error = new AtomicBoolean(false);
/*  380 */       res.action(ActionCode.IS_ERROR, error);
/*      */       
/*  382 */       if ((request.isAsyncCompleting()) && (error.get()))
/*      */       {
/*      */ 
/*      */ 
/*  386 */         res.action(ActionCode.ASYNC_POST_PROCESS, null);
/*  387 */         async = false;
/*      */       }
/*      */       
/*      */ 
/*  391 */       if ((!async) && (postParseSuccess))
/*      */       {
/*      */ 
/*  394 */         Context context = request.getContext();
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  400 */         if (context != null) {
/*  401 */           context.logAccess(request, response, 
/*  402 */             System.currentTimeMillis() - req.getStartTime(), false);
/*      */         }
/*      */       }
/*      */       
/*  406 */       req.getRequestProcessor().setWorkerThreadName(null);
/*      */       
/*      */ 
/*  409 */       if (!async) {
/*  410 */         request.recycle();
/*  411 */         response.recycle();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean prepare(org.apache.coyote.Request req, org.apache.coyote.Response res)
/*      */     throws IOException, ServletException
/*      */   {
/*  420 */     Request request = (Request)req.getNote(1);
/*  421 */     Response response = (Response)res.getNote(1);
/*      */     
/*  423 */     return postParseRequest(req, request, res, response);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void log(org.apache.coyote.Request req, org.apache.coyote.Response res, long time)
/*      */   {
/*  431 */     Request request = (Request)req.getNote(1);
/*  432 */     Response response = (Response)res.getNote(1);
/*      */     
/*  434 */     if (request == null)
/*      */     {
/*  436 */       request = this.connector.createRequest();
/*  437 */       request.setCoyoteRequest(req);
/*  438 */       response = this.connector.createResponse();
/*  439 */       response.setCoyoteResponse(res);
/*      */       
/*      */ 
/*  442 */       request.setResponse(response);
/*  443 */       response.setRequest(request);
/*      */       
/*      */ 
/*  446 */       req.setNote(1, request);
/*  447 */       res.setNote(1, response);
/*      */       
/*      */ 
/*  450 */       req.getParameters().setQueryStringCharset(this.connector.getURICharset());
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  456 */       boolean logged = false;
/*  457 */       Context context = request.mappingData.context;
/*  458 */       Host host = request.mappingData.host;
/*  459 */       if (context != null) {
/*  460 */         logged = true;
/*  461 */         context.logAccess(request, response, time, true);
/*  462 */       } else if (host != null) {
/*  463 */         logged = true;
/*  464 */         host.logAccess(request, response, time, true);
/*      */       }
/*  466 */       if (!logged) {
/*  467 */         this.connector.getService().getContainer().logAccess(request, response, time, true);
/*      */       }
/*      */     } catch (Throwable t) {
/*  470 */       ExceptionUtils.handleThrowable(t);
/*  471 */       log.warn(sm.getString("coyoteAdapter.accesslogFail"), t);
/*      */     } finally {
/*  473 */       request.recycle();
/*  474 */       response.recycle();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void checkRecycled(org.apache.coyote.Request req, org.apache.coyote.Response res)
/*      */   {
/*  486 */     Request request = (Request)req.getNote(1);
/*  487 */     Response response = (Response)res.getNote(1);
/*  488 */     String messageKey = null;
/*  489 */     if ((request != null) && (request.getHost() != null)) {
/*  490 */       messageKey = "coyoteAdapter.checkRecycled.request";
/*  491 */     } else if ((response != null) && (response.getContentWritten() != 0L)) {
/*  492 */       messageKey = "coyoteAdapter.checkRecycled.response";
/*      */     }
/*  494 */     if (messageKey != null)
/*      */     {
/*      */ 
/*  497 */       log(req, res, 0L);
/*      */       
/*  499 */       if (this.connector.getState().isAvailable()) {
/*  500 */         if (log.isInfoEnabled()) {
/*  501 */           log.info(sm.getString(messageKey), new RecycleRequiredException(null));
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */       }
/*  508 */       else if (log.isDebugEnabled()) {
/*  509 */         log.debug(sm.getString(messageKey), new RecycleRequiredException(null));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDomain()
/*      */   {
/*  519 */     return this.connector.getDomain();
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
/*      */   protected boolean postParseRequest(org.apache.coyote.Request req, Request request, org.apache.coyote.Response res, Response response)
/*      */     throws IOException, ServletException
/*      */   {
/*  549 */     if (req.scheme().isNull())
/*      */     {
/*      */ 
/*  552 */       req.scheme().setString(this.connector.getScheme());
/*  553 */       request.setSecure(this.connector.getSecure());
/*      */     }
/*      */     else {
/*  556 */       request.setSecure(req.scheme().equals("https"));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  561 */     String proxyName = this.connector.getProxyName();
/*  562 */     int proxyPort = this.connector.getProxyPort();
/*  563 */     if (proxyPort != 0) {
/*  564 */       req.setServerPort(proxyPort);
/*  565 */     } else if (req.getServerPort() == -1)
/*      */     {
/*  567 */       if (req.scheme().equals("https")) {
/*  568 */         req.setServerPort(443);
/*      */       } else {
/*  570 */         req.setServerPort(80);
/*      */       }
/*      */     }
/*  573 */     if (proxyName != null) {
/*  574 */       req.serverName().setString(proxyName);
/*      */     }
/*      */     
/*  577 */     MessageBytes undecodedURI = req.requestURI();
/*      */     
/*      */ 
/*  580 */     if (undecodedURI.equals("*")) {
/*  581 */       if (req.method().equalsIgnoreCase("OPTIONS")) {
/*  582 */         StringBuilder allow = new StringBuilder();
/*  583 */         allow.append("GET, HEAD, POST, PUT, DELETE");
/*      */         
/*  585 */         if (this.connector.getAllowTrace()) {
/*  586 */           allow.append(", TRACE");
/*      */         }
/*      */         
/*  589 */         allow.append(", OPTIONS");
/*  590 */         res.setHeader("Allow", allow.toString());
/*      */       } else {
/*  592 */         res.setStatus(404);
/*  593 */         res.setMessage("Not found");
/*      */       }
/*  595 */       this.connector.getService().getContainer().logAccess(request, response, 0L, true);
/*      */       
/*  597 */       return false;
/*      */     }
/*      */     
/*  600 */     MessageBytes decodedURI = req.decodedURI();
/*      */     
/*  602 */     if (undecodedURI.getType() == 2)
/*      */     {
/*  604 */       decodedURI.duplicate(undecodedURI);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  609 */       parsePathParameters(req, request);
/*      */       
/*      */ 
/*      */       try
/*      */       {
/*  614 */         req.getURLDecoder().convert(decodedURI, false);
/*      */       } catch (IOException ioe) {
/*  616 */         res.setStatus(400);
/*  617 */         res.setMessage("Invalid URI: " + ioe.getMessage());
/*  618 */         this.connector.getService().getContainer().logAccess(request, response, 0L, true);
/*      */         
/*  620 */         return false;
/*      */       }
/*      */       
/*  623 */       if (!normalize(req.decodedURI())) {
/*  624 */         res.setStatus(400);
/*  625 */         res.setMessage("Invalid URI");
/*  626 */         this.connector.getService().getContainer().logAccess(request, response, 0L, true);
/*      */         
/*  628 */         return false;
/*      */       }
/*      */       
/*  631 */       convertURI(decodedURI, request);
/*      */       
/*  633 */       if (!checkNormalize(req.decodedURI())) {
/*  634 */         res.setStatus(400);
/*  635 */         res.setMessage("Invalid URI character encoding");
/*  636 */         this.connector.getService().getContainer().logAccess(request, response, 0L, true);
/*      */         
/*  638 */         return false;
/*      */ 
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*  648 */       decodedURI.toChars();
/*      */       
/*      */ 
/*  651 */       CharChunk uriCC = decodedURI.getCharChunk();
/*  652 */       int semicolon = uriCC.indexOf(';');
/*  653 */       if (semicolon > 0)
/*      */       {
/*  655 */         decodedURI.setChars(uriCC.getBuffer(), uriCC.getStart(), semicolon);
/*      */       }
/*      */     }
/*      */     
/*      */     MessageBytes serverName;
/*      */     
/*  661 */     if (this.connector.getUseIPVHosts()) {
/*  662 */       MessageBytes serverName = req.localName();
/*  663 */       if (serverName.isNull())
/*      */       {
/*  665 */         res.action(ActionCode.REQ_LOCAL_NAME_ATTRIBUTE, null);
/*      */       }
/*      */     } else {
/*  668 */       serverName = req.serverName();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  673 */     String version = null;
/*  674 */     Context versionContext = null;
/*  675 */     boolean mapRequired = true;
/*      */     
/*  677 */     while (mapRequired)
/*      */     {
/*  679 */       this.connector.getService().getMapper().map(serverName, decodedURI, version, request
/*  680 */         .getMappingData());
/*      */       
/*      */ 
/*      */ 
/*  684 */       if (request.getContext() == null) {
/*  685 */         res.setStatus(404);
/*  686 */         res.setMessage("Not found");
/*      */         
/*  688 */         Host host = request.getHost();
/*      */         
/*  690 */         if (host != null) {
/*  691 */           host.logAccess(request, response, 0L, true);
/*      */         }
/*  693 */         return false;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  701 */       if (request.getServletContext().getEffectiveSessionTrackingModes().contains(SessionTrackingMode.URL))
/*      */       {
/*      */ 
/*  704 */         String sessionID = request.getPathParameter(
/*  705 */           SessionConfig.getSessionUriParamName(request
/*  706 */           .getContext()));
/*  707 */         if (sessionID != null) {
/*  708 */           request.setRequestedSessionId(sessionID);
/*  709 */           request.setRequestedSessionURL(true);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  714 */       parseSessionCookiesId(request);
/*  715 */       parseSessionSslId(request);
/*      */       
/*  717 */       String sessionID = request.getRequestedSessionId();
/*      */       
/*  719 */       mapRequired = false;
/*  720 */       if ((version == null) || (request.getContext() != versionContext))
/*      */       {
/*      */ 
/*  723 */         version = null;
/*  724 */         versionContext = null;
/*      */         
/*  726 */         Context[] contexts = request.getMappingData().contexts;
/*      */         
/*      */ 
/*  729 */         if ((contexts != null) && (sessionID != null))
/*      */         {
/*  731 */           for (int i = contexts.length; i > 0; i--) {
/*  732 */             Context ctxt = contexts[(i - 1)];
/*  733 */             if (ctxt.getManager().findSession(sessionID) != null)
/*      */             {
/*      */ 
/*  736 */               if (ctxt.equals(request.getMappingData().context)) {
/*      */                 break;
/*      */               }
/*  739 */               version = ctxt.getWebappVersion();
/*  740 */               versionContext = ctxt;
/*      */               
/*  742 */               request.getMappingData().recycle();
/*  743 */               mapRequired = true;
/*      */               
/*      */ 
/*      */ 
/*  747 */               request.recycleSessionInfo();
/*  748 */               request.recycleCookieInfo(true); break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  756 */       if ((!mapRequired) && (request.getContext().getPaused()))
/*      */       {
/*      */ 
/*      */         try
/*      */         {
/*  761 */           Thread.sleep(1000L);
/*      */         }
/*      */         catch (InterruptedException localInterruptedException) {}
/*      */         
/*      */ 
/*  766 */         request.getMappingData().recycle();
/*  767 */         mapRequired = true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  772 */     MessageBytes redirectPathMB = request.getMappingData().redirectPath;
/*  773 */     if (!redirectPathMB.isNull()) {
/*  774 */       String redirectPath = URLEncoder.DEFAULT.encode(redirectPathMB
/*  775 */         .toString(), StandardCharsets.UTF_8);
/*  776 */       String query = request.getQueryString();
/*  777 */       if (request.isRequestedSessionIdFromURL())
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  783 */         redirectPath = redirectPath + ";" + SessionConfig.getSessionUriParamName(request.getContext()) + "=" + request.getRequestedSessionId();
/*      */       }
/*  785 */       if (query != null)
/*      */       {
/*      */ 
/*  788 */         redirectPath = redirectPath + "?" + query;
/*      */       }
/*  790 */       response.sendRedirect(redirectPath);
/*  791 */       request.getContext().logAccess(request, response, 0L, true);
/*  792 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  796 */     if ((!this.connector.getAllowTrace()) && 
/*  797 */       (req.method().equalsIgnoreCase("TRACE"))) {
/*  798 */       Wrapper wrapper = request.getWrapper();
/*  799 */       String header = null;
/*  800 */       if (wrapper != null) {
/*  801 */         String[] methods = wrapper.getServletMethods();
/*  802 */         if (methods != null) {
/*  803 */           for (int i = 0; i < methods.length; i++) {
/*  804 */             if (!"TRACE".equals(methods[i]))
/*      */             {
/*      */ 
/*  807 */               if (header == null) {
/*  808 */                 header = methods[i];
/*      */               } else
/*  810 */                 header = header + ", " + methods[i];
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*  815 */       res.setStatus(405);
/*  816 */       res.addHeader("Allow", header);
/*  817 */       res.setMessage("TRACE method is not allowed");
/*  818 */       request.getContext().logAccess(request, response, 0L, true);
/*  819 */       return false;
/*      */     }
/*      */     
/*  822 */     doConnectorAuthenticationAuthorization(req, request);
/*      */     
/*  824 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   private void doConnectorAuthenticationAuthorization(org.apache.coyote.Request req, Request request)
/*      */   {
/*  830 */     String username = req.getRemoteUser().toString();
/*  831 */     if (username != null) {
/*  832 */       if (log.isDebugEnabled()) {
/*  833 */         log.debug(sm.getString("coyoteAdapter.authenticate", new Object[] { username }));
/*      */       }
/*  835 */       if (req.getRemoteUserNeedsAuthorization()) {
/*  836 */         Authenticator authenticator = request.getContext().getAuthenticator();
/*  837 */         if (authenticator == null)
/*      */         {
/*      */ 
/*      */ 
/*  841 */           request.setUserPrincipal(new CoyotePrincipal(username));
/*  842 */         } else if (!(authenticator instanceof AuthenticatorBase)) {
/*  843 */           if (log.isDebugEnabled()) {
/*  844 */             log.debug(sm.getString("coyoteAdapter.authorize", new Object[] { username }));
/*      */           }
/*      */           
/*      */ 
/*  848 */           request.setUserPrincipal(request
/*  849 */             .getContext().getRealm().authenticate(username));
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*  858 */         request.setUserPrincipal(new CoyotePrincipal(username));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  863 */     String authtype = req.getAuthType().toString();
/*  864 */     if (authtype != null) {
/*  865 */       request.setAuthType(authtype);
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
/*      */   protected void parsePathParameters(org.apache.coyote.Request req, Request request)
/*      */   {
/*  883 */     req.decodedURI().toBytes();
/*      */     
/*  885 */     ByteChunk uriBC = req.decodedURI().getByteChunk();
/*  886 */     int semicolon = uriBC.indexOf(';', 0);
/*      */     
/*      */ 
/*  889 */     if (semicolon == -1) {
/*  890 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  895 */     Charset charset = this.connector.getURICharset();
/*      */     
/*  897 */     if (log.isDebugEnabled()) {
/*  898 */       log.debug(sm.getString("coyoteAdapter.debug", new Object[] { "uriBC", uriBC
/*  899 */         .toString() }));
/*  900 */       log.debug(sm.getString("coyoteAdapter.debug", new Object[] { "semicolon", 
/*  901 */         String.valueOf(semicolon) }));
/*  902 */       log.debug(sm.getString("coyoteAdapter.debug", new Object[] { "enc", charset.name() }));
/*      */     }
/*      */     
/*  905 */     while (semicolon > -1)
/*      */     {
/*  907 */       int start = uriBC.getStart();
/*  908 */       int end = uriBC.getEnd();
/*      */       
/*  910 */       int pathParamStart = semicolon + 1;
/*  911 */       int pathParamEnd = ByteChunk.findBytes(uriBC.getBuffer(), start + pathParamStart, end, new byte[] { 59, 47 });
/*      */       
/*      */ 
/*      */ 
/*  915 */       String pv = null;
/*      */       
/*  917 */       if (pathParamEnd >= 0) {
/*  918 */         if (charset != null) {
/*  919 */           pv = new String(uriBC.getBuffer(), start + pathParamStart, pathParamEnd - pathParamStart, charset);
/*      */         }
/*      */         
/*      */ 
/*  923 */         byte[] buf = uriBC.getBuffer();
/*  924 */         for (int i = 0; i < end - start - pathParamEnd; i++) {
/*  925 */           buf[(start + semicolon + i)] = buf[(start + i + pathParamEnd)];
/*      */         }
/*      */         
/*  928 */         uriBC.setBytes(buf, start, end - start - pathParamEnd + semicolon);
/*      */       }
/*      */       else {
/*  931 */         if (charset != null) {
/*  932 */           pv = new String(uriBC.getBuffer(), start + pathParamStart, end - start - pathParamStart, charset);
/*      */         }
/*      */         
/*  935 */         uriBC.setEnd(start + semicolon);
/*      */       }
/*      */       
/*  938 */       if (log.isDebugEnabled()) {
/*  939 */         log.debug(sm.getString("coyoteAdapter.debug", new Object[] { "pathParamStart", 
/*  940 */           String.valueOf(pathParamStart) }));
/*  941 */         log.debug(sm.getString("coyoteAdapter.debug", new Object[] { "pathParamEnd", 
/*  942 */           String.valueOf(pathParamEnd) }));
/*  943 */         log.debug(sm.getString("coyoteAdapter.debug", new Object[] { "pv", pv }));
/*      */       }
/*      */       
/*  946 */       if (pv != null) {
/*  947 */         int equals = pv.indexOf('=');
/*  948 */         if (equals > -1) {
/*  949 */           String name = pv.substring(0, equals);
/*  950 */           String value = pv.substring(equals + 1);
/*  951 */           request.addPathParameter(name, value);
/*  952 */           if (log.isDebugEnabled()) {
/*  953 */             log.debug(sm.getString("coyoteAdapter.debug", new Object[] { "equals", 
/*  954 */               String.valueOf(equals) }));
/*  955 */             log.debug(sm.getString("coyoteAdapter.debug", new Object[] { "name", name }));
/*      */             
/*  957 */             log.debug(sm.getString("coyoteAdapter.debug", new Object[] { "value", value }));
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  963 */       semicolon = uriBC.indexOf(';', semicolon);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void parseSessionSslId(Request request)
/*      */   {
/*  975 */     if ((request.getRequestedSessionId() == null) && 
/*  976 */       (SSL_ONLY.equals(request.getServletContext()
/*  977 */       .getEffectiveSessionTrackingModes())) && (request.connector.secure))
/*      */     {
/*      */ 
/*  979 */       String sessionId = (String)request.getAttribute("javax.servlet.request.ssl_session_id");
/*  980 */       if (sessionId != null) {
/*  981 */         request.setRequestedSessionId(sessionId);
/*  982 */         request.setRequestedSessionSSL(true);
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
/*      */   protected void parseSessionCookiesId(Request request)
/*      */   {
/*  999 */     Context context = request.getMappingData().context;
/* 1000 */     if ((context != null) && 
/* 1001 */       (!context.getServletContext().getEffectiveSessionTrackingModes().contains(SessionTrackingMode.COOKIE)))
/*      */     {
/* 1003 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1007 */     ServerCookies serverCookies = request.getServerCookies();
/* 1008 */     int count = serverCookies.getCookieCount();
/* 1009 */     if (count <= 0) {
/* 1010 */       return;
/*      */     }
/*      */     
/* 1013 */     String sessionCookieName = SessionConfig.getSessionCookieName(context);
/*      */     
/* 1015 */     for (int i = 0; i < count; i++) {
/* 1016 */       ServerCookie scookie = serverCookies.getCookie(i);
/* 1017 */       if (scookie.getName().equals(sessionCookieName))
/*      */       {
/* 1019 */         if (!request.isRequestedSessionIdFromCookie())
/*      */         {
/* 1021 */           convertMB(scookie.getValue());
/* 1022 */           request
/* 1023 */             .setRequestedSessionId(scookie.getValue().toString());
/* 1024 */           request.setRequestedSessionCookie(true);
/* 1025 */           request.setRequestedSessionURL(false);
/* 1026 */           if (log.isDebugEnabled()) {
/* 1027 */             log.debug(" Requested cookie session id is " + request
/* 1028 */               .getRequestedSessionId());
/*      */           }
/*      */         }
/* 1031 */         else if (!request.isRequestedSessionIdValid())
/*      */         {
/* 1033 */           convertMB(scookie.getValue());
/* 1034 */           request
/* 1035 */             .setRequestedSessionId(scookie.getValue().toString());
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
/*      */   protected void convertURI(MessageBytes uri, Request request)
/*      */     throws IOException
/*      */   {
/* 1053 */     ByteChunk bc = uri.getByteChunk();
/* 1054 */     int length = bc.getLength();
/* 1055 */     CharChunk cc = uri.getCharChunk();
/* 1056 */     cc.allocate(length, -1);
/*      */     
/* 1058 */     Charset charset = this.connector.getURICharset();
/*      */     
/* 1060 */     B2CConverter conv = request.getURIConverter();
/* 1061 */     if (conv == null) {
/* 1062 */       conv = new B2CConverter(charset, true);
/* 1063 */       request.setURIConverter(conv);
/*      */     } else {
/* 1065 */       conv.recycle();
/*      */     }
/*      */     try
/*      */     {
/* 1069 */       conv.convert(bc, cc, true);
/* 1070 */       uri.setChars(cc.getBuffer(), cc.getStart(), cc.getLength());
/*      */     }
/*      */     catch (IOException ioe)
/*      */     {
/* 1074 */       request.getResponse().sendError(400);
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
/*      */   protected void convertMB(MessageBytes mb)
/*      */   {
/* 1087 */     if (mb.getType() != 2) {
/* 1088 */       return;
/*      */     }
/*      */     
/* 1091 */     ByteChunk bc = mb.getByteChunk();
/* 1092 */     CharChunk cc = mb.getCharChunk();
/* 1093 */     int length = bc.getLength();
/* 1094 */     cc.allocate(length, -1);
/*      */     
/*      */ 
/* 1097 */     byte[] bbuf = bc.getBuffer();
/* 1098 */     char[] cbuf = cc.getBuffer();
/* 1099 */     int start = bc.getStart();
/* 1100 */     for (int i = 0; i < length; i++) {
/* 1101 */       cbuf[i] = ((char)(bbuf[(i + start)] & 0xFF));
/*      */     }
/* 1103 */     mb.setChars(cbuf, 0, length);
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
/*      */   public static boolean normalize(MessageBytes uriMB)
/*      */   {
/* 1119 */     ByteChunk uriBC = uriMB.getByteChunk();
/* 1120 */     byte[] b = uriBC.getBytes();
/* 1121 */     int start = uriBC.getStart();
/* 1122 */     int end = uriBC.getEnd();
/*      */     
/*      */ 
/* 1125 */     if (start == end) {
/* 1126 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1130 */     if ((end - start == 1) && (b[start] == 42)) {
/* 1131 */       return true;
/*      */     }
/*      */     
/* 1134 */     int pos = 0;
/* 1135 */     int index = 0;
/*      */     
/*      */ 
/*      */ 
/* 1139 */     for (pos = start; pos < end; pos++) {
/* 1140 */       if (b[pos] == 92) {
/* 1141 */         if (ALLOW_BACKSLASH) {
/* 1142 */           b[pos] = 47;
/*      */         } else {
/* 1144 */           return false;
/*      */         }
/*      */       }
/* 1147 */       if (b[pos] == 0) {
/* 1148 */         return false;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1153 */     if (b[start] != 47) {
/* 1154 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1158 */     for (pos = start; pos < end - 1; pos++) {
/* 1159 */       if (b[pos] == 47) {
/* 1160 */         while ((pos + 1 < end) && (b[(pos + 1)] == 47)) {
/* 1161 */           copyBytes(b, pos, pos + 1, end - pos - 1);
/* 1162 */           end--;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1170 */     if ((end - start >= 2) && (b[(end - 1)] == 46) && (
/* 1171 */       (b[(end - 2)] == 47) || ((b[(end - 2)] == 46) && (b[(end - 3)] == 47))))
/*      */     {
/*      */ 
/* 1174 */       b[end] = 47;
/* 1175 */       end++;
/*      */     }
/*      */     
/*      */ 
/* 1179 */     uriBC.setEnd(end);
/*      */     
/* 1181 */     index = 0;
/*      */     
/*      */     for (;;)
/*      */     {
/* 1185 */       index = uriBC.indexOf("/./", 0, 3, index);
/* 1186 */       if (index < 0) {
/*      */         break;
/*      */       }
/* 1189 */       copyBytes(b, start + index, start + index + 2, end - start - index - 2);
/*      */       
/* 1191 */       end -= 2;
/* 1192 */       uriBC.setEnd(end);
/*      */     }
/*      */     
/* 1195 */     index = 0;
/*      */     
/*      */     for (;;)
/*      */     {
/* 1199 */       index = uriBC.indexOf("/../", 0, 4, index);
/* 1200 */       if (index < 0) {
/*      */         break;
/*      */       }
/*      */       
/* 1204 */       if (index == 0) {
/* 1205 */         return false;
/*      */       }
/* 1207 */       int index2 = -1;
/* 1208 */       for (pos = start + index - 1; (pos >= 0) && (index2 < 0); pos--) {
/* 1209 */         if (b[pos] == 47) {
/* 1210 */           index2 = pos;
/*      */         }
/*      */       }
/* 1213 */       copyBytes(b, start + index2, start + index + 3, end - start - index - 3);
/*      */       
/* 1215 */       end = end + index2 - index - 3;
/* 1216 */       uriBC.setEnd(end);
/* 1217 */       index = index2;
/*      */     }
/*      */     
/* 1220 */     return true;
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
/*      */   public static boolean checkNormalize(MessageBytes uriMB)
/*      */   {
/* 1237 */     CharChunk uriCC = uriMB.getCharChunk();
/* 1238 */     char[] c = uriCC.getChars();
/* 1239 */     int start = uriCC.getStart();
/* 1240 */     int end = uriCC.getEnd();
/*      */     
/* 1242 */     int pos = 0;
/*      */     
/*      */ 
/* 1245 */     for (pos = start; pos < end; pos++) {
/* 1246 */       if (c[pos] == '\\') {
/* 1247 */         return false;
/*      */       }
/* 1249 */       if (c[pos] == 0) {
/* 1250 */         return false;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1255 */     for (pos = start; pos < end - 1; pos++) {
/* 1256 */       if ((c[pos] == '/') && 
/* 1257 */         (c[(pos + 1)] == '/')) {
/* 1258 */         return false;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1264 */     if ((end - start >= 2) && (c[(end - 1)] == '.') && (
/* 1265 */       (c[(end - 2)] == '/') || ((c[(end - 2)] == '.') && (c[(end - 3)] == '/'))))
/*      */     {
/*      */ 
/* 1268 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1273 */     if (uriCC.indexOf("/./", 0, 3, 0) >= 0) {
/* 1274 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1278 */     if (uriCC.indexOf("/../", 0, 4, 0) >= 0) {
/* 1279 */       return false;
/*      */     }
/*      */     
/* 1282 */     return true;
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
/*      */   protected static void copyBytes(byte[] b, int dest, int src, int len)
/*      */   {
/* 1300 */     for (int pos = 0; pos < len; pos++) {
/* 1301 */       b[(pos + dest)] = b[(pos + src)];
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RecycleRequiredException
/*      */     extends Exception
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\connector\CoyoteAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */