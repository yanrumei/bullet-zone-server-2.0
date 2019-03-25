/*     */ package org.apache.catalina.valves;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.ServletException;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Engine;
/*     */ import org.apache.catalina.Globals;
/*     */ import org.apache.catalina.Host;
/*     */ import org.apache.catalina.Manager;
/*     */ import org.apache.catalina.Session;
/*     */ import org.apache.catalina.Store;
/*     */ import org.apache.catalina.StoreManager;
/*     */ import org.apache.catalina.Valve;
/*     */ import org.apache.catalina.connector.Request;
/*     */ import org.apache.catalina.connector.Response;
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
/*     */ public class PersistentValve
/*     */   extends ValveBase
/*     */ {
/*  52 */   private static final ClassLoader MY_CLASSLOADER = PersistentValve.class.getClassLoader();
/*     */   
/*     */ 
/*     */   private volatile boolean clBindRequired;
/*     */   
/*     */ 
/*     */   public PersistentValve()
/*     */   {
/*  60 */     super(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContainer(Container container)
/*     */   {
/*  68 */     super.setContainer(container);
/*  69 */     if (((container instanceof Engine)) || ((container instanceof Host))) {
/*  70 */       this.clBindRequired = true;
/*     */     } else {
/*  72 */       this.clBindRequired = false;
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
/*     */   public void invoke(Request request, Response response)
/*     */     throws IOException, ServletException
/*     */   {
/*  93 */     Context context = request.getContext();
/*  94 */     if (context == null) {
/*  95 */       response.sendError(500, sm
/*  96 */         .getString("standardHost.noContext"));
/*  97 */       return;
/*     */     }
/*     */     
/*     */ 
/* 101 */     String sessionId = request.getRequestedSessionId();
/* 102 */     Manager manager = context.getManager();
/* 103 */     if ((sessionId != null) && ((manager instanceof StoreManager))) {
/* 104 */       Store store = ((StoreManager)manager).getStore();
/* 105 */       if (store != null) {
/* 106 */         Session session = null;
/*     */         try {
/* 108 */           session = store.load(sessionId);
/*     */         } catch (Exception e) {
/* 110 */           this.container.getLogger().error("deserializeError");
/*     */         }
/* 112 */         if (session != null) {
/* 113 */           if ((!session.isValid()) || 
/* 114 */             (isSessionStale(session, System.currentTimeMillis()))) {
/* 115 */             if (this.container.getLogger().isDebugEnabled()) {
/* 116 */               this.container.getLogger().debug("session swapped in is invalid or expired");
/*     */             }
/* 118 */             session.expire();
/* 119 */             store.remove(sessionId);
/*     */           } else {
/* 121 */             session.setManager(manager);
/*     */             
/* 123 */             manager.add(session);
/*     */             
/* 125 */             session.access();
/* 126 */             session.endAccess();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 131 */     if (this.container.getLogger().isDebugEnabled()) {
/* 132 */       this.container.getLogger().debug("sessionId: " + sessionId);
/*     */     }
/*     */     
/*     */ 
/* 136 */     getNext().invoke(request, response);
/*     */     
/*     */ 
/* 139 */     if (!request.isAsync())
/*     */     {
/*     */       Session hsess;
/*     */       try
/*     */       {
/* 144 */         hsess = request.getSessionInternal(false);
/*     */       } catch (Exception ex) { Session hsess;
/* 146 */         hsess = null;
/*     */       }
/* 148 */       String newsessionId = null;
/* 149 */       if (hsess != null) {
/* 150 */         newsessionId = hsess.getIdInternal();
/*     */       }
/*     */       
/* 153 */       if (this.container.getLogger().isDebugEnabled()) {
/* 154 */         this.container.getLogger().debug("newsessionId: " + newsessionId);
/*     */       }
/* 156 */       if (newsessionId != null) {
/*     */         try {
/* 158 */           bind(context);
/*     */           
/*     */ 
/* 161 */           if ((manager instanceof StoreManager)) {
/* 162 */             Session session = manager.findSession(newsessionId);
/* 163 */             Store store = ((StoreManager)manager).getStore();
/* 164 */             if ((store != null) && (session != null) && (session.isValid()) && 
/* 165 */               (!isSessionStale(session, System.currentTimeMillis()))) {
/* 166 */               store.save(session);
/* 167 */               ((StoreManager)manager).removeSuper(session);
/* 168 */               session.recycle();
/*     */             }
/* 170 */             else if (this.container.getLogger().isDebugEnabled()) {
/* 171 */               this.container.getLogger().debug("newsessionId store: " + store + " session: " + session + " valid: " + (session == null ? "N/A" : 
/*     */               
/*     */ 
/* 174 */                 Boolean.toString(session
/* 175 */                 .isValid())) + " stale: " + 
/* 176 */                 isSessionStale(session, 
/* 177 */                 System.currentTimeMillis()));
/*     */             }
/*     */             
/*     */ 
/*     */           }
/* 182 */           else if (this.container.getLogger().isDebugEnabled()) {
/* 183 */             this.container.getLogger().debug("newsessionId Manager: " + manager);
/*     */           }
/*     */         }
/*     */         finally
/*     */         {
/* 188 */           unbind(context);
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
/*     */   protected boolean isSessionStale(Session session, long timeNow)
/*     */   {
/* 206 */     if (session != null) {
/* 207 */       int maxInactiveInterval = session.getMaxInactiveInterval();
/* 208 */       if (maxInactiveInterval >= 0)
/*     */       {
/* 210 */         int timeIdle = (int)((timeNow - session.getThisAccessedTime()) / 1000L);
/* 211 */         if (timeIdle >= maxInactiveInterval) {
/* 212 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 217 */     return false;
/*     */   }
/*     */   
/*     */   private void bind(Context context)
/*     */   {
/* 222 */     if (this.clBindRequired) {
/* 223 */       context.bind(Globals.IS_SECURITY_ENABLED, MY_CLASSLOADER);
/*     */     }
/*     */   }
/*     */   
/*     */   private void unbind(Context context)
/*     */   {
/* 229 */     if (this.clBindRequired) {
/* 230 */       context.unbind(Globals.IS_SECURITY_ENABLED, MY_CLASSLOADER);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\PersistentValve.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */