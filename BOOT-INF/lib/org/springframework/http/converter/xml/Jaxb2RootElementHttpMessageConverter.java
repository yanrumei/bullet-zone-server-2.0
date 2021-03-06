/*     */ package org.springframework.http.converter.xml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.nio.charset.Charset;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.MarshalException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.PropertyException;
/*     */ import javax.xml.bind.UnmarshalException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.HttpMessageConversionException;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.XMLReaderFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Jaxb2RootElementHttpMessageConverter
/*     */   extends AbstractJaxb2HttpMessageConverter<Object>
/*     */ {
/*  69 */   private boolean supportDtd = false;
/*     */   
/*  71 */   private boolean processExternalEntities = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSupportDtd(boolean supportDtd)
/*     */   {
/*  79 */     this.supportDtd = supportDtd;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isSupportDtd()
/*     */   {
/*  86 */     return this.supportDtd;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProcessExternalEntities(boolean processExternalEntities)
/*     */   {
/*  96 */     this.processExternalEntities = processExternalEntities;
/*  97 */     if (processExternalEntities) {
/*  98 */       setSupportDtd(true);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isProcessExternalEntities()
/*     */   {
/* 106 */     return this.processExternalEntities;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean canRead(Class<?> clazz, MediaType mediaType)
/*     */   {
/* 112 */     return ((clazz.isAnnotationPresent(XmlRootElement.class)) || (clazz.isAnnotationPresent(XmlType.class))) && 
/* 113 */       (canRead(mediaType));
/*     */   }
/*     */   
/*     */   public boolean canWrite(Class<?> clazz, MediaType mediaType)
/*     */   {
/* 118 */     return (AnnotationUtils.findAnnotation(clazz, XmlRootElement.class) != null) && (canWrite(mediaType));
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean supports(Class<?> clazz)
/*     */   {
/* 124 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected Object readFromSource(Class<?> clazz, HttpHeaders headers, Source source) throws IOException
/*     */   {
/*     */     try {
/* 130 */       source = processSource(source);
/* 131 */       Unmarshaller unmarshaller = createUnmarshaller(clazz);
/* 132 */       if (clazz.isAnnotationPresent(XmlRootElement.class)) {
/* 133 */         return unmarshaller.unmarshal(source);
/*     */       }
/*     */       
/* 136 */       JAXBElement<?> jaxbElement = unmarshaller.unmarshal(source, clazz);
/* 137 */       return jaxbElement.getValue();
/*     */     }
/*     */     catch (NullPointerException ex)
/*     */     {
/* 141 */       if (!isSupportDtd()) {
/* 142 */         throw new HttpMessageNotReadableException("NPE while unmarshalling. This can happen due to the presence of DTD declarations which are disabled.", ex);
/*     */       }
/*     */       
/* 145 */       throw ex;
/*     */     }
/*     */     catch (UnmarshalException ex) {
/* 148 */       throw new HttpMessageNotReadableException("Could not unmarshal to [" + clazz + "]: " + ex.getMessage(), ex);
/*     */     }
/*     */     catch (JAXBException ex)
/*     */     {
/* 152 */       throw new HttpMessageConversionException("Could not instantiate JAXBContext: " + ex.getMessage(), ex);
/*     */     }
/*     */   }
/*     */   
/*     */   protected Source processSource(Source source) {
/* 157 */     if ((source instanceof StreamSource)) {
/* 158 */       StreamSource streamSource = (StreamSource)source;
/* 159 */       InputSource inputSource = new InputSource(streamSource.getInputStream());
/*     */       try {
/* 161 */         XMLReader xmlReader = XMLReaderFactory.createXMLReader();
/* 162 */         xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", !isSupportDtd());
/* 163 */         String featureName = "http://xml.org/sax/features/external-general-entities";
/* 164 */         xmlReader.setFeature(featureName, isProcessExternalEntities());
/* 165 */         if (!isProcessExternalEntities()) {
/* 166 */           xmlReader.setEntityResolver(NO_OP_ENTITY_RESOLVER);
/*     */         }
/* 168 */         return new SAXSource(xmlReader, inputSource);
/*     */       }
/*     */       catch (SAXException ex) {
/* 171 */         this.logger.warn("Processing of external entities could not be disabled", ex);
/* 172 */         return source;
/*     */       }
/*     */     }
/*     */     
/* 176 */     return source;
/*     */   }
/*     */   
/*     */   protected void writeToResult(Object o, HttpHeaders headers, Result result) throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 183 */       Class<?> clazz = ClassUtils.getUserClass(o);
/* 184 */       Marshaller marshaller = createMarshaller(clazz);
/* 185 */       setCharset(headers.getContentType(), marshaller);
/* 186 */       marshaller.marshal(o, result);
/*     */     }
/*     */     catch (MarshalException ex) {
/* 189 */       throw new HttpMessageNotWritableException("Could not marshal [" + o + "]: " + ex.getMessage(), ex);
/*     */     }
/*     */     catch (JAXBException ex) {
/* 192 */       throw new HttpMessageConversionException("Could not instantiate JAXBContext: " + ex.getMessage(), ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setCharset(MediaType contentType, Marshaller marshaller) throws PropertyException {
/* 197 */     if ((contentType != null) && (contentType.getCharset() != null)) {
/* 198 */       marshaller.setProperty("jaxb.encoding", contentType.getCharset().name());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/* 203 */   private static final EntityResolver NO_OP_ENTITY_RESOLVER = new EntityResolver()
/*     */   {
/*     */     public InputSource resolveEntity(String publicId, String systemId) {
/* 206 */       return new InputSource(new StringReader(""));
/*     */     }
/*     */   };
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\converter\xml\Jaxb2RootElementHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */