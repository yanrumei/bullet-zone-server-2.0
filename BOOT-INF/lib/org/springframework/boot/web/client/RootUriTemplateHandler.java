/*    */ package org.springframework.boot.web.client;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.Map;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.StringUtils;
/*    */ import org.springframework.web.client.RestTemplate;
/*    */ import org.springframework.web.util.DefaultUriTemplateHandler;
/*    */ import org.springframework.web.util.UriTemplateHandler;
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
/*    */ public class RootUriTemplateHandler
/*    */   implements UriTemplateHandler
/*    */ {
/*    */   private final String rootUri;
/*    */   private final UriTemplateHandler handler;
/*    */   
/*    */   protected RootUriTemplateHandler(UriTemplateHandler handler)
/*    */   {
/* 41 */     this.rootUri = null;
/* 42 */     this.handler = handler;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public RootUriTemplateHandler(String rootUri)
/*    */   {
/* 50 */     this(rootUri, new DefaultUriTemplateHandler());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public RootUriTemplateHandler(String rootUri, UriTemplateHandler handler)
/*    */   {
/* 59 */     Assert.notNull(rootUri, "RootUri must not be null");
/* 60 */     Assert.notNull(handler, "Handler must not be null");
/* 61 */     this.rootUri = rootUri;
/* 62 */     this.handler = handler;
/*    */   }
/*    */   
/*    */   public URI expand(String uriTemplate, Map<String, ?> uriVariables)
/*    */   {
/* 67 */     return this.handler.expand(apply(uriTemplate), uriVariables);
/*    */   }
/*    */   
/*    */   public URI expand(String uriTemplate, Object... uriVariables)
/*    */   {
/* 72 */     return this.handler.expand(apply(uriTemplate), uriVariables);
/*    */   }
/*    */   
/*    */   private String apply(String uriTemplate) {
/* 76 */     if (StringUtils.startsWithIgnoreCase(uriTemplate, "/")) {
/* 77 */       return getRootUri() + uriTemplate;
/*    */     }
/* 79 */     return uriTemplate;
/*    */   }
/*    */   
/*    */   public String getRootUri() {
/* 83 */     return this.rootUri;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static RootUriTemplateHandler addTo(RestTemplate restTemplate, String rootUri)
/*    */   {
/* 94 */     Assert.notNull(restTemplate, "RestTemplate must not be null");
/*    */     
/* 96 */     RootUriTemplateHandler handler = new RootUriTemplateHandler(rootUri, restTemplate.getUriTemplateHandler());
/* 97 */     restTemplate.setUriTemplateHandler(handler);
/* 98 */     return handler;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\client\RootUriTemplateHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */