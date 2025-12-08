package ru.galkin.socialmedia.entity;

import java.time.LocalDateTime;
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
@Table(name = "friendship_queries")
@Getter
@Setter
@NoArgsConstructor
public class FriendshipQuery {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "query_date")
  private LocalDateTime queryDate;

  @ManyToOne
  @JoinColumn(name = "sender_user_id")
  private User senderUser;

  @ManyToOne
  @JoinColumn(name = "receiver_user_id")
  private User receiverUser;

  @ManyToOne
  @JoinColumn(name = "status_id")
  private QueryStatus status;
}
