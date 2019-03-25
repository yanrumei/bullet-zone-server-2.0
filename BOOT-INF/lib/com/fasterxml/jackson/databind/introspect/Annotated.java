/*    */ package com.fasterxml.jackson.databind.introspect;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.type.TypeBindings;
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.reflect.AnnotatedElement;
/*    */ import java.lang.reflect.Modifier;
/*    */ import java.lang.reflect.Type;
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
/*    */ public abstract class Annotated
/*    */ {
/*    */   public abstract <A extends Annotation> A getAnnotation(Class<A> paramClass);
/*    */   
/*    */   public abstract boolean hasAnnotation(Class<?> paramClass);
/*    */   
/*    */   public abstract boolean hasOneOf(Class<? extends Annotation>[] paramArrayOfClass);
/*    */   
/*    */   public abstract Annotated withAnnotations(AnnotationMap paramAnnotationMap);
/*    */   
/*    */   public final Annotated withFallBackAnnotationsFrom(Annotated annotated)
/*    */   {
/* 39 */     return withAnnotations(AnnotationMap.merge(getAllAnnotations(), annotated.getAllAnnotations()));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public abstract AnnotatedElement getAnnotated();
/*    */   
/*    */ 
/*    */   protected abstract int getModifiers();
/*    */   
/*    */ 
/*    */   public final boolean isPublic()
/*    */   {
/* 52 */     return Modifier.isPublic(getModifiers());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract String getName();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract JavaType getType();
/*    */   
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public final JavaType getType(TypeBindings bogus)
/*    */   {
/* 70 */     return getType();
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
/*    */   @Deprecated
/*    */   public Type getGenericType()
/*    */   {
/* 84 */     return getRawType();
/*    */   }
/*    */   
/*    */   public abstract Class<?> getRawType();
/*    */   
/*    */   public abstract Iterable<Annotation> annotations();
/*    */   
/*    */   protected abstract AnnotationMap getAllAnnotations();
/*    */   
/*    */   public abstract boolean equals(Object paramObject);
/*    */   
/*    */   public abstract int hashCode();
/*    */   
/*    */   public abstract String toString();
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\introspect\Annotated.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */