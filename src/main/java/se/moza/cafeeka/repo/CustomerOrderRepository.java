package se.moza.cafeeka.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import se.moza.cafeeka.model.CustomerOrder;

public interface CustomerOrderRepository extends PagingAndSortingRepository<CustomerOrder, Long> {
}
