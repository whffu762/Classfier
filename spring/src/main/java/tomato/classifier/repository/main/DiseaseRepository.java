package tomato.classifier.repository.main;

import org.springframework.data.jpa.repository.JpaRepository;
import tomato.classifier.entity.Disease;

public interface DiseaseRepository extends JpaRepository<Disease, String> {
}
