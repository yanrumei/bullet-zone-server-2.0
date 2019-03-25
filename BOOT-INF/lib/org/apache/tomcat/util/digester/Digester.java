/*      */ package org.apache.tomcat.util.digester;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.security.Permission;
/*      */ import java.util.EmptyStackException;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.PropertyPermission;
/*      */ import java.util.Set;
/*      */ import javax.xml.parsers.ParserConfigurationException;
/*      */ import javax.xml.parsers.SAXParser;
/*      */ import javax.xml.parsers.SAXParserFactory;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.IntrospectionUtils;
/*      */ import org.apache.tomcat.util.IntrospectionUtils.PropertySource;
/*      */ import org.apache.tomcat.util.buf.B2CConverter;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ import org.apache.tomcat.util.security.PermissionCheck;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.EntityResolver;
/*      */ import org.xml.sax.ErrorHandler;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.SAXNotRecognizedException;
/*      */ import org.xml.sax.SAXNotSupportedException;
/*      */ import org.xml.sax.SAXParseException;
/*      */ import org.xml.sax.XMLReader;
/*      */ import org.xml.sax.ext.DefaultHandler2;
/*      */ import org.xml.sax.ext.Locator2;
/*      */ import org.xml.sax.helpers.AttributesImpl;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Digester
/*      */   extends DefaultHandler2
/*      */ {
/*      */   protected static IntrospectionUtils.PropertySource propertySource;
/*   87 */   private static boolean propertySourceSet = false;
/*      */   
/*      */   static {
/*   90 */     String className = System.getProperty("org.apache.tomcat.util.digester.PROPERTY_SOURCE");
/*   91 */     IntrospectionUtils.PropertySource source = null;
/*   92 */     if (className != null)
/*      */     {
/*   94 */       ClassLoader[] cls = { Digester.class.getClassLoader(), Thread.currentThread().getContextClassLoader() };
/*   95 */       for (int i = 0; i < cls.length; i++) {
/*      */         try {
/*   97 */           Class<?> clazz = Class.forName(className, true, cls[i]);
/*      */           
/*   99 */           source = (IntrospectionUtils.PropertySource)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*      */         }
/*      */         catch (Throwable t) {
/*  102 */           ExceptionUtils.handleThrowable(t);
/*  103 */           LogFactory.getLog("org.apache.tomcat.util.digester.Digester")
/*  104 */             .error("Unable to load property source[" + className + "].", t);
/*      */         }
/*      */       }
/*      */     }
/*  108 */     if (source != null) {
/*  109 */       propertySource = source;
/*  110 */       propertySourceSet = true;
/*      */     }
/*  112 */     if (Boolean.getBoolean("org.apache.tomcat.util.digester.REPLACE_SYSTEM_PROPERTIES")) {
/*  113 */       replaceSystemProperties();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void setPropertySource(IntrospectionUtils.PropertySource propertySource) {
/*  118 */     if (!propertySourceSet) {
/*  119 */       propertySource = propertySource;
/*  120 */       propertySourceSet = true;
/*      */     }
/*      */   }
/*      */   
/*      */   private class SystemPropertySource implements IntrospectionUtils.PropertySource
/*      */   {
/*      */     private SystemPropertySource() {}
/*      */     
/*      */     public String getProperty(String key)
/*      */     {
/*  130 */       ClassLoader cl = Digester.this.getClassLoader();
/*  131 */       if ((cl instanceof PermissionCheck)) {
/*  132 */         Permission p = new PropertyPermission(key, "read");
/*  133 */         if (!((PermissionCheck)cl).check(p)) {
/*  134 */           return null;
/*      */         }
/*      */       }
/*  137 */       return System.getProperty(key);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*  142 */   protected IntrospectionUtils.PropertySource[] source = { new SystemPropertySource(null) };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  149 */   protected StringBuilder bodyText = new StringBuilder();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  155 */   protected ArrayStack<StringBuilder> bodyTexts = new ArrayStack();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  168 */   protected ArrayStack<List<Rule>> matches = new ArrayStack(10);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  176 */   protected ClassLoader classLoader = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  182 */   protected boolean configured = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected EntityResolver entityResolver;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  194 */   protected HashMap<String, String> entityValidator = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  201 */   protected ErrorHandler errorHandler = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  207 */   protected SAXParserFactory factory = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  212 */   protected Locator locator = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  218 */   protected String match = "";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  224 */   protected boolean namespaceAware = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  235 */   protected HashMap<String, ArrayStack<String>> namespaces = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  242 */   protected ArrayStack<Object> params = new ArrayStack();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  247 */   protected SAXParser parser = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  254 */   protected String publicId = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  260 */   protected XMLReader reader = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  267 */   protected Object root = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  276 */   protected Rules rules = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  281 */   protected ArrayStack<Object> stack = new ArrayStack();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  288 */   protected boolean useContextClassLoader = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  294 */   protected boolean validating = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  300 */   protected boolean rulesValidation = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  306 */   protected Map<Class<?>, List<String>> fakeAttributes = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  312 */   protected Log log = LogFactory.getLog(Digester.class);
/*  313 */   protected static final StringManager sm = StringManager.getManager(Digester.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  318 */   protected Log saxLog = LogFactory.getLog("org.apache.tomcat.util.digester.Digester.sax");
/*      */   
/*      */   public Digester()
/*      */   {
/*  322 */     propertySourceSet = true;
/*  323 */     if (propertySource != null) {
/*  324 */       this.source = new IntrospectionUtils.PropertySource[] { propertySource, this.source[0] };
/*      */     }
/*      */   }
/*      */   
/*      */   public static void replaceSystemProperties()
/*      */   {
/*  330 */     Log log = LogFactory.getLog(Digester.class);
/*  331 */     IntrospectionUtils.PropertySource[] propertySources; if (propertySource != null) {
/*  332 */       propertySources = new IntrospectionUtils.PropertySource[] { propertySource };
/*      */       
/*  334 */       Properties properties = System.getProperties();
/*  335 */       Set<String> names = properties.stringPropertyNames();
/*  336 */       for (String name : names) {
/*  337 */         String value = System.getProperty(name);
/*  338 */         if (value != null) {
/*      */           try {
/*  340 */             String newValue = IntrospectionUtils.replaceProperties(value, null, propertySources);
/*  341 */             if (!value.equals(newValue)) {
/*  342 */               System.setProperty(name, newValue);
/*      */             }
/*      */           } catch (Exception e) {
/*  345 */             log.warn(sm.getString("digester.failedToUpdateSystemProperty", new Object[] { name, value }), e);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String findNamespaceURI(String prefix)
/*      */   {
/*  365 */     ArrayStack<String> stack = (ArrayStack)this.namespaces.get(prefix);
/*  366 */     if (stack == null) {
/*  367 */       return null;
/*      */     }
/*      */     try {
/*  370 */       return (String)stack.peek();
/*      */     } catch (EmptyStackException e) {}
/*  372 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ClassLoader getClassLoader()
/*      */   {
/*  391 */     if (this.classLoader != null) {
/*  392 */       return this.classLoader;
/*      */     }
/*  394 */     if (this.useContextClassLoader) {
/*  395 */       ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/*  396 */       if (classLoader != null) {
/*  397 */         return classLoader;
/*      */       }
/*      */     }
/*  400 */     return getClass().getClassLoader();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setClassLoader(ClassLoader classLoader)
/*      */   {
/*  414 */     this.classLoader = classLoader;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getCount()
/*      */   {
/*  424 */     return this.stack.size();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getCurrentElementName()
/*      */   {
/*  434 */     String elementName = this.match;
/*  435 */     int lastSlash = elementName.lastIndexOf('/');
/*  436 */     if (lastSlash >= 0) {
/*  437 */       elementName = elementName.substring(lastSlash + 1);
/*      */     }
/*  439 */     return elementName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ErrorHandler getErrorHandler()
/*      */   {
/*  449 */     return this.errorHandler;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setErrorHandler(ErrorHandler errorHandler)
/*      */   {
/*  461 */     this.errorHandler = errorHandler;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SAXParserFactory getFactory()
/*      */     throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException
/*      */   {
/*  476 */     if (this.factory == null) {
/*  477 */       this.factory = SAXParserFactory.newInstance();
/*      */       
/*  479 */       this.factory.setNamespaceAware(this.namespaceAware);
/*      */       
/*  481 */       if (this.namespaceAware) {
/*  482 */         this.factory.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
/*      */       }
/*      */       
/*  485 */       this.factory.setValidating(this.validating);
/*  486 */       if (this.validating)
/*      */       {
/*  488 */         this.factory.setFeature("http://xml.org/sax/features/validation", true);
/*      */         
/*  490 */         this.factory.setFeature("http://apache.org/xml/features/validation/schema", true);
/*      */       }
/*      */     }
/*  493 */     return this.factory;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFeature(String feature, boolean value)
/*      */     throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException
/*      */   {
/*  521 */     getFactory().setFeature(feature, value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Log getLogger()
/*      */   {
/*  531 */     return this.log;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLogger(Log log)
/*      */   {
/*  542 */     this.log = log;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Log getSAXLogger()
/*      */   {
/*  555 */     return this.saxLog;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSAXLogger(Log saxLog)
/*      */   {
/*  568 */     this.saxLog = saxLog;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getMatch()
/*      */   {
/*  576 */     return this.match;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getNamespaceAware()
/*      */   {
/*  586 */     return this.namespaceAware;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNamespaceAware(boolean namespaceAware)
/*      */   {
/*  598 */     this.namespaceAware = namespaceAware;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPublicId(String publicId)
/*      */   {
/*  608 */     this.publicId = publicId;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getPublicId()
/*      */   {
/*  618 */     return this.publicId;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public String getRuleNamespaceURI()
/*      */   {
/*  632 */     return getRules().getNamespaceURI();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public void setRuleNamespaceURI(String ruleNamespaceURI)
/*      */   {
/*  650 */     getRules().setNamespaceURI(ruleNamespaceURI);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SAXParser getParser()
/*      */   {
/*  662 */     if (this.parser != null) {
/*  663 */       return this.parser;
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  668 */       this.parser = getFactory().newSAXParser();
/*      */     } catch (Exception e) {
/*  670 */       this.log.error("Digester.getParser: ", e);
/*  671 */       return null;
/*      */     }
/*      */     
/*  674 */     return this.parser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getProperty(String property)
/*      */     throws SAXNotRecognizedException, SAXNotSupportedException
/*      */   {
/*  696 */     return getParser().getProperty(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Rules getRules()
/*      */   {
/*  709 */     if (this.rules == null) {
/*  710 */       this.rules = new RulesBase();
/*  711 */       this.rules.setDigester(this);
/*      */     }
/*  713 */     return this.rules;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRules(Rules rules)
/*      */   {
/*  726 */     this.rules = rules;
/*  727 */     this.rules.setDigester(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getUseContextClassLoader()
/*      */   {
/*  737 */     return this.useContextClassLoader;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseContextClassLoader(boolean use)
/*      */   {
/*  753 */     this.useContextClassLoader = use;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getValidating()
/*      */   {
/*  763 */     return this.validating;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setValidating(boolean validating)
/*      */   {
/*  776 */     this.validating = validating;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getRulesValidation()
/*      */   {
/*  786 */     return this.rulesValidation;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRulesValidation(boolean rulesValidation)
/*      */   {
/*  799 */     this.rulesValidation = rulesValidation;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Map<Class<?>, List<String>> getFakeAttributes()
/*      */   {
/*  809 */     return this.fakeAttributes;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isFakeAttribute(Object object, String name)
/*      */   {
/*  822 */     if (this.fakeAttributes == null) {
/*  823 */       return false;
/*      */     }
/*  825 */     List<String> result = (List)this.fakeAttributes.get(object.getClass());
/*  826 */     if (result == null) {
/*  827 */       result = (List)this.fakeAttributes.get(Object.class);
/*      */     }
/*  829 */     if (result == null) {
/*  830 */       return false;
/*      */     }
/*  832 */     return result.contains(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFakeAttributes(Map<Class<?>, List<String>> fakeAttributes)
/*      */   {
/*  845 */     this.fakeAttributes = fakeAttributes;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public XMLReader getXMLReader()
/*      */     throws SAXException
/*      */   {
/*  859 */     if (this.reader == null) {
/*  860 */       this.reader = getParser().getXMLReader();
/*      */     }
/*      */     
/*  863 */     this.reader.setDTDHandler(this);
/*  864 */     this.reader.setContentHandler(this);
/*      */     
/*  866 */     if (this.entityResolver == null) {
/*  867 */       this.reader.setEntityResolver(this);
/*      */     } else {
/*  869 */       this.reader.setEntityResolver(this.entityResolver);
/*      */     }
/*      */     
/*  872 */     this.reader.setProperty("http://xml.org/sax/properties/lexical-handler", this);
/*      */     
/*  874 */     this.reader.setErrorHandler(this);
/*  875 */     return this.reader;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void characters(char[] buffer, int start, int length)
/*      */     throws SAXException
/*      */   {
/*  894 */     if (this.saxLog.isDebugEnabled()) {
/*  895 */       this.saxLog.debug("characters(" + new String(buffer, start, length) + ")");
/*      */     }
/*      */     
/*  898 */     this.bodyText.append(buffer, start, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void endDocument()
/*      */     throws SAXException
/*      */   {
/*  911 */     if (this.saxLog.isDebugEnabled()) {
/*  912 */       if (getCount() > 1) {
/*  913 */         this.saxLog.debug("endDocument():  " + getCount() + " elements left");
/*      */       } else {
/*  915 */         this.saxLog.debug("endDocument()");
/*      */       }
/*      */     }
/*      */     
/*  919 */     while (getCount() > 1) {
/*  920 */       pop();
/*      */     }
/*      */     
/*      */ 
/*  924 */     Iterator<Rule> rules = getRules().rules().iterator();
/*  925 */     while (rules.hasNext()) {
/*  926 */       Rule rule = (Rule)rules.next();
/*      */       try {
/*  928 */         rule.finish();
/*      */       } catch (Exception e) {
/*  930 */         this.log.error("Finish event threw exception", e);
/*  931 */         throw createSAXException(e);
/*      */       } catch (Error e) {
/*  933 */         this.log.error("Finish event threw error", e);
/*  934 */         throw e;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  939 */     clear();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void endElement(String namespaceURI, String localName, String qName)
/*      */     throws SAXException
/*      */   {
/*  960 */     boolean debug = this.log.isDebugEnabled();
/*      */     
/*  962 */     if (debug) {
/*  963 */       if (this.saxLog.isDebugEnabled()) {
/*  964 */         this.saxLog.debug("endElement(" + namespaceURI + "," + localName + "," + qName + ")");
/*      */       }
/*  966 */       this.log.debug("  match='" + this.match + "'");
/*  967 */       this.log.debug("  bodyText='" + this.bodyText + "'");
/*      */     }
/*      */     
/*      */ 
/*  971 */     this.bodyText = updateBodyText(this.bodyText);
/*      */     
/*      */ 
/*      */ 
/*  975 */     String name = localName;
/*  976 */     if ((name == null) || (name.length() < 1)) {
/*  977 */       name = qName;
/*      */     }
/*      */     
/*      */ 
/*  981 */     List<Rule> rules = (List)this.matches.pop();
/*  982 */     if ((rules != null) && (rules.size() > 0)) {
/*  983 */       String bodyText = this.bodyText.toString();
/*  984 */       for (int i = 0; i < rules.size(); i++) {
/*      */         try {
/*  986 */           Rule rule = (Rule)rules.get(i);
/*  987 */           if (debug) {
/*  988 */             this.log.debug("  Fire body() for " + rule);
/*      */           }
/*  990 */           rule.body(namespaceURI, name, bodyText);
/*      */         } catch (Exception e) {
/*  992 */           this.log.error("Body event threw exception", e);
/*  993 */           throw createSAXException(e);
/*      */         } catch (Error e) {
/*  995 */           this.log.error("Body event threw error", e);
/*  996 */           throw e;
/*      */         }
/*      */       }
/*      */     } else {
/* 1000 */       if (debug) {
/* 1001 */         this.log.debug("  No rules found matching '" + this.match + "'.");
/*      */       }
/* 1003 */       if (this.rulesValidation) {
/* 1004 */         this.log.warn("  No rules found matching '" + this.match + "'.");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1009 */     this.bodyText = ((StringBuilder)this.bodyTexts.pop());
/*      */     
/*      */ 
/* 1012 */     if (rules != null) {
/* 1013 */       for (int i = 0; i < rules.size(); i++) {
/* 1014 */         int j = rules.size() - i - 1;
/*      */         try {
/* 1016 */           Rule rule = (Rule)rules.get(j);
/* 1017 */           if (debug) {
/* 1018 */             this.log.debug("  Fire end() for " + rule);
/*      */           }
/* 1020 */           rule.end(namespaceURI, name);
/*      */         } catch (Exception e) {
/* 1022 */           this.log.error("End event threw exception", e);
/* 1023 */           throw createSAXException(e);
/*      */         } catch (Error e) {
/* 1025 */           this.log.error("End event threw error", e);
/* 1026 */           throw e;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1032 */     int slash = this.match.lastIndexOf('/');
/* 1033 */     if (slash >= 0) {
/* 1034 */       this.match = this.match.substring(0, slash);
/*      */     } else {
/* 1036 */       this.match = "";
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void endPrefixMapping(String prefix)
/*      */     throws SAXException
/*      */   {
/* 1052 */     if (this.saxLog.isDebugEnabled()) {
/* 1053 */       this.saxLog.debug("endPrefixMapping(" + prefix + ")");
/*      */     }
/*      */     
/*      */ 
/* 1057 */     ArrayStack<String> stack = (ArrayStack)this.namespaces.get(prefix);
/* 1058 */     if (stack == null) {
/* 1059 */       return;
/*      */     }
/*      */     try {
/* 1062 */       stack.pop();
/* 1063 */       if (stack.empty())
/* 1064 */         this.namespaces.remove(prefix);
/*      */     } catch (EmptyStackException e) {
/* 1066 */       throw createSAXException("endPrefixMapping popped too many times");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void ignorableWhitespace(char[] buffer, int start, int len)
/*      */     throws SAXException
/*      */   {
/* 1085 */     if (this.saxLog.isDebugEnabled()) {
/* 1086 */       this.saxLog.debug("ignorableWhitespace(" + new String(buffer, start, len) + ")");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void processingInstruction(String target, String data)
/*      */     throws SAXException
/*      */   {
/* 1105 */     if (this.saxLog.isDebugEnabled()) {
/* 1106 */       this.saxLog.debug("processingInstruction('" + target + "','" + data + "')");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Locator getDocumentLocator()
/*      */   {
/* 1121 */     return this.locator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDocumentLocator(Locator locator)
/*      */   {
/* 1133 */     if (this.saxLog.isDebugEnabled()) {
/* 1134 */       this.saxLog.debug("setDocumentLocator(" + locator + ")");
/*      */     }
/*      */     
/* 1137 */     this.locator = locator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void skippedEntity(String name)
/*      */     throws SAXException
/*      */   {
/* 1152 */     if (this.saxLog.isDebugEnabled()) {
/* 1153 */       this.saxLog.debug("skippedEntity(" + name + ")");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void startDocument()
/*      */     throws SAXException
/*      */   {
/* 1170 */     if (this.saxLog.isDebugEnabled()) {
/* 1171 */       this.saxLog.debug("startDocument()");
/*      */     }
/*      */     
/* 1174 */     if ((this.locator instanceof Locator2)) {
/* 1175 */       if ((this.root instanceof DocumentProperties.Charset)) {
/* 1176 */         String enc = ((Locator2)this.locator).getEncoding();
/* 1177 */         if (enc != null) {
/*      */           try {
/* 1179 */             ((DocumentProperties.Charset)this.root).setCharset(B2CConverter.getCharset(enc));
/*      */           } catch (UnsupportedEncodingException e) {
/* 1181 */             this.log.warn(sm.getString("disgester.encodingInvalid", new Object[] { enc }), e);
/*      */           }
/*      */         }
/* 1184 */       } else if ((this.root instanceof DocumentProperties.Encoding)) {
/* 1185 */         ((DocumentProperties.Encoding)this.root).setEncoding(((Locator2)this.locator).getEncoding());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1192 */     configure();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void startElement(String namespaceURI, String localName, String qName, Attributes list)
/*      */     throws SAXException
/*      */   {
/* 1212 */     boolean debug = this.log.isDebugEnabled();
/*      */     
/* 1214 */     if (this.saxLog.isDebugEnabled()) {
/* 1215 */       this.saxLog.debug("startElement(" + namespaceURI + "," + localName + "," + qName + ")");
/*      */     }
/*      */     
/*      */ 
/* 1219 */     list = updateAttributes(list);
/*      */     
/*      */ 
/* 1222 */     this.bodyTexts.push(this.bodyText);
/* 1223 */     this.bodyText = new StringBuilder();
/*      */     
/*      */ 
/*      */ 
/* 1227 */     String name = localName;
/* 1228 */     if ((name == null) || (name.length() < 1)) {
/* 1229 */       name = qName;
/*      */     }
/*      */     
/*      */ 
/* 1233 */     StringBuilder sb = new StringBuilder(this.match);
/* 1234 */     if (this.match.length() > 0) {
/* 1235 */       sb.append('/');
/*      */     }
/* 1237 */     sb.append(name);
/* 1238 */     this.match = sb.toString();
/* 1239 */     if (debug) {
/* 1240 */       this.log.debug("  New match='" + this.match + "'");
/*      */     }
/*      */     
/*      */ 
/* 1244 */     List<Rule> rules = getRules().match(namespaceURI, this.match);
/* 1245 */     this.matches.push(rules);
/* 1246 */     if ((rules != null) && (rules.size() > 0)) {
/* 1247 */       for (int i = 0; i < rules.size(); i++) {
/*      */         try {
/* 1249 */           Rule rule = (Rule)rules.get(i);
/* 1250 */           if (debug) {
/* 1251 */             this.log.debug("  Fire begin() for " + rule);
/*      */           }
/* 1253 */           rule.begin(namespaceURI, name, list);
/*      */         } catch (Exception e) {
/* 1255 */           this.log.error("Begin event threw exception", e);
/* 1256 */           throw createSAXException(e);
/*      */         } catch (Error e) {
/* 1258 */           this.log.error("Begin event threw error", e);
/* 1259 */           throw e;
/*      */         }
/*      */         
/*      */       }
/* 1263 */     } else if (debug) {
/* 1264 */       this.log.debug("  No rules found matching '" + this.match + "'.");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void startPrefixMapping(String prefix, String namespaceURI)
/*      */     throws SAXException
/*      */   {
/* 1282 */     if (this.saxLog.isDebugEnabled()) {
/* 1283 */       this.saxLog.debug("startPrefixMapping(" + prefix + "," + namespaceURI + ")");
/*      */     }
/*      */     
/*      */ 
/* 1287 */     ArrayStack<String> stack = (ArrayStack)this.namespaces.get(prefix);
/* 1288 */     if (stack == null) {
/* 1289 */       stack = new ArrayStack();
/* 1290 */       this.namespaces.put(prefix, stack);
/*      */     }
/* 1292 */     stack.push(namespaceURI);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void notationDecl(String name, String publicId, String systemId)
/*      */   {
/* 1310 */     if (this.saxLog.isDebugEnabled()) {
/* 1311 */       this.saxLog.debug("notationDecl(" + name + "," + publicId + "," + systemId + ")");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void unparsedEntityDecl(String name, String publicId, String systemId, String notation)
/*      */   {
/* 1328 */     if (this.saxLog.isDebugEnabled()) {
/* 1329 */       this.saxLog.debug("unparsedEntityDecl(" + name + "," + publicId + "," + systemId + "," + notation + ")");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEntityResolver(EntityResolver entityResolver)
/*      */   {
/* 1345 */     this.entityResolver = entityResolver;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public EntityResolver getEntityResolver()
/*      */   {
/* 1354 */     return this.entityResolver;
/*      */   }
/*      */   
/*      */ 
/*      */   public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId)
/*      */     throws SAXException, IOException
/*      */   {
/* 1361 */     if (this.saxLog.isDebugEnabled()) {
/* 1362 */       this.saxLog.debug("resolveEntity('" + publicId + "', '" + systemId + "', '" + baseURI + "')");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1367 */     String entityURL = null;
/* 1368 */     if (publicId != null) {
/* 1369 */       entityURL = (String)this.entityValidator.get(publicId);
/*      */     }
/*      */     
/* 1372 */     if (entityURL == null) {
/* 1373 */       if (systemId == null)
/*      */       {
/* 1375 */         if (this.log.isDebugEnabled()) {
/* 1376 */           this.log.debug(" Cannot resolve entity: '" + publicId + "'");
/*      */         }
/* 1378 */         return null;
/*      */       }
/*      */       
/*      */ 
/* 1382 */       if (this.log.isDebugEnabled()) {
/* 1383 */         this.log.debug(" Trying to resolve using system ID '" + systemId + "'");
/*      */       }
/* 1385 */       entityURL = systemId;
/*      */       
/* 1387 */       if (baseURI != null) {
/*      */         try {
/* 1389 */           URI uri = new URI(systemId);
/* 1390 */           if (!uri.isAbsolute()) {
/* 1391 */             entityURL = new URI(baseURI).resolve(uri).toString();
/*      */           }
/*      */         } catch (URISyntaxException e) {
/* 1394 */           if (this.log.isDebugEnabled()) {
/* 1395 */             this.log.debug("Invalid URI '" + baseURI + "' or '" + systemId + "'");
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1403 */     if (this.log.isDebugEnabled()) {
/* 1404 */       this.log.debug(" Resolving to alternate DTD '" + entityURL + "'");
/*      */     }
/*      */     try
/*      */     {
/* 1408 */       return new InputSource(entityURL);
/*      */     } catch (Exception e) {
/* 1410 */       throw createSAXException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void startDTD(String name, String publicId, String systemId)
/*      */     throws SAXException
/*      */   {
/* 1419 */     setPublicId(publicId);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void error(SAXParseException exception)
/*      */     throws SAXException
/*      */   {
/* 1436 */     this.log.error("Parse Error at line " + exception.getLineNumber() + " column " + exception
/* 1437 */       .getColumnNumber() + ": " + exception.getMessage(), exception);
/* 1438 */     if (this.errorHandler != null) {
/* 1439 */       this.errorHandler.error(exception);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void fatalError(SAXParseException exception)
/*      */     throws SAXException
/*      */   {
/* 1456 */     this.log.error("Parse Fatal Error at line " + exception.getLineNumber() + " column " + exception
/* 1457 */       .getColumnNumber() + ": " + exception.getMessage(), exception);
/* 1458 */     if (this.errorHandler != null) {
/* 1459 */       this.errorHandler.fatalError(exception);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void warning(SAXParseException exception)
/*      */     throws SAXException
/*      */   {
/* 1475 */     if (this.errorHandler != null) {
/* 1476 */       this.log.warn("Parse Warning Error at line " + exception
/* 1477 */         .getLineNumber() + " column " + exception
/* 1478 */         .getColumnNumber() + ": " + exception.getMessage(), exception);
/*      */       
/*      */ 
/* 1481 */       this.errorHandler.warning(exception);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object parse(File file)
/*      */     throws IOException, SAXException
/*      */   {
/* 1500 */     configure();
/* 1501 */     InputSource input = new InputSource(new FileInputStream(file));
/* 1502 */     input.setSystemId("file://" + file.getAbsolutePath());
/* 1503 */     getXMLReader().parse(input);
/* 1504 */     return this.root;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object parse(InputSource input)
/*      */     throws IOException, SAXException
/*      */   {
/* 1520 */     configure();
/* 1521 */     getXMLReader().parse(input);
/* 1522 */     return this.root;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object parse(InputStream input)
/*      */     throws IOException, SAXException
/*      */   {
/* 1538 */     configure();
/* 1539 */     InputSource is = new InputSource(input);
/* 1540 */     getXMLReader().parse(is);
/* 1541 */     return this.root;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void register(String publicId, String entityURL)
/*      */   {
/* 1569 */     if (this.log.isDebugEnabled()) {
/* 1570 */       this.log.debug("register('" + publicId + "', '" + entityURL + "'");
/*      */     }
/* 1572 */     this.entityValidator.put(publicId, entityURL);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addRule(String pattern, Rule rule)
/*      */   {
/* 1589 */     rule.setDigester(this);
/* 1590 */     getRules().add(pattern, rule);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addRuleSet(RuleSet ruleSet)
/*      */   {
/* 1602 */     String oldNamespaceURI = getRuleNamespaceURI();
/*      */     
/* 1604 */     String newNamespaceURI = ruleSet.getNamespaceURI();
/* 1605 */     if (this.log.isDebugEnabled()) {
/* 1606 */       if (newNamespaceURI == null) {
/* 1607 */         this.log.debug("addRuleSet() with no namespace URI");
/*      */       } else {
/* 1609 */         this.log.debug("addRuleSet() with namespace URI " + newNamespaceURI);
/*      */       }
/*      */     }
/* 1612 */     setRuleNamespaceURI(newNamespaceURI);
/* 1613 */     ruleSet.addRuleInstances(this);
/* 1614 */     setRuleNamespaceURI(oldNamespaceURI);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addCallMethod(String pattern, String methodName)
/*      */   {
/* 1628 */     addRule(pattern, new CallMethodRule(methodName));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addCallMethod(String pattern, String methodName, int paramCount)
/*      */   {
/* 1643 */     addRule(pattern, new CallMethodRule(methodName, paramCount));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addCallParam(String pattern, int paramIndex)
/*      */   {
/* 1658 */     addRule(pattern, new CallParamRule(paramIndex));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addFactoryCreate(String pattern, ObjectCreationFactory creationFactory, boolean ignoreCreateExceptions)
/*      */   {
/* 1676 */     creationFactory.setDigester(this);
/* 1677 */     addRule(pattern, new FactoryCreateRule(creationFactory, ignoreCreateExceptions));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addObjectCreate(String pattern, String className)
/*      */   {
/* 1690 */     addRule(pattern, new ObjectCreateRule(className));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addObjectCreate(String pattern, String className, String attributeName)
/*      */   {
/* 1706 */     addRule(pattern, new ObjectCreateRule(className, attributeName));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addSetNext(String pattern, String methodName, String paramType)
/*      */   {
/* 1724 */     addRule(pattern, new SetNextRule(methodName, paramType));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addSetProperties(String pattern)
/*      */   {
/* 1737 */     addRule(pattern, new SetPropertiesRule());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clear()
/*      */   {
/* 1755 */     this.match = "";
/* 1756 */     this.bodyTexts.clear();
/* 1757 */     this.params.clear();
/* 1758 */     this.publicId = null;
/* 1759 */     this.stack.clear();
/* 1760 */     this.log = null;
/* 1761 */     this.saxLog = null;
/* 1762 */     this.configured = false;
/*      */   }
/*      */   
/*      */ 
/*      */   public void reset()
/*      */   {
/* 1768 */     this.root = null;
/* 1769 */     setErrorHandler(null);
/* 1770 */     clear();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object peek()
/*      */   {
/*      */     try
/*      */     {
/* 1782 */       return this.stack.peek();
/*      */     } catch (EmptyStackException e) {
/* 1784 */       this.log.warn("Empty stack (returning null)"); }
/* 1785 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object peek(int n)
/*      */   {
/*      */     try
/*      */     {
/* 1803 */       return this.stack.peek(n);
/*      */     } catch (EmptyStackException e) {
/* 1805 */       this.log.warn("Empty stack (returning null)"); }
/* 1806 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object pop()
/*      */   {
/*      */     try
/*      */     {
/* 1820 */       return this.stack.pop();
/*      */     } catch (EmptyStackException e) {
/* 1822 */       this.log.warn("Empty stack (returning null)"); }
/* 1823 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void push(Object object)
/*      */   {
/* 1836 */     if (this.stack.size() == 0) {
/* 1837 */       this.root = object;
/*      */     }
/* 1839 */     this.stack.push(object);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getRoot()
/*      */   {
/* 1852 */     return this.root;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void configure()
/*      */   {
/* 1876 */     if (this.configured) {
/* 1877 */       return;
/*      */     }
/*      */     
/* 1880 */     this.log = LogFactory.getLog("org.apache.tomcat.util.digester.Digester");
/* 1881 */     this.saxLog = LogFactory.getLog("org.apache.tomcat.util.digester.Digester.sax");
/*      */     
/*      */ 
/* 1884 */     this.configured = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object peekParams()
/*      */   {
/*      */     try
/*      */     {
/* 1899 */       return this.params.peek();
/*      */     } catch (EmptyStackException e) {
/* 1901 */       this.log.warn("Empty stack (returning null)"); }
/* 1902 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object popParams()
/*      */   {
/*      */     try
/*      */     {
/* 1919 */       if (this.log.isTraceEnabled()) {
/* 1920 */         this.log.trace("Popping params");
/*      */       }
/* 1922 */       return this.params.pop();
/*      */     } catch (EmptyStackException e) {
/* 1924 */       this.log.warn("Empty stack (returning null)"); }
/* 1925 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void pushParams(Object object)
/*      */   {
/* 1940 */     if (this.log.isTraceEnabled()) {
/* 1941 */       this.log.trace("Pushing params");
/*      */     }
/* 1943 */     this.params.push(object);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SAXException createSAXException(String message, Exception e)
/*      */   {
/* 1955 */     if ((e != null) && ((e instanceof InvocationTargetException))) {
/* 1956 */       Throwable t = e.getCause();
/* 1957 */       if ((t instanceof ThreadDeath)) {
/* 1958 */         throw ((ThreadDeath)t);
/*      */       }
/* 1960 */       if ((t instanceof VirtualMachineError)) {
/* 1961 */         throw ((VirtualMachineError)t);
/*      */       }
/* 1963 */       if ((t instanceof Exception)) {
/* 1964 */         e = (Exception)t;
/*      */       }
/*      */     }
/* 1967 */     if (this.locator != null) {
/* 1968 */       String error = "Error at (" + this.locator.getLineNumber() + ", " + this.locator.getColumnNumber() + ") : " + message;
/*      */       
/* 1970 */       if (e != null) {
/* 1971 */         return new SAXParseException(error, this.locator, e);
/*      */       }
/* 1973 */       return new SAXParseException(error, this.locator);
/*      */     }
/*      */     
/* 1976 */     this.log.error("No Locator!");
/* 1977 */     if (e != null) {
/* 1978 */       return new SAXException(message, e);
/*      */     }
/* 1980 */     return new SAXException(message);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SAXException createSAXException(Exception e)
/*      */   {
/* 1991 */     if ((e instanceof InvocationTargetException)) {
/* 1992 */       Throwable t = e.getCause();
/* 1993 */       if ((t instanceof ThreadDeath)) {
/* 1994 */         throw ((ThreadDeath)t);
/*      */       }
/* 1996 */       if ((t instanceof VirtualMachineError)) {
/* 1997 */         throw ((VirtualMachineError)t);
/*      */       }
/* 1999 */       if ((t instanceof Exception)) {
/* 2000 */         e = (Exception)t;
/*      */       }
/*      */     }
/* 2003 */     return createSAXException(e.getMessage(), e);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SAXException createSAXException(String message)
/*      */   {
/* 2013 */     return createSAXException(message, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Attributes updateAttributes(Attributes list)
/*      */   {
/* 2027 */     if (list.getLength() == 0) {
/* 2028 */       return list;
/*      */     }
/*      */     
/* 2031 */     AttributesImpl newAttrs = new AttributesImpl(list);
/* 2032 */     int nAttributes = newAttrs.getLength();
/* 2033 */     for (int i = 0; i < nAttributes; i++) {
/* 2034 */       String value = newAttrs.getValue(i);
/*      */       try {
/* 2036 */         String newValue = IntrospectionUtils.replaceProperties(value, null, this.source);
/* 2037 */         if (value != newValue) {
/* 2038 */           newAttrs.setValue(i, newValue);
/*      */         }
/*      */       } catch (Exception e) {
/* 2041 */         this.log.warn(sm.getString("digester.failedToUpdateAttributes", new Object[] { newAttrs.getLocalName(i), value }), e);
/*      */       }
/*      */     }
/*      */     
/* 2045 */     return newAttrs;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private StringBuilder updateBodyText(StringBuilder bodyText)
/*      */   {
/* 2056 */     String in = bodyText.toString();
/*      */     try
/*      */     {
/* 2059 */       out = IntrospectionUtils.replaceProperties(in, null, this.source);
/*      */     } catch (Exception e) { String out;
/* 2061 */       return bodyText;
/*      */     }
/*      */     String out;
/* 2064 */     if (out == in)
/*      */     {
/*      */ 
/* 2067 */       return bodyText;
/*      */     }
/* 2069 */     return new StringBuilder(out);
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\digester\Digester.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */