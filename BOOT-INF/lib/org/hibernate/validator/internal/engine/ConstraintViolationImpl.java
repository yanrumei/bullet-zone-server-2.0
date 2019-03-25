/*     */ package org.hibernate.validator.internal.engine;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.util.Map;
/*     */ import javax.validation.ConstraintViolation;
/*     */ import javax.validation.Path;
/*     */ import javax.validation.metadata.ConstraintDescriptor;
/*     */ import org.hibernate.validator.engine.HibernateConstraintViolation;
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
/*     */ public class ConstraintViolationImpl<T>
/*     */   implements HibernateConstraintViolation<T>, Serializable
/*     */ {
/*  25 */   private static final Log log = ;
/*     */   
/*     */   private static final long serialVersionUID = -4970067626703103139L;
/*     */   
/*     */   private final String interpolatedMessage;
/*     */   
/*     */   private final T rootBean;
/*     */   
/*     */   private final Object value;
/*     */   
/*     */   private final Path propertyPath;
/*     */   
/*     */   private final Object leafBeanInstance;
/*     */   
/*     */   private final ConstraintDescriptor<?> constraintDescriptor;
/*     */   
/*     */   private final String messageTemplate;
/*     */   
/*     */   private final Map<String, Object> expressionVariables;
/*     */   
/*     */   private final Class<T> rootBeanClass;
/*     */   private final ElementType elementType;
/*     */   private final Object[] executableParameters;
/*     */   private final Object executableReturnValue;
/*     */   private final Object dynamicPayload;
/*     */   private final int hashCode;
/*     */   
/*     */   public static <T> ConstraintViolation<T> forBeanValidation(String messageTemplate, Map<String, Object> expressionVariables, String interpolatedMessage, Class<T> rootBeanClass, T rootBean, Object leafBeanInstance, Object value, Path propertyPath, ConstraintDescriptor<?> constraintDescriptor, ElementType elementType, Object dynamicPayload)
/*     */   {
/*  54 */     return new ConstraintViolationImpl(messageTemplate, expressionVariables, interpolatedMessage, rootBeanClass, rootBean, leafBeanInstance, value, propertyPath, constraintDescriptor, elementType, null, null, dynamicPayload);
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
/*     */   public static <T> ConstraintViolation<T> forParameterValidation(String messageTemplate, Map<String, Object> expressionVariables, String interpolatedMessage, Class<T> rootBeanClass, T rootBean, Object leafBeanInstance, Object value, Path propertyPath, ConstraintDescriptor<?> constraintDescriptor, ElementType elementType, Object[] executableParameters, Object dynamicPayload)
/*     */   {
/*  83 */     return new ConstraintViolationImpl(messageTemplate, expressionVariables, interpolatedMessage, rootBeanClass, rootBean, leafBeanInstance, value, propertyPath, constraintDescriptor, elementType, executableParameters, null, dynamicPayload);
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
/*     */   public static <T> ConstraintViolation<T> forReturnValueValidation(String messageTemplate, Map<String, Object> expressionVariables, String interpolatedMessage, Class<T> rootBeanClass, T rootBean, Object leafBeanInstance, Object value, Path propertyPath, ConstraintDescriptor<?> constraintDescriptor, ElementType elementType, Object executableReturnValue, Object dynamicPayload)
/*     */   {
/* 112 */     return new ConstraintViolationImpl(messageTemplate, expressionVariables, interpolatedMessage, rootBeanClass, rootBean, leafBeanInstance, value, propertyPath, constraintDescriptor, elementType, null, executableReturnValue, dynamicPayload);
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
/*     */   private ConstraintViolationImpl(String messageTemplate, Map<String, Object> expressionVariables, String interpolatedMessage, Class<T> rootBeanClass, T rootBean, Object leafBeanInstance, Object value, Path propertyPath, ConstraintDescriptor<?> constraintDescriptor, ElementType elementType, Object[] executableParameters, Object executableReturnValue, Object dynamicPayload)
/*     */   {
/* 142 */     this.messageTemplate = messageTemplate;
/* 143 */     this.expressionVariables = expressionVariables;
/* 144 */     this.interpolatedMessage = interpolatedMessage;
/* 145 */     this.rootBean = rootBean;
/* 146 */     this.value = value;
/* 147 */     this.propertyPath = propertyPath;
/* 148 */     this.leafBeanInstance = leafBeanInstance;
/* 149 */     this.constraintDescriptor = constraintDescriptor;
/* 150 */     this.rootBeanClass = rootBeanClass;
/* 151 */     this.elementType = elementType;
/* 152 */     this.executableParameters = executableParameters;
/* 153 */     this.executableReturnValue = executableReturnValue;
/* 154 */     this.dynamicPayload = dynamicPayload;
/*     */     
/* 156 */     this.hashCode = createHashCode();
/*     */   }
/*     */   
/*     */   public final String getMessage()
/*     */   {
/* 161 */     return this.interpolatedMessage;
/*     */   }
/*     */   
/*     */   public final String getMessageTemplate()
/*     */   {
/* 166 */     return this.messageTemplate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, Object> getExpressionVariables()
/*     */   {
/* 174 */     return this.expressionVariables;
/*     */   }
/*     */   
/*     */   public final T getRootBean()
/*     */   {
/* 179 */     return (T)this.rootBean;
/*     */   }
/*     */   
/*     */   public final Class<T> getRootBeanClass()
/*     */   {
/* 184 */     return this.rootBeanClass;
/*     */   }
/*     */   
/*     */   public final Object getLeafBean()
/*     */   {
/* 189 */     return this.leafBeanInstance;
/*     */   }
/*     */   
/*     */   public final Object getInvalidValue()
/*     */   {
/* 194 */     return this.value;
/*     */   }
/*     */   
/*     */   public final Path getPropertyPath()
/*     */   {
/* 199 */     return this.propertyPath;
/*     */   }
/*     */   
/*     */   public final ConstraintDescriptor<?> getConstraintDescriptor()
/*     */   {
/* 204 */     return this.constraintDescriptor;
/*     */   }
/*     */   
/*     */ 
/*     */   public <C> C unwrap(Class<C> type)
/*     */   {
/* 210 */     if (type.isAssignableFrom(ConstraintViolation.class)) {
/* 211 */       return (C)type.cast(this);
/*     */     }
/* 213 */     if (type.isAssignableFrom(HibernateConstraintViolation.class)) {
/* 214 */       return (C)type.cast(this);
/*     */     }
/* 216 */     throw log.getTypeNotSupportedForUnwrappingException(type);
/*     */   }
/*     */   
/*     */   public Object[] getExecutableParameters()
/*     */   {
/* 221 */     return this.executableParameters;
/*     */   }
/*     */   
/*     */   public Object getExecutableReturnValue()
/*     */   {
/* 226 */     return this.executableReturnValue;
/*     */   }
/*     */   
/*     */   public <C> C getDynamicPayload(Class<C> type)
/*     */   {
/* 231 */     if ((this.dynamicPayload != null) && (type.isAssignableFrom(this.dynamicPayload.getClass()))) {
/* 232 */       return (C)type.cast(this.dynamicPayload);
/*     */     }
/*     */     
/* 235 */     return null;
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
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 250 */     if (this == o) {
/* 251 */       return true;
/*     */     }
/* 253 */     if ((o == null) || (getClass() != o.getClass())) {
/* 254 */       return false;
/*     */     }
/*     */     
/* 257 */     ConstraintViolationImpl<?> that = (ConstraintViolationImpl)o;
/*     */     
/* 259 */     if (this.interpolatedMessage != null ? !this.interpolatedMessage.equals(that.interpolatedMessage) : that.interpolatedMessage != null) {
/* 260 */       return false;
/*     */     }
/* 262 */     if (this.propertyPath != null ? !this.propertyPath.equals(that.propertyPath) : that.propertyPath != null) {
/* 263 */       return false;
/*     */     }
/* 265 */     if (this.rootBean != null ? !this.rootBean.equals(that.rootBean) : that.rootBean != null) {
/* 266 */       return false;
/*     */     }
/* 268 */     if (this.leafBeanInstance != null ? !this.leafBeanInstance.equals(that.leafBeanInstance) : that.leafBeanInstance != null) {
/* 269 */       return false;
/*     */     }
/* 271 */     if (this.constraintDescriptor != null ? !this.constraintDescriptor.equals(that.constraintDescriptor) : that.constraintDescriptor != null) {
/* 272 */       return false;
/*     */     }
/* 274 */     if (this.elementType != null ? !this.elementType.equals(that.elementType) : that.elementType != null) {
/* 275 */       return false;
/*     */     }
/* 277 */     if (this.messageTemplate != null ? !this.messageTemplate.equals(that.messageTemplate) : that.messageTemplate != null) {
/* 278 */       return false;
/*     */     }
/* 280 */     if (this.rootBeanClass != null ? !this.rootBeanClass.equals(that.rootBeanClass) : that.rootBeanClass != null) {
/* 281 */       return false;
/*     */     }
/* 283 */     if (this.value != null ? !this.value.equals(that.value) : that.value != null) {
/* 284 */       return false;
/*     */     }
/*     */     
/* 287 */     return true;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 292 */     return this.hashCode;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 297 */     StringBuilder sb = new StringBuilder();
/* 298 */     sb.append("ConstraintViolationImpl");
/* 299 */     sb.append("{interpolatedMessage='").append(this.interpolatedMessage).append('\'');
/* 300 */     sb.append(", propertyPath=").append(this.propertyPath);
/* 301 */     sb.append(", rootBeanClass=").append(this.rootBeanClass);
/* 302 */     sb.append(", messageTemplate='").append(this.messageTemplate).append('\'');
/* 303 */     sb.append('}');
/* 304 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private int createHashCode()
/*     */   {
/* 311 */     int result = this.interpolatedMessage != null ? this.interpolatedMessage.hashCode() : 0;
/* 312 */     result = 31 * result + (this.propertyPath != null ? this.propertyPath.hashCode() : 0);
/* 313 */     result = 31 * result + (this.rootBean != null ? this.rootBean.hashCode() : 0);
/* 314 */     result = 31 * result + (this.leafBeanInstance != null ? this.leafBeanInstance.hashCode() : 0);
/* 315 */     result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
/* 316 */     result = 31 * result + (this.constraintDescriptor != null ? this.constraintDescriptor.hashCode() : 0);
/* 317 */     result = 31 * result + (this.messageTemplate != null ? this.messageTemplate.hashCode() : 0);
/* 318 */     result = 31 * result + (this.rootBeanClass != null ? this.rootBeanClass.hashCode() : 0);
/* 319 */     result = 31 * result + (this.elementType != null ? this.elementType.hashCode() : 0);
/* 320 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\ConstraintViolationImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */