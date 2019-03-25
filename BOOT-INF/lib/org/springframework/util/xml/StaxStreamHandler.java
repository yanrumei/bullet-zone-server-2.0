/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import org.springframework.util.Assert;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
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
/*     */ class StaxStreamHandler
/*     */   extends AbstractStaxHandler
/*     */ {
/*     */   private final XMLStreamWriter streamWriter;
/*     */   
/*     */   public StaxStreamHandler(XMLStreamWriter streamWriter)
/*     */   {
/*  45 */     Assert.notNull(streamWriter, "XMLStreamWriter must not be null");
/*  46 */     this.streamWriter = streamWriter;
/*     */   }
/*     */   
/*     */   protected void startDocumentInternal()
/*     */     throws XMLStreamException
/*     */   {
/*  52 */     this.streamWriter.writeStartDocument();
/*     */   }
/*     */   
/*     */   protected void endDocumentInternal() throws XMLStreamException
/*     */   {
/*  57 */     this.streamWriter.writeEndDocument();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void startElementInternal(QName name, Attributes attributes, Map<String, String> namespaceMapping)
/*     */     throws XMLStreamException
/*     */   {
/*  64 */     this.streamWriter.writeStartElement(name.getPrefix(), name.getLocalPart(), name.getNamespaceURI());
/*     */     
/*  66 */     for (Map.Entry<String, String> entry : namespaceMapping.entrySet()) {
/*  67 */       String prefix = (String)entry.getKey();
/*  68 */       String namespaceUri = (String)entry.getValue();
/*  69 */       this.streamWriter.writeNamespace(prefix, namespaceUri);
/*  70 */       if ("".equals(prefix)) {
/*  71 */         this.streamWriter.setDefaultNamespace(namespaceUri);
/*     */       }
/*     */       else {
/*  74 */         this.streamWriter.setPrefix(prefix, namespaceUri);
/*     */       }
/*     */     }
/*  77 */     for (int i = 0; i < attributes.getLength(); i++) {
/*  78 */       QName attrName = toQName(attributes.getURI(i), attributes.getQName(i));
/*  79 */       if (!isNamespaceDeclaration(attrName)) {
/*  80 */         this.streamWriter.writeAttribute(attrName.getPrefix(), attrName.getNamespaceURI(), attrName
/*  81 */           .getLocalPart(), attributes.getValue(i));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void endElementInternal(QName name, Map<String, String> namespaceMapping) throws XMLStreamException
/*     */   {
/*  88 */     this.streamWriter.writeEndElement();
/*     */   }
/*     */   
/*     */   protected void charactersInternal(String data) throws XMLStreamException
/*     */   {
/*  93 */     this.streamWriter.writeCharacters(data);
/*     */   }
/*     */   
/*     */   protected void cDataInternal(String data) throws XMLStreamException
/*     */   {
/*  98 */     this.streamWriter.writeCData(data);
/*     */   }
/*     */   
/*     */   protected void ignorableWhitespaceInternal(String data) throws XMLStreamException
/*     */   {
/* 103 */     this.streamWriter.writeCharacters(data);
/*     */   }
/*     */   
/*     */   protected void processingInstructionInternal(String target, String data) throws XMLStreamException
/*     */   {
/* 108 */     this.streamWriter.writeProcessingInstruction(target, data);
/*     */   }
/*     */   
/*     */   protected void dtdInternal(String dtd) throws XMLStreamException
/*     */   {
/* 113 */     this.streamWriter.writeDTD(dtd);
/*     */   }
/*     */   
/*     */   protected void commentInternal(String comment) throws XMLStreamException
/*     */   {
/* 118 */     this.streamWriter.writeComment(comment);
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {}
/*     */   
/*     */   public void startEntity(String name)
/*     */     throws SAXException
/*     */   {}
/*     */   
/*     */   public void endEntity(String name)
/*     */     throws SAXException
/*     */   {}
/*     */   
/*     */   protected void skippedEntityInternal(String name)
/*     */     throws XMLStreamException
/*     */   {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\xml\StaxStreamHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */