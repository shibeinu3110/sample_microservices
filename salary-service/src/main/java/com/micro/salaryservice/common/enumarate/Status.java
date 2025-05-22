package com.micro.salaryservice.common.enumarate;

public enum Status {
    CREATED,
    UPDATED,
    ACCEPTED,
    REJECTED;

    public static boolean isValidToUpdate(String status) {
        return status.equalsIgnoreCase(CREATED.name()) || status.equalsIgnoreCase(UPDATED.name());
    }

    public static boolean isValidStatusForLeader(String status) {
        return status.equalsIgnoreCase(CREATED.name()) || status.equalsIgnoreCase(UPDATED.name());
    }

    public static boolean isValidStatusForLeaderDecision(String status) {
        return status.equalsIgnoreCase(ACCEPTED.name()) || status.equalsIgnoreCase(REJECTED.name());
    }
}


