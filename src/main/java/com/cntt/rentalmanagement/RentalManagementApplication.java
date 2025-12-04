package com.cntt.rentalmanagement;

import com.cntt.rentalmanagement.config.AppProperties;
import com.cntt.rentalmanagement.config.FileStorageProperties;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class, AppProperties.class})
public class RentalManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentalManagementApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
