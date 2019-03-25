/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class FilteredBeanPropertyWriter
/*     */ {
/*     */   public static BeanPropertyWriter constructViewBased(BeanPropertyWriter base, Class<?>[] viewsToIncludeIn)
/*     */   {
/*  19 */     if (viewsToIncludeIn.length == 1) {
/*  20 */       return new SingleView(base, viewsToIncludeIn[0]);
/*     */     }
/*  22 */     return new MultiView(base, viewsToIncludeIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class SingleView
/*     */     extends BeanPropertyWriter
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */     protected final BeanPropertyWriter _delegate;
/*     */     
/*     */ 
/*     */     protected final Class<?> _view;
/*     */     
/*     */ 
/*     */ 
/*     */     protected SingleView(BeanPropertyWriter delegate, Class<?> view)
/*     */     {
/*  43 */       super();
/*  44 */       this._delegate = delegate;
/*  45 */       this._view = view;
/*     */     }
/*     */     
/*     */     public SingleView rename(NameTransformer transformer)
/*     */     {
/*  50 */       return new SingleView(this._delegate.rename(transformer), this._view);
/*     */     }
/*     */     
/*     */     public void assignSerializer(JsonSerializer<Object> ser)
/*     */     {
/*  55 */       this._delegate.assignSerializer(ser);
/*     */     }
/*     */     
/*     */     public void assignNullSerializer(JsonSerializer<Object> nullSer)
/*     */     {
/*  60 */       this._delegate.assignNullSerializer(nullSer);
/*     */     }
/*     */     
/*     */ 
/*     */     public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider prov)
/*     */       throws Exception
/*     */     {
/*  67 */       Class<?> activeView = prov.getActiveView();
/*  68 */       if ((activeView == null) || (this._view.isAssignableFrom(activeView))) {
/*  69 */         this._delegate.serializeAsField(bean, jgen, prov);
/*     */       } else {
/*  71 */         this._delegate.serializeAsOmittedField(bean, jgen, prov);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void serializeAsElement(Object bean, JsonGenerator jgen, SerializerProvider prov)
/*     */       throws Exception
/*     */     {
/*  79 */       Class<?> activeView = prov.getActiveView();
/*  80 */       if ((activeView == null) || (this._view.isAssignableFrom(activeView))) {
/*  81 */         this._delegate.serializeAsElement(bean, jgen, prov);
/*     */       } else {
/*  83 */         this._delegate.serializeAsPlaceholder(bean, jgen, prov);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void depositSchemaProperty(JsonObjectFormatVisitor v, SerializerProvider provider)
/*     */       throws JsonMappingException
/*     */     {
/*  91 */       Class<?> activeView = provider.getActiveView();
/*  92 */       if ((activeView == null) || (this._view.isAssignableFrom(activeView))) {
/*  93 */         super.depositSchemaProperty(v, provider);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class MultiView
/*     */     extends BeanPropertyWriter
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     protected final BeanPropertyWriter _delegate;
/*     */     protected final Class<?>[] _views;
/*     */     
/*     */     protected MultiView(BeanPropertyWriter delegate, Class<?>[] views)
/*     */     {
/* 109 */       super();
/* 110 */       this._delegate = delegate;
/* 111 */       this._views = views;
/*     */     }
/*     */     
/*     */     public MultiView rename(NameTransformer transformer)
/*     */     {
/* 116 */       return new MultiView(this._delegate.rename(transformer), this._views);
/*     */     }
/*     */     
/*     */     public void assignSerializer(JsonSerializer<Object> ser)
/*     */     {
/* 121 */       this._delegate.assignSerializer(ser);
/*     */     }
/*     */     
/*     */     public void assignNullSerializer(JsonSerializer<Object> nullSer)
/*     */     {
/* 126 */       this._delegate.assignNullSerializer(nullSer);
/*     */     }
/*     */     
/*     */ 
/*     */     public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider prov)
/*     */       throws Exception
/*     */     {
/* 133 */       Class<?> activeView = prov.getActiveView();
/* 134 */       if (activeView != null) {
/* 135 */         int i = 0;int len = this._views.length;
/* 136 */         for (; i < len; i++) {
/* 137 */           if (this._views[i].isAssignableFrom(activeView))
/*     */             break;
/*     */         }
/* 140 */         if (i == len) {
/* 141 */           this._delegate.serializeAsOmittedField(bean, jgen, prov);
/* 142 */           return;
/*     */         }
/*     */       }
/* 145 */       this._delegate.serializeAsField(bean, jgen, prov);
/*     */     }
/*     */     
/*     */ 
/*     */     public void serializeAsElement(Object bean, JsonGenerator jgen, SerializerProvider prov)
/*     */       throws Exception
/*     */     {
/* 152 */       Class<?> activeView = prov.getActiveView();
/* 153 */       if (activeView != null) {
/* 154 */         int i = 0;int len = this._views.length;
/* 155 */         for (; i < len; i++) {
/* 156 */           if (this._views[i].isAssignableFrom(activeView))
/*     */             break;
/*     */         }
/* 159 */         if (i == len) {
/* 160 */           this._delegate.serializeAsPlaceholder(bean, jgen, prov);
/* 161 */           return;
/*     */         }
/*     */       }
/* 164 */       this._delegate.serializeAsElement(bean, jgen, prov);
/*     */     }
/*     */     
/*     */ 
/*     */     public void depositSchemaProperty(JsonObjectFormatVisitor v, SerializerProvider provider)
/*     */       throws JsonMappingException
/*     */     {
/* 171 */       Class<?> activeView = provider.getActiveView();
/* 172 */       if (activeView != null) {
/* 173 */         int i = 0;int len = this._views.length;
/* 174 */         for (; i < len; i++)
/* 175 */           if (this._views[i].isAssignableFrom(activeView))
/*     */             break;
/* 177 */         if (i == len) {
/* 178 */           return;
/*     */         }
/*     */       }
/* 181 */       super.depositSchemaProperty(v, provider);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\impl\FilteredBeanPropertyWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */