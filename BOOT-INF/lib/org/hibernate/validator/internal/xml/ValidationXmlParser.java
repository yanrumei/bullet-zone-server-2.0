/*     */ package org.hibernate.validator.internal.xml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javax.validation.BootstrapConfiguration;
/*     */ import javax.validation.executable.ExecutableType;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.validation.Schema;
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
/*     */ public class ValidationXmlParser
/*     */ {
/*  44 */   private static final Log log = ;
/*     */   
/*     */   private static final String VALIDATION_XML_FILE = "META-INF/validation.xml";
/*  47 */   private static final ConcurrentMap<String, String> SCHEMAS_BY_VERSION = new ConcurrentHashMap(2, 0.75F, 1);
/*     */   
/*     */ 
/*     */   private final ClassLoader externalClassLoader;
/*     */   
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*  56 */     SCHEMAS_BY_VERSION.put("1.0", "META-INF/validation-configuration-1.0.xsd");
/*  57 */     SCHEMAS_BY_VERSION.put("1.1", "META-INF/validation-configuration-1.1.xsd");
/*     */   }
/*     */   
/*     */   public ValidationXmlParser(ClassLoader externalClassLoader) {
/*  61 */     this.externalClassLoader = externalClassLoader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final BootstrapConfiguration parseValidationXml()
/*     */   {
/*  70 */     InputStream inputStream = getValidationXmlInputStream();
/*  71 */     if (inputStream == null) {
/*  72 */       return BootstrapConfigurationImpl.getDefaultBootstrapConfiguration();
/*     */     }
/*     */     
/*  75 */     ClassLoader previousTccl = (ClassLoader)run(GetClassLoader.fromContext());
/*     */     try
/*     */     {
/*  78 */       run(SetContextClassLoader.action(ValidationXmlParser.class.getClassLoader()));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  83 */       XmlParserHelper xmlParserHelper = new XmlParserHelper();
/*  84 */       XMLEventReader xmlEventReader = xmlParserHelper.createXmlEventReader("META-INF/validation.xml", inputStream);
/*     */       
/*  86 */       String schemaVersion = xmlParserHelper.getSchemaVersion("META-INF/validation.xml", xmlEventReader);
/*  87 */       Schema schema = getSchema(xmlParserHelper, schemaVersion);
/*  88 */       ValidationConfigType validationConfig = unmarshal(xmlEventReader, schema);
/*     */       
/*  90 */       return createBootstrapConfiguration(validationConfig);
/*     */     }
/*     */     finally {
/*  93 */       run(SetContextClassLoader.action(previousTccl));
/*  94 */       closeStream(inputStream);
/*     */     }
/*     */   }
/*     */   
/*     */   private InputStream getValidationXmlInputStream() {
/*  99 */     log.debugf("Trying to load %s for XML based Validator configuration.", "META-INF/validation.xml");
/* 100 */     InputStream inputStream = ResourceLoaderHelper.getResettableInputStreamForPath("META-INF/validation.xml", this.externalClassLoader);
/*     */     
/* 102 */     if (inputStream != null) {
/* 103 */       return inputStream;
/*     */     }
/*     */     
/* 106 */     log.debugf("No %s found. Using annotation based configuration only.", "META-INF/validation.xml");
/* 107 */     return null;
/*     */   }
/*     */   
/*     */   private Schema getSchema(XmlParserHelper xmlParserHelper, String schemaVersion)
/*     */   {
/* 112 */     String schemaResource = (String)SCHEMAS_BY_VERSION.get(schemaVersion);
/*     */     
/* 114 */     if (schemaResource == null) {
/* 115 */       throw log.getUnsupportedSchemaVersionException("META-INF/validation.xml", schemaVersion);
/*     */     }
/*     */     
/* 118 */     return xmlParserHelper.getSchema(schemaResource);
/*     */   }
/*     */   
/*     */   private ValidationConfigType unmarshal(XMLEventReader xmlEventReader, Schema schema) {
/* 122 */     log.parsingXMLFile("META-INF/validation.xml");
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 127 */       JAXBContext jc = (JAXBContext)run(NewJaxbContext.action(ValidationConfigType.class));
/* 128 */       Unmarshaller unmarshaller = jc.createUnmarshaller();
/* 129 */       unmarshaller.setSchema(schema);
/*     */       
/*     */ 
/*     */ 
/* 133 */       JAXBElement<ValidationConfigType> root = (JAXBElement)run(Unmarshal.action(unmarshaller, xmlEventReader, ValidationConfigType.class));
/* 134 */       return (ValidationConfigType)root.getValue();
/*     */     }
/*     */     catch (Exception e) {
/* 137 */       throw log.getUnableToParseValidationXmlFileException("META-INF/validation.xml", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void closeStream(InputStream inputStream) {
/*     */     try {
/* 143 */       inputStream.close();
/*     */     }
/*     */     catch (IOException io) {
/* 146 */       log.unableToCloseXMLFileInputStream("META-INF/validation.xml");
/*     */     }
/*     */   }
/*     */   
/*     */   private BootstrapConfiguration createBootstrapConfiguration(ValidationConfigType config) {
/* 151 */     Map<String, String> properties = new HashMap();
/* 152 */     for (PropertyType property : config.getProperty()) {
/* 153 */       if (log.isDebugEnabled()) {
/* 154 */         log.debugf("Found property '%s' with value '%s' in validation.xml.", property
/*     */         
/* 156 */           .getName(), property
/* 157 */           .getValue());
/*     */       }
/*     */       
/* 160 */       properties.put(property.getName(), property.getValue());
/*     */     }
/*     */     
/* 163 */     ExecutableValidationType executableValidationType = config.getExecutableValidation();
/*     */     
/*     */ 
/* 166 */     EnumSet<ExecutableType> defaultValidatedExecutableTypes = executableValidationType == null ? getValidatedExecutableTypes(null) : getValidatedExecutableTypes(executableValidationType.getDefaultValidatedExecutableTypes());
/* 167 */     boolean executableValidationEnabled = (executableValidationType == null) || (executableValidationType.getEnabled().booleanValue());
/*     */     
/* 169 */     return new BootstrapConfigurationImpl(config
/* 170 */       .getDefaultProvider(), config
/* 171 */       .getConstraintValidatorFactory(), config
/* 172 */       .getMessageInterpolator(), config
/* 173 */       .getTraversableResolver(), config
/* 174 */       .getParameterNameProvider(), defaultValidatedExecutableTypes, executableValidationEnabled, new HashSet(config
/*     */       
/*     */ 
/* 177 */       .getConstraintMapping()), properties);
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
/*     */   private EnumSet<ExecutableType> getValidatedExecutableTypes(DefaultValidatedExecutableTypesType validatedExecutables)
/*     */   {
/* 192 */     if (validatedExecutables == null) {
/* 193 */       return null;
/*     */     }
/*     */     
/* 196 */     EnumSet<ExecutableType> executableTypes = EnumSet.noneOf(ExecutableType.class);
/* 197 */     executableTypes.addAll(validatedExecutables.getExecutableType());
/*     */     
/* 199 */     return executableTypes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static <T> T run(PrivilegedAction<T> action)
/*     */   {
/* 209 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private <T> T run(PrivilegedExceptionAction<T> action)
/*     */     throws Exception
/*     */   {
/* 219 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\ValidationXmlParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */