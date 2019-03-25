/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLEventFactory;
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.stream.events.EndElement;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ import javax.xml.stream.events.StartElement;
/*     */ import org.springframework.util.Assert;
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
/*     */ class XMLEventStreamWriter
/*     */   implements XMLStreamWriter
/*     */ {
/*     */   private static final String DEFAULT_ENCODING = "UTF-8";
/*     */   private final XMLEventWriter eventWriter;
/*     */   private final XMLEventFactory eventFactory;
/*  50 */   private final List<EndElement> endElements = new ArrayList();
/*     */   
/*  52 */   private boolean emptyElement = false;
/*     */   
/*     */   public XMLEventStreamWriter(XMLEventWriter eventWriter, XMLEventFactory eventFactory)
/*     */   {
/*  56 */     Assert.notNull(eventWriter, "'eventWriter' must not be null");
/*  57 */     Assert.notNull(eventFactory, "'eventFactory' must not be null");
/*  58 */     this.eventWriter = eventWriter;
/*  59 */     this.eventFactory = eventFactory;
/*     */   }
/*     */   
/*     */   public void setNamespaceContext(NamespaceContext context)
/*     */     throws XMLStreamException
/*     */   {
/*  65 */     this.eventWriter.setNamespaceContext(context);
/*     */   }
/*     */   
/*     */   public NamespaceContext getNamespaceContext()
/*     */   {
/*  70 */     return this.eventWriter.getNamespaceContext();
/*     */   }
/*     */   
/*     */   public void setPrefix(String prefix, String uri) throws XMLStreamException
/*     */   {
/*  75 */     this.eventWriter.setPrefix(prefix, uri);
/*     */   }
/*     */   
/*     */   public String getPrefix(String uri) throws XMLStreamException
/*     */   {
/*  80 */     return this.eventWriter.getPrefix(uri);
/*     */   }
/*     */   
/*     */   public void setDefaultNamespace(String uri) throws XMLStreamException
/*     */   {
/*  85 */     this.eventWriter.setDefaultNamespace(uri);
/*     */   }
/*     */   
/*     */   public Object getProperty(String name) throws IllegalArgumentException
/*     */   {
/*  90 */     throw new IllegalArgumentException();
/*     */   }
/*     */   
/*     */   public void writeStartDocument()
/*     */     throws XMLStreamException
/*     */   {
/*  96 */     closeEmptyElementIfNecessary();
/*  97 */     this.eventWriter.add(this.eventFactory.createStartDocument());
/*     */   }
/*     */   
/*     */   public void writeStartDocument(String version) throws XMLStreamException
/*     */   {
/* 102 */     closeEmptyElementIfNecessary();
/* 103 */     this.eventWriter.add(this.eventFactory.createStartDocument("UTF-8", version));
/*     */   }
/*     */   
/*     */   public void writeStartDocument(String encoding, String version) throws XMLStreamException
/*     */   {
/* 108 */     closeEmptyElementIfNecessary();
/* 109 */     this.eventWriter.add(this.eventFactory.createStartDocument(encoding, version));
/*     */   }
/*     */   
/*     */   public void writeStartElement(String localName) throws XMLStreamException
/*     */   {
/* 114 */     closeEmptyElementIfNecessary();
/* 115 */     doWriteStartElement(this.eventFactory.createStartElement(new QName(localName), null, null));
/*     */   }
/*     */   
/*     */   public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException
/*     */   {
/* 120 */     closeEmptyElementIfNecessary();
/* 121 */     doWriteStartElement(this.eventFactory.createStartElement(new QName(namespaceURI, localName), null, null));
/*     */   }
/*     */   
/*     */   public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException
/*     */   {
/* 126 */     closeEmptyElementIfNecessary();
/* 127 */     doWriteStartElement(this.eventFactory.createStartElement(new QName(namespaceURI, localName, prefix), null, null));
/*     */   }
/*     */   
/*     */   private void doWriteStartElement(StartElement startElement) throws XMLStreamException {
/* 131 */     this.eventWriter.add(startElement);
/* 132 */     this.endElements.add(this.eventFactory.createEndElement(startElement.getName(), startElement.getNamespaces()));
/*     */   }
/*     */   
/*     */   public void writeEmptyElement(String localName) throws XMLStreamException
/*     */   {
/* 137 */     closeEmptyElementIfNecessary();
/* 138 */     writeStartElement(localName);
/* 139 */     this.emptyElement = true;
/*     */   }
/*     */   
/*     */   public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException
/*     */   {
/* 144 */     closeEmptyElementIfNecessary();
/* 145 */     writeStartElement(namespaceURI, localName);
/* 146 */     this.emptyElement = true;
/*     */   }
/*     */   
/*     */   public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException
/*     */   {
/* 151 */     closeEmptyElementIfNecessary();
/* 152 */     writeStartElement(prefix, localName, namespaceURI);
/* 153 */     this.emptyElement = true;
/*     */   }
/*     */   
/*     */   private void closeEmptyElementIfNecessary() throws XMLStreamException {
/* 157 */     if (this.emptyElement) {
/* 158 */       this.emptyElement = false;
/* 159 */       writeEndElement();
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeEndElement() throws XMLStreamException
/*     */   {
/* 165 */     closeEmptyElementIfNecessary();
/* 166 */     int last = this.endElements.size() - 1;
/* 167 */     EndElement lastEndElement = (EndElement)this.endElements.get(last);
/* 168 */     this.eventWriter.add(lastEndElement);
/* 169 */     this.endElements.remove(last);
/*     */   }
/*     */   
/*     */   public void writeAttribute(String localName, String value) throws XMLStreamException
/*     */   {
/* 174 */     this.eventWriter.add(this.eventFactory.createAttribute(localName, value));
/*     */   }
/*     */   
/*     */   public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException
/*     */   {
/* 179 */     this.eventWriter.add(this.eventFactory.createAttribute(new QName(namespaceURI, localName), value));
/*     */   }
/*     */   
/*     */ 
/*     */   public void writeAttribute(String prefix, String namespaceURI, String localName, String value)
/*     */     throws XMLStreamException
/*     */   {
/* 186 */     this.eventWriter.add(this.eventFactory.createAttribute(prefix, namespaceURI, localName, value));
/*     */   }
/*     */   
/*     */   public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException
/*     */   {
/* 191 */     doWriteNamespace(this.eventFactory.createNamespace(prefix, namespaceURI));
/*     */   }
/*     */   
/*     */   public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException
/*     */   {
/* 196 */     doWriteNamespace(this.eventFactory.createNamespace(namespaceURI));
/*     */   }
/*     */   
/*     */   private void doWriteNamespace(Namespace namespace) throws XMLStreamException
/*     */   {
/* 201 */     int last = this.endElements.size() - 1;
/* 202 */     EndElement oldEndElement = (EndElement)this.endElements.get(last);
/* 203 */     Iterator oldNamespaces = oldEndElement.getNamespaces();
/* 204 */     List<Namespace> newNamespaces = new ArrayList();
/* 205 */     while (oldNamespaces.hasNext()) {
/* 206 */       Namespace oldNamespace = (Namespace)oldNamespaces.next();
/* 207 */       newNamespaces.add(oldNamespace);
/*     */     }
/* 209 */     newNamespaces.add(namespace);
/* 210 */     EndElement newEndElement = this.eventFactory.createEndElement(oldEndElement.getName(), newNamespaces.iterator());
/* 211 */     this.eventWriter.add(namespace);
/* 212 */     this.endElements.set(last, newEndElement);
/*     */   }
/*     */   
/*     */   public void writeCharacters(String text) throws XMLStreamException
/*     */   {
/* 217 */     closeEmptyElementIfNecessary();
/* 218 */     this.eventWriter.add(this.eventFactory.createCharacters(text));
/*     */   }
/*     */   
/*     */   public void writeCharacters(char[] text, int start, int len) throws XMLStreamException
/*     */   {
/* 223 */     closeEmptyElementIfNecessary();
/* 224 */     this.eventWriter.add(this.eventFactory.createCharacters(new String(text, start, len)));
/*     */   }
/*     */   
/*     */   public void writeCData(String data) throws XMLStreamException
/*     */   {
/* 229 */     closeEmptyElementIfNecessary();
/* 230 */     this.eventWriter.add(this.eventFactory.createCData(data));
/*     */   }
/*     */   
/*     */   public void writeComment(String data) throws XMLStreamException
/*     */   {
/* 235 */     closeEmptyElementIfNecessary();
/* 236 */     this.eventWriter.add(this.eventFactory.createComment(data));
/*     */   }
/*     */   
/*     */   public void writeProcessingInstruction(String target) throws XMLStreamException
/*     */   {
/* 241 */     closeEmptyElementIfNecessary();
/* 242 */     this.eventWriter.add(this.eventFactory.createProcessingInstruction(target, ""));
/*     */   }
/*     */   
/*     */   public void writeProcessingInstruction(String target, String data) throws XMLStreamException
/*     */   {
/* 247 */     closeEmptyElementIfNecessary();
/* 248 */     this.eventWriter.add(this.eventFactory.createProcessingInstruction(target, data));
/*     */   }
/*     */   
/*     */   public void writeDTD(String dtd) throws XMLStreamException
/*     */   {
/* 253 */     closeEmptyElementIfNecessary();
/* 254 */     this.eventWriter.add(this.eventFactory.createDTD(dtd));
/*     */   }
/*     */   
/*     */   public void writeEntityRef(String name) throws XMLStreamException
/*     */   {
/* 259 */     closeEmptyElementIfNecessary();
/* 260 */     this.eventWriter.add(this.eventFactory.createEntityReference(name, null));
/*     */   }
/*     */   
/*     */   public void writeEndDocument() throws XMLStreamException
/*     */   {
/* 265 */     closeEmptyElementIfNecessary();
/* 266 */     this.eventWriter.add(this.eventFactory.createEndDocument());
/*     */   }
/*     */   
/*     */   public void flush() throws XMLStreamException
/*     */   {
/* 271 */     this.eventWriter.flush();
/*     */   }
/*     */   
/*     */   public void close() throws XMLStreamException
/*     */   {
/* 276 */     closeEmptyElementIfNecessary();
/* 277 */     this.eventWriter.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\xml\XMLEventStreamWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */