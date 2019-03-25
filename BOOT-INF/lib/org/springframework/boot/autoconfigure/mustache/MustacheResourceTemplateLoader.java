/*    */ package org.springframework.boot.autoconfigure.mustache;
/*    */ 
/*    */ import com.samskivert.mustache.Mustache.TemplateLoader;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.Reader;
/*    */ import org.springframework.context.ResourceLoaderAware;
/*    */ import org.springframework.core.io.DefaultResourceLoader;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.core.io.ResourceLoader;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MustacheResourceTemplateLoader
/*    */   implements Mustache.TemplateLoader, ResourceLoaderAware
/*    */ {
/* 44 */   private String prefix = "";
/*    */   
/* 46 */   private String suffix = "";
/*    */   
/* 48 */   private String charSet = "UTF-8";
/*    */   
/* 50 */   private ResourceLoader resourceLoader = new DefaultResourceLoader();
/*    */   
/*    */ 
/*    */   public MustacheResourceTemplateLoader() {}
/*    */   
/*    */   public MustacheResourceTemplateLoader(String prefix, String suffix)
/*    */   {
/* 57 */     this.prefix = prefix;
/* 58 */     this.suffix = suffix;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setCharset(String charSet)
/*    */   {
/* 66 */     this.charSet = charSet;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setResourceLoader(ResourceLoader resourceLoader)
/*    */   {
/* 75 */     this.resourceLoader = resourceLoader;
/*    */   }
/*    */   
/*    */   public Reader getTemplate(String name) throws Exception
/*    */   {
/* 80 */     return new InputStreamReader(this.resourceLoader
/* 81 */       .getResource(this.prefix + name + this.suffix).getInputStream(), this.charSet);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\mustache\MustacheResourceTemplateLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */