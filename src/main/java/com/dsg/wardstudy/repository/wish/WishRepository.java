package com.dsg.wardstudy.repository.wish;

import com.dsg.wardstudy.domain.wish.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {

}
