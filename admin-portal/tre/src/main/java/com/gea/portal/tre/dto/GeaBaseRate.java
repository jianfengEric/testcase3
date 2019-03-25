package com.gea.portal.tre.dto;

import java.io.Serializable;

public class GeaBaseRate implements Serializable {

    private ParticipantEntry participantEntry;

    public ParticipantEntry getParticipantEntry() {
        return participantEntry;
    }

    public void setParticipantEntry(ParticipantEntry participantEntry) {
        this.participantEntry = participantEntry;
    }
}
