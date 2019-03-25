/*      */ package org.apache.catalina.filters;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.PrintWriter;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.servlet.FilterChain;
/*      */ import javax.servlet.FilterConfig;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.ServletOutputStream;
/*      */ import javax.servlet.ServletRequest;
/*      */ import javax.servlet.ServletResponse;
/*      */ import javax.servlet.WriteListener;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import javax.servlet.http.HttpServletResponseWrapper;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.util.res.StringManager;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ExpiresFilter
/*      */   extends FilterBase
/*      */ {
/*      */   protected static class Duration
/*      */   {
/*      */     protected final int amount;
/*      */     protected final ExpiresFilter.DurationUnit unit;
/*      */     
/*      */     public Duration(int amount, ExpiresFilter.DurationUnit unit)
/*      */     {
/*  442 */       this.amount = amount;
/*  443 */       this.unit = unit;
/*      */     }
/*      */     
/*      */     public int getAmount() {
/*  447 */       return this.amount;
/*      */     }
/*      */     
/*      */     public ExpiresFilter.DurationUnit getUnit() {
/*  451 */       return this.unit;
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/*  456 */       return this.amount + " " + this.unit;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static enum DurationUnit
/*      */   {
/*  464 */     DAY(6),  HOUR(10),  MINUTE(12),  MONTH(2), 
/*  465 */     SECOND(13),  WEEK(3), 
/*  466 */     YEAR(1);
/*      */     
/*      */     private final int calendarField;
/*      */     
/*  470 */     private DurationUnit(int calendarField) { this.calendarField = calendarField; }
/*      */     
/*      */     public int getCalendardField()
/*      */     {
/*  474 */       return this.calendarField;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static class ExpiresConfiguration
/*      */   {
/*      */     private final List<ExpiresFilter.Duration> durations;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private final ExpiresFilter.StartingPoint startingPoint;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public ExpiresConfiguration(ExpiresFilter.StartingPoint startingPoint, List<ExpiresFilter.Duration> durations)
/*      */     {
/*  501 */       this.startingPoint = startingPoint;
/*  502 */       this.durations = durations;
/*      */     }
/*      */     
/*      */     public List<ExpiresFilter.Duration> getDurations() {
/*  506 */       return this.durations;
/*      */     }
/*      */     
/*      */     public ExpiresFilter.StartingPoint getStartingPoint() {
/*  510 */       return this.startingPoint;
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/*  515 */       return "ExpiresConfiguration[startingPoint=" + this.startingPoint + ", duration=" + this.durations + "]";
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static enum StartingPoint
/*      */   {
/*  527 */     ACCESS_TIME,  LAST_MODIFICATION_TIME;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private StartingPoint() {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public class XHttpServletResponse
/*      */     extends HttpServletResponseWrapper
/*      */   {
/*      */     private String cacheControlHeader;
/*      */     
/*      */ 
/*      */ 
/*      */     private long lastModifiedHeader;
/*      */     
/*      */ 
/*      */ 
/*      */     private boolean lastModifiedHeaderSet;
/*      */     
/*      */ 
/*      */ 
/*      */     private PrintWriter printWriter;
/*      */     
/*      */ 
/*      */ 
/*      */     private final HttpServletRequest request;
/*      */     
/*      */ 
/*      */ 
/*      */     private ServletOutputStream servletOutputStream;
/*      */     
/*      */ 
/*      */ 
/*      */     private boolean writeResponseBodyStarted;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public XHttpServletResponse(HttpServletRequest request, HttpServletResponse response)
/*      */     {
/*  573 */       super();
/*  574 */       this.request = request;
/*      */     }
/*      */     
/*      */     public void addDateHeader(String name, long date)
/*      */     {
/*  579 */       super.addDateHeader(name, date);
/*  580 */       if (!this.lastModifiedHeaderSet) {
/*  581 */         this.lastModifiedHeader = date;
/*  582 */         this.lastModifiedHeaderSet = true;
/*      */       }
/*      */     }
/*      */     
/*      */     public void addHeader(String name, String value)
/*      */     {
/*  588 */       super.addHeader(name, value);
/*  589 */       if (("Cache-Control".equalsIgnoreCase(name)) && (this.cacheControlHeader == null))
/*      */       {
/*  591 */         this.cacheControlHeader = value;
/*      */       }
/*      */     }
/*      */     
/*      */     public String getCacheControlHeader() {
/*  596 */       return this.cacheControlHeader;
/*      */     }
/*      */     
/*      */     public long getLastModifiedHeader() {
/*  600 */       return this.lastModifiedHeader;
/*      */     }
/*      */     
/*      */     public ServletOutputStream getOutputStream() throws IOException
/*      */     {
/*  605 */       if (this.servletOutputStream == null)
/*      */       {
/*  607 */         this.servletOutputStream = new ExpiresFilter.XServletOutputStream(ExpiresFilter.this, super.getOutputStream(), this.request, this);
/*      */       }
/*  609 */       return this.servletOutputStream;
/*      */     }
/*      */     
/*      */     public PrintWriter getWriter() throws IOException
/*      */     {
/*  614 */       if (this.printWriter == null) {
/*  615 */         this.printWriter = new ExpiresFilter.XPrintWriter(ExpiresFilter.this, super.getWriter(), this.request, this);
/*      */       }
/*  617 */       return this.printWriter;
/*      */     }
/*      */     
/*      */     public boolean isLastModifiedHeaderSet() {
/*  621 */       return this.lastModifiedHeaderSet;
/*      */     }
/*      */     
/*      */     public boolean isWriteResponseBodyStarted() {
/*  625 */       return this.writeResponseBodyStarted;
/*      */     }
/*      */     
/*      */     public void reset()
/*      */     {
/*  630 */       super.reset();
/*  631 */       this.lastModifiedHeader = 0L;
/*  632 */       this.lastModifiedHeaderSet = false;
/*  633 */       this.cacheControlHeader = null;
/*      */     }
/*      */     
/*      */     public void setDateHeader(String name, long date)
/*      */     {
/*  638 */       super.setDateHeader(name, date);
/*  639 */       if ("Last-Modified".equalsIgnoreCase(name)) {
/*  640 */         this.lastModifiedHeader = date;
/*  641 */         this.lastModifiedHeaderSet = true;
/*      */       }
/*      */     }
/*      */     
/*      */     public void setHeader(String name, String value)
/*      */     {
/*  647 */       super.setHeader(name, value);
/*  648 */       if ("Cache-Control".equalsIgnoreCase(name)) {
/*  649 */         this.cacheControlHeader = value;
/*      */       }
/*      */     }
/*      */     
/*      */     public void setWriteResponseBodyStarted(boolean writeResponseBodyStarted) {
/*  654 */       this.writeResponseBodyStarted = writeResponseBodyStarted;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public class XPrintWriter
/*      */     extends PrintWriter
/*      */   {
/*      */     private final PrintWriter out;
/*      */     
/*      */     private final HttpServletRequest request;
/*      */     
/*      */     private final ExpiresFilter.XHttpServletResponse response;
/*      */     
/*      */ 
/*      */     public XPrintWriter(PrintWriter out, HttpServletRequest request, ExpiresFilter.XHttpServletResponse response)
/*      */     {
/*  671 */       super();
/*  672 */       this.out = out;
/*  673 */       this.request = request;
/*  674 */       this.response = response;
/*      */     }
/*      */     
/*      */     public PrintWriter append(char c)
/*      */     {
/*  679 */       fireBeforeWriteResponseBodyEvent();
/*  680 */       return this.out.append(c);
/*      */     }
/*      */     
/*      */     public PrintWriter append(CharSequence csq)
/*      */     {
/*  685 */       fireBeforeWriteResponseBodyEvent();
/*  686 */       return this.out.append(csq);
/*      */     }
/*      */     
/*      */     public PrintWriter append(CharSequence csq, int start, int end)
/*      */     {
/*  691 */       fireBeforeWriteResponseBodyEvent();
/*  692 */       return this.out.append(csq, start, end);
/*      */     }
/*      */     
/*      */     public void close()
/*      */     {
/*  697 */       fireBeforeWriteResponseBodyEvent();
/*  698 */       this.out.close();
/*      */     }
/*      */     
/*      */     private void fireBeforeWriteResponseBodyEvent() {
/*  702 */       if (!this.response.isWriteResponseBodyStarted()) {
/*  703 */         this.response.setWriteResponseBodyStarted(true);
/*  704 */         ExpiresFilter.this.onBeforeWriteResponseBody(this.request, this.response);
/*      */       }
/*      */     }
/*      */     
/*      */     public void flush()
/*      */     {
/*  710 */       fireBeforeWriteResponseBodyEvent();
/*  711 */       this.out.flush();
/*      */     }
/*      */     
/*      */     public void print(boolean b)
/*      */     {
/*  716 */       fireBeforeWriteResponseBodyEvent();
/*  717 */       this.out.print(b);
/*      */     }
/*      */     
/*      */     public void print(char c)
/*      */     {
/*  722 */       fireBeforeWriteResponseBodyEvent();
/*  723 */       this.out.print(c);
/*      */     }
/*      */     
/*      */     public void print(char[] s)
/*      */     {
/*  728 */       fireBeforeWriteResponseBodyEvent();
/*  729 */       this.out.print(s);
/*      */     }
/*      */     
/*      */     public void print(double d)
/*      */     {
/*  734 */       fireBeforeWriteResponseBodyEvent();
/*  735 */       this.out.print(d);
/*      */     }
/*      */     
/*      */     public void print(float f)
/*      */     {
/*  740 */       fireBeforeWriteResponseBodyEvent();
/*  741 */       this.out.print(f);
/*      */     }
/*      */     
/*      */     public void print(int i)
/*      */     {
/*  746 */       fireBeforeWriteResponseBodyEvent();
/*  747 */       this.out.print(i);
/*      */     }
/*      */     
/*      */     public void print(long l)
/*      */     {
/*  752 */       fireBeforeWriteResponseBodyEvent();
/*  753 */       this.out.print(l);
/*      */     }
/*      */     
/*      */     public void print(Object obj)
/*      */     {
/*  758 */       fireBeforeWriteResponseBodyEvent();
/*  759 */       this.out.print(obj);
/*      */     }
/*      */     
/*      */     public void print(String s)
/*      */     {
/*  764 */       fireBeforeWriteResponseBodyEvent();
/*  765 */       this.out.print(s);
/*      */     }
/*      */     
/*      */     public PrintWriter printf(Locale l, String format, Object... args)
/*      */     {
/*  770 */       fireBeforeWriteResponseBodyEvent();
/*  771 */       return this.out.printf(l, format, args);
/*      */     }
/*      */     
/*      */     public PrintWriter printf(String format, Object... args)
/*      */     {
/*  776 */       fireBeforeWriteResponseBodyEvent();
/*  777 */       return this.out.printf(format, args);
/*      */     }
/*      */     
/*      */     public void println()
/*      */     {
/*  782 */       fireBeforeWriteResponseBodyEvent();
/*  783 */       this.out.println();
/*      */     }
/*      */     
/*      */     public void println(boolean x)
/*      */     {
/*  788 */       fireBeforeWriteResponseBodyEvent();
/*  789 */       this.out.println(x);
/*      */     }
/*      */     
/*      */     public void println(char x)
/*      */     {
/*  794 */       fireBeforeWriteResponseBodyEvent();
/*  795 */       this.out.println(x);
/*      */     }
/*      */     
/*      */     public void println(char[] x)
/*      */     {
/*  800 */       fireBeforeWriteResponseBodyEvent();
/*  801 */       this.out.println(x);
/*      */     }
/*      */     
/*      */     public void println(double x)
/*      */     {
/*  806 */       fireBeforeWriteResponseBodyEvent();
/*  807 */       this.out.println(x);
/*      */     }
/*      */     
/*      */     public void println(float x)
/*      */     {
/*  812 */       fireBeforeWriteResponseBodyEvent();
/*  813 */       this.out.println(x);
/*      */     }
/*      */     
/*      */     public void println(int x)
/*      */     {
/*  818 */       fireBeforeWriteResponseBodyEvent();
/*  819 */       this.out.println(x);
/*      */     }
/*      */     
/*      */     public void println(long x)
/*      */     {
/*  824 */       fireBeforeWriteResponseBodyEvent();
/*  825 */       this.out.println(x);
/*      */     }
/*      */     
/*      */     public void println(Object x)
/*      */     {
/*  830 */       fireBeforeWriteResponseBodyEvent();
/*  831 */       this.out.println(x);
/*      */     }
/*      */     
/*      */     public void println(String x)
/*      */     {
/*  836 */       fireBeforeWriteResponseBodyEvent();
/*  837 */       this.out.println(x);
/*      */     }
/*      */     
/*      */     public void write(char[] buf)
/*      */     {
/*  842 */       fireBeforeWriteResponseBodyEvent();
/*  843 */       this.out.write(buf);
/*      */     }
/*      */     
/*      */     public void write(char[] buf, int off, int len)
/*      */     {
/*  848 */       fireBeforeWriteResponseBodyEvent();
/*  849 */       this.out.write(buf, off, len);
/*      */     }
/*      */     
/*      */     public void write(int c)
/*      */     {
/*  854 */       fireBeforeWriteResponseBodyEvent();
/*  855 */       this.out.write(c);
/*      */     }
/*      */     
/*      */     public void write(String s)
/*      */     {
/*  860 */       fireBeforeWriteResponseBodyEvent();
/*  861 */       this.out.write(s);
/*      */     }
/*      */     
/*      */     public void write(String s, int off, int len)
/*      */     {
/*  866 */       fireBeforeWriteResponseBodyEvent();
/*  867 */       this.out.write(s, off, len);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public class XServletOutputStream
/*      */     extends ServletOutputStream
/*      */   {
/*      */     private final HttpServletRequest request;
/*      */     
/*      */ 
/*      */     private final ExpiresFilter.XHttpServletResponse response;
/*      */     
/*      */ 
/*      */     private final ServletOutputStream servletOutputStream;
/*      */     
/*      */ 
/*      */     public XServletOutputStream(ServletOutputStream servletOutputStream, HttpServletRequest request, ExpiresFilter.XHttpServletResponse response)
/*      */     {
/*  887 */       this.servletOutputStream = servletOutputStream;
/*  888 */       this.response = response;
/*  889 */       this.request = request;
/*      */     }
/*      */     
/*      */     public void close() throws IOException
/*      */     {
/*  894 */       fireOnBeforeWriteResponseBodyEvent();
/*  895 */       this.servletOutputStream.close();
/*      */     }
/*      */     
/*      */     private void fireOnBeforeWriteResponseBodyEvent() {
/*  899 */       if (!this.response.isWriteResponseBodyStarted()) {
/*  900 */         this.response.setWriteResponseBodyStarted(true);
/*  901 */         ExpiresFilter.this.onBeforeWriteResponseBody(this.request, this.response);
/*      */       }
/*      */     }
/*      */     
/*      */     public void flush() throws IOException
/*      */     {
/*  907 */       fireOnBeforeWriteResponseBodyEvent();
/*  908 */       this.servletOutputStream.flush();
/*      */     }
/*      */     
/*      */     public void print(boolean b) throws IOException
/*      */     {
/*  913 */       fireOnBeforeWriteResponseBodyEvent();
/*  914 */       this.servletOutputStream.print(b);
/*      */     }
/*      */     
/*      */     public void print(char c) throws IOException
/*      */     {
/*  919 */       fireOnBeforeWriteResponseBodyEvent();
/*  920 */       this.servletOutputStream.print(c);
/*      */     }
/*      */     
/*      */     public void print(double d) throws IOException
/*      */     {
/*  925 */       fireOnBeforeWriteResponseBodyEvent();
/*  926 */       this.servletOutputStream.print(d);
/*      */     }
/*      */     
/*      */     public void print(float f) throws IOException
/*      */     {
/*  931 */       fireOnBeforeWriteResponseBodyEvent();
/*  932 */       this.servletOutputStream.print(f);
/*      */     }
/*      */     
/*      */     public void print(int i) throws IOException
/*      */     {
/*  937 */       fireOnBeforeWriteResponseBodyEvent();
/*  938 */       this.servletOutputStream.print(i);
/*      */     }
/*      */     
/*      */     public void print(long l) throws IOException
/*      */     {
/*  943 */       fireOnBeforeWriteResponseBodyEvent();
/*  944 */       this.servletOutputStream.print(l);
/*      */     }
/*      */     
/*      */     public void print(String s) throws IOException
/*      */     {
/*  949 */       fireOnBeforeWriteResponseBodyEvent();
/*  950 */       this.servletOutputStream.print(s);
/*      */     }
/*      */     
/*      */     public void println() throws IOException
/*      */     {
/*  955 */       fireOnBeforeWriteResponseBodyEvent();
/*  956 */       this.servletOutputStream.println();
/*      */     }
/*      */     
/*      */     public void println(boolean b) throws IOException
/*      */     {
/*  961 */       fireOnBeforeWriteResponseBodyEvent();
/*  962 */       this.servletOutputStream.println(b);
/*      */     }
/*      */     
/*      */     public void println(char c) throws IOException
/*      */     {
/*  967 */       fireOnBeforeWriteResponseBodyEvent();
/*  968 */       this.servletOutputStream.println(c);
/*      */     }
/*      */     
/*      */     public void println(double d) throws IOException
/*      */     {
/*  973 */       fireOnBeforeWriteResponseBodyEvent();
/*  974 */       this.servletOutputStream.println(d);
/*      */     }
/*      */     
/*      */     public void println(float f) throws IOException
/*      */     {
/*  979 */       fireOnBeforeWriteResponseBodyEvent();
/*  980 */       this.servletOutputStream.println(f);
/*      */     }
/*      */     
/*      */     public void println(int i) throws IOException
/*      */     {
/*  985 */       fireOnBeforeWriteResponseBodyEvent();
/*  986 */       this.servletOutputStream.println(i);
/*      */     }
/*      */     
/*      */     public void println(long l) throws IOException
/*      */     {
/*  991 */       fireOnBeforeWriteResponseBodyEvent();
/*  992 */       this.servletOutputStream.println(l);
/*      */     }
/*      */     
/*      */     public void println(String s) throws IOException
/*      */     {
/*  997 */       fireOnBeforeWriteResponseBodyEvent();
/*  998 */       this.servletOutputStream.println(s);
/*      */     }
/*      */     
/*      */     public void write(byte[] b) throws IOException
/*      */     {
/* 1003 */       fireOnBeforeWriteResponseBodyEvent();
/* 1004 */       this.servletOutputStream.write(b);
/*      */     }
/*      */     
/*      */     public void write(byte[] b, int off, int len) throws IOException
/*      */     {
/* 1009 */       fireOnBeforeWriteResponseBodyEvent();
/* 1010 */       this.servletOutputStream.write(b, off, len);
/*      */     }
/*      */     
/*      */     public void write(int b) throws IOException
/*      */     {
/* 1015 */       fireOnBeforeWriteResponseBodyEvent();
/* 1016 */       this.servletOutputStream.write(b);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isReady()
/*      */     {
/* 1025 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setWriteListener(WriteListener listener) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1045 */   private static final Pattern commaSeparatedValuesPattern = Pattern.compile("\\s*,\\s*");
/*      */   
/*      */   private static final String HEADER_CACHE_CONTROL = "Cache-Control";
/*      */   
/*      */   private static final String HEADER_EXPIRES = "Expires";
/*      */   
/*      */   private static final String HEADER_LAST_MODIFIED = "Last-Modified";
/*      */   
/* 1053 */   private static final Log log = LogFactory.getLog(ExpiresFilter.class);
/*      */   
/*      */ 
/*      */   private static final String PARAMETER_EXPIRES_BY_TYPE = "ExpiresByType";
/*      */   
/*      */ 
/*      */   private static final String PARAMETER_EXPIRES_DEFAULT = "ExpiresDefault";
/*      */   
/*      */ 
/*      */   private static final String PARAMETER_EXPIRES_EXCLUDED_RESPONSE_STATUS_CODES = "ExpiresExcludedResponseStatusCodes";
/*      */   
/*      */ 
/*      */   private ExpiresConfiguration defaultExpiresConfiguration;
/*      */   
/*      */ 
/*      */   protected static int[] commaDelimitedListToIntArray(String commaDelimitedInts)
/*      */   {
/* 1070 */     String[] intsAsStrings = commaDelimitedListToStringArray(commaDelimitedInts);
/* 1071 */     int[] ints = new int[intsAsStrings.length];
/* 1072 */     for (int i = 0; i < intsAsStrings.length; i++) {
/* 1073 */       String intAsString = intsAsStrings[i];
/*      */       try {
/* 1075 */         ints[i] = Integer.parseInt(intAsString);
/*      */       } catch (NumberFormatException e) {
/* 1077 */         throw new RuntimeException("Exception parsing number '" + i + "' (zero based) of comma delimited list '" + commaDelimitedInts + "'");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1082 */     return ints;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static String[] commaDelimitedListToStringArray(String commaDelimitedStrings)
/*      */   {
/* 1093 */     return (commaDelimitedStrings == null) || (commaDelimitedStrings.length() == 0) ? new String[0] : commaSeparatedValuesPattern
/* 1094 */       .split(commaDelimitedStrings);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static boolean contains(String str, String searchStr)
/*      */   {
/* 1104 */     if ((str == null) || (searchStr == null)) {
/* 1105 */       return false;
/*      */     }
/* 1107 */     return str.indexOf(searchStr) >= 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static String intsToCommaDelimitedString(int[] ints)
/*      */   {
/* 1116 */     if (ints == null) {
/* 1117 */       return "";
/*      */     }
/*      */     
/* 1120 */     StringBuilder result = new StringBuilder();
/*      */     
/* 1122 */     for (int i = 0; i < ints.length; i++) {
/* 1123 */       result.append(ints[i]);
/* 1124 */       if (i < ints.length - 1) {
/* 1125 */         result.append(", ");
/*      */       }
/*      */     }
/* 1128 */     return result.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static boolean isEmpty(String str)
/*      */   {
/* 1137 */     return (str == null) || (str.length() == 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static boolean isNotEmpty(String str)
/*      */   {
/* 1146 */     return !isEmpty(str);
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
/*      */   protected static boolean startsWithIgnoreCase(String string, String prefix)
/*      */   {
/* 1159 */     if ((string == null) || (prefix == null)) {
/* 1160 */       return (string == null) && (prefix == null);
/*      */     }
/* 1162 */     if (prefix.length() > string.length()) {
/* 1163 */       return false;
/*      */     }
/*      */     
/* 1166 */     return string.regionMatches(true, 0, prefix, 0, prefix.length());
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
/*      */   protected static String substringBefore(String str, String separator)
/*      */   {
/* 1181 */     if ((str == null) || (str.isEmpty()) || (separator == null)) {
/* 1182 */       return null;
/*      */     }
/*      */     
/* 1185 */     if (separator.isEmpty()) {
/* 1186 */       return "";
/*      */     }
/*      */     
/* 1189 */     int separatorIndex = str.indexOf(separator);
/* 1190 */     if (separatorIndex == -1) {
/* 1191 */       return str;
/*      */     }
/* 1193 */     return str.substring(0, separatorIndex);
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
/* 1205 */   private int[] excludedResponseStatusCodes = { 304 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 1210 */   private Map<String, ExpiresConfiguration> expiresConfigurationByContentType = new LinkedHashMap();
/*      */   
/*      */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
/*      */     throws IOException, ServletException
/*      */   {
/* 1215 */     if (((request instanceof HttpServletRequest)) && ((response instanceof HttpServletResponse)))
/*      */     {
/* 1217 */       HttpServletRequest httpRequest = (HttpServletRequest)request;
/* 1218 */       HttpServletResponse httpResponse = (HttpServletResponse)response;
/*      */       
/* 1220 */       if (response.isCommitted()) {
/* 1221 */         if (log.isDebugEnabled()) {
/* 1222 */           log.debug(sm.getString("expiresFilter.responseAlreadyCommited", new Object[] {httpRequest
/*      */           
/* 1224 */             .getRequestURL() }));
/*      */         }
/* 1226 */         chain.doFilter(request, response);
/*      */       } else {
/* 1228 */         XHttpServletResponse xResponse = new XHttpServletResponse(httpRequest, httpResponse);
/*      */         
/* 1230 */         chain.doFilter(request, xResponse);
/* 1231 */         if (!xResponse.isWriteResponseBodyStarted())
/*      */         {
/*      */ 
/* 1234 */           onBeforeWriteResponseBody(httpRequest, xResponse);
/*      */         }
/*      */       }
/*      */     } else {
/* 1238 */       chain.doFilter(request, response);
/*      */     }
/*      */   }
/*      */   
/*      */   public ExpiresConfiguration getDefaultExpiresConfiguration() {
/* 1243 */     return this.defaultExpiresConfiguration;
/*      */   }
/*      */   
/*      */   public String getExcludedResponseStatusCodes() {
/* 1247 */     return intsToCommaDelimitedString(this.excludedResponseStatusCodes);
/*      */   }
/*      */   
/*      */   public int[] getExcludedResponseStatusCodesAsInts() {
/* 1251 */     return this.excludedResponseStatusCodes;
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
/*      */   protected Date getExpirationDate(XHttpServletResponse response)
/*      */   {
/* 1269 */     String contentType = response.getContentType();
/* 1270 */     if (contentType != null) {
/* 1271 */       contentType = contentType.toLowerCase(Locale.ENGLISH);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1276 */     ExpiresConfiguration configuration = (ExpiresConfiguration)this.expiresConfigurationByContentType.get(contentType);
/* 1277 */     if (configuration != null) {
/* 1278 */       Date result = getExpirationDate(configuration, response);
/* 1279 */       if (log.isDebugEnabled()) {
/* 1280 */         log.debug(sm.getString("expiresFilter.useMatchingConfiguration", new Object[] { configuration, contentType, contentType, result }));
/*      */       }
/*      */       
/*      */ 
/* 1284 */       return result;
/*      */     }
/*      */     
/* 1287 */     if (contains(contentType, ";"))
/*      */     {
/* 1289 */       String contentTypeWithoutCharset = substringBefore(contentType, ";").trim();
/* 1290 */       configuration = (ExpiresConfiguration)this.expiresConfigurationByContentType.get(contentTypeWithoutCharset);
/*      */       
/* 1292 */       if (configuration != null) {
/* 1293 */         Date result = getExpirationDate(configuration, response);
/* 1294 */         if (log.isDebugEnabled()) {
/* 1295 */           log.debug(sm.getString("expiresFilter.useMatchingConfiguration", new Object[] { configuration, contentTypeWithoutCharset, contentType, result }));
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1300 */         return result;
/*      */       }
/*      */     }
/*      */     
/* 1304 */     if (contains(contentType, "/"))
/*      */     {
/* 1306 */       String majorType = substringBefore(contentType, "/");
/* 1307 */       configuration = (ExpiresConfiguration)this.expiresConfigurationByContentType.get(majorType);
/* 1308 */       if (configuration != null) {
/* 1309 */         Date result = getExpirationDate(configuration, response);
/* 1310 */         if (log.isDebugEnabled()) {
/* 1311 */           log.debug(sm.getString("expiresFilter.useMatchingConfiguration", new Object[] { configuration, majorType, contentType, result }));
/*      */         }
/*      */         
/*      */ 
/* 1315 */         return result;
/*      */       }
/*      */     }
/*      */     
/* 1319 */     if (this.defaultExpiresConfiguration != null) {
/* 1320 */       Date result = getExpirationDate(this.defaultExpiresConfiguration, response);
/*      */       
/* 1322 */       if (log.isDebugEnabled()) {
/* 1323 */         log.debug(sm.getString("expiresFilter.useDefaultConfiguration", new Object[] { this.defaultExpiresConfiguration, contentType, result }));
/*      */       }
/*      */       
/* 1326 */       return result;
/*      */     }
/*      */     
/* 1329 */     if (log.isDebugEnabled()) {
/* 1330 */       log.debug(sm.getString("expiresFilter.noExpirationConfiguredForContentType", new Object[] { contentType }));
/*      */     }
/*      */     
/*      */ 
/* 1334 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Date getExpirationDate(ExpiresConfiguration configuration, XHttpServletResponse response)
/*      */   {
/*      */     Calendar calendar;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     Calendar calendar;
/*      */     
/*      */ 
/*      */ 
/* 1352 */     switch (configuration.getStartingPoint()) {
/*      */     case ACCESS_TIME: 
/* 1354 */       calendar = Calendar.getInstance();
/* 1355 */       break;
/*      */     case LAST_MODIFICATION_TIME: 
/* 1357 */       if (response.isLastModifiedHeaderSet()) {
/*      */         Calendar calendar;
/* 1359 */         try { long lastModified = response.getLastModifiedHeader();
/* 1360 */           Calendar calendar = Calendar.getInstance();
/* 1361 */           calendar.setTimeInMillis(lastModified);
/*      */         }
/*      */         catch (NumberFormatException e) {
/* 1364 */           calendar = Calendar.getInstance();
/*      */         }
/*      */       }
/*      */       else {
/* 1368 */         calendar = Calendar.getInstance();
/*      */       }
/* 1370 */       break;
/*      */     default: 
/* 1372 */       throw new IllegalStateException(sm.getString("expiresFilter.unsupportedStartingPoint", new Object[] {configuration
/*      */       
/* 1374 */         .getStartingPoint() })); }
/*      */     Calendar calendar;
/* 1376 */     for (Duration duration : configuration.getDurations()) {
/* 1377 */       calendar.add(duration.getUnit().getCalendardField(), duration
/* 1378 */         .getAmount());
/*      */     }
/*      */     
/* 1381 */     return calendar.getTime();
/*      */   }
/*      */   
/*      */   public Map<String, ExpiresConfiguration> getExpiresConfigurationByContentType() {
/* 1385 */     return this.expiresConfigurationByContentType;
/*      */   }
/*      */   
/*      */   protected Log getLogger()
/*      */   {
/* 1390 */     return log;
/*      */   }
/*      */   
/*      */   public void init(FilterConfig filterConfig) throws ServletException
/*      */   {
/* 1395 */     for (Enumeration<String> names = filterConfig.getInitParameterNames(); names.hasMoreElements();) {
/* 1396 */       String name = (String)names.nextElement();
/* 1397 */       String value = filterConfig.getInitParameter(name);
/*      */       try
/*      */       {
/* 1400 */         if (name.startsWith("ExpiresByType"))
/*      */         {
/* 1402 */           String contentType = name.substring("ExpiresByType".length()).trim().toLowerCase(Locale.ENGLISH);
/* 1403 */           ExpiresConfiguration expiresConfiguration = parseExpiresConfiguration(value);
/* 1404 */           this.expiresConfigurationByContentType.put(contentType, expiresConfiguration);
/*      */         }
/* 1406 */         else if (name.equalsIgnoreCase("ExpiresDefault")) {
/* 1407 */           ExpiresConfiguration expiresConfiguration = parseExpiresConfiguration(value);
/* 1408 */           this.defaultExpiresConfiguration = expiresConfiguration;
/* 1409 */         } else if (name.equalsIgnoreCase("ExpiresExcludedResponseStatusCodes")) {
/* 1410 */           this.excludedResponseStatusCodes = commaDelimitedListToIntArray(value);
/*      */         } else {
/* 1412 */           log.warn(sm.getString("expiresFilter.unknownParameterIgnored", new Object[] { name, value }));
/*      */         }
/*      */       }
/*      */       catch (RuntimeException e)
/*      */       {
/* 1417 */         throw new ServletException(sm.getString("expiresFilter.exceptionProcessingParameter", new Object[] { name, value }), e);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1423 */     log.debug(sm.getString("expiresFilter.filterInitialized", new Object[] {
/* 1424 */       toString() }));
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
/*      */   protected boolean isEligibleToExpirationHeaderGeneration(HttpServletRequest request, XHttpServletResponse response)
/*      */   {
/* 1438 */     boolean expirationHeaderHasBeenSet = (response.containsHeader("Expires")) || (contains(response.getCacheControlHeader(), "max-age"));
/* 1439 */     if (expirationHeaderHasBeenSet) {
/* 1440 */       if (log.isDebugEnabled()) {
/* 1441 */         log.debug(sm.getString("expiresFilter.expirationHeaderAlreadyDefined", new Object[] {request
/*      */         
/* 1443 */           .getRequestURI(), 
/* 1444 */           Integer.valueOf(response.getStatus()), response
/* 1445 */           .getContentType() }));
/*      */       }
/* 1447 */       return false;
/*      */     }
/*      */     
/* 1450 */     for (int skippedStatusCode : this.excludedResponseStatusCodes) {
/* 1451 */       if (response.getStatus() == skippedStatusCode) {
/* 1452 */         if (log.isDebugEnabled()) {
/* 1453 */           log.debug(sm.getString("expiresFilter.skippedStatusCode", new Object[] {request
/* 1454 */             .getRequestURI(), 
/* 1455 */             Integer.valueOf(response.getStatus()), response
/* 1456 */             .getContentType() }));
/*      */         }
/* 1458 */         return false;
/*      */       }
/*      */     }
/*      */     
/* 1462 */     return true;
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
/*      */   public void onBeforeWriteResponseBody(HttpServletRequest request, XHttpServletResponse response)
/*      */   {
/* 1488 */     if (!isEligibleToExpirationHeaderGeneration(request, response)) {
/* 1489 */       return;
/*      */     }
/*      */     
/* 1492 */     Date expirationDate = getExpirationDate(response);
/* 1493 */     if (expirationDate == null) {
/* 1494 */       if (log.isDebugEnabled()) {
/* 1495 */         log.debug(sm.getString("expiresFilter.noExpirationConfigured", new Object[] {request
/* 1496 */           .getRequestURI(), 
/* 1497 */           Integer.valueOf(response.getStatus()), response
/* 1498 */           .getContentType() }));
/*      */       }
/*      */     } else {
/* 1501 */       if (log.isDebugEnabled()) {
/* 1502 */         log.debug(sm.getString("expiresFilter.setExpirationDate", new Object[] {request
/* 1503 */           .getRequestURI(), 
/* 1504 */           Integer.valueOf(response.getStatus()), response
/* 1505 */           .getContentType(), expirationDate }));
/*      */       }
/*      */       
/*      */ 
/* 1509 */       String maxAgeDirective = "max-age=" + (expirationDate.getTime() - System.currentTimeMillis()) / 1000L;
/*      */       
/* 1511 */       String cacheControlHeader = response.getCacheControlHeader();
/* 1512 */       String newCacheControlHeader = cacheControlHeader + ", " + maxAgeDirective;
/*      */       
/* 1514 */       response.setHeader("Cache-Control", newCacheControlHeader);
/* 1515 */       response.setDateHeader("Expires", expirationDate.getTime());
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
/*      */   protected ExpiresConfiguration parseExpiresConfiguration(String inputLine)
/*      */   {
/* 1529 */     String line = inputLine.trim();
/*      */     
/* 1531 */     StringTokenizer tokenizer = new StringTokenizer(line, " ");
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 1536 */       currentToken = tokenizer.nextToken();
/*      */     } catch (NoSuchElementException e) { String currentToken;
/* 1538 */       throw new IllegalStateException(sm.getString("expiresFilter.startingPointNotFound", new Object[] { line }));
/*      */     }
/*      */     
/*      */     String currentToken;
/*      */     StartingPoint startingPoint;
/* 1543 */     if (("access".equalsIgnoreCase(currentToken)) || 
/* 1544 */       ("now".equalsIgnoreCase(currentToken))) {
/* 1545 */       startingPoint = StartingPoint.ACCESS_TIME; } else { StartingPoint startingPoint;
/* 1546 */       if ("modification".equalsIgnoreCase(currentToken)) {
/* 1547 */         startingPoint = StartingPoint.LAST_MODIFICATION_TIME;
/* 1548 */       } else if ((!tokenizer.hasMoreTokens()) && 
/* 1549 */         (startsWithIgnoreCase(currentToken, "a"))) {
/* 1550 */         StartingPoint startingPoint = StartingPoint.ACCESS_TIME;
/*      */         
/* 1552 */         tokenizer = new StringTokenizer(currentToken.substring(1) + " seconds", " ");
/*      */       }
/* 1554 */       else if ((!tokenizer.hasMoreTokens()) && 
/* 1555 */         (startsWithIgnoreCase(currentToken, "m"))) {
/* 1556 */         StartingPoint startingPoint = StartingPoint.LAST_MODIFICATION_TIME;
/*      */         
/* 1558 */         tokenizer = new StringTokenizer(currentToken.substring(1) + " seconds", " ");
/*      */       }
/*      */       else {
/* 1561 */         throw new IllegalStateException(sm.getString("expiresFilter.startingPointInvalid", new Object[] { currentToken, line }));
/*      */       }
/*      */     }
/*      */     StartingPoint startingPoint;
/*      */     try {
/* 1566 */       currentToken = tokenizer.nextToken();
/*      */     } catch (NoSuchElementException e) {
/* 1568 */       throw new IllegalStateException(sm.getString("expiresFilter.noDurationFound", new Object[] { line }));
/*      */     }
/*      */     
/*      */ 
/* 1572 */     if ("plus".equalsIgnoreCase(currentToken)) {
/*      */       try
/*      */       {
/* 1575 */         currentToken = tokenizer.nextToken();
/*      */       } catch (NoSuchElementException e) {
/* 1577 */         throw new IllegalStateException(sm.getString("expiresFilter.noDurationFound", new Object[] { line }));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1582 */     List<Duration> durations = new ArrayList();
/*      */     
/* 1584 */     while (currentToken != null)
/*      */     {
/*      */       try {
/* 1587 */         amount = Integer.parseInt(currentToken);
/*      */       } catch (NumberFormatException e) { int amount;
/* 1589 */         throw new IllegalStateException(sm.getString("expiresFilter.invalidDurationNumber", new Object[] { currentToken, line }));
/*      */       }
/*      */       
/*      */       int amount;
/*      */       try
/*      */       {
/* 1595 */         currentToken = tokenizer.nextToken();
/*      */       }
/*      */       catch (NoSuchElementException e) {
/* 1598 */         throw new IllegalStateException(sm.getString("expiresFilter.noDurationUnitAfterAmount", new Object[] {
/*      */         
/* 1600 */           Integer.valueOf(amount), line }));
/*      */       }
/*      */       DurationUnit durationUnit;
/* 1603 */       if (("year".equalsIgnoreCase(currentToken)) || 
/* 1604 */         ("years".equalsIgnoreCase(currentToken))) {
/* 1605 */         durationUnit = DurationUnit.YEAR; } else { DurationUnit durationUnit;
/* 1606 */         if (("month".equalsIgnoreCase(currentToken)) || 
/* 1607 */           ("months".equalsIgnoreCase(currentToken))) {
/* 1608 */           durationUnit = DurationUnit.MONTH; } else { DurationUnit durationUnit;
/* 1609 */           if (("week".equalsIgnoreCase(currentToken)) || 
/* 1610 */             ("weeks".equalsIgnoreCase(currentToken))) {
/* 1611 */             durationUnit = DurationUnit.WEEK; } else { DurationUnit durationUnit;
/* 1612 */             if (("day".equalsIgnoreCase(currentToken)) || 
/* 1613 */               ("days".equalsIgnoreCase(currentToken))) {
/* 1614 */               durationUnit = DurationUnit.DAY; } else { DurationUnit durationUnit;
/* 1615 */               if (("hour".equalsIgnoreCase(currentToken)) || 
/* 1616 */                 ("hours".equalsIgnoreCase(currentToken))) {
/* 1617 */                 durationUnit = DurationUnit.HOUR; } else { DurationUnit durationUnit;
/* 1618 */                 if (("minute".equalsIgnoreCase(currentToken)) || 
/* 1619 */                   ("minutes".equalsIgnoreCase(currentToken))) {
/* 1620 */                   durationUnit = DurationUnit.MINUTE; } else { DurationUnit durationUnit;
/* 1621 */                   if (("second".equalsIgnoreCase(currentToken)) || 
/* 1622 */                     ("seconds".equalsIgnoreCase(currentToken))) {
/* 1623 */                     durationUnit = DurationUnit.SECOND;
/*      */                   }
/*      */                   else
/* 1626 */                     throw new IllegalStateException(sm.getString("expiresFilter.invalidDurationUnit", new Object[] { currentToken, line }));
/*      */                 }
/*      */               }
/*      */             } } } }
/*      */       DurationUnit durationUnit;
/* 1631 */       Duration duration = new Duration(amount, durationUnit);
/* 1632 */       durations.add(duration);
/*      */       
/* 1634 */       if (tokenizer.hasMoreTokens()) {
/* 1635 */         currentToken = tokenizer.nextToken();
/*      */       } else {
/* 1637 */         currentToken = null;
/*      */       }
/*      */     }
/*      */     
/* 1641 */     return new ExpiresConfiguration(startingPoint, durations);
/*      */   }
/*      */   
/*      */   public void setDefaultExpiresConfiguration(ExpiresConfiguration defaultExpiresConfiguration)
/*      */   {
/* 1646 */     this.defaultExpiresConfiguration = defaultExpiresConfiguration;
/*      */   }
/*      */   
/*      */   public void setExcludedResponseStatusCodes(int[] excludedResponseStatusCodes) {
/* 1650 */     this.excludedResponseStatusCodes = excludedResponseStatusCodes;
/*      */   }
/*      */   
/*      */   public void setExpiresConfigurationByContentType(Map<String, ExpiresConfiguration> expiresConfigurationByContentType)
/*      */   {
/* 1655 */     this.expiresConfigurationByContentType = expiresConfigurationByContentType;
/*      */   }
/*      */   
/*      */   public String toString()
/*      */   {
/* 1660 */     return 
/* 1661 */       getClass().getSimpleName() + "[excludedResponseStatusCode=[" + intsToCommaDelimitedString(this.excludedResponseStatusCodes) + "], default=" + this.defaultExpiresConfiguration + ", byType=" + this.expiresConfigurationByContentType + "]";
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\filters\ExpiresFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */