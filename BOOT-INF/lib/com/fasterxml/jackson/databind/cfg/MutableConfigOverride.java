/*    */ package com.fasterxml.jackson.databind.cfg;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties.Value;
/*    */ import com.fasterxml.jackson.annotation.JsonInclude.Value;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MutableConfigOverride
/*    */   extends ConfigOverride
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public MutableConfigOverride() {}
/*    */   
/*    */   protected MutableConfigOverride(MutableConfigOverride src)
/*    */   {
/* 25 */     super(src);
/*    */   }
/*    */   
/*    */   protected MutableConfigOverride copy() {
/* 29 */     return new MutableConfigOverride(this);
/*    */   }
/*    */   
/*    */   public MutableConfigOverride setFormat(JsonFormat.Value v) {
/* 33 */     this._format = v;
/* 34 */     return this;
/*    */   }
/*    */   
/*    */   public MutableConfigOverride setInclude(JsonInclude.Value v) {
/* 38 */     this._include = v;
/* 39 */     return this;
/*    */   }
/*    */   
/*    */   public MutableConfigOverride setIgnorals(JsonIgnoreProperties.Value v) {
/* 43 */     this._ignorals = v;
/* 44 */     return this;
/*    */   }
/*    */   
/*    */   public MutableConfigOverride setIsIgnoredType(Boolean v) {
/* 48 */     this._isIgnoredType = v;
/* 49 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\cfg\MutableConfigOverride.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */