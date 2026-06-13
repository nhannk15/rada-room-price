package com.radar.backend.model.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateListingRequest {

    @NotBlank(message = "URL không được để trống")
    private String url;

    private String imageUrl;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(min = 0, max = 255, message = "Tiêu đề phải từ 0 - 255 kí tự")
    private String title;

    @NotBlank(message = "Quận không được để trống")
    private String district;

    @Positive(message = "Giá cả phải lớn hơn 0")
    private BigDecimal price;

    @Positive(message = "Diện tích phải lớn hơn không")
    private BigDecimal area;
}
