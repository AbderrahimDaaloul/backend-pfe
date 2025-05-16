package com.daaloul.BackEnd.services;


import com.daaloul.BackEnd.models.ESupervisor;
import com.daaloul.BackEnd.models.Student;
import com.daaloul.BackEnd.repos.ESupervisorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class EsService {

    @Autowired
    private ESupervisorRepo eSupervisorRepo;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);


    public List<ESupervisor> findAllESupervisors() { // Renamed for clarity
        return eSupervisorRepo.findAll(); // Return the list of ESupervisor entities
    }

    public Optional<ESupervisor> findESupervisorByID(UUID id) {
        return eSupervisorRepo.findById(id); // Return the ESupervisor entity with the given ID
    }

    public ESupervisor saveESupervisor(ESupervisor eSupervisor) {

        eSupervisor.setPassword(bCryptPasswordEncoder.encode(eSupervisor.getPassword()));

        return eSupervisorRepo.save(eSupervisor); // Save the ESupervisor entity
    }


    public void delete(ESupervisor esupervisor) {
        eSupervisorRepo.delete(esupervisor);
    }
}