package com.daaloul.BackEnd.repos;

import com.daaloul.BackEnd.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository

public interface MessageRepo extends JpaRepository<Message, UUID> {
    List<Message> findByChatRoomId(UUID chatRoomId);

}
