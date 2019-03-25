/*     */ package ch.qos.logback.core.pattern.parser;
/*     */ 
/*     */ import ch.qos.logback.core.pattern.CompositeConverter;
/*     */ import ch.qos.logback.core.pattern.Converter;
/*     */ import ch.qos.logback.core.pattern.DynamicConverter;
/*     */ import ch.qos.logback.core.pattern.LiteralConverter;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.status.ErrorStatus;
/*     */ import ch.qos.logback.core.util.OptionHelper;
/*     */ import java.util.Map;
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
/*     */ class Compiler<E>
/*     */   extends ContextAwareBase
/*     */ {
/*     */   Converter<E> head;
/*     */   Converter<E> tail;
/*     */   final Node top;
/*     */   final Map converterMap;
/*     */   
/*     */   Compiler(Node top, Map converterMap)
/*     */   {
/*  34 */     this.top = top;
/*  35 */     this.converterMap = converterMap;
/*     */   }
/*     */   
/*     */   Converter<E> compile() {
/*  39 */     this.head = (this.tail = null);
/*  40 */     for (Node n = this.top; n != null; n = n.next) {
/*  41 */       switch (n.type) {
/*     */       case 0: 
/*  43 */         addToList(new LiteralConverter((String)n.getValue()));
/*  44 */         break;
/*     */       case 2: 
/*  46 */         CompositeNode cn = (CompositeNode)n;
/*  47 */         CompositeConverter<E> compositeConverter = createCompositeConverter(cn);
/*  48 */         if (compositeConverter == null) {
/*  49 */           addError("Failed to create converter for [%" + cn.getValue() + "] keyword");
/*  50 */           addToList(new LiteralConverter("%PARSER_ERROR[" + cn.getValue() + "]"));
/*     */         }
/*     */         else {
/*  53 */           compositeConverter.setFormattingInfo(cn.getFormatInfo());
/*  54 */           compositeConverter.setOptionList(cn.getOptions());
/*  55 */           Compiler<E> childCompiler = new Compiler(cn.getChildNode(), this.converterMap);
/*  56 */           childCompiler.setContext(this.context);
/*  57 */           Converter<E> childConverter = childCompiler.compile();
/*  58 */           compositeConverter.setChildConverter(childConverter);
/*  59 */           addToList(compositeConverter); }
/*  60 */         break;
/*     */       case 1: 
/*  62 */         SimpleKeywordNode kn = (SimpleKeywordNode)n;
/*  63 */         DynamicConverter<E> dynaConverter = createConverter(kn);
/*  64 */         if (dynaConverter != null) {
/*  65 */           dynaConverter.setFormattingInfo(kn.getFormatInfo());
/*  66 */           dynaConverter.setOptionList(kn.getOptions());
/*  67 */           addToList(dynaConverter);
/*     */         }
/*     */         else
/*     */         {
/*  71 */           Converter<E> errConveter = new LiteralConverter("%PARSER_ERROR[" + kn.getValue() + "]");
/*  72 */           addStatus(new ErrorStatus("[" + kn.getValue() + "] is not a valid conversion word", this));
/*  73 */           addToList(errConveter);
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/*  78 */     return this.head;
/*     */   }
/*     */   
/*     */   private void addToList(Converter<E> c) {
/*  82 */     if (this.head == null) {
/*  83 */       this.head = (this.tail = c);
/*     */     } else {
/*  85 */       this.tail.setNext(c);
/*  86 */       this.tail = c;
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
/*     */   DynamicConverter<E> createConverter(SimpleKeywordNode kn)
/*     */   {
/*  99 */     String keyword = (String)kn.getValue();
/* 100 */     String converterClassStr = (String)this.converterMap.get(keyword);
/*     */     
/* 102 */     if (converterClassStr != null) {
/*     */       try {
/* 104 */         return (DynamicConverter)OptionHelper.instantiateByClassName(converterClassStr, DynamicConverter.class, this.context);
/*     */       } catch (Exception e) {
/* 106 */         addError("Failed to instantiate converter class [" + converterClassStr + "] for keyword [" + keyword + "]", e);
/* 107 */         return null;
/*     */       }
/*     */     }
/* 110 */     addError("There is no conversion class registered for conversion word [" + keyword + "]");
/* 111 */     return null;
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
/*     */   CompositeConverter<E> createCompositeConverter(CompositeNode cn)
/*     */   {
/* 124 */     String keyword = (String)cn.getValue();
/* 125 */     String converterClassStr = (String)this.converterMap.get(keyword);
/*     */     
/* 127 */     if (converterClassStr != null) {
/*     */       try {
/* 129 */         return (CompositeConverter)OptionHelper.instantiateByClassName(converterClassStr, CompositeConverter.class, this.context);
/*     */       } catch (Exception e) {
/* 131 */         addError("Failed to instantiate converter class [" + converterClassStr + "] as a composite converter for keyword [" + keyword + "]", e);
/* 132 */         return null;
/*     */       }
/*     */     }
/* 135 */     addError("There is no conversion class registered for composite conversion word [" + keyword + "]");
/* 136 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\pattern\parser\Compiler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */