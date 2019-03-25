/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Set;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.parsing.EmptyReaderEventListener;
/*     */ import org.springframework.beans.factory.parsing.FailFastProblemReporter;
/*     */ import org.springframework.beans.factory.parsing.NullSourceExtractor;
/*     */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*     */ import org.springframework.beans.factory.parsing.ReaderEventListener;
/*     */ import org.springframework.beans.factory.parsing.SourceExtractor;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.core.Constants;
/*     */ import org.springframework.core.NamedThreadLocal;
/*     */ import org.springframework.core.io.DescriptiveResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.EncodedResource;
/*     */ import org.springframework.util.xml.SimpleSaxErrorHandler;
/*     */ import org.springframework.util.xml.XmlValidationModeDetector;
/*     */ import org.w3c.dom.Document;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
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
/*     */ public class XmlBeanDefinitionReader
/*     */   extends AbstractBeanDefinitionReader
/*     */ {
/*     */   public static final int VALIDATION_NONE = 0;
/*     */   public static final int VALIDATION_AUTO = 1;
/*     */   public static final int VALIDATION_DTD = 2;
/*     */   public static final int VALIDATION_XSD = 3;
/* 101 */   private static final Constants constants = new Constants(XmlBeanDefinitionReader.class);
/*     */   
/* 103 */   private int validationMode = 1;
/*     */   
/* 105 */   private boolean namespaceAware = false;
/*     */   
/* 107 */   private Class<?> documentReaderClass = DefaultBeanDefinitionDocumentReader.class;
/*     */   
/* 109 */   private ProblemReporter problemReporter = new FailFastProblemReporter();
/*     */   
/* 111 */   private ReaderEventListener eventListener = new EmptyReaderEventListener();
/*     */   
/* 113 */   private SourceExtractor sourceExtractor = new NullSourceExtractor();
/*     */   
/*     */   private NamespaceHandlerResolver namespaceHandlerResolver;
/*     */   
/* 117 */   private DocumentLoader documentLoader = new DefaultDocumentLoader();
/*     */   
/*     */   private EntityResolver entityResolver;
/*     */   
/* 121 */   private ErrorHandler errorHandler = new SimpleSaxErrorHandler(this.logger);
/*     */   
/* 123 */   private final XmlValidationModeDetector validationModeDetector = new XmlValidationModeDetector();
/*     */   
/* 125 */   private final ThreadLocal<Set<EncodedResource>> resourcesCurrentlyBeingLoaded = new NamedThreadLocal("XML bean definition resources currently being loaded");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public XmlBeanDefinitionReader(BeanDefinitionRegistry registry)
/*     */   {
/* 135 */     super(registry);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValidating(boolean validating)
/*     */   {
/* 147 */     this.validationMode = (validating ? 1 : 0);
/* 148 */     this.namespaceAware = (!validating);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValidationModeName(String validationModeName)
/*     */   {
/* 156 */     setValidationMode(constants.asNumber(validationModeName).intValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValidationMode(int validationMode)
/*     */   {
/* 166 */     this.validationMode = validationMode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getValidationMode()
/*     */   {
/* 173 */     return this.validationMode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setNamespaceAware(boolean namespaceAware)
/*     */   {
/* 184 */     this.namespaceAware = namespaceAware;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isNamespaceAware()
/*     */   {
/* 191 */     return this.namespaceAware;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProblemReporter(ProblemReporter problemReporter)
/*     */   {
/* 201 */     this.problemReporter = (problemReporter != null ? problemReporter : new FailFastProblemReporter());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEventListener(ReaderEventListener eventListener)
/*     */   {
/* 211 */     this.eventListener = (eventListener != null ? eventListener : new EmptyReaderEventListener());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSourceExtractor(SourceExtractor sourceExtractor)
/*     */   {
/* 221 */     this.sourceExtractor = (sourceExtractor != null ? sourceExtractor : new NullSourceExtractor());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setNamespaceHandlerResolver(NamespaceHandlerResolver namespaceHandlerResolver)
/*     */   {
/* 230 */     this.namespaceHandlerResolver = namespaceHandlerResolver;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDocumentLoader(DocumentLoader documentLoader)
/*     */   {
/* 239 */     this.documentLoader = (documentLoader != null ? documentLoader : new DefaultDocumentLoader());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEntityResolver(EntityResolver entityResolver)
/*     */   {
/* 248 */     this.entityResolver = entityResolver;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected EntityResolver getEntityResolver()
/*     */   {
/* 256 */     if (this.entityResolver == null)
/*     */     {
/* 258 */       ResourceLoader resourceLoader = getResourceLoader();
/* 259 */       if (resourceLoader != null) {
/* 260 */         this.entityResolver = new ResourceEntityResolver(resourceLoader);
/*     */       }
/*     */       else {
/* 263 */         this.entityResolver = new DelegatingEntityResolver(getBeanClassLoader());
/*     */       }
/*     */     }
/* 266 */     return this.entityResolver;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setErrorHandler(ErrorHandler errorHandler)
/*     */   {
/* 278 */     this.errorHandler = errorHandler;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDocumentReaderClass(Class<?> documentReaderClass)
/*     */   {
/* 288 */     if ((documentReaderClass == null) || (!BeanDefinitionDocumentReader.class.isAssignableFrom(documentReaderClass))) {
/* 289 */       throw new IllegalArgumentException("documentReaderClass must be an implementation of the BeanDefinitionDocumentReader interface");
/*     */     }
/*     */     
/* 292 */     this.documentReaderClass = documentReaderClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int loadBeanDefinitions(Resource resource)
/*     */     throws BeanDefinitionStoreException
/*     */   {
/* 304 */     return loadBeanDefinitions(new EncodedResource(resource));
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public int loadBeanDefinitions(EncodedResource encodedResource)
/*     */     throws BeanDefinitionStoreException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: ldc 50
/*     */     //   3: invokestatic 51	org/springframework/util/Assert:notNull	(Ljava/lang/Object;Ljava/lang/String;)V
/*     */     //   6: aload_0
/*     */     //   7: getfield 21	org/springframework/beans/factory/xml/XmlBeanDefinitionReader:logger	Lorg/apache/commons/logging/Log;
/*     */     //   10: invokeinterface 52 1 0
/*     */     //   15: ifeq +34 -> 49
/*     */     //   18: aload_0
/*     */     //   19: getfield 21	org/springframework/beans/factory/xml/XmlBeanDefinitionReader:logger	Lorg/apache/commons/logging/Log;
/*     */     //   22: new 53	java/lang/StringBuilder
/*     */     //   25: dup
/*     */     //   26: invokespecial 54	java/lang/StringBuilder:<init>	()V
/*     */     //   29: ldc 55
/*     */     //   31: invokevirtual 56	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   34: aload_1
/*     */     //   35: invokevirtual 57	org/springframework/core/io/support/EncodedResource:getResource	()Lorg/springframework/core/io/Resource;
/*     */     //   38: invokevirtual 58	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   41: invokevirtual 59	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   44: invokeinterface 60 2 0
/*     */     //   49: aload_0
/*     */     //   50: getfield 29	org/springframework/beans/factory/xml/XmlBeanDefinitionReader:resourcesCurrentlyBeingLoaded	Ljava/lang/ThreadLocal;
/*     */     //   53: invokevirtual 61	java/lang/ThreadLocal:get	()Ljava/lang/Object;
/*     */     //   56: checkcast 62	java/util/Set
/*     */     //   59: astore_2
/*     */     //   60: aload_2
/*     */     //   61: ifnonnull +20 -> 81
/*     */     //   64: new 63	java/util/HashSet
/*     */     //   67: dup
/*     */     //   68: iconst_4
/*     */     //   69: invokespecial 64	java/util/HashSet:<init>	(I)V
/*     */     //   72: astore_2
/*     */     //   73: aload_0
/*     */     //   74: getfield 29	org/springframework/beans/factory/xml/XmlBeanDefinitionReader:resourcesCurrentlyBeingLoaded	Ljava/lang/ThreadLocal;
/*     */     //   77: aload_2
/*     */     //   78: invokevirtual 65	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
/*     */     //   81: aload_2
/*     */     //   82: aload_1
/*     */     //   83: invokeinterface 66 2 0
/*     */     //   88: ifne +35 -> 123
/*     */     //   91: new 67	org/springframework/beans/factory/BeanDefinitionStoreException
/*     */     //   94: dup
/*     */     //   95: new 53	java/lang/StringBuilder
/*     */     //   98: dup
/*     */     //   99: invokespecial 54	java/lang/StringBuilder:<init>	()V
/*     */     //   102: ldc 68
/*     */     //   104: invokevirtual 56	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   107: aload_1
/*     */     //   108: invokevirtual 58	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   111: ldc 69
/*     */     //   113: invokevirtual 56	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   116: invokevirtual 59	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   119: invokespecial 70	org/springframework/beans/factory/BeanDefinitionStoreException:<init>	(Ljava/lang/String;)V
/*     */     //   122: athrow
/*     */     //   123: aload_1
/*     */     //   124: invokevirtual 57	org/springframework/core/io/support/EncodedResource:getResource	()Lorg/springframework/core/io/Resource;
/*     */     //   127: invokeinterface 71 1 0
/*     */     //   132: astore_3
/*     */     //   133: new 72	org/xml/sax/InputSource
/*     */     //   136: dup
/*     */     //   137: aload_3
/*     */     //   138: invokespecial 73	org/xml/sax/InputSource:<init>	(Ljava/io/InputStream;)V
/*     */     //   141: astore 4
/*     */     //   143: aload_1
/*     */     //   144: invokevirtual 74	org/springframework/core/io/support/EncodedResource:getEncoding	()Ljava/lang/String;
/*     */     //   147: ifnull +12 -> 159
/*     */     //   150: aload 4
/*     */     //   152: aload_1
/*     */     //   153: invokevirtual 74	org/springframework/core/io/support/EncodedResource:getEncoding	()Ljava/lang/String;
/*     */     //   156: invokevirtual 75	org/xml/sax/InputSource:setEncoding	(Ljava/lang/String;)V
/*     */     //   159: aload_0
/*     */     //   160: aload 4
/*     */     //   162: aload_1
/*     */     //   163: invokevirtual 57	org/springframework/core/io/support/EncodedResource:getResource	()Lorg/springframework/core/io/Resource;
/*     */     //   166: invokevirtual 76	org/springframework/beans/factory/xml/XmlBeanDefinitionReader:doLoadBeanDefinitions	(Lorg/xml/sax/InputSource;Lorg/springframework/core/io/Resource;)I
/*     */     //   169: istore 5
/*     */     //   171: aload_3
/*     */     //   172: invokevirtual 77	java/io/InputStream:close	()V
/*     */     //   175: aload_2
/*     */     //   176: aload_1
/*     */     //   177: invokeinterface 78 2 0
/*     */     //   182: pop
/*     */     //   183: aload_2
/*     */     //   184: invokeinterface 79 1 0
/*     */     //   189: ifeq +10 -> 199
/*     */     //   192: aload_0
/*     */     //   193: getfield 29	org/springframework/beans/factory/xml/XmlBeanDefinitionReader:resourcesCurrentlyBeingLoaded	Ljava/lang/ThreadLocal;
/*     */     //   196: invokevirtual 80	java/lang/ThreadLocal:remove	()V
/*     */     //   199: iload 5
/*     */     //   201: ireturn
/*     */     //   202: astore 6
/*     */     //   204: aload_3
/*     */     //   205: invokevirtual 77	java/io/InputStream:close	()V
/*     */     //   208: aload 6
/*     */     //   210: athrow
/*     */     //   211: astore_3
/*     */     //   212: new 67	org/springframework/beans/factory/BeanDefinitionStoreException
/*     */     //   215: dup
/*     */     //   216: new 53	java/lang/StringBuilder
/*     */     //   219: dup
/*     */     //   220: invokespecial 54	java/lang/StringBuilder:<init>	()V
/*     */     //   223: ldc 82
/*     */     //   225: invokevirtual 56	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   228: aload_1
/*     */     //   229: invokevirtual 57	org/springframework/core/io/support/EncodedResource:getResource	()Lorg/springframework/core/io/Resource;
/*     */     //   232: invokevirtual 58	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   235: invokevirtual 59	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   238: aload_3
/*     */     //   239: invokespecial 83	org/springframework/beans/factory/BeanDefinitionStoreException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   242: athrow
/*     */     //   243: astore 7
/*     */     //   245: aload_2
/*     */     //   246: aload_1
/*     */     //   247: invokeinterface 78 2 0
/*     */     //   252: pop
/*     */     //   253: aload_2
/*     */     //   254: invokeinterface 79 1 0
/*     */     //   259: ifeq +10 -> 269
/*     */     //   262: aload_0
/*     */     //   263: getfield 29	org/springframework/beans/factory/xml/XmlBeanDefinitionReader:resourcesCurrentlyBeingLoaded	Ljava/lang/ThreadLocal;
/*     */     //   266: invokevirtual 80	java/lang/ThreadLocal:remove	()V
/*     */     //   269: aload 7
/*     */     //   271: athrow
/*     */     // Line number table:
/*     */     //   Java source line #315	-> byte code offset #0
/*     */     //   Java source line #316	-> byte code offset #6
/*     */     //   Java source line #317	-> byte code offset #18
/*     */     //   Java source line #320	-> byte code offset #49
/*     */     //   Java source line #321	-> byte code offset #60
/*     */     //   Java source line #322	-> byte code offset #64
/*     */     //   Java source line #323	-> byte code offset #73
/*     */     //   Java source line #325	-> byte code offset #81
/*     */     //   Java source line #326	-> byte code offset #91
/*     */     //   Java source line #330	-> byte code offset #123
/*     */     //   Java source line #332	-> byte code offset #133
/*     */     //   Java source line #333	-> byte code offset #143
/*     */     //   Java source line #334	-> byte code offset #150
/*     */     //   Java source line #336	-> byte code offset #159
/*     */     //   Java source line #339	-> byte code offset #171
/*     */     //   Java source line #347	-> byte code offset #175
/*     */     //   Java source line #348	-> byte code offset #183
/*     */     //   Java source line #349	-> byte code offset #192
/*     */     //   Java source line #336	-> byte code offset #199
/*     */     //   Java source line #339	-> byte code offset #202
/*     */     //   Java source line #342	-> byte code offset #211
/*     */     //   Java source line #343	-> byte code offset #212
/*     */     //   Java source line #344	-> byte code offset #229
/*     */     //   Java source line #347	-> byte code offset #243
/*     */     //   Java source line #348	-> byte code offset #253
/*     */     //   Java source line #349	-> byte code offset #262
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	272	0	this	XmlBeanDefinitionReader
/*     */     //   0	272	1	encodedResource	EncodedResource
/*     */     //   59	195	2	currentResources	Set<EncodedResource>
/*     */     //   132	73	3	inputStream	InputStream
/*     */     //   211	28	3	ex	IOException
/*     */     //   141	20	4	inputSource	InputSource
/*     */     //   169	31	5	i	int
/*     */     //   202	7	6	localObject1	Object
/*     */     //   243	27	7	localObject2	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   133	171	202	finally
/*     */     //   202	204	202	finally
/*     */     //   123	175	211	java/io/IOException
/*     */     //   202	211	211	java/io/IOException
/*     */     //   123	175	243	finally
/*     */     //   202	245	243	finally
/*     */   }
/*     */   
/*     */   public int loadBeanDefinitions(InputSource inputSource)
/*     */     throws BeanDefinitionStoreException
/*     */   {
/* 361 */     return loadBeanDefinitions(inputSource, "resource loaded through SAX InputSource");
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
/*     */   public int loadBeanDefinitions(InputSource inputSource, String resourceDescription)
/*     */     throws BeanDefinitionStoreException
/*     */   {
/* 375 */     return doLoadBeanDefinitions(inputSource, new DescriptiveResource(resourceDescription));
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
/*     */   protected int doLoadBeanDefinitions(InputSource inputSource, Resource resource)
/*     */     throws BeanDefinitionStoreException
/*     */   {
/*     */     try
/*     */     {
/* 391 */       Document doc = doLoadDocument(inputSource, resource);
/* 392 */       return registerBeanDefinitions(doc, resource);
/*     */     }
/*     */     catch (BeanDefinitionStoreException ex) {
/* 395 */       throw ex;
/*     */     }
/*     */     catch (SAXParseException ex)
/*     */     {
/* 399 */       throw new XmlBeanDefinitionStoreException(resource.getDescription(), "Line " + ex.getLineNumber() + " in XML document from " + resource + " is invalid", ex);
/*     */     }
/*     */     catch (SAXException ex) {
/* 402 */       throw new XmlBeanDefinitionStoreException(resource.getDescription(), "XML document from " + resource + " is invalid", ex);
/*     */     }
/*     */     catch (ParserConfigurationException ex)
/*     */     {
/* 406 */       throw new BeanDefinitionStoreException(resource.getDescription(), "Parser configuration exception parsing XML from " + resource, ex);
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 410 */       throw new BeanDefinitionStoreException(resource.getDescription(), "IOException parsing XML document from " + resource, ex);
/*     */     }
/*     */     catch (Throwable ex)
/*     */     {
/* 414 */       throw new BeanDefinitionStoreException(resource.getDescription(), "Unexpected exception parsing XML document from " + resource, ex);
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
/*     */   protected Document doLoadDocument(InputSource inputSource, Resource resource)
/*     */     throws Exception
/*     */   {
/* 429 */     return this.documentLoader.loadDocument(inputSource, getEntityResolver(), this.errorHandler, 
/* 430 */       getValidationModeForResource(resource), isNamespaceAware());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getValidationModeForResource(Resource resource)
/*     */   {
/* 442 */     int validationModeToUse = getValidationMode();
/* 443 */     if (validationModeToUse != 1) {
/* 444 */       return validationModeToUse;
/*     */     }
/* 446 */     int detectedMode = detectValidationMode(resource);
/* 447 */     if (detectedMode != 1) {
/* 448 */       return detectedMode;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 453 */     return 3;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int detectValidationMode(Resource resource)
/*     */   {
/* 464 */     if (resource.isOpen()) {
/* 465 */       throw new BeanDefinitionStoreException("Passed-in Resource [" + resource + "] contains an open stream: cannot determine validation mode automatically. Either pass in a Resource that is able to create fresh streams, or explicitly specify the validationMode on your XmlBeanDefinitionReader instance.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 474 */       inputStream = resource.getInputStream();
/*     */     } catch (IOException ex) {
/*     */       InputStream inputStream;
/* 477 */       throw new BeanDefinitionStoreException("Unable to determine validation mode for [" + resource + "]: cannot open InputStream. Did you attempt to load directly from a SAX InputSource without specifying the validationMode on your XmlBeanDefinitionReader instance?", ex);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*     */       InputStream inputStream;
/*     */       
/* 484 */       return this.validationModeDetector.detectValidationMode(inputStream);
/*     */     }
/*     */     catch (IOException ex) {
/* 487 */       throw new BeanDefinitionStoreException("Unable to determine validation mode for [" + resource + "]: an error occurred whilst reading from the InputStream.", ex);
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
/*     */ 
/*     */ 
/*     */   public int registerBeanDefinitions(Document doc, Resource resource)
/*     */     throws BeanDefinitionStoreException
/*     */   {
/* 506 */     BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
/* 507 */     int countBefore = getRegistry().getBeanDefinitionCount();
/* 508 */     documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
/* 509 */     return getRegistry().getBeanDefinitionCount() - countBefore;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanDefinitionDocumentReader createBeanDefinitionDocumentReader()
/*     */   {
/* 519 */     return (BeanDefinitionDocumentReader)BeanDefinitionDocumentReader.class.cast(BeanUtils.instantiateClass(this.documentReaderClass));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public XmlReaderContext createReaderContext(Resource resource)
/*     */   {
/* 526 */     return new XmlReaderContext(resource, this.problemReporter, this.eventListener, this.sourceExtractor, this, 
/* 527 */       getNamespaceHandlerResolver());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NamespaceHandlerResolver getNamespaceHandlerResolver()
/*     */   {
/* 535 */     if (this.namespaceHandlerResolver == null) {
/* 536 */       this.namespaceHandlerResolver = createDefaultNamespaceHandlerResolver();
/*     */     }
/* 538 */     return this.namespaceHandlerResolver;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected NamespaceHandlerResolver createDefaultNamespaceHandlerResolver()
/*     */   {
/* 546 */     return new DefaultNamespaceHandlerResolver(getResourceLoader().getClassLoader());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\xml\XmlBeanDefinitionReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */