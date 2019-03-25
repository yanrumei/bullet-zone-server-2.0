/*    */ package org.springframework.util.comparator;
/*    */ 
/*    */ import java.util.Comparator;
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
/*    */ public class ComparableComparator<T extends Comparable<T>>
/*    */   implements Comparator<T>
/*    */ {
/* 33 */   public static final ComparableComparator INSTANCE = new ComparableComparator();
/*    */   
/*    */   public int compare(T o1, T o2)
/*    */   {
/* 37 */     return o1.compareTo(o2);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\comparator\ComparableComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */