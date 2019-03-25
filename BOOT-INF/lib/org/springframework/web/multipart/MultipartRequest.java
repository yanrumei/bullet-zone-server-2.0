package org.springframework.web.multipart;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.util.MultiValueMap;

public abstract interface MultipartRequest
{
  public abstract Iterator<String> getFileNames();
  
  public abstract MultipartFile getFile(String paramString);
  
  public abstract List<MultipartFile> getFiles(String paramString);
  
  public abstract Map<String, MultipartFile> getFileMap();
  
  public abstract MultiValueMap<String, MultipartFile> getMultiFileMap();
  
  public abstract String getMultipartContentType(String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\multipart\MultipartRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */