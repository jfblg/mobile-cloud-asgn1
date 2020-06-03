package org.magnum.dataup;

import org.magnum.dataup.model.MyVideo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestRunner {
    public static void main(String[] args) {
        MyVideo mv1 = new MyVideo();
        mv1.setTitle("Movie 1");
        mv1.setId(100);

        MyVideo mv2 = new MyVideo();
        mv1.setTitle("Movie 2");
        mv1.setId(200);

        List<MyVideo> myVideos = new ArrayList<>();
        myVideos.add(mv1);
        myVideos.add(mv2);

//        System.out.println(myVideos);
        System.out.println(UUID.randomUUID().toString());
    }
}
