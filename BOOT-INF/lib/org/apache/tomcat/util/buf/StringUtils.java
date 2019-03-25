/*    */ package org.apache.tomcat.util.buf;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class StringUtils
/*    */ {
/*    */   private static final String EMPTY_STRING = "";
/*    */   
/*    */   public static String join(String[] array)
/*    */   {
/* 38 */     if (array == null) {
/* 39 */       return "";
/*    */     }
/* 41 */     return join(Arrays.asList(array));
/*    */   }
/*    */   
/*    */   public static void join(String[] array, char separator, StringBuilder sb)
/*    */   {
/* 46 */     if (array == null) {
/* 47 */       return;
/*    */     }
/* 49 */     join(Arrays.asList(array), separator, sb);
/*    */   }
/*    */   
/*    */   public static String join(Collection<String> collection)
/*    */   {
/* 54 */     return join(collection, ',');
/*    */   }
/*    */   
/*    */ 
/*    */   public static String join(Collection<String> collection, char separator)
/*    */   {
/* 60 */     if ((collection == null) || (collection.isEmpty())) {
/* 61 */       return "";
/*    */     }
/*    */     
/* 64 */     StringBuilder result = new StringBuilder();
/* 65 */     join(collection, separator, result);
/* 66 */     return result.toString();
/*    */   }
/*    */   
/*    */   public static void join(Iterable<String> iterable, char separator, StringBuilder sb)
/*    */   {
/* 71 */     join(iterable, separator, new Function() {
/* 72 */       public String apply(String t) { return t; } }, sb);
/*    */   }
/*    */   
/*    */ 
/*    */   public static <T> void join(T[] array, char separator, Function<T> function, StringBuilder sb)
/*    */   {
/* 78 */     if (array == null) {
/* 79 */       return;
/*    */     }
/* 81 */     join(Arrays.asList(array), separator, function, sb);
/*    */   }
/*    */   
/*    */ 
/*    */   public static <T> void join(Iterable<T> iterable, char separator, Function<T> function, StringBuilder sb)
/*    */   {
/* 87 */     if (iterable == null) {
/* 88 */       return;
/*    */     }
/* 90 */     boolean first = true;
/* 91 */     for (T value : iterable) {
/* 92 */       if (first) {
/* 93 */         first = false;
/*    */       } else {
/* 95 */         sb.append(separator);
/*    */       }
/* 97 */       sb.append(function.apply(value));
/*    */     }
/*    */   }
/*    */   
/*    */   public static abstract interface Function<T>
/*    */   {
/*    */     public abstract String apply(T paramT);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\buf\StringUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */