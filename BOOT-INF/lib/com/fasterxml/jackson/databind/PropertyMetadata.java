/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertyMetadata
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -1L;
/*  16 */   public static final PropertyMetadata STD_REQUIRED = new PropertyMetadata(Boolean.TRUE, null, null, null);
/*     */   
/*  18 */   public static final PropertyMetadata STD_OPTIONAL = new PropertyMetadata(Boolean.FALSE, null, null, null);
/*     */   
/*  20 */   public static final PropertyMetadata STD_REQUIRED_OR_OPTIONAL = new PropertyMetadata(null, null, null, null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Boolean _required;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String _description;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Integer _index;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String _defaultValue;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected PropertyMetadata(Boolean req, String desc)
/*     */   {
/*  54 */     this(req, desc, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected PropertyMetadata(Boolean req, String desc, Integer index, String def)
/*     */   {
/*  61 */     this._required = req;
/*  62 */     this._description = desc;
/*  63 */     this._index = index;
/*  64 */     this._defaultValue = ((def == null) || (def.isEmpty()) ? null : def);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static PropertyMetadata construct(boolean req, String desc)
/*     */   {
/*  72 */     return construct(req, desc, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static PropertyMetadata construct(Boolean req, String desc, Integer index, String defaultValue)
/*     */   {
/*  80 */     if ((desc != null) || (index != null) || (defaultValue != null)) {
/*  81 */       return new PropertyMetadata(req, desc, index, defaultValue);
/*     */     }
/*  83 */     if (req == null) {
/*  84 */       return STD_REQUIRED_OR_OPTIONAL;
/*     */     }
/*  86 */     return req.booleanValue() ? STD_REQUIRED : STD_OPTIONAL;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static PropertyMetadata construct(boolean req, String desc, Integer index, String defaultValue)
/*     */   {
/*  92 */     if ((desc != null) || (index != null) || (defaultValue != null)) {
/*  93 */       return new PropertyMetadata(Boolean.valueOf(req), desc, index, defaultValue);
/*     */     }
/*  95 */     return req ? STD_REQUIRED : STD_OPTIONAL;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object readResolve()
/*     */   {
/* 104 */     if ((this._description == null) && (this._index == null) && (this._defaultValue == null)) {
/* 105 */       if (this._required == null) {
/* 106 */         return STD_REQUIRED_OR_OPTIONAL;
/*     */       }
/* 108 */       return this._required.booleanValue() ? STD_REQUIRED : STD_OPTIONAL;
/*     */     }
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   public PropertyMetadata withDescription(String desc) {
/* 114 */     return new PropertyMetadata(this._required, desc, this._index, this._defaultValue);
/*     */   }
/*     */   
/*     */   public PropertyMetadata withDefaultValue(String def) {
/* 118 */     if ((def == null) || (def.isEmpty())) {
/* 119 */       if (this._defaultValue == null) {
/* 120 */         return this;
/*     */       }
/* 122 */       def = null;
/* 123 */     } else if (this._defaultValue.equals(def)) {
/* 124 */       return this;
/*     */     }
/* 126 */     return new PropertyMetadata(this._required, this._description, this._index, def);
/*     */   }
/*     */   
/*     */   public PropertyMetadata withIndex(Integer index) {
/* 130 */     return new PropertyMetadata(this._required, this._description, index, this._defaultValue);
/*     */   }
/*     */   
/*     */   public PropertyMetadata withRequired(Boolean b) {
/* 134 */     if (b == null) {
/* 135 */       if (this._required == null) {
/* 136 */         return this;
/*     */       }
/*     */     }
/* 139 */     else if ((this._required != null) && (this._required.booleanValue() == b.booleanValue())) {
/* 140 */       return this;
/*     */     }
/*     */     
/* 143 */     return new PropertyMetadata(b, this._description, this._index, this._defaultValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 152 */     return this._description;
/*     */   }
/*     */   
/*     */   public String getDefaultValue()
/*     */   {
/* 157 */     return this._defaultValue;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public boolean hasDefuaultValue()
/*     */   {
/* 163 */     return hasDefaultValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 171 */   public boolean hasDefaultValue() { return this._defaultValue != null; }
/*     */   
/* 173 */   public boolean isRequired() { return (this._required != null) && (this._required.booleanValue()); }
/*     */   
/* 175 */   public Boolean getRequired() { return this._required; }
/*     */   
/*     */ 
/*     */   public Integer getIndex()
/*     */   {
/* 180 */     return this._index;
/*     */   }
/*     */   
/*     */   public boolean hasIndex()
/*     */   {
/* 185 */     return this._index != null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\PropertyMetadata.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */