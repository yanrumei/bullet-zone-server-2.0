/*     */ package org.apache.tomcat.util.buf;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.compat.JreCompat;
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
/*     */ public class ByteBufferUtils
/*     */ {
/*  32 */   private static final StringManager sm = StringManager.getManager("org.apache.tomcat.util.buf");
/*  33 */   private static final Log log = LogFactory.getLog(ByteBufferUtils.class);
/*     */   private static final Object unsafe;
/*     */   private static final Method cleanerMethod;
/*     */   private static final Method cleanMethod;
/*     */   private static final Method invokeCleanerMethod;
/*     */   
/*     */   static
/*     */   {
/*  41 */     ByteBuffer tempBuffer = ByteBuffer.allocateDirect(0);
/*  42 */     Method cleanerMethodLocal = null;
/*  43 */     Method cleanMethodLocal = null;
/*  44 */     Object unsafeLocal = null;
/*  45 */     Method invokeCleanerMethodLocal = null;
/*  46 */     if (JreCompat.isJre9Available()) {
/*     */       try {
/*  48 */         Class<?> clazz = Class.forName("sun.misc.Unsafe");
/*  49 */         Field theUnsafe = clazz.getDeclaredField("theUnsafe");
/*  50 */         theUnsafe.setAccessible(true);
/*  51 */         unsafeLocal = theUnsafe.get(null);
/*  52 */         invokeCleanerMethodLocal = clazz.getMethod("invokeCleaner", new Class[] { ByteBuffer.class });
/*  53 */         invokeCleanerMethodLocal.invoke(unsafeLocal, new Object[] { tempBuffer });
/*     */       }
/*     */       catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException|ClassNotFoundException|NoSuchFieldException e)
/*     */       {
/*  57 */         log.warn(sm.getString("byteBufferUtils.cleaner"), e);
/*  58 */         unsafeLocal = null;
/*  59 */         invokeCleanerMethodLocal = null;
/*     */       }
/*     */     } else {
/*     */       try {
/*  63 */         cleanerMethodLocal = tempBuffer.getClass().getMethod("cleaner", new Class[0]);
/*  64 */         cleanerMethodLocal.setAccessible(true);
/*  65 */         Object cleanerObject = cleanerMethodLocal.invoke(tempBuffer, new Object[0]);
/*  66 */         cleanMethodLocal = cleanerObject.getClass().getMethod("clean", new Class[0]);
/*  67 */         cleanMethodLocal.invoke(cleanerObject, new Object[0]);
/*     */       }
/*     */       catch (NoSuchMethodException|SecurityException|IllegalAccessException|IllegalArgumentException|InvocationTargetException e) {
/*  70 */         log.warn(sm.getString("byteBufferUtils.cleaner"), e);
/*  71 */         cleanerMethodLocal = null;
/*  72 */         cleanMethodLocal = null;
/*     */       }
/*     */     }
/*  75 */     cleanerMethod = cleanerMethodLocal;
/*  76 */     cleanMethod = cleanMethodLocal;
/*  77 */     unsafe = unsafeLocal;
/*  78 */     invokeCleanerMethod = invokeCleanerMethodLocal;
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
/*     */   public static ByteBuffer expand(ByteBuffer in, int newSize)
/*     */   {
/*  98 */     if (in.capacity() >= newSize) {
/*  99 */       return in;
/*     */     }
/*     */     
/*     */ 
/* 103 */     boolean direct = false;
/* 104 */     ByteBuffer out; if (in.isDirect()) {
/* 105 */       ByteBuffer out = ByteBuffer.allocateDirect(newSize);
/* 106 */       direct = true;
/*     */     } else {
/* 108 */       out = ByteBuffer.allocate(newSize);
/*     */     }
/*     */     
/*     */ 
/* 112 */     in.flip();
/* 113 */     out.put(in);
/*     */     
/* 115 */     if (direct) {
/* 116 */       cleanDirectBuffer(in);
/*     */     }
/*     */     
/* 119 */     return out;
/*     */   }
/*     */   
/*     */   public static void cleanDirectBuffer(ByteBuffer buf) {
/* 123 */     if (cleanMethod != null) {
/*     */       try {
/* 125 */         cleanMethod.invoke(cleanerMethod.invoke(buf, new Object[0]), new Object[0]);
/*     */ 
/*     */       }
/*     */       catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException|SecurityException localIllegalAccessException) {}
/*     */     }
/* 130 */     else if (invokeCleanerMethod != null) {
/*     */       try {
/* 132 */         invokeCleanerMethod.invoke(unsafe, new Object[] { buf });
/*     */       }
/*     */       catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException|SecurityException e)
/*     */       {
/* 136 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\buf\ByteBufferUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */