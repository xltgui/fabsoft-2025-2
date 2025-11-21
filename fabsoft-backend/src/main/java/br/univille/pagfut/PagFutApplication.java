package br.univille.pagfut;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = {
    "br.univille.pagfut",
    "br.univille.pagfut.web"
})
public class PagFutApplication {

    public static void main(String[] args) {
        SpringApplication.run(PagFutApplication.class, args);
    }

}
