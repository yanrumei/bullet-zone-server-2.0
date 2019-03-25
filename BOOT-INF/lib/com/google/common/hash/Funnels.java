/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import javax.annotation.Nullable;
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
/*     */ @Beta
/*     */ public final class Funnels
/*     */ {
/*     */   public static Funnel<byte[]> byteArrayFunnel()
/*     */   {
/*  38 */     return ByteArrayFunnel.INSTANCE;
/*     */   }
/*     */   
/*     */   private static enum ByteArrayFunnel implements Funnel<byte[]> {
/*  42 */     INSTANCE;
/*     */     
/*     */     private ByteArrayFunnel() {}
/*  45 */     public void funnel(byte[] from, PrimitiveSink into) { into.putBytes(from); }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/*  50 */       return "Funnels.byteArrayFunnel()";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Funnel<CharSequence> unencodedCharsFunnel()
/*     */   {
/*  62 */     return UnencodedCharsFunnel.INSTANCE;
/*     */   }
/*     */   
/*     */   private static enum UnencodedCharsFunnel implements Funnel<CharSequence> {
/*  66 */     INSTANCE;
/*     */     
/*     */     private UnencodedCharsFunnel() {}
/*  69 */     public void funnel(CharSequence from, PrimitiveSink into) { into.putUnencodedChars(from); }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/*  74 */       return "Funnels.unencodedCharsFunnel()";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Funnel<CharSequence> stringFunnel(Charset charset)
/*     */   {
/*  85 */     return new StringCharsetFunnel(charset);
/*     */   }
/*     */   
/*     */   private static class StringCharsetFunnel implements Funnel<CharSequence>, Serializable {
/*     */     private final Charset charset;
/*     */     
/*     */     StringCharsetFunnel(Charset charset) {
/*  92 */       this.charset = ((Charset)Preconditions.checkNotNull(charset));
/*     */     }
/*     */     
/*     */     public void funnel(CharSequence from, PrimitiveSink into) {
/*  96 */       into.putString(from, this.charset);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 101 */       return "Funnels.stringFunnel(" + this.charset.name() + ")";
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object o)
/*     */     {
/* 106 */       if ((o instanceof StringCharsetFunnel)) {
/* 107 */         StringCharsetFunnel funnel = (StringCharsetFunnel)o;
/* 108 */         return this.charset.equals(funnel.charset);
/*     */       }
/* 110 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 115 */       return StringCharsetFunnel.class.hashCode() ^ this.charset.hashCode();
/*     */     }
/*     */     
/*     */     Object writeReplace() {
/* 119 */       return new SerializedForm(this.charset);
/*     */     }
/*     */     
/*     */     private static class SerializedForm implements Serializable {
/*     */       private final String charsetCanonicalName;
/*     */       private static final long serialVersionUID = 0L;
/*     */       
/* 126 */       SerializedForm(Charset charset) { this.charsetCanonicalName = charset.name(); }
/*     */       
/*     */       private Object readResolve()
/*     */       {
/* 130 */         return Funnels.stringFunnel(Charset.forName(this.charsetCanonicalName));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Funnel<Integer> integerFunnel()
/*     */   {
/* 143 */     return IntegerFunnel.INSTANCE;
/*     */   }
/*     */   
/*     */   private static enum IntegerFunnel implements Funnel<Integer> {
/* 147 */     INSTANCE;
/*     */     
/*     */     private IntegerFunnel() {}
/* 150 */     public void funnel(Integer from, PrimitiveSink into) { into.putInt(from.intValue()); }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 155 */       return "Funnels.integerFunnel()";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Funnel<Iterable<? extends E>> sequentialFunnel(Funnel<E> elementFunnel)
/*     */   {
/* 166 */     return new SequentialFunnel(elementFunnel);
/*     */   }
/*     */   
/*     */   private static class SequentialFunnel<E> implements Funnel<Iterable<? extends E>>, Serializable {
/*     */     private final Funnel<E> elementFunnel;
/*     */     
/*     */     SequentialFunnel(Funnel<E> elementFunnel) {
/* 173 */       this.elementFunnel = ((Funnel)Preconditions.checkNotNull(elementFunnel));
/*     */     }
/*     */     
/*     */     public void funnel(Iterable<? extends E> from, PrimitiveSink into) {
/* 177 */       for (E e : from) {
/* 178 */         this.elementFunnel.funnel(e, into);
/*     */       }
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 184 */       return "Funnels.sequentialFunnel(" + this.elementFunnel + ")";
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object o)
/*     */     {
/* 189 */       if ((o instanceof SequentialFunnel)) {
/* 190 */         SequentialFunnel<?> funnel = (SequentialFunnel)o;
/* 191 */         return this.elementFunnel.equals(funnel.elementFunnel);
/*     */       }
/* 193 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 198 */       return SequentialFunnel.class.hashCode() ^ this.elementFunnel.hashCode();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Funnel<Long> longFunnel()
/*     */   {
/* 208 */     return LongFunnel.INSTANCE;
/*     */   }
/*     */   
/*     */   private static enum LongFunnel implements Funnel<Long> {
/* 212 */     INSTANCE;
/*     */     
/*     */     private LongFunnel() {}
/* 215 */     public void funnel(Long from, PrimitiveSink into) { into.putLong(from.longValue()); }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 220 */       return "Funnels.longFunnel()";
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
/*     */   public static OutputStream asOutputStream(PrimitiveSink sink)
/*     */   {
/* 235 */     return new SinkAsStream(sink);
/*     */   }
/*     */   
/*     */   private static class SinkAsStream extends OutputStream {
/*     */     final PrimitiveSink sink;
/*     */     
/*     */     SinkAsStream(PrimitiveSink sink) {
/* 242 */       this.sink = ((PrimitiveSink)Preconditions.checkNotNull(sink));
/*     */     }
/*     */     
/*     */     public void write(int b)
/*     */     {
/* 247 */       this.sink.putByte((byte)b);
/*     */     }
/*     */     
/*     */     public void write(byte[] bytes)
/*     */     {
/* 252 */       this.sink.putBytes(bytes);
/*     */     }
/*     */     
/*     */     public void write(byte[] bytes, int off, int len)
/*     */     {
/* 257 */       this.sink.putBytes(bytes, off, len);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 262 */       return "Funnels.asOutputStream(" + this.sink + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\hash\Funnels.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */