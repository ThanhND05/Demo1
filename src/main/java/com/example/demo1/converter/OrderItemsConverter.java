package com.example.demo1.converter;

import com.example.demo1.entity.OrderItems;
import com.example.demo1.entity.Product;
import com.example.demo1.payload.OrderItemsDTO;
import com.example.demo1.payload.ProductShortDTO;
import com.example.demo1.repository.OrdersRepository;
import com.example.demo1.repository.ProductRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {ProductShortConverter.class})
public abstract class OrderItemsConverter {

    @Autowired
    protected ProductRepository productRepository;
    @Autowired
    protected OrdersRepository orderRepository;

    @Mapping(source = "order.orderId", target = "orderId")
    @Mapping(source = "product", target = "product")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "subTotal", target = "subTotal")
    public abstract OrderItemsDTO toDTO(OrderItems orderItem);


    @Mapping(target = "order", source = "orderId", qualifiedByName = "orderIdToOrder")
    @Mapping(target = "product", source = "product", qualifiedByName = "productShortDTOToProduct")
    @Mapping(target = "price", source="price")
    @Mapping(target = "subTotal", source="subTotal")
    @Mapping(target = "orderItemId", ignore = true)
    public abstract OrderItems toEntity(OrderItemsDTO orderItemsDTO);

    @Named("productShortDTOToProduct")
    public Product productShortDTOToProduct(ProductShortDTO productShortDTO) {
        if (productShortDTO == null || productShortDTO.getProductId() == null) {
            return null;
        }
        return productRepository.findById(productShortDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Product ID: " + productShortDTO.getProductId()));
    }


    @Named("orderIdToOrder")
    public com.example.demo1.entity.Orders orderIdToOrder(Integer orderId) {
        if (orderId == null) {
            return null;
        }
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Order ID: " + orderId));
    }
}