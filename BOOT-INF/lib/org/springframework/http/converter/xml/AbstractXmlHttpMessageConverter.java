/*    */ package org.springframework.http.converter.xml;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.xml.transform.Result;
/*    */ import javax.xml.transform.Source;
/*    */ import javax.xml.transform.Transformer;
/*    */ import javax.xml.transform.TransformerException;
/*    */ import javax.xml.transform.TransformerFactory;
/*    */ import javax.xml.transform.stream.StreamResult;
/*    */ import javax.xml.transform.stream.StreamSource;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.HttpInputMessage;
/*    */ import org.springframework.http.HttpOutputMessage;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.http.converter.AbstractHttpMessageConverter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractXmlHttpMessageConverter<T>
/*    */   extends AbstractHttpMessageConverter<T>
/*    */ {
/* 47 */   private final TransformerFactory transformerFactory = TransformerFactory.newInstance();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected AbstractXmlHttpMessageConverter()
/*    */   {
/* 55 */     super(new MediaType[] { MediaType.APPLICATION_XML, MediaType.TEXT_XML, new MediaType("application", "*+xml") });
/*    */   }
/*    */   
/*    */   public final T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage)
/*    */     throws IOException
/*    */   {
/* 61 */     return (T)readFromSource(clazz, inputMessage.getHeaders(), new StreamSource(inputMessage.getBody()));
/*    */   }
/*    */   
/*    */   protected final void writeInternal(T t, HttpOutputMessage outputMessage) throws IOException
/*    */   {
/* 66 */     writeToResult(t, outputMessage.getHeaders(), new StreamResult(outputMessage.getBody()));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void transform(Source source, Result result)
/*    */     throws TransformerException
/*    */   {
/* 76 */     this.transformerFactory.newTransformer().transform(source, result);
/*    */   }
/*    */   
/*    */   protected abstract T readFromSource(Class<? extends T> paramClass, HttpHeaders paramHttpHeaders, Source paramSource)
/*    */     throws IOException;
/*    */   
/*    */   protected abstract void writeToResult(T paramT, HttpHeaders paramHttpHeaders, Result paramResult)
/*    */     throws IOException;
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\converter\xml\AbstractXmlHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */