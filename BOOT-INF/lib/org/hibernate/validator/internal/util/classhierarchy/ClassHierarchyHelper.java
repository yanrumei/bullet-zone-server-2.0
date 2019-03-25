/*     */ package org.hibernate.validator.internal.util.classhierarchy;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.Contracts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassHierarchyHelper
/*     */ {
/*     */   public static <T> List<Class<? super T>> getHierarchy(Class<T> clazz, Filter... filters)
/*     */   {
/*  47 */     Contracts.assertNotNull(clazz);
/*     */     
/*  49 */     List<Class<? super T>> classes = CollectionHelper.newArrayList();
/*     */     
/*  51 */     List<Filter> allFilters = CollectionHelper.newArrayList();
/*  52 */     allFilters.addAll(Arrays.asList(filters));
/*  53 */     allFilters.add(Filters.excludeProxies());
/*     */     
/*  55 */     getHierarchy(clazz, classes, allFilters);
/*  56 */     return classes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static <T> void getHierarchy(Class<? super T> clazz, List<Class<? super T>> classes, Iterable<Filter> filters)
/*     */   {
/*  68 */     for (Class<? super T> current = clazz; current != null; current = current.getSuperclass()) {
/*  69 */       if (classes.contains(current)) {
/*  70 */         return;
/*     */       }
/*     */       
/*  73 */       if (acceptedByAllFilters(current, filters)) {
/*  74 */         classes.add(current);
/*     */       }
/*     */       
/*  77 */       for (Class<?> currentInterface : current.getInterfaces())
/*     */       {
/*     */ 
/*  80 */         Class<? super T> currentInterfaceCasted = currentInterface;
/*  81 */         getHierarchy(currentInterfaceCasted, classes, filters);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean acceptedByAllFilters(Class<?> clazz, Iterable<Filter> filters) {
/*  87 */     for (Filter classFilter : filters) {
/*  88 */       if (!classFilter.accepts(clazz)) {
/*  89 */         return false;
/*     */       }
/*     */     }
/*     */     
/*  93 */     return true;
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
/*     */   public static <T> Set<Class<? super T>> getDirectlyImplementedInterfaces(Class<T> clazz)
/*     */   {
/* 109 */     Contracts.assertNotNull(clazz);
/*     */     
/* 111 */     Set<Class<? super T>> classes = CollectionHelper.newHashSet();
/* 112 */     getImplementedInterfaces(clazz, classes);
/* 113 */     return classes;
/*     */   }
/*     */   
/*     */   private static <T> void getImplementedInterfaces(Class<? super T> clazz, Set<Class<? super T>> classes) {
/* 117 */     for (Class<?> currentInterface : clazz.getInterfaces())
/*     */     {
/* 119 */       Class<? super T> currentInterfaceCasted = currentInterface;
/* 120 */       classes.add(currentInterfaceCasted);
/* 121 */       getImplementedInterfaces(currentInterfaceCasted, classes);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\classhierarchy\ClassHierarchyHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */