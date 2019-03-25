/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
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
/*     */ abstract class AbstractXMLStreamReader
/*     */   implements XMLStreamReader
/*     */ {
/*     */   public String getElementText()
/*     */     throws XMLStreamException
/*     */   {
/*  36 */     if (getEventType() != 1) {
/*  37 */       throw new XMLStreamException("parser must be on START_ELEMENT to read next text", getLocation());
/*     */     }
/*  39 */     int eventType = next();
/*  40 */     StringBuilder builder = new StringBuilder();
/*  41 */     while (eventType != 2) {
/*  42 */       if ((eventType == 4) || (eventType == 12) || (eventType == 6) || (eventType == 9))
/*     */       {
/*  44 */         builder.append(getText());
/*     */       }
/*  46 */       else if ((eventType != 3) && (eventType != 5))
/*     */       {
/*     */ 
/*     */ 
/*  50 */         if (eventType == 8)
/*     */         {
/*  52 */           throw new XMLStreamException("unexpected end of document when reading element text content", getLocation());
/*     */         }
/*  54 */         if (eventType == 1) {
/*  55 */           throw new XMLStreamException("element text content may not contain START_ELEMENT", getLocation());
/*     */         }
/*     */         
/*  58 */         throw new XMLStreamException("Unexpected event type " + eventType, getLocation());
/*     */       }
/*  60 */       eventType = next();
/*     */     }
/*  62 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public String getAttributeLocalName(int index)
/*     */   {
/*  67 */     return getAttributeName(index).getLocalPart();
/*     */   }
/*     */   
/*     */   public String getAttributeNamespace(int index)
/*     */   {
/*  72 */     return getAttributeName(index).getNamespaceURI();
/*     */   }
/*     */   
/*     */   public String getAttributePrefix(int index)
/*     */   {
/*  77 */     return getAttributeName(index).getPrefix();
/*     */   }
/*     */   
/*     */   public String getNamespaceURI()
/*     */   {
/*  82 */     int eventType = getEventType();
/*  83 */     if ((eventType == 1) || (eventType == 2)) {
/*  84 */       return getName().getNamespaceURI();
/*     */     }
/*     */     
/*  87 */     throw new IllegalStateException("parser must be on START_ELEMENT or END_ELEMENT state");
/*     */   }
/*     */   
/*     */ 
/*     */   public String getNamespaceURI(String prefix)
/*     */   {
/*  93 */     Assert.notNull(prefix, "No prefix given");
/*  94 */     return getNamespaceContext().getNamespaceURI(prefix);
/*     */   }
/*     */   
/*     */   public boolean hasText()
/*     */   {
/*  99 */     int eventType = getEventType();
/* 100 */     return (eventType == 6) || (eventType == 4) || (eventType == 5) || (eventType == 12) || (eventType == 9);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getPrefix()
/*     */   {
/* 107 */     int eventType = getEventType();
/* 108 */     if ((eventType == 1) || (eventType == 2)) {
/* 109 */       return getName().getPrefix();
/*     */     }
/*     */     
/* 112 */     throw new IllegalStateException("parser must be on START_ELEMENT or END_ELEMENT state");
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean hasName()
/*     */   {
/* 118 */     int eventType = getEventType();
/* 119 */     return (eventType == 1) || (eventType == 2);
/*     */   }
/*     */   
/*     */   public boolean isWhiteSpace()
/*     */   {
/* 124 */     return getEventType() == 6;
/*     */   }
/*     */   
/*     */   public boolean isStartElement()
/*     */   {
/* 129 */     return getEventType() == 1;
/*     */   }
/*     */   
/*     */   public boolean isEndElement()
/*     */   {
/* 134 */     return getEventType() == 2;
/*     */   }
/*     */   
/*     */   public boolean isCharacters()
/*     */   {
/* 139 */     return getEventType() == 4;
/*     */   }
/*     */   
/*     */   public int nextTag() throws XMLStreamException
/*     */   {
/* 144 */     int eventType = next();
/* 145 */     while (((eventType == 4) && (isWhiteSpace())) || ((eventType == 12) && 
/* 146 */       (isWhiteSpace())) || (eventType == 6) || (eventType == 3) || (eventType == 5))
/*     */     {
/* 148 */       eventType = next();
/*     */     }
/* 150 */     if ((eventType != 1) && (eventType != 2)) {
/* 151 */       throw new XMLStreamException("expected start or end tag", getLocation());
/*     */     }
/* 153 */     return eventType;
/*     */   }
/*     */   
/*     */   public void require(int expectedType, String namespaceURI, String localName) throws XMLStreamException
/*     */   {
/* 158 */     int eventType = getEventType();
/* 159 */     if (eventType != expectedType) {
/* 160 */       throw new XMLStreamException("Expected [" + expectedType + "] but read [" + eventType + "]");
/*     */     }
/*     */   }
/*     */   
/*     */   public String getAttributeValue(String namespaceURI, String localName)
/*     */   {
/* 166 */     for (int i = 0; i < getAttributeCount(); i++) {
/* 167 */       QName name = getAttributeName(i);
/* 168 */       if ((name.getLocalPart().equals(localName)) && ((namespaceURI == null) || 
/* 169 */         (name.getNamespaceURI().equals(namespaceURI)))) {
/* 170 */         return getAttributeValue(i);
/*     */       }
/*     */     }
/* 173 */     return null;
/*     */   }
/*     */   
/*     */   public boolean hasNext() throws XMLStreamException
/*     */   {
/* 178 */     return getEventType() != 8;
/*     */   }
/*     */   
/*     */   public String getLocalName()
/*     */   {
/* 183 */     return getName().getLocalPart();
/*     */   }
/*     */   
/*     */   public char[] getTextCharacters()
/*     */   {
/* 188 */     return getText().toCharArray();
/*     */   }
/*     */   
/*     */   public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length)
/*     */     throws XMLStreamException
/*     */   {
/* 194 */     char[] source = getTextCharacters();
/* 195 */     length = Math.min(length, source.length);
/* 196 */     System.arraycopy(source, sourceStart, target, targetStart, length);
/* 197 */     return length;
/*     */   }
/*     */   
/*     */   public int getTextLength()
/*     */   {
/* 202 */     return getText().length();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\xml\AbstractXMLStreamReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */