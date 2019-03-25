/*     */ package org.hibernate.validator.internal.xml;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.validation.Payload;
/*     */ import javax.validation.ValidationException;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
/*     */ import org.hibernate.validator.internal.metadata.core.MetaConstraint;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl.ConstraintType;
/*     */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.annotationfactory.AnnotationDescriptor;
/*     */ import org.hibernate.validator.internal.util.annotationfactory.AnnotationFactory;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetMethod;
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
/*     */ class MetaConstraintBuilder
/*     */ {
/*  41 */   private static final Log log = ;
/*     */   
/*  43 */   private static final Pattern IS_ONLY_WHITESPACE = Pattern.compile("\\s*");
/*     */   
/*     */   private static final String MESSAGE_PARAM = "message";
/*     */   private static final String GROUPS_PARAM = "groups";
/*     */   private static final String PAYLOAD_PARAM = "payload";
/*     */   private final ClassLoadingHelper classLoadingHelper;
/*     */   private final ConstraintHelper constraintHelper;
/*     */   
/*     */   MetaConstraintBuilder(ClassLoadingHelper classLoadingHelper, ConstraintHelper constraintHelper)
/*     */   {
/*  53 */     this.classLoadingHelper = classLoadingHelper;
/*  54 */     this.constraintHelper = constraintHelper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   <A extends Annotation> MetaConstraint<A> buildMetaConstraint(ConstraintLocation constraintLocation, ConstraintType constraint, java.lang.annotation.ElementType type, String defaultPackage, ConstraintDescriptorImpl.ConstraintType constraintType)
/*     */   {
/*     */     try
/*     */     {
/*  65 */       annotationClass = this.classLoadingHelper.loadClass(constraint.getAnnotation(), defaultPackage);
/*     */     } catch (ValidationException e) {
/*     */       Class<A> annotationClass;
/*  68 */       throw log.getUnableToLoadConstraintAnnotationClassException(constraint.getAnnotation(), e); }
/*     */     Class<A> annotationClass;
/*  70 */     AnnotationDescriptor<A> annotationDescriptor = new AnnotationDescriptor(annotationClass);
/*     */     
/*  72 */     if (constraint.getMessage() != null) {
/*  73 */       annotationDescriptor.setValue("message", constraint.getMessage());
/*     */     }
/*  75 */     annotationDescriptor.setValue("groups", getGroups(constraint.getGroups(), defaultPackage));
/*  76 */     annotationDescriptor.setValue("payload", getPayload(constraint.getPayload(), defaultPackage));
/*     */     
/*  78 */     for (ElementType elementType : constraint.getElement()) {
/*  79 */       String name = elementType.getName();
/*  80 */       checkNameIsValid(name);
/*  81 */       Class<?> returnType = getAnnotationParameterType(annotationClass, name);
/*  82 */       Object elementValue = getElementValue(elementType, returnType, defaultPackage);
/*  83 */       annotationDescriptor.setValue(name, elementValue);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  88 */       annotation = AnnotationFactory.create(annotationDescriptor);
/*     */     } catch (RuntimeException e) {
/*     */       Object annotation;
/*  91 */       throw log.getUnableToCreateAnnotationForConfiguredConstraintException(e);
/*     */     }
/*     */     
/*     */ 
/*     */     A annotation;
/*     */     
/*  97 */     ConstraintDescriptorImpl<A> constraintDescriptor = new ConstraintDescriptorImpl(this.constraintHelper, constraintLocation.getMember(), annotation, type, constraintType);
/*     */     
/*     */ 
/* 100 */     return new MetaConstraint(constraintDescriptor, constraintLocation);
/*     */   }
/*     */   
/*     */   private <A extends Annotation> Annotation buildAnnotation(AnnotationType annotationType, Class<A> returnType, String defaultPackage) {
/* 104 */     AnnotationDescriptor<A> annotationDescriptor = new AnnotationDescriptor(returnType);
/* 105 */     for (ElementType elementType : annotationType.getElement()) {
/* 106 */       String name = elementType.getName();
/* 107 */       Class<?> parameterType = getAnnotationParameterType(returnType, name);
/* 108 */       Object elementValue = getElementValue(elementType, parameterType, defaultPackage);
/* 109 */       annotationDescriptor.setValue(name, elementValue);
/*     */     }
/* 111 */     return AnnotationFactory.create(annotationDescriptor);
/*     */   }
/*     */   
/*     */   private static void checkNameIsValid(String name) {
/* 115 */     if (("message".equals(name)) || ("groups".equals(name))) {
/* 116 */       throw log.getReservedParameterNamesException("message", "groups", "payload");
/*     */     }
/*     */   }
/*     */   
/*     */   private static <A extends Annotation> Class<?> getAnnotationParameterType(Class<A> annotationClass, String name) {
/* 121 */     Method m = (Method)run(GetMethod.action(annotationClass, name));
/* 122 */     if (m == null) {
/* 123 */       throw log.getAnnotationDoesNotContainAParameterException(annotationClass.getName(), name);
/*     */     }
/* 125 */     return m.getReturnType();
/*     */   }
/*     */   
/*     */   private Object getElementValue(ElementType elementType, Class<?> returnType, String defaultPackage) {
/* 129 */     removeEmptyContentElements(elementType);
/*     */     
/* 131 */     boolean isArray = returnType.isArray();
/* 132 */     if (!isArray) {
/* 133 */       if (elementType.getContent().size() == 0) {
/* 134 */         if (returnType == String.class) {
/* 135 */           return "";
/*     */         }
/*     */         
/* 138 */         throw log.getEmptyElementOnlySupportedWhenCharSequenceIsExpectedExpection();
/*     */       }
/*     */       
/* 141 */       if (elementType.getContent().size() > 1) {
/* 142 */         throw log.getAttemptToSpecifyAnArrayWhereSingleValueIsExpectedException();
/*     */       }
/* 144 */       return getSingleValue((Serializable)elementType.getContent().get(0), returnType, defaultPackage);
/*     */     }
/*     */     
/* 147 */     List<Object> values = CollectionHelper.newArrayList();
/* 148 */     for (Serializable s : elementType.getContent()) {
/* 149 */       values.add(getSingleValue(s, returnType.getComponentType(), defaultPackage));
/*     */     }
/* 151 */     return values.toArray((Object[])Array.newInstance(returnType.getComponentType(), values.size()));
/*     */   }
/*     */   
/*     */   private static void removeEmptyContentElements(ElementType elementType)
/*     */   {
/* 156 */     for (Iterator<Serializable> contentIterator = elementType.getContent().iterator(); contentIterator.hasNext();) {
/* 157 */       Serializable content = (Serializable)contentIterator.next();
/* 158 */       if (((content instanceof String)) && (IS_ONLY_WHITESPACE.matcher((String)content).matches())) {
/* 159 */         contentIterator.remove();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private Object getSingleValue(Serializable serializable, Class<?> returnType, String defaultPackage)
/*     */   {
/*     */     Object returnValue;
/* 167 */     if ((serializable instanceof String)) {
/* 168 */       String value = (String)serializable;
/* 169 */       returnValue = convertStringToReturnType(returnType, value, defaultPackage);
/*     */     } else { Object returnValue;
/* 171 */       if (((serializable instanceof JAXBElement)) && 
/* 172 */         (((JAXBElement)serializable).getDeclaredType().equals(String.class))) {
/* 173 */         JAXBElement<?> elem = (JAXBElement)serializable;
/* 174 */         String value = (String)elem.getValue();
/* 175 */         returnValue = convertStringToReturnType(returnType, value, defaultPackage);
/*     */       } else { Object returnValue;
/* 177 */         if (((serializable instanceof JAXBElement)) && 
/* 178 */           (((JAXBElement)serializable).getDeclaredType().equals(AnnotationType.class))) {
/* 179 */           JAXBElement<?> elem = (JAXBElement)serializable;
/* 180 */           AnnotationType annotationType = (AnnotationType)elem.getValue();
/*     */           try
/*     */           {
/* 183 */             Class<Annotation> annotationClass = returnType;
/* 184 */             returnValue = buildAnnotation(annotationType, annotationClass, defaultPackage);
/*     */           } catch (ClassCastException e) {
/*     */             Object returnValue;
/* 187 */             throw log.getUnexpectedParameterValueException(e);
/*     */           }
/*     */         }
/*     */         else {
/* 191 */           throw log.getUnexpectedParameterValueException(); } } }
/*     */     Object returnValue;
/* 193 */     return returnValue;
/*     */   }
/*     */   
/*     */ 
/*     */   private Object convertStringToReturnType(Class<?> returnType, String value, String defaultPackage)
/*     */   {
/* 199 */     if (returnType == Byte.TYPE) {
/*     */       try {
/* 201 */         returnValue = Byte.valueOf(Byte.parseByte(value));
/*     */       } catch (NumberFormatException e) {
/*     */         Object returnValue;
/* 204 */         throw log.getInvalidNumberFormatException("byte", e);
/*     */       }
/*     */     }
/* 207 */     else if (returnType == Short.TYPE) {
/*     */       try {
/* 209 */         returnValue = Short.valueOf(Short.parseShort(value));
/*     */       } catch (NumberFormatException e) {
/*     */         Object returnValue;
/* 212 */         throw log.getInvalidNumberFormatException("short", e);
/*     */       }
/*     */     }
/* 215 */     else if (returnType == Integer.TYPE) {
/*     */       try {
/* 217 */         returnValue = Integer.valueOf(Integer.parseInt(value));
/*     */       } catch (NumberFormatException e) {
/*     */         Object returnValue;
/* 220 */         throw log.getInvalidNumberFormatException("int", e);
/*     */       }
/*     */     }
/* 223 */     else if (returnType == Long.TYPE) {
/*     */       try {
/* 225 */         returnValue = Long.valueOf(Long.parseLong(value));
/*     */       } catch (NumberFormatException e) {
/*     */         Object returnValue;
/* 228 */         throw log.getInvalidNumberFormatException("long", e);
/*     */       }
/*     */     }
/* 231 */     else if (returnType == Float.TYPE) {
/*     */       try {
/* 233 */         returnValue = Float.valueOf(Float.parseFloat(value));
/*     */       } catch (NumberFormatException e) {
/*     */         Object returnValue;
/* 236 */         throw log.getInvalidNumberFormatException("float", e);
/*     */       }
/*     */     }
/* 239 */     else if (returnType == Double.TYPE) {
/*     */       try {
/* 241 */         returnValue = Double.valueOf(Double.parseDouble(value));
/*     */       } catch (NumberFormatException e) {
/*     */         Object returnValue;
/* 244 */         throw log.getInvalidNumberFormatException("double", e);
/*     */       }
/*     */     } else { Object returnValue;
/* 247 */       if (returnType == Boolean.TYPE) {
/* 248 */         returnValue = Boolean.valueOf(Boolean.parseBoolean(value));
/*     */       } else { Object returnValue;
/* 250 */         if (returnType == Character.TYPE) {
/* 251 */           if (value.length() != 1) {
/* 252 */             throw log.getInvalidCharValueException(value);
/*     */           }
/* 254 */           returnValue = Character.valueOf(value.charAt(0));
/*     */         } else { Object returnValue;
/* 256 */           if (returnType == String.class) {
/* 257 */             returnValue = value;
/*     */           } else { Object returnValue;
/* 259 */             if (returnType == Class.class) {
/* 260 */               returnValue = this.classLoadingHelper.loadClass(value, defaultPackage);
/*     */             }
/*     */             else
/*     */               try
/*     */               {
/* 265 */                 Class<Enum> enumClass = returnType;
/* 266 */                 returnValue = Enum.valueOf(enumClass, value);
/*     */               } catch (ClassCastException e) {
/*     */                 Object returnValue;
/* 269 */                 throw log.getInvalidReturnTypeException(returnType, e);
/*     */               } } } } }
/*     */     Object returnValue;
/* 272 */     return returnValue;
/*     */   }
/*     */   
/*     */   private Class<?>[] getGroups(GroupsType groupsType, String defaultPackage) {
/* 276 */     if (groupsType == null) {
/* 277 */       return new Class[0];
/*     */     }
/*     */     
/* 280 */     List<Class<?>> groupList = CollectionHelper.newArrayList();
/* 281 */     for (String groupClass : groupsType.getValue()) {
/* 282 */       groupList.add(this.classLoadingHelper.loadClass(groupClass, defaultPackage));
/*     */     }
/* 284 */     return (Class[])groupList.toArray(new Class[groupList.size()]);
/*     */   }
/*     */   
/*     */   private Class<? extends Payload>[] getPayload(PayloadType payloadType, String defaultPackage)
/*     */   {
/* 289 */     if (payloadType == null) {
/* 290 */       return new Class[0];
/*     */     }
/*     */     
/* 293 */     List<Class<? extends Payload>> payloadList = CollectionHelper.newArrayList();
/* 294 */     for (String groupClass : payloadType.getValue()) {
/* 295 */       Class<?> payload = this.classLoadingHelper.loadClass(groupClass, defaultPackage);
/* 296 */       if (!Payload.class.isAssignableFrom(payload)) {
/* 297 */         throw log.getWrongPayloadClassException(payload.getName());
/*     */       }
/*     */       
/* 300 */       payloadList.add(payload);
/*     */     }
/*     */     
/* 303 */     return (Class[])payloadList.toArray(new Class[payloadList.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static <T> T run(PrivilegedAction<T> action)
/*     */   {
/* 313 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\MetaConstraintBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */