package br.univille.pagfut;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PagFutApplication {

    public static void main(String[] args) {
        SpringApplication.run(PagFutApplication.class, args);
    }

}
