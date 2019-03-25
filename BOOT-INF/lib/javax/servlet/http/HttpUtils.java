/*     */ package javax.servlet.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Hashtable;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.servlet.ServletInputStream;
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
/*     */ /**
/*     */  * @deprecated
/*     */  */
/*     */ public class HttpUtils
/*     */ {
/*     */   private static final String LSTRING_FILE = "javax.servlet.http.LocalStrings";
/*  39 */   private static final ResourceBundle lStrings = ResourceBundle.getBundle("javax.servlet.http.LocalStrings");
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
/*     */   public static Hashtable<String, String[]> parseQueryString(String s)
/*     */   {
/*  84 */     String[] valArray = null;
/*     */     
/*  86 */     if (s == null) {
/*  87 */       throw new IllegalArgumentException();
/*     */     }
/*  89 */     Hashtable<String, String[]> ht = new Hashtable();
/*  90 */     StringBuilder sb = new StringBuilder();
/*  91 */     StringTokenizer st = new StringTokenizer(s, "&");
/*  92 */     while (st.hasMoreTokens()) {
/*  93 */       String pair = st.nextToken();
/*  94 */       int pos = pair.indexOf('=');
/*  95 */       if (pos == -1)
/*     */       {
/*     */ 
/*  98 */         throw new IllegalArgumentException();
/*     */       }
/* 100 */       String key = parseName(pair.substring(0, pos), sb);
/* 101 */       String val = parseName(pair.substring(pos + 1, pair.length()), sb);
/* 102 */       if (ht.containsKey(key)) {
/* 103 */         String[] oldVals = (String[])ht.get(key);
/* 104 */         valArray = new String[oldVals.length + 1];
/* 105 */         for (int i = 0; i < oldVals.length; i++)
/* 106 */           valArray[i] = oldVals[i];
/* 107 */         valArray[oldVals.length] = val;
/*     */       } else {
/* 109 */         valArray = new String[1];
/* 110 */         valArray[0] = val;
/*     */       }
/* 112 */       ht.put(key, valArray);
/*     */     }
/* 114 */     return ht;
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
/*     */   public static Hashtable<String, String[]> parsePostData(int len, ServletInputStream in)
/*     */   {
/* 163 */     if (len <= 0) {
/* 164 */       return new Hashtable();
/*     */     }
/* 166 */     if (in == null) {
/* 167 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/*     */ 
/* 171 */     byte[] postedBytes = new byte[len];
/*     */     try {
/* 173 */       int offset = 0;
/*     */       do
/*     */       {
/* 176 */         int inputLen = in.read(postedBytes, offset, len - offset);
/* 177 */         if (inputLen <= 0) {
/* 178 */           String msg = lStrings.getString("err.io.short_read");
/* 179 */           throw new IllegalArgumentException(msg);
/*     */         }
/* 181 */         offset += inputLen;
/* 182 */       } while (len - offset > 0);
/*     */     }
/*     */     catch (IOException e) {
/* 185 */       throw new IllegalArgumentException(e.getMessage(), e);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 192 */       String postedBody = new String(postedBytes, 0, len, "8859_1");
/* 193 */       return parseQueryString(postedBody);
/*     */     }
/*     */     catch (UnsupportedEncodingException e)
/*     */     {
/* 197 */       throw new IllegalArgumentException(e.getMessage(), e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String parseName(String s, StringBuilder sb)
/*     */   {
/* 206 */     sb.setLength(0);
/* 207 */     for (int i = 0; i < s.length(); i++) {
/* 208 */       char c = s.charAt(i);
/* 209 */       switch (c) {
/*     */       case '+': 
/* 211 */         sb.append(' ');
/* 212 */         break;
/*     */       case '%': 
/*     */         try {
/* 215 */           sb.append((char)Integer.parseInt(s.substring(i + 1, i + 3), 16));
/*     */           
/* 217 */           i += 2;
/*     */         }
/*     */         catch (NumberFormatException e)
/*     */         {
/* 221 */           throw new IllegalArgumentException();
/*     */         } catch (StringIndexOutOfBoundsException e) {
/* 223 */           String rest = s.substring(i);
/* 224 */           sb.append(rest);
/* 225 */           if (rest.length() == 2) {
/* 226 */             i++;
/*     */           }
/*     */         }
/*     */       
/*     */       default: 
/* 231 */         sb.append(c);
/*     */       }
/*     */       
/*     */     }
/* 235 */     return sb.toString();
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
/*     */   public static StringBuffer getRequestURL(HttpServletRequest req)
/*     */   {
/* 262 */     StringBuffer url = new StringBuffer();
/* 263 */     String scheme = req.getScheme();
/* 264 */     int port = req.getServerPort();
/* 265 */     String urlPath = req.getRequestURI();
/*     */     
/* 267 */     url.append(scheme);
/* 268 */     url.append("://");
/* 269 */     url.append(req.getServerName());
/* 270 */     if (((scheme.equals("http")) && (port != 80)) || ((scheme.equals("https")) && (port != 443))) {
/* 271 */       url.append(':');
/* 272 */       url.append(req.getServerPort());
/*     */     }
/*     */     
/* 275 */     url.append(urlPath);
/* 276 */     return url;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\http\HttpUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */