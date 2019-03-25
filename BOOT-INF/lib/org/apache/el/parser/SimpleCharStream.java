/*     */ package org.apache.el.parser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleCharStream
/*     */ {
/*     */   public static final boolean staticFlag = false;
/*     */   int bufsize;
/*     */   int available;
/*     */   int tokenBegin;
/*  19 */   public int bufpos = -1;
/*     */   
/*     */   protected int[] bufline;
/*     */   protected int[] bufcolumn;
/*  23 */   protected int column = 0;
/*  24 */   protected int line = 1;
/*     */   
/*  26 */   protected boolean prevCharIsCR = false;
/*  27 */   protected boolean prevCharIsLF = false;
/*     */   
/*     */   protected Reader inputStream;
/*     */   
/*     */   protected char[] buffer;
/*  32 */   protected int maxNextCharInd = 0;
/*  33 */   protected int inBuf = 0;
/*  34 */   protected int tabSize = 8;
/*     */   
/*  36 */   protected void setTabSize(int i) { this.tabSize = i; }
/*  37 */   protected int getTabSize(int i) { return this.tabSize; }
/*     */   
/*     */ 
/*     */   protected void ExpandBuff(boolean wrapAround)
/*     */   {
/*  42 */     char[] newbuffer = new char[this.bufsize + 2048];
/*  43 */     int[] newbufline = new int[this.bufsize + 2048];
/*  44 */     int[] newbufcolumn = new int[this.bufsize + 2048];
/*     */     
/*     */     try
/*     */     {
/*  48 */       if (wrapAround)
/*     */       {
/*  50 */         System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
/*  51 */         System.arraycopy(this.buffer, 0, newbuffer, this.bufsize - this.tokenBegin, this.bufpos);
/*  52 */         this.buffer = newbuffer;
/*     */         
/*  54 */         System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
/*  55 */         System.arraycopy(this.bufline, 0, newbufline, this.bufsize - this.tokenBegin, this.bufpos);
/*  56 */         this.bufline = newbufline;
/*     */         
/*  58 */         System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
/*  59 */         System.arraycopy(this.bufcolumn, 0, newbufcolumn, this.bufsize - this.tokenBegin, this.bufpos);
/*  60 */         this.bufcolumn = newbufcolumn;
/*     */         
/*  62 */         this.maxNextCharInd = (this.bufpos += this.bufsize - this.tokenBegin);
/*     */       }
/*     */       else
/*     */       {
/*  66 */         System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
/*  67 */         this.buffer = newbuffer;
/*     */         
/*  69 */         System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
/*  70 */         this.bufline = newbufline;
/*     */         
/*  72 */         System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
/*  73 */         this.bufcolumn = newbufcolumn;
/*     */         
/*  75 */         this.maxNextCharInd = (this.bufpos -= this.tokenBegin);
/*     */       }
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/*  80 */       throw new Error(t.getMessage());
/*     */     }
/*     */     
/*     */ 
/*  84 */     this.bufsize += 2048;
/*  85 */     this.available = this.bufsize;
/*  86 */     this.tokenBegin = 0;
/*     */   }
/*     */   
/*     */   protected void FillBuff() throws IOException
/*     */   {
/*  91 */     if (this.maxNextCharInd == this.available)
/*     */     {
/*  93 */       if (this.available == this.bufsize)
/*     */       {
/*  95 */         if (this.tokenBegin > 2048)
/*     */         {
/*  97 */           this.bufpos = (this.maxNextCharInd = 0);
/*  98 */           this.available = this.tokenBegin;
/*     */         }
/* 100 */         else if (this.tokenBegin < 0) {
/* 101 */           this.bufpos = (this.maxNextCharInd = 0);
/*     */         } else {
/* 103 */           ExpandBuff(false);
/*     */         }
/* 105 */       } else if (this.available > this.tokenBegin) {
/* 106 */         this.available = this.bufsize;
/* 107 */       } else if (this.tokenBegin - this.available < 2048) {
/* 108 */         ExpandBuff(true);
/*     */       } else {
/* 110 */         this.available = this.tokenBegin;
/*     */       }
/*     */     }
/*     */     try {
/*     */       int i;
/* 115 */       if ((i = this.inputStream.read(this.buffer, this.maxNextCharInd, this.available - this.maxNextCharInd)) == -1)
/*     */       {
/* 117 */         this.inputStream.close();
/* 118 */         throw new IOException();
/*     */       }
/*     */       
/* 121 */       this.maxNextCharInd += i;
/* 122 */       return;
/*     */     }
/*     */     catch (IOException e) {
/* 125 */       this.bufpos -= 1;
/* 126 */       backup(0);
/* 127 */       if (this.tokenBegin == -1)
/* 128 */         this.tokenBegin = this.bufpos;
/* 129 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */   public char BeginToken()
/*     */     throws IOException
/*     */   {
/* 136 */     this.tokenBegin = -1;
/* 137 */     char c = readChar();
/* 138 */     this.tokenBegin = this.bufpos;
/*     */     
/* 140 */     return c;
/*     */   }
/*     */   
/*     */   protected void UpdateLineColumn(char c)
/*     */   {
/* 145 */     this.column += 1;
/*     */     
/* 147 */     if (this.prevCharIsLF)
/*     */     {
/* 149 */       this.prevCharIsLF = false;
/* 150 */       this.line += (this.column = 1);
/*     */     }
/* 152 */     else if (this.prevCharIsCR)
/*     */     {
/* 154 */       this.prevCharIsCR = false;
/* 155 */       if (c == '\n')
/*     */       {
/* 157 */         this.prevCharIsLF = true;
/*     */       }
/*     */       else {
/* 160 */         this.line += (this.column = 1);
/*     */       }
/*     */     }
/* 163 */     switch (c)
/*     */     {
/*     */     case '\r': 
/* 166 */       this.prevCharIsCR = true;
/* 167 */       break;
/*     */     case '\n': 
/* 169 */       this.prevCharIsLF = true;
/* 170 */       break;
/*     */     case '\t': 
/* 172 */       this.column -= 1;
/* 173 */       this.column += this.tabSize - this.column % this.tabSize;
/* 174 */       break;
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 179 */     this.bufline[this.bufpos] = this.line;
/* 180 */     this.bufcolumn[this.bufpos] = this.column;
/*     */   }
/*     */   
/*     */   public char readChar()
/*     */     throws IOException
/*     */   {
/* 186 */     if (this.inBuf > 0)
/*     */     {
/* 188 */       this.inBuf -= 1;
/*     */       
/* 190 */       if (++this.bufpos == this.bufsize) {
/* 191 */         this.bufpos = 0;
/*     */       }
/* 193 */       return this.buffer[this.bufpos];
/*     */     }
/*     */     
/* 196 */     if (++this.bufpos >= this.maxNextCharInd) {
/* 197 */       FillBuff();
/*     */     }
/* 199 */     char c = this.buffer[this.bufpos];
/*     */     
/* 201 */     UpdateLineColumn(c);
/* 202 */     return c;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public int getColumn()
/*     */   {
/* 212 */     return this.bufcolumn[this.bufpos];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public int getLine()
/*     */   {
/* 222 */     return this.bufline[this.bufpos];
/*     */   }
/*     */   
/*     */   public int getEndColumn()
/*     */   {
/* 227 */     return this.bufcolumn[this.bufpos];
/*     */   }
/*     */   
/*     */   public int getEndLine()
/*     */   {
/* 232 */     return this.bufline[this.bufpos];
/*     */   }
/*     */   
/*     */   public int getBeginColumn()
/*     */   {
/* 237 */     return this.bufcolumn[this.tokenBegin];
/*     */   }
/*     */   
/*     */   public int getBeginLine()
/*     */   {
/* 242 */     return this.bufline[this.tokenBegin];
/*     */   }
/*     */   
/*     */ 
/*     */   public void backup(int amount)
/*     */   {
/* 248 */     this.inBuf += amount;
/* 249 */     if (this.bufpos -= amount < 0) {
/* 250 */       this.bufpos += this.bufsize;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public SimpleCharStream(Reader dstream, int startline, int startcolumn, int buffersize)
/*     */   {
/* 257 */     this.inputStream = dstream;
/* 258 */     this.line = startline;
/* 259 */     this.column = (startcolumn - 1);
/*     */     
/* 261 */     this.available = (this.bufsize = buffersize);
/* 262 */     this.buffer = new char[buffersize];
/* 263 */     this.bufline = new int[buffersize];
/* 264 */     this.bufcolumn = new int[buffersize];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SimpleCharStream(Reader dstream, int startline, int startcolumn)
/*     */   {
/* 271 */     this(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */   
/*     */ 
/*     */   public SimpleCharStream(Reader dstream)
/*     */   {
/* 277 */     this(dstream, 1, 1, 4096);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void ReInit(Reader dstream, int startline, int startcolumn, int buffersize)
/*     */   {
/* 284 */     this.inputStream = dstream;
/* 285 */     this.line = startline;
/* 286 */     this.column = (startcolumn - 1);
/*     */     
/* 288 */     if ((this.buffer == null) || (buffersize != this.buffer.length))
/*     */     {
/* 290 */       this.available = (this.bufsize = buffersize);
/* 291 */       this.buffer = new char[buffersize];
/* 292 */       this.bufline = new int[buffersize];
/* 293 */       this.bufcolumn = new int[buffersize];
/*     */     }
/* 295 */     this.prevCharIsLF = (this.prevCharIsCR = 0);
/* 296 */     this.tokenBegin = (this.inBuf = this.maxNextCharInd = 0);
/* 297 */     this.bufpos = -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void ReInit(Reader dstream, int startline, int startcolumn)
/*     */   {
/* 304 */     ReInit(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */   
/*     */ 
/*     */   public void ReInit(Reader dstream)
/*     */   {
/* 310 */     ReInit(dstream, 1, 1, 4096);
/*     */   }
/*     */   
/*     */   public SimpleCharStream(InputStream dstream, String encoding, int startline, int startcolumn, int buffersize)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 316 */     this(encoding == null ? new InputStreamReader(dstream) : new InputStreamReader(dstream, encoding), startline, startcolumn, buffersize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SimpleCharStream(InputStream dstream, int startline, int startcolumn, int buffersize)
/*     */   {
/* 323 */     this(new InputStreamReader(dstream), startline, startcolumn, buffersize);
/*     */   }
/*     */   
/*     */ 
/*     */   public SimpleCharStream(InputStream dstream, String encoding, int startline, int startcolumn)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 330 */     this(dstream, encoding, startline, startcolumn, 4096);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SimpleCharStream(InputStream dstream, int startline, int startcolumn)
/*     */   {
/* 337 */     this(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */   
/*     */   public SimpleCharStream(InputStream dstream, String encoding)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 343 */     this(dstream, encoding, 1, 1, 4096);
/*     */   }
/*     */   
/*     */ 
/*     */   public SimpleCharStream(InputStream dstream)
/*     */   {
/* 349 */     this(dstream, 1, 1, 4096);
/*     */   }
/*     */   
/*     */ 
/*     */   public void ReInit(InputStream dstream, String encoding, int startline, int startcolumn, int buffersize)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 356 */     ReInit(encoding == null ? new InputStreamReader(dstream) : new InputStreamReader(dstream, encoding), startline, startcolumn, buffersize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void ReInit(InputStream dstream, int startline, int startcolumn, int buffersize)
/*     */   {
/* 363 */     ReInit(new InputStreamReader(dstream), startline, startcolumn, buffersize);
/*     */   }
/*     */   
/*     */   public void ReInit(InputStream dstream, String encoding)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 369 */     ReInit(dstream, encoding, 1, 1, 4096);
/*     */   }
/*     */   
/*     */ 
/*     */   public void ReInit(InputStream dstream)
/*     */   {
/* 375 */     ReInit(dstream, 1, 1, 4096);
/*     */   }
/*     */   
/*     */   public void ReInit(InputStream dstream, String encoding, int startline, int startcolumn)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 381 */     ReInit(dstream, encoding, startline, startcolumn, 4096);
/*     */   }
/*     */   
/*     */ 
/*     */   public void ReInit(InputStream dstream, int startline, int startcolumn)
/*     */   {
/* 387 */     ReInit(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */   
/*     */   public String GetImage()
/*     */   {
/* 392 */     if (this.bufpos >= this.tokenBegin) {
/* 393 */       return new String(this.buffer, this.tokenBegin, this.bufpos - this.tokenBegin + 1);
/*     */     }
/* 395 */     return new String(this.buffer, this.tokenBegin, this.bufsize - this.tokenBegin) + new String(this.buffer, 0, this.bufpos + 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public char[] GetSuffix(int len)
/*     */   {
/* 402 */     char[] ret = new char[len];
/*     */     
/* 404 */     if (this.bufpos + 1 >= len) {
/* 405 */       System.arraycopy(this.buffer, this.bufpos - len + 1, ret, 0, len);
/*     */     }
/*     */     else {
/* 408 */       System.arraycopy(this.buffer, this.bufsize - (len - this.bufpos - 1), ret, 0, len - this.bufpos - 1);
/*     */       
/* 410 */       System.arraycopy(this.buffer, 0, ret, len - this.bufpos - 1, this.bufpos + 1);
/*     */     }
/*     */     
/* 413 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */   public void Done()
/*     */   {
/* 419 */     this.buffer = null;
/* 420 */     this.bufline = null;
/* 421 */     this.bufcolumn = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void adjustBeginLineColumn(int newLine, int newCol)
/*     */   {
/* 429 */     int start = this.tokenBegin;
/*     */     int len;
/*     */     int len;
/* 432 */     if (this.bufpos >= this.tokenBegin)
/*     */     {
/* 434 */       len = this.bufpos - this.tokenBegin + this.inBuf + 1;
/*     */     }
/*     */     else
/*     */     {
/* 438 */       len = this.bufsize - this.tokenBegin + this.bufpos + 1 + this.inBuf;
/*     */     }
/*     */     
/* 441 */     int i = 0;int j = 0;int k = 0;
/* 442 */     int nextColDiff = 0;int columnDiff = 0;
/*     */     
/* 444 */     while ((i < len) && (this.bufline[(j = start % this.bufsize)] == this.bufline[(k = ++start % this.bufsize)]))
/*     */     {
/* 446 */       this.bufline[j] = newLine;
/* 447 */       nextColDiff = columnDiff + this.bufcolumn[k] - this.bufcolumn[j];
/* 448 */       this.bufcolumn[j] = (newCol + columnDiff);
/* 449 */       columnDiff = nextColDiff;
/* 450 */       i++;
/*     */     }
/*     */     
/* 453 */     if (i < len)
/*     */     {
/* 455 */       this.bufline[j] = (newLine++);
/* 456 */       this.bufcolumn[j] = (newCol + columnDiff);
/*     */       
/* 458 */       while (i++ < len)
/*     */       {
/* 460 */         if (this.bufline[(j = start % this.bufsize)] != this.bufline[(++start % this.bufsize)]) {
/* 461 */           this.bufline[j] = (newLine++);
/*     */         } else {
/* 463 */           this.bufline[j] = newLine;
/*     */         }
/*     */       }
/*     */     }
/* 467 */     this.line = this.bufline[j];
/* 468 */     this.column = this.bufcolumn[j];
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\SimpleCharStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */