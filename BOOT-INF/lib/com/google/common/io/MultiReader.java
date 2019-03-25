/*    */ package com.google.common.io;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
/*    */ import java.util.Iterator;
/*    */ import javax.annotation.Nullable;
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
/*    */ @GwtIncompatible
/*    */ class MultiReader
/*    */   extends Reader
/*    */ {
/*    */   private final Iterator<? extends CharSource> it;
/*    */   private Reader current;
/*    */   
/*    */   MultiReader(Iterator<? extends CharSource> readers)
/*    */     throws IOException
/*    */   {
/* 36 */     this.it = readers;
/* 37 */     advance();
/*    */   }
/*    */   
/*    */ 
/*    */   private void advance()
/*    */     throws IOException
/*    */   {
/* 44 */     close();
/* 45 */     if (this.it.hasNext()) {
/* 46 */       this.current = ((CharSource)this.it.next()).openStream();
/*    */     }
/*    */   }
/*    */   
/*    */   public int read(@Nullable char[] cbuf, int off, int len) throws IOException
/*    */   {
/* 52 */     if (this.current == null) {
/* 53 */       return -1;
/*    */     }
/* 55 */     int result = this.current.read(cbuf, off, len);
/* 56 */     if (result == -1) {
/* 57 */       advance();
/* 58 */       return read(cbuf, off, len);
/*    */     }
/* 60 */     return result;
/*    */   }
/*    */   
/*    */   public long skip(long n) throws IOException
/*    */   {
/* 65 */     Preconditions.checkArgument(n >= 0L, "n is negative");
/* 66 */     if (n > 0L) {
/* 67 */       while (this.current != null) {
/* 68 */         long result = this.current.skip(n);
/* 69 */         if (result > 0L) {
/* 70 */           return result;
/*    */         }
/* 72 */         advance();
/*    */       }
/*    */     }
/* 75 */     return 0L;
/*    */   }
/*    */   
/*    */   public boolean ready() throws IOException
/*    */   {
/* 80 */     return (this.current != null) && (this.current.ready());
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public void close()
/*    */     throws IOException
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: getfield 9	com/google/common/io/MultiReader:current	Ljava/io/Reader;
/*    */     //   4: ifnull +26 -> 30
/*    */     //   7: aload_0
/*    */     //   8: getfield 9	com/google/common/io/MultiReader:current	Ljava/io/Reader;
/*    */     //   11: invokevirtual 16	java/io/Reader:close	()V
/*    */     //   14: aload_0
/*    */     //   15: aconst_null
/*    */     //   16: putfield 9	com/google/common/io/MultiReader:current	Ljava/io/Reader;
/*    */     //   19: goto +11 -> 30
/*    */     //   22: astore_1
/*    */     //   23: aload_0
/*    */     //   24: aconst_null
/*    */     //   25: putfield 9	com/google/common/io/MultiReader:current	Ljava/io/Reader;
/*    */     //   28: aload_1
/*    */     //   29: athrow
/*    */     //   30: return
/*    */     // Line number table:
/*    */     //   Java source line #85	-> byte code offset #0
/*    */     //   Java source line #87	-> byte code offset #7
/*    */     //   Java source line #89	-> byte code offset #14
/*    */     //   Java source line #90	-> byte code offset #19
/*    */     //   Java source line #89	-> byte code offset #22
/*    */     //   Java source line #92	-> byte code offset #30
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	31	0	this	MultiReader
/*    */     //   22	7	1	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   7	14	22	finally
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\io\MultiReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */