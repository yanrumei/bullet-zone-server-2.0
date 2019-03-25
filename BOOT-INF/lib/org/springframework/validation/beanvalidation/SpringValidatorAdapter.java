/*     */ package org.springframework.validation.beanvalidation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import javax.validation.ConstraintViolation;
/*     */ import javax.validation.ValidationException;
/*     */ import javax.validation.Validator;
/*     */ import javax.validation.metadata.BeanDescriptor;
/*     */ import javax.validation.metadata.ConstraintDescriptor;
/*     */ import org.springframework.beans.NotReadablePropertyException;
/*     */ import org.springframework.context.MessageSourceResolvable;
/*     */ import org.springframework.context.support.DefaultMessageSourceResolvable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.validation.Errors;
/*     */ import org.springframework.validation.FieldError;
/*     */ import org.springframework.validation.ObjectError;
/*     */ import org.springframework.validation.SmartValidator;
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
/*     */ public class SpringValidatorAdapter
/*     */   implements SmartValidator, Validator
/*     */ {
/*  62 */   private static final Set<String> internalAnnotationAttributes = new HashSet(3);
/*     */   private Validator targetValidator;
/*     */   
/*  65 */   static { internalAnnotationAttributes.add("message");
/*  66 */     internalAnnotationAttributes.add("groups");
/*  67 */     internalAnnotationAttributes.add("payload");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringValidatorAdapter(Validator targetValidator)
/*     */   {
/*  78 */     Assert.notNull(targetValidator, "Target Validator must not be null");
/*  79 */     this.targetValidator = targetValidator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void setTargetValidator(Validator targetValidator)
/*     */   {
/*  86 */     this.targetValidator = targetValidator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean supports(Class<?> clazz)
/*     */   {
/*  96 */     return this.targetValidator != null;
/*     */   }
/*     */   
/*     */   public void validate(Object target, Errors errors)
/*     */   {
/* 101 */     if (this.targetValidator != null) {
/* 102 */       processConstraintViolations(this.targetValidator.validate(target, new Class[0]), errors);
/*     */     }
/*     */   }
/*     */   
/*     */   public void validate(Object target, Errors errors, Object... validationHints)
/*     */   {
/* 108 */     if (this.targetValidator != null) {
/* 109 */       Set<Class<?>> groups = new LinkedHashSet();
/* 110 */       if (validationHints != null) {
/* 111 */         for (Object hint : validationHints) {
/* 112 */           if ((hint instanceof Class)) {
/* 113 */             groups.add((Class)hint);
/*     */           }
/*     */         }
/*     */       }
/* 117 */       processConstraintViolations(this.targetValidator
/* 118 */         .validate(target, (Class[])groups.toArray(new Class[groups.size()])), errors);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void processConstraintViolations(Set<ConstraintViolation<Object>> violations, Errors errors)
/*     */   {
/* 129 */     for (ConstraintViolation<Object> violation : violations) {
/* 130 */       String field = determineField(violation);
/* 131 */       FieldError fieldError = errors.getFieldError(field);
/* 132 */       if ((fieldError == null) || (!fieldError.isBindingFailure())) {
/*     */         try {
/* 134 */           ConstraintDescriptor<?> cd = violation.getConstraintDescriptor();
/* 135 */           String errorCode = determineErrorCode(cd);
/* 136 */           Object[] errorArgs = getArgumentsForConstraint(errors.getObjectName(), field, cd);
/* 137 */           if ((errors instanceof BindingResult))
/*     */           {
/*     */ 
/* 140 */             BindingResult bindingResult = (BindingResult)errors;
/* 141 */             String nestedField = bindingResult.getNestedPath() + field;
/* 142 */             if ("".equals(nestedField)) {
/* 143 */               String[] errorCodes = bindingResult.resolveMessageCodes(errorCode);
/* 144 */               bindingResult.addError(new ObjectError(errors
/* 145 */                 .getObjectName(), errorCodes, errorArgs, violation.getMessage()));
/*     */             }
/*     */             else {
/* 148 */               Object rejectedValue = getRejectedValue(field, violation, bindingResult);
/* 149 */               String[] errorCodes = bindingResult.resolveMessageCodes(errorCode, field);
/* 150 */               bindingResult.addError(new FieldError(errors
/* 151 */                 .getObjectName(), nestedField, rejectedValue, false, errorCodes, errorArgs, violation
/* 152 */                 .getMessage()));
/*     */             }
/*     */             
/*     */           }
/*     */           else
/*     */           {
/* 158 */             errors.rejectValue(field, errorCode, errorArgs, violation.getMessage());
/*     */           }
/*     */         }
/*     */         catch (NotReadablePropertyException ex) {
/* 162 */           throw new IllegalStateException("JSR-303 validated property '" + field + "' does not have a corresponding accessor for Spring data binding - check your DataBinder's configuration (bean property versus direct field access)", ex);
/*     */         }
/*     */       }
/*     */     }
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
/*     */   protected String determineField(ConstraintViolation<Object> violation)
/*     */   {
/* 180 */     String path = violation.getPropertyPath().toString();
/* 181 */     int elementIndex = path.indexOf(".<");
/* 182 */     return elementIndex >= 0 ? path.substring(0, elementIndex) : path;
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
/*     */   protected String determineErrorCode(ConstraintDescriptor<?> descriptor)
/*     */   {
/* 198 */     return descriptor.getAnnotation().annotationType().getSimpleName();
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
/*     */   protected Object[] getArgumentsForConstraint(String objectName, String field, ConstraintDescriptor<?> descriptor)
/*     */   {
/* 218 */     List<Object> arguments = new LinkedList();
/* 219 */     arguments.add(getResolvableField(objectName, field));
/*     */     
/* 221 */     Map<String, Object> attributesToExpose = new TreeMap();
/* 222 */     for (Map.Entry<String, Object> entry : descriptor.getAttributes().entrySet()) {
/* 223 */       String attributeName = (String)entry.getKey();
/* 224 */       Object attributeValue = entry.getValue();
/* 225 */       if (!internalAnnotationAttributes.contains(attributeName)) {
/* 226 */         if ((attributeValue instanceof String)) {
/* 227 */           attributeValue = new ResolvableAttribute(attributeValue.toString());
/*     */         }
/* 229 */         attributesToExpose.put(attributeName, attributeValue);
/*     */       }
/*     */     }
/* 232 */     arguments.addAll(attributesToExpose.values());
/* 233 */     return arguments.toArray(new Object[arguments.size()]);
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
/*     */   protected MessageSourceResolvable getResolvableField(String objectName, String field)
/*     */   {
/* 248 */     String[] codes = { objectName + "." + field, field };
/* 249 */     return new DefaultMessageSourceResolvable(codes, field);
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
/*     */   protected Object getRejectedValue(String field, ConstraintViolation<Object> violation, BindingResult bindingResult)
/*     */   {
/* 265 */     Object invalidValue = violation.getInvalidValue();
/* 266 */     if ((!"".equals(field)) && (!field.contains("[]")) && (
/* 267 */       (invalidValue == violation.getLeafBean()) || (field.contains("[")) || (field.contains("."))))
/*     */     {
/*     */ 
/* 270 */       invalidValue = bindingResult.getRawFieldValue(field);
/*     */     }
/* 272 */     return invalidValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups)
/*     */   {
/* 282 */     Assert.state(this.targetValidator != null, "No target Validator set");
/* 283 */     return this.targetValidator.validate(object, groups);
/*     */   }
/*     */   
/*     */   public <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName, Class<?>... groups)
/*     */   {
/* 288 */     Assert.state(this.targetValidator != null, "No target Validator set");
/* 289 */     return this.targetValidator.validateProperty(object, propertyName, groups);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups)
/*     */   {
/* 296 */     Assert.state(this.targetValidator != null, "No target Validator set");
/* 297 */     return this.targetValidator.validateValue(beanType, propertyName, value, groups);
/*     */   }
/*     */   
/*     */   public BeanDescriptor getConstraintsForClass(Class<?> clazz)
/*     */   {
/* 302 */     Assert.state(this.targetValidator != null, "No target Validator set");
/* 303 */     return this.targetValidator.getConstraintsForClass(clazz);
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T unwrap(Class<T> type)
/*     */   {
/* 309 */     Assert.state(this.targetValidator != null, "No target Validator set");
/*     */     try {
/* 311 */       return (T)(type != null ? this.targetValidator.unwrap(type) : this.targetValidator);
/*     */     }
/*     */     catch (ValidationException ex)
/*     */     {
/* 315 */       if (Validator.class == type) {
/* 316 */         return this.targetValidator;
/*     */       }
/* 318 */       throw ex;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   SpringValidatorAdapter() {}
/*     */   
/*     */ 
/*     */   private static class ResolvableAttribute
/*     */     implements MessageSourceResolvable, Serializable
/*     */   {
/*     */     private final String resolvableString;
/*     */     
/*     */     public ResolvableAttribute(String resolvableString)
/*     */     {
/* 333 */       this.resolvableString = resolvableString;
/*     */     }
/*     */     
/*     */     public String[] getCodes()
/*     */     {
/* 338 */       return new String[] { this.resolvableString };
/*     */     }
/*     */     
/*     */     public Object[] getArguments()
/*     */     {
/* 343 */       return null;
/*     */     }
/*     */     
/*     */     public String getDefaultMessage()
/*     */     {
/* 348 */       return this.resolvableString;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\validation\beanvalidation\SpringValidatorAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */