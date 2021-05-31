package com.ujiuye.job;

import com.ujiuye.utils.util.RedisConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.Set;

/**
 * @author JuBoxin
 * @date 2021/5/21 - 17:47
 */
@Component
public class CleanImagesJob {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Scheduled(cron = "0 09 19 * * ?")
    public void cleanImagesJob() {
        Set<String> differenceSet = redisTemplate.opsForSet().difference(RedisConstant.SETMEAL_PIC_UPLOAD, RedisConstant.SETMEAL_PIC_DB);
        if (differenceSet != null && differenceSet.size() > 0) {
            //条件成立表示有垃圾图片
            for (String img : differenceSet) {
                File rubbishImg = new File("D:/fileupload/img/" + img);
                rubbishImg.delete();
            }
        }
        redisTemplate.delete(RedisConstant.SETMEAL_PIC_UPLOAD);
        redisTemplate.delete(RedisConstant.SETMEAL_PIC_DB);
    }
}
