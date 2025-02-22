package com.qualityplus.pets.api;

import com.qualityplus.pets.api.service.PetService;
import com.qualityplus.pets.api.service.UserPetService;
import com.qualityplus.pets.api.skills.DependencyHandler;
import org.bukkit.plugin.Plugin;

public interface ThePetsAPI {
    DependencyHandler getDependencyHandler();
    UserPetService getUsersService();
    PetService getPetsService();
    Plugin getPlugin();
}
