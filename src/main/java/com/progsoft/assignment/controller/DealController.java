package com.progsoft.assignment.controller;

import com.progsoft.assignment.exceptions.MissingDataException;
import com.progsoft.assignment.model.Deal;
import com.progsoft.assignment.service.FileProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class DealController {

    private final FileProcessingService fileProcessingService;

    @Autowired
    public DealController(final FileProcessingService fileProcessingService) {
        this.fileProcessingService = fileProcessingService;
    }

    @RequestMapping(value = "/fetch")
    public String listEmployee(Model model) throws IOException {
        List<String> fileNames = fileProcessingService.fetchAllFileNames();
        model.addAttribute("fileNames", fileNames);
        return "index";
    }

    @RequestMapping(value = "/fetch/{fileName}")
    public List<Deal> allDealsByFileName(Model model, @PathVariable final String fileName) throws IOException {
        List<Deal> deals = fileProcessingService.fetchAllByFileName(fileName);
        return deals;
    }

    @PostMapping("/upload")
    public Object handleFileUpload(Model model,final HttpServletRequest httpServletRequest) throws IOException{
        boolean isMultipart = ServletFileUpload.isMultipartContent(httpServletRequest);
        boolean isUploaded = false;
        Map response = null;
        if (isMultipart) {
            try {
                ServletFileUpload upload = new ServletFileUpload();
                FileItemIterator itemStream = upload.getItemIterator(httpServletRequest);
                while (itemStream.hasNext()) {
                    FileItemStream item = itemStream.next();
                    InputStream inputStream = item.openStream();
                    if (!item.isFormField() && "text/csv".equals(item.getContentType())) {
                        response = fileProcessingService.process(inputStream, item.getName());
                        isUploaded = true;
                    }
                }
            } catch (Exception e) {
                throw new MissingDataException(e.getMessage());
            }

        } else {
            log.info("File not found.");
            throw new MissingDataException("File not found.");
        }
        if (isUploaded) {
            model.addAllAttributes(response);
            return listEmployee(model);
           // return ResponseEntity.ok(response);
        } else {
            throw new MissingDataException("Please upload file in CSV format.");
        }

    }
}
