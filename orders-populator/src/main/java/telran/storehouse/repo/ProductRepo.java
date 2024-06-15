package telran.storehouse.repo;

import org.springframework.data.jpa.repository.JpaRepository;


import telran.storehouse.entity.Product;

public interface ProductRepo extends JpaRepository<Product, String> {



}
