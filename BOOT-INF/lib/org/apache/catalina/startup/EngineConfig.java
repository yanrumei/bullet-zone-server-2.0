/*     */ package org.apache.catalina.startup;
/*     */ 
/*     */ import org.apache.catalina.Engine;
/*     */ import org.apache.catalina.LifecycleEvent;
/*     */ import org.apache.catalina.LifecycleListener;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ public class EngineConfig
/*     */   implements LifecycleListener
/*     */ {
/*  41 */   private static final Log log = LogFactory.getLog(EngineConfig.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  49 */   protected Engine engine = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  56 */   protected static final StringManager sm = StringManager.getManager("org.apache.catalina.startup");
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
/*     */   public void lifecycleEvent(LifecycleEvent event)
/*     */   {
/*     */     try
/*     */     {
/*  72 */       this.engine = ((Engine)event.getLifecycle());
/*     */     } catch (ClassCastException e) {
/*  74 */       log.error(sm.getString("engineConfig.cce", new Object[] { event.getLifecycle() }), e);
/*  75 */       return;
/*     */     }
/*     */     
/*     */ 
/*  79 */     if (event.getType().equals("start")) {
/*  80 */       start();
/*  81 */     } else if (event.getType().equals("stop")) {
/*  82 */       stop();
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
/*     */   protected void start()
/*     */   {
/*  95 */     if (this.engine.getLogger().isDebugEnabled()) {
/*  96 */       this.engine.getLogger().debug(sm.getString("engineConfig.start"));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void stop()
/*     */   {
/* 106 */     if (this.engine.getLogger().isDebugEnabled()) {
/* 107 */       this.engine.getLogger().debug(sm.getString("engineConfig.stop"));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\EngineConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */