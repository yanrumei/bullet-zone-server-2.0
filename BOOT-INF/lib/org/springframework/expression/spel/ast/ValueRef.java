/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
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
/*     */ public abstract interface ValueRef
/*     */ {
/*     */   public abstract TypedValue getValue();
/*     */   
/*     */   public abstract void setValue(Object paramObject);
/*     */   
/*     */   public abstract boolean isWritable();
/*     */   
/*     */   public static class NullValueRef
/*     */     implements ValueRef
/*     */   {
/*  62 */     static final NullValueRef INSTANCE = new NullValueRef();
/*     */     
/*     */     public TypedValue getValue()
/*     */     {
/*  66 */       return TypedValue.NULL;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setValue(Object newValue)
/*     */     {
/*  74 */       throw new SpelEvaluationException(0, SpelMessage.NOT_ASSIGNABLE, new Object[] { "null" });
/*     */     }
/*     */     
/*     */     public boolean isWritable()
/*     */     {
/*  79 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class TypedValueHolderValueRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final TypedValue typedValue;
/*     */     
/*     */     private final SpelNodeImpl node;
/*     */     
/*     */ 
/*     */     public TypedValueHolderValueRef(TypedValue typedValue, SpelNodeImpl node)
/*     */     {
/*  94 */       this.typedValue = typedValue;
/*  95 */       this.node = node;
/*     */     }
/*     */     
/*     */     public TypedValue getValue()
/*     */     {
/* 100 */       return this.typedValue;
/*     */     }
/*     */     
/*     */     public void setValue(Object newValue)
/*     */     {
/* 105 */       throw new SpelEvaluationException(this.node.pos, SpelMessage.NOT_ASSIGNABLE, new Object[] { this.node.toStringAST() });
/*     */     }
/*     */     
/*     */     public boolean isWritable()
/*     */     {
/* 110 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\ast\ValueRef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */