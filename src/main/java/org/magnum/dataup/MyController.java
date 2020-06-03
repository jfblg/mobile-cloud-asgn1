package org.magnum.dataup;

import org.magnum.dataup.model.MyVideo;
import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class MyController {

    private List<Video> videoList;

    @Autowired
    VideoFileManager videoFileManager;

    @PostConstruct
    public void init() {
        videoList = new ArrayList<>();
    }

    @RequestMapping(value = "/test")
    @ResponseBody
    public String getTest() {
        return "Hello World!";
    }

    @GetMapping(value = "/test/{videoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MyVideo getVideoById(@PathVariable Integer videoId) {
        MyVideo myVideo = new MyVideo();
        myVideo.setId(videoId);
        myVideo.setTitle("DeadAlive");
        return myVideo;
    }

    @RequestMapping(value = "/video", method = RequestMethod.GET)
    public @ResponseBody List<Video> getVideo() {
        return videoList;
    }

    @RequestMapping(
            value = "/video",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public @ResponseBody Video postVideo(@RequestBody Video video, HttpServletRequest request) {
        long uuidLong = Math.abs(ThreadLocalRandom.current().nextLong()+1);
        String dataUrl = request.getRequestURL().toString() + "/" + uuidLong + "/data";
        video.setId(uuidLong);
        video.setDataUrl(dataUrl);
        videoList.add(video);
        return video;
    }

    @RequestMapping(value = "/video/{id}/data", method = RequestMethod.POST)
    @ResponseBody
    public VideoStatus postVideoIdData(
            @PathVariable long id,
            @RequestParam("data") MultipartFile file,
            HttpServletResponse res
    ) {
        try {
            InputStream inputStream = file.getInputStream();
            for (Video video : videoList)
                if (video.getId() == id) {
                    videoFileManager.saveVideoData(video, inputStream);
                    return new VideoStatus(VideoStatus.VideoState.READY);
                }
        } catch (IOException e) {
            System.out.println("IOException when saving multipart file");
        }
        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return null;
    }

    @RequestMapping(value = "/video/{id}/data", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getVideoIdData(@PathVariable long id, HttpServletResponse res) throws IOException {
        OutputStream outputStream = res.getOutputStream();
        HttpHeaders headers = new HttpHeaders();
        for (Video video : videoList) {
            if (video.getId() == id && videoFileManager.hasVideoData(video)) {
                videoFileManager.copyVideoData(video, outputStream);
                return new ResponseEntity(headers, HttpStatus.OK);
            }
        }
        return new ResponseEntity(headers, HttpStatus.NOT_FOUND);
    }
}
