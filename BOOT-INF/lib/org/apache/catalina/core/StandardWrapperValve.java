/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import javax.servlet.DispatcherType;
/*     */ import javax.servlet.Servlet;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.UnavailableException;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.connector.ClientAbortException;
/*     */ import org.apache.catalina.connector.Request;
/*     */ import org.apache.catalina.connector.Response;
/*     */ import org.apache.catalina.valves.ValveBase;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
/*     */ import org.apache.tomcat.util.log.SystemLogHandler;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ final class StandardWrapperValve
/*     */   extends ValveBase
/*     */ {
/*     */   private volatile long processingTime;
/*     */   private volatile long maxTime;
/*     */   
/*     */   public StandardWrapperValve()
/*     */   {
/*  55 */     super(true);
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
/*  66 */   private volatile long minTime = Long.MAX_VALUE;
/*  67 */   private final AtomicInteger requestCount = new AtomicInteger(0);
/*  68 */   private final AtomicInteger errorCount = new AtomicInteger(0);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  75 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.core");
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
/*     */   public final void invoke(Request request, Response response)
/*     */     throws IOException, ServletException
/*     */   {
/*  96 */     boolean unavailable = false;
/*  97 */     Throwable throwable = null;
/*     */     
/*  99 */     long t1 = System.currentTimeMillis();
/* 100 */     this.requestCount.incrementAndGet();
/* 101 */     StandardWrapper wrapper = (StandardWrapper)getContainer();
/* 102 */     Servlet servlet = null;
/* 103 */     Context context = (Context)wrapper.getParent();
/*     */     
/*     */ 
/* 106 */     if (!context.getState().isAvailable()) {
/* 107 */       response.sendError(503, sm
/* 108 */         .getString("standardContext.isUnavailable"));
/* 109 */       unavailable = true;
/*     */     }
/*     */     
/*     */ 
/* 113 */     if ((!unavailable) && (wrapper.isUnavailable())) {
/* 114 */       this.container.getLogger().info(sm.getString("standardWrapper.isUnavailable", new Object[] {wrapper
/* 115 */         .getName() }));
/* 116 */       long available = wrapper.getAvailable();
/* 117 */       if ((available > 0L) && (available < Long.MAX_VALUE)) {
/* 118 */         response.setDateHeader("Retry-After", available);
/* 119 */         response.sendError(503, sm
/* 120 */           .getString("standardWrapper.isUnavailable", new Object[] {wrapper
/* 121 */           .getName() }));
/* 122 */       } else if (available == Long.MAX_VALUE) {
/* 123 */         response.sendError(404, sm
/* 124 */           .getString("standardWrapper.notFound", new Object[] {wrapper
/* 125 */           .getName() }));
/*     */       }
/* 127 */       unavailable = true;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 132 */       if (!unavailable) {
/* 133 */         servlet = wrapper.allocate();
/*     */       }
/*     */     } catch (UnavailableException e) {
/* 136 */       this.container.getLogger().error(sm
/* 137 */         .getString("standardWrapper.allocateException", new Object[] {wrapper
/* 138 */         .getName() }), e);
/* 139 */       long available = wrapper.getAvailable();
/* 140 */       if ((available > 0L) && (available < Long.MAX_VALUE)) {
/* 141 */         response.setDateHeader("Retry-After", available);
/* 142 */         response.sendError(503, sm
/* 143 */           .getString("standardWrapper.isUnavailable", new Object[] {wrapper
/* 144 */           .getName() }));
/* 145 */       } else if (available == Long.MAX_VALUE) {
/* 146 */         response.sendError(404, sm
/* 147 */           .getString("standardWrapper.notFound", new Object[] {wrapper
/* 148 */           .getName() }));
/*     */       }
/*     */     } catch (ServletException e) {
/* 151 */       this.container.getLogger().error(sm.getString("standardWrapper.allocateException", new Object[] {wrapper
/* 152 */         .getName() }), StandardWrapper.getRootCause(e));
/* 153 */       throwable = e;
/* 154 */       exception(request, response, e);
/*     */     } catch (Throwable e) {
/* 156 */       ExceptionUtils.handleThrowable(e);
/* 157 */       this.container.getLogger().error(sm.getString("standardWrapper.allocateException", new Object[] {wrapper
/* 158 */         .getName() }), e);
/* 159 */       throwable = e;
/* 160 */       exception(request, response, e);
/* 161 */       servlet = null;
/*     */     }
/*     */     
/* 164 */     MessageBytes requestPathMB = request.getRequestPathMB();
/* 165 */     DispatcherType dispatcherType = DispatcherType.REQUEST;
/* 166 */     if (request.getDispatcherType() == DispatcherType.ASYNC) dispatcherType = DispatcherType.ASYNC;
/* 167 */     request.setAttribute("org.apache.catalina.core.DISPATCHER_TYPE", dispatcherType);
/* 168 */     request.setAttribute("org.apache.catalina.core.DISPATCHER_REQUEST_PATH", requestPathMB);
/*     */     
/*     */ 
/*     */ 
/* 172 */     ApplicationFilterChain filterChain = ApplicationFilterFactory.createFilterChain(request, wrapper, servlet);
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 177 */       if ((servlet != null) && (filterChain != null))
/*     */       {
/* 179 */         if (context.getSwallowOutput()) {
/*     */           try {
/* 181 */             SystemLogHandler.startCapture();
/* 182 */             if (request.isAsyncDispatching()) {
/* 183 */               request.getAsyncContextInternal().doInternalDispatch();
/*     */             } else
/* 185 */               filterChain.doFilter(request.getRequest(), response
/* 186 */                 .getResponse());
/*     */           } finally {
/*     */             String log;
/* 189 */             String log = SystemLogHandler.stopCapture();
/* 190 */             if ((log != null) && (log.length() > 0)) {
/* 191 */               context.getLogger().info(log);
/*     */             }
/*     */             
/*     */           }
/* 195 */         } else if (request.isAsyncDispatching()) {
/* 196 */           request.getAsyncContextInternal().doInternalDispatch();
/*     */         }
/*     */         else {
/* 199 */           filterChain.doFilter(request.getRequest(), response.getResponse());
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (ClientAbortException e)
/*     */     {
/* 205 */       throwable = e;
/* 206 */       exception(request, response, e);
/*     */     } catch (IOException e) {
/* 208 */       this.container.getLogger().error(sm.getString("standardWrapper.serviceException", new Object[] {wrapper
/* 209 */         .getName(), context
/* 210 */         .getName() }), e);
/* 211 */       throwable = e;
/* 212 */       exception(request, response, e);
/*     */     } catch (UnavailableException e) {
/* 214 */       this.container.getLogger().error(sm.getString("standardWrapper.serviceException", new Object[] {wrapper
/* 215 */         .getName(), context
/* 216 */         .getName() }), e);
/*     */       
/*     */ 
/* 219 */       wrapper.unavailable(e);
/* 220 */       long available = wrapper.getAvailable();
/* 221 */       if ((available > 0L) && (available < Long.MAX_VALUE)) {
/* 222 */         response.setDateHeader("Retry-After", available);
/* 223 */         response.sendError(503, sm
/* 224 */           .getString("standardWrapper.isUnavailable", new Object[] {wrapper
/* 225 */           .getName() }));
/* 226 */       } else if (available == Long.MAX_VALUE) {
/* 227 */         response.sendError(404, sm
/* 228 */           .getString("standardWrapper.notFound", new Object[] {wrapper
/* 229 */           .getName() }));
/*     */       }
/*     */     }
/*     */     catch (ServletException e)
/*     */     {
/* 234 */       Throwable rootCause = StandardWrapper.getRootCause(e);
/* 235 */       if (!(rootCause instanceof ClientAbortException)) {
/* 236 */         this.container.getLogger().error(sm.getString("standardWrapper.serviceExceptionRoot", new Object[] {wrapper
/*     */         
/* 238 */           .getName(), context.getName(), e.getMessage() }), rootCause);
/*     */       }
/*     */       
/* 241 */       throwable = e;
/* 242 */       exception(request, response, e);
/*     */     } catch (Throwable e) {
/* 244 */       ExceptionUtils.handleThrowable(e);
/* 245 */       this.container.getLogger().error(sm.getString("standardWrapper.serviceException", new Object[] {wrapper
/* 246 */         .getName(), context
/* 247 */         .getName() }), e);
/* 248 */       throwable = e;
/* 249 */       exception(request, response, e);
/*     */     }
/*     */     
/*     */ 
/* 253 */     if (filterChain != null) {
/* 254 */       filterChain.release();
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 259 */       if (servlet != null) {
/* 260 */         wrapper.deallocate(servlet);
/*     */       }
/*     */     } catch (Throwable e) {
/* 263 */       ExceptionUtils.handleThrowable(e);
/* 264 */       this.container.getLogger().error(sm.getString("standardWrapper.deallocateException", new Object[] {wrapper
/* 265 */         .getName() }), e);
/* 266 */       if (throwable == null) {
/* 267 */         throwable = e;
/* 268 */         exception(request, response, e);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 275 */       if ((servlet != null) && 
/* 276 */         (wrapper.getAvailable() == Long.MAX_VALUE)) {
/* 277 */         wrapper.unload();
/*     */       }
/*     */     } catch (Throwable e) {
/* 280 */       ExceptionUtils.handleThrowable(e);
/* 281 */       this.container.getLogger().error(sm.getString("standardWrapper.unloadException", new Object[] {wrapper
/* 282 */         .getName() }), e);
/* 283 */       if (throwable == null) {
/* 284 */         throwable = e;
/* 285 */         exception(request, response, e);
/*     */       }
/*     */     }
/* 288 */     long t2 = System.currentTimeMillis();
/*     */     
/* 290 */     long time = t2 - t1;
/* 291 */     this.processingTime += time;
/* 292 */     if (time > this.maxTime) this.maxTime = time;
/* 293 */     if (time < this.minTime) { this.minTime = time;
/*     */     }
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
/*     */ 
/*     */ 
/*     */   private void exception(Request request, Response response, Throwable exception)
/*     */   {
/* 313 */     request.setAttribute("javax.servlet.error.exception", exception);
/* 314 */     response.setStatus(500);
/* 315 */     response.setError();
/*     */   }
/*     */   
/*     */   public long getProcessingTime() {
/* 319 */     return this.processingTime;
/*     */   }
/*     */   
/*     */   public long getMaxTime() {
/* 323 */     return this.maxTime;
/*     */   }
/*     */   
/*     */   public long getMinTime() {
/* 327 */     return this.minTime;
/*     */   }
/*     */   
/*     */   public int getRequestCount() {
/* 331 */     return this.requestCount.get();
/*     */   }
/*     */   
/*     */   public int getErrorCount() {
/* 335 */     return this.errorCount.get();
/*     */   }
/*     */   
/*     */   public void incrementErrorCount() {
/* 339 */     this.errorCount.incrementAndGet();
/*     */   }
/*     */   
/*     */   protected void initInternal()
/*     */     throws LifecycleException
/*     */   {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\StandardWrapperValve.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */