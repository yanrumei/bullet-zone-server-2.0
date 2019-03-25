/*     */ package org.apache.coyote.http11.upgrade;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.WriteListener;
/*     */ import org.apache.coyote.ContainerThreadMarker;
/*     */ import org.apache.coyote.UpgradeToken;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.ContextBind;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.net.DispatchType;
/*     */ import org.apache.tomcat.util.net.SocketWrapperBase;
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
/*     */ public class UpgradeServletOutputStream
/*     */   extends ServletOutputStream
/*     */ {
/*  34 */   private static final Log log = LogFactory.getLog(UpgradeServletOutputStream.class);
/*     */   
/*  36 */   private static final StringManager sm = StringManager.getManager(UpgradeServletOutputStream.class);
/*     */   
/*     */ 
/*     */   private final UpgradeProcessorBase processor;
/*     */   
/*     */   private final SocketWrapperBase<?> socketWrapper;
/*     */   
/*  43 */   private final Object registeredLock = new Object();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  50 */   private final Object writeLock = new Object();
/*     */   
/*  52 */   private volatile boolean flushing = false;
/*     */   
/*  54 */   private volatile boolean closed = false;
/*     */   
/*     */ 
/*  57 */   private volatile WriteListener listener = null;
/*     */   
/*     */ 
/*  60 */   private boolean registered = false;
/*     */   
/*     */ 
/*     */ 
/*     */   public UpgradeServletOutputStream(UpgradeProcessorBase processor, SocketWrapperBase<?> socketWrapper)
/*     */   {
/*  66 */     this.processor = processor;
/*  67 */     this.socketWrapper = socketWrapper;
/*     */   }
/*     */   
/*     */ 
/*     */   public final boolean isReady()
/*     */   {
/*  73 */     if (this.listener == null)
/*     */     {
/*  75 */       throw new IllegalStateException(sm.getString("upgrade.sos.canWrite.ise"));
/*     */     }
/*  77 */     if (this.closed) {
/*  78 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  83 */     synchronized (this.registeredLock) {
/*  84 */       if (this.flushing)
/*     */       {
/*     */ 
/*  87 */         this.registered = true;
/*  88 */         return false; }
/*  89 */       if (this.registered)
/*     */       {
/*     */ 
/*  92 */         return false;
/*     */       }
/*  94 */       boolean result = this.socketWrapper.isReadyForWrite();
/*  95 */       this.registered = (!result);
/*  96 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void setWriteListener(WriteListener listener)
/*     */   {
/* 104 */     if (listener == null)
/*     */     {
/* 106 */       throw new IllegalArgumentException(sm.getString("upgrade.sos.writeListener.null"));
/*     */     }
/* 108 */     if (this.listener != null)
/*     */     {
/* 110 */       throw new IllegalArgumentException(sm.getString("upgrade.sos.writeListener.set"));
/*     */     }
/* 112 */     if (this.closed) {
/* 113 */       throw new IllegalStateException(sm.getString("upgrade.sos.write.closed"));
/*     */     }
/* 115 */     this.listener = listener;
/*     */     
/* 117 */     synchronized (this.registeredLock) {
/* 118 */       this.registered = true;
/*     */       
/* 120 */       if (ContainerThreadMarker.isContainerThread()) {
/* 121 */         this.processor.addDispatch(DispatchType.NON_BLOCKING_WRITE);
/*     */       } else {
/* 123 */         this.socketWrapper.registerWriteInterest();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   final boolean isClosed()
/*     */   {
/* 131 */     return this.closed;
/*     */   }
/*     */   
/*     */   public void write(int b)
/*     */     throws IOException
/*     */   {
/* 137 */     synchronized (this.writeLock) {
/* 138 */       preWriteChecks();
/* 139 */       writeInternal(new byte[] { (byte)b }, 0, 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void write(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/* 146 */     synchronized (this.writeLock) {
/* 147 */       preWriteChecks();
/* 148 */       writeInternal(b, off, len);
/*     */     }
/*     */   }
/*     */   
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 155 */     preWriteChecks();
/* 156 */     flushInternal(this.listener == null, true);
/*     */   }
/*     */   
/*     */   private void flushInternal(boolean block, boolean updateFlushing) throws IOException
/*     */   {
/*     */     try {
/* 162 */       synchronized (this.writeLock) {
/* 163 */         if (updateFlushing) {
/* 164 */           this.flushing = this.socketWrapper.flush(block);
/* 165 */           if (this.flushing) {
/* 166 */             this.socketWrapper.registerWriteInterest();
/*     */           }
/*     */         } else {
/* 169 */           this.socketWrapper.flush(block);
/*     */         }
/*     */       }
/*     */     } catch (Throwable t) {
/* 173 */       ExceptionUtils.handleThrowable(t);
/* 174 */       onError(t);
/* 175 */       if ((t instanceof IOException)) {
/* 176 */         throw ((IOException)t);
/*     */       }
/* 178 */       throw new IOException(t);
/*     */     }
/*     */   }
/*     */   
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 185 */     if (this.closed) {
/* 186 */       return;
/*     */     }
/* 188 */     this.closed = true;
/* 189 */     flushInternal(this.listener == null, false);
/*     */   }
/*     */   
/*     */   private void preWriteChecks()
/*     */   {
/* 194 */     if ((this.listener != null) && (!this.socketWrapper.canWrite())) {
/* 195 */       throw new IllegalStateException(sm.getString("upgrade.sos.write.ise"));
/*     */     }
/* 197 */     if (this.closed) {
/* 198 */       throw new IllegalStateException(sm.getString("upgrade.sos.write.closed"));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void writeInternal(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/* 207 */     if (this.listener == null)
/*     */     {
/* 209 */       this.socketWrapper.write(true, b, off, len);
/*     */     } else {
/* 211 */       this.socketWrapper.write(false, b, off, len);
/*     */     }
/*     */   }
/*     */   
/*     */   final void onWritePossible()
/*     */   {
/*     */     try {
/* 218 */       if (this.flushing) {
/* 219 */         flushInternal(false, true);
/* 220 */         if (!this.flushing) {}
/*     */ 
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/* 227 */         flushInternal(false, false);
/*     */       }
/*     */     } catch (IOException ioe) {
/* 230 */       onError(ioe);
/* 231 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 237 */     boolean fire = false;
/* 238 */     synchronized (this.registeredLock) {
/* 239 */       if (this.socketWrapper.isReadyForWrite()) {
/* 240 */         this.registered = false;
/* 241 */         fire = true;
/*     */       } else {
/* 243 */         this.registered = true;
/*     */       }
/*     */     }
/*     */     
/* 247 */     if (fire) {
/* 248 */       ClassLoader oldCL = this.processor.getUpgradeToken().getContextBind().bind(false, null);
/*     */       try {
/* 250 */         this.listener.onWritePossible();
/*     */       } catch (Throwable t) {
/* 252 */         ExceptionUtils.handleThrowable(t);
/* 253 */         onError(t);
/*     */       } finally {
/* 255 */         this.processor.getUpgradeToken().getContextBind().unbind(false, oldCL);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private final void onError(Throwable t)
/*     */   {
/* 262 */     if (this.listener == null) {
/* 263 */       return;
/*     */     }
/* 265 */     ClassLoader oldCL = this.processor.getUpgradeToken().getContextBind().bind(false, null);
/*     */     try {
/* 267 */       this.listener.onError(t);
/*     */     } catch (Throwable t2) {
/* 269 */       ExceptionUtils.handleThrowable(t2);
/* 270 */       log.warn(sm.getString("upgrade.sos.onErrorFail"), t2);
/*     */     } finally {
/* 272 */       this.processor.getUpgradeToken().getContextBind().unbind(false, oldCL);
/*     */     }
/*     */     try {
/* 275 */       close();
/*     */     } catch (IOException ioe) {
/* 277 */       if (log.isDebugEnabled()) {
/* 278 */         log.debug(sm.getString("upgrade.sos.errorCloseFail"), ioe);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http1\\upgrade\UpgradeServletOutputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */