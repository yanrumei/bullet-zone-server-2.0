/*      */ package com.google.common.base;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible
/*      */ public final class Preconditions
/*      */ {
/*      */   public static void checkArgument(boolean expression)
/*      */   {
/*  119 */     if (!expression) {
/*  120 */       throw new IllegalArgumentException();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean expression, @Nullable Object errorMessage)
/*      */   {
/*  133 */     if (!expression) {
/*  134 */       throw new IllegalArgumentException(String.valueOf(errorMessage));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean expression, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs)
/*      */   {
/*  157 */     if (!expression) {
/*  158 */       throw new IllegalArgumentException(format(errorMessageTemplate, errorMessageArgs));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, char p1)
/*      */   {
/*  168 */     if (!b) {
/*  169 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, int p1)
/*      */   {
/*  179 */     if (!b) {
/*  180 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, long p1)
/*      */   {
/*  190 */     if (!b) {
/*  191 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1)
/*      */   {
/*  202 */     if (!b) {
/*  203 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { p1 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, char p1, char p2)
/*      */   {
/*  214 */     if (!b) {
/*  215 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, char p1, int p2)
/*      */   {
/*  226 */     if (!b) {
/*  227 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, char p1, long p2)
/*      */   {
/*  238 */     if (!b) {
/*  239 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, char p1, @Nullable Object p2)
/*      */   {
/*  250 */     if (!b) {
/*  251 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), p2 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, int p1, char p2)
/*      */   {
/*  262 */     if (!b) {
/*  263 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, int p1, int p2)
/*      */   {
/*  274 */     if (!b) {
/*  275 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, int p1, long p2)
/*      */   {
/*  286 */     if (!b) {
/*  287 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, int p1, @Nullable Object p2)
/*      */   {
/*  298 */     if (!b) {
/*  299 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), p2 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, long p1, char p2)
/*      */   {
/*  310 */     if (!b) {
/*  311 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, long p1, int p2)
/*      */   {
/*  322 */     if (!b) {
/*  323 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, long p1, long p2)
/*      */   {
/*  334 */     if (!b) {
/*  335 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, long p1, @Nullable Object p2)
/*      */   {
/*  346 */     if (!b) {
/*  347 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), p2 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, char p2)
/*      */   {
/*  358 */     if (!b) {
/*  359 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { p1, Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, int p2)
/*      */   {
/*  370 */     if (!b) {
/*  371 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { p1, Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, long p2)
/*      */   {
/*  382 */     if (!b) {
/*  383 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { p1, Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2)
/*      */   {
/*  394 */     if (!b) {
/*  395 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { p1, p2 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2, @Nullable Object p3)
/*      */   {
/*  410 */     if (!b) {
/*  411 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { p1, p2, p3 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2, @Nullable Object p3, @Nullable Object p4)
/*      */   {
/*  427 */     if (!b) {
/*  428 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { p1, p2, p3, p4 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean expression)
/*      */   {
/*  440 */     if (!expression) {
/*  441 */       throw new IllegalStateException();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean expression, @Nullable Object errorMessage)
/*      */   {
/*  455 */     if (!expression) {
/*  456 */       throw new IllegalStateException(String.valueOf(errorMessage));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean expression, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs)
/*      */   {
/*  480 */     if (!expression) {
/*  481 */       throw new IllegalStateException(format(errorMessageTemplate, errorMessageArgs));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, char p1)
/*      */   {
/*  492 */     if (!b) {
/*  493 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, int p1)
/*      */   {
/*  504 */     if (!b) {
/*  505 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, long p1)
/*      */   {
/*  516 */     if (!b) {
/*  517 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1)
/*      */   {
/*  529 */     if (!b) {
/*  530 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { p1 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, char p1, char p2)
/*      */   {
/*  542 */     if (!b) {
/*  543 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, char p1, int p2)
/*      */   {
/*  554 */     if (!b) {
/*  555 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, char p1, long p2)
/*      */   {
/*  567 */     if (!b) {
/*  568 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, char p1, @Nullable Object p2)
/*      */   {
/*  580 */     if (!b) {
/*  581 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), p2 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, int p1, char p2)
/*      */   {
/*  592 */     if (!b) {
/*  593 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, int p1, int p2)
/*      */   {
/*  604 */     if (!b) {
/*  605 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, int p1, long p2)
/*      */   {
/*  616 */     if (!b) {
/*  617 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, int p1, @Nullable Object p2)
/*      */   {
/*  629 */     if (!b) {
/*  630 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), p2 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, long p1, char p2)
/*      */   {
/*  642 */     if (!b) {
/*  643 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, long p1, int p2)
/*      */   {
/*  654 */     if (!b) {
/*  655 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, long p1, long p2)
/*      */   {
/*  667 */     if (!b) {
/*  668 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, long p1, @Nullable Object p2)
/*      */   {
/*  680 */     if (!b) {
/*  681 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), p2 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, char p2)
/*      */   {
/*  693 */     if (!b) {
/*  694 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { p1, Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, int p2)
/*      */   {
/*  706 */     if (!b) {
/*  707 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { p1, Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, long p2)
/*      */   {
/*  719 */     if (!b) {
/*  720 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { p1, Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2)
/*      */   {
/*  732 */     if (!b) {
/*  733 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { p1, p2 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2, @Nullable Object p3)
/*      */   {
/*  749 */     if (!b) {
/*  750 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { p1, p2, p3 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2, @Nullable Object p3, @Nullable Object p4)
/*      */   {
/*  767 */     if (!b) {
/*  768 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { p1, p2, p3, p4 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T reference)
/*      */   {
/*  781 */     if (reference == null) {
/*  782 */       throw new NullPointerException();
/*      */     }
/*  784 */     return reference;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T reference, @Nullable Object errorMessage)
/*      */   {
/*  798 */     if (reference == null) {
/*  799 */       throw new NullPointerException(String.valueOf(errorMessage));
/*      */     }
/*  801 */     return reference;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T reference, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs)
/*      */   {
/*  821 */     if (reference == null)
/*      */     {
/*  823 */       throw new NullPointerException(format(errorMessageTemplate, errorMessageArgs));
/*      */     }
/*  825 */     return reference;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, char p1)
/*      */   {
/*  835 */     if (obj == null) {
/*  836 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1) }));
/*      */     }
/*  838 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, int p1)
/*      */   {
/*  848 */     if (obj == null) {
/*  849 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1) }));
/*      */     }
/*  851 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, long p1)
/*      */   {
/*  861 */     if (obj == null) {
/*  862 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1) }));
/*      */     }
/*  864 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, @Nullable Object p1)
/*      */   {
/*  875 */     if (obj == null) {
/*  876 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { p1 }));
/*      */     }
/*  878 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, char p1, char p2)
/*      */   {
/*  888 */     if (obj == null) {
/*  889 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*  891 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, char p1, int p2)
/*      */   {
/*  901 */     if (obj == null) {
/*  902 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*  904 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, char p1, long p2)
/*      */   {
/*  914 */     if (obj == null) {
/*  915 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*  917 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, char p1, @Nullable Object p2)
/*      */   {
/*  928 */     if (obj == null) {
/*  929 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), p2 }));
/*      */     }
/*  931 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, int p1, char p2)
/*      */   {
/*  941 */     if (obj == null) {
/*  942 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*  944 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, int p1, int p2)
/*      */   {
/*  954 */     if (obj == null) {
/*  955 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*  957 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, int p1, long p2)
/*      */   {
/*  967 */     if (obj == null) {
/*  968 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*  970 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, int p1, @Nullable Object p2)
/*      */   {
/*  981 */     if (obj == null) {
/*  982 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), p2 }));
/*      */     }
/*  984 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, long p1, char p2)
/*      */   {
/*  994 */     if (obj == null) {
/*  995 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*  997 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, long p1, int p2)
/*      */   {
/* 1007 */     if (obj == null) {
/* 1008 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/* 1010 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, long p1, long p2)
/*      */   {
/* 1020 */     if (obj == null) {
/* 1021 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/* 1023 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, long p1, @Nullable Object p2)
/*      */   {
/* 1034 */     if (obj == null) {
/* 1035 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), p2 }));
/*      */     }
/* 1037 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, char p2)
/*      */   {
/* 1048 */     if (obj == null) {
/* 1049 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { p1, Character.valueOf(p2) }));
/*      */     }
/* 1051 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, int p2)
/*      */   {
/* 1062 */     if (obj == null) {
/* 1063 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { p1, Integer.valueOf(p2) }));
/*      */     }
/* 1065 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, long p2)
/*      */   {
/* 1076 */     if (obj == null) {
/* 1077 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { p1, Long.valueOf(p2) }));
/*      */     }
/* 1079 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2)
/*      */   {
/* 1090 */     if (obj == null) {
/* 1091 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { p1, p2 }));
/*      */     }
/* 1093 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2, @Nullable Object p3)
/*      */   {
/* 1108 */     if (obj == null) {
/* 1109 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { p1, p2, p3 }));
/*      */     }
/* 1111 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2, @Nullable Object p3, @Nullable Object p4)
/*      */   {
/* 1127 */     if (obj == null) {
/* 1128 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { p1, p2, p3, p4 }));
/*      */     }
/* 1130 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static int checkElementIndex(int index, int size)
/*      */   {
/* 1171 */     return checkElementIndex(index, size, "index");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static int checkElementIndex(int index, int size, @Nullable String desc)
/*      */   {
/* 1188 */     if ((index < 0) || (index >= size)) {
/* 1189 */       throw new IndexOutOfBoundsException(badElementIndex(index, size, desc));
/*      */     }
/* 1191 */     return index;
/*      */   }
/*      */   
/*      */   private static String badElementIndex(int index, int size, String desc) {
/* 1195 */     if (index < 0)
/* 1196 */       return format("%s (%s) must not be negative", new Object[] { desc, Integer.valueOf(index) });
/* 1197 */     if (size < 0) {
/* 1198 */       throw new IllegalArgumentException("negative size: " + size);
/*      */     }
/* 1200 */     return format("%s (%s) must be less than size (%s)", new Object[] { desc, Integer.valueOf(index), Integer.valueOf(size) });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static int checkPositionIndex(int index, int size)
/*      */   {
/* 1216 */     return checkPositionIndex(index, size, "index");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static int checkPositionIndex(int index, int size, @Nullable String desc)
/*      */   {
/* 1233 */     if ((index < 0) || (index > size)) {
/* 1234 */       throw new IndexOutOfBoundsException(badPositionIndex(index, size, desc));
/*      */     }
/* 1236 */     return index;
/*      */   }
/*      */   
/*      */   private static String badPositionIndex(int index, int size, String desc) {
/* 1240 */     if (index < 0)
/* 1241 */       return format("%s (%s) must not be negative", new Object[] { desc, Integer.valueOf(index) });
/* 1242 */     if (size < 0) {
/* 1243 */       throw new IllegalArgumentException("negative size: " + size);
/*      */     }
/* 1245 */     return format("%s (%s) must not be greater than size (%s)", new Object[] { desc, Integer.valueOf(index), Integer.valueOf(size) });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkPositionIndexes(int start, int end, int size)
/*      */   {
/* 1263 */     if ((start < 0) || (end < start) || (end > size)) {
/* 1264 */       throw new IndexOutOfBoundsException(badPositionIndexes(start, end, size));
/*      */     }
/*      */   }
/*      */   
/*      */   private static String badPositionIndexes(int start, int end, int size) {
/* 1269 */     if ((start < 0) || (start > size)) {
/* 1270 */       return badPositionIndex(start, size, "start index");
/*      */     }
/* 1272 */     if ((end < 0) || (end > size)) {
/* 1273 */       return badPositionIndex(end, size, "end index");
/*      */     }
/*      */     
/* 1276 */     return format("end index (%s) must not be less than start index (%s)", new Object[] { Integer.valueOf(end), Integer.valueOf(start) });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static String format(String template, @Nullable Object... args)
/*      */   {
/* 1291 */     template = String.valueOf(template);
/*      */     
/*      */ 
/* 1294 */     StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
/* 1295 */     int templateStart = 0;
/* 1296 */     int i = 0;
/* 1297 */     while (i < args.length) {
/* 1298 */       int placeholderStart = template.indexOf("%s", templateStart);
/* 1299 */       if (placeholderStart == -1) {
/*      */         break;
/*      */       }
/* 1302 */       builder.append(template, templateStart, placeholderStart);
/* 1303 */       builder.append(args[(i++)]);
/* 1304 */       templateStart = placeholderStart + 2;
/*      */     }
/* 1306 */     builder.append(template, templateStart, template.length());
/*      */     
/*      */ 
/* 1309 */     if (i < args.length) {
/* 1310 */       builder.append(" [");
/* 1311 */       builder.append(args[(i++)]);
/* 1312 */       while (i < args.length) {
/* 1313 */         builder.append(", ");
/* 1314 */         builder.append(args[(i++)]);
/*      */       }
/* 1316 */       builder.append(']');
/*      */     }
/*      */     
/* 1319 */     return builder.toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\base\Preconditions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */