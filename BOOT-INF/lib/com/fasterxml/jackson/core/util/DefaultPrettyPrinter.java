/*     */ package com.fasterxml.jackson.core.util;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.PrettyPrinter;
/*     */ import com.fasterxml.jackson.core.SerializableString;
/*     */ import com.fasterxml.jackson.core.io.SerializedString;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ public class DefaultPrettyPrinter
/*     */   implements PrettyPrinter, Instantiatable<DefaultPrettyPrinter>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  28 */   public static final SerializedString DEFAULT_ROOT_VALUE_SEPARATOR = new SerializedString(" ");
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
/*  52 */   protected Indenter _arrayIndenter = FixedSpaceIndenter.instance;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  60 */   protected Indenter _objectIndenter = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final SerializableString _rootSeparator;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  74 */   protected boolean _spacesInObjectEntries = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected transient int _nesting;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultPrettyPrinter()
/*     */   {
/*  91 */     this(DEFAULT_ROOT_VALUE_SEPARATOR);
/*     */   }
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
/*     */   public DefaultPrettyPrinter(String rootSeparator)
/*     */   {
/* 106 */     this(rootSeparator == null ? null : new SerializedString(rootSeparator));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultPrettyPrinter(SerializableString rootSeparator)
/*     */   {
/* 118 */     this._rootSeparator = rootSeparator;
/*     */   }
/*     */   
/*     */   public DefaultPrettyPrinter(DefaultPrettyPrinter base) {
/* 122 */     this(base, base._rootSeparator);
/*     */   }
/*     */   
/*     */ 
/*     */   public DefaultPrettyPrinter(DefaultPrettyPrinter base, SerializableString rootSeparator)
/*     */   {
/* 128 */     this._arrayIndenter = base._arrayIndenter;
/* 129 */     this._objectIndenter = base._objectIndenter;
/* 130 */     this._spacesInObjectEntries = base._spacesInObjectEntries;
/* 131 */     this._nesting = base._nesting;
/*     */     
/* 133 */     this._rootSeparator = rootSeparator;
/*     */   }
/*     */   
/*     */   public DefaultPrettyPrinter withRootSeparator(SerializableString rootSeparator)
/*     */   {
/* 138 */     if ((this._rootSeparator == rootSeparator) || ((rootSeparator != null) && (rootSeparator.equals(this._rootSeparator))))
/*     */     {
/* 140 */       return this;
/*     */     }
/* 142 */     return new DefaultPrettyPrinter(this, rootSeparator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DefaultPrettyPrinter withRootSeparator(String rootSeparator)
/*     */   {
/* 149 */     return withRootSeparator(rootSeparator == null ? null : new SerializedString(rootSeparator));
/*     */   }
/*     */   
/*     */   public void indentArraysWith(Indenter i) {
/* 153 */     this._arrayIndenter = (i == null ? NopIndenter.instance : i);
/*     */   }
/*     */   
/*     */   public void indentObjectsWith(Indenter i) {
/* 157 */     this._objectIndenter = (i == null ? NopIndenter.instance : i);
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public void spacesInObjectEntries(boolean b)
/*     */   {
/* 164 */     this._spacesInObjectEntries = b;
/*     */   }
/*     */   
/*     */ 
/*     */   public DefaultPrettyPrinter withArrayIndenter(Indenter i)
/*     */   {
/* 170 */     if (i == null) {
/* 171 */       i = NopIndenter.instance;
/*     */     }
/* 173 */     if (this._arrayIndenter == i) {
/* 174 */       return this;
/*     */     }
/* 176 */     DefaultPrettyPrinter pp = new DefaultPrettyPrinter(this);
/* 177 */     pp._arrayIndenter = i;
/* 178 */     return pp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DefaultPrettyPrinter withObjectIndenter(Indenter i)
/*     */   {
/* 185 */     if (i == null) {
/* 186 */       i = NopIndenter.instance;
/*     */     }
/* 188 */     if (this._objectIndenter == i) {
/* 189 */       return this;
/*     */     }
/* 191 */     DefaultPrettyPrinter pp = new DefaultPrettyPrinter(this);
/* 192 */     pp._objectIndenter = i;
/* 193 */     return pp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultPrettyPrinter withSpacesInObjectEntries()
/*     */   {
/* 205 */     return _withSpaces(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultPrettyPrinter withoutSpacesInObjectEntries()
/*     */   {
/* 217 */     return _withSpaces(false);
/*     */   }
/*     */   
/*     */   protected DefaultPrettyPrinter _withSpaces(boolean state)
/*     */   {
/* 222 */     if (this._spacesInObjectEntries == state) {
/* 223 */       return this;
/*     */     }
/* 225 */     DefaultPrettyPrinter pp = new DefaultPrettyPrinter(this);
/* 226 */     pp._spacesInObjectEntries = state;
/* 227 */     return pp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultPrettyPrinter createInstance()
/*     */   {
/* 238 */     return new DefaultPrettyPrinter(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeRootValueSeparator(JsonGenerator jg)
/*     */     throws IOException
/*     */   {
/* 250 */     if (this._rootSeparator != null) {
/* 251 */       jg.writeRaw(this._rootSeparator);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeStartObject(JsonGenerator jg)
/*     */     throws IOException
/*     */   {
/* 258 */     jg.writeRaw('{');
/* 259 */     if (!this._objectIndenter.isInline()) {
/* 260 */       this._nesting += 1;
/*     */     }
/*     */   }
/*     */   
/*     */   public void beforeObjectEntries(JsonGenerator jg)
/*     */     throws IOException
/*     */   {
/* 267 */     this._objectIndenter.writeIndentation(jg, this._nesting);
/*     */   }
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
/*     */   public void writeObjectFieldValueSeparator(JsonGenerator jg)
/*     */     throws IOException
/*     */   {
/* 282 */     if (this._spacesInObjectEntries) {
/* 283 */       jg.writeRaw(" : ");
/*     */     } else {
/* 285 */       jg.writeRaw(':');
/*     */     }
/*     */   }
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
/*     */   public void writeObjectEntrySeparator(JsonGenerator jg)
/*     */     throws IOException
/*     */   {
/* 301 */     jg.writeRaw(',');
/* 302 */     this._objectIndenter.writeIndentation(jg, this._nesting);
/*     */   }
/*     */   
/*     */   public void writeEndObject(JsonGenerator jg, int nrOfEntries)
/*     */     throws IOException
/*     */   {
/* 308 */     if (!this._objectIndenter.isInline()) {
/* 309 */       this._nesting -= 1;
/*     */     }
/* 311 */     if (nrOfEntries > 0) {
/* 312 */       this._objectIndenter.writeIndentation(jg, this._nesting);
/*     */     } else {
/* 314 */       jg.writeRaw(' ');
/*     */     }
/* 316 */     jg.writeRaw('}');
/*     */   }
/*     */   
/*     */   public void writeStartArray(JsonGenerator jg)
/*     */     throws IOException
/*     */   {
/* 322 */     if (!this._arrayIndenter.isInline()) {
/* 323 */       this._nesting += 1;
/*     */     }
/* 325 */     jg.writeRaw('[');
/*     */   }
/*     */   
/*     */   public void beforeArrayValues(JsonGenerator jg) throws IOException
/*     */   {
/* 330 */     this._arrayIndenter.writeIndentation(jg, this._nesting);
/*     */   }
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
/*     */   public void writeArrayValueSeparator(JsonGenerator gen)
/*     */     throws IOException
/*     */   {
/* 345 */     gen.writeRaw(',');
/* 346 */     this._arrayIndenter.writeIndentation(gen, this._nesting);
/*     */   }
/*     */   
/*     */   public void writeEndArray(JsonGenerator gen, int nrOfValues)
/*     */     throws IOException
/*     */   {
/* 352 */     if (!this._arrayIndenter.isInline()) {
/* 353 */       this._nesting -= 1;
/*     */     }
/* 355 */     if (nrOfValues > 0) {
/* 356 */       this._arrayIndenter.writeIndentation(gen, this._nesting);
/*     */     } else {
/* 358 */       gen.writeRaw(' ');
/*     */     }
/* 360 */     gen.writeRaw(']');
/*     */   }
/*     */   
/*     */ 
/*     */   public static abstract interface Indenter
/*     */   {
/*     */     public abstract void writeIndentation(JsonGenerator paramJsonGenerator, int paramInt)
/*     */       throws IOException;
/*     */     
/*     */     public abstract boolean isInline();
/*     */   }
/*     */   
/*     */   public static class NopIndenter
/*     */     implements DefaultPrettyPrinter.Indenter, Serializable
/*     */   {
/* 375 */     public static final NopIndenter instance = new NopIndenter();
/*     */     
/*     */     public void writeIndentation(JsonGenerator jg, int level) throws IOException
/*     */     {}
/*     */     
/*     */     public boolean isInline() {
/* 381 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class FixedSpaceIndenter
/*     */     extends DefaultPrettyPrinter.NopIndenter
/*     */   {
/* 392 */     public static final FixedSpaceIndenter instance = new FixedSpaceIndenter();
/*     */     
/*     */     public void writeIndentation(JsonGenerator jg, int level)
/*     */       throws IOException
/*     */     {
/* 397 */       jg.writeRaw(' ');
/*     */     }
/*     */     
/*     */     public boolean isInline() {
/* 401 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\cor\\util\DefaultPrettyPrinter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */