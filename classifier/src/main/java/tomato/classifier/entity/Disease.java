package tomato.classifier.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
public class Disease {

    @Id
    private String id;

    private String dName;

    private String solution;

    private String src;
}
