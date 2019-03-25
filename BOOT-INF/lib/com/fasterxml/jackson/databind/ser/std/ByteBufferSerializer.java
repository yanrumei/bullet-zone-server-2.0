/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ public class ByteBufferSerializer extends StdScalarSerializer<ByteBuffer>
/*    */ {
/*    */   public ByteBufferSerializer()
/*    */   {
/* 13 */     super(ByteBuffer.class);
/*    */   }
/*    */   
/*    */   public void serialize(ByteBuffer bbuf, JsonGenerator gen, SerializerProvider provider)
/*    */     throws IOException
/*    */   {
/* 19 */     if (bbuf.hasArray()) {
/* 20 */       gen.writeBinary(bbuf.array(), 0, bbuf.limit());
/* 21 */       return;
/*    */     }
/*    */     
/*    */ 
/* 25 */     ByteBuffer copy = bbuf.asReadOnlyBuffer();
/* 26 */     if (copy.position() > 0) {
/* 27 */       copy.rewind();
/*    */     }
/* 29 */     InputStream in = new com.fasterxml.jackson.databind.util.ByteBufferBackedInputStream(copy);
/* 30 */     gen.writeBinary(in, copy.remaining());
/* 31 */     in.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\ByteBufferSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */