package com.example.linux.pdfPublisheru.controller;

import com.example.linux.pdfPublisheru.exception.StorageFileNotFoundException;
import com.example.linux.pdfPublisheru.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URL;
import java.time.Clock;
import java.time.Instant;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/fileStorage")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                "serveFile",
                                generateFileName(path.getFileName().toString())).build().toUri().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    //TODO do wywalenia
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
//    @PostMapping("/file")
//    @ResponseBody
//    public ResponseEntity<Resource> downloadFile(Model model) {
//        model.addAttribute("files", storageService.loadAll().map(
//                        path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
//                                "serveFile",
//                                generateFileName(path.getFileName().toString())).build().toUri().toString())
//                .collect(Collectors.toList()));
//
////        Resource file = storageService.loadAsResource(filename);
//        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
//                "attachment; filename=\"" + ).getFilename() + "\"").body(file);
//    }

    @PostMapping("/file")
    public ResponseEntity<byte[]> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                   RedirectAttributes redirectAttributes) throws IOException {

        storageService.store(file); // do wywalenia?
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

//        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
//                "attachment; filename=\"" + file.getName() + "\"").body(file);
        // Locate file in the cache memory TODO Czy na pewno potrzebuje memory
        // Send file to the atlassian confluence page
        try {
            URL url = new URL("http://"+ ${atlassian.confluence.hostname}+ "localhost:8090/rest/api/content/65603/child/attachment");
        } catch (IOException e) {

        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getOriginalFilename() + "\"")
                .body(file.getBytes());
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    private String generateFileName(String fileName) {
        Instant currentTimeStamp = Clock.systemUTC().instant();
        return fileName+=currentTimeStamp;
    }
}
