/*    */ package org.springframework.boot.autoconfigure.info;
/*    */ 
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.beans.factory.annotation.Value;
/*    */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*    */ import org.springframework.core.io.ClassPathResource;
/*    */ import org.springframework.core.io.Resource;
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
/*    */ @ConfigurationProperties(prefix="spring.info")
/*    */ public class ProjectInfoProperties
/*    */ {
/* 34 */   private final Build build = new Build();
/*    */   
/* 36 */   private final Git git = new Git();
/*    */   
/*    */   public Build getBuild() {
/* 39 */     return this.build;
/*    */   }
/*    */   
/*    */   public Git getGit() {
/* 43 */     return this.git;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @Autowired
/*    */   void setDefaultGitLocation(@Value("${spring.git.properties:classpath:git.properties}") Resource defaultGitLocation)
/*    */   {
/* 53 */     getGit().setLocation(defaultGitLocation);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static class Build
/*    */   {
/* 64 */     private Resource location = new ClassPathResource("META-INF/build-info.properties");
/*    */     
/*    */     public Resource getLocation()
/*    */     {
/* 68 */       return this.location;
/*    */     }
/*    */     
/*    */     public void setLocation(Resource location) {
/* 72 */       this.location = location;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static class Git
/*    */   {
/*    */     private Resource location;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     public Resource getLocation()
/*    */     {
/* 88 */       return this.location;
/*    */     }
/*    */     
/*    */     public void setLocation(Resource location) {
/* 92 */       this.location = location;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\info\ProjectInfoProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */