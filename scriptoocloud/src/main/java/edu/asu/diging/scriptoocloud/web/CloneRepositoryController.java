package edu.asu.diging.scriptoocloud.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.scriptoocloud.core.forms.CloneForm;
import edu.asu.diging.scriptoocloud.core.model.Repository;
import edu.asu.diging.scriptoocloud.core.service.CloneRepository;
import edu.asu.diging.scriptoocloud.core.service.impl.CloneRepositoryImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.NoSuchElementException;


@Controller
public class CloneRepositoryController {

    @Autowired
    CloneRepository cloneService;
 
  
    @RequestMapping(value = "/clone", method = RequestMethod.GET)
    public String clone(Model model) {
        model.addAttribute("clone", new CloneForm());
        return "clone";
    }
  

    @RequestMapping(value = "/clone", method = RequestMethod.POST)
    public String clone(@Valid @ModelAttribute("clone") CloneForm cloneForm, BindingResult result, Model model) 
                             throws InvalidRemoteException, TransportException, 
                                MalformedURLException, NoSuchElementException, GitAPIException {
                                                       
        if(result.hasErrors()){
            model.addAttribute("clone", new CloneForm());
            return "clone";
        }                 
                          
        return cloneService.cloneRepo(cloneForm.getUrl());               
    }
    
    
}