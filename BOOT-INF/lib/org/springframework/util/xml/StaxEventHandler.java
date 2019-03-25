/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLEventFactory;
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Attribute;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
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
/*     */ class StaxEventHandler
/*     */   extends AbstractStaxHandler
/*     */ {
/*     */   private final XMLEventFactory eventFactory;
/*     */   private final XMLEventWriter eventWriter;
/*     */   
/*     */   public StaxEventHandler(XMLEventWriter eventWriter)
/*     */   {
/*  54 */     this.eventFactory = XMLEventFactory.newInstance();
/*  55 */     this.eventWriter = eventWriter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StaxEventHandler(XMLEventWriter eventWriter, XMLEventFactory factory)
/*     */   {
/*  65 */     this.eventFactory = factory;
/*  66 */     this.eventWriter = eventWriter;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setDocumentLocator(Locator locator)
/*     */   {
/*  72 */     if (locator != null) {
/*  73 */       this.eventFactory.setLocation(new LocatorLocationAdapter(locator));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void startDocumentInternal() throws XMLStreamException
/*     */   {
/*  79 */     this.eventWriter.add(this.eventFactory.createStartDocument());
/*     */   }
/*     */   
/*     */   protected void endDocumentInternal() throws XMLStreamException
/*     */   {
/*  84 */     this.eventWriter.add(this.eventFactory.createEndDocument());
/*     */   }
/*     */   
/*     */ 
/*     */   protected void startElementInternal(QName name, Attributes atts, Map<String, String> namespaceMapping)
/*     */     throws XMLStreamException
/*     */   {
/*  91 */     List<Attribute> attributes = getAttributes(atts);
/*  92 */     List<Namespace> namespaces = getNamespaces(namespaceMapping);
/*  93 */     this.eventWriter.add(this.eventFactory
/*  94 */       .createStartElement(name, attributes.iterator(), namespaces.iterator()));
/*     */   }
/*     */   
/*     */   private List<Namespace> getNamespaces(Map<String, String> namespaceMapping)
/*     */   {
/*  99 */     List<Namespace> result = new ArrayList();
/* 100 */     for (Map.Entry<String, String> entry : namespaceMapping.entrySet()) {
/* 101 */       String prefix = (String)entry.getKey();
/* 102 */       String namespaceUri = (String)entry.getValue();
/* 103 */       result.add(this.eventFactory.createNamespace(prefix, namespaceUri));
/*     */     }
/* 105 */     return result;
/*     */   }
/*     */   
/*     */   private List<Attribute> getAttributes(Attributes attributes) {
/* 109 */     List<Attribute> result = new ArrayList();
/* 110 */     for (int i = 0; i < attributes.getLength(); i++) {
/* 111 */       QName attrName = toQName(attributes.getURI(i), attributes.getQName(i));
/* 112 */       if (!isNamespaceDeclaration(attrName)) {
/* 113 */         result.add(this.eventFactory.createAttribute(attrName, attributes.getValue(i)));
/*     */       }
/*     */     }
/* 116 */     return result;
/*     */   }
/*     */   
/*     */   protected void endElementInternal(QName name, Map<String, String> namespaceMapping) throws XMLStreamException
/*     */   {
/* 121 */     List<Namespace> namespaces = getNamespaces(namespaceMapping);
/* 122 */     this.eventWriter.add(this.eventFactory.createEndElement(name, namespaces.iterator()));
/*     */   }
/*     */   
/*     */   protected void charactersInternal(String data) throws XMLStreamException
/*     */   {
/* 127 */     this.eventWriter.add(this.eventFactory.createCharacters(data));
/*     */   }
/*     */   
/*     */   protected void cDataInternal(String data) throws XMLStreamException
/*     */   {
/* 132 */     this.eventWriter.add(this.eventFactory.createCData(data));
/*     */   }
/*     */   
/*     */   protected void ignorableWhitespaceInternal(String data) throws XMLStreamException
/*     */   {
/* 137 */     this.eventWriter.add(this.eventFactory.createIgnorableSpace(data));
/*     */   }
/*     */   
/*     */   protected void processingInstructionInternal(String target, String data) throws XMLStreamException
/*     */   {
/* 142 */     this.eventWriter.add(this.eventFactory.createProcessingInstruction(target, data));
/*     */   }
/*     */   
/*     */   protected void dtdInternal(String dtd) throws XMLStreamException
/*     */   {
/* 147 */     this.eventWriter.add(this.eventFactory.createDTD(dtd));
/*     */   }
/*     */   
/*     */   protected void commentInternal(String comment) throws XMLStreamException
/*     */   {
/* 152 */     this.eventWriter.add(this.eventFactory.createComment(comment));
/*     */   }
/*     */   
/*     */ 
/*     */   protected void skippedEntityInternal(String name)
/*     */     throws XMLStreamException
/*     */   {}
/*     */   
/*     */   private static final class LocatorLocationAdapter
/*     */     implements Location
/*     */   {
/*     */     private final Locator locator;
/*     */     
/*     */     public LocatorLocationAdapter(Locator locator)
/*     */     {
/* 167 */       this.locator = locator;
/*     */     }
/*     */     
/*     */     public int getLineNumber()
/*     */     {
/* 172 */       return this.locator.getLineNumber();
/*     */     }
/*     */     
/*     */     public int getColumnNumber()
/*     */     {
/* 177 */       return this.locator.getColumnNumber();
/*     */     }
/*     */     
/*     */     public int getCharacterOffset()
/*     */     {
/* 182 */       return -1;
/*     */     }
/*     */     
/*     */     public String getPublicId()
/*     */     {
/* 187 */       return this.locator.getPublicId();
/*     */     }
/*     */     
/*     */     public String getSystemId()
/*     */     {
/* 192 */       return this.locator.getSystemId();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\xml\StaxEventHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */