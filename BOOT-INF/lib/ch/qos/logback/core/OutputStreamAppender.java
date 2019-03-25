/*     */ package ch.qos.logback.core;
/*     */ 
/*     */ import ch.qos.logback.core.encoder.Encoder;
/*     */ import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
/*     */ import ch.qos.logback.core.spi.DeferredProcessingAware;
/*     */ import ch.qos.logback.core.status.ErrorStatus;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.concurrent.locks.ReentrantLock;
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
/*     */ public class OutputStreamAppender<E>
/*     */   extends UnsynchronizedAppenderBase<E>
/*     */ {
/*     */   protected Encoder<E> encoder;
/*  47 */   protected final ReentrantLock lock = new ReentrantLock(false);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private OutputStream outputStream;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OutputStream getOutputStream()
/*     */   {
/*  60 */     return this.outputStream;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void start()
/*     */   {
/*  68 */     int errors = 0;
/*  69 */     if (this.encoder == null) {
/*  70 */       addStatus(new ErrorStatus("No encoder set for the appender named \"" + this.name + "\".", this));
/*  71 */       errors++;
/*     */     }
/*     */     
/*  74 */     if (this.outputStream == null) {
/*  75 */       addStatus(new ErrorStatus("No output stream set for the appender named \"" + this.name + "\".", this));
/*  76 */       errors++;
/*     */     }
/*     */     
/*  79 */     if (errors == 0) {
/*  80 */       super.start();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setLayout(Layout<E> layout) {
/*  85 */     addWarn("This appender no longer admits a layout as a sub-component, set an encoder instead.");
/*  86 */     addWarn("To ensure compatibility, wrapping your layout in LayoutWrappingEncoder.");
/*  87 */     addWarn("See also http://logback.qos.ch/codes.html#layoutInsteadOfEncoder for details");
/*  88 */     LayoutWrappingEncoder<E> lwe = new LayoutWrappingEncoder();
/*  89 */     lwe.setLayout(layout);
/*  90 */     lwe.setContext(this.context);
/*  91 */     this.encoder = lwe;
/*     */   }
/*     */   
/*     */   protected void append(E eventObject)
/*     */   {
/*  96 */     if (!isStarted()) {
/*  97 */       return;
/*     */     }
/*     */     
/* 100 */     subAppend(eventObject);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void stop()
/*     */   {
/* 111 */     this.lock.lock();
/*     */     try {
/* 113 */       closeOutputStream();
/* 114 */       super.stop();
/*     */     } finally {
/* 116 */       this.lock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void closeOutputStream()
/*     */   {
/* 124 */     if (this.outputStream != null) {
/*     */       try
/*     */       {
/* 127 */         encoderClose();
/* 128 */         this.outputStream.close();
/* 129 */         this.outputStream = null;
/*     */       } catch (IOException e) {
/* 131 */         addStatus(new ErrorStatus("Could not close output stream for OutputStreamAppender.", this, e));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void encoderInit() {
/* 137 */     if ((this.encoder != null) && (this.outputStream != null)) {
/*     */       try {
/* 139 */         this.encoder.init(this.outputStream);
/*     */       } catch (IOException ioe) {
/* 141 */         this.started = false;
/* 142 */         addStatus(new ErrorStatus("Failed to initialize encoder for appender named [" + this.name + "].", this, ioe));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void encoderClose() {
/* 148 */     if ((this.encoder != null) && (this.outputStream != null)) {
/*     */       try {
/* 150 */         this.encoder.close();
/*     */       } catch (IOException ioe) {
/* 152 */         this.started = false;
/* 153 */         addStatus(new ErrorStatus("Failed to write footer for appender named [" + this.name + "].", this, ioe));
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
/*     */   public void setOutputStream(OutputStream outputStream)
/*     */   {
/* 169 */     this.lock.lock();
/*     */     try
/*     */     {
/* 172 */       closeOutputStream();
/*     */       
/* 174 */       this.outputStream = outputStream;
/* 175 */       if (this.encoder == null) {
/* 176 */         addWarn("Encoder has not been set. Cannot invoke its init method.");
/*     */       }
/*     */       else
/*     */       {
/* 180 */         encoderInit(); }
/*     */     } finally {
/* 182 */       this.lock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void writeOut(E event) throws IOException {
/* 187 */     this.encoder.doEncode(event);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void subAppend(E event)
/*     */   {
/* 199 */     if (!isStarted()) {
/* 200 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 204 */       if ((event instanceof DeferredProcessingAware)) {
/* 205 */         ((DeferredProcessingAware)event).prepareForDeferredProcessing();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 210 */       this.lock.lock();
/*     */       try {
/* 212 */         writeOut(event);
/*     */       } finally {
/* 214 */         this.lock.unlock();
/*     */       }
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/* 219 */       this.started = false;
/* 220 */       addStatus(new ErrorStatus("IO failure in appender", this, ioe));
/*     */     }
/*     */   }
/*     */   
/*     */   public Encoder<E> getEncoder() {
/* 225 */     return this.encoder;
/*     */   }
/*     */   
/*     */   public void setEncoder(Encoder<E> encoder) {
/* 229 */     this.encoder = encoder;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\OutputStreamAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */