package org.knows.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tika.Tika;
import org.joda.time.DateTime;
import org.knows.model.KSSnippet;
import org.knows.model.KSSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.thymeleaf.util.StringUtils;

@Component
public class TikaContentExtractor {
	public String parseContent(KSSource kssource) throws Exception {
	    Tika tika = new Tika();
	    if(kssource.getLocation().startsWith("http")) {
	    	return getContentfromUrl(kssource);
	    }
	    //try parsing it as file input stream
	    if(!new File(kssource.getLocation()).exists()) {
	    	return null;
	    }
	    
	    FileInputStream fis = new FileInputStream(kssource.getLocation());
	    return tika.parseToString(fis);
	}
	
	String getContentfromUrl(KSSource kssource) throws Exception {
		CloseableHttpClient  httpclient =  HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(kssource.getLocation());

            System.out.println("Executing request " + httpget.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            String responseBody = httpclient.execute(httpget, responseHandler);
            return responseBody;
        } finally {
            httpclient.close();
        }
	}
	
	public boolean preSaveSnippet(final KSSnippet kssnippet, final BindingResult bindingResult) {
		kssnippet.getSource().setSourceDt(DateTime.now().toDate());
		kssnippet.getSource().setVersion(0);
    	if(!kssnippet.getSource().getLocation().startsWith("http")) {
    		String sname = new File(kssnippet.getSource().getLocation()).getName();
    		kssnippet.getSource().setName(sname);
    	}
    	if(kssnippet.getContent()==null || kssnippet.getContent().length()<1) {
    		TikaContentExtractor te = new TikaContentExtractor();
    		try {
	    		String s = te.parseContent(kssnippet.getSource());
	    		if(s==null) {
	    			if(bindingResult!=null) bindingResult.reject("Cannot find content from source or text area");
	    			return false;
	    		}
	    		kssnippet.setContent(s);
    		} catch(Exception e) {
    			if(bindingResult!=null) bindingResult.reject("Failed to parse the file from location");
    			return false;
    		}
    	}
    	kssnippet.setCreatedDt(DateTime.now().toDate());
    	addTag(kssnippet, kssnippet.getDomain());
    	addTag(kssnippet, kssnippet.getLink1());
    	addTag(kssnippet, kssnippet.getLink2());
    	addTag(kssnippet, kssnippet.getLink3());

    	return true;
    }
    
    void addTag(final KSSnippet kssnippet, String tag) {
    	if(kssnippet.getTags()==null) 
    		kssnippet.setTags(new LinkedList<String>());
    	if(!StringUtils.isEmptyOrWhitespace(tag)) {
    		if(kssnippet.getTags().contains(tag)) 
    			return;
    	}
    	kssnippet.getTags().add(tag);
    }
}
