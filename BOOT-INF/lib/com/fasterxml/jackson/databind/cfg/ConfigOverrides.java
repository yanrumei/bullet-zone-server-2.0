/*    */ package com.fasterxml.jackson.databind.cfg;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConfigOverrides
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected Map<Class<?>, MutableConfigOverride> _overrides;
/*    */   
/*    */   public ConfigOverrides()
/*    */   {
/* 18 */     this._overrides = null;
/*    */   }
/*    */   
/*    */   protected ConfigOverrides(Map<Class<?>, MutableConfigOverride> overrides) {
/* 22 */     this._overrides = overrides;
/*    */   }
/*    */   
/*    */   public ConfigOverrides copy()
/*    */   {
/* 27 */     if (this._overrides == null) {
/* 28 */       return new ConfigOverrides();
/*    */     }
/* 30 */     Map<Class<?>, MutableConfigOverride> newOverrides = _newMap();
/* 31 */     for (Map.Entry<Class<?>, MutableConfigOverride> entry : this._overrides.entrySet()) {
/* 32 */       newOverrides.put(entry.getKey(), ((MutableConfigOverride)entry.getValue()).copy());
/*    */     }
/* 34 */     return new ConfigOverrides(newOverrides);
/*    */   }
/*    */   
/*    */   public ConfigOverride findOverride(Class<?> type) {
/* 38 */     if (this._overrides == null) {
/* 39 */       return null;
/*    */     }
/* 41 */     return (ConfigOverride)this._overrides.get(type);
/*    */   }
/*    */   
/*    */   public MutableConfigOverride findOrCreateOverride(Class<?> type) {
/* 45 */     if (this._overrides == null) {
/* 46 */       this._overrides = _newMap();
/*    */     }
/* 48 */     MutableConfigOverride override = (MutableConfigOverride)this._overrides.get(type);
/* 49 */     if (override == null) {
/* 50 */       override = new MutableConfigOverride();
/* 51 */       this._overrides.put(type, override);
/*    */     }
/* 53 */     return override;
/*    */   }
/*    */   
/*    */   protected Map<Class<?>, MutableConfigOverride> _newMap() {
/* 57 */     return new HashMap();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\cfg\ConfigOverrides.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */