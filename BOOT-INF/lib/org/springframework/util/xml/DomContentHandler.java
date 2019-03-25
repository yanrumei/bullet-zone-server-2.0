/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.util.Assert;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.w3c.dom.Text;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
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
/*     */ class DomContentHandler
/*     */   implements ContentHandler
/*     */ {
/*     */   private final Document document;
/*  45 */   private final List<Element> elements = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */   private final Node node;
/*     */   
/*     */ 
/*     */ 
/*     */   DomContentHandler(Node node)
/*     */   {
/*  55 */     Assert.notNull(node, "node must not be null");
/*  56 */     this.node = node;
/*  57 */     if ((node instanceof Document)) {
/*  58 */       this.document = ((Document)node);
/*     */     }
/*     */     else {
/*  61 */       this.document = node.getOwnerDocument();
/*     */     }
/*  63 */     Assert.notNull(this.document, "document must not be null");
/*     */   }
/*     */   
/*     */   private Node getParent() {
/*  67 */     if (!this.elements.isEmpty()) {
/*  68 */       return (Node)this.elements.get(this.elements.size() - 1);
/*     */     }
/*     */     
/*  71 */     return this.node;
/*     */   }
/*     */   
/*     */   public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */     throws SAXException
/*     */   {
/*  77 */     Node parent = getParent();
/*  78 */     Element element = this.document.createElementNS(uri, qName);
/*  79 */     for (int i = 0; i < attributes.getLength(); i++) {
/*  80 */       String attrUri = attributes.getURI(i);
/*  81 */       String attrQname = attributes.getQName(i);
/*  82 */       String value = attributes.getValue(i);
/*  83 */       if (!attrQname.startsWith("xmlns")) {
/*  84 */         element.setAttributeNS(attrUri, attrQname, value);
/*     */       }
/*     */     }
/*  87 */     element = (Element)parent.appendChild(element);
/*  88 */     this.elements.add(element);
/*     */   }
/*     */   
/*     */   public void endElement(String uri, String localName, String qName) throws SAXException
/*     */   {
/*  93 */     this.elements.remove(this.elements.size() - 1);
/*     */   }
/*     */   
/*     */   public void characters(char[] ch, int start, int length) throws SAXException
/*     */   {
/*  98 */     String data = new String(ch, start, length);
/*  99 */     Node parent = getParent();
/* 100 */     Node lastChild = parent.getLastChild();
/* 101 */     if ((lastChild != null) && (lastChild.getNodeType() == 3)) {
/* 102 */       ((Text)lastChild).appendData(data);
/*     */     }
/*     */     else {
/* 105 */       Text text = this.document.createTextNode(data);
/* 106 */       parent.appendChild(text);
/*     */     }
/*     */   }
/*     */   
/*     */   public void processingInstruction(String target, String data) throws SAXException
/*     */   {
/* 112 */     Node parent = getParent();
/* 113 */     ProcessingInstruction pi = this.document.createProcessingInstruction(target, data);
/* 114 */     parent.appendChild(pi);
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {}
/*     */   
/*     */   public void startDocument()
/*     */     throws SAXException
/*     */   {}
/*     */   
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {}
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri)
/*     */     throws SAXException
/*     */   {}
/*     */   
/*     */   public void endPrefixMapping(String prefix)
/*     */     throws SAXException
/*     */   {}
/*     */   
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {}
/*     */   
/*     */   public void skippedEntity(String name)
/*     */     throws SAXException
/*     */   {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\xml\DomContentHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */