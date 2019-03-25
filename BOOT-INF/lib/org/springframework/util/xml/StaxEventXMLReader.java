/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Attribute;
/*     */ import javax.xml.stream.events.Characters;
/*     */ import javax.xml.stream.events.Comment;
/*     */ import javax.xml.stream.events.DTD;
/*     */ import javax.xml.stream.events.EndElement;
/*     */ import javax.xml.stream.events.EntityDeclaration;
/*     */ import javax.xml.stream.events.EntityReference;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ import javax.xml.stream.events.NotationDeclaration;
/*     */ import javax.xml.stream.events.ProcessingInstruction;
/*     */ import javax.xml.stream.events.StartDocument;
/*     */ import javax.xml.stream.events.StartElement;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.DTDHandler;
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
/*     */ class StaxEventXMLReader
/*     */   extends AbstractStaxXMLReader
/*     */ {
/*     */   private static final String DEFAULT_XML_VERSION = "1.0";
/*     */   private final XMLEventReader reader;
/*  66 */   private String xmlVersion = "1.0";
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
/*     */   StaxEventXMLReader(XMLEventReader reader)
/*     */   {
/*  79 */     Assert.notNull(reader, "'reader' must not be null");
/*     */     try {
/*  81 */       XMLEvent event = reader.peek();
/*  82 */       if ((event != null) && (!event.isStartDocument()) && (!event.isStartElement())) {
/*  83 */         throw new IllegalStateException("XMLEventReader not at start of document or element");
/*     */       }
/*     */     }
/*     */     catch (XMLStreamException ex) {
/*  87 */       throw new IllegalStateException("Could not read first element: " + ex.getMessage());
/*     */     }
/*  89 */     this.reader = reader;
/*     */   }
/*     */   
/*     */   protected void parseInternal()
/*     */     throws SAXException, XMLStreamException
/*     */   {
/*  95 */     boolean documentStarted = false;
/*  96 */     boolean documentEnded = false;
/*  97 */     int elementDepth = 0;
/*  98 */     while ((this.reader.hasNext()) && (elementDepth >= 0)) {
/*  99 */       XMLEvent event = this.reader.nextEvent();
/* 100 */       if ((!event.isStartDocument()) && (!event.isEndDocument()) && (!documentStarted)) {
/* 101 */         handleStartDocument(event);
/* 102 */         documentStarted = true;
/*     */       }
/* 104 */       switch (event.getEventType()) {
/*     */       case 7: 
/* 106 */         handleStartDocument(event);
/* 107 */         documentStarted = true;
/* 108 */         break;
/*     */       case 1: 
/* 110 */         elementDepth++;
/* 111 */         handleStartElement(event.asStartElement());
/* 112 */         break;
/*     */       case 2: 
/* 114 */         elementDepth--;
/* 115 */         if (elementDepth >= 0) {
/* 116 */           handleEndElement(event.asEndElement());
/*     */         }
/*     */         break;
/*     */       case 3: 
/* 120 */         handleProcessingInstruction((ProcessingInstruction)event);
/* 121 */         break;
/*     */       case 4: 
/*     */       case 6: 
/*     */       case 12: 
/* 125 */         handleCharacters(event.asCharacters());
/* 126 */         break;
/*     */       case 8: 
/* 128 */         handleEndDocument();
/* 129 */         documentEnded = true;
/* 130 */         break;
/*     */       case 14: 
/* 132 */         handleNotationDeclaration((NotationDeclaration)event);
/* 133 */         break;
/*     */       case 15: 
/* 135 */         handleEntityDeclaration((EntityDeclaration)event);
/* 136 */         break;
/*     */       case 5: 
/* 138 */         handleComment((Comment)event);
/* 139 */         break;
/*     */       case 11: 
/* 141 */         handleDtd((DTD)event);
/* 142 */         break;
/*     */       case 9: 
/* 144 */         handleEntityReference((EntityReference)event);
/*     */       }
/*     */       
/*     */     }
/* 148 */     if ((documentStarted) && (!documentEnded)) {
/* 149 */       handleEndDocument();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleStartDocument(XMLEvent event) throws SAXException
/*     */   {
/* 155 */     if (event.isStartDocument()) {
/* 156 */       StartDocument startDocument = (StartDocument)event;
/* 157 */       String xmlVersion = startDocument.getVersion();
/* 158 */       if (StringUtils.hasLength(xmlVersion)) {
/* 159 */         this.xmlVersion = xmlVersion;
/*     */       }
/* 161 */       if (startDocument.encodingSet()) {
/* 162 */         this.encoding = startDocument.getCharacterEncodingScheme();
/*     */       }
/*     */     }
/* 165 */     if (getContentHandler() != null) {
/* 166 */       final Location location = event.getLocation();
/* 167 */       getContentHandler().setDocumentLocator(new Locator2()
/*     */       {
/*     */         public int getColumnNumber() {
/* 170 */           return location != null ? location.getColumnNumber() : -1;
/*     */         }
/*     */         
/*     */         public int getLineNumber() {
/* 174 */           return location != null ? location.getLineNumber() : -1;
/*     */         }
/*     */         
/*     */         public String getPublicId() {
/* 178 */           return location != null ? location.getPublicId() : null;
/*     */         }
/*     */         
/*     */         public String getSystemId() {
/* 182 */           return location != null ? location.getSystemId() : null;
/*     */         }
/*     */         
/*     */         public String getXMLVersion() {
/* 186 */           return StaxEventXMLReader.this.xmlVersion;
/*     */         }
/*     */         
/*     */         public String getEncoding() {
/* 190 */           return StaxEventXMLReader.this.encoding;
/*     */         }
/* 192 */       });
/* 193 */       getContentHandler().startDocument();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleStartElement(StartElement startElement) throws SAXException {
/* 198 */     if (getContentHandler() != null) {
/* 199 */       QName qName = startElement.getName();
/* 200 */       if (hasNamespacesFeature()) {
/* 201 */         for (Iterator i = startElement.getNamespaces(); i.hasNext();) {
/* 202 */           Namespace namespace = (Namespace)i.next();
/* 203 */           startPrefixMapping(namespace.getPrefix(), namespace.getNamespaceURI());
/*     */         }
/* 205 */         for (Iterator i = startElement.getAttributes(); i.hasNext();) {
/* 206 */           Attribute attribute = (Attribute)i.next();
/* 207 */           QName attributeName = attribute.getName();
/* 208 */           startPrefixMapping(attributeName.getPrefix(), attributeName.getNamespaceURI());
/*     */         }
/*     */         
/* 211 */         getContentHandler().startElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName), 
/* 212 */           getAttributes(startElement));
/*     */       }
/*     */       else {
/* 215 */         getContentHandler().startElement("", "", toQualifiedName(qName), getAttributes(startElement));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleCharacters(Characters characters) throws SAXException {
/* 221 */     char[] data = characters.getData().toCharArray();
/* 222 */     if ((getContentHandler() != null) && (characters.isIgnorableWhiteSpace())) {
/* 223 */       getContentHandler().ignorableWhitespace(data, 0, data.length);
/* 224 */       return;
/*     */     }
/* 226 */     if ((characters.isCData()) && (getLexicalHandler() != null)) {
/* 227 */       getLexicalHandler().startCDATA();
/*     */     }
/* 229 */     if (getContentHandler() != null) {
/* 230 */       getContentHandler().characters(data, 0, data.length);
/*     */     }
/* 232 */     if ((characters.isCData()) && (getLexicalHandler() != null)) {
/* 233 */       getLexicalHandler().endCDATA();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleEndElement(EndElement endElement) throws SAXException {
/* 238 */     if (getContentHandler() != null) {
/* 239 */       QName qName = endElement.getName();
/* 240 */       Iterator i; if (hasNamespacesFeature()) {
/* 241 */         getContentHandler().endElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName));
/* 242 */         for (i = endElement.getNamespaces(); i.hasNext();) {
/* 243 */           Namespace namespace = (Namespace)i.next();
/* 244 */           endPrefixMapping(namespace.getPrefix());
/*     */         }
/*     */       }
/*     */       else {
/* 248 */         getContentHandler().endElement("", "", toQualifiedName(qName));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleEndDocument() throws SAXException
/*     */   {
/* 255 */     if (getContentHandler() != null) {
/* 256 */       getContentHandler().endDocument();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleNotationDeclaration(NotationDeclaration declaration) throws SAXException {
/* 261 */     if (getDTDHandler() != null) {
/* 262 */       getDTDHandler().notationDecl(declaration.getName(), declaration.getPublicId(), declaration.getSystemId());
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleEntityDeclaration(EntityDeclaration entityDeclaration) throws SAXException {
/* 267 */     if (getDTDHandler() != null) {
/* 268 */       getDTDHandler().unparsedEntityDecl(entityDeclaration.getName(), entityDeclaration.getPublicId(), entityDeclaration
/* 269 */         .getSystemId(), entityDeclaration.getNotationName());
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleProcessingInstruction(ProcessingInstruction pi) throws SAXException {
/* 274 */     if (getContentHandler() != null) {
/* 275 */       getContentHandler().processingInstruction(pi.getTarget(), pi.getData());
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleComment(Comment comment) throws SAXException {
/* 280 */     if (getLexicalHandler() != null) {
/* 281 */       char[] ch = comment.getText().toCharArray();
/* 282 */       getLexicalHandler().comment(ch, 0, ch.length);
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleDtd(DTD dtd) throws SAXException {
/* 287 */     if (getLexicalHandler() != null) {
/* 288 */       Location location = dtd.getLocation();
/* 289 */       getLexicalHandler().startDTD(null, location.getPublicId(), location.getSystemId());
/*     */     }
/* 291 */     if (getLexicalHandler() != null) {
/* 292 */       getLexicalHandler().endDTD();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleEntityReference(EntityReference reference) throws SAXException
/*     */   {
/* 298 */     if (getLexicalHandler() != null) {
/* 299 */       getLexicalHandler().startEntity(reference.getName());
/*     */     }
/* 301 */     if (getLexicalHandler() != null) {
/* 302 */       getLexicalHandler().endEntity(reference.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   private Attributes getAttributes(StartElement event)
/*     */   {
/* 308 */     AttributesImpl attributes = new AttributesImpl();
/* 309 */     for (Iterator i = event.getAttributes(); i.hasNext();) {
/* 310 */       Attribute attribute = (Attribute)i.next();
/* 311 */       QName qName = attribute.getName();
/* 312 */       String namespace = qName.getNamespaceURI();
/* 313 */       if ((namespace == null) || (!hasNamespacesFeature())) {
/* 314 */         namespace = "";
/*     */       }
/* 316 */       String type = attribute.getDTDType();
/* 317 */       if (type == null) {
/* 318 */         type = "CDATA";
/*     */       }
/* 320 */       attributes.addAttribute(namespace, qName.getLocalPart(), toQualifiedName(qName), type, attribute.getValue()); }
/*     */     Iterator i;
/* 322 */     if (hasNamespacePrefixesFeature()) {
/* 323 */       for (i = event.getNamespaces(); i.hasNext();) {
/* 324 */         Namespace namespace = (Namespace)i.next();
/* 325 */         String prefix = namespace.getPrefix();
/* 326 */         String namespaceUri = namespace.getNamespaceURI();
/*     */         String qName;
/* 328 */         String qName; if (StringUtils.hasLength(prefix)) {
/* 329 */           qName = "xmlns:" + prefix;
/*     */         }
/*     */         else {
/* 332 */           qName = "xmlns";
/*     */         }
/* 334 */         attributes.addAttribute("", "", qName, "CDATA", namespaceUri);
/*     */       }
/*     */     }
/*     */     
/* 338 */     return attributes;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\xml\StaxEventXMLReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */