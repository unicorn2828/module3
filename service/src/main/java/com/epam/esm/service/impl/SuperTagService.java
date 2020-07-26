package com.epam.esm.service.impl;

import com.epam.esm.dto.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class SuperTagService {

    static List<TagDto> findUserTags(OrdersDto orders) {
        List<TagDto> userTagList = new ArrayList<>();
        orders.getOrders()
              .forEach(o -> o.getCertificates()
                             .forEach(c -> c.getTags()
                                            .stream()
                                            .filter(t -> !userTagList.contains(t))
                                            .forEach(t -> userTagList.add(t))));
        return userTagList;
    }

    static List<TagDto> findSuperTagList(List<TagDto> userTagList, OrdersDto orders) {
        List<TagDto> superTagList = new ArrayList<>();
        BigDecimal maxTagCost = new BigDecimal(0);
        SuperTagDto superTag = new SuperTagDto();
        for (TagDto userTag : userTagList) {
            BigDecimal superTagCost = new BigDecimal(0);
            for (OrderDto order : orders.getOrders()) {
                List<CertificateDto> certificatesDto = order.getCertificates();
                for (CertificateDto certificate : certificatesDto) {
                    List<TagDto> tagsDto = certificate.getTags();
                    for (TagDto currentTag : tagsDto) {
                        if (currentTag.equals(userTag)) {
                            superTagCost = superTagCost.add(order.getOrderPrice());
                        }
                    }
                }
            }
            if (superTagCost.compareTo(maxTagCost) == 0) {
                maxTagCost = superTagCost;
                superTag.setId(userTag.getId());
                superTag.setTagName(userTag.getTagName());
                superTag.setTotalPrice(maxTagCost);
                if (!superTagList.contains(superTag)) {
                    superTagList.add(superTag);
                }
            }
            if (superTagCost.compareTo(maxTagCost) > 0) {
                maxTagCost = superTagCost;
                superTag.setId(userTag.getId());
                superTag.setTagName(userTag.getTagName());
                superTag.setTotalPrice(maxTagCost);
                superTagList.clear();
                superTagList.add(superTag);
            }
        }
        return superTagList;
    }
}
