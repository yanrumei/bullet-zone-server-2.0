/*     */ package org.hibernate.validator.internal.metadata.location;
/*     */ 
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Type;
/*     */ import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
/*     */ import org.hibernate.validator.internal.util.ReflectionHelper;
/*     */ import org.hibernate.validator.internal.util.TypeHelper;
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
/*     */ public class ConstraintLocation
/*     */ {
/*     */   private final Member member;
/*     */   private final Class<?> declaringClass;
/*     */   private final Type typeForValidatorResolution;
/*     */   
/*     */   public static ConstraintLocation forClass(Class<?> declaringClass)
/*     */   {
/*  54 */     Type type = declaringClass.getTypeParameters().length == 0 ? declaringClass : TypeHelper.parameterizedType(declaringClass, declaringClass.getTypeParameters());
/*     */     
/*  56 */     return new ConstraintLocation(declaringClass, null, type);
/*     */   }
/*     */   
/*     */   public static ConstraintLocation forProperty(Member member) {
/*  60 */     return new ConstraintLocation(member
/*  61 */       .getDeclaringClass(), member, 
/*     */       
/*  63 */       ReflectionHelper.typeOf(member));
/*     */   }
/*     */   
/*     */   public static ConstraintLocation forTypeArgument(Member member, Type type)
/*     */   {
/*  68 */     return new ConstraintLocation(member
/*  69 */       .getDeclaringClass(), member, type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ConstraintLocation forReturnValue(ExecutableElement executable)
/*     */   {
/*  76 */     return new ConstraintLocation(executable
/*  77 */       .getMember().getDeclaringClass(), executable
/*  78 */       .getMember(), 
/*  79 */       ReflectionHelper.typeOf(executable.getMember()));
/*     */   }
/*     */   
/*     */   public static ConstraintLocation forCrossParameter(ExecutableElement executable)
/*     */   {
/*  84 */     return new ConstraintLocation(executable
/*  85 */       .getMember().getDeclaringClass(), executable
/*  86 */       .getMember(), Object[].class);
/*     */   }
/*     */   
/*     */ 
/*     */   public static ConstraintLocation forParameter(ExecutableElement executable, int index)
/*     */   {
/*  92 */     return new ConstraintLocation(executable
/*  93 */       .getMember().getDeclaringClass(), executable
/*  94 */       .getMember(), 
/*  95 */       ReflectionHelper.typeOf(executable, index));
/*     */   }
/*     */   
/*     */   private ConstraintLocation(Class<?> declaringClass, Member member, Type typeOfAnnotatedElement)
/*     */   {
/* 100 */     this.declaringClass = declaringClass;
/* 101 */     this.member = member;
/*     */     
/* 103 */     if (((typeOfAnnotatedElement instanceof Class)) && (((Class)typeOfAnnotatedElement).isPrimitive())) {
/* 104 */       this.typeForValidatorResolution = ReflectionHelper.boxedType((Class)typeOfAnnotatedElement);
/*     */     }
/*     */     else {
/* 107 */       this.typeForValidatorResolution = typeOfAnnotatedElement;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getDeclaringClass()
/*     */   {
/* 117 */     return this.declaringClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Member getMember()
/*     */   {
/* 126 */     return this.member;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Type getTypeForValidatorResolution()
/*     */   {
/* 137 */     return this.typeForValidatorResolution;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 142 */     return "ConstraintLocation [member=" + this.member + ", declaringClass=" + this.declaringClass + ", typeForValidatorResolution=" + this.typeForValidatorResolution + "]";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 149 */     if (this == o) {
/* 150 */       return true;
/*     */     }
/* 152 */     if ((o == null) || (getClass() != o.getClass())) {
/* 153 */       return false;
/*     */     }
/*     */     
/* 156 */     ConstraintLocation that = (ConstraintLocation)o;
/*     */     
/* 158 */     if (!this.declaringClass.equals(that.declaringClass)) {
/* 159 */       return false;
/*     */     }
/* 161 */     if (this.member != null ? !this.member.equals(that.member) : that.member != null) {
/* 162 */       return false;
/*     */     }
/* 164 */     if (!this.typeForValidatorResolution.equals(that.typeForValidatorResolution)) {
/* 165 */       return false;
/*     */     }
/*     */     
/* 168 */     return true;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 173 */     int result = this.member != null ? this.member.hashCode() : 0;
/* 174 */     result = 31 * result + this.declaringClass.hashCode();
/* 175 */     result = 31 * result + this.typeForValidatorResolution.hashCode();
/* 176 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\location\ConstraintLocation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */