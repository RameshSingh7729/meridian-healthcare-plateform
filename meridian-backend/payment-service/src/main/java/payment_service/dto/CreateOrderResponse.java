package payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateOrderResponse {

    private String orderId;

    private Integer amount;

    private String currency;

    private String key;
}