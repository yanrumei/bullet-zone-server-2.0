/*      */ package org.apache.catalina.session;
/*      */ 
/*      */ import java.beans.PropertyChangeSupport;
/*      */ import java.io.IOException;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import org.apache.catalina.Lifecycle;
/*      */ import org.apache.catalina.LifecycleException;
/*      */ import org.apache.catalina.LifecycleState;
/*      */ import org.apache.catalina.Session;
/*      */ import org.apache.catalina.Store;
/*      */ import org.apache.catalina.StoreManager;
/*      */ import org.apache.catalina.security.SecurityUtil;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class PersistentManagerBase
/*      */   extends ManagerBase
/*      */   implements StoreManager
/*      */ {
/*   52 */   private static final Log log = LogFactory.getLog(PersistentManagerBase.class);
/*      */   
/*      */   private static final String name = "PersistentManagerBase";
/*      */   private static final String PERSISTED_LAST_ACCESSED_TIME = "org.apache.catalina.session.PersistentManagerBase.persistedLastAccessedTime";
/*      */   
/*      */   private class PrivilegedStoreClear
/*      */     implements PrivilegedExceptionAction<Void>
/*      */   {
/*      */     PrivilegedStoreClear() {}
/*      */     
/*      */     public Void run()
/*      */       throws Exception
/*      */     {
/*   65 */       PersistentManagerBase.this.store.clear();
/*   66 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */   private class PrivilegedStoreRemove implements PrivilegedExceptionAction<Void>
/*      */   {
/*      */     private String id;
/*      */     
/*      */     PrivilegedStoreRemove(String id)
/*      */     {
/*   76 */       this.id = id;
/*      */     }
/*      */     
/*      */     public Void run() throws Exception
/*      */     {
/*   81 */       PersistentManagerBase.this.store.remove(this.id);
/*   82 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */   private class PrivilegedStoreLoad implements PrivilegedExceptionAction<Session>
/*      */   {
/*      */     private String id;
/*      */     
/*      */     PrivilegedStoreLoad(String id)
/*      */     {
/*   92 */       this.id = id;
/*      */     }
/*      */     
/*      */     public Session run() throws Exception
/*      */     {
/*   97 */       return PersistentManagerBase.this.store.load(this.id);
/*      */     }
/*      */   }
/*      */   
/*      */   private class PrivilegedStoreSave implements PrivilegedExceptionAction<Void>
/*      */   {
/*      */     private Session session;
/*      */     
/*      */     PrivilegedStoreSave(Session session)
/*      */     {
/*  107 */       this.session = session;
/*      */     }
/*      */     
/*      */     public Void run() throws Exception
/*      */     {
/*  112 */       PersistentManagerBase.this.store.save(this.session);
/*  113 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private class PrivilegedStoreKeys
/*      */     implements PrivilegedExceptionAction<String[]>
/*      */   {
/*      */     PrivilegedStoreKeys() {}
/*      */     
/*      */     public String[] run()
/*      */       throws Exception
/*      */     {
/*  126 */       return PersistentManagerBase.this.store.keys();
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
/*  147 */   protected Store store = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  154 */   protected boolean saveOnRestart = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  161 */   protected int maxIdleBackup = -1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  170 */   protected int minIdleSwap = -1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  178 */   protected int maxIdleSwap = -1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  184 */   private final Map<String, Object> sessionSwapInLocks = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxIdleBackup()
/*      */   {
/*  199 */     return this.maxIdleBackup;
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
/*      */   public void setMaxIdleBackup(int backup)
/*      */   {
/*  226 */     if (backup == this.maxIdleBackup)
/*  227 */       return;
/*  228 */     int oldBackup = this.maxIdleBackup;
/*  229 */     this.maxIdleBackup = backup;
/*  230 */     this.support.firePropertyChange("maxIdleBackup", 
/*  231 */       Integer.valueOf(oldBackup), 
/*  232 */       Integer.valueOf(this.maxIdleBackup));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxIdleSwap()
/*      */   {
/*  243 */     return this.maxIdleSwap;
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
/*      */   public void setMaxIdleSwap(int max)
/*      */   {
/*  257 */     if (max == this.maxIdleSwap)
/*  258 */       return;
/*  259 */     int oldMaxIdleSwap = this.maxIdleSwap;
/*  260 */     this.maxIdleSwap = max;
/*  261 */     this.support.firePropertyChange("maxIdleSwap", 
/*  262 */       Integer.valueOf(oldMaxIdleSwap), 
/*  263 */       Integer.valueOf(this.maxIdleSwap));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMinIdleSwap()
/*      */   {
/*  274 */     return this.minIdleSwap;
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
/*      */   public void setMinIdleSwap(int min)
/*      */   {
/*  288 */     if (this.minIdleSwap == min)
/*  289 */       return;
/*  290 */     int oldMinIdleSwap = this.minIdleSwap;
/*  291 */     this.minIdleSwap = min;
/*  292 */     this.support.firePropertyChange("minIdleSwap", 
/*  293 */       Integer.valueOf(oldMinIdleSwap), 
/*  294 */       Integer.valueOf(this.minIdleSwap));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isLoaded(String id)
/*      */   {
/*      */     try
/*      */     {
/*  308 */       if (super.findSession(id) != null)
/*  309 */         return true;
/*      */     } catch (IOException e) {
/*  311 */       log.error("checking isLoaded for id, " + id + ", " + e.getMessage(), e);
/*      */     }
/*  313 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public String getName()
/*      */   {
/*  319 */     return "PersistentManagerBase";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setStore(Store store)
/*      */   {
/*  330 */     this.store = store;
/*  331 */     store.setManager(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Store getStore()
/*      */   {
/*  341 */     return this.store;
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
/*      */   public boolean getSaveOnRestart()
/*      */   {
/*  354 */     return this.saveOnRestart;
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
/*      */   public void setSaveOnRestart(boolean saveOnRestart)
/*      */   {
/*  370 */     if (saveOnRestart == this.saveOnRestart) {
/*  371 */       return;
/*      */     }
/*  373 */     boolean oldSaveOnRestart = this.saveOnRestart;
/*  374 */     this.saveOnRestart = saveOnRestart;
/*  375 */     this.support.firePropertyChange("saveOnRestart", 
/*  376 */       Boolean.valueOf(oldSaveOnRestart), 
/*  377 */       Boolean.valueOf(this.saveOnRestart));
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
/*      */   public void clearStore()
/*      */   {
/*  390 */     if (this.store == null) {
/*  391 */       return;
/*      */     }
/*      */     try {
/*  394 */       if (SecurityUtil.isPackageProtectionEnabled()) {
/*      */         try {
/*  396 */           AccessController.doPrivileged(new PrivilegedStoreClear());
/*      */         } catch (PrivilegedActionException ex) {
/*  398 */           Exception exception = ex.getException();
/*  399 */           log.error("Exception clearing the Store: " + exception, exception);
/*      */         }
/*      */         
/*      */       } else {
/*  403 */         this.store.clear();
/*      */       }
/*      */     } catch (IOException e) {
/*  406 */       log.error("Exception clearing the Store: " + e, e);
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
/*      */   public void processExpires()
/*      */   {
/*  420 */     long timeNow = System.currentTimeMillis();
/*  421 */     Session[] sessions = findSessions();
/*  422 */     int expireHere = 0;
/*  423 */     if (log.isDebugEnabled())
/*  424 */       log.debug("Start expire sessions " + getName() + " at " + timeNow + " sessioncount " + sessions.length);
/*  425 */     for (int i = 0; i < sessions.length; i++) {
/*  426 */       if (!sessions[i].isValid()) {
/*  427 */         this.expiredSessions.incrementAndGet();
/*  428 */         expireHere++;
/*      */       }
/*      */     }
/*  431 */     processPersistenceChecks();
/*  432 */     if ((getStore() instanceof StoreBase)) {
/*  433 */       ((StoreBase)getStore()).processExpires();
/*      */     }
/*      */     
/*  436 */     long timeEnd = System.currentTimeMillis();
/*  437 */     if (log.isDebugEnabled())
/*  438 */       log.debug("End expire sessions " + getName() + " processingTime " + (timeEnd - timeNow) + " expired sessions: " + expireHere);
/*  439 */     this.processingTime += timeEnd - timeNow;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void processPersistenceChecks()
/*      */   {
/*  450 */     processMaxIdleSwaps();
/*  451 */     processMaxActiveSwaps();
/*  452 */     processMaxIdleBackups();
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
/*      */   public Session findSession(String id)
/*      */     throws IOException
/*      */   {
/*  466 */     Session session = super.findSession(id);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  473 */     if (session != null) {
/*  474 */       synchronized (session) {
/*  475 */         session = super.findSession(session.getIdInternal());
/*  476 */         if (session != null)
/*      */         {
/*      */ 
/*  479 */           session.access();
/*  480 */           session.endAccess();
/*      */         }
/*      */       }
/*      */     }
/*  484 */     if (session != null) {
/*  485 */       return session;
/*      */     }
/*      */     
/*  488 */     session = swapIn(id);
/*  489 */     return session;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeSuper(Session session)
/*      */   {
/*  500 */     super.remove(session, false);
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
/*      */   public void load()
/*      */   {
/*  517 */     this.sessions.clear();
/*      */     
/*  519 */     if (this.store == null) {
/*  520 */       return;
/*      */     }
/*  522 */     String[] ids = null;
/*      */     try {
/*  524 */       if (SecurityUtil.isPackageProtectionEnabled()) {
/*      */         try {
/*  526 */           ids = (String[])AccessController.doPrivileged(new PrivilegedStoreKeys());
/*      */         }
/*      */         catch (PrivilegedActionException ex) {
/*  529 */           Exception exception = ex.getException();
/*  530 */           log.error("Exception in the Store during load: " + exception, exception);
/*      */           
/*  532 */           return;
/*      */         }
/*      */       } else {
/*  535 */         ids = this.store.keys();
/*      */       }
/*      */     } catch (IOException e) {
/*  538 */       log.error("Can't load sessions from store, " + e.getMessage(), e);
/*  539 */       return;
/*      */     }
/*      */     
/*  542 */     int n = ids.length;
/*  543 */     if (n == 0) {
/*  544 */       return;
/*      */     }
/*  546 */     if (log.isDebugEnabled()) {
/*  547 */       log.debug(sm.getString("persistentManager.loading", new Object[] { String.valueOf(n) }));
/*      */     }
/*  549 */     for (int i = 0; i < n; i++) {
/*      */       try {
/*  551 */         swapIn(ids[i]);
/*      */       } catch (IOException e) {
/*  553 */         log.error("Failed load session from store, " + e.getMessage(), e);
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
/*      */   public void remove(Session session, boolean update)
/*      */   {
/*  567 */     super.remove(session, update);
/*      */     
/*  569 */     if (this.store != null) {
/*  570 */       removeSession(session.getIdInternal());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void removeSession(String id)
/*      */   {
/*      */     try
/*      */     {
/*  583 */       if (SecurityUtil.isPackageProtectionEnabled()) {
/*      */         try {
/*  585 */           AccessController.doPrivileged(new PrivilegedStoreRemove(id));
/*      */         } catch (PrivilegedActionException ex) {
/*  587 */           Exception exception = ex.getException();
/*  588 */           log.error("Exception in the Store during removeSession: " + exception, exception);
/*      */         }
/*      */         
/*      */       } else {
/*  592 */         this.store.remove(id);
/*      */       }
/*      */     } catch (IOException e) {
/*  595 */       log.error("Exception removing session  " + e.getMessage(), e);
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
/*      */   public void unload()
/*      */   {
/*  611 */     if (this.store == null) {
/*  612 */       return;
/*      */     }
/*  614 */     Session[] sessions = findSessions();
/*  615 */     int n = sessions.length;
/*  616 */     if (n == 0) {
/*  617 */       return;
/*      */     }
/*  619 */     if (log.isDebugEnabled()) {
/*  620 */       log.debug(sm.getString("persistentManager.unloading", new Object[] {
/*  621 */         String.valueOf(n) }));
/*      */     }
/*  623 */     for (int i = 0; i < n; i++) {
/*      */       try {
/*  625 */         swapOut(sessions[i]);
/*      */       }
/*      */       catch (IOException localIOException) {}
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getActiveSessionsFull()
/*      */   {
/*  636 */     int result = getActiveSessions();
/*      */     try
/*      */     {
/*  639 */       result += getStore().getSize();
/*      */     } catch (IOException ioe) {
/*  641 */       log.warn(sm.getString("persistentManager.storeSizeException"));
/*      */     }
/*  643 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */   public Set<String> getSessionIdsFull()
/*      */   {
/*  649 */     Set<String> sessionIds = new HashSet();
/*      */     
/*  651 */     sessionIds.addAll(this.sessions.keySet());
/*      */     
/*      */     try
/*      */     {
/*  655 */       String[] storeKeys = getStore().keys();
/*  656 */       for (String storeKey : storeKeys) {
/*  657 */         sessionIds.add(storeKey);
/*      */       }
/*      */     } catch (IOException e) {
/*  660 */       log.warn(sm.getString("persistentManager.storeKeysException"));
/*      */     }
/*  662 */     return sessionIds;
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
/*      */   protected Session swapIn(String id)
/*      */     throws IOException
/*      */   {
/*  681 */     if (this.store == null) {
/*  682 */       return null;
/*      */     }
/*  684 */     Object swapInLock = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  694 */     synchronized (this) {
/*  695 */       swapInLock = this.sessionSwapInLocks.get(id);
/*  696 */       if (swapInLock == null) {
/*  697 */         swapInLock = new Object();
/*  698 */         this.sessionSwapInLocks.put(id, swapInLock);
/*      */       }
/*      */     }
/*      */     
/*  702 */     Session session = null;
/*      */     
/*  704 */     synchronized (swapInLock)
/*      */     {
/*      */ 
/*  707 */       session = (Session)this.sessions.get(id);
/*      */       
/*  709 */       if (session == null) {
/*      */         try {
/*  711 */           if (SecurityUtil.isPackageProtectionEnabled()) {
/*      */             try {
/*  713 */               session = (Session)AccessController.doPrivileged(new PrivilegedStoreLoad(id));
/*      */             }
/*      */             catch (PrivilegedActionException ex) {
/*  716 */               Exception e = ex.getException();
/*  717 */               log.error(sm.getString("persistentManager.swapInException", new Object[] { id }), e);
/*      */               
/*      */ 
/*  720 */               if ((e instanceof IOException))
/*  721 */                 throw ((IOException)e);
/*  722 */               if ((e instanceof ClassNotFoundException)) {
/*  723 */                 throw ((ClassNotFoundException)e);
/*      */               }
/*      */             }
/*      */           } else {
/*  727 */             session = this.store.load(id);
/*      */           }
/*      */         } catch (ClassNotFoundException e) {
/*  730 */           String msg = sm.getString("persistentManager.deserializeError", new Object[] { id });
/*      */           
/*  732 */           log.error(msg, e);
/*  733 */           throw new IllegalStateException(msg, e);
/*      */         }
/*      */         
/*  736 */         if ((session != null) && (!session.isValid())) {
/*  737 */           log.error(sm.getString("persistentManager.swapInInvalid", new Object[] { id }));
/*      */           
/*  739 */           session.expire();
/*  740 */           removeSession(id);
/*  741 */           session = null;
/*      */         }
/*      */         
/*  744 */         if (session != null) {
/*  745 */           if (log.isDebugEnabled()) {
/*  746 */             log.debug(sm.getString("persistentManager.swapIn", new Object[] { id }));
/*      */           }
/*  748 */           session.setManager(this);
/*      */           
/*  750 */           ((StandardSession)session).tellNew();
/*  751 */           add(session);
/*  752 */           ((StandardSession)session).activate();
/*      */           
/*      */ 
/*      */ 
/*  756 */           session.access();
/*  757 */           session.endAccess();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  763 */     synchronized (this) {
/*  764 */       this.sessionSwapInLocks.remove(id);
/*      */     }
/*      */     
/*  767 */     return session;
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
/*      */   protected void swapOut(Session session)
/*      */     throws IOException
/*      */   {
/*  783 */     if ((this.store == null) || (!session.isValid())) {
/*  784 */       return;
/*      */     }
/*      */     
/*  787 */     ((StandardSession)session).passivate();
/*  788 */     writeSession(session);
/*  789 */     super.remove(session, true);
/*  790 */     session.recycle();
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
/*      */   protected void writeSession(Session session)
/*      */     throws IOException
/*      */   {
/*  804 */     if ((this.store == null) || (!session.isValid())) {
/*  805 */       return;
/*      */     }
/*      */     try
/*      */     {
/*  809 */       if (SecurityUtil.isPackageProtectionEnabled()) {
/*      */         try {
/*  811 */           AccessController.doPrivileged(new PrivilegedStoreSave(session));
/*      */         } catch (PrivilegedActionException ex) {
/*  813 */           Exception exception = ex.getException();
/*  814 */           if ((exception instanceof IOException)) {
/*  815 */             throw ((IOException)exception);
/*      */           }
/*  817 */           log.error("Exception in the Store during writeSession: " + exception, exception);
/*      */         }
/*      */         
/*      */       } else {
/*  821 */         this.store.save(session);
/*      */       }
/*      */     } catch (IOException e) {
/*  824 */       log.error(sm
/*  825 */         .getString("persistentManager.serializeError", new Object[] {session.getIdInternal(), e }));
/*  826 */       throw e;
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
/*      */   protected synchronized void startInternal()
/*      */     throws LifecycleException
/*      */   {
/*  842 */     super.startInternal();
/*      */     
/*  844 */     if (this.store == null) {
/*  845 */       log.error("No Store configured, persistence disabled");
/*  846 */     } else if ((this.store instanceof Lifecycle)) {
/*  847 */       ((Lifecycle)this.store).start();
/*      */     }
/*  849 */     setState(LifecycleState.STARTING);
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
/*  863 */     if (log.isDebugEnabled()) {
/*  864 */       log.debug("Stopping");
/*      */     }
/*  866 */     setState(LifecycleState.STOPPING);
/*      */     
/*  868 */     if ((getStore() != null) && (this.saveOnRestart)) {
/*  869 */       unload();
/*      */     }
/*      */     else {
/*  872 */       Session[] sessions = findSessions();
/*  873 */       for (int i = 0; i < sessions.length; i++) {
/*  874 */         StandardSession session = (StandardSession)sessions[i];
/*  875 */         if (session.isValid())
/*      */         {
/*  877 */           session.expire();
/*      */         }
/*      */       }
/*      */     }
/*  881 */     if ((getStore() instanceof Lifecycle)) {
/*  882 */       ((Lifecycle)getStore()).stop();
/*      */     }
/*      */     
/*      */ 
/*  886 */     super.stopInternal();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void processMaxIdleSwaps()
/*      */   {
/*  898 */     if ((!getState().isAvailable()) || (this.maxIdleSwap < 0)) {
/*  899 */       return;
/*      */     }
/*  901 */     Session[] sessions = findSessions();
/*      */     
/*      */ 
/*  904 */     if (this.maxIdleSwap >= 0) {
/*  905 */       for (int i = 0; i < sessions.length; i++) {
/*  906 */         StandardSession session = (StandardSession)sessions[i];
/*  907 */         synchronized (session) {
/*  908 */           if (session.isValid())
/*      */           {
/*  910 */             int timeIdle = (int)(session.getIdleTimeInternal() / 1000L);
/*  911 */             if ((timeIdle >= this.maxIdleSwap) && (timeIdle >= this.minIdleSwap)) {
/*  912 */               if ((session.accessCount != null) && 
/*  913 */                 (session.accessCount.get() > 0)) {
/*      */                 continue;
/*      */               }
/*      */               
/*  917 */               if (log.isDebugEnabled())
/*  918 */                 log.debug(sm
/*  919 */                   .getString("persistentManager.swapMaxIdle", new Object[] {session
/*  920 */                   .getIdInternal(), 
/*  921 */                   Integer.valueOf(timeIdle) }));
/*      */               try {
/*  923 */                 swapOut(session);
/*      */               }
/*      */               catch (IOException localIOException) {}
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
/*      */   protected void processMaxActiveSwaps()
/*      */   {
/*  940 */     if ((!getState().isAvailable()) || (getMaxActiveSessions() < 0)) {
/*  941 */       return;
/*      */     }
/*  943 */     Session[] sessions = findSessions();
/*      */     
/*      */ 
/*  946 */     int limit = (int)(getMaxActiveSessions() * 0.9D);
/*      */     
/*  948 */     if (limit >= sessions.length) {
/*  949 */       return;
/*      */     }
/*  951 */     if (log.isDebugEnabled()) {
/*  952 */       log.debug(sm
/*  953 */         .getString("persistentManager.tooManyActive", new Object[] {
/*  954 */         Integer.valueOf(sessions.length) }));
/*      */     }
/*  956 */     int toswap = sessions.length - limit;
/*      */     
/*  958 */     for (int i = 0; (i < sessions.length) && (toswap > 0); i++) {
/*  959 */       StandardSession session = (StandardSession)sessions[i];
/*  960 */       synchronized (session) {
/*  961 */         int timeIdle = (int)(session.getIdleTimeInternal() / 1000L);
/*  962 */         if (timeIdle >= this.minIdleSwap) {
/*  963 */           if ((session.accessCount != null) && 
/*  964 */             (session.accessCount.get() > 0)) {
/*      */             continue;
/*      */           }
/*      */           
/*  968 */           if (log.isDebugEnabled())
/*  969 */             log.debug(sm
/*  970 */               .getString("persistentManager.swapTooManyActive", new Object[] {session
/*  971 */               .getIdInternal(), 
/*  972 */               Integer.valueOf(timeIdle) }));
/*      */           try {
/*  974 */             swapOut(session);
/*      */           }
/*      */           catch (IOException localIOException) {}
/*      */           
/*  978 */           toswap--;
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
/*      */   protected void processMaxIdleBackups()
/*      */   {
/*  991 */     if ((!getState().isAvailable()) || (this.maxIdleBackup < 0)) {
/*  992 */       return;
/*      */     }
/*  994 */     Session[] sessions = findSessions();
/*      */     
/*      */ 
/*  997 */     if (this.maxIdleBackup >= 0) {
/*  998 */       for (int i = 0; i < sessions.length; i++) {
/*  999 */         StandardSession session = (StandardSession)sessions[i];
/* 1000 */         synchronized (session) {
/* 1001 */           if (session.isValid())
/*      */           {
/* 1003 */             long lastAccessedTime = session.getLastAccessedTimeInternal();
/*      */             
/* 1005 */             Long persistedLastAccessedTime = (Long)session.getNote("org.apache.catalina.session.PersistentManagerBase.persistedLastAccessedTime");
/* 1006 */             if ((persistedLastAccessedTime == null) || 
/* 1007 */               (lastAccessedTime != persistedLastAccessedTime.longValue()))
/*      */             {
/* 1009 */               int timeIdle = (int)(session.getIdleTimeInternal() / 1000L);
/* 1010 */               if (timeIdle >= this.maxIdleBackup) {
/* 1011 */                 if (log.isDebugEnabled()) {
/* 1012 */                   log.debug(sm
/* 1013 */                     .getString("persistentManager.backupMaxIdle", new Object[] {session
/* 1014 */                     .getIdInternal(), 
/* 1015 */                     Integer.valueOf(timeIdle) }));
/*      */                 }
/*      */                 try {
/* 1018 */                   writeSession(session);
/*      */                 }
/*      */                 catch (IOException localIOException) {}
/*      */                 
/* 1022 */                 session.setNote("org.apache.catalina.session.PersistentManagerBase.persistedLastAccessedTime", 
/* 1023 */                   Long.valueOf(lastAccessedTime));
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\session\PersistentManagerBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */