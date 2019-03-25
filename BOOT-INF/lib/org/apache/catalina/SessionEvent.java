/*     */ package org.apache.catalina;
/*     */ 
/*     */ import java.util.EventObject;
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
/*     */ public final class SessionEvent
/*     */   extends EventObject
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final Object data;
/*     */   private final Session session;
/*     */   private final String type;
/*     */   
/*     */   public SessionEvent(Session session, String type, Object data)
/*     */   {
/*  62 */     super(session);
/*  63 */     this.session = session;
/*  64 */     this.type = type;
/*  65 */     this.data = data;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getData()
/*     */   {
/*  75 */     return this.data;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Session getSession()
/*     */   {
/*  85 */     return this.session;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getType()
/*     */   {
/*  95 */     return this.type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 103 */     return 
/* 104 */       "SessionEvent['" + getSession() + "','" + getType() + "']";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\SessionEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */