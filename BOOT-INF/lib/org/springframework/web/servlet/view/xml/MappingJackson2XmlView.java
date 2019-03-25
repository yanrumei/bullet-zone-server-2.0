/*     */ package org.springframework.web.servlet.view.xml;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonView;
/*     */ import com.fasterxml.jackson.dataformat.xml.XmlMapper;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.web.servlet.view.json.AbstractJackson2View;
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
/*     */ 
/*     */ public class MappingJackson2XmlView
/*     */   extends AbstractJackson2View
/*     */ {
/*     */   public static final String DEFAULT_CONTENT_TYPE = "application/xml";
/*     */   private String modelKey;
/*     */   
/*     */   public MappingJackson2XmlView()
/*     */   {
/*  58 */     super(Jackson2ObjectMapperBuilder.xml().build(), "application/xml");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MappingJackson2XmlView(XmlMapper xmlMapper)
/*     */   {
/*  67 */     super(xmlMapper, "application/xml");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setModelKey(String modelKey)
/*     */   {
/*  75 */     this.modelKey = modelKey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object filterModel(Map<String, Object> model)
/*     */   {
/*  86 */     Object value = null;
/*  87 */     if (this.modelKey != null) {
/*  88 */       value = model.get(this.modelKey);
/*  89 */       if (value == null) {
/*  90 */         throw new IllegalStateException("Model contains no object with key [" + this.modelKey + "]");
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  95 */       for (Map.Entry<String, Object> entry : model.entrySet()) {
/*  96 */         if ((!(entry.getValue() instanceof BindingResult)) && (!((String)entry.getKey()).equals(JsonView.class.getName()))) {
/*  97 */           if (value != null) {
/*  98 */             throw new IllegalStateException("Model contains more than one object to render, only one is supported");
/*     */           }
/* 100 */           value = entry.getValue();
/*     */         }
/*     */       }
/*     */     }
/* 104 */     return value;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\xml\MappingJackson2XmlView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */