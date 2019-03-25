/*    */ package ch.qos.logback.core.encoder;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class ObjectStreamEncoder<E>
/*    */   extends EncoderBase<E>
/*    */ {
/*    */   public static final int START_PEBBLE = 1853421169;
/*    */   public static final int STOP_PEBBLE = 640373619;
/* 37 */   private int MAX_BUFFER_SIZE = 100;
/*    */   
/* 39 */   List<E> bufferList = new ArrayList(this.MAX_BUFFER_SIZE);
/*    */   
/*    */   public void doEncode(E event) throws IOException {
/* 42 */     this.bufferList.add(event);
/* 43 */     if (this.bufferList.size() == this.MAX_BUFFER_SIZE) {
/* 44 */       writeBuffer();
/*    */     }
/*    */   }
/*    */   
/*    */   void writeHeader(ByteArrayOutputStream baos, int bufferSize) {
/* 49 */     ByteArrayUtil.writeInt(baos, 1853421169);
/* 50 */     ByteArrayUtil.writeInt(baos, bufferSize);
/* 51 */     ByteArrayUtil.writeInt(baos, 0);
/* 52 */     ByteArrayUtil.writeInt(baos, 0x6E78F671 ^ bufferSize);
/*    */   }
/*    */   
/*    */   void writeFooter(ByteArrayOutputStream baos, int bufferSize) {
/* 56 */     ByteArrayUtil.writeInt(baos, 640373619);
/* 57 */     ByteArrayUtil.writeInt(baos, 0x262B5373 ^ bufferSize);
/*    */   }
/*    */   
/*    */   void writeBuffer() throws IOException {
/* 61 */     ByteArrayOutputStream baos = new ByteArrayOutputStream(10000);
/*    */     
/* 63 */     int size = this.bufferList.size();
/* 64 */     writeHeader(baos, size);
/* 65 */     ObjectOutputStream oos = new ObjectOutputStream(baos);
/* 66 */     for (E e : this.bufferList) {
/* 67 */       oos.writeObject(e);
/*    */     }
/* 69 */     this.bufferList.clear();
/* 70 */     oos.flush();
/*    */     
/* 72 */     writeFooter(baos, size);
/*    */     
/* 74 */     byte[] byteArray = baos.toByteArray();
/* 75 */     oos.close();
/* 76 */     writeEndPosition(byteArray);
/* 77 */     this.outputStream.write(byteArray);
/*    */   }
/*    */   
/*    */   void writeEndPosition(byte[] byteArray)
/*    */   {
/* 82 */     int offset = 8;
/* 83 */     ByteArrayUtil.writeInt(byteArray, offset, byteArray.length - offset);
/*    */   }
/*    */   
/*    */   public void init(OutputStream os) throws IOException
/*    */   {
/* 88 */     super.init(os);
/* 89 */     this.bufferList.clear();
/*    */   }
/*    */   
/*    */   public void close() throws IOException {
/* 93 */     writeBuffer();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\encoder\ObjectStreamEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */