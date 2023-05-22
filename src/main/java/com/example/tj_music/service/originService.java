package com.example.tj_music.service;

import com.example.tj_music.db.entity.Image;
import com.example.tj_music.db.entity.Origin;
import com.example.tj_music.db.entity.OriginFrontEnd;
import com.example.tj_music.db.mapper.OriginMapper;

import com.example.tj_music.db.mapper.UserMapper;
import com.example.tj_music.utils.ImageUtils;
import com.example.tj_music.utils.MusicUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.tj_music.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
@Service
public class originService {
    @Autowired
    private OriginMapper originMapper;

    /**
     * search origin by key word.
     * code:0 represents origin does not exist.
     * code:1 represents search succeeded.
     * @param originTag
     * @return Result
     */
    public Result searchOriginByTag(String originTag) {
        List<Origin> originList =  originMapper.selectOriginByTag(originTag);
        if (originList.isEmpty()) {
            return Result.fail("origin does not exist.");
        }
        return Result.success(originList);
    }

    /**
     * search origin by work id.
     * code:0 represents origin does not exist.
     * code:1 represents search succeeded.
     * @param workId
     * @return Result
     */
    public Result searchOriginByWorkId(Integer workId) {
        Origin origin = originMapper.selectOriginByWorkId(workId);
        if (origin == null) {
            return Result.fail("origin does not exist.");
        }
        return Result.success(origin);
    }

    /**
     * get origin by origin id.
     * code:0 represents origin does not exist.
     * code:1 represents search succeeded.
     * @param originId
     * @return Result
     */
    public Result getOriginByOriginId(Integer originId) {
        Origin origin = originMapper.selectOriginByOriginId(originId);
        if (origin == null) {
            return Result.fail("origin does not exist.");
        }
        return Result.success(origin);
    }

    public void insertOrigin(OriginFrontEnd originFrontEnd) {
        // copy non-file attributes from originFrontEnd to origin
        Origin origin = new Origin();
        origin.setOriginName(originFrontEnd.getOriginName());
        origin.setOriginAuthor(originFrontEnd.getOriginAuthor());
        origin.setOriginDuration(10);
        origin.setOriginIntroduction(originFrontEnd.getOriginIntroduction());

        // save the file to the server
        MusicUtils musicUtils = new MusicUtils();
        try {
            String bgmusicFilename = musicUtils.upload(originFrontEnd.getOriginBgmusicFile(), "admin", origin.getOriginName());
            origin.setOriginBgmusicFilename(bgmusicFilename);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        try {
            String voiceFilename = musicUtils.upload(originFrontEnd.getOriginVoiceFile(), "admin", origin.getOriginName());
            origin.setOriginVoiceFilename(voiceFilename);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        ImageUtils imageUtils = new ImageUtils();
        try {
            String prefaceFilename = imageUtils.upload(originFrontEnd.getOriginPrefaceFile(), "admin", origin.getOriginName());
            origin.setOriginPrefaceFilename(prefaceFilename);
        } catch (Exception e) {
            e.printStackTrace();

            return;
        }


        originMapper.insertOrigin(origin.getOriginName(), origin.getOriginAuthor(), origin.getOriginBgmusicFilename(), origin.getOriginVoiceFilename(), origin.getOriginDuration(), origin.getOriginPrefaceFilename(), origin.getOriginIntroduction());
    }
}
