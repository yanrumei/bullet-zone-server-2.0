/*     */ package org.hibernate.validator.internal.xml;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javax.validation.ConstraintValidator;
/*     */ import javax.validation.ParameterNameProvider;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.validation.Schema;
/*     */ import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptions;
/*     */ import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
/*     */ import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
/*     */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedField;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedType;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.NewJaxbContext;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.SetContextClassLoader;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.Unmarshal;
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
/*     */ public class XmlMappingParser
/*     */ {
/*  57 */   private static final Log log = ;
/*     */   
/*  59 */   private final Set<Class<?>> processedClasses = CollectionHelper.newHashSet();
/*     */   
/*     */   private final ConstraintHelper constraintHelper;
/*     */   
/*     */   private final AnnotationProcessingOptionsImpl annotationProcessingOptions;
/*     */   
/*     */   private final Map<Class<?>, List<Class<?>>> defaultSequences;
/*     */   private final Map<Class<?>, Set<ConstrainedElement>> constrainedElements;
/*     */   private final XmlParserHelper xmlParserHelper;
/*     */   private final ParameterNameProvider parameterNameProvider;
/*     */   private final ClassLoadingHelper classLoadingHelper;
/*  70 */   private static final ConcurrentMap<String, String> SCHEMAS_BY_VERSION = new ConcurrentHashMap(2, 0.75F, 1);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*  77 */     SCHEMAS_BY_VERSION.put("1.0", "META-INF/validation-mapping-1.0.xsd");
/*  78 */     SCHEMAS_BY_VERSION.put("1.1", "META-INF/validation-mapping-1.1.xsd");
/*     */   }
/*     */   
/*     */   public XmlMappingParser(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider, ClassLoader externalClassLoader)
/*     */   {
/*  83 */     this.constraintHelper = constraintHelper;
/*  84 */     this.annotationProcessingOptions = new AnnotationProcessingOptionsImpl();
/*  85 */     this.defaultSequences = CollectionHelper.newHashMap();
/*  86 */     this.constrainedElements = CollectionHelper.newHashMap();
/*  87 */     this.xmlParserHelper = new XmlParserHelper();
/*  88 */     this.parameterNameProvider = parameterNameProvider;
/*  89 */     this.classLoadingHelper = new ClassLoadingHelper(externalClassLoader);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void parse(Set<InputStream> mappingStreams)
/*     */   {
/*     */     try
/*     */     {
/* 102 */       jc = (JAXBContext)run(NewJaxbContext.action(ConstraintMappingsType.class));
/*     */       
/* 104 */       MetaConstraintBuilder metaConstraintBuilder = new MetaConstraintBuilder(this.classLoadingHelper, this.constraintHelper);
/*     */       
/*     */ 
/*     */ 
/* 108 */       GroupConversionBuilder groupConversionBuilder = new GroupConversionBuilder(this.classLoadingHelper);
/*     */       
/* 110 */       constrainedTypeBuilder = new ConstrainedTypeBuilder(this.classLoadingHelper, metaConstraintBuilder, this.annotationProcessingOptions, this.defaultSequences);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 116 */       constrainedFieldBuilder = new ConstrainedFieldBuilder(metaConstraintBuilder, groupConversionBuilder, this.annotationProcessingOptions);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 121 */       constrainedExecutableBuilder = new ConstrainedExecutableBuilder(this.classLoadingHelper, this.parameterNameProvider, metaConstraintBuilder, groupConversionBuilder, this.annotationProcessingOptions);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 128 */       constrainedGetterBuilder = new ConstrainedGetterBuilder(metaConstraintBuilder, groupConversionBuilder, this.annotationProcessingOptions);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 134 */       alreadyProcessedConstraintDefinitions = CollectionHelper.newHashSet();
/* 135 */       for (InputStream in : mappingStreams)
/*     */       {
/*     */ 
/* 138 */         boolean markSupported = in.markSupported();
/* 139 */         if (markSupported) {
/* 140 */           in.mark(Integer.MAX_VALUE);
/*     */         }
/*     */         
/* 143 */         ConstraintMappingsType mapping = unmarshal(jc, in);
/* 144 */         String defaultPackage = mapping.getDefaultPackage();
/*     */         
/* 146 */         parseConstraintDefinitions(mapping
/* 147 */           .getConstraintDefinition(), defaultPackage, alreadyProcessedConstraintDefinitions);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 152 */         for (BeanType bean : mapping.getBean()) {
/* 153 */           processBeanType(constrainedTypeBuilder, constrainedFieldBuilder, constrainedExecutableBuilder, constrainedGetterBuilder, defaultPackage, bean);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 163 */         if (markSupported)
/*     */           try {
/* 165 */             in.reset();
/*     */           }
/*     */           catch (IOException e) {
/* 168 */             log.debug("Unable to reset input stream."); } } } catch (JAXBException e) { JAXBContext jc;
/*     */       ConstrainedTypeBuilder constrainedTypeBuilder;
/*     */       ConstrainedFieldBuilder constrainedFieldBuilder;
/*     */       ConstrainedExecutableBuilder constrainedExecutableBuilder;
/*     */       ConstrainedGetterBuilder constrainedGetterBuilder;
/*     */       Set<String> alreadyProcessedConstraintDefinitions;
/* 174 */       throw log.getErrorParsingMappingFileException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private ConstraintMappingsType unmarshal(JAXBContext jc, InputStream in) throws JAXBException {
/* 179 */     ClassLoader previousTccl = (ClassLoader)run(GetClassLoader.fromContext());
/*     */     try
/*     */     {
/* 182 */       run(SetContextClassLoader.action(XmlMappingParser.class.getClassLoader()));
/*     */       
/* 184 */       XMLEventReader xmlEventReader = this.xmlParserHelper.createXmlEventReader("constraint mapping file", new CloseIgnoringInputStream(in));
/* 185 */       String schemaVersion = this.xmlParserHelper.getSchemaVersion("constraint mapping file", xmlEventReader);
/* 186 */       String schemaResourceName = getSchemaResourceName(schemaVersion);
/* 187 */       Schema schema = this.xmlParserHelper.getSchema(schemaResourceName);
/*     */       
/* 189 */       Unmarshaller unmarshaller = jc.createUnmarshaller();
/* 190 */       unmarshaller.setSchema(schema);
/*     */       
/* 192 */       return getValidationConfig(xmlEventReader, unmarshaller);
/*     */     }
/*     */     finally {
/* 195 */       run(SetContextClassLoader.action(previousTccl));
/*     */     }
/*     */   }
/*     */   
/*     */   public final Set<Class<?>> getXmlConfiguredClasses() {
/* 200 */     return this.processedClasses;
/*     */   }
/*     */   
/*     */   public final AnnotationProcessingOptions getAnnotationProcessingOptions() {
/* 204 */     return this.annotationProcessingOptions;
/*     */   }
/*     */   
/*     */   public final Set<ConstrainedElement> getConstrainedElementsForClass(Class<?> beanClass) {
/* 208 */     if (this.constrainedElements.containsKey(beanClass)) {
/* 209 */       return (Set)this.constrainedElements.get(beanClass);
/*     */     }
/*     */     
/* 212 */     return Collections.emptySet();
/*     */   }
/*     */   
/*     */   public final List<Class<?>> getDefaultSequenceForClass(Class<?> beanClass)
/*     */   {
/* 217 */     return (List)this.defaultSequences.get(beanClass);
/*     */   }
/*     */   
/*     */   private void processBeanType(ConstrainedTypeBuilder constrainedTypeBuilder, ConstrainedFieldBuilder constrainedFieldBuilder, ConstrainedExecutableBuilder constrainedExecutableBuilder, ConstrainedGetterBuilder constrainedGetterBuilder, String defaultPackage, BeanType bean) {
/* 221 */     Class<?> beanClass = this.classLoadingHelper.loadClass(bean.getClazz(), defaultPackage);
/* 222 */     checkClassHasNotBeenProcessed(this.processedClasses, beanClass);
/*     */     
/*     */ 
/* 225 */     this.annotationProcessingOptions.ignoreAnnotationConstraintForClass(beanClass, bean
/*     */     
/* 227 */       .getIgnoreAnnotations());
/*     */     
/*     */ 
/* 230 */     ConstrainedType constrainedType = constrainedTypeBuilder.buildConstrainedType(bean
/* 231 */       .getClassType(), beanClass, defaultPackage);
/*     */     
/*     */ 
/*     */ 
/* 235 */     if (constrainedType != null) {
/* 236 */       addConstrainedElement(beanClass, constrainedType);
/*     */     }
/*     */     
/* 239 */     Set<ConstrainedField> constrainedFields = constrainedFieldBuilder.buildConstrainedFields(bean
/* 240 */       .getField(), beanClass, defaultPackage);
/*     */     
/*     */ 
/*     */ 
/* 244 */     addConstrainedElements(beanClass, constrainedFields);
/*     */     
/* 246 */     Set<ConstrainedExecutable> constrainedGetters = constrainedGetterBuilder.buildConstrainedGetters(bean
/* 247 */       .getGetter(), beanClass, defaultPackage);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 252 */     addConstrainedElements(beanClass, constrainedGetters);
/*     */     
/* 254 */     Set<ConstrainedExecutable> constrainedConstructors = constrainedExecutableBuilder.buildConstructorConstrainedExecutable(bean
/* 255 */       .getConstructor(), beanClass, defaultPackage);
/*     */     
/*     */ 
/*     */ 
/* 259 */     addConstrainedElements(beanClass, constrainedConstructors);
/*     */     
/* 261 */     Set<ConstrainedExecutable> constrainedMethods = constrainedExecutableBuilder.buildMethodConstrainedExecutable(bean
/* 262 */       .getMethod(), beanClass, defaultPackage);
/*     */     
/*     */ 
/*     */ 
/* 266 */     addConstrainedElements(beanClass, constrainedMethods);
/*     */     
/* 268 */     this.processedClasses.add(beanClass);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void parseConstraintDefinitions(List<ConstraintDefinitionType> constraintDefinitionList, String defaultPackage, Set<String> alreadyProcessedConstraintDefinitions)
/*     */   {
/* 275 */     for (ConstraintDefinitionType constraintDefinition : constraintDefinitionList) {
/* 276 */       String annotationClassName = constraintDefinition.getAnnotation();
/* 277 */       if (alreadyProcessedConstraintDefinitions.contains(annotationClassName)) {
/* 278 */         throw log.getOverridingConstraintDefinitionsInMultipleMappingFilesException(annotationClassName);
/*     */       }
/*     */       
/* 281 */       alreadyProcessedConstraintDefinitions.add(annotationClassName);
/*     */       
/*     */ 
/* 284 */       Class<?> clazz = this.classLoadingHelper.loadClass(annotationClassName, defaultPackage);
/* 285 */       if (!clazz.isAnnotation()) {
/* 286 */         throw log.getIsNotAnAnnotationException(annotationClassName);
/*     */       }
/* 288 */       Class<? extends Annotation> annotationClass = clazz;
/*     */       
/* 290 */       addValidatorDefinitions(annotationClass, defaultPackage, constraintDefinition.getValidatedBy());
/*     */     }
/*     */   }
/*     */   
/*     */   private <A extends Annotation> void addValidatorDefinitions(Class<A> annotationClass, String defaultPackage, ValidatedByType validatedByType)
/*     */   {
/* 296 */     List<Class<? extends ConstraintValidator<A, ?>>> constraintValidatorClasses = CollectionHelper.newArrayList();
/*     */     
/* 298 */     for (String validatorClassName : validatedByType.getValue())
/*     */     {
/*     */ 
/* 301 */       Class<? extends ConstraintValidator<A, ?>> validatorClass = this.classLoadingHelper.loadClass(validatorClassName, defaultPackage);
/*     */       
/* 303 */       if (!ConstraintValidator.class.isAssignableFrom(validatorClass)) {
/* 304 */         throw log.getIsNotAConstraintValidatorClassException(validatorClass);
/*     */       }
/*     */       
/* 307 */       constraintValidatorClasses.add(validatorClass);
/*     */     }
/* 309 */     this.constraintHelper.putValidatorClasses(annotationClass, constraintValidatorClasses, Boolean.TRUE
/*     */     
/*     */ 
/* 312 */       .equals(validatedByType.getIncludeExistingValidators()));
/*     */   }
/*     */   
/*     */   private void checkClassHasNotBeenProcessed(Set<Class<?>> processedClasses, Class<?> beanClass)
/*     */   {
/* 317 */     if (processedClasses.contains(beanClass)) {
/* 318 */       throw log.getBeanClassHasAlreadyBeConfiguredInXmlException(beanClass.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   private void addConstrainedElement(Class<?> beanClass, ConstrainedElement constrainedElement) {
/* 323 */     if (this.constrainedElements.containsKey(beanClass)) {
/* 324 */       ((Set)this.constrainedElements.get(beanClass)).add(constrainedElement);
/*     */     }
/*     */     else {
/* 327 */       Set<ConstrainedElement> tmpList = CollectionHelper.newHashSet();
/* 328 */       tmpList.add(constrainedElement);
/* 329 */       this.constrainedElements.put(beanClass, tmpList);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addConstrainedElements(Class<?> beanClass, Set<? extends ConstrainedElement> newConstrainedElements) { Set<ConstrainedElement> existingConstrainedElements;
/* 334 */     if (this.constrainedElements.containsKey(beanClass)) {
/* 335 */       existingConstrainedElements = (Set)this.constrainedElements.get(beanClass);
/* 336 */       for (ConstrainedElement constrainedElement : newConstrainedElements) {
/* 337 */         for (ConstrainedElement existingConstrainedElement : existingConstrainedElements) {
/* 338 */           if ((existingConstrainedElement.getLocation().getMember() != null) && 
/* 339 */             (existingConstrainedElement.getLocation().getMember().equals(constrainedElement
/* 340 */             .getLocation().getMember())))
/*     */           {
/* 342 */             ConstraintLocation location = constrainedElement.getLocation();
/* 343 */             throw log.getConstrainedElementConfiguredMultipleTimesException(location
/* 344 */               .getMember().toString());
/*     */           }
/*     */         }
/*     */         
/* 348 */         existingConstrainedElements.add(constrainedElement);
/*     */       }
/*     */     }
/*     */     else {
/* 352 */       Set<ConstrainedElement> tmpSet = CollectionHelper.newHashSet();
/* 353 */       tmpSet.addAll(newConstrainedElements);
/* 354 */       this.constrainedElements.put(beanClass, tmpSet);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private ConstraintMappingsType getValidationConfig(XMLEventReader xmlEventReader, Unmarshaller unmarshaller)
/*     */   {
/*     */     try
/*     */     {
/* 363 */       JAXBElement<ConstraintMappingsType> root = (JAXBElement)run(
/* 364 */         Unmarshal.action(unmarshaller, xmlEventReader, ConstraintMappingsType.class));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 370 */       constraintMappings = (ConstraintMappingsType)root.getValue();
/*     */     } catch (Exception e) {
/*     */       ConstraintMappingsType constraintMappings;
/* 373 */       throw log.getErrorParsingMappingFileException(e); }
/*     */     ConstraintMappingsType constraintMappings;
/* 375 */     return constraintMappings;
/*     */   }
/*     */   
/*     */   private String getSchemaResourceName(String schemaVersion) {
/* 379 */     String schemaResource = (String)SCHEMAS_BY_VERSION.get(schemaVersion);
/*     */     
/* 381 */     if (schemaResource == null) {
/* 382 */       throw log.getUnsupportedSchemaVersionException("constraint mapping file", schemaVersion);
/*     */     }
/*     */     
/* 385 */     return schemaResource;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static <T> T run(PrivilegedAction<T> action)
/*     */   {
/* 395 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private <T> T run(PrivilegedExceptionAction<T> action)
/*     */     throws JAXBException
/*     */   {
/*     */     try
/*     */     {
/* 406 */       return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */     }
/*     */     catch (JAXBException e) {
/* 409 */       throw e;
/*     */     }
/*     */     catch (Exception e) {
/* 412 */       throw log.getErrorParsingMappingFileException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CloseIgnoringInputStream
/*     */     extends FilterInputStream
/*     */   {
/*     */     public CloseIgnoringInputStream(InputStream in)
/*     */     {
/* 421 */       super();
/*     */     }
/*     */     
/*     */     public void close() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\XmlMappingParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */