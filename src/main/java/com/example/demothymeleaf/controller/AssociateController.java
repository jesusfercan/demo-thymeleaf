package com.example.demothymeleaf.controller;

import com.example.demothymeleaf.dto.AssociateDto;
import com.example.demothymeleaf.service.AssociateService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/associate")
public class AssociateController {

    private static final String ASSOCIATE_LIST_VIEW = "associates";
    private static final String ASSOCIATE_FORM_VIEW = "associate";

    @Autowired
    private AssociateService associateService;

    @GetMapping
    public String listAllAssociates(Model model){

        model.addAttribute("associateList",associateService.getAllAssociates());
        model.addAttribute("importDataTables",true);


        return ASSOCIATE_LIST_VIEW;
    }

    @GetMapping("/{associateId}")
    public String getAssociate(@PathVariable Long associateId, Model model){

        model.addAttribute("associate",associateService.getAssociateById(associateId));

        return ASSOCIATE_FORM_VIEW;
    }

    @GetMapping("/create")
    public ModelAndView createAssociate(){
        return new ModelAndView("associate", "associate", new AssociateDto());
    }
    @PostMapping//(consumes = { "multipart/form-data" })
    public String createAssociate(@RequestParam MultipartFile image,
                                @Valid @ModelAttribute(name = "associate") AssociateDto associateDto,
                                BindingResult bindingResult,
                                Model model){

        if(!bindingResult.hasErrors()) {
            return "redirect:/associate/"+ associateService.createAssociate(associateDto).getId()
                    + "?created";
        }

        return ASSOCIATE_FORM_VIEW;

    }

    @PostMapping("/{associateId}")
    public String updateAssociate(@PathVariable Long associateId,
                                  @RequestParam MultipartFile image,
                                  @Valid @ModelAttribute(name = "associate") AssociateDto associateDto,
                                  BindingResult bindingResult,
                                  Model model){

        if(!bindingResult.hasErrors()) {
            associateService.updateAssociate(associateId, associateDto);
            return "redirect:/associate/"+associateId+"?updated";
        }

        return ASSOCIATE_FORM_VIEW;
    }

    @DeleteMapping("/{associateId}")
    public ResponseEntity<String> deleteAssociate(@PathVariable Long associateId){

        String deleteMessageInfo = "Eliminado";
        HttpStatus status = HttpStatus.OK;
        try {
            associateService.deleteAssociate(associateId);
        }catch (Exception e){
            deleteMessageInfo = "Error: " + e.getLocalizedMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status).body(deleteMessageInfo);
    }


    /*
    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest req, Exception ex) {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("error");

        mav.addObject("errorMessage", ex.getLocalizedMessage());
        mav.addObject("errorCode", "500");
        mav.addObject("url", "/");

        return mav;
    }
    */

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError2(HttpServletRequest req, Exception ex) {

        ModelAndView mv = new ModelAndView("error");

        mv.addObject("errorMessage", ex.getLocalizedMessage());
        if(ex instanceof ResponseStatusException){
            HttpStatusCode statusCode = ((ResponseStatusException)ex).getStatusCode();
            mv.setStatus(statusCode);

            mv.addObject("errorCode", statusCode.value());
            mv.addObject("url", "/associate");

        }else {
            mv.addObject("errorCode", "500");
            mv.addObject("url", "/");
        }

        return mv;
    }
}
