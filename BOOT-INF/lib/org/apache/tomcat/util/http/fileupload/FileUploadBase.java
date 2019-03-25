/*      */ package org.apache.tomcat.util.http.fileupload;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import org.apache.tomcat.util.http.fileupload.util.Closeable;
/*      */ import org.apache.tomcat.util.http.fileupload.util.FileItemHeadersImpl;
/*      */ import org.apache.tomcat.util.http.fileupload.util.LimitedInputStream;
/*      */ import org.apache.tomcat.util.http.fileupload.util.Streams;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class FileUploadBase
/*      */ {
/*      */   public static final String CONTENT_TYPE = "Content-type";
/*      */   public static final String CONTENT_DISPOSITION = "Content-disposition";
/*      */   public static final String CONTENT_LENGTH = "Content-length";
/*      */   public static final String FORM_DATA = "form-data";
/*      */   public static final String ATTACHMENT = "attachment";
/*      */   public static final String MULTIPART = "multipart/";
/*      */   public static final String MULTIPART_FORM_DATA = "multipart/form-data";
/*      */   public static final String MULTIPART_MIXED = "multipart/mixed";
/*      */   
/*      */   public static final boolean isMultipartContent(RequestContext ctx)
/*      */   {
/*   69 */     String contentType = ctx.getContentType();
/*   70 */     if (contentType == null) {
/*   71 */       return false;
/*      */     }
/*   73 */     if (contentType.toLowerCase(Locale.ENGLISH).startsWith("multipart/")) {
/*   74 */       return true;
/*      */     }
/*   76 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  127 */   private long sizeMax = -1L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  133 */   private long fileSizeMax = -1L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String headerEncoding;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private ProgressListener listener;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract FileItemFactory getFileItemFactory();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract void setFileItemFactory(FileItemFactory paramFileItemFactory);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getSizeMax()
/*      */   {
/*  172 */     return this.sizeMax;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSizeMax(long sizeMax)
/*      */   {
/*  186 */     this.sizeMax = sizeMax;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getFileSizeMax()
/*      */   {
/*  197 */     return this.fileSizeMax;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFileSizeMax(long fileSizeMax)
/*      */   {
/*  208 */     this.fileSizeMax = fileSizeMax;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getHeaderEncoding()
/*      */   {
/*  220 */     return this.headerEncoding;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setHeaderEncoding(String encoding)
/*      */   {
/*  232 */     this.headerEncoding = encoding;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FileItemIterator getItemIterator(RequestContext ctx)
/*      */     throws FileUploadException, IOException
/*      */   {
/*      */     try
/*      */     {
/*  256 */       return new FileItemIteratorImpl(ctx);
/*      */     }
/*      */     catch (FileUploadIOException e) {
/*  259 */       throw ((FileUploadException)e.getCause());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<FileItem> parseRequest(RequestContext ctx)
/*      */     throws FileUploadException
/*      */   {
/*  277 */     List<FileItem> items = new ArrayList();
/*  278 */     boolean successful = false;
/*      */     try {
/*  280 */       FileItemIterator iter = getItemIterator(ctx);
/*  281 */       FileItemFactory fac = getFileItemFactory();
/*  282 */       if (fac == null)
/*  283 */         throw new NullPointerException("No FileItemFactory has been set.");
/*      */       FileItemStream item;
/*  285 */       String fileName; while (iter.hasNext()) {
/*  286 */         item = iter.next();
/*      */         
/*  288 */         fileName = ((FileUploadBase.FileItemIteratorImpl.FileItemStreamImpl)item).name;
/*  289 */         FileItem fileItem = fac.createItem(item.getFieldName(), item.getContentType(), item
/*  290 */           .isFormField(), fileName);
/*  291 */         items.add(fileItem);
/*      */         try {
/*  293 */           Streams.copy(item.openStream(), fileItem.getOutputStream(), true);
/*      */         } catch (FileUploadIOException e) {
/*  295 */           throw ((FileUploadException)e.getCause());
/*      */         } catch (IOException e) {
/*  297 */           throw new IOFileUploadException(String.format("Processing of %s request failed. %s", new Object[] { "multipart/form-data", e
/*  298 */             .getMessage() }), e);
/*      */         }
/*  300 */         FileItemHeaders fih = item.getHeaders();
/*  301 */         fileItem.setHeaders(fih);
/*      */       }
/*  303 */       successful = true;
/*  304 */       FileItem fileItem; return items;
/*      */     } catch (FileUploadIOException e) {
/*  306 */       throw ((FileUploadException)e.getCause());
/*      */     } catch (IOException e) {
/*  308 */       throw new FileUploadException(e.getMessage(), e);
/*      */     } finally {
/*  310 */       if (!successful) {
/*  311 */         for (FileItem fileItem : items) {
/*      */           try {
/*  313 */             fileItem.delete();
/*      */           }
/*      */           catch (Exception localException1) {}
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Map<String, List<FileItem>> parseParameterMap(RequestContext ctx)
/*      */     throws FileUploadException
/*      */   {
/*  337 */     List<FileItem> items = parseRequest(ctx);
/*  338 */     Map<String, List<FileItem>> itemsMap = new HashMap(items.size());
/*      */     
/*  340 */     for (FileItem fileItem : items) {
/*  341 */       String fieldName = fileItem.getFieldName();
/*  342 */       List<FileItem> mappedItems = (List)itemsMap.get(fieldName);
/*      */       
/*  344 */       if (mappedItems == null) {
/*  345 */         mappedItems = new ArrayList();
/*  346 */         itemsMap.put(fieldName, mappedItems);
/*      */       }
/*      */       
/*  349 */       mappedItems.add(fileItem);
/*      */     }
/*      */     
/*  352 */     return itemsMap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected byte[] getBoundary(String contentType)
/*      */   {
/*  366 */     ParameterParser parser = new ParameterParser();
/*  367 */     parser.setLowerCaseNames(true);
/*      */     
/*      */ 
/*  370 */     Map<String, String> params = parser.parse(contentType, new char[] { ';', ',' });
/*  371 */     String boundaryStr = (String)params.get("boundary");
/*      */     
/*  373 */     if (boundaryStr == null) {
/*  374 */       return null;
/*      */     }
/*      */     
/*  377 */     byte[] boundary = boundaryStr.getBytes(StandardCharsets.ISO_8859_1);
/*  378 */     return boundary;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getFileName(FileItemHeaders headers)
/*      */   {
/*  390 */     return getFileName(headers.getHeader("Content-disposition"));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String getFileName(String pContentDisposition)
/*      */   {
/*  399 */     String fileName = null;
/*  400 */     if (pContentDisposition != null) {
/*  401 */       String cdl = pContentDisposition.toLowerCase(Locale.ENGLISH);
/*  402 */       if ((cdl.startsWith("form-data")) || (cdl.startsWith("attachment"))) {
/*  403 */         ParameterParser parser = new ParameterParser();
/*  404 */         parser.setLowerCaseNames(true);
/*      */         
/*      */ 
/*  407 */         Map<String, String> params = parser.parse(pContentDisposition, ';');
/*  408 */         if (params.containsKey("filename")) {
/*  409 */           fileName = (String)params.get("filename");
/*  410 */           if (fileName != null) {
/*  411 */             fileName = fileName.trim();
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*  416 */             fileName = "";
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  421 */     return fileName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getFieldName(FileItemHeaders headers)
/*      */   {
/*  433 */     return getFieldName(headers.getHeader("Content-disposition"));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String getFieldName(String pContentDisposition)
/*      */   {
/*  443 */     String fieldName = null;
/*  444 */     if ((pContentDisposition != null) && 
/*  445 */       (pContentDisposition.toLowerCase(Locale.ENGLISH).startsWith("form-data"))) {
/*  446 */       ParameterParser parser = new ParameterParser();
/*  447 */       parser.setLowerCaseNames(true);
/*      */       
/*  449 */       Map<String, String> params = parser.parse(pContentDisposition, ';');
/*  450 */       fieldName = (String)params.get("name");
/*  451 */       if (fieldName != null) {
/*  452 */         fieldName = fieldName.trim();
/*      */       }
/*      */     }
/*  455 */     return fieldName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected FileItemHeaders getParsedHeaders(String headerPart)
/*      */   {
/*  471 */     int len = headerPart.length();
/*  472 */     FileItemHeadersImpl headers = newFileItemHeaders();
/*  473 */     int start = 0;
/*      */     for (;;) {
/*  475 */       int end = parseEndOfLine(headerPart, start);
/*  476 */       if (start == end) {
/*      */         break;
/*      */       }
/*  479 */       StringBuilder header = new StringBuilder(headerPart.substring(start, end));
/*  480 */       start = end + 2;
/*  481 */       while (start < len) {
/*  482 */         int nonWs = start;
/*  483 */         while (nonWs < len) {
/*  484 */           char c = headerPart.charAt(nonWs);
/*  485 */           if ((c != ' ') && (c != '\t')) {
/*      */             break;
/*      */           }
/*  488 */           nonWs++;
/*      */         }
/*  490 */         if (nonWs == start) {
/*      */           break;
/*      */         }
/*      */         
/*  494 */         end = parseEndOfLine(headerPart, nonWs);
/*  495 */         header.append(" ").append(headerPart.substring(nonWs, end));
/*  496 */         start = end + 2;
/*      */       }
/*  498 */       parseHeaderLine(headers, header.toString());
/*      */     }
/*  500 */     return headers;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected FileItemHeadersImpl newFileItemHeaders()
/*      */   {
/*  508 */     return new FileItemHeadersImpl();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int parseEndOfLine(String headerPart, int end)
/*      */   {
/*  520 */     int index = end;
/*      */     for (;;) {
/*  522 */       int offset = headerPart.indexOf('\r', index);
/*  523 */       if ((offset == -1) || (offset + 1 >= headerPart.length())) {
/*  524 */         throw new IllegalStateException("Expected headers to be terminated by an empty line.");
/*      */       }
/*      */       
/*  527 */       if (headerPart.charAt(offset + 1) == '\n') {
/*  528 */         return offset;
/*      */       }
/*  530 */       index = offset + 1;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void parseHeaderLine(FileItemHeadersImpl headers, String header)
/*      */   {
/*  540 */     int colonOffset = header.indexOf(':');
/*  541 */     if (colonOffset == -1)
/*      */     {
/*  543 */       return;
/*      */     }
/*  545 */     String headerName = header.substring(0, colonOffset).trim();
/*      */     
/*  547 */     String headerValue = header.substring(header.indexOf(':') + 1).trim();
/*  548 */     headers.addHeader(headerName, headerValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private class FileItemIteratorImpl
/*      */     implements FileItemIterator
/*      */   {
/*      */     private final MultipartStream multi;
/*      */     
/*      */ 
/*      */     private final MultipartStream.ProgressNotifier notifier;
/*      */     
/*      */ 
/*      */     private final byte[] boundary;
/*      */     
/*      */ 
/*      */     private FileItemStreamImpl currentItem;
/*      */     
/*      */ 
/*      */     private String currentFieldName;
/*      */     
/*      */ 
/*      */     private boolean skipPreamble;
/*      */     
/*      */ 
/*      */     private boolean itemValid;
/*      */     
/*      */ 
/*      */     private boolean eof;
/*      */     
/*      */ 
/*      */ 
/*      */     class FileItemStreamImpl
/*      */       implements FileItemStream
/*      */     {
/*      */       private final String contentType;
/*      */       
/*      */ 
/*      */       private final String fieldName;
/*      */       
/*      */ 
/*      */       private final String name;
/*      */       
/*      */ 
/*      */       private final boolean formField;
/*      */       
/*      */ 
/*      */       private final InputStream stream;
/*      */       
/*      */ 
/*      */       private FileItemHeaders headers;
/*      */       
/*      */ 
/*      */       FileItemStreamImpl(String pName, String pFieldName, String pContentType, boolean pFormField, long pContentLength)
/*      */         throws IOException
/*      */       {
/*  605 */         this.name = pName;
/*  606 */         this.fieldName = pFieldName;
/*  607 */         this.contentType = pContentType;
/*  608 */         this.formField = pFormField;
/*  609 */         final MultipartStream.ItemInputStream itemStream = FileUploadBase.FileItemIteratorImpl.this.multi.newInputStream();
/*  610 */         InputStream istream = itemStream;
/*  611 */         if (FileUploadBase.this.fileSizeMax != -1L) {
/*  612 */           if ((pContentLength != -1L) && 
/*  613 */             (pContentLength > FileUploadBase.this.fileSizeMax))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*  618 */             FileUploadBase.FileSizeLimitExceededException e = new FileUploadBase.FileSizeLimitExceededException(String.format("The field %s exceeds its maximum permitted size of %s bytes.", new Object[] { this.fieldName, Long.valueOf(FileUploadBase.this.fileSizeMax) }), pContentLength, FileUploadBase.this.fileSizeMax);
/*  619 */             e.setFileName(pName);
/*  620 */             e.setFieldName(pFieldName);
/*  621 */             throw new FileUploadBase.FileUploadIOException(e);
/*      */           }
/*  623 */           istream = new LimitedInputStream(istream, FileUploadBase.this.fileSizeMax)
/*      */           {
/*      */             protected void raiseError(long pSizeMax, long pCount) throws IOException
/*      */             {
/*  627 */               this.val$itemStream.close(true);
/*      */               
/*      */ 
/*  630 */               FileUploadBase.FileSizeLimitExceededException e = new FileUploadBase.FileSizeLimitExceededException(String.format("The field %s exceeds its maximum permitted size of %s bytes.", new Object[] {
/*  631 */                 FileUploadBase.FileItemIteratorImpl.FileItemStreamImpl.this.fieldName, Long.valueOf(pSizeMax) }), pCount, pSizeMax);
/*      */               
/*  633 */               e.setFieldName(FileUploadBase.FileItemIteratorImpl.FileItemStreamImpl.this.fieldName);
/*  634 */               e.setFileName(FileUploadBase.FileItemIteratorImpl.FileItemStreamImpl.this.name);
/*  635 */               throw new FileUploadBase.FileUploadIOException(e);
/*      */             }
/*      */           };
/*      */         }
/*  639 */         this.stream = istream;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       public String getContentType()
/*      */       {
/*  649 */         return this.contentType;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       public String getFieldName()
/*      */       {
/*  659 */         return this.fieldName;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       public String getName()
/*      */       {
/*  673 */         return Streams.checkFileName(this.name);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       public boolean isFormField()
/*      */       {
/*  684 */         return this.formField;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       public InputStream openStream()
/*      */         throws IOException
/*      */       {
/*  696 */         if (((Closeable)this.stream).isClosed()) {
/*  697 */           throw new FileItemStream.ItemSkippedException();
/*      */         }
/*  699 */         return this.stream;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       void close()
/*      */         throws IOException
/*      */       {
/*  708 */         this.stream.close();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       public FileItemHeaders getHeaders()
/*      */       {
/*  718 */         return this.headers;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       public void setHeaders(FileItemHeaders pHeaders)
/*      */       {
/*  728 */         this.headers = pHeaders;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     FileItemIteratorImpl(RequestContext ctx)
/*      */       throws FileUploadException, IOException
/*      */     {
/*  784 */       if (ctx == null) {
/*  785 */         throw new NullPointerException("ctx parameter");
/*      */       }
/*      */       
/*  788 */       String contentType = ctx.getContentType();
/*  789 */       if ((null == contentType) || 
/*  790 */         (!contentType.toLowerCase(Locale.ENGLISH).startsWith("multipart/"))) {
/*  791 */         throw new FileUploadBase.InvalidContentTypeException(String.format("the request doesn't contain a %s or %s stream, content type header is %s", new Object[] { "multipart/form-data", "multipart/mixed", contentType }));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  797 */       long requestSize = ((UploadContext)ctx).contentLength();
/*      */       InputStream input;
/*      */       InputStream input;
/*  800 */       if (FileUploadBase.this.sizeMax >= 0L) {
/*  801 */         if ((requestSize != -1L) && (requestSize > FileUploadBase.this.sizeMax))
/*      */         {
/*      */ 
/*      */ 
/*  805 */           throw new FileUploadBase.SizeLimitExceededException(String.format("the request was rejected because its size (%s) exceeds the configured maximum (%s)", new Object[] {Long.valueOf(requestSize), Long.valueOf(FileUploadBase.this.sizeMax) }), requestSize, FileUploadBase.this.sizeMax);
/*      */         }
/*      */         
/*  808 */         input = new LimitedInputStream(ctx.getInputStream(), FileUploadBase.this.sizeMax)
/*      */         {
/*      */           protected void raiseError(long pSizeMax, long pCount)
/*      */             throws IOException
/*      */           {
/*  813 */             FileUploadException ex = new FileUploadBase.SizeLimitExceededException(String.format("the request was rejected because its size (%s) exceeds the configured maximum (%s)", new Object[] {
/*  814 */               Long.valueOf(pCount), Long.valueOf(pSizeMax) }), pCount, pSizeMax);
/*      */             
/*  816 */             throw new FileUploadBase.FileUploadIOException(ex);
/*      */           }
/*      */         };
/*      */       } else {
/*  820 */         input = ctx.getInputStream();
/*      */       }
/*      */       
/*  823 */       String charEncoding = FileUploadBase.this.headerEncoding;
/*  824 */       if (charEncoding == null) {
/*  825 */         charEncoding = ctx.getCharacterEncoding();
/*      */       }
/*      */       
/*  828 */       this.boundary = FileUploadBase.this.getBoundary(contentType);
/*  829 */       if (this.boundary == null) {
/*  830 */         IOUtils.closeQuietly(input);
/*  831 */         throw new FileUploadException("the request was rejected because no multipart boundary was found");
/*      */       }
/*      */       
/*  834 */       this.notifier = new MultipartStream.ProgressNotifier(FileUploadBase.this.listener, requestSize);
/*      */       try {
/*  836 */         this.multi = new MultipartStream(input, this.boundary, this.notifier);
/*      */       } catch (IllegalArgumentException iae) {
/*  838 */         IOUtils.closeQuietly(input);
/*      */         
/*  840 */         throw new FileUploadBase.InvalidContentTypeException(String.format("The boundary specified in the %s header is too long", new Object[] { "Content-type" }), iae);
/*      */       }
/*  842 */       this.multi.setHeaderEncoding(charEncoding);
/*      */       
/*  844 */       this.skipPreamble = true;
/*  845 */       findNextItem();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private boolean findNextItem()
/*      */       throws IOException
/*      */     {
/*  855 */       if (this.eof) {
/*  856 */         return false;
/*      */       }
/*  858 */       if (this.currentItem != null) {
/*  859 */         this.currentItem.close();
/*  860 */         this.currentItem = null;
/*      */       }
/*      */       for (;;) { boolean nextPart;
/*      */         boolean nextPart;
/*  864 */         if (this.skipPreamble) {
/*  865 */           nextPart = this.multi.skipPreamble();
/*      */         } else {
/*  867 */           nextPart = this.multi.readBoundary();
/*      */         }
/*  869 */         if (!nextPart) {
/*  870 */           if (this.currentFieldName == null)
/*      */           {
/*  872 */             this.eof = true;
/*  873 */             return false;
/*      */           }
/*      */           
/*  876 */           this.multi.setBoundary(this.boundary);
/*  877 */           this.currentFieldName = null;
/*      */         }
/*      */         else {
/*  880 */           FileItemHeaders headers = FileUploadBase.this.getParsedHeaders(this.multi.readHeaders());
/*  881 */           if (this.currentFieldName == null)
/*      */           {
/*  883 */             String fieldName = FileUploadBase.this.getFieldName(headers);
/*  884 */             if (fieldName != null) {
/*  885 */               String subContentType = headers.getHeader("Content-type");
/*  886 */               if (subContentType != null)
/*      */               {
/*  888 */                 if (subContentType.toLowerCase(Locale.ENGLISH).startsWith("multipart/mixed")) {
/*  889 */                   this.currentFieldName = fieldName;
/*      */                   
/*  891 */                   byte[] subBoundary = FileUploadBase.this.getBoundary(subContentType);
/*  892 */                   this.multi.setBoundary(subBoundary);
/*  893 */                   this.skipPreamble = true;
/*  894 */                   continue;
/*      */                 } }
/*  896 */               String fileName = FileUploadBase.this.getFileName(headers);
/*      */               
/*      */ 
/*  899 */               this.currentItem = new FileItemStreamImpl(fileName, fieldName, headers.getHeader("Content-type"), fileName == null, getContentLength(headers));
/*  900 */               this.currentItem.setHeaders(headers);
/*  901 */               this.notifier.noteItem();
/*  902 */               this.itemValid = true;
/*  903 */               return true;
/*      */             }
/*      */           } else {
/*  906 */             String fileName = FileUploadBase.this.getFileName(headers);
/*  907 */             if (fileName != null)
/*      */             {
/*      */ 
/*      */ 
/*  911 */               this.currentItem = new FileItemStreamImpl(fileName, this.currentFieldName, headers.getHeader("Content-type"), false, getContentLength(headers));
/*  912 */               this.currentItem.setHeaders(headers);
/*  913 */               this.notifier.noteItem();
/*  914 */               this.itemValid = true;
/*  915 */               return true;
/*      */             }
/*      */           }
/*  918 */           this.multi.discardBodyData();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     private long getContentLength(FileItemHeaders pHeaders) {
/*  924 */       try { return Long.parseLong(pHeaders.getHeader("Content-length"));
/*      */       } catch (Exception e) {}
/*  926 */       return -1L;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean hasNext()
/*      */       throws FileUploadException, IOException
/*      */     {
/*  942 */       if (this.eof) {
/*  943 */         return false;
/*      */       }
/*  945 */       if (this.itemValid) {
/*  946 */         return true;
/*      */       }
/*      */       try {
/*  949 */         return findNextItem();
/*      */       }
/*      */       catch (FileUploadBase.FileUploadIOException e) {
/*  952 */         throw ((FileUploadException)e.getCause());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public FileItemStream next()
/*      */       throws FileUploadException, IOException
/*      */     {
/*  969 */       if ((this.eof) || ((!this.itemValid) && (!hasNext()))) {
/*  970 */         throw new NoSuchElementException();
/*      */       }
/*  972 */       this.itemValid = false;
/*  973 */       return this.currentItem;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static class FileUploadIOException
/*      */     extends IOException
/*      */   {
/*      */     private static final long serialVersionUID = -3082868232248803474L;
/*      */     
/*      */ 
/*      */ 
/*      */     public FileUploadIOException() {}
/*      */     
/*      */ 
/*      */     public FileUploadIOException(String message, Throwable cause)
/*      */     {
/*  991 */       super(cause);
/*      */     }
/*      */     
/*      */     public FileUploadIOException(String message) {
/*  995 */       super();
/*      */     }
/*      */     
/*      */     public FileUploadIOException(Throwable cause) {
/*  999 */       super();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class InvalidContentTypeException
/*      */     extends FileUploadException
/*      */   {
/*      */     private static final long serialVersionUID = -9073026332015646668L;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public InvalidContentTypeException() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public InvalidContentTypeException(String message)
/*      */     {
/* 1029 */       super();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public InvalidContentTypeException(String msg, Throwable cause)
/*      */     {
/* 1042 */       super(cause);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class IOFileUploadException
/*      */     extends FileUploadException
/*      */   {
/*      */     private static final long serialVersionUID = -5858565745868986701L;
/*      */     
/*      */ 
/*      */     public IOFileUploadException() {}
/*      */     
/*      */ 
/*      */     public IOFileUploadException(String message, Throwable cause)
/*      */     {
/* 1058 */       super(cause);
/*      */     }
/*      */     
/*      */     public IOFileUploadException(String message) {
/* 1062 */       super();
/*      */     }
/*      */     
/*      */     public IOFileUploadException(Throwable cause) {
/* 1066 */       super();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static abstract class SizeException
/*      */     extends FileUploadException
/*      */   {
/*      */     private static final long serialVersionUID = -8776225574705254126L;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private final long actual;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private final long permitted;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected SizeException(String message, long actual, long permitted)
/*      */     {
/* 1099 */       super();
/* 1100 */       this.actual = actual;
/* 1101 */       this.permitted = permitted;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public long getActualSize()
/*      */     {
/* 1111 */       return this.actual;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public long getPermittedSize()
/*      */     {
/* 1121 */       return this.permitted;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class SizeLimitExceededException
/*      */     extends FileUploadBase.SizeException
/*      */   {
/*      */     private static final long serialVersionUID = -2474893167098052828L;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public SizeLimitExceededException(String message, long actual, long permitted)
/*      */     {
/* 1147 */       super(actual, permitted);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class FileSizeLimitExceededException
/*      */     extends FileUploadBase.SizeException
/*      */   {
/*      */     private static final long serialVersionUID = 8150776562029630058L;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private String fileName;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private String fieldName;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public FileSizeLimitExceededException(String message, long actual, long permitted)
/*      */     {
/* 1183 */       super(actual, permitted);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String getFileName()
/*      */     {
/* 1193 */       return this.fileName;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setFileName(String pFileName)
/*      */     {
/* 1203 */       this.fileName = pFileName;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String getFieldName()
/*      */     {
/* 1213 */       return this.fieldName;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setFieldName(String pFieldName)
/*      */     {
/* 1224 */       this.fieldName = pFieldName;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ProgressListener getProgressListener()
/*      */   {
/* 1235 */     return this.listener;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setProgressListener(ProgressListener pListener)
/*      */   {
/* 1244 */     this.listener = pListener;
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileupload\FileUploadBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */