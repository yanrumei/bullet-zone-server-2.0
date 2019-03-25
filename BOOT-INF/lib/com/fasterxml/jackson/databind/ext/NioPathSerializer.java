/*    */ package com.fasterxml.jackson.databind.ext;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import java.nio.file.Path;
/*    */ 
/*    */ 
/*    */ public class NioPathSerializer
/*    */   extends StdScalarSerializer<Path>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public NioPathSerializer()
/*    */   {
/* 18 */     super(Path.class);
/*    */   }
/*    */   
/*    */   public void serialize(Path value, JsonGenerator gen, SerializerProvider serializers) throws IOException
/*    */   {
/* 23 */     gen.writeString(value.toUri().toString());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ext\NioPathSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */