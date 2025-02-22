package com.qualityplus.skills.persistance.data;

import com.qualityplus.skills.persistance.data.user.UserSkills;
import com.qualityplus.assistant.lib.eu.okaeri.persistence.document.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data @EqualsAndHashCode(callSuper = true)
public final class UserData extends Document {
    private UUID uuid;
    private String name;
    private UserSkills skills = new UserSkills();

    public void resetData() {
        skills.getXp().clear();
        skills.getLevel().clear();

        skills.fillIfEmpty();
    }
}
