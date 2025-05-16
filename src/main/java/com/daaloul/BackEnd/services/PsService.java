package com.daaloul.BackEnd.services;


import com.daaloul.BackEnd.models.ESupervisor;
import com.daaloul.BackEnd.models.PSupervisor;
import com.daaloul.BackEnd.repos.PSupervisorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PsService {
    @Autowired
    private PSupervisorRepo pSupervisorRepo;


    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);


    public List<PSupervisor> findAllPSupervisors() { // Renamed for clarity
        return pSupervisorRepo.findAll(); // Return the list of ESupervisor entities
    }

    public Optional<PSupervisor> findPSupervisorByID(UUID id) {
        return pSupervisorRepo.findById(id); // Return the ESupervisor entity with the given ID
    }

    public PSupervisor savePSupervisor(PSupervisor pSupervisor) {

        pSupervisor.setPassword(bCryptPasswordEncoder.encode(pSupervisor.getPassword()));

        return pSupervisorRepo.save(pSupervisor); // Save the ESupervisor entity
    }


    public void delete(PSupervisor psupervisor) {
        pSupervisorRepo.delete(psupervisor);
    }



}
