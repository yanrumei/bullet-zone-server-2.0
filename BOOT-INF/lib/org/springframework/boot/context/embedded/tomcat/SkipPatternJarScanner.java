/*     */ package org.springframework.boot.context.embedded.tomcat;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.apache.tomcat.JarScanner;
/*     */ import org.apache.tomcat.JarScannerCallback;
/*     */ import org.apache.tomcat.util.scan.StandardJarScanFilter;
/*     */ import org.apache.tomcat.util.scan.StandardJarScanner;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SkipPatternJarScanner
/*     */   extends StandardJarScanner
/*     */ {
/*     */   private static final String JAR_SCAN_FILTER_CLASS = "org.apache.tomcat.JarScanFilter";
/*     */   private final JarScanner jarScanner;
/*     */   private final Set<String> patterns;
/*     */   
/*     */   SkipPatternJarScanner(JarScanner jarScanner, Set<String> patterns)
/*     */   {
/*  52 */     Assert.notNull(jarScanner, "JarScanner must not be null");
/*  53 */     Assert.notNull(patterns, "Patterns must not be null");
/*  54 */     this.jarScanner = jarScanner;
/*  55 */     this.patterns = patterns;
/*  56 */     setPatternToTomcat8SkipFilter();
/*     */   }
/*     */   
/*     */   private void setPatternToTomcat8SkipFilter() {
/*  60 */     if (ClassUtils.isPresent("org.apache.tomcat.JarScanFilter", null)) {
/*  61 */       new Tomcat8TldSkipSetter(this).setSkipPattern(this.patterns);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void scan(ServletContext context, ClassLoader classloader, JarScannerCallback callback, Set<String> jarsToSkip)
/*     */   {
/*  68 */     Method scanMethod = ReflectionUtils.findMethod(this.jarScanner.getClass(), "scan", new Class[] { ServletContext.class, ClassLoader.class, JarScannerCallback.class, Set.class });
/*     */     
/*     */ 
/*  71 */     Assert.notNull(scanMethod, "Unable to find scan method");
/*     */     try {
/*  73 */       scanMethod.invoke(this.jarScanner, new Object[] { context, classloader, callback, jarsToSkip == null ? this.patterns : jarsToSkip });
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*  77 */       throw new IllegalStateException("Tomcat 7 reflection failed", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static void apply(TomcatEmbeddedContext context, Set<String> patterns)
/*     */   {
/*  87 */     SkipPatternJarScanner scanner = new SkipPatternJarScanner(context.getJarScanner(), patterns);
/*     */     
/*  89 */     context.setJarScanner(scanner);
/*     */   }
/*     */   
/*     */ 
/*     */   private static class Tomcat8TldSkipSetter
/*     */   {
/*     */     private final StandardJarScanner jarScanner;
/*     */     
/*     */ 
/*     */     Tomcat8TldSkipSetter(StandardJarScanner jarScanner)
/*     */     {
/* 100 */       this.jarScanner = jarScanner;
/*     */     }
/*     */     
/*     */     public void setSkipPattern(Set<String> patterns) {
/* 104 */       StandardJarScanFilter filter = new StandardJarScanFilter();
/* 105 */       filter.setTldSkip(StringUtils.collectionToCommaDelimitedString(patterns));
/* 106 */       this.jarScanner.setJarScanFilter(filter);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\tomcat\SkipPatternJarScanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */