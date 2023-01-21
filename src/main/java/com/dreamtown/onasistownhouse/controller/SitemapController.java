package com.dreamtown.onasistownhouse.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dreamtown.onasistownhouse.utils.XmlUrl;
import com.dreamtown.onasistownhouse.utils.XmlUrlSet;

@Controller
public class SitemapController {
    private List<String> URLS = List.of("/");
    private String DOMAIN = "https://onasistownhouse.com";

    @GetMapping(value = "/sitemap.xml")
    @ResponseBody
    public XmlUrlSet main() {
        XmlUrlSet xmlUrlSet = new XmlUrlSet();
        for (String eachLink : URLS) {
            create(xmlUrlSet, eachLink, XmlUrl.Priority.HIGH);
        }
        return xmlUrlSet;
    }

    private void create(XmlUrlSet xmlUrlSet, String link, XmlUrl.Priority priority) {
        xmlUrlSet.addUrl(new XmlUrl(DOMAIN + link, priority));
    }
}
