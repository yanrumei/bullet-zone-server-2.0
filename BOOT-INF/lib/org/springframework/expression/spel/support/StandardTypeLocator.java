/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypeLocator;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardTypeLocator
/*     */   implements TypeLocator
/*     */ {
/*     */   private final ClassLoader classLoader;
/*  42 */   private final List<String> knownPackagePrefixes = new LinkedList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StandardTypeLocator()
/*     */   {
/*  50 */     this(ClassUtils.getDefaultClassLoader());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public StandardTypeLocator(ClassLoader classLoader)
/*     */   {
/*  58 */     this.classLoader = classLoader;
/*     */     
/*  60 */     registerImport("java.lang");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void registerImport(String prefix)
/*     */   {
/*  70 */     this.knownPackagePrefixes.add(prefix);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeImport(String prefix)
/*     */   {
/*  78 */     this.knownPackagePrefixes.remove(prefix);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getImportPrefixes()
/*     */   {
/*  86 */     return Collections.unmodifiableList(this.knownPackagePrefixes);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> findType(String typeName)
/*     */     throws EvaluationException
/*     */   {
/*  99 */     String nameToLookup = typeName;
/*     */     try {
/* 101 */       return ClassUtils.forName(nameToLookup, this.classLoader);
/*     */ 
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException)
/*     */     {
/* 106 */       for (String prefix : this.knownPackagePrefixes) {
/*     */         try {
/* 108 */           nameToLookup = prefix + '.' + typeName;
/* 109 */           return ClassUtils.forName(nameToLookup, this.classLoader);
/*     */         }
/*     */         catch (ClassNotFoundException localClassNotFoundException1) {}
/*     */       }
/*     */       
/*     */ 
/* 115 */       throw new SpelEvaluationException(SpelMessage.TYPE_NOT_FOUND, new Object[] { typeName });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\support\StandardTypeLocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */