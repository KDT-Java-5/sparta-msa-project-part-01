package com.sparta.msa.project.domain.category.entity;

import com.sparta.msa.project.global.entity.BaseEntity;
import com.sparta.msa.project.global.exception.DomainException;
import com.sparta.msa.project.global.exception.DomainExceptionCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@Entity
@Table(name = "categories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(nullable = false, length = 50)
  String name;

  @Column(length = 255)
  String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  Category parent;

  @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
  List<Category> children = new ArrayList<>();

  @Builder
  private Category(String name, String description, Category parent) {
    this.name = name;
    this.description = description;
    changeParent(parent);
  }

  public List<Category> getChildren() {
    return Collections.unmodifiableList(children);
  }

  public void updateInfo(String name, String description) {
    this.name = name;
    this.description = description;
  }

  /**
   * 부모 카테고리를 변경한다. (양방향 연관관계 편의 메서드)
   * 새 부모가 자기 자신이거나 자신의 하위 카테고리이면 순환 참조가 발생하므로 거부한다.
   */
  public void changeParent(Category newParent) {
    validateNotCircular(newParent);

    if (this.parent != null) {
      this.parent.removeChild(this);
    }
    this.parent = newParent;
    if (newParent != null) {
      newParent.addChild(this);
    }
  }

  private void validateNotCircular(Category newParent) {
    if (this.id == null) {
      return;
    }
    // 새 부모에서 루트까지 거슬러 올라가며 자기 자신이 나오는지 확인 (프록시 대응을 위해 getter/id로 비교)
    for (Category ancestor = newParent; ancestor != null; ancestor = ancestor.getParent()) {
      if (Objects.equals(ancestor.getId(), this.id)) {
        throw new DomainException(DomainExceptionCode.CATEGORY_CIRCULAR_REFERENCE);
      }
    }
  }

  protected void addChild(Category child) {
    if (!children.contains(child)) {
      children.add(child);
    }
  }

  protected void removeChild(Category child) {
    children.remove(child);
  }
}
