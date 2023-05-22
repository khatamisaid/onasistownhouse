package com.dreamtown.onasistownhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.dreamtown.onasistownhouse.entity.ContactPerson;
import com.dreamtown.onasistownhouse.entity.LogAktivitas;
import com.dreamtown.onasistownhouse.entity.LogWhatsApp;
import com.dreamtown.onasistownhouse.entity.Property;
import com.dreamtown.onasistownhouse.entity.PropertyDetails;
import com.dreamtown.onasistownhouse.entity.Website;
import com.dreamtown.onasistownhouse.repository.ContactPersonRepository;
import com.dreamtown.onasistownhouse.repository.LogAktivitasRepository;
import com.dreamtown.onasistownhouse.repository.LogWhatsAppRepository;
import com.dreamtown.onasistownhouse.repository.WebsiteRepository;
import com.dreamtown.onasistownhouse.service.PropertyDetailsService;
import com.dreamtown.onasistownhouse.service.PropertyService;
import com.dreamtown.onasistownhouse.service.VideoStreamService;
import com.dreamtown.onasistownhouse.service.WebsiteService;
import com.dreamtown.onasistownhouse.utils.Utils;

import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpSession;

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

    @Autowired
    private ContactPersonRepository contactPersonRepository;

    @Autowired
    private Utils utils;

    @Autowired
    private HttpSession session;

    @Autowired
    private LogAktivitasRepository logAktivitasRepository;

    @Autowired
    private LogWhatsAppRepository logWhatsAppRepository;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @GetMapping(value = "/")
    public String index(Model model) {
        Website web = websiteRepository.findAll().get(0);
        model.addAttribute("website", web);
        model.addAttribute("websiteName", websiteService.websiteName());
        String namafile = web.getWebsiteVideo().split("\\.")[0];
        model.addAttribute("websiteVideo", "/stream/mp4/" + namafile);
        model.addAttribute("contactPerson", contactPersonRepository.findAll().size() > 0);
        if (activeProfile.equalsIgnoreCase("production")) {
            logAktivitasRepository.save(new LogAktivitas(null, "Beranda", "/"));
        }
        return "index";
    }

    @GetMapping("/getWhatsApp")
    public ResponseEntity<Map> getWhatsApp() {
        Map data = new HashMap<>();
        List<ContactPerson> listContactPerson = contactPersonRepository.findAll();
        if (listContactPerson.size() > 0) {
            ContactPerson cp = listContactPerson.get(utils.getRandomIndex(listContactPerson.size()));
            data.put("res", "https://wa.me/+62" + cp.getNomorTelpon());
            if (activeProfile.equalsIgnoreCase("production")) {
                logWhatsAppRepository.save(new LogWhatsApp(null, cp.getNomorTelpon(),
                        cp.getNamaContact()));
            }
            return new ResponseEntity<>(data, HttpStatus.OK);
        }
        data.put("res", "Kontak WhatsApp Belum Tersedia");
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
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
            data.put("message", "gagal upload file");
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(value = "/loadfile/{filename}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<InputStreamResource> loadfile(@PathVariable(required = true) String filename)
            throws MalformedURLException, IOException {
        // String splitFileName[] = filename.split("\\.");
        // String extension = splitFileName[splitFileName.length - 1];
        File fileTemp = new File(env.getProperty("storage.images") + filename);
        if (!fileTemp.exists()) {
            File file = new File(env.getProperty("storage.images") + "imagenotfound.png");
            URLConnection connection = file.toURL().openConnection();
            String mimeType = connection.getContentType();
            InputStream is = new FileInputStream(file);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .body(new InputStreamResource(is));
        }
        // if (extension.equalsIgnoreCase("mp4")) {
        // fileTemp = new File(env.getProperty("storage.videos") + filename);
        // } else if (extensionJpeg().contains(extension)) {
        // fileTemp =
        // }
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
            @RequestParam("size") Optional<Integer> size, @RequestParam("area") Optional<Integer> area) {
        Map res = new HashMap<>();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(8);
        Page<Property> propertyPage = propertyService.findPaginated(PageRequest.of(currentPage - 1, pageSize),
                area.orElse(null));
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
        Optional<PropertyDetails> propertyDetails = propertyDetailsService.getPropertyDetails(property.getIdProperty(),
                tipeProperty.get());
        if (!propertyDetails.isPresent()) {
            return "redirect:/";
        }
        Website web = websiteRepository.findAll().get(0);
        model.addAttribute("website", web);
        model.addAttribute("property", propertyDetails.get());
        model.addAttribute("websiteName", websiteService.websiteName());
        String[] splitDeskripsi = null;
        String[] deskripsiArr1 = null;
        String[] deskripsiArr2 = null;
        try {
            splitDeskripsi = propertyDetails.get().getDeskripsi().split("\n");
            deskripsiArr1 = Arrays.copyOfRange(splitDeskripsi, 0, splitDeskripsi.length / 2);
            deskripsiArr2 = Arrays.copyOfRange(splitDeskripsi, splitDeskripsi.length / 2,
                    splitDeskripsi.length);
        } catch (NullPointerException e) {
            splitDeskripsi = new String[] {};
            deskripsiArr1 = new String[] {};
            deskripsiArr2 = new String[] {};
        }
        model.addAttribute("deskripsiArr1", deskripsiArr1);
        model.addAttribute("deskripsiArr2", deskripsiArr2);
        model.addAttribute("namaProperty", namaProperty.get());
        if (propertyDetails.get().getListPhoto().size() > 5) {
            model.addAttribute("sizePhotoLainnya",
                    "+" + (propertyDetails.get().getListPhoto().size() - 5) + " Lainnya");
        }
        model.addAttribute("contactPerson", contactPersonRepository.findAll().size() > 0);
        model.addAttribute("lineSeparator", System.lineSeparator());
        if (activeProfile.equalsIgnoreCase("production")) {
            logAktivitasRepository.save(
                    new LogAktivitas(null, namaProperty.get().trim() + " tipe " +
                            tipeProperty.get(),
                            "/p/" + namaProperty.get().trim() + "/" + tipeProperty.get()));
        }
        return "propertyDetails";
    }

    @RequestMapping(value = "/p/{namaProperty}/details", method = RequestMethod.GET)
    public String detailsProperty(Model model, @PathVariable Optional<String> namaProperty) {
        Property property = propertyService.getPropertyByName(namaProperty.get());
        Website web = websiteRepository.findAll().get(0);
        model.addAttribute("universitas", utils.checkNullOrEmptyString(property.getUniversitas()));
        model.addAttribute("sekolah", utils.checkNullOrEmptyString(property.getSekolah()));
        model.addAttribute("belanja", utils.checkNullOrEmptyString(property.getBelanja()));
        model.addAttribute("transportasi", utils.checkNullOrEmptyString(property.getTransportasi()));
        model.addAttribute("rumahSakit", utils.checkNullOrEmptyString(property.getRumahSakit()));
        model.addAttribute("lainnya", utils.checkNullOrEmptyString(property.getLainnya()));
        model.addAttribute("property", property);
        model.addAttribute("listPropertyDetails", propertyDetailsService
                .findAllByIdPropertyAndPropertyStatusIsNotOrderByTipePropertyAsc(property.getIdProperty()));
        model.addAttribute("website", web);
        model.addAttribute("websiteName", websiteService.websiteName());
        if (activeProfile.equalsIgnoreCase("production")) {
            logAktivitasRepository.save(
                    new LogAktivitas(null, namaProperty.get().trim(), "/p/" +
                            namaProperty.get().trim() + "/details"));
        }
        model.addAttribute("contactPerson", contactPersonRepository.findAll().size() > 0);
        
        return "listDetailsProperty";
    }

    @RequestMapping(value = "/listRekomendasi")
    public ResponseEntity<Map> listRekomendasi(@RequestParam(name = "page", defaultValue = "1") Optional<Integer> page,
            @RequestParam(name = "size", defaultValue = "4") Optional<Integer> size,
            @RequestParam(name = "area", defaultValue = "") Optional<String> area,
            @RequestParam(name = "sortBy", defaultValue = "1") Optional<String> sortBy) {
        Map res = new HashMap<>();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);
        Page<Property> propertyPage = propertyService.listRekomendasi(PageRequest.of(currentPage - 1, pageSize),
                session.getAttribute("role") != null, area.get(), sortBy.get());
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

    @RequestMapping(value = "/cobalogwhatsapp")
    public ResponseEntity<Map> cobalogwhatsapp() {
        Map res = new HashMap<>();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String nextDate = new SimpleDateFormat("yyyy-MM-dd").format(utils.addDays(new Date(), 1));
        List<Object[]> list = logWhatsAppRepository.logWhatsApp(currentDate.replace("-", ""),
                nextDate.replace("-", ""));
        res.put("data", list);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
