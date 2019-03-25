/*    */ package org.springframework.web.servlet.support;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import javax.servlet.http.HttpSession;
/*    */ import org.springframework.web.servlet.FlashMap;
/*    */ import org.springframework.web.util.WebUtils;
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
/*    */ public class SessionFlashMapManager
/*    */   extends AbstractFlashMapManager
/*    */ {
/* 36 */   private static final String FLASH_MAPS_SESSION_ATTRIBUTE = SessionFlashMapManager.class.getName() + ".FLASH_MAPS";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected List<FlashMap> retrieveFlashMaps(HttpServletRequest request)
/*    */   {
/* 45 */     HttpSession session = request.getSession(false);
/* 46 */     return session != null ? (List)session.getAttribute(FLASH_MAPS_SESSION_ATTRIBUTE) : null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void updateFlashMaps(List<FlashMap> flashMaps, HttpServletRequest request, HttpServletResponse response)
/*    */   {
/* 54 */     WebUtils.setSessionAttribute(request, FLASH_MAPS_SESSION_ATTRIBUTE, !flashMaps.isEmpty() ? flashMaps : null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected Object getFlashMapsMutex(HttpServletRequest request)
/*    */   {
/* 64 */     return WebUtils.getSessionMutex(request.getSession());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\support\SessionFlashMapManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */