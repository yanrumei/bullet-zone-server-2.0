/*    */ package org.hibernate.validator.internal.metadata.provider;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import javax.validation.ParameterNameProvider;
/*    */ import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptions;
/*    */ import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
/*    */ import org.hibernate.validator.internal.metadata.raw.BeanConfiguration;
/*    */ import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
/*    */ import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
/*    */ import org.hibernate.validator.internal.xml.XmlMappingParser;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XmlMetaDataProvider
/*    */   extends MetaDataProviderKeyedByClassName
/*    */ {
/*    */   private final AnnotationProcessingOptions annotationProcessingOptions;
/*    */   
/*    */   public XmlMetaDataProvider(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider, Set<InputStream> mappingStreams, ClassLoader externalClassLoader)
/*    */   {
/* 46 */     this(constraintHelper, createMappingParser(constraintHelper, parameterNameProvider, mappingStreams, externalClassLoader));
/*    */   }
/*    */   
/*    */   private XmlMetaDataProvider(ConstraintHelper constraintHelper, XmlMappingParser mappingParser) {
/* 50 */     super(constraintHelper, createBeanConfigurations(mappingParser));
/* 51 */     this.annotationProcessingOptions = mappingParser.getAnnotationProcessingOptions();
/*    */   }
/*    */   
/*    */   private static XmlMappingParser createMappingParser(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider, Set<InputStream> mappingStreams, ClassLoader externalClassLoader)
/*    */   {
/* 56 */     XmlMappingParser mappingParser = new XmlMappingParser(constraintHelper, parameterNameProvider, externalClassLoader);
/* 57 */     mappingParser.parse(mappingStreams);
/* 58 */     return mappingParser;
/*    */   }
/*    */   
/*    */   private static Map<String, BeanConfiguration<?>> createBeanConfigurations(XmlMappingParser mappingParser) {
/* 62 */     Map<String, BeanConfiguration<?>> configuredBeans = new HashMap();
/* 63 */     for (Class<?> clazz : mappingParser.getXmlConfiguredClasses()) {
/* 64 */       Set<ConstrainedElement> constrainedElements = mappingParser.getConstrainedElementsForClass(clazz);
/*    */       
/* 66 */       BeanConfiguration<?> beanConfiguration = createBeanConfiguration(ConfigurationSource.XML, clazz, constrainedElements, mappingParser
/*    */       
/*    */ 
/*    */ 
/* 70 */         .getDefaultSequenceForClass(clazz), null);
/*    */       
/*    */ 
/* 73 */       configuredBeans.put(clazz.getName(), beanConfiguration);
/*    */     }
/* 75 */     return configuredBeans;
/*    */   }
/*    */   
/*    */   public AnnotationProcessingOptions getAnnotationProcessingOptions()
/*    */   {
/* 80 */     return this.annotationProcessingOptions;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\provider\XmlMetaDataProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */