/*    */ package org.springframework.boot.autoconfigure.hateoas;
/*    */ 
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
/*    */ 
/*    */ @ConfigurationProperties(prefix="spring.hateoas")
/*    */ public class HateoasProperties
/*    */ {
/* 35 */   private boolean useHalAsDefaultJsonMediaType = true;
/*    */   
/*    */   public boolean getUseHalAsDefaultJsonMediaType() {
/* 38 */     return this.useHalAsDefaultJsonMediaType;
/*    */   }
/*    */   
/*    */   public void setUseHalAsDefaultJsonMediaType(boolean useHalAsDefaultJsonMediaType) {
/* 42 */     this.useHalAsDefaultJsonMediaType = useHalAsDefaultJsonMediaType;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\hateoas\HateoasProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */