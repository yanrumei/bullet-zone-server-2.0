/*     */ package org.apache.tomcat.util.descriptor.web;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Locale;
/*     */ import javax.servlet.DispatcherType;
/*     */ import org.apache.tomcat.util.buf.UDecoder;
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
/*     */ public class FilterMap
/*     */   extends XmlEncodingBase
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public static final int ERROR = 1;
/*     */   public static final int FORWARD = 2;
/*     */   public static final int INCLUDE = 4;
/*     */   public static final int REQUEST = 8;
/*     */   public static final int ASYNC = 16;
/*     */   private static final int NOT_SET = 0;
/*  58 */   private int dispatcherMapping = 0;
/*     */   
/*  60 */   private String filterName = null;
/*     */   
/*     */   public String getFilterName() {
/*  63 */     return this.filterName;
/*     */   }
/*     */   
/*     */   public void setFilterName(String filterName) {
/*  67 */     this.filterName = filterName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  74 */   private String[] servletNames = new String[0];
/*     */   
/*     */   public String[] getServletNames() {
/*  77 */     if (this.matchAllServletNames) {
/*  78 */       return new String[0];
/*     */     }
/*  80 */     return this.servletNames;
/*     */   }
/*     */   
/*     */   public void addServletName(String servletName)
/*     */   {
/*  85 */     if ("*".equals(servletName)) {
/*  86 */       this.matchAllServletNames = true;
/*     */     } else {
/*  88 */       String[] results = new String[this.servletNames.length + 1];
/*  89 */       System.arraycopy(this.servletNames, 0, results, 0, this.servletNames.length);
/*  90 */       results[this.servletNames.length] = servletName;
/*  91 */       this.servletNames = results;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  99 */   private boolean matchAllUrlPatterns = false;
/*     */   
/*     */   public boolean getMatchAllUrlPatterns() {
/* 102 */     return this.matchAllUrlPatterns;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 109 */   private boolean matchAllServletNames = false;
/*     */   
/*     */   public boolean getMatchAllServletNames() {
/* 112 */     return this.matchAllServletNames;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 119 */   private String[] urlPatterns = new String[0];
/*     */   
/*     */   public String[] getURLPatterns() {
/* 122 */     if (this.matchAllUrlPatterns) {
/* 123 */       return new String[0];
/*     */     }
/* 125 */     return this.urlPatterns;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 130 */   public void addURLPattern(String urlPattern) { addURLPatternDecoded(UDecoder.URLDecode(urlPattern, getCharset())); }
/*     */   
/*     */   public void addURLPatternDecoded(String urlPattern) {
/* 133 */     if ("*".equals(urlPattern)) {
/* 134 */       this.matchAllUrlPatterns = true;
/*     */     } else {
/* 136 */       String[] results = new String[this.urlPatterns.length + 1];
/* 137 */       System.arraycopy(this.urlPatterns, 0, results, 0, this.urlPatterns.length);
/* 138 */       results[this.urlPatterns.length] = UDecoder.URLDecode(urlPattern);
/* 139 */       this.urlPatterns = results;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDispatcher(String dispatcherString)
/*     */   {
/* 150 */     String dispatcher = dispatcherString.toUpperCase(Locale.ENGLISH);
/*     */     
/* 152 */     if (dispatcher.equals(DispatcherType.FORWARD.name()))
/*     */     {
/* 154 */       this.dispatcherMapping |= 0x2;
/* 155 */     } else if (dispatcher.equals(DispatcherType.INCLUDE.name()))
/*     */     {
/* 157 */       this.dispatcherMapping |= 0x4;
/* 158 */     } else if (dispatcher.equals(DispatcherType.REQUEST.name()))
/*     */     {
/* 160 */       this.dispatcherMapping |= 0x8;
/* 161 */     } else if (dispatcher.equals(DispatcherType.ERROR.name()))
/*     */     {
/* 163 */       this.dispatcherMapping |= 0x1;
/* 164 */     } else if (dispatcher.equals(DispatcherType.ASYNC.name()))
/*     */     {
/* 166 */       this.dispatcherMapping |= 0x10;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int getDispatcherMapping()
/*     */   {
/* 173 */     if (this.dispatcherMapping == 0) { return 8;
/*     */     }
/* 175 */     return this.dispatcherMapping;
/*     */   }
/*     */   
/*     */   public String[] getDispatcherNames() {
/* 179 */     ArrayList<String> result = new ArrayList();
/* 180 */     if ((this.dispatcherMapping & 0x2) != 0) {
/* 181 */       result.add(DispatcherType.FORWARD.name());
/*     */     }
/* 183 */     if ((this.dispatcherMapping & 0x4) != 0) {
/* 184 */       result.add(DispatcherType.INCLUDE.name());
/*     */     }
/* 186 */     if ((this.dispatcherMapping & 0x8) != 0) {
/* 187 */       result.add(DispatcherType.REQUEST.name());
/*     */     }
/* 189 */     if ((this.dispatcherMapping & 0x1) != 0) {
/* 190 */       result.add(DispatcherType.ERROR.name());
/*     */     }
/* 192 */     if ((this.dispatcherMapping & 0x10) != 0) {
/* 193 */       result.add(DispatcherType.ASYNC.name());
/*     */     }
/* 195 */     return (String[])result.toArray(new String[result.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 207 */     StringBuilder sb = new StringBuilder("FilterMap[");
/* 208 */     sb.append("filterName=");
/* 209 */     sb.append(this.filterName);
/* 210 */     for (int i = 0; i < this.servletNames.length; i++) {
/* 211 */       sb.append(", servletName=");
/* 212 */       sb.append(this.servletNames[i]);
/*     */     }
/* 214 */     for (int i = 0; i < this.urlPatterns.length; i++) {
/* 215 */       sb.append(", urlPattern=");
/* 216 */       sb.append(this.urlPatterns[i]);
/*     */     }
/* 218 */     sb.append("]");
/* 219 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\FilterMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */