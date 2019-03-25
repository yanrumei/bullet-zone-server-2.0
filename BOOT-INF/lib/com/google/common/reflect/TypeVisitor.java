/*    */ package com.google.common.reflect;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.lang.reflect.GenericArrayType;
/*    */ import java.lang.reflect.ParameterizedType;
/*    */ import java.lang.reflect.Type;
/*    */ import java.lang.reflect.TypeVariable;
/*    */ import java.lang.reflect.WildcardType;
/*    */ import java.util.Set;
/*    */ import javax.annotation.concurrent.NotThreadSafe;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @NotThreadSafe
/*    */ abstract class TypeVisitor
/*    */ {
/* 59 */   private final Set<Type> visited = Sets.newHashSet();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final void visit(Type... types)
/*    */   {
/* 66 */     for (Type type : types) {
/* 67 */       if ((type != null) && (this.visited.add(type)))
/*    */       {
/*    */ 
/*    */ 
/* 71 */         boolean succeeded = false;
/*    */         try {
/* 73 */           if ((type instanceof TypeVariable)) {
/* 74 */             visitTypeVariable((TypeVariable)type);
/* 75 */           } else if ((type instanceof WildcardType)) {
/* 76 */             visitWildcardType((WildcardType)type);
/* 77 */           } else if ((type instanceof ParameterizedType)) {
/* 78 */             visitParameterizedType((ParameterizedType)type);
/* 79 */           } else if ((type instanceof Class)) {
/* 80 */             visitClass((Class)type);
/* 81 */           } else if ((type instanceof GenericArrayType)) {
/* 82 */             visitGenericArrayType((GenericArrayType)type);
/*    */           } else {
/* 84 */             throw new AssertionError("Unknown type: " + type);
/*    */           }
/* 86 */           succeeded = true;
/*    */         } finally {
/* 88 */           if (!succeeded) {
/* 89 */             this.visited.remove(type);
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   void visitClass(Class<?> t) {}
/*    */   
/*    */   void visitGenericArrayType(GenericArrayType t) {}
/*    */   
/*    */   void visitParameterizedType(ParameterizedType t) {}
/*    */   
/*    */   void visitTypeVariable(TypeVariable<?> t) {}
/*    */   
/*    */   void visitWildcardType(WildcardType t) {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\reflect\TypeVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */