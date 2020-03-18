package com.leongregori.cmdbclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
public class CmdbClientController {

    Logger logger = LoggerFactory.getLogger(CmdbClientController.class);

    @Value("${git.token}")
    private String gitToken;
    @Value("${git.cmdb.url}")
    private String gitUrl;

    @RequestMapping(method = RequestMethod.GET, value = "/api")
    public HashMap api(HttpServletRequest request){
        HashMap api = new HashMap<String, String>();
        api.put("self", request.getRequestURL().toString());
        api.put("getProperties", request.getRequestURL().toString()+"/getProperties/{gitCmdbBranch}/{filePathInGitRepo}");
        api.put("example", request.getRequestURL().toString()+"/getProperties/master/gitops/helm.properties");
        return api;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/getProperties/{branch}/**")
    public HashMap cmdbContent(@PathVariable String branch, HttpServletRequest request) {
        String urlTail = new AntPathMatcher().extractPathWithinPattern("api/getProperties/{branch}/**", request.getRequestURI());
        logger.info("received GET request, Branch:"+branch+" urlTail:"+urlTail);
        CmdbGitInterface cmdb = new CmdbGitInterface(gitUrl, gitToken, branch, urlTail);
        HashMap configuration = cmdb.getCmdb();
        return configuration;
    }
}