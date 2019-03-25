/*     */ package javax.servlet;
/*     */ 
/*     */ import java.io.CharConversionException;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ResourceBundle;
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
/*     */ public abstract class ServletOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private static final String LSTRING_FILE = "javax.servlet.LocalStrings";
/*  39 */   private static final ResourceBundle lStrings = ResourceBundle.getBundle("javax.servlet.LocalStrings");
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
/*     */   public void print(String s)
/*     */     throws IOException
/*     */   {
/*  58 */     if (s == null)
/*  59 */       s = "null";
/*  60 */     int len = s.length();
/*  61 */     for (int i = 0; i < len; i++) {
/*  62 */       char c = s.charAt(i);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  70 */       if ((c & 0xFF00) != 0) {
/*  71 */         String errMsg = lStrings.getString("err.not_iso8859_1");
/*  72 */         Object[] errArgs = new Object[1];
/*  73 */         errArgs[0] = Character.valueOf(c);
/*  74 */         errMsg = MessageFormat.format(errMsg, errArgs);
/*  75 */         throw new CharConversionException(errMsg);
/*     */       }
/*  77 */       write(c);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void print(boolean b)
/*     */     throws IOException
/*     */   {
/*     */     String msg;
/*     */     
/*     */ 
/*     */     String msg;
/*     */     
/*     */ 
/*  92 */     if (b) {
/*  93 */       msg = lStrings.getString("value.true");
/*     */     } else {
/*  95 */       msg = lStrings.getString("value.false");
/*     */     }
/*  97 */     print(msg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void print(char c)
/*     */     throws IOException
/*     */   {
/* 110 */     print(String.valueOf(c));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void print(int i)
/*     */     throws IOException
/*     */   {
/* 123 */     print(String.valueOf(i));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void print(long l)
/*     */     throws IOException
/*     */   {
/* 136 */     print(String.valueOf(l));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void print(float f)
/*     */     throws IOException
/*     */   {
/* 149 */     print(String.valueOf(f));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void print(double d)
/*     */     throws IOException
/*     */   {
/* 162 */     print(String.valueOf(d));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void println()
/*     */     throws IOException
/*     */   {
/* 172 */     print("\r\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void println(String s)
/*     */     throws IOException
/*     */   {
/* 185 */     print(s);
/* 186 */     println();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void println(boolean b)
/*     */     throws IOException
/*     */   {
/* 199 */     print(b);
/* 200 */     println();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void println(char c)
/*     */     throws IOException
/*     */   {
/* 213 */     print(c);
/* 214 */     println();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void println(int i)
/*     */     throws IOException
/*     */   {
/* 227 */     print(i);
/* 228 */     println();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void println(long l)
/*     */     throws IOException
/*     */   {
/* 241 */     print(l);
/* 242 */     println();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void println(float f)
/*     */     throws IOException
/*     */   {
/* 255 */     print(f);
/* 256 */     println();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void println(double d)
/*     */     throws IOException
/*     */   {
/* 269 */     print(d);
/* 270 */     println();
/*     */   }
/*     */   
/*     */   public abstract boolean isReady();
/*     */   
/*     */   public abstract void setWriteListener(WriteListener paramWriteListener);
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\ServletOutputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */