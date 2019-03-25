/*    */ package org.apache.catalina.manager.util;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import org.apache.catalina.Session;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class BaseSessionComparator<T>
/*    */   implements Comparator<Session>
/*    */ {
/*    */   public abstract Comparable<T> getComparableObject(Session paramSession);
/*    */   
/*    */   public final int compare(Session s1, Session s2)
/*    */   {
/* 48 */     Comparable<T> c1 = getComparableObject(s1);
/* 49 */     Comparable<T> c2 = getComparableObject(s2);
/* 50 */     return c2 == null ? 1 : c1 == null ? -1 : c2 == null ? 0 : c1.compareTo(c2);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\manage\\util\BaseSessionComparator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */