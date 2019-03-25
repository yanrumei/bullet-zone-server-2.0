/*     */ package org.springframework.boot.autoconfigure.condition;
/*     */ 
/*     */ import javax.naming.NamingException;
/*     */ import org.springframework.context.annotation.ConditionContext;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.jndi.JndiLocatorDelegate;
/*     */ import org.springframework.jndi.JndiLocatorSupport;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ @Order(2147483627)
/*     */ class OnJndiCondition
/*     */   extends SpringBootCondition
/*     */ {
/*     */   public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */   {
/*  44 */     AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(metadata
/*  45 */       .getAnnotationAttributes(ConditionalOnJndi.class.getName()));
/*  46 */     String[] locations = annotationAttributes.getStringArray("value");
/*     */     try {
/*  48 */       return getMatchOutcome(locations);
/*     */     }
/*     */     catch (NoClassDefFoundError ex) {}
/*  51 */     return 
/*  52 */       ConditionOutcome.noMatch(ConditionMessage.forCondition(ConditionalOnJndi.class, new Object[0])
/*  53 */       .because("JNDI class not found"));
/*     */   }
/*     */   
/*     */   private ConditionOutcome getMatchOutcome(String[] locations)
/*     */   {
/*  58 */     if (!isJndiAvailable()) {
/*  59 */       return 
/*  60 */         ConditionOutcome.noMatch(ConditionMessage.forCondition(ConditionalOnJndi.class, new Object[0])
/*  61 */         .notAvailable("JNDI environment"));
/*     */     }
/*  63 */     if (locations.length == 0) {
/*  64 */       return ConditionOutcome.match(
/*  65 */         ConditionMessage.forCondition(ConditionalOnJndi.class, new Object[0]).available("JNDI environment"));
/*     */     }
/*  67 */     JndiLocator locator = getJndiLocator(locations);
/*  68 */     String location = locator.lookupFirstLocation();
/*  69 */     String details = "(" + StringUtils.arrayToCommaDelimitedString(locations) + ")";
/*  70 */     if (location != null) {
/*  71 */       return 
/*  72 */         ConditionOutcome.match(ConditionMessage.forCondition(ConditionalOnJndi.class, new Object[] { details })
/*  73 */         .foundExactly("\"" + location + "\""));
/*     */     }
/*  75 */     return 
/*  76 */       ConditionOutcome.noMatch(ConditionMessage.forCondition(ConditionalOnJndi.class, new Object[] { details })
/*  77 */       .didNotFind("any matching JNDI location").atAll());
/*     */   }
/*     */   
/*     */   protected boolean isJndiAvailable() {
/*  81 */     return JndiLocatorDelegate.isDefaultJndiEnvironmentAvailable();
/*     */   }
/*     */   
/*     */   protected JndiLocator getJndiLocator(String[] locations) {
/*  85 */     return new JndiLocator(locations);
/*     */   }
/*     */   
/*     */   protected static class JndiLocator extends JndiLocatorSupport
/*     */   {
/*     */     private String[] locations;
/*     */     
/*     */     public JndiLocator(String[] locations) {
/*  93 */       this.locations = locations;
/*     */     }
/*     */     
/*     */     public String lookupFirstLocation() {
/*  97 */       for (String location : this.locations) {
/*     */         try {
/*  99 */           lookup(location);
/* 100 */           return location;
/*     */         }
/*     */         catch (NamingException localNamingException) {}
/*     */       }
/*     */       
/*     */ 
/* 106 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\condition\OnJndiCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */