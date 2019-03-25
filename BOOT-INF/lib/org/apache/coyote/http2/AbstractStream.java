/*     */ package org.apache.coyote.http2;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ abstract class AbstractStream
/*     */ {
/*  32 */   private static final Log log = LogFactory.getLog(AbstractStream.class);
/*  33 */   private static final StringManager sm = StringManager.getManager(AbstractStream.class);
/*     */   
/*     */   private final Integer identifier;
/*     */   
/*  37 */   private volatile AbstractStream parentStream = null;
/*     */   
/*  39 */   private final Set<Stream> childStreams = Collections.newSetFromMap(new ConcurrentHashMap());
/*  40 */   private long windowSize = 65535L;
/*     */   
/*     */   public Integer getIdentifier() {
/*  43 */     return this.identifier;
/*     */   }
/*     */   
/*     */   public AbstractStream(Integer identifier)
/*     */   {
/*  48 */     this.identifier = identifier;
/*     */   }
/*     */   
/*     */   void detachFromParent()
/*     */   {
/*  53 */     if (this.parentStream != null) {
/*  54 */       this.parentStream.getChildStreams().remove(this);
/*  55 */       this.parentStream = null;
/*     */     }
/*     */   }
/*     */   
/*     */   final void addChild(Stream child)
/*     */   {
/*  61 */     child.setParentStream(this);
/*  62 */     this.childStreams.add(child);
/*     */   }
/*     */   
/*     */   boolean isDescendant(AbstractStream stream)
/*     */   {
/*  67 */     if (this.childStreams.contains(stream)) {
/*  68 */       return true;
/*     */     }
/*  70 */     for (AbstractStream child : this.childStreams) {
/*  71 */       if (child.isDescendant(stream)) {
/*  72 */         return true;
/*     */       }
/*     */     }
/*  75 */     return false;
/*     */   }
/*     */   
/*     */   AbstractStream getParentStream()
/*     */   {
/*  80 */     return this.parentStream;
/*     */   }
/*     */   
/*     */   void setParentStream(AbstractStream parentStream)
/*     */   {
/*  85 */     this.parentStream = parentStream;
/*     */   }
/*     */   
/*     */   final Set<Stream> getChildStreams()
/*     */   {
/*  90 */     return this.childStreams;
/*     */   }
/*     */   
/*     */   protected synchronized void setWindowSize(long windowSize)
/*     */   {
/*  95 */     this.windowSize = windowSize;
/*     */   }
/*     */   
/*     */   protected synchronized long getWindowSize()
/*     */   {
/* 100 */     return this.windowSize;
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
/*     */   protected synchronized void incrementWindowSize(int increment)
/*     */     throws Http2Exception
/*     */   {
/* 114 */     this.windowSize += increment;
/*     */     
/* 116 */     if (log.isDebugEnabled()) {
/* 117 */       log.debug(sm.getString("abstractStream.windowSizeInc", new Object[] { getConnectionId(), 
/* 118 */         getIdentifier(), Integer.toString(increment), Long.toString(this.windowSize) }));
/*     */     }
/*     */     
/* 121 */     if (this.windowSize > 2147483647L) {
/* 122 */       String msg = sm.getString("abstractStream.windowSizeTooBig", new Object[] { getConnectionId(), this.identifier, 
/* 123 */         Integer.toString(increment), Long.toString(this.windowSize) });
/* 124 */       if (this.identifier.intValue() == 0) {
/* 125 */         throw new ConnectionException(msg, Http2Error.FLOW_CONTROL_ERROR);
/*     */       }
/*     */       
/* 128 */       throw new StreamException(msg, Http2Error.FLOW_CONTROL_ERROR, this.identifier.intValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected synchronized void decrementWindowSize(int decrement)
/*     */   {
/* 138 */     this.windowSize -= decrement;
/* 139 */     if (log.isDebugEnabled()) {
/* 140 */       log.debug(sm.getString("abstractStream.windowSizeDec", new Object[] { getConnectionId(), 
/* 141 */         getIdentifier(), Integer.toString(decrement), Long.toString(this.windowSize) }));
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract String getConnectionId();
/*     */   
/*     */   protected abstract int getWeight();
/*     */   
/*     */   @Deprecated
/*     */   protected abstract void doNotifyAll();
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\AbstractStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */