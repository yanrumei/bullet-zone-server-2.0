/*     */ package org.apache.tomcat.jni;
/*     */ 
/*     */ import java.io.File;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Library
/*     */ {
/*  29 */   private static final String[] NAMES = { "tcnative-1", "libtcnative-1" };
/*     */   
/*     */ 
/*     */ 
/*  33 */   private static Library _instance = null;
/*     */   
/*     */   private Library() throws Exception {
/*  36 */     boolean loaded = false;
/*  37 */     String path = System.getProperty("java.library.path");
/*  38 */     String[] paths = path.split(File.pathSeparator);
/*  39 */     StringBuilder err = new StringBuilder();
/*  40 */     String name; int j; for (int i = 0; i < NAMES.length; i++) {
/*     */       try {
/*  42 */         System.loadLibrary(NAMES[i]);
/*  43 */         loaded = true;
/*     */       } catch (ThreadDeath t) {
/*  45 */         throw t;
/*     */       }
/*     */       catch (VirtualMachineError t)
/*     */       {
/*  49 */         throw t;
/*     */       } catch (Throwable t) {
/*  51 */         name = System.mapLibraryName(NAMES[i]);
/*  52 */         for (j = 0; j < paths.length; j++) {
/*  53 */           File fd = new File(paths[j], name);
/*  54 */           if (fd.exists())
/*     */           {
/*  56 */             throw t;
/*     */           }
/*     */         }
/*  59 */         if (i > 0) {
/*  60 */           err.append(", ");
/*     */         }
/*  62 */         err.append(t.getMessage());
/*     */       }
/*  64 */       if (loaded) {
/*     */         break;
/*     */       }
/*     */     }
/*  68 */     if (!loaded) {
/*  69 */       StringBuilder names = new StringBuilder();
/*  70 */       for (String name : NAMES) {
/*  71 */         names.append(name);
/*  72 */         names.append(", ");
/*     */       }
/*  74 */       throw new LibraryNotFoundError(names.substring(0, names.length() - 2), err.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   private Library(String libraryName)
/*     */   {
/*  80 */     System.loadLibrary(libraryName);
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
/*  99 */   public static int TCN_MAJOR_VERSION = 0;
/*     */   
/* 101 */   public static int TCN_MINOR_VERSION = 0;
/*     */   
/* 103 */   public static int TCN_PATCH_VERSION = 0;
/*     */   
/* 105 */   public static int TCN_IS_DEV_VERSION = 0;
/*     */   
/* 107 */   public static int APR_MAJOR_VERSION = 0;
/*     */   
/* 109 */   public static int APR_MINOR_VERSION = 0;
/*     */   
/* 111 */   public static int APR_PATCH_VERSION = 0;
/*     */   
/* 113 */   public static int APR_IS_DEV_VERSION = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 121 */   public static boolean APR_HAVE_IPV6 = false;
/* 122 */   public static boolean APR_HAS_SHARED_MEMORY = false;
/* 123 */   public static boolean APR_HAS_THREADS = false;
/* 124 */   public static boolean APR_HAS_SENDFILE = false;
/* 125 */   public static boolean APR_HAS_MMAP = false;
/* 126 */   public static boolean APR_HAS_FORK = false;
/* 127 */   public static boolean APR_HAS_RANDOM = false;
/* 128 */   public static boolean APR_HAS_OTHER_CHILD = false;
/* 129 */   public static boolean APR_HAS_DSO = false;
/* 130 */   public static boolean APR_HAS_SO_ACCEPTFILTER = false;
/* 131 */   public static boolean APR_HAS_UNICODE_FS = false;
/* 132 */   public static boolean APR_HAS_PROC_INVOKED = false;
/* 133 */   public static boolean APR_HAS_USER = false;
/* 134 */   public static boolean APR_HAS_LARGE_FILES = false;
/* 135 */   public static boolean APR_HAS_XTHREAD_FILES = false;
/* 136 */   public static boolean APR_HAS_OS_UUID = false;
/*     */   
/* 138 */   public static boolean APR_IS_BIGENDIAN = false;
/*     */   
/*     */ 
/*     */ 
/* 142 */   public static boolean APR_FILES_AS_SOCKETS = false;
/*     */   
/*     */ 
/* 145 */   public static boolean APR_CHARSET_EBCDIC = false;
/*     */   
/*     */ 
/* 148 */   public static boolean APR_TCP_NODELAY_INHERITED = false;
/*     */   
/*     */ 
/* 151 */   public static boolean APR_O_NONBLOCK_INHERITED = false;
/*     */   public static int APR_SIZEOF_VOIDP;
/*     */   public static int APR_PATH_MAX;
/*     */   public static int APRMAXHOSTLEN;
/*     */   public static int APR_MAX_IOVEC_SIZE;
/*     */   public static int APR_MAX_SECS_TO_LINGER;
/*     */   public static int APR_MMAP_THRESHOLD;
/*     */   public static int APR_MMAP_LIMIT;
/*     */   
/*     */   private static native boolean initialize();
/*     */   
/*     */   public static native void terminate();
/*     */   
/*     */   private static native boolean has(int paramInt);
/*     */   
/*     */   private static native int version(int paramInt);
/*     */   
/*     */   private static native int size(int paramInt);
/*     */   
/*     */   public static native String versionString();
/*     */   
/*     */   public static native String aprVersionString();
/*     */   
/*     */   public static native long globalPool();
/*     */   
/* 176 */   public static synchronized boolean initialize(String libraryName) throws Exception { if (_instance == null) {
/* 177 */       if (libraryName == null) {
/* 178 */         _instance = new Library();
/*     */       } else
/* 180 */         _instance = new Library(libraryName);
/* 181 */       TCN_MAJOR_VERSION = version(1);
/* 182 */       TCN_MINOR_VERSION = version(2);
/* 183 */       TCN_PATCH_VERSION = version(3);
/* 184 */       TCN_IS_DEV_VERSION = version(4);
/* 185 */       APR_MAJOR_VERSION = version(17);
/* 186 */       APR_MINOR_VERSION = version(18);
/* 187 */       APR_PATCH_VERSION = version(19);
/* 188 */       APR_IS_DEV_VERSION = version(20);
/*     */       
/* 190 */       APR_SIZEOF_VOIDP = size(1);
/* 191 */       APR_PATH_MAX = size(2);
/* 192 */       APRMAXHOSTLEN = size(3);
/* 193 */       APR_MAX_IOVEC_SIZE = size(4);
/* 194 */       APR_MAX_SECS_TO_LINGER = size(5);
/* 195 */       APR_MMAP_THRESHOLD = size(6);
/* 196 */       APR_MMAP_LIMIT = size(7);
/*     */       
/* 198 */       APR_HAVE_IPV6 = has(0);
/* 199 */       APR_HAS_SHARED_MEMORY = has(1);
/* 200 */       APR_HAS_THREADS = has(2);
/* 201 */       APR_HAS_SENDFILE = has(3);
/* 202 */       APR_HAS_MMAP = has(4);
/* 203 */       APR_HAS_FORK = has(5);
/* 204 */       APR_HAS_RANDOM = has(6);
/* 205 */       APR_HAS_OTHER_CHILD = has(7);
/* 206 */       APR_HAS_DSO = has(8);
/* 207 */       APR_HAS_SO_ACCEPTFILTER = has(9);
/* 208 */       APR_HAS_UNICODE_FS = has(10);
/* 209 */       APR_HAS_PROC_INVOKED = has(11);
/* 210 */       APR_HAS_USER = has(12);
/* 211 */       APR_HAS_LARGE_FILES = has(13);
/* 212 */       APR_HAS_XTHREAD_FILES = has(14);
/* 213 */       APR_HAS_OS_UUID = has(15);
/* 214 */       APR_IS_BIGENDIAN = has(16);
/* 215 */       APR_FILES_AS_SOCKETS = has(17);
/* 216 */       APR_CHARSET_EBCDIC = has(18);
/* 217 */       APR_TCP_NODELAY_INHERITED = has(19);
/* 218 */       APR_O_NONBLOCK_INHERITED = has(20);
/* 219 */       if (APR_MAJOR_VERSION < 1)
/*     */       {
/* 221 */         throw new UnsatisfiedLinkError("Unsupported APR Version (" + aprVersionString() + ")");
/*     */       }
/* 223 */       if (!APR_HAS_THREADS) {
/* 224 */         throw new UnsatisfiedLinkError("Missing APR_HAS_THREADS");
/*     */       }
/*     */     }
/* 227 */     return initialize();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\jni\Library.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */