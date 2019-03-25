/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.lang.ref.WeakReference;
/*    */ import java.util.Iterator;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import java.util.ServiceConfigurationError;
/*    */ import java.util.ServiceLoader;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import java.util.regex.Pattern;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible(emulated=true)
/*    */ final class Platform
/*    */ {
/* 35 */   private static final Logger logger = Logger.getLogger(Platform.class.getName());
/* 36 */   private static final PatternCompiler patternCompiler = loadPatternCompiler();
/*    */   
/*    */ 
/*    */ 
/*    */   static long systemNanoTime()
/*    */   {
/* 42 */     return System.nanoTime();
/*    */   }
/*    */   
/*    */   static CharMatcher precomputeCharMatcher(CharMatcher matcher) {
/* 46 */     return matcher.precomputedInternal();
/*    */   }
/*    */   
/*    */   static <T extends Enum<T>> Optional<T> getEnumIfPresent(Class<T> enumClass, String value) {
/* 50 */     WeakReference<? extends Enum<?>> ref = (WeakReference)Enums.getEnumConstants(enumClass).get(value);
/* 51 */     return ref == null ? 
/* 52 */       Optional.absent() : 
/* 53 */       Optional.of(enumClass.cast(ref.get()));
/*    */   }
/*    */   
/*    */   static String formatCompact4Digits(double value) {
/* 57 */     return String.format(Locale.ROOT, "%.4g", new Object[] { Double.valueOf(value) });
/*    */   }
/*    */   
/*    */   static boolean stringIsNullOrEmpty(@Nullable String string) {
/* 61 */     return (string == null) || (string.isEmpty());
/*    */   }
/*    */   
/*    */   static CommonPattern compilePattern(String pattern) {
/* 65 */     Preconditions.checkNotNull(pattern);
/* 66 */     return patternCompiler.compile(pattern);
/*    */   }
/*    */   
/*    */   static boolean usingJdkPatternCompiler() {
/* 70 */     return patternCompiler instanceof JdkPatternCompiler;
/*    */   }
/*    */   
/*    */   private static PatternCompiler loadPatternCompiler() {
/* 74 */     ServiceLoader<PatternCompiler> loader = ServiceLoader.load(PatternCompiler.class);
/*    */     try
/*    */     {
/* 77 */       for (it = loader.iterator(); it.hasNext();)
/*    */         try {
/* 79 */           return (PatternCompiler)it.next();
/*    */         } catch (ServiceConfigurationError e) {
/* 81 */           logPatternCompilerError(e);
/*    */         }
/*    */     } catch (ServiceConfigurationError e) {
/*    */       Iterator<PatternCompiler> it;
/* 85 */       logPatternCompilerError(e);
/*    */     }
/*    */     
/* 88 */     return new JdkPatternCompiler(null);
/*    */   }
/*    */   
/*    */   private static void logPatternCompilerError(ServiceConfigurationError e) {
/* 92 */     logger.log(Level.WARNING, "Error loading regex compiler, falling back to next option", e);
/*    */   }
/*    */   
/*    */   private static final class JdkPatternCompiler implements PatternCompiler
/*    */   {
/*    */     public CommonPattern compile(String pattern) {
/* 98 */       return new JdkPattern(Pattern.compile(pattern));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\base\Platform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */