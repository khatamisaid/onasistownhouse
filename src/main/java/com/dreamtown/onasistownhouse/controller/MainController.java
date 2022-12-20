package com.dreamtown.onasistownhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dreamtown.onasistownhouse.utils.FileManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class MainController {

    @Autowired
    private Environment env;

    @Autowired
    private FileManager fileManager;

    @GetMapping(value = "/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping(value = "/ftp")
    public ResponseEntity<Map> ftp(Model model) {
        Map data = new HashMap<>();
        data.put("isConnected", fileManager.connected());
        return new ResponseEntity<>(data, HttpStatus.OK);
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
    public ResponseEntity<InputStreamResource> loadfile(@PathVariable String filename) throws MalformedURLException, IOException {
        File fileTemp = new File(env.getProperty("storage") + filename);
        URLConnection connection = fileTemp.toURL().openConnection();
        String mimeType = connection.getContentType();
        InputStream is = new FileInputStream(fileTemp);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .body(new InputStreamResource(is));
    }
}
