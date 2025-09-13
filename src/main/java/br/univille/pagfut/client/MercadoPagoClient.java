package br.univille.pagfut.client;

import br.univille.pagfut.api.pix.PixPaymentRequest;
import br.univille.pagfut.api.pix.PixPaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "mercadopago", url = "https://api.mercadopago.com")
public interface MercadoPagoClient {
    @PostMapping("v1/payments")
    PixPaymentResponse createPayment(
            @RequestHeader("Authorization") String authorization,
            @RequestBody PixPaymentRequest request);

}
