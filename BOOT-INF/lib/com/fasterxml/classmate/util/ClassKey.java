/*    */ package com.fasterxml.classmate.util;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public class ClassKey
/*    */   implements Comparable<ClassKey>, Serializable
/*    */ {
/*    */   private final String _className;
/*    */   private final Class<?> _class;
/*    */   private final int _hashCode;
/*    */   
/*    */   public ClassKey(Class<?> clz)
/*    */   {
/* 23 */     this._class = clz;
/* 24 */     this._className = clz.getName();
/* 25 */     this._hashCode = this._className.hashCode();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int compareTo(ClassKey other)
/*    */   {
/* 38 */     return this._className.compareTo(other._className);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 50 */     if (o == this) return true;
/* 51 */     if (o == null) return false;
/* 52 */     if (o.getClass() != getClass()) return false;
/* 53 */     ClassKey other = (ClassKey)o;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 62 */     return other._class == this._class;
/*    */   }
/*    */   
/* 65 */   public int hashCode() { return this._hashCode; }
/*    */   
/* 67 */   public String toString() { return this._className; }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmat\\util\ClassKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */