/*     */ package org.apache.catalina.session;
/*     */ 
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.Loader;
/*     */ import org.apache.catalina.Session;
/*     */ import org.apache.catalina.security.SecurityUtil;
/*     */ import org.apache.catalina.util.CustomObjectInputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardManager
/*     */   extends ManagerBase
/*     */ {
/*  61 */   private final Log log = LogFactory.getLog(StandardManager.class);
/*     */   
/*     */   protected static final String name = "StandardManager";
/*     */   
/*     */ 
/*     */   private class PrivilegedDoLoad
/*     */     implements PrivilegedExceptionAction<Void>
/*     */   {
/*     */     PrivilegedDoLoad() {}
/*     */     
/*     */     public Void run()
/*     */       throws Exception
/*     */     {
/*  74 */       StandardManager.this.doLoad();
/*  75 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class PrivilegedDoUnload
/*     */     implements PrivilegedExceptionAction<Void>
/*     */   {
/*     */     PrivilegedDoUnload() {}
/*     */     
/*     */     public Void run()
/*     */       throws Exception
/*     */     {
/*  88 */       StandardManager.this.doUnload();
/*  89 */       return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 111 */   protected String pathname = "SESSIONS.ser";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 118 */     return "StandardManager";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPathname()
/*     */   {
/* 126 */     return this.pathname;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPathname(String pathname)
/*     */   {
/* 137 */     String oldPathname = this.pathname;
/* 138 */     this.pathname = pathname;
/* 139 */     this.support.firePropertyChange("pathname", oldPathname, this.pathname);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void load()
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/* 147 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/*     */       try {
/* 149 */         AccessController.doPrivileged(new PrivilegedDoLoad());
/*     */       } catch (PrivilegedActionException ex) {
/* 151 */         Exception exception = ex.getException();
/* 152 */         if ((exception instanceof ClassNotFoundException))
/* 153 */           throw ((ClassNotFoundException)exception);
/* 154 */         if ((exception instanceof IOException)) {
/* 155 */           throw ((IOException)exception);
/*     */         }
/* 157 */         if (this.log.isDebugEnabled()) {
/* 158 */           this.log.debug("Unreported exception in load() ", exception);
/*     */         }
/*     */       }
/*     */     } else {
/* 162 */       doLoad();
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
/*     */   protected void doLoad()
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/* 177 */     if (this.log.isDebugEnabled()) {
/* 178 */       this.log.debug("Start: Loading persisted sessions");
/*     */     }
/*     */     
/*     */ 
/* 182 */     this.sessions.clear();
/*     */     
/*     */ 
/* 185 */     File file = file();
/* 186 */     if (file == null) {
/* 187 */       return;
/*     */     }
/* 189 */     if (this.log.isDebugEnabled()) {
/* 190 */       this.log.debug(sm.getString("standardManager.loading", new Object[] { this.pathname }));
/*     */     }
/* 192 */     Loader loader = null;
/* 193 */     ClassLoader classLoader = null;
/* 194 */     Log logger = null;
/* 195 */     try { FileInputStream fis = new FileInputStream(file.getAbsolutePath());Throwable localThrowable9 = null;
/* 196 */       try { BufferedInputStream bis = new BufferedInputStream(fis);Throwable localThrowable10 = null;
/* 197 */         try { Context c = getContext();
/* 198 */           loader = c.getLoader();
/* 199 */           logger = c.getLogger();
/* 200 */           if (loader != null) {
/* 201 */             classLoader = loader.getClassLoader();
/*     */           }
/* 203 */           if (classLoader == null) {
/* 204 */             classLoader = getClass().getClassLoader();
/*     */           }
/*     */           
/*     */ 
/* 208 */           synchronized (this.sessions)
/*     */           {
/*     */             try {
/* 211 */               ObjectInputStream ois = new CustomObjectInputStream(bis, classLoader, logger, getSessionAttributeValueClassNamePattern(), getWarnOnSessionAttributeFilterFailure());Throwable localThrowable11 = null;
/* 212 */               try { Integer count = (Integer)ois.readObject();
/* 213 */                 int n = count.intValue();
/* 214 */                 if (this.log.isDebugEnabled())
/* 215 */                   this.log.debug("Loading " + n + " persisted sessions");
/* 216 */                 for (int i = 0; i < n; i++) {
/* 217 */                   StandardSession session = getNewSession();
/* 218 */                   session.readObjectData(ois);
/* 219 */                   session.setManager(this);
/* 220 */                   this.sessions.put(session.getIdInternal(), session);
/* 221 */                   session.activate();
/* 222 */                   if (!session.isValidInternal())
/*     */                   {
/*     */ 
/* 225 */                     session.setValid(true);
/* 226 */                     session.expire();
/*     */                   }
/* 228 */                   this.sessionCounter += 1L;
/*     */                 }
/*     */               }
/*     */               catch (Throwable localThrowable1)
/*     */               {
/* 209 */                 localThrowable11 = localThrowable1;throw localThrowable1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               }
/*     */               finally {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             }
/*     */             finally
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 232 */               if (file.exists()) {
/* 233 */                 file.delete();
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         catch (Throwable localThrowable4)
/*     */         {
/* 195 */           localThrowable10 = localThrowable4;throw localThrowable4; } finally {} } catch (Throwable localThrowable7) { localThrowable9 = localThrowable7;throw localThrowable7;
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
/*     */       }
/*     */       finally
/*     */       {
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
/* 237 */         if (fis != null) if (localThrowable9 != null) try { fis.close(); } catch (Throwable localThrowable8) { localThrowable9.addSuppressed(localThrowable8); } else fis.close();
/* 238 */       } } catch (FileNotFoundException e) { if (this.log.isDebugEnabled()) {
/* 239 */         this.log.debug("No persisted data file found");
/*     */       }
/* 241 */       return;
/*     */     }
/*     */     
/* 244 */     if (this.log.isDebugEnabled()) {
/* 245 */       this.log.debug("Finish: Loading persisted sessions");
/*     */     }
/*     */   }
/*     */   
/*     */   public void unload()
/*     */     throws IOException
/*     */   {
/* 252 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/*     */       try {
/* 254 */         AccessController.doPrivileged(new PrivilegedDoUnload());
/*     */       } catch (PrivilegedActionException ex) {
/* 256 */         Exception exception = ex.getException();
/* 257 */         if ((exception instanceof IOException)) {
/* 258 */           throw ((IOException)exception);
/*     */         }
/* 260 */         if (this.log.isDebugEnabled()) {
/* 261 */           this.log.debug("Unreported exception in unLoad()", exception);
/*     */         }
/*     */       }
/*     */     } else {
/* 265 */       doUnload();
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
/*     */   protected void doUnload()
/*     */     throws IOException
/*     */   {
/* 279 */     if (this.log.isDebugEnabled()) {
/* 280 */       this.log.debug(sm.getString("standardManager.unloading.debug"));
/*     */     }
/* 282 */     if (this.sessions.isEmpty()) {
/* 283 */       this.log.debug(sm.getString("standardManager.unloading.nosessions"));
/* 284 */       return;
/*     */     }
/*     */     
/*     */ 
/* 288 */     File file = file();
/* 289 */     if (file == null) {
/* 290 */       return;
/*     */     }
/* 292 */     if (this.log.isDebugEnabled()) {
/* 293 */       this.log.debug(sm.getString("standardManager.unloading", new Object[] { this.pathname }));
/*     */     }
/*     */     
/*     */ 
/* 297 */     ArrayList<StandardSession> list = new ArrayList();
/*     */     
/* 299 */     FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());Throwable localThrowable10 = null;
/* 300 */     try { BufferedOutputStream bos = new BufferedOutputStream(fos);Throwable localThrowable11 = null;
/* 301 */       try { ObjectOutputStream oos = new ObjectOutputStream(bos);Throwable localThrowable12 = null;
/*     */         try {
/* 303 */           synchronized (this.sessions) {
/* 304 */             if (this.log.isDebugEnabled()) {
/* 305 */               this.log.debug("Unloading " + this.sessions.size() + " sessions");
/*     */             }
/*     */             
/* 308 */             oos.writeObject(Integer.valueOf(this.sessions.size()));
/* 309 */             Iterator<Session> elements = this.sessions.values().iterator();
/* 310 */             while (elements.hasNext())
/*     */             {
/* 312 */               StandardSession session = (StandardSession)elements.next();
/* 313 */               list.add(session);
/* 314 */               session.passivate();
/* 315 */               session.writeObjectData(oos);
/*     */             }
/*     */           }
/*     */         }
/*     */         catch (Throwable localThrowable2)
/*     */         {
/* 299 */           localThrowable12 = localThrowable2;throw localThrowable2; } finally {} } catch (Throwable localThrowable5) { localThrowable11 = localThrowable5;throw localThrowable5; } finally {} } catch (Throwable localThrowable8) { localThrowable10 = localThrowable8;throw localThrowable8;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 318 */       if (fos != null) if (localThrowable10 != null) try { fos.close(); } catch (Throwable localThrowable9) { localThrowable10.addSuppressed(localThrowable9); } else { fos.close();
/*     */         }
/*     */     }
/* 321 */     if (this.log.isDebugEnabled()) {
/* 322 */       this.log.debug("Expiring " + list.size() + " persisted sessions");
/*     */     }
/* 324 */     Iterator<StandardSession> expires = list.iterator();
/* 325 */     while (expires.hasNext()) {
/* 326 */       StandardSession session = (StandardSession)expires.next();
/*     */       try {
/* 328 */         session.expire(false);
/*     */       } catch (Throwable t) {
/* 330 */         ExceptionUtils.handleThrowable(t);
/*     */       } finally {
/* 332 */         session.recycle();
/*     */       }
/*     */     }
/*     */     
/* 336 */     if (this.log.isDebugEnabled()) {
/* 337 */       this.log.debug("Unloading complete");
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
/*     */   protected synchronized void startInternal()
/*     */     throws LifecycleException
/*     */   {
/* 352 */     super.startInternal();
/*     */     
/*     */     try
/*     */     {
/* 356 */       load();
/*     */     } catch (Throwable t) {
/* 358 */       ExceptionUtils.handleThrowable(t);
/* 359 */       this.log.error(sm.getString("standardManager.managerLoad"), t);
/*     */     }
/*     */     
/* 362 */     setState(LifecycleState.STARTING);
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
/* 376 */     if (this.log.isDebugEnabled()) {
/* 377 */       this.log.debug("Stopping");
/*     */     }
/*     */     
/* 380 */     setState(LifecycleState.STOPPING);
/*     */     
/*     */     try
/*     */     {
/* 384 */       unload();
/*     */     } catch (Throwable t) {
/* 386 */       ExceptionUtils.handleThrowable(t);
/* 387 */       this.log.error(sm.getString("standardManager.managerUnload"), t);
/*     */     }
/*     */     
/*     */ 
/* 391 */     Session[] sessions = findSessions();
/* 392 */     for (int i = 0; i < sessions.length; i++) {
/* 393 */       Session session = sessions[i];
/*     */       try {
/* 395 */         if (session.isValid()) {
/* 396 */           session.expire();
/*     */         }
/*     */       } catch (Throwable t) {
/* 399 */         ExceptionUtils.handleThrowable(t);
/*     */       }
/*     */       finally
/*     */       {
/* 403 */         session.recycle();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 408 */     super.stopInternal();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected File file()
/*     */   {
/* 420 */     if ((this.pathname == null) || (this.pathname.length() == 0)) {
/* 421 */       return null;
/*     */     }
/* 423 */     File file = new File(this.pathname);
/* 424 */     if (!file.isAbsolute()) {
/* 425 */       Context context = getContext();
/* 426 */       ServletContext servletContext = context.getServletContext();
/* 427 */       File tempdir = (File)servletContext.getAttribute("javax.servlet.context.tempdir");
/* 428 */       if (tempdir != null) {
/* 429 */         file = new File(tempdir, this.pathname);
/*     */       }
/*     */     }
/* 432 */     return file;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\session\StandardManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */