package org.knows.service;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.knows.dao.SnippetRepository;
import org.knows.dao.TikaContentExtractor;
import org.knows.model.KSSnippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CIIServiceController {
	@Autowired
	private SnippetRepository snippetDao = null;
	
	@Autowired
	private TikaContentExtractor extractor = null;
	
	static Logger logger = Logger.getLogger(CIIServiceController.class);
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    
    @RequestMapping(value = "/ciiservice/save", method = RequestMethod.POST)//, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponse<String>> saveKSSnippet(@RequestBody KSSnippet kssnippet) {
        boolean b = extractor.preSaveSnippet(kssnippet,null);
        if(!b) {
        	RestResponse<String> res = new RestResponse("Invalid","1001");
        	return ResponseEntity.badRequest().body(res);
        }
        
        snippetDao.save(kssnippet);
        RestResponse<String> res = new RestResponse(kssnippet.getId(),"0");
        return ResponseEntity.accepted().body(res);
    }
}

