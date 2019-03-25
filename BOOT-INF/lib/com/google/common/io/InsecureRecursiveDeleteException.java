/*    */ package com.google.common.io;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.nio.file.FileSystemException;
/*    */ import javax.annotation.Nullable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Beta
/*    */ @GwtIncompatible
/*    */ public final class InsecureRecursiveDeleteException
/*    */   extends FileSystemException
/*    */ {
/*    */   public InsecureRecursiveDeleteException(@Nullable String file)
/*    */   {
/* 44 */     super(file, null, "unable to guarantee security of recursive delete");
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\io\InsecureRecursiveDeleteException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */