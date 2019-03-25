/*     */ package org.springframework.boot.context.embedded.undertow;
/*     */ 
/*     */ import io.undertow.servlet.UndertowServletLogger;
/*     */ import io.undertow.servlet.api.SessionPersistenceManager;
/*     */ import io.undertow.servlet.api.SessionPersistenceManager.PersistentSession;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Date;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.springframework.core.ConfigurableObjectInputStream;
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
/*     */ public class FileSessionPersistence
/*     */   implements SessionPersistenceManager
/*     */ {
/*     */   private final File dir;
/*     */   
/*     */   public FileSessionPersistence(File dir)
/*     */   {
/*  47 */     this.dir = dir;
/*     */   }
/*     */   
/*     */   public void persistSessions(String deploymentName, Map<String, SessionPersistenceManager.PersistentSession> sessionData)
/*     */   {
/*     */     try
/*     */     {
/*  54 */       save(sessionData, getSessionFile(deploymentName));
/*     */     }
/*     */     catch (Exception ex) {
/*  57 */       UndertowServletLogger.ROOT_LOGGER.failedToPersistSessions(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private void save(Map<String, SessionPersistenceManager.PersistentSession> sessionData, File file) throws IOException
/*     */   {
/*  63 */     ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
/*     */     try {
/*  65 */       save(sessionData, stream);
/*     */     }
/*     */     finally {
/*  68 */       stream.close();
/*     */     }
/*     */   }
/*     */   
/*     */   private void save(Map<String, SessionPersistenceManager.PersistentSession> sessionData, ObjectOutputStream stream) throws IOException
/*     */   {
/*  74 */     Map<String, Serializable> session = new LinkedHashMap();
/*  75 */     for (Map.Entry<String, SessionPersistenceManager.PersistentSession> entry : sessionData.entrySet()) {
/*  76 */       session.put(entry.getKey(), new SerializablePersistentSession(
/*  77 */         (SessionPersistenceManager.PersistentSession)entry.getValue()));
/*     */     }
/*  79 */     stream.writeObject(session);
/*     */   }
/*     */   
/*     */   public Map<String, SessionPersistenceManager.PersistentSession> loadSessionAttributes(String deploymentName, ClassLoader classLoader)
/*     */   {
/*     */     try
/*     */     {
/*  86 */       File file = getSessionFile(deploymentName);
/*  87 */       if (file.exists()) {
/*  88 */         return load(file, classLoader);
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {
/*  92 */       UndertowServletLogger.ROOT_LOGGER.failedtoLoadPersistentSessions(ex);
/*     */     }
/*  94 */     return null;
/*     */   }
/*     */   
/*     */   private Map<String, SessionPersistenceManager.PersistentSession> load(File file, ClassLoader classLoader) throws IOException, ClassNotFoundException
/*     */   {
/*  99 */     ObjectInputStream stream = new ConfigurableObjectInputStream(new FileInputStream(file), classLoader);
/*     */     try
/*     */     {
/* 102 */       return load(stream);
/*     */     }
/*     */     finally {
/* 105 */       stream.close();
/*     */     }
/*     */   }
/*     */   
/*     */   private Map<String, SessionPersistenceManager.PersistentSession> load(ObjectInputStream stream) throws ClassNotFoundException, IOException
/*     */   {
/* 111 */     Map<String, SerializablePersistentSession> session = readSession(stream);
/* 112 */     long time = System.currentTimeMillis();
/* 113 */     Map<String, SessionPersistenceManager.PersistentSession> result = new LinkedHashMap();
/* 114 */     for (Map.Entry<String, SerializablePersistentSession> entry : session
/* 115 */       .entrySet()) {
/* 116 */       SessionPersistenceManager.PersistentSession entrySession = ((SerializablePersistentSession)entry.getValue()).getPersistentSession();
/* 117 */       if (entrySession.getExpiration().getTime() > time) {
/* 118 */         result.put(entry.getKey(), entrySession);
/*     */       }
/*     */     }
/* 121 */     return result;
/*     */   }
/*     */   
/*     */   private Map<String, SerializablePersistentSession> readSession(ObjectInputStream stream)
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/* 127 */     return (Map)stream.readObject();
/*     */   }
/*     */   
/*     */   private File getSessionFile(String deploymentName) {
/* 131 */     if (!this.dir.exists()) {
/* 132 */       this.dir.mkdirs();
/*     */     }
/* 134 */     return new File(this.dir, deploymentName + ".session");
/*     */   }
/*     */   
/*     */   public void clear(String deploymentName)
/*     */   {
/* 139 */     getSessionFile(deploymentName).delete();
/*     */   }
/*     */   
/*     */ 
/*     */   static class SerializablePersistentSession
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private final Date expiration;
/*     */     
/*     */     private final Map<String, Object> sessionData;
/*     */     
/*     */     SerializablePersistentSession(SessionPersistenceManager.PersistentSession session)
/*     */     {
/* 154 */       this.expiration = session.getExpiration();
/*     */       
/* 156 */       this.sessionData = new LinkedHashMap(session.getSessionData());
/*     */     }
/*     */     
/*     */     public SessionPersistenceManager.PersistentSession getPersistentSession() {
/* 160 */       return new SessionPersistenceManager.PersistentSession(this.expiration, this.sessionData);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedde\\undertow\FileSessionPersistence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */