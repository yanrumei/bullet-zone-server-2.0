/*     */ package org.apache.catalina.session;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import javax.servlet.http.HttpSessionContext;
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
/*     */ public class StandardSessionFacade
/*     */   implements HttpSession
/*     */ {
/*     */   private final HttpSession session;
/*     */   
/*     */   public StandardSessionFacade(HttpSession session)
/*     */   {
/*  39 */     this.session = session;
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
/*     */   public long getCreationTime()
/*     */   {
/*  55 */     return this.session.getCreationTime();
/*     */   }
/*     */   
/*     */ 
/*     */   public String getId()
/*     */   {
/*  61 */     return this.session.getId();
/*     */   }
/*     */   
/*     */ 
/*     */   public long getLastAccessedTime()
/*     */   {
/*  67 */     return this.session.getLastAccessedTime();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ServletContext getServletContext()
/*     */   {
/*  74 */     return this.session.getServletContext();
/*     */   }
/*     */   
/*     */ 
/*     */   public void setMaxInactiveInterval(int interval)
/*     */   {
/*  80 */     this.session.setMaxInactiveInterval(interval);
/*     */   }
/*     */   
/*     */ 
/*     */   public int getMaxInactiveInterval()
/*     */   {
/*  86 */     return this.session.getMaxInactiveInterval();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public HttpSessionContext getSessionContext()
/*     */   {
/*  97 */     return this.session.getSessionContext();
/*     */   }
/*     */   
/*     */ 
/*     */   public Object getAttribute(String name)
/*     */   {
/* 103 */     return this.session.getAttribute(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Object getValue(String name)
/*     */   {
/* 114 */     return this.session.getAttribute(name);
/*     */   }
/*     */   
/*     */ 
/*     */   public Enumeration<String> getAttributeNames()
/*     */   {
/* 120 */     return this.session.getAttributeNames();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public String[] getValueNames()
/*     */   {
/* 131 */     return this.session.getValueNames();
/*     */   }
/*     */   
/*     */ 
/*     */   public void setAttribute(String name, Object value)
/*     */   {
/* 137 */     this.session.setAttribute(name, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void putValue(String name, Object value)
/*     */   {
/* 148 */     this.session.setAttribute(name, value);
/*     */   }
/*     */   
/*     */ 
/*     */   public void removeAttribute(String name)
/*     */   {
/* 154 */     this.session.removeAttribute(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void removeValue(String name)
/*     */   {
/* 165 */     this.session.removeAttribute(name);
/*     */   }
/*     */   
/*     */ 
/*     */   public void invalidate()
/*     */   {
/* 171 */     this.session.invalidate();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isNew()
/*     */   {
/* 177 */     return this.session.isNew();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\session\StandardSessionFacade.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */