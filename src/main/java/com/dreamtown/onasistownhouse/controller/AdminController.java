package com.dreamtown.onasistownhouse.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.dreamtown.onasistownhouse.entity.ContactPerson;
import com.dreamtown.onasistownhouse.entity.MWilayah;
import com.dreamtown.onasistownhouse.entity.Photo;
import com.dreamtown.onasistownhouse.entity.Property;
import com.dreamtown.onasistownhouse.entity.PropertyDetails;
import com.dreamtown.onasistownhouse.entity.Video;
import com.dreamtown.onasistownhouse.entity.Website;
import com.dreamtown.onasistownhouse.entity.WebsitePhoto;
import com.dreamtown.onasistownhouse.repository.ContactPersonRepository;
import com.dreamtown.onasistownhouse.repository.MWilayahRepository;
import com.dreamtown.onasistownhouse.repository.PhotoRepository;
import com.dreamtown.onasistownhouse.repository.PropertyDetailsRepository;
import com.dreamtown.onasistownhouse.repository.PropertyRepository;
import com.dreamtown.onasistownhouse.repository.PropertyStatusRepository;
import com.dreamtown.onasistownhouse.repository.UserRepository;
import com.dreamtown.onasistownhouse.repository.VideoRepository;
import com.dreamtown.onasistownhouse.repository.WebsitePhotoRepository;
import com.dreamtown.onasistownhouse.repository.WebsiteRepository;
import com.dreamtown.onasistownhouse.service.WebsiteService;
import com.dreamtown.onasistownhouse.utils.Menu;
import com.dreamtown.onasistownhouse.utils.TipeProperty;
import com.dreamtown.onasistownhouse.utils.UUIDGenerator;
import com.dreamtown.onasistownhouse.viewmodel.ViewModelTambahKontak;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private Menu menu;

    @Autowired
    private TipeProperty tipeProperty;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PropertyDetailsRepository propertyDetailsRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private PropertyStatusRepository propertyStatusRepository;

    @Autowired
    private MWilayahRepository mWilayahRepository;

    @Autowired
    private WebsiteRepository websiteRepository;

    @Autowired
    private WebsiteService websiteService;

    @Autowired
    private WebsitePhotoRepository websitePhotoRepository;

    @Autowired
    private ContactPersonRepository contactPersonRepository;

    @Autowired
    private Environment env;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("website", websiteRepository.findAll().get(0));
        model.addAttribute("username", httpSession.getAttribute("username"));
        model.addAttribute("menus", menu.getListProperty());
        model.addAttribute("listWilayah", mWilayahRepository.findAll());
        model.addAttribute("websiteName", websiteService.websiteNameAdmin());
        return "admin/index";
    }

    @RequestMapping(value = "/managementUser", method = RequestMethod.GET)
    public String tambahUser(Model model) {
        model.addAttribute("username", httpSession.getAttribute("username"));
        model.addAttribute("menus", menu.getListProperty());
        model.addAttribute("listWilayah", mWilayahRepository.findAll());
        model.addAttribute("websiteName", websiteService.websiteNameAdmin());
        return "admin/managementUser";
    }

    @RequestMapping(value = "/p/{propertyName}", method = RequestMethod.GET)
    public String findPropertyWithId(Model model, @PathVariable String propertyName) {
        Property property = propertyRepository.findOneByPropertyName(propertyName).get();
        model.addAttribute("website", websiteRepository.findAll().get(0));
        model.addAttribute("username", httpSession.getAttribute("username"));
        model.addAttribute("menus", menu.getListProperty());
        model.addAttribute("listWilayah", mWilayahRepository.findAll());
        model.addAttribute("property", property);
        model.addAttribute("websiteName", websiteService.websiteNameAdmin());
        return "admin/property";
    }

    @RequestMapping(value = "/p/{propertyName}/{id}", method = RequestMethod.GET)
    public String propertyDetails(Model model, @PathVariable String propertyName, @PathVariable Integer id) {
        PropertyDetails propertyDetails = propertyDetailsRepository.findById(id).get();
        Property property = propertyRepository.findOneByPropertyName(propertyName).get();
        model.addAttribute("propertyDetails", propertyDetails);
        model.addAttribute("propertyName", propertyName);
        model.addAttribute("property", property);
        model.addAttribute("propertyStatus", propertyStatusRepository.findAll());
        model.addAttribute("tipeProperty", tipeProperty.getListTipeProperty());
        model.addAttribute("menus", menu.getListProperty());
        model.addAttribute("listWilayah", mWilayahRepository.findAll());
        model.addAttribute("websiteName", websiteService.websiteNameAdmin());
        return "admin/detailsProperty";
    }

    @RequestMapping(value = "/tambahProperty", method = RequestMethod.GET)
    public String tambahProperty(Model model) {
        model.addAttribute("website", websiteRepository.findAll().get(0));
        model.addAttribute("username", httpSession.getAttribute("username"));
        model.addAttribute("menus", menu.getListProperty());
        model.addAttribute("listWilayah", mWilayahRepository.findAll());
        model.addAttribute("websiteName", websiteService.websiteNameAdmin());
        return "admin/tambahProperty";
    }

    @RequestMapping(value = "/p/{propertyName}/add", method = RequestMethod.GET)
    public String addPropertyDetails(Model model, @PathVariable String propertyName) {
        Property property = propertyRepository.findOneByPropertyName(propertyName).get();
        List<String> tpProperty = propertyDetailsRepository
                .findDistinctTipePropertyByIdProperty(property.getIdProperty());
        List<String> tTipeProperty = tipeProperty.getListTipeProperty();
        for (int i = 0; i < tpProperty.size(); i++) {
            for (int e = 0; e < tTipeProperty.size(); e++) {
                if (tpProperty.get(i).equalsIgnoreCase(tTipeProperty.get(e))) {
                    tTipeProperty.remove(e);
                }
            }
        }
        model.addAttribute("property", property);
        model.addAttribute("propertyDetails", new PropertyDetails());
        model.addAttribute("propertyStatus", propertyStatusRepository.findAll());
        model.addAttribute("tipeProperty", tTipeProperty);
        model.addAttribute("menus", menu.getListProperty());
        model.addAttribute("listWilayah", mWilayahRepository.findAll());
        model.addAttribute("websiteName", websiteService.websiteNameAdmin());
        return "admin/addDetailsProperty";
    }

    @RequestMapping(value = "/p/{propertyName}/edit", method = RequestMethod.GET)
    public String editProperty(Model model, @PathVariable String propertyName) {
        Property property = propertyRepository.findOneByPropertyName(propertyName).get();
        model.addAttribute("property", property);
        model.addAttribute("website", websiteRepository.findAll().get(0));
        model.addAttribute("username", httpSession.getAttribute("username"));
        model.addAttribute("menus", menu.getListProperty());
        model.addAttribute("listWilayah", mWilayahRepository.findAll());
        model.addAttribute("websiteName", websiteService.websiteNameAdmin());
        return "admin/editProperty";
    }

    @RequestMapping(value = "/property/{id}", method = RequestMethod.GET)
    public ResponseEntity<Property> getPropertyById(@PathVariable Integer id) {
        return new ResponseEntity<>(propertyRepository.findById(id).get(), HttpStatus.OK);
    }

    @RequestMapping(value = "/property", method = RequestMethod.PUT)
    public ResponseEntity<Map> putProperty(@RequestPart Property property,
            @RequestPart(required = false) MultipartFile file,
            @RequestPart(required = false) MultipartFile p1, @RequestPart(required = false) MultipartFile p2,
            @RequestPart(required = false) MultipartFile p3)
            throws IllegalStateException, IOException {
        Map response = new HashMap<>();
        Optional<Property> tempPropertyFindById = propertyRepository.findById(property.getIdProperty());
        if (file != null) {
            property.setPropertyName(property.getPropertyName().trim());
            String[] splitFileName = file.getOriginalFilename().split("\\.");
            String extension = splitFileName[splitFileName.length - 1];
            String fileName = UUIDGenerator.generateType4UUID().toString() + "." +
                    extension;
            File fileTemp = new File(env.getProperty("storage.images") + fileName);
            file.transferTo(fileTemp);
            property.setPropertyBanner(fileName);
        } else {
            property.setPropertyBanner(tempPropertyFindById.get().getPropertyBanner());
        }
        if (p1 != null) {
            String[] splitP1 = p1.getOriginalFilename().split("\\.");
            String extensionP1 = splitP1[splitP1.length - 1];
            String fileNameP1 = UUIDGenerator.generateType4UUID().toString() + "." +
                    extensionP1;
            File fileTempP1 = new File(env.getProperty("storage.images") + fileNameP1);
            p1.transferTo(fileTempP1);
            property.setP1(fileNameP1);
        } else {
            property.setP1(tempPropertyFindById.get().getP1());
        }
        if (p2 != null) {
            String[] splitP2 = p2.getOriginalFilename().split("\\.");
            String extensionP2 = splitP2[splitP2.length - 1];
            String fileNameP2 = UUIDGenerator.generateType4UUID().toString() + "." +
                    extensionP2;
            File fileTempP2 = new File(env.getProperty("storage.images") + fileNameP2);
            p2.transferTo(fileTempP2);
            property.setP2(fileNameP2);
        } else {
            property.setP2(tempPropertyFindById.get().getP2());
        }
        if (p3 != null) {
            String[] splitP3 = p3.getOriginalFilename().split("\\.");
            String extensionP3 = splitP3[splitP3.length - 1];
            String fileNameP3 = UUIDGenerator.generateType4UUID().toString() + "." +
                    extensionP3;
            File fileTempP3 = new File(env.getProperty("storage.images") + fileNameP3);
            p3.transferTo(fileTempP3);
            property.setP3(fileNameP3);
        } else {
            property.setP3(tempPropertyFindById.get().getP3());
        }
        property.setListPropertyDetails(tempPropertyFindById.get().getListPropertyDetails());
        propertyRepository.save(property);
        response.put("message", "Property Berhasil di edit");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/property", method = RequestMethod.POST)
    public ResponseEntity<Map> postProperty(@RequestPart Property property, @RequestPart MultipartFile file,
            @RequestPart(required = true) MultipartFile p1, @RequestPart(required = false) MultipartFile p2,
            @RequestPart(required = false) MultipartFile p3)
            throws IllegalStateException, IOException {
        Map response = new HashMap<>();
        if (propertyRepository.findOneByPropertyName(property.getPropertyName()).isPresent()) {
            response.put("message", "Nama property sudah ada");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        property.setPropertyName(property.getPropertyName().trim());
        String[] splitFileName = file.getOriginalFilename().split("\\.");
        String extension = splitFileName[splitFileName.length - 1];
        String fileName = UUIDGenerator.generateType4UUID().toString() + "." + extension;
        File fileTemp = new File(env.getProperty("storage.images") + fileName);
        file.transferTo(fileTemp);
        property.setPropertyBanner(fileName);
        if (p1.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        String[] splitP1 = p1.getOriginalFilename().split("\\.");
        String extensionP1 = splitP1[splitP1.length - 1];
        String fileNameP1 = UUIDGenerator.generateType4UUID().toString() + "." + extensionP1;
        File fileTempP1 = new File(env.getProperty("storage.images") + fileNameP1);
        p1.transferTo(fileTempP1);
        property.setP1(fileNameP1);
        try {
            String[] splitP2 = p2.getOriginalFilename().split("\\.");
            String extensionP2 = splitP2[splitP2.length - 1];
            String fileNameP2 = UUIDGenerator.generateType4UUID().toString() + "." + extensionP2;
            File fileTempP2 = new File(env.getProperty("storage.images") + fileNameP2);
            p2.transferTo(fileTempP2);
            property.setP2(fileNameP2);
        } catch (NullPointerException e) {
        }
        try {
            String[] splitP3 = p3.getOriginalFilename().split("\\.");
            String extensionP3 = splitP3[splitP3.length - 1];
            String fileNameP3 = UUIDGenerator.generateType4UUID().toString() + "." + extensionP3;
            File fileTempP3 = new File(env.getProperty("storage.images") + fileNameP3);
            p3.transferTo(fileTempP3);
            property.setP3(fileNameP3);
        } catch (NullPointerException e) {
        }
        propertyRepository.save(property);
        response.put("message", "Property Berhasil di tambahkan");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/property", method = RequestMethod.DELETE)
    public ResponseEntity<Map> postProperty(@RequestParam String namaProperty) {
        Map response = new HashMap<>();
        propertyRepository.deleteById(propertyRepository.findOneByPropertyName(namaProperty).get().getIdProperty());
        response.put("message", "Berhasil menghapus property");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteDetailsPropertyById/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Map> deleteDetailsPropertyById(@PathVariable Integer id) {
        Map response = new HashMap<>();
        propertyDetailsRepository.deleteById(id);
        response.put("message", "Berhasil menghapus detail property");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/postPropertyDetails", method = RequestMethod.POST)
    public ResponseEntity<Map> postPropertyDetails(@RequestBody PropertyDetails propertyDetails) {
        Map response = new HashMap<>();
        propertyDetailsRepository.save(propertyDetails);
        response.put("message", "Property Berhasil di tambahkan");
        response.put("propertyDetails", propertyDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/deletePhoto", method = RequestMethod.DELETE)
    public ResponseEntity<Map> deletePhoto(@RequestParam Integer idPhoto) {
        Map response = new HashMap<>();
        String namaFoto = photoRepository.findById(idPhoto).get().getNamaPhoto();
        File file = new File(env.getProperty("storage") + namaFoto);
        if (!file.exists()) {
            response.put("message", "file not exists");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        file.delete();
        photoRepository.deleteById(idPhoto);
        response.put("message", "Berhasil menghapus foto");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/postingFile", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE }, method = RequestMethod.POST)
    public ResponseEntity<Map> postProperty(@RequestParam Integer idDetailsProperty,
            @RequestParam(required = true) List<MultipartFile> imageFiles,
            @RequestParam(required = false) MultipartFile videoFile) {
        Map response = new HashMap<>();
        if (imageFiles.isEmpty()) {
            response.put("message", "Foto harus diisi");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (videoFile.isEmpty()) {
            response.put("message", "Video harus diisi");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        for (MultipartFile file : imageFiles) {
            String[] splitFileName = file.getOriginalFilename().split("\\.");
            String extension = splitFileName[splitFileName.length - 1];
            String fileName = UUIDGenerator.generateType4UUID().toString() + "." + extension;
            File fileTemp = new File(env.getProperty("storage") + fileName);
            try {
                file.transferTo(fileTemp);
                Photo photo = new Photo(null, fileName, idDetailsProperty);
                photoRepository.save(photo);
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        String[] splitFileName = videoFile.getOriginalFilename().split("\\.");
        String extension = splitFileName[splitFileName.length - 1];
        String videoFileName = UUIDGenerator.generateType4UUID().toString() + "." + extension;
        Video tempVideo = new Video();
        tempVideo.setIdDetailsProperty(idDetailsProperty);
        Example<Video> exampeVideo = Example.of(tempVideo);
        Optional<Video> video = videoRepository.findOne(exampeVideo);
        if (video.isPresent()) {
            videoRepository.save(new Video(video.get().getIdVideo(), videoFileName, idDetailsProperty));
            response.put("message", "Property Berhasil di tambahkan");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        videoRepository.save(new Video(null, videoFileName, idDetailsProperty));
        response.put("message", "Property Berhasil di tambahkan");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/simulasi_sistem_pembayaran", method = RequestMethod.GET)
    public String formulirPemesanan(Model model) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        model.addAttribute("tanggal", sdf.format(new Date()));
        model.addAttribute("menus", menu.getListProperty());
        model.addAttribute("listProperty", propertyRepository.findAll());
        model.addAttribute("websiteName", websiteService.websiteNameAdmin());
        return "admin/formulirPemesanan";
    }

    @RequestMapping(value = "/wilayah", method = RequestMethod.GET)
    public String wilayahView(Model model) {
        model.addAttribute("username", httpSession.getAttribute("username"));
        model.addAttribute("menus", menu.getListProperty());
        model.addAttribute("listWilayah", mWilayahRepository.findAll());
        model.addAttribute("websiteName", websiteService.websiteNameAdmin());
        return "admin/wilayah";
    }

    @RequestMapping(value = "/wilayah/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> hapusWilayah(@PathVariable Integer id) {
        if(propertyRepository.findByWilayah(new MWilayah(id, null)).size() > 0){
            return new ResponseEntity<>("Tidak dapat menghapus wilayah karena terdapat property di wilayah tersebut", HttpStatus.BAD_REQUEST);
        }
        mWilayahRepository.deleteById(id);
        return new ResponseEntity<>("Berhasil Menghapus Wilayah", HttpStatus.OK);
    }

    @RequestMapping(value = "/judul_website", method = RequestMethod.GET)
    public String judulWebsiteView(Model model) {
        model.addAttribute("username", httpSession.getAttribute("username"));
        model.addAttribute("menus", menu.getListProperty());
        model.addAttribute("listWilayah", mWilayahRepository.findAll());
        model.addAttribute("website", websiteService.website());
        model.addAttribute("websiteName", websiteService.websiteNameAdmin());
        return "admin/judulWebsite";
    }

    @RequestMapping(value = "/judul_website/{idWebsite}", method = RequestMethod.POST)
    public ResponseEntity<String> gantiJudulWebsite(@PathVariable Integer idWebsite, @RequestParam String namaWebsite) {
        Website web = websiteRepository.findById(idWebsite).get();
        web.setWebsiteName(namaWebsite);
        websiteRepository.save(web);
        return new ResponseEntity<>("Berhasil mengubah nama website", HttpStatus.OK);
    }

    @RequestMapping(value = "/photo_background", method = RequestMethod.GET)
    public String backgroundPhotoView(Model model) {
        model.addAttribute("username", httpSession.getAttribute("username"));
        model.addAttribute("menus", menu.getListProperty());
        model.addAttribute("listWilayah", mWilayahRepository.findAll());
        model.addAttribute("website", websiteService.website());
        model.addAttribute("websiteName", websiteService.websiteNameAdmin());
        return "admin/backgroundPhoto";
    }

    @RequestMapping(value = "/photo_background/{idWebsite}", method = RequestMethod.POST)
    public ResponseEntity<String> gantiPhotoBackground(@PathVariable Integer idWebsite,
            @RequestPart MultipartFile photo) throws IllegalStateException, IOException {
        WebsitePhoto webPhoto = websitePhotoRepository.findById(idWebsite).get();
        String[] splitFileName = photo.getOriginalFilename().split("\\.");
        String extension = splitFileName[splitFileName.length - 1];
        String fileName = UUIDGenerator.generateType4UUID().toString() + "." + extension;
        File fileTemp = new File(env.getProperty("storage.images") + fileName);
        webPhoto.setNamaPhoto(fileName);
        websitePhotoRepository.save(webPhoto);
        photo.transferTo(fileTemp);
        return new ResponseEntity<>("Berhasil mengubah photo background website", HttpStatus.OK);
    }

    @RequestMapping(value = "/video_animasi", method = RequestMethod.GET)
    public String videoAnimasiView(Model model) {
        model.addAttribute("username", httpSession.getAttribute("username"));
        model.addAttribute("menus", menu.getListProperty());
        model.addAttribute("listWilayah", mWilayahRepository.findAll());
        Website web = websiteService.website();
        model.addAttribute("website", web);
        String namafile = web.getWebsiteVideo().split("\\.")[0];
        model.addAttribute("websiteVideo", "/stream/mp4/" + namafile);
        model.addAttribute("websiteName", websiteService.websiteNameAdmin());
        return "admin/videoAnimasi";
    }

    @RequestMapping(value = "/video_animasi/{idWebsite}", method = RequestMethod.POST)
    public ResponseEntity<String> gantiAnimasiVideo(@PathVariable Integer idWebsite,
            @RequestPart MultipartFile video) throws IllegalStateException, IOException {
        Website website = websiteRepository.findById(idWebsite).get();
        String[] splitFileName = video.getOriginalFilename().split("\\.");
        String extension = splitFileName[splitFileName.length - 1];
        String fileName = UUIDGenerator.generateType4UUID().toString() + "." + extension;
        File fileTemp = new File(env.getProperty("storage.videos") + fileName);
        website.setWebsiteVideo(fileName);
        websiteRepository.save(website);
        video.transferTo(fileTemp);
        return new ResponseEntity<>("Berhasil mengubah video animasi", HttpStatus.OK);
    }

    @RequestMapping(value = "/tambah_kontak", method = RequestMethod.POST)
    public ResponseEntity<String> tambah_kontak(@RequestBody ViewModelTambahKontak vModelTambahKontak) {
        contactPersonRepository.save(new ContactPerson(vModelTambahKontak.getIdContactPerson(),
                vModelTambahKontak.getNomorTelpon(), vModelTambahKontak.getNamaContact()));
        return new ResponseEntity<>("Berhasil menambah kontak", HttpStatus.OK);
    }

    @RequestMapping(value = "/edit_kontak", method = RequestMethod.POST)
    public ResponseEntity<String> edit_kontak(@RequestBody ViewModelTambahKontak vModelTambahKontak) {
        contactPersonRepository.save(new ContactPerson(vModelTambahKontak.getIdContactPerson(),
                vModelTambahKontak.getNomorTelpon(), vModelTambahKontak.getNamaContact()));
        return new ResponseEntity<>("Berhasil merubah kontak", HttpStatus.OK);
    }

    @RequestMapping(value = "/delete_kontak/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete_kontak(@PathVariable Integer id) {
        contactPersonRepository.deleteById(id);
        return new ResponseEntity<>("Berhasil menghapus kontak", HttpStatus.OK);
    }

    @RequestMapping(value = "/get_kontak_whatsapp", method = RequestMethod.GET)
    public ResponseEntity<Map> get_kontak_whatsapp(@RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "5") Integer length) {
        Map data = new HashMap();
        Pageable pageable = PageRequest.of(start, length, Sort.by("createdAt").descending());
        Page<ContactPerson> pageCp = contactPersonRepository.findAll(pageable);
        data.put("data", pageCp);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(value = "/getContactById/{id}", method = RequestMethod.GET)
    public ResponseEntity<ContactPerson> get_kontak_whatsapp(@PathVariable Integer id) {
        return new ResponseEntity<>(contactPersonRepository.findById(id).get(), HttpStatus.OK);
    }

    @RequestMapping(value = "/kontak_whatsapp", method = RequestMethod.GET)
    public String kontakWhatsApp(Model model) {
        model.addAttribute("menus", menu.getListProperty());
        model.addAttribute("listProperty", propertyRepository.findAll());
        model.addAttribute("websiteName", websiteService.websiteNameAdmin());
        return "admin/kontakWhatsapp";
    }
}
