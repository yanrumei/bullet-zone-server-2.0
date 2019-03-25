/*    */ package org.springframework.boot.autoconfigure.condition;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.context.annotation.ConditionContext;
/*    */ import org.springframework.core.annotation.Order;
/*    */ import org.springframework.core.env.Environment;
/*    */ import org.springframework.core.io.DefaultResourceLoader;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.core.io.ResourceLoader;
/*    */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.MultiValueMap;
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
/*    */ @Order(-2147483628)
/*    */ class OnResourceCondition
/*    */   extends SpringBootCondition
/*    */ {
/* 42 */   private final ResourceLoader defaultResourceLoader = new DefaultResourceLoader();
/*    */   
/*    */ 
/*    */ 
/*    */   public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*    */   {
/* 48 */     MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(ConditionalOnResource.class.getName(), true);
/*    */     
/* 50 */     ResourceLoader loader = context.getResourceLoader() == null ? this.defaultResourceLoader : context.getResourceLoader();
/* 51 */     List<String> locations = new ArrayList();
/* 52 */     collectValues(locations, (List)attributes.get("resources"));
/* 53 */     Assert.isTrue(!locations.isEmpty(), "@ConditionalOnResource annotations must specify at least one resource location");
/*    */     
/*    */ 
/* 56 */     List<String> missing = new ArrayList();
/* 57 */     for (String location : locations) {
/* 58 */       String resource = context.getEnvironment().resolvePlaceholders(location);
/* 59 */       if (!loader.getResource(resource).exists()) {
/* 60 */         missing.add(location);
/*    */       }
/*    */     }
/* 63 */     if (!missing.isEmpty()) {
/* 64 */       return ConditionOutcome.noMatch(
/* 65 */         ConditionMessage.forCondition(ConditionalOnResource.class, new Object[0])
/* 66 */         .didNotFind("resource", "resources").items(ConditionMessage.Style.QUOTE, missing));
/*    */     }
/* 68 */     return 
/* 69 */       ConditionOutcome.match(ConditionMessage.forCondition(ConditionalOnResource.class, new Object[0])
/* 70 */       .found("location", "locations").items(locations));
/*    */   }
/*    */   
/*    */   private void collectValues(List<String> names, List<Object> values) {
/* 74 */     for (Object value : values) {
/* 75 */       for (Object item : (Object[])value) {
/* 76 */         names.add((String)item);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\condition\OnResourceCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */