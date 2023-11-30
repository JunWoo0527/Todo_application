package com.project.board.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.board.comment.Comment;
import com.project.board.entity.Timestamped;
import com.project.board.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "post")
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false, length = 20)
    private String title;
    @Column(name = "content", nullable = false, length = 100)
    private String content;
    @Column(name = "complete", nullable = false)
    private Boolean complete = false;

    // 관계설정

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    public Post(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
    }



}
