package org.knows.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.knows.dao.SnippetRepository;
import org.knows.hello.HelloController;
import org.knows.model.KSQuery;
import org.knows.model.KSSnippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class QueryController {
	@Autowired
	private SnippetRepository snippetDao = null;

	static Logger logger = Logger.getLogger(HelloController.class);
    
	@RequestMapping("/ks/query")
    public String doquery(final KSQuery ksquery, final ModelMap model) {
        logger.info("in /query");
        Pageable page =  new PageRequest(0, 10);
        if(ksquery.getPageno()!=0) 
        {
        	page = new PageRequest(ksquery.getPageno()*10,10);
        }
        List<KSQuery> results = null;
        if(!StringUtils.isEmpty(ksquery.getContent()) && !StringUtils.isEmpty(ksquery.getTag()) ) {
        	String[] tags = ksquery.getTag().split(",");
        	Page<KSSnippet> rs = snippetDao.findByContentContainingAndTagsIn(ksquery.getContent(), tags, page);
        	model.addAttribute("results",rs.getContent());
        } else if(!StringUtils.isEmpty(ksquery.getTag())) {
        	String[] tags = ksquery.getTag().split(",");
        	Page<KSSnippet> rs = snippetDao.findByTagsIn( tags, page);
        	model.addAttribute("results",rs.getContent());
        } else if(!StringUtils.isEmpty(ksquery.getContent())) {
        	Page<KSSnippet> rs = snippetDao.findByContentContaining(ksquery.getContent(), page);
        	model.addAttribute("results",rs.getContent());        	
        }
        model.addAttribute("ksquery",ksquery);
        return "ksquery";
    }
	
	@RequestMapping("/ks/view")
    public String doview(@RequestParam("id") String id, final ModelMap model) {
        logger.info("in /query");
        KSSnippet snippet = snippetDao.findOne(id);
        model.addAttribute("snippet",snippet);
        return "ksview";
	}
}
