/*    */ package org.springframework.boot;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.springframework.core.env.SimpleCommandLinePropertySource;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class DefaultApplicationArguments
/*    */   implements ApplicationArguments
/*    */ {
/*    */   private final Source source;
/*    */   private final String[] args;
/*    */   
/*    */   public DefaultApplicationArguments(String[] args)
/*    */   {
/* 41 */     Assert.notNull(args, "Args must not be null");
/* 42 */     this.source = new Source(args);
/* 43 */     this.args = args;
/*    */   }
/*    */   
/*    */   public String[] getSourceArgs()
/*    */   {
/* 48 */     return this.args;
/*    */   }
/*    */   
/*    */   public Set<String> getOptionNames()
/*    */   {
/* 53 */     String[] names = this.source.getPropertyNames();
/* 54 */     return Collections.unmodifiableSet(new HashSet(Arrays.asList(names)));
/*    */   }
/*    */   
/*    */   public boolean containsOption(String name)
/*    */   {
/* 59 */     return this.source.containsProperty(name);
/*    */   }
/*    */   
/*    */   public List<String> getOptionValues(String name)
/*    */   {
/* 64 */     List<String> values = this.source.getOptionValues(name);
/* 65 */     return values == null ? null : Collections.unmodifiableList(values);
/*    */   }
/*    */   
/*    */   public List<String> getNonOptionArgs()
/*    */   {
/* 70 */     return this.source.getNonOptionArgs();
/*    */   }
/*    */   
/*    */   private static class Source extends SimpleCommandLinePropertySource
/*    */   {
/*    */     Source(String[] args) {
/* 76 */       super();
/*    */     }
/*    */     
/*    */     public List<String> getNonOptionArgs()
/*    */     {
/* 81 */       return super.getNonOptionArgs();
/*    */     }
/*    */     
/*    */     public List<String> getOptionValues(String name)
/*    */     {
/* 86 */       return super.getOptionValues(name);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\DefaultApplicationArguments.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */