package ru.galkin.socialmedia.entity;

import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "header", nullable = false)
  private String header;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "create_date")
  private LocalDateTime created;

  @ManyToOne(fetch =  FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "postId")
  private List<PostImage> images;
}