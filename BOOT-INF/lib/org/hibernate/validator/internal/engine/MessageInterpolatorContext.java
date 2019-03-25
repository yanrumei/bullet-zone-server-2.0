/*     */ package org.hibernate.validator.internal.engine;
/*     */ 
/*     */ import java.util.Map;
/*     */ import javax.validation.metadata.ConstraintDescriptor;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.messageinterpolation.HibernateMessageInterpolatorContext;
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
/*     */ public class MessageInterpolatorContext
/*     */   implements HibernateMessageInterpolatorContext
/*     */ {
/*  25 */   private static final Log log = ;
/*     */   
/*     */   private final ConstraintDescriptor<?> constraintDescriptor;
/*     */   
/*     */   private final Object validatedValue;
/*     */   
/*     */   private final Class<?> rootBeanType;
/*     */   private final Map<String, Object> messageParameters;
/*     */   
/*     */   public MessageInterpolatorContext(ConstraintDescriptor<?> constraintDescriptor, Object validatedValue, Class<?> rootBeanType, Map<String, Object> messageParameters)
/*     */   {
/*  36 */     this.constraintDescriptor = constraintDescriptor;
/*  37 */     this.validatedValue = validatedValue;
/*  38 */     this.rootBeanType = rootBeanType;
/*  39 */     this.messageParameters = messageParameters;
/*     */   }
/*     */   
/*     */   public ConstraintDescriptor<?> getConstraintDescriptor()
/*     */   {
/*  44 */     return this.constraintDescriptor;
/*     */   }
/*     */   
/*     */   public Object getValidatedValue()
/*     */   {
/*  49 */     return this.validatedValue;
/*     */   }
/*     */   
/*     */   public Class<?> getRootBeanType()
/*     */   {
/*  54 */     return this.rootBeanType;
/*     */   }
/*     */   
/*     */   public Map<String, Object> getMessageParameters() {
/*  58 */     return this.messageParameters;
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T unwrap(Class<T> type)
/*     */   {
/*  64 */     if (type.isAssignableFrom(HibernateMessageInterpolatorContext.class)) {
/*  65 */       return (T)type.cast(this);
/*     */     }
/*  67 */     throw log.getTypeNotSupportedForUnwrappingException(type);
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/*  72 */     if (this == o) {
/*  73 */       return true;
/*     */     }
/*  75 */     if ((o == null) || (getClass() != o.getClass())) {
/*  76 */       return false;
/*     */     }
/*     */     
/*  79 */     MessageInterpolatorContext that = (MessageInterpolatorContext)o;
/*     */     
/*  81 */     if (this.constraintDescriptor != null ? !this.constraintDescriptor.equals(that.constraintDescriptor) : that.constraintDescriptor != null) {
/*  82 */       return false;
/*     */     }
/*  84 */     if (this.rootBeanType != null ? !this.rootBeanType.equals(that.rootBeanType) : that.rootBeanType != null) {
/*  85 */       return false;
/*     */     }
/*  87 */     if (this.validatedValue != null ? !this.validatedValue.equals(that.validatedValue) : that.validatedValue != null) {
/*  88 */       return false;
/*     */     }
/*     */     
/*  91 */     return true;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  96 */     int result = this.constraintDescriptor != null ? this.constraintDescriptor.hashCode() : 0;
/*  97 */     result = 31 * result + (this.validatedValue != null ? this.validatedValue.hashCode() : 0);
/*  98 */     result = 31 * result + (this.rootBeanType != null ? this.rootBeanType.hashCode() : 0);
/*  99 */     return result;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 104 */     StringBuilder sb = new StringBuilder();
/* 105 */     sb.append("MessageInterpolatorContext");
/* 106 */     sb.append("{constraintDescriptor=").append(this.constraintDescriptor);
/* 107 */     sb.append(", validatedValue=").append(this.validatedValue);
/* 108 */     sb.append('}');
/* 109 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\MessageInterpolatorContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */