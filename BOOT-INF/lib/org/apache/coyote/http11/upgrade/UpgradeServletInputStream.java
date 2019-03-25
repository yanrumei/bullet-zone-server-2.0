/*     */ package org.apache.coyote.http11.upgrade;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.ReadListener;
/*     */ import javax.servlet.ServletInputStream;
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
/*     */ public class UpgradeServletInputStream
/*     */   extends ServletInputStream
/*     */ {
/*  34 */   private static final Log log = LogFactory.getLog(UpgradeServletInputStream.class);
/*     */   
/*  36 */   private static final StringManager sm = StringManager.getManager(UpgradeServletInputStream.class);
/*     */   
/*     */   private final UpgradeProcessorBase processor;
/*     */   
/*     */   private final SocketWrapperBase<?> socketWrapper;
/*  41 */   private volatile boolean closed = false;
/*  42 */   private volatile boolean eof = false;
/*     */   
/*  44 */   private volatile Boolean ready = Boolean.TRUE;
/*  45 */   private volatile ReadListener listener = null;
/*     */   
/*     */ 
/*     */   public UpgradeServletInputStream(UpgradeProcessorBase processor, SocketWrapperBase<?> socketWrapper)
/*     */   {
/*  50 */     this.processor = processor;
/*  51 */     this.socketWrapper = socketWrapper;
/*     */   }
/*     */   
/*     */ 
/*     */   public final boolean isFinished()
/*     */   {
/*  57 */     if (this.listener == null)
/*     */     {
/*  59 */       throw new IllegalStateException(sm.getString("upgrade.sis.isFinished.ise"));
/*     */     }
/*  61 */     return this.eof;
/*     */   }
/*     */   
/*     */ 
/*     */   public final boolean isReady()
/*     */   {
/*  67 */     if (this.listener == null)
/*     */     {
/*  69 */       throw new IllegalStateException(sm.getString("upgrade.sis.isReady.ise"));
/*     */     }
/*     */     
/*  72 */     if ((this.eof) || (this.closed)) {
/*  73 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  77 */     if (this.ready != null) {
/*  78 */       return this.ready.booleanValue();
/*     */     }
/*     */     try
/*     */     {
/*  82 */       this.ready = Boolean.valueOf(this.socketWrapper.isReadyForRead());
/*     */     } catch (IOException e) {
/*  84 */       onError(e);
/*     */     }
/*  86 */     return this.ready.booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */   public final void setReadListener(ReadListener listener)
/*     */   {
/*  92 */     if (listener == null)
/*     */     {
/*  94 */       throw new IllegalArgumentException(sm.getString("upgrade.sis.readListener.null"));
/*     */     }
/*  96 */     if (this.listener != null)
/*     */     {
/*  98 */       throw new IllegalArgumentException(sm.getString("upgrade.sis.readListener.set"));
/*     */     }
/* 100 */     if (this.closed) {
/* 101 */       throw new IllegalStateException(sm.getString("upgrade.sis.read.closed"));
/*     */     }
/*     */     
/* 104 */     this.listener = listener;
/*     */     
/*     */ 
/* 107 */     if (ContainerThreadMarker.isContainerThread()) {
/* 108 */       this.processor.addDispatch(DispatchType.NON_BLOCKING_READ);
/*     */     } else {
/* 110 */       this.socketWrapper.registerReadInterest();
/*     */     }
/*     */     
/*     */ 
/* 114 */     this.ready = null;
/*     */   }
/*     */   
/*     */   public final int read()
/*     */     throws IOException
/*     */   {
/* 120 */     preReadChecks();
/*     */     
/* 122 */     return readInternal();
/*     */   }
/*     */   
/*     */   public final int readLine(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/* 128 */     preReadChecks();
/*     */     
/* 130 */     if (len <= 0) {
/* 131 */       return 0;
/*     */     }
/* 133 */     int count = 0;
/*     */     int c;
/* 135 */     while ((c = readInternal()) != -1) {
/* 136 */       b[(off++)] = ((byte)c);
/* 137 */       count++;
/* 138 */       if (c != 10) if (count == len) {
/*     */           break;
/*     */         }
/*     */     }
/* 142 */     return count > 0 ? count : -1;
/*     */   }
/*     */   
/*     */   public final int read(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/* 148 */     preReadChecks();
/*     */     try
/*     */     {
/* 151 */       int result = this.socketWrapper.read(this.listener == null, b, off, len);
/* 152 */       if (result == -1) {
/* 153 */         this.eof = true;
/*     */       }
/* 155 */       return result;
/*     */     } catch (IOException ioe) {
/* 157 */       close();
/* 158 */       throw ioe;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 166 */     this.eof = true;
/* 167 */     this.closed = true;
/*     */   }
/*     */   
/*     */   private void preReadChecks()
/*     */   {
/* 172 */     if ((this.listener != null) && ((this.ready == null) || (!this.ready.booleanValue()))) {
/* 173 */       throw new IllegalStateException(sm.getString("upgrade.sis.read.ise"));
/*     */     }
/* 175 */     if (this.closed) {
/* 176 */       throw new IllegalStateException(sm.getString("upgrade.sis.read.closed"));
/*     */     }
/*     */     
/* 179 */     this.ready = null;
/*     */   }
/*     */   
/*     */ 
/*     */   private int readInternal()
/*     */     throws IOException
/*     */   {
/* 186 */     byte[] b = new byte[1];
/*     */     try
/*     */     {
/* 189 */       result = this.socketWrapper.read(this.listener == null, b, 0, 1);
/*     */     } catch (IOException ioe) { int result;
/* 191 */       close();
/* 192 */       throw ioe; }
/*     */     int result;
/* 194 */     if (result == 0)
/* 195 */       return -1;
/* 196 */     if (result == -1) {
/* 197 */       this.eof = true;
/* 198 */       return -1;
/*     */     }
/* 200 */     return b[0] & 0xFF;
/*     */   }
/*     */   
/*     */   final void onDataAvailable()
/*     */   {
/*     */     try
/*     */     {
/* 207 */       if ((this.listener == null) || (!this.socketWrapper.isReadyForRead())) {
/* 208 */         return;
/*     */       }
/*     */     } catch (IOException e) {
/* 211 */       onError(e);
/*     */     }
/* 213 */     this.ready = Boolean.TRUE;
/* 214 */     ClassLoader oldCL = this.processor.getUpgradeToken().getContextBind().bind(false, null);
/*     */     try {
/* 216 */       if (!this.eof) {
/* 217 */         this.listener.onDataAvailable();
/*     */       }
/* 219 */       if (this.eof) {
/* 220 */         this.listener.onAllDataRead();
/*     */       }
/*     */     } catch (Throwable t) {
/* 223 */       ExceptionUtils.handleThrowable(t);
/* 224 */       onError(t);
/*     */     } finally {
/* 226 */       this.processor.getUpgradeToken().getContextBind().unbind(false, oldCL);
/*     */     }
/*     */   }
/*     */   
/*     */   private final void onError(Throwable t)
/*     */   {
/* 232 */     if (this.listener == null) {
/* 233 */       return;
/*     */     }
/* 235 */     ClassLoader oldCL = this.processor.getUpgradeToken().getContextBind().bind(false, null);
/*     */     try {
/* 237 */       this.listener.onError(t);
/*     */     } catch (Throwable t2) {
/* 239 */       ExceptionUtils.handleThrowable(t2);
/* 240 */       log.warn(sm.getString("upgrade.sis.onErrorFail"), t2);
/*     */     } finally {
/* 242 */       this.processor.getUpgradeToken().getContextBind().unbind(false, oldCL);
/*     */     }
/*     */     try {
/* 245 */       close();
/*     */     } catch (IOException ioe) {
/* 247 */       if (log.isDebugEnabled()) {
/* 248 */         log.debug(sm.getString("upgrade.sis.errorCloseFail"), ioe);
/*     */       }
/*     */     }
/* 251 */     this.ready = Boolean.FALSE;
/*     */   }
/*     */   
/*     */   final boolean isClosed()
/*     */   {
/* 256 */     return this.closed;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http1\\upgrade\UpgradeServletInputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */