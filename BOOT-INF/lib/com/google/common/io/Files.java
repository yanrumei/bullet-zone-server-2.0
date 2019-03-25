/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.TreeTraverser;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.MappedByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.FileChannel.MapMode;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class Files
/*     */ {
/*     */   private static final int TEMP_DIR_ATTEMPTS = 10000;
/*     */   
/*     */   public static BufferedReader newReader(File file, Charset charset)
/*     */     throws FileNotFoundException
/*     */   {
/*  86 */     Preconditions.checkNotNull(file);
/*  87 */     Preconditions.checkNotNull(charset);
/*  88 */     return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
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
/*     */   public static BufferedWriter newWriter(File file, Charset charset)
/*     */     throws FileNotFoundException
/*     */   {
/* 104 */     Preconditions.checkNotNull(file);
/* 105 */     Preconditions.checkNotNull(charset);
/* 106 */     return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteSource asByteSource(File file)
/*     */   {
/* 115 */     return new FileByteSource(file, null);
/*     */   }
/*     */   
/*     */   private static final class FileByteSource extends ByteSource
/*     */   {
/*     */     private final File file;
/*     */     
/*     */     private FileByteSource(File file) {
/* 123 */       this.file = ((File)Preconditions.checkNotNull(file));
/*     */     }
/*     */     
/*     */     public FileInputStream openStream() throws IOException
/*     */     {
/* 128 */       return new FileInputStream(this.file);
/*     */     }
/*     */     
/*     */     public Optional<Long> sizeIfKnown()
/*     */     {
/* 133 */       if (this.file.isFile()) {
/* 134 */         return Optional.of(Long.valueOf(this.file.length()));
/*     */       }
/* 136 */       return Optional.absent();
/*     */     }
/*     */     
/*     */     public long size()
/*     */       throws IOException
/*     */     {
/* 142 */       if (!this.file.isFile()) {
/* 143 */         throw new FileNotFoundException(this.file.toString());
/*     */       }
/* 145 */       return this.file.length();
/*     */     }
/*     */     
/*     */     public byte[] read() throws IOException
/*     */     {
/* 150 */       Closer closer = Closer.create();
/*     */       try {
/* 152 */         FileInputStream in = (FileInputStream)closer.register(openStream());
/* 153 */         return Files.readFile(in, in.getChannel().size());
/*     */       } catch (Throwable e) {
/* 155 */         throw closer.rethrow(e);
/*     */       } finally {
/* 157 */         closer.close();
/*     */       }
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 163 */       return "Files.asByteSource(" + this.file + ")";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static byte[] readFile(InputStream in, long expectedSize)
/*     */     throws IOException
/*     */   {
/* 173 */     if (expectedSize > 2147483647L) {
/* 174 */       throw new OutOfMemoryError("file is too large to fit in a byte array: " + expectedSize + " bytes");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 180 */     return expectedSize == 0L ? 
/* 181 */       ByteStreams.toByteArray(in) : 
/* 182 */       ByteStreams.toByteArray(in, (int)expectedSize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteSink asByteSink(File file, FileWriteMode... modes)
/*     */   {
/* 194 */     return new FileByteSink(file, modes, null);
/*     */   }
/*     */   
/*     */   private static final class FileByteSink extends ByteSink
/*     */   {
/*     */     private final File file;
/*     */     private final ImmutableSet<FileWriteMode> modes;
/*     */     
/*     */     private FileByteSink(File file, FileWriteMode... modes) {
/* 203 */       this.file = ((File)Preconditions.checkNotNull(file));
/* 204 */       this.modes = ImmutableSet.copyOf(modes);
/*     */     }
/*     */     
/*     */     public FileOutputStream openStream() throws IOException
/*     */     {
/* 209 */       return new FileOutputStream(this.file, this.modes.contains(FileWriteMode.APPEND));
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 214 */       return "Files.asByteSink(" + this.file + ", " + this.modes + ")";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static CharSource asCharSource(File file, Charset charset)
/*     */   {
/* 225 */     return asByteSource(file).asCharSource(charset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static CharSink asCharSink(File file, Charset charset, FileWriteMode... modes)
/*     */   {
/* 237 */     return asByteSink(file, modes).asCharSink(charset);
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
/*     */   public static byte[] toByteArray(File file)
/*     */     throws IOException
/*     */   {
/* 252 */     return asByteSource(file).read();
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
/*     */   @Deprecated
/*     */   public static String toString(File file, Charset charset)
/*     */     throws IOException
/*     */   {
/* 268 */     return asCharSource(file, charset).read();
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
/*     */   public static void write(byte[] from, File to)
/*     */     throws IOException
/*     */   {
/* 282 */     asByteSink(to, new FileWriteMode[0]).write(from);
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
/*     */   public static void copy(File from, OutputStream to)
/*     */     throws IOException
/*     */   {
/* 296 */     asByteSource(from).copyTo(to);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void copy(File from, File to)
/*     */     throws IOException
/*     */   {
/* 319 */     Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
/* 320 */     asByteSource(from).copyTo(asByteSink(to, new FileWriteMode[0]));
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
/*     */   @Deprecated
/*     */   public static void write(CharSequence from, File to, Charset charset)
/*     */     throws IOException
/*     */   {
/* 336 */     asCharSink(to, charset, new FileWriteMode[0]).write(from);
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
/*     */   @Deprecated
/*     */   public static void append(CharSequence from, File to, Charset charset)
/*     */     throws IOException
/*     */   {
/* 352 */     asCharSink(to, charset, new FileWriteMode[] { FileWriteMode.APPEND }).write(from);
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
/*     */   @Deprecated
/*     */   public static void copy(File from, Charset charset, Appendable to)
/*     */     throws IOException
/*     */   {
/* 368 */     asCharSource(from, charset).copyTo(to);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean equal(File file1, File file2)
/*     */     throws IOException
/*     */   {
/* 377 */     Preconditions.checkNotNull(file1);
/* 378 */     Preconditions.checkNotNull(file2);
/* 379 */     if ((file1 == file2) || (file1.equals(file2))) {
/* 380 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 388 */     long len1 = file1.length();
/* 389 */     long len2 = file2.length();
/* 390 */     if ((len1 != 0L) && (len2 != 0L) && (len1 != len2)) {
/* 391 */       return false;
/*     */     }
/* 393 */     return asByteSource(file1).contentEquals(asByteSource(file2));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static File createTempDir()
/*     */   {
/* 416 */     File baseDir = new File(System.getProperty("java.io.tmpdir"));
/* 417 */     String baseName = System.currentTimeMillis() + "-";
/*     */     
/* 419 */     for (int counter = 0; counter < 10000; counter++) {
/* 420 */       File tempDir = new File(baseDir, baseName + counter);
/* 421 */       if (tempDir.mkdir()) {
/* 422 */         return tempDir;
/*     */       }
/*     */     }
/* 425 */     throw new IllegalStateException("Failed to create directory within 10000 attempts (tried " + baseName + "0 to " + baseName + 9999 + ')');
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
/*     */   public static void touch(File file)
/*     */     throws IOException
/*     */   {
/* 444 */     Preconditions.checkNotNull(file);
/* 445 */     if ((!file.createNewFile()) && (!file.setLastModified(System.currentTimeMillis()))) {
/* 446 */       throw new IOException("Unable to update modification time of " + file);
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
/*     */   public static void createParentDirs(File file)
/*     */     throws IOException
/*     */   {
/* 460 */     Preconditions.checkNotNull(file);
/* 461 */     File parent = file.getCanonicalFile().getParentFile();
/* 462 */     if (parent == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 469 */       return;
/*     */     }
/* 471 */     parent.mkdirs();
/* 472 */     if (!parent.isDirectory()) {
/* 473 */       throw new IOException("Unable to create parent directories of " + file);
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
/*     */ 
/*     */   public static void move(File from, File to)
/*     */     throws IOException
/*     */   {
/* 490 */     Preconditions.checkNotNull(from);
/* 491 */     Preconditions.checkNotNull(to);
/* 492 */     Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
/*     */     
/* 494 */     if (!from.renameTo(to)) {
/* 495 */       copy(from, to);
/* 496 */       if (!from.delete()) {
/* 497 */         if (!to.delete()) {
/* 498 */           throw new IOException("Unable to delete " + to);
/*     */         }
/* 500 */         throw new IOException("Unable to delete " + from);
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
/*     */ 
/*     */   @Deprecated
/*     */   public static String readFirstLine(File file, Charset charset)
/*     */     throws IOException
/*     */   {
/* 519 */     return asCharSource(file, charset).readFirstLine();
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
/*     */ 
/*     */ 
/*     */   public static List<String> readLines(File file, Charset charset)
/*     */     throws IOException
/*     */   {
/* 541 */     
/* 542 */       (List)asCharSource(file, charset).readLines(new LineProcessor()
/*     */       {
/* 544 */         final List<String> result = Lists.newArrayList();
/*     */         
/*     */         public boolean processLine(String line)
/*     */         {
/* 548 */           this.result.add(line);
/* 549 */           return true;
/*     */         }
/*     */         
/*     */         public List<String> getResult()
/*     */         {
/* 554 */           return this.result;
/*     */         }
/*     */       });
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readLines(File file, Charset charset, LineProcessor<T> callback)
/*     */     throws IOException
/*     */   {
/* 576 */     return (T)asCharSource(file, charset).readLines(callback);
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readBytes(File file, ByteProcessor<T> processor)
/*     */     throws IOException
/*     */   {
/* 594 */     return (T)asByteSource(file).read(processor);
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
/*     */   @Deprecated
/*     */   public static HashCode hash(File file, HashFunction hashFunction)
/*     */     throws IOException
/*     */   {
/* 610 */     return asByteSource(file).hash(hashFunction);
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
/*     */   public static MappedByteBuffer map(File file)
/*     */     throws IOException
/*     */   {
/* 629 */     Preconditions.checkNotNull(file);
/* 630 */     return map(file, FileChannel.MapMode.READ_ONLY);
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
/*     */ 
/*     */   public static MappedByteBuffer map(File file, FileChannel.MapMode mode)
/*     */     throws IOException
/*     */   {
/* 651 */     Preconditions.checkNotNull(file);
/* 652 */     Preconditions.checkNotNull(mode);
/* 653 */     if (!file.exists()) {
/* 654 */       throw new FileNotFoundException(file.toString());
/*     */     }
/* 656 */     return map(file, mode, file.length());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static MappedByteBuffer map(File file, FileChannel.MapMode mode, long size)
/*     */     throws FileNotFoundException, IOException
/*     */   {
/* 680 */     Preconditions.checkNotNull(file);
/* 681 */     Preconditions.checkNotNull(mode);
/*     */     
/* 683 */     Closer closer = Closer.create();
/*     */     try
/*     */     {
/* 686 */       RandomAccessFile raf = (RandomAccessFile)closer.register(new RandomAccessFile(file, mode == FileChannel.MapMode.READ_ONLY ? "r" : "rw"));
/* 687 */       return map(raf, mode, size);
/*     */     } catch (Throwable e) {
/* 689 */       throw closer.rethrow(e);
/*     */     } finally {
/* 691 */       closer.close();
/*     */     }
/*     */   }
/*     */   
/*     */   private static MappedByteBuffer map(RandomAccessFile raf, FileChannel.MapMode mode, long size) throws IOException
/*     */   {
/* 697 */     Closer closer = Closer.create();
/*     */     try {
/* 699 */       FileChannel channel = (FileChannel)closer.register(raf.getChannel());
/* 700 */       return channel.map(mode, 0L, size);
/*     */     } catch (Throwable e) {
/* 702 */       throw closer.rethrow(e);
/*     */     } finally {
/* 704 */       closer.close();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String simplifyPath(String pathname)
/*     */   {
/* 729 */     Preconditions.checkNotNull(pathname);
/* 730 */     if (pathname.length() == 0) {
/* 731 */       return ".";
/*     */     }
/*     */     
/*     */ 
/* 735 */     Iterable<String> components = Splitter.on('/').omitEmptyStrings().split(pathname);
/* 736 */     List<String> path = new ArrayList();
/*     */     
/*     */ 
/* 739 */     for (String component : components) {
/* 740 */       if (!component.equals("."))
/*     */       {
/* 742 */         if (component.equals("..")) {
/* 743 */           if ((path.size() > 0) && (!((String)path.get(path.size() - 1)).equals(".."))) {
/* 744 */             path.remove(path.size() - 1);
/*     */           } else {
/* 746 */             path.add("..");
/*     */           }
/*     */         } else {
/* 749 */           path.add(component);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 754 */     String result = Joiner.on('/').join(path);
/* 755 */     if (pathname.charAt(0) == '/') {
/* 756 */       result = "/" + result;
/*     */     }
/*     */     
/* 759 */     while (result.startsWith("/../")) {
/* 760 */       result = result.substring(3);
/*     */     }
/* 762 */     if (result.equals("/..")) {
/* 763 */       result = "/";
/* 764 */     } else if ("".equals(result)) {
/* 765 */       result = ".";
/*     */     }
/*     */     
/* 768 */     return result;
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
/*     */   public static String getFileExtension(String fullName)
/*     */   {
/* 786 */     Preconditions.checkNotNull(fullName);
/* 787 */     String fileName = new File(fullName).getName();
/* 788 */     int dotIndex = fileName.lastIndexOf('.');
/* 789 */     return dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
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
/*     */   public static String getNameWithoutExtension(String file)
/*     */   {
/* 803 */     Preconditions.checkNotNull(file);
/* 804 */     String fileName = new File(file).getName();
/* 805 */     int dotIndex = fileName.lastIndexOf('.');
/* 806 */     return dotIndex == -1 ? fileName : fileName.substring(0, dotIndex);
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
/*     */   public static TreeTraverser<File> fileTreeTraverser()
/*     */   {
/* 820 */     return FILE_TREE_TRAVERSER;
/*     */   }
/*     */   
/* 823 */   private static final TreeTraverser<File> FILE_TREE_TRAVERSER = new TreeTraverser()
/*     */   {
/*     */ 
/*     */     public Iterable<File> children(File file)
/*     */     {
/* 828 */       if (file.isDirectory()) {
/* 829 */         File[] files = file.listFiles();
/* 830 */         if (files != null) {
/* 831 */           return Collections.unmodifiableList(Arrays.asList(files));
/*     */         }
/*     */       }
/*     */       
/* 835 */       return Collections.emptyList();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 840 */       return "Files.fileTreeTraverser()";
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Predicate<File> isDirectory()
/*     */   {
/* 850 */     return FilePredicate.IS_DIRECTORY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Predicate<File> isFile()
/*     */   {
/* 859 */     return FilePredicate.IS_FILE;
/*     */   }
/*     */   
/*     */   private static abstract enum FilePredicate implements Predicate<File> {
/* 863 */     IS_DIRECTORY, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 875 */     IS_FILE;
/*     */     
/*     */     private FilePredicate() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\io\Files.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */