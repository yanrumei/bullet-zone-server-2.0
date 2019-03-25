/*     */ package org.springframework.web.servlet.view.xml;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.springframework.oxm.Marshaller;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.web.servlet.view.AbstractView;
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
/*     */ public class MarshallingView
/*     */   extends AbstractView
/*     */ {
/*     */   public static final String DEFAULT_CONTENT_TYPE = "application/xml";
/*     */   private Marshaller marshaller;
/*     */   private String modelKey;
/*     */   
/*     */   public MarshallingView()
/*     */   {
/*  63 */     setContentType("application/xml");
/*  64 */     setExposePathVariables(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public MarshallingView(Marshaller marshaller)
/*     */   {
/*  71 */     this();
/*  72 */     Assert.notNull(marshaller, "Marshaller must not be null");
/*  73 */     this.marshaller = marshaller;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMarshaller(Marshaller marshaller)
/*     */   {
/*  81 */     this.marshaller = marshaller;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setModelKey(String modelKey)
/*     */   {
/*  90 */     this.modelKey = modelKey;
/*     */   }
/*     */   
/*     */   protected void initApplicationContext()
/*     */   {
/*  95 */     Assert.notNull(this.marshaller, "Property 'marshaller' is required");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 103 */     Object toBeMarshalled = locateToBeMarshalled(model);
/* 104 */     if (toBeMarshalled == null) {
/* 105 */       throw new IllegalStateException("Unable to locate object to be marshalled in model: " + model);
/*     */     }
/* 107 */     ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
/* 108 */     this.marshaller.marshal(toBeMarshalled, new StreamResult(baos));
/*     */     
/* 110 */     setResponseContentType(request, response);
/* 111 */     response.setContentLength(baos.size());
/* 112 */     baos.writeTo(response.getOutputStream());
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
/*     */   protected Object locateToBeMarshalled(Map<String, Object> model)
/*     */     throws IllegalStateException
/*     */   {
/* 127 */     if (this.modelKey != null) {
/* 128 */       value = model.get(this.modelKey);
/* 129 */       if (value == null) {
/* 130 */         throw new IllegalStateException("Model contains no object with key [" + this.modelKey + "]");
/*     */       }
/* 132 */       if (!isEligibleForMarshalling(this.modelKey, value)) {
/* 133 */         throw new IllegalStateException("Model object [" + value + "] retrieved via key [" + this.modelKey + "] is not supported by the Marshaller");
/*     */       }
/*     */       
/* 136 */       return value;
/*     */     }
/* 138 */     for (Object value = model.entrySet().iterator(); ((Iterator)value).hasNext();) { Map.Entry<String, Object> entry = (Map.Entry)((Iterator)value).next();
/* 139 */       Object value = entry.getValue();
/* 140 */       if ((value != null) && ((model.size() == 1) || (!(value instanceof BindingResult))) && 
/* 141 */         (isEligibleForMarshalling((String)entry.getKey(), value))) {
/* 142 */         return value;
/*     */       }
/*     */     }
/* 145 */     return null;
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
/*     */   protected boolean isEligibleForMarshalling(String modelKey, Object value)
/*     */   {
/* 159 */     Class<?> classToCheck = value.getClass();
/* 160 */     if ((value instanceof JAXBElement)) {
/* 161 */       classToCheck = ((JAXBElement)value).getDeclaredType();
/*     */     }
/* 163 */     return this.marshaller.supports(classToCheck);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\xml\MarshallingView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */