package com.dreamtown.onasistownhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import com.dreamtown.onasistownhouse.entity.Property;
import com.dreamtown.onasistownhouse.entity.PropertyDetails;
import com.dreamtown.onasistownhouse.entity.Website;
import com.dreamtown.onasistownhouse.repository.WebsiteRepository;
import com.dreamtown.onasistownhouse.service.PropertyDetailsService;
import com.dreamtown.onasistownhouse.service.PropertyService;
import com.dreamtown.onasistownhouse.service.VideoStreamService;
import com.dreamtown.onasistownhouse.service.WebsiteService;

import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class MainController {

    @Autowired
    private Environment env;

    @Autowired
    private VideoStreamService videoStreamService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private PropertyDetailsService propertyDetailsService;

    @Autowired
    private WebsiteRepository websiteRepository;

    @Autowired
    private WebsiteService websiteService;

    @GetMapping(value = "/")
    public String index(Model model) {
        Website web = websiteRepository.findAll().get(0);
        model.addAttribute("website", web);
        model.addAttribute("websiteName", websiteService.websiteName());
        String namafile = web.getWebsiteVideo().split("\\.")[0];
        model.addAttribute("websiteVideo", "/stream/mp4/" + namafile);
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

    @RequestMapping(value = "/listProperty", method = RequestMethod.GET)
    public ResponseEntity<Map> listProperty(@RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size, @RequestParam("wilayah") Optional<Integer> wilayah) {
        Map res = new HashMap<>();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(8);
        Page<Property> propertyPage = propertyService.findPaginated(PageRequest.of(currentPage - 1, pageSize),
                wilayah.orElse(null));
        res.put("propertyPage", propertyPage);
        int totalPages = propertyPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            res.put("pageNumbers", pageNumbers);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @RequestMapping(value = "/p/{namaProperty}/{tipeProperty}", method = RequestMethod.GET)
    public String detailsProperty(Model model, @PathVariable Optional<String> namaProperty,
            @PathVariable Optional<String> tipeProperty) {
        Property property = propertyService.getPropertyByName(namaProperty.get());
        PropertyDetails propertyDetails = propertyDetailsService.getPropertyDetails(property.getIdProperty(),
                tipeProperty.get());
        model.addAttribute("property", propertyDetails);
        model.addAttribute("websiteName", websiteService.websiteName());
        String[] splitDeskripsi = propertyDetails.getDeskripsi().split("\n");
        String[] deskripsiArr1 = Arrays.copyOfRange(splitDeskripsi, 0, splitDeskripsi.length / 2);
        String[] deskripsiArr2 = Arrays.copyOfRange(splitDeskripsi, splitDeskripsi.length / 2, splitDeskripsi.length);
        model.addAttribute("deskripsiArr1", deskripsiArr1);
        model.addAttribute("deskripsiArr2", deskripsiArr2);
        model.addAttribute("namaProperty", namaProperty.get());
        if (propertyDetails.getListPhoto().size() > 5) {
            model.addAttribute("sizePhotoLainnya", "+" + (propertyDetails.getListPhoto().size() - 5) + " Lainnya");
        }
        model.addAttribute("lineSeparator", System.lineSeparator());
        return "propertyDetails";
    }

    @RequestMapping(value = "/p/{namaProperty}/details", method = RequestMethod.GET)
    public String detailsProperty(Model model, @PathVariable Optional<String> namaProperty) {
        Property property = propertyService.getPropertyByName(namaProperty.get());
        Website web = websiteRepository.findAll().get(0);
        String[] universitas = property.getUniversitas() == null ? null
                : property.getUniversitas().contains("\n") ? property.getUniversitas().split("\n") : (property.getUniversitas() + System.lineSeparator()).split("\n");
        model.addAttribute("universitas", universitas);
        String[] sekolah = property.getSekolah() == null ? null : property.getSekolah().contains("\n") ? property.getSekolah().split("\n") : (property.getSekolah() + System.lineSeparator()).split("\n");
        model.addAttribute("sekolah", sekolah);
        String[] belanja = property.getBelanja() == null ? null : property.getBelanja().contains("\n") ? property.getBelanja().split("\n") : (property.getBelanja() + System.lineSeparator()).split("\n");
        model.addAttribute("belanja", belanja);
        String[] kuliner = property.getKuliner() == null ? null : property.getKuliner().contains("\n") ? property.getKuliner().split("\n") : (property.getKuliner() + System.lineSeparator()).split("\n");
        model.addAttribute("kuliner", kuliner);
        String[] rumahSakit = property.getRumahSakit() == null ? null : property.getRumahSakit().contains("\n") ? property.getRumahSakit().split("\n") : (property.getRumahSakit() + System.lineSeparator()).split("\n");
        model.addAttribute("rumahSakit", rumahSakit);
        String[] lainnya = property.getLainnya() == null ? null : property.getLainnya().contains("\n") ? property.getLainnya().split("\n") : (property.getLainnya() + System.lineSeparator()).split("\n");
        model.addAttribute("lainnya", lainnya);
        model.addAttribute("property", property);
        model.addAttribute("website", web);
        model.addAttribute("websiteName", websiteService.websiteName());
        return "listDetailsProperty";
    }

    @RequestMapping(value = "/listRekomendasi")
    public ResponseEntity<Map> listRekomendasi(@RequestParam(name = "page", defaultValue = "1") Optional<Integer> page,
            @RequestParam(name = "size", defaultValue = "4") Optional<Integer> size) {
        Map res = new HashMap<>();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);
        Page<Property> propertyPage = propertyService.listRekomendasi(PageRequest.of(currentPage - 1, pageSize));

        res.put("propertyPage", propertyPage);
        int totalPages = propertyPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            res.put("pageNumbers", pageNumbers);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @RequestMapping(value = "/tentangkami", method = RequestMethod.GET)
    public String tentangkamiView(Model model) {
        Website web = websiteRepository.findAll().get(0);
        model.addAttribute("website", web);
        model.addAttribute("websiteName", websiteService.websiteName());
        return "tentangkami";
    }
}
