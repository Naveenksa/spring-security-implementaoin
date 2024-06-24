package com.securityBase.securityBase.repository;

import com.securityBase.securityBase.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product,Integer> {


}
