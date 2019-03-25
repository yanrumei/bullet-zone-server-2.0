/*     */ package org.apache.catalina.util;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import org.apache.catalina.Lifecycle;
/*     */ import org.apache.catalina.Lifecycle.SingleUse;
/*     */ import org.apache.catalina.LifecycleEvent;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleListener;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
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
/*     */ public abstract class LifecycleBase
/*     */   implements Lifecycle
/*     */ {
/*  41 */   private static final Log log = LogFactory.getLog(LifecycleBase.class);
/*     */   
/*  43 */   private static final StringManager sm = StringManager.getManager(LifecycleBase.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  49 */   private final List<LifecycleListener> lifecycleListeners = new CopyOnWriteArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  55 */   private volatile LifecycleState state = LifecycleState.NEW;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addLifecycleListener(LifecycleListener listener)
/*     */   {
/*  63 */     this.lifecycleListeners.add(listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LifecycleListener[] findLifecycleListeners()
/*     */   {
/*  72 */     return (LifecycleListener[])this.lifecycleListeners.toArray(new LifecycleListener[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeLifecycleListener(LifecycleListener listener)
/*     */   {
/*  81 */     this.lifecycleListeners.remove(listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void fireLifecycleEvent(String type, Object data)
/*     */   {
/*  92 */     LifecycleEvent event = new LifecycleEvent(this, type, data);
/*  93 */     for (LifecycleListener listener : this.lifecycleListeners) {
/*  94 */       listener.lifecycleEvent(event);
/*     */     }
/*     */   }
/*     */   
/*     */   public final synchronized void init()
/*     */     throws LifecycleException
/*     */   {
/* 101 */     if (!this.state.equals(LifecycleState.NEW)) {
/* 102 */       invalidTransition("before_init");
/*     */     }
/*     */     try
/*     */     {
/* 106 */       setStateInternal(LifecycleState.INITIALIZING, null, false);
/* 107 */       initInternal();
/* 108 */       setStateInternal(LifecycleState.INITIALIZED, null, false);
/*     */     } catch (Throwable t) {
/* 110 */       ExceptionUtils.handleThrowable(t);
/* 111 */       setStateInternal(LifecycleState.FAILED, null, false);
/*     */       
/* 113 */       throw new LifecycleException(sm.getString("lifecycleBase.initFail", new Object[] {toString() }), t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract void initInternal()
/*     */     throws LifecycleException;
/*     */   
/*     */ 
/*     */   public final synchronized void start()
/*     */     throws LifecycleException
/*     */   {
/* 126 */     if ((LifecycleState.STARTING_PREP.equals(this.state)) || (LifecycleState.STARTING.equals(this.state)) || 
/* 127 */       (LifecycleState.STARTED.equals(this.state)))
/*     */     {
/* 129 */       if (log.isDebugEnabled()) {
/* 130 */         Exception e = new LifecycleException();
/* 131 */         log.debug(sm.getString("lifecycleBase.alreadyStarted", new Object[] { toString() }), e);
/* 132 */       } else if (log.isInfoEnabled()) {
/* 133 */         log.info(sm.getString("lifecycleBase.alreadyStarted", new Object[] { toString() }));
/*     */       }
/*     */       
/* 136 */       return;
/*     */     }
/*     */     
/* 139 */     if (this.state.equals(LifecycleState.NEW)) {
/* 140 */       init();
/* 141 */     } else if (this.state.equals(LifecycleState.FAILED)) {
/* 142 */       stop();
/* 143 */     } else if ((!this.state.equals(LifecycleState.INITIALIZED)) && 
/* 144 */       (!this.state.equals(LifecycleState.STOPPED))) {
/* 145 */       invalidTransition("before_start");
/*     */     }
/*     */     try
/*     */     {
/* 149 */       setStateInternal(LifecycleState.STARTING_PREP, null, false);
/* 150 */       startInternal();
/* 151 */       if (this.state.equals(LifecycleState.FAILED))
/*     */       {
/*     */ 
/* 154 */         stop();
/* 155 */       } else if (!this.state.equals(LifecycleState.STARTING))
/*     */       {
/*     */ 
/* 158 */         invalidTransition("after_start");
/*     */       } else {
/* 160 */         setStateInternal(LifecycleState.STARTED, null, false);
/*     */       }
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/* 165 */       ExceptionUtils.handleThrowable(t);
/* 166 */       setStateInternal(LifecycleState.FAILED, null, false);
/* 167 */       throw new LifecycleException(sm.getString("lifecycleBase.startFail", new Object[] { toString() }), t);
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
/*     */   protected abstract void startInternal()
/*     */     throws LifecycleException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final synchronized void stop()
/*     */     throws LifecycleException
/*     */   {
/* 194 */     if ((LifecycleState.STOPPING_PREP.equals(this.state)) || (LifecycleState.STOPPING.equals(this.state)) || 
/* 195 */       (LifecycleState.STOPPED.equals(this.state)))
/*     */     {
/* 197 */       if (log.isDebugEnabled()) {
/* 198 */         Exception e = new LifecycleException();
/* 199 */         log.debug(sm.getString("lifecycleBase.alreadyStopped", new Object[] { toString() }), e);
/* 200 */       } else if (log.isInfoEnabled()) {
/* 201 */         log.info(sm.getString("lifecycleBase.alreadyStopped", new Object[] { toString() }));
/*     */       }
/*     */       
/* 204 */       return;
/*     */     }
/*     */     
/* 207 */     if (this.state.equals(LifecycleState.NEW)) {
/* 208 */       this.state = LifecycleState.STOPPED;
/* 209 */       return;
/*     */     }
/*     */     
/* 212 */     if ((!this.state.equals(LifecycleState.STARTED)) && (!this.state.equals(LifecycleState.FAILED))) {
/* 213 */       invalidTransition("before_stop");
/*     */     }
/*     */     try
/*     */     {
/* 217 */       if (this.state.equals(LifecycleState.FAILED))
/*     */       {
/*     */ 
/*     */ 
/* 221 */         fireLifecycleEvent("before_stop", null);
/*     */       } else {
/* 223 */         setStateInternal(LifecycleState.STOPPING_PREP, null, false);
/*     */       }
/*     */       
/* 226 */       stopInternal();
/*     */       
/*     */ 
/*     */ 
/* 230 */       if ((!this.state.equals(LifecycleState.STOPPING)) && (!this.state.equals(LifecycleState.FAILED))) {
/* 231 */         invalidTransition("after_stop");
/*     */       }
/*     */       
/* 234 */       setStateInternal(LifecycleState.STOPPED, null, false);
/*     */     } catch (Throwable t) {
/* 236 */       ExceptionUtils.handleThrowable(t);
/* 237 */       setStateInternal(LifecycleState.FAILED, null, false);
/* 238 */       throw new LifecycleException(sm.getString("lifecycleBase.stopFail", new Object[] { toString() }), t);
/*     */     } finally {
/* 240 */       if ((this instanceof Lifecycle.SingleUse))
/*     */       {
/* 242 */         setStateInternal(LifecycleState.STOPPED, null, false);
/* 243 */         destroy();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void stopInternal()
/*     */     throws LifecycleException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final synchronized void destroy()
/*     */     throws LifecycleException
/*     */   {
/* 261 */     if (LifecycleState.FAILED.equals(this.state)) {
/*     */       try
/*     */       {
/* 264 */         stop();
/*     */       }
/*     */       catch (LifecycleException e) {
/* 267 */         log.warn(sm.getString("lifecycleBase.destroyStopFail", new Object[] {
/* 268 */           toString() }), e);
/*     */       }
/*     */     }
/*     */     
/* 272 */     if ((LifecycleState.DESTROYING.equals(this.state)) || 
/* 273 */       (LifecycleState.DESTROYED.equals(this.state)))
/*     */     {
/* 275 */       if (log.isDebugEnabled()) {
/* 276 */         Exception e = new LifecycleException();
/* 277 */         log.debug(sm.getString("lifecycleBase.alreadyDestroyed", new Object[] { toString() }), e);
/* 278 */       } else if ((log.isInfoEnabled()) && (!(this instanceof Lifecycle.SingleUse)))
/*     */       {
/*     */ 
/*     */ 
/* 282 */         log.info(sm.getString("lifecycleBase.alreadyDestroyed", new Object[] { toString() }));
/*     */       }
/*     */       
/* 285 */       return;
/*     */     }
/*     */     
/* 288 */     if ((!this.state.equals(LifecycleState.STOPPED)) && 
/* 289 */       (!this.state.equals(LifecycleState.FAILED)) && 
/* 290 */       (!this.state.equals(LifecycleState.NEW)) && 
/* 291 */       (!this.state.equals(LifecycleState.INITIALIZED))) {
/* 292 */       invalidTransition("before_destroy");
/*     */     }
/*     */     try
/*     */     {
/* 296 */       setStateInternal(LifecycleState.DESTROYING, null, false);
/* 297 */       destroyInternal();
/* 298 */       setStateInternal(LifecycleState.DESTROYED, null, false);
/*     */     } catch (Throwable t) {
/* 300 */       ExceptionUtils.handleThrowable(t);
/* 301 */       setStateInternal(LifecycleState.FAILED, null, false);
/*     */       
/* 303 */       throw new LifecycleException(sm.getString("lifecycleBase.destroyFail", new Object[] {toString() }), t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract void destroyInternal()
/*     */     throws LifecycleException;
/*     */   
/*     */ 
/*     */   public LifecycleState getState()
/*     */   {
/* 315 */     return this.state;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getStateName()
/*     */   {
/* 324 */     return getState().toString();
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
/*     */   protected synchronized void setState(LifecycleState state)
/*     */     throws LifecycleException
/*     */   {
/* 339 */     setStateInternal(state, null, true);
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
/*     */   protected synchronized void setState(LifecycleState state, Object data)
/*     */     throws LifecycleException
/*     */   {
/* 355 */     setStateInternal(state, data, true);
/*     */   }
/*     */   
/*     */   private synchronized void setStateInternal(LifecycleState state, Object data, boolean check)
/*     */     throws LifecycleException
/*     */   {
/* 361 */     if (log.isDebugEnabled()) {
/* 362 */       log.debug(sm.getString("lifecycleBase.setState", new Object[] { this, state }));
/*     */     }
/*     */     
/* 365 */     if (check)
/*     */     {
/*     */ 
/*     */ 
/* 369 */       if (state == null) {
/* 370 */         invalidTransition("null");
/*     */         
/*     */ 
/* 373 */         return;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 380 */       if ((state != LifecycleState.FAILED) && ((this.state != LifecycleState.STARTING_PREP) || (state != LifecycleState.STARTING)) && ((this.state != LifecycleState.STOPPING_PREP) || (state != LifecycleState.STOPPING)) && ((this.state != LifecycleState.FAILED) || (state != LifecycleState.STOPPING)))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 388 */         invalidTransition(state.name());
/*     */       }
/*     */     }
/*     */     
/* 392 */     this.state = state;
/* 393 */     String lifecycleEvent = state.getLifecycleEvent();
/* 394 */     if (lifecycleEvent != null) {
/* 395 */       fireLifecycleEvent(lifecycleEvent, data);
/*     */     }
/*     */   }
/*     */   
/*     */   private void invalidTransition(String type) throws LifecycleException {
/* 400 */     String msg = sm.getString("lifecycleBase.invalidTransition", new Object[] { type, 
/* 401 */       toString(), this.state });
/* 402 */     throw new LifecycleException(msg);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\LifecycleBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */