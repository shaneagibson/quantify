package uk.co.epsilontechnologies.quantify.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import uk.co.epsilontechnologies.quantify.model.MethodInvocation;
import uk.co.epsilontechnologies.quantify.persistence.MethodInvocationDataStore;

import java.util.List;

@RequestMapping(value = { "/methodinvocation" })
@Controller
public class MethodInvocationController {

    @RequestMapping(
            method = { RequestMethod.GET },
            produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public List<MethodInvocation> getAll() {
        return MethodInvocationDataStore.getInstance().getAll();
    }
    
}