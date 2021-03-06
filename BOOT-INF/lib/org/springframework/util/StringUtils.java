/*      */ package org.springframework.util;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TimeZone;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class StringUtils
/*      */ {
/*      */   private static final String FOLDER_SEPARATOR = "/";
/*      */   private static final String WINDOWS_FOLDER_SEPARATOR = "\\";
/*      */   private static final String TOP_PATH = "..";
/*      */   private static final String CURRENT_PATH = ".";
/*      */   private static final char EXTENSION_SEPARATOR = '.';
/*      */   
/*      */   public static boolean isEmpty(Object str)
/*      */   {
/*   85 */     return (str == null) || ("".equals(str));
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
/*      */   public static boolean hasLength(CharSequence str)
/*      */   {
/*  104 */     return (str != null) && (str.length() > 0);
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
/*      */   public static boolean hasLength(String str)
/*      */   {
/*  117 */     return (str != null) && (!str.isEmpty());
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
/*      */   public static boolean hasText(CharSequence str)
/*      */   {
/*  138 */     return (hasLength(str)) && (containsText(str));
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
/*      */   public static boolean hasText(String str)
/*      */   {
/*  152 */     return (hasLength(str)) && (containsText(str));
/*      */   }
/*      */   
/*      */   private static boolean containsText(CharSequence str) {
/*  156 */     int strLen = str.length();
/*  157 */     for (int i = 0; i < strLen; i++) {
/*  158 */       if (!Character.isWhitespace(str.charAt(i))) {
/*  159 */         return true;
/*      */       }
/*      */     }
/*  162 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean containsWhitespace(CharSequence str)
/*      */   {
/*  173 */     if (!hasLength(str)) {
/*  174 */       return false;
/*      */     }
/*      */     
/*  177 */     int strLen = str.length();
/*  178 */     for (int i = 0; i < strLen; i++) {
/*  179 */       if (Character.isWhitespace(str.charAt(i))) {
/*  180 */         return true;
/*      */       }
/*      */     }
/*  183 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean containsWhitespace(String str)
/*      */   {
/*  194 */     return containsWhitespace(str);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String trimWhitespace(String str)
/*      */   {
/*  204 */     if (!hasLength(str)) {
/*  205 */       return str;
/*      */     }
/*      */     
/*  208 */     StringBuilder sb = new StringBuilder(str);
/*  209 */     while ((sb.length() > 0) && (Character.isWhitespace(sb.charAt(0)))) {
/*  210 */       sb.deleteCharAt(0);
/*      */     }
/*  212 */     while ((sb.length() > 0) && (Character.isWhitespace(sb.charAt(sb.length() - 1)))) {
/*  213 */       sb.deleteCharAt(sb.length() - 1);
/*      */     }
/*  215 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String trimAllWhitespace(String str)
/*      */   {
/*  226 */     if (!hasLength(str)) {
/*  227 */       return str;
/*      */     }
/*      */     
/*  230 */     int len = str.length();
/*  231 */     StringBuilder sb = new StringBuilder(str.length());
/*  232 */     for (int i = 0; i < len; i++) {
/*  233 */       char c = str.charAt(i);
/*  234 */       if (!Character.isWhitespace(c)) {
/*  235 */         sb.append(c);
/*      */       }
/*      */     }
/*  238 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String trimLeadingWhitespace(String str)
/*      */   {
/*  248 */     if (!hasLength(str)) {
/*  249 */       return str;
/*      */     }
/*      */     
/*  252 */     StringBuilder sb = new StringBuilder(str);
/*  253 */     while ((sb.length() > 0) && (Character.isWhitespace(sb.charAt(0)))) {
/*  254 */       sb.deleteCharAt(0);
/*      */     }
/*  256 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String trimTrailingWhitespace(String str)
/*      */   {
/*  266 */     if (!hasLength(str)) {
/*  267 */       return str;
/*      */     }
/*      */     
/*  270 */     StringBuilder sb = new StringBuilder(str);
/*  271 */     while ((sb.length() > 0) && (Character.isWhitespace(sb.charAt(sb.length() - 1)))) {
/*  272 */       sb.deleteCharAt(sb.length() - 1);
/*      */     }
/*  274 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String trimLeadingCharacter(String str, char leadingCharacter)
/*      */   {
/*  284 */     if (!hasLength(str)) {
/*  285 */       return str;
/*      */     }
/*      */     
/*  288 */     StringBuilder sb = new StringBuilder(str);
/*  289 */     while ((sb.length() > 0) && (sb.charAt(0) == leadingCharacter)) {
/*  290 */       sb.deleteCharAt(0);
/*      */     }
/*  292 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String trimTrailingCharacter(String str, char trailingCharacter)
/*      */   {
/*  302 */     if (!hasLength(str)) {
/*  303 */       return str;
/*      */     }
/*      */     
/*  306 */     StringBuilder sb = new StringBuilder(str);
/*  307 */     while ((sb.length() > 0) && (sb.charAt(sb.length() - 1) == trailingCharacter)) {
/*  308 */       sb.deleteCharAt(sb.length() - 1);
/*      */     }
/*  310 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean startsWithIgnoreCase(String str, String prefix)
/*      */   {
/*  321 */     return (str != null) && (prefix != null) && (str.length() >= prefix.length()) && 
/*  322 */       (str.regionMatches(true, 0, prefix, 0, prefix.length()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean endsWithIgnoreCase(String str, String suffix)
/*      */   {
/*  333 */     return (str != null) && (suffix != null) && (str.length() >= suffix.length()) && 
/*  334 */       (str.regionMatches(true, str.length() - suffix.length(), suffix, 0, suffix.length()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean substringMatch(CharSequence str, int index, CharSequence substring)
/*      */   {
/*  345 */     if (index + substring.length() > str.length()) {
/*  346 */       return false;
/*      */     }
/*  348 */     for (int i = 0; i < substring.length(); i++) {
/*  349 */       if (str.charAt(index + i) != substring.charAt(i)) {
/*  350 */         return false;
/*      */       }
/*      */     }
/*  353 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int countOccurrencesOf(String str, String sub)
/*      */   {
/*  362 */     if ((!hasLength(str)) || (!hasLength(sub))) {
/*  363 */       return 0;
/*      */     }
/*      */     
/*  366 */     int count = 0;
/*  367 */     int pos = 0;
/*      */     int idx;
/*  369 */     while ((idx = str.indexOf(sub, pos)) != -1) {
/*  370 */       count++;
/*  371 */       pos = idx + sub.length();
/*      */     }
/*  373 */     return count;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String replace(String inString, String oldPattern, String newPattern)
/*      */   {
/*  384 */     if ((!hasLength(inString)) || (!hasLength(oldPattern)) || (newPattern == null)) {
/*  385 */       return inString;
/*      */     }
/*  387 */     int index = inString.indexOf(oldPattern);
/*  388 */     if (index == -1)
/*      */     {
/*  390 */       return inString;
/*      */     }
/*      */     
/*  393 */     int capacity = inString.length();
/*  394 */     if (newPattern.length() > oldPattern.length()) {
/*  395 */       capacity += 16;
/*      */     }
/*  397 */     StringBuilder sb = new StringBuilder(capacity);
/*      */     
/*  399 */     int pos = 0;
/*  400 */     int patLen = oldPattern.length();
/*  401 */     while (index >= 0) {
/*  402 */       sb.append(inString.substring(pos, index));
/*  403 */       sb.append(newPattern);
/*  404 */       pos = index + patLen;
/*  405 */       index = inString.indexOf(oldPattern, pos);
/*      */     }
/*      */     
/*      */ 
/*  409 */     sb.append(inString.substring(pos));
/*  410 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String delete(String inString, String pattern)
/*      */   {
/*  420 */     return replace(inString, pattern, "");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String deleteAny(String inString, String charsToDelete)
/*      */   {
/*  431 */     if ((!hasLength(inString)) || (!hasLength(charsToDelete))) {
/*  432 */       return inString;
/*      */     }
/*      */     
/*  435 */     StringBuilder sb = new StringBuilder(inString.length());
/*  436 */     for (int i = 0; i < inString.length(); i++) {
/*  437 */       char c = inString.charAt(i);
/*  438 */       if (charsToDelete.indexOf(c) == -1) {
/*  439 */         sb.append(c);
/*      */       }
/*      */     }
/*  442 */     return sb.toString();
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
/*      */   public static String quote(String str)
/*      */   {
/*  457 */     return str != null ? "'" + str + "'" : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Object quoteIfString(Object obj)
/*      */   {
/*  468 */     return (obj instanceof String) ? quote((String)obj) : obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String unqualify(String qualifiedName)
/*      */   {
/*  477 */     return unqualify(qualifiedName, '.');
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String unqualify(String qualifiedName, char separator)
/*      */   {
/*  487 */     return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String capitalize(String str)
/*      */   {
/*  498 */     return changeFirstCharacterCase(str, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String uncapitalize(String str)
/*      */   {
/*  509 */     return changeFirstCharacterCase(str, false);
/*      */   }
/*      */   
/*      */   private static String changeFirstCharacterCase(String str, boolean capitalize) {
/*  513 */     if (!hasLength(str)) {
/*  514 */       return str;
/*      */     }
/*      */     
/*  517 */     char baseChar = str.charAt(0);
/*      */     char updatedChar;
/*  519 */     char updatedChar; if (capitalize) {
/*  520 */       updatedChar = Character.toUpperCase(baseChar);
/*      */     }
/*      */     else {
/*  523 */       updatedChar = Character.toLowerCase(baseChar);
/*      */     }
/*  525 */     if (baseChar == updatedChar) {
/*  526 */       return str;
/*      */     }
/*      */     
/*  529 */     char[] chars = str.toCharArray();
/*  530 */     chars[0] = updatedChar;
/*  531 */     return new String(chars, 0, chars.length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getFilename(String path)
/*      */   {
/*  541 */     if (path == null) {
/*  542 */       return null;
/*      */     }
/*      */     
/*  545 */     int separatorIndex = path.lastIndexOf("/");
/*  546 */     return separatorIndex != -1 ? path.substring(separatorIndex + 1) : path;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getFilenameExtension(String path)
/*      */   {
/*  556 */     if (path == null) {
/*  557 */       return null;
/*      */     }
/*      */     
/*  560 */     int extIndex = path.lastIndexOf('.');
/*  561 */     if (extIndex == -1) {
/*  562 */       return null;
/*      */     }
/*      */     
/*  565 */     int folderIndex = path.lastIndexOf("/");
/*  566 */     if (folderIndex > extIndex) {
/*  567 */       return null;
/*      */     }
/*      */     
/*  570 */     return path.substring(extIndex + 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String stripFilenameExtension(String path)
/*      */   {
/*  580 */     if (path == null) {
/*  581 */       return null;
/*      */     }
/*      */     
/*  584 */     int extIndex = path.lastIndexOf('.');
/*  585 */     if (extIndex == -1) {
/*  586 */       return path;
/*      */     }
/*      */     
/*  589 */     int folderIndex = path.lastIndexOf("/");
/*  590 */     if (folderIndex > extIndex) {
/*  591 */       return path;
/*      */     }
/*      */     
/*  594 */     return path.substring(0, extIndex);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String applyRelativePath(String path, String relativePath)
/*      */   {
/*  606 */     int separatorIndex = path.lastIndexOf("/");
/*  607 */     if (separatorIndex != -1) {
/*  608 */       String newPath = path.substring(0, separatorIndex);
/*  609 */       if (!relativePath.startsWith("/")) {
/*  610 */         newPath = newPath + "/";
/*      */       }
/*  612 */       return newPath + relativePath;
/*      */     }
/*      */     
/*  615 */     return relativePath;
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
/*      */   public static String cleanPath(String path)
/*      */   {
/*  628 */     if (path == null) {
/*  629 */       return null;
/*      */     }
/*  631 */     String pathToUse = replace(path, "\\", "/");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  637 */     int prefixIndex = pathToUse.indexOf(":");
/*  638 */     String prefix = "";
/*  639 */     if (prefixIndex != -1) {
/*  640 */       prefix = pathToUse.substring(0, prefixIndex + 1);
/*  641 */       if (prefix.contains("/")) {
/*  642 */         prefix = "";
/*      */       }
/*      */       else {
/*  645 */         pathToUse = pathToUse.substring(prefixIndex + 1);
/*      */       }
/*      */     }
/*  648 */     if (pathToUse.startsWith("/")) {
/*  649 */       prefix = prefix + "/";
/*  650 */       pathToUse = pathToUse.substring(1);
/*      */     }
/*      */     
/*  653 */     String[] pathArray = delimitedListToStringArray(pathToUse, "/");
/*  654 */     List<String> pathElements = new LinkedList();
/*  655 */     int tops = 0;
/*      */     
/*  657 */     for (int i = pathArray.length - 1; i >= 0; i--) {
/*  658 */       String element = pathArray[i];
/*  659 */       if (!".".equals(element))
/*      */       {
/*      */ 
/*  662 */         if ("..".equals(element))
/*      */         {
/*  664 */           tops++;
/*      */ 
/*      */         }
/*  667 */         else if (tops > 0)
/*      */         {
/*  669 */           tops--;
/*      */         }
/*      */         else
/*      */         {
/*  673 */           pathElements.add(0, element);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  679 */     for (int i = 0; i < tops; i++) {
/*  680 */       pathElements.add(0, "..");
/*      */     }
/*      */     
/*  683 */     return prefix + collectionToDelimitedString(pathElements, "/");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean pathEquals(String path1, String path2)
/*      */   {
/*  693 */     return cleanPath(path1).equals(cleanPath(path2));
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
/*      */   public static Locale parseLocaleString(String localeString)
/*      */   {
/*  706 */     String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
/*  707 */     String language = parts.length > 0 ? parts[0] : "";
/*  708 */     String country = parts.length > 1 ? parts[1] : "";
/*      */     
/*  710 */     validateLocalePart(language);
/*  711 */     validateLocalePart(country);
/*      */     
/*  713 */     String variant = "";
/*  714 */     if (parts.length > 2)
/*      */     {
/*      */ 
/*  717 */       int endIndexOfCountryCode = localeString.indexOf(country, language.length()) + country.length();
/*      */       
/*  719 */       variant = trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
/*  720 */       if (variant.startsWith("_")) {
/*  721 */         variant = trimLeadingCharacter(variant, '_');
/*      */       }
/*      */     }
/*  724 */     return language.length() > 0 ? new Locale(language, country, variant) : null;
/*      */   }
/*      */   
/*      */   private static void validateLocalePart(String localePart) {
/*  728 */     for (int i = 0; i < localePart.length(); i++) {
/*  729 */       char ch = localePart.charAt(i);
/*  730 */       if ((ch != ' ') && (ch != '_') && (ch != '#') && (!Character.isLetterOrDigit(ch))) {
/*  731 */         throw new IllegalArgumentException("Locale part \"" + localePart + "\" contains invalid characters");
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
/*      */   public static String toLanguageTag(Locale locale)
/*      */   {
/*  744 */     return locale.getLanguage() + (hasText(locale.getCountry()) ? "-" + locale.getCountry() : "");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static TimeZone parseTimeZoneString(String timeZoneString)
/*      */   {
/*  755 */     TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
/*  756 */     if (("GMT".equals(timeZone.getID())) && (!timeZoneString.startsWith("GMT")))
/*      */     {
/*  758 */       throw new IllegalArgumentException("Invalid time zone specification '" + timeZoneString + "'");
/*      */     }
/*  760 */     return timeZone;
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
/*      */   public static String[] addStringToArray(String[] array, String str)
/*      */   {
/*  777 */     if (ObjectUtils.isEmpty(array)) {
/*  778 */       return new String[] { str };
/*      */     }
/*      */     
/*  781 */     String[] newArr = new String[array.length + 1];
/*  782 */     System.arraycopy(array, 0, newArr, 0, array.length);
/*  783 */     newArr[array.length] = str;
/*  784 */     return newArr;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String[] concatenateStringArrays(String[] array1, String[] array2)
/*      */   {
/*  796 */     if (ObjectUtils.isEmpty(array1)) {
/*  797 */       return array2;
/*      */     }
/*  799 */     if (ObjectUtils.isEmpty(array2)) {
/*  800 */       return array1;
/*      */     }
/*      */     
/*  803 */     String[] newArr = new String[array1.length + array2.length];
/*  804 */     System.arraycopy(array1, 0, newArr, 0, array1.length);
/*  805 */     System.arraycopy(array2, 0, newArr, array1.length, array2.length);
/*  806 */     return newArr;
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
/*      */   public static String[] mergeStringArrays(String[] array1, String[] array2)
/*      */   {
/*  820 */     if (ObjectUtils.isEmpty(array1)) {
/*  821 */       return array2;
/*      */     }
/*  823 */     if (ObjectUtils.isEmpty(array2)) {
/*  824 */       return array1;
/*      */     }
/*      */     
/*  827 */     List<String> result = new ArrayList();
/*  828 */     result.addAll(Arrays.asList(array1));
/*  829 */     for (String str : array2) {
/*  830 */       if (!result.contains(str)) {
/*  831 */         result.add(str);
/*      */       }
/*      */     }
/*  834 */     return toStringArray(result);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String[] sortStringArray(String[] array)
/*      */   {
/*  843 */     if (ObjectUtils.isEmpty(array)) {
/*  844 */       return new String[0];
/*      */     }
/*      */     
/*  847 */     Arrays.sort(array);
/*  848 */     return array;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String[] toStringArray(Collection<String> collection)
/*      */   {
/*  858 */     if (collection == null) {
/*  859 */       return null;
/*      */     }
/*      */     
/*  862 */     return (String[])collection.toArray(new String[collection.size()]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String[] toStringArray(Enumeration<String> enumeration)
/*      */   {
/*  872 */     if (enumeration == null) {
/*  873 */       return null;
/*      */     }
/*      */     
/*  876 */     List<String> list = Collections.list(enumeration);
/*  877 */     return (String[])list.toArray(new String[list.size()]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String[] trimArrayElements(String[] array)
/*      */   {
/*  887 */     if (ObjectUtils.isEmpty(array)) {
/*  888 */       return new String[0];
/*      */     }
/*      */     
/*  891 */     String[] result = new String[array.length];
/*  892 */     for (int i = 0; i < array.length; i++) {
/*  893 */       String element = array[i];
/*  894 */       result[i] = (element != null ? element.trim() : null);
/*      */     }
/*  896 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String[] removeDuplicateStrings(String[] array)
/*      */   {
/*  906 */     if (ObjectUtils.isEmpty(array)) {
/*  907 */       return array;
/*      */     }
/*      */     
/*  910 */     Set<String> set = new LinkedHashSet();
/*  911 */     for (String element : array) {
/*  912 */       set.add(element);
/*      */     }
/*  914 */     return toStringArray(set);
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
/*      */   public static String[] split(String toSplit, String delimiter)
/*      */   {
/*  927 */     if ((!hasLength(toSplit)) || (!hasLength(delimiter))) {
/*  928 */       return null;
/*      */     }
/*  930 */     int offset = toSplit.indexOf(delimiter);
/*  931 */     if (offset < 0) {
/*  932 */       return null;
/*      */     }
/*      */     
/*  935 */     String beforeDelimiter = toSplit.substring(0, offset);
/*  936 */     String afterDelimiter = toSplit.substring(offset + delimiter.length());
/*  937 */     return new String[] { beforeDelimiter, afterDelimiter };
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
/*      */   public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter)
/*      */   {
/*  952 */     return splitArrayElementsIntoProperties(array, delimiter, null);
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
/*      */   public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter, String charsToDelete)
/*      */   {
/*  972 */     if (ObjectUtils.isEmpty(array)) {
/*  973 */       return null;
/*      */     }
/*      */     
/*  976 */     Properties result = new Properties();
/*  977 */     for (String element : array) {
/*  978 */       if (charsToDelete != null) {
/*  979 */         element = deleteAny(element, charsToDelete);
/*      */       }
/*  981 */       String[] splittedElement = split(element, delimiter);
/*  982 */       if (splittedElement != null)
/*      */       {
/*      */ 
/*  985 */         result.setProperty(splittedElement[0].trim(), splittedElement[1].trim()); }
/*      */     }
/*  987 */     return result;
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
/*      */   public static String[] tokenizeToStringArray(String str, String delimiters)
/*      */   {
/* 1007 */     return tokenizeToStringArray(str, delimiters, true, true);
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
/*      */   public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens)
/*      */   {
/* 1032 */     if (str == null) {
/* 1033 */       return null;
/*      */     }
/*      */     
/* 1036 */     StringTokenizer st = new StringTokenizer(str, delimiters);
/* 1037 */     List<String> tokens = new ArrayList();
/* 1038 */     while (st.hasMoreTokens()) {
/* 1039 */       String token = st.nextToken();
/* 1040 */       if (trimTokens) {
/* 1041 */         token = token.trim();
/*      */       }
/* 1043 */       if ((!ignoreEmptyTokens) || (token.length() > 0)) {
/* 1044 */         tokens.add(token);
/*      */       }
/*      */     }
/* 1047 */     return toStringArray(tokens);
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
/*      */   public static String[] delimitedListToStringArray(String str, String delimiter)
/*      */   {
/* 1064 */     return delimitedListToStringArray(str, delimiter, null);
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
/*      */   public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete)
/*      */   {
/* 1083 */     if (str == null) {
/* 1084 */       return new String[0];
/*      */     }
/* 1086 */     if (delimiter == null) {
/* 1087 */       return new String[] { str };
/*      */     }
/*      */     
/* 1090 */     List<String> result = new ArrayList();
/* 1091 */     if ("".equals(delimiter)) {
/* 1092 */       for (int i = 0; i < str.length(); i++) {
/* 1093 */         result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
/*      */       }
/*      */     }
/*      */     else {
/* 1097 */       int pos = 0;
/*      */       int delPos;
/* 1099 */       while ((delPos = str.indexOf(delimiter, pos)) != -1) {
/* 1100 */         result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
/* 1101 */         pos = delPos + delimiter.length();
/*      */       }
/* 1103 */       if ((str.length() > 0) && (pos <= str.length()))
/*      */       {
/* 1105 */         result.add(deleteAny(str.substring(pos), charsToDelete));
/*      */       }
/*      */     }
/* 1108 */     return toStringArray(result);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String[] commaDelimitedListToStringArray(String str)
/*      */   {
/* 1118 */     return delimitedListToStringArray(str, ",");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Set<String> commaDelimitedListToSet(String str)
/*      */   {
/* 1130 */     Set<String> set = new LinkedHashSet();
/* 1131 */     String[] tokens = commaDelimitedListToStringArray(str);
/* 1132 */     for (String token : tokens) {
/* 1133 */       set.add(token);
/*      */     }
/* 1135 */     return set;
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
/*      */   public static String collectionToDelimitedString(Collection<?> coll, String delim, String prefix, String suffix)
/*      */   {
/* 1148 */     if (CollectionUtils.isEmpty(coll)) {
/* 1149 */       return "";
/*      */     }
/*      */     
/* 1152 */     StringBuilder sb = new StringBuilder();
/* 1153 */     Iterator<?> it = coll.iterator();
/* 1154 */     while (it.hasNext()) {
/* 1155 */       sb.append(prefix).append(it.next()).append(suffix);
/* 1156 */       if (it.hasNext()) {
/* 1157 */         sb.append(delim);
/*      */       }
/*      */     }
/* 1160 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String collectionToDelimitedString(Collection<?> coll, String delim)
/*      */   {
/* 1171 */     return collectionToDelimitedString(coll, delim, "", "");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String collectionToCommaDelimitedString(Collection<?> coll)
/*      */   {
/* 1181 */     return collectionToDelimitedString(coll, ",");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String arrayToDelimitedString(Object[] arr, String delim)
/*      */   {
/* 1192 */     if (ObjectUtils.isEmpty(arr)) {
/* 1193 */       return "";
/*      */     }
/* 1195 */     if (arr.length == 1) {
/* 1196 */       return ObjectUtils.nullSafeToString(arr[0]);
/*      */     }
/*      */     
/* 1199 */     StringBuilder sb = new StringBuilder();
/* 1200 */     for (int i = 0; i < arr.length; i++) {
/* 1201 */       if (i > 0) {
/* 1202 */         sb.append(delim);
/*      */       }
/* 1204 */       sb.append(arr[i]);
/*      */     }
/* 1206 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String arrayToCommaDelimitedString(Object[] arr)
/*      */   {
/* 1217 */     return arrayToDelimitedString(arr, ",");
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\StringUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */