/*      */ package org.apache.catalina.session;
/*      */ 
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.beans.PropertyChangeSupport;
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.Deque;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import java.util.regex.PatternSyntaxException;
/*      */ import javax.servlet.http.HttpSession;
/*      */ import org.apache.catalina.Container;
/*      */ import org.apache.catalina.Context;
/*      */ import org.apache.catalina.Engine;
/*      */ import org.apache.catalina.Globals;
/*      */ import org.apache.catalina.Lifecycle;
/*      */ import org.apache.catalina.LifecycleException;
/*      */ import org.apache.catalina.LifecycleState;
/*      */ import org.apache.catalina.Manager;
/*      */ import org.apache.catalina.Session;
/*      */ import org.apache.catalina.SessionIdGenerator;
/*      */ import org.apache.catalina.util.LifecycleMBeanBase;
/*      */ import org.apache.catalina.util.SessionIdGeneratorBase;
/*      */ import org.apache.catalina.util.StandardSessionIdGenerator;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
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
/*      */ public abstract class ManagerBase
/*      */   extends LifecycleMBeanBase
/*      */   implements Manager
/*      */ {
/*   64 */   private final Log log = LogFactory.getLog(ManagerBase.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Context context;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final String name = "ManagerBase";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   87 */   protected String secureRandomClass = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   98 */   protected String secureRandomAlgorithm = "SHA1PRNG";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  108 */   protected String secureRandomProvider = null;
/*      */   
/*  110 */   protected SessionIdGenerator sessionIdGenerator = null;
/*  111 */   protected Class<? extends SessionIdGenerator> sessionIdGeneratorClass = null;
/*      */   
/*      */ 
/*      */   protected volatile int sessionMaxAliveTime;
/*      */   
/*      */ 
/*  117 */   private final Object sessionMaxAliveTimeUpdateLock = new Object();
/*      */   
/*      */ 
/*      */   protected static final int TIMING_STATS_CACHE_SIZE = 100;
/*      */   
/*  122 */   protected final Deque<SessionTiming> sessionCreationTiming = new LinkedList();
/*      */   
/*      */ 
/*  125 */   protected final Deque<SessionTiming> sessionExpirationTiming = new LinkedList();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  131 */   protected final AtomicLong expiredSessions = new AtomicLong(0L);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  138 */   protected Map<String, Session> sessions = new ConcurrentHashMap();
/*      */   
/*      */ 
/*  141 */   protected long sessionCounter = 0L;
/*      */   
/*  143 */   protected volatile int maxActive = 0;
/*      */   
/*  145 */   private final Object maxActiveUpdateLock = new Object();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  150 */   protected int maxActiveSessions = -1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  155 */   protected int rejectedSessions = 0;
/*      */   
/*      */ 
/*  158 */   protected volatile int duplicates = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  163 */   protected long processingTime = 0L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  168 */   private int count = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  177 */   protected int processExpiresFrequency = 6;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  182 */   protected static final StringManager sm = StringManager.getManager(ManagerBase.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  187 */   protected final PropertyChangeSupport support = new PropertyChangeSupport(this);
/*      */   
/*      */ 
/*      */   private Pattern sessionAttributeNamePattern;
/*      */   
/*      */ 
/*      */   private Pattern sessionAttributeValueClassNamePattern;
/*      */   
/*      */   private boolean warnOnSessionAttributeFilterFailure;
/*      */   
/*      */ 
/*      */   public ManagerBase()
/*      */   {
/*  200 */     if (Globals.IS_SECURITY_ENABLED)
/*      */     {
/*      */ 
/*  203 */       setSessionAttributeValueClassNameFilter("java\\.lang\\.(?:Boolean|Integer|Long|Number|String)");
/*      */       
/*  205 */       setWarnOnSessionAttributeFilterFailure(true);
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
/*      */   public String getSessionAttributeNameFilter()
/*      */   {
/*  223 */     if (this.sessionAttributeNamePattern == null) {
/*  224 */       return null;
/*      */     }
/*  226 */     return this.sessionAttributeNamePattern.toString();
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
/*      */   public void setSessionAttributeNameFilter(String sessionAttributeNameFilter)
/*      */     throws PatternSyntaxException
/*      */   {
/*  244 */     if ((sessionAttributeNameFilter == null) || (sessionAttributeNameFilter.length() == 0)) {
/*  245 */       this.sessionAttributeNamePattern = null;
/*      */     } else {
/*  247 */       this.sessionAttributeNamePattern = Pattern.compile(sessionAttributeNameFilter);
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
/*      */   protected Pattern getSessionAttributeNamePattern()
/*      */   {
/*  260 */     return this.sessionAttributeNamePattern;
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
/*      */   public String getSessionAttributeValueClassNameFilter()
/*      */   {
/*  275 */     if (this.sessionAttributeValueClassNamePattern == null) {
/*  276 */       return null;
/*      */     }
/*  278 */     return this.sessionAttributeValueClassNamePattern.toString();
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
/*      */   protected Pattern getSessionAttributeValueClassNamePattern()
/*      */   {
/*  291 */     return this.sessionAttributeValueClassNamePattern;
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
/*      */   public void setSessionAttributeValueClassNameFilter(String sessionAttributeValueClassNameFilter)
/*      */     throws PatternSyntaxException
/*      */   {
/*  310 */     if ((sessionAttributeValueClassNameFilter == null) || 
/*  311 */       (sessionAttributeValueClassNameFilter.length() == 0)) {
/*  312 */       this.sessionAttributeValueClassNamePattern = null;
/*      */     }
/*      */     else {
/*  315 */       this.sessionAttributeValueClassNamePattern = Pattern.compile(sessionAttributeValueClassNameFilter);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getWarnOnSessionAttributeFilterFailure()
/*      */   {
/*  327 */     return this.warnOnSessionAttributeFilterFailure;
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
/*      */   public void setWarnOnSessionAttributeFilterFailure(boolean warnOnSessionAttributeFilterFailure)
/*      */   {
/*  341 */     this.warnOnSessionAttributeFilterFailure = warnOnSessionAttributeFilterFailure;
/*      */   }
/*      */   
/*      */ 
/*      */   public Context getContext()
/*      */   {
/*  347 */     return this.context;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setContext(Context context)
/*      */   {
/*  353 */     if (this.context == context)
/*      */     {
/*  355 */       return;
/*      */     }
/*  357 */     if (!getState().equals(LifecycleState.NEW)) {
/*  358 */       throw new IllegalStateException(sm.getString("managerBase.setContextNotNew"));
/*      */     }
/*  360 */     Context oldContext = this.context;
/*  361 */     this.context = context;
/*  362 */     this.support.firePropertyChange("context", oldContext, this.context);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getClassName()
/*      */   {
/*  370 */     return getClass().getName();
/*      */   }
/*      */   
/*      */ 
/*      */   public SessionIdGenerator getSessionIdGenerator()
/*      */   {
/*  376 */     if (this.sessionIdGenerator != null)
/*  377 */       return this.sessionIdGenerator;
/*  378 */     if (this.sessionIdGeneratorClass != null) {
/*      */       try {
/*  380 */         this.sessionIdGenerator = ((SessionIdGenerator)this.sessionIdGeneratorClass.getConstructor(new Class[0]).newInstance(new Object[0]));
/*  381 */         return this.sessionIdGenerator;
/*      */       }
/*      */       catch (ReflectiveOperationException localReflectiveOperationException) {}
/*      */     }
/*      */     
/*  386 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setSessionIdGenerator(SessionIdGenerator sessionIdGenerator)
/*      */   {
/*  392 */     this.sessionIdGenerator = sessionIdGenerator;
/*  393 */     this.sessionIdGeneratorClass = sessionIdGenerator.getClass();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getName()
/*      */   {
/*  402 */     return "ManagerBase";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSecureRandomClass()
/*      */   {
/*  411 */     return this.secureRandomClass;
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
/*      */   public void setSecureRandomClass(String secureRandomClass)
/*      */   {
/*  424 */     String oldSecureRandomClass = this.secureRandomClass;
/*  425 */     this.secureRandomClass = secureRandomClass;
/*  426 */     this.support.firePropertyChange("secureRandomClass", oldSecureRandomClass, this.secureRandomClass);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSecureRandomAlgorithm()
/*      */   {
/*  436 */     return this.secureRandomAlgorithm;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSecureRandomAlgorithm(String secureRandomAlgorithm)
/*      */   {
/*  447 */     this.secureRandomAlgorithm = secureRandomAlgorithm;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSecureRandomProvider()
/*      */   {
/*  455 */     return this.secureRandomProvider;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSecureRandomProvider(String secureRandomProvider)
/*      */   {
/*  466 */     this.secureRandomProvider = secureRandomProvider;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getRejectedSessions()
/*      */   {
/*  472 */     return this.rejectedSessions;
/*      */   }
/*      */   
/*      */ 
/*      */   public long getExpiredSessions()
/*      */   {
/*  478 */     return this.expiredSessions.get();
/*      */   }
/*      */   
/*      */ 
/*      */   public void setExpiredSessions(long expiredSessions)
/*      */   {
/*  484 */     this.expiredSessions.set(expiredSessions);
/*      */   }
/*      */   
/*      */   public long getProcessingTime() {
/*  488 */     return this.processingTime;
/*      */   }
/*      */   
/*      */   public void setProcessingTime(long processingTime)
/*      */   {
/*  493 */     this.processingTime = processingTime;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getProcessExpiresFrequency()
/*      */   {
/*  501 */     return this.processExpiresFrequency;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setProcessExpiresFrequency(int processExpiresFrequency)
/*      */   {
/*  512 */     if (processExpiresFrequency <= 0) {
/*  513 */       return;
/*      */     }
/*      */     
/*  516 */     int oldProcessExpiresFrequency = this.processExpiresFrequency;
/*  517 */     this.processExpiresFrequency = processExpiresFrequency;
/*  518 */     this.support.firePropertyChange("processExpiresFrequency", 
/*  519 */       Integer.valueOf(oldProcessExpiresFrequency), 
/*  520 */       Integer.valueOf(this.processExpiresFrequency));
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
/*      */   public void backgroundProcess()
/*      */   {
/*  533 */     this.count = ((this.count + 1) % this.processExpiresFrequency);
/*  534 */     if (this.count == 0) {
/*  535 */       processExpires();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void processExpires()
/*      */   {
/*  543 */     long timeNow = System.currentTimeMillis();
/*  544 */     Session[] sessions = findSessions();
/*  545 */     int expireHere = 0;
/*      */     
/*  547 */     if (this.log.isDebugEnabled())
/*  548 */       this.log.debug("Start expire sessions " + getName() + " at " + timeNow + " sessioncount " + sessions.length);
/*  549 */     for (int i = 0; i < sessions.length; i++) {
/*  550 */       if ((sessions[i] != null) && (!sessions[i].isValid())) {
/*  551 */         expireHere++;
/*      */       }
/*      */     }
/*  554 */     long timeEnd = System.currentTimeMillis();
/*  555 */     if (this.log.isDebugEnabled())
/*  556 */       this.log.debug("End expire sessions " + getName() + " processingTime " + (timeEnd - timeNow) + " expired sessions: " + expireHere);
/*  557 */     this.processingTime += timeEnd - timeNow;
/*      */   }
/*      */   
/*      */ 
/*      */   protected void initInternal()
/*      */     throws LifecycleException
/*      */   {
/*  564 */     super.initInternal();
/*      */     
/*  566 */     if (this.context == null) {
/*  567 */       throw new LifecycleException(sm.getString("managerBase.contextNull"));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void startInternal()
/*      */     throws LifecycleException
/*      */   {
/*  577 */     while (this.sessionCreationTiming.size() < 100) {
/*  578 */       this.sessionCreationTiming.add(null);
/*      */     }
/*  580 */     while (this.sessionExpirationTiming.size() < 100) {
/*  581 */       this.sessionExpirationTiming.add(null);
/*      */     }
/*      */     
/*      */ 
/*  585 */     SessionIdGenerator sessionIdGenerator = getSessionIdGenerator();
/*  586 */     if (sessionIdGenerator == null) {
/*  587 */       sessionIdGenerator = new StandardSessionIdGenerator();
/*  588 */       setSessionIdGenerator(sessionIdGenerator);
/*      */     }
/*      */     
/*  591 */     sessionIdGenerator.setJvmRoute(getJvmRoute());
/*  592 */     if ((sessionIdGenerator instanceof SessionIdGeneratorBase)) {
/*  593 */       SessionIdGeneratorBase sig = (SessionIdGeneratorBase)sessionIdGenerator;
/*  594 */       sig.setSecureRandomAlgorithm(getSecureRandomAlgorithm());
/*  595 */       sig.setSecureRandomClass(getSecureRandomClass());
/*  596 */       sig.setSecureRandomProvider(getSecureRandomProvider());
/*      */     }
/*      */     
/*  599 */     if ((sessionIdGenerator instanceof Lifecycle)) {
/*  600 */       ((Lifecycle)sessionIdGenerator).start();
/*      */     }
/*      */     else {
/*  603 */       if (this.log.isDebugEnabled())
/*  604 */         this.log.debug("Force random number initialization starting");
/*  605 */       sessionIdGenerator.generateSessionId();
/*  606 */       if (this.log.isDebugEnabled()) {
/*  607 */         this.log.debug("Force random number initialization completed");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void stopInternal() throws LifecycleException
/*      */   {
/*  614 */     if ((this.sessionIdGenerator instanceof Lifecycle)) {
/*  615 */       ((Lifecycle)this.sessionIdGenerator).stop();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void add(Session session)
/*      */   {
/*  622 */     this.sessions.put(session.getIdInternal(), session);
/*  623 */     int size = getActiveSessions();
/*  624 */     if (size > this.maxActive) {
/*  625 */       synchronized (this.maxActiveUpdateLock) {
/*  626 */         if (size > this.maxActive) {
/*  627 */           this.maxActive = size;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void addPropertyChangeListener(PropertyChangeListener listener)
/*      */   {
/*  636 */     this.support.addPropertyChangeListener(listener);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Session createSession(String sessionId)
/*      */   {
/*  643 */     if ((this.maxActiveSessions >= 0) && 
/*  644 */       (getActiveSessions() >= this.maxActiveSessions)) {
/*  645 */       this.rejectedSessions += 1;
/*      */       
/*  647 */       throw new TooManyActiveSessionsException(sm.getString("managerBase.createSession.ise"), this.maxActiveSessions);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  652 */     Session session = createEmptySession();
/*      */     
/*      */ 
/*  655 */     session.setNew(true);
/*  656 */     session.setValid(true);
/*  657 */     session.setCreationTime(System.currentTimeMillis());
/*  658 */     session.setMaxInactiveInterval(getContext().getSessionTimeout() * 60);
/*  659 */     String id = sessionId;
/*  660 */     if (id == null) {
/*  661 */       id = generateSessionId();
/*      */     }
/*  663 */     session.setId(id);
/*  664 */     this.sessionCounter += 1L;
/*      */     
/*  666 */     SessionTiming timing = new SessionTiming(session.getCreationTime(), 0);
/*  667 */     synchronized (this.sessionCreationTiming) {
/*  668 */       this.sessionCreationTiming.add(timing);
/*  669 */       this.sessionCreationTiming.poll();
/*      */     }
/*  671 */     return session;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Session createEmptySession()
/*      */   {
/*  678 */     return getNewSession();
/*      */   }
/*      */   
/*      */   public Session findSession(String id)
/*      */     throws IOException
/*      */   {
/*  684 */     if (id == null) {
/*  685 */       return null;
/*      */     }
/*  687 */     return (Session)this.sessions.get(id);
/*      */   }
/*      */   
/*      */ 
/*      */   public Session[] findSessions()
/*      */   {
/*  693 */     return (Session[])this.sessions.values().toArray(new Session[0]);
/*      */   }
/*      */   
/*      */ 
/*      */   public void remove(Session session)
/*      */   {
/*  699 */     remove(session, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void remove(Session session, boolean update)
/*      */   {
/*  707 */     if (update) {
/*  708 */       long timeNow = System.currentTimeMillis();
/*      */       
/*  710 */       int timeAlive = (int)(timeNow - session.getCreationTimeInternal()) / 1000;
/*  711 */       updateSessionMaxAliveTime(timeAlive);
/*  712 */       this.expiredSessions.incrementAndGet();
/*  713 */       SessionTiming timing = new SessionTiming(timeNow, timeAlive);
/*  714 */       synchronized (this.sessionExpirationTiming) {
/*  715 */         this.sessionExpirationTiming.add(timing);
/*  716 */         this.sessionExpirationTiming.poll();
/*      */       }
/*      */     }
/*      */     
/*  720 */     if (session.getIdInternal() != null) {
/*  721 */       this.sessions.remove(session.getIdInternal());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void removePropertyChangeListener(PropertyChangeListener listener)
/*      */   {
/*  728 */     this.support.removePropertyChangeListener(listener);
/*      */   }
/*      */   
/*      */ 
/*      */   public void changeSessionId(Session session)
/*      */   {
/*  734 */     String newId = generateSessionId();
/*  735 */     changeSessionId(session, newId, true, true);
/*      */   }
/*      */   
/*      */ 
/*      */   public void changeSessionId(Session session, String newId)
/*      */   {
/*  741 */     changeSessionId(session, newId, true, true);
/*      */   }
/*      */   
/*      */ 
/*      */   protected void changeSessionId(Session session, String newId, boolean notifySessionListeners, boolean notifyContainerListeners)
/*      */   {
/*  747 */     String oldId = session.getIdInternal();
/*  748 */     session.setId(newId, false);
/*  749 */     session.tellChangedSessionId(newId, oldId, notifySessionListeners, notifyContainerListeners);
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
/*      */   public boolean willAttributeDistribute(String name, Object value)
/*      */   {
/*  764 */     Pattern sessionAttributeNamePattern = getSessionAttributeNamePattern();
/*  765 */     if ((sessionAttributeNamePattern != null) && 
/*  766 */       (!sessionAttributeNamePattern.matcher(name).matches())) {
/*  767 */       if ((getWarnOnSessionAttributeFilterFailure()) || (this.log.isDebugEnabled())) {
/*  768 */         String msg = sm.getString("managerBase.sessionAttributeNameFilter", new Object[] { name, sessionAttributeNamePattern });
/*      */         
/*  770 */         if (getWarnOnSessionAttributeFilterFailure()) {
/*  771 */           this.log.warn(msg);
/*      */         } else {
/*  773 */           this.log.debug(msg);
/*      */         }
/*      */       }
/*  776 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  780 */     Pattern sessionAttributeValueClassNamePattern = getSessionAttributeValueClassNamePattern();
/*  781 */     if ((value != null) && (sessionAttributeValueClassNamePattern != null))
/*      */     {
/*  783 */       if (!sessionAttributeValueClassNamePattern.matcher(value.getClass().getName()).matches()) {
/*  784 */         if ((getWarnOnSessionAttributeFilterFailure()) || (this.log.isDebugEnabled())) {
/*  785 */           String msg = sm.getString("managerBase.sessionAttributeValueClassNameFilter", new Object[] { name, value
/*  786 */             .getClass().getName(), sessionAttributeValueClassNamePattern });
/*  787 */           if (getWarnOnSessionAttributeFilterFailure()) {
/*  788 */             this.log.warn(msg);
/*      */           } else {
/*  790 */             this.log.debug(msg);
/*      */           }
/*      */         }
/*  793 */         return false;
/*      */       }
/*      */     }
/*      */     
/*  797 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected StandardSession getNewSession()
/*      */   {
/*  809 */     return new StandardSession(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String generateSessionId()
/*      */   {
/*  819 */     String result = null;
/*      */     do
/*      */     {
/*  822 */       if (result != null)
/*      */       {
/*      */ 
/*      */ 
/*  826 */         this.duplicates += 1;
/*      */       }
/*      */       
/*  829 */       result = this.sessionIdGenerator.generateSessionId();
/*      */     }
/*  831 */     while (this.sessions.containsKey(result));
/*      */     
/*  833 */     return result;
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
/*      */   public Engine getEngine()
/*      */   {
/*  846 */     Engine e = null;
/*  847 */     for (Container c = getContext(); (e == null) && (c != null); c = c.getParent()) {
/*  848 */       if ((c instanceof Engine)) {
/*  849 */         e = (Engine)c;
/*      */       }
/*      */     }
/*  852 */     return e;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getJvmRoute()
/*      */   {
/*  861 */     Engine e = getEngine();
/*  862 */     return e == null ? null : e.getJvmRoute();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSessionCounter(long sessionCounter)
/*      */   {
/*  871 */     this.sessionCounter = sessionCounter;
/*      */   }
/*      */   
/*      */ 
/*      */   public long getSessionCounter()
/*      */   {
/*  877 */     return this.sessionCounter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getDuplicates()
/*      */   {
/*  888 */     return this.duplicates;
/*      */   }
/*      */   
/*      */   public void setDuplicates(int duplicates)
/*      */   {
/*  893 */     this.duplicates = duplicates;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getActiveSessions()
/*      */   {
/*  899 */     return this.sessions.size();
/*      */   }
/*      */   
/*      */ 
/*      */   public int getMaxActive()
/*      */   {
/*  905 */     return this.maxActive;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setMaxActive(int maxActive)
/*      */   {
/*  911 */     synchronized (this.maxActiveUpdateLock) {
/*  912 */       this.maxActive = maxActive;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxActiveSessions()
/*      */   {
/*  923 */     return this.maxActiveSessions;
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
/*      */   public void setMaxActiveSessions(int max)
/*      */   {
/*  936 */     int oldMaxActiveSessions = this.maxActiveSessions;
/*  937 */     this.maxActiveSessions = max;
/*  938 */     this.support.firePropertyChange("maxActiveSessions", 
/*  939 */       Integer.valueOf(oldMaxActiveSessions), 
/*  940 */       Integer.valueOf(this.maxActiveSessions));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getSessionMaxAliveTime()
/*      */   {
/*  947 */     return this.sessionMaxAliveTime;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setSessionMaxAliveTime(int sessionMaxAliveTime)
/*      */   {
/*  953 */     synchronized (this.sessionMaxAliveTimeUpdateLock) {
/*  954 */       this.sessionMaxAliveTime = sessionMaxAliveTime;
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
/*      */   public void updateSessionMaxAliveTime(int sessionAliveTime)
/*      */   {
/*  967 */     if (sessionAliveTime > this.sessionMaxAliveTime) {
/*  968 */       synchronized (this.sessionMaxAliveTimeUpdateLock) {
/*  969 */         if (sessionAliveTime > this.sessionMaxAliveTime) {
/*  970 */           this.sessionMaxAliveTime = sessionAliveTime;
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
/*      */   public int getSessionAverageAliveTime()
/*      */   {
/*  985 */     List<SessionTiming> copy = new ArrayList();
/*  986 */     synchronized (this.sessionExpirationTiming) {
/*  987 */       copy.addAll(this.sessionExpirationTiming);
/*      */     }
/*      */     
/*      */ 
/*  991 */     int counter = 0;
/*  992 */     int result = 0;
/*  993 */     Iterator<SessionTiming> iter = copy.iterator();
/*      */     
/*      */ 
/*  996 */     while (iter.hasNext()) {
/*  997 */       SessionTiming timing = (SessionTiming)iter.next();
/*  998 */       if (timing != null) {
/*  999 */         int timeAlive = timing.getDuration();
/* 1000 */         counter++;
/*      */         
/* 1002 */         result = result * ((counter - 1) / counter) + timeAlive / counter;
/*      */       }
/*      */     }
/*      */     
/* 1006 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getSessionCreateRate()
/*      */   {
/* 1018 */     List<SessionTiming> copy = new ArrayList();
/* 1019 */     synchronized (this.sessionCreationTiming) {
/* 1020 */       copy.addAll(this.sessionCreationTiming);
/*      */     }
/*      */     
/* 1023 */     return calculateRate(copy);
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
/*      */   public int getSessionExpireRate()
/*      */   {
/* 1038 */     List<SessionTiming> copy = new ArrayList();
/* 1039 */     synchronized (this.sessionExpirationTiming) {
/* 1040 */       copy.addAll(this.sessionExpirationTiming);
/*      */     }
/*      */     
/* 1043 */     return calculateRate(copy);
/*      */   }
/*      */   
/*      */ 
/*      */   private static int calculateRate(List<SessionTiming> sessionTiming)
/*      */   {
/* 1049 */     long now = System.currentTimeMillis();
/* 1050 */     long oldest = now;
/* 1051 */     int counter = 0;
/* 1052 */     int result = 0;
/* 1053 */     Iterator<SessionTiming> iter = sessionTiming.iterator();
/*      */     
/*      */ 
/* 1056 */     while (iter.hasNext()) {
/* 1057 */       SessionTiming timing = (SessionTiming)iter.next();
/* 1058 */       if (timing != null) {
/* 1059 */         counter++;
/* 1060 */         if (timing.getTimestamp() < oldest) {
/* 1061 */           oldest = timing.getTimestamp();
/*      */         }
/*      */       }
/*      */     }
/* 1065 */     if (counter > 0) {
/* 1066 */       if (oldest < now) {
/* 1067 */         result = 60000 * counter / (int)(now - oldest);
/*      */       }
/*      */       else {
/* 1070 */         result = Integer.MAX_VALUE;
/*      */       }
/*      */     }
/* 1073 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String listSessionIds()
/*      */   {
/* 1083 */     StringBuilder sb = new StringBuilder();
/* 1084 */     Iterator<String> keys = this.sessions.keySet().iterator();
/* 1085 */     while (keys.hasNext()) {
/* 1086 */       sb.append((String)keys.next()).append(" ");
/*      */     }
/* 1088 */     return sb.toString();
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
/*      */   public String getSessionAttribute(String sessionId, String key)
/*      */   {
/* 1102 */     Session s = (Session)this.sessions.get(sessionId);
/* 1103 */     if (s == null) {
/* 1104 */       if (this.log.isInfoEnabled())
/* 1105 */         this.log.info("Session not found " + sessionId);
/* 1106 */       return null;
/*      */     }
/* 1108 */     Object o = s.getSession().getAttribute(key);
/* 1109 */     if (o == null) return null;
/* 1110 */     return o.toString();
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
/*      */   public HashMap<String, String> getSession(String sessionId)
/*      */   {
/* 1127 */     Session s = (Session)this.sessions.get(sessionId);
/* 1128 */     if (s == null) {
/* 1129 */       if (this.log.isInfoEnabled()) {
/* 1130 */         this.log.info("Session not found " + sessionId);
/*      */       }
/* 1132 */       return null;
/*      */     }
/*      */     
/* 1135 */     Enumeration<String> ee = s.getSession().getAttributeNames();
/* 1136 */     if ((ee == null) || (!ee.hasMoreElements())) {
/* 1137 */       return null;
/*      */     }
/*      */     
/* 1140 */     HashMap<String, String> map = new HashMap();
/* 1141 */     while (ee.hasMoreElements()) {
/* 1142 */       String attrName = (String)ee.nextElement();
/* 1143 */       map.put(attrName, getSessionAttribute(sessionId, attrName));
/*      */     }
/*      */     
/* 1146 */     return map;
/*      */   }
/*      */   
/*      */   public void expireSession(String sessionId)
/*      */   {
/* 1151 */     Session s = (Session)this.sessions.get(sessionId);
/* 1152 */     if (s == null) {
/* 1153 */       if (this.log.isInfoEnabled())
/* 1154 */         this.log.info("Session not found " + sessionId);
/* 1155 */       return;
/*      */     }
/* 1157 */     s.expire();
/*      */   }
/*      */   
/*      */   public long getThisAccessedTimestamp(String sessionId) {
/* 1161 */     Session s = (Session)this.sessions.get(sessionId);
/* 1162 */     if (s == null)
/* 1163 */       return -1L;
/* 1164 */     return s.getThisAccessedTime();
/*      */   }
/*      */   
/*      */   public String getThisAccessedTime(String sessionId) {
/* 1168 */     Session s = (Session)this.sessions.get(sessionId);
/* 1169 */     if (s == null) {
/* 1170 */       if (this.log.isInfoEnabled())
/* 1171 */         this.log.info("Session not found " + sessionId);
/* 1172 */       return "";
/*      */     }
/* 1174 */     return new Date(s.getThisAccessedTime()).toString();
/*      */   }
/*      */   
/*      */   public long getLastAccessedTimestamp(String sessionId) {
/* 1178 */     Session s = (Session)this.sessions.get(sessionId);
/* 1179 */     if (s == null)
/* 1180 */       return -1L;
/* 1181 */     return s.getLastAccessedTime();
/*      */   }
/*      */   
/*      */   public String getLastAccessedTime(String sessionId) {
/* 1185 */     Session s = (Session)this.sessions.get(sessionId);
/* 1186 */     if (s == null) {
/* 1187 */       if (this.log.isInfoEnabled())
/* 1188 */         this.log.info("Session not found " + sessionId);
/* 1189 */       return "";
/*      */     }
/* 1191 */     return new Date(s.getLastAccessedTime()).toString();
/*      */   }
/*      */   
/*      */   public String getCreationTime(String sessionId) {
/* 1195 */     Session s = (Session)this.sessions.get(sessionId);
/* 1196 */     if (s == null) {
/* 1197 */       if (this.log.isInfoEnabled())
/* 1198 */         this.log.info("Session not found " + sessionId);
/* 1199 */       return "";
/*      */     }
/* 1201 */     return new Date(s.getCreationTime()).toString();
/*      */   }
/*      */   
/*      */   public long getCreationTimestamp(String sessionId) {
/* 1205 */     Session s = (Session)this.sessions.get(sessionId);
/* 1206 */     if (s == null)
/* 1207 */       return -1L;
/* 1208 */     return s.getCreationTime();
/*      */   }
/*      */   
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1214 */     StringBuilder sb = new StringBuilder(getClass().getName());
/* 1215 */     sb.append('[');
/* 1216 */     if (this.context == null) {
/* 1217 */       sb.append("Context is null");
/*      */     } else {
/* 1219 */       sb.append(this.context.getName());
/*      */     }
/* 1221 */     sb.append(']');
/* 1222 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getObjectNameKeyProperties()
/*      */   {
/* 1230 */     StringBuilder name = new StringBuilder("type=Manager");
/*      */     
/* 1232 */     name.append(",host=");
/* 1233 */     name.append(this.context.getParent().getName());
/*      */     
/* 1235 */     name.append(",context=");
/* 1236 */     String contextName = this.context.getName();
/* 1237 */     if (!contextName.startsWith("/")) {
/* 1238 */       name.append('/');
/*      */     }
/* 1240 */     name.append(contextName);
/*      */     
/* 1242 */     return name.toString();
/*      */   }
/*      */   
/*      */   public String getDomainInternal()
/*      */   {
/* 1247 */     return this.context.getDomain();
/*      */   }
/*      */   
/*      */ 
/*      */   protected static final class SessionTiming
/*      */   {
/*      */     private final long timestamp;
/*      */     private final int duration;
/*      */     
/*      */     public SessionTiming(long timestamp, int duration)
/*      */     {
/* 1258 */       this.timestamp = timestamp;
/* 1259 */       this.duration = duration;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public long getTimestamp()
/*      */     {
/* 1267 */       return this.timestamp;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public int getDuration()
/*      */     {
/* 1275 */       return this.duration;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\session\ManagerBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */