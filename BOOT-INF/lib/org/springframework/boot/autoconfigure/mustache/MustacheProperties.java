/*    */ package org.springframework.boot.autoconfigure.mustache;
/*    */ 
/*    */ import org.springframework.boot.autoconfigure.template.AbstractTemplateViewResolverProperties;
/*    */ import org.springframework.boot.context.properties.ConfigurationProperties;
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
/*    */ @ConfigurationProperties(prefix="spring.mustache")
/*    */ public class MustacheProperties
/*    */   extends AbstractTemplateViewResolverProperties
/*    */ {
/*    */   public static final String DEFAULT_PREFIX = "classpath:/templates/";
/*    */   public static final String DEFAULT_SUFFIX = ".html";
/* 38 */   private String prefix = "classpath:/templates/";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 43 */   private String suffix = ".html";
/*    */   
/*    */   public MustacheProperties() {
/* 46 */     super("classpath:/templates/", ".html");
/*    */   }
/*    */   
/*    */   public String getPrefix()
/*    */   {
/* 51 */     return this.prefix;
/*    */   }
/*    */   
/*    */   public void setPrefix(String prefix)
/*    */   {
/* 56 */     this.prefix = prefix;
/*    */   }
/*    */   
/*    */   public String getSuffix()
/*    */   {
/* 61 */     return this.suffix;
/*    */   }
/*    */   
/*    */   public void setSuffix(String suffix)
/*    */   {
/* 66 */     this.suffix = suffix;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\mustache\MustacheProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */