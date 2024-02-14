package com.shopwa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass //là một lớp cơ sở cho các Entity khác trong JPA. Nó không tạo ra một bảng trong cơ sở dữ liệu
// như @Entity mà chỉ là một lớp cha cho các Entity khác để tái sử dụng code và định nghĩa các thuộc tính chung.
public abstract class IdBasedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
}
