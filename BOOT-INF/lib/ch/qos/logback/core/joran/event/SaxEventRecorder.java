/*     */ package ch.qos.logback.core.joran.event;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.joran.spi.ElementPath;
/*     */ import ch.qos.logback.core.joran.spi.JoranException;
/*     */ import ch.qos.logback.core.spi.ContextAware;
/*     */ import ch.qos.logback.core.spi.ContextAwareImpl;
/*     */ import ch.qos.logback.core.status.Status;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
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
/*     */ public class SaxEventRecorder
/*     */   extends DefaultHandler
/*     */   implements ContextAware
/*     */ {
/*     */   final ContextAwareImpl cai;
/*     */   
/*     */   public SaxEventRecorder(Context context)
/*     */   {
/*  45 */     this.cai = new ContextAwareImpl(context, this);
/*     */   }
/*     */   
/*  48 */   public List<SaxEvent> saxEventList = new ArrayList();
/*     */   Locator locator;
/*  50 */   ElementPath globalElementPath = new ElementPath();
/*     */   
/*     */   public final void recordEvents(InputStream inputStream) throws JoranException {
/*  53 */     recordEvents(new InputSource(inputStream));
/*     */   }
/*     */   
/*     */   public List<SaxEvent> recordEvents(InputSource inputSource) throws JoranException {
/*  57 */     SAXParser saxParser = buildSaxParser();
/*     */     try {
/*  59 */       saxParser.parse(inputSource, this);
/*  60 */       return this.saxEventList;
/*     */     } catch (IOException ie) {
/*  62 */       handleError("I/O error occurred while parsing xml file", ie);
/*     */     }
/*     */     catch (SAXException se) {
/*  65 */       throw new JoranException("Problem parsing XML document. See previously reported errors.", se);
/*     */     } catch (Exception ex) {
/*  67 */       handleError("Unexpected exception while parsing XML document.", ex);
/*     */     }
/*  69 */     throw new IllegalStateException("This point can never be reached");
/*     */   }
/*     */   
/*     */   private void handleError(String errMsg, Throwable t) throws JoranException {
/*  73 */     addError(errMsg, t);
/*  74 */     throw new JoranException(errMsg, t);
/*     */   }
/*     */   
/*     */   private SAXParser buildSaxParser() throws JoranException {
/*     */     try {
/*  79 */       SAXParserFactory spf = SAXParserFactory.newInstance();
/*  80 */       spf.setValidating(false);
/*  81 */       spf.setNamespaceAware(true);
/*  82 */       return spf.newSAXParser();
/*     */     } catch (Exception pce) {
/*  84 */       String errMsg = "Parser configuration error occurred";
/*  85 */       addError(errMsg, pce);
/*  86 */       throw new JoranException(errMsg, pce);
/*     */     }
/*     */   }
/*     */   
/*     */   public void startDocument() {}
/*     */   
/*     */   public Locator getLocator()
/*     */   {
/*  94 */     return this.locator;
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator l) {
/*  98 */     this.locator = l;
/*     */   }
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
/*     */   {
/* 103 */     String tagName = getTagName(localName, qName);
/* 104 */     this.globalElementPath.push(tagName);
/* 105 */     ElementPath current = this.globalElementPath.duplicate();
/* 106 */     this.saxEventList.add(new StartEvent(current, namespaceURI, localName, qName, atts, getLocator()));
/*     */   }
/*     */   
/*     */   public void characters(char[] ch, int start, int length) {
/* 110 */     String bodyStr = new String(ch, start, length);
/* 111 */     SaxEvent lastEvent = getLastEvent();
/* 112 */     if ((lastEvent instanceof BodyEvent)) {
/* 113 */       BodyEvent be = (BodyEvent)lastEvent;
/* 114 */       be.append(bodyStr);
/*     */ 
/*     */     }
/* 117 */     else if (!isSpaceOnly(bodyStr)) {
/* 118 */       this.saxEventList.add(new BodyEvent(bodyStr, getLocator()));
/*     */     }
/*     */   }
/*     */   
/*     */   boolean isSpaceOnly(String bodyStr)
/*     */   {
/* 124 */     String bodyTrimmed = bodyStr.trim();
/* 125 */     return bodyTrimmed.length() == 0;
/*     */   }
/*     */   
/*     */   SaxEvent getLastEvent() {
/* 129 */     if (this.saxEventList.isEmpty()) {
/* 130 */       return null;
/*     */     }
/* 132 */     int size = this.saxEventList.size();
/* 133 */     return (SaxEvent)this.saxEventList.get(size - 1);
/*     */   }
/*     */   
/*     */   public void endElement(String namespaceURI, String localName, String qName) {
/* 137 */     this.saxEventList.add(new EndEvent(namespaceURI, localName, qName, getLocator()));
/* 138 */     this.globalElementPath.pop();
/*     */   }
/*     */   
/*     */   String getTagName(String localName, String qName) {
/* 142 */     String tagName = localName;
/* 143 */     if ((tagName == null) || (tagName.length() < 1)) {
/* 144 */       tagName = qName;
/*     */     }
/* 146 */     return tagName;
/*     */   }
/*     */   
/*     */   public void error(SAXParseException spe) throws SAXException {
/* 150 */     addError("XML_PARSING - Parsing error on line " + spe.getLineNumber() + " and column " + spe.getColumnNumber());
/* 151 */     addError(spe.toString());
/*     */   }
/*     */   
/*     */   public void fatalError(SAXParseException spe) throws SAXException
/*     */   {
/* 156 */     addError("XML_PARSING - Parsing fatal error on line " + spe.getLineNumber() + " and column " + spe.getColumnNumber());
/* 157 */     addError(spe.toString());
/*     */   }
/*     */   
/*     */   public void warning(SAXParseException spe) throws SAXException {
/* 161 */     addWarn("XML_PARSING - Parsing warning on line " + spe.getLineNumber() + " and column " + spe.getColumnNumber(), spe);
/*     */   }
/*     */   
/*     */   public void addError(String msg) {
/* 165 */     this.cai.addError(msg);
/*     */   }
/*     */   
/*     */   public void addError(String msg, Throwable ex) {
/* 169 */     this.cai.addError(msg, ex);
/*     */   }
/*     */   
/*     */   public void addInfo(String msg) {
/* 173 */     this.cai.addInfo(msg);
/*     */   }
/*     */   
/*     */   public void addInfo(String msg, Throwable ex) {
/* 177 */     this.cai.addInfo(msg, ex);
/*     */   }
/*     */   
/*     */   public void addStatus(Status status) {
/* 181 */     this.cai.addStatus(status);
/*     */   }
/*     */   
/*     */   public void addWarn(String msg) {
/* 185 */     this.cai.addWarn(msg);
/*     */   }
/*     */   
/*     */   public void addWarn(String msg, Throwable ex) {
/* 189 */     this.cai.addWarn(msg, ex);
/*     */   }
/*     */   
/*     */   public Context getContext() {
/* 193 */     return this.cai.getContext();
/*     */   }
/*     */   
/*     */   public void setContext(Context context) {
/* 197 */     this.cai.setContext(context);
/*     */   }
/*     */   
/*     */   public List<SaxEvent> getSaxEventList() {
/* 201 */     return this.saxEventList;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\joran\event\SaxEventRecorder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */