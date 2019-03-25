/*     */ package org.hibernate.validator.internal.util.annotationfactory;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredMethod;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredMethods;
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
/*     */ class AnnotationProxy
/*     */   implements Annotation, InvocationHandler, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 6907601010599429454L;
/*  52 */   private static final Log log = ;
/*     */   private final Class<? extends Annotation> annotationType;
/*     */   private final Map<String, Object> values;
/*     */   private final int hashCode;
/*     */   
/*     */   AnnotationProxy(AnnotationDescriptor<?> descriptor)
/*     */   {
/*  59 */     this.annotationType = descriptor.type();
/*  60 */     this.values = Collections.unmodifiableMap(getAnnotationValues(descriptor));
/*  61 */     this.hashCode = calculateHashCode();
/*     */   }
/*     */   
/*     */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
/*     */   {
/*  66 */     if (this.values.containsKey(method.getName())) {
/*  67 */       return this.values.get(method.getName());
/*     */     }
/*  69 */     return method.invoke(this, args);
/*     */   }
/*     */   
/*     */   public Class<? extends Annotation> annotationType()
/*     */   {
/*  74 */     return this.annotationType;
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
/*     */   public boolean equals(Object obj)
/*     */   {
/*  88 */     if (this == obj) {
/*  89 */       return true;
/*     */     }
/*  91 */     if (obj == null) {
/*  92 */       return false;
/*     */     }
/*  94 */     if (!this.annotationType.isInstance(obj)) {
/*  95 */       return false;
/*     */     }
/*     */     
/*  98 */     Annotation other = (Annotation)this.annotationType.cast(obj);
/*     */     
/*     */ 
/* 101 */     for (Map.Entry<String, Object> member : this.values.entrySet())
/*     */     {
/* 103 */       Object value = member.getValue();
/* 104 */       Object otherValue = getAnnotationMemberValue(other, (String)member.getKey());
/*     */       
/* 106 */       if (!areEqual(value, otherValue)) {
/* 107 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 111 */     return true;
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
/*     */   public int hashCode()
/*     */   {
/* 124 */     return this.hashCode;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 129 */     StringBuilder result = new StringBuilder();
/* 130 */     result.append('@').append(this.annotationType.getName()).append('(');
/* 131 */     for (String s : getRegisteredMethodsInAlphabeticalOrder()) {
/* 132 */       result.append(s).append('=').append(this.values.get(s)).append(", ");
/*     */     }
/*     */     
/* 135 */     if (this.values.size() > 0) {
/* 136 */       result.delete(result.length() - 2, result.length());
/* 137 */       result.append(")");
/*     */     }
/*     */     else {
/* 140 */       result.delete(result.length() - 1, result.length());
/*     */     }
/*     */     
/* 143 */     return result.toString();
/*     */   }
/*     */   
/*     */   private Map<String, Object> getAnnotationValues(AnnotationDescriptor<?> descriptor)
/*     */   {
/* 148 */     Map<String, Object> result = CollectionHelper.newHashMap();
/* 149 */     int processedValuesFromDescriptor = 0;
/* 150 */     Method[] declaredMethods = (Method[])run(GetDeclaredMethods.action(this.annotationType));
/* 151 */     for (Method m : declaredMethods) {
/* 152 */       if (descriptor.containsElement(m.getName())) {
/* 153 */         result.put(m.getName(), descriptor.valueOf(m.getName()));
/* 154 */         processedValuesFromDescriptor++;
/*     */       }
/* 156 */       else if (m.getDefaultValue() != null) {
/* 157 */         result.put(m.getName(), m.getDefaultValue());
/*     */       }
/*     */       else {
/* 160 */         throw log.getNoValueProvidedForAnnotationParameterException(m
/* 161 */           .getName(), this.annotationType
/* 162 */           .getSimpleName());
/*     */       }
/*     */     }
/*     */     
/* 166 */     if (processedValuesFromDescriptor != descriptor.numberOfElements())
/*     */     {
/* 168 */       Object unknownParameters = descriptor.getElements().keySet();
/* 169 */       ((Set)unknownParameters).removeAll(result.keySet());
/*     */       
/* 171 */       throw log.getTryingToInstantiateAnnotationWithUnknownParametersException(this.annotationType, (Set)unknownParameters);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 176 */     return result;
/*     */   }
/*     */   
/*     */   private int calculateHashCode() {
/* 180 */     int hashCode = 0;
/*     */     
/* 182 */     for (Map.Entry<String, Object> member : this.values.entrySet()) {
/* 183 */       Object value = member.getValue();
/*     */       
/* 185 */       int nameHashCode = ((String)member.getKey()).hashCode();
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
/* 200 */       int valueHashCode = value.getClass() == short[].class ? Arrays.hashCode((short[])value) : value.getClass() == long[].class ? Arrays.hashCode((long[])value) : value.getClass() == int[].class ? Arrays.hashCode((int[])value) : value.getClass() == float[].class ? Arrays.hashCode((float[])value) : value.getClass() == double[].class ? Arrays.hashCode((double[])value) : value.getClass() == char[].class ? Arrays.hashCode((char[])value) : value.getClass() == byte[].class ? Arrays.hashCode((byte[])value) : value.getClass() == boolean[].class ? Arrays.hashCode((boolean[])value) : !value.getClass().isArray() ? value.hashCode() : Arrays.hashCode((Object[])value);
/*     */       
/* 202 */       hashCode += (127 * nameHashCode ^ valueHashCode);
/*     */     }
/*     */     
/* 205 */     return hashCode;
/*     */   }
/*     */   
/*     */   private SortedSet<String> getRegisteredMethodsInAlphabeticalOrder() {
/* 209 */     SortedSet<String> result = new TreeSet();
/* 210 */     result.addAll(this.values.keySet());
/* 211 */     return result;
/*     */   }
/*     */   
/*     */   private boolean areEqual(Object o1, Object o2) {
/* 215 */     return 
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
/* 236 */       o1.getClass() == short[].class ? Arrays.equals((short[])o1, (short[])o2) : o1.getClass() == long[].class ? Arrays.equals((long[])o1, (long[])o2) : o1.getClass() == int[].class ? Arrays.equals((int[])o1, (int[])o2) : o1.getClass() == float[].class ? Arrays.equals((float[])o1, (float[])o2) : o1.getClass() == double[].class ? Arrays.equals((double[])o1, (double[])o2) : o1.getClass() == char[].class ? Arrays.equals((char[])o1, (char[])o2) : o1.getClass() == byte[].class ? Arrays.equals((byte[])o1, (byte[])o2) : o1.getClass() == boolean[].class ? Arrays.equals((boolean[])o1, (boolean[])o2) : !o1.getClass().isArray() ? o1.equals(o2) : 
/*     */       
/*     */ 
/*     */ 
/* 240 */       Arrays.equals((Object[])o1, (Object[])o2);
/*     */   }
/*     */   
/*     */ 
/*     */   private Object getAnnotationMemberValue(Annotation annotation, String name)
/*     */   {
/*     */     try
/*     */     {
/* 248 */       return ((Method)run(GetDeclaredMethod.action(annotation.annotationType(), name, new Class[0]))).invoke(annotation, new Object[0]);
/*     */     }
/*     */     catch (IllegalAccessException e) {
/* 251 */       throw log.getUnableToRetrieveAnnotationParameterValueException(e);
/*     */     }
/*     */     catch (IllegalArgumentException e) {
/* 254 */       throw log.getUnableToRetrieveAnnotationParameterValueException(e);
/*     */     }
/*     */     catch (InvocationTargetException e) {
/* 257 */       throw log.getUnableToRetrieveAnnotationParameterValueException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private <T> T run(PrivilegedAction<T> action)
/*     */   {
/* 268 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\annotationfactory\AnnotationProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */