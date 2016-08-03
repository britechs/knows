package org.knows.dao;

import org.junit.Test;
import org.knows.model.KSSource;

public class TikaContentExtractorTest {
	@Test
	public void testParseURL() throws Exception
	{
		TikaContentExtractor te = new TikaContentExtractor();
		KSSource source = new KSSource();
		source.setLocation("https://hc.apache.org/httpcomponents-client-ga/httpclient/examples/org/apache/http/examples/client/ClientWithResponseHandler.java");
		String stext = te.parseContent(source);
		System.out.println(stext);
	}
	
	@Test
	public void testParsePDF() throws Exception
	{
		TikaContentExtractor te = new TikaContentExtractor();
		KSSource source = new KSSource();
		source.setLocation("c:/temp/waterV2Pdf.pdf");
		String stext = te.parseContent(source);
		System.out.println(stext);
	}
}
