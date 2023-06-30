package tomato.classifier.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Test {

    @Id
    String name;

    String eMail;

    int age;
}
