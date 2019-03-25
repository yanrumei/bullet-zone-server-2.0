/*     */ package org.hibernate.validator.internal.metadata.core;
/*     */ 
/*     */ import java.lang.reflect.Member;
/*     */ import java.util.Map;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
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
/*     */ public class AnnotationProcessingOptionsImpl
/*     */   implements AnnotationProcessingOptions
/*     */ {
/*  26 */   private static final Log log = ;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  32 */   private final Map<Class<?>, Boolean> ignoreAnnotationDefaults = CollectionHelper.newHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  37 */   private final Map<Class<?>, Boolean> annotationIgnoresForClasses = CollectionHelper.newHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  42 */   private final Map<Member, Boolean> annotationIgnoredForMembers = CollectionHelper.newHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  47 */   private final Map<Member, Boolean> annotationIgnoresForReturnValues = CollectionHelper.newHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  52 */   private final Map<Member, Boolean> annotationIgnoresForCrossParameter = CollectionHelper.newHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  57 */   private final Map<ExecutableParameterKey, Boolean> annotationIgnoresForMethodParameter = CollectionHelper.newHashMap();
/*     */   
/*     */   public boolean areMemberConstraintsIgnoredFor(Member member)
/*     */   {
/*  61 */     Class<?> clazz = member.getDeclaringClass();
/*  62 */     if (this.annotationIgnoredForMembers.containsKey(member)) {
/*  63 */       return ((Boolean)this.annotationIgnoredForMembers.get(member)).booleanValue();
/*     */     }
/*     */     
/*  66 */     return areAllConstraintAnnotationsIgnoredFor(clazz);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean areReturnValueConstraintsIgnoredFor(Member member)
/*     */   {
/*  72 */     if (this.annotationIgnoresForReturnValues.containsKey(member)) {
/*  73 */       return ((Boolean)this.annotationIgnoresForReturnValues.get(member)).booleanValue();
/*     */     }
/*     */     
/*  76 */     return areMemberConstraintsIgnoredFor(member);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean areCrossParameterConstraintsIgnoredFor(Member member)
/*     */   {
/*  82 */     if (this.annotationIgnoresForCrossParameter.containsKey(member)) {
/*  83 */       return ((Boolean)this.annotationIgnoresForCrossParameter.get(member)).booleanValue();
/*     */     }
/*     */     
/*  86 */     return areMemberConstraintsIgnoredFor(member);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean areParameterConstraintsIgnoredFor(Member member, int index)
/*     */   {
/*  92 */     ExecutableParameterKey key = new ExecutableParameterKey(member, index);
/*  93 */     if (this.annotationIgnoresForMethodParameter.containsKey(key)) {
/*  94 */       return ((Boolean)this.annotationIgnoresForMethodParameter.get(key)).booleanValue();
/*     */     }
/*     */     
/*  97 */     return areMemberConstraintsIgnoredFor(member);
/*     */   }
/*     */   
/*     */   public boolean areClassLevelConstraintsIgnoredFor(Class<?> clazz)
/*     */   {
/*     */     boolean ignoreAnnotation;
/*     */     boolean ignoreAnnotation;
/* 104 */     if (this.annotationIgnoresForClasses.containsKey(clazz)) {
/* 105 */       ignoreAnnotation = ((Boolean)this.annotationIgnoresForClasses.get(clazz)).booleanValue();
/*     */     }
/*     */     else {
/* 108 */       ignoreAnnotation = areAllConstraintAnnotationsIgnoredFor(clazz);
/*     */     }
/* 110 */     if ((log.isDebugEnabled()) && (ignoreAnnotation)) {
/* 111 */       log.debugf("Class level annotation are getting ignored for %s.", clazz.getName());
/*     */     }
/* 113 */     return ignoreAnnotation;
/*     */   }
/*     */   
/*     */   public void merge(AnnotationProcessingOptions annotationProcessingOptions)
/*     */   {
/* 118 */     AnnotationProcessingOptionsImpl annotationProcessingOptionsImpl = (AnnotationProcessingOptionsImpl)annotationProcessingOptions;
/*     */     
/*     */ 
/* 121 */     this.ignoreAnnotationDefaults.putAll(annotationProcessingOptionsImpl.ignoreAnnotationDefaults);
/* 122 */     this.annotationIgnoresForClasses.putAll(annotationProcessingOptionsImpl.annotationIgnoresForClasses);
/* 123 */     this.annotationIgnoredForMembers.putAll(annotationProcessingOptionsImpl.annotationIgnoredForMembers);
/* 124 */     this.annotationIgnoresForReturnValues
/* 125 */       .putAll(annotationProcessingOptionsImpl.annotationIgnoresForReturnValues);
/* 126 */     this.annotationIgnoresForCrossParameter
/* 127 */       .putAll(annotationProcessingOptionsImpl.annotationIgnoresForCrossParameter);
/* 128 */     this.annotationIgnoresForMethodParameter.putAll(annotationProcessingOptionsImpl.annotationIgnoresForMethodParameter);
/*     */   }
/*     */   
/*     */   public void ignoreAnnotationConstraintForClass(Class<?> clazz, Boolean b) {
/* 132 */     if (b == null) {
/* 133 */       this.ignoreAnnotationDefaults.put(clazz, Boolean.TRUE);
/*     */     }
/*     */     else {
/* 136 */       this.ignoreAnnotationDefaults.put(clazz, b);
/*     */     }
/*     */   }
/*     */   
/*     */   public void ignoreConstraintAnnotationsOnMember(Member member, Boolean b) {
/* 141 */     this.annotationIgnoredForMembers.put(member, b);
/*     */   }
/*     */   
/*     */   public void ignoreConstraintAnnotationsForReturnValue(Member member, Boolean b) {
/* 145 */     this.annotationIgnoresForReturnValues.put(member, b);
/*     */   }
/*     */   
/*     */   public void ignoreConstraintAnnotationsForCrossParameterConstraint(Member member, Boolean b) {
/* 149 */     this.annotationIgnoresForCrossParameter.put(member, b);
/*     */   }
/*     */   
/*     */   public void ignoreConstraintAnnotationsOnParameter(Member member, int index, Boolean b) {
/* 153 */     ExecutableParameterKey key = new ExecutableParameterKey(member, index);
/* 154 */     this.annotationIgnoresForMethodParameter.put(key, b);
/*     */   }
/*     */   
/*     */   public void ignoreClassLevelConstraintAnnotations(Class<?> clazz, boolean b) {
/* 158 */     this.annotationIgnoresForClasses.put(clazz, Boolean.valueOf(b));
/*     */   }
/*     */   
/*     */   private boolean areAllConstraintAnnotationsIgnoredFor(Class<?> clazz) {
/* 162 */     return (this.ignoreAnnotationDefaults.containsKey(clazz)) && (((Boolean)this.ignoreAnnotationDefaults.get(clazz)).booleanValue());
/*     */   }
/*     */   
/*     */   public class ExecutableParameterKey {
/*     */     private final Member member;
/*     */     private final int index;
/*     */     
/*     */     public ExecutableParameterKey(Member member, int index) {
/* 170 */       this.member = member;
/* 171 */       this.index = index;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o)
/*     */     {
/* 176 */       if (this == o) {
/* 177 */         return true;
/*     */       }
/* 179 */       if ((o == null) || (getClass() != o.getClass())) {
/* 180 */         return false;
/*     */       }
/*     */       
/* 183 */       ExecutableParameterKey that = (ExecutableParameterKey)o;
/*     */       
/* 185 */       if (this.index != that.index) {
/* 186 */         return false;
/*     */       }
/* 188 */       if (this.member != null ? !this.member.equals(that.member) : that.member != null) {
/* 189 */         return false;
/*     */       }
/*     */       
/* 192 */       return true;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 197 */       int result = this.member != null ? this.member.hashCode() : 0;
/* 198 */       result = 31 * result + this.index;
/* 199 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\core\AnnotationProcessingOptionsImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */