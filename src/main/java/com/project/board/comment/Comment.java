package com.project.board.comment;

import com.project.board.post.Post;
import com.project.board.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }

    public void updateComment(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }
}
