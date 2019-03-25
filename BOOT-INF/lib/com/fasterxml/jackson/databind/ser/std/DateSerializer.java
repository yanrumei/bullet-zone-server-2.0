/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*    */ import java.io.IOException;
/*    */ import java.text.DateFormat;
/*    */ import java.util.Date;
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
/*    */ @JacksonStdImpl
/*    */ public class DateSerializer
/*    */   extends DateTimeSerializerBase<Date>
/*    */ {
/* 24 */   public static final DateSerializer instance = new DateSerializer();
/*    */   
/*    */   public DateSerializer() {
/* 27 */     this(null, null);
/*    */   }
/*    */   
/*    */   public DateSerializer(Boolean useTimestamp, DateFormat customFormat) {
/* 31 */     super(Date.class, useTimestamp, customFormat);
/*    */   }
/*    */   
/*    */   public DateSerializer withFormat(Boolean timestamp, DateFormat customFormat)
/*    */   {
/* 36 */     return new DateSerializer(timestamp, customFormat);
/*    */   }
/*    */   
/*    */   protected long _timestamp(Date value)
/*    */   {
/* 41 */     return value == null ? 0L : value.getTime();
/*    */   }
/*    */   
/*    */   public void serialize(Date value, JsonGenerator gen, SerializerProvider provider)
/*    */     throws IOException
/*    */   {
/* 47 */     if (_asTimestamp(provider)) {
/* 48 */       gen.writeNumber(_timestamp(value));
/* 49 */     } else if (this._customFormat != null)
/*    */     {
/* 51 */       synchronized (this._customFormat) {
/* 52 */         gen.writeString(this._customFormat.format(value));
/*    */       }
/*    */     } else {
/* 55 */       provider.defaultSerializeDateValue(value, gen);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\DateSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */