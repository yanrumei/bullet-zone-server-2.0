/*     */ package ch.qos.logback.core.encoder;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class EventObjectInputStream<E>
/*     */   extends InputStream
/*     */ {
/*     */   NonClosableInputStream ncis;
/*  36 */   List<E> buffer = new ArrayList();
/*     */   
/*  38 */   int index = 0;
/*     */   
/*     */   EventObjectInputStream(InputStream is) throws IOException {
/*  41 */     this.ncis = new NonClosableInputStream(is);
/*     */   }
/*     */   
/*     */   public int read() throws IOException
/*     */   {
/*  46 */     throw new UnsupportedOperationException("Only the readEvent method is supported.");
/*     */   }
/*     */   
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/*  53 */     return this.ncis.available();
/*     */   }
/*     */   
/*     */   public E readEvent() throws IOException
/*     */   {
/*  58 */     E event = getFromBuffer();
/*  59 */     if (event != null) {
/*  60 */       return event;
/*     */     }
/*     */     
/*  63 */     internalReset();
/*  64 */     int count = readHeader();
/*  65 */     if (count == -1) {
/*  66 */       return null;
/*     */     }
/*  68 */     readPayload(count);
/*  69 */     readFooter(count);
/*  70 */     return (E)getFromBuffer();
/*     */   }
/*     */   
/*     */   private void internalReset() {
/*  74 */     this.index = 0;
/*  75 */     this.buffer.clear();
/*     */   }
/*     */   
/*     */   E getFromBuffer() {
/*  79 */     if (this.index >= this.buffer.size()) {
/*  80 */       return null;
/*     */     }
/*  82 */     return (E)this.buffer.get(this.index++);
/*     */   }
/*     */   
/*     */   int readHeader() throws IOException {
/*  86 */     byte[] headerBA = new byte[16];
/*     */     
/*  88 */     int bytesRead = this.ncis.read(headerBA);
/*  89 */     if (bytesRead == -1) {
/*  90 */       return -1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  96 */     int offset = 0;
/*  97 */     int startPebble = ByteArrayUtil.readInt(headerBA, offset);
/*  98 */     if (startPebble != 1853421169) {
/*  99 */       throw new IllegalStateException("Does not look like data created by ObjectStreamEncoder");
/*     */     }
/* 101 */     offset += 4;
/* 102 */     int count = ByteArrayUtil.readInt(headerBA, offset);
/* 103 */     offset += 4;
/* 104 */     int endPointer = ByteArrayUtil.readInt(headerBA, offset);
/* 105 */     offset += 4;
/* 106 */     int checksum = ByteArrayUtil.readInt(headerBA, offset);
/* 107 */     if (checksum != (0x6E78F671 ^ count)) {
/* 108 */       throw new IllegalStateException("Invalid checksum");
/*     */     }
/* 110 */     return count;
/*     */   }
/*     */   
/*     */   E readEvents(ObjectInputStream ois) throws IOException
/*     */   {
/* 115 */     E e = null;
/*     */     try {
/* 117 */       e = ois.readObject();
/* 118 */       this.buffer.add(e);
/*     */     }
/*     */     catch (ClassNotFoundException e1) {
/* 121 */       e1.printStackTrace();
/*     */     }
/* 123 */     return e;
/*     */   }
/*     */   
/*     */   void readFooter(int count) throws IOException {
/* 127 */     byte[] headerBA = new byte[8];
/* 128 */     this.ncis.read(headerBA);
/*     */     
/* 130 */     int offset = 0;
/* 131 */     int stopPebble = ByteArrayUtil.readInt(headerBA, offset);
/* 132 */     if (stopPebble != 640373619) {
/* 133 */       throw new IllegalStateException("Looks like a corrupt stream");
/*     */     }
/* 135 */     offset += 4;
/* 136 */     int checksum = ByteArrayUtil.readInt(headerBA, offset);
/* 137 */     if (checksum != (0x262B5373 ^ count)) {
/* 138 */       throw new IllegalStateException("Invalid checksum");
/*     */     }
/*     */   }
/*     */   
/*     */   void readPayload(int count) throws IOException {
/* 143 */     List<E> eventList = new ArrayList(count);
/* 144 */     ObjectInputStream ois = new ObjectInputStream(this.ncis);
/* 145 */     for (int i = 0; i < count; i++) {
/* 146 */       E e = readEvents(ois);
/* 147 */       eventList.add(e);
/*     */     }
/* 149 */     ois.close();
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 153 */     this.ncis.realClose();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\encoder\EventObjectInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */