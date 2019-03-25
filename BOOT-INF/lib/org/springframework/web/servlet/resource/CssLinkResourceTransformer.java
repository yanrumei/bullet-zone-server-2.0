/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringWriter;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.util.FileCopyUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class CssLinkResourceTransformer
/*     */   extends ResourceTransformerSupport
/*     */ {
/*  52 */   private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
/*     */   
/*  54 */   private static final Log logger = LogFactory.getLog(CssLinkResourceTransformer.class);
/*     */   
/*  56 */   private final List<CssLinkParser> linkParsers = new ArrayList(2);
/*     */   
/*     */   public CssLinkResourceTransformer()
/*     */   {
/*  60 */     this.linkParsers.add(new ImportStatementCssLinkParser(null));
/*  61 */     this.linkParsers.add(new UrlFunctionCssLinkParser(null));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain)
/*     */     throws IOException
/*     */   {
/*  69 */     resource = transformerChain.transform(request, resource);
/*     */     
/*  71 */     String filename = resource.getFilename();
/*  72 */     if (!"css".equals(StringUtils.getFilenameExtension(filename))) {
/*  73 */       return resource;
/*     */     }
/*     */     
/*  76 */     if (logger.isTraceEnabled()) {
/*  77 */       logger.trace("Transforming resource: " + resource);
/*     */     }
/*     */     
/*  80 */     byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
/*  81 */     String content = new String(bytes, DEFAULT_CHARSET);
/*     */     
/*  83 */     Set<CssLinkInfo> infos = new HashSet(8);
/*  84 */     for (CssLinkParser parser : this.linkParsers) {
/*  85 */       parser.parseLink(content, infos);
/*     */     }
/*     */     
/*  88 */     if (infos.isEmpty()) {
/*  89 */       if (logger.isTraceEnabled()) {
/*  90 */         logger.trace("No links found.");
/*     */       }
/*  92 */       return resource;
/*     */     }
/*     */     
/*  95 */     Object sortedInfos = new ArrayList(infos);
/*  96 */     Collections.sort((List)sortedInfos);
/*     */     
/*  98 */     int index = 0;
/*  99 */     StringWriter writer = new StringWriter();
/* 100 */     for (CssLinkInfo info : (List)sortedInfos) {
/* 101 */       writer.write(content.substring(index, info.getStart()));
/* 102 */       String link = content.substring(info.getStart(), info.getEnd());
/* 103 */       String newLink = null;
/* 104 */       if (!hasScheme(link)) {
/* 105 */         newLink = resolveUrlPath(link, request, resource, transformerChain);
/*     */       }
/* 107 */       if (logger.isTraceEnabled()) {
/* 108 */         if ((newLink != null) && (!link.equals(newLink))) {
/* 109 */           logger.trace("Link modified: " + newLink + " (original: " + link + ")");
/*     */         }
/*     */         else {
/* 112 */           logger.trace("Link not modified: " + link);
/*     */         }
/*     */       }
/* 115 */       writer.write(newLink != null ? newLink : link);
/* 116 */       index = info.getEnd();
/*     */     }
/* 118 */     writer.write(content.substring(index));
/*     */     
/* 120 */     return new TransformedResource(resource, writer.toString().getBytes(DEFAULT_CHARSET));
/*     */   }
/*     */   
/*     */   private boolean hasScheme(String link) {
/* 124 */     int schemeIndex = link.indexOf(":");
/* 125 */     return ((schemeIndex > 0) && (!link.substring(0, schemeIndex).contains("/"))) || (link.indexOf("//") == 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected static abstract interface CssLinkParser
/*     */   {
/*     */     public abstract void parseLink(String paramString, Set<CssLinkResourceTransformer.CssLinkInfo> paramSet);
/*     */   }
/*     */   
/*     */ 
/*     */   protected static abstract class AbstractCssLinkParser
/*     */     implements CssLinkResourceTransformer.CssLinkParser
/*     */   {
/*     */     protected abstract String getKeyword();
/*     */     
/*     */ 
/*     */     public void parseLink(String content, Set<CssLinkResourceTransformer.CssLinkInfo> linkInfos)
/*     */     {
/* 144 */       int index = 0;
/*     */       for (;;) {
/* 146 */         index = content.indexOf(getKeyword(), index);
/* 147 */         if (index == -1) {
/*     */           break;
/*     */         }
/* 150 */         index = skipWhitespace(content, index + getKeyword().length());
/* 151 */         if (content.charAt(index) == '\'') {
/* 152 */           index = addLink(index, "'", content, linkInfos);
/*     */         }
/* 154 */         else if (content.charAt(index) == '"') {
/* 155 */           index = addLink(index, "\"", content, linkInfos);
/*     */         }
/*     */         else {
/* 158 */           index = extractLink(index, content, linkInfos);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private int skipWhitespace(String content, int index)
/*     */     {
/* 167 */       while (Character.isWhitespace(content.charAt(index))) {
/* 168 */         index++;
/*     */       }
/*     */       
/* 171 */       return index;
/*     */     }
/*     */     
/*     */     protected int addLink(int index, String endKey, String content, Set<CssLinkResourceTransformer.CssLinkInfo> linkInfos)
/*     */     {
/* 176 */       int start = index + 1;
/* 177 */       int end = content.indexOf(endKey, start);
/* 178 */       linkInfos.add(new CssLinkResourceTransformer.CssLinkInfo(start, end));
/* 179 */       return end + endKey.length();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected abstract int extractLink(int paramInt, String paramString, Set<CssLinkResourceTransformer.CssLinkInfo> paramSet);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class ImportStatementCssLinkParser
/*     */     extends CssLinkResourceTransformer.AbstractCssLinkParser
/*     */   {
/*     */     protected String getKeyword()
/*     */     {
/* 195 */       return "@import";
/*     */     }
/*     */     
/*     */     protected int extractLink(int index, String content, Set<CssLinkResourceTransformer.CssLinkInfo> linkInfos)
/*     */     {
/* 200 */       if (!content.substring(index, index + 4).equals("url("))
/*     */       {
/*     */ 
/* 203 */         if (CssLinkResourceTransformer.logger.isErrorEnabled())
/* 204 */           CssLinkResourceTransformer.logger.error("Unexpected syntax for @import link at index " + index);
/*     */       }
/* 206 */       return index;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class UrlFunctionCssLinkParser
/*     */     extends CssLinkResourceTransformer.AbstractCssLinkParser
/*     */   {
/*     */     protected String getKeyword()
/*     */     {
/* 215 */       return "url(";
/*     */     }
/*     */     
/*     */ 
/*     */     protected int extractLink(int index, String content, Set<CssLinkResourceTransformer.CssLinkInfo> linkInfos)
/*     */     {
/* 221 */       return addLink(index - 1, ")", content, linkInfos);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CssLinkInfo
/*     */     implements Comparable<CssLinkInfo>
/*     */   {
/*     */     private final int start;
/*     */     private final int end;
/*     */     
/*     */     public CssLinkInfo(int start, int end)
/*     */     {
/* 233 */       this.start = start;
/* 234 */       this.end = end;
/*     */     }
/*     */     
/*     */     public int getStart() {
/* 238 */       return this.start;
/*     */     }
/*     */     
/*     */     public int getEnd() {
/* 242 */       return this.end;
/*     */     }
/*     */     
/*     */     public int compareTo(CssLinkInfo other)
/*     */     {
/* 247 */       return this.start == other.start ? 0 : this.start < other.start ? -1 : 1;
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 252 */       if (this == obj) {
/* 253 */         return true;
/*     */       }
/* 255 */       if ((obj != null) && ((obj instanceof CssLinkInfo))) {
/* 256 */         CssLinkInfo other = (CssLinkInfo)obj;
/* 257 */         return (this.start == other.start) && (this.end == other.end);
/*     */       }
/* 259 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 264 */       return this.start * 31 + this.end;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\CssLinkResourceTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */