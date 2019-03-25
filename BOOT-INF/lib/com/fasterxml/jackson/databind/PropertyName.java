/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.core.SerializableString;
/*     */ import com.fasterxml.jackson.core.io.SerializedString;
/*     */ import com.fasterxml.jackson.core.util.InternCache;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import java.io.Serializable;
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
/*     */ public class PropertyName
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final String _USE_DEFAULT = "";
/*     */   private static final String _NO_NAME = "";
/*  28 */   public static final PropertyName USE_DEFAULT = new PropertyName("", null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  35 */   public static final PropertyName NO_NAME = new PropertyName(new String(""), null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String _simpleName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String _namespace;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SerializableString _encodedSimple;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertyName(String simpleName)
/*     */   {
/*  60 */     this(simpleName, null);
/*     */   }
/*     */   
/*     */   public PropertyName(String simpleName, String namespace)
/*     */   {
/*  65 */     this._simpleName = (simpleName == null ? "" : simpleName);
/*  66 */     this._namespace = namespace;
/*     */   }
/*     */   
/*     */   protected Object readResolve()
/*     */   {
/*  71 */     if ((this._simpleName == null) || ("".equals(this._simpleName))) {
/*  72 */       return USE_DEFAULT;
/*     */     }
/*  74 */     if ((this._simpleName.equals("")) && (this._namespace == null)) {
/*  75 */       return NO_NAME;
/*     */     }
/*  77 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static PropertyName construct(String simpleName)
/*     */   {
/*  85 */     if ((simpleName == null) || (simpleName.length() == 0)) {
/*  86 */       return USE_DEFAULT;
/*     */     }
/*  88 */     return new PropertyName(InternCache.instance.intern(simpleName), null);
/*     */   }
/*     */   
/*     */   public static PropertyName construct(String simpleName, String ns)
/*     */   {
/*  93 */     if (simpleName == null) {
/*  94 */       simpleName = "";
/*     */     }
/*  96 */     if ((ns == null) && (simpleName.length() == 0)) {
/*  97 */       return USE_DEFAULT;
/*     */     }
/*  99 */     return new PropertyName(InternCache.instance.intern(simpleName), ns);
/*     */   }
/*     */   
/*     */   public PropertyName internSimpleName()
/*     */   {
/* 104 */     if (this._simpleName.length() == 0) {
/* 105 */       return this;
/*     */     }
/* 107 */     String interned = InternCache.instance.intern(this._simpleName);
/* 108 */     if (interned == this._simpleName) {
/* 109 */       return this;
/*     */     }
/* 111 */     return new PropertyName(interned, this._namespace);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertyName withSimpleName(String simpleName)
/*     */   {
/* 120 */     if (simpleName == null) {
/* 121 */       simpleName = "";
/*     */     }
/* 123 */     if (simpleName.equals(this._simpleName)) {
/* 124 */       return this;
/*     */     }
/* 126 */     return new PropertyName(simpleName, this._namespace);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertyName withNamespace(String ns)
/*     */   {
/* 134 */     if (ns == null) {
/* 135 */       if (this._namespace == null) {
/* 136 */         return this;
/*     */       }
/* 138 */     } else if (ns.equals(this._namespace)) {
/* 139 */       return this;
/*     */     }
/* 141 */     return new PropertyName(this._simpleName, ns);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSimpleName()
/*     */   {
/* 151 */     return this._simpleName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SerializableString simpleAsEncoded(MapperConfig<?> config)
/*     */   {
/* 161 */     SerializableString sstr = this._encodedSimple;
/* 162 */     if (sstr == null) {
/* 163 */       if (config == null) {
/* 164 */         sstr = new SerializedString(this._simpleName);
/*     */       } else {
/* 166 */         sstr = config.compileString(this._simpleName);
/*     */       }
/* 168 */       this._encodedSimple = sstr;
/*     */     }
/* 170 */     return sstr;
/*     */   }
/*     */   
/*     */   public String getNamespace() {
/* 174 */     return this._namespace;
/*     */   }
/*     */   
/*     */   public boolean hasSimpleName() {
/* 178 */     return this._simpleName.length() > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean hasSimpleName(String str)
/*     */   {
/* 185 */     if (str == null) {
/* 186 */       return this._simpleName == null;
/*     */     }
/* 188 */     return str.equals(this._simpleName);
/*     */   }
/*     */   
/*     */   public boolean hasNamespace() {
/* 192 */     return this._namespace != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 204 */     return (this._namespace == null) && (this._simpleName.isEmpty());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 216 */     if (o == this) return true;
/* 217 */     if (o == null) { return false;
/*     */     }
/*     */     
/*     */ 
/* 221 */     if (o.getClass() != getClass()) { return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 226 */     PropertyName other = (PropertyName)o;
/* 227 */     if (this._simpleName == null) {
/* 228 */       if (other._simpleName != null) return false;
/* 229 */     } else if (!this._simpleName.equals(other._simpleName)) {
/* 230 */       return false;
/*     */     }
/* 232 */     if (this._namespace == null) {
/* 233 */       return null == other._namespace;
/*     */     }
/* 235 */     return this._namespace.equals(other._namespace);
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 240 */     if (this._namespace == null) {
/* 241 */       return this._simpleName.hashCode();
/*     */     }
/* 243 */     return this._namespace.hashCode() ^ this._simpleName.hashCode();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 248 */     if (this._namespace == null) {
/* 249 */       return this._simpleName;
/*     */     }
/* 251 */     return "{" + this._namespace + "}" + this._simpleName;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\PropertyName.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */