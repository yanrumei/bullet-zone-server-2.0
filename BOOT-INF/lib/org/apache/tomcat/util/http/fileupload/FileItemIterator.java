package org.apache.tomcat.util.http.fileupload;

import java.io.IOException;

public abstract interface FileItemIterator
{
  public abstract boolean hasNext()
    throws FileUploadException, IOException;
  
  public abstract FileItemStream next()
    throws FileUploadException, IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileupload\FileItemIterator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */