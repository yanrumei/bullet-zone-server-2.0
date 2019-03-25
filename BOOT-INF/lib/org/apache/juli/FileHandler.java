/*     */ package org.apache.juli;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.DirectoryStream.Filter;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.logging.ErrorManager;
/*     */ import java.util.logging.Filter;
/*     */ import java.util.logging.Formatter;
/*     */ import java.util.logging.Handler;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.LogManager;
/*     */ import java.util.logging.LogRecord;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class FileHandler
/*     */   extends Handler
/*     */ {
/*     */   public static final int DEFAULT_MAX_DAYS = -1;
/* 101 */   private static final ExecutorService DELETE_FILES_SERVICE = Executors.newSingleThreadExecutor(new ThreadFactory()
/*     */   {
/*     */     private final boolean isSecurityEnabled;
/*     */     
/*     */ 
/*     */ 
/*     */     private final ThreadGroup group;
/*     */     
/*     */ 
/*     */ 
/*     */     private final AtomicInteger threadNumber;
/*     */     
/*     */ 
/*     */ 
/*     */     private final String namePrefix = "FileHandlerLogFilesCleaner-";
/*     */     
/*     */ 
/*     */ 
/*     */     public Thread newThread(Runnable r)
/*     */     {
/* 120 */       final ClassLoader loader = Thread.currentThread().getContextClassLoader();
/*     */       try
/*     */       {
/* 123 */         if (this.isSecurityEnabled) {
/* 124 */           AccessController.doPrivileged(new PrivilegedAction()
/*     */           {
/*     */ 
/*     */             public Void run()
/*     */             {
/* 129 */               Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
/* 130 */               return null;
/*     */             }
/*     */             
/*     */           });
/*     */         } else {
/* 135 */           Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
/*     */         }
/*     */         
/* 138 */         Thread t = new Thread(this.group, r, "FileHandlerLogFilesCleaner-" + this.threadNumber.getAndIncrement());
/* 139 */         t.setDaemon(true);
/* 140 */         return t;
/*     */       } finally {
/* 142 */         if (this.isSecurityEnabled) {
/* 143 */           AccessController.doPrivileged(new PrivilegedAction()
/*     */           {
/*     */             public Void run()
/*     */             {
/* 147 */               Thread.currentThread().setContextClassLoader(loader);
/* 148 */               return null;
/*     */             }
/*     */           });
/*     */         } else {
/* 152 */           Thread.currentThread().setContextClassLoader(loader);
/*     */         }
/*     */       }
/*     */     }
/* 101 */   });
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
/*     */   public FileHandler()
/*     */   {
/* 162 */     this(null, null, null, -1);
/*     */   }
/*     */   
/*     */   public FileHandler(String directory, String prefix, String suffix)
/*     */   {
/* 167 */     this(directory, prefix, suffix, -1);
/*     */   }
/*     */   
/*     */   public FileHandler(String directory, String prefix, String suffix, int maxDays) {
/* 171 */     this.directory = directory;
/* 172 */     this.prefix = prefix;
/* 173 */     this.suffix = suffix;
/* 174 */     this.maxDays = maxDays;
/* 175 */     configure();
/* 176 */     openWriter();
/* 177 */     clean();
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
/* 188 */   private volatile String date = "";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 194 */   private String directory = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 200 */   private String prefix = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 206 */   private String suffix = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 212 */   private boolean rotatable = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 218 */   private int maxDays = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 224 */   private volatile PrintWriter writer = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 230 */   protected final ReadWriteLock writerLock = new ReentrantReadWriteLock();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 236 */   private int bufferSize = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Pattern pattern;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void publish(LogRecord record)
/*     */   {
/* 257 */     if (!isLoggable(record)) {
/* 258 */       return;
/*     */     }
/*     */     
/*     */ 
/* 262 */     Timestamp ts = new Timestamp(System.currentTimeMillis());
/* 263 */     String tsDate = ts.toString().substring(0, 10);
/*     */     
/* 265 */     this.writerLock.readLock().lock();
/*     */     try
/*     */     {
/* 268 */       if ((this.rotatable) && (!this.date.equals(tsDate)))
/*     */       {
/* 270 */         this.writerLock.readLock().unlock();
/* 271 */         this.writerLock.writeLock().lock();
/*     */         try
/*     */         {
/* 274 */           if (!this.date.equals(tsDate)) {
/* 275 */             closeWriter();
/* 276 */             this.date = tsDate;
/* 277 */             openWriter();
/* 278 */             clean();
/*     */           }
/*     */         }
/*     */         finally
/*     */         {
/* 283 */           this.writerLock.readLock().lock();
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 288 */       String result = null;
/*     */       try {
/* 290 */         result = getFormatter().format(record);
/*     */       } catch (Exception e) {
/* 292 */         reportError(null, e, 5);
/* 293 */         return;
/*     */       }
/*     */       try
/*     */       {
/* 297 */         if (this.writer != null) {
/* 298 */           this.writer.write(result);
/* 299 */           if (this.bufferSize < 0) {
/* 300 */             this.writer.flush();
/*     */           }
/*     */         } else {
/* 303 */           reportError("FileHandler is closed or not yet initialized, unable to log [" + result + "]", null, 1);
/*     */         }
/*     */       }
/*     */       catch (Exception e) {
/* 307 */         reportError(null, e, 1);
/* 308 */         return;
/*     */       }
/*     */     } finally {
/* 311 */       this.writerLock.readLock().unlock();
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
/*     */   public void close()
/*     */   {
/* 324 */     closeWriter();
/*     */   }
/*     */   
/*     */   protected void closeWriter()
/*     */   {
/* 329 */     this.writerLock.writeLock().lock();
/*     */     try {
/* 331 */       if (this.writer == null) {
/* 332 */         return;
/*     */       }
/* 334 */       this.writer.write(getFormatter().getTail(this));
/* 335 */       this.writer.flush();
/* 336 */       this.writer.close();
/* 337 */       this.writer = null;
/* 338 */       this.date = "";
/*     */     } catch (Exception e) {
/* 340 */       reportError(null, e, 3);
/*     */     } finally {
/* 342 */       this.writerLock.writeLock().unlock();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void flush()
/*     */   {
/* 353 */     this.writerLock.readLock().lock();
/*     */     try {
/* 355 */       if (this.writer == null) {
/* 356 */         return;
/*     */       }
/* 358 */       this.writer.flush();
/*     */     } catch (Exception e) {
/* 360 */       reportError(null, e, 2);
/*     */     } finally {
/* 362 */       this.writerLock.readLock().unlock();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void configure()
/*     */   {
/* 373 */     Timestamp ts = new Timestamp(System.currentTimeMillis());
/* 374 */     String tsString = ts.toString().substring(0, 19);
/* 375 */     this.date = tsString.substring(0, 10);
/*     */     
/* 377 */     String className = getClass().getName();
/*     */     
/* 379 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/*     */     
/*     */ 
/* 382 */     this.rotatable = Boolean.parseBoolean(getProperty(className + ".rotatable", "true"));
/* 383 */     if (this.directory == null) {
/* 384 */       this.directory = getProperty(className + ".directory", "logs");
/*     */     }
/* 386 */     if (this.prefix == null) {
/* 387 */       this.prefix = getProperty(className + ".prefix", "juli.");
/*     */     }
/* 389 */     if (this.suffix == null) {
/* 390 */       this.suffix = getProperty(className + ".suffix", ".log");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 395 */     boolean shouldCheckForRedundantSeparator = (!this.rotatable) && (!this.prefix.isEmpty()) && (!this.suffix.isEmpty());
/*     */     
/*     */ 
/* 398 */     if ((shouldCheckForRedundantSeparator) && 
/* 399 */       (this.prefix.charAt(this.prefix.length() - 1) == this.suffix.charAt(0))) {
/* 400 */       this.suffix = this.suffix.substring(1);
/*     */     }
/*     */     
/* 403 */     this.pattern = Pattern.compile("^(" + Pattern.quote(this.prefix) + ")\\d{4}-\\d{1,2}-\\d{1,2}(" + 
/* 404 */       Pattern.quote(this.suffix) + ")$");
/* 405 */     String sMaxDays = getProperty(className + ".maxDays", String.valueOf(-1));
/* 406 */     if (this.maxDays <= 0) {
/*     */       try {
/* 408 */         this.maxDays = Integer.parseInt(sMaxDays);
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException) {}
/*     */     }
/*     */     
/* 413 */     String sBufferSize = getProperty(className + ".bufferSize", String.valueOf(this.bufferSize));
/*     */     try {
/* 415 */       this.bufferSize = Integer.parseInt(sBufferSize);
/*     */     }
/*     */     catch (NumberFormatException localNumberFormatException1) {}
/*     */     
/*     */ 
/* 420 */     String encoding = getProperty(className + ".encoding", null);
/* 421 */     if ((encoding != null) && (encoding.length() > 0)) {
/*     */       try {
/* 423 */         setEncoding(encoding);
/*     */       }
/*     */       catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 430 */     setLevel(Level.parse(getProperty(className + ".level", "" + Level.ALL)));
/*     */     
/*     */ 
/* 433 */     String filterName = getProperty(className + ".filter", null);
/* 434 */     if (filterName != null) {
/*     */       try {
/* 436 */         setFilter((Filter)cl.loadClass(filterName).getConstructor(new Class[0]).newInstance(new Object[0]));
/*     */       }
/*     */       catch (Exception localException1) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 443 */     String formatterName = getProperty(className + ".formatter", null);
/* 444 */     if (formatterName != null) {
/*     */       try {
/* 446 */         setFormatter(
/* 447 */           (Formatter)cl.loadClass(formatterName).getConstructor(new Class[0]).newInstance(new Object[0]));
/*     */       }
/*     */       catch (Exception e) {
/* 450 */         setFormatter(new OneLineFormatter());
/*     */       }
/*     */     } else {
/* 453 */       setFormatter(new OneLineFormatter());
/*     */     }
/*     */     
/*     */ 
/* 457 */     setErrorManager(new ErrorManager());
/*     */   }
/*     */   
/*     */ 
/*     */   private String getProperty(String name, String defaultValue)
/*     */   {
/* 463 */     String value = LogManager.getLogManager().getProperty(name);
/* 464 */     if (value == null) {
/* 465 */       value = defaultValue;
/*     */     } else {
/* 467 */       value = value.trim();
/*     */     }
/* 469 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void open()
/*     */   {
/* 477 */     openWriter();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void openWriter()
/*     */   {
/* 483 */     File dir = new File(this.directory);
/* 484 */     if ((!dir.mkdirs()) && (!dir.isDirectory())) {
/* 485 */       reportError("Unable to create [" + dir + "]", null, 4);
/* 486 */       this.writer = null;
/* 487 */       return;
/*     */     }
/*     */     
/*     */ 
/* 491 */     this.writerLock.writeLock().lock();
/* 492 */     FileOutputStream fos = null;
/* 493 */     OutputStream os = null;
/*     */     try {
/* 495 */       File pathname = new File(dir.getAbsoluteFile(), this.prefix + (this.rotatable ? this.date : "") + this.suffix);
/*     */       
/* 497 */       File parent = pathname.getParentFile();
/* 498 */       if ((!parent.mkdirs()) && (!parent.isDirectory())) {
/* 499 */         reportError("Unable to create [" + parent + "]", null, 4);
/* 500 */         this.writer = null;
/* 501 */         return;
/*     */       }
/* 503 */       String encoding = getEncoding();
/* 504 */       fos = new FileOutputStream(pathname, true);
/* 505 */       os = this.bufferSize > 0 ? new BufferedOutputStream(fos, this.bufferSize) : fos;
/* 506 */       this.writer = new PrintWriter(encoding != null ? new OutputStreamWriter(os, encoding) : new OutputStreamWriter(os), false);
/*     */       
/*     */ 
/* 509 */       this.writer.write(getFormatter().getHead(this));
/*     */     } catch (Exception e) {
/* 511 */       reportError(null, e, 4);
/* 512 */       this.writer = null;
/* 513 */       if (fos != null) {
/*     */         try {
/* 515 */           fos.close();
/*     */         }
/*     */         catch (IOException localIOException) {}
/*     */       }
/*     */       
/* 520 */       if (os != null) {
/*     */         try {
/* 522 */           os.close();
/*     */         }
/*     */         catch (IOException localIOException1) {}
/*     */       }
/*     */     }
/*     */     finally {
/* 528 */       this.writerLock.writeLock().unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   private void clean() {
/* 533 */     if (this.maxDays <= 0) {
/* 534 */       return;
/*     */     }
/* 536 */     DELETE_FILES_SERVICE.submit(new Runnable()
/*     */     {
/*     */       public void run() {
/*     */         try {
/* 540 */           DirectoryStream<Path> files = FileHandler.this.streamFilesForDelete();Throwable localThrowable3 = null;
/* 541 */           try { for (Path file : files) {
/* 542 */               Files.delete(file);
/*     */             }
/*     */           }
/*     */           catch (Throwable localThrowable5)
/*     */           {
/* 540 */             localThrowable3 = localThrowable5;throw localThrowable5;
/*     */           }
/*     */           finally
/*     */           {
/* 544 */             if (files != null) if (localThrowable3 != null) try { files.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else files.close();
/* 545 */           } } catch (IOException e) { FileHandler.this.reportError("Unable to delete log files older than [" + FileHandler.this.maxDays + "] days", null, 0);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private DirectoryStream<Path> streamFilesForDelete() throws IOException
/*     */   {
/* 553 */     final Date maxDaysOffset = getMaxDaysOffset();
/* 554 */     final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
/* 555 */     Files.newDirectoryStream(new File(this.directory).toPath(), new DirectoryStream.Filter()
/*     */     {
/*     */       public boolean accept(Path path)
/*     */         throws IOException
/*     */       {
/* 560 */         boolean result = false;
/* 561 */         String date = FileHandler.this.obtainDateFromPath(path);
/* 562 */         if (date != null) {
/*     */           try {
/* 564 */             Date dateFromFile = formatter.parse(date);
/* 565 */             result = dateFromFile.before(maxDaysOffset);
/*     */           }
/*     */           catch (ParseException localParseException) {}
/*     */         }
/*     */         
/* 570 */         return result;
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private String obtainDateFromPath(Path path) {
/* 576 */     Path fileName = path.getFileName();
/* 577 */     if (fileName == null) {
/* 578 */       return null;
/*     */     }
/* 580 */     String date = fileName.toString();
/* 581 */     if (this.pattern.matcher(date).matches()) {
/* 582 */       date = date.substring(this.prefix.length());
/* 583 */       return date.substring(0, date.length() - this.suffix.length());
/*     */     }
/* 585 */     return null;
/*     */   }
/*     */   
/*     */   private Date getMaxDaysOffset()
/*     */   {
/* 590 */     Calendar cal = Calendar.getInstance();
/* 591 */     cal.set(11, 0);
/* 592 */     cal.set(12, 0);
/* 593 */     cal.set(13, 0);
/* 594 */     cal.set(14, 0);
/* 595 */     cal.add(5, -this.maxDays);
/* 596 */     return cal.getTime();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\juli\FileHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */