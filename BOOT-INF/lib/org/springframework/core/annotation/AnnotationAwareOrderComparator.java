/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.core.DecoratingProxy;
/*     */ import org.springframework.core.OrderComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationAwareOrderComparator
/*     */   extends OrderComparator
/*     */ {
/*  52 */   public static final AnnotationAwareOrderComparator INSTANCE = new AnnotationAwareOrderComparator();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Integer findOrder(Object obj)
/*     */   {
/*  63 */     Integer order = super.findOrder(obj);
/*  64 */     if (order != null) {
/*  65 */       return order;
/*     */     }
/*     */     
/*     */ 
/*  69 */     if ((obj instanceof Class)) {
/*  70 */       return OrderUtils.getOrder((Class)obj);
/*     */     }
/*  72 */     if ((obj instanceof Method)) {
/*  73 */       Order ann = (Order)AnnotationUtils.findAnnotation((Method)obj, Order.class);
/*  74 */       if (ann != null) {
/*  75 */         return Integer.valueOf(ann.value());
/*     */       }
/*     */     }
/*  78 */     else if ((obj instanceof AnnotatedElement)) {
/*  79 */       Order ann = (Order)AnnotationUtils.getAnnotation((AnnotatedElement)obj, Order.class);
/*  80 */       if (ann != null) {
/*  81 */         return Integer.valueOf(ann.value());
/*     */       }
/*     */     }
/*  84 */     else if (obj != null) {
/*  85 */       order = OrderUtils.getOrder(obj.getClass());
/*  86 */       if ((order == null) && ((obj instanceof DecoratingProxy))) {
/*  87 */         order = OrderUtils.getOrder(((DecoratingProxy)obj).getDecoratedClass());
/*     */       }
/*     */     }
/*     */     
/*  91 */     return order;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getPriority(Object obj)
/*     */   {
/* 101 */     Integer priority = null;
/* 102 */     if ((obj instanceof Class)) {
/* 103 */       priority = OrderUtils.getPriority((Class)obj);
/*     */     }
/* 105 */     else if (obj != null) {
/* 106 */       priority = OrderUtils.getPriority(obj.getClass());
/* 107 */       if ((priority == null) && ((obj instanceof DecoratingProxy))) {
/* 108 */         priority = OrderUtils.getOrder(((DecoratingProxy)obj).getDecoratedClass());
/*     */       }
/*     */     }
/* 111 */     return priority;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void sort(List<?> list)
/*     */   {
/* 123 */     if (list.size() > 1) {
/* 124 */       Collections.sort(list, INSTANCE);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void sort(Object[] array)
/*     */   {
/* 136 */     if (array.length > 1) {
/* 137 */       Arrays.sort(array, INSTANCE);
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
/*     */   public static void sortIfNecessary(Object value)
/*     */   {
/* 150 */     if ((value instanceof Object[])) {
/* 151 */       sort((Object[])value);
/*     */     }
/* 153 */     else if ((value instanceof List)) {
/* 154 */       sort((List)value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\annotation\AnnotationAwareOrderComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */