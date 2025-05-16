package com.daaloul.BackEnd.repos;

import com.daaloul.BackEnd.models.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository

public interface ChatRoomRepo extends JpaRepository<ChatRoom, UUID> {
}
