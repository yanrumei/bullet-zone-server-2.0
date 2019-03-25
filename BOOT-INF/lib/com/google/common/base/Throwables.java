/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.AbstractList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated=true)
/*     */ public final class Throwables
/*     */ {
/*     */   @GwtIncompatible
/*     */   private static final String JAVA_LANG_ACCESS_CLASSNAME = "sun.misc.JavaLangAccess";
/*     */   @GwtIncompatible
/*     */   @VisibleForTesting
/*     */   static final String SHARED_SECRETS_CLASSNAME = "sun.misc.SharedSecrets";
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <X extends Throwable> void throwIfInstanceOf(Throwable throwable, Class<X> declaredType)
/*     */     throws Throwable
/*     */   {
/*  74 */     Preconditions.checkNotNull(throwable);
/*  75 */     if (declaredType.isInstance(throwable)) {
/*  76 */       throw ((Throwable)declaredType.cast(throwable));
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
/*     */   @Deprecated
/*     */   @GwtIncompatible
/*     */   public static <X extends Throwable> void propagateIfInstanceOf(@Nullable Throwable throwable, Class<X> declaredType)
/*     */     throws Throwable
/*     */   {
/* 103 */     if (throwable != null) {
/* 104 */       throwIfInstanceOf(throwable, declaredType);
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
/*     */   public static void throwIfUnchecked(Throwable throwable)
/*     */   {
/* 128 */     Preconditions.checkNotNull(throwable);
/* 129 */     if ((throwable instanceof RuntimeException)) {
/* 130 */       throw ((RuntimeException)throwable);
/*     */     }
/* 132 */     if ((throwable instanceof Error)) {
/* 133 */       throw ((Error)throwable);
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
/*     */   @Deprecated
/*     */   @GwtIncompatible
/*     */   public static void propagateIfPossible(@Nullable Throwable throwable)
/*     */   {
/* 158 */     if (throwable != null) {
/* 159 */       throwIfUnchecked(throwable);
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
/*     */   @GwtIncompatible
/*     */   public static <X extends Throwable> void propagateIfPossible(@Nullable Throwable throwable, Class<X> declaredType)
/*     */     throws Throwable
/*     */   {
/* 184 */     propagateIfInstanceOf(throwable, declaredType);
/* 185 */     propagateIfPossible(throwable);
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
/*     */   @GwtIncompatible
/*     */   public static <X1 extends Throwable, X2 extends Throwable> void propagateIfPossible(@Nullable Throwable throwable, Class<X1> declaredType1, Class<X2> declaredType2)
/*     */     throws Throwable, Throwable
/*     */   {
/* 203 */     Preconditions.checkNotNull(declaredType2);
/* 204 */     propagateIfInstanceOf(throwable, declaredType1);
/* 205 */     propagateIfPossible(throwable, declaredType2);
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
/*     */   @GwtIncompatible
/*     */   public static RuntimeException propagate(Throwable throwable)
/*     */   {
/* 240 */     throwIfUnchecked(throwable);
/* 241 */     throw new RuntimeException(throwable);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Throwable getRootCause(Throwable throwable)
/*     */   {
/*     */     Throwable cause;
/*     */     
/*     */ 
/*     */ 
/* 254 */     while ((cause = throwable.getCause()) != null) {
/* 255 */       throwable = cause;
/*     */     }
/* 257 */     return throwable;
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
/*     */   @Beta
/*     */   public static List<Throwable> getCausalChain(Throwable throwable)
/*     */   {
/* 277 */     Preconditions.checkNotNull(throwable);
/* 278 */     List<Throwable> causes = new ArrayList(4);
/* 279 */     while (throwable != null) {
/* 280 */       causes.add(throwable);
/* 281 */       throwable = throwable.getCause();
/*     */     }
/* 283 */     return Collections.unmodifiableList(causes);
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
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static <X extends Throwable> X getCauseAs(Throwable throwable, Class<X> expectedCauseType)
/*     */   {
/*     */     try
/*     */     {
/* 304 */       return (Throwable)expectedCauseType.cast(throwable.getCause());
/*     */     } catch (ClassCastException e) {
/* 306 */       e.initCause(throwable);
/* 307 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public static String getStackTraceAsString(Throwable throwable)
/*     */   {
/* 319 */     StringWriter stringWriter = new StringWriter();
/* 320 */     throwable.printStackTrace(new PrintWriter(stringWriter));
/* 321 */     return stringWriter.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static List<StackTraceElement> lazyStackTrace(Throwable throwable)
/*     */   {
/* 356 */     return lazyStackTraceIsLazy() ? 
/* 357 */       jlaStackTrace(throwable) : 
/* 358 */       Collections.unmodifiableList(Arrays.asList(throwable.getStackTrace()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static boolean lazyStackTraceIsLazy()
/*     */   {
/* 370 */     return (getStackTraceElementMethod != null ? 1 : 0) & (getStackTraceDepthMethod != null ? 1 : 0);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static List<StackTraceElement> jlaStackTrace(Throwable t) {
/* 375 */     Preconditions.checkNotNull(t);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 382 */     new AbstractList()
/*     */     {
/*     */       public StackTraceElement get(int n) {
/* 385 */         return 
/* 386 */           (StackTraceElement)Throwables.invokeAccessibleNonThrowingMethod(Throwables.getStackTraceElementMethod, Throwables.jla, new Object[] { this.val$t, Integer.valueOf(n) });
/*     */       }
/*     */       
/*     */       public int size()
/*     */       {
/* 391 */         return ((Integer)Throwables.invokeAccessibleNonThrowingMethod(Throwables.getStackTraceDepthMethod, Throwables.jla, new Object[] { this.val$t })).intValue();
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static Object invokeAccessibleNonThrowingMethod(Method method, Object receiver, Object... params)
/*     */   {
/*     */     try {
/* 400 */       return method.invoke(receiver, params);
/*     */     } catch (IllegalAccessException e) {
/* 402 */       throw new RuntimeException(e);
/*     */     } catch (InvocationTargetException e) {
/* 404 */       throw propagate(e.getCause());
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
/*     */   @Nullable
/*     */   @GwtIncompatible
/* 420 */   private static final Object jla = ;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Nullable
/*     */   @GwtIncompatible
/* 428 */   private static final Method getStackTraceElementMethod = jla == null ? null : getGetMethod();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Nullable
/*     */   @GwtIncompatible
/* 436 */   private static final Method getStackTraceDepthMethod = jla == null ? null : getSizeMethod();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Nullable
/*     */   @GwtIncompatible
/*     */   private static Object getJLA()
/*     */   {
/*     */     try
/*     */     {
/* 450 */       Class<?> sharedSecrets = Class.forName("sun.misc.SharedSecrets", false, null);
/* 451 */       Method langAccess = sharedSecrets.getMethod("getJavaLangAccess", new Class[0]);
/* 452 */       return langAccess.invoke(null, new Object[0]);
/*     */     } catch (ThreadDeath death) {
/* 454 */       throw death;
/*     */     }
/*     */     catch (Throwable t) {}
/*     */     
/*     */ 
/*     */ 
/* 460 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Nullable
/*     */   @GwtIncompatible
/*     */   private static Method getGetMethod()
/*     */   {
/* 471 */     return getJlaMethod("getStackTraceElement", new Class[] { Throwable.class, Integer.TYPE });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Nullable
/*     */   @GwtIncompatible
/*     */   private static Method getSizeMethod()
/*     */   {
/* 481 */     return getJlaMethod("getStackTraceDepth", new Class[] { Throwable.class });
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   @GwtIncompatible
/*     */   private static Method getJlaMethod(String name, Class<?>... parameterTypes) throws ThreadDeath {
/*     */     try {
/* 488 */       return Class.forName("sun.misc.JavaLangAccess", false, null).getMethod(name, parameterTypes);
/*     */     } catch (ThreadDeath death) {
/* 490 */       throw death;
/*     */     }
/*     */     catch (Throwable t) {}
/*     */     
/*     */ 
/*     */ 
/* 496 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\base\Throwables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */