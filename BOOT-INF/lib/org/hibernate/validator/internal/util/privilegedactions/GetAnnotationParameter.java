/*    */ package org.hibernate.validator.internal.util.privilegedactions;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.security.PrivilegedAction;
/*    */ import org.hibernate.validator.internal.util.logging.Log;
/*    */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
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
/*    */ public final class GetAnnotationParameter<T>
/*    */   implements PrivilegedAction<T>
/*    */ {
/* 23 */   private static final Log log = ;
/*    */   private final Annotation annotation;
/*    */   private final String parameterName;
/*    */   private final Class<T> type;
/*    */   
/*    */   public static <T> GetAnnotationParameter<T> action(Annotation annotation, String parameterName, Class<T> type)
/*    */   {
/* 30 */     return new GetAnnotationParameter(annotation, parameterName, type);
/*    */   }
/*    */   
/*    */   private GetAnnotationParameter(Annotation annotation, String parameterName, Class<T> type) {
/* 34 */     this.annotation = annotation;
/* 35 */     this.parameterName = parameterName;
/* 36 */     this.type = type;
/*    */   }
/*    */   
/*    */   public T run()
/*    */   {
/*    */     try {
/* 42 */       Method m = this.annotation.getClass().getMethod(this.parameterName, new Class[0]);
/* 43 */       m.setAccessible(true);
/* 44 */       Object o = m.invoke(this.annotation, new Object[0]);
/* 45 */       if (this.type.isAssignableFrom(o.getClass())) {
/* 46 */         return (T)o;
/*    */       }
/*    */       
/* 49 */       throw log.getWrongParameterTypeException(this.type.getName(), o.getClass().getName());
/*    */     }
/*    */     catch (NoSuchMethodException e)
/*    */     {
/* 53 */       throw log.getUnableToFindAnnotationParameterException(this.parameterName, e);
/*    */     }
/*    */     catch (IllegalAccessException e) {
/* 56 */       throw log.getUnableToGetAnnotationParameterException(this.parameterName, this.annotation.getClass().getName(), e);
/*    */     }
/*    */     catch (InvocationTargetException e) {
/* 59 */       throw log.getUnableToGetAnnotationParameterException(this.parameterName, this.annotation.getClass().getName(), e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\privilegedactions\GetAnnotationParameter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */