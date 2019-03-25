package org.springframework.web.multipart;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.core.io.InputStreamSource;

public abstract interface MultipartFile
  extends InputStreamSource
{
  public abstract String getName();
  
  public abstract String getOriginalFilename();
  
  public abstract String getContentType();
  
  public abstract boolean isEmpty();
  
  public abstract long getSize();
  
  public abstract byte[] getBytes()
    throws IOException;
  
  public abstract InputStream getInputStream()
    throws IOException;
  
  public abstract void transferTo(File paramFile)
    throws IOException, IllegalStateException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\multipart\MultipartFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */