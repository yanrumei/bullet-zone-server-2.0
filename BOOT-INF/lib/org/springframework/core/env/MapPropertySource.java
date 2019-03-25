/*    */ package org.springframework.core.env;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public class MapPropertySource
/*    */   extends EnumerablePropertySource<Map<String, Object>>
/*    */ {
/*    */   public MapPropertySource(String name, Map<String, Object> source)
/*    */   {
/* 34 */     super(name, source);
/*    */   }
/*    */   
/*    */ 
/*    */   public Object getProperty(String name)
/*    */   {
/* 40 */     return ((Map)this.source).get(name);
/*    */   }
/*    */   
/*    */   public boolean containsProperty(String name)
/*    */   {
/* 45 */     return ((Map)this.source).containsKey(name);
/*    */   }
/*    */   
/*    */   public String[] getPropertyNames()
/*    */   {
/* 50 */     return StringUtils.toStringArray(((Map)this.source).keySet());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\env\MapPropertySource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */