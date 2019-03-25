/*     */ package org.springframework.boot.autoconfigure.mustache;
/*     */ 
/*     */ import com.samskivert.mustache.Mustache;
/*     */ import com.samskivert.mustache.Mustache.Collector;
/*     */ import com.samskivert.mustache.Mustache.Compiler;
/*     */ import com.samskivert.mustache.Mustache.Escaper;
/*     */ import com.samskivert.mustache.Mustache.Formatter;
/*     */ import com.samskivert.mustache.Mustache.TemplateLoader;
/*     */ import org.springframework.beans.factory.FactoryBean;
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
/*     */ @Deprecated
/*     */ public class MustacheCompilerFactoryBean
/*     */   implements FactoryBean<Mustache.Compiler>
/*     */ {
/*     */   private String delims;
/*     */   private Mustache.TemplateLoader templateLoader;
/*     */   private Mustache.Formatter formatter;
/*     */   private Mustache.Escaper escaper;
/*     */   private Mustache.Collector collector;
/*     */   private Mustache.Compiler compiler;
/*     */   private String defaultValue;
/*     */   private Boolean emptyStringIsFalse;
/*     */   
/*     */   public void setDelims(String delims)
/*     */   {
/*  58 */     this.delims = delims;
/*     */   }
/*     */   
/*     */   public void setTemplateLoader(Mustache.TemplateLoader templateLoader) {
/*  62 */     this.templateLoader = templateLoader;
/*     */   }
/*     */   
/*     */   public void setFormatter(Mustache.Formatter formatter) {
/*  66 */     this.formatter = formatter;
/*     */   }
/*     */   
/*     */   public void setEscaper(Mustache.Escaper escaper) {
/*  70 */     this.escaper = escaper;
/*     */   }
/*     */   
/*     */   public void setCollector(Mustache.Collector collector) {
/*  74 */     this.collector = collector;
/*     */   }
/*     */   
/*     */   public void setDefaultValue(String defaultValue) {
/*  78 */     this.defaultValue = defaultValue;
/*     */   }
/*     */   
/*     */   public void setEmptyStringIsFalse(Boolean emptyStringIsFalse) {
/*  82 */     this.emptyStringIsFalse = emptyStringIsFalse;
/*     */   }
/*     */   
/*     */   public Mustache.Compiler getObject() throws Exception
/*     */   {
/*  87 */     this.compiler = Mustache.compiler();
/*  88 */     if (this.delims != null) {
/*  89 */       this.compiler = this.compiler.withDelims(this.delims);
/*     */     }
/*  91 */     if (this.templateLoader != null) {
/*  92 */       this.compiler = this.compiler.withLoader(this.templateLoader);
/*     */     }
/*  94 */     if (this.formatter != null) {
/*  95 */       this.compiler = this.compiler.withFormatter(this.formatter);
/*     */     }
/*  97 */     if (this.escaper != null) {
/*  98 */       this.compiler = this.compiler.withEscaper(this.escaper);
/*     */     }
/* 100 */     if (this.collector != null) {
/* 101 */       this.compiler = this.compiler.withCollector(this.collector);
/*     */     }
/* 103 */     if (this.defaultValue != null) {
/* 104 */       this.compiler = this.compiler.defaultValue(this.defaultValue);
/*     */     }
/* 106 */     if (this.emptyStringIsFalse != null) {
/* 107 */       this.compiler = this.compiler.emptyStringIsFalse(this.emptyStringIsFalse.booleanValue());
/*     */     }
/* 109 */     return this.compiler;
/*     */   }
/*     */   
/*     */   public Class<?> getObjectType()
/*     */   {
/* 114 */     return Mustache.Compiler.class;
/*     */   }
/*     */   
/*     */   public boolean isSingleton()
/*     */   {
/* 119 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\mustache\MustacheCompilerFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */