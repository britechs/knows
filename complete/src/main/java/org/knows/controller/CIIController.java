package org.knows.controller;

import java.io.File;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.knows.dao.SnippetRepository;
import org.knows.dao.TikaContentExtractor;
import org.knows.hello.HelloController;
import org.knows.model.KSSnippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

@Controller
public class CIIController {
	
	@Autowired
	private SnippetRepository snippetDao = null;
	
	@Autowired
	private TikaContentExtractor extractor = null;
	
    public CIIController() {
        super();
    }

	static Logger logger = Logger.getLogger(HelloController.class);
    
	@RequestMapping("/cii")
    public String docapture(final KSSnippet kssnippet, final ModelMap model) {
        logger.info("in /cii");
        //KSSnippet snippet = new KSSnippet();
        model.addAttribute("kssnippet",kssnippet);
        return "ciiupload";
    }
	
    @RequestMapping(value="/ciiupdate", params={"save"})
    public String saveKSSnippet(final KSSnippet kssnippet, final BindingResult bindingResult, final ModelMap model) {
        if (bindingResult.hasErrors()) {
        	model.addAttribute("kssnippet",kssnippet);
        	return "ciiupload";
        }
        boolean b = extractor.preSaveSnippet(kssnippet,bindingResult);
        if(!b) {
        	logger.error("Failed to extract content :" + kssnippet.getSource().getLocation());
        	model.addAttribute("kssnippet",kssnippet);
        	return "ciiupload";
        }
        
        snippetDao.save(kssnippet);
        logger.info("Indexed a snippet:" + kssnippet.getId());
        model.clear();
        return "redirect:/cii";
    }
    
    @RequestMapping(value="/ciiupdate", params={"addTag"})
    public String addRow(final KSSnippet kssnippet, final BindingResult bindingResult,
    		final ModelMap model) {
    	kssnippet.getTags().add("");
    	model.addAttribute("kssnippet",kssnippet);
        return "ciiupload";
    }
    
    @RequestMapping(value="/ciiupdate", params={"removeTag"})
    public String removeRow(final KSSnippet kssnippet, final BindingResult bindingResult, 
    		final ModelMap model,final HttpServletRequest req) {
        final Integer rowId = Integer.valueOf(req.getParameter("removeTag"));
        kssnippet.getTags().remove(rowId.intValue());
        model.addAttribute("kssnippet",kssnippet);
        return "ciiupload";
    }
    
}
