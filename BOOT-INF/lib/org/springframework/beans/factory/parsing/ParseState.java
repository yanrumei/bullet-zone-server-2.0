/*     */ package org.springframework.beans.factory.parsing;
/*     */ 
/*     */ import java.util.Stack;
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
/*     */ public final class ParseState
/*     */ {
/*     */   private static final char TAB = '\t';
/*     */   private final Stack<Entry> state;
/*     */   
/*     */   public ParseState()
/*     */   {
/*  50 */     this.state = new Stack();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ParseState(ParseState other)
/*     */   {
/*  59 */     this.state = ((Stack)other.state.clone());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void push(Entry entry)
/*     */   {
/*  67 */     this.state.push(entry);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void pop()
/*     */   {
/*  74 */     this.state.pop();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Entry peek()
/*     */   {
/*  82 */     return this.state.empty() ? null : (Entry)this.state.peek();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ParseState snapshot()
/*     */   {
/*  90 */     return new ParseState(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/*  99 */     StringBuilder sb = new StringBuilder();
/* 100 */     for (int x = 0; x < this.state.size(); x++) {
/* 101 */       if (x > 0) {
/* 102 */         sb.append('\n');
/* 103 */         for (int y = 0; y < x; y++) {
/* 104 */           sb.append('\t');
/*     */         }
/* 106 */         sb.append("-> ");
/*     */       }
/* 108 */       sb.append(this.state.get(x));
/*     */     }
/* 110 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static abstract interface Entry {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\parsing\ParseState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */