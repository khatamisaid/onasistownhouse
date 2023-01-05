package com.dreamtown.onasistownhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dreamtown.onasistownhouse.service.VideoStreamService;

import reactor.core.publisher.Mono;

public class VideoStreamController {
    @Autowired
    private VideoStreamService videoStreamService;

    @RequestMapping(value = "/stream/{fileType}/{fileName}", method = RequestMethod.GET)
    public Mono<ResponseEntity<byte[]>> streamVideo(
            @RequestHeader(value = "Range", required = false) String httpRangeList,
            @PathVariable("fileType") String fileType,
            // @PathVariable("path") String path,
            @PathVariable("fileName") String fileName) {
        return Mono.just(videoStreamService.prepareContent(fileName, fileType, httpRangeList));
    }
}
