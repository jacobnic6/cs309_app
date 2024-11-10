package com.cs309.websocket3.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>{

    long countDistinctByUserName(@NonNull String userName);

    @Query("select m from Message m where m.userName = ?1")
    List<Message> findByUserName(@NonNull String userName);
}
