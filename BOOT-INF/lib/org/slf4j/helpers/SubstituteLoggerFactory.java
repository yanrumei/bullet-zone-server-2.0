/*    */ package org.slf4j.helpers;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.LinkedBlockingQueue;
/*    */ import org.slf4j.ILoggerFactory;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.event.SubstituteLoggingEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SubstituteLoggerFactory
/*    */   implements ILoggerFactory
/*    */ {
/* 45 */   boolean postInitialization = false;
/*    */   
/* 47 */   final Map<String, SubstituteLogger> loggers = new HashMap();
/*    */   
/* 49 */   final LinkedBlockingQueue<SubstituteLoggingEvent> eventQueue = new LinkedBlockingQueue();
/*    */   
/*    */   public synchronized Logger getLogger(String name) {
/* 52 */     SubstituteLogger logger = (SubstituteLogger)this.loggers.get(name);
/* 53 */     if (logger == null) {
/* 54 */       logger = new SubstituteLogger(name, this.eventQueue, this.postInitialization);
/* 55 */       this.loggers.put(name, logger);
/*    */     }
/* 57 */     return logger;
/*    */   }
/*    */   
/*    */   public List<String> getLoggerNames() {
/* 61 */     return new ArrayList(this.loggers.keySet());
/*    */   }
/*    */   
/*    */   public List<SubstituteLogger> getLoggers() {
/* 65 */     return new ArrayList(this.loggers.values());
/*    */   }
/*    */   
/*    */   public LinkedBlockingQueue<SubstituteLoggingEvent> getEventQueue() {
/* 69 */     return this.eventQueue;
/*    */   }
/*    */   
/*    */   public void postInitialization() {
/* 73 */     this.postInitialization = true;
/*    */   }
/*    */   
/*    */   public void clear() {
/* 77 */     this.loggers.clear();
/* 78 */     this.eventQueue.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\slf4j-api-1.7.25.jar!\org\slf4j\helpers\SubstituteLoggerFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */