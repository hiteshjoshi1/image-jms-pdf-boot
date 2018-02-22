package com.consensys.hitesh.producer.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.consensys.hitesh.producer.model.ImageRequestDTO;



@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageRepositoryTest {


    @Autowired
    ImageRepository imageRepository;
    
    ImageRequestDTO req1, req2, req3, req4;
    
    
    @Before
    public void setUp() {

//    	imageRepository.deleteAll();

    	req1 = imageRepository.save(new ImageRequestDTO("C:\\Users\\hitjoshi\\ImageFolder","abc.png", "Hitesh", java.util.Calendar.getInstance().getTime()));
    	req2 = imageRepository.save(new ImageRequestDTO("C:\\Users\\hitjoshi\\ImageFolder","cde.png", "Joshi", java.util.Calendar.getInstance().getTime()));
    	req3 = imageRepository.save(new ImageRequestDTO("C:\\Users\\hitjoshi\\ImageFolder","xyz.png", "Hitesh", java.util.Calendar.getInstance().getTime()));
    }



    @Test
    public void findByImageOwner() {

        List<ImageRequestDTO> result = imageRepository.findByImageOwner("Hitesh");

        assertThat(result).extracting("imageOwner").contains("Hitesh");
    }
    
    @After
    public void destory() {
    	imageRepository.delete(req1);
    	imageRepository.delete(req2);
    	imageRepository.delete(req3);
    	
    }
    

}
