/*     */ package org.springframework.http;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.InvalidMimeTypeException;
/*     */ import org.springframework.util.MimeType;
/*     */ import org.springframework.util.MimeType.SpecificityComparator;
/*     */ import org.springframework.util.MimeTypeUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.comparator.CompoundComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MediaType
/*     */   extends MimeType
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2069937152339670231L;
/* 258 */   public static final MediaType ALL = valueOf("*/*");
/* 259 */   public static final String ALL_VALUE = "*/*"; public static final MediaType APPLICATION_ATOM_XML = valueOf("application/atom+xml");
/* 260 */   public static final String APPLICATION_ATOM_XML_VALUE = "application/atom+xml"; public static final MediaType APPLICATION_FORM_URLENCODED = valueOf("application/x-www-form-urlencoded");
/* 261 */   public static final String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded"; public static final MediaType APPLICATION_JSON = valueOf("application/json");
/* 262 */   public static final String APPLICATION_JSON_VALUE = "application/json"; public static final MediaType APPLICATION_JSON_UTF8 = valueOf("application/json;charset=UTF-8");
/* 263 */   public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8"; public static final MediaType APPLICATION_OCTET_STREAM = valueOf("application/octet-stream");
/* 264 */   public static final String APPLICATION_OCTET_STREAM_VALUE = "application/octet-stream"; public static final MediaType APPLICATION_PDF = valueOf("application/pdf");
/* 265 */   public static final String APPLICATION_PDF_VALUE = "application/pdf"; public static final MediaType APPLICATION_RSS_XML = valueOf("application/rss+xml");
/* 266 */   public static final String APPLICATION_RSS_XML_VALUE = "application/rss+xml"; public static final MediaType APPLICATION_XHTML_XML = valueOf("application/xhtml+xml");
/* 267 */   public static final String APPLICATION_XHTML_XML_VALUE = "application/xhtml+xml"; public static final MediaType APPLICATION_XML = valueOf("application/xml");
/* 268 */   public static final String APPLICATION_XML_VALUE = "application/xml"; public static final MediaType IMAGE_GIF = valueOf("image/gif");
/* 269 */   public static final String IMAGE_GIF_VALUE = "image/gif"; public static final MediaType IMAGE_JPEG = valueOf("image/jpeg");
/* 270 */   public static final String IMAGE_JPEG_VALUE = "image/jpeg"; public static final MediaType IMAGE_PNG = valueOf("image/png");
/* 271 */   public static final String IMAGE_PNG_VALUE = "image/png"; public static final MediaType MULTIPART_FORM_DATA = valueOf("multipart/form-data");
/* 272 */   public static final String MULTIPART_FORM_DATA_VALUE = "multipart/form-data"; public static final MediaType TEXT_EVENT_STREAM = valueOf("text/event-stream");
/* 273 */   public static final String TEXT_EVENT_STREAM_VALUE = "text/event-stream"; public static final MediaType TEXT_HTML = valueOf("text/html");
/* 274 */   public static final String TEXT_HTML_VALUE = "text/html"; public static final MediaType TEXT_MARKDOWN = valueOf("text/markdown");
/* 275 */   public static final String TEXT_MARKDOWN_VALUE = "text/markdown"; public static final MediaType TEXT_PLAIN = valueOf("text/plain");
/* 276 */   public static final String TEXT_PLAIN_VALUE = "text/plain"; public static final MediaType TEXT_XML = valueOf("text/xml");
/*     */   
/*     */ 
/*     */   public static final String TEXT_XML_VALUE = "text/xml";
/*     */   
/*     */ 
/*     */   private static final String PARAM_QUALITY_FACTOR = "q";
/*     */   
/*     */ 
/*     */   public MediaType(String type)
/*     */   {
/* 287 */     super(type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MediaType(String type, String subtype)
/*     */   {
/* 298 */     super(type, subtype, Collections.emptyMap());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MediaType(String type, String subtype, Charset charset)
/*     */   {
/* 309 */     super(type, subtype, charset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MediaType(String type, String subtype, double qualityValue)
/*     */   {
/* 320 */     this(type, subtype, Collections.singletonMap("q", Double.toString(qualityValue)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MediaType(MediaType other, Charset charset)
/*     */   {
/* 332 */     super(other, charset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MediaType(MediaType other, Map<String, String> parameters)
/*     */   {
/* 343 */     super(other.getType(), other.getSubtype(), parameters);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MediaType(String type, String subtype, Map<String, String> parameters)
/*     */   {
/* 354 */     super(type, subtype, parameters);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void checkParameters(String attribute, String value)
/*     */   {
/* 360 */     super.checkParameters(attribute, value);
/* 361 */     if ("q".equals(attribute)) {
/* 362 */       value = unquote(value);
/* 363 */       double d = Double.parseDouble(value);
/* 364 */       Assert.isTrue((d >= 0.0D) && (d <= 1.0D), "Invalid quality value \"" + value + "\": should be between 0.0 and 1.0");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getQualityValue()
/*     */   {
/* 375 */     String qualityFactory = getParameter("q");
/* 376 */     return qualityFactory != null ? Double.parseDouble(unquote(qualityFactory)) : 1.0D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean includes(MediaType other)
/*     */   {
/* 387 */     return super.includes(other);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCompatibleWith(MediaType other)
/*     */   {
/* 398 */     return super.isCompatibleWith(other);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MediaType copyQualityValue(MediaType mediaType)
/*     */   {
/* 406 */     if (!mediaType.getParameters().containsKey("q")) {
/* 407 */       return this;
/*     */     }
/* 409 */     Map<String, String> params = new LinkedHashMap(getParameters());
/* 410 */     params.put("q", mediaType.getParameters().get("q"));
/* 411 */     return new MediaType(this, params);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MediaType removeQualityValue()
/*     */   {
/* 419 */     if (!getParameters().containsKey("q")) {
/* 420 */       return this;
/*     */     }
/* 422 */     Map<String, String> params = new LinkedHashMap(getParameters());
/* 423 */     params.remove("q");
/* 424 */     return new MediaType(this, params);
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
/*     */   public static MediaType valueOf(String value)
/*     */   {
/* 437 */     return parseMediaType(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static MediaType parseMediaType(String mediaType)
/*     */   {
/*     */     try
/*     */     {
/* 449 */       type = MimeTypeUtils.parseMimeType(mediaType);
/*     */     } catch (InvalidMimeTypeException ex) {
/*     */       MimeType type;
/* 452 */       throw new InvalidMediaTypeException(ex);
/*     */     }
/*     */     try { MimeType type;
/* 455 */       return new MediaType(type.getType(), type.getSubtype(), type.getParameters());
/*     */     }
/*     */     catch (IllegalArgumentException ex) {
/* 458 */       throw new InvalidMediaTypeException(mediaType, ex.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<MediaType> parseMediaTypes(String mediaTypes)
/*     */   {
/* 470 */     if (!StringUtils.hasLength(mediaTypes)) {
/* 471 */       return Collections.emptyList();
/*     */     }
/* 473 */     String[] tokens = StringUtils.tokenizeToStringArray(mediaTypes, ",");
/* 474 */     List<MediaType> result = new ArrayList(tokens.length);
/* 475 */     for (String token : tokens) {
/* 476 */       result.add(parseMediaType(token));
/*     */     }
/* 478 */     return result;
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
/*     */   public static List<MediaType> parseMediaTypes(List<String> mediaTypes)
/*     */   {
/* 491 */     if (CollectionUtils.isEmpty(mediaTypes)) {
/* 492 */       return Collections.emptyList();
/*     */     }
/* 494 */     if (mediaTypes.size() == 1) {
/* 495 */       return parseMediaTypes((String)mediaTypes.get(0));
/*     */     }
/*     */     
/* 498 */     List<MediaType> result = new ArrayList(8);
/* 499 */     for (String mediaType : mediaTypes) {
/* 500 */       result.addAll(parseMediaTypes(mediaType));
/*     */     }
/* 502 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toString(Collection<MediaType> mediaTypes)
/*     */   {
/* 513 */     return MimeTypeUtils.toString(mediaTypes);
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
/*     */   public static void sortBySpecificity(List<MediaType> mediaTypes)
/*     */   {
/* 544 */     Assert.notNull(mediaTypes, "'mediaTypes' must not be null");
/* 545 */     if (mediaTypes.size() > 1) {
/* 546 */       Collections.sort(mediaTypes, SPECIFICITY_COMPARATOR);
/*     */     }
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
/*     */   public static void sortByQualityValue(List<MediaType> mediaTypes)
/*     */   {
/* 571 */     Assert.notNull(mediaTypes, "'mediaTypes' must not be null");
/* 572 */     if (mediaTypes.size() > 1) {
/* 573 */       Collections.sort(mediaTypes, QUALITY_VALUE_COMPARATOR);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void sortBySpecificityAndQuality(List<MediaType> mediaTypes)
/*     */   {
/* 584 */     Assert.notNull(mediaTypes, "'mediaTypes' must not be null");
/* 585 */     if (mediaTypes.size() > 1) {
/* 586 */       Collections.sort(mediaTypes, new CompoundComparator(new Comparator[] { SPECIFICITY_COMPARATOR, QUALITY_VALUE_COMPARATOR }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 595 */   public static final Comparator<MediaType> QUALITY_VALUE_COMPARATOR = new Comparator()
/*     */   {
/*     */     public int compare(MediaType mediaType1, MediaType mediaType2)
/*     */     {
/* 599 */       double quality1 = mediaType1.getQualityValue();
/* 600 */       double quality2 = mediaType2.getQualityValue();
/* 601 */       int qualityComparison = Double.compare(quality2, quality1);
/* 602 */       if (qualityComparison != 0) {
/* 603 */         return qualityComparison;
/*     */       }
/* 605 */       if ((mediaType1.isWildcardType()) && (!mediaType2.isWildcardType())) {
/* 606 */         return 1;
/*     */       }
/* 608 */       if ((mediaType2.isWildcardType()) && (!mediaType1.isWildcardType())) {
/* 609 */         return -1;
/*     */       }
/* 611 */       if (!mediaType1.getType().equals(mediaType2.getType())) {
/* 612 */         return 0;
/*     */       }
/*     */       
/* 615 */       if ((mediaType1.isWildcardSubtype()) && (!mediaType2.isWildcardSubtype())) {
/* 616 */         return 1;
/*     */       }
/* 618 */       if ((mediaType2.isWildcardSubtype()) && (!mediaType1.isWildcardSubtype())) {
/* 619 */         return -1;
/*     */       }
/* 621 */       if (!mediaType1.getSubtype().equals(mediaType2.getSubtype())) {
/* 622 */         return 0;
/*     */       }
/*     */       
/* 625 */       int paramsSize1 = mediaType1.getParameters().size();
/* 626 */       int paramsSize2 = mediaType2.getParameters().size();
/* 627 */       return paramsSize2 == paramsSize1 ? 0 : paramsSize2 < paramsSize1 ? -1 : 1;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 637 */   public static final Comparator<MediaType> SPECIFICITY_COMPARATOR = new MimeType.SpecificityComparator()
/*     */   {
/*     */     protected int compareParameters(MediaType mediaType1, MediaType mediaType2)
/*     */     {
/* 641 */       double quality1 = mediaType1.getQualityValue();
/* 642 */       double quality2 = mediaType2.getQualityValue();
/* 643 */       int qualityComparison = Double.compare(quality2, quality1);
/* 644 */       if (qualityComparison != 0) {
/* 645 */         return qualityComparison;
/*     */       }
/* 647 */       return super.compareParameters(mediaType1, mediaType2);
/*     */     }
/*     */   };
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\MediaType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */