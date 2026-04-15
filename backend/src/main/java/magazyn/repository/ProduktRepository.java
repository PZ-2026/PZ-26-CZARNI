package magazyn.repository;

import magazyn.entity.Produkt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduktRepository extends JpaRepository<Produkt, Integer> {
    // Hibernate automatycznie dołączy tabelę stan_magazynu dzięki relacjom w encjach
}