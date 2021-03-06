/*     */ package org.springframework.aop.aspectj;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import org.aspectj.weaver.ReferenceType;
/*     */ import org.aspectj.weaver.ReferenceTypeDelegate;
/*     */ import org.aspectj.weaver.ResolvedType;
/*     */ import org.aspectj.weaver.ast.And;
/*     */ import org.aspectj.weaver.ast.Call;
/*     */ import org.aspectj.weaver.ast.FieldGetCall;
/*     */ import org.aspectj.weaver.ast.HasAnnotation;
/*     */ import org.aspectj.weaver.ast.ITestVisitor;
/*     */ import org.aspectj.weaver.ast.Instanceof;
/*     */ import org.aspectj.weaver.ast.Literal;
/*     */ import org.aspectj.weaver.ast.Not;
/*     */ import org.aspectj.weaver.ast.Or;
/*     */ import org.aspectj.weaver.ast.Test;
/*     */ import org.aspectj.weaver.internal.tools.MatchingContextBasedTest;
/*     */ import org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate;
/*     */ import org.aspectj.weaver.reflect.ReflectionVar;
/*     */ import org.aspectj.weaver.reflect.ShadowMatchImpl;
/*     */ import org.aspectj.weaver.tools.ShadowMatch;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ class RuntimeTestWalker
/*     */ {
/*     */   private static final Field residualTestField;
/*     */   private static final Field varTypeField;
/*     */   private static final Field myClassField;
/*     */   private final Test runtimeTest;
/*     */   
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  70 */       residualTestField = ShadowMatchImpl.class.getDeclaredField("residualTest");
/*  71 */       varTypeField = ReflectionVar.class.getDeclaredField("varType");
/*  72 */       myClassField = ReflectionBasedReferenceTypeDelegate.class.getDeclaredField("myClass");
/*     */     }
/*     */     catch (NoSuchFieldException ex) {
/*  75 */       throw new IllegalStateException("The version of aspectjtools.jar / aspectjweaver.jar on the classpath is incompatible with this version of Spring: " + ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RuntimeTestWalker(ShadowMatch shadowMatch)
/*     */   {
/*     */     try
/*     */     {
/*  86 */       ReflectionUtils.makeAccessible(residualTestField);
/*  87 */       this.runtimeTest = ((Test)residualTestField.get(shadowMatch));
/*     */     }
/*     */     catch (IllegalAccessException ex) {
/*  90 */       throw new IllegalStateException(ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean testsSubtypeSensitiveVars()
/*     */   {
/* 100 */     return (this.runtimeTest != null) && 
/* 101 */       (new SubtypeSensitiveVarTypeTestVisitor(null).testsSubtypeSensitiveVars(this.runtimeTest));
/*     */   }
/*     */   
/*     */   public boolean testThisInstanceOfResidue(Class<?> thisClass) {
/* 105 */     return (this.runtimeTest != null) && 
/* 106 */       (new ThisInstanceOfResidueTestVisitor(thisClass).thisInstanceOfMatches(this.runtimeTest));
/*     */   }
/*     */   
/*     */   public boolean testTargetInstanceOfResidue(Class<?> targetClass) {
/* 110 */     return (this.runtimeTest != null) && 
/* 111 */       (new TargetInstanceOfResidueTestVisitor(targetClass).targetInstanceOfMatches(this.runtimeTest));
/*     */   }
/*     */   
/*     */   private static class TestVisitorAdapter
/*     */     implements ITestVisitor
/*     */   {
/*     */     protected static final int THIS_VAR = 0;
/*     */     protected static final int TARGET_VAR = 1;
/*     */     protected static final int AT_THIS_VAR = 3;
/*     */     protected static final int AT_TARGET_VAR = 4;
/*     */     protected static final int AT_ANNOTATION_VAR = 8;
/*     */     
/*     */     public void visit(And e)
/*     */     {
/* 125 */       e.getLeft().accept(this);
/* 126 */       e.getRight().accept(this);
/*     */     }
/*     */     
/*     */     public void visit(Or e)
/*     */     {
/* 131 */       e.getLeft().accept(this);
/* 132 */       e.getRight().accept(this);
/*     */     }
/*     */     
/*     */     public void visit(Not e)
/*     */     {
/* 137 */       e.getBody().accept(this);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void visit(Instanceof i) {}
/*     */     
/*     */ 
/*     */ 
/*     */     public void visit(Literal literal) {}
/*     */     
/*     */ 
/*     */ 
/*     */     public void visit(Call call) {}
/*     */     
/*     */ 
/*     */     public void visit(FieldGetCall fieldGetCall) {}
/*     */     
/*     */ 
/*     */     public void visit(HasAnnotation hasAnnotation) {}
/*     */     
/*     */ 
/*     */     public void visit(MatchingContextBasedTest matchingContextTest) {}
/*     */     
/*     */ 
/*     */     protected int getVarType(ReflectionVar v)
/*     */     {
/*     */       try
/*     */       {
/* 166 */         ReflectionUtils.makeAccessible(RuntimeTestWalker.varTypeField);
/* 167 */         return ((Integer)RuntimeTestWalker.varTypeField.get(v)).intValue();
/*     */       }
/*     */       catch (IllegalAccessException ex) {
/* 170 */         throw new IllegalStateException(ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class InstanceOfResidueTestVisitor
/*     */     extends RuntimeTestWalker.TestVisitorAdapter
/*     */   {
/*     */     private final Class<?> matchClass;
/*     */     private boolean matches;
/*     */     private final int matchVarType;
/*     */     
/*     */     public InstanceOfResidueTestVisitor(Class<?> matchClass, boolean defaultMatches, int matchVarType)
/*     */     {
/* 184 */       super();
/* 185 */       this.matchClass = matchClass;
/* 186 */       this.matches = defaultMatches;
/* 187 */       this.matchVarType = matchVarType;
/*     */     }
/*     */     
/*     */     public boolean instanceOfMatches(Test test) {
/* 191 */       test.accept(this);
/* 192 */       return this.matches;
/*     */     }
/*     */     
/*     */     public void visit(Instanceof i)
/*     */     {
/* 197 */       int varType = getVarType((ReflectionVar)i.getVar());
/* 198 */       if (varType != this.matchVarType) {
/* 199 */         return;
/*     */       }
/* 201 */       Class<?> typeClass = null;
/* 202 */       ResolvedType type = (ResolvedType)i.getType();
/* 203 */       if ((type instanceof ReferenceType)) {
/* 204 */         ReferenceTypeDelegate delegate = ((ReferenceType)type).getDelegate();
/* 205 */         if ((delegate instanceof ReflectionBasedReferenceTypeDelegate)) {
/*     */           try {
/* 207 */             ReflectionUtils.makeAccessible(RuntimeTestWalker.myClassField);
/* 208 */             typeClass = (Class)RuntimeTestWalker.myClassField.get(delegate);
/*     */           }
/*     */           catch (IllegalAccessException ex) {
/* 211 */             throw new IllegalStateException(ex);
/*     */           }
/*     */         }
/*     */       }
/*     */       try
/*     */       {
/* 217 */         if (typeClass == null) {
/* 218 */           typeClass = ClassUtils.forName(type.getName(), this.matchClass.getClassLoader());
/*     */         }
/* 220 */         this.matches = typeClass.isAssignableFrom(this.matchClass);
/*     */       }
/*     */       catch (ClassNotFoundException ex) {
/* 223 */         this.matches = false;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class TargetInstanceOfResidueTestVisitor
/*     */     extends RuntimeTestWalker.InstanceOfResidueTestVisitor
/*     */   {
/*     */     public TargetInstanceOfResidueTestVisitor(Class<?> targetClass)
/*     */     {
/* 235 */       super(false, 1);
/*     */     }
/*     */     
/*     */     public boolean targetInstanceOfMatches(Test test) {
/* 239 */       return instanceOfMatches(test);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class ThisInstanceOfResidueTestVisitor
/*     */     extends RuntimeTestWalker.InstanceOfResidueTestVisitor
/*     */   {
/*     */     public ThisInstanceOfResidueTestVisitor(Class<?> thisClass)
/*     */     {
/* 250 */       super(true, 0);
/*     */     }
/*     */     
/*     */     public boolean thisInstanceOfMatches(Test test)
/*     */     {
/* 255 */       return instanceOfMatches(test);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SubtypeSensitiveVarTypeTestVisitor extends RuntimeTestWalker.TestVisitorAdapter {
/* 260 */     private SubtypeSensitiveVarTypeTestVisitor() { super(); }
/*     */     
/* 262 */     private final Object thisObj = new Object();
/*     */     
/* 264 */     private final Object targetObj = new Object();
/*     */     
/* 266 */     private final Object[] argsObjs = new Object[0];
/*     */     
/* 268 */     private boolean testsSubtypeSensitiveVars = false;
/*     */     
/*     */     public boolean testsSubtypeSensitiveVars(Test aTest) {
/* 271 */       aTest.accept(this);
/* 272 */       return this.testsSubtypeSensitiveVars;
/*     */     }
/*     */     
/*     */     public void visit(Instanceof i)
/*     */     {
/* 277 */       ReflectionVar v = (ReflectionVar)i.getVar();
/* 278 */       Object varUnderTest = v.getBindingAtJoinPoint(this.thisObj, this.targetObj, this.argsObjs);
/* 279 */       if ((varUnderTest == this.thisObj) || (varUnderTest == this.targetObj)) {
/* 280 */         this.testsSubtypeSensitiveVars = true;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void visit(HasAnnotation hasAnn)
/*     */     {
/* 287 */       ReflectionVar v = (ReflectionVar)hasAnn.getVar();
/* 288 */       int varType = getVarType(v);
/* 289 */       if ((varType == 3) || (varType == 4) || (varType == 8)) {
/* 290 */         this.testsSubtypeSensitiveVars = true;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\aspectj\RuntimeTestWalker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */