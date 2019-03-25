/*     */ package org.springframework.boot.autoconfigure.condition;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*     */ import org.springframework.context.annotation.ConditionContext;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
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
/*     */ public abstract class ResourceCondition
/*     */   extends SpringBootCondition
/*     */ {
/*     */   private final String name;
/*     */   private final String prefix;
/*     */   private final String propertyName;
/*     */   private final String[] resourceLocations;
/*     */   
/*     */   protected ResourceCondition(String name, String prefix, String propertyName, String... resourceLocations)
/*     */   {
/*  58 */     this.name = name;
/*  59 */     this.prefix = (prefix + ".");
/*  60 */     this.propertyName = propertyName;
/*  61 */     this.resourceLocations = resourceLocations;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */   {
/*  68 */     RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(context.getEnvironment(), this.prefix);
/*  69 */     if (resolver.containsProperty(this.propertyName)) {
/*  70 */       return ConditionOutcome.match(startConditionMessage()
/*  71 */         .foundExactly("property " + this.prefix + this.propertyName));
/*     */     }
/*  73 */     return getResourceOutcome(context, metadata);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ConditionOutcome getResourceOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */   {
/*  84 */     List<String> found = new ArrayList();
/*  85 */     for (String location : this.resourceLocations) {
/*  86 */       Resource resource = context.getResourceLoader().getResource(location);
/*  87 */       if ((resource != null) && (resource.exists())) {
/*  88 */         found.add(location);
/*     */       }
/*     */     }
/*  91 */     if (found.isEmpty())
/*     */     {
/*     */ 
/*  94 */       ConditionMessage message = startConditionMessage().didNotFind("resource", "resources").items(ConditionMessage.Style.QUOTE, Arrays.asList(this.resourceLocations));
/*  95 */       return ConditionOutcome.noMatch(message);
/*     */     }
/*     */     
/*  98 */     ConditionMessage message = startConditionMessage().found("resource", "resources").items(ConditionMessage.Style.QUOTE, found);
/*  99 */     return ConditionOutcome.match(message);
/*     */   }
/*     */   
/*     */   protected final ConditionMessage.Builder startConditionMessage() {
/* 103 */     return ConditionMessage.forCondition("ResourceCondition", new Object[] { "(" + this.name + ")" });
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\condition\ResourceCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */