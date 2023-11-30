package com.project.board.post;


import com.project.board.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(Long id);

    List<Post> findAllByUserOrderByModifiedAtDesc(User user);

    List<Post> findALLByUser_Username(String username);




}
