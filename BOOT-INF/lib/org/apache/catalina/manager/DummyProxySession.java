/*     */ package org.apache.catalina.manager;
/*     */ 
/*     */ import java.security.Principal;
/*     */ import java.util.Iterator;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.apache.catalina.Manager;
/*     */ import org.apache.catalina.Session;
/*     */ import org.apache.catalina.SessionListener;
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
/*     */ public class DummyProxySession
/*     */   implements Session
/*     */ {
/*     */   private String sessionId;
/*     */   
/*     */   public DummyProxySession(String sessionId)
/*     */   {
/*  33 */     this.sessionId = sessionId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void access() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addSessionListener(SessionListener listener) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void endAccess() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void expire() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public String getAuthType()
/*     */   {
/*  58 */     return null;
/*     */   }
/*     */   
/*     */   public long getCreationTime()
/*     */   {
/*  63 */     return 0L;
/*     */   }
/*     */   
/*     */   public long getCreationTimeInternal()
/*     */   {
/*  68 */     return 0L;
/*     */   }
/*     */   
/*     */   public String getId()
/*     */   {
/*  73 */     return this.sessionId;
/*     */   }
/*     */   
/*     */   public String getIdInternal()
/*     */   {
/*  78 */     return this.sessionId;
/*     */   }
/*     */   
/*     */   public long getLastAccessedTime()
/*     */   {
/*  83 */     return 0L;
/*     */   }
/*     */   
/*     */   public long getLastAccessedTimeInternal()
/*     */   {
/*  88 */     return 0L;
/*     */   }
/*     */   
/*     */   public long getIdleTime()
/*     */   {
/*  93 */     return 0L;
/*     */   }
/*     */   
/*     */   public long getIdleTimeInternal()
/*     */   {
/*  98 */     return 0L;
/*     */   }
/*     */   
/*     */   public Manager getManager()
/*     */   {
/* 103 */     return null;
/*     */   }
/*     */   
/*     */   public int getMaxInactiveInterval()
/*     */   {
/* 108 */     return 0;
/*     */   }
/*     */   
/*     */   public Object getNote(String name)
/*     */   {
/* 113 */     return null;
/*     */   }
/*     */   
/*     */   public Iterator<String> getNoteNames()
/*     */   {
/* 118 */     return null;
/*     */   }
/*     */   
/*     */   public Principal getPrincipal()
/*     */   {
/* 123 */     return null;
/*     */   }
/*     */   
/*     */   public HttpSession getSession()
/*     */   {
/* 128 */     return null;
/*     */   }
/*     */   
/*     */   public long getThisAccessedTime()
/*     */   {
/* 133 */     return 0L;
/*     */   }
/*     */   
/*     */   public long getThisAccessedTimeInternal()
/*     */   {
/* 138 */     return 0L;
/*     */   }
/*     */   
/*     */   public boolean isValid()
/*     */   {
/* 143 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void recycle() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeNote(String name) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeSessionListener(SessionListener listener) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void setAuthType(String authType) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void setCreationTime(long time) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void setId(String id)
/*     */   {
/* 173 */     this.sessionId = id;
/*     */   }
/*     */   
/*     */   public void setId(String id, boolean notify)
/*     */   {
/* 178 */     this.sessionId = id;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setManager(Manager manager) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMaxInactiveInterval(int interval) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setNew(boolean isNew) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setNote(String name, Object value) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPrincipal(Principal principal) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValid(boolean isValid) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void tellChangedSessionId(String newId, String oldId, boolean notifySessionListeners, boolean notifyContainerListeners) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isAttributeDistributable(String name, Object value)
/*     */   {
/* 220 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\manager\DummyProxySession.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */