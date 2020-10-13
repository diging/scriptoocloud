package edu.asu.diging.scriptoocloud.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.scriptoocloud.core.service.CloneRepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;


@Controller
public class CloneRepositoryController {

    //@Autowired
    //CloneRepository cloneService;
 
    /*
    @RequestMapping(value = "/clone", method = RequestMethod.GET)
    public String clone(Model model) {
        return "clone";
    }
    */
    /*
    @RequestMapping(value = "/clone", method = RequestMethod.POST)
    public String cloneURL(HttpServletRequest request, HttpServletResponse response) 
     throws MalformedURLException, InvalidRemoteException, TransportException, GitAPIException{
        return cloneService.cloneRepo(request.getParameter("repoURL"));
    }
    */
    
}