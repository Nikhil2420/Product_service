package com.ProductService.backend.scheduler;

import com.ProductService.backend.constants.ShippingStatus;
import com.ProductService.backend.entity.DeliveryInfo;
import com.ProductService.backend.repository.DeliveryInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductDeliveryStatusUpdateScheduler {

    private final DeliveryInfoRepository deliveryInfoRepository;

    /*
            With Spring Data JPA, findAll() does not return null when there are no records.
            It returns an empty list.
     */
    /*
           scheduler runs every hour.
     */
    @Scheduled(cron = "0 0 * * * *")
    public void updateProductDeliveryStatus() {
        System.out.println("Scheduler is running");
        List<DeliveryInfo> deliveryInfoList = deliveryInfoRepository.findAll();
        deliveryInfoList.forEach(deliveryInfo -> {
            System.out.println(
                    deliveryInfo.getDeliveryInfoId() + " "
                            + deliveryInfo.getNumberOfDays() + " "
                            + deliveryInfo.getShippingStatus());
        });
        deliveryInfoList.forEach(deliveryInfo -> {
            if (deliveryInfo.getShippingStatus() != ShippingStatus.DELIVERED
                    && deliveryInfo.getShippingStatus() != ShippingStatus.FAILED
                    && deliveryInfo.getShippingStatus() != ShippingStatus.CANCELLED) {
                deliveryInfo.setShippingStatus(ShippingStatus.OUT_FOR_DELIVERY);
                deliveryInfoRepository.save(deliveryInfo);
            }
        });
    }
}
