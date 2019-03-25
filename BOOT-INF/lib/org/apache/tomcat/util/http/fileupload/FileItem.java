package org.apache.tomcat.util.http.fileupload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public abstract interface FileItem
  extends FileItemHeadersSupport
{
  public abstract InputStream getInputStream()
    throws IOException;
  
  public abstract String getContentType();
  
  public abstract String getName();
  
  public abstract boolean isInMemory();
  
  public abstract long getSize();
  
  public abstract byte[] get();
  
  public abstract String getString(String paramString)
    throws UnsupportedEncodingException;
  
  public abstract String getString();
  
  public abstract void write(File paramFile)
    throws Exception;
  
  public abstract void delete();
  
  public abstract String getFieldName();
  
  public abstract void setFieldName(String paramString);
  
  public abstract boolean isFormField();
  
  public abstract void setFormField(boolean paramBoolean);
  
  public abstract OutputStream getOutputStream()
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileupload\FileItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */