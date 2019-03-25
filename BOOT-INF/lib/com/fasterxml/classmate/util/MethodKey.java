/*    */ package com.fasterxml.classmate.util;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MethodKey
/*    */   implements Serializable
/*    */ {
/* 12 */   private static final Class<?>[] NO_CLASSES = new Class[0];
/*    */   
/*    */   private final String _name;
/*    */   
/*    */   private final Class<?>[] _argumentTypes;
/*    */   
/*    */   private final int _hashCode;
/*    */   
/*    */   public MethodKey(String name)
/*    */   {
/* 22 */     this._name = name;
/* 23 */     this._argumentTypes = NO_CLASSES;
/* 24 */     this._hashCode = name.hashCode();
/*    */   }
/*    */   
/*    */   public MethodKey(String name, Class<?>[] argTypes)
/*    */   {
/* 29 */     this._name = name;
/* 30 */     this._argumentTypes = argTypes;
/* 31 */     this._hashCode = (name.hashCode() + argTypes.length);
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
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 45 */     if (o == this) return true;
/* 46 */     if ((o == null) || (o.getClass() != getClass())) return false;
/* 47 */     MethodKey other = (MethodKey)o;
/* 48 */     Class<?>[] otherArgs = other._argumentTypes;
/* 49 */     int len = this._argumentTypes.length;
/* 50 */     if (otherArgs.length != len) return false;
/* 51 */     for (int i = 0; i < len; i++) {
/* 52 */       if (otherArgs[i] != this._argumentTypes[i]) return false;
/*    */     }
/* 54 */     return this._name.equals(other._name);
/*    */   }
/*    */   
/* 57 */   public int hashCode() { return this._hashCode; }
/*    */   
/*    */   public String toString()
/*    */   {
/* 61 */     StringBuilder sb = new StringBuilder();
/* 62 */     sb.append(this._name);
/* 63 */     sb.append('(');
/* 64 */     int i = 0; for (int len = this._argumentTypes.length; i < len; i++) {
/* 65 */       if (i > 0) sb.append(',');
/* 66 */       sb.append(this._argumentTypes[i].getName());
/*    */     }
/* 68 */     sb.append(')');
/* 69 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmat\\util\MethodKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */