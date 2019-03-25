/*     */ package org.apache.tomcat.util.digester;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.EmptyStackException;
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
/*     */ public class ArrayStack<E>
/*     */   extends ArrayList<E>
/*     */ {
/*     */   private static final long serialVersionUID = 2130079159931574599L;
/*     */   
/*     */   public ArrayStack() {}
/*     */   
/*     */   public ArrayStack(int initialSize)
/*     */   {
/*  67 */     super(initialSize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean empty()
/*     */   {
/*  79 */     return isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public E peek()
/*     */     throws EmptyStackException
/*     */   {
/*  89 */     int n = size();
/*  90 */     if (n <= 0) {
/*  91 */       throw new EmptyStackException();
/*     */     }
/*  93 */     return (E)get(n - 1);
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
/*     */   public E peek(int n)
/*     */     throws EmptyStackException
/*     */   {
/* 107 */     int m = size() - n - 1;
/* 108 */     if (m < 0) {
/* 109 */       throw new EmptyStackException();
/*     */     }
/* 111 */     return (E)get(m);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public E pop()
/*     */     throws EmptyStackException
/*     */   {
/* 122 */     int n = size();
/* 123 */     if (n <= 0) {
/* 124 */       throw new EmptyStackException();
/*     */     }
/* 126 */     return (E)remove(n - 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public E push(E item)
/*     */   {
/* 138 */     add(item);
/* 139 */     return item;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\digester\ArrayStack.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */