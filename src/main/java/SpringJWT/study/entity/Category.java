package SpringJWT.study.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CATEGORY")
@Getter @Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // 카테고리 이름

    @ManyToOne
    private Member member; // 이 카테고리에 속한 멤버들
}
