/*     */ package ch.qos.logback.core.encoder;
/*     */ 
/*     */ import ch.qos.logback.core.CoreConstants;
/*     */ import ch.qos.logback.core.Layout;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
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
/*     */ public class LayoutWrappingEncoder<E>
/*     */   extends EncoderBase<E>
/*     */ {
/*     */   protected Layout<E> layout;
/*     */   private Charset charset;
/*  37 */   private boolean immediateFlush = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setImmediateFlush(boolean immediateFlush)
/*     */   {
/*  47 */     this.immediateFlush = immediateFlush;
/*     */   }
/*     */   
/*     */   public boolean isImmediateFlush() {
/*  51 */     return this.immediateFlush;
/*     */   }
/*     */   
/*     */   public Layout<E> getLayout() {
/*  55 */     return this.layout;
/*     */   }
/*     */   
/*     */   public void setLayout(Layout<E> layout) {
/*  59 */     this.layout = layout;
/*     */   }
/*     */   
/*     */   public Charset getCharset() {
/*  63 */     return this.charset;
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
/*     */   public void setCharset(Charset charset)
/*     */   {
/*  77 */     this.charset = charset;
/*     */   }
/*     */   
/*     */   public void init(OutputStream os) throws IOException {
/*  81 */     super.init(os);
/*  82 */     writeHeader();
/*     */   }
/*     */   
/*     */   void writeHeader() throws IOException {
/*  86 */     if ((this.layout != null) && (this.outputStream != null)) {
/*  87 */       StringBuilder sb = new StringBuilder();
/*  88 */       appendIfNotNull(sb, this.layout.getFileHeader());
/*  89 */       appendIfNotNull(sb, this.layout.getPresentationHeader());
/*  90 */       if (sb.length() > 0) {
/*  91 */         sb.append(CoreConstants.LINE_SEPARATOR);
/*     */         
/*     */ 
/*     */ 
/*  95 */         this.outputStream.write(convertToBytes(sb.toString()));
/*  96 */         this.outputStream.flush();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 102 */     writeFooter();
/*     */   }
/*     */   
/*     */   void writeFooter() throws IOException {
/* 106 */     if ((this.layout != null) && (this.outputStream != null)) {
/* 107 */       StringBuilder sb = new StringBuilder();
/* 108 */       appendIfNotNull(sb, this.layout.getPresentationFooter());
/* 109 */       appendIfNotNull(sb, this.layout.getFileFooter());
/* 110 */       if (sb.length() > 0) {
/* 111 */         this.outputStream.write(convertToBytes(sb.toString()));
/* 112 */         this.outputStream.flush();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private byte[] convertToBytes(String s) {
/* 118 */     if (this.charset == null) {
/* 119 */       return s.getBytes();
/*     */     }
/*     */     try {
/* 122 */       return s.getBytes(this.charset.name());
/*     */     } catch (UnsupportedEncodingException e) {
/* 124 */       throw new IllegalStateException("An existing charset cannot possibly be unsupported.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void doEncode(E event) throws IOException
/*     */   {
/* 130 */     String txt = this.layout.doLayout(event);
/* 131 */     this.outputStream.write(convertToBytes(txt));
/* 132 */     if (this.immediateFlush)
/* 133 */       this.outputStream.flush();
/*     */   }
/*     */   
/*     */   public boolean isStarted() {
/* 137 */     return false;
/*     */   }
/*     */   
/*     */   public void start() {
/* 141 */     this.started = true;
/*     */   }
/*     */   
/*     */   public void stop() {
/* 145 */     this.started = false;
/* 146 */     if (this.outputStream != null) {
/*     */       try {
/* 148 */         this.outputStream.flush();
/*     */       }
/*     */       catch (IOException e) {}
/*     */     }
/*     */   }
/*     */   
/*     */   private void appendIfNotNull(StringBuilder sb, String s) {
/* 155 */     if (s != null) {
/* 156 */       sb.append(s);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\encoder\LayoutWrappingEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */