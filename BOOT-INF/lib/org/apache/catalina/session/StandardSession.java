/*      */ package org.apache.catalina.session;
/*      */ 
/*      */ import java.beans.PropertyChangeSupport;
/*      */ import java.io.IOException;
/*      */ import java.io.NotSerializableException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.io.WriteAbortedException;
/*      */ import java.security.AccessController;
/*      */ import java.security.Principal;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import javax.servlet.ServletContext;
/*      */ import javax.servlet.http.HttpSession;
/*      */ import javax.servlet.http.HttpSessionActivationListener;
/*      */ import javax.servlet.http.HttpSessionAttributeListener;
/*      */ import javax.servlet.http.HttpSessionBindingEvent;
/*      */ import javax.servlet.http.HttpSessionBindingListener;
/*      */ import javax.servlet.http.HttpSessionContext;
/*      */ import javax.servlet.http.HttpSessionEvent;
/*      */ import javax.servlet.http.HttpSessionIdListener;
/*      */ import javax.servlet.http.HttpSessionListener;
/*      */ import org.apache.catalina.Context;
/*      */ import org.apache.catalina.Globals;
/*      */ import org.apache.catalina.Manager;
/*      */ import org.apache.catalina.Session;
/*      */ import org.apache.catalina.SessionEvent;
/*      */ import org.apache.catalina.SessionListener;
/*      */ import org.apache.catalina.TomcatPrincipal;
/*      */ import org.apache.catalina.security.SecurityUtil;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
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
/*      */ public class StandardSession
/*      */   implements HttpSession, Session, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*   93 */   protected static final boolean STRICT_SERVLET_COMPLIANCE = Globals.STRICT_SERVLET_COMPLIANCE;
/*      */   
/*   95 */   static { String activityCheck = System.getProperty("org.apache.catalina.session.StandardSession.ACTIVITY_CHECK");
/*      */     
/*   97 */     if (activityCheck == null) {
/*   98 */       ACTIVITY_CHECK = STRICT_SERVLET_COMPLIANCE;
/*      */     } else {
/*  100 */       ACTIVITY_CHECK = Boolean.parseBoolean(activityCheck);
/*      */     }
/*      */     
/*  103 */     String lastAccessAtStart = System.getProperty("org.apache.catalina.session.StandardSession.LAST_ACCESS_AT_START");
/*      */     
/*  105 */     if (lastAccessAtStart == null) {
/*  106 */       LAST_ACCESS_AT_START = STRICT_SERVLET_COMPLIANCE;
/*      */     } else {
/*  108 */       LAST_ACCESS_AT_START = Boolean.parseBoolean(lastAccessAtStart);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final boolean ACTIVITY_CHECK;
/*      */   
/*      */ 
/*      */ 
/*      */   protected static final boolean LAST_ACCESS_AT_START;
/*      */   
/*      */ 
/*      */   public StandardSession(Manager manager)
/*      */   {
/*  124 */     this.manager = manager;
/*      */     
/*      */ 
/*  127 */     if (ACTIVITY_CHECK) {
/*  128 */       this.accessCount = new AtomicInteger();
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
/*  140 */   protected static final String[] EMPTY_ARRAY = new String[0];
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  146 */   protected ConcurrentMap<String, Object> attributes = new ConcurrentHashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  154 */   protected transient String authType = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  161 */   protected long creationTime = 0L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  169 */   protected volatile transient boolean expiring = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  176 */   protected transient StandardSessionFacade facade = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  182 */   protected String id = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  188 */   protected volatile long lastAccessedTime = this.creationTime;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  194 */   protected transient ArrayList<SessionListener> listeners = new ArrayList();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  200 */   protected transient Manager manager = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  208 */   protected volatile int maxInactiveInterval = -1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  214 */   protected volatile boolean isNew = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  220 */   protected volatile boolean isValid = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  228 */   protected transient Map<String, Object> notes = new Hashtable();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  236 */   protected transient Principal principal = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  242 */   protected static final StringManager sm = StringManager.getManager(StandardSession.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*  250 */   protected static volatile HttpSessionContext sessionContext = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  257 */   protected final transient PropertyChangeSupport support = new PropertyChangeSupport(this);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  264 */   protected volatile long thisAccessedTime = this.creationTime;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  270 */   protected transient AtomicInteger accessCount = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getAuthType()
/*      */   {
/*  283 */     return this.authType;
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
/*      */   public void setAuthType(String authType)
/*      */   {
/*  297 */     String oldAuthType = this.authType;
/*  298 */     this.authType = authType;
/*  299 */     this.support.firePropertyChange("authType", oldAuthType, this.authType);
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
/*      */   public void setCreationTime(long time)
/*      */   {
/*  313 */     this.creationTime = time;
/*  314 */     this.lastAccessedTime = time;
/*  315 */     this.thisAccessedTime = time;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getId()
/*      */   {
/*  326 */     return this.id;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getIdInternal()
/*      */   {
/*  337 */     return this.id;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setId(String id)
/*      */   {
/*  349 */     setId(id, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setId(String id, boolean notify)
/*      */   {
/*  359 */     if ((this.id != null) && (this.manager != null)) {
/*  360 */       this.manager.remove(this);
/*      */     }
/*  362 */     this.id = id;
/*      */     
/*  364 */     if (this.manager != null) {
/*  365 */       this.manager.add(this);
/*      */     }
/*  367 */     if (notify) {
/*  368 */       tellNew();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void tellNew()
/*      */   {
/*  380 */     fireSessionEvent("createSession", null);
/*      */     
/*      */ 
/*  383 */     Context context = this.manager.getContext();
/*  384 */     Object[] listeners = context.getApplicationLifecycleListeners();
/*  385 */     if ((listeners != null) && (listeners.length > 0))
/*      */     {
/*  387 */       HttpSessionEvent event = new HttpSessionEvent(getSession());
/*  388 */       for (int i = 0; i < listeners.length; i++) {
/*  389 */         if ((listeners[i] instanceof HttpSessionListener))
/*      */         {
/*  391 */           HttpSessionListener listener = (HttpSessionListener)listeners[i];
/*      */           try
/*      */           {
/*  394 */             context.fireContainerEvent("beforeSessionCreated", listener);
/*      */             
/*  396 */             listener.sessionCreated(event);
/*  397 */             context.fireContainerEvent("afterSessionCreated", listener);
/*      */           } catch (Throwable t) {
/*  399 */             ExceptionUtils.handleThrowable(t);
/*      */             try {
/*  401 */               context.fireContainerEvent("afterSessionCreated", listener);
/*      */             }
/*      */             catch (Exception localException) {}
/*      */             
/*      */ 
/*      */ 
/*  407 */             this.manager.getContext().getLogger().error(sm.getString("standardSession.sessionEvent"), t);
/*      */           }
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
/*      */   public void tellChangedSessionId(String newId, String oldId, boolean notifySessionListeners, boolean notifyContainerListeners)
/*      */   {
/*  427 */     Context context = this.manager.getContext();
/*      */     
/*  429 */     if (notifyContainerListeners) {
/*  430 */       context.fireContainerEvent("changeSessionId", new String[] { oldId, newId });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  435 */     if (notifySessionListeners) {
/*  436 */       Object[] listeners = context.getApplicationEventListeners();
/*  437 */       if ((listeners != null) && (listeners.length > 0))
/*      */       {
/*  439 */         HttpSessionEvent event = new HttpSessionEvent(getSession());
/*      */         
/*  441 */         for (Object listener : listeners) {
/*  442 */           if ((listener instanceof HttpSessionIdListener))
/*      */           {
/*      */ 
/*  445 */             HttpSessionIdListener idListener = (HttpSessionIdListener)listener;
/*      */             try
/*      */             {
/*  448 */               idListener.sessionIdChanged(event, oldId);
/*      */             }
/*      */             catch (Throwable t) {
/*  451 */               this.manager.getContext().getLogger().error(sm.getString("standardSession.sessionEvent"), t);
/*      */             }
/*      */           }
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
/*      */   public long getThisAccessedTime()
/*      */   {
/*  469 */     if (!isValidInternal())
/*      */     {
/*  471 */       throw new IllegalStateException(sm.getString("standardSession.getThisAccessedTime.ise"));
/*      */     }
/*      */     
/*  474 */     return this.thisAccessedTime;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getThisAccessedTimeInternal()
/*      */   {
/*  483 */     return this.thisAccessedTime;
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
/*      */   public long getLastAccessedTime()
/*      */   {
/*  496 */     if (!isValidInternal())
/*      */     {
/*  498 */       throw new IllegalStateException(sm.getString("standardSession.getLastAccessedTime.ise"));
/*      */     }
/*      */     
/*  501 */     return this.lastAccessedTime;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getLastAccessedTimeInternal()
/*      */   {
/*  510 */     return this.lastAccessedTime;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getIdleTime()
/*      */   {
/*  519 */     if (!isValidInternal())
/*      */     {
/*  521 */       throw new IllegalStateException(sm.getString("standardSession.getIdleTime.ise"));
/*      */     }
/*      */     
/*  524 */     return getIdleTimeInternal();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getIdleTimeInternal()
/*      */   {
/*  533 */     long timeNow = System.currentTimeMillis();
/*      */     long timeIdle;
/*  535 */     long timeIdle; if (LAST_ACCESS_AT_START) {
/*  536 */       timeIdle = timeNow - this.lastAccessedTime;
/*      */     } else {
/*  538 */       timeIdle = timeNow - this.thisAccessedTime;
/*      */     }
/*  540 */     return timeIdle;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Manager getManager()
/*      */   {
/*  548 */     return this.manager;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setManager(Manager manager)
/*      */   {
/*  559 */     this.manager = manager;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxInactiveInterval()
/*      */   {
/*  571 */     return this.maxInactiveInterval;
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
/*      */   public void setMaxInactiveInterval(int interval)
/*      */   {
/*  585 */     this.maxInactiveInterval = interval;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNew(boolean isNew)
/*      */   {
/*  597 */     this.isNew = isNew;
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
/*      */   public Principal getPrincipal()
/*      */   {
/*  612 */     return this.principal;
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
/*      */   public void setPrincipal(Principal principal)
/*      */   {
/*  628 */     Principal oldPrincipal = this.principal;
/*  629 */     this.principal = principal;
/*  630 */     this.support.firePropertyChange("principal", oldPrincipal, this.principal);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpSession getSession()
/*      */   {
/*  642 */     if (this.facade == null) {
/*  643 */       if (SecurityUtil.isPackageProtectionEnabled()) {
/*  644 */         final StandardSession fsession = this;
/*  645 */         this.facade = ((StandardSessionFacade)AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public StandardSessionFacade run()
/*      */           {
/*  649 */             return new StandardSessionFacade(fsession);
/*      */           }
/*      */         }));
/*      */       } else {
/*  653 */         this.facade = new StandardSessionFacade(this);
/*      */       }
/*      */     }
/*  656 */     return this.facade;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isValid()
/*      */   {
/*  667 */     if (!this.isValid) {
/*  668 */       return false;
/*      */     }
/*      */     
/*  671 */     if (this.expiring) {
/*  672 */       return true;
/*      */     }
/*      */     
/*  675 */     if ((ACTIVITY_CHECK) && (this.accessCount.get() > 0)) {
/*  676 */       return true;
/*      */     }
/*      */     
/*  679 */     if (this.maxInactiveInterval > 0) {
/*  680 */       int timeIdle = (int)(getIdleTimeInternal() / 1000L);
/*  681 */       if (timeIdle >= this.maxInactiveInterval) {
/*  682 */         expire(true);
/*      */       }
/*      */     }
/*      */     
/*  686 */     return this.isValid;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setValid(boolean isValid)
/*      */   {
/*  697 */     this.isValid = isValid;
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
/*      */   public void access()
/*      */   {
/*  712 */     this.thisAccessedTime = System.currentTimeMillis();
/*      */     
/*  714 */     if (ACTIVITY_CHECK) {
/*  715 */       this.accessCount.incrementAndGet();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void endAccess()
/*      */   {
/*  727 */     this.isNew = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  733 */     if (LAST_ACCESS_AT_START) {
/*  734 */       this.lastAccessedTime = this.thisAccessedTime;
/*  735 */       this.thisAccessedTime = System.currentTimeMillis();
/*      */     } else {
/*  737 */       this.thisAccessedTime = System.currentTimeMillis();
/*  738 */       this.lastAccessedTime = this.thisAccessedTime;
/*      */     }
/*      */     
/*  741 */     if (ACTIVITY_CHECK) {
/*  742 */       this.accessCount.decrementAndGet();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addSessionListener(SessionListener listener)
/*      */   {
/*  754 */     this.listeners.add(listener);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void expire()
/*      */   {
/*  766 */     expire(true);
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
/*      */   public void expire(boolean notify)
/*      */   {
/*  783 */     if (!this.isValid) {
/*  784 */       return;
/*      */     }
/*  786 */     synchronized (this)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  791 */       if ((this.expiring) || (!this.isValid)) {
/*  792 */         return;
/*      */       }
/*  794 */       if (this.manager == null) {
/*  795 */         return;
/*      */       }
/*      */       
/*  798 */       this.expiring = true;
/*      */       
/*      */ 
/*      */ 
/*  802 */       Context context = this.manager.getContext();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  807 */       if (notify) {
/*  808 */         ClassLoader oldContextClassLoader = null;
/*      */         try {
/*  810 */           oldContextClassLoader = context.bind(Globals.IS_SECURITY_ENABLED, null);
/*  811 */           Object[] listeners = context.getApplicationLifecycleListeners();
/*  812 */           if ((listeners != null) && (listeners.length > 0))
/*      */           {
/*  814 */             HttpSessionEvent event = new HttpSessionEvent(getSession());
/*  815 */             for (int i = 0; i < listeners.length; i++) {
/*  816 */               int j = listeners.length - 1 - i;
/*  817 */               if ((listeners[j] instanceof HttpSessionListener))
/*      */               {
/*  819 */                 HttpSessionListener listener = (HttpSessionListener)listeners[j];
/*      */                 try
/*      */                 {
/*  822 */                   context.fireContainerEvent("beforeSessionDestroyed", listener);
/*      */                   
/*  824 */                   listener.sessionDestroyed(event);
/*  825 */                   context.fireContainerEvent("afterSessionDestroyed", listener);
/*      */                 }
/*      */                 catch (Throwable t) {
/*  828 */                   ExceptionUtils.handleThrowable(t);
/*      */                   try {
/*  830 */                     context.fireContainerEvent("afterSessionDestroyed", listener);
/*      */                   }
/*      */                   catch (Exception localException1) {}
/*      */                   
/*      */ 
/*      */ 
/*  836 */                   this.manager.getContext().getLogger().error(sm.getString("standardSession.sessionEvent"), t);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*  841 */         } finally { context.unbind(Globals.IS_SECURITY_ENABLED, oldContextClassLoader);
/*      */         }
/*      */       }
/*      */       
/*  845 */       if (ACTIVITY_CHECK) {
/*  846 */         this.accessCount.set(0);
/*      */       }
/*      */       
/*      */ 
/*  850 */       this.manager.remove(this, true);
/*      */       
/*      */ 
/*  853 */       if (notify) {
/*  854 */         fireSessionEvent("destroySession", null);
/*      */       }
/*      */       
/*      */ 
/*  858 */       if ((this.principal instanceof TomcatPrincipal)) {
/*  859 */         TomcatPrincipal gp = (TomcatPrincipal)this.principal;
/*      */         try {
/*  861 */           gp.logout();
/*      */         } catch (Exception e) {
/*  863 */           this.manager.getContext().getLogger().error(sm
/*  864 */             .getString("standardSession.logoutfail"), e);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  870 */       setValid(false);
/*  871 */       this.expiring = false;
/*      */       
/*      */ 
/*  874 */       String[] keys = keys();
/*  875 */       ClassLoader oldContextClassLoader = null;
/*      */       try {
/*  877 */         oldContextClassLoader = context.bind(Globals.IS_SECURITY_ENABLED, null);
/*  878 */         for (int i = 0; i < keys.length; i++) {
/*  879 */           removeAttributeInternal(keys[i], notify);
/*      */         }
/*      */       } finally {
/*  882 */         context.unbind(Globals.IS_SECURITY_ENABLED, oldContextClassLoader);
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
/*      */   public void passivate()
/*      */   {
/*  896 */     fireSessionEvent("passivateSession", null);
/*      */     
/*      */ 
/*  899 */     HttpSessionEvent event = null;
/*  900 */     String[] keys = keys();
/*  901 */     for (int i = 0; i < keys.length; i++) {
/*  902 */       Object attribute = this.attributes.get(keys[i]);
/*  903 */       if ((attribute instanceof HttpSessionActivationListener)) {
/*  904 */         if (event == null) {
/*  905 */           event = new HttpSessionEvent(getSession());
/*      */         }
/*      */         try {
/*  908 */           ((HttpSessionActivationListener)attribute).sessionWillPassivate(event);
/*      */         } catch (Throwable t) {
/*  910 */           ExceptionUtils.handleThrowable(t);
/*  911 */           this.manager.getContext().getLogger()
/*  912 */             .error(sm.getString("standardSession.attributeEvent"), t);
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
/*      */   public void activate()
/*      */   {
/*  927 */     if (ACTIVITY_CHECK) {
/*  928 */       this.accessCount = new AtomicInteger();
/*      */     }
/*      */     
/*      */ 
/*  932 */     fireSessionEvent("activateSession", null);
/*      */     
/*      */ 
/*  935 */     HttpSessionEvent event = null;
/*  936 */     String[] keys = keys();
/*  937 */     for (int i = 0; i < keys.length; i++) {
/*  938 */       Object attribute = this.attributes.get(keys[i]);
/*  939 */       if ((attribute instanceof HttpSessionActivationListener)) {
/*  940 */         if (event == null) {
/*  941 */           event = new HttpSessionEvent(getSession());
/*      */         }
/*      */         try {
/*  944 */           ((HttpSessionActivationListener)attribute).sessionDidActivate(event);
/*      */         } catch (Throwable t) {
/*  946 */           ExceptionUtils.handleThrowable(t);
/*  947 */           this.manager.getContext().getLogger()
/*  948 */             .error(sm.getString("standardSession.attributeEvent"), t);
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
/*      */   public Object getNote(String name)
/*      */   {
/*  965 */     return this.notes.get(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Iterator<String> getNoteNames()
/*      */   {
/*  977 */     return this.notes.keySet().iterator();
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
/*      */   public void recycle()
/*      */   {
/*  990 */     this.attributes.clear();
/*  991 */     setAuthType(null);
/*  992 */     this.creationTime = 0L;
/*  993 */     this.expiring = false;
/*  994 */     this.id = null;
/*  995 */     this.lastAccessedTime = 0L;
/*  996 */     this.maxInactiveInterval = -1;
/*  997 */     this.notes.clear();
/*  998 */     setPrincipal(null);
/*  999 */     this.isNew = false;
/* 1000 */     this.isValid = false;
/* 1001 */     this.manager = null;
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
/*      */   public void removeNote(String name)
/*      */   {
/* 1015 */     this.notes.remove(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeSessionListener(SessionListener listener)
/*      */   {
/* 1026 */     this.listeners.remove(listener);
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
/*      */   public void setNote(String name, Object value)
/*      */   {
/* 1041 */     this.notes.put(name, value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1052 */     StringBuilder sb = new StringBuilder();
/* 1053 */     sb.append("StandardSession[");
/* 1054 */     sb.append(this.id);
/* 1055 */     sb.append("]");
/* 1056 */     return sb.toString();
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
/*      */   public void readObjectData(ObjectInputStream stream)
/*      */     throws ClassNotFoundException, IOException
/*      */   {
/* 1077 */     doReadObject(stream);
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
/*      */   public void writeObjectData(ObjectOutputStream stream)
/*      */     throws IOException
/*      */   {
/* 1094 */     doWriteObject(stream);
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
/*      */   public long getCreationTime()
/*      */   {
/* 1112 */     if (!isValidInternal())
/*      */     {
/* 1114 */       throw new IllegalStateException(sm.getString("standardSession.getCreationTime.ise"));
/*      */     }
/* 1116 */     return this.creationTime;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getCreationTimeInternal()
/*      */   {
/* 1127 */     return this.creationTime;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ServletContext getServletContext()
/*      */   {
/* 1136 */     if (this.manager == null) {
/* 1137 */       return null;
/*      */     }
/* 1139 */     Context context = this.manager.getContext();
/* 1140 */     return context.getServletContext();
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
/*      */   @Deprecated
/*      */   public HttpSessionContext getSessionContext()
/*      */   {
/* 1155 */     if (sessionContext == null)
/* 1156 */       sessionContext = new StandardSessionContext();
/* 1157 */     return sessionContext;
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
/*      */   public Object getAttribute(String name)
/*      */   {
/* 1177 */     if (!isValidInternal())
/*      */     {
/* 1179 */       throw new IllegalStateException(sm.getString("standardSession.getAttribute.ise"));
/*      */     }
/* 1181 */     if (name == null) { return null;
/*      */     }
/* 1183 */     return this.attributes.get(name);
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
/*      */   public Enumeration<String> getAttributeNames()
/*      */   {
/* 1198 */     if (!isValidInternal())
/*      */     {
/* 1200 */       throw new IllegalStateException(sm.getString("standardSession.getAttributeNames.ise"));
/*      */     }
/* 1202 */     Set<String> names = new HashSet();
/* 1203 */     names.addAll(this.attributes.keySet());
/* 1204 */     return Collections.enumeration(names);
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
/*      */   @Deprecated
/*      */   public Object getValue(String name)
/*      */   {
/* 1224 */     return getAttribute(name);
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
/*      */   public String[] getValueNames()
/*      */   {
/* 1243 */     if (!isValidInternal())
/*      */     {
/* 1245 */       throw new IllegalStateException(sm.getString("standardSession.getValueNames.ise"));
/*      */     }
/* 1247 */     return keys();
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
/*      */   public void invalidate()
/*      */   {
/* 1261 */     if (!isValidInternal())
/*      */     {
/* 1263 */       throw new IllegalStateException(sm.getString("standardSession.invalidate.ise"));
/*      */     }
/*      */     
/* 1266 */     expire();
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
/*      */   public boolean isNew()
/*      */   {
/* 1284 */     if (!isValidInternal())
/*      */     {
/* 1286 */       throw new IllegalStateException(sm.getString("standardSession.isNew.ise"));
/*      */     }
/* 1288 */     return this.isNew;
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
/*      */   @Deprecated
/*      */   public void putValue(String name, Object value)
/*      */   {
/* 1315 */     setAttribute(name, value);
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
/*      */   public void removeAttribute(String name)
/*      */   {
/* 1337 */     removeAttribute(name, true);
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
/*      */   public void removeAttribute(String name, boolean notify)
/*      */   {
/* 1361 */     if (!isValidInternal())
/*      */     {
/* 1363 */       throw new IllegalStateException(sm.getString("standardSession.removeAttribute.ise"));
/*      */     }
/* 1365 */     removeAttributeInternal(name, notify);
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
/*      */   @Deprecated
/*      */   public void removeValue(String name)
/*      */   {
/* 1391 */     removeAttribute(name);
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
/*      */   public void setAttribute(String name, Object value)
/*      */   {
/* 1415 */     setAttribute(name, value, true);
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
/*      */   public void setAttribute(String name, Object value, boolean notify)
/*      */   {
/* 1438 */     if (name == null)
/*      */     {
/* 1440 */       throw new IllegalArgumentException(sm.getString("standardSession.setAttribute.namenull"));
/*      */     }
/*      */     
/* 1443 */     if (value == null) {
/* 1444 */       removeAttribute(name);
/* 1445 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1449 */     if (!isValidInternal()) {
/* 1450 */       throw new IllegalStateException(sm.getString("standardSession.setAttribute.ise", new Object[] {
/* 1451 */         getIdInternal() }));
/*      */     }
/* 1453 */     if ((this.manager != null) && (this.manager.getContext().getDistributable()) && 
/* 1454 */       (!isAttributeDistributable(name, value)) && (!exclude(name, value))) {
/* 1455 */       throw new IllegalArgumentException(sm.getString("standardSession.setAttribute.iae", new Object[] { name }));
/*      */     }
/*      */     
/*      */ 
/* 1459 */     HttpSessionBindingEvent event = null;
/*      */     
/*      */ 
/* 1462 */     if ((notify) && ((value instanceof HttpSessionBindingListener)))
/*      */     {
/* 1464 */       Object oldValue = this.attributes.get(name);
/* 1465 */       if (value != oldValue) {
/* 1466 */         event = new HttpSessionBindingEvent(getSession(), name, value);
/*      */         try {
/* 1468 */           ((HttpSessionBindingListener)value).valueBound(event);
/*      */         }
/*      */         catch (Throwable t) {
/* 1471 */           this.manager.getContext().getLogger().error(sm.getString("standardSession.bindingEvent"), t);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1477 */     Object unbound = this.attributes.put(name, value);
/*      */     
/*      */ 
/* 1480 */     if ((notify) && (unbound != null) && (unbound != value) && ((unbound instanceof HttpSessionBindingListener)))
/*      */     {
/*      */       try
/*      */       {
/* 1484 */         ((HttpSessionBindingListener)unbound).valueUnbound(new HttpSessionBindingEvent(getSession(), name));
/*      */       } catch (Throwable t) {
/* 1486 */         ExceptionUtils.handleThrowable(t);
/* 1487 */         this.manager.getContext().getLogger()
/* 1488 */           .error(sm.getString("standardSession.bindingEvent"), t);
/*      */       }
/*      */     }
/*      */     
/* 1492 */     if (!notify) { return;
/*      */     }
/*      */     
/* 1495 */     Context context = this.manager.getContext();
/* 1496 */     Object[] listeners = context.getApplicationEventListeners();
/* 1497 */     if (listeners == null)
/* 1498 */       return;
/* 1499 */     for (int i = 0; i < listeners.length; i++) {
/* 1500 */       if ((listeners[i] instanceof HttpSessionAttributeListener))
/*      */       {
/* 1502 */         HttpSessionAttributeListener listener = (HttpSessionAttributeListener)listeners[i];
/*      */         try
/*      */         {
/* 1505 */           if (unbound != null) {
/* 1506 */             context.fireContainerEvent("beforeSessionAttributeReplaced", listener);
/*      */             
/* 1508 */             if (event == null)
/*      */             {
/* 1510 */               event = new HttpSessionBindingEvent(getSession(), name, unbound);
/*      */             }
/* 1512 */             listener.attributeReplaced(event);
/* 1513 */             context.fireContainerEvent("afterSessionAttributeReplaced", listener);
/*      */           }
/*      */           else {
/* 1516 */             context.fireContainerEvent("beforeSessionAttributeAdded", listener);
/*      */             
/* 1518 */             if (event == null)
/*      */             {
/* 1520 */               event = new HttpSessionBindingEvent(getSession(), name, value);
/*      */             }
/* 1522 */             listener.attributeAdded(event);
/* 1523 */             context.fireContainerEvent("afterSessionAttributeAdded", listener);
/*      */           }
/*      */         }
/*      */         catch (Throwable t) {
/* 1527 */           ExceptionUtils.handleThrowable(t);
/*      */           try {
/* 1529 */             if (unbound != null) {
/* 1530 */               context.fireContainerEvent("afterSessionAttributeReplaced", listener);
/*      */             }
/*      */             else {
/* 1533 */               context.fireContainerEvent("afterSessionAttributeAdded", listener);
/*      */             }
/*      */           }
/*      */           catch (Exception localException) {}
/*      */           
/*      */ 
/*      */ 
/* 1540 */           this.manager.getContext().getLogger().error(sm.getString("standardSession.attributeEvent"), t);
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
/*      */   protected boolean isValidInternal()
/*      */   {
/* 1555 */     return this.isValid;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isAttributeDistributable(String name, Object value)
/*      */   {
/* 1567 */     return value instanceof Serializable;
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
/*      */   protected void doReadObject(ObjectInputStream stream)
/*      */     throws ClassNotFoundException, IOException
/*      */   {
/* 1587 */     this.authType = null;
/* 1588 */     this.creationTime = ((Long)stream.readObject()).longValue();
/* 1589 */     this.lastAccessedTime = ((Long)stream.readObject()).longValue();
/* 1590 */     this.maxInactiveInterval = ((Integer)stream.readObject()).intValue();
/* 1591 */     this.isNew = ((Boolean)stream.readObject()).booleanValue();
/* 1592 */     this.isValid = ((Boolean)stream.readObject()).booleanValue();
/* 1593 */     this.thisAccessedTime = ((Long)stream.readObject()).longValue();
/* 1594 */     this.principal = null;
/*      */     
/* 1596 */     this.id = ((String)stream.readObject());
/* 1597 */     if (this.manager.getContext().getLogger().isDebugEnabled())
/*      */     {
/* 1599 */       this.manager.getContext().getLogger().debug("readObject() loading session " + this.id);
/*      */     }
/*      */     
/* 1602 */     if (this.attributes == null)
/* 1603 */       this.attributes = new ConcurrentHashMap();
/* 1604 */     int n = ((Integer)stream.readObject()).intValue();
/* 1605 */     boolean isValidSave = this.isValid;
/* 1606 */     this.isValid = true;
/* 1607 */     for (int i = 0; i < n; i++) {
/* 1608 */       String name = (String)stream.readObject();
/*      */       try
/*      */       {
/* 1611 */         value = stream.readObject();
/*      */       } catch (WriteAbortedException wae) { Object value;
/* 1613 */         if ((wae.getCause() instanceof NotSerializableException)) {
/* 1614 */           String msg = sm.getString("standardSession.notDeserializable", new Object[] { name, this.id });
/* 1615 */           if (this.manager.getContext().getLogger().isDebugEnabled()) {
/* 1616 */             this.manager.getContext().getLogger().debug(msg, wae);
/*      */           } else {
/* 1618 */             this.manager.getContext().getLogger().warn(msg);
/*      */           }
/*      */           
/* 1621 */           continue;
/*      */         }
/* 1623 */         throw wae; }
/*      */       Object value;
/* 1625 */       if (this.manager.getContext().getLogger().isDebugEnabled()) {
/* 1626 */         this.manager.getContext().getLogger().debug("  loading attribute '" + name + "' with value '" + value + "'");
/*      */       }
/*      */       
/*      */ 
/* 1630 */       if (!exclude(name, value))
/*      */       {
/*      */ 
/* 1633 */         this.attributes.put(name, value); }
/*      */     }
/* 1635 */     this.isValid = isValidSave;
/*      */     
/* 1637 */     if (this.listeners == null) {
/* 1638 */       this.listeners = new ArrayList();
/*      */     }
/*      */     
/* 1641 */     if (this.notes == null) {
/* 1642 */       this.notes = new Hashtable();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void doWriteObject(ObjectOutputStream stream)
/*      */     throws IOException
/*      */   {
/* 1669 */     stream.writeObject(Long.valueOf(this.creationTime));
/* 1670 */     stream.writeObject(Long.valueOf(this.lastAccessedTime));
/* 1671 */     stream.writeObject(Integer.valueOf(this.maxInactiveInterval));
/* 1672 */     stream.writeObject(Boolean.valueOf(this.isNew));
/* 1673 */     stream.writeObject(Boolean.valueOf(this.isValid));
/* 1674 */     stream.writeObject(Long.valueOf(this.thisAccessedTime));
/* 1675 */     stream.writeObject(this.id);
/* 1676 */     if (this.manager.getContext().getLogger().isDebugEnabled())
/*      */     {
/* 1678 */       this.manager.getContext().getLogger().debug("writeObject() storing session " + this.id);
/*      */     }
/*      */     
/* 1681 */     String[] keys = keys();
/* 1682 */     ArrayList<String> saveNames = new ArrayList();
/* 1683 */     ArrayList<Object> saveValues = new ArrayList();
/* 1684 */     for (int i = 0; i < keys.length; i++) {
/* 1685 */       Object value = this.attributes.get(keys[i]);
/* 1686 */       if (value != null)
/*      */       {
/* 1688 */         if ((isAttributeDistributable(keys[i], value)) && (!exclude(keys[i], value))) {
/* 1689 */           saveNames.add(keys[i]);
/* 1690 */           saveValues.add(value);
/*      */         } else {
/* 1692 */           removeAttributeInternal(keys[i], true);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1697 */     int n = saveNames.size();
/* 1698 */     stream.writeObject(Integer.valueOf(n));
/* 1699 */     for (int i = 0; i < n; i++) {
/* 1700 */       stream.writeObject(saveNames.get(i));
/*      */       try {
/* 1702 */         stream.writeObject(saveValues.get(i));
/* 1703 */         if (this.manager.getContext().getLogger().isDebugEnabled())
/* 1704 */           this.manager.getContext().getLogger().debug("  storing attribute '" + 
/* 1705 */             (String)saveNames.get(i) + "' with value '" + saveValues.get(i) + "'");
/*      */       } catch (NotSerializableException e) {
/* 1707 */         this.manager.getContext().getLogger().warn(sm
/* 1708 */           .getString("standardSession.notSerializable", new Object[] {saveNames.get(i), this.id }), e);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean exclude(String name, Object value)
/*      */   {
/* 1734 */     if (Constants.excludedAttributeNames.contains(name)) {
/* 1735 */       return true;
/*      */     }
/*      */     
/*      */ 
/* 1739 */     Manager manager = getManager();
/* 1740 */     if (manager == null)
/*      */     {
/*      */ 
/* 1743 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1747 */     return !manager.willAttributeDistribute(name, value);
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
/*      */   public void fireSessionEvent(String type, Object data)
/*      */   {
/* 1762 */     if (this.listeners.size() < 1)
/* 1763 */       return;
/* 1764 */     SessionEvent event = new SessionEvent(this, type, data);
/* 1765 */     SessionListener[] list = new SessionListener[0];
/* 1766 */     synchronized (this.listeners) {
/* 1767 */       list = (SessionListener[])this.listeners.toArray(list);
/*      */     }
/*      */     
/* 1770 */     for (int i = 0; i < list.length; i++) {
/* 1771 */       list[i].sessionEvent(event);
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
/*      */   protected String[] keys()
/*      */   {
/* 1784 */     return (String[])this.attributes.keySet().toArray(EMPTY_ARRAY);
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
/*      */   protected void removeAttributeInternal(String name, boolean notify)
/*      */   {
/* 1805 */     if (name == null) { return;
/*      */     }
/*      */     
/* 1808 */     Object value = this.attributes.remove(name);
/*      */     
/*      */ 
/* 1811 */     if ((!notify) || (value == null)) {
/* 1812 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1816 */     HttpSessionBindingEvent event = null;
/* 1817 */     if ((value instanceof HttpSessionBindingListener)) {
/* 1818 */       event = new HttpSessionBindingEvent(getSession(), name, value);
/* 1819 */       ((HttpSessionBindingListener)value).valueUnbound(event);
/*      */     }
/*      */     
/*      */ 
/* 1823 */     Context context = this.manager.getContext();
/* 1824 */     Object[] listeners = context.getApplicationEventListeners();
/* 1825 */     if (listeners == null)
/* 1826 */       return;
/* 1827 */     for (int i = 0; i < listeners.length; i++) {
/* 1828 */       if ((listeners[i] instanceof HttpSessionAttributeListener))
/*      */       {
/* 1830 */         HttpSessionAttributeListener listener = (HttpSessionAttributeListener)listeners[i];
/*      */         try
/*      */         {
/* 1833 */           context.fireContainerEvent("beforeSessionAttributeRemoved", listener);
/*      */           
/* 1835 */           if (event == null)
/*      */           {
/* 1837 */             event = new HttpSessionBindingEvent(getSession(), name, value);
/*      */           }
/* 1839 */           listener.attributeRemoved(event);
/* 1840 */           context.fireContainerEvent("afterSessionAttributeRemoved", listener);
/*      */         }
/*      */         catch (Throwable t) {
/* 1843 */           ExceptionUtils.handleThrowable(t);
/*      */           try {
/* 1845 */             context.fireContainerEvent("afterSessionAttributeRemoved", listener);
/*      */           }
/*      */           catch (Exception localException) {}
/*      */           
/*      */ 
/*      */ 
/* 1851 */           this.manager.getContext().getLogger().error(sm.getString("standardSession.attributeEvent"), t);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\session\StandardSession.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */