package ru.galkin.socialmedia.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "friends")
@Getter
@Setter
@NoArgsConstructor
public class Friend {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "begin_date")
  private LocalDateTime beginDate;

  @ManyToOne
  @JoinColumn(name = "first_user_id")
  private User firstUser;

  @ManyToOne
  @JoinColumn(name = "second_user_id")
  private User secondUser;

  @ManyToOne
  @JoinColumn(name ="query_id")
  private FriendshipQuery friendshipQuery;
}
