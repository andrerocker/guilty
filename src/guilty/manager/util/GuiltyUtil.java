package guilty.manager.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Encapsula diversas funcionalidade que inicialmente
 * nao tem um local muito especifico.
 * 
 * @author andresouza aka andrerocker
 */
public class GuiltyUtil 
{
	/**
	 * Responsavel por armazenar stream passada como paramentro
	 * a arquivo solicitado (tabem passado como parametro.
	 * @param input
	 * @param file
	 * @return
	 * @throws Exception
	 */
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
	
	/**
	 * Processa linha crua a fim de obter o nome da pagina
	 * @param rawLink
	 * @return
	 */
	public String extractPageLink(String rawLink)
	{
		String frag = rawLink.substring("<li><a href=\"".length());
		return frag.substring(0, frag.indexOf("\""));
	}
	
	/**
	 * Processa link a fim de obter a edicao do manga (episodio) 
	 * @param manga
	 * @param page
	 * @return
	 */
	public String extractEdicao(String manga, String page)
	{
		String frag = page.substring(manga.length());
		return frag.substring(0, frag.lastIndexOf("-"));
	}
}
