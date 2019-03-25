/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MimeTypeUtils
/*     */ {
/*  43 */   private static final byte[] BOUNDARY_CHARS = { 45, 95, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  49 */   private static final Random RND = new Random();
/*     */   
/*  51 */   private static Charset US_ASCII = Charset.forName("US-ASCII");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  56 */   public static final Comparator<MimeType> SPECIFICITY_COMPARATOR = new MimeType.SpecificityComparator();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 216 */   public static final MimeType ALL = MimeType.valueOf("*/*"); public static final String ALL_VALUE = "*/*"; @Deprecated
/* 217 */   public static final MimeType APPLICATION_ATOM_XML = MimeType.valueOf("application/atom+xml"); @Deprecated
/*     */   public static final String APPLICATION_ATOM_XML_VALUE = "application/atom+xml"; @Deprecated
/* 218 */   public static final MimeType APPLICATION_FORM_URLENCODED = MimeType.valueOf("application/x-www-form-urlencoded"); @Deprecated
/* 219 */   public static final String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded"; public static final MimeType APPLICATION_JSON = MimeType.valueOf("application/json");
/* 220 */   public static final String APPLICATION_JSON_VALUE = "application/json"; public static final MimeType APPLICATION_OCTET_STREAM = MimeType.valueOf("application/octet-stream"); public static final String APPLICATION_OCTET_STREAM_VALUE = "application/octet-stream"; @Deprecated
/* 221 */   public static final MimeType APPLICATION_XHTML_XML = MimeType.valueOf("application/xhtml+xml"); @Deprecated
/* 222 */   public static final String APPLICATION_XHTML_XML_VALUE = "application/xhtml+xml"; public static final MimeType APPLICATION_XML = MimeType.valueOf("application/xml");
/* 223 */   public static final String APPLICATION_XML_VALUE = "application/xml"; public static final MimeType IMAGE_GIF = MimeType.valueOf("image/gif");
/* 224 */   public static final String IMAGE_GIF_VALUE = "image/gif"; public static final MimeType IMAGE_JPEG = MimeType.valueOf("image/jpeg");
/* 225 */   public static final String IMAGE_JPEG_VALUE = "image/jpeg"; public static final MimeType IMAGE_PNG = MimeType.valueOf("image/png"); public static final String IMAGE_PNG_VALUE = "image/png"; @Deprecated
/* 226 */   public static final MimeType MULTIPART_FORM_DATA = MimeType.valueOf("multipart/form-data"); @Deprecated
/* 227 */   public static final String MULTIPART_FORM_DATA_VALUE = "multipart/form-data"; public static final MimeType TEXT_HTML = MimeType.valueOf("text/html");
/* 228 */   public static final String TEXT_HTML_VALUE = "text/html"; public static final MimeType TEXT_PLAIN = MimeType.valueOf("text/plain");
/* 229 */   public static final String TEXT_PLAIN_VALUE = "text/plain"; public static final MimeType TEXT_XML = MimeType.valueOf("text/xml");
/*     */   
/*     */ 
/*     */ 
/*     */   public static final String TEXT_XML_VALUE = "text/xml";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static MimeType parseMimeType(String mimeType)
/*     */   {
/* 240 */     if (!StringUtils.hasLength(mimeType)) {
/* 241 */       throw new InvalidMimeTypeException(mimeType, "'mimeType' must not be empty");
/*     */     }
/*     */     
/* 244 */     int index = mimeType.indexOf(';');
/* 245 */     String fullType = (index >= 0 ? mimeType.substring(0, index) : mimeType).trim();
/* 246 */     if (fullType.isEmpty()) {
/* 247 */       throw new InvalidMimeTypeException(mimeType, "'mimeType' must not be empty");
/*     */     }
/*     */     
/*     */ 
/* 251 */     if ("*".equals(fullType)) {
/* 252 */       fullType = "*/*";
/*     */     }
/* 254 */     int subIndex = fullType.indexOf('/');
/* 255 */     if (subIndex == -1) {
/* 256 */       throw new InvalidMimeTypeException(mimeType, "does not contain '/'");
/*     */     }
/* 258 */     if (subIndex == fullType.length() - 1) {
/* 259 */       throw new InvalidMimeTypeException(mimeType, "does not contain subtype after '/'");
/*     */     }
/* 261 */     String type = fullType.substring(0, subIndex);
/* 262 */     String subtype = fullType.substring(subIndex + 1, fullType.length());
/* 263 */     if (("*".equals(type)) && (!"*".equals(subtype))) {
/* 264 */       throw new InvalidMimeTypeException(mimeType, "wildcard type is legal only in '*/*' (all mime types)");
/*     */     }
/*     */     
/* 267 */     Map<String, String> parameters = null;
/*     */     do {
/* 269 */       int nextIndex = index + 1;
/* 270 */       boolean quoted = false;
/* 271 */       while (nextIndex < mimeType.length()) {
/* 272 */         char ch = mimeType.charAt(nextIndex);
/* 273 */         if (ch == ';') {
/* 274 */           if (!quoted) {
/*     */             break;
/*     */           }
/*     */         }
/* 278 */         else if (ch == '"') {
/* 279 */           quoted = !quoted;
/*     */         }
/* 281 */         nextIndex++;
/*     */       }
/* 283 */       String parameter = mimeType.substring(index + 1, nextIndex).trim();
/* 284 */       if (parameter.length() > 0) {
/* 285 */         if (parameters == null) {
/* 286 */           parameters = new LinkedHashMap(4);
/*     */         }
/* 288 */         int eqIndex = parameter.indexOf('=');
/* 289 */         if (eqIndex >= 0) {
/* 290 */           String attribute = parameter.substring(0, eqIndex);
/* 291 */           String value = parameter.substring(eqIndex + 1, parameter.length());
/* 292 */           parameters.put(attribute, value);
/*     */         }
/*     */       }
/* 295 */       index = nextIndex;
/*     */     }
/* 297 */     while (index < mimeType.length());
/*     */     try
/*     */     {
/* 300 */       return new MimeType(type, subtype, parameters);
/*     */     }
/*     */     catch (UnsupportedCharsetException ex) {
/* 303 */       throw new InvalidMimeTypeException(mimeType, "unsupported charset '" + ex.getCharsetName() + "'");
/*     */     }
/*     */     catch (IllegalArgumentException ex) {
/* 306 */       throw new InvalidMimeTypeException(mimeType, ex.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<MimeType> parseMimeTypes(String mimeTypes)
/*     */   {
/* 317 */     if (!StringUtils.hasLength(mimeTypes)) {
/* 318 */       return Collections.emptyList();
/*     */     }
/* 320 */     String[] tokens = StringUtils.tokenizeToStringArray(mimeTypes, ",");
/* 321 */     List<MimeType> result = new ArrayList(tokens.length);
/* 322 */     for (String token : tokens) {
/* 323 */       result.add(parseMimeType(token));
/*     */     }
/* 325 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toString(Collection<? extends MimeType> mimeTypes)
/*     */   {
/* 335 */     StringBuilder builder = new StringBuilder();
/* 336 */     for (Iterator<? extends MimeType> iterator = mimeTypes.iterator(); iterator.hasNext();) {
/* 337 */       MimeType mimeType = (MimeType)iterator.next();
/* 338 */       mimeType.appendTo(builder);
/* 339 */       if (iterator.hasNext()) {
/* 340 */         builder.append(", ");
/*     */       }
/*     */     }
/* 343 */     return builder.toString();
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
/*     */   public static void sortBySpecificity(List<MimeType> mimeTypes)
/*     */   {
/* 372 */     Assert.notNull(mimeTypes, "'mimeTypes' must not be null");
/* 373 */     if (mimeTypes.size() > 1) {
/* 374 */       Collections.sort(mimeTypes, SPECIFICITY_COMPARATOR);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static byte[] generateMultipartBoundary()
/*     */   {
/* 382 */     byte[] boundary = new byte[RND.nextInt(11) + 30];
/* 383 */     for (int i = 0; i < boundary.length; i++) {
/* 384 */       boundary[i] = BOUNDARY_CHARS[RND.nextInt(BOUNDARY_CHARS.length)];
/*     */     }
/* 386 */     return boundary;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String generateMultipartBoundaryString()
/*     */   {
/* 393 */     return new String(generateMultipartBoundary(), US_ASCII);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\MimeTypeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */