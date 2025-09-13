package br.univille.pagfut.domain;

import br.univille.pagfut.api.pix.PixPaymentRequest;
import br.univille.pagfut.api.pix.PixPaymentResponse;
import br.univille.pagfut.client.MercadoPagoClient;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class MercadoPagoService {


    @Value("${mercadopago.access.token}")
    private String accessToken;

    private final MercadoPagoClient client;

    public PixPaymentResponse createPixPayment(BigDecimal amount, String description) {
        PixPaymentRequest request = new PixPaymentRequest(amount, description, accessToken);
        return client.createPayment("Bearer " + accessToken, request);
    }
}
