package com.dreamtown.onasistownhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dreamtown.onasistownhouse.service.VideoStreamService;
import com.dreamtown.onasistownhouse.utils.FileManager;

import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class MainController {

    @Autowired
    private Environment env;

    @Autowired
    private FileManager fileManager;

    @Autowired
    private VideoStreamService videoStreamService;

    @GetMapping(value = "/")
    public String index(Model model) {
        return "index";
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<Map> upload(@RequestParam("file") MultipartFile multipartFile) {
        Map data = new HashMap<>();
        try {
            File fileTemp = new File(env.getProperty("storage") + multipartFile.getOriginalFilename());
            multipartFile.transferTo(fileTemp);
            URLConnection connection = fileTemp.toURL().openConnection();
            String mimeType = connection.getContentType();
            data.put("message", "sukses upload file");
            data.put("mimeType", mimeType);
        } catch (IllegalStateException | IOException e) {
            System.out.println(e.getMessage());
            data.put("message", "gagal upload file");
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(value = "/loadfile/{filename}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<InputStreamResource> loadfile(@PathVariable(required = true) String filename)
            throws MalformedURLException, IOException {
        String splitFileName[] = filename.split("\\.");
        String extension = splitFileName[splitFileName.length - 1];
        File fileTemp = null;
        if (extension.equalsIgnoreCase("mp4")) {
            fileTemp = new File(env.getProperty("storage.videos") + filename);
        } else if (extensionJpeg().contains(extension)) {
            fileTemp = new File(env.getProperty("storage.images") + filename);
        }
        URLConnection connection = fileTemp.toURL().openConnection();
        String mimeType = connection.getContentType();
        InputStream is = new FileInputStream(fileTemp);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .body(new InputStreamResource(is));
    }

    @RequestMapping(value = "/stream/{fileType}/{fileName}", method = RequestMethod.GET)
    public Mono<ResponseEntity<byte[]>> streamVideo(
            @RequestHeader(value = "Range", required = false) String httpRangeList,
            @PathVariable("fileType") String fileType,
            // @PathVariable("path") String path,
            @PathVariable("fileName") String fileName) {
        return Mono.just(videoStreamService.prepareContent(fileName, fileType, httpRangeList));
    }

    private List<String> extensionJpeg() {
        List<String> ext = new ArrayList<>();
        ext.add("jpg");
        ext.add("jpeg");
        ext.add("png");
        return ext;
    }
}
