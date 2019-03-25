/*     */ package org.apache.tomcat.util.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.util.Enumeration;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ public class MimeHeaders
/*     */ {
/*     */   public static final int DEFAULT_HEADER_SIZE = 8;
/*  98 */   private static final StringManager sm = StringManager.getManager("org.apache.tomcat.util.http");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 103 */   private MimeHeaderField[] headers = new MimeHeaderField[8];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int count;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 114 */   private int limit = -1;
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
/*     */   public void setLimit(int limit)
/*     */   {
/* 128 */     this.limit = limit;
/* 129 */     if ((limit > 0) && (this.headers.length > limit) && (this.count < limit))
/*     */     {
/* 131 */       MimeHeaderField[] tmp = new MimeHeaderField[limit];
/* 132 */       System.arraycopy(this.headers, 0, tmp, 0, this.count);
/* 133 */       this.headers = tmp;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void recycle()
/*     */   {
/* 142 */     clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/* 149 */     for (int i = 0; i < this.count; i++) {
/* 150 */       this.headers[i].recycle();
/*     */     }
/* 152 */     this.count = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 160 */     StringWriter sw = new StringWriter();
/* 161 */     PrintWriter pw = new PrintWriter(sw);
/* 162 */     pw.println("=== MimeHeaders ===");
/* 163 */     Enumeration<String> e = names();
/* 164 */     while (e.hasMoreElements()) {
/* 165 */       String n = (String)e.nextElement();
/* 166 */       Enumeration<String> ev = values(n);
/* 167 */       while (ev.hasMoreElements()) {
/* 168 */         pw.print(n);
/* 169 */         pw.print(" = ");
/* 170 */         pw.println((String)ev.nextElement());
/*     */       }
/*     */     }
/* 173 */     return sw.toString();
/*     */   }
/*     */   
/*     */   public void duplicate(MimeHeaders source) throws IOException
/*     */   {
/* 178 */     for (int i = 0; i < source.size(); i++) {
/* 179 */       MimeHeaderField mhf = createHeader();
/* 180 */       mhf.getName().duplicate(source.getName(i));
/* 181 */       mhf.getValue().duplicate(source.getValue(i));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 192 */     return this.count;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MessageBytes getName(int n)
/*     */   {
/* 201 */     return (n >= 0) && (n < this.count) ? this.headers[n].getName() : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MessageBytes getValue(int n)
/*     */   {
/* 210 */     return (n >= 0) && (n < this.count) ? this.headers[n].getValue() : null;
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
/*     */ 
/*     */ 
/*     */   public int findHeader(String name, int starting)
/*     */   {
/* 227 */     for (int i = starting; i < this.count; i++) {
/* 228 */       if (this.headers[i].getName().equalsIgnoreCase(name)) {
/* 229 */         return i;
/*     */       }
/*     */     }
/* 232 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Enumeration<String> names()
/*     */   {
/* 244 */     return new NamesEnumerator(this);
/*     */   }
/*     */   
/*     */   public Enumeration<String> values(String name) {
/* 248 */     return new ValuesEnumerator(this, name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private MimeHeaderField createHeader()
/*     */   {
/* 259 */     if ((this.limit > -1) && (this.count >= this.limit)) {
/* 260 */       throw new IllegalStateException(sm.getString("headers.maxCountFail", new Object[] {
/* 261 */         Integer.valueOf(this.limit) }));
/*     */     }
/*     */     
/* 264 */     int len = this.headers.length;
/* 265 */     if (this.count >= len)
/*     */     {
/* 267 */       int newLength = this.count * 2;
/* 268 */       if ((this.limit > 0) && (newLength > this.limit)) {
/* 269 */         newLength = this.limit;
/*     */       }
/* 271 */       MimeHeaderField[] tmp = new MimeHeaderField[newLength];
/* 272 */       System.arraycopy(this.headers, 0, tmp, 0, len);
/* 273 */       this.headers = tmp; }
/*     */     MimeHeaderField mh;
/* 275 */     if ((mh = this.headers[this.count]) == null) {
/* 276 */       this.headers[this.count] = (mh = new MimeHeaderField());
/*     */     }
/* 278 */     this.count += 1;
/* 279 */     return mh;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MessageBytes addValue(String name)
/*     */   {
/* 289 */     MimeHeaderField mh = createHeader();
/* 290 */     mh.getName().setString(name);
/* 291 */     return mh.getValue();
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
/*     */   public MessageBytes addValue(byte[] b, int startN, int len)
/*     */   {
/* 305 */     MimeHeaderField mhf = createHeader();
/* 306 */     mhf.getName().setBytes(b, startN, len);
/* 307 */     return mhf.getValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MessageBytes setValue(String name)
/*     */   {
/* 317 */     for (int i = 0; i < this.count; i++) {
/* 318 */       if (this.headers[i].getName().equalsIgnoreCase(name)) {
/* 319 */         for (int j = i + 1; j < this.count; j++) {
/* 320 */           if (this.headers[j].getName().equalsIgnoreCase(name)) {
/* 321 */             removeHeader(j--);
/*     */           }
/*     */         }
/* 324 */         return this.headers[i].getValue();
/*     */       }
/*     */     }
/* 327 */     MimeHeaderField mh = createHeader();
/* 328 */     mh.getName().setString(name);
/* 329 */     return mh.getValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MessageBytes getValue(String name)
/*     */   {
/* 341 */     for (int i = 0; i < this.count; i++) {
/* 342 */       if (this.headers[i].getName().equalsIgnoreCase(name)) {
/* 343 */         return this.headers[i].getValue();
/*     */       }
/*     */     }
/* 346 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MessageBytes getUniqueValue(String name)
/*     */   {
/* 358 */     MessageBytes result = null;
/* 359 */     for (int i = 0; i < this.count; i++) {
/* 360 */       if (this.headers[i].getName().equalsIgnoreCase(name)) {
/* 361 */         if (result == null) {
/* 362 */           result = this.headers[i].getValue();
/*     */         } else {
/* 364 */           throw new IllegalArgumentException();
/*     */         }
/*     */       }
/*     */     }
/* 368 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getHeader(String name)
/*     */   {
/* 374 */     MessageBytes mh = getValue(name);
/* 375 */     return mh != null ? mh.toString() : null;
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
/*     */   public void removeHeader(String name)
/*     */   {
/* 388 */     for (int i = 0; i < this.count; i++) {
/* 389 */       if (this.headers[i].getName().equalsIgnoreCase(name)) {
/* 390 */         removeHeader(i--);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void removeHeader(int idx)
/*     */   {
/* 400 */     MimeHeaderField mh = this.headers[idx];
/*     */     
/* 402 */     mh.recycle();
/* 403 */     this.headers[idx] = this.headers[(this.count - 1)];
/* 404 */     this.headers[(this.count - 1)] = mh;
/* 405 */     this.count -= 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\MimeHeaders.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */