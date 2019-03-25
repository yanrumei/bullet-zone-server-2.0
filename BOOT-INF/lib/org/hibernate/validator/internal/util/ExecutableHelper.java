/*     */ package org.hibernate.validator.internal.util;
/*     */ 
/*     */ import com.fasterxml.classmate.Filter;
/*     */ import com.fasterxml.classmate.MemberResolver;
/*     */ import com.fasterxml.classmate.ResolvedType;
/*     */ import com.fasterxml.classmate.ResolvedTypeWithMembers;
/*     */ import com.fasterxml.classmate.TypeResolver;
/*     */ import com.fasterxml.classmate.members.RawMethod;
/*     */ import com.fasterxml.classmate.members.ResolvedMethod;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Type;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetResolvedMemberMethods;
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
/*     */ public final class ExecutableHelper
/*     */ {
/*  36 */   private static final Log log = ;
/*     */   private final TypeResolver typeResolver;
/*     */   
/*     */   public ExecutableHelper(TypeResolutionHelper typeResolutionHelper) {
/*  40 */     this.typeResolver = typeResolutionHelper.getTypeResolver();
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
/*     */ 
/*     */   public boolean overrides(ExecutableElement executableElement, ExecutableElement other)
/*     */   {
/*  54 */     if (((executableElement.getMember() instanceof Constructor)) || ((other.getMember() instanceof Constructor))) {
/*  55 */       return false;
/*     */     }
/*     */     
/*  58 */     return overrides((Method)executableElement.getMember(), (Method)other.getMember());
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
/*     */   public boolean overrides(Method subTypeMethod, Method superTypeMethod)
/*     */   {
/*  71 */     Contracts.assertValueNotNull(subTypeMethod, "subTypeMethod");
/*  72 */     Contracts.assertValueNotNull(superTypeMethod, "superTypeMethod");
/*     */     
/*  74 */     if (subTypeMethod.equals(superTypeMethod)) {
/*  75 */       return false;
/*     */     }
/*     */     
/*  78 */     if (!subTypeMethod.getName().equals(superTypeMethod.getName())) {
/*  79 */       return false;
/*     */     }
/*     */     
/*  82 */     if (subTypeMethod.getParameterTypes().length != superTypeMethod.getParameterTypes().length) {
/*  83 */       return false;
/*     */     }
/*     */     
/*  86 */     if (!superTypeMethod.getDeclaringClass().isAssignableFrom(subTypeMethod.getDeclaringClass())) {
/*  87 */       return false;
/*     */     }
/*     */     
/*  90 */     if ((Modifier.isStatic(superTypeMethod.getModifiers())) || (Modifier.isStatic(subTypeMethod
/*  91 */       .getModifiers())))
/*     */     {
/*  93 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  98 */     if (subTypeMethod.isBridge()) {
/*  99 */       return false;
/*     */     }
/*     */     
/* 102 */     if (Modifier.isPrivate(superTypeMethod.getModifiers())) {
/* 103 */       return false;
/*     */     }
/*     */     
/* 106 */     if ((!Modifier.isPublic(superTypeMethod.getModifiers())) && (!Modifier.isProtected(superTypeMethod.getModifiers())) && 
/* 107 */       (!superTypeMethod.getDeclaringClass().getPackage().equals(subTypeMethod.getDeclaringClass().getPackage()))) {
/* 108 */       return false;
/*     */     }
/*     */     
/* 111 */     return instanceMethodParametersResolveToSameTypes(subTypeMethod, superTypeMethod);
/*     */   }
/*     */   
/*     */   public static String getSignature(String name, Class<?>[] parameterTypes) {
/* 115 */     int parameterCount = parameterTypes.length;
/*     */     
/* 117 */     StringBuilder signature = new StringBuilder(name);
/* 118 */     signature.append("(");
/* 119 */     for (int i = 0; i < parameterCount; i++) {
/* 120 */       signature.append(parameterTypes[i].getName());
/* 121 */       if (i < parameterCount - 1) {
/* 122 */         signature.append(",");
/*     */       }
/*     */     }
/* 125 */     signature.append(")");
/*     */     
/* 127 */     return signature.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean instanceMethodParametersResolveToSameTypes(Method subTypeMethod, Method superTypeMethod)
/*     */   {
/* 139 */     if (subTypeMethod.getParameterTypes().length == 0) {
/* 140 */       return true;
/*     */     }
/*     */     
/* 143 */     ResolvedType resolvedSubType = this.typeResolver.resolve(subTypeMethod.getDeclaringClass(), new Type[0]);
/*     */     
/* 145 */     MemberResolver memberResolver = new MemberResolver(this.typeResolver);
/* 146 */     memberResolver.setMethodFilter(new SimpleMethodFilter(subTypeMethod, superTypeMethod, null));
/* 147 */     ResolvedTypeWithMembers typeWithMembers = memberResolver.resolve(resolvedSubType, null, null);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 156 */     ResolvedMethod[] resolvedMethods = (ResolvedMethod[])run(GetResolvedMemberMethods.action(typeWithMembers));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 161 */     if (resolvedMethods.length == 1) {
/* 162 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 168 */       for (int i = 0; i < resolvedMethods[0].getArgumentCount(); i++)
/*     */       {
/* 170 */         if (!resolvedMethods[0].getArgumentType(i).equals(resolvedMethods[1].getArgumentType(i))) {
/* 171 */           return false;
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException e)
/*     */     {
/* 178 */       log.debug("Error in ExecutableHelper#instanceMethodParametersResolveToSameTypes comparing " + subTypeMethod + " with " + superTypeMethod);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 186 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private <T> T run(PrivilegedAction<T> action)
/*     */   {
/* 196 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */   }
/*     */   
/*     */ 
/*     */   private static class SimpleMethodFilter
/*     */     implements Filter<RawMethod>
/*     */   {
/*     */     private final Method method1;
/*     */     
/*     */     private final Method method2;
/*     */     
/*     */     private SimpleMethodFilter(Method method1, Method method2)
/*     */     {
/* 209 */       this.method1 = method1;
/* 210 */       this.method2 = method2;
/*     */     }
/*     */     
/*     */     public boolean include(RawMethod element)
/*     */     {
/* 215 */       return (element.getRawMember().equals(this.method1)) || 
/* 216 */         (element.getRawMember().equals(this.method2));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\ExecutableHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */