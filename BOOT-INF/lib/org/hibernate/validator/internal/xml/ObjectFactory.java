/*     */ package org.hibernate.validator.internal.xml;
/*     */ 
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.annotation.XmlElementDecl;
/*     */ import javax.xml.bind.annotation.XmlRegistry;
/*     */ import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*     */ import javax.xml.namespace.QName;
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
/*     */ @XmlRegistry
/*     */ public class ObjectFactory
/*     */ {
/*  36 */   private static final QName _ValidationConfig_QNAME = new QName("http://jboss.org/xml/ns/javax/validation/configuration", "validation-config");
/*  37 */   private static final QName _ConstraintMappings_QNAME = new QName("http://jboss.org/xml/ns/javax/validation/mapping", "constraint-mappings");
/*  38 */   private static final QName _ElementTypeValue_QNAME = new QName("http://jboss.org/xml/ns/javax/validation/mapping", "value");
/*  39 */   private static final QName _ElementTypeAnnotation_QNAME = new QName("http://jboss.org/xml/ns/javax/validation/mapping", "annotation");
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
/*     */   public ValidationConfigType createValidationConfigType()
/*     */   {
/*  53 */     return new ValidationConfigType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ExecutableValidationType createExecutableValidationType()
/*     */   {
/*  61 */     return new ExecutableValidationType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultValidatedExecutableTypesType createDefaultValidatedExecutableTypesType()
/*     */   {
/*  69 */     return new DefaultValidatedExecutableTypesType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertyType createPropertyType()
/*     */   {
/*  77 */     return new PropertyType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConstraintMappingsType createConstraintMappingsType()
/*     */   {
/*  85 */     return new ConstraintMappingsType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PayloadType createPayloadType()
/*     */   {
/*  93 */     return new PayloadType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public GroupsType createGroupsType()
/*     */   {
/* 101 */     return new GroupsType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public GroupSequenceType createGroupSequenceType()
/*     */   {
/* 109 */     return new GroupSequenceType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public GroupConversionType createGroupConversionType()
/*     */   {
/* 117 */     return new GroupConversionType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ValidatedByType createValidatedByType()
/*     */   {
/* 125 */     return new ValidatedByType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConstraintType createConstraintType()
/*     */   {
/* 133 */     return new ConstraintType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ElementType createElementType()
/*     */   {
/* 141 */     return new ElementType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ClassType createClassType()
/*     */   {
/* 149 */     return new ClassType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanType createBeanType()
/*     */   {
/* 157 */     return new BeanType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationType createAnnotationType()
/*     */   {
/* 165 */     return new AnnotationType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public GetterType createGetterType()
/*     */   {
/* 173 */     return new GetterType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MethodType createMethodType()
/*     */   {
/* 181 */     return new MethodType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConstructorType createConstructorType()
/*     */   {
/* 189 */     return new ConstructorType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ParameterType createParameterType()
/*     */   {
/* 197 */     return new ParameterType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ReturnValueType createReturnValueType()
/*     */   {
/* 205 */     return new ReturnValueType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CrossParameterType createCrossParameterType()
/*     */   {
/* 213 */     return new CrossParameterType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConstraintDefinitionType createConstraintDefinitionType()
/*     */   {
/* 221 */     return new ConstraintDefinitionType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FieldType createFieldType()
/*     */   {
/* 229 */     return new FieldType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @XmlElementDecl(namespace="http://jboss.org/xml/ns/javax/validation/configuration", name="validation-config")
/*     */   public JAXBElement<ValidationConfigType> createValidationConfig(ValidationConfigType value)
/*     */   {
/* 238 */     return new JAXBElement(_ValidationConfig_QNAME, ValidationConfigType.class, null, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @XmlElementDecl(namespace="http://jboss.org/xml/ns/javax/validation/mapping", name="constraint-mappings")
/*     */   public JAXBElement<ConstraintMappingsType> createConstraintMappings(ConstraintMappingsType value)
/*     */   {
/* 247 */     return new JAXBElement(_ConstraintMappings_QNAME, ConstraintMappingsType.class, null, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @XmlElementDecl(namespace="http://jboss.org/xml/ns/javax/validation/mapping", name="value", scope=ElementType.class)
/*     */   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
/*     */   public JAXBElement<String> createElementTypeValue(String value)
/*     */   {
/* 257 */     return new JAXBElement(_ElementTypeValue_QNAME, String.class, ElementType.class, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @XmlElementDecl(namespace="http://jboss.org/xml/ns/javax/validation/mapping", name="annotation", scope=ElementType.class)
/*     */   public JAXBElement<AnnotationType> createElementTypeAnnotation(AnnotationType value)
/*     */   {
/* 266 */     return new JAXBElement(_ElementTypeAnnotation_QNAME, AnnotationType.class, ElementType.class, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\ObjectFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */