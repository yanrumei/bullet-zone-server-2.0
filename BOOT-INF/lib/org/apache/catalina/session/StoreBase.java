/*     */ package org.apache.catalina.session;
/*     */ 
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.Manager;
/*     */ import org.apache.catalina.Store;
/*     */ import org.apache.catalina.util.CustomObjectInputStream;
/*     */ import org.apache.catalina.util.LifecycleBase;
/*     */ import org.apache.juli.logging.Log;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class StoreBase
/*     */   extends LifecycleBase
/*     */   implements Store
/*     */ {
/*     */   protected static final String storeName = "StoreBase";
/*  53 */   protected final PropertyChangeSupport support = new PropertyChangeSupport(this);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  58 */   protected static final StringManager sm = StringManager.getManager(StoreBase.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Manager manager;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getStoreName()
/*     */   {
/*  72 */     return "StoreBase";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setManager(Manager manager)
/*     */   {
/*  83 */     Manager oldManager = this.manager;
/*  84 */     this.manager = manager;
/*  85 */     this.support.firePropertyChange("manager", oldManager, this.manager);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Manager getManager()
/*     */   {
/*  93 */     return this.manager;
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
/*     */   public void addPropertyChangeListener(PropertyChangeListener listener)
/*     */   {
/* 106 */     this.support.addPropertyChangeListener(listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removePropertyChangeListener(PropertyChangeListener listener)
/*     */   {
/* 116 */     this.support.removePropertyChangeListener(listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] expiredKeys()
/*     */     throws IOException
/*     */   {
/* 128 */     return keys();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processExpires()
/*     */   {
/* 138 */     String[] keys = null;
/*     */     
/* 140 */     if (!getState().isAvailable()) {
/* 141 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 145 */       keys = expiredKeys();
/*     */     } catch (IOException e) {
/* 147 */       this.manager.getContext().getLogger().error("Error getting keys", e);
/* 148 */       return;
/*     */     }
/* 150 */     if (this.manager.getContext().getLogger().isDebugEnabled()) {
/* 151 */       this.manager.getContext().getLogger().debug(getStoreName() + ": processExpires check number of " + keys.length + " sessions");
/*     */     }
/*     */     
/* 154 */     long timeNow = System.currentTimeMillis();
/*     */     
/* 156 */     for (int i = 0; i < keys.length; i++) {
/*     */       try {
/* 158 */         StandardSession session = (StandardSession)load(keys[i]);
/* 159 */         if (session != null)
/*     */         {
/*     */ 
/* 162 */           int timeIdle = (int)((timeNow - session.getThisAccessedTime()) / 1000L);
/* 163 */           if (timeIdle >= session.getMaxInactiveInterval())
/*     */           {
/*     */ 
/* 166 */             if (this.manager.getContext().getLogger().isDebugEnabled()) {
/* 167 */               this.manager.getContext().getLogger().debug(getStoreName() + ": processExpires expire store session " + keys[i]);
/*     */             }
/* 169 */             boolean isLoaded = false;
/* 170 */             if ((this.manager instanceof PersistentManagerBase)) {
/* 171 */               isLoaded = ((PersistentManagerBase)this.manager).isLoaded(keys[i]);
/*     */             } else {
/*     */               try {
/* 174 */                 if (this.manager.findSession(keys[i]) != null) {
/* 175 */                   isLoaded = true;
/*     */                 }
/*     */               }
/*     */               catch (IOException localIOException1) {}
/*     */             }
/*     */             
/* 181 */             if (isLoaded)
/*     */             {
/* 183 */               session.recycle();
/*     */             }
/*     */             else {
/* 186 */               session.expire();
/*     */             }
/* 188 */             remove(keys[i]);
/*     */           }
/* 190 */         } } catch (Exception e) { this.manager.getContext().getLogger().error("Session: " + keys[i] + "; ", e);
/*     */         try {
/* 192 */           remove(keys[i]);
/*     */         } catch (IOException e2) {
/* 194 */           this.manager.getContext().getLogger().error("Error removing key", e2);
/*     */         }
/*     */       }
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
/*     */   protected ObjectInputStream getObjectInputStream(InputStream is)
/*     */     throws IOException
/*     */   {
/* 217 */     BufferedInputStream bis = new BufferedInputStream(is);
/*     */     
/*     */ 
/* 220 */     ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/*     */     CustomObjectInputStream ois;
/* 222 */     CustomObjectInputStream ois; if ((this.manager instanceof ManagerBase)) {
/* 223 */       ManagerBase managerBase = (ManagerBase)this.manager;
/*     */       
/*     */ 
/* 226 */       ois = new CustomObjectInputStream(bis, classLoader, this.manager.getContext().getLogger(), managerBase.getSessionAttributeValueClassNamePattern(), managerBase.getWarnOnSessionAttributeFilterFailure());
/*     */     } else {
/* 228 */       ois = new CustomObjectInputStream(bis, classLoader);
/*     */     }
/*     */     
/* 231 */     return ois;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initInternal() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected synchronized void startInternal()
/*     */     throws LifecycleException
/*     */   {
/* 251 */     setState(LifecycleState.STARTING);
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
/*     */   protected synchronized void stopInternal()
/*     */     throws LifecycleException
/*     */   {
/* 265 */     setState(LifecycleState.STOPPING);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void destroyInternal() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 280 */     StringBuilder sb = new StringBuilder(getClass().getName());
/* 281 */     sb.append('[');
/* 282 */     if (this.manager == null) {
/* 283 */       sb.append("Manager is null");
/*     */     } else {
/* 285 */       sb.append(this.manager);
/*     */     }
/* 287 */     sb.append(']');
/* 288 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\session\StoreBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */