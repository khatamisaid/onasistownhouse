package com.dreamtown.onasistownhouse.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamtown.onasistownhouse.entity.Website;
import com.dreamtown.onasistownhouse.repository.WebsiteRepository;

@Service
public class WebsiteService {

    @Autowired
    private WebsiteRepository websiteRepository;

    public String websiteName() {
        Website web = websiteRepository.findAll().get(0);
        String[] splitNamaWeb = web.getWebsiteName().split(" ");
        if (splitNamaWeb.length > 1) {
            String temp = "";
            for (int i = 1; i < splitNamaWeb.length; i++) {
                temp += " " + splitNamaWeb[i];
            }
            String span = "<span style=\"color: black;\">" + temp + "</span>";
            return "<h2>" + splitNamaWeb[0] + span + "<h2>";
        }
        return "<h2>" + web.getWebsiteName() + "<h2>";
    }

    public Website website() {
        return websiteRepository.findAll().get(0);
    }

    public String websiteNameAdmin() {
        Website web = websiteRepository.findAll().get(0);
        String[] splitNamaWeb = web.getWebsiteName().split(" ");
        if (splitNamaWeb.length > 1) {
            String temp = "";
            for (int i = 1; i < splitNamaWeb.length; i++) {
                temp += " " + splitNamaWeb[i];
            }
            String span = "<span>" + temp + "</span>";
            return "<h2>" + splitNamaWeb[0] + span + "<h2>";
        }
        return "<h2>" + web.getWebsiteName() + "<h2>";
    }
}
