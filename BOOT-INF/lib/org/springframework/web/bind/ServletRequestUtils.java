/*     */ package org.springframework.web.bind;
/*     */ 
/*     */ import javax.servlet.ServletRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ServletRequestUtils
/*     */ {
/*  34 */   private static final IntParser INT_PARSER = new IntParser(null);
/*     */   
/*  36 */   private static final LongParser LONG_PARSER = new LongParser(null);
/*     */   
/*  38 */   private static final FloatParser FLOAT_PARSER = new FloatParser(null);
/*     */   
/*  40 */   private static final DoubleParser DOUBLE_PARSER = new DoubleParser(null);
/*     */   
/*  42 */   private static final BooleanParser BOOLEAN_PARSER = new BooleanParser(null);
/*     */   
/*  44 */   private static final StringParser STRING_PARSER = new StringParser(null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Integer getIntParameter(ServletRequest request, String name)
/*     */     throws ServletRequestBindingException
/*     */   {
/*  59 */     if (request.getParameter(name) == null) {
/*  60 */       return null;
/*     */     }
/*  62 */     return Integer.valueOf(getRequiredIntParameter(request, name));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getIntParameter(ServletRequest request, String name, int defaultVal)
/*     */   {
/*  73 */     if (request.getParameter(name) == null) {
/*  74 */       return defaultVal;
/*     */     }
/*     */     try {
/*  77 */       return getRequiredIntParameter(request, name);
/*     */     }
/*     */     catch (ServletRequestBindingException ex) {}
/*  80 */     return defaultVal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int[] getIntParameters(ServletRequest request, String name)
/*     */   {
/*     */     try
/*     */     {
/*  91 */       return getRequiredIntParameters(request, name);
/*     */     }
/*     */     catch (ServletRequestBindingException ex) {}
/*  94 */     return new int[0];
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
/*     */   public static int getRequiredIntParameter(ServletRequest request, String name)
/*     */     throws ServletRequestBindingException
/*     */   {
/* 108 */     return INT_PARSER.parseInt(name, request.getParameter(name));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int[] getRequiredIntParameters(ServletRequest request, String name)
/*     */     throws ServletRequestBindingException
/*     */   {
/* 121 */     return INT_PARSER.parseInts(name, request.getParameterValues(name));
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
/*     */   public static Long getLongParameter(ServletRequest request, String name)
/*     */     throws ServletRequestBindingException
/*     */   {
/* 137 */     if (request.getParameter(name) == null) {
/* 138 */       return null;
/*     */     }
/* 140 */     return Long.valueOf(getRequiredLongParameter(request, name));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long getLongParameter(ServletRequest request, String name, long defaultVal)
/*     */   {
/* 151 */     if (request.getParameter(name) == null) {
/* 152 */       return defaultVal;
/*     */     }
/*     */     try {
/* 155 */       return getRequiredLongParameter(request, name);
/*     */     }
/*     */     catch (ServletRequestBindingException ex) {}
/* 158 */     return defaultVal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long[] getLongParameters(ServletRequest request, String name)
/*     */   {
/*     */     try
/*     */     {
/* 169 */       return getRequiredLongParameters(request, name);
/*     */     }
/*     */     catch (ServletRequestBindingException ex) {}
/* 172 */     return new long[0];
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
/*     */   public static long getRequiredLongParameter(ServletRequest request, String name)
/*     */     throws ServletRequestBindingException
/*     */   {
/* 186 */     return LONG_PARSER.parseLong(name, request.getParameter(name));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long[] getRequiredLongParameters(ServletRequest request, String name)
/*     */     throws ServletRequestBindingException
/*     */   {
/* 199 */     return LONG_PARSER.parseLongs(name, request.getParameterValues(name));
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
/*     */   public static Float getFloatParameter(ServletRequest request, String name)
/*     */     throws ServletRequestBindingException
/*     */   {
/* 215 */     if (request.getParameter(name) == null) {
/* 216 */       return null;
/*     */     }
/* 218 */     return Float.valueOf(getRequiredFloatParameter(request, name));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static float getFloatParameter(ServletRequest request, String name, float defaultVal)
/*     */   {
/* 229 */     if (request.getParameter(name) == null) {
/* 230 */       return defaultVal;
/*     */     }
/*     */     try {
/* 233 */       return getRequiredFloatParameter(request, name);
/*     */     }
/*     */     catch (ServletRequestBindingException ex) {}
/* 236 */     return defaultVal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static float[] getFloatParameters(ServletRequest request, String name)
/*     */   {
/*     */     try
/*     */     {
/* 247 */       return getRequiredFloatParameters(request, name);
/*     */     }
/*     */     catch (ServletRequestBindingException ex) {}
/* 250 */     return new float[0];
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
/*     */   public static float getRequiredFloatParameter(ServletRequest request, String name)
/*     */     throws ServletRequestBindingException
/*     */   {
/* 264 */     return FLOAT_PARSER.parseFloat(name, request.getParameter(name));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static float[] getRequiredFloatParameters(ServletRequest request, String name)
/*     */     throws ServletRequestBindingException
/*     */   {
/* 277 */     return FLOAT_PARSER.parseFloats(name, request.getParameterValues(name));
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
/*     */   public static Double getDoubleParameter(ServletRequest request, String name)
/*     */     throws ServletRequestBindingException
/*     */   {
/* 293 */     if (request.getParameter(name) == null) {
/* 294 */       return null;
/*     */     }
/* 296 */     return Double.valueOf(getRequiredDoubleParameter(request, name));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static double getDoubleParameter(ServletRequest request, String name, double defaultVal)
/*     */   {
/* 307 */     if (request.getParameter(name) == null) {
/* 308 */       return defaultVal;
/*     */     }
/*     */     try {
/* 311 */       return getRequiredDoubleParameter(request, name);
/*     */     }
/*     */     catch (ServletRequestBindingException ex) {}
/* 314 */     return defaultVal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static double[] getDoubleParameters(ServletRequest request, String name)
/*     */   {
/*     */     try
/*     */     {
/* 325 */       return getRequiredDoubleParameters(request, name);
/*     */     }
/*     */     catch (ServletRequestBindingException ex) {}
/* 328 */     return new double[0];
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
/*     */   public static double getRequiredDoubleParameter(ServletRequest request, String name)
/*     */     throws ServletRequestBindingException
/*     */   {
/* 342 */     return DOUBLE_PARSER.parseDouble(name, request.getParameter(name));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static double[] getRequiredDoubleParameters(ServletRequest request, String name)
/*     */     throws ServletRequestBindingException
/*     */   {
/* 355 */     return DOUBLE_PARSER.parseDoubles(name, request.getParameterValues(name));
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
/*     */   public static Boolean getBooleanParameter(ServletRequest request, String name)
/*     */     throws ServletRequestBindingException
/*     */   {
/* 373 */     if (request.getParameter(name) == null) {
/* 374 */       return null;
/*     */     }
/* 376 */     return Boolean.valueOf(getRequiredBooleanParameter(request, name));
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
/*     */   public static boolean getBooleanParameter(ServletRequest request, String name, boolean defaultVal)
/*     */   {
/* 389 */     if (request.getParameter(name) == null) {
/* 390 */       return defaultVal;
/*     */     }
/*     */     try {
/* 393 */       return getRequiredBooleanParameter(request, name);
/*     */     }
/*     */     catch (ServletRequestBindingException ex) {}
/* 396 */     return defaultVal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean[] getBooleanParameters(ServletRequest request, String name)
/*     */   {
/*     */     try
/*     */     {
/* 409 */       return getRequiredBooleanParameters(request, name);
/*     */     }
/*     */     catch (ServletRequestBindingException ex) {}
/* 412 */     return new boolean[0];
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
/*     */   public static boolean getRequiredBooleanParameter(ServletRequest request, String name)
/*     */     throws ServletRequestBindingException
/*     */   {
/* 429 */     return BOOLEAN_PARSER.parseBoolean(name, request.getParameter(name));
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
/*     */   public static boolean[] getRequiredBooleanParameters(ServletRequest request, String name)
/*     */     throws ServletRequestBindingException
/*     */   {
/* 445 */     return BOOLEAN_PARSER.parseBooleans(name, request.getParameterValues(name));
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
/*     */   public static String getStringParameter(ServletRequest request, String name)
/*     */     throws ServletRequestBindingException
/*     */   {
/* 460 */     if (request.getParameter(name) == null) {
/* 461 */       return null;
/*     */     }
/* 463 */     return getRequiredStringParameter(request, name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getStringParameter(ServletRequest request, String name, String defaultVal)
/*     */   {
/* 474 */     String val = request.getParameter(name);
/* 475 */     return val != null ? val : defaultVal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] getStringParameters(ServletRequest request, String name)
/*     */   {
/*     */     try
/*     */     {
/* 485 */       return getRequiredStringParameters(request, name);
/*     */     }
/*     */     catch (ServletRequestBindingException ex) {}
/* 488 */     return new String[0];
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
/*     */   public static String getRequiredStringParameter(ServletRequest request, String name)
/*     */     throws ServletRequestBindingException
/*     */   {
/* 502 */     return STRING_PARSER.validateRequiredString(name, request.getParameter(name));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] getRequiredStringParameters(ServletRequest request, String name)
/*     */     throws ServletRequestBindingException
/*     */   {
/* 515 */     return STRING_PARSER.validateRequiredStrings(name, request.getParameterValues(name));
/*     */   }
/*     */   
/*     */   private static abstract class ParameterParser<T>
/*     */   {
/*     */     protected final T parse(String name, String parameter) throws ServletRequestBindingException
/*     */     {
/* 522 */       validateRequiredParameter(name, parameter);
/*     */       try {
/* 524 */         return (T)doParse(parameter);
/*     */       }
/*     */       catch (NumberFormatException ex)
/*     */       {
/* 528 */         throw new ServletRequestBindingException("Required " + getType() + " parameter '" + name + "' with value of '" + parameter + "' is not a valid number", ex);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     protected final void validateRequiredParameter(String name, Object parameter)
/*     */       throws ServletRequestBindingException
/*     */     {
/* 536 */       if (parameter == null)
/* 537 */         throw new MissingServletRequestParameterException(name, getType());
/*     */     }
/*     */     
/*     */     protected abstract String getType();
/*     */     
/*     */     protected abstract T doParse(String paramString) throws NumberFormatException;
/*     */   }
/*     */   
/*     */   private static class IntParser extends ServletRequestUtils.ParameterParser<Integer> {
/*     */     private IntParser() {
/* 547 */       super();
/*     */     }
/*     */     
/*     */     protected String getType() {
/* 551 */       return "int";
/*     */     }
/*     */     
/*     */     protected Integer doParse(String s) throws NumberFormatException
/*     */     {
/* 556 */       return Integer.valueOf(s);
/*     */     }
/*     */     
/*     */     public int parseInt(String name, String parameter) throws ServletRequestBindingException {
/* 560 */       return ((Integer)parse(name, parameter)).intValue();
/*     */     }
/*     */     
/*     */     public int[] parseInts(String name, String[] values) throws ServletRequestBindingException {
/* 564 */       validateRequiredParameter(name, values);
/* 565 */       int[] parameters = new int[values.length];
/* 566 */       for (int i = 0; i < values.length; i++) {
/* 567 */         parameters[i] = parseInt(name, values[i]);
/*     */       }
/* 569 */       return parameters;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LongParser extends ServletRequestUtils.ParameterParser<Long> {
/* 574 */     private LongParser() { super(); }
/*     */     
/*     */     protected String getType()
/*     */     {
/* 578 */       return "long";
/*     */     }
/*     */     
/*     */     protected Long doParse(String parameter) throws NumberFormatException
/*     */     {
/* 583 */       return Long.valueOf(parameter);
/*     */     }
/*     */     
/*     */     public long parseLong(String name, String parameter) throws ServletRequestBindingException {
/* 587 */       return ((Long)parse(name, parameter)).longValue();
/*     */     }
/*     */     
/*     */     public long[] parseLongs(String name, String[] values) throws ServletRequestBindingException {
/* 591 */       validateRequiredParameter(name, values);
/* 592 */       long[] parameters = new long[values.length];
/* 593 */       for (int i = 0; i < values.length; i++) {
/* 594 */         parameters[i] = parseLong(name, values[i]);
/*     */       }
/* 596 */       return parameters;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FloatParser extends ServletRequestUtils.ParameterParser<Float> {
/* 601 */     private FloatParser() { super(); }
/*     */     
/*     */     protected String getType()
/*     */     {
/* 605 */       return "float";
/*     */     }
/*     */     
/*     */     protected Float doParse(String parameter) throws NumberFormatException
/*     */     {
/* 610 */       return Float.valueOf(parameter);
/*     */     }
/*     */     
/*     */     public float parseFloat(String name, String parameter) throws ServletRequestBindingException {
/* 614 */       return ((Float)parse(name, parameter)).floatValue();
/*     */     }
/*     */     
/*     */     public float[] parseFloats(String name, String[] values) throws ServletRequestBindingException {
/* 618 */       validateRequiredParameter(name, values);
/* 619 */       float[] parameters = new float[values.length];
/* 620 */       for (int i = 0; i < values.length; i++) {
/* 621 */         parameters[i] = parseFloat(name, values[i]);
/*     */       }
/* 623 */       return parameters;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DoubleParser extends ServletRequestUtils.ParameterParser<Double> {
/* 628 */     private DoubleParser() { super(); }
/*     */     
/*     */     protected String getType()
/*     */     {
/* 632 */       return "double";
/*     */     }
/*     */     
/*     */     protected Double doParse(String parameter) throws NumberFormatException
/*     */     {
/* 637 */       return Double.valueOf(parameter);
/*     */     }
/*     */     
/*     */     public double parseDouble(String name, String parameter) throws ServletRequestBindingException {
/* 641 */       return ((Double)parse(name, parameter)).doubleValue();
/*     */     }
/*     */     
/*     */     public double[] parseDoubles(String name, String[] values) throws ServletRequestBindingException {
/* 645 */       validateRequiredParameter(name, values);
/* 646 */       double[] parameters = new double[values.length];
/* 647 */       for (int i = 0; i < values.length; i++) {
/* 648 */         parameters[i] = parseDouble(name, values[i]);
/*     */       }
/* 650 */       return parameters;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class BooleanParser extends ServletRequestUtils.ParameterParser<Boolean> {
/* 655 */     private BooleanParser() { super(); }
/*     */     
/*     */     protected String getType()
/*     */     {
/* 659 */       return "boolean";
/*     */     }
/*     */     
/*     */     protected Boolean doParse(String parameter) throws NumberFormatException
/*     */     {
/* 664 */       return Boolean.valueOf((parameter.equalsIgnoreCase("true")) || (parameter.equalsIgnoreCase("on")) || 
/* 665 */         (parameter.equalsIgnoreCase("yes")) || (parameter.equals("1")));
/*     */     }
/*     */     
/*     */     public boolean parseBoolean(String name, String parameter) throws ServletRequestBindingException {
/* 669 */       return ((Boolean)parse(name, parameter)).booleanValue();
/*     */     }
/*     */     
/*     */     public boolean[] parseBooleans(String name, String[] values) throws ServletRequestBindingException {
/* 673 */       validateRequiredParameter(name, values);
/* 674 */       boolean[] parameters = new boolean[values.length];
/* 675 */       for (int i = 0; i < values.length; i++) {
/* 676 */         parameters[i] = parseBoolean(name, values[i]);
/*     */       }
/* 678 */       return parameters;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class StringParser extends ServletRequestUtils.ParameterParser<String> {
/* 683 */     private StringParser() { super(); }
/*     */     
/*     */     protected String getType()
/*     */     {
/* 687 */       return "string";
/*     */     }
/*     */     
/*     */     protected String doParse(String parameter) throws NumberFormatException
/*     */     {
/* 692 */       return parameter;
/*     */     }
/*     */     
/*     */     public String validateRequiredString(String name, String value) throws ServletRequestBindingException {
/* 696 */       validateRequiredParameter(name, value);
/* 697 */       return value;
/*     */     }
/*     */     
/*     */     public String[] validateRequiredStrings(String name, String[] values) throws ServletRequestBindingException {
/* 701 */       validateRequiredParameter(name, values);
/* 702 */       for (String value : values) {
/* 703 */         validateRequiredParameter(name, value);
/*     */       }
/* 705 */       return values;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\bind\ServletRequestUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */