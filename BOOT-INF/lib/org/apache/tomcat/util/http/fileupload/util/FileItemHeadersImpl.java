/*    */ package org.apache.tomcat.util.http.fileupload.util;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.apache.tomcat.util.http.fileupload.FileItemHeaders;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileItemHeadersImpl
/*    */   implements FileItemHeaders, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -4455695752627032559L;
/* 47 */   private final Map<String, List<String>> headerNameToValueListMap = new LinkedHashMap();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getHeader(String name)
/*    */   {
/* 55 */     String nameLower = name.toLowerCase(Locale.ENGLISH);
/* 56 */     List<String> headerValueList = (List)this.headerNameToValueListMap.get(nameLower);
/* 57 */     if (null == headerValueList) {
/* 58 */       return null;
/*    */     }
/* 60 */     return (String)headerValueList.get(0);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Iterator<String> getHeaderNames()
/*    */   {
/* 68 */     return this.headerNameToValueListMap.keySet().iterator();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Iterator<String> getHeaders(String name)
/*    */   {
/* 76 */     String nameLower = name.toLowerCase(Locale.ENGLISH);
/* 77 */     List<String> headerValueList = (List)this.headerNameToValueListMap.get(nameLower);
/* 78 */     if (null == headerValueList) {
/* 79 */       headerValueList = Collections.emptyList();
/*    */     }
/* 81 */     return headerValueList.iterator();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public synchronized void addHeader(String name, String value)
/*    */   {
/* 91 */     String nameLower = name.toLowerCase(Locale.ENGLISH);
/* 92 */     List<String> headerValueList = (List)this.headerNameToValueListMap.get(nameLower);
/* 93 */     if (null == headerValueList) {
/* 94 */       headerValueList = new ArrayList();
/* 95 */       this.headerNameToValueListMap.put(nameLower, headerValueList);
/*    */     }
/* 97 */     headerValueList.add(value);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileuploa\\util\FileItemHeadersImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */