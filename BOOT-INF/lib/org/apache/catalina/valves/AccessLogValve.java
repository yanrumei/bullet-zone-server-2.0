/*     */ package org.apache.catalina.valves;
/*     */ 
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.CharArrayWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.buf.B2CConverter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AccessLogValve
/*     */   extends AbstractAccessLogValve
/*     */ {
/*  65 */   private static final Log log = LogFactory.getLog(AccessLogValve.class);
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
/*  79 */   private volatile String dateStamp = "";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  85 */   private String directory = "logs";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  90 */   protected String prefix = "access_log";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  96 */   protected boolean rotatable = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */   protected boolean renameOnRotate = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 108 */   private boolean buffered = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 114 */   protected String suffix = "";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 120 */   protected PrintWriter writer = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 127 */   protected SimpleDateFormat fileDateFormatter = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 134 */   protected File currentLogFile = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 139 */   private volatile long rotationLastChecked = 0L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 145 */   private boolean checkExists = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 150 */   protected String fileDateFormat = ".yyyy-MM-dd";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 157 */   protected String encoding = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDirectory()
/*     */   {
/* 166 */     return this.directory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDirectory(String directory)
/*     */   {
/* 176 */     this.directory = directory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCheckExists()
/*     */   {
/* 185 */     return this.checkExists;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCheckExists(boolean checkExists)
/*     */   {
/* 197 */     this.checkExists = checkExists;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPrefix()
/*     */   {
/* 206 */     return this.prefix;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPrefix(String prefix)
/*     */   {
/* 216 */     this.prefix = prefix;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRotatable()
/*     */   {
/* 226 */     return this.rotatable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRotatable(boolean rotatable)
/*     */   {
/* 236 */     this.rotatable = rotatable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRenameOnRotate()
/*     */   {
/* 247 */     return this.renameOnRotate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRenameOnRotate(boolean renameOnRotate)
/*     */   {
/* 258 */     this.renameOnRotate = renameOnRotate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBuffered()
/*     */   {
/* 267 */     return this.buffered;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBuffered(boolean buffered)
/*     */   {
/* 277 */     this.buffered = buffered;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSuffix()
/*     */   {
/* 285 */     return this.suffix;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSuffix(String suffix)
/*     */   {
/* 295 */     this.suffix = suffix;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getFileDateFormat()
/*     */   {
/* 302 */     return this.fileDateFormat;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setFileDateFormat(String fileDateFormat)
/*     */   {
/*     */     String newFormat;
/*     */     
/*     */     String newFormat;
/*     */     
/* 312 */     if (fileDateFormat == null) {
/* 313 */       newFormat = "";
/*     */     } else {
/* 315 */       newFormat = fileDateFormat;
/*     */     }
/* 317 */     this.fileDateFormat = newFormat;
/*     */     
/* 319 */     synchronized (this) {
/* 320 */       this.fileDateFormatter = new SimpleDateFormat(newFormat, Locale.US);
/* 321 */       this.fileDateFormatter.setTimeZone(TimeZone.getDefault());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getEncoding()
/*     */   {
/* 332 */     return this.encoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEncoding(String encoding)
/*     */   {
/* 341 */     if ((encoding != null) && (encoding.length() > 0)) {
/* 342 */       this.encoding = encoding;
/*     */     } else {
/* 344 */       this.encoding = null;
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
/*     */   public synchronized void backgroundProcess()
/*     */   {
/* 357 */     if ((getState().isAvailable()) && (getEnabled()) && (this.writer != null) && (this.buffered))
/*     */     {
/* 359 */       this.writer.flush();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void rotate()
/*     */   {
/* 367 */     if (this.rotatable)
/*     */     {
/* 369 */       long systime = System.currentTimeMillis();
/* 370 */       if (systime - this.rotationLastChecked > 1000L) {
/* 371 */         synchronized (this) {
/* 372 */           if (systime - this.rotationLastChecked > 1000L) {
/* 373 */             this.rotationLastChecked = systime;
/*     */             
/*     */ 
/*     */ 
/* 377 */             String tsDate = this.fileDateFormatter.format(new Date(systime));
/*     */             
/*     */ 
/* 380 */             if (!this.dateStamp.equals(tsDate)) {
/* 381 */               close(true);
/* 382 */               this.dateStamp = tsDate;
/* 383 */               open();
/*     */             }
/*     */           }
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
/*     */   public synchronized boolean rotate(String newFileName)
/*     */   {
/* 401 */     if (this.currentLogFile != null) {
/* 402 */       File holder = this.currentLogFile;
/* 403 */       close(false);
/*     */       try {
/* 405 */         holder.renameTo(new File(newFileName));
/*     */       } catch (Throwable e) {
/* 407 */         ExceptionUtils.handleThrowable(e);
/* 408 */         log.error(sm.getString("accessLogValve.rotateFail"), e);
/*     */       }
/*     */       
/*     */ 
/* 412 */       this.dateStamp = this.fileDateFormatter.format(new Date(
/* 413 */         System.currentTimeMillis()));
/*     */       
/* 415 */       open();
/* 416 */       return true;
/*     */     }
/* 418 */     return false;
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
/*     */   private File getLogFile(boolean useDateStamp)
/*     */   {
/* 437 */     File dir = new File(this.directory);
/* 438 */     if (!dir.isAbsolute()) {
/* 439 */       dir = new File(getContainer().getCatalinaBase(), this.directory);
/*     */     }
/* 441 */     if ((!dir.mkdirs()) && (!dir.isDirectory())) {
/* 442 */       log.error(sm.getString("accessLogValve.openDirFail", new Object[] { dir }));
/*     */     }
/*     */     
/*     */     File pathname;
/*     */     File pathname;
/* 447 */     if (useDateStamp) {
/* 448 */       pathname = new File(dir.getAbsoluteFile(), this.prefix + this.dateStamp + this.suffix);
/*     */     }
/*     */     else {
/* 451 */       pathname = new File(dir.getAbsoluteFile(), this.prefix + this.suffix);
/*     */     }
/* 453 */     File parent = pathname.getParentFile();
/* 454 */     if ((!parent.mkdirs()) && (!parent.isDirectory())) {
/* 455 */       log.error(sm.getString("accessLogValve.openDirFail", new Object[] { parent }));
/*     */     }
/* 457 */     return pathname;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void restore()
/*     */   {
/* 466 */     File newLogFile = getLogFile(false);
/* 467 */     File rotatedLogFile = getLogFile(true);
/* 468 */     if ((rotatedLogFile.exists()) && (!newLogFile.exists()) && 
/* 469 */       (!rotatedLogFile.equals(newLogFile))) {
/*     */       try {
/* 471 */         if (!rotatedLogFile.renameTo(newLogFile)) {
/* 472 */           log.error(sm.getString("accessLogValve.renameFail", new Object[] { rotatedLogFile, newLogFile }));
/*     */         }
/*     */       } catch (Throwable e) {
/* 475 */         ExceptionUtils.handleThrowable(e);
/* 476 */         log.error(sm.getString("accessLogValve.renameFail", new Object[] { rotatedLogFile, newLogFile }), e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private synchronized void close(boolean rename)
/*     */   {
/* 488 */     if (this.writer == null) {
/* 489 */       return;
/*     */     }
/* 491 */     this.writer.flush();
/* 492 */     this.writer.close();
/* 493 */     if ((rename) && (this.renameOnRotate)) {
/* 494 */       File newLogFile = getLogFile(true);
/* 495 */       if (!newLogFile.exists()) {
/*     */         try {
/* 497 */           if (!this.currentLogFile.renameTo(newLogFile)) {
/* 498 */             log.error(sm.getString("accessLogValve.renameFail", new Object[] { this.currentLogFile, newLogFile }));
/*     */           }
/*     */         } catch (Throwable e) {
/* 501 */           ExceptionUtils.handleThrowable(e);
/* 502 */           log.error(sm.getString("accessLogValve.renameFail", new Object[] { this.currentLogFile, newLogFile }), e);
/*     */         }
/*     */       } else {
/* 505 */         log.error(sm.getString("accessLogValve.alreadyExists", new Object[] { this.currentLogFile, newLogFile }));
/*     */       }
/*     */     }
/* 508 */     this.writer = null;
/* 509 */     this.dateStamp = "";
/* 510 */     this.currentLogFile = null;
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
/*     */   public void log(CharArrayWriter message)
/*     */   {
/* 523 */     rotate();
/*     */     
/*     */ 
/* 526 */     if (this.checkExists) {
/* 527 */       synchronized (this) {
/* 528 */         if ((this.currentLogFile != null) && (!this.currentLogFile.exists())) {
/*     */           try {
/* 530 */             close(false);
/*     */           } catch (Throwable e) {
/* 532 */             ExceptionUtils.handleThrowable(e);
/* 533 */             log.info(sm.getString("accessLogValve.closeFail"), e);
/*     */           }
/*     */           
/*     */ 
/* 537 */           this.dateStamp = this.fileDateFormatter.format(new Date(
/* 538 */             System.currentTimeMillis()));
/*     */           
/* 540 */           open();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 547 */       synchronized (this) {
/* 548 */         if (this.writer != null) {
/* 549 */           message.writeTo(this.writer);
/* 550 */           this.writer.println("");
/* 551 */           if (!this.buffered) {
/* 552 */             this.writer.flush();
/*     */           }
/*     */         }
/*     */       }
/*     */     } catch (IOException ioe) {
/* 557 */       log.warn(sm.getString("accessLogValve.writeFail", new Object[] {message
/* 558 */         .toString() }), ioe);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected synchronized void open()
/*     */   {
/* 569 */     File pathname = getLogFile((this.rotatable) && (!this.renameOnRotate));
/*     */     
/* 571 */     Charset charset = null;
/* 572 */     if (this.encoding != null) {
/*     */       try {
/* 574 */         charset = B2CConverter.getCharset(this.encoding);
/*     */       } catch (UnsupportedEncodingException ex) {
/* 576 */         log.error(sm.getString("accessLogValve.unsupportedEncoding", new Object[] { this.encoding }), ex);
/*     */       }
/*     */     }
/*     */     
/* 580 */     if (charset == null) {
/* 581 */       charset = StandardCharsets.ISO_8859_1;
/*     */     }
/*     */     try
/*     */     {
/* 585 */       this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathname, true), charset), 128000), false);
/*     */       
/*     */ 
/*     */ 
/* 589 */       this.currentLogFile = pathname;
/*     */     } catch (IOException e) {
/* 591 */       this.writer = null;
/* 592 */       this.currentLogFile = null;
/* 593 */       log.error(sm.getString("accessLogValve.openFail", new Object[] { pathname }), e);
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
/*     */   protected synchronized void startInternal()
/*     */     throws LifecycleException
/*     */   {
/* 608 */     String format = getFileDateFormat();
/* 609 */     this.fileDateFormatter = new SimpleDateFormat(format, Locale.US);
/* 610 */     this.fileDateFormatter.setTimeZone(TimeZone.getDefault());
/* 611 */     this.dateStamp = this.fileDateFormatter.format(new Date(System.currentTimeMillis()));
/* 612 */     if ((this.rotatable) && (this.renameOnRotate)) {
/* 613 */       restore();
/*     */     }
/* 615 */     open();
/*     */     
/* 617 */     super.startInternal();
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
/*     */   protected synchronized void stopInternal()
/*     */     throws LifecycleException
/*     */   {
/* 631 */     super.stopInternal();
/* 632 */     close(false);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\AccessLogValve.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */