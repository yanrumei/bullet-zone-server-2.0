/*     */ package org.springframework.http.converter.xml;
/*     */ 
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import org.springframework.http.converter.HttpMessageConversionException;
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
/*     */ public abstract class AbstractJaxb2HttpMessageConverter<T>
/*     */   extends AbstractXmlHttpMessageConverter<T>
/*     */ {
/*  39 */   private final ConcurrentMap<Class<?>, JAXBContext> jaxbContexts = new ConcurrentHashMap(64);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Marshaller createMarshaller(Class<?> clazz)
/*     */   {
/*     */     try
/*     */     {
/*  50 */       JAXBContext jaxbContext = getJaxbContext(clazz);
/*  51 */       Marshaller marshaller = jaxbContext.createMarshaller();
/*  52 */       customizeMarshaller(marshaller);
/*  53 */       return marshaller;
/*     */     }
/*     */     catch (JAXBException ex)
/*     */     {
/*  57 */       throw new HttpMessageConversionException("Could not create Marshaller for class [" + clazz + "]: " + ex.getMessage(), ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void customizeMarshaller(Marshaller marshaller) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Unmarshaller createUnmarshaller(Class<?> clazz)
/*     */     throws JAXBException
/*     */   {
/*     */     try
/*     */     {
/*  79 */       JAXBContext jaxbContext = getJaxbContext(clazz);
/*  80 */       Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
/*  81 */       customizeUnmarshaller(unmarshaller);
/*  82 */       return unmarshaller;
/*     */     }
/*     */     catch (JAXBException ex)
/*     */     {
/*  86 */       throw new HttpMessageConversionException("Could not create Unmarshaller for class [" + clazz + "]: " + ex.getMessage(), ex);
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
/*     */   protected void customizeUnmarshaller(Unmarshaller unmarshaller) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JAXBContext getJaxbContext(Class<?> clazz)
/*     */   {
/* 107 */     Assert.notNull(clazz, "'clazz' must not be null");
/* 108 */     JAXBContext jaxbContext = (JAXBContext)this.jaxbContexts.get(clazz);
/* 109 */     if (jaxbContext == null) {
/*     */       try {
/* 111 */         jaxbContext = JAXBContext.newInstance(new Class[] { clazz });
/* 112 */         this.jaxbContexts.putIfAbsent(clazz, jaxbContext);
/*     */       }
/*     */       catch (JAXBException ex)
/*     */       {
/* 116 */         throw new HttpMessageConversionException("Could not instantiate JAXBContext for class [" + clazz + "]: " + ex.getMessage(), ex);
/*     */       }
/*     */     }
/* 119 */     return jaxbContext;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\converter\xml\AbstractJaxb2HttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */