/*     */ package org.springframework.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.springframework.core.io.InputStreamResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.ResourceRegion;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class HttpRange
/*     */ {
/*     */   private static final String BYTE_RANGE_PREFIX = "bytes=";
/*     */   
/*     */   public ResourceRegion toResourceRegion(Resource resource)
/*     */   {
/*  59 */     Assert.isTrue(resource.getClass() != InputStreamResource.class, "Cannot convert an InputStreamResource to a ResourceRegion");
/*     */     try
/*     */     {
/*  62 */       long contentLength = resource.contentLength();
/*  63 */       Assert.isTrue(contentLength > 0L, "Resource content length should be > 0");
/*  64 */       long start = getRangeStart(contentLength);
/*  65 */       long end = getRangeEnd(contentLength);
/*  66 */       return new ResourceRegion(resource, start, end - start + 1L);
/*     */     }
/*     */     catch (IOException ex) {
/*  69 */       throw new IllegalArgumentException("Failed to convert Resource to ResourceRegion", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract long getRangeStart(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract long getRangeEnd(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HttpRange createByteRange(long firstBytePos)
/*     */   {
/*  95 */     return new ByteRange(firstBytePos, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HttpRange createByteRange(long firstBytePos, long lastBytePos)
/*     */   {
/* 106 */     return new ByteRange(firstBytePos, Long.valueOf(lastBytePos));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HttpRange createSuffixRange(long suffixLength)
/*     */   {
/* 116 */     return new SuffixByteRange(suffixLength);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<HttpRange> parseRanges(String ranges)
/*     */   {
/* 127 */     if (!StringUtils.hasLength(ranges)) {
/* 128 */       return Collections.emptyList();
/*     */     }
/* 130 */     if (!ranges.startsWith("bytes=")) {
/* 131 */       throw new IllegalArgumentException("Range '" + ranges + "' does not start with 'bytes='");
/*     */     }
/* 133 */     ranges = ranges.substring("bytes=".length());
/*     */     
/* 135 */     String[] tokens = StringUtils.tokenizeToStringArray(ranges, ",");
/* 136 */     List<HttpRange> result = new ArrayList(tokens.length);
/* 137 */     for (String token : tokens) {
/* 138 */       result.add(parseRange(token));
/*     */     }
/* 140 */     return result;
/*     */   }
/*     */   
/*     */   private static HttpRange parseRange(String range) {
/* 144 */     Assert.hasLength(range, "Range String must not be empty");
/* 145 */     int dashIdx = range.indexOf('-');
/* 146 */     if (dashIdx > 0) {
/* 147 */       long firstPos = Long.parseLong(range.substring(0, dashIdx));
/* 148 */       if (dashIdx < range.length() - 1) {
/* 149 */         Long lastPos = Long.valueOf(Long.parseLong(range.substring(dashIdx + 1, range.length())));
/* 150 */         return new ByteRange(firstPos, lastPos);
/*     */       }
/*     */       
/* 153 */       return new ByteRange(firstPos, null);
/*     */     }
/*     */     
/* 156 */     if (dashIdx == 0) {
/* 157 */       long suffixLength = Long.parseLong(range.substring(1));
/* 158 */       return new SuffixByteRange(suffixLength);
/*     */     }
/*     */     
/* 161 */     throw new IllegalArgumentException("Range '" + range + "' does not contain \"-\"");
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
/*     */   public static List<ResourceRegion> toResourceRegions(List<HttpRange> ranges, Resource resource)
/*     */   {
/* 174 */     if (CollectionUtils.isEmpty(ranges)) {
/* 175 */       return Collections.emptyList();
/*     */     }
/* 177 */     List<ResourceRegion> regions = new ArrayList(ranges.size());
/* 178 */     for (HttpRange range : ranges) {
/* 179 */       regions.add(range.toResourceRegion(resource));
/*     */     }
/* 181 */     return regions;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toString(Collection<HttpRange> ranges)
/*     */   {
/* 191 */     Assert.notEmpty(ranges, "Ranges Collection must not be empty");
/* 192 */     StringBuilder builder = new StringBuilder("bytes=");
/* 193 */     for (Iterator<HttpRange> iterator = ranges.iterator(); iterator.hasNext();) {
/* 194 */       HttpRange range = (HttpRange)iterator.next();
/* 195 */       builder.append(range);
/* 196 */       if (iterator.hasNext()) {
/* 197 */         builder.append(", ");
/*     */       }
/*     */     }
/* 200 */     return builder.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class ByteRange
/*     */     extends HttpRange
/*     */   {
/*     */     private final long firstPos;
/*     */     
/*     */ 
/*     */     private final Long lastPos;
/*     */     
/*     */ 
/*     */ 
/*     */     public ByteRange(long firstPos, Long lastPos)
/*     */     {
/* 217 */       assertPositions(firstPos, lastPos);
/* 218 */       this.firstPos = firstPos;
/* 219 */       this.lastPos = lastPos;
/*     */     }
/*     */     
/*     */     private void assertPositions(long firstBytePos, Long lastBytePos) {
/* 223 */       if (firstBytePos < 0L) {
/* 224 */         throw new IllegalArgumentException("Invalid first byte position: " + firstBytePos);
/*     */       }
/* 226 */       if ((lastBytePos != null) && (lastBytePos.longValue() < firstBytePos)) {
/* 227 */         throw new IllegalArgumentException("firstBytePosition=" + firstBytePos + " should be less then or equal to lastBytePosition=" + lastBytePos);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public long getRangeStart(long length)
/*     */     {
/* 234 */       return this.firstPos;
/*     */     }
/*     */     
/*     */     public long getRangeEnd(long length)
/*     */     {
/* 239 */       if ((this.lastPos != null) && (this.lastPos.longValue() < length)) {
/* 240 */         return this.lastPos.longValue();
/*     */       }
/*     */       
/* 243 */       return length - 1L;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean equals(Object other)
/*     */     {
/* 249 */       if (this == other) {
/* 250 */         return true;
/*     */       }
/* 252 */       if (!(other instanceof ByteRange)) {
/* 253 */         return false;
/*     */       }
/* 255 */       ByteRange otherRange = (ByteRange)other;
/* 256 */       return (this.firstPos == otherRange.firstPos) && 
/* 257 */         (ObjectUtils.nullSafeEquals(this.lastPos, otherRange.lastPos));
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 262 */       return 
/* 263 */         ObjectUtils.nullSafeHashCode(Long.valueOf(this.firstPos)) * 31 + ObjectUtils.nullSafeHashCode(this.lastPos);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 268 */       StringBuilder builder = new StringBuilder();
/* 269 */       builder.append(this.firstPos);
/* 270 */       builder.append('-');
/* 271 */       if (this.lastPos != null) {
/* 272 */         builder.append(this.lastPos);
/*     */       }
/* 274 */       return builder.toString();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class SuffixByteRange
/*     */     extends HttpRange
/*     */   {
/*     */     private final long suffixLength;
/*     */     
/*     */ 
/*     */ 
/*     */     public SuffixByteRange(long suffixLength)
/*     */     {
/* 289 */       if (suffixLength < 0L) {
/* 290 */         throw new IllegalArgumentException("Invalid suffix length: " + suffixLength);
/*     */       }
/* 292 */       this.suffixLength = suffixLength;
/*     */     }
/*     */     
/*     */     public long getRangeStart(long length)
/*     */     {
/* 297 */       if (this.suffixLength < length) {
/* 298 */         return length - this.suffixLength;
/*     */       }
/*     */       
/* 301 */       return 0L;
/*     */     }
/*     */     
/*     */ 
/*     */     public long getRangeEnd(long length)
/*     */     {
/* 307 */       return length - 1L;
/*     */     }
/*     */     
/*     */     public boolean equals(Object other)
/*     */     {
/* 312 */       if (this == other) {
/* 313 */         return true;
/*     */       }
/* 315 */       if (!(other instanceof SuffixByteRange)) {
/* 316 */         return false;
/*     */       }
/* 318 */       SuffixByteRange otherRange = (SuffixByteRange)other;
/* 319 */       return this.suffixLength == otherRange.suffixLength;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 324 */       return ObjectUtils.hashCode(this.suffixLength);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 329 */       return "-" + this.suffixLength;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\HttpRange.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */