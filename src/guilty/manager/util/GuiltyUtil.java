package guilty.manager.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class GuiltyUtil 
{
	public File store(InputStream input, File file) throws Exception
	{
		FileOutputStream fileOuput = new FileOutputStream(file);
		BufferedOutputStream output = new BufferedOutputStream(fileOuput);
		
		byte[] buffer = new byte[2048];
		int len = 0;
		
		while((len = input.read(buffer))>0)
			output.write(buffer, 0, len);
		
		output.flush();
		output.close();
		
		return file;
	}
	
	public String extractPageLink(String rawLink)
	{
		String frag = rawLink.substring("<li><a href=\"".length());
		return frag.substring(0, frag.indexOf("\""));
	}
	
	public String extractEdicao(String page)
	{
		String frag = page.substring("naruto".length());
		return frag.substring(frag.lastIndexOf("-"));
	}
}
