/*     */ package org.hibernate.validator.internal.xml;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLInputFactory;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Attribute;
/*     */ import javax.xml.stream.events.StartElement;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ import javax.xml.validation.Schema;
/*     */ import javax.xml.validation.SchemaFactory;
/*     */ import org.hibernate.validator.internal.util.Contracts;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.internal.util.logging.Messages;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetResource;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.NewSchema;
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
/*     */ public class XmlParserHelper
/*     */ {
/*  44 */   private static final Log log = ;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int NUMBER_OF_SCHEMAS = 4;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String DEFAULT_VERSION = "1.0";
/*     */   
/*     */ 
/*  55 */   private final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
/*     */   
/*  57 */   private static final ConcurrentMap<String, Schema> schemaCache = new ConcurrentHashMap(4);
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
/*     */   public String getSchemaVersion(String resourceName, XMLEventReader xmlEventReader)
/*     */   {
/*  76 */     Contracts.assertNotNull(xmlEventReader, Messages.MESSAGES.parameterMustNotBeNull("xmlEventReader"));
/*     */     try {
/*  78 */       StartElement rootElement = getRootElement(xmlEventReader);
/*     */       
/*  80 */       return getVersionValue(rootElement);
/*     */     }
/*     */     catch (XMLStreamException e) {
/*  83 */       throw log.getUnableToDetermineSchemaVersionException(resourceName, e);
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized XMLEventReader createXmlEventReader(String resourceName, InputStream xmlStream) {
/*     */     try {
/*  89 */       return this.xmlInputFactory.createXMLEventReader(xmlStream);
/*     */     }
/*     */     catch (Exception e) {
/*  92 */       throw log.getUnableToCreateXMLEventReader(resourceName, e);
/*     */     }
/*     */   }
/*     */   
/*     */   private String getVersionValue(StartElement startElement) {
/*  97 */     if (startElement == null) {
/*  98 */       return null;
/*     */     }
/*     */     
/* 101 */     Attribute versionAttribute = startElement.getAttributeByName(new QName("version"));
/* 102 */     return versionAttribute != null ? versionAttribute.getValue() : "1.0";
/*     */   }
/*     */   
/*     */   private StartElement getRootElement(XMLEventReader xmlEventReader) throws XMLStreamException {
/* 106 */     XMLEvent event = xmlEventReader.peek();
/* 107 */     while ((event != null) && (!event.isStartElement())) {
/* 108 */       xmlEventReader.nextEvent();
/* 109 */       event = xmlEventReader.peek();
/*     */     }
/*     */     
/* 112 */     return event == null ? null : event.asStartElement();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Schema getSchema(String schemaResource)
/*     */   {
/* 124 */     Schema schema = (Schema)schemaCache.get(schemaResource);
/*     */     
/* 126 */     if (schema != null) {
/* 127 */       return schema;
/*     */     }
/*     */     
/* 130 */     schema = loadSchema(schemaResource);
/*     */     
/* 132 */     if (schema != null) {
/* 133 */       Schema previous = (Schema)schemaCache.putIfAbsent(schemaResource, schema);
/* 134 */       return previous != null ? previous : schema;
/*     */     }
/*     */     
/* 137 */     return null;
/*     */   }
/*     */   
/*     */   private Schema loadSchema(String schemaResource)
/*     */   {
/* 142 */     ClassLoader loader = (ClassLoader)run(GetClassLoader.fromClass(XmlParserHelper.class));
/*     */     
/* 144 */     URL schemaUrl = (URL)run(GetResource.action(loader, schemaResource));
/* 145 */     SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
/* 146 */     Schema schema = null;
/*     */     try {
/* 148 */       schema = (Schema)run(NewSchema.action(sf, schemaUrl));
/*     */     }
/*     */     catch (Exception e) {
/* 151 */       log.unableToCreateSchema(schemaResource, e.getMessage());
/*     */     }
/* 153 */     return schema;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private <T> T run(PrivilegedAction<T> action)
/*     */   {
/* 163 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */   }
/*     */   
/*     */   private <T> T run(PrivilegedExceptionAction<T> action) throws Exception {
/* 167 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\XmlParserHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */