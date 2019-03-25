/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ import org.xml.sax.ext.Locator2;
/*     */ import org.xml.sax.helpers.AttributesImpl;
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
/*     */ class StaxStreamXMLReader
/*     */   extends AbstractStaxXMLReader
/*     */ {
/*     */   private static final String DEFAULT_XML_VERSION = "1.0";
/*     */   private final XMLStreamReader reader;
/*  51 */   private String xmlVersion = "1.0";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String encoding;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   StaxStreamXMLReader(XMLStreamReader reader)
/*     */   {
/*  64 */     Assert.notNull(reader, "'reader' must not be null");
/*  65 */     int event = reader.getEventType();
/*  66 */     if ((event != 7) && (event != 1)) {
/*  67 */       throw new IllegalStateException("XMLEventReader not at start of document or element");
/*     */     }
/*  69 */     this.reader = reader;
/*     */   }
/*     */   
/*     */   protected void parseInternal()
/*     */     throws SAXException, XMLStreamException
/*     */   {
/*  75 */     boolean documentStarted = false;
/*  76 */     boolean documentEnded = false;
/*  77 */     int elementDepth = 0;
/*  78 */     int eventType = this.reader.getEventType();
/*     */     for (;;) {
/*  80 */       if ((eventType != 7) && (eventType != 8) && (!documentStarted))
/*     */       {
/*  82 */         handleStartDocument();
/*  83 */         documentStarted = true;
/*     */       }
/*  85 */       switch (eventType) {
/*     */       case 1: 
/*  87 */         elementDepth++;
/*  88 */         handleStartElement();
/*  89 */         break;
/*     */       case 2: 
/*  91 */         elementDepth--;
/*  92 */         if (elementDepth >= 0) {
/*  93 */           handleEndElement();
/*     */         }
/*     */         break;
/*     */       case 3: 
/*  97 */         handleProcessingInstruction();
/*  98 */         break;
/*     */       case 4: 
/*     */       case 6: 
/*     */       case 12: 
/* 102 */         handleCharacters();
/* 103 */         break;
/*     */       case 7: 
/* 105 */         handleStartDocument();
/* 106 */         documentStarted = true;
/* 107 */         break;
/*     */       case 8: 
/* 109 */         handleEndDocument();
/* 110 */         documentEnded = true;
/* 111 */         break;
/*     */       case 5: 
/* 113 */         handleComment();
/* 114 */         break;
/*     */       case 11: 
/* 116 */         handleDtd();
/* 117 */         break;
/*     */       case 9: 
/* 119 */         handleEntityReference();
/*     */       }
/*     */       
/* 122 */       if ((!this.reader.hasNext()) || (elementDepth < 0)) break;
/* 123 */       eventType = this.reader.next();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 129 */     if (!documentEnded) {
/* 130 */       handleEndDocument();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleStartDocument() throws SAXException {
/* 135 */     if (7 == this.reader.getEventType()) {
/* 136 */       String xmlVersion = this.reader.getVersion();
/* 137 */       if (StringUtils.hasLength(xmlVersion)) {
/* 138 */         this.xmlVersion = xmlVersion;
/*     */       }
/* 140 */       this.encoding = this.reader.getCharacterEncodingScheme();
/*     */     }
/* 142 */     if (getContentHandler() != null) {
/* 143 */       final Location location = this.reader.getLocation();
/* 144 */       getContentHandler().setDocumentLocator(new Locator2()
/*     */       {
/*     */         public int getColumnNumber() {
/* 147 */           return location != null ? location.getColumnNumber() : -1;
/*     */         }
/*     */         
/*     */         public int getLineNumber() {
/* 151 */           return location != null ? location.getLineNumber() : -1;
/*     */         }
/*     */         
/*     */         public String getPublicId() {
/* 155 */           return location != null ? location.getPublicId() : null;
/*     */         }
/*     */         
/*     */         public String getSystemId() {
/* 159 */           return location != null ? location.getSystemId() : null;
/*     */         }
/*     */         
/*     */         public String getXMLVersion() {
/* 163 */           return StaxStreamXMLReader.this.xmlVersion;
/*     */         }
/*     */         
/*     */         public String getEncoding() {
/* 167 */           return StaxStreamXMLReader.this.encoding;
/*     */         }
/* 169 */       });
/* 170 */       getContentHandler().startDocument();
/* 171 */       if (this.reader.standaloneSet()) {
/* 172 */         setStandalone(this.reader.isStandalone());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleStartElement() throws SAXException {
/* 178 */     if (getContentHandler() != null) {
/* 179 */       QName qName = this.reader.getName();
/* 180 */       if (hasNamespacesFeature()) {
/* 181 */         for (int i = 0; i < this.reader.getNamespaceCount(); i++) {
/* 182 */           startPrefixMapping(this.reader.getNamespacePrefix(i), this.reader.getNamespaceURI(i));
/*     */         }
/* 184 */         for (int i = 0; i < this.reader.getAttributeCount(); i++) {
/* 185 */           String prefix = this.reader.getAttributePrefix(i);
/* 186 */           String namespace = this.reader.getAttributeNamespace(i);
/* 187 */           if (StringUtils.hasLength(namespace)) {
/* 188 */             startPrefixMapping(prefix, namespace);
/*     */           }
/*     */         }
/* 191 */         getContentHandler().startElement(qName.getNamespaceURI(), qName.getLocalPart(), 
/* 192 */           toQualifiedName(qName), getAttributes());
/*     */       }
/*     */       else {
/* 195 */         getContentHandler().startElement("", "", toQualifiedName(qName), getAttributes());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleEndElement() throws SAXException {
/* 201 */     if (getContentHandler() != null) {
/* 202 */       QName qName = this.reader.getName();
/* 203 */       if (hasNamespacesFeature()) {
/* 204 */         getContentHandler().endElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName));
/* 205 */         for (int i = 0; i < this.reader.getNamespaceCount(); i++) {
/* 206 */           String prefix = this.reader.getNamespacePrefix(i);
/* 207 */           if (prefix == null) {
/* 208 */             prefix = "";
/*     */           }
/* 210 */           endPrefixMapping(prefix);
/*     */         }
/*     */       }
/*     */       else {
/* 214 */         getContentHandler().endElement("", "", toQualifiedName(qName));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleCharacters() throws SAXException {
/* 220 */     if ((12 == this.reader.getEventType()) && (getLexicalHandler() != null)) {
/* 221 */       getLexicalHandler().startCDATA();
/*     */     }
/* 223 */     if (getContentHandler() != null) {
/* 224 */       getContentHandler().characters(this.reader.getTextCharacters(), this.reader
/* 225 */         .getTextStart(), this.reader.getTextLength());
/*     */     }
/* 227 */     if ((12 == this.reader.getEventType()) && (getLexicalHandler() != null)) {
/* 228 */       getLexicalHandler().endCDATA();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleComment() throws SAXException {
/* 233 */     if (getLexicalHandler() != null) {
/* 234 */       getLexicalHandler().comment(this.reader.getTextCharacters(), this.reader
/* 235 */         .getTextStart(), this.reader.getTextLength());
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleDtd() throws SAXException {
/* 240 */     if (getLexicalHandler() != null) {
/* 241 */       Location location = this.reader.getLocation();
/* 242 */       getLexicalHandler().startDTD(null, location.getPublicId(), location.getSystemId());
/*     */     }
/* 244 */     if (getLexicalHandler() != null) {
/* 245 */       getLexicalHandler().endDTD();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleEntityReference() throws SAXException {
/* 250 */     if (getLexicalHandler() != null) {
/* 251 */       getLexicalHandler().startEntity(this.reader.getLocalName());
/*     */     }
/* 253 */     if (getLexicalHandler() != null) {
/* 254 */       getLexicalHandler().endEntity(this.reader.getLocalName());
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleEndDocument() throws SAXException {
/* 259 */     if (getContentHandler() != null) {
/* 260 */       getContentHandler().endDocument();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleProcessingInstruction() throws SAXException {
/* 265 */     if (getContentHandler() != null) {
/* 266 */       getContentHandler().processingInstruction(this.reader.getPITarget(), this.reader.getPIData());
/*     */     }
/*     */   }
/*     */   
/*     */   private Attributes getAttributes() {
/* 271 */     AttributesImpl attributes = new AttributesImpl();
/* 272 */     for (int i = 0; i < this.reader.getAttributeCount(); i++) {
/* 273 */       String namespace = this.reader.getAttributeNamespace(i);
/* 274 */       if ((namespace == null) || (!hasNamespacesFeature())) {
/* 275 */         namespace = "";
/*     */       }
/* 277 */       String type = this.reader.getAttributeType(i);
/* 278 */       if (type == null) {
/* 279 */         type = "CDATA";
/*     */       }
/* 281 */       attributes.addAttribute(namespace, this.reader.getAttributeLocalName(i), 
/* 282 */         toQualifiedName(this.reader.getAttributeName(i)), type, this.reader.getAttributeValue(i));
/*     */     }
/* 284 */     if (hasNamespacePrefixesFeature()) {
/* 285 */       for (int i = 0; i < this.reader.getNamespaceCount(); i++) {
/* 286 */         String prefix = this.reader.getNamespacePrefix(i);
/* 287 */         String namespaceUri = this.reader.getNamespaceURI(i);
/*     */         String qName;
/* 289 */         String qName; if (StringUtils.hasLength(prefix)) {
/* 290 */           qName = "xmlns:" + prefix;
/*     */         }
/*     */         else {
/* 293 */           qName = "xmlns";
/*     */         }
/* 295 */         attributes.addAttribute("", "", qName, "CDATA", namespaceUri);
/*     */       }
/*     */     }
/*     */     
/* 299 */     return attributes;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\xml\StaxStreamXMLReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */