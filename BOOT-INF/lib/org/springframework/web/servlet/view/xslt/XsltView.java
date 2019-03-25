/*     */ package org.springframework.web.servlet.view.xslt;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.net.URI;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.xml.transform.ErrorListener;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.Templates;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.TransformerFactoryConfigurationError;
/*     */ import javax.xml.transform.URIResolver;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.xml.SimpleTransformErrorListener;
/*     */ import org.springframework.util.xml.TransformerUtils;
/*     */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XsltView
/*     */   extends AbstractUrlBasedView
/*     */ {
/*     */   private Class<? extends TransformerFactory> transformerFactoryClass;
/*     */   private String sourceKey;
/*     */   private URIResolver uriResolver;
/*  82 */   private ErrorListener errorListener = new SimpleTransformErrorListener(this.logger);
/*     */   
/*  84 */   private boolean indent = true;
/*     */   
/*     */   private Properties outputProperties;
/*     */   
/*  88 */   private boolean cacheTemplates = true;
/*     */   
/*     */ 
/*     */ 
/*     */   private TransformerFactory transformerFactory;
/*     */   
/*     */ 
/*     */   private Templates cachedTemplates;
/*     */   
/*     */ 
/*     */ 
/*     */   public void setTransformerFactoryClass(Class<? extends TransformerFactory> transformerFactoryClass)
/*     */   {
/* 101 */     this.transformerFactoryClass = transformerFactoryClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSourceKey(String sourceKey)
/*     */   {
/* 114 */     this.sourceKey = sourceKey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUriResolver(URIResolver uriResolver)
/*     */   {
/* 122 */     this.uriResolver = uriResolver;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setErrorListener(ErrorListener errorListener)
/*     */   {
/* 135 */     this.errorListener = (errorListener != null ? errorListener : new SimpleTransformErrorListener(this.logger));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIndent(boolean indent)
/*     */   {
/* 146 */     this.indent = indent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOutputProperties(Properties outputProperties)
/*     */   {
/* 156 */     this.outputProperties = outputProperties;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCacheTemplates(boolean cacheTemplates)
/*     */   {
/* 165 */     this.cacheTemplates = cacheTemplates;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initApplicationContext()
/*     */     throws BeansException
/*     */   {
/* 174 */     this.transformerFactory = newTransformerFactory(this.transformerFactoryClass);
/* 175 */     this.transformerFactory.setErrorListener(this.errorListener);
/* 176 */     if (this.uriResolver != null) {
/* 177 */       this.transformerFactory.setURIResolver(this.uriResolver);
/*     */     }
/* 179 */     if (this.cacheTemplates) {
/* 180 */       this.cachedTemplates = loadTemplates();
/*     */     }
/*     */   }
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
/*     */   protected TransformerFactory newTransformerFactory(Class<? extends TransformerFactory> transformerFactoryClass)
/*     */   {
/* 198 */     if (transformerFactoryClass != null) {
/*     */       try {
/* 200 */         return (TransformerFactory)transformerFactoryClass.newInstance();
/*     */       }
/*     */       catch (Exception ex) {
/* 203 */         throw new TransformerFactoryConfigurationError(ex, "Could not instantiate TransformerFactory");
/*     */       }
/*     */     }
/*     */     
/* 207 */     return TransformerFactory.newInstance();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final TransformerFactory getTransformerFactory()
/*     */   {
/* 216 */     return this.transformerFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 225 */     Templates templates = this.cachedTemplates;
/* 226 */     if (templates == null) {
/* 227 */       templates = loadTemplates();
/*     */     }
/*     */     
/* 230 */     Transformer transformer = createTransformer(templates);
/* 231 */     configureTransformer(model, response, transformer);
/* 232 */     configureResponse(model, response, transformer);
/* 233 */     Source source = null;
/*     */     try {
/* 235 */       source = locateSource(model);
/* 236 */       if (source == null) {
/* 237 */         throw new IllegalArgumentException("Unable to locate Source object in model: " + model);
/*     */       }
/* 239 */       transformer.transform(source, createResult(response));
/*     */     }
/*     */     finally {
/* 242 */       closeSourceIfNecessary(source);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Result createResult(HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 255 */     return new StreamResult(response.getOutputStream());
/*     */   }
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
/*     */   protected Source locateSource(Map<String, Object> model)
/*     */     throws Exception
/*     */   {
/* 271 */     if (this.sourceKey != null) {
/* 272 */       return convertSource(model.get(this.sourceKey));
/*     */     }
/* 274 */     Object source = CollectionUtils.findValueOfType(model.values(), getSourceTypes());
/* 275 */     return source != null ? convertSource(source) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Class<?>[] getSourceTypes()
/*     */   {
/* 286 */     return new Class[] { Source.class, Document.class, Node.class, Reader.class, InputStream.class, Resource.class };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Source convertSource(Object source)
/*     */     throws Exception
/*     */   {
/* 297 */     if ((source instanceof Source)) {
/* 298 */       return (Source)source;
/*     */     }
/* 300 */     if ((source instanceof Document)) {
/* 301 */       return new DOMSource(((Document)source).getDocumentElement());
/*     */     }
/* 303 */     if ((source instanceof Node)) {
/* 304 */       return new DOMSource((Node)source);
/*     */     }
/* 306 */     if ((source instanceof Reader)) {
/* 307 */       return new StreamSource((Reader)source);
/*     */     }
/* 309 */     if ((source instanceof InputStream)) {
/* 310 */       return new StreamSource((InputStream)source);
/*     */     }
/* 312 */     if ((source instanceof Resource)) {
/* 313 */       Resource resource = (Resource)source;
/* 314 */       return new StreamSource(resource.getInputStream(), resource.getURI().toASCIIString());
/*     */     }
/*     */     
/* 317 */     throw new IllegalArgumentException("Value '" + source + "' cannot be converted to XSLT Source");
/*     */   }
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
/*     */   protected void configureTransformer(Map<String, Object> model, HttpServletResponse response, Transformer transformer)
/*     */   {
/* 336 */     copyModelParameters(model, transformer);
/* 337 */     copyOutputProperties(transformer);
/* 338 */     configureIndentation(transformer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void configureIndentation(Transformer transformer)
/*     */   {
/* 348 */     if (this.indent) {
/* 349 */       TransformerUtils.enableIndenting(transformer);
/*     */     }
/*     */     else {
/* 352 */       TransformerUtils.disableIndenting(transformer);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void copyOutputProperties(Transformer transformer)
/*     */   {
/* 363 */     if (this.outputProperties != null) {
/* 364 */       Enumeration<?> en = this.outputProperties.propertyNames();
/* 365 */       while (en.hasMoreElements()) {
/* 366 */         String name = (String)en.nextElement();
/* 367 */         transformer.setOutputProperty(name, this.outputProperties.getProperty(name));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void copyModelParameters(Map<String, Object> model, Transformer transformer)
/*     */   {
/* 380 */     for (Map.Entry<String, Object> entry : model.entrySet()) {
/* 381 */       transformer.setParameter((String)entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
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
/*     */   protected void configureResponse(Map<String, Object> model, HttpServletResponse response, Transformer transformer)
/*     */   {
/* 397 */     String contentType = getContentType();
/* 398 */     String mediaType = transformer.getOutputProperty("media-type");
/* 399 */     String encoding = transformer.getOutputProperty("encoding");
/* 400 */     if (StringUtils.hasText(mediaType)) {
/* 401 */       contentType = mediaType;
/*     */     }
/* 403 */     if (StringUtils.hasText(encoding))
/*     */     {
/* 405 */       if ((contentType != null) && (!contentType.toLowerCase().contains(";charset="))) {
/* 406 */         contentType = contentType + ";charset=" + encoding;
/*     */       }
/*     */     }
/* 409 */     response.setContentType(contentType);
/*     */   }
/*     */   
/*     */ 
/*     */   private Templates loadTemplates()
/*     */     throws ApplicationContextException
/*     */   {
/* 416 */     Source stylesheetSource = getStylesheetSource();
/*     */     try {
/* 418 */       Templates templates = this.transformerFactory.newTemplates(stylesheetSource);
/* 419 */       if (this.logger.isDebugEnabled()) {
/* 420 */         this.logger.debug("Loading templates '" + templates + "'");
/*     */       }
/* 422 */       return templates;
/*     */     }
/*     */     catch (TransformerConfigurationException ex) {
/* 425 */       throw new ApplicationContextException("Can't load stylesheet from '" + getUrl() + "'", ex);
/*     */     }
/*     */     finally {
/* 428 */       closeSourceIfNecessary(stylesheetSource);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Transformer createTransformer(Templates templates)
/*     */     throws TransformerConfigurationException
/*     */   {
/* 441 */     Transformer transformer = templates.newTransformer();
/* 442 */     if (this.uriResolver != null) {
/* 443 */       transformer.setURIResolver(this.uriResolver);
/*     */     }
/* 445 */     return transformer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Source getStylesheetSource()
/*     */   {
/* 453 */     String url = getUrl();
/* 454 */     if (this.logger.isDebugEnabled()) {
/* 455 */       this.logger.debug("Loading XSLT stylesheet from '" + url + "'");
/*     */     }
/*     */     try {
/* 458 */       Resource resource = getApplicationContext().getResource(url);
/* 459 */       return new StreamSource(resource.getInputStream(), resource.getURI().toASCIIString());
/*     */     }
/*     */     catch (IOException ex) {
/* 462 */       throw new ApplicationContextException("Can't load XSLT stylesheet from '" + url + "'", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void closeSourceIfNecessary(Source source)
/*     */   {
/* 472 */     if ((source instanceof StreamSource)) {
/* 473 */       StreamSource streamSource = (StreamSource)source;
/* 474 */       if (streamSource.getReader() != null) {
/*     */         try {
/* 476 */           streamSource.getReader().close();
/*     */         }
/*     */         catch (IOException localIOException) {}
/*     */       }
/*     */       
/*     */ 
/* 482 */       if (streamSource.getInputStream() != null) {
/*     */         try {
/* 484 */           streamSource.getInputStream().close();
/*     */         }
/*     */         catch (IOException localIOException1) {}
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\xslt\XsltView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */